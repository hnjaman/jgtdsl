package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.BillingMeteredDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.enums.MeterStatus;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class DisconnectionService {

	/* For METERED Customer */
	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
			.getSession().getAttribute("user");

	public ResponseDTO saveMeterDisconnInfo(DisconnectDTO disconn,
			MeterReadingDTO reading) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		PreparedStatement stmt = null;
		OracleCallableStatement callable_statement = null;
		PreparedStatement coll_stmt = null;
		ResultSet r = null;
		String reading_id = "";
		response = validateDisconnInfo(disconn, reading);
		if (response.isResponse() == false)
			return response;

		int totalDayDiff = Utils.getDateDiffInDays(
				reading.getPrev_reading_date(), reading.getCurr_reading_date());
		totalDayDiff = totalDayDiff + 1;
		float propMinLoad = Utils.getProportionalLoad(reading.getMin_load(),
				totalDayDiff, reading.getBilling_month(),
				reading.getBilling_year());
		float propMaxLoad = Utils.getProportionalLoad(reading.getMax_load(),
				totalDayDiff, reading.getBilling_month(),
				reading.getBilling_year());

		String sqlReading = "Insert into METER_READING(READING_ID, CUSTOMER_ID, METER_ID, TARIFF_ID, RATE, BILLING_MONTH, "
				+ " BILLING_YEAR, READING_PURPOSE, PREV_READING,PREV_READING_DATE, CURR_READING, CURR_READING_DATE, "
				+ " DIFFERENCE, HHV_NHV,MIN_LOAD, MAX_LOAD, ACTUAL_CONSUMPTION,TOTAL_CONSUMPTION, METER_RENT,PRESSURE, PRESSURE_FACTOR,  "
				+ " TEMPERATURE, TEMPERATURE_FACTOR, REMARKS,INSERT_BY,PMIN_LOAD,PMAX_LOAD) "
				+ " Values(?,?,?,?,?,?, "
				+ "?,?,?,to_date(?,'dd-MM-YYYY'),?,to_date(?,'dd-MM-YYYY'), "
				+ "?,?,?,?,?,?,?,?,?,?, " + "?,?,?,?,?)";

		String sqlReadingId = "Select max(Reading_Id) Reading_Id from METER_READING WHERE  Reading_Purpose=? "
				+ "And customer_id=? and meter_id=? ";
		String sqlInsert = " Insert Into DISCONN_METERED(PID,CUSTOMER_ID,METER_ID,DISCONNECT_CAUSE,DISCONNECT_TYPE,DISCONNECT_BY,DISCONNECT_DATE,REMARKS,INSERT_BY,READING_ID) "
				+ " Values(SQN_DISC_ME.nextval,?,?,?,?,?,to_date(?,'dd-MM-YYYY'),?,?,?)";
		String sqlUpdate = " Update CUSTOMER_METER set status=? where customer_id=? and meter_ID=? ";

		try {
			coll_stmt = conn
					.prepareStatement("Select SQN_METER_READING.nextval reading_id from dual");
			r = coll_stmt.executeQuery();

			String select_query = "select * from customer_meter where customer_id= ? AND METER_ID= ? ";

			PreparedStatement unit_type = null;
			ResultSet rs = null;
			String unit = "";
			double total_consumption = 0.0;
			double actual_consumption = 0.0;

			unit_type = conn.prepareStatement(select_query);
			unit_type.setString(1, reading.getCustomer_id());
			unit_type.setString(2, reading.getMeter_id());
			rs = unit_type.executeQuery();
			if (rs.next()) {
				unit = rs.getString("UNIT");
			}

			if (unit.equals("CM")) {
				actual_consumption = reading.getActual_consumption();
				total_consumption = reading.getTotal_consumption();
			} else if (unit.equals("FT")) {
				actual_consumption = round((reading.getActual_consumption() / 35.3147),2);
				total_consumption = round((reading.getTotal_consumption() / 35.3147),2);				
			}

			if (r.next())
				reading_id = r.getString("reading_id");

			stmt = conn.prepareStatement(sqlReading);
			stmt.setString(1, reading_id);
			stmt.setString(2, reading.getCustomer_id());
			stmt.setString(3, reading.getMeter_id());
			stmt.setInt(4, reading.getTariff_id());
			stmt.setDouble(5, reading.getRate());
			stmt.setInt(6, reading.getBilling_month());
			stmt.setInt(7, reading.getBilling_year());
			stmt.setInt(8, ReadingPurpose.DISCONNECT_METER.getId());
			stmt.setDouble(9, reading.getPrev_reading());
			stmt.setString(10, reading.getPrev_reading_date());
			stmt.setDouble(11, reading.getCurr_reading());
			stmt.setString(12, disconn.getDisconnect_date());
			stmt.setDouble(13, reading.getDifference());
			stmt.setDouble(14, reading.getHhv_nhv());
			stmt.setFloat(15, reading.getMin_load());
			stmt.setFloat(16, reading.getMax_load());
//			stmt.setDouble(17, reading.getActual_consumption());
//			stmt.setDouble(18, reading.getTotal_consumption());
			stmt.setDouble(17,actual_consumption );
			stmt.setDouble(18,total_consumption);
			
			double pressurefactd;
			pressurefactd=round( reading.getPressure(),2);
			
			stmt.setDouble(19, reading.getMeter_rent());
			stmt.setDouble(20, reading.getPressure());
			stmt.setDouble(21, pressurefactd);
			stmt.setDouble(22, reading.getTemperature());
			stmt.setDouble(23, reading.getTemperature_factor());
			stmt.setString(24, reading.getRemarks());
			stmt.setString(25, loggedInUser.getUserName());
			stmt.setFloat(26, propMinLoad);
			stmt.setFloat(27, propMaxLoad);

			stmt.execute();

			stmt = conn.prepareStatement(sqlReadingId);
			stmt.setInt(1, ReadingPurpose.GENERAL_BILLING.getId());
			stmt.setString(2, reading.getCustomer_id());
			stmt.setString(3, reading.getMeter_id());

			stmt = conn.prepareStatement(sqlInsert);
			stmt.setString(1, reading.getCustomer_id());
			stmt.setString(2, reading.getMeter_id());
			stmt.setString(3, disconn.getDisconnect_cause_str());
			stmt.setString(4, disconn.getDisconnect_type_str());
			stmt.setString(5, disconn.getDisconnect_by());
			stmt.setString(6, disconn.getDisconnect_date());
			stmt.setString(7, disconn.getRemarks());
			stmt.setString(8, disconn.getInsert_by());
			stmt.setString(9, reading_id);
			stmt.execute();

			stmt = conn.prepareStatement(sqlUpdate);
			stmt.setInt(1, MeterStatus.DISCONNECT.getId());
			stmt.setString(2, reading.getCustomer_id());
			stmt.setString(3, reading.getMeter_id());
			stmt.execute();

			callable_statement = (OracleCallableStatement) conn
					.prepareCall("{ call UpdateMeterCustomerStatus(?,?)  }");
			callable_statement.setString(1, reading.getCustomer_id());
			callable_statement.registerOutParameter(2, java.sql.Types.INTEGER);
			callable_statement.executeUpdate();

			transactionManager.commit();

			response.setMessasge("Successfully Saved Disconnection Information.");
			response.setResponse(true);
			String cKey = "CUSTOMER_INFO_" + reading.getCustomer_id();
			CacheUtil.clear(cKey);

			CustomerDTO customer = ((CustomerDTO) CacheUtil
					.getObjFromCache(cKey));
			CustomerService customerService = new CustomerService();
			if (customer == null)
				customer = customerService.getCustomerInfo(reading
						.getCustomer_id());
			CacheUtil.clearStartWith("ALL_METERED_CONNECTED_CUSTOMER_ID_"
					+ customer.getArea());
			CacheUtil.clearStartWith("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"
					+ customer.getArea());

		}

		catch (Exception e) {
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			e.printStackTrace();
			try {
				transactionManager.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				transactionManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return response;

	}

	public ResponseDTO updateMeterDisconnInfo(DisconnectDTO disconn,
			MeterReadingDTO reading) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		MeterReadingService mrService = new MeterReadingService();

		response = validateDisconnInfo(disconn, reading);
		if (response.isResponse() == false)
			return response;

		String sqlDisconnInfo = " Update DISCONN_METERED "
				+ " Set "
				+ " DISCONNECT_CAUSE=?,DISCONNECT_TYPE=?,DISCONNECT_BY=?,DISCONNECT_DATE=to_date(?,'dd-MM-YYYY'),REMARKS=?,INSERT_BY=?"
				+ " Where PID =?";

		PreparedStatement stmt = null;
		try {
			if (reading.getBill_id() == null
					|| reading.getBill_id().equalsIgnoreCase("")) {
				stmt = mrService.getStatementForReadingUpdate(conn, reading);
				stmt.execute();
			}

			stmt = conn.prepareStatement(sqlDisconnInfo);
			stmt.setString(1, disconn.getDisconnect_cause_str());
			stmt.setString(2, disconn.getDisconnect_type_str());
			stmt.setString(3, disconn.getDisconnect_by());
			stmt.setString(4, disconn.getDisconnect_date());
			stmt.setString(5, disconn.getRemarks());
			stmt.setString(6, disconn.getInsert_by());
			stmt.setString(7, disconn.getPid());
			stmt.execute();

			transactionManager.commit();

			response.setMessasge("Successfully Updated Disconnection Information.");
			response.setResponse(true);
			String cKey = "CUSTOMER_INFO_" + disconn.getCustomer_id();
			CacheUtil.clear(cKey);

		}

		catch (Exception e) {
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			e.printStackTrace();
			try {
				transactionManager.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				transactionManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return response;

	}

	public ResponseDTO validateDisconnInfo(DisconnectDTO disconn,
			MeterReadingDTO reading) {
		ResponseDTO response = new ResponseDTO();
		BillingService bService = new BillingService();
		BillingMeteredDTO bill = bService.getBillStatus(
				reading.getBilling_month(), reading.getBilling_year(),
				disconn.getCustomer_id());

		if (bill == null) {

			if (isValidDisconnDate(reading.getMeter_id(),
					disconn.getDisconnect_date()) == true)
				response.setResponse(true);
			else {
				response.setResponse(false);
				response.setMessasge("Sorry, This is an invalid disconnection date.");
			}

		} else {
			response.setResponse(false);
			response.setMessasge("Sorry, Billing has already done(for disconnection reading month-year) of the disconnnection reading entry.");
		}

		return response;
	}

	public ResponseDTO deleteMeterDisconnInfo(String disconnect_id) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlReading = "Select * from DISCONN_METERED  Where PID=?";
		String sqlDisconnect = "Delete DISCONN_METERED Where PID=?";
		String sqlUpdate = " Update CUSTOMER_METER set status=1 where customer_id=? and meter_ID=?";
		String sqlDeleteReading = "Delete METER_READING Where READING_ID=?";

		PreparedStatement stmt = null;
		OracleCallableStatement callable_statement = null;
		try {
			stmt = conn.prepareStatement(sqlReading);
			stmt.setString(1, disconnect_id);
			ResultSet r = stmt.executeQuery();
			String meter_id = "";
			String customer_id = "";
			String reading_id = "";

			if (r.next()) {
				meter_id = r.getString("METER_ID");
				customer_id = r.getString("CUSTOMER_ID");
				reading_id = r.getString("READING_ID");
			}

			stmt = conn.prepareStatement(sqlDisconnect);
			stmt.setString(1, disconnect_id);
			stmt.execute();

			stmt = conn.prepareStatement(sqlUpdate);
			stmt.setString(1, customer_id);
			stmt.setString(2, meter_id);
			stmt.execute();

			stmt = conn.prepareStatement(sqlDeleteReading);
			stmt.setString(1, reading_id);
			stmt.execute();

			callable_statement = (OracleCallableStatement) conn
					.prepareCall("{ call UpdateMeterCustomerStatus(?,?)  }");
			callable_statement.setString(1, customer_id);
			callable_statement.registerOutParameter(2, java.sql.Types.INTEGER);
			callable_statement.executeUpdate();

			transactionManager.commit();

			response.setMessasge("Successfully Deleted Disconnection Information.");
			response.setResponse(true);

			String cKey = "CUSTOMER_INFO_" + customer_id;
			CacheUtil.clear(cKey);
			CustomerDTO customer = ((CustomerDTO) CacheUtil
					.getObjFromCache(cKey));
			CacheUtil.clearStartWith("ALL_METERED_CONNECTED_CUSTOMER_ID_"
					+ customer.getArea());
			CacheUtil.clearStartWith("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"
					+ customer.getArea());
		}

		catch (Exception e) {
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			e.printStackTrace();
			try {
				transactionManager.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				transactionManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return response;

	}

	public ArrayList<DisconnectDTO> getMeterDisconnectionList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		DisconnectDTO disConn = null;
		ArrayList<DisconnectDTO> disConnList = new ArrayList<DisconnectDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " SELECT PID,DISCONN_METERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,DISCONN_METERED.METER_ID,CUSTOMER_METER.METER_SL_NO,DISCONNECT_CAUSE, "
					+ " DISCONNECT_TYPE,DISCONNECT_BY,TO_CHAR (DISCONNECT_DATE, 'DD-MM-YYYY') DISCONNECT_DATE,"
					+ " DISCONN_METERED.REMARKS,READING_ID,MST_AREA.AREA_ID,MST_AREA.AREA_NAME"
					+ " FROM DISCONN_METERED,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER "
					+ " WHERE DISCONN_METERED.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=DISCONN_METERED.CUSTOMER_ID "
					+ " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")")) + " ";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( SELECT PID,DISCONN_METERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,DISCONN_METERED.METER_ID,CUSTOMER_METER.METER_SL_NO,DISCONNECT_CAUSE, "
					+ " DISCONNECT_TYPE,DISCONNECT_BY,TO_CHAR (DISCONNECT_DATE, 'DD-MM-YYYY') DISCONNECT_DATE,"
					+ " DISCONN_METERED.REMARKS,READING_ID,MST_AREA.AREA_ID,MST_AREA.AREA_NAME"
					+ " FROM DISCONN_METERED,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER "
					+ " WHERE DISCONN_METERED.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=DISCONN_METERED.CUSTOMER_ID "
					+ " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID "
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")")) + " " + orderByQuery
					+ " )tmp1 " + " )tmp2   "
					+ " Where serial Between ? and ? ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (total != 0) {
				stmt.setInt(1, index);
				stmt.setInt(2, index + offset);
			}
			r = stmt.executeQuery();
			while (r.next()) {
				disConn = new DisconnectDTO();
				disConn.setPid(r.getString("PID"));
				disConn.setCustomer_id(r.getString("CUSTOMER_ID"));
				disConn.setCustomer_name(r.getString("FULL_NAME"));
				disConn.setMeter_id(r.getString("METER_ID"));
				disConn.setMeter_sl_no(r.getString("METER_SL_NO"));
				disConn.setDisconnect_cause_str(r.getString("DISCONNECT_CAUSE"));
				disConn.setDisconnect_type_str(r.getString("DISCONNECT_TYPE"));
				disConn.setDisconnect_cause_name(DisconnCause.values()[r
						.getInt("DISCONNECT_CAUSE")].getLabel());
				disConn.setDisconnect_type_name(DisconnType.values()[r
						.getInt("DISCONNECT_TYPE")].getLabel());
				disConn.setDisconnect_date(r.getString("DISCONNECT_DATE"));
				disConn.setRemarks(r.getString("REMARKS"));
				disConnList.add(disConn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return disConnList;
	}

	public DisconnectDTO getMeterDisconnectionInfo(String disconnection_id,
			String meter_id) {
		DisconnectDTO disconn = null;
		MeterReadingDTO reading = new MeterReadingDTO();
		CustomerDTO customer = new CustomerDTO();
		CustomerService customerService = new CustomerService();

		Connection conn = ConnectionManager.getConnection();
		String whereCondition = "";
		if (disconnection_id == null)
			whereCondition = " ( Select max(pid) from DISCONN_METERED Where Meter_Id=? ) ";
		else
			whereCondition = "? ";

		String sql = " Select tmp1.*,brm.BILL_ID from  "
				+ " (Select DIM.PID,DIM.CUSTOMER_ID DISCONN_CUSTOMER_ID,DIM.METER_ID DISCONN_METER_ID,CM.METER_SL_NO, "
				+ " DIM.DISCONNECT_CAUSE,DIM.DISCONNECT_TYPE,DIM.DISCONNECT_BY,to_char(DIM.DISCONNECT_DATE,'dd-MM-YYYY') DISCONNECT_DATE,"
				+ " DIM.REMARKS DISCONN_REMARKS,DIM.READING_ID disconn_reading_id,"
				+ " MR.READING_ID,MR.METER_ID,MR.METER_RENT,MR.BILLING_MONTH, MR.BILLING_YEAR,MR.READING_PURPOSE,"
				+ " MR.PREV_READING,to_char(MR.PREV_READING_DATE,'dd-MM-YYYY') PREV_READING_DATE,MR.CURR_READING,to_char(MR.CURR_READING_DATE,'dd-MM-YYYY') CURR_READING_DATE,"
				+ " MR.DIFFERENCE,MR.HHV_NHV,CM.MEASUREMENT_TYPE,MR.TARIFF_ID,MR.RATE,MR.ACTUAL_CONSUMPTION,MR.TOTAL_CONSUMPTION,"
				+ " MR.MIN_LOAD,MR.MAX_LOAD,MR.REMARKS READING_REMARKS,MR.PRESSURE,MR.PRESSURE_FACTOR,MR.TEMPERATURE,MR.TEMPERATURE_FACTOR"
				+ " From DISCONN_METERED dim,CUSTOMER c,CUSTOMER_PERSONAL_INFO cpi,METER_READING mr,CUSTOMER_METER cm"
				+ " Where PID= "
				+ whereCondition
				+ " And dim.READING_ID=MR.READING_ID And C.CUSTOMER_ID=CPI.CUSTOMER_ID And DIM.CUSTOMER_ID=C.CUSTOMER_ID"
				+ " And DIM.METER_ID=CM.METER_ID)tmp1 Left Outer Join BILLING_READING_MAP brm "
				+ " on tmp1.READING_ID=brm.READING_ID";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);

			if (disconnection_id == null)
				stmt.setString(1, meter_id);
			else
				stmt.setString(1, disconnection_id);

			r = stmt.executeQuery();
			if (r.next()) {
				disconn = new DisconnectDTO();
				disconn.setPid(r.getString("PID"));
				disconn.setCustomer_id(r.getString("DISCONN_CUSTOMER_ID"));
				disconn.setMeter_id(r.getString("DISCONN_METER_ID"));
				disconn.setMeter_sl_no(r.getString("METER_SL_NO"));
				disconn.setDisconnect_cause_name(DisconnCause.values()[r
						.getInt("DISCONNECT_CAUSE")].getLabel());
				disconn.setDisconnect_cause_str(r.getString("DISCONNECT_CAUSE"));
				disconn.setDisconnect_type_name(DisconnType.values()[r
						.getInt("DISCONNECT_TYPE")].getLabel());
				disconn.setDisconnect_type_str(r.getString("DISCONNECT_TYPE"));
				disconn.setDisconnect_by(r.getString("DISCONNECT_BY"));
				disconn.setDisconnect_by_name(EmployeeService
						.getEmpNameFromEmpId(r.getString("DISCONNECT_BY")));
				disconn.setDisconnect_date(r.getString("DISCONNECT_DATE"));
				disconn.setReading_id(r.getString("disconn_reading_id"));
				disconn.setRemarks(r.getString("DISCONN_REMARKS"));

				customer = customerService.getCustomerInfo(r
						.getString("DISCONN_CUSTOMER_ID"));
				disconn.setCustomer(customer);

				reading.setReading_id(r.getString("READING_ID"));
				reading.setMeter_id(r.getString("METER_ID"));
				reading.setMeter_sl_no(r.getString("METER_SL_NO"));
				reading.setMeter_rent(r.getFloat("METER_RENT"));
				reading.setBilling_month(r.getInt("BILLING_MONTH"));
				reading.setBilling_year(r.getInt("BILLING_YEAR"));
				reading.setReading_purpose_str(r.getString("READING_PURPOSE"));
				reading.setPrev_reading(r.getLong("PREV_READING"));
				reading.setPrev_reading_date(r.getString("PREV_READING_DATE"));
				reading.setCurr_reading(r.getLong("CURR_READING"));
				reading.setCurr_reading_date(r.getString("CURR_READING_DATE"));
				reading.setDifference(r.getLong("DIFFERENCE"));
				reading.setHhv_nhv(r.getFloat("HHV_NHV"));
				reading.setMeasurement_type_str(r.getString("MEASUREMENT_TYPE"));
				reading.setMeasurement_type_name(MeterMeasurementType.values()[r
						.getInt("MEASUREMENT_TYPE")].getLabel());
				reading.setTariff_id(r.getInt("TARIFF_ID") == 0 ? null : r
						.getInt("TARIFF_ID"));
				reading.setRate(r.getFloat("RATE") == 0 ? null : r
						.getFloat("RATE"));

				reading.setActual_consumption(r.getFloat("ACTUAL_CONSUMPTION"));
				reading.setTotal_consumption(r.getFloat("TOTAL_CONSUMPTION"));
				reading.setMin_load(r.getFloat("MIN_LOAD"));
				reading.setMax_load(r.getFloat("MAX_LOAD"));
				reading.setRemarks(r.getString("READING_REMARKS"));
				reading.setBill_id(r.getString("BILL_ID"));

				reading.setPressure(r.getFloat("PRESSURE"));
				reading.setPressure_factor(r.getFloat("PRESSURE_FACTOR"));
				reading.setTemperature(r.getFloat("TEMPERATURE"));
				reading.setTemperature_factor(r.getFloat("TEMPERATURE_FACTOR"));

				disconn.setReading(reading);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return disconn;

	}

	public boolean isValidDisconnDate(String meter_id, String disconn_date) {
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select COUNT(METER_ID) TOTAL from CUSTOMER_METER where Meter_Id=? AND to_date(?,'DD-MM-YYYY')>=INSTALLED_DATE";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, meter_id);
			stmt.setString(2, disconn_date);
			r = stmt.executeQuery();
			if (r.next()) {
				if (r.getInt("TOTAL") > 0)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return false;
	}

	/* For Non-METERED Customer */

	public ResponseDTO saveNonMeterDisconnInfo(DisconnectDTO disconn) {
		// Validation Logic need to place here....
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlInsert = " Insert Into DISCONN_NONMETERED(PID,CUSTOMER_ID,DISCONNECT_CAUSE,DISCONNECT_TYPE,DISCONNECT_BY,DISCONNECT_DATE,REMARKS,INSERT_BY) "
				+ " Values(SQN_DISC_NME.nextval,?,?,?,?,to_date(?,'dd-MM-YYYY'),?,?)";
		String sqlUpdate = " Update CUSTOMER_CONNECTION set status="
				+ ConnectionStatus.DISCONNECTED.getId()
				+ " where customer_id=?";

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sqlInsert);
			stmt.setString(1, disconn.getCustomer_id());
			stmt.setString(2, disconn.getDisconnect_cause_str());
			stmt.setString(3, disconn.getDisconnect_type_str());
			stmt.setString(4, disconn.getDisconnect_by());
			stmt.setString(5, disconn.getDisconnect_date());
			stmt.setString(6, disconn.getRemarks());
			stmt.setString(7, disconn.getInsert_by());
			stmt.execute();
			stmt = conn.prepareStatement(sqlUpdate);
			stmt.setString(1, disconn.getCustomer_id());
			stmt.execute();

			transactionManager.commit();

			response.setMessasge("Successfully Saved Disconnection Information.");
			response.setResponse(true);
			String cKey = "CUSTOMER_INFO_" + disconn.getCustomer_id();
			CacheUtil.clear(cKey);

		}

		catch (Exception e) {
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			e.printStackTrace();
			try {
				transactionManager.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				transactionManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return response;
	}

	public ResponseDTO updateNonMeterDisconnInfo(DisconnectDTO disconn) {
		// Validation Logic need to place here....
		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();

		String sqlDisconnInfo = " Update DISCONN_NONMETERED "
				+ " Set "
				+ " DISCONNECT_CAUSE=?,DISCONNECT_TYPE=?,DISCONNECT_BY=?,DISCONNECT_DATE=to_date(?,'dd-MM-YYYY'),REMARKS=?,INSERT_BY=?"
				+ " Where PID =?";

		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(sqlDisconnInfo);
			stmt.setString(1, disconn.getDisconnect_cause_str());
			stmt.setString(2, disconn.getDisconnect_type_str());
			stmt.setString(3, disconn.getDisconnect_by());
			stmt.setString(4, disconn.getDisconnect_date());
			stmt.setString(5, disconn.getRemarks());
			stmt.setString(6, disconn.getInsert_by());
			stmt.setString(7, disconn.getPid());
			stmt.executeUpdate();

			response.setMessasge("Successfully Updated Disconnection Information.");
			response.setResponse(true);

		}

		catch (Exception e) {
			e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return response;

	}

	public DisconnectDTO getNonMeterDisconnectionInfo(String disconnection_id,
			String customer_id) {
		DisconnectDTO disconn = null;
		Connection conn = ConnectionManager.getConnection();

		String sql = "";
		String whereClause = "";

		if (customer_id == null || customer_id.equalsIgnoreCase(""))
			whereClause = "PID=? ";
		else
			whereClause = "PID in (Select max(pid) from DISCONN_NONMETERED Where Customer_ID=? )";

		sql = " Select DINM.PID,DINM.CUSTOMER_ID DISCONN_CUSTOMER_ID, "
				+ " DINM.DISCONNECT_CAUSE,DINM.DISCONNECT_TYPE,DINM.DISCONNECT_BY,to_char(DINM.DISCONNECT_DATE,'dd-MM-YYYY') DISCONNECT_DATE,"
				+ " DINM.REMARKS"
				+ " From DISCONN_NONMETERED dinm,CUSTOMER c,CUSTOMER_PERSONAL_INFO cpi"
				+ " Where "
				+ whereClause
				+ " And  C.CUSTOMER_ID=CPI.CUSTOMER_ID And DINM.CUSTOMER_ID=C.CUSTOMER_ID";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (customer_id == null || customer_id.equalsIgnoreCase(""))
				stmt.setString(1, disconnection_id);
			else
				stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			if (r.next()) {
				disconn = new DisconnectDTO();
				disconn.setPid(r.getString("PID"));
				disconn.setCustomer_id(r.getString("DISCONN_CUSTOMER_ID"));
				disconn.setDisconnect_cause_name(DisconnCause.values()[r
						.getInt("DISCONNECT_CAUSE")].getLabel());
				disconn.setDisconnect_cause_str(r.getString("DISCONNECT_CAUSE"));
				disconn.setDisconnect_type_name(DisconnType.values()[r
						.getInt("DISCONNECT_TYPE")].getLabel());
				disconn.setDisconnect_type_str(r.getString("DISCONNECT_TYPE"));
				disconn.setDisconnect_by(r.getString("DISCONNECT_BY"));
				disconn.setDisconnect_by_name(EmployeeService
						.getEmpNameFromEmpId(r.getString("DISCONNECT_BY")));
				disconn.setDisconnect_date(r.getString("DISCONNECT_DATE"));
				disconn.setRemarks(r.getString("REMARKS"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return disconn;

	}

	public ResponseDTO deleteNonMeterDisconnInfo(String disconnect_id) {
		// Validation Logic need to place here....
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlReading = "Select * from DISCONN_NONMETERED  Where PID=?";
		String sqlDisconnect = "Delete DISCONN_NONMETERED Where PID=?";
		String sqlUpdate = " Update CUSTOMER_CONNECTION set status=1 where customer_id=?";

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sqlReading);
			stmt.setString(1, disconnect_id);
			ResultSet r = stmt.executeQuery();
			String customer_id = "";

			if (r.next()) {
				customer_id = r.getString("CUSTOMER_ID");
			}

			stmt = conn.prepareStatement(sqlReading);
			stmt.setString(1, disconnect_id);
			stmt.execute();

			stmt = conn.prepareStatement(sqlDisconnect);
			stmt.setString(1, disconnect_id);
			stmt.execute();

			stmt = conn.prepareStatement(sqlUpdate);
			stmt.setString(1, customer_id);
			stmt.execute();

			transactionManager.commit();

			response.setMessasge("Successfully Deleted Disconnection Information.");
			response.setResponse(true);
		}

		catch (Exception e) {
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			e.printStackTrace();
			try {
				transactionManager.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				stmt.close();
				transactionManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return response;

	}

	public ArrayList<DisconnectDTO> getNonMeterDisconnectionList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		DisconnectDTO disConn = null;
		ArrayList<DisconnectDTO> disConnList = new ArrayList<DisconnectDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " SELECT PID,DISCONN_NONMETERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,DISCONNECT_CAUSE,  "
					+ " DISCONNECT_TYPE,DISCONNECT_BY,TO_CHAR (DISCONNECT_DATE, 'DD-MM-YYYY') DISCONNECT_DATE,REMARKS,MST_AREA.AREA_ID,MST_AREA.AREA_NAME"
					+ " FROM DISCONN_NONMETERED,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER "
					+ " WHERE  CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=DISCONN_NONMETERED.CUSTOMER_ID "
					+ " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID "
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")")) + " ";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( SELECT PID,DISCONN_NONMETERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,DISCONNECT_CAUSE,  "
					+ " DISCONNECT_TYPE,DISCONNECT_BY,TO_CHAR (DISCONNECT_DATE, 'DD-MM-YYYY') DISCONNECT_DATE,REMARKS,MST_AREA.AREA_ID,MST_AREA.AREA_NAME "
					+ " FROM DISCONN_NONMETERED,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER "
					+ " WHERE  CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=DISCONN_NONMETERED.CUSTOMER_ID "
					+ " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID "
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")")) + " " + orderByQuery
					+ " )tmp1 " + " )tmp2   "
					+ " Where serial Between ? and ? ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (total != 0) {
				stmt.setInt(1, index);
				stmt.setInt(2, index + offset);
			}
			r = stmt.executeQuery();
			while (r.next()) {
				disConn = new DisconnectDTO();
				disConn.setPid(r.getString("PID"));
				disConn.setCustomer_id(r.getString("CUSTOMER_ID"));
				disConn.setCustomer_name(r.getString("FULL_NAME"));
				disConn.setDisconnect_cause_str(r.getString("DISCONNECT_CAUSE"));
				disConn.setDisconnect_type_str(r.getString("DISCONNECT_TYPE"));
				disConn.setDisconnect_cause_name(DisconnCause.values()[r
						.getInt("DISCONNECT_CAUSE")].getLabel());
				disConn.setDisconnect_type_name(DisconnType.values()[r
						.getInt("DISCONNECT_TYPE")].getLabel());
				disConn.setDisconnect_date(r.getString("DISCONNECT_DATE"));
				disConn.setRemarks(r.getString("REMARKS"));
				disConnList.add(disConn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return disConnList;
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}

package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.EVCModelDTO;
import org.jgtdsl.dto.EmployeeDTO;
import org.jgtdsl.dto.MeterGRatingDTO;
import org.jgtdsl.dto.MeterMfgDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.MeterRepairmentDTO;
import org.jgtdsl.dto.MeterTypeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.StatusDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.enums.MeterStatus;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class MeterService {

	// UserDTO loggedInUser=(UserDTO)
	// ServletActionContext.getRequest().getSession().getAttribute("user");
	public ArrayList<MeterTypeDTO> getMeterTypeList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {
		String cKey = "METER_TYPE_"
				+ Utils.constructCacheKey(index, offset, total, whereClause,
						sortFieldName, sortOrder);
		ArrayList<MeterTypeDTO> meterTypeList = CacheUtil.getListFromCache(
				cKey, MeterTypeDTO.class);
		if (meterTypeList != null)
			return meterTypeList;
		else
			meterTypeList = new ArrayList<MeterTypeDTO>();

		MeterTypeDTO meterType = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select * from MST_METER_TYPE  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " ";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select * from MST_METER_TYPE "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery
					+ "    )tmp1 " + "    )tmp2   "
					+ "  Where serial Between ? and ? ";

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
				meterType = new MeterTypeDTO();
				meterType.setType_id(r.getString("TYPE_ID"));
				meterType.setType_name(r.getString("TYPE_NAME"));
				meterType.setDescription(r.getString("DESCRIPTION"));
				meterType.setStatus(r.getInt("STATUS"));
				meterType.setView_order(r.getInt("VIEW_ORDER"));
				meterTypeList.add(meterType);
			}
			CacheUtil.setListToCache(cKey, meterTypeList);
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

		return meterTypeList;
	}

	public ArrayList<MeterTypeDTO> getMeterTypeList() {
		return getMeterTypeList(0, 0, Utils.EMPTY_STRING, Utils.EMPTY_STRING,
				Utils.EMPTY_STRING, 0);
	}

	public String addMeterType(String data) {
		Gson gson = new Gson();
		MeterTypeDTO meterDTO = gson.fromJson(data, MeterTypeDTO.class);
		Connection conn = ConnectionManager.getConnection();
		String sql = " Insert Into MST_METER_TYPE(TYPE_ID, TYPE_NAME, DESCRIPTION, STATUS) Values(?,?,?,?)";
		int response = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, meterDTO.getType_id());
			stmt.setString(2, meterDTO.getType_name());
			stmt.setString(3, meterDTO.getDescription());
			stmt.setInt(4, meterDTO.getStatus());

			response = stmt.executeUpdate();
			CacheUtil.clearStartWith("METER_TYPE_");
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
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

		if (response == 1)
			return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX
					+ AC.MST_METER_TYPE);
		else
			return Utils.getJsonString(AC.STATUS_ERROR,
					AC.MSG_CREATE_ERROR_PREFIX + AC.MST_METER_TYPE);

	}

	public String updateMeterType(String data) {
		Gson gson = new Gson();
		MeterTypeDTO meterDTO = gson.fromJson(data, MeterTypeDTO.class);
		Connection conn = ConnectionManager.getConnection();
		String sql = " Update MST_METER_TYPE Set TYPE_NAME=?,Description=?,Status=? Where TYPE_ID=?";
		int response = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, meterDTO.getType_name());
			stmt.setString(2, meterDTO.getDescription());
			stmt.setInt(3, meterDTO.getStatus());
			stmt.setString(4, meterDTO.getType_id());

			response = stmt.executeUpdate();
			CacheUtil.clearStartWith("METER_TYPE_");
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
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

		if (response == 1)
			return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX
					+ AC.MST_METER_TYPE);
		else
			return Utils.getJsonString(AC.STATUS_ERROR,
					AC.MSG_UPDATE_ERROR_PREFIX + AC.MST_METER_TYPE);

	}

	public String deleteMeterType(String meterTypeId) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		String typeId = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(meterTypeId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());
		}
		typeId = (String) jsonObject.get("id");
		Connection conn = ConnectionManager.getConnection();
		String sql = " Delete MST_METER_TYPE Where TYPE_ID=?";
		int response = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, typeId);
			response = stmt.executeUpdate();
			CacheUtil.clearStartWith("METER_TYPE_");
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
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

		if (response == 1)
			return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX
					+ AC.MST_METER_TYPE);
		else
			return Utils.getJsonString(AC.STATUS_ERROR,
					AC.MSG_DELETE_ERROR_PREFIX + AC.MST_METER_TYPE);

	}

	public ArrayList<StatusDTO> getMeterStatus() {
		StatusDTO status = null;
		ArrayList<StatusDTO> statusList = new ArrayList<StatusDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select * from MST_METER_STATUS";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next()) {
				status = new StatusDTO();
				status.setStatus_id(r.getString("STATUS_ID"));
				status.setStatus_name(r.getString("STATUS_NAME"));
				statusList.add(status);
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

		return statusList;
	}

	public ArrayList<CustomerMeterDTO> getMeterList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {
		String customer_id = whereClause.split("=")[1].replaceAll("'", "");
		return getCustomerMeterList(customer_id.trim(), Utils.EMPTY_STRING,
				Utils.EMPTY_STRING);
	}

	public ArrayList<CustomerApplianceDTO> getApplianceList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		String customer_id = whereClause.split("=")[1].replaceAll("'", "");
		return getCustomerApplianceList(customer_id.trim());
	}

	public ArrayList<CustomerMeterDTO> getCustomerMeterList(String customer_id,
			String meter_id, String where_clause) {
		ArrayList<CustomerMeterDTO> meterList = new ArrayList<CustomerMeterDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		if (meter_id == null || meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
			sql = "Select tmp1.customer_id,meter_id,meter_sl_no,meter_mfg,mfg_name,meter_year,measurement_type, tmp1.unit, "
					+ " evc_sl_no,evc_model,evc_year,meter_type,type_name meter_type_name,g_rating,rating_name g_rating_name,conn_size,max_reading, "
					+

					" ini_reading,pressure,temperature,tmp1.meter_rent,vat_rebate,to_char(installed_date,'DD-MM-YYYY') installed_date,installed_by,tmp1.status,remarks "
					+

					// " ini_reading,pressure,temperature,meter_rent,to_char(installed_date,'DD-MM-YYYY') installed_date,installed_by,tmp1.status,remarks "
					// +

					// " ini_reading,pressure,temperature,tmp1.meter_rent,to_char(installed_date,'DD-MM-YYYY') installed_date,installed_by,tmp1.status,remarks "
					// +

					" from  "
					+ " ( "
					+ " Select * from customer_meter  WHERE customer_meter.customer_id = ? "
					+ where_clause
					+ " )tmp1  "
					+ " Left Outer Join MST_METER_MFG on tmp1.meter_mfg=MST_METER_MFG.MFG_ID "
					+ " Left Outer Join MST_EVC_MODEL on tmp1.evc_model=MST_EVC_MODEL.MODEL_ID "
					+ " Left Outer Join MST_METER_TYPE on tmp1.meter_type=MST_METER_TYPE.TYPE_ID "
					+ " Left Outer Join MST_METER_GRATING on tmp1.g_rating=MST_METER_GRATING.RATING_ID "
					+ " ORDER BY installed_date DESC ";
		else
			sql = " SELECT * "
					+ "        FROM (Select tmp1.customer_id,meter_id,meter_sl_no,meter_mfg,mfg_name,meter_year,measurement_type, tmp1.unit, "
					+ "        evc_sl_no,evc_model,evc_year,meter_type,type_name meter_type_name,g_rating,rating_name g_rating_name,conn_size,max_reading, "
					+ "        ini_reading,pressure,temperature,tmp1.meter_rent,vat_rebate,to_char(installed_date,'DD-MM-YYYY') installed_date,installed_by,tmp1.status,remarks,GETPREVREADINGINFO(meter_id) reading_prev_info  "
					+ "        From  "
					+ "        ( "
					+ "            Select * from customer_meter  WHERE meter_id = ? "
					+ where_clause
					+ " )tmp1  "
					+ "        Left Outer Join MST_METER_MFG on tmp1.meter_mfg=MST_METER_MFG.MFG_ID "
					+ "        Left Outer Join MST_EVC_MODEL on tmp1.evc_model=MST_EVC_MODEL.MODEL_ID "
					+ "        Left Outer Join MST_METER_TYPE on tmp1.meter_type=MST_METER_TYPE.TYPE_ID "
					+ "        Left Outer Join MST_METER_GRATING on tmp1.g_rating=MST_METER_GRATING.RATING_ID "
					+ "        )tmp2 ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		CustomerMeterDTO meter = null;
		MeterReadingDTO reading = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (meter_id == null
					|| meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
				stmt.setString(1, customer_id);
			else
				stmt.setString(1, meter_id);

			r = stmt.executeQuery();
			while (r.next()) {
				meter = new CustomerMeterDTO();
				reading = new MeterReadingDTO();
				meter.setCustomer_id(r.getString("CUSTOMER_ID"));
				meter.setMeter_id(r.getString("METER_ID"));
				meter.setMeter_sl_no(r.getString("METER_SL_NO"));
				meter.setMeter_mfg(r.getString("METER_MFG"));
				meter.setMeter_mfg_name(r.getString("MFG_NAME"));
				meter.setMeter_year(r.getString("METER_YEAR"));
				meter.setMeasurement_type(MeterMeasurementType.values()[r
						.getInt("MEASUREMENT_TYPE")]);
				meter.setMeasurement_type_str(String
						.valueOf(MeterMeasurementType.values()[r
								.getInt("MEASUREMENT_TYPE")].getId()));
				meter.setMeasurement_type_name(MeterMeasurementType.values()[r
						.getInt("MEASUREMENT_TYPE")].getLabel());
				meter.setEvc_sl_no(r.getString("EVC_SL_NO"));
				meter.setEvc_model(r.getString("EVC_MODEL"));
				meter.setEvc_year(r.getString("EVC_YEAR"));
				meter.setMeter_type(r.getString("METER_TYPE"));
				meter.setMeter_type_name(r.getString("METER_TYPE_NAME"));
				meter.setG_rating(r.getString("G_RATING"));
				meter.setG_rating_name(r.getString("G_RATING_NAME"));
				meter.setConn_size(r.getString("CONN_SIZE"));
				meter.setMax_reading(r.getString("MAX_READING"));
				meter.setIni_reading(r.getString("INI_READING"));
				meter.setUnit(r.getString("UNIT"));

				meter.setPressure(r.getString("PRESSURE"));
				meter.setTemperature(r.getString("TEMPERATURE"));
				meter.setMeter_rent(r.getString("METER_RENT"));
				meter.setVat_rebate(r.getDouble("vat_rebate"));
				meter.setInstalled_date(r.getString("INSTALLED_DATE"));
				meter.setInstalled_by(r.getString("INSTALLED_BY"));
				meter.setInstalled_by_str(EmployeeService.getEmpNameFromEmpId(r
						.getString("INSTALLED_BY")));

				meter.setRemarks(r.getString("REMARKS"));
				meter.setStatus(MeterStatus.values()[r.getInt("STATUS")]);
				meter.setStatus_str(String.valueOf(MeterStatus.values()[r
						.getInt("STATUS")].getId()));
				meter.setStatus_name(MeterStatus.values()[r.getInt("STATUS")]
						.getLabel());

				if (!Utils.isNullOrEmpty(meter_id)) {
					String[] pre_reading_array = r.getString(
							"reading_prev_info").split(",");
					reading.setPrev_reading(Long.valueOf(pre_reading_array[0]));
					reading.setPrev_reading_date(pre_reading_array[1]);
					meter.setReading(reading);
				}

				meterList.add(meter);
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

		return meterList;
	}

	public ArrayList<CustomerApplianceDTO> getCustomerApplianceList(
			String customer_id) {
		UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
				.getSession().getAttribute("user");
		ArrayList<CustomerApplianceDTO> applianceList = new ArrayList<CustomerApplianceDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";

		sql = "SELECT BQC.PID, "
				+ "       BQC.APPLIANCE_TYPE_CODE, "
				+ "       AI.APPLIANCE_NAME, AI.APPLIANCE_RATE, "
				+ "       BQC.CUSTOMER_ID, "
				+ "       BQC.OLD_APPLIANCE_QNT, "
				+ "       BQC.OLD_APPLIANCE_QNT_BILLCALL, "
				+ "       BQC.NEW_APPLIANCE_QNT, "
				+ "       BQC.NEW_APPLIANCE_QNT_BILLCAL, "
				+ "       (BQC.NEW_APPLIANCE_QNT_BILLCAL-BQC.NEW_APPLIANCE_QNT)*2 TOTAL_PARTIAL_DISS_QNT, "
				+ "       BQC.TOTAL_PDISCONNECTED_QNT, "
				+ "       BQC.TOTAL_TDISCONNECTED_QNT-((BQC.NEW_APPLIANCE_QNT_BILLCAL-BQC.NEW_APPLIANCE_QNT)*2) TOTAL_TDISCONNECTED_QNT "
				+ "  FROM BURNER_QNT_CHANGE BQC,APPLIANCE_INFO AI "
				+ "  WHERE   BQC.APPLIANCE_TYPE_CODE=AI.APPLIANCE_ID AND AREA_ID=? AND PID IN "
				+ "          (SELECT MAX (PID) "
				+ "             FROM BURNER_QNT_CHANGE "
				+ "            WHERE CUSTOMER_ID =? GROUP BY APPLIANCE_TYPE_CODE) ORDER BY  BQC.APPLIANCE_TYPE_CODE";

		PreparedStatement stmt = null;
		ResultSet r = null;
		CustomerApplianceDTO appliance = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, loggedInUser.getArea_id());
			stmt.setString(2, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {
				appliance = new CustomerApplianceDTO();
				appliance.setApplianc_id(r.getString("APPLIANCE_TYPE_CODE")
						.trim());
				appliance.setApplianc_name(r.getString("APPLIANCE_NAME"));
				appliance.setApplianc_qnt(r.getString("NEW_APPLIANCE_QNT"));
				appliance.setApplianc_rate(r.getString("APPLIANCE_RATE"));
				appliance.setApplianc_qnt_billcal(r
						.getString("NEW_APPLIANCE_QNT_BILLCAL"));
				appliance.setApplianc_perm_diss(r
						.getString("TOTAL_PDISCONNECTED_QNT"));
				appliance.setApplianc_temp_diss(r
						.getString("TOTAL_TDISCONNECTED_QNT"));
				appliance.setApplianc_partial_diss(r
						.getString("TOTAL_PARTIAL_DISS_QNT"));
				appliance.setApplianc_status("1");
				applianceList.add(appliance);

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

		return applianceList;
	}

	public ArrayList<CustomerMeterDTO> getDisconnectedMeterList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		ArrayList<CustomerMeterDTO> meterList = new ArrayList<CustomerMeterDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		sql = " Select CUSTOMER_METER.*,to_char(INSTALLED_DATE,'DD-MM-YYYY') INSTALLED_DATE_FORMATTED from CUSTOMER_METER Where "
				+ whereClause;

		PreparedStatement stmt = null;
		ResultSet r = null;
		CustomerMeterDTO meter = null;

		try {
			stmt = conn.prepareStatement(sql);

			r = stmt.executeQuery();
			while (r.next()) {
				meter = new CustomerMeterDTO();
				meter.setCustomer_id(r.getString("CUSTOMER_ID"));
				meter.setMeter_id(r.getString("METER_ID"));
				meter.setMeter_sl_no(r.getString("METER_SL_NO"));
				meter.setInstalled_date(r.getString("INSTALLED_DATE_FORMATTED"));
				meter.setRemarks(r.getString("REMARKS"));
				meter.setStatus(MeterStatus.values()[r.getInt("STATUS")]);
				meter.setStatus_str(String.valueOf(MeterStatus.values()[r
						.getInt("STATUS")].getId()));
				meter.setStatus_name(MeterStatus.values()[r.getInt("STATUS")]
						.getLabel());

				meterList.add(meter);
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

		return meterList;
	}

	public DisconnectDTO getLatestDisconnectInfo(String customer_id,
			String meter_no) {
		DisconnectDTO disconnInfo = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		if (meter_no == null || meter_no.equalsIgnoreCase(Utils.EMPTY_STRING))
			sql = "SELECT * "
					+ "  FROM history_disconn_nonmetered "
					+ " WHERE customer_id = ? "
					+ "   AND (pid, disconnect_date) IN ( "
					+ "          SELECT pid, disconnect_date "
					+ "            FROM history_disconn_nonmetered "
					+ "           WHERE customer_id = ? "
					+ "             AND disconnect_date IN (SELECT MAX (disconnect_date) "
					+ "                                       FROM history_disconn_nonmetered "
					+ "                                      WHERE customer_id = ?))";

		else
			sql = "SELECT * "
					+ "  FROM history_disconn_metered "
					+ " WHERE customer_id = ?  "
					+ "   AND (pid, disconnect_date) IN ( "
					+ "          SELECT pid, disconnect_date "
					+ "            FROM history_disconn_metered "
					+ "           WHERE customer_id = ? "
					+ "             AND disconnect_date IN (SELECT MAX (disconnect_date) "
					+ "                                       FROM history_disconn_metered "
					+ "                                      WHERE customer_id = ? and meter_no=?)) ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (meter_no == null
					|| meter_no.equalsIgnoreCase(Utils.EMPTY_STRING)) {
				stmt.setString(1, customer_id);
			} else {
				stmt.setString(1, customer_id);
				stmt.setString(2, meter_no);
			}
			r = stmt.executeQuery();
			while (r.next()) {
				disconnInfo = new DisconnectDTO();
				disconnInfo.setPid(r.getString("PID"));
				disconnInfo.setCustomer_id(r.getString("CUSTOMER_ID"));
				disconnInfo.setDisconnect_cause(DisconnCause.values()[r
						.getInt("DISCONNECT_CAUSE")]);
				// disconnInfo.setStr_disconnect_cause(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getLabel());
				disconnInfo.setDisconnect_type(DisconnType.values()[r
						.getInt("DISCONNECT_TYPE")]);
				// disconnInfo.setStr_disconnect_type(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getLabel());
				disconnInfo.setDisconnect_date(r.getString("DISCONNECT_DATE"));
				disconnInfo.setRemarks(r.getString("REMARKS"));
				disconnInfo.setInsert_by(r.getString("INSERT_BY"));
				disconnInfo.setInsert_date(r.getString("INSERT_DATE"));
				if (meter_no != null
						&& !meter_no.equalsIgnoreCase(Utils.EMPTY_STRING)) {
					// disconnInfo.setMeter_no(r.getString("METER_NO"));
					disconnInfo.setMeter_reading(r.getString("METER_READING"));
				}

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

		return disconnInfo;
	}

	public String getNextId(String data) {
		Connection conn = ConnectionManager.getConnection();
		String sql = " select lpad(max(to_number(TYPE_ID))+1,2,'0') ID from MST_METER_TYPE";
		String response = "";
		PreparedStatement stmt = null;
		ResultSet r = null;

		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next()) {
				response = r.getString("ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
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

		return Utils.getJsonString(AC.STATUS_OK, response);

	}

	public ArrayList<EVCModelDTO> getEvcModelList() {
		EVCModelDTO model = null;
		ArrayList<EVCModelDTO> modelList = new ArrayList<EVCModelDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select * from MST_EVC_MODEL";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next()) {
				model = new EVCModelDTO();
				model.setModel_id(r.getString("MODEL_ID"));
				model.setModel_name(r.getString("MODEL_NAME"));
				modelList.add(model);
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

		return modelList;
	}

	public ArrayList<MeterMfgDTO> getMfgList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {
		String cKey = "METER_MFG_"
				+ Utils.constructCacheKey(index, offset, total, whereClause,
						sortFieldName, sortOrder);
		ArrayList<MeterMfgDTO> mfgList = CacheUtil.getListFromCache(cKey,
				MeterMfgDTO.class);
		if (mfgList != null)
			return mfgList;
		else
			mfgList = new ArrayList<MeterMfgDTO>();

		MeterMfgDTO manufacture = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select * from MST_METER_MFG  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " ";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select * from MST_METER_MFG "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery
					+ "    )tmp1 " + "    )tmp2   "
					+ "  Where serial Between ? and ? ";

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
				manufacture = new MeterMfgDTO();
				manufacture.setMfg_id(r.getString("MFG_ID"));
				manufacture.setMfg_name(r.getString("MFG_NAME"));
				manufacture.setView_order(r.getString("VIEW_ORDER"));
				mfgList.add(manufacture);
			}
			CacheUtil.setListToCache(cKey, mfgList);
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

		return mfgList;
	}

	public ArrayList<MeterMfgDTO> getMfgList() {
		return getMfgList(0, 0, Utils.EMPTY_STRING, Utils.EMPTY_STRING,
				Utils.EMPTY_STRING, 0);
	}

	public ArrayList<MeterGRatingDTO> getGRatingList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {
		String cKey = "GRATING_"
				+ Utils.constructCacheKey(index, offset, total, whereClause,
						sortFieldName, sortOrder);
		ArrayList<MeterGRatingDTO> gRatingList = CacheUtil.getListFromCache(
				cKey, MeterGRatingDTO.class);
		if (gRatingList != null)
			return gRatingList;
		else
			gRatingList = new ArrayList<MeterGRatingDTO>();

		MeterGRatingDTO gRating = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select * from MST_METER_GRATING  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery;
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select * from MST_METER_GRATING "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery
					+ "    )tmp1 " + "    )tmp2   "
					+ "  Where serial Between ? and ? ";

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
				gRating = new MeterGRatingDTO();
				gRating.setRating_id(r.getString("RATING_ID"));
				gRating.setRating_name(r.getString("RATING_NAME"));
				gRating.setDescription(r.getString("DESCRIPTION"));
				gRating.setStatus(r.getInt("STATUS"));
				gRating.setView_order(r.getInt("VIEW_ORDER"));
				gRatingList.add(gRating);
			}
			CacheUtil.setListToCache(cKey, gRatingList);
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

		return gRatingList;
	}

	public ArrayList<MeterGRatingDTO> getGRatingList() {
		return getGRatingList(0, 0, Utils.EMPTY_STRING, Utils.EMPTY_STRING,
				Utils.EMPTY_STRING, 0);
	}

	public ArrayList<EVCModelDTO> getEvcModelList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {
		String cKey = "EVC_MODEL_"
				+ Utils.constructCacheKey(index, offset, total, whereClause,
						sortFieldName, sortOrder);
		ArrayList<EVCModelDTO> evcModelLit = CacheUtil.getListFromCache(cKey,
				EVCModelDTO.class);
		if (evcModelLit != null)
			return evcModelLit;
		else
			evcModelLit = new ArrayList<EVCModelDTO>();

		EVCModelDTO model = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select * from MST_EVC_MODEL  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery;
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select * from MST_EVC_MODEL  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery
					+ "    )tmp1 " + "    )tmp2   "
					+ "  Where serial Between ? and ? ";

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
				model = new EVCModelDTO();
				model.setModel_id(r.getString("MODEL_ID"));
				model.setModel_name(r.getString("MODEL_NAME"));
				evcModelLit.add(model);
			}
			CacheUtil.setListToCache(cKey, evcModelLit);
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

		return evcModelLit;
	}

	public static ResponseDTO isMeterBasicInfoChangeValid(String meter_id) {

		return MeterService.isMeterDeleteValid(meter_id); // Because both share
															// common logic

	}

	public static ResponseDTO isMeterDeleteValid(String meter_id) {
		ResponseDTO response = new ResponseDTO();
		response.setDialogCaption("Delete Confrimation");
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select count(READING_ID) TOTAL from METER_READING Where Meter_Id=? and reading_purpose!="
				+ ReadingPurpose.NEW_METER.getId();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, meter_id);
			ResultSet r = stmt.executeQuery();

			if (r.next()) {
				if (r.getInt("TOTAL") > 0) {
					response.setResponse(false);
					response.setMessasge("Meter reading entry exist for selected meter. Please delete meter reading at first.");
				} else {
					response.setResponse(true);
				}
			}
		} catch (Exception e) {
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

	public ArrayList<MeterRepairmentDTO> getRepairmentList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		String cKey = "REPAIRMENT_"
				+ Utils.constructCacheKey(index, offset, total, whereClause,
						sortFieldName, sortOrder);
		ArrayList<MeterRepairmentDTO> repairmentList = CacheUtil
				.getListFromCache(cKey, MeterRepairmentDTO.class);

		if (repairmentList != null)
			return repairmentList;
		else
			repairmentList = new ArrayList<MeterRepairmentDTO>();

		MeterRepairmentDTO repair = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select cp.CUSTOMER_ID,FULL_NAME,PID,repairment.METER_ID,METER_SL_NO,PREV_READING,To_Char(PREV_READING_DATE,'DD-MM-YYYY HH24:MI') PREV_READING_DATE,CURR_READING,To_Char(CURR_READING_DATE,'DD-MM-YYYY HH24:MI') CURR_READING_DATE,REPAIRED_BY,repairment.REMARKS,READING_ID "
					+ "  From METER_REPAIRMENT repairment,CUSTOMER_PERSONAL_INFO cp,CUSTOMER_METER meter Where cp.customer_id=meter.customer_id and meter.meter_id=repairment.meter_id   "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery;
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select cp.CUSTOMER_ID,FULL_NAME,PID,repairment.METER_ID,METER_SL_NO,PREV_READING,To_Char(PREV_READING_DATE,'DD-MM-YYYY HH24:MI') PREV_READING_DATE,CURR_READING,To_Char(CURR_READING_DATE,'DD-MM-YYYY HH24:MI') CURR_READING_DATE,REPAIRED_BY,repairment.REMARKS,READING_ID "
					+ "  From METER_REPAIRMENT repairment,CUSTOMER_PERSONAL_INFO cp,CUSTOMER_METER meter Where cp.customer_id=meter.customer_id and meter.meter_id=repairment.meter_id "
					+ whereClause + " " + orderByQuery + "    )tmp1 "
					+ "    )tmp2   " + "  Where serial Between ? and ? ";

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
				repair = new MeterRepairmentDTO();
				repair.setPid(r.getString("PID"));
				repair.setMeter_id(r.getString("METER_ID"));
				repair.setPrev_reading(r.getString("PREV_READING"));
				repair.setPrev_reading_date(r.getString("PREV_READING_DATE"));
				repair.setCurr_reading(r.getString("CURR_READING"));
				repair.setCurr_reading_date(r.getString("CURR_READING_DATE"));
				repair.setRepaired_by(r.getString("REPAIRED_BY"));
				repair.setRemarks(r.getString("REMARKS"));
				repair.setReading_id(r.getString("READING_ID"));

				repair.setCustomer_id(r.getString("CUSTOMER_ID"));
				repair.setCustomer_name(r.getString("FULL_NAME"));
				repair.setMeter_sl_no(r.getString("METER_SL_NO"));
				repairmentList.add(repair);
			}
			CacheUtil.setListToCache(cKey, repairmentList);
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

		return repairmentList;
	}

	public static MeterRepairmentDTO getRepairInfo(String repair_id) {
		String cKey = "REPAIRMENT_" + repair_id;
		MeterRepairmentDTO repair = (MeterRepairmentDTO) CacheUtil
				.getObjFromCache(cKey);

		if (repair != null)
			return repair;
		else
			repair = new MeterRepairmentDTO();

		Connection conn = ConnectionManager.getConnection();
		String sql = " Select cp.CUSTOMER_ID,FULL_NAME,PID,repairment.METER_ID,METER_SL_NO,PREV_READING,To_Char(PREV_READING_DATE,'DD-MM-YYYY HH24:MI') PREV_READING_DATE,CURR_READING,To_Char(CURR_READING_DATE,'DD-MM-YYYY HH24:MI') CURR_READING_DATE,REPAIRED_BY,repairment.REMARKS,READING_ID,isRepairEditValid(PID) edit_valid "
				+ "  From METER_REPAIRMENT repairment,CUSTOMER_PERSONAL_INFO cp,CUSTOMER_METER meter Where cp.customer_id=meter.customer_id and meter.meter_id=repairment.meter_id "
				+ " and PID=?";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, repair_id);
			r = stmt.executeQuery();
			while (r.next()) {
				repair = new MeterRepairmentDTO();
				repair.setPid(r.getString("PID"));
				repair.setMeter_id(r.getString("METER_ID"));
				repair.setPrev_reading(r.getString("PREV_READING"));
				repair.setPrev_reading_date(r.getString("PREV_READING_DATE"));
				repair.setCurr_reading(r.getString("CURR_READING"));
				repair.setCurr_reading_date(r.getString("CURR_READING_DATE"));
				repair.setRepaired_by(r.getString("REPAIRED_BY"));
				repair.setRemarks(r.getString("REMARKS"));
				repair.setReading_id(r.getString("READING_ID"));

				repair.setCustomer_id(r.getString("CUSTOMER_ID"));
				repair.setCustomer_name(r.getString("FULL_NAME"));
				repair.setMeter_sl_no(r.getString("METER_SL_NO"));

			}
			CacheUtil.setObjToCache(cKey, repair);
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

		return repair;
	}

	public static ResponseDTO saveMeterReplacement(String meter_id,
			CustomerMeterDTO meter) {
		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int resp_code = 0;
		String resp_msg = Utils.EMPTY_STRING;
		try {

			// System.out.println("===>>Procedure : [saveMeterReplacement] START");
			stmt = (OracleCallableStatement) conn
					.prepareCall("{ call saveMeterReplacement(?,?,?,?,?,?,?,?,?,?)  }");
			// System.out.println("==>>Procedure : [saveMeterReplacement] END");

			stmt.setString(1, meter_id);
			stmt.setString(2, meter.getMeter_id());
			stmt.setString(3, meter.getMeter_sl_no());
			stmt.setString(4, meter.getMeter_year());
			stmt.setString(5, meter.getMeasurement_type_str());
			stmt.setString(6, meter.getMeter_type());
			stmt.setString(7, meter.getG_rating());
			stmt.setString(8, meter.getConn_size());
			stmt.setString(9, meter.getMax_reading());
			stmt.setString(10, meter.getIni_reading());
			stmt.setString(11, meter.getPressure());
			stmt.setString(12, meter.getTemperature());
			stmt.setString(13, meter.getMeter_rent());
			stmt.setString(14, meter.getInstalled_by());
			stmt.setString(15, meter.getInstalled_date());
			stmt.setString(16, meter.getRemarks());
			stmt.setString(17, meter.getEvc_sl_no());
			stmt.setString(18, meter.getEvc_year());
			stmt.setString(19, meter.getEvc_model());

			stmt.registerOutParameter(20, java.sql.Types.INTEGER);
			stmt.registerOutParameter(21, java.sql.Types.VARCHAR);
			stmt.executeUpdate();
			resp_code = stmt.getInt(20);
			resp_msg = stmt.getString(21);
			if (resp_code == 1) {
				response.setMessasge("Successfully Saved Meter Repairment Information.");
				response.setResponse(true);
				CacheUtil.clearStartWith("REPAIRMENT");
			} else {
				response.setMessasge(resp_msg);
				response.setResponse(false);
			}
		} catch (Exception e) {
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

}

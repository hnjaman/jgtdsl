package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.BillingMeteredDTO;
import org.jgtdsl.dto.BillingNonMeteredDTO;
import org.jgtdsl.dto.BillingParamDTO;
import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.DuesSurchargeDTO;
import org.jgtdsl.dto.InstallmentDTO;
import org.jgtdsl.dto.MBillDTO;
import org.jgtdsl.dto.MBillGovtMarginDTO;
import org.jgtdsl.dto.MBillGridDTO;
import org.jgtdsl.dto.MBillPbMarginDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.MinistryBillDTO;
import org.jgtdsl.dto.MobBillDTO;
import org.jgtdsl.dto.NMAdjustmentDTO;
import org.jgtdsl.dto.OthersDto;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.BillStatus;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.enums.Month;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class BillingService {
	Connection connection = ConnectionManager.getConnection();
	UserDTO loggedInUser = (UserDTO) ServletActionContext.getRequest()
			.getSession().getAttribute("user");
	ArrayList<MBillDTO> nonMeteredBillInfo = new ArrayList<MBillDTO>();

	public ResponseDTO processBill(BillingParamDTO bill_parameter) {
		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code = 0;

		try {

			if (bill_parameter.getIsMetered_str().equalsIgnoreCase("1")) {
				// System.out.println("===>>Procedure : [GENERATEBILL_METERED] START");
				stmt = (OracleCallableStatement) conn
						.prepareCall("{ call GENERATEBILL_METERED(?,?,?,?,?,?,?,?,?,?,?,?,?)  }");
			} else if (bill_parameter.getIsMetered_str().equalsIgnoreCase("0")) {
				// System.out.println("===>>Procedure : [GENERATEBILL_NONMETERED] START");
				stmt = (OracleCallableStatement) conn
						.prepareCall("{ call GENERATEBILL_NONMETERED(?,?,?,?,?,?,?,?,?,?,?,?,?)  }");
			}
			// System.out.println("==>>Procedure : END");

			stmt.setString(1, bill_parameter.getBill_for());
			stmt.setString(2, bill_parameter.getCustomer_id());
			stmt.setString(3, bill_parameter.getCustomer_category());
			stmt.setString(4, bill_parameter.getArea_id());
			stmt.setString(5, bill_parameter.getBilling_month_str());
			stmt.setString(6, bill_parameter.getBilling_year());
			if (bill_parameter.getIsMetered_str().equalsIgnoreCase("1")) {
				stmt.setString(7, bill_parameter.getIssue_date());
			} else if (bill_parameter.getIsMetered_str().equalsIgnoreCase("0")) {
				stmt.setString(7, bill_parameter.getBill_generation_date());
			}

			stmt.setString(8, bill_parameter.getProcessed_by());
			stmt.setString(9, bill_parameter.getRemarks());
			stmt.setString(10, bill_parameter.getReProcess() == null ? "N"
					: "Y");
			stmt.setString(11, "1"); // Here 1 means regularbill
			stmt.registerOutParameter(12, java.sql.Types.INTEGER);
			stmt.registerOutParameter(13, java.sql.Types.VARCHAR);

			stmt.executeUpdate();
			response_code = stmt.getInt(12);
			response.setMessasge(stmt.getString(13));
			if (response_code == 1) {
				// response.setMessasge("Successfully Saved Billing Information.");
				response.setResponse(true);
			} else {
				// response.setMessasge("Need to show error message here....");
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

	public ArrayList<BurnerQntChangeDTO> getNonMeterEventList(String billId) {
		BurnerQntChangeDTO event = null;
		ArrayList<BurnerQntChangeDTO> eventList = new ArrayList<BurnerQntChangeDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "Select to_char(effective_date,'dd-MM-YYYY') Effective_Date, "
				+ "Old_Double_Burner_Qnt Old_Qnt, "
				+ "New_Permanent_discon_qnt P_Disconn_Qnt,New_Temporary_Disconn_Qnt T_Disconn_Qnt, "
				+ "New_Increased_Qnt Increased_Qnt,(New_Reconn_Qnt_4m_Temporary+NEW_RECONN_QNT_4M_TEMP_HALF)  Reconn_4M_Tmp,New_Reconn_Qnt_4m_Permanent Reconn_4M_Per,New_Double_Burner_Qnt New_Qnt "
				+ " From Burner_Qnt_Change,BILL_NON_METERED "
				+ "Where  Burner_Qnt_Change.CUSTOMER_ID=BILL_NON_METERED.CUSTOMER_ID "
				+ "And BILL_NON_METERED.bill_id=? "
				+ "And to_char(effective_date,'MM') =BILL_NON_METERED.BILL_MONTH and to_char(effective_date,'YYYY')=BILL_NON_METERED.BILL_YEAR "
				+ "Order by effective_date,pid ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, billId);
			r = stmt.executeQuery();
			while (r.next()) {
				event = new BurnerQntChangeDTO();
				event.setOld_double_burner_qnt(r.getString("Old_Qnt"));
				event.setNew_permanent_disconnected_burner_qnt(r
						.getString("P_Disconn_Qnt"));
				event.setNew_temporary_disconnected_burner_qnt(r
						.getString("T_Disconn_Qnt"));
				event.setNew_incrased_burner_qnt(r.getString("Increased_Qnt"));
				event.setNew_reconnected_burner_qnt(r
						.getString("Reconn_4M_Tmp"));
				event.setNew_reconnected_burner_qnt_permanent(r
						.getString("Reconn_4M_Per"));
				event.setNew_double_burner_qnt(r.getString("New_Qnt"));
				event.setEffective_date(r.getString("Effective_Date"));

				eventList.add(event);
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

		return eventList;
	}

	public ResponseDTO getApproxTotalBillingCount(BillingParamDTO bill_parameter) {

		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;

		try {

			System.out
					.println("===>>Procedure : [GET_APPROX_TOTAL_BILL_COUNT] START");
			stmt = (OracleCallableStatement) conn
					.prepareCall("{ call GET_APPROX_TOTAL_BILL_COUNT(?,?,?,?,?,?,?)  }");
			System.out.println("==>>Procedure : END");

			stmt.setString(1, bill_parameter.getBill_for());
			stmt.setString(2, bill_parameter.getArea_id());
			stmt.setString(3, bill_parameter.getCustomer_category());
			stmt.setString(4, bill_parameter.getBilling_month_str());
			stmt.setString(5, bill_parameter.getBilling_year());
			stmt.setString(6, bill_parameter.getIsMetered_str());

			stmt.registerOutParameter(7, java.sql.Types.VARCHAR);

			stmt.executeUpdate();
			response.setMessasge(stmt.getString(7));
			response.setResponse(true);

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

	public ResponseDTO getProcessedTotalBillingCount(
			BillingParamDTO bill_parameter) {

		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;

		try {

			System.out
					.println("===>>Procedure : [GET_APPROX_TOTAL_BILL_COUNT] START");
			stmt = (OracleCallableStatement) conn
					.prepareCall("{ call GET_PROCESSED_TOTAL_BILL_COUNT(?,?,?,?,?,?,?)  }");
			System.out.println("==>>Procedure : END");

			stmt.setString(1, bill_parameter.getBill_for());
			stmt.setString(2, bill_parameter.getArea_id());
			stmt.setString(3, bill_parameter.getCustomer_category());
			stmt.setString(4, bill_parameter.getBilling_month_str());
			stmt.setString(5, bill_parameter.getBilling_year());
			stmt.setString(6, bill_parameter.getIsMetered_str());

			stmt.registerOutParameter(7, java.sql.Types.VARCHAR);

			stmt.executeUpdate();
			response.setMessasge(stmt.getString(7));
			response.setResponse(true);

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

	public ResponseDTO unlockDatabase(String isMeter, String area_id) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String unlockDatabase = "UPDATE BILLING_SEMAPHORE SET STATUS='0' WHERE AREA_ID =? AND ISMETERED=?";

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(unlockDatabase);
			stmt.setString(1, area_id);
			stmt.setString(2, isMeter);
			stmt.execute();
			transactionManager.commit();
			response.setMessasge("Successfully Unlock DataBase");
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

	public ArrayList<MBillDTO> getBill(String bill_id,
			String customer_category, String area_id, String customer_id,
			String bill_month, String bill_year, String download_type) {
		MBillDTO bill = null;
		ArrayList<MBillDTO> billList = new ArrayList<MBillDTO>();
		MBillGovtMarginDTO govtMarginDTO = null;
		MeterReadingDTO readingDTO = null;
		MBillPbMarginDTO pgMarginDTO = null;
		ArrayList<MeterReadingDTO> readingList = new ArrayList<MeterReadingDTO>();
		Connection conn = ConnectionManager.getConnection();
		String where_condition = "";

		// /preview bill
		if (download_type.equalsIgnoreCase("prev") && !(area_id == null))
			where_condition = " And area_id='" + area_id + "' "
					+ " AND bill.status=0 And Bill_Month=" + bill_month
					+ " and Bill_Year=" + bill_year;

		if (download_type.equalsIgnoreCase("prev") && !(area_id == null)
				&& !(customer_category == null))
			where_condition = " And area_id='" + area_id + "' "
					+ "And customer_category='" + customer_category + "'"
					+ " AND bill.status=0 And Bill_Month=" + bill_month
					+ " and Bill_Year=" + bill_year;

		// /preview bill

		if (download_type.equalsIgnoreCase("S")) // Single Bill
			where_condition = " AND bill.bill_id = ? ";
		else if (download_type.equalsIgnoreCase("M")) // Multiple bill [given
														// multiple bill id]
			where_condition = " AND bill.bill_id in ( " + bill_id + " )";

		else if (download_type.equalsIgnoreCase("A")) { // Approved Bills
			where_condition = " AND bill.status=1 And Bill_Month=" + bill_month
					+ " and Bill_Year=" + bill_year;
			if (customer_id != null && !customer_id.equalsIgnoreCase(""))
				where_condition += " And Customer_id='" + customer_id + "'";
			else if (customer_category != null
					&& !customer_category.equalsIgnoreCase(""))
				where_condition += " And area_id='" + area_id
						+ "' And customer_category='" + customer_category + "'";
			else if (area_id != null && !area_id.equalsIgnoreCase(""))
				where_condition += " And area_id='" + area_id + "'";

		}

		else if (download_type.equalsIgnoreCase("O")) // OutSide Bills
			where_condition = " AND  Customer_id='" + customer_id
					+ "' And Bill_Month=" + bill_month + " and Bill_Year="
					+ bill_year;
		else if (download_type.equalsIgnoreCase("NA")) {// Non-Approved Bills
			where_condition = " AND  (bill.status is null or bill.status=0) And Bill_Month="
					+ bill_month + " and Bill_Year=" + bill_year;
			if (customer_id != null && !customer_id.equalsIgnoreCase(""))
				where_condition += " And Customer_id='" + customer_id + "'";
			else if (customer_category != null
					&& !customer_category.equalsIgnoreCase(""))
				where_condition += " And area_id='" + area_id
						+ "' And customer_category='" + customer_category + "'";
			else if (area_id != null && !area_id.equalsIgnoreCase(""))
				where_condition += " And area_id='" + area_id + "'";
		} else if (download_type.equalsIgnoreCase("area")) // preview
			where_condition = " AND   (bill.status is null or bill.status=0) And area_id='"
					+ area_id
					+ "' And Bill_Month="
					+ bill_month
					+ " and Bill_Year=" + bill_year;

		else if (download_type.equalsIgnoreCase("GBCC")) // Group By Customer
															// Category
			where_condition = " AND bill.bill_id = ? AND bill.CUSTOMER_CATEGORY=? and bill.AREA_ID=?";

		String sql = "SELECT brm.READING_ID,  "
				+ "       mr.READING_PURPOSE,  "
				+ "       mr.PREV_READING,  "
				+ "       mr.CURR_READING,  "
				+ "       mr.DIFFERENCE,  "
				+ "       mr.PRESSURE_FACTOR,  "
				+ "       mr.HHV_NHV,  "
				+ "       mr.RATE, "
				+ "       tmp1.*  "
				+ "  FROM BILLING_READING_MAP brm,  "
				+ "       METER_READING mr,  "
				+ "       (SELECT bill.bill_id,  "
				+ "               bill_month,  "
				+ "               bill_year,  "
				+ "               bill.customer_id,  "
				+ "               initcap(customer_name) CUSTOMER_NAME,  "
				+ "               proprietor_name,  "
				+ "               customer_category,  "
				+ "               customer_category_name,  "
				+ "               area_id,  "
				+ "               INITCAP(area_name) area_name,  "
				+ "               address,  "
				+ "               PHONE,MOBILE,  "
				+ "               to_char(issue_date,'dd-MM-YYYY') issue_date,  "
				+ "               to_char(last_pay_date_wo_sc_view,'dd-MM-YYYY') last_pay_date_wo_sc_view ,  "
				+ "               to_char(last_pay_date_w_sc_view,'dd-MM-YYYY') last_pay_date_w_sc_view ,  "
				+ "               to_char(last_pay_date_w_sc_view+1,'dd-MM-YYYY') last_disconn_reconn_date,  "
				+ "               minimum_load,  "
				+ "               other_consumption,  "
				+ "               mixed_consumption,  "
				+ "               billed_consumption,  "
				+ "               payable_amount,  "
				+ "               amount_in_word,  "
				+ "               govt.VAT_AMOUNT govt_total,  "
				+ "               gas_bill,  "
				+ "               min_load_bill,  "
				+ "               bill.meter_rent,  "
				+ "               hhv_nhv_bill,  "
				+ "               adjustment_amount, "
				+ "               ADJUSTMENT_COMMENTS,  "
				+ "               SURCHARGE_PERCENTAGE,  "
				+ "               bill.SURCHARGE_AMOUNT,  "
				+ "               pb.OTHERS_AMOUNT pb_others,   pb.OTHERS_COMMENTS pb_others_comments, "
				+ "		        PB.Gas_Bill+govt.SD_AMOUNT INDIVIDUAL_GAS_BILL, "
				+ "               vat_rebate_percent,  "
				+ "               vat_rebate_amount,  "
				+ "               pb.total_amount pb_total,bill.status ,  to_char(bill.SURCHARGE_ISSUE_DATE,'dd-MM-YYYY') SURCHARGE_ISSUE_DATE,tm.pmin_load "
				+ "          FROM bill_metered bill,  "
				+ "               summary_margin_govt govt,  "
				+ "               summary_margin_pb pb ,(Select customer_id,billing_month,billing_year, sum(NVL(PMIN_LOAD,0)) pmin_load      "
				+ "          From VIEW_METER_READING  "
				+ "          group by customer_id,billing_month,billing_year) tm "
				+ "         WHERE     bill.bill_id = govt.bill_id  "
				+ "               AND bill.bill_id = pb.bill_id  "
				+ "               and BILL.CUSTOMER_ID=tm.CUSTOMER_ID "
				+ "               and BILL.BILL_MONTH=tm.billing_month "
				+ "               and BILL.BILL_YEAR=tm.billing_year "
				+ where_condition + " ) tmp1  "
				+ " WHERE     BRM.BILL_ID = +tmp1.BILL_ID  "
				+ "       AND BRM.READING_ID = +MR.READING_ID  "
				+ " Order by tmp1.CUSTOMER_CATEGORY,tmp1.CUSTOMER_ID ";

		//
		// "SELECT brm.READING_ID, " +
		// "       METER.METER_SL_NO, " +
		// "       mr.READING_PURPOSE, " +
		// "       mr.PREV_READING, " +
		// "       mr.CURR_READING, " +
		// "       mr.DIFFERENCE, " +
		// "       mr.PRESSURE, " +
		// "       mr.TEMPERATURE, " +
		// "       mr.PRESSURE_FACTOR, " +
		// "       mr.TEMPERATURE_FACTOR, " +
		// "       mr.HHV_NHV, " +
		// "       mr.RATE,  mr.ACTUAL_CONSUMPTION, " +
		// "       tmp1.* " +
		// "  FROM BILLING_READING_MAP brm, " +
		// "       METER_READING mr, " +
		// "       CUSTOMER_METER meter, " +
		// "       (SELECT bill.bill_id, " +
		// "               invoice_no, " +
		// "               bill_month, " +
		// "               bill_year, " +
		// "               customer_id, " +
		// "               initcap(customer_name) CUSTOMER_NAME, " +
		// "               proprietor_name, " +
		// "               customer_category, " +
		// "               customer_category_name, " +
		// "               category_type, " +
		// "               area_id, " +
		// "               INITCAP(area_name) area_name, " +
		// "               address, " +
		// "               PHONE,MOBILE, " +
		// "               to_char(issue_date,'dd-MM-YYYY') issue_date, " +
		// "               to_char(last_pay_date_wo_sc_view,'dd-MM-YYYY') last_pay_date_wo_sc_view , "
		// +
		// "               to_char(last_pay_date_w_sc_view,'dd-MM-YYYY') last_pay_date_w_sc_view , "
		// +
		// "               to_char(last_disconn_reconn_date,'dd-MM-YYYY') last_disconn_reconn_date, "
		// +
		// "               monthly_contactual_load, " +
		// "               minimum_load, " +
		// "               actual_gas_consumption, " +
		// "               other_consumption, " +
		// "               mixed_consumption, " +
		// "               hhv_nhv_adj_qnt, " +
		// "               billed_consumption, " +
		// "               payable_amount, " +
		// "               amount_in_word, " +
		// "               prepared_by, " +
		// "               prepared_date, " +
		// "               vat_amount, " +
		// "               sd_amount, " +
		// "               govt.others_amount govt_others, " +
		// "               govt.total_amount govt_total, " +
		// "               gas_bill, " +
		// "               min_load_bill, " +
		// "               meter_rent, " +
		// "               hhv_nhv_bill, " +
		// "               adjustment_amount," +
		// "               ADJUSTMENT_COMMENTS, " +
		// "               SURCHARGE_PERCENTAGE, " +
		// "               SURCHARGE_AMOUNT, " +
		// "               pb.OTHERS_AMOUNT pb_others,   pb.OTHERS_COMMENTS pb_others_comments,"
		// +
		// "               vat_rebate_percent, " +
		// "               vat_rebate_amount, " +
		// "               pb.total_amount pb_total,bill.status ,  to_char(bill.SURCHARGE_ISSUE_DATE,'dd-MM-YYYY') SURCHARGE_ISSUE_DATE"
		// +
		// "          FROM bill_metered bill, " +
		// "               summary_margin_govt govt, " +
		// "               summary_margin_pb pb " +
		// "         WHERE     bill.bill_id = govt.bill_id " +
		// "               AND bill.bill_id = pb.bill_id " +
		// where_condition+
		// "               ) tmp1 " +
		// " WHERE     BRM.BILL_ID = +tmp1.BILL_ID " +
		// "       AND BRM.READING_ID = +MR.READING_ID " +
		// "       AND MR.METER_ID = METER.METER_ID "+
		// " Order by tmp1.CUSTOMER_CATEGORY,tmp1.CUSTOMER_ID";

		PreparedStatement stmt = null;
		ResultSet r = null;
		String previousBillId = "";
		int i = 0;
		try {
			stmt = conn.prepareStatement(sql);

			if (download_type.equalsIgnoreCase("GBCC")) {
				stmt.setString(1, bill_id);
				stmt.setString(2, customer_category);
				stmt.setString(3, area_id);
			} else if (download_type.equalsIgnoreCase("S")) {// //download_type.equalsIgnoreCase("M")
				stmt.setString(1, bill_id);
			}
			r = stmt.executeQuery();
			while (r.next()) {
				if ((!previousBillId.equalsIgnoreCase(r.getString("BILL_ID")) && !previousBillId
						.equals("0")) || i == 0) {

					if (i != 0) {
						bill.setReadingList(readingList);
						billList.add(bill);
						readingList = new ArrayList<MeterReadingDTO>();
					}

					bill = new MBillDTO();
					bill.setBill_id(r.getString("BILL_ID"));
					bill.setBill_month(r.getInt("BILL_MONTH"));
					bill.setBill_month_name(Month.values()[r
							.getInt("BILL_MONTH") - 1].getLabel());
					bill.setBill_year(r.getInt("BILL_Year"));
					bill.setCustomer_id(r.getString("CUSTOMER_ID"));
					bill.setCustomer_name(r.getString("CUSTOMER_NAME"));
					bill.setProprietor_name(r.getString("PROPRIETOR_NAME"));
					bill.setCustomer_category(r.getString("CUSTOMER_CATEGORY"));
					bill.setCustomer_category_name(r
							.getString("CUSTOMER_CATEGORY_NAME"));
					bill.setArea_id(r.getString("AREA_ID"));
					bill.setArea_name(r.getString("AREA_NAME"));
					bill.setAddress(r.getString("ADDRESS"));
					bill.setPhone(r.getString("PHONE"));
					bill.setMobile(r.getString("MOBILE"));
					bill.setIssue_date(r.getString("ISSUE_DATE"));
					bill.setLast_pay_date_wo_sc(r
							.getString("LAST_PAY_DATE_WO_SC_VIEW"));
					bill.setLast_pay_date_w_sc(r
							.getString("SURCHARGE_ISSUE_DATE") == null ? r
							.getString("LAST_PAY_DATE_W_SC_VIEW") : r
							.getString("SURCHARGE_ISSUE_DATE"));
					bill.setLast_disconn_reconn_date(r
							.getString("last_disconn_reconn_date"));
					bill.setMinimum_load(r.getDouble("minimum_load"));
					bill.setOther_consumption(r.getDouble("OTHER_CONSUMPTION"));
					bill.setMixed_consumption(r.getDouble("MIXED_CONSUMPTION"));
					bill.setBilled_consumption(r
							.getDouble("BILLED_CONSUMPTION"));
					bill.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
					bill.setAmount_in_word(r.getString("AMOUNT_IN_WORD"));
					bill.setBill_status(BillStatus.values()[r.getInt("STATUS")]);
					bill.setBill_status_name(BillStatus.values()[r
							.getInt("STATUS")].getLabel());
					bill.setBill_status_str(BillStatus.values()[r
							.getInt("STATUS")].getLabel().toString());

					govtMarginDTO = new MBillGovtMarginDTO();
					pgMarginDTO = new MBillPbMarginDTO();
					govtMarginDTO.setTotal_amount(r.getDouble("GOVT_TOTAL"));

					bill.setGovtMarginDTO(govtMarginDTO);

					pgMarginDTO.setGas_bill(r.getDouble("GAS_BILL"));
					pgMarginDTO.setMin_load_bill(r.getDouble("MIN_LOAD_BILL"));
					pgMarginDTO.setMeter_rent(r.getDouble("METER_RENT"));
					pgMarginDTO.setHhv_nhv_bill(r.getDouble("HHV_NHV_BILL"));
					pgMarginDTO.setAdjustment(r.getDouble("ADJUSTMENT_AMOUNT"));
					pgMarginDTO.setAdjustment_comments(r
							.getString("ADJUSTMENT_COMMENTS"));
					pgMarginDTO.setSurcharge_amount(r
							.getDouble("SURCHARGE_AMOUNT"));
					pgMarginDTO.setOthers(r.getDouble("PB_OTHERS"));
					pgMarginDTO.setOther_comments(r
							.getString("pb_others_comments"));
					pgMarginDTO.setVat_rebate_percent(r
							.getDouble("VAT_REBATE_PERCENT"));
					pgMarginDTO.setVat_rebate_amount(r
							.getDouble("VAT_REBATE_AMOUNT"));
					pgMarginDTO.setTotal_amount(r.getDouble("PB_TOTAL"));

					bill.setPbMarginDTO(pgMarginDTO);

					readingDTO = new MeterReadingDTO();
					readingDTO.setReading_purpose_str(r
							.getString("READING_PURPOSE"));
					readingDTO
							.setReading_purpose_name(ReadingPurpose.values()[r
									.getInt("READING_PURPOSE")].getLabel());
					readingDTO.setCurr_reading(r.getLong("CURR_READING"));
					readingDTO.setPrev_reading(r.getLong("PREV_READING"));
					readingDTO.setDifference(r.getLong("DIFFERENCE"));
					readingDTO
							.setPressure_factor(r.getFloat("PRESSURE_FACTOR"));
					readingDTO.setHhv_nhv(r.getFloat("HHV_NHV"));
					readingDTO.setRate(r.getFloat("RATE"));
					readingDTO.setIndividual_gas_bill(r
							.getDouble("INDIVIDUAL_GAS_BILL"));
					readingList.add(readingDTO);

				} else {
					readingDTO = new MeterReadingDTO();
					readingDTO.setReading_purpose_str(r
							.getString("READING_PURPOSE"));
					readingDTO
							.setReading_purpose_name(ReadingPurpose.values()[r
									.getInt("READING_PURPOSE")].getLabel());
					readingDTO.setCurr_reading(r.getLong("CURR_READING"));
					readingDTO.setPrev_reading(r.getLong("PREV_READING"));
					readingDTO.setDifference(r.getLong("DIFFERENCE"));
					readingDTO
							.setPressure_factor(r.getFloat("PRESSURE_FACTOR"));
					readingDTO.setHhv_nhv(r.getFloat("HHV_NHV"));
					readingDTO.setRate(r.getFloat("RATE"));
					readingDTO.setIndividual_gas_bill(r
							.getDouble("INDIVIDUAL_GAS_BILL"));
					readingList.add(readingDTO);

				}
				i++;
				previousBillId = r.getString("BILL_ID");
			}
			// if(i>0)

			bill.setReadingList(readingList);
			billList.add(bill);
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

		return billList;

	}

	// //prev
	public ArrayList<MBillDTO> getBillprev(String customer_category,
			String area_id, String bill_month, String bill_year,
			String download_type, String report_for) {
		MBillDTO bill = null;
		ArrayList<MBillDTO> billList = new ArrayList<MBillDTO>();
		MBillGovtMarginDTO govtMarginDTO = null;
		MeterReadingDTO readingDTO = null;
		MBillPbMarginDTO pgMarginDTO = null;
		ArrayList<MeterReadingDTO> readingList = new ArrayList<MeterReadingDTO>();
		Connection conn = ConnectionManager.getConnection();
		String where_condition = "";

		if (download_type.equalsIgnoreCase("prev") && !(area_id == null))
			where_condition = " And area_id='" + area_id + "' "
					+ "  And Bill_Month=" + bill_month
					+ " and Bill_Year=" + bill_year;

		if (download_type.equalsIgnoreCase("prev") && !(area_id == null)
				&& !(customer_category == null)
				&& report_for.equalsIgnoreCase("category_wise")) {
			where_condition = " And area_id='" + area_id + "' "
					+ "And customer_category='" + customer_category + "'"
					+ " And Bill_Month=" + bill_month
					+ " and Bill_Year=" + bill_year;
		} else if (download_type.equalsIgnoreCase("prev") && !(area_id == null)
				&& report_for.equalsIgnoreCase("area_wise")) {
			where_condition = " And area_id='" + area_id
					+ "' AND Bill_Month=" + bill_month
					+ " and Bill_Year=" + bill_year;
		}

		String sql = "SELECT brm.READING_ID,  "
				+ "       mr.READING_PURPOSE,  "
				+ "       mr.PREV_READING,  "
				+ "       mr.CURR_READING,  "
				+ "       mr.DIFFERENCE,  "
				+ "       mr.PRESSURE_FACTOR,  "
				+ "       mr.HHV_NHV,  "
				+ "       mr.RATE, "
				+ "       tmp1.*  "
				+ "  FROM BILLING_READING_MAP brm,  "
				+ "       METER_READING mr,  "
				+ "       (SELECT bill.bill_id,  "
				+ "               bill_month,  "
				+ "               bill_year,  "
				+ "               bill.customer_id,  "
				+ "               initcap(customer_name) CUSTOMER_NAME,  "
				+ "               proprietor_name,  "
				+ "               customer_category,  "
				+ "               customer_category_name,  "
				+ "               area_id,  "
				+ "               INITCAP(area_name) area_name,  "
				+ "               address,  "
				+ "               PHONE,MOBILE,  "
				+ "               to_char(issue_date,'dd-MM-YYYY') issue_date,  "
				+ "               to_char(last_pay_date_wo_sc_view,'dd-MM-YYYY') last_pay_date_wo_sc_view ,  "
				+ "               to_char(last_pay_date_w_sc_view,'dd-MM-YYYY') last_pay_date_w_sc_view ,  "
				+ "               to_char(last_pay_date_w_sc_view+1,'dd-MM-YYYY') last_disconn_reconn_date,  "
				+ "               minimum_load,  "
				+ "               other_consumption,  "
				+ "               mixed_consumption,  "
				+ "               billed_consumption,  "
				+ "               payable_amount,  "
				+ "               amount_in_word,  "
				+ "               govt.VAT_AMOUNT govt_total,  "
				+ "               gas_bill,  "
				+ "               min_load_bill,  "
				+ "               bill.meter_rent,  "
				+ "               hhv_nhv_bill,  "
				+ "               adjustment_amount, "
				+ "               ADJUSTMENT_COMMENTS,  "
				+ "               SURCHARGE_PERCENTAGE,  "
				+ "               bill.SURCHARGE_AMOUNT,  "
				+ "               pb.OTHERS_AMOUNT pb_others,   pb.OTHERS_COMMENTS pb_others_comments, "
				+ "		        PB.Gas_Bill+govt.SD_AMOUNT INDIVIDUAL_GAS_BILL, "
				+ "               vat_rebate_percent,  "
				+ "               vat_rebate_amount,  "
				+ "               pb.total_amount pb_total,bill.status ,  to_char(bill.SURCHARGE_ISSUE_DATE,'dd-MM-YYYY') SURCHARGE_ISSUE_DATE,tm.pmin_load "
				+ "          FROM bill_metered bill,  "
				+ "               summary_margin_govt govt,  "
				+ "               summary_margin_pb pb ,(Select customer_id,billing_month,billing_year, sum(NVL(PMIN_LOAD,0)) pmin_load      "
				+ "          From VIEW_METER_READING  "
				+ "          group by customer_id,billing_month,billing_year) tm "
				+ "         WHERE     bill.bill_id = govt.bill_id  "
				+ "               AND bill.bill_id = pb.bill_id  "
				+ "               and BILL.CUSTOMER_ID=tm.CUSTOMER_ID "
				+ "               and BILL.BILL_MONTH=tm.billing_month "
				+ "               and BILL.BILL_YEAR=tm.billing_year "
				+ "				  AND bill.status=0"
				+ where_condition + " ) tmp1  "
				+ " WHERE     BRM.BILL_ID = +tmp1.BILL_ID  "
				+ "       AND BRM.READING_ID = +MR.READING_ID  "
				+ " Order by tmp1.CUSTOMER_CATEGORY,tmp1.CUSTOMER_ID ";

		Statement stmt = null;
		ResultSet r = null;
		String previousBillId = "";
		int i = 0;
		try {
			stmt = conn.createStatement();

			r = stmt.executeQuery(sql);
			while (r.next()) {
				if ((!previousBillId.equalsIgnoreCase(r.getString("BILL_ID")) && !previousBillId
						.equals("0")) || i == 0) {

					if (i != 0) {
						bill.setReadingList(readingList);
						billList.add(bill);
						readingList = new ArrayList<MeterReadingDTO>();
					}

					bill = new MBillDTO();
					bill.setBill_id(r.getString("BILL_ID"));
					bill.setBill_month(r.getInt("BILL_MONTH"));
					bill.setBill_month_name(Month.values()[r
							.getInt("BILL_MONTH") - 1].getLabel());
					bill.setBill_year(r.getInt("BILL_Year"));
					bill.setCustomer_id(r.getString("CUSTOMER_ID"));
					bill.setCustomer_name(r.getString("CUSTOMER_NAME"));
					bill.setProprietor_name(r.getString("PROPRIETOR_NAME"));
					bill.setCustomer_category(r.getString("CUSTOMER_CATEGORY"));
					bill.setCustomer_category_name(r
							.getString("CUSTOMER_CATEGORY_NAME"));
					bill.setArea_id(r.getString("AREA_ID"));
					bill.setArea_name(r.getString("AREA_NAME"));
					bill.setAddress(r.getString("ADDRESS"));
					bill.setPhone(r.getString("PHONE"));
					bill.setMobile(r.getString("MOBILE"));
					bill.setIssue_date(r.getString("ISSUE_DATE"));
					bill.setLast_pay_date_wo_sc(r
							.getString("LAST_PAY_DATE_WO_SC_VIEW"));
					bill.setLast_pay_date_w_sc(r
							.getString("SURCHARGE_ISSUE_DATE") == null ? r
							.getString("LAST_PAY_DATE_W_SC_VIEW") : r
							.getString("SURCHARGE_ISSUE_DATE"));
					bill.setLast_disconn_reconn_date(r
							.getString("last_disconn_reconn_date"));
					bill.setMinimum_load(r.getDouble("minimum_load"));
					bill.setOther_consumption(r.getDouble("OTHER_CONSUMPTION"));
					bill.setMixed_consumption(r.getDouble("MIXED_CONSUMPTION"));
					bill.setBilled_consumption(r
							.getDouble("BILLED_CONSUMPTION"));
					bill.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
					bill.setAmount_in_word(r.getString("AMOUNT_IN_WORD"));
					bill.setBill_status(BillStatus.values()[r.getInt("STATUS")]);
					bill.setBill_status_name(BillStatus.values()[r
							.getInt("STATUS")].getLabel());
					bill.setBill_status_str(BillStatus.values()[r
							.getInt("STATUS")].getLabel().toString());

					govtMarginDTO = new MBillGovtMarginDTO();
					pgMarginDTO = new MBillPbMarginDTO();
					govtMarginDTO.setTotal_amount(r.getDouble("GOVT_TOTAL"));

					bill.setGovtMarginDTO(govtMarginDTO);

					pgMarginDTO.setGas_bill(r.getDouble("GAS_BILL"));
					pgMarginDTO.setMin_load_bill(r.getDouble("MIN_LOAD_BILL"));
					pgMarginDTO.setMeter_rent(r.getDouble("METER_RENT"));
					pgMarginDTO.setHhv_nhv_bill(r.getDouble("HHV_NHV_BILL"));
					pgMarginDTO.setAdjustment(r.getDouble("ADJUSTMENT_AMOUNT"));
					pgMarginDTO.setAdjustment_comments(r
							.getString("ADJUSTMENT_COMMENTS"));
					pgMarginDTO.setSurcharge_amount(r
							.getDouble("SURCHARGE_AMOUNT"));
					pgMarginDTO.setOthers(r.getDouble("PB_OTHERS"));
					pgMarginDTO.setOther_comments(r
							.getString("pb_others_comments"));
					pgMarginDTO.setVat_rebate_percent(r
							.getDouble("VAT_REBATE_PERCENT"));
					pgMarginDTO.setVat_rebate_amount(r
							.getDouble("VAT_REBATE_AMOUNT"));
					pgMarginDTO.setTotal_amount(r.getDouble("PB_TOTAL"));

					bill.setPbMarginDTO(pgMarginDTO);

					readingDTO = new MeterReadingDTO();
					readingDTO.setReading_purpose_str(r
							.getString("READING_PURPOSE"));
					readingDTO
							.setReading_purpose_name(ReadingPurpose.values()[r
									.getInt("READING_PURPOSE")].getLabel());
					readingDTO.setCurr_reading(r.getLong("CURR_READING"));
					readingDTO.setPrev_reading(r.getLong("PREV_READING"));
					readingDTO.setDifference(r.getLong("DIFFERENCE"));
					readingDTO
							.setPressure_factor(r.getFloat("PRESSURE_FACTOR"));
					readingDTO.setHhv_nhv(r.getFloat("HHV_NHV"));
					readingDTO.setRate(r.getFloat("RATE"));
					readingDTO.setIndividual_gas_bill(r
							.getDouble("INDIVIDUAL_GAS_BILL"));
					readingList.add(readingDTO);

				} else {
					readingDTO = new MeterReadingDTO();
					readingDTO.setReading_purpose_str(r
							.getString("READING_PURPOSE"));
					readingDTO
							.setReading_purpose_name(ReadingPurpose.values()[r
									.getInt("READING_PURPOSE")].getLabel());
					readingDTO.setCurr_reading(r.getLong("CURR_READING"));
					readingDTO.setPrev_reading(r.getLong("PREV_READING"));
					readingDTO.setDifference(r.getLong("DIFFERENCE"));
					readingDTO
							.setPressure_factor(r.getFloat("PRESSURE_FACTOR"));
					readingDTO.setHhv_nhv(r.getFloat("HHV_NHV"));
					readingDTO.setRate(r.getFloat("RATE"));
					readingDTO.setIndividual_gas_bill(r
							.getDouble("INDIVIDUAL_GAS_BILL"));
					readingList.add(readingDTO);

				}
				i++;
				previousBillId = r.getString("BILL_ID");
			}
			// if(i>0)

			bill.setReadingList(readingList);
			billList.add(bill);
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

		return billList;

	}

	// /prev

	public ArrayList<MBillGridDTO> getMeteredBilledCustomerList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		MBillGridDTO bill = null;
		ArrayList<MBillGridDTO> billList = new ArrayList<MBillGridDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select * from BILL_METERED  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " ";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select bill.bill_id,customer_id,customer_name,pb_margin.meter_rent,ACTUAL_GAS_CONSUMPTION,payable_amount,status from BILL_METERED bill,SUMMARY_MARGIN_PB pb_margin Where bill.bill_id=pb_margin.bill_id "
					+ (whereClause.equalsIgnoreCase("") ? "" : (" and ( "
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
				bill = new MBillGridDTO();
				bill.setBill_id(r.getString("BILL_ID"));
				bill.setCustomer_id(r.getString("CUSTOMER_ID"));
				bill.setFull_name(r.getString("CUSTOMER_NAME"));
				bill.setActual_consumption(r
						.getDouble("ACTUAL_GAS_CONSUMPTION"));
				bill.setTotal_bill_amount(r.getDouble("PAYABLE_AMOUNT"));
				if (total != 0)
					bill.setMeter_rent(r.getDouble("METER_RENT"));
				if (r.getInt("STATUS") == 0)
					bill.setStatus("Waiting for Approval");
				else if (r.getInt("STATUS") == 1)
					bill.setStatus("Approved");
				else if (r.getInt("STATUS") == 2)
					bill.setStatus("Collected");

				billList.add(bill);
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

		return billList;
	}

	// ////////////////ministry///////////
	public ArrayList<MinistryBillDTO> getMinistryBilledCustomerList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) throws SQLException {

		MinistryBillDTO mbilldto = null;
		ArrayList<MinistryBillDTO> dueBillList = new ArrayList<MinistryBillDTO>();
		String orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder
				+ " ";

		String[] condition = whereClause.split("(?=AND)");

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		sql = "SELECT CC.CUSTOMER_ID, "
				+ "       CPI.FULL_NAME, "
				+ "       CC.ISMETERED, "
				+ "       BI.BILL_ID, "
				+ "       BI.AMOUNT, "
				+ "       CC.MINISTRY_ID, "
				+ "       MM.MINISTRY_NAME "
				+ "  FROM CUSTOMER_CONNECTION CC, "
				+ "       MST_MINISTRY MM, "
				+ "       CUSTOMER_PERSONAL_INFO CPI, "
				+ "       (SELECT CUSTOMER_ID, BILL_ID, PAYABLE_AMOUNT AMOUNT "
				+ "          FROM BILL_METERED BM "
				+ "         WHERE     BM.STATUS = 1 AND "
				+ condition[0]
				+ condition[1]
				+ condition[2]
				+ "        UNION "
				+ "        SELECT CUSTOMER_ID, BILL_ID, ACTUAL_PAYABLE_AMOUNT AMOUNT "
				+ "          FROM BILL_NON_METERED BNM "
				+ "         WHERE     BNM.STATUS = 1 AND " + condition[0]
				+ condition[1] + condition[2] +

				" 			) BI " + " WHERE     CC.MINISTRY_ID = MM.MINISTRY_ID "
				+ "       AND CC.CUSTOMER_ID = CPI.CUSTOMER_ID "
				+ "       AND CC.CUSTOMER_ID = BI.CUSTOMER_ID " + condition[3]
				+ orderByQuery;

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();

			while (r.next()) {
				mbilldto = new MinistryBillDTO();
				mbilldto.setCustomer_id(r.getString("CUSTOMER_ID"));
				mbilldto.setfull_name(r.getString("FULL_NAME"));
				mbilldto.setBill_id(r.getString("BILL_ID"));
				mbilldto.setMinistry_name(r.getString("MINISTRY_NAME"));
				mbilldto.setIs_metered(r.getInt("ISMETERED"));
				mbilldto.setBill_amount(r.getDouble("AMOUNT"));
				dueBillList.add(mbilldto);

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

		return dueBillList;
	}

	// ///////////////////ministry/////////////
	public ArrayList<MBillGridDTO> getNonMeteredBilledCustomerList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		MBillGridDTO bill = null;
		ArrayList<MBillGridDTO> billList = new ArrayList<MBillGridDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";

		if (sortFieldName.equalsIgnoreCase("non_collected_payable_amount")) {
			sortFieldName = "actual_payable_amount-nvl(collected_payable_amount,0)";
		} else if (sortFieldName.equalsIgnoreCase("full_name")) {
			sortFieldName = "customer_name";
		}

		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select * from BILL_NON_METERED  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " ";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select bill_id,customer_id,customer_name, "
					+ "  ACTUAL_PAYABLE_AMOUNT,COLLECTED_PAYABLE_AMOUNT,"
					+ "  status From "
					+ "    BILL_NON_METERED Where "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("  ( "
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
				bill = new MBillGridDTO();
				bill.setBill_id(r.getString("BILL_ID"));
				bill.setCustomer_id(r.getString("CUSTOMER_ID"));
				bill.setFull_name(r.getString("CUSTOMER_NAME"));

				bill.setActual_payable_amount(r
						.getDouble("actual_payable_amount"));
				bill.setCollected_payable_amount(r
						.getDouble("collected_payable_amount"));
				bill.setNon_collected_payable_amount(r
						.getDouble("actual_payable_amount")
						- r.getDouble("collected_payable_amount"));

				if (r.getInt("STATUS") == 0)
					bill.setStatus("Waiting for Approval");
				else if (r.getInt("STATUS") == 1)
					bill.setStatus("Approved");
				else if (r.getInt("STATUS") == 2)
					bill.setStatus("Collected");

				billList.add(bill);
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

		return billList;

	}

	public ArrayList<InstallmentDTO> getInstallmentBillList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		InstallmentDTO installment = null;
		ArrayList<InstallmentDTO> billList = new ArrayList<InstallmentDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = "SELECT customer.Customer_Id, "
					+ "       Full_Name, "
					+ "       Installment_Id, "
					+ "       Bill_Month, "
					+ "       Bill_Year, "
					+ "       Principal, "
					+ "       Surcharge, "
					+ "       Meter_Rent, "
					+ "       Total, "
					+ "       BILL_INSTALLMENT_MST.Status "
					+ "  FROM BILL_INSTALLMENT_MST, "
					+ "       MVIEW_CUSTOMER_INFO customer, "
					+ "       BILL_INSTALLMENT "
					+ " WHERE     BILL_INSTALLMENT.AGREEMENT_ID = BILL_INSTALLMENT_MST.AGREEMENT_ID "
					+ "       AND BILL_INSTALLMENT.CUSTOMER_ID = customer.CUSTOMER_ID "
					+ "       AND BILL_INSTALLMENT.CUSTOMER_ID = customer.CUSTOMER_ID "
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")")) + " ";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( SELECT customer.Customer_Id, "
					+ "       Full_Name, "
					+ "       Installment_Id, "
					+ "       Bill_Month, "
					+ "       Bill_Year, "
					+ "       Principal, "
					+ "       Surcharge, "
					+ "       Meter_Rent, "
					+ "       Total, "
					+ "       BILL_INSTALLMENT_MST.Status "
					+ "  FROM BILL_INSTALLMENT_MST, "
					+ "       MVIEW_CUSTOMER_INFO customer, "
					+ "       BILL_INSTALLMENT "
					+ " WHERE     BILL_INSTALLMENT.AGREEMENT_ID = BILL_INSTALLMENT_MST.AGREEMENT_ID "
					+ "       AND BILL_INSTALLMENT.CUSTOMER_ID = customer.CUSTOMER_ID "
					+ "       AND BILL_INSTALLMENT.CUSTOMER_ID = customer.CUSTOMER_ID  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("  And ( "
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
				installment = new InstallmentDTO();
				installment.setInstallmentId(r.getString("INSTALLMENT_ID"));
				installment.setCustomerId(r.getString("CUSTOMER_ID"));
				installment.setCustomerName(r.getString("Full_Name"));

				installment.setPrincipal(r.getDouble("PRINCIPAL"));
				installment.setSurcharge(r.getDouble("SURCHARGE"));
				installment.setMeterRent(r.getDouble("METER_RENT"));
				installment.setTotal(r.getDouble("TOTAL"));
				installment.setStatus(r.getInt("STATUS"));

				if (r.getInt("STATUS") == 0)
					installment.setStatusName("Created");
				else if (r.getInt("STATUS") == 1)
					installment.setStatusName("Collected");

				billList.add(installment);
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

		return billList;
	}

	// Get Due Bill List. Used by Security Adjustment
	public ArrayList<DuesSurchargeDTO> getOnlyDueBillList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		DuesSurchargeDTO dues = null;
		ArrayList<DuesSurchargeDTO> dueBillList = new ArrayList<DuesSurchargeDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String customerId = whereClause.substring(whereClause.indexOf("=") + 1,
				whereClause.length());
		System.out.println(customerId);

		sql = " Select tmp1.*,1 bill_Type From "
				+ " (select * from table ( getBillInfoForInstallment ("
				+ customerId
				+ ",'','dues_bills')))tmp1  "
				+ "Union  "
				+ " Select Installment_Id,Bill_Month,Bill_Year,Total,0,Total,2  "
				+ " From BILL_INSTALLMENT_MST Where Agreement_Id in (Select Agreement_Id From BILL_INSTALLMENT Where Customer_Id="
				+ customerId + " and Status=0) and Status=0 ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next()) {
				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setBill_month(r.getString("BILL_MONTH"));
				dues.setBill_month_name(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
				dues.setCollected_amount(r.getDouble("COLLECTED_AMOUNT"));
				dues.setRemaining_amount(r.getDouble("REMAINING_AMOUNT"));
				dues.setBill_type(r.getString("BILL_TYPE"));
				dueBillList.add(dues);

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

		return dueBillList;
	}

	// Get Due Bill List. Used By Installment Billing Interface for showing the
	// dues bills of a customer
	public ArrayList<DuesSurchargeDTO> getDueBillList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {
		DuesSurchargeDTO dues = null;
		ArrayList<DuesSurchargeDTO> dueBillList = new ArrayList<DuesSurchargeDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String customerId = whereClause.substring(whereClause.indexOf("=") + 1,
				whereClause.length());
		System.out.println(customerId);

		if (total == 0)
			sql = " select * from table ( getBillInfoForInstallment ("
					+ customerId + ",'','dues_bills'))";
		else
			sql = " select * from table ( getBillInfoForInstallment ("
					+ customerId + ",'','dues_bills'))";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next()) {
				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setBill_month(r.getString("BILL_MONTH"));
				dues.setBill_month_name(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
				dues.setCollected_amount(r.getDouble("COLLECTED_AMOUNT"));
				dues.setRemaining_amount(r.getDouble("REMAINING_AMOUNT"));

				dueBillList.add(dues);

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

		return dueBillList;
	}

	// Get Installment Bill List. Used By Installment Billing Interface for
	// showing the bill for a selected installment agreement.
	public ArrayList<DuesSurchargeDTO> getInstallmentBillList(String agreementId) {
		DuesSurchargeDTO dues = null;
		ArrayList<DuesSurchargeDTO> dueBillList = new ArrayList<DuesSurchargeDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = " select * from table ( getBillInfoForInstallment ('',"
				+ agreementId + ",'installment_bills'))";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next()) {
				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setBill_month(r.getString("BILL_MONTH"));
				dues.setBill_month_name(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
				dues.setCollected_amount(r.getDouble("COLLECTED_AMOUNT"));
				dues.setRemaining_amount(r.getDouble("REMAINING_AMOUNT"));

				dueBillList.add(dues);

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

		return dueBillList;
	}

	public ArrayList<DuesSurchargeDTO> getMeteredCustomerDueBillList(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		DuesSurchargeDTO dues = null;
		ArrayList<DuesSurchargeDTO> dueBillList = new ArrayList<DuesSurchargeDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select BILL_METERED.*,SUMMARY_MARGIN_PB.SURCHARGE_AMOUNT SURCHARGE_AMOUNT from BILL_METERED,SUMMARY_MARGIN_PB  Where BILL_METERED.bill_id=SUMMARY_MARGIN_PB.bill_id   "
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")"))
					+ " And sysdate>LAST_PAY_DATE_WO_SC";
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select BILL_METERED.bill_id, bill_year,bill_month,BILLED_AMOUNT,SUMMARY_MARGIN_PB.SURCHARGE_AMOUNT SURCHARGE_AMOUNT,to_char(LAST_PAY_DATE_WO_SC,'dd-mm-yyyy') LAST_PAY_DATE_WO_SC from BILL_METERED,SUMMARY_MARGIN_PB  Where BILL_METERED.bill_id=SUMMARY_MARGIN_PB.bill_id "
					+ (whereClause.equalsIgnoreCase("") ? ""
							: (" And ( " + whereClause + ") And sysdate>LAST_PAY_DATE_WO_SC"))
					+ " " + orderByQuery + "    )tmp1 " + "    )tmp2   "
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
				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setBill_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_month_value(r.getInt("BILL_MONTH"));
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setBilled_amount(r.getDouble("BILLED_AMOUNT"));
				dues.setDue_date(r.getString("LAST_PAY_DATE_WO_SC"));
				dueBillList.add(dues);
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

		return dueBillList;
	}

	public ArrayList<BillingNonMeteredDTO> getNonMeteredCustomerDueBillList(
			String customer_id, String collection_date, String r_coll_from,
			String r_coll_to, String c_coll_from, String c_coll_to,
			String a_coll_from, String a_coll_to) {
		BillingNonMeteredDTO dueBill = null;
		ArrayList<BillingNonMeteredDTO> billList = new ArrayList<BillingNonMeteredDTO>();
		TariffService ts = new TariffService();
		String Advance_bill_amount = ts.getTariffRateForDomestic(customer_id);

		Connection conn = ConnectionManager.getConnection();
		String sql_to_be = " Select bill_id,bill_month,bill_year,BILLED_AMOUNT,COLLECTED_BILLED_AMOUNT,(calcualteSurcharge (bill_id, '"
				+ collection_date
				+ "')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"
				+ collection_date
				+ "')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,"
				+ "(calcualteSurcharge(bill_id,'"
				+ collection_date
				+ "')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL,COLLECTED_SURCHARGE,COLLECTED_PAYABLE_AMOUNT "
				+ " From BILL_NON_METERED Where Customer_Id=? AND bill_year||LPAD(bill_month, 2, '0') Between ? And ?  And Status=1 Order by Bill_Year,Bill_Month";
		String bill_id_query = "Select SQN_BILL_NONMETERED.NextVal BILL_ID  From Dual";
		String sql_yet_to_paid = " Select bill_id,bill_month,bill_year,BILLED_AMOUNT,COLLECTED_BILLED_AMOUNT,(calcualteSurcharge (bill_id, '"
				+ collection_date
				+ "')) surcharge_per_coll,(calcualteSurcharge(bill_id,'"
				+ collection_date
				+ "')+NVL(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE_CAL,"
				+ "(calcualteSurcharge(bill_id,'"
				+ collection_date
				+ "')+NVL(ACTUAL_SURCHARGE,0)+NVL(BILLED_AMOUNT,0)) ACTUAL_PAYABLE_AMOUNT_CAL,COLLECTED_SURCHARGE,COLLECTED_PAYABLE_AMOUNT "
				+ " From BILL_NON_METERED Where Customer_Id=? AND bill_year||LPAD(bill_month, 2, '0') NOT Between ? And ?  And Status=1 Order by Bill_Year,Bill_Month";

		PreparedStatement bill_id_stmt = null;
		PreparedStatement stmt = null;
		ResultSet r = null;

		int bill_id = 0;
		try {
			stmt = conn.prepareStatement(sql_to_be);
			stmt.setString(1, customer_id);
			stmt.setString(2, r_coll_from);
			stmt.setString(3, r_coll_to);
			r = stmt.executeQuery();
			while (r.next()) {
				dueBill = new BillingNonMeteredDTO();
				dueBill.setBill_id(r.getString("BILL_ID"));
				dueBill.setStr_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dueBill.setYear(r.getInt("BILL_YEAR"));
				dueBill.setActual_billed_amount(r.getFloat("BILLED_AMOUNT"));
				dueBill.setCollected_billed_amount(r
						.getFloat("COLLECTED_BILLED_AMOUNT"));
				dueBill.setActual_surcharge_amount(r
						.getFloat("ACTUAL_SURCHARGE_CAL"));
				dueBill.setSurcharge_per_collection(r
						.getFloat("ACTUAL_SURCHARGE_CAL"));
				dueBill.setCollected_surcharge_amount(r
						.getFloat("COLLECTED_SURCHARGE"));
				dueBill.setActual_payable_amount(r
						.getFloat("ACTUAL_PAYABLE_AMOUNT_CAL"));
				dueBill.setCollected_payable_amount(r
						.getFloat("COLLECTED_PAYABLE_AMOUNT"));
				dueBill.setBill_type("R"); // R=Regular bill
				dueBill.setPaid_status("GTBP");// GTBP=Going to be paid
				billList.add(dueBill);
			}

			if (Integer.valueOf(c_coll_from) != 0) {
				bill_id_stmt = conn.prepareStatement(bill_id_query);

				r = bill_id_stmt.executeQuery();
				if (r.next())
					bill_id = Integer.valueOf(r.getString("BILL_ID"));
				int total_current = Utils.getNumberOfMonth(c_coll_from,
						c_coll_to);
				int curr_adv_month = Integer.valueOf(c_coll_from
						.substring(4, 6));
				int curr_adv_year = Integer
						.valueOf(c_coll_from.substring(0, 4));
				for (int i = 1; i <= total_current; i++) {
					dueBill = new BillingNonMeteredDTO();
					dueBill.setBill_id(String.valueOf(bill_id));
					dueBill.setStr_month(Month.values()[curr_adv_month - 1]
							.getLabel());
					dueBill.setYear(curr_adv_year);
					dueBill.setActual_billed_amount(Integer
							.valueOf(Advance_bill_amount));
					dueBill.setCollected_billed_amount(0);
					dueBill.setActual_surcharge_amount(0);
					dueBill.setSurcharge_per_collection(0);
					dueBill.setCollected_surcharge_amount(0);
					dueBill.setActual_payable_amount(0);
					dueBill.setCollected_payable_amount(0);
					dueBill.setBill_type("A"); // A=Advance bill
					dueBill.setPaid_status("GTBP");// GTBP=Going to be paid
					billList.add(dueBill);
					if (curr_adv_month == 12) {
						curr_adv_month = 0;
						curr_adv_year = curr_adv_year + 1;
					}
					curr_adv_month = curr_adv_month + 1;
					bill_id = bill_id + 1;
				}
			}

			if (Integer.valueOf(a_coll_from) != 0) {
				bill_id_stmt = conn.prepareStatement(bill_id_query);

				r = bill_id_stmt.executeQuery();
				if (r.next())
					bill_id = Integer.valueOf(r.getString("BILL_ID"));
				int total_advance = Utils.getNumberOfMonth(a_coll_from,
						a_coll_to);
				int curr_adv_month = Integer.valueOf(a_coll_from
						.substring(4, 6));
				int curr_adv_year = Integer
						.valueOf(a_coll_from.substring(0, 4));
				for (int i = 1; i <= total_advance; i++) {
					dueBill = new BillingNonMeteredDTO();
					dueBill.setBill_id(String.valueOf(bill_id));
					dueBill.setStr_month(Month.values()[curr_adv_month - 1]
							.getLabel());
					dueBill.setYear(curr_adv_year);
					dueBill.setActual_billed_amount(Integer
							.valueOf(Advance_bill_amount));
					dueBill.setCollected_billed_amount(0);
					dueBill.setActual_surcharge_amount(0);
					dueBill.setSurcharge_per_collection(0);
					dueBill.setCollected_surcharge_amount(0);
					dueBill.setActual_payable_amount(0);
					dueBill.setCollected_payable_amount(0);
					dueBill.setBill_type("A"); // A=Advance bill
					dueBill.setPaid_status("GTBP");// GTBP=Going to be paid
					billList.add(dueBill);
					if (curr_adv_month == 12) {
						curr_adv_month = 0;
						curr_adv_year = curr_adv_year + 1;
					}
					curr_adv_month = curr_adv_month + 1;
					bill_id = bill_id + 1;
				}
			}

			stmt = conn.prepareStatement(sql_yet_to_paid);
			stmt.setString(1, customer_id);
			stmt.setString(2, r_coll_from);
			stmt.setString(3, r_coll_to);
			r = stmt.executeQuery();
			while (r.next()) {
				dueBill = new BillingNonMeteredDTO();
				dueBill.setBill_id(r.getString("BILL_ID"));
				dueBill.setStr_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dueBill.setYear(r.getInt("BILL_YEAR"));
				dueBill.setActual_billed_amount(r.getFloat("BILLED_AMOUNT"));
				dueBill.setCollected_billed_amount(r
						.getFloat("COLLECTED_BILLED_AMOUNT"));
				dueBill.setActual_surcharge_amount(r
						.getFloat("ACTUAL_SURCHARGE_CAL"));
				dueBill.setSurcharge_per_collection(r
						.getFloat("ACTUAL_SURCHARGE_CAL"));
				dueBill.setCollected_surcharge_amount(r
						.getFloat("COLLECTED_SURCHARGE"));
				dueBill.setActual_payable_amount(r
						.getFloat("ACTUAL_PAYABLE_AMOUNT_CAL"));
				dueBill.setCollected_payable_amount(r
						.getFloat("COLLECTED_PAYABLE_AMOUNT"));
				dueBill.setBill_type("R"); // R=Regular bill
				dueBill.setPaid_status("YTBP");// YTBP=Yet to be paid
				billList.add(dueBill);
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

		return billList;
	}

	public ArrayList<BillingNonMeteredDTO> getDuesBill(String customer_id)// Step
																			// 5
																			// account
																			// ledger
	{
		BillingNonMeteredDTO dueBill = null;
		ArrayList<BillingNonMeteredDTO> billList = new ArrayList<BillingNonMeteredDTO>();
		CustomerService customerService = new CustomerService();
		String tableName = "";
		String dueListSql = "";
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			dueListSql = "SELECT customer_id, "
					+ "       BILL_ID, "
					+ "       BILL_MONTH, "
					+ "       BILL_YEAR, "
					+ "       BILLED_AMOUNT PAYABLE_AMOUNT, "
					+ "       0 PAID_AMOUNT, "
					+ "       BILLED_AMOUNT DUE_AMOUNT "
					+ "  FROM BILL_METERED "
					+ " WHERE status = 1 AND customer_id =? order by bill_year asc,bill_month asc";
		else
			dueListSql = "SELECT customer_id, "
					+ "       BILL_ID, "
					+ "       BILL_MONTH, "
					+ "       BILL_YEAR, "
					+ "       BILLED_AMOUNT PAYABLE_AMOUNT, "
					+ "       COLLECTED_BILLED_AMOUNT PAID_AMOUNT, "
					+ "       BILLED_AMOUNT - nvl(COLLECTED_BILLED_AMOUNT,0) DUE_AMOUNT "
					+ "  FROM BILL_NON_METERED "
					+ "         WHERE customer_id =? AND STATUS = 1 order by bill_year asc,bill_month asc";

		Connection conn = ConnectionManager.getConnection();

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(dueListSql);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {
				dueBill = new BillingNonMeteredDTO();
				dueBill.setBill_id(r.getString("BILL_ID"));
				dueBill.setStr_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dueBill.setYear(r.getInt("BILL_YEAR"));
				dueBill.setActual_payable_amount(r.getFloat("PAYABLE_AMOUNT"));
				dueBill.setPaid_amount(r.getFloat("PAID_AMOUNT"));
				dueBill.setDue_amount(r.getFloat("DUE_AMOUNT"));
				billList.add(dueBill);

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

		return billList;
	}

	public String getDuesBillListByString(String customer_id)// Step 5 account
																// ledger
	{
		BillingNonMeteredDTO dueBill = null;
		CustomerService customerService = new CustomerService();
		String tableName;
		String sqlDueListByString = "";
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getStatus_str().equalsIgnoreCase("2")) {
			return "";
		}
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			sqlDueListByString = "SELECT customer_id, "
					+ "       BILL_ID, "
					+ "       BILL_MONTH, "
					+ "       BILL_YEAR, TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR,"
					+ "       BILLED_AMOUNT, "
					+ "       0 PAID_AMOUNT, "
					+ "       BILLED_AMOUNT DUE_AMOUNT "
					+ "  FROM BILL_METERED "
					+ " WHERE status = 1 AND customer_id =? order by bill_year asc,bill_month asc ";
		else
			sqlDueListByString = "SELECT customer_id, "
					+ "       BILL_ID, "
					+ "       BILL_MONTH, "
					+ "       BILL_YEAR,TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR,"
					+ "       BILLED_AMOUNT PAYABLE_AMOUNT, "
					+ "       COLLECTED_BILLED_AMOUNT PAID_AMOUNT, "
					+ "       BILLED_AMOUNT - nvl(COLLECTED_BILLED_AMOUNT,0) DUE_AMOUNT "
					+ "  FROM BILL_NON_METERED "
					+ "         WHERE customer_id =? AND STATUS = 1 order by bill_year asc,bill_month asc";

		Connection conn = ConnectionManager.getConnection();
		StringBuffer duesListString = new StringBuffer("");
		// / query need to be changed with respect to meter or nonmeter customer
		// and exexcute the

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sqlDueListByString);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {
				duesListString.append(r.getString("MONTH_YEAR"));
				duesListString.append(",  ");

			}
			if (duesListString.length() > 0)
				duesListString.deleteCharAt(duesListString.length() - 3);
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

		return duesListString.toString();
	}

	public String isDefaulterOrNot(String customer_id) {
		BillingNonMeteredDTO dueBill = null;
		ArrayList<BillingNonMeteredDTO> billList = new ArrayList<BillingNonMeteredDTO>();
		CustomerService customerService = new CustomerService();
		String tableName = "";
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getStatus_str().equalsIgnoreCase("2"))
			return "green";
		else if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Non-Metered"))
			tableName = "BILL_NON_METERED";
		else if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			tableName = "BILL_METERED";

		Connection conn = ConnectionManager.getConnection();
		String sql = "select count(*) COUNT from " + tableName
				+ " where status=01 and customer_id=?";
		int numberOfdues = 0;
		String isDefauler_color = "";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer_id);

			r = stmt.executeQuery();
			while (r.next()) {
				numberOfdues = r.getInt("COUNT");
			}

			if (numberOfdues > 0) {
				isDefauler_color = "'red'";
			} else {
				isDefauler_color = "'green'";
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

		return isDefauler_color;
	}

	public ArrayList<DuesSurchargeDTO> getBillList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {

		// Not a good solution. But in future we need to refactor it.
		CustomerService customerService = new CustomerService();
		String tableName = "";
		int sIndex = whereClause.indexOf("'");
		String customer_id = whereClause.substring(sIndex + 1, sIndex + 10);
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			tableName = "VIEW_METER_BILLINFO";
		else
			tableName = "VIEW_NON_METER_BILLINFO";

		// End of bad solution

		DuesSurchargeDTO dues = null;
		ArrayList<DuesSurchargeDTO> dueBillList = new ArrayList<DuesSurchargeDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select BILL_ID,INVOICE_NO,CUSTOMER_ID,BILL_MONTH,BILL_YEAR,BILLED_AMOUNT,ADJUSTMENT_AMOUNT,SURCHARGE_AMOUNT,VAT_REBATE_AMOUNT,PAYABLE_AMOUNT,TAX_AMOUNT,PAYABLE_AMOUNT NET_PAYABLE_AMOUNT,COLLECTED_AMOUNT from "
					+ tableName + " Where    " + whereClause;
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select BILL_ID,INVOICE_NO,CUSTOMER_ID,BILL_MONTH,BILL_YEAR,BILLED_AMOUNT,ADJUSTMENT_AMOUNT,SURCHARGE_AMOUNT,VAT_REBATE_AMOUNT,PAYABLE_AMOUNT,TAX_AMOUNT,PAYABLE_AMOUNT NET_PAYABLE_AMOUNT,COLLECTED_AMOUNT from "
					+ tableName + "  Where " + whereClause + " " + orderByQuery
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

				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setInvoice_no(r.getString("INVOICE_NO"));
				dues.setCustomer_id(r.getString("CUSTOMER_ID"));
				dues.setBill_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setBilled_amount(r.getDouble("BILLED_AMOUNT"));
				dues.setAdjustment_amount(r.getDouble("ADJUSTMENT_AMOUNT"));
				dues.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
				dues.setVat_rebate_amount(r.getDouble("VAT_REBATE_AMOUNT"));
				dues.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
				dues.setTax_amount(r.getDouble("TAX_AMOUNT"));
				dues.setNet_payable_amount(r.getDouble("NET_PAYABLE_AMOUNT"));
				dues.setCollection_amount(r.getDouble("COLLECTED_AMOUNT"));
				dueBillList.add(dues);
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

		return dueBillList;
	}

	public ArrayList<DuesSurchargeDTO> getBillListNonMeterSalesAdjustment(
			int index, int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {

		// Not a good solution. But in future we need to refactor it.
		CustomerService customerService = new CustomerService();
		String tableName = "";
		int sIndex = whereClause.indexOf("'");
		String customer_id = whereClause.substring(sIndex + 1, sIndex + 10);
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			tableName = "VIEW_METER_BILLINFO";
		else
			tableName = "VIEW_NON_METER_BILLINFO";

		// End of bad solution

		DuesSurchargeDTO dues = null;
		ArrayList<DuesSurchargeDTO> dueBillList = new ArrayList<DuesSurchargeDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";

		if (total == 0)
			sql = "select CUSTOMER_ID,bnm.BILL_ID,BILL_MONTH,BILL_YEAR,BILLED_AMOUNT,NVL(SALES_AMOUNT,0) ADJUSTMENT_SALES_AMOUNT,NVL(SALES_CONSUMPTION,0) ADJUSTMENT_SALES_CONSUMPTION,ACTUAL_PAYABLE_AMOUNT,TOTAL_CONSUMPTION "
					+ "from BILL_NON_METERED bnm,NON_METER_ADJUSTMENT nma "
					+ "where bnm.bill_id=nma.bill_id(+) "
					+ "and "
					+ whereClause;
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( select CUSTOMER_ID,bnm.BILL_ID,BILL_MONTH,BILL_YEAR,BILLED_AMOUNT,NVL(SALES_AMOUNT,0) ADJUSTMENT_SALES_AMOUNT,NVL(SALES_CONSUMPTION,0) ADJUSTMENT_SALES_CONSUMPTION,ACTUAL_PAYABLE_AMOUNT,TOTAL_CONSUMPTION "
					+ "from BILL_NON_METERED bnm,NON_METER_ADJUSTMENT nma "
					+ "where bnm.bill_id=nma.bill_id(+) " + "and "
					+ whereClause + ")tmp1 " + "    )tmp2   "
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
				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setCustomer_id(r.getString("CUSTOMER_ID"));
				dues.setBill_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setBilled_amount(r.getDouble("BILLED_AMOUNT"));
				dues.setAdjustment_amount(r
						.getDouble("ADJUSTMENT_SALES_AMOUNT"));
				dues.setAdjustment_consumption(r
						.getDouble("ADJUSTMENT_SALES_CONSUMPTION"));
				dues.setNet_payable_amount(r.getDouble("ACTUAL_PAYABLE_AMOUNT"));
				dues.setTotal_consumption(r.getDouble("TOTAL_CONSUMPTION"));
				dueBillList.add(dues);
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

		return dueBillList;
	}

	public DuesSurchargeDTO getBillInfoForAdjustment(String bill_id,
			String customer_id) {
		DuesSurchargeDTO dues = null;

		CustomerService customerService = new CustomerService();
		String tableName = "";
		Connection conn = ConnectionManager.getConnection();
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			tableName = "VIEW_METER_BILLINFO";
		else
			tableName = "VIEW_NON_METER_BILLINFO";

		String sql = " Select BILL_ID,CUSTOMER_ID,INVOICE_NO,BILL_MONTH,BILL_YEAR,BILLED_AMOUNT,ADJUSTMENT_AMOUNT,ADJUSTMENT_COMMENTS,SURCHARGE_AMOUNT,VAT_REBATE_AMOUNT,PAYABLE_AMOUNT,TAX_AMOUNT,PAYABLE_AMOUNT NET_PAYABLE_AMOUNT,COLLECTED_AMOUNT from "
				+ tableName
				+ " Where    Bill_Id="
				+ bill_id
				+ " And Customer_Id='" + customer_id + "'";
		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			if (r.next()) {
				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setInvoice_no(r.getString("INVOICE_NO"));
				dues.setCustomer_id(r.getString("CUSTOMER_ID"));
				dues.setBill_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setBilled_amount(r.getDouble("BILLED_AMOUNT"));
				dues.setAdjustment_amount(r.getDouble("ADJUSTMENT_AMOUNT"));
				dues.setAdjustment_comment(r.getString("ADJUSTMENT_COMMENTS"));
				dues.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
				dues.setVat_rebate_amount(r.getDouble("VAT_REBATE_AMOUNT"));
				dues.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
				dues.setTax_amount(r.getDouble("TAX_AMOUNT"));
				dues.setNet_payable_amount(r.getDouble("NET_PAYABLE_AMOUNT"));
				dues.setCollection_amount(r.getDouble("COLLECTED_AMOUNT"));

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

		return dues;
	}

	public DuesSurchargeDTO getBillInfoForNonMeterSalesAdjustment(
			String bill_id, String customer_id) {
		DuesSurchargeDTO dues = null;

		CustomerService customerService = new CustomerService();
		String tableName = "";
		Connection conn = ConnectionManager.getConnection();
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			tableName = "VIEW_METER_BILLINFO";
		else
			tableName = "VIEW_NON_METER_BILLINFO";

		String sql = "select CUSTOMER_ID,bnm.BILL_ID,BILL_MONTH,BILL_YEAR,BILLED_AMOUNT,NVL(SALES_AMOUNT,0) ADJUSTMENT_SALES_AMOUNT,NVL(SALES_CONSUMPTION,0) ADJUSTMENT_SALES_CONSUMPTION,ACTUAL_PAYABLE_AMOUNT,TOTAL_CONSUMPTION "
				+ "from BILL_NON_METERED bnm,NON_METER_ADJUSTMENT nma "
				+ " where bnm.bill_id=nma.bill_id(+) "
				+ " and bnm.bill_id="
				+ bill_id + " and customer_id='" + customer_id + "'";
		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			if (r.next()) {
				dues = new DuesSurchargeDTO();
				dues.setBill_id(r.getString("BILL_ID"));
				dues.setCustomer_id(r.getString("CUSTOMER_ID"));
				dues.setBill_month(Month.values()[r.getInt("BILL_MONTH") - 1]
						.getLabel());
				dues.setBill_year(r.getString("BILL_YEAR"));
				dues.setBilled_amount(r.getDouble("BILLED_AMOUNT"));
				dues.setAdjustment_amount(r
						.getDouble("ADJUSTMENT_SALES_AMOUNT"));
				dues.setAdjustment_consumption(r
						.getDouble("ADJUSTMENT_SALES_CONSUMPTION"));
				dues.setNet_payable_amount(r.getDouble("ACTUAL_PAYABLE_AMOUNT"));
				dues.setTotal_consumption(r.getDouble("TOTAL_CONSUMPTION"));

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

		return dues;
	}

	public CollectionDTO getBillInfo(String bill_month, String bill_year,
			String customer_id) {
		CollectionDTO billInfo = null;

		CustomerService customerService = new CustomerService();
		String tableName = "";
		Connection conn = ConnectionManager.getConnection();
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			tableName = "VIEW_METER_BILLINFO";
		else
			tableName = "VIEW_NON_METER_BILLINFO";

		String sql = " Select PAYABLE_AMOUNT,COLLECTED_AMOUNT,STATUS from "
				+ tableName + " Where    Customer_Id='" + customer_id
				+ "' And Bill_Year=" + bill_year + " And Bill_Month="
				+ bill_month;

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			if (r.next()) {
				billInfo = new CollectionDTO();
				billInfo.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
				billInfo.setCollected_amount(r.getDouble("COLLECTED_AMOUNT"));
				billInfo.setStatusId(r.getString("STATUS"));
				billInfo.setBill_month_name(Month.values()[Integer
						.valueOf(bill_month) - 1].getLabel());

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

		return billInfo;
	}

	public CollectionDTO getBillInfoWeb(String customer_id) {
		CollectionDTO billInfo = null;

		CustomerService customerService = new CustomerService();
		String tableName = "";
		Connection conn = ConnectionManager.getConnection();
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered"))
			tableName = "VIEW_METER_BILLINFO";
		else
			tableName = "VIEW_NON_METER_BILLINFO";

		String sql = " Select PAYABLE_AMOUNT,COLLECTED_AMOUNT,STATUS from "
				+ tableName + " Where    Customer_Id='" + customer_id
				+ "' And rownum<2 ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			if (r.next()) {
				billInfo = new CollectionDTO();
				billInfo.setPayable_amount(r.getDouble("PAYABLE_AMOUNT"));
				billInfo.setCollected_amount(r.getDouble("COLLECTED_AMOUNT"));
				billInfo.setStatusId(r.getString("STATUS"));

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

		return billInfo;
	}

	/*---------------------------------------------------------------------Method for paid bill info--------------------------------------------------*/

	public ArrayList<CollectionDTO> getPaidBillInfo(String customer_id) {
		ArrayList<CollectionDTO> billInfo = new ArrayList<CollectionDTO>();
		CollectionDTO paidInfo = null;

		CustomerService customerService = new CustomerService();
		String sql = "";
		Connection conn = ConnectionManager.getConnection();
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered")) {
			sql = "select * from( "
					+ "select BILL_MONTH,BILL_YEAR,COLLECTION_AMOUNT COLLECTED_BILL_AMOUNT,BM.SURCHARGE_AMOUNT,bcm.BANK_ID,bcm.BRANCH_ID,MBI.BANK_NAME,BI.BRANCH_NAME,to_char(BCM.COLLECTION_DATE) COLLECTION_DATE from BILL_COLLECTION_METERED BCM,SUMMARY_MARGIN_PB SMB,BILL_METERED BM,MST_BANK_INFO MBI,MST_BRANCH_INFO BI "
					+ "where BCM.BILL_ID=BM.BILL_ID  "
					+ "AND BCM.BILL_ID=SMB.BILL_ID "
					+ "AND BCM.BANK_ID=MBI.BANK_ID "
					+ "AND BCM.BRANCH_ID=BI.BRANCH_ID "
					+ "AND BCM.CUSTOMER_ID=? "
					+ "order by BILL_YEAR||lpad(BILL_MONTH,2,0) desc) "
					+ "where rownum<4 ";
		} else {
			sql = "select * from ( "
					+ "select BILL_MONTH,BILL_YEAR,COLLECTED_BILL_AMOUNT, COLLECTED_SURCHARGE_AMOUNT SURCHARGE_AMOUNT,bc.BANK_ID,bc.BRANCH_ID,MBI.BANK_NAME,BI.BRANCH_NAME,to_char(bc.COLLECTION_DATE) COLLECTION_DATE from bill_collection_non_metered bc,bill_non_metered bm,MST_BANK_INFO MBI,MST_BRANCH_INFO BI "
					+ "where BC.BILL_ID=BM.BILL_ID "
					+ "AND BC.BANK_ID=MBI.BANK_ID "
					+ "AND BC.BRANCH_ID=BI.BRANCH_ID "
					+ "AND BC.CUSTOMER_ID = ? "
					+ "order by BILL_YEAR||lpad(BILL_MONTH,2,0) desc "
					+ ") where rownum<4 ";
		}

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {
				paidInfo = new CollectionDTO();
				paidInfo.setBill_month(r.getString("BILL_MONTH"));
				paidInfo.setBill_year(r.getString("BILL_YEAR"));
				paidInfo.setCollected_amount(r
						.getDouble("COLLECTED_BILL_AMOUNT"));
				paidInfo.setSurcharge_amount(r.getDouble("SURCHARGE_AMOUNT"));
				paidInfo.setBank_id(r.getString("BANK_NAME"));
				paidInfo.setBranch_id(r.getString("BRANCH_NAME"));
				paidInfo.setCollection_date(r.getString("COLLECTION_DATE"));
				billInfo.add(paidInfo);

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

		return billInfo;
	}

	public MobBillDTO getMobBillInfo(String customer_id) {
		MobBillDTO mob = new MobBillDTO();
		Connection conn = ConnectionManager.getConnection();

		String sql = "select LISTAGG(MON||' '||substr(BILL_YEAR,3,2), ', ') WITHIN GROUP (ORDER BY BILL_YEAR,BILL_MONTH) monyear,  "
				+ " sum(nvl(BILLED_AMOUNT,0)) BILLED_AMOUNT, sum(nvl(ACTUAL_SURCHARGE,0)) ACTUAL_SURCHARGE, count(*) cn "
				+ " from BILL_NON_METERED bm, mst_month mm "
				+ " where bm.BILL_MONTH=mm.M_ID "
				+ " and CUSTOMER_ID=? "
				+ " and STATUS=1 ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {

				mob.setMonyear(r.getString("monyear"));
				mob.setBill_amount(r.getDouble("BILLED_AMOUNT"));
				mob.setSurcharge(r.getDouble("ACTUAL_SURCHARGE"));
				mob.setBillcount(r.getInt("cn"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mob;

	}

	/*---------------------------------------------------------------------------------------------------Method for Due Bill Info----------------------------------------------------------------------------------------*/

	public OthersDto getOthersInfoForBill(String bill_id, String customer_id) {
		OthersDto others = new OthersDto();
		Connection conn = ConnectionManager.getConnection();

		String sql = "  Select * From OTHERS_AMOUNT Where Bill_Id=? and Customer_Id=?";

		PreparedStatement stmt = null;
		ResultSet r = null;
		String comment = "";
		String amount = "";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bill_id);
			stmt.setString(2, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {
				comment += r.getString("COMMENTS") + "#ifti#";
				amount += r.getDouble("OTHER_AMOUNT") + "#ifti#";

			}
			if (comment.length() > 0)
				comment = comment.substring(0, comment.length() - 6);
			if (amount.length() > 0)
				amount = amount.substring(0, amount.length() - 6);
			others.setTotalOthersCommentString(comment);
			others.setTotalOthersAmountString(amount);
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

		return others;
	}

	public ResponseDTO approveBill(BillingParamDTO bill_parameter) {
		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code = 0;
		bill_parameter.setProcessed_by(loggedInUser.getUserId());
		try {

			System.out.println("===>>Procedure : [ApproveBill] START");
			stmt = (OracleCallableStatement) conn
					.prepareCall("{ call ApproveBill(?,?,?,?,?,?,?,?,?,?,?)  }");
			System.out.println("==>>Procedure : [ApproveBill] END");

			stmt.setString(1, bill_parameter.getIsMetered_str());
			stmt.setString(2, bill_parameter.getBill_for());
			stmt.setString(3, bill_parameter.getBill_id());
			stmt.setString(4, bill_parameter.getCustomer_id());
			stmt.setString(5, bill_parameter.getCustomer_category());
			stmt.setString(6, bill_parameter.getArea_id());
			stmt.setString(7, bill_parameter.getBilling_month_str());
			stmt.setString(8, bill_parameter.getBilling_year());
			stmt.setString(9, bill_parameter.getProcessed_by());
			stmt.registerOutParameter(10, java.sql.Types.INTEGER);
			stmt.registerOutParameter(11, java.sql.Types.VARCHAR);

			stmt.executeUpdate();
			response_code = stmt.getInt(10);
			response.setMessasge(stmt.getString(11));
			if (response_code == 1) {
				response.setResponse(true);
			} else {
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

	public BillingMeteredDTO getBillStatus(int bill_month, int bill_year,
			String customer_id) {
		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select BILL_ID,STATUS from BILL_METERED where BILL_MONTH=? AND BILL_YEAR=? AND CUSTOMER_ID=?";
		PreparedStatement stmt = null;
		ResultSet r = null;
		BillingMeteredDTO bill = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, bill_month);
			stmt.setInt(2, bill_year);
			stmt.setString(3, customer_id);

			r = stmt.executeQuery();
			if (r.next()) {
				bill = new BillingMeteredDTO();
				bill.setBill_id(r.getString("BILL_ID"));
				bill.setBill_satus(BillStatus.values()[r.getInt("STATUS")]);
				bill.setBill_status_name(BillStatus.values()[r.getInt("STATUS")]
						.getLabel());
				bill.setBill_status_str(r.getString("STATUS"));
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

		return bill;

	}

	// Get the bill status by the following way
	// Reconnect id -> Disconnect id -> disconnect reading id.
	// Then find the reading id bill status
	public BillingMeteredDTO getBillStatus(String reconnect_id) {
		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select BILL_ID,STATUS from BILL_METERED Where Bill_Id in  "
				+ "( "
				+ "  SELECT Bill_ID FROM BILLING_READING_MAP Where Reading_Id in  "
				+ "    (Select Reading_Id From DISCONN_METERED WHERE PID IN (Select DISCONNECT_ID from RECONN_METERED WHERE PID=?)) "
				+ ") ";
		PreparedStatement stmt = null;
		ResultSet r = null;
		BillingMeteredDTO bill = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, reconnect_id);

			r = stmt.executeQuery();
			if (r.next()) {
				bill = new BillingMeteredDTO();
				bill.setBill_id(r.getString("BILL_ID"));
				bill.setBill_satus(BillStatus.values()[r.getInt("STATUS")]);
				bill.setBill_status_name(BillStatus.values()[r.getInt("STATUS")]
						.getLabel());
				bill.setBill_status_str(r.getString("STATUS"));
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

		return bill;

	}

	public ResponseDTO updateAdjustmentInfo(String bill_id,
			String metered_status, double adjustment_amount, String remarks) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlMeteredOldAdjAmount = "", sqlMeteredAdjUpdate = "", sqlMeteredPayableUpdate = "", sqlNonMeteredOldAdjAmount = "", sqlNonMeteredUpdate = "";

		sqlMeteredOldAdjAmount = "Select adjustment_amount from SUMMARY_MARGIN_PB Where Bill_Id=?";
		sqlMeteredAdjUpdate = " Update SUMMARY_MARGIN_PB Set adjustment_amount=NVL(adjustment_amount,0)-?+?, adjustment_comments=?, "
				+ "  TOTAL_AMOUNT=NVL(TOTAL_AMOUNT,0)-?+?  "
				+ "  Where Bill_Id=?";
		sqlMeteredPayableUpdate = " Update BILL_METERED Set payable_amount=NVL(payable_amount,0)-?+? ,"
				+ "  AMOUNT_IN_WORD=NUMBER_SPELLOUT_FUNC(NVL(payable_amount,0)-?+?) "
				+ " Where Bill_Id=?";

		sqlNonMeteredOldAdjAmount = "Select adjustment_amount from BILL_NON_METERED Where Bill_Id=?";
		sqlNonMeteredUpdate = "Update BILL_NON_METERED set ADJUSTMENT_AMOUNT=?, adjustment_comments=?, PAYABLE_AMOUNT=NVL(payable_amount,0)-?+? Where Bill_Id=?";

		PreparedStatement stmt = null;
		try {
			if (MeteredStatus.values()[Integer.parseInt(metered_status)]
					.getId() == MeteredStatus.METERED.getId()) {
				stmt = conn.prepareStatement(sqlMeteredOldAdjAmount);
				stmt.setString(1, bill_id);
				ResultSet r = stmt.executeQuery();
				double oldAdjAmount = 0;
				if (r.next()) {
					oldAdjAmount = r.getDouble("adjustment_amount");
				}

				stmt = conn.prepareStatement(sqlMeteredAdjUpdate);
				stmt.setDouble(1, oldAdjAmount);
				stmt.setDouble(2, adjustment_amount);
				stmt.setString(3, remarks);
				stmt.setDouble(4, oldAdjAmount);
				stmt.setDouble(5, adjustment_amount);
				stmt.setString(6, bill_id);
				stmt.executeUpdate();

				stmt = conn.prepareStatement(sqlMeteredPayableUpdate);
				stmt.setDouble(1, oldAdjAmount);
				stmt.setDouble(2, adjustment_amount);
				stmt.setDouble(3, oldAdjAmount);
				stmt.setDouble(4, adjustment_amount);
				stmt.setString(5, bill_id);
				stmt.executeUpdate();
			} else if (MeteredStatus.values()[Integer.parseInt(metered_status)]
					.getId() == MeteredStatus.NONMETERED.getId()) {

				stmt = conn.prepareStatement(sqlNonMeteredOldAdjAmount);
				stmt.setString(1, bill_id);
				ResultSet r = stmt.executeQuery();
				double oldAdjAmount = 0;
				if (r.next()) {
					oldAdjAmount = r.getDouble("adjustment_amount");
				}

				stmt = conn.prepareStatement(sqlNonMeteredUpdate);
				stmt.setDouble(1, adjustment_amount - oldAdjAmount);
				stmt.setString(2, remarks);
				stmt.setDouble(3, oldAdjAmount);
				stmt.setDouble(4, adjustment_amount);
				stmt.setString(5, bill_id);
				stmt.executeUpdate();

			}

			transactionManager.commit();

			response.setMessasge("Successfully Saved Adjustment Information.");
			response.setResponse(true);
		} catch (Exception e) {
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

	public ResponseDTO updateNonMeterSalesAdjustmentInfo(String bill_id,
			String metered_status, double payable_amount, double adj_amount,
			double new_payable_amount, double total_consumption,
			double adj_consumtion, double new_total_consumption, String remarks) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlNonMeteredUpdate = "", sqlSalesReportUpdate = "", sqlCustomerLedgerUpdate, sqlInsertAdjustmentTable = "";

		sqlInsertAdjustmentTable = "INSERT INTO NON_METER_ADJUSTMENT (BILL_ID, SALES_AMOUNT, SALES_CONSUMPTION, SALES_ADJUSTMET_COMMENT,INSERTED_BY) VALUES (?,?,?,?,?)";
		sqlNonMeteredUpdate = "Update BILL_NON_METERED set  ACTUAL_PAYABLE_AMOUNT=?,TOTAL_CONSUMPTION=? Where Bill_Id=?";
		sqlSalesReportUpdate = "Update SALES_REPORT set ACTUAL_EXCEPT_MINIMUM=?,TOTAL_ACTUAL_CONSUMPTION=?,VALUE_OF_ACTUAL_CONSUMPTION=?,TOTAL_AMOUNT=? Where Bill_Id=?";
		sqlCustomerLedgerUpdate = "Update CUSTOMER_LEDGER set DEBIT=? Where Bill_Id=? ";

		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(sqlInsertAdjustmentTable);
			stmt.setString(1, bill_id);
			stmt.setDouble(2, adj_amount);
			stmt.setDouble(3, adj_consumtion);
			stmt.setString(4, remarks);
			stmt.setString(5, loggedInUser.getUserName());
			stmt.executeQuery();

			stmt = conn.prepareStatement(sqlNonMeteredUpdate);
			stmt.setDouble(1, new_payable_amount);
			stmt.setDouble(2, new_total_consumption);
			stmt.setString(3, bill_id);
			stmt.executeUpdate();

			stmt = conn.prepareStatement(sqlSalesReportUpdate);
			stmt.setDouble(1, new_total_consumption);
			stmt.setDouble(2, new_total_consumption);
			stmt.setDouble(3, new_payable_amount);
			stmt.setDouble(4, new_payable_amount);
			stmt.setString(5, bill_id);
			stmt.executeUpdate();

			stmt = conn.prepareStatement(sqlCustomerLedgerUpdate);
			stmt.setDouble(1, new_payable_amount);
			stmt.setString(2, bill_id);
			stmt.executeUpdate();

			transactionManager.commit();

			response.setMessasge("Successfully Saved Adjustment Information.");
			response.setResponse(true);
		} catch (Exception e) {
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

	public ResponseDTO saveOthersAmountForBilling(String bill_id,
			String customer_id, String others_comments, String others_amount,
			double total_others_amount, String total_others_comment) {

		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code = 0;
		String response_msg = "";
		ResponseDTO response = new ResponseDTO();

		String[] commentsArr;
		if (others_comments.equalsIgnoreCase(""))
			commentsArr = new String[0];
		else
			commentsArr = others_comments.split("#ifti#");

		String[] amountStrArr;
		if (others_amount.equalsIgnoreCase(""))
			amountStrArr = new String[0];
		else
			amountStrArr = others_amount.split("#ifti#");

		double[] amountArr = new double[amountStrArr.length];
		for (int i = 0; i < amountStrArr.length; i++) {
			amountArr[i] = Double.parseDouble(amountStrArr[i]);
		}

		// sqlNonMeteredUpdate="update sales_report set MINIMUM_CHARGE=MINIMUM_CHARGE+? where bill_id=?";
		try {
			ArrayDescriptor arrString = new ArrayDescriptor("VARCHARARRAY",
					conn);
			ArrayDescriptor arrNumber = new ArrayDescriptor("NUMBERARRAY", conn);

			ARRAY inputComment = new ARRAY(arrString, conn, commentsArr);
			ARRAY inputAmount = new ARRAY(arrNumber, conn, amountArr);

			System.out.println("Procedure SaveOthersAmount Begins");
			stmt = (OracleCallableStatement) conn
					.prepareCall("{ call SaveOthersAmount	(?,?,?,?,?,?,?,?) }");

			stmt.setString(1, bill_id);
			stmt.setString(2, customer_id);

			stmt.setArray(3, inputComment);
			stmt.setArray(4, inputAmount);
			stmt.setString(5, total_others_comment);
			stmt.setDouble(6, total_others_amount);
			stmt.registerOutParameter(7, java.sql.Types.INTEGER);
			stmt.registerOutParameter(8, java.sql.Types.VARCHAR);

			stmt.executeUpdate();
			response_code = stmt.getInt(7);
			response_msg = (stmt.getString(8)).trim();

			/*
			 * n_stmt = conn.prepareStatement(sqlNonMeteredUpdate);
			 * 
			 * 
			 * n_stmt.setDouble(1,total_others_amount);
			 * n_stmt.setString(2,bill_id); n_stmt.executeUpdate();
			 */

			response.setMessasge(response_msg);
			response.setResponse(response_code == 1 ? true : false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponse(false);
			response.setMessasge(e.getMessage());
			return response;
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

	public ResponseDTO saveSalesAdjustmentBilling(String customer_id,
			String bill_month, String bill_year, String issue_date,
			String due_date, double bill_amount, double surcharge_amount,
			double meter_rent, double total_consumption, String payment_status) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		NMAdjustmentDTO bdDto = new NMAdjustmentDTO();

		String abc, customer_ledger_insert_debit, customer_ledger_insert_credit;

		abc = " Insert into SALES_ADJUSTMENT ( BILL_ID,BILL_MONTH,BILL_YEAR,CUSTOMER_ID,CUSTOMER_NAME,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,CATEGORY_TYPE,ADDRESS,DOUBLE_BURNER_QNT,MINIMUM_LOAD,MONTHLY_CONTACTUAL_LOAD,BILLED_AMOUNT,ACTUAL_SURCHARGE,METER_RENT,PAYABLE_AMOUNT,ISSUE_DATE,DUE_DATE,TOTAL_CONSUMPTION,PREPARED_BY,STATUS,ISMETERED,FF_QUOTA,PREPARED_ON) "
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'dd-MM-YYYY'),to_date(?,'dd-MM-YYYY'),?,?,?,?,?,sysdate) ";

		customer_ledger_insert_debit = " INSERT INTO customer_ledger (TRANS_ID,TRANS_DATE,PARTICULARS,DEBIT,BILL_ID,INSERTED_BY,CUSTOMER_ID,STATUS,INSERTED_ON) "
				+ " values (?,to_date(sysdate,'dd-MM-RRRR'),?,?,?,?,?,?,sysdate) ";

		customer_ledger_insert_credit = " INSERT INTO customer_ledger (TRANS_ID,TRANS_DATE,PARTICULARS,CREDIT,BILL_ID,INSERTED_BY,CUSTOMER_ID,STATUS,INSERTED_ON) "
				+ " values (?,to_date(sysdate,'dd-MM-RRRR'),?,?,?,?,?,?,sysdate) ";

		PreparedStatement stmt = null;
		PreparedStatement ledger_stmt = null;

		PreparedStatement bill_stmt = null;
		PreparedStatement sqlFetchInfo = null;
		PreparedStatement trans_id_generation = null;
		ResultSet r = null;
		String bill_id = null;
		String trans_id = null;

		String status = "";

		double totalAmount = bill_amount + meter_rent + surcharge_amount;
		double totalSales = bill_amount + meter_rent;

		double adjAmount = 0.0;
		try {

			if (totalSales < 0) {
				adjAmount = totalSales * (-1);

			} else {
				adjAmount = totalSales;
			}

			bill_stmt = conn
					.prepareStatement("Select SQN_BILL_ADJ.NEXTVAL bill_id from dual");

			r = bill_stmt.executeQuery();
			if (r.next())
				bill_id = r.getString("bill_id");

			trans_id_generation = conn
					.prepareStatement("select SQN_CL.NEXTVAL TRANS_ID from dual");

			r = trans_id_generation.executeQuery();
			if (r.next())
				trans_id = r.getString("TRANS_ID");

			sqlFetchInfo = conn
					.prepareStatement("select * from MVIEW_CUSTOMER_INFO where customer_id= ? ");

			sqlFetchInfo.setString(1, customer_id);

			ResultSet resultSet = sqlFetchInfo.executeQuery();

			/*
			 * if(totalAmount>0 && payment_status.equals("02")){ status="1";
			 * }else{ status="2"; }
			 */

			while (resultSet.next()) {

				bdDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
				bdDto.setCustomer_name(resultSet.getString("FULL_NAME"));
				bdDto.setCustomer_categoryID(resultSet.getString("CATEGORY_ID"));
				bdDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
				bdDto.setCategory_type(resultSet.getString("CATEGORY_TYPE"));
				bdDto.setAddress(resultSet.getString("ADDRESS_LINE1"));
				bdDto.setIsMetered(resultSet.getString("ISMETERED"));
				bdDto.setDouble_burner(resultSet.getString("DOUBLE_BURNER_QNT"));
				bdDto.setContractul_load(resultSet.getDouble("MAX_LOAD"));
				bdDto.setMin_load(resultSet.getDouble("MIN_LOAD"));
				bdDto.setFf_quata(resultSet.getString("FREEDOM_FIGHTER"));

			}

			status = "2";

			if (totalAmount > 0) {

				ledger_stmt = conn
						.prepareStatement(customer_ledger_insert_debit);

				ledger_stmt.setString(1, trans_id);
				ledger_stmt
						.setString(
								2,
								"Adjustment Sales, "
										+ Month.values()[Integer
												.valueOf(bill_month) - 1]
												.getLabel() + " " + bill_year);
				ledger_stmt.setDouble(3, adjAmount);
				ledger_stmt.setString(4, bill_id);
				ledger_stmt.setString(5, loggedInUser.getUserName());
				ledger_stmt.setString(6, customer_id);
				ledger_stmt.setString(7, "1");

				ledger_stmt.executeQuery();

				if (payment_status.equals("02"))
					status = "1";
			} else {

				ledger_stmt = conn
						.prepareStatement(customer_ledger_insert_credit);

				ledger_stmt.setString(1, trans_id);
				ledger_stmt
						.setString(
								2,
								"Adjustment Sales, "
										+ Month.values()[Integer
												.valueOf(bill_month) - 1]
												.getLabel() + " " + bill_year);
				ledger_stmt.setDouble(3, adjAmount);
				ledger_stmt.setString(4, bill_id);
				ledger_stmt.setString(5, loggedInUser.getUserName());
				ledger_stmt.setString(6, customer_id);
				ledger_stmt.setString(7, "1");

				ledger_stmt.executeQuery();

			}

			stmt = conn.prepareStatement(abc);

			stmt.setString(1, bill_id);
			stmt.setString(2, bill_month);
			stmt.setString(3, bill_year);
			stmt.setString(4, customer_id);
			stmt.setString(5, bdDto.getCustomer_name());
			stmt.setString(6, bdDto.getCustomer_categoryID());
			stmt.setString(7, bdDto.getCategory_name());
			stmt.setString(8, bdDto.getCategory_type());
			stmt.setString(9, bdDto.getAddress());
			stmt.setString(10, bdDto.getDouble_burner());
			stmt.setDouble(11, bdDto.getMin_load());
			stmt.setDouble(12, bdDto.getContractul_load());
			stmt.setDouble(13, totalSales);
			stmt.setDouble(14, surcharge_amount);
			stmt.setDouble(15, meter_rent);
			stmt.setDouble(16, totalAmount);
			stmt.setString(17, issue_date);
			stmt.setString(18, due_date);
			stmt.setDouble(19, total_consumption);
			stmt.setString(20, loggedInUser.getUserName());
			stmt.setString(21, status);
			stmt.setString(22, bdDto.getIsMetered());
			stmt.setString(23, bdDto.getFf_quata());

			stmt.executeQuery();

			transactionManager.commit();

			response.setMessasge("Successfully Saved Sales Adjustment Information.");
			response.setResponse(true);
		} catch (Exception e) {
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

	public ResponseDTO saveSalesAdjustmentBilling(String bill_id,
			String customer_id, String customer_name, String father_name,
			String address, String customer_category, String meter_status,
			String bill_month, String bill_year, String issue_date,
			String due_date, double bill_amount, double surcharge_amount,
			double meter_rent, double total_amount, double total_consumption,
			String payment_status) {
		// TODO Auto-generated method stub
		return null;
	}

}

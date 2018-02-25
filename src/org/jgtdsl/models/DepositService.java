package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerGridDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.DepositDtlDTO;
import org.jgtdsl.dto.DepositTypeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.BankAccountTransactionType;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.DepositPurpose;
import org.jgtdsl.enums.DepositType;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class DepositService {

	public ArrayList<DepositTypeDTO> getDepositTypeList(int index, int offset,
			String whereClause, String sortFieldName, String sortOrder,
			int total) {
		DepositTypeDTO depositDTO = null;
		ArrayList<DepositTypeDTO> depositTypeList = new ArrayList<DepositTypeDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = " Select * from MST_DEPOSIT_TYPES  "
					+ (whereClause.equalsIgnoreCase("") ? "" : ("Where ( "
							+ whereClause + ")")) + " " + orderByQuery;
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select * from MST_DEPOSIT_TYPES "
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
				depositDTO = new DepositTypeDTO();
				depositDTO.setType_id(r.getString("TYPE_ID"));
				depositDTO.setType_name_eng(r.getString("TYPE_NAME_ENG"));
				depositDTO.setDescription(r.getString("DESCRIPTION"));
				depositDTO.setView_order(r.getString("VIEW_ORDER"));
				depositDTO.setStatus(r.getString("STATUS"));
				depositTypeList.add(depositDTO);
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

		return depositTypeList;
	}

	public ArrayList<DepositTypeDTO> getDepositTypeList() {
		return getDepositTypeList(0, 0, Utils.EMPTY_STRING, Utils.EMPTY_STRING,
				Utils.EMPTY_STRING, 0);
	}

	public String addDepositType(String data) {
		Gson gson = new Gson();
		DepositTypeDTO depositDTO = gson.fromJson(data, DepositTypeDTO.class);
		Connection conn = ConnectionManager.getConnection();
		String sql = " Insert into MST_DEPOSIT_TYPES(TYPE_ID,TYPE_NAME_ENG,DESCRIPTION,VIEW_ORDER,STATUS) Values(?,?,?,?,?)";
		int response = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, depositDTO.getType_id());
			stmt.setString(2, depositDTO.getType_name_eng());
			stmt.setString(3, depositDTO.getDescription());
			stmt.setString(4, depositDTO.getView_order());
			stmt.setString(5, depositDTO.getStatus());

			response = stmt.executeUpdate();
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
					+ AC.MST_DEPOSIT_TYPE);
		else
			return Utils.getJsonString(AC.STATUS_ERROR,
					AC.MSG_CREATE_ERROR_PREFIX + AC.MST_DEPOSIT_TYPE);

	}

	public String deleteDepositType(String depositTypeId) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		String dtId = null;
		;
		try {
			jsonObject = (JSONObject) jsonParser.parse(depositTypeId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());
		}
		dtId = (String) jsonObject.get("id");
		Connection conn = ConnectionManager.getConnection();
		String sql = " Delete MST_DEPOSIT_TYPES Where TYPE_ID=?";
		int response = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, dtId);
			response = stmt.executeUpdate();
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
					+ AC.MST_DEPOSIT_TYPE);
		else
			return Utils.getJsonString(AC.STATUS_ERROR,
					AC.MSG_DELETE_ERROR_PREFIX + AC.MST_DEPOSIT_TYPE);

	}

	public String updateDepositType(String data) {
		Gson gson = new Gson();
		DepositTypeDTO depositTypeDTO = gson.fromJson(data,
				DepositTypeDTO.class);
		Connection conn = ConnectionManager.getConnection();
		String sql = " Update MST_DEPOSIT_TYPES SET TYPE_NAME_ENG=?,DESCRIPTION=?,VIEW_ORDER=?,STATUS=? Where TYPE_ID=?";
		int response = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, depositTypeDTO.getType_name_eng());
			stmt.setString(2, depositTypeDTO.getDescription());
			stmt.setString(3, depositTypeDTO.getView_order());
			stmt.setString(4, depositTypeDTO.getStatus());
			stmt.setString(5, depositTypeDTO.getType_id());

			response = stmt.executeUpdate();
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
					+ AC.MST_DEPOSIT_TYPE);
		else
			return Utils.getJsonString(AC.STATUS_ERROR,
					AC.MSG_UPDATE_ERROR_PREFIX + AC.MST_DEPOSIT_TYPE);

	}

	public int getDepositStatus(String deposit_id) {
		Connection conn = ConnectionManager.getConnection();
		String sql = "Select Status  from CUSTOMER_SECURITY_LEDGER where DEPOSIT_ID=?";

		PreparedStatement stmt = null;
		ResultSet r = null;

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, deposit_id);

			r = stmt.executeQuery();
			if (r.next()) {
				return r.getInt("STATUS");
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

		return 1;
	}

	public ResponseDTO saveDeposit(String customer_id, DepositDTO deposit) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String mstDepositSql = " Insert Into MST_DEPOSIT(DEPOSIT_ID,CUSTOMER_ID,DEPOSIT_TYPE,DEPOSIT_PURPOSE,VALID_FROM,VALID_TO,BANK_ID,BRANCH_ID, "
				+ " ACCOUNT_NO,DEPOSIT_DATE,TOTAL_DEPOSIT,INSERTED_BY,REMARKS) "
				+ " Values(?,?,?,?,to_date(?,'dd-MM-YYYY'),to_date(?,'dd-MM-YYYY'),?,?,?,to_date(?,'dd-MM-YYYY'),?,?,?)";
		
		//no need for dtl_deposit now

//		String dtlDepositSql = " Insert Into DTL_DEPOSIT(DEPOSIT_ID,TYPE_ID,AMOUNT) "
//				+ " Values(?,?,?) ";

		
		//no need for CUSTOMER_SECURITY_LEDGER now
		
//		String depositLedgerSql = " Insert Into CUSTOMER_SECURITY_LEDGER(TRANS_ID, TRANS_DATE, DESCRIPTION, SECURITY_AMOUNT,DEBIT, BALANCE, DEPOSIT_ID, "
//				+ " CUSTOMER_ID,STATUS,INSERTED_BY) "
//				+ " Values(SQN_DEPOSITID.NEXTVAL,TO_DATE(?,'dd-MM-YYYY'),?,?,?,?,?,?,?,?)";
//
//		String depositAccountBalanceQuery = " Select Balance from CUSTOMER_SECURITY_LEDGER Where Trans_Id "
//				+ " In(Select max(Trans_Id) from CUSTOMER_SECURITY_LEDGER where Customer_Id=?) ";

		String bankAccountLedgerQuery = " Insert Into BANK_ACCOUNT_LEDGER(TRANS_ID,TRANS_DATE,TRANS_TYPE,PARTICULARS,BANK_ID,BRANCH_ID,ACCOUNT_NO,DEBIT,BALANCE,REF_ID, "
				+ " INSERTED_BY,CUSTOMER_ID,STATUS,MISCELLANEOUS) "
				+ " Values(SQN_BAL.nextval,to_date(?,'dd-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?)";

		String bankAccountBalanceQuery = " Select Balance from BANK_ACCOUNT_LEDGER Where Trans_Id "
				+ " In(Select max(Trans_Id) from BANK_ACCOUNT_LEDGER where Customer_Id=? and Bank_Id=? and Branch_Id=? and Account_no=?) ";

		PreparedStatement mst_stmt = null;
		//PreparedStatement dtl_stmt = null;
		PreparedStatement deposit_ledger_stmt = null;
		PreparedStatement deposit_account_balance_stmt = null;
		PreparedStatement bank_account_balance_stmt = null;
		PreparedStatement bank_account_stmt = null;

		ResultSet r = null;
		String deposit_id = null;
		double securityMoney = 0;
		double depositAccountBalance = 0;
		double bankAccountBalance = 0;
		try {
			mst_stmt = conn
					.prepareStatement("Select SQN_DEPOSIT.nextval deposit_id from dual");
			r = mst_stmt.executeQuery();
			if (r.next())
				deposit_id = r.getString("deposit_id");

			// Insert data in DTL_DEPOSIT Table
			
//			deposit_account_balance_stmt = conn
//					.prepareStatement(depositAccountBalanceQuery);
//			deposit_account_balance_stmt.setString(1, customer_id);
//			r = deposit_account_balance_stmt.executeQuery();
//			if (r.next())
//				depositAccountBalance = r.getDouble("Balance");
			
			//no need for detail_deposit now
//			dtl_stmt = conn.prepareStatement(dtlDepositSql);
//			for (DepositDtlDTO item : deposit.getDepositDetail()) {
//				if (item.getAmount() > 0) {
//					dtl_stmt.setString(1, deposit_id);
//					dtl_stmt.setString(2, item.getType_id());
//					dtl_stmt.setDouble(3, item.getAmount());
//					dtl_stmt.addBatch();
//
//					if (item.getType_id().equalsIgnoreCase("01"))
//						securityMoney = item.getAmount();
//				}
//			}
//			dtl_stmt.executeBatch();

			// Transaction for Customer's Security/Other Deposit Account Ledger
//			deposit_ledger_stmt = conn.prepareStatement(depositLedgerSql);
//			deposit_ledger_stmt.setString(1, deposit.getDeposit_date());
//			deposit_ledger_stmt.setString(2, String.valueOf(DepositType
//					.values()[Integer.valueOf(deposit.getStr_deposit_type())]));
//			deposit_ledger_stmt.setDouble(3, securityMoney);
//			deposit_ledger_stmt.setString(4, deposit.getTotal_deposit());
//			deposit_ledger_stmt.setDouble(
//					5,
//					depositAccountBalance
//							+ Double.valueOf(deposit.getTotal_deposit()));
//			deposit_ledger_stmt.setString(6, deposit_id);
//			deposit_ledger_stmt.setString(7, customer_id);
//			deposit_ledger_stmt.setInt(8, 0);
//			deposit_ledger_stmt.setString(9, deposit.getInserted_by());
//			deposit_ledger_stmt.execute();

			// Insert data in MST_DEPOSIT Table
			mst_stmt = conn.prepareStatement(mstDepositSql);
			mst_stmt.setString(1, deposit_id);
			mst_stmt.setString(2, customer_id);
			mst_stmt.setString(3, deposit.getStr_deposit_type());
			
			if(deposit.getStr_deposit_purpose().equals("9")){
				mst_stmt.setString(4,"1");
			}else{
				mst_stmt.setString(4, deposit.getStr_deposit_purpose());
			}
			
			mst_stmt.setString(5, deposit.getValid_from());
			mst_stmt.setString(6, deposit.getValid_to());

			mst_stmt.setString(
					7,
					(Integer.valueOf(deposit.getStr_deposit_type()) == DepositType.BANK_GURANTEE
							.getId() || Integer.valueOf(deposit
							.getStr_deposit_type()) == DepositType.FDR.getId()) ? ""
							: deposit.getBank());
			mst_stmt.setString(
					8,
					(Integer.valueOf(deposit.getStr_deposit_type()) == DepositType.BANK_GURANTEE
							.getId() || Integer.valueOf(deposit
							.getStr_deposit_type()) == DepositType.FDR.getId()) ? ""
							: deposit.getBranch());
			mst_stmt.setString(
					9,
					(Integer.valueOf(deposit.getStr_deposit_type()) == DepositType.BANK_GURANTEE
							.getId() || Integer.valueOf(deposit
							.getStr_deposit_type()) == DepositType.FDR.getId()) ? "B.G." : deposit.getAccount_no());
			mst_stmt.setString(10, deposit.getDeposit_date());
			mst_stmt.setString(11, deposit.getTotal_deposit());
			mst_stmt.setString(12, deposit.getInserted_by());
			mst_stmt.setString(13, deposit.getRemarks_on_bg());
			mst_stmt.execute();

			// Transaction for Bank Account Ledger
			if (Integer.valueOf(deposit.getStr_deposit_type()) == DepositType.CASH_BANK
					.getId()) {

				// Find out last balance of the targe bank account
				bank_account_balance_stmt = conn
						.prepareStatement(bankAccountBalanceQuery);
				bank_account_balance_stmt.setString(1, customer_id);
				bank_account_balance_stmt.setString(2, deposit.getBank());
				bank_account_balance_stmt.setString(3, deposit.getBranch());
				bank_account_balance_stmt.setString(4, deposit.getAccount_no());
				r = bank_account_balance_stmt.executeQuery();
				if (r.next())
					bankAccountBalance = r.getDouble("Balance");

				bank_account_stmt = conn
						.prepareStatement(bankAccountLedgerQuery);
				bank_account_stmt.setString(1, deposit.getDeposit_date());
				
				//special trans_type for security_deposit
				if(deposit.getStr_deposit_purpose().equals("9"))
				{
					bank_account_stmt.setInt(2,0);
				}else{
					bank_account_stmt.setInt(2,7);
				}
				bank_account_stmt.setString(3, "Security/Other Deposit");
				bank_account_stmt.setString(4, deposit.getBank());
				bank_account_stmt.setString(5, deposit.getBranch());
				bank_account_stmt.setString(6, deposit.getAccount_no());
				bank_account_stmt.setString(7, deposit.getTotal_deposit());
				bank_account_stmt.setDouble(
						8,
						bankAccountBalance
								+ Double.valueOf(deposit.getTotal_deposit()));
				bank_account_stmt.setString(9, deposit_id);
				bank_account_stmt.setString(10, deposit.getInserted_by());
				bank_account_stmt.setString(11, customer_id);
				bank_account_stmt.setInt(12, 0);
				bank_account_stmt.setDouble(
						13,
						Double.valueOf(deposit.getTotal_deposit())
								- Double.valueOf(securityMoney));
				bank_account_stmt.execute();
			}

			transactionManager.commit();

			response.setMessasge("Successfully Saved Deposit Information.");
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
				mst_stmt.close();
				//dtl_stmt.close();
				transactionManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mst_stmt = null;
			//dtl_stmt = null;
			conn = null;
		}

		return response;
	}

	public ResponseDTO editDeposit(DepositDTO deposit) {

		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();
//~FOR SAVING HISTORY ~SEPT 21 ~ Prince
		String customerSecurityLedgerHistorySql = "INSERT INTO CUST_SECURITY_LEDGER_HISTORY ( CUSTOMER_ID, DEPOSIT_ID, TRANS_DATE," 
				+" DESCRIPTION, SECURITY_AMOUNT, DEBIT, CREDIT, BALANCE, VALID_FROM, VALID_TO)" 
				+"( SELECT CSL.CUSTOMER_ID, CSL.DEPOSIT_ID,CSL.TRANS_DATE,CSL.DESCRIPTION,CSL.SECURITY_AMOUNT,CSL.DEBIT,"
				+" CSL.CREDIT,CSL.BALANCE, MD.VALID_FROM, MD.VALID_TO FROM CUSTOMER_SECURITY_LEDGER CSL," 
				+" MST_DEPOSIT MD WHERE  CSL.DEPOSIT_ID = ?"
				+" AND CSL.DEPOSIT_ID = MD.DEPOSIT_ID )";


		String mstDepositSql = "Update MST_DEPOSIT Set VALID_FROM=TO_DATE(?,'DD-MM-YYYY'),VALID_TO=TO_DATE(?,'DD-MM-YYYY'),BANK_ID=?,BRANCH_ID=?,ACCOUNT_NO=?,TOTAL_DEPOSIT=? Where Deposit_ID=?";
		String dtlDepositDeleteSql = "Delete DTL_DEPOSIT Where Deposit_Id=?";
		String dtlDepositSql = " Insert Into DTL_DEPOSIT(DEPOSIT_ID,TYPE_ID,AMOUNT) "
				+ " Values(?,?,?) ";

		String depositLedgerSql = " Update CUSTOMER_SECURITY_LEDGER Set SECURITY_AMOUNT=?,DEBIT=?,VALID_TO=TO_DATE(?,'DD-MM-YYYY') Where DEPOSIT_ID=?";

		String bankAccountLedgerSql = " Update BANK_ACCOUNT_LEDGER Set BANK_ID=?,BRANCH_ID=?,ACCOUNT_NO=?,DEBIT=?,MISCELLANEOUS=? Where REF_ID=?";
//
		PreparedStatement security_history_stmt = null;
		PreparedStatement mst_stmt = null;
		PreparedStatement dtl_stmt = null;
		PreparedStatement delete_dtl_stmt = null;
		PreparedStatement deposit_ledger_stmt = null;
		PreparedStatement bank_account_stmt = null;
		double securityMoney = 0;

		try {
			// save security history ~sept 21 ~ Prince
			security_history_stmt = conn.prepareStatement(customerSecurityLedgerHistorySql);
			//security_history_stmt.setString(1, deposit.getCustomer_id());
			security_history_stmt.setString(1, deposit.getDeposit_id());
			security_history_stmt.execute();
			

			// Update data in MST_DEPOSIT Table
			mst_stmt = conn.prepareStatement(mstDepositSql);
			mst_stmt.setString(1, deposit.getValid_from());
			mst_stmt.setString(2, deposit.getValid_to());
			mst_stmt.setString(3, deposit.getBank());
			mst_stmt.setString(4, deposit.getBranch());
			mst_stmt.setString(5, deposit.getAccount_no());
			mst_stmt.setString(6, deposit.getTotal_deposit());
			mst_stmt.setString(7, deposit.getDeposit_id());
			mst_stmt.execute();

			// Delete data in DTL_DEPOSIT Table
			delete_dtl_stmt = conn.prepareStatement(dtlDepositDeleteSql);
			delete_dtl_stmt.setString(1, deposit.getDeposit_id());
			delete_dtl_stmt.execute();

			// Insert data in DTL_DEPOSIT Table
			dtl_stmt = conn.prepareStatement(dtlDepositSql);
			for (DepositDtlDTO item : deposit.getDepositDetail()) {
				if (item.getAmount() > 0) {
					dtl_stmt.setString(1, deposit.getDeposit_id());
					dtl_stmt.setString(2, item.getType_id());
					dtl_stmt.setDouble(3, item.getAmount());
					dtl_stmt.addBatch();

					if (item.getType_id().equalsIgnoreCase("01"))
						securityMoney = item.getAmount();
				}
			}
			dtl_stmt.executeBatch();

			// Transaction for Customer's Security/Other Deposit Account Ledger
			deposit_ledger_stmt = conn.prepareStatement(depositLedgerSql);
			deposit_ledger_stmt.setDouble(1, securityMoney);
			deposit_ledger_stmt.setString(2, deposit.getTotal_deposit());
			//CHANGING EXPIRE DATE IN SECURITY LEDGER ~SEPT 21 ~ Prince
			deposit_ledger_stmt.setString(3, deposit.getValid_to());
			deposit_ledger_stmt.setString(4, deposit.getDeposit_id());
			deposit_ledger_stmt.execute();

			// Transaction for Bank Account Ledger
			if (Integer.valueOf(deposit.getStr_deposit_type()) == DepositType.CASH_BANK
					.getId()) {

				bank_account_stmt = conn.prepareStatement(bankAccountLedgerSql);
				bank_account_stmt.setString(1, deposit.getBank());
				bank_account_stmt.setString(2, deposit.getBranch());
				bank_account_stmt.setString(3, deposit.getAccount_no());
				bank_account_stmt.setString(4, deposit.getTotal_deposit());
				bank_account_stmt.setDouble(
						5,
						Double.valueOf(deposit.getTotal_deposit())
								- Double.valueOf(securityMoney));
				bank_account_stmt.setString(6, deposit.getDeposit_id());
				bank_account_stmt.execute();
			}

			transactionManager.commit();

			response.setMessasge("Successfully Updated Deposit Information.");
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
				mst_stmt.close();
				dtl_stmt.close();
				transactionManager.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mst_stmt = null;
			dtl_stmt = null;
			conn = null;
		}

		return response;
	}

	public ResponseDTO deleteDepositInfo(String deposit_id) {
		ResponseDTO response = new ResponseDTO();
		TransactionManager transactionManager = new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlMstDeposit = "DELETE MST_DEPOSIT WHERE DEPOSIT_ID=?";
		String sqlDtlDeposit = "DELETE DTL_DEPOSIT WHERE DEPOSIT_ID=?";
		String sqlCustomerSecurityLedger = " DELETE CUSTOMER_SECURITY_LEDGER WHERE DEPOSIT_ID=?";
		String sqlBankLedger = "DELETE BANK_ACCOUNT_LEDGER Where Ref_Id=?";

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sqlMstDeposit);
			stmt.setString(1, deposit_id);
			stmt.executeUpdate();

			stmt = conn.prepareStatement(sqlDtlDeposit);
			stmt.setString(1, deposit_id);
			stmt.executeUpdate();

			stmt = conn.prepareStatement(sqlCustomerSecurityLedger);
			stmt.setString(1, deposit_id);
			stmt.executeUpdate();

			stmt = conn.prepareStatement(sqlBankLedger);
			stmt.setString(1, deposit_id);
			stmt.executeUpdate();

			transactionManager.commit();

			response.setMessasge("Successfully Deleted Deposit Information.");
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

	public ArrayList<DepositDTO> getDepositList(String customer_id) {
		DepositDTO deposit = null;
		ArrayList<DepositDTO> depositList = new ArrayList<DepositDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "Select MST_DEPOSIT.*,TO_CHAR(DEPOSIT_DATE,'DD-MM-YYYY')  DEPOSIT_DATE_ from MST_DEPOSIT Where Customer_Id=? order by deposit_id desc";
		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {
				deposit = new DepositDTO();
				deposit.setDeposit_id(r.getString("DEPOSIT_ID"));
				if(r.getInt("DEPOSIT_PURPOSE")==1){
					deposit.setStr_deposit_purpose(DepositPurpose.values()[r
					                               						.getInt("DEPOSIT_PURPOSE")-1].getLabel());
					deposit.setDeposit_purpose(DepositPurpose.values()[r
					                               						.getInt("DEPOSIT_PURPOSE")-1]);
					deposit.setStr_deposit_purpose(DepositPurpose.values()[r
					                               						.getInt("DEPOSIT_PURPOSE")-1].getLabel());
					deposit.setDeposit_date(r.getString("DEPOSIT_DATE_"));
				}else{
					deposit.setStr_deposit_purpose(DepositPurpose.values()[r
					                               						.getInt("DEPOSIT_PURPOSE")-9].getLabel());
					deposit.setDeposit_purpose(DepositPurpose.values()[r
					                               						.getInt("DEPOSIT_PURPOSE")-9]);
					deposit.setStr_deposit_purpose(DepositPurpose.values()[r
					                               						.getInt("DEPOSIT_PURPOSE")-9].getLabel());
					deposit.setDeposit_date(r.getString("DEPOSIT_DATE_"));
				}
			
				depositList.add(deposit);
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

		return depositList;
	}

	public DepositDTO getDepositDetail(String deposit_id) {
		DepositDTO deposit = null;
		Connection conn = ConnectionManager.getConnection();
		String sql = "SELECT tmp3.*, account_name "
				+ "  FROM (SELECT tmp2.*, branch_name "
				+ "          FROM (SELECT tmp1.*, bank.bank_name "
				+ "                  FROM (SELECT deposit_id, customer_id, deposit_type, "
				+ "                               deposit_purpose, "
				+ "                               TO_CHAR (valid_from, "
				+ "                                        'dd-MM-YYYY' "
				+ "                                       ) valid_from, "
				+ "                               TO_CHAR (valid_to, "
				+ "                                        'dd-MM-YYYY' "
				+ "                                       ) valid_to, "
				+ "                               bank_id, branch_id, ACCOUNT_NO, "
				+ "                               TO_CHAR (deposit_date, "
				+ "                                        'dd-MM-YYYY' "
				+ "                                       ) deposit_date, "
				+ "                               total_deposit "
				+ "                          FROM mst_deposit "
				+ "                         WHERE deposit_id = ?) tmp1 "
				+ "                       LEFT OUTER JOIN "
				+ "                       mst_bank_info bank ON tmp1.bank_id = bank.bank_id "
				+ "                       ) tmp2 "
				+ "               LEFT OUTER JOIN "
				+ "               mst_branch_info branch ON tmp2.branch_id = branch.branch_id "
				+ "               ) tmp3 "
				+ "       LEFT OUTER JOIN "
				+ "       mst_account_info ACCOUNT ON tmp3.ACCOUNT_no = ACCOUNT.account_no ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, deposit_id);
			r = stmt.executeQuery();
			while (r.next()) {
				
				
				deposit = new DepositDTO();
				deposit.setDeposit_id(r.getString("DEPOSIT_ID"));
				deposit.setDeposit_type(DepositType.values()[r
						.getInt("DEPOSIT_TYPE")]);
				deposit.setStr_deposit_type(DepositType.values()[r
						.getInt("DEPOSIT_TYPE")].getLabel());
				deposit.setStr_deposit_purpose(DepositPurpose.values()[r
						.getInt("DEPOSIT_PURPOSE")].getLabel());
				
				deposit.setDeposit_purpose(DepositPurpose.values()[r
						.getInt("DEPOSIT_PURPOSE")]);
				deposit.setTotal_deposit(r.getString("TOTAL_DEPOSIT"));

				deposit.setCustomer_id(r.getString("CUSTOMER_ID"));
				deposit.setValid_from(r.getString("VALID_FROM"));
				deposit.setValid_to(r.getString("VALID_TO"));
				deposit.setBank(r.getString("BANK_ID"));
				deposit.setBank_name(r.getString("BANK_NAME"));
				deposit.setBranch(r.getString("BRANCH_ID"));
				deposit.setBranch_name(r.getString("BRANCH_NAME"));
				deposit.setAccount_no(r.getString("ACCOUNT_NO"));
				deposit.setAccount_name(r.getString("ACCOUNT_NAME") == null ? r
						.getString("ACCOUNT_NO") : r.getString("ACCOUNT_NAME"));
				deposit.setDeposit_date(r.getString("DEPOSIT_DATE"));

			}
			//QUERY HAS BEEN CHANGED
			sql = "  SELECT MST_DEPOSIT_TYPES.*, MST_DEPOSIT.TOTAL_DEPOSIT AMOUNT " +
					"    FROM MST_DEPOSIT_TYPES " +
					"         LEFT OUTER JOIN MST_DEPOSIT " +
					"            ON     MST_DEPOSIT_TYPES.TYPE_ID = MST_DEPOSIT.DEPOSIT_PURPOSE " +
					"               AND MST_DEPOSIT_TYPES.STATUS = 1 " +
					"               AND DEPOSIT_ID = ? " +
					"ORDER BY VIEW_ORDER " ;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, deposit_id);
			r = stmt.executeQuery();
			ArrayList<DepositDtlDTO> detailList = new ArrayList<DepositDtlDTO>();
			DepositDtlDTO detail;
			while (r.next()) {
				detail = new DepositDtlDTO();
				detail.setType_id(r.getString("TYPE_ID"));
				detail.setType_name_eng(r.getString("TYPE_NAME_ENG"));
				detail.setAmount(r.getDouble("AMOUNT"));
				detailList.add(detail);

			}
			deposit.setDepositDetail(detailList);
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

		return deposit;
	}

	public ArrayList<DepositDTO> getCandidatesForBankGuaranteeExpire(int index,
			int offset, String whereClause, String sortFieldName,
			String sortOrder, int total) {
		DepositDTO deposit = null;
		ArrayList<DepositDTO> expireList = new ArrayList<DepositDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "";
		String orderByQuery = "";
		String days="90";
		if (whereClause.contains("customer_name")
				|| sortFieldName.contains("customer_name")) {
			whereClause = whereClause.replace("customer_name", "FULL_NAME");
			sortFieldName = sortFieldName.replace("customer_name", "FULL_NAME");
		}
		if(whereClause==""){
			whereClause="(valid_to - TRUNC (SYSDATE))<="+days;
		}
		if (sortFieldName != null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery = " ORDER BY " + sortFieldName + " " + sortOrder + " ";
		if (total == 0)
			sql = "Select deposit.deposit_id,deposit.customer_id,CUSTOMER_CATEGORY,CATEGORY_NAME,FULL_NAME ,bank.bank_id,branch.branch_id,account.account_no,bank_name,branch_name,account_name,total_deposit,to_char(valid_to,'dd-MM-YYYY') valid_to,valid_to -trunc(sysdate) expire_in "
					+ " From MST_DEPOSIT deposit,CUSTOMER_PERSONAL_INFO CUSTOMER,MST_BANK_INFO bank,MST_BRANCH_INFO branch,MST_ACCOUNT_INFO account,CUSTOMER CUS,MST_CUSTOMER_CATEGORY MCC"
					+ " Where bank.bank_id=deposit.bank_id And deposit.customer_id=CUSTOMER.CUSTOMER_ID"
					+ " And branch.branch_id=deposit.branch_id "
					+ " And account.account_no=deposit.account_no "
					+ " AND CUS.CUSTOMER_ID=deposit.customer_id "
					+ " AND MCC.CATEGORY_ID=CUS.CUSTOMER_CATEGORY"
					+ " And Deposit_Type=1" 
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")")) + " " + orderByQuery;
		else
			sql = " Select * from ( "
					+ " Select rownum serial,tmp1.* from "
					+ " ( Select deposit.deposit_id,deposit.customer_id,CUSTOMER_CATEGORY,CATEGORY_NAME,FULL_NAME ,bank.bank_id,branch.branch_id,account.account_no,bank_name,branch_name,account_name,total_deposit,to_char(valid_to,'dd-MM-YYYY') valid_to,valid_to -trunc(sysdate) expire_in "
					+ " From MST_DEPOSIT deposit,CUSTOMER_PERSONAL_INFO CUSTOMER,MST_BANK_INFO bank,MST_BRANCH_INFO branch,MST_ACCOUNT_INFO account,CUSTOMER CUS,MST_CUSTOMER_CATEGORY MCC "
					+ " Where bank.bank_id=deposit.bank_id And deposit.customer_id=CUSTOMER.CUSTOMER_ID"
					+ " And branch.branch_id=deposit.branch_id "
					+ " And account.account_no=deposit.account_no "
					+ " AND CUS.CUSTOMER_ID=deposit.customer_id "
					+ " AND MCC.CATEGORY_ID=CUS.CUSTOMER_CATEGORY"
					+ " And Deposit_Type=1"
					+ (whereClause.equalsIgnoreCase("") ? "" : (" And ( "
							+ whereClause + ")")) + " " + orderByQuery
					+ "    )tmp1 " + "    )tmp2   "
					+ "  Where serial Between ? and ? ";

		PreparedStatement stmt = null;
		ResultSet r = null;
		CustomerDTO cusDTO = new CustomerDTO();
		try {
			stmt = conn.prepareStatement(sql);
			if (total != 0) {
				stmt.setInt(1, index);
				stmt.setInt(2, index + offset);
			}
			r = stmt.executeQuery();
			while (r.next()) {
				deposit = new DepositDTO();

				deposit.setDeposit_id(r.getString("DEPOSIT_ID"));
				deposit.setCustomer_id(r.getString("CUSTOMER_ID"));
				deposit.setCustomer_name(r.getString("FULL_NAME"));
				deposit.setCustomer_category(r.getString("CUSTOMER_CATEGORY"));
				deposit.setCustomer_category_name(r.getString("CATEGORY_NAME"));
				deposit.setBank(r.getString("BANK_ID"));
				deposit.setBranch(r.getString("BRANCH_ID"));
				deposit.setAccount_no(r.getString("ACCOUNT_NO"));
				deposit.setBank_name(r.getString("BANK_NAME"));
				deposit.setBranch_name(r.getString("BRANCH_NAME"));
				deposit.setAccount_name(r.getString("ACCOUNT_NAME"));
				deposit.setTotal_deposit(r.getString("TOTAL_DEPOSIT"));
				deposit.setValid_to(r.getString("VALID_TO"));
				deposit.setExpire_in(r.getString("EXPIRE_IN"));

				expireList.add(deposit);
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

		return expireList;
	}

	public String getNextId(String data) {
		Connection conn = ConnectionManager.getConnection();
		String sql = " select lpad(max(to_number(TYPE_ID))+1,2,'0') ID from MST_DEPOSIT_TYPES";
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
	
	public ResponseDTO saveGankGarantieExpireExtentionInfo(DepositDTO bgChange)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		

		String sqlInsert=" Insert into BG_EXPIRE_CHANGE_HISTORY(PID,DEPOSIT_ID, CUSTOMER_ID, OLD_EXPIRE_DATE, NEW_EXPIRE_DATE, ENTRY_DATE,REMARKS, INSERT_BY, INSERT_DATE) " +
				 		  " Values(SQN_BG_EXPIRE.NEXTVAL,?, ?, TO_DATE(?, 'DD-MM-YYYY'), TO_DATE(?, 'DD-MM-YYYY'), TO_DATE(?, 'DD-MM-YYYY'),?,?,TO_DATE(sysdate, 'DD-MM-YYYY'))";
		

		String sqlUpdate="Update MST_DEPOSIT set VALID_TO=TO_DATE(?, 'DD-MM-YYYY'),STATUS=? WHERE deposit_id=?";
		
		

		PreparedStatement stmt = null;
			try
			{
				

				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,bgChange.getDeposit_id());
				stmt.setString(2,bgChange.getCustomer_id());
				stmt.setString(3,bgChange.getOld_expire_date());
				stmt.setString(4,bgChange.getNew_expire_date());
				stmt.setString(5,bgChange.getEntry_date());
				stmt.setString(6,bgChange.getRemarks_on_bg());
				stmt.setString(7,bgChange.getInserted_by());					
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlUpdate);
				stmt.setString(1,bgChange.getNew_expire_date());
				stmt.setString(2,"Extended");
				stmt.setString(3,bgChange.getDeposit_id());
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Saved Bank Garantie Expire Date Extension Information.");
				response.setResponse(true);
				
				String cKey="CUSTOMER_INFO_"+bgChange.getCustomer_id();
				CacheUtil.clear(cKey);

			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return response;
	}
	public ArrayList<DepositDTO> getBGExpireChangHistory(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		DepositDTO expireChangeDTO=null;
		ArrayList<DepositDTO> expireChangeList=new ArrayList<DepositDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT PID,DEPOSIT_ID,BG_EXPIRE_CHANGE_HISTORY.CUSTOMER_ID,TO_CHAR (OLD_EXPIRE_DATE, 'DD-MM-YYYY')  OLD_EXPIRE_DATE," +
				  		" TO_CHAR (OLD_EXPIRE_DATE, 'DD-MM-YYYY')  OLD_EXPIRE_DATE, " +
				  		" TO_CHAR (NEW_EXPIRE_DATE, 'DD-MM-YYYY')  NEW_EXPIRE_DATE, " +
				  		" TO_CHAR (ENTRY_DATE, 'DD-MM-YYYY')  ENTRY_DATE,REMARKS,INSERT_BY,INSERT_DATE " +
						" FROM BG_EXPIRE_CHANGE_HISTORY,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER"+
						" WHERE" +	
						" CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=BG_EXPIRE_CHANGE_HISTORY.CUSTOMER_ID "+
						" AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
						(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (  SELECT PID,DEPOSIT_ID,BG_EXPIRE_CHANGE_HISTORY.CUSTOMER_ID,TO_CHAR (OLD_EXPIRE_DATE, 'DD-MM-YYYY')  OLD_EXPIRE_DATE," +
				  		" TO_CHAR (NEW_EXPIRE_DATE, 'DD-MM-YYYY')  NEW_EXPIRE_DATE, " +
				  		" TO_CHAR (ENTRY_DATE, 'DD-MM-YYYY')  ENTRY_DATE,REMARKS,INSERT_BY,INSERT_DATE " +
						" FROM BG_EXPIRE_CHANGE_HISTORY,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER"+
						" WHERE" +	
						" CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=BG_EXPIRE_CHANGE_HISTORY.CUSTOMER_ID "+
						" AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
						(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+
				  	  " )tmp1 " +
				  	  " )tmp2   " +
				  	  " Where serial Between ? and ? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(total!=0)
				{
				stmt.setInt(1, index);
				stmt.setInt(2, index+offset);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					expireChangeDTO=new DepositDTO();
					expireChangeDTO.setpId(r.getString("PID"));
					expireChangeDTO.setDeposit_id(r.getString("DEPOSIT_ID"));
					expireChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					expireChangeDTO.setOld_expire_date(r.getString("OLD_EXPIRE_DATE"));
					expireChangeDTO.setNew_expire_date(r.getString("NEW_EXPIRE_DATE"));
					expireChangeDTO.setEntry_date(r.getString("ENTRY_DATE"));
					expireChangeDTO.setRemarks_on_bg(r.getString("REMARKS"));
					expireChangeDTO.setInserted_by(r.getString("INSERT_BY"));
					
					expireChangeList.add(expireChangeDTO);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return expireChangeList;
	}
	public ResponseDTO deleteBankGarantieExpireChangeInfo(String pId)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlBGChangeInfo="select PID,DEPOSIT_ID,OLD_EXPIRE_DATE from BG_EXPIRE_CHANGE_HISTORY Where PID=?";						  
		String sqlCountBgChangInfo="select COUNT(PID) from BG_EXPIRE_CHANGE_HISTORY Where PID=?";
		String sqlDeleteBgChangInfo="Delete BG_EXPIRE_CHANGE_HISTORY Where PID=?";
		String sqlUpdate=" Update MST_DEPOSIT set VALID_TO=TO_DATE(?, 'DD-MM-YYYY') WHERE  DEPOSIT_ID=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlBGChangeInfo);
				stmt.setString(1,pId);				
				ResultSet r =stmt.executeQuery();
				String deposit_id="";
				String old_expire_date="";
				String old_rent="";
				
				if (r.next())
				{
					deposit_id=r.getString("DEPOSIT_ID");
					old_expire_date=r.getString("OLD_EXPIRE_DATE");
				}
				
			
				stmt = conn.prepareStatement(sqlDeleteBgChangInfo);
				stmt.setString(1,pId);
				stmt.execute();
				
			
				stmt = conn.prepareStatement(sqlUpdate);
				stmt.setString(1,old_expire_date);
				stmt.setString(2,pId);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Deleted Meter Rent Change Information.");
				response.setResponse(true);				
			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return response;
	 	
	}
	
}

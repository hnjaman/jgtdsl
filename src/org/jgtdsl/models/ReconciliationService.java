package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.BankBookDTO;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.DepositDtlDTO;
import org.jgtdsl.dto.ReconciliationDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.reports.BankBookRevenue;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class ReconciliationService {
	
	

	public static ArrayList<ReconciliationDTO> getReconciliationCauseList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		ReconciliationDTO cause=null;
		ArrayList<ReconciliationDTO> causeList=new ArrayList<ReconciliationDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		
		if(whereClause.contains("status") & !whereClause.contains(".status"))
			whereClause=whereClause.replace("status", "bank.status");
		
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select CAUSE_ID,CAUSE_NAME from RECONCILIATION_CAUSE  "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery;
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select CAUSE_ID,CAUSE_NAME from RECONCILIATION_CAUSE "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	  "    )tmp1 " +
				  	  "    )tmp2   " +
				  	  "  Where serial Between ? and ? ";
		   
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
					cause=new ReconciliationDTO();
					cause.setCause_id(r.getString("CAUSE_ID"));
					cause.setCause_name(r.getString("CAUSE_NAME"));
					causeList.add(cause);
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		return causeList;
	}
	public ArrayList<ReconciliationDTO> getReconciliationCauseList()
	{
		return getReconciliationCauseList(0, 0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}
	
	
	public static ResponseDTO deleteReconsilation(String bank_id,String branch_id,String account_no,String collection_month,String collection_year)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		int count=0;
		String sql_MST_RECONCILIATION=" delete  MST_RECONCILIATION where " +
				" BANK_ID='"+bank_id+"'"+
				" and BRANCH_ID='"+branch_id+"'"+
				" and ACCOUNT_NO='"+account_no+"'"+
				" and Month='"+collection_month+"'"+
				" and Year='"+collection_year+"'";
		
		String sql_DTL_RECONCILIATION=" delete  DTL_RECONCILIATION where PID= ( Select PID from MST_RECONCILIATION where " +
				" BANK_ID='"+bank_id+"'"+
				" and BRANCH_ID='"+branch_id+"'"+
				" and ACCOUNT_NO='"+account_no+"'"+
				" and Month='"+collection_month+"'"+
				" and Year='"+collection_year+"')";

		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
			
				stmt = conn.prepareStatement(sql_DTL_RECONCILIATION);
				stmt.executeUpdate();
				stmt = conn.prepareStatement(sql_MST_RECONCILIATION);
				stmt.executeUpdate();
				transactionManager.commit();
				
			} 
			catch (Exception e){e.printStackTrace();
			
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	       
			
			response.setMessasge("<font color='green'>Successfully Deleted Reconsilation Information.</font>");
			response.setResponse(true);	
			return response;

	}

	
	
	
	public static ResponseDTO saveReconcilationInfo(String bank_id,String branch_id,String account_no,String collection_month,String collection_year,String add_comments,String add_amount,String lessComment,String lessAmount,String addAccount,String lessAccount,String balance_bank_statement,String opening_balance )
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String sqlUpdate="";
		OracleCallableStatement stmt = null;
		int response_code=0;

		
		String[] addCommentsArr;
		if(add_comments.equalsIgnoreCase(""))
			addCommentsArr=new String[0];
		else 
			addCommentsArr=add_comments.split("#ifti#");
		
		String[] addAmountArr;
		if(add_amount.equalsIgnoreCase(""))
			addAmountArr=new String[0];
		else 
			addAmountArr=add_amount.split("#ifti#");
		
		String[] addAccountsArr;
		if(addAccount.equalsIgnoreCase(""))
			addAccountsArr=new String[0];
		else 
			addAccountsArr=addAccount.split("#ifti#");
		
		String[] lessCommentsArr;
		if(lessComment.equalsIgnoreCase(""))
			lessCommentsArr=new String[0];
		else 
			lessCommentsArr=lessComment.split("#ifti#");

		String[] lessAmountArr;
		if(lessAmount.equalsIgnoreCase(""))
			lessAmountArr=new String[0];
		else 
			lessAmountArr=lessAmount.split("#ifti#");
		
		
		String[] lessAccountsArr;
		if(lessAccount.equalsIgnoreCase(""))
			lessAccountsArr=new String[0];
		else 
			lessAccountsArr=lessAccount.split("#ifti#");
	
		String mst_recon_sql=" Insert Into MST_RECONCILIATION(PID,BANK_ID,BRANCH_ID,ACCOUNT_NO,MONTH,YEAR,OPENING_BALANCE,CLOSING_BALANCE) " +
	 		     " Values(?,?,?,?,?,?,?,?)";
		
		String dtl_recon_sql=" Insert Into DTL_RECONCILIATION(PID,TYPE,PARTICULARS,AC_NO,AMOUNT) " +
			  	 " Values(?,?,?,?,?) ";
		
		PreparedStatement mst_stmt = null;
		PreparedStatement dtl_stmt = null;
	
		
	
		ResultSet r = null;
		String pid=null;
			try
			{
				
				
				
				mst_stmt = conn.prepareStatement("Select SQN_RECONCILIATION.nextval pid from dual");
				r = mst_stmt.executeQuery();
				if (r.next())
					pid=r.getString("pid"); 
				
				
				
				mst_stmt=conn.prepareStatement(mst_recon_sql);
				mst_stmt.setString(1,pid );
				mst_stmt.setString(2,bank_id );
				mst_stmt.setString(3, branch_id);
				mst_stmt.setString(4,account_no );
				mst_stmt.setString(5, collection_month);
				mst_stmt.setString(6, collection_year);
				mst_stmt.setString(7, opening_balance);
				mst_stmt.setString(8, balance_bank_statement);
				
				mst_stmt.executeQuery();
				
				dtl_stmt = conn.prepareStatement(dtl_recon_sql);
				
				for(int i=0;i<addCommentsArr.length;i++)
				{
					dtl_stmt.setString(1,  pid);
					dtl_stmt.setString(2,  "add");
					dtl_stmt.setString(3, addCommentsArr[i]);
					dtl_stmt.setString(4, addAccountsArr[i]);
					dtl_stmt.setString(5, addAmountArr[i]);
					dtl_stmt.addBatch();	
				}
				
				for(int i=0;i<lessCommentsArr.length;i++)
				{
					dtl_stmt.setString(1,  pid);
					dtl_stmt.setString(2,  "less");
					dtl_stmt.setString(3, lessCommentsArr[i]);
					dtl_stmt.setString(4, lessAccountsArr[i]);
					dtl_stmt.setString(5, lessAmountArr[i]);
					dtl_stmt.addBatch();	
				}
			
				dtl_stmt.executeBatch();
			
				

	            
	 
                transactionManager.commit();
			    response.setMessasge("Successfully saved Reconciliation information.");
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
	 		finally{try{dtl_stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}dtl_stmt = null;conn = null;}

	 		return response;
	}
	
	public static ArrayList<BankDTO> getAllBankList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		BankDTO bank=null;
		ArrayList<BankDTO> bankList=new ArrayList<BankDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		
		if(whereClause.contains("status") & !whereClause.contains(".status"))
			whereClause=whereClause.replace("status", "bank.status");
		
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select bank.* from MST_BANK_INFO bank    "+(whereClause.equalsIgnoreCase("")?"":("  Where  "+whereClause+" "))+" "+orderByQuery;
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select bank.* from MST_BANK_INFO bank "+(whereClause.equalsIgnoreCase("")?"":(" Where  "+whereClause+" "))+" "+orderByQuery+			  	  
				  	  "    )tmp1 " +
				  	  "    )tmp2   " +
				  	  "  Where serial Between ? and ? ";
		   
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
					bank=new BankDTO();
					bank.setBank_id(r.getString("BANK_ID"));
					bank.setBank_name(r.getString("BANK_NAME"));
					bank.setAddress(r.getString("ADDRESS"));
					bank.setPhone(r.getString("PHONE"));
					bank.setFax(r.getString("FAX"));
					bank.setEmail(r.getString("EMAIL"));
					bank.setUrl(r.getString("URL"));
					bank.setDescription(r.getString("DESCRIPTION"));
					bank.setStatus(r.getInt("STATUS"));
					bankList.add(bank);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		return bankList;
	}

	
	
	public static String isReconiliatedOrNot(String bank_id,String branch_id,String account_no,String collection_month,String collection_year)
	{
		
		Connection conn = ConnectionManager.getConnection();
		int count=0;
		String sql=" select count(*) count from MST_RECONCILIATION where " +
				" BANK_ID='"+bank_id+"'"+
				" and BRANCH_ID='"+branch_id+"'"+
				" and ACCOUNT_NO='"+account_no+"'"+
				" and Month='"+collection_month+"'"+
				" and Year='"+collection_year+"'";
		int response=0;
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					count=r.getInt("count");
				}
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(count==1)
	 		return "yes";
	 	else
	 		return "no";

	}

	
	
	public static double getBankBookClosingBalance(String bank_id,String branch_id,String account_no,String collection_month,String collection_year){
		

		ArrayList<BankBookDTO> bankBookListDebit=new ArrayList<BankBookDTO>();
		ArrayList<BankBookDTO> bankBookListCredit=new ArrayList<BankBookDTO>();
		  
		  bankBookListCredit=getCreditList(bank_id,branch_id,account_no,collection_month,collection_year);
		  
		  int creditListSize=bankBookListCredit.size();
		  bankBookListDebit=getDebitList(bank_id,branch_id,account_no,collection_month,collection_year);
		  double totalRevenue=0.0;
		  double totalTransferAmount=0.0;
		  
		  int debitListSize=bankBookListDebit.size();
		  

		  
		  double bankBookOpebibgBalance=getBankBookOpeningBalance(bank_id,branch_id,account_no,collection_month,collection_year);
		  
		  for (int i = 0; i < debitListSize; i++) {
		   totalRevenue=totalRevenue+bankBookListDebit.get(i).getTotal_revenue();
		   
		  }
		  for (int i = 0; i < creditListSize; i++) {
		   totalTransferAmount=totalTransferAmount+bankBookListCredit.get(i).getCredit();
		  }
		  double debitTotal=bankBookOpebibgBalance+totalRevenue;
		  double closingBalance=debitTotal-totalTransferAmount;
		  double total=closingBalance+totalTransferAmount;
		  
		  return closingBalance;
		  
		 }
	
	private static ArrayList<BankBookDTO> getDebitList(String bank_id,String branch_id,String account_no,String collection_month,String collection_year)
	{
	ArrayList<BankBookDTO> bankBookDebitList=new ArrayList<BankBookDTO>();
	Connection conn = ConnectionManager.getConnection();	
		try {
			
			String wClause=" And to_char(TRANS_DATE,'mm')="+collection_month+" and to_char(TRANS_DATE,'YYYY')="+collection_year;
			String wClause2=" And to_char(COLLECTION_DATE,'mm')="+collection_month+" and to_char(COLLECTION_DATE,'YYYY')="+collection_year;
			
		
			
			
			String transaction_sql="  SELECT TO_CHAR (TRANS_DATE, 'dd-mm-yyyy') TRANS_DATE, " +
					"         PARTICULARS, " +
					"         DEBIT, " +
					"         METER_RENT, " +
					"         SURCHARGE, " +
					"         interest, " +
					"         Miscellaneous, " +
					"         Transfer_Amount, " +
					"         HHV_NHV_BILL " +
					"    FROM (SELECT TRANS_DATE, " +
					"                 PARTICULARS, " +
					"                 DEBIT, " +
					"                 METER_RENT, " +
					"                 SURCHARGE, " +
					"                 interest, " +
					"                 Miscellaneous, " +
					"                 Transfer_Amount, " +
					"                 HHV_NHV_BILL " +
					"            FROM (  SELECT PARTICULARS, " +
					"                           MAX (TRANS_DATE) TRANS_DATE, " +
					"                           SUM (DEBIT) DEBIT, " +
					"                           SUM (METER_RENT) METER_RENT, " +
					"                           SUM (SURCHARGE) SURCHARGE, " +
					"                           SUM (INTEREST) INTEREST, " +
					"                           SUM (MISCELLANEOUS) MISCELLANEOUS, " +
					"                           SUM (TRANSFER_AMOUNT) TRANSFER_AMOUNT, " +
					"                           SUM (HHV_NHV_BILL) HHV_NHV_BILL " +
					"                      FROM (SELECT TRANS_DATE, " +
					"                                   DECODE ( " +
					"                                      cat, " +
					"                                      '01', 'Accounts Receivable Domestic Pvt.', " +
					"                                      '02', 'Accounts Receivable Domestic Govt.', " +
					"                                      '03', 'Accounts Receivable Commercial Pvt.', " +
					"                                      '04', 'Accounts Receivable Commercial Govt.', " +
					"                                      '05', 'Accounts Receivable Industrial Pvt.', " +
					"                                      '06', 'Accounts Receivable Industrial Govt.', " +
					"                                      '07', 'Accounts Receivable Captive Pvt.', " +
					"                                      '08', 'Accounts Receivable Captive Govt.', " +
					"                                      '09', 'Accounts Receivable C.N.G Pvt.', " +
					"                                      '10', 'Accounts Receivable C.N.G Govt.', " +
					"                                      '11', 'Accounts Receivable Power Pvt.', " +
					"                                      '12', 'Accounts Receivable Power Govt.') " +
					"                                      PARTICULARS, " +
					"                                   DEBIT, " +
					"                                   METER_RENT, " +
					"                                   SURCHARGE, " +
					"                                   NULL interest, " +
					"                                   NULL Miscellaneous, " +
					"                                   NULL Transfer_Amount, " +
					"                                   HHV_NHV_BILL " +
					"                              FROM (SELECT TRANS_DATE,cat,DEBIT,METER_RENT,SURCHARGE,CATEGORY,HHV_NHV_BILL FROM ( " +
					"                                SELECT TRANS_DATE,SUBSTR (CUSTOMER_ID, 3, 2) cat,ACCOUNT_NO,SUM (DEBIT) DEBIT,SUM (METER_RENT) METER_RENT,SUM (SURCHARGE) SURCHARGE " +
					"                                                FROM BANK_ACCOUNT_LEDGER " +
					"                                               WHERE     TRANS_TYPE = 1 " +
					"                                                     AND CUSTOMER_ID IS NOT NULL " +
					"                                                     AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                                            GROUP BY TRANS_DATE, " +
					"                                                     SUBSTR (CUSTOMER_ID, 3, 2),ACCOUNT_NO) " +
					"                                           tab1, " +
					"                                           (SELECT NVL (SUM (HHV_NHV_BILL), 0) " +
					"                                                        HHV_NHV_BILL, " +
					"                                                     SUBSTR (CUSTOMER_ID, 3, 2) " +
					"                                                        CATEGORY,ACCOUNT_NO,COLLECTION_DATE " +
					"                                                FROM SUMMARY_MARGIN_PB pb, " +
					"                                                     BILL_COLLECTION_METERED bcm " +
					"                                               WHERE     pb.bill_id = BCM.BILL_ID " + wClause2+
					"                                                     AND ACCOUNT_NO='"+account_no+"' " +
					"                                            GROUP BY SUBSTR (CUSTOMER_ID, 3, 2),ACCOUNT_NO,COLLECTION_DATE) " +
					"                                           tab2 " +
					"                                     WHERE tab1.cat = tab2.CATEGORY(+) " +
					"                                     and tab1.ACCOUNT_NO=tab2.ACCOUNT_NO(+) " +
					"                                     and tab1.TRANS_DATE=tab2.COLLECTION_DATE(+))) " +
					"                  GROUP BY PARTICULARS) " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE, " +
					"                 PARTICULARS, " +
					"                 DEBIT, " +
					"                 METER_RENT, " +
					"                 SURCHARGE, " +
					"                 NULL interest, " +
					"                 NULL Miscellaneous, " +
					"                 NULL Transfer_Amount, " +
					"                 NULL HHV_NHV_BILL " +
					"            FROM (  SELECT TRANS_DATE, " +
					"                           PARTICULARS, " +
					"                           SUM (DEBIT) DEBIT, " +
					"                           SUM (METER_RENT) METER_RENT, " +
					"                           SUM (SURCHARGE) SURCHARGE " +
					"                      FROM BANK_ACCOUNT_LEDGER " +
					"                     WHERE     TRANS_TYPE = 4 " +
					"                           AND DEBIT > 0 " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                  GROUP BY TRANS_DATE, PARTICULARS) " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE, " +
					"                 PARTICULARS, " +
					"                 DEBIT, " +
					"                 METER_RENT, " +
					"                 SURCHARGE, " +
					"                 DEBIT interest, " +
					"                 NULL Miscellaneous, " +
					"                 NULL Transfer_Amount, " +
					"                 NULL HHV_NHV_BILL " +
					"            FROM (  SELECT TRANS_DATE, " +
					"                           PARTICULARS, " +
					"                           SUM (DEBIT) DEBIT, " +
					"                           SUM (METER_RENT) METER_RENT, " +
					"                           SUM (SURCHARGE) SURCHARGE " +
					"                      FROM BANK_ACCOUNT_LEDGER " +
					"                     WHERE     TRANS_TYPE = 5 " +
					"                           AND DEBIT > 0 " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                  GROUP BY TRANS_DATE, PARTICULARS) " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE, " +
					"                 PARTICULARS, " +
					"                 DEBIT, " +
					"                 METER_RENT, " +
					"                 SURCHARGE, " +
					"                 DEBIT interest, " +
					"                 NULL Miscellaneous, " +
					"                 NULL Transfer_Amount, " +
					"                 NULL HHV_NHV_BILL " +
					"            FROM (  SELECT TRANS_DATE, " +
					"                           PARTICULARS, " +
					"                           SUM (DEBIT) DEBIT, " +
					"                           SUM (METER_RENT) METER_RENT, " +
					"                           SUM (SURCHARGE) SURCHARGE " +
					"                      FROM BANK_ACCOUNT_LEDGER " +
					"                     WHERE     TRANS_TYPE = 3 " +
					"                           AND DEBIT > 0 " +
					"                           AND PARTICULARS NOT LIKE '%Refund%' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                  GROUP BY TRANS_DATE, PARTICULARS) " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE, " +
					"                 PARTICULARS, " +
					"                 DEBIT, " +
					"                 METER_RENT, " +
					"                 SURCHARGE, " +
					"                 NULL interest, " +
					"                 DEBIT Miscellaneous, " +
					"                 NULL Transfer_Amount, " +
					"                 NULL HHV_NHV_BILL " +
					"            FROM (  SELECT TRANS_DATE, " +
					"                           PARTICULARS, " +
					"                           SUM (DEBIT) DEBIT, " +
					"                           SUM (METER_RENT) METER_RENT, " +
					"                           SUM (SURCHARGE) SURCHARGE " +
					"                      FROM BANK_ACCOUNT_LEDGER " +
					"                     WHERE     TRANS_TYPE = 3 " +
					"                           AND DEBIT > 0 " +
					"                           AND PARTICULARS LIKE '%Refund%' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                  GROUP BY TRANS_DATE, PARTICULARS) " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE, " +
					"                 PARTICULARS, " +
					"                 DEBIT DEBIT, " +
					"                 METER_RENT, " +
					"                 SURCHARGE, " +
					"                 NULL interest, " +
					"                 NULL Miscellaneous, " +
					"                 NULL Transfer_Amount, " +
					"                 NULL HHV_NHV_BILL " +
					"            FROM (  SELECT TRANS_DATE, " +
					"                           PARTICULARS, " +
					"                           SUM (DEBIT) DEBIT, " +
					"                           SUM (METER_RENT) METER_RENT, " +
					"                           SUM (SURCHARGE) SURCHARGE " +
					"                      FROM BANK_ACCOUNT_LEDGER " +
					"                     WHERE     TRANS_TYPE = 7 " +
					"                           AND DEBIT > 0 " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                  GROUP BY TRANS_DATE, PARTICULARS)) " +
					"ORDER BY Transfer_Amount ASC, TRANS_DATE, PARTICULARS " ;
					
					
					
					
					
					
					
					
					
					
					
					
					
//					"SELECT TO_CHAR (TRANS_DATE, 'dd-mm-yyyy') TRANS_DATE, PARTICULARS, DEBIT, METER_RENT, SURCHARGE, interest, Miscellaneous, Transfer_Amount "+
//					"    FROM ( select TRANS_DATE,PARTICULARS, DEBIT, METER_RENT, SURCHARGE, interest, Miscellaneous, Transfer_Amount " +
//					"	 FROM ( select PARTICULARS,max(TRANS_DATE) TRANS_DATE, sum(DEBIT) DEBIT, sum(METER_RENT) METER_RENT,sum(SURCHARGE) SURCHARGE, sum(INTEREST) INTEREST, sum(MISCELLANEOUS) MISCELLANEOUS,sum(TRANSFER_AMOUNT) TRANSFER_AMOUNT "+
//					"	 FROM (SELECT TRANS_DATE, DECODE (cat, '01', 'Accounts Receivable Domestic Pvt.', "+
//					"                         				   '02', 'Accounts Receivable Domestic Govt.', "+
//					"                         				   '03', 'Accounts Receivable Commercial Pvt.', "+
//					"                         				   '04', 'Accounts Receivable Commercial Govt.', "+
//					"                         				   '05', 'Accounts Receivable Industrial Pvt.', "+
//					"                         				   '06', 'Accounts Receivable Industrial Govt.', "+
//					"                         				   '07', 'Accounts Receivable Captive Pvt.', "+
//					"                         				   '08', 'Accounts Receivable Captive Govt.', "+
//					"                         				   '09', 'Accounts Receivable C.N.G Pvt.', "+
//					"                        				   '10', 'Accounts Receivable C.N.G Govt.', "+
//					"                        				   '11', 'Accounts Receivable Power Pvt.', "+
//					"                        				   '12', 'Accounts Receivable Power Govt.') "+
//					"                 PARTICULARS, "+
//					"                 DEBIT, "+
//					"                 METER_RENT, "+
//					"                 SURCHARGE, "+
//					"                 NULL interest, "+
//					"                 NULL Miscellaneous, "+
//					"                 NULL Transfer_Amount "+
//					"            FROM (  SELECT TRANS_DATE, "+
//					"                           SUBSTR (CUSTOMER_ID, 3, 2) cat, "+
//					"                           SUM (DEBIT) DEBIT, " +
//					"                           SUM (METER_RENT) METER_RENT, " +
//					"                           SUM (SURCHARGE) SURCHARGE " +
//					"                      FROM BANK_ACCOUNT_LEDGER " +
//					"                     WHERE     TRANS_TYPE = 1 " +
//					"                           AND CUSTOMER_ID IS NOT NULL " +
//					"                           AND ACCOUNT_NO = '"+account_no+"' " +
//					"                           AND TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
//					"                           AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
//					"                  GROUP BY TRANS_DATE, SUBSTR (CUSTOMER_ID, 3, 2)) " +
//					"                  )group by PARTICULARS) " +
//					"          UNION ALL " +
//					"          SELECT TRANS_DATE, " +
//					"                 PARTICULARS, " +
//					"                 DEBIT, "+
//					"                 METER_RENT, " +
//					"                 SURCHARGE, " +
//					"                 NULL interest, " +
//					"                 NULL Miscellaneous, " +
//					"                 NULL Transfer_Amount " +
//					"            FROM (  SELECT TRANS_DATE, " +
//					"                           PARTICULARS, " +
//					"                           SUM (DEBIT) DEBIT, " +
//					"                           SUM (METER_RENT) METER_RENT, " +
//					"                           SUM (SURCHARGE) SURCHARGE " +
//					"                      FROM BANK_ACCOUNT_LEDGER " +
//					"                     WHERE     TRANS_TYPE = 4 " +
//					"                           AND DEBIT > 0 " +
//					"                           AND ACCOUNT_NO = '"+account_no+"' " +
//					"                           AND TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
//					"                           AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
//					"                  GROUP BY TRANS_DATE, PARTICULARS) " +
//					"          UNION ALL " +
//					"          SELECT TRANS_DATE, " +
//					"                 PARTICULARS, " +
//					"                 DEBIT, " +
//					"                 METER_RENT, " +
//					"                 SURCHARGE, " +
//					"                 DEBIT interest, " +
//					"                 NULL Miscellaneous, " +
//					"                 NULL Transfer_Amount " +
//					"            FROM (  SELECT TRANS_DATE, " +
//					"                           PARTICULARS, " +
//					"                           SUM (DEBIT) DEBIT, " +
//					"                           SUM (METER_RENT) METER_RENT, " +
//					"                           SUM (SURCHARGE) SURCHARGE " +
//					"                      FROM BANK_ACCOUNT_LEDGER " +
//					"                     WHERE     TRANS_TYPE = 5 " +
//					"                           AND DEBIT > 0 " +
//					"                           AND ACCOUNT_NO = '"+account_no+"' " +
//					"                           AND TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
//					"                           AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
//					"                  GROUP BY TRANS_DATE, PARTICULARS)) " +
//					"ORDER BY Transfer_Amount ASC, TRANS_DATE, PARTICULARS ";

			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BankBookDTO bankDto = new BankBookDTO();
   
        		bankDto.setTrans_date(resultSet.getString("TRANS_DATE"));
        		bankDto.setParticular(resultSet.getString("PARTICULARS"));
        		bankDto.setTotal_revenue(resultSet.getDouble("DEBIT"));
        		bankDto.setMeter_rent(resultSet.getDouble("METER_RENT"));
        		bankDto.setSuecharge(resultSet.getDouble("SURCHARGE"));
        		bankDto.setInterest(resultSet.getDouble("interest"));
        		bankDto.setMiscellaneous(resultSet.getDouble("Miscellaneous"));
        		bankDto.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		bankDto.setCredit(resultSet.getDouble("Transfer_Amount"));
        		
   
        		
        		bankBookDebitList.add(bankDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bankBookDebitList;
	}
	
	private static ArrayList<BankBookDTO> getCreditList(String bank_id,String branch_id,String account_no,String collection_month,String collection_year)
	{
		ArrayList<BankBookDTO> bankBookCreditList=new ArrayList<BankBookDTO>();
		Connection conn = ConnectionManager.getConnection();
		
		try {
			
			
			
		
			
			
			String transaction_sql="  SELECT TO_CHAR(TRANS_DATE,'dd-mm-yyyy')TRANS_DATE,PARTICULARS,DEBIT,ACTUAL_REVENUE,METER_RENT,SURCHARGE,interest,Miscellaneous,CREDIT "+
					"    FROM (SELECT TRANS_DATE,PARTICULARS,NULL DEBIT,ACTUAL_REVENUE,METER_RENT,SURCHARGE,NULL interest,NULL Miscellaneous,CREDIT "+
					"            FROM (  SELECT TRANS_DATE,PARTICULARS,SUM(CREDIT)CREDIT,SUM (ACTUAL_REVENUE)ACTUAL_REVENUE,SUM (METER_RENT) METER_RENT,SUM (SURCHARGE) SURCHARGE "+
					"				FROM BANK_ACCOUNT_LEDGER "+
					"                     WHERE     TRANS_TYPE IN (2, 4) "+
					"                           AND CREDIT > 0 "+
					"                           AND ACCOUNT_NO = '"+account_no+"' "+
					"                           and TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"							and TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                  GROUP BY TRANS_DATE, PARTICULARS)) "+
					"ORDER BY TRANS_DATE, PARTICULARS ";
			






			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BankBookDTO bankDto1 = new BankBookDTO();
   
        		bankDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		bankDto1.setParticular(resultSet.getString("PARTICULARS"));
        		bankDto1.setTotal_revenue(resultSet.getDouble("DEBIT"));
        		bankDto1.setActual_revenue(resultSet.getDouble("ACTUAL_REVENUE"));
        		bankDto1.setMeter_rent(resultSet.getDouble("METER_RENT"));
        		bankDto1.setSuecharge(resultSet.getDouble("SURCHARGE"));
        		bankDto1.setInterest(resultSet.getDouble("interest"));
        		bankDto1.setMiscellaneous(resultSet.getDouble("Miscellaneous"));
        		bankDto1.setCredit(resultSet.getDouble("CREDIT"));
        		
   
        		
        		bankBookCreditList.add(bankDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bankBookCreditList;
	}
	
	public static Double getBankBookOpeningBalance(String bank_id,String branch_id,String account_no,String collection_month,String collection_year)
	{
		Connection conn = ConnectionManager.getConnection();
		int closingMonth=Integer.valueOf(collection_month);
		int closingYear=Integer.valueOf(collection_year);
		
		
		int tempMonth=0;
		int tempYear=0;
		double openingBalance=0.0; 
		
		
		try {
			if(closingMonth==1){
				tempMonth=12;
				tempYear=closingYear-1;
			}else if(closingMonth>1){
				tempMonth=closingMonth-1;
				tempYear=closingYear;
			}
		
			String account_info_sql="select get_opening_balance ('"+bank_id+"','"+branch_id+"','"+account_no+"','"+tempMonth+"','"+tempYear+"') BALANCE from dual " ;
					
					
//					"select BALANCE from BANK_ACCOUNT_LEDGER WHERE TRANS_ID IN "+
//					"( " +
//					"select MAX(TRANS_ID) TRANS_ID from BANK_ACCOUNT_LEDGER where TRANS_DATE in " +
//					"(select  MAX(TRANS_DATE) FROM BANK_ACCOUNT_LEDGER " +
//					"WHERE  TO_CHAR (TRANS_DATE, 'MM') = "+tempMonth+" "+
//					"AND TO_CHAR (TRANS_DATE, 'YYYY') ="+tempYear+" " +
//					"and branch_id='"+branch_id+"' " +
//					"AND ACCOUNT_NO='"+account_no+"' " +
//					" And Status=1 ) " +
//					" and branch_id='"+branch_id+"' " +
//					"AND ACCOUNT_NO='"+account_no+"'" +
//					" And Status=1  " +
//					") ";




			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		openingBalance=resultSet.getDouble("BALANCE");
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return openingBalance;
	}
}
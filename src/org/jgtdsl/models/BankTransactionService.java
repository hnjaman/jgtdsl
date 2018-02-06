package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.BankDepositWithdrawDTO;
import org.jgtdsl.dto.BillingParamDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.TransactionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.BankAccountTransactionType;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class BankTransactionService {
	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	public synchronized ResponseDTO saveBankTransaction(BankDepositWithdrawDTO transaction)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();	
		
		String source_bank_sql="";
		String mater_trans_sql="Select SQN_BAM.nextval trans_id from dual";
		String bank_trans_sql="Select SQN_BAL.nextval trans_id from dual";
		String margin_month_year_sql="";
		
		
		String master_sql=" Insert into BANK_ACCOUNT_MASTER(TRANS_ID,TRANS_DATE, TRANS_TYPE, TRANS_MODE, TRANS_AMOUNT, SOURCE_TRANS_ID,TARGET_TRANS_ID) " +
		  				  " Values(?,TO_DATE(?, 'dd-MM-YYYY'),?,?,?,?,?)";
		
		if(BankAccountTransactionType.values()[Integer.parseInt(transaction.getTransaction_type_str())]==BankAccountTransactionType.TRANSFER ||(BankAccountTransactionType.values()[Integer.parseInt(transaction.getTransaction_type_str())]==BankAccountTransactionType.PAYMENT) || (BankAccountTransactionType.values()[Integer.parseInt(transaction.getTransaction_type_str())]==BankAccountTransactionType.MARGIN_PAYMENT))
		{
			 source_bank_sql=" Insert into BANK_ACCOUNT_LEDGER(TRANS_ID, TRANS_DATE, TRANS_TYPE, PARTICULARS, BANK_ID,BRANCH_ID, ACCOUNT_NO,CREDIT, " +
					   " REF_ID, INSERTED_BY, STATUS) " +
					   " Values(?, TO_DATE(?, 'dd-MM-YYYY'),?,?,?,?,?,?,?,?,0)";
		}else
		{
			 source_bank_sql=" Insert into BANK_ACCOUNT_LEDGER(TRANS_ID, TRANS_DATE, TRANS_TYPE, PARTICULARS, BANK_ID,BRANCH_ID, ACCOUNT_NO,DEBIT, " +
					   " REF_ID, INSERTED_BY, STATUS) " +
					   " Values(?, TO_DATE(?, 'dd-MM-YYYY'),?,?,?,?,?,?,?,?,0)";
		}
		
		if(BankAccountTransactionType.values()[Integer.parseInt(transaction.getTransaction_type_str())]==BankAccountTransactionType.MARGIN_PAYMENT)
		{
			margin_month_year_sql="Insert into MARGIN_ACCOUNT_PAYABLE_DTL(TRANS_ID,COLLECTION_MONTH,COLLECTION_YEAR) values(?,?,?)";
		}
		String target_bank_sql=" Insert into BANK_ACCOUNT_LEDGER(TRANS_ID, TRANS_DATE, TRANS_TYPE, PARTICULARS, BANK_ID,BRANCH_ID, ACCOUNT_NO,DEBIT, " +
		   " REF_ID, INSERTED_BY, STATUS) " +
		   " Values(?, TO_DATE(?, 'dd-MM-YYYY'),?,?,?,?,?,?,?,?,0)";
		
		
		String target_bank_info="select distinct(MBI.BANK_ID) BANK_ID ,MBI.BANK_NAME,mbri.BRANCH_ID,mbri.BRANCH_NAME,mai.ACCOUNT_NO,mai.ACCOUNT_TYPE,mai.ACCOUNT_NAME from MST_BANK_INFO mbi,MST_BRANCH_INFO mbri,MST_ACCOUNT_INFO mai"
							+" where MBI.BANK_ID=mbri.BANK_ID"
							+" and mbri.BRANCH_ID=mai.BRANCH_ID and mai.ACCOUNT_NO='"+transaction.getTarget_account_no()+"'";
		String source_bank_info="select distinct(MBI.BANK_ID) BANK_ID ,MBI.BANK_NAME,mbri.BRANCH_ID,mbri.BRANCH_NAME,mai.ACCOUNT_NO,mai.ACCOUNT_TYPE,mai.ACCOUNT_NAME from MST_BANK_INFO mbi,MST_BRANCH_INFO mbri,MST_ACCOUNT_INFO mai"
							+" where MBI.BANK_ID=mbri.BANK_ID"
							+" and mbri.BRANCH_ID=mai.BRANCH_ID and mai.ACCOUNT_NO='"+transaction.getSource_account_no()+"'";
		
		PreparedStatement trans_stmt = null;
		PreparedStatement master_stmt = null;
		PreparedStatement source_stmt = null;
		PreparedStatement target_stmt = null;
		PreparedStatement bank_stmt = null;
		PreparedStatement month_year_stmt = null;
		String master_transId="";
		String source_transId="";
		String target_transId="";
		String target_bank_name="";
		String target_branch_name="";
		String target_account_name="";
		String source_bank_name="";
		String source_branch_name="";
		String source_account_name="";
		
		ResultSet r = null;
			try
			{
				
				trans_stmt = conn.prepareStatement(mater_trans_sql);
				r = trans_stmt.executeQuery();
				if (r.next()){
					master_transId=r.getString("trans_id");
				}
					 
				
				trans_stmt = conn.prepareStatement(bank_trans_sql);				
				r = trans_stmt.executeQuery();
				if (r.next())
				{
					source_transId=r.getString("trans_id");
				}
					 
				
				bank_stmt =conn.prepareStatement(target_bank_info);
				r = bank_stmt.executeQuery();
				if (r.next()){
					target_bank_name=r.getString("BANK_NAME"); 
				    target_branch_name=r.getString("BRANCH_NAME");
				    target_account_name=r.getString("ACCOUNT_NAME");
				}
				    
				 bank_stmt =conn.prepareStatement(source_bank_info);
			     r = bank_stmt.executeQuery();
					if (r.next()){
						source_bank_name=r.getString("BANK_NAME"); 
					    source_branch_name=r.getString("BRANCH_NAME");
					    source_account_name=r.getString("ACCOUNT_NAME");    
					}
				
				
				
				//Source Bank Account Ledger Entry
				source_stmt= conn.prepareStatement(source_bank_sql);
				source_stmt.setString(1,source_transId);
				source_stmt.setString(2,transaction.getSource_transaction_date());
				source_stmt.setString(3,transaction.getTransaction_type_str());
				source_stmt.setString(4,BankAccountTransactionType.values()[Integer.parseInt(transaction.getTransaction_type_str())]==BankAccountTransactionType.TRANSFER?"AMOUNT TRANSFER TO  A/C NO. "+transaction.getTarget_account_no()+","+target_bank_name+", "+target_branch_name+"":transaction.getSource_transaction_particulars());
				source_stmt.setString(5,transaction.getSource_bank_id());				
				source_stmt.setString(6,transaction.getSource_branch_id());
				source_stmt.setString(7,transaction.getSource_account_no());
				source_stmt.setString(8,transaction.getTransaction_amount());
				source_stmt.setString(9,master_transId);	
				source_stmt.setString(10, transaction.getInserted_by());
				source_stmt.executeUpdate();

				//Target Bank Account Ledger Entry
				if(BankAccountTransactionType.values()[Integer.parseInt(transaction.getTransaction_type_str())]==BankAccountTransactionType.TRANSFER){
					
					trans_stmt = conn.prepareStatement(bank_trans_sql);				
					r = trans_stmt.executeQuery();
					if (r.next())
						target_transId=r.getString("trans_id"); 
										
					target_stmt= conn.prepareStatement(target_bank_sql);
					target_stmt.setString(1,target_transId);
					target_stmt.setString(2,transaction.getTarget_transaction_date());
					target_stmt.setString(3,transaction.getTransaction_type_str());
					target_stmt.setString(4,"AMOUNT RECEIVED FROM  A/C NO. "+transaction.getSource_account_no()+","+source_bank_name+" ,"+source_branch_name+"");
					target_stmt.setString(5,transaction.getTarget_bank_id());				
					target_stmt.setString(6,transaction.getTarget_branch_id());
					target_stmt.setString(7,transaction.getTarget_account_no());
					target_stmt.setString(8,transaction.getTransaction_amount());
					target_stmt.setString(9,master_transId);
					target_stmt.setString(10, transaction.getInserted_by());
					target_stmt.executeUpdate();
				}
				if(BankAccountTransactionType.values()[Integer.parseInt(transaction.getTransaction_type_str())]==BankAccountTransactionType.MARGIN_PAYMENT)
				{
					margin_month_year_sql="Insert into MARGIN_ACCOUNT_PAYABLE_DTL(TRANS_ID,COLLECTION_MONTH,COLLECTION_YEAR) values(?,?,?)";
					month_year_stmt=conn.prepareStatement(margin_month_year_sql);
					month_year_stmt.setString(1,source_transId);
					month_year_stmt.setString(2,transaction.getCollection_month());
					month_year_stmt.setString(3,transaction.getCollection_year());
					month_year_stmt.executeUpdate();
				}
				
				//Bank Account Master Entry
				master_stmt= conn.prepareStatement(master_sql);
				master_stmt.setString(1,master_transId);
				master_stmt.setString(2,transaction.getSource_transaction_date());
				master_stmt.setString(3,transaction.getTransaction_type_str());
				master_stmt.setString(4,"1");// transaction mode is no longer valided
				master_stmt.setString(5,transaction.getTransaction_amount());
				master_stmt.setString(6,source_transId);				
				master_stmt.setString(7,target_transId);
				master_stmt.executeUpdate();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully saved collection information.");
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
	 		finally{try{trans_stmt.close();source_stmt.close();master_stmt.close();transactionManager.close();
	 		if(target_stmt!=null)target_stmt.close();} catch (Exception e)
				{e.printStackTrace();}trans_stmt = null;source_stmt=null;target_stmt=null;master_stmt=null;conn = null;}

	 		return response;
	}
	
	public ArrayList<BankDepositWithdrawDTO> getBankTransactionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		BankDepositWithdrawDTO transaction=null;
		ArrayList<BankDepositWithdrawDTO> transactionList=new ArrayList<BankDepositWithdrawDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = "Select master.trans_id,to_char(MASTER.trans_date,'dd-MM-YYYY') trans_date,MASTER.trans_type,trans_mode, " +
						"LEDGER.PARTICULARS,LEDGER.BANK_ID,LEDGER.BRANCH_ID,LEDGER.ACCOUNT_NO,bank_name,branch_name,account_name, " +
						"LEDGER.DEBIT,LEDGER.CREDIT,ledger.STATUS " +
						"From BANK_ACCOUNT_LEDGER ledger,BANK_ACCOUNT_MASTER MASTER,MST_BANK_INFO bank, MST_BRANCH_INFO branch, MST_ACCOUNT_INFO account_info " +
						"Where " +
						"MASTER.SOURCE_TRANS_ID=+LEDGER.TRANS_ID " +
						"And ledger.bank_id=bank.bank_id " +
						"And ledger.branch_id=branch.branch_id " +
						"And ledger.account_no=account_info.account_no  "+(whereClause.equalsIgnoreCase("")?"":(" And  "+whereClause+" "))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select master.trans_id,to_char(MASTER.trans_date,'dd-MM-YYYY') trans_date,MASTER.trans_type,trans_mode, " +
					  " LEDGER.PARTICULARS,LEDGER.BANK_ID,LEDGER.BRANCH_ID,LEDGER.ACCOUNT_NO,bank_name,branch_name,account_name, " +
					  " LEDGER.DEBIT,LEDGER.CREDIT,ledger.STATUS " +
					  " From BANK_ACCOUNT_LEDGER ledger,BANK_ACCOUNT_MASTER MASTER,MST_BANK_INFO bank, MST_BRANCH_INFO branch, MST_ACCOUNT_INFO account_info " +
					  " Where " +
					  " MASTER.SOURCE_TRANS_ID=+LEDGER.TRANS_ID " +
					  " And ledger.bank_id=bank.bank_id " +
					  " And ledger.branch_id=branch.branch_id " +
					  " And ledger.account_no=account_info.account_no   "+(whereClause.equalsIgnoreCase("")?"":(" And  "+whereClause+" "))+" "+orderByQuery+			  	  
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
					transaction=new BankDepositWithdrawDTO();
					
					transaction.setTrans_id(r.getString("TRANS_ID"));
					transaction.setTrans_date(r.getString("TRANS_DATE"));
					transaction.setTransaction_type_name(BankAccountTransactionType.values()[r.getInt("TRANS_TYPE")].getLabel());
					transaction.setTransaction_mode_name(BankAccountTransactionType.values()[r.getInt("TRANS_MODE")].getLabel());
					transaction.setSource_transaction_particulars(r.getString("PARTICULARS"));
					transaction.setSource_bank_id(r.getString("BANK_ID"));
					transaction.setSource_branch_id(r.getString("BRANCH_ID"));
					transaction.setSource_account_no(r.getString("ACCOUNT_NO"));
					transaction.setSource_bank_name(r.getString("BANK_NAME"));
					transaction.setSource_branch_name(r.getString("BRANCH_NAME"));
					transaction.setSource_account_name(r.getString("ACCOUNT_NAME"));
					transaction.setDebit(r.getString("DEBIT"));
					transaction.setCredit(r.getString("CREDIT"));
					transaction.setStatus(r.getString("STATUS"));
					transactionList.add(transaction);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return transactionList;
	}
	

	public ArrayList<TransactionDTO> getUnAuthorizedTransactions(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		TransactionDTO transaction=null;
		ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql =" Select Trans_Id,To_Char(Trans_Date,'DD-MM-YYYY') Trans_Date_char,Trans_Date,PARTICULARS,Bank.BANK_ID,Branch.BRANCH_ID,BankAccount.ACCOUNT_NO,BANK_NAME,BRANCH_NAME,BankAccount.ACCOUNT_NAME, " +
				  " DEBIT,CREDIT,BALANCE,REF_ID,TO_CHAR(INSERTED_ON,'DD-MM-YYYY') INSERTED_ON,CUSTOMER_ID,TRANS_TYPE " +
				  " From BANK_ACCOUNT_LEDGER Ledger,MST_BANK_INFO Bank,MST_BRANCH_INFO Branch,MST_ACCOUNT_INFO BankAccount " +
				  " Where LEDGER.BANK_ID=BANK.BANK_ID " +
				  " And LEDGER.BRANCH_ID=BRANCH.BRANCH_ID " +
				  " And LEDGER.ACCOUNT_NO=BANKACCOUNT.ACCOUNT_NO " +
				  " AND LEDGER.BANK_ID=BANKACCOUNT.BANK_ID " +
				  " And Branch.Area_id="+loggedInUser.getArea_id()
				  +" and Ledger.Status=0 "+(whereClause.equalsIgnoreCase("")?"":(" And  "+whereClause+" "))+" "+orderByQuery;
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select Trans_Id,To_Char(Trans_Date,'DD-MM-YYYY') Trans_Date_char,Trans_Date,PARTICULARS,Bank.BANK_ID,Branch.BRANCH_ID,BankAccount.ACCOUNT_NO,BANK_NAME,BRANCH_NAME,BankAccount.ACCOUNT_NAME, " +
						  " DEBIT,CREDIT,BALANCE,REF_ID,TO_CHAR(INSERTED_ON,'DD-MM-YYYY') INSERTED_ON,CUSTOMER_ID,TRANS_TYPE " +
						  " From BANK_ACCOUNT_LEDGER Ledger,MST_BANK_INFO Bank,MST_BRANCH_INFO Branch,MST_ACCOUNT_INFO BankAccount " +
						  " Where LEDGER.BANK_ID=BANK.BANK_ID " +
						  " And LEDGER.BRANCH_ID=BRANCH.BRANCH_ID " +
						  " AND LEDGER.BANK_ID=BANKACCOUNT.BANK_ID " +
						  " And Branch.Area_id="+loggedInUser.getArea_id()+
						  " And LEDGER.ACCOUNT_NO=BANKACCOUNT.ACCOUNT_NO " +
					  "	 And Ledger.Status=0 "+(whereClause.equalsIgnoreCase("")?"":(" And  "+whereClause+" "))+" "+orderByQuery+			  	  
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
					transaction=new TransactionDTO();
					
					transaction.setTrans_id(r.getString("TRANS_ID"));
					transaction.setTrans_date(r.getString("Trans_Date_char"));
					transaction.setParticulars(r.getString("PARTICULARS"));
					transaction.setBank_id(r.getString("BANK_ID"));
					transaction.setBranch_id(r.getString("BRANCH_ID"));
					transaction.setAccount_no(r.getString("ACCOUNT_NO"));
					transaction.setBank_name(r.getString("BANK_NAME"));
					transaction.setBranch_name(r.getString("BRANCH_NAME"));
					transaction.setAccount_name(r.getString("ACCOUNT_NAME"));
					transaction.setDebit(r.getDouble("DEBIT"));
					transaction.setCredit(r.getDouble("CREDIT"));
					transaction.setRef_id(r.getString("REF_ID"));
					transaction.setInserted_on(r.getString("INSERTED_ON"));
					transaction.setCustomer_id(r.getString("CUSTOMER_ID"));
					transaction.setTrans_type(r.getString("TRANS_TYPE"));
					
					transactionList.add(transaction);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return transactionList;
	}
	
	public ArrayList<TransactionDTO> getAuthorizedTransactions(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		TransactionDTO transaction=null;
		ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql =" Select Trans_Id,To_Char(Trans_Date,'DD-MM-YYYY') Trans_Date,PARTICULARS,Bank.BANK_ID,Branch.BRANCH_ID,BankAccount.ACCOUNT_NO,BANK_NAME,BRANCH_NAME,BankAccount.ACCOUNT_NAME, " +
				  " DEBIT,CREDIT,BALANCE,REF_ID,TO_CHAR(INSERTED_ON,'DD-MM-YYYY') INSERTED_ON,CUSTOMER_ID,TRANS_TYPE " +
				  " From BANK_ACCOUNT_LEDGER Ledger,MST_BANK_INFO Bank,MST_BRANCH_INFO Branch,MST_ACCOUNT_INFO BankAccount " +
				  " Where LEDGER.BANK_ID=BANK.BANK_ID " +
				  " And LEDGER.BRANCH_ID=BRANCH.BRANCH_ID " +
				  " And LEDGER.ACCOUNT_NO=BANKACCOUNT.ACCOUNT_NO " +
				  " And Ledger.Status=1 "+(whereClause.equalsIgnoreCase("")?"":(" And  "+whereClause+" "))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select Trans_Id,To_Char(Trans_Date,'DD-MM-YYYY') Trans_Date,PARTICULARS,Bank.BANK_ID,Branch.BRANCH_ID,BankAccount.ACCOUNT_NO,BANK_NAME,BRANCH_NAME,BankAccount.ACCOUNT_NAME, " +
						  " DEBIT,CREDIT,BALANCE,REF_ID,TO_CHAR(INSERTED_ON,'DD-MM-YYYY') INSERTED_ON,CUSTOMER_ID,TRANS_TYPE " +
						  " From BANK_ACCOUNT_LEDGER Ledger,MST_BANK_INFO Bank,MST_BRANCH_INFO Branch,MST_ACCOUNT_INFO BankAccount " +
						  " Where LEDGER.BANK_ID=BANK.BANK_ID " +
						  " And LEDGER.BRANCH_ID=BRANCH.BRANCH_ID " +
						  " And LEDGER.ACCOUNT_NO=BANKACCOUNT.ACCOUNT_NO " +
					  "	 And Ledger.Status=1 "+(whereClause.equalsIgnoreCase("")?"":(" And  "+whereClause+" "))+" "+orderByQuery+			  	  
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
					transaction=new TransactionDTO();
					
					transaction.setTrans_id(r.getString("TRANS_ID"));
					transaction.setTrans_date(r.getString("TRANS_DATE"));
					transaction.setParticulars(r.getString("PARTICULARS"));
					transaction.setBank_id(r.getString("BANK_ID"));
					transaction.setBranch_id(r.getString("BRANCH_ID"));
					transaction.setAccount_no(r.getString("ACCOUNT_NO"));
					transaction.setBank_name(r.getString("BANK_NAME"));
					transaction.setBranch_name(r.getString("BRANCH_NAME"));
					transaction.setAccount_name(r.getString("ACCOUNT_NAME"));
					transaction.setDebit(r.getDouble("DEBIT"));
					transaction.setCredit(r.getDouble("CREDIT"));
					transaction.setBalance(r.getDouble("BALANCE"));
                    transaction.setRecon_cause("");
					transaction.setRef_id(r.getString("REF_ID"));
					transaction.setInserted_on(r.getString("INSERTED_ON"));
					transaction.setCustomer_id(r.getString("CUSTOMER_ID"));
					transaction.setTrans_type(r.getString("TRANS_TYPE"));
					
					transactionList.add(transaction);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return transactionList;
	}
	
	
	
	public ArrayList<TransactionDTO> getUnAuthCount(String area_id)
	{
		TransactionDTO transaction=null;
		ArrayList<TransactionDTO> unAuthList=new ArrayList<TransactionDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="Select bank.bank_id,branch.branch_id,account.account_no, bank.bank_name,branch.branch_name,account.account_name,unauth_count FROM   " +
		"(Select Max(Bank_Id),Max(Branch_Id),Max(Account_No) Account_No,Count(Account_No) UNAUTH_COUNT From  BANK_ACCOUNT_LEDGER Where Status=0  group by Bank_Id,Branch_id,Account_No ) " +
		"TMP1,MST_BANK_INFO bank,MST_BRANCH_INFO branch,MST_ACCOUNT_INFO account  " +
		"Where  area_id=? AND tmp1.account_no= account.account_no  " +
		"And bank.bank_id=account.bank_id  " +
		"And branch.branch_id=account.branch_id  " +
		"Order by bank.Bank_Id,branch.Branch_Id,Account.account_no ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, area_id);
				r = stmt.executeQuery();
				while (r.next())
				{
					transaction=new TransactionDTO();
					
					transaction.setBank_id(r.getString("BANK_ID"));
					transaction.setBank_name(r.getString("BANK_NAME"));
					transaction.setBranch_id(r.getString("BRANCH_ID"));
					transaction.setBranch_name(r.getString("BRANCH_NAME"));
					transaction.setAccount_no(r.getString("ACCOUNT_NO"));
					transaction.setAccount_name(r.getString("ACCOUNT_NAME"));
					transaction.setUnauth_count(r.getString("UNAUTH_COUNT"));
					unAuthList.add(transaction);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return unAuthList;
	}
	
	public ResponseDTO deleteBankTransaction(String transaction_id)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code=0;
		
		try {
       	 
   			//System.out.println("===>>Procedure : [DeleteBankTransaction] START");            
            stmt = (OracleCallableStatement) conn.prepareCall("{ call DeleteBankTransaction(?,?,?)  }");
            //System.out.println("==>>Procedure : [DeleteBankTransaction] END");
          
            
            
            stmt.setString(1, transaction_id); 
            stmt.registerOutParameter(2, java.sql.Types.INTEGER);
            stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
            
            stmt.executeUpdate();
            response_code = stmt.getInt(2);
            response.setMessasge(stmt.getString(3));
            if(response_code == 1){
            	response.setResponse(true);
            }
            else{
            	response.setResponse(false);
            }
		} 
		catch (Exception e){e.printStackTrace();
		response.setMessasge(e.getMessage());
		response.setResponse(false);
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
        
        return response;
        

	}
	
	public String getTotalDebitCredit(String whereClause)
	{
		String debitCreditStr="0#0";
		Connection conn = ConnectionManager.getConnection();
		String sql="Select NVL(sum(debit),0) total_debit,NVL(sum(Credit),0) total_credit From BANK_ACCOUNT_LEDGER ";
		if(!whereClause.equalsIgnoreCase("")){
			sql+=" Where "+whereClause;
		}
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{				
					debitCreditStr=r.getBigDecimal("total_debit")+"#"+r.getBigDecimal("total_credit");
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return debitCreditStr;
        

	}
		
	
	public Double getOpeningBalance(String bank_id,String branch_id,String account_no,String month,String year,String trans_type)
	{
		TransactionDTO transaction=null;
		ArrayList<TransactionDTO> unAuthList=new ArrayList<TransactionDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				//stmt = conn.prepareStatement(sql);
				//stmt.setString(1, );
				r = stmt.executeQuery();
				while (r.next())
				{
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		return 23125.30;
	}
	
	

}

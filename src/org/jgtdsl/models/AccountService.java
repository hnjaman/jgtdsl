package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class AccountService {
	
	public ArrayList<AccountDTO> getAccountList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		AccountDTO account=null;
		ArrayList<AccountDTO> accountList=new ArrayList<AccountDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY MST_ACCOUNT_INFO."+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from MST_ACCOUNT_INFO  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * From MST_ACCOUNT_INFO "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					account=new AccountDTO();
					account.setBank_id(r.getString("BANK_ID"));
					account.setBranch_id(r.getString("BRANCH_ID"));
					account.setAccount_no(r.getString("ACCOUNT_NO"));
					account.setAccount_type(r.getString("ACCOUNT_TYPE"));
					account.setAccount_name(r.getString("ACCOUNT_NAME"));
					account.setAc_opening_date(r.getString("AC_OPENING_DATE"));
					account.setOpening_balance(r.getString("OPENING_BALANCE"));
					account.setOpening_date(r.getString("OPENING_DATE"));
					account.setDescription(r.getString("DESCRIPTION"));
					account.setStatus(r.getInt("STATUS"));
					accountList.add(account);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return accountList;
	}
	
	
	public String addAccount(String data)
	{
		Gson gson = new Gson();  
		AccountDTO accountDTO = gson.fromJson(data, AccountDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Insert Into MST_ACCOUNT_INFO(ACCOUNT_NO,BANK_ID,BRANCH_ID,ACCOUNT_TYPE,ACCOUNT_NAME,AC_OPENING_DATE, " +
				"OPENING_BALANCE,OPENING_DATE,DESCRIPTION,STATUS) " +
				" Values(SQN_BANK_ACCOUNTID.NEXTVAL,?,?,?,?,to_date(?,'dd-MM-YYYY'),?,to_date(?,'dd-MM-YYYY'),?,?)";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,accountDTO.getBank_id());
				stmt.setString(2,accountDTO.getBranch_id());
				stmt.setString(3,accountDTO.getAccount_type());
				stmt.setString(4,accountDTO.getAccount_name());
				stmt.setString(5,accountDTO.getAc_opening_date());
				stmt.setString(6,accountDTO.getOpening_balance());
				stmt.setString(7,accountDTO.getOpening_date());
				stmt.setString(8,accountDTO.getDescription());
				stmt.setInt(9,accountDTO.getStatus());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX+AC.MST_ACCOUNT_INFO);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_CREATE_ERROR_PREFIX+AC.MST_ACCOUNT_INFO);

	}
	
public String updateAccount(String data)
{
	Gson gson = new Gson();  
	AccountDTO accountDTO = gson.fromJson(data, AccountDTO.class);  	
	Connection conn = ConnectionManager.getConnection();
	String sql=" Update MST_ACCOUNT_INFO Set BANK_ID=?,BRANCH_ID=?,ACCOUNT_TYPE=?,ACCOUNT_NAME=?,AC_OPENING_DATE=to_date(?,'dd-MM-YYYY'),OPENING_BALANCE=?,OPENING_DATE=to_date(?,'dd-MM-YYYY'),DESCRIPTION=?,STATUS=? Where ACCOUNT_NO=?";
	int response=0;
	PreparedStatement stmt = null;
		try
		{
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1,accountDTO.getBank_id());
			stmt.setString(2,accountDTO.getBranch_id());
			stmt.setString(3,accountDTO.getAccount_type());
			stmt.setString(4,accountDTO.getAccount_name());
			stmt.setString(5,accountDTO.getAc_opening_date());
			stmt.setString(6,accountDTO.getOpening_balance());
			stmt.setString(7,accountDTO.getOpening_date());
			stmt.setString(8,accountDTO.getDescription());
			stmt.setInt(9,accountDTO.getStatus());
			stmt.setString(10,accountDTO.getAccount_no());
			
			response = stmt.executeUpdate();
		} 
		catch (Exception e){e.printStackTrace();
		return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
 	if(response==1)
 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX+AC.MST_CUSTOMER_CATEGORY);
 	else
 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_UPDATE_ERROR_PREFIX+AC.MST_CUSTOMER_CATEGORY);

}	
public String deleteAccount(String accountId)
{
	 JSONParser jsonParser = new JSONParser();		
	 JSONObject jsonObject=null;
	 String aId=null;
	 try{
		 jsonObject= (JSONObject) jsonParser.parse(accountId);
	 }
	 catch(Exception ex){
		 ex.printStackTrace();
		 return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());			 
	 }
	aId=(String)jsonObject.get("id");
	Connection conn = ConnectionManager.getConnection();
	String sql=" Delete MST_ACCOUNT_INFO Where ACCOUNT_NO=?";
	int response=0;
	PreparedStatement stmt = null;
		try
		{
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,aId);
			response = stmt.executeUpdate();
		} 
		catch (Exception e){e.printStackTrace();
		return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		

 		if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX+AC.MST_ACCOUNT_INFO);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_DELETE_ERROR_PREFIX+AC.MST_ACCOUNT_INFO);

}

}

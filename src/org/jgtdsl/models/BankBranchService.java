package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class BankBranchService {

	public static ArrayList<BankDTO> getBankList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
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
		//for admin role who can see the list of all banks of all area ~Prince ~feb 17
		if(total==999999)
			     sql="  SELECT DISTINCT bank.bank_id, " +
			    		 "                  bank.AREA_ID, " +
			    		 "                  bank.AREA_ID ||' - '|| bank.bank_name  bank_name, " +
			    		 "                  bank.address, " +
			    		 "                  bank.phone, " +
			    		 "                  bank.fax, " +
			    		 "                  bank.email, " +
			    		 "                  bank.url, " +
			    		 "                  bank.description, " +
			    		 "                  bank.status " +
			    		 "    FROM MST_BANK_INFO bank, MST_BRANCH_INFO branch " +
			    		 "   WHERE bank.bank_id = branch.bank_id "+
			    		 (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+" )"))+
			    		 " "+orderByQuery ;
		//end of additional code
		else if(total==0)
				  sql = " Select distinct bank.bank_id,bank_name,bank.address,bank.phone," +
				  		"bank.fax,bank.email,bank.url,bank.description,bank.status " +
				  		"from MST_BANK_INFO bank,MST_BRANCH_INFO branch " +
				  		"Where bank.bank_id=branch.bank_id  "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+" )"))+" "+orderByQuery;
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select distinct bank.bank_id,bank_name,bank.address,bank.phone,bank.fax,bank.email,bank.url,bank.description,bank.status from MST_BANK_INFO bank,MST_BRANCH_INFO branch Where bank.bank_id=branch.bank_id  "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	  "    )tmp1 " +
				  	  "    )tmp2   " +
				  	  "  Where serial Between ? and ? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(total==999999 || total==0){
					//do nothing
				}
				else{
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
	public ArrayList<BankDTO> getBankList()
	{
		return getBankList(0, 0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
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

	public ArrayList<BankDTO> getAllBankList()
	{
		return getAllBankList(0, 0,Utils.EMPTY_STRING,"BANK_NAME","ASC",0);
	}
	
	public String addBank(String data)
	{
		Gson gson = new Gson();  
		BankDTO bankDTO = gson.fromJson(data, BankDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Insert Into MST_BANK_INFO Values(?,?,?,?,?,?,?,?,?)";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,bankDTO.getBank_id());
				stmt.setString(2,bankDTO.getBank_name());
				stmt.setString(3,bankDTO.getAddress());
				stmt.setString(4,bankDTO.getPhone());
				stmt.setString(5,bankDTO.getFax());
				stmt.setString(6,bankDTO.getEmail());
				stmt.setString(7,bankDTO.getUrl());
				stmt.setString(8,bankDTO.getDescription());
				stmt.setInt(9,bankDTO.getStatus());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX+AC.MST_CUSTOMER_CATEGORY);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_CREATE_ERROR_PREFIX+AC.MST_CUSTOMER_CATEGORY);

	}
	public String updateBank(String data)
	{
		Gson gson = new Gson();  
		BankDTO bankDTO = gson.fromJson(data, BankDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Update MST_BANK_INFO Set BANK_NAME=?,ADDRESS=?,PHONE=?,FAX=?,EMAIL=?,URL=?,DESCRIPTION=?,STATUS=? Where BANK_ID=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1,bankDTO.getBank_name());
				stmt.setString(2,bankDTO.getAddress());
				stmt.setString(3,bankDTO.getPhone());
				stmt.setString(4,bankDTO.getFax());
				stmt.setString(5,bankDTO.getEmail());
				stmt.setString(6,bankDTO.getUrl());
				stmt.setString(7,bankDTO.getDescription());
				stmt.setInt(8,bankDTO.getStatus());
				stmt.setString(9,bankDTO.getBank_id());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX+AC.MST_BANK);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_UPDATE_ERROR_PREFIX+AC.MST_BANK);

	}	
	public String deleteBank(String bankId)
	{
		 JSONParser jsonParser = new JSONParser();		
		 JSONObject jsonObject=null;
		 String bId=null;
		 try{
			 jsonObject= (JSONObject) jsonParser.parse(bankId);
		 }
		 catch(Exception ex){
			 ex.printStackTrace();
			 return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());			 
		 }
		bId=(String)jsonObject.get("id");
		Connection conn = ConnectionManager.getConnection();
		String sql=" Delete MST_BANK_INFO Where BANK_ID=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,bId);
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		

	 		if(response==1)
		 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX+AC.MST_BANK);
		 	else
		 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_DELETE_ERROR_PREFIX+AC.MST_BANK);

	}
	public ArrayList<BranchDTO> getBranchList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		BranchDTO branch=null;
		ArrayList<BranchDTO> branchList=new ArrayList<BranchDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY MST_BRANCH_INFO."+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from MST_BRANCH_INFO  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from MST_BRANCH_INFO  "+(whereClause.equalsIgnoreCase("")?"":(" Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					branch=new BranchDTO();
					branch.setArea_id(r.getString("AREA_ID"));
					branch.setBank_id(r.getString("BANK_ID"));
					branch.setBranch_id(r.getString("BRANCH_ID"));
					//branch.setBank_name(r.getString("BANK_NAME"));
					branch.setBranch_name(r.getString("BRANCH_NAME"));
					branch.setAddress(r.getString("ADDRESS"));
					branch.setCperson(r.getString("CPERSON"));
					branch.setPhone(r.getString("PHONE"));
					branch.setMobile(r.getString("MOBILE"));
					branch.setFax(r.getString("FAX"));
					branch.setEmail(r.getString("EMAIL"));
					branch.setDescription(r.getString("DESCRIPTION"));
					branch.setStatus(r.getInt("STATUS"));
					branchList.add(branch);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return branchList;
	}
	
	
	public String addBranch(String data)
	{
		Gson gson = new Gson();  
		BranchDTO branchDTO = gson.fromJson(data, BranchDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Insert Into MST_BRANCH_INFO(AREA_ID,BANK_ID,BRANCH_ID,BRANCH_NAME,ADDRESS,CPERSON,PHONE,MOBILE,FAX,EMAIL,DESCRIPTION,STATUS) " +
				" Values(?,?,?,?,?,?,?,?,?,?,?,?)";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,branchDTO.getArea_id());
				stmt.setString(2,branchDTO.getBank_id());
				stmt.setString(3,branchDTO.getBranch_id());
				stmt.setString(4,branchDTO.getBranch_name());
				stmt.setString(5,branchDTO.getAddress());
				stmt.setString(6,branchDTO.getCperson());
				stmt.setString(7,branchDTO.getPhone());
				stmt.setString(8,branchDTO.getMobile());
				stmt.setString(9,branchDTO.getFax());
				stmt.setString(10,branchDTO.getEmail());
				stmt.setString(11,branchDTO.getDescription());
				stmt.setInt(12,branchDTO.getStatus());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX+AC.MST_BRANCH);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_CREATE_ERROR_PREFIX+AC.MST_BRANCH);

	}
	
public String updateBranch(String data)
{
	Gson gson = new Gson();  
	BranchDTO branchDTO = gson.fromJson(data, BranchDTO.class);  	
	Connection conn = ConnectionManager.getConnection();
	String sql=" Update MST_BRANCH_INFO Set AREA_ID=?,BANK_ID=?,BRANCH_NAME=?,ADDRESS=?,CPERSON=?,PHONE=?,MOBILE=?,FAX=?,EMAIL=?,DESCRIPTION=?,STATUS=? Where BRANCH_ID=?";
	int response=0;
	PreparedStatement stmt = null;
		try
		{
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1,branchDTO.getArea_id());
			stmt.setString(2,branchDTO.getBank_id());
			stmt.setString(3,branchDTO.getBranch_name());
			stmt.setString(4,branchDTO.getAddress());
			stmt.setString(5,branchDTO.getCperson());
			stmt.setString(6,branchDTO.getPhone());
			stmt.setString(7,branchDTO.getMobile());
			stmt.setString(8,branchDTO.getFax());
			stmt.setString(9,branchDTO.getEmail());
			stmt.setString(10,branchDTO.getDescription());
			stmt.setInt(11,branchDTO.getStatus());
			stmt.setString(12,branchDTO.getBranch_id());
			
			response = stmt.executeUpdate();
		} 
		catch (Exception e){e.printStackTrace();
		return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
 	if(response==1)
 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX+AC.MST_BRANCH);
 	else
 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_UPDATE_ERROR_PREFIX+AC.MST_BRANCH);

}	
public String deleteBranch(String branchId)
{
	 JSONParser jsonParser = new JSONParser();		
	 JSONObject jsonObject=null;
	 String bId=null;
	 try{
		 jsonObject= (JSONObject) jsonParser.parse(branchId);
	 }
	 catch(Exception ex){
		 ex.printStackTrace();
		 return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());			 
	 }
	bId=(String)jsonObject.get("id");
	Connection conn = ConnectionManager.getConnection();
	String sql=" Delete MST_BRANCH_INFO Where BRANCH_ID=?";
	int response=0;
	PreparedStatement stmt = null;
		try
		{
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,bId);
			response = stmt.executeUpdate();
		} 
		catch (Exception e){e.printStackTrace();
		return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		

 		if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX+AC.MST_BRANCH);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_DELETE_ERROR_PREFIX+AC.MST_BRANCH);

}
public String getNextBankId(String data)
{  	
	Connection conn = ConnectionManager.getConnection();
	String sql=" select lpad(max(to_number(BANK_ID))+1,2,'0') ID from MST_BANK_INFO";
	String response="";
	PreparedStatement stmt = null;
	ResultSet r = null;
		
		try
		{
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next())
			{
				response=r.getString("ID");
			}
		} 
		catch (Exception e){e.printStackTrace();
		return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
 		return Utils.getJsonString(AC.STATUS_OK, response);
}
public String getNextBranchId(String data)
{  	
	Connection conn = ConnectionManager.getConnection();
	String sql=" select lpad(max(to_number(BRANCH_ID))+1,3,'0') ID from MST_BRANCH_INFO";
	String response="";
	PreparedStatement stmt = null;
	ResultSet r = null;
		
		try
		{
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next())
			{
				response=r.getString("ID");
			}
		} 
		catch (Exception e){e.printStackTrace();
		return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
 		return Utils.getJsonString(AC.STATUS_OK, response);

}
}
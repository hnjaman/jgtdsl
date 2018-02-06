package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;


import org.jgtdsl.dto.CustomerSmsDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.connection.ConnectionManager;

public class SMSService {
	
	
	
	
	
	
	public String saveSMSDefaulter(String areaId, String customerCategory, String billMonth, String billYear, String monthNumber, String dueDate)
	{
		
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		try {
			
			 stmt = (OracleCallableStatement) conn.prepareCall("{ call Save_Sms_Defaulter(?,?,?,?,?,?)  }");
	         
			 stmt.setString(1, areaId); 
			 stmt.setString(2, customerCategory); 
			 stmt.setString(3, billMonth); 
			 stmt.setString(4, billYear); 			
			 stmt.setString(5, monthNumber); 
			 stmt.setString(6, dueDate); 
			 
			 stmt.executeUpdate();
			 
			
		}
		catch (Exception e){e.printStackTrace();
			return "error";
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	
				
		return "success";
	}
	
	
	
	
	public  ArrayList<CustomerSmsDTO> getSMSDefaulter(String areaId, String customerCategory, String billMonth, String billYear)
	{
		CustomerSmsDTO cust=null;
		ArrayList<CustomerSmsDTO> custList=new ArrayList<CustomerSmsDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		
		sql = "SELECT  " +
				"	CUSTOMER_ID, CUSTOMER_NAME,   " +
				"   BILL_COUNT, TOTAL_AMOUNT, DUE_MONTHS,  " +
				"   DUE_DATE, TEXT_SMS, MOBILE_NO,  " +
				"   to_char(SEND_DATE,'DD-MM-YYYY') SEND_DATE, STATUS " +
				"	FROM TEMP_SMS " +
				"	where AREA_ID=?  " +
				"   and CUSTOMER_CATEGORY=? " +
				"   and BILL_MONTH=? and  BILL_YEAR=? " ;

		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, areaId);
				stmt.setString(2, customerCategory);
				stmt.setString(3, billMonth);
				stmt.setString(4, billYear);
				
				
				r = stmt.executeQuery();
				while (r.next())
				{
					cust=new CustomerSmsDTO();
					cust.setCustomerId(r.getString("CUSTOMER_ID"));
					cust.setCustomerName(r.getString("CUSTOMER_NAME"));
					cust.setBillcount(r.getString("BILL_COUNT"));
					cust.setTotalAmount(r.getString("TOTAL_AMOUNT"));
					cust.setDueMonths(r.getString("DUE_MONTHS"));
					cust.setDueDate(r.getString("DUE_DATE"));
					cust.setTextSMS(r.getString("TEXT_SMS"));
					cust.setMobileNo(r.getString("MOBILE_NO"));
					cust.setSendDate(r.getString("SEND_DATE"));
					cust.setStatus(r.getString("STATUS"));
					
					
					custList.add(cust);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		return custList;
	}
	
	public  ArrayList<CustomerSmsDTO> getSMSSendDefaulter(String areaId, String customerCategory, String billMonth, String billYear)
	{
		CustomerSmsDTO cust=null;
		ArrayList<CustomerSmsDTO> custList=new ArrayList<CustomerSmsDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		
		sql = "SELECT  " +
				"	CUSTOMER_ID, CUSTOMER_NAME,   " +
				"   BILL_COUNT, TOTAL_AMOUNT, DUE_MONTHS,  " +
				"   DUE_DATE, TEXT_SMS, MOBILE_NO,  " +
				"   to_char(SEND_DATE,'DD-MM-YYYY') SEND_DATE, STATUS " +
				"	FROM TEMP_SMS " +
				"	where AREA_ID=?  " +
				"   and CUSTOMER_CATEGORY=? " +
				"   and BILL_MONTH=? and  BILL_YEAR=? and STATUS='N'" ;

		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, areaId);
				stmt.setString(2, customerCategory);
				stmt.setString(3, billMonth);
				stmt.setString(4, billYear);
				
				
				r = stmt.executeQuery();
				while (r.next())
				{
					cust=new CustomerSmsDTO();
					cust.setCustomerId(r.getString("CUSTOMER_ID"));
					cust.setCustomerName(r.getString("CUSTOMER_NAME"));
					cust.setBillcount(r.getString("BILL_COUNT"));
					cust.setTotalAmount(r.getString("TOTAL_AMOUNT"));
					cust.setDueMonths(r.getString("DUE_MONTHS"));
					cust.setDueDate(r.getString("DUE_DATE"));
					cust.setTextSMS(r.getString("TEXT_SMS"));
					cust.setMobileNo(r.getString("MOBILE_NO"));
					cust.setSendDate(r.getString("SEND_DATE"));
					cust.setStatus(r.getString("STATUS"));
					
					
					custList.add(cust);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		return custList;
	}
	
	
	
	
	static PreparedStatement stmtASUpdate = null;
	static String sqlASUpdate = "update TEMP_SMS set status='Y', SEND_DATE=sysdate where CUSTOMER_ID=? and BILL_MONTH=? and BILL_YEAR=?";
	
	public static synchronized int sentCustSMS(String customerID,String billMonth, String billYear)
    {
		Connection conn = ConnectionManager.getConnection();
		int tmp = 0;
           try {
	            
	            if(stmtASUpdate==null)
	            	stmtASUpdate = ConnectionManager.getConnection().prepareStatement(sqlASUpdate);
	            int parameterIndex = 1;	            
	            stmtASUpdate.setString(parameterIndex++, customerID);
	            stmtASUpdate.setString(parameterIndex++, billMonth);
	            stmtASUpdate.setString(parameterIndex++, billYear);

	            tmp = stmtASUpdate.executeUpdate();
	            stmtASUpdate.clearParameters();
	            

	          } catch (SQLException e) {
	            System.out.println(e.toString());
	            stmtASUpdate = null;
	            ConnectionManager.closeConnection(conn);
	        }	
	          return tmp;
    }
	
	public ResponseDTO getCountSMS(String areaId, String customerCategory, String billMonth, String billYear)
	{

		Connection conn = ConnectionManager.getConnection();
		String totalCount="";
		String sql="";
		ResponseDTO response=new ResponseDTO();
		sql = "select count(CUSTOMER_ID) TOTALCTN from TEMP_SMS where AREA_ID=? AND CUSTOMER_CATEGORY=? AND BILL_MONTH=? AND BILL_YEAR=? AND STATUS='Y'" ;

		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, areaId);
				stmt.setString(2, customerCategory);
				stmt.setString(3, billMonth);
				stmt.setString(4, billYear);
				
				
				r = stmt.executeQuery();
				while (r.next())
				{
					
					totalCount="<font size='6'> Total Sent : <font>"+r.getString("TOTALCTN");
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
			response.setMessasge(totalCount);
			
		return response;
	}
	
	public ResponseDTO getCountCustomer(String areaId, String customerCategory, String billMonth, String billYear)
	{

		Connection conn = ConnectionManager.getConnection();
		String totalCount="";
		String sql="";
		ResponseDTO response=new ResponseDTO();
		sql = "select count(CUSTOMER_ID) TOTALCTN from TEMP_SMS where AREA_ID=? AND CUSTOMER_CATEGORY=? AND BILL_MONTH=? AND BILL_YEAR=? AND STATUS='N'" ;

		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, areaId);
				stmt.setString(2, customerCategory);
				stmt.setString(3, billMonth);
				stmt.setString(4, billYear);
				
				
				r = stmt.executeQuery();
				while (r.next())
				{
					
					totalCount="<font size='6'>Total Customers : <font>"+r.getString("TOTALCTN");
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
			response.setMessasge(totalCount);
			
		return response;
	}
	

}

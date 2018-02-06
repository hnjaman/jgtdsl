package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.jgtdsl.dto.InstallmentAgreementDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.connection.ConnectionManager;

public class SecurityAdjustmentService {


	public String getSecurityDepositBalance(String customerId)
	{
		String query="Select total_security_amount, total_debit-total_credit-total_security_amount Other_Deposit From  " +
		"( " +
		"Select * From " +
		"(Select sum(debit) total_debit From CUSTOMER_SECURITY_LEDGER Where Customer_Id=?)tmp1, " +
		"(Select nvl(sum(credit),0) total_credit From CUSTOMER_SECURITY_LEDGER Where Customer_Id=?)tmp2, " +
		"(Select sum(security_amount) total_security_amount From CUSTOMER_SECURITY_LEDGER Where Customer_Id=?)tmp3 " +
		") " ;
		
		String balanceJson="";
		Connection conn = ConnectionManager.getConnection();
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(query);
				stmt.setString(1, customerId);
				stmt.setString(2, customerId);
				stmt.setString(3, customerId);
				r = stmt.executeQuery();
				if (r.next())
				{
					balanceJson="{\"security\":"+r.getString("total_security_amount")+",\"others\":"+r.getString("Other_Deposit")+"}";
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return balanceJson;
	}
	
	public ResponseDTO saveSecurityAdjustment(String customerId,double securityAmount,double OtherAmount,String adjustmentMode,double totalAdjustableAmount,String comment,
			String collectionDate,String refundOther,String refundBank,String refundBranch,String refundAccount,
			String deductOther,String deductBank,String deductBranch,String deductAccount,
			String addOther,String addBank,String addBranch,String addAccount,String adjustmentBillStr,String loggedInUser)
	{
		
		Connection conn = ConnectionManager.getConnection();
	 	OracleCallableStatement stmt=null;
	 	int response_code=0;
	 	String response_msg="";
	 	ResponseDTO response=new ResponseDTO();
		
		String[] billArr;
		
		if(adjustmentBillStr.equalsIgnoreCase(""))
			billArr=new String[0];
		else 
			billArr=adjustmentBillStr.split("@");
		
		String[] billIdArr =new String[billArr.length];
		double[] adjustmentAmountArr = new double[billArr.length];
		double[] surchargeAmountArr =new double[billArr.length];
		int[] billTypeArr =new int[billArr.length];
		
		for(int i=0; i<billArr.length; i++)
		{
			String[] parts=billArr[i].split("#");
			billTypeArr[i] = Integer.parseInt(parts[0]);
			billIdArr[i] = parts[1];
			adjustmentAmountArr[i] =Double.parseDouble(parts[2]);
			try{
			surchargeAmountArr[i] = Double.parseDouble(parts[3]);
			}
			catch(Exception ex){
				surchargeAmountArr[i] = 0;
			}
			
		}		
		
		 try	
		  {
	    	ArrayDescriptor arrString = new ArrayDescriptor("VARCHARARRAY", conn);
	    	ArrayDescriptor arrNumber = new ArrayDescriptor("NUMBERARRAY", conn);
			
		
			ARRAY inputBillId=new ARRAY(arrString,conn,billIdArr);	
	    	ARRAY inputAdjustAmount=new ARRAY(arrNumber,conn,adjustmentAmountArr);
	    	ARRAY inputSurchargeAmount=new ARRAY(arrNumber,conn,surchargeAmountArr);
	    	ARRAY inputBillType=new ARRAY(arrNumber,conn,billTypeArr);

			
			//System.out.println("Procedure Save_Security_Adjustment Begins");
			stmt = (OracleCallableStatement) conn.prepareCall(
					 	  "{ call Save_Security_Adjustment(?,?,?,?,?,?,?, " +
					 	  " ?,?,?,?,?,?,?,?," +
					 	  " ?,?,?,?, " +
					 	  " ?,?,?,?,?, " +
					 	  " ?,?) }");
			   
			stmt.setString(1, customerId);
			stmt.setDouble(2, securityAmount);
			stmt.setDouble(3, OtherAmount);
			stmt.setString(4, adjustmentMode);
			stmt.setDouble(5, totalAdjustableAmount);			
			stmt.setString(6, comment);
			stmt.setString(7, collectionDate);
			
			stmt.setString(8, refundOther);
			stmt.setString(9, refundBank);
			stmt.setString(10, refundBranch);
			stmt.setString(11, refundAccount);
			stmt.setString(12, deductOther);
			stmt.setString(13, deductBank);
			stmt.setString(14, deductBranch);
			stmt.setString(15, deductAccount);
			
			stmt.setString(16, addOther);
			stmt.setString(17, addBank);
			stmt.setString(18, addBranch);
			stmt.setString(19, addAccount);
			
			stmt.setArray(20, inputBillId);			
			stmt.setArray(21, inputAdjustAmount);
			stmt.setArray(22, inputSurchargeAmount);
			stmt.setArray(23, inputBillType);
			stmt.setString(24, loggedInUser);
			
			stmt.registerOutParameter(25, java.sql.Types.INTEGER);
			stmt.registerOutParameter(26, java.sql.Types.VARCHAR);
			
    			stmt.executeUpdate();
 			response_code = stmt.getInt(25);
			response_msg = (stmt.getString(26)).trim();

			response.setMessasge(response_msg);
			response.setResponse(response_code==1?true:false);
		  }
	    catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());return response;}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
	 		
		 return response;	
	
	}
	
	
}

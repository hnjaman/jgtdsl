package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.SurchargeDTO;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.TransactionManager;

public class SurchargeService {

	public ArrayList<SurchargeDTO> getSurchargeList(String surcharge_bills){
		ArrayList<SurchargeDTO> surchargeList=new  ArrayList<SurchargeDTO>();
		SurchargeDTO surcharge=null;
		String[] sBills=surcharge_bills.split("@");
		for(int i=0;i<sBills.length;i++){
			
			String[] surcharge_bill=sBills[i].split("#");
			surcharge=new SurchargeDTO();
			surcharge.setCustomer_id(surcharge_bill[0]);
			surcharge.setPay_date(surcharge_bill[1]);
			surcharge.setSurcharge_rate(surcharge_bill[2]);
			surcharge.setBill_id(surcharge_bill[3]);
			surcharge.setSurcharge_amount(surcharge_bill[4]);
			surchargeList.add(surcharge);
		}
				
		return surchargeList;
	}
	public ResponseDTO updateSurchargeInfo(ArrayList<SurchargeDTO> surchargeList,String pay_date)
	{	
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		float surcharge_amount=0;
		float oldPbMargin_amount=0;
		float oldPayableAmount_amount=0;
		float newPbMargin_amount=0;
		float newPayableAmount_amount=0;
		int resp_code=0;
		String resp_msg=Utils.EMPTY_STRING;
		ResultSet r = null;
		
		String sql_OldSurchargeAmount="Select SURCHARGE_AMOUNT,TOTAL_AMOUNT FROM SUMMARY_MARGIN_PB wHERE Bill_Id=?";
		String sql_OldPayableAmount="Select PAYABLE_AMOUNT FROM BILL_METERED wHERE Bill_Id=?";
		
		String sql_margin=" UPdate SUMMARY_MARGIN_PB set SURCHARGE_PERCENTAGE=?, SURCHARGE_AMOUNT=?, TOTAL_AMOUNT=? Where BILL_ID=?";
		String sql_bill=" Update BILL_METERED set PAYABLE_AMOUNT=?,Amount_In_Word=NUMBER_SPELLOUT_FUNC(?), " +
				"Note='Surcharge Added',SURCHARGE_ISSUE_DATE=to_date(?,'dd-MM-YYYY'),SURCHARGE_AMOUNT=?  Where BILL_ID=? ";
					
		PreparedStatement stmt_margin = null;		
		PreparedStatement stmt_bill = null;
		OracleCallableStatement cstmt_backup = null;
		PreparedStatement stmt_oldSurchargeAmount = null;
		PreparedStatement stmt_oldPayableAmount = null;
		
			try
			{
				stmt_oldSurchargeAmount = conn.prepareStatement(sql_OldSurchargeAmount);
				stmt_oldPayableAmount = conn.prepareStatement(sql_OldPayableAmount);
				
				
				stmt_margin = conn.prepareStatement(sql_margin);
				stmt_bill = conn.prepareStatement(sql_bill);
				cstmt_backup = (OracleCallableStatement) conn.prepareCall("{ call BackUpMeteredBill(?,?,?)  }");
				String bill_id="";
				int count=0;
				for (SurchargeDTO surcharge : surchargeList) {
					/*if(count==0){*/
						stmt_oldSurchargeAmount.setString(1,surcharge.getBill_id());
						r = stmt_oldSurchargeAmount.executeQuery();
						if (r.next())
							surcharge_amount=r.getFloat("SURCHARGE_AMOUNT");
						    oldPbMargin_amount=r.getFloat("TOTAL_AMOUNT");
						
						    
						stmt_oldPayableAmount.setString(1,surcharge.getBill_id());
					    r = stmt_oldPayableAmount.executeQuery();
						if (r.next())
						        oldPayableAmount_amount=r.getFloat("PAYABLE_AMOUNT");    
					/*}
					count++;*/
					Float actual_surcharge= Float.valueOf(surcharge.getSurcharge_amount())-surcharge_amount;
					newPbMargin_amount=oldPbMargin_amount+actual_surcharge;
					stmt_margin.setFloat(1,Float.valueOf(surcharge.getSurcharge_rate()));
					stmt_margin.setFloat(2,  Float.valueOf(surcharge.getSurcharge_amount()));
					stmt_margin.setDouble(3, newPbMargin_amount);
					stmt_margin.setString(4, surcharge.getBill_id());
					stmt_margin.addBatch();	
					
					//Minus surcharge because we want to remove old surcharge and add new surcharge amount with total payable amount.
					newPayableAmount_amount=oldPayableAmount_amount+actual_surcharge;
					stmt_bill.setFloat(1,  newPayableAmount_amount);
					
					stmt_bill.setFloat(2, newPayableAmount_amount);
					stmt_bill.setString(3, pay_date);
					stmt_bill.setFloat(4,Float.valueOf(surcharge.getSurcharge_amount()));
					stmt_bill.setString(5, surcharge.getBill_id());
					stmt_bill.addBatch();	
					
					bill_id= surcharge.getBill_id();
			      }
				cstmt_backup.setString(1, bill_id);
				cstmt_backup.registerOutParameter(2, java.sql.Types.INTEGER);
				cstmt_backup.registerOutParameter(3, java.sql.Types.VARCHAR);
	            //cstmt_backup.executeUpdate();				
				//resp_code = cstmt_backup.getInt(2);
	            //resp_msg = cstmt_backup.getString(3);
	            
	            stmt_margin.executeBatch();
				stmt_bill.executeBatch();

	            
				transactionManager.commit();
				
				response.setMessasge("Successfully Saved..");
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
	 		finally{try{stmt_margin.close();stmt_bill.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt_margin = null;stmt_bill=null;conn = null;}
	 		
	 	
	 		return response;

	}
	
}

package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.MeterReplacementDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.MeterStatus;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class MeterReplacementService {

	
	public ResponseDTO saveMeterReplacementInfo(CustomerMeterDTO oldMeter,CustomerMeterDTO newMeter)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();		
		MeterInformationService miService=new MeterInformationService();
		String sql_meter=" Insert into METER_REPLACEMENT(PID,CUSTOMER_ID, OLD_METER_ID, NEW_METER_ID, INSERT_BY) " +
						 " Values(SQN_REPLACE.nextval, ?,?,  ?, ?)";

		String sql_customerMeter=" Update CUSTOMER_METER set Status="+MeterStatus.DISCONNECT.getId() +" Where METER_ID=?";
		
		
		PreparedStatement stmt = null;
		PreparedStatement mst_stmt = null;
		String meter_id=Utils.EMPTY_STRING;
		ResultSet r = null;
			try
			{
				mst_stmt = conn.prepareStatement("Select SQN_METER.nextval meter_id from dual");
				r = mst_stmt.executeQuery();
				if (r.next())
					meter_id=r.getString("meter_id"); 
				
				stmt = conn.prepareStatement(sql_meter);
				stmt.setString(1,newMeter.getCustomer_id());
				stmt.setString(2,oldMeter.getMeter_id());
				stmt.setString(3,meter_id);
				stmt.setString(4,newMeter.getInsert_by());				
				stmt.execute();
				
				stmt = conn.prepareStatement(sql_customerMeter);
				stmt.setString(1,oldMeter.getMeter_id());
				stmt.execute();
				
				newMeter.setMeter_id(meter_id);
				
				stmt=miService.getInsertStatementForNewMeter(conn, newMeter);
				stmt.execute();
				
				stmt = miService.getInsertStatementForNewMeterReading(conn, newMeter);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Replaced a new Meter.");
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
	
	public ArrayList<MeterReplacementDTO> getMeterReplacementList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		MeterReplacementDTO replacement=null;
		ArrayList<MeterReplacementDTO> replacementList=new ArrayList<MeterReplacementDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select TMP1.*,METER.METER_SL_NO NEW_METER_SL_NO,TO_CHAR(METER.INSTALLED_DATE,'DD-MM-YYYY') REPLACEMENT_DATE From ( " +
					  " Select PID,REPLACEMENT.CUSTOMER_ID,PI.FULL_NAME,substr(PI.CUSTOMER_ID,1,2) Area_Id,METER.METER_SL_NO OLD_METER_SL_NO, OLD_METER_ID,NEW_METER_ID From METER_REPLACEMENT REPLACEMENT,CUSTOMER_METER METER,CUSTOMER_PERSONAL_INFO PI " +
					  " Where REPLACEMENT.CUSTOMER_ID=METER.CUSTOMER_ID AND REPLACEMENT.OLD_METER_ID=METER.METER_ID " +
					  " AND REPLACEMENT.CUSTOMER_ID=PI.CUSTOMER_ID)TMP1,CUSTOMER_METER METER " +
					  " WHERE tmp1.NEW_METER_ID=METER.METER_ID   "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select TMP1.*,METER.METER_SL_NO NEW_METER_SL_NO,TO_CHAR(METER.INSTALLED_DATE,'DD-MM-YYYY') REPLACEMENT_DATE From ( " +
					  " Select PID,REPLACEMENT.CUSTOMER_ID,PI.FULL_NAME,substr(PI.CUSTOMER_ID,1,2) Area_Id,METER.METER_SL_NO OLD_METER_SL_NO, OLD_METER_ID,NEW_METER_ID From METER_REPLACEMENT REPLACEMENT,CUSTOMER_METER METER,CUSTOMER_PERSONAL_INFO PI " +
					  " Where REPLACEMENT.CUSTOMER_ID=METER.CUSTOMER_ID AND REPLACEMENT.OLD_METER_ID=METER.METER_ID " +
					  " AND REPLACEMENT.CUSTOMER_ID=PI.CUSTOMER_ID)TMP1,CUSTOMER_METER METER " +
					  " WHERE tmp1.NEW_METER_ID=METER.METER_ID "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					replacement=new MeterReplacementDTO();
					replacement.setPid(r.getString("PID"));
					replacement.setCustomer_id(r.getString("CUSTOMER_ID"));
					replacement.setCustomer_name(r.getString("FULL_NAME"));
					replacement.setOld_meter_id(r.getString("OLD_METER_ID"));
					replacement.setOld_meter_sl_no(r.getString("OLD_METER_SL_NO"));					
					replacement.setNew_meter_id(r.getString("NEW_METER_ID"));
					replacement.setNew_meter_sl_no(r.getString("NEW_METER_SL_NO"));
					replacement.setReplacement_date(r.getString("REPLACEMENT_DATE"));
					replacementList.add(replacement);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return replacementList;
	}
	
	public MeterReplacementDTO getMeterReplacementInfo(String replacment_id)
	{		
		MeterReplacementDTO replacement=null;
		MeterService meterService=new MeterService();
		Connection conn = ConnectionManager.getConnection();
		
		String sql= " Select OLD_METER_ID,NEW_METER_ID,CUSTOMER_ID,PID From METER_REPLACEMENT Where PID=?";
		String old_meter_id="";
		String new_meter_id="";
		String customer_id="";
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, replacment_id);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					old_meter_id=r.getString("OLD_METER_ID");
					new_meter_id=r.getString("NEW_METER_ID");
					customer_id=r.getString("CUSTOMER_ID");
					replacement=new MeterReplacementDTO();
					replacement.setPid(r.getString("PID"));
				   
				}
				replacement.setOldMeter(meterService.getCustomerMeterList(customer_id, old_meter_id,Utils.EMPTY_STRING).get(0));
				replacement.setNewMeter(meterService.getCustomerMeterList(customer_id, new_meter_id,Utils.EMPTY_STRING).get(0));
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return replacement;	 	
	}
	public ResponseDTO deleteReplacementInfo(String replacement_id)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		//@TODO:
		//		ResponseDTO response=MeterService.isMeterDeleteValid(meter_id);
		//		if(response.isResponse()==false)
		//			return response;
		
		String sqlReplacementInfo="Select * from METER_REPLACEMENT  Where PID=?";						  
		String sqlDeleteReplacementInfo="Delete METER_REPLACEMENT Where PID=?";
		String sqlDeleteNewMeter=" Delete CUSTOMER_METER Where  meter_id=?";
		String sqlConnectOldMeter=" Update CUSTOMER_METER Set Status="+MeterStatus.CONNECTED.getId()+" Where  meter_id=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlReplacementInfo);
				stmt.setString(1,replacement_id);				
				ResultSet r =stmt.executeQuery();
				String old_meter_id="";
				String new_meter_id="";
				
				if (r.next())
				{
					old_meter_id=r.getString("OLD_METER_ID");
					new_meter_id=r.getString("NEW_METER_ID");
				}
				
			
				stmt = conn.prepareStatement(sqlDeleteReplacementInfo);
				stmt.setString(1,replacement_id);
				stmt.execute();
				
			
				stmt = conn.prepareStatement(sqlDeleteNewMeter);
				stmt.setString(1,new_meter_id);
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlConnectOldMeter);
				stmt.setString(1,old_meter_id);
				stmt.execute();
				
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Delete Replacement Information.");
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

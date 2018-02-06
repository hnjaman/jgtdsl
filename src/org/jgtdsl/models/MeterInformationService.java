package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.TransactionManager;

public class MeterInformationService {

	public ResponseDTO saveMeterInfo(CustomerMeterDTO meter)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();		


		
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
				
				meter.setMeter_id(meter_id);
				
				stmt=getInsertStatementForNewMeter(conn, meter);
				stmt.execute();
				
				stmt = getInsertStatementForNewMeterReading(conn, meter);
				stmt.execute();
				
				String conn_update_sql="Update customer_connection set STATUS=1";
				stmt=conn.prepareStatement(conn_update_sql);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Created a new Meter");
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
	
	public PreparedStatement getInsertStatementForNewMeter(Connection conn,CustomerMeterDTO meter){
		
		PreparedStatement stmt = null;
		
		String sql_meter=" Insert Into CUSTOMER_METER(CUSTOMER_ID,METER_ID,METER_SL_NO,METER_MFG,METER_YEAR,MEASUREMENT_TYPE, " +
		 " EVC_SL_NO,EVC_MODEL,EVC_YEAR,METER_TYPE,G_RATING,CONN_SIZE,MAX_READING,INI_READING, " +
		 " PRESSURE,TEMPERATURE,METER_RENT,INSTALLED_BY,INSTALLED_DATE,STATUS,REMARKS,VAT_REBATE,UNIT) " +
		 " Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'dd-MM-YYYY HH24:MI'),1,?,?,?)";
		String conn_update_sql="Update customer_connection set STATUS=0";
		
		try {
			stmt = conn.prepareStatement(sql_meter);
			stmt.setString(1,meter.getCustomer_id());
			stmt.setString(2,meter.getMeter_id());
			stmt.setString(3,meter.getMeter_sl_no());
			stmt.setString(4,meter.getMeter_mfg());
			stmt.setString(5,meter.getMeter_year());
			stmt.setString(6,meter.getMeasurement_type_str());
			stmt.setString(7,meter.getEvc_sl_no());
			stmt.setString(8,meter.getEvc_model());
			stmt.setString(9,meter.getEvc_year());
			
			stmt.setString(10,meter.getMeter_type());
			stmt.setString(11,meter.getG_rating());
			stmt.setString(12,meter.getConn_size());
			stmt.setString(13,meter.getMax_reading());
			stmt.setString(14,meter.getIni_reading());
			stmt.setString(15,meter.getPressure());
			stmt.setString(16,meter.getTemperature());
			stmt.setString(17,meter.getMeter_rent());
			stmt.setString(18,meter.getInstalled_by());
			stmt.setString(19,meter.getInstalled_date());
			stmt.setString(20,meter.getRemarks());
			stmt.setDouble(21, meter.getVat_rebate());
			stmt.setString(22,meter.getUnit());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stmt;
	}
	public PreparedStatement getInsertStatementForNewMeterReading(Connection conn,CustomerMeterDTO meter){
		
		PreparedStatement stmt = null;
		
		String sql_reading=" Insert into METER_READING(READING_ID, CUSTOMER_ID, METER_ID,READING_PURPOSE,CURR_READING,CURR_READING_DATE,PRESSURE,TEMPERATURE,REMARKS) " +
		   " Values(SQN_METER_READING.nextval,?,?,?,?,to_date(?,'dd-MM-YYYY HH24:MI'),?,?,?) ";

		try {
			stmt = conn.prepareStatement(sql_reading);
			stmt.setString(1,meter.getCustomer_id());
			stmt.setString(2,meter.getMeter_id());
			stmt.setInt(3,ReadingPurpose.NEW_METER.getId());				
			stmt.setString(4,meter.getIni_reading());
			stmt.setString(5,meter.getInstalled_date());
			stmt.setString(6,meter.getPressure());
			stmt.setString(7,meter.getTemperature());
			stmt.setString(8,"New Meter Installation");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stmt;
	}
	public ResponseDTO updateMeterInfo(CustomerMeterDTO meter)
	{		
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();		
		
		ResponseDTO editResponse=MeterService.isMeterBasicInfoChangeValid(meter.getMeter_id());
		ResponseDTO response=new ResponseDTO();
		response.setDialogCaption("Edit Confirmation");
		
		String sql_all_data=" Update CUSTOMER_METER Set MEASUREMENT_TYPE=?,EVC_SL_NO=?,EVC_MODEL=?, " +
				   " EVC_YEAR=?,METER_TYPE=?,G_RATING=?,CONN_SIZE=?,MAX_READING=?,INSTALLED_BY=?,VAT_REBATE=?,REMARKS=?," +
				   " METER_SL_NO=?, METER_MFG=?,METER_YEAR=?,INI_READING=?,PRESSURE=?, " +
				   " TEMPERATURE=?,INSTALLED_DATE=to_date(?,'DD-MM-YYYY') Where Meter_Id=?";
		
		String sql_data=" Update CUSTOMER_METER Set MEASUREMENT_TYPE=?,EVC_SL_NO=?,EVC_MODEL=?, " +
		   " EVC_YEAR=?,METER_TYPE=?,G_RATING=?,CONN_SIZE=?,MAX_READING=?,INSTALLED_BY=?,VAT_REBATE=?,REMARKS=?" +
		   "  Where Meter_Id=?";

		String sql=editResponse.isResponse()==true?sql_all_data:sql_data;
		PreparedStatement stmt = null;
		try
		{
			
			stmt = conn.prepareStatement(sql);

			stmt.setString(1,meter.getMeasurement_type_str());
			stmt.setString(2,meter.getEvc_sl_no());
			stmt.setString(3,meter.getEvc_model());
			stmt.setString(4,meter.getEvc_year());
			stmt.setString(5,meter.getMeter_type());
			stmt.setString(6,meter.getG_rating());
			stmt.setString(7,meter.getConn_size());
			stmt.setString(8,meter.getMax_reading());
			stmt.setString(9,meter.getInstalled_by());
			stmt.setDouble(10, meter.getVat_rebate());
			stmt.setString(11,meter.getRemarks());
			
			if(editResponse.isResponse()==true){
				stmt.setString(12,meter.getMeter_sl_no());
				stmt.setString(13,meter.getMeter_mfg());
				stmt.setString(14,meter.getMeter_year());
				stmt.setString(15,meter.getIni_reading());
				stmt.setString(16,meter.getPressure());
				stmt.setString(17,meter.getTemperature());			
				stmt.setString(18,meter.getInstalled_date());
				stmt.setString(19,meter.getMeter_id());
			}
			else
				stmt.setString(12,meter.getMeter_id());
			
			stmt.executeUpdate();
			
			if(editResponse.isResponse()==true){
			sql="Update METER_READING Set CURR_READING=?, CURR_READING_DATE=to_date(?,'DD-MM-YYYY') Where Meter_Id=? and Reading_Purpose=0";
			stmt = conn.prepareStatement(sql);

			stmt.setString(1,meter.getIni_reading());
			stmt.setString(2,meter.getInstalled_date());
			stmt.setString(3,meter.getMeter_id());

			stmt.executeUpdate();
			}
			
			transactionManager.commit();
			response.setMessasge("Successfully edited selected meter.");
			response.setResponse(true);
		} 
		catch (Exception e){e.printStackTrace();
			response.setMessasge( e.getMessage());
			response.setResponse(false);
			try {transactionManager.rollback();} catch (Exception ex) {ex.printStackTrace();}
		}
 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
		{e.printStackTrace();}stmt = null;conn = null;}
 		
 		return response;
	}
	public static ResponseDTO deleteMeter(String meter_id)
	{
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();	
		

		ResponseDTO response=MeterService.isMeterDeleteValid(meter_id);
		if(response.isResponse()==false)
			return response;
		
		response.setDialogCaption("Delete Confrimation");
		String sql=" Delete CUSTOMER_METER Where METER_ID=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,meter_id);
			stmt.executeUpdate();
			
			sql="Delete METER_READING Where Meter_Id=? and Reading_Purpose=0";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,meter_id);
			stmt.executeUpdate();
			
			transactionManager.commit();
			
			response.setMessasge("Successfully deleted selected meter.");
			response.setResponse(true);
			
		} 
		catch (Exception e){e.printStackTrace();
			response.setMessasge( e.getMessage());
			response.setResponse(false);
			try {transactionManager.rollback();} catch (Exception ex) {ex.printStackTrace();}
		}
 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
		{e.printStackTrace();}stmt = null;conn = null;}
 		
 		return response;
	}
	
}

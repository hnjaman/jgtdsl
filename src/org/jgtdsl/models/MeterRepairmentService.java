package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.MeterRepairmentDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.utils.connection.ConnectionManager;

public class MeterRepairmentService {

	public ResponseDTO saveMeterRepairmentInfo(MeterRepairmentDTO repair)
	{
		
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();		
		String sql=" Insert Into METER_REPAIRMENT(PID,CUSTOMER_ID, METER_ID, PREV_READING, PREV_READING_DATE, CURR_READING,  " +
						 " CURR_READING_DATE, REPAIRED_BY, REMARKS, READING_ID)" +
						 " Values(SQN_REPAIR.NEXTVAL,?,?,?,to_date(?,'DD-MM-YYYY'),?,to_date(?,'DD-MM-YYYY'),?,?,?)";
	
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,repair.getMeter_id());
				stmt.setString(2,repair.getCustomer_id());
				if(repair.getMeasurement_type_str().equalsIgnoreCase(String.valueOf(MeterMeasurementType.NORMAL.getId()))){
					
						stmt.setNull(3,java.sql.Types.NULL);
						stmt.setNull(4,java.sql.Types.NULL);
						stmt.setNull(5,java.sql.Types.NULL);
						stmt.setNull(6,java.sql.Types.NULL);
						stmt.setNull(9,java.sql.Types.NULL);
					
				}else{
					stmt.setString(3,repair.getPrev_reading());
					stmt.setString(4,repair.getPrev_reading_date());
					stmt.setString(5,repair.getCurr_reading());
					stmt.setString(6,repair.getCurr_reading_date());
					stmt.setString(9,repair.getReading_id());
				}
				
				stmt.setString(7,repair.getRepaired_by());
				stmt.setString(8,repair.getRemarks());
				
				
				stmt.execute();
				response.setMessasge("Successfully Saved Meter Repairment Information.");
				response.setResponse(true);
				
			} 
			
			catch (Exception e){e.printStackTrace();response.setMessasge(e.getMessage());
			response.setResponse(false);
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		

	 		return response;
	}
	
	public ArrayList<DisconnectDTO> getMeterRepairmentList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		DisconnectDTO disConn=null;
		ArrayList<DisconnectDTO> disConnList=new ArrayList<DisconnectDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT PID,DISCONN_METERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,DISCONN_METERED.METER_ID,CUSTOMER_METER.METER_SL_NO,DISCONNECT_CAUSE, "+
				  		" DISCONNECT_TYPE,DISCONNECT_BY,TO_CHAR (DISCONNECT_DATE, 'DD-MM-YYYY') DISCONNECT_DATE,"+
				  		" DISCONN_METERED.REMARKS,READING_ID"+
				  		" FROM DISCONN_METERED,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO"+
				  		" WHERE DISCONN_METERED.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=DISCONN_METERED.CUSTOMER_ID   "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( SELECT PID,DISCONN_METERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,DISCONN_METERED.METER_ID,CUSTOMER_METER.METER_SL_NO,DISCONNECT_CAUSE, "+
				  	  " DISCONNECT_TYPE,DISCONNECT_BY,TO_CHAR (DISCONNECT_DATE, 'DD-MM-YYYY') DISCONNECT_DATE,"+
				  	  " DISCONN_METERED.REMARKS,READING_ID"+
				  	  " FROM DISCONN_METERED,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO"+
				  	  " WHERE DISCONN_METERED.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=DISCONN_METERED.CUSTOMER_ID "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					disConn=new DisconnectDTO();
					disConn.setPid(r.getString("PID"));
					disConn.setCustomer_id(r.getString("CUSTOMER_ID"));
					disConn.setCustomer_name(r.getString("FULL_NAME"));
					disConn.setMeter_id(r.getString("METER_ID"));
					disConn.setMeter_sl_no(r.getString("METER_SL_NO"));
					disConn.setDisconnect_cause_str(r.getString("DISCONNECT_CAUSE"));					
					disConn.setDisconnect_type_str(r.getString("DISCONNECT_TYPE"));					
					disConn.setDisconnect_cause_name(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getLabel());
					disConn.setDisconnect_type_name(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getLabel());					
					disConn.setDisconnect_date(r.getString("DISCONNECT_DATE"));
					disConn.setRemarks(r.getString("REMARKS"));
					disConnList.add(disConn);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return disConnList;
	}
}

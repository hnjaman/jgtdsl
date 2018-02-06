package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class MeterRentService {

	
	public ResponseDTO saveMeterRentChangeInfo(MeterRentChangeDTO mRentChagne)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		
//EFFECTIVE DATE IS SET TO FIRST DAY OF THE MONTH. UPDATED ON SEPT 14,2017	
		String sqlInsert=" Insert into METER_RENT_CHANGE(PID, CUSTOMER_ID, METER_ID, OLD_RENT, NEW_RENT,EFFECTIVE_DATE, REMARKS, INSERT_BY) " +
				 		  " Values(SQN_CNG_MRENT_RENT.nextval, ?, ?, ?, ?,LAST_DAY(ADD_MONTHS(TO_DATE(?, 'DD-MM-YYYY'),-1)) + 1 , ?, ?)";
		

		String sqlUpdate=" Update CUSTOMER_METER set Meter_Rent=? WHERE customer_id=?";
		
		

		PreparedStatement stmt = null;
			try
			{
				

				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,mRentChagne.getCustomer_id());
				stmt.setString(2,mRentChagne.getMeter_id());
				stmt.setString(3,mRentChagne.getOld_rent());
				stmt.setString(4,mRentChagne.getNew_rent());
				stmt.setString(5,mRentChagne.getEffective_date());
				stmt.setString(6,mRentChagne.getRemarks());
				stmt.setString(7,mRentChagne.getInsert_by());					
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlUpdate);
				stmt.setString(1,mRentChagne.getNew_rent());
				stmt.setString(2,mRentChagne.getCustomer_id());
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Saved Meter Rent Change Information.");
				response.setResponse(true);
				
				String cKey="CUSTOMER_INFO_"+mRentChagne.getCustomer_id();
				CacheUtil.clear(cKey);

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
	
	public ResponseDTO udpateMeterRentChangeInfo(MeterRentChangeDTO mRentChagne)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = ConnectionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		
//EFFECTIVE DATE IS SET TO FIRST DAY OF THE MONTH. UPDATED ON SEPT 14,2017		
						  
		String sqlInsert=" Update METER_RENT_CHANGE  Set OLD_RENT=?, NEW_RENT=?,EFFECTIVE_DATE=LAST_DAY(ADD_MONTHS(TO_DATE(?, 'DD-MM-YYYY'),-1)) + 1 , REMARKS=?  " +
						 " Where PID=? ";
		String sqlUpdate=" Update CUSTOMER_METER set Meter_Rent=? WHERE customer_id=? and meter_id=?";
		
		
		
		PreparedStatement stmt = null;
		try
		{			
			stmt = conn.prepareStatement(sqlInsert);
			stmt.setString(1,mRentChagne.getOld_rent());
			stmt.setString(2,mRentChagne.getNew_rent());
			stmt.setString(3,mRentChagne.getEffective_date());
			stmt.setString(4,mRentChagne.getRemarks());
			stmt.setString(5,mRentChagne.getPid());
			stmt.execute();
			
			stmt = conn.prepareStatement(sqlUpdate);
			stmt.setString(1,mRentChagne.getNew_rent());
			stmt.setString(2,mRentChagne.getCustomer_id());
			stmt.setString(3,mRentChagne.getMeter_id());
			stmt.execute();
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Updated Meter Rent Change Information.");
			response.setResponse(true);
			
			String cKey="CUSTOMER_INFO_"+mRentChagne.getCustomer_id();
			CacheUtil.clear(cKey);
			
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
	
	public ArrayList<MeterRentChangeDTO> getMeterRentChangeList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		MeterRentChangeDTO rentChangeDTO=null;
		ArrayList<MeterRentChangeDTO> rentChangeList=new ArrayList<MeterRentChangeDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT PID,METER_RENT_CHANGE.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,METER_RENT_CHANGE.METER_ID,CUSTOMER_METER.METER_SL_NO,OLD_RENT,NEW_RENT, " +
						" TO_CHAR (EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,METER_RENT_CHANGE.REMARKS,MST_AREA.AREA_ID,MST_AREA.AREA_NAME  " +
						" FROM METER_RENT_CHANGE,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER  " +
						" WHERE METER_RENT_CHANGE.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=METER_RENT_CHANGE.CUSTOMER_ID " +
						" AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
						(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
			      sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (   SELECT PID,METER_RENT_CHANGE.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,METER_RENT_CHANGE.METER_ID,CUSTOMER_METER.METER_SL_NO,OLD_RENT,NEW_RENT, " +
				  	" TO_CHAR (EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,METER_RENT_CHANGE.REMARKS,MST_AREA.AREA_ID,MST_AREA.AREA_NAME  " +
					" FROM METER_RENT_CHANGE,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER  " +
					" WHERE METER_RENT_CHANGE.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=METER_RENT_CHANGE.CUSTOMER_ID " +
					" AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
					  (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					rentChangeDTO=new MeterRentChangeDTO();
					rentChangeDTO.setPid(r.getString("PID"));
					rentChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					rentChangeDTO.setCustomer_name(r.getString("FULL_NAME"));
					rentChangeDTO.setMeter_id(r.getString("METER_ID"));
					rentChangeDTO.setMeter_sl_no(r.getString("METER_SL_NO"));
					rentChangeDTO.setOld_rent(r.getString("OLD_RENT"));
					rentChangeDTO.setNew_rent(r.getString("NEW_RENT"));
					rentChangeDTO.setEffective_date(r.getString("EFFECTIVE_DATE"));
					rentChangeDTO.setRemarks(r.getString("REMARKS"));
					
					rentChangeList.add(rentChangeDTO);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return rentChangeList;
	}
	public MeterRentChangeDTO getMeterRentChangeInfo(String meterRentChangeId)
	{		
		MeterRentChangeDTO rentChangeDTO=null;
		Connection conn = ConnectionManager.getConnection();
		
		String sql= " SELECT PID,METER_RENT_CHANGE.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,METER_RENT_CHANGE.METER_ID,CUSTOMER_METER.METER_SL_NO,OLD_RENT,NEW_RENT, " +
		  " TO_CHAR (EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,METER_RENT_CHANGE.REMARKS  " +
		  " FROM METER_RENT_CHANGE,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO  " +
		  " WHERE METER_RENT_CHANGE.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=METER_RENT_CHANGE.CUSTOMER_ID and PID= ?";
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, meterRentChangeId);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					rentChangeDTO=new MeterRentChangeDTO();
					rentChangeDTO.setPid(r.getString("PID"));
					rentChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					rentChangeDTO.setCustomer_name(r.getString("FULL_NAME"));
					rentChangeDTO.setMeter_id(r.getString("METER_ID"));
					rentChangeDTO.setMeter_sl_no(r.getString("METER_SL_NO"));
					rentChangeDTO.setOld_rent(r.getString("OLD_RENT"));
					rentChangeDTO.setNew_rent(r.getString("NEW_RENT"));
					rentChangeDTO.setEffective_date(r.getString("EFFECTIVE_DATE"));
					rentChangeDTO.setRemarks(r.getString("REMARKS"));
				   
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return rentChangeDTO;	 	
	}
	
	public ResponseDTO deleteMeterRentChangeInfo(String meterRentChangeId)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlRentChangeInfo="Select * from METER_RENT_CHANGE  Where PID=?";						  
		String sqlDeleteRentChange="Delete METER_RENT_CHANGE Where PID=?";
		String sqlUpdate=" Update CUSTOMER_METER set Meter_Rent=? WHERE customer_id=? and meter_id=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlRentChangeInfo);
				stmt.setString(1,meterRentChangeId);				
				ResultSet r =stmt.executeQuery();
				String meter_id="";
				String customer_id="";
				String old_rent="";
				
				if (r.next())
				{
					meter_id=r.getString("METER_ID");
					customer_id=r.getString("CUSTOMER_ID");
					old_rent=r.getString("OLD_RENT");
				}
				
			
				stmt = conn.prepareStatement(sqlDeleteRentChange);
				stmt.setString(1,meterRentChangeId);
				stmt.execute();
				
			
				stmt = conn.prepareStatement(sqlUpdate);
				stmt.setString(1,old_rent);
				stmt.setString(2,customer_id);
				stmt.setString(3,meter_id);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Deleted Meter Rent Change Information.");
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

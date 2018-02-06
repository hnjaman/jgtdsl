package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.BillingMeteredDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.ReconnectDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.MeterStatus;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class ReconnectionService {

	/* For METERED Customer */
	public ResponseDTO saveMeterReconnectInfo(ReconnectDTO reconn,DisconnectDTO disconn)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		OracleCallableStatement callable_statement = null;
		PreparedStatement stmt = null;
		
		response=validateReconnInfo(reconn,disconn);
		if(response.isResponse()==false)
			return response;
		

		String sqlReading=" Insert into METER_READING(READING_ID, CUSTOMER_ID, METER_ID,READING_PURPOSE,CURR_READING,CURR_READING_DATE,REMARKS) " +
			  " Values(SQN_METER_READING.nextval,?,?,?,?,to_date(?,'dd-MM-YYYY HH24:MI'),?) ";
		
		String sqlReadingId="Select max(Reading_Id) Reading_Id from METER_READING WHERE  Reading_Purpose=? "+
	    					"And customer_id=? and meter_id=? ";
		
		String sqlInsert=" Insert Into RECONN_METERED(PID,CUSTOMER_ID,METER_ID,METER_READING,RECONECT_BY,RECONNECT_DATE,REMARKS,INSERT_BY,DISCONNECT_ID,READING_ID) " +
						   " Values(SQN_RECONN_ME.nextval,?,?,?,?,to_date(?,'dd-MM-YYYY HH24:MI'),?,?,?,?)";
		

		String sqlUpdate=" Update CUSTOMER_METER set status=? where customer_id=? and meter_id=?";

		

		
			try
			{
				

				stmt = conn.prepareStatement(sqlReading);
				stmt.setString(1,disconn.getCustomer_id());
				stmt.setString(2,disconn.getMeter_id());				
				stmt.setInt(3,ReadingPurpose.RECONNECT_METER.getId());
				stmt.setString(4,reconn.getMeter_reading());
				stmt.setString(5,reconn.getReconnect_date());
				stmt.setString(6,"Reconnect Meter");
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlReadingId);
				stmt.setInt(1,ReadingPurpose.GENERAL_BILLING.getId());
				stmt.setString(2,disconn.getCustomer_id());
				stmt.setString(3,disconn.getMeter_id());
				ResultSet r =stmt.executeQuery();
				int reading_id=0;				
				if (r.next())
				{
					reading_id=r.getInt("READING_ID");
				}				
				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,disconn.getCustomer_id());
				stmt.setString(2,disconn.getMeter_id());
				stmt.setString(3,reconn.getMeter_reading());
				stmt.setString(4,reconn.getReconnect_by());
				stmt.setString(5,reconn.getReconnect_date());
				stmt.setString(6,reconn.getRemarks());
				stmt.setString(7,reconn.getInsert_by());
				stmt.setString(8,disconn.getPid());
				stmt.setInt(9,reading_id);		
				stmt.execute();
												
				stmt = conn.prepareStatement(sqlUpdate);				
				stmt.setInt(1,MeterStatus.CONNECTED.getId());
				stmt.setString(2,disconn.getCustomer_id());
				stmt.setString(3,disconn.getMeter_id());
				stmt.executeUpdate();

				callable_statement = (OracleCallableStatement) conn.prepareCall("{ call UpdateMeterCustomerStatus(?,?)  }");
				callable_statement.setString(1, disconn.getCustomer_id());           
				callable_statement.registerOutParameter(2, java.sql.Types.INTEGER);
				callable_statement.executeUpdate();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Saved Reconnection Information.");
				response.setResponse(true);
				
				String cKey="CUSTOMER_INFO_"+disconn.getCustomer_id();
				CustomerDTO customer=((CustomerDTO)CacheUtil.getObjFromCache(cKey));
				CacheUtil.clear(cKey);
				if(customer==null){
					CustomerService customerService=new CustomerService();
					customer=customerService.getCustomerInfo(disconn.getCustomer_id());
				}
				
				CacheUtil.clearStartWith("ALL_METERED_CONNECTED_CUSTOMER_ID_"+customer.getArea());
				CacheUtil.clearStartWith("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"+customer.getArea());

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
	
	public ResponseDTO updateMeterReconnInfo(ReconnectDTO reconn,DisconnectDTO disconn)
	{		
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		
		response=validateReconnInfo(reconn,disconn);
		if(response.isResponse()==false)
			return response;
						  
		String sqlReconnInfo=" Update RECONN_METERED " +
							  " Set "+
							  " METER_READING=?,RECONECT_BY=?,RECONNECT_DATE=to_date(?,'DD-MM-YYYY'),REMARKS=?"+
							  " Where PID =?";
		
		PreparedStatement stmt = null;
			try
			{
				
				stmt = conn.prepareStatement(sqlReconnInfo);
				stmt.setString(1,reconn.getMeter_reading());
				stmt.setString(2,reconn.getReconnect_by());
				stmt.setString(3,reconn.getReconnect_date());
				stmt.setString(4,reconn.getRemarks());
				stmt.setString(5,reconn.getPid());
				stmt.execute();
				
				
				response.setMessasge("Successfully Updated Reconnection Information.");
				response.setResponse(true);
				
			} 
			
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return response;
	 	
	}
	
	
	public ArrayList<ReconnectDTO> getMeterReconnectionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		ReconnectDTO reConn=null;
		ArrayList<ReconnectDTO> reConnList=new ArrayList<ReconnectDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT PID,RECONN_METERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,RECONN_METERED.METER_ID,CUSTOMER_METER.METER_SL_NO, " +
					  " RECONECT_BY,TO_CHAR (RECONNECT_DATE, 'DD-MM-YYYY') RECONNECT_DATE, " +
					  " RECONN_METERED.REMARKS,DISCONNECT_ID,MST_AREA.AREA_ID,MST_AREA.AREA_NAME " +
					  " FROM RECONN_METERED,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER " +
					  " WHERE RECONN_METERED.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=RECONN_METERED.CUSTOMER_ID  " +
					  " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID "+
					  (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (  SELECT PID,RECONN_METERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,RECONN_METERED.METER_ID,CUSTOMER_METER.METER_SL_NO, " +
				  	  " RECONECT_BY,TO_CHAR (RECONNECT_DATE, 'DD-MM-YYYY') RECONNECT_DATE, " +
				  	  " RECONN_METERED.REMARKS,DISCONNECT_ID ,MST_AREA.AREA_ID,MST_AREA.AREA_NAME" +
				  	  " FROM RECONN_METERED,CUSTOMER_METER,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER " +
				  	  " WHERE RECONN_METERED.METER_ID=CUSTOMER_METER.METER_ID AND CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=RECONN_METERED.CUSTOMER_ID  "+
				  	  " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID "+
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
					reConn=new ReconnectDTO();
					reConn.setPid(r.getString("PID"));
					reConn.setCustomer_id(r.getString("CUSTOMER_ID"));
					reConn.setCustomer_name(r.getString("FULL_NAME"));
					reConn.setMeter_id(r.getString("METER_ID"));
					reConn.setMeter_sl_no(r.getString("METER_SL_NO"));
					reConn.setReconnect_date(r.getString("RECONNECT_DATE"));
					reConn.setRemarks(r.getString("REMARKS"));
					reConnList.add(reConn);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return reConnList;
	}
	public ReconnectDTO getMeterReconnectionInfo(String reconnection_id,String meter_id)
	{		
		ReconnectDTO reconn=null;
		DisconnectDTO disconn=null;
		
		Connection conn = ConnectionManager.getConnection();
		String whereCondition="";
		if(reconnection_id==null)
			whereCondition=" ( Select max(pid) from RECONN_METERED Where Meter_Id=? ) ";
		else
			whereCondition="? ";
		
		String sql= " Select tmp1.*,brm.BILL_ID from  " +
				    " (Select RM.PID RECONNECT_ID,RM.CUSTOMER_ID,RM.METER_ID,RM.METER_READING,RM.RECONECT_BY,TO_CHAR(RM.RECONNECT_DATE,'DD-MM-YYYY') RECONNECT_DATE, " +
					" RM.REMARKS Reconn_REMARKS,RM.DISCONNECT_ID,CPI.FULL_NAME,DM.DISCONNECT_CAUSE,DM.DISCONNECT_TYPE,DM.DISCONNECT_BY,TO_CHAR(DM.DISCONNECT_DATE,'DD-MM-YYYY') DISCONNECT_DATE,DM.READING_ID,MR.CURR_READING, " +
					" DM.REMARKS Disconn_REMARKS,CM.METER_SL_NO " +
					" From RECONN_METERED RM,DISCONN_METERED DM,CUSTOMER C,CUSTOMER_PERSONAL_INFO CPI,CUSTOMER_METER CM,METER_READING MR " +
					" Where RM.PID= "+whereCondition+
					" And C.CUSTOMER_ID=CPI.CUSTOMER_ID And RM.CUSTOMER_ID=C.CUSTOMER_ID AND RM.DISCONNECT_ID=DM.PID AND RM.METER_ID=CM.METER_ID " +
					" And MR.READING_ID=DM.READING_ID )tmp1 Left Outer Join BILLING_READING_MAP brm "+
				    " on tmp1.READING_ID=brm.READING_ID";
			
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				if(reconnection_id==null)
					stmt.setString(1, meter_id);
				else
					stmt.setString(1, reconnection_id);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					reconn=new ReconnectDTO();
					disconn=new DisconnectDTO();
					
					disconn.setPid(r.getString("DISCONNECT_ID"));
					disconn.setCustomer_id(r.getString("CUSTOMER_ID"));
					disconn.setMeter_id(r.getString("METER_ID"));
					disconn.setMeter_sl_no(r.getString("METER_SL_NO"));
					disconn.setDisconnect_cause_name(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getLabel());
					disconn.setDisconnect_cause_str(r.getString("DISCONNECT_CAUSE"));
					disconn.setDisconnect_type_name(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getLabel());
					disconn.setDisconnect_type_str(r.getString("DISCONNECT_TYPE"));
					disconn.setDisconnect_by(r.getString("DISCONNECT_BY"));
					disconn.setDisconnect_by_name(EmployeeService.getEmpNameFromEmpId(r.getString("DISCONNECT_BY")));
					disconn.setDisconnect_date(r.getString("DISCONNECT_DATE"));
					disconn.setReading_id(r.getString("READING_ID"));
					disconn.setRemarks(r.getString("DISCONN_REMARKS"));
					disconn.setMeter_reading(r.getString("CURR_READING"));
					
					reconn.setDisconnectionInfo(disconn);
					
					reconn.setPid(r.getString("RECONNECT_ID"));
					reconn.setReconnect_date(r.getString("RECONNECT_DATE"));
					reconn.setReconnect_by(r.getString("RECONECT_BY"));
					reconn.setMeter_reading(r.getString("METER_READING"));
					reconn.setRemarks(r.getString("RECONN_REMARKS"));
					reconn.setMeter_reading(r.getString("METER_READING"));
					
					
					
				   
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return reconn;	 	
	}
	
	public ResponseDTO deleteMeterReconnInfo(String reconnect_id)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlReconnInfo="Select * from RECONN_METERED  Where PID=?";						  
		String sqlDeleteReconn="Delete RECONN_METERED Where PID=?";
		String sqlDeleteReading="Delete METER_READING Where READING_ID=?";
		String sqlUpdateMeter=" Update CUSTOMER_METER set status=0 where customer_id=? and meter_ID=?";
		
		PreparedStatement stmt = null;
		OracleCallableStatement callable_statement = null;
		
			try
			{
				stmt = conn.prepareStatement(sqlReconnInfo);
				stmt.setString(1,reconnect_id);				
				ResultSet r =stmt.executeQuery();
				String meter_id="";
				String customer_id="";
				String reading_id="";
				
				if (r.next())
				{
					meter_id=r.getString("METER_ID");
					customer_id=r.getString("CUSTOMER_ID");
					reading_id=r.getString("READING_ID");
				}
				
			
				stmt = conn.prepareStatement(sqlDeleteReconn);
				stmt.setString(1,reconnect_id);
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlDeleteReading);
				stmt.setString(1,reading_id);
				stmt.execute();
				
				
				stmt = conn.prepareStatement(sqlUpdateMeter);
				stmt.setString(1,customer_id);
				stmt.setString(2,meter_id);
				stmt.execute();
				
				callable_statement = (OracleCallableStatement) conn.prepareCall("{ call UpdateMeterCustomerStatus(?,?)  }");
				callable_statement.setString(1, customer_id);           
				callable_statement.registerOutParameter(2, java.sql.Types.INTEGER);
				callable_statement.executeUpdate();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Deleted Reconnection Information.");
				response.setResponse(true);		
				
				String cKey="CUSTOMER_INFO_"+customer_id;
				CacheUtil.clear(cKey);
				CustomerDTO customer=((CustomerDTO)CacheUtil.getObjFromCache(cKey));
				CacheUtil.clearStartWith("ALL_METERED_CONNECTED_CUSTOMER_ID_"+customer.getArea());
				CacheUtil.clearStartWith("ALL_METERED_DISCONNECTED_CUSTOMER_ID_"+customer.getArea());
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
	
	
	public ResponseDTO validateReconnInfo(ReconnectDTO reconn,DisconnectDTO disconn){
		ResponseDTO response=new ResponseDTO();
		BillingService bService=new BillingService();
		BillingMeteredDTO bill= bService.getBillStatus(reconn.getPid());
		
		if(bill==null){
			
			if(isValidReconnDate(reconn.getReconnect_date(),disconn.getDisconnect_date())==true)
				response.setResponse(true);
			else
			{
				response.setResponse(false);
				response.setMessasge("Sorry, This is an invalid reconnection date.");
			}
				
		}
		else{
			response.setResponse(false);
			response.setMessasge("Sorry, Billing has already done(for disconnection reading) of the Reconnnection reading entry.");
			}
				
		return response;
	}
	
	
	//Validate: Reconnect date must be greater or equal to disconnect date.
	public  boolean isValidReconnDate(String reconn_date,String disconn_date)
	{
		Connection conn = ConnectionManager.getConnection();
		String sql=" select case when to_date(?,'DD-MM-YYYY') >= to_date(?,'DD-MM-YYYY') then 1 else 0 end STATUS from dual";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, reconn_date);
				stmt.setString(2, disconn_date);
				r = stmt.executeQuery();
				if(r.next())
				{
					if(r.getInt("STATUS")==1)
						return true;
					else
						return false;
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return false;
	}
	
	//For Non-Metered
	public ResponseDTO saveNonMeterReconnectInfo(ReconnectDTO reconn,DisconnectDTO disconn)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlInsert=" Insert Into RECONN_NONMETERED(PID,CUSTOMER_ID,RECONECT_BY,RECONNECT_DATE,REMARKS,INSERT_BY,DISCONNECT_ID) " +
						   " Values(SQN_DISC_NME.nextval,?,?,to_date(?,'dd-MM-YYYY'),?,?,?)";
		String sqlUpdate=" Update CUSTOMER_CONNECTION set status="+ConnectionStatus.CONNECTED.getId()+" where customer_id=?";

		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,disconn.getCustomer_id());
				stmt.setString(2,reconn.getReconnect_by());
				stmt.setString(3,reconn.getReconnect_date());
				stmt.setString(4,reconn.getRemarks());			
				stmt.setString(5,reconn.getInsert_by());
				stmt.setString(6,reconn.getDisconnect_id());
				stmt.execute();
				stmt = conn.prepareStatement(sqlUpdate);
				stmt.setString(1,reconn.getCustomer_id());
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Saved Reconnection Information.");
				response.setResponse(true);
				String cKey="CUSTOMER_INFO_"+disconn.getCustomer_id();
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
	
	public ResponseDTO updateNonMeterReconnInfo(ReconnectDTO reconn,DisconnectDTO disconn)
	{		
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
						  
		String sqlReconnInfo=" Update RECONN_NONMETERED " +
							  " Set "+
							  " RECONECT_BY=?,RECONNECT_DATE=to_date(?,'DD-MM-YYYY'),REMARKS=?"+
							  " Where PID =?";
		
		PreparedStatement stmt = null;
			try
			{
				
				stmt = conn.prepareStatement(sqlReconnInfo);
				stmt.setString(1,reconn.getReconnect_by());
				stmt.setString(2,reconn.getReconnect_date());
				stmt.setString(3,reconn.getRemarks());
				stmt.setString(4,reconn.getPid());
				stmt.execute();
				
				
				response.setMessasge("Successfully Updated Reconnection Information.");
				response.setResponse(true);
				
			} 
			
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return response;
	 	
	}
	
	public ArrayList<ReconnectDTO> getNonMeterReconnectionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		ReconnectDTO reConn=null;
		ArrayList<ReconnectDTO> reConnList=new ArrayList<ReconnectDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql =   " SELECT PID,RECONN_NONMETERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,RECONECT_BY,TO_CHAR (RECONNECT_DATE, 'DD-MM-YYYY') RECONNECT_DATE,  " +
						  " RECONN_NONMETERED.REMARKS,DISCONNECT_ID,MST_AREA.AREA_NAME  " +
						  " FROM RECONN_NONMETERED,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER  " +
						  " WHERE CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=RECONN_NONMETERED.CUSTOMER_ID   "  +
						  " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID "+
						  (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (   SELECT PID,RECONN_NONMETERED.CUSTOMER_ID,CUSTOMER_PERSONAL_INFO.FULL_NAME,RECONECT_BY,TO_CHAR (RECONNECT_DATE, 'DD-MM-YYYY') RECONNECT_DATE,  " +
					  " RECONN_NONMETERED.REMARKS,DISCONNECT_ID,MST_AREA.AREA_NAME  " +
					  " FROM RECONN_NONMETERED,CUSTOMER_PERSONAL_INFO,MST_AREA,CUSTOMER  " +
					  " WHERE CUSTOMER_PERSONAL_INFO.CUSTOMER_ID=RECONN_NONMETERED.CUSTOMER_ID   "+
					  " AND CUSTOMER.CUSTOMER_ID=CUSTOMER_PERSONAL_INFO.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID "+
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
					reConn=new ReconnectDTO();
					reConn.setPid(r.getString("PID"));
					reConn.setCustomer_id(r.getString("CUSTOMER_ID"));
					reConn.setCustomer_name(r.getString("FULL_NAME"));
					reConn.setReconnect_date(r.getString("RECONNECT_DATE"));
					reConn.setRemarks(r.getString("REMARKS"));
					reConnList.add(reConn);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return reConnList;
	}
	
	public ReconnectDTO getNonMeterReconnectionInfo(String reconnection_id)
	{		
		ReconnectDTO reconn=null;
		DisconnectDTO disconn=null;
		
		Connection conn = ConnectionManager.getConnection();
		
		String sql= "Select RNM.PID RECONNECT_ID,RNM.CUSTOMER_ID,RNM.RECONECT_BY,TO_CHAR(RNM.RECONNECT_DATE,'DD-MM-YYYY') RECONNECT_DATE,  " +
					"RNM.REMARKS Reconn_REMARKS,RNM.DISCONNECT_ID,CPI.FULL_NAME,DNM.DISCONNECT_CAUSE,DNM.DISCONNECT_TYPE,DNM.DISCONNECT_BY,TO_CHAR(DNM.DISCONNECT_DATE,'DD-MM-YYYY') DISCONNECT_DATE,  " +
					"DNM.REMARKS Disconn_REMARKS  " +
					"From RECONN_NONMETERED RNM,DISCONN_NONMETERED DNM,CUSTOMER C,CUSTOMER_PERSONAL_INFO CPI " +
					"Where RNM.PID= ? " +
					"And C.CUSTOMER_ID=CPI.CUSTOMER_ID And C.CUSTOMER_ID=RNM.CUSTOMER_ID And RNM.CUSTOMER_ID=DNM.CUSTOMER_ID " ;
			
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, reconnection_id);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					reconn=new ReconnectDTO();
					disconn=new DisconnectDTO();
					
					disconn.setPid(r.getString("DISCONNECT_ID"));
					disconn.setCustomer_id(r.getString("CUSTOMER_ID"));
					disconn.setDisconnect_cause_name(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getLabel());
					disconn.setDisconnect_cause_str(r.getString("DISCONNECT_CAUSE"));
					disconn.setDisconnect_type_name(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getLabel());
					disconn.setDisconnect_type_str(r.getString("DISCONNECT_TYPE"));
					disconn.setDisconnect_by(r.getString("DISCONNECT_BY"));
					disconn.setDisconnect_by_name(EmployeeService.getEmpNameFromEmpId(r.getString("DISCONNECT_BY")));
					disconn.setDisconnect_date(r.getString("DISCONNECT_DATE"));
					disconn.setRemarks(r.getString("DISCONN_REMARKS"));
					
					reconn.setDisconnectionInfo(disconn);
					
					reconn.setPid(r.getString("RECONNECT_ID"));
					reconn.setReconnect_date(r.getString("RECONNECT_DATE"));
					reconn.setReconnect_by(r.getString("RECONECT_BY"));
					reconn.setRemarks(r.getString("RECONN_REMARKS"));
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return reconn;	 	
	}
	
	public ResponseDTO deleteNonMeterReconnInfo(String reconnect_id)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlReconnInfo="Select * from RECONN_NONMETERED  Where PID=?";						  
		String sqlDeleteReconn="Delete RECONN_NONMETERED Where PID=?";
		String sqlUpdateConn=" Update CUSTOMER_CONNECTION set status=0 where customer_id=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlReconnInfo);
				stmt.setString(1,reconnect_id);				
				ResultSet r =stmt.executeQuery();
				String customer_id="";
				
				if (r.next())
				{
					customer_id=r.getString("CUSTOMER_ID");
				}
				
			
				stmt = conn.prepareStatement(sqlDeleteReconn);
				stmt.setString(1,reconnect_id);
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlUpdateConn);
				stmt.setString(1,customer_id);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Deleted Reconnection Information.");
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

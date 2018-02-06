package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerTypeChangeDTO;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class CustomerTypeChangeService {

	
	public ResponseDTO saveCustomerTypeChangeInfo(CustomerDTO customer)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		String sqlInsert="";
		String burnerInfoSql="";
		String type_change_his_sql="";
		
        if(customer.getConnectionInfo().getIsMetered_str().equals("0"))
        {														
        	sqlInsert ="Update CUSTOMER_CONNECTION set ISMETERED=?,CONNECTION_TYPE=?,HAS_SUB_CONNECTION=?,PARENT_CONNECTION=?,MINISTRY_ID=?,PAY_WITHIN_WO_SC=?,PAY_WITHIN_W_SC=?,SINGLE_BURNER_QNT=?,DOUBLE_BURNER_QNT=?,DOUBLE_BURNER_QNT_BILLCAL=? Where customer_id=?";
        	 burnerInfoSql="INSERT INTO BURNER_QNT_CHANGE (PID, CUSTOMER_ID, OLD_DOUBLE_BURNER_QNT,OLD_DOUBLE_BURNER_QNT_BILLCALL,NEW_DOUBLE_BURNER_QNT, NEW_DOUBLE_BURNER_QNT_BILLCAL,EFFECTIVE_DATE, REMARKS, INSERT_BY,INSERT_DATE) VALUES (SQN_CNG_BURNER_QNT.nextval,?,?,?,?,?,to_date(?,'dd-MM-YYYY'),'Balance Transfer',?,sysdate)";
        }else
        {			
        	sqlInsert ="Update CUSTOMER_CONNECTION set ISMETERED=?,CONNECTION_TYPE=?,HAS_SUB_CONNECTION=?,PARENT_CONNECTION=?,MINISTRY_ID=?,PAY_WITHIN_WO_SC=?,PAY_WITHIN_W_SC=?,MIN_LOAD=?,MAX_LOAD=?,HHV_NHV=?,VAT_REBATE=? Where customer_id=?";
           
        }
	
        type_change_his_sql="INSERT INTO CUSTOMER_TYPE_CHANGE_HISTORY (PID, OLD_METER_STATUS, NEW_METER_STATUS, CHANGE_DATE, REMARKS, INSERTED_BY,CUSTOMER_ID) VALUES ( SQN_TYPE_CHANGE.nextval,?,?,to_date(?,'dd-MM-YYYY'),?,?,?)";
		

		PreparedStatement stmt = null;
			try
			{
				

				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,customer.getConnectionInfo().getIsMetered_str());
				stmt.setString(2,customer.getConnectionInfo().getConnection_type_str());
				stmt.setString(3,customer.getConnectionInfo().getConnection_type_str().equals("0")?"N":"Y");
				stmt.setString(4,customer.getConnectionInfo().getParent_connection());
				stmt.setString(5,customer.getConnectionInfo().getMinistry_id());
				stmt.setDouble(6,customer.getConnectionInfo().getPay_within_wo_sc());
				stmt.setDouble(7,customer.getConnectionInfo().getPay_within_w_sc());
				if(customer.getConnectionInfo().getIsMetered_str().equals("0"))
		        {
					stmt.setDouble(8,customer.getConnectionInfo().getSingle_burner_qnt());
					stmt.setDouble(9,customer.getConnectionInfo().getDouble_burner_qnt());
					stmt.setDouble(10,customer.getConnectionInfo().getDouble_burner_qnt());		
					stmt.setString(11,customer.getCustomer_id());
		        }else
		        {
		        	stmt.setString(8,customer.getConnectionInfo().getMin_load());
					stmt.setString(9,customer.getConnectionInfo().getMax_load());
					stmt.setDouble(10,customer.getConnectionInfo().getHhv_nhv());	
					stmt.setDouble(11,customer.getConnectionInfo().getVat_rebate());
					stmt.setString(12,customer.getCustomer_id());
		        }
				stmt.execute();
				
				if(customer.getConnectionInfo().getIsMetered_str().equals("0"))
		        {
					stmt = conn.prepareStatement(burnerInfoSql);
					stmt.setString(1,customer.getCustomer_id());
					stmt.setInt(2,customer.getConnectionInfo().getDouble_burner_qnt());
					stmt.setInt(3,customer.getConnectionInfo().getDouble_burner_qnt());
					stmt.setInt(4,customer.getConnectionInfo().getDouble_burner_qnt());
					stmt.setInt(5,customer.getConnectionInfo().getDouble_burner_qnt());
					stmt.setString(6,customer.getConnectionInfo().getType_change_date());
					stmt.setString(7,customer.getInserted_by());
					stmt.executeUpdate();
		        }
				
				stmt = conn.prepareStatement(type_change_his_sql);
				stmt.setString(1,customer.getConnectionInfo().getIsMetered_str().equals("0")?"1":"0");
				stmt.setString(2,customer.getConnectionInfo().getIsMetered_str());
				stmt.setString(3,customer.getConnectionInfo().getType_change_date());
				stmt.setString(4,customer.getConnectionInfo().getType_change_remarks());
				stmt.setString(5,customer.getInserted_by());
				stmt.setString(6,customer.getCustomer_id());
				stmt.executeUpdate();
				transactionManager.commit();
				
				response.setMessasge("Successfully Saved Customer Type Change Information.");
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
	
	public ResponseDTO udpateMeterRentChangeInfo(MeterRentChangeDTO mRentChagne)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = ConnectionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
						  
		String sqlInsert=" Update METER_RENT_CHANGE  Set OLD_RENT=?, NEW_RENT=?,EFFECTIVE_DATE=to_date(?,'DD-MM-YYYY'), REMARKS=?  " +
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
	
	public ArrayList<CustomerTypeChangeDTO> getCustomerTypeChangeList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
  		CustomerTypeChangeDTO typeChangeDTO=null;
		ArrayList<CustomerTypeChangeDTO> typeChangeList=new ArrayList<CustomerTypeChangeDTO>();	
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT PID,ctch.CUSTOMER_ID,OLD_METER_STATUS,NEW_METER_STATUS,to_char(CHANGE_DATE,'dd-MM-YYYY') CHANGE_DATE,REMARKS,INSERTED_BY,INSERTED_ON "+						
						" FROM CUSTOMER_TYPE_CHANGE_HISTORY ctch,MVIEW_CUSTOMER_INFO mci where ctch.customer_id=mci.customer_id "+
						(whereClause.equalsIgnoreCase("")?"":(" and (  "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (  SELECT PID,ctch.CUSTOMER_ID,OLD_METER_STATUS,NEW_METER_STATUS,to_char(CHANGE_DATE,'dd-MM-YYYY') CHANGE_DATE,REMARKS,INSERTED_BY,INSERTED_ON "+						
						" FROM CUSTOMER_TYPE_CHANGE_HISTORY ctch,MVIEW_CUSTOMER_INFO mci where ctch.customer_id=mci.customer_id"+
					  (whereClause.equalsIgnoreCase("")?"":(" and ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					typeChangeDTO=new CustomerTypeChangeDTO();
					typeChangeDTO.setPid(r.getString("PID"));
					typeChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					typeChangeDTO.setOld_meter_status(r.getString("OLD_METER_STATUS").equals("0")?"NON-METERED":"METERED");
					typeChangeDTO.setNew_meter_status(r.getString("NEW_METER_STATUS").equals("0")?"NON-METERED":"METERED");
					typeChangeDTO.setChange_date(r.getString("CHANGE_DATE"));
					typeChangeDTO.setRemarks(r.getString("REMARKS"));
					typeChangeDTO.setInserted_by(r.getString("INSERTED_BY"));
					
					
					typeChangeList.add(typeChangeDTO);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return typeChangeList;
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

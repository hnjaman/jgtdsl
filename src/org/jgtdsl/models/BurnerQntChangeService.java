package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.MinistryDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class BurnerQntChangeService {

	public ResponseDTO saveBurnerQntChangeInfo(BurnerQntChangeDTO bqc)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String sqlUpdate="";
		OracleCallableStatement stmt = null;
		int response_code=0;
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		String totalPermanentDisconnBurner= String.valueOf(Integer.valueOf(bqc.getOld_pdisconnected_burner_qnt().equals("")?"0":bqc.getOld_pdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt())-Integer.valueOf(bqc.getNew_reconnected_burner_qnt_permanent().equals("")?"0":bqc.getNew_reconnected_burner_qnt_permanent()));
		String totalTemporaryDisconnBurner=String.valueOf(Integer.valueOf(bqc.getOld_tdisconnected_burner_qnt().equals("")?"0":bqc.getOld_tdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_temporary_disconnected_burner_qnt().equals("")?"0":bqc.getNew_temporary_disconnected_burner_qnt())-Integer.valueOf(bqc.getNew_reconnected_burner_qnt().equals("")?"0":bqc.getNew_reconnected_burner_qnt())+Integer.valueOf(bqc.getOld_tdisconnected_half_burner_qnt().equals("")?"0":bqc.getOld_tdisconnected_half_burner_qnt()));
			if(bqc.getDisconn_type()!=null && bqc.getDisconn_type().equals("03")){
				totalTemporaryDisconnBurner=String.valueOf(Integer.valueOf(totalTemporaryDisconnBurner)-Integer.valueOf(bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt()));
				bqc.setDisconn_type("03");
			}
	
			try
			{
				
				
				//System.out.println("===>>Procedure : [Save_burner_management_info] START");            
	            stmt = (OracleCallableStatement) conn.prepareCall("{ call SAVE_BURNER_MANGEMENT_INFO(?,?,?,?,?,?,?,?,?,?," +
	            		"																			 ?,?,?,?,?,?,?,?,?,?,?)  }");
	            //System.out.println("==>>Procedure : [Save_burner_management_info] END");

	            stmt.setString(1, bqc.getCustomer_id()); 
	            stmt.setString(2, bqc.getOld_double_burner_qnt_billcal()); 
	            stmt.setString(3,bqc.getOld_double_burner_qnt()); 
	            stmt.setString(4,bqc.getNew_double_qnt_billcal().equals("")?"0":bqc.getNew_double_qnt_billcal());
	            stmt.setString(5,bqc.getNew_double_burner_qnt().equals("")?"0":bqc.getNew_double_burner_qnt());
	            stmt.setString(6,bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt());
	            stmt.setString(7,bqc.getNew_temporary_disconnected_burner_qnt().equals("")?"0":bqc.getNew_temporary_disconnected_burner_qnt());
	            stmt.setString(8,bqc.getDisconn_type());
	            stmt.setString(9,bqc.getNew_permanent_disconnected_cause());
	            stmt.setString(10,bqc.getNew_incrased_burner_qnt().equals("")?"0":bqc.getNew_incrased_burner_qnt());
	            stmt.setString(11,bqc.getNew_reconnected_burner_qnt_permanent().equals("")?"0":bqc.getNew_reconnected_burner_qnt_permanent());
	            stmt.setString(12,bqc.getNew_reconnected_burner_qnt().equals("")?"0":bqc.getNew_reconnected_burner_qnt());
	            stmt.setString(13,bqc.getReconnection_cause());
	            stmt.setString(14,bqc.getEffective_date());
	            stmt.setString(15,bqc.getRemarks().replaceAll("\\s+",""));
	            stmt.setString(16,bqc.getInsert_by());
	            stmt.setString(17,totalPermanentDisconnBurner);
	            stmt.setString(18,totalTemporaryDisconnBurner);
	            stmt.setString(19,bqc.getAppliance_id());  
	            stmt.registerOutParameter(20, java.sql.Types.INTEGER);
	            stmt.registerOutParameter(21, java.sql.Types.VARCHAR);
	            
	            stmt.executeUpdate();
	            response_code = stmt.getInt(20);
	            response.setMessasge(stmt.getString(21));
	            if(response_code == 1){
	            	response.setResponse(true);
	            }
	            else{
	            	response.setResponse(false);
	            }
				
				String cKey="CUSTOMER_INFO_"+bqc.getCustomer_id();
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
	
	public ResponseDTO saveNewApplianceInfo(CustomerApplianceDTO app)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
						  
		String insert_sql = "INSERT INTO BURNER_QNT_CHANGE (PID,CUSTOMER_ID,APPLIANCE_TYPE_CODE,NEW_APPLIANCE_QNT,NEW_APPLIANCE_QNT_BILLCAL,EFFECTIVE_DATE,REMARKS,INSERT_BY,INSERT_DATE)" +
				            "VALUES (SQN_CNG_BURNER_QNT.nextval,?,?,?,?,to_date(?,'dd-mm-yyyy'),?,?,sysdate)";
		String insert_appliance_sql="INSERT INTO CUSTOMER_APPLIANCE (CUSTOMER_ID, APPLIANCE_ID, QUANTITY) " +
						  "VALUES (?,?,?)";
		
		
		
		PreparedStatement stmt = null;
		try
		{			
			stmt = conn.prepareStatement(insert_sql);
			
			stmt.setString(1,app.getCustomer_id());
			stmt.setString(2,app.getApplianc_id());
			stmt.setString(3,app.getApplianc_qnt());
			stmt.setString(4,app.getApplianc_qnt());
			stmt.setString(5,app.getEffective_date());
			stmt.setString(6,"'Balance Transfer'");
			stmt.setString(7,app.getInsert_by());
		
			stmt.execute();
			
			
			stmt = conn.prepareStatement(insert_appliance_sql);
			stmt.setString(1,app.getCustomer_id());
			stmt.setString(2,app.getApplianc_id());
			stmt.setString(3,app.getApplianc_qnt());
			stmt.execute();
			
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Saved New Appliance Information.");
			response.setResponse(true);
			
			String cKey="CUSTOMER_INFO_"+app.getCustomer_id();
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
	public ResponseDTO dissconnectRaizer(CustomerApplianceDTO app)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String sqlUpdate="";
		OracleCallableStatement stmt = null;
		int response_code=0;
		
			try
			{
				
				
				//System.out.println("===>>Procedure : [Save_burner_management_info] START");            
	            stmt = (OracleCallableStatement) conn.prepareCall("{ call raizar_disconnection(?,?,?,?,?,?,?,?)  }");
	            //System.out.println("==>>Procedure : [Save_burner_management_info] END");

	            stmt.setString(1, app.getCustomer_id()); 
	            stmt.setString(2, app.getEffective_date()); 
	            stmt.setString(3,app.getDisconn_type()); 
	            stmt.setString(4,app.getDisconnected_cause());
	            stmt.setString(5,app.getRemarks());
	            stmt.setString(6,app.getInsert_by());
	           
	            stmt.registerOutParameter(7, java.sql.Types.INTEGER);
	            stmt.registerOutParameter(8, java.sql.Types.VARCHAR);
	            
	            stmt.executeUpdate();
	            response_code = stmt.getInt(7);
	            response.setMessasge(stmt.getString(8));
	            if(response_code == 1){
	            	response.setResponse(true);
	            }
	            else{
	            	response.setResponse(false);
	            }
				
				String cKey="CUSTOMER_INFO_"+app.getCustomer_id();
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
	
	public ResponseDTO updateBurnerQntChangeInfo(BurnerQntChangeDTO bqc)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String totalPermanentDisconnBurner= String.valueOf(Integer.valueOf(bqc.getOld_pdisconnected_burner_qnt().equals("")?"0":bqc.getOld_pdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_permanent_disconnected_burner_qnt().equals("")?"0":bqc.getNew_permanent_disconnected_burner_qnt()));
		String totalTemporaryDisconnBurner=String.valueOf(Integer.valueOf(bqc.getOld_tdisconnected_burner_qnt().equals("")?"0":bqc.getOld_tdisconnected_burner_qnt())+Integer.valueOf(bqc.getNew_temporary_disconnected_burner_qnt().equals("")?"0":bqc.getNew_temporary_disconnected_burner_qnt())-Integer.valueOf(bqc.getNew_reconnected_burner_qnt().equals("")?"0":bqc.getNew_reconnected_burner_qnt()));;
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
						  
		String sqlUpdate1 = " UPDATE BURNER_QNT_CHANGE SET NEW_DOUBLE_BURNER_QNT=?,NEW_PERMANENT_DISCON_QNT=?,DISCONN_CAUSE=?,NEW_TEMPORARY_DISCONN_QNT=?,"
							+ "NEW_INCREASED_QNT=?,NEW_RECONN_QNT=?,"
							+ "NEW_DOUBLE_BURNER_QNT_BILLCAL=?,TOTAL_TDISCONNECTED_BURNER_QNT=?,TOTAL_PDISCONNECTED_BURNER_QNT=?,"
							+ "EFFECTIVE_DATE=TO_DATE(?, 'DD-MM-YYYY'), REMARKS=? "
							+ "WHERE PID=? ";
		String sqlUpdate2=" Update CUSTOMER_CONNECTION set  DOUBLE_BURNER_QNT=?,DOUBLE_BURNER_QNT_BILLCAL=?,PDISCONNECTED_BURNER_QNT=?,TDISCONNECTED_BURNER_QNT=? where customer_id=?";
		
		
		
		PreparedStatement stmt = null;
		try
		{			
			stmt = conn.prepareStatement(sqlUpdate1);
			
			stmt.setString(1,bqc.getNew_double_burner_qnt());
			stmt.setString(2,bqc.getNew_permanent_disconnected_burner_qnt());
			stmt.setString(3,bqc.getNew_permanent_disconnected_cause());
			stmt.setString(4,bqc.getNew_temporary_disconnected_burner_qnt());
			stmt.setString(5,bqc.getNew_incrased_burner_qnt());
			stmt.setString(6,bqc.getNew_reconnected_burner_qnt());
			stmt.setString(7,bqc.getNew_double_burner_billcal_qnt());
			stmt.setString(8,totalTemporaryDisconnBurner);
			stmt.setString(9,totalPermanentDisconnBurner);
			stmt.setString(10,bqc.getEffective_date());
			stmt.setString(11,bqc.getRemarks());
			stmt.setString(11,bqc.getPid());
			stmt.execute();
			
			
			stmt = conn.prepareStatement(sqlUpdate2);
			stmt.setString(1,bqc.getNew_double_burner_qnt());
			stmt.setString(2,bqc.getNew_double_qnt_billcal());
			stmt.setString(3,totalPermanentDisconnBurner);
			stmt.setString(4,totalTemporaryDisconnBurner);
			stmt.setString(5,bqc.getCustomer_id());
			stmt.execute();
			
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Updated Burner Quantity Change Information.");
			response.setResponse(true);
			
			String cKey="CUSTOMER_INFO_"+bqc.getCustomer_id();
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
	
	public ArrayList<BurnerQntChangeDTO> getBurnerQntChangeList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		BurnerQntChangeDTO bChangeDTO=null;
		ArrayList<BurnerQntChangeDTO> burnerChangeList=new ArrayList<BurnerQntChangeDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		
		//whereClause=whereClause.replace("Area_Id=", "MST_AREA.Area_Id=");
		
		String orderByQuery="";
		whereClause=whereClause.replace("Area_Id", "MST_AREA.Area_Id");
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql=	  "SELECT BQC.PID, " +
						  "       BQC.CUSTOMER_ID, " +
						  "       CPI.FULL_NAME, " +
						  "       BQC.APPLIANCE_TYPE_CODE, " +
						  "       AI.APPLIANCE_NAME, " +
						  "       BQC.OLD_APPLIANCE_QNT, " +
						  "       BQC.NEW_APPLIANCE_QNT, " +
						  "       BQC.NEW_PERMANENT_DISCON_QNT, " +
						  "       BQC.NEW_TEMPORARY_DISCONN_QNT, " +
						  "       BQC.NEW_INCREASED_QNT, " +
						  "       BQC.NEW_RECONN_QNT_4M_TEMPORARY + BQC.NEW_RECONN_QNT_4M_TEMP_HALF " +
						  "       NEW_RECONN_QNT_4M_TEMPORARY, " +
						  "       BQC.NEW_RECONN_QNT_4M_PERMANENT, " +
						  "       BQC.DISCONN_CAUSE, " +
						  "       BQC.TOTAL_TDISCONNECTED_QNT, " +
						  "       BQC.TOTAL_PDISCONNECTED_QNT, " +
						  "       EFFECTIVE_DATE, " +
						  "       TO_CHAR (EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE_VIEW, " +
						  "       REMARKS, " +
						  "       MST_AREA.AREA_ID, " +
						  "       MST_AREA.AREA_NAME " +
						  "  FROM BURNER_QNT_CHANGE BQC, " +
						  "       CUSTOMER_PERSONAL_INFO CPI, " +
						  "       MST_AREA, " +
						  "       CUSTOMER, " +
						  "       APPLIANCE_INFO AI " +
						  " WHERE     BQC.CUSTOMER_ID = CPI.CUSTOMER_ID " +
						  "       AND CUSTOMER.CUSTOMER_ID = CPI.CUSTOMER_ID " +
						  "       AND CUSTOMER.AREA = MST_AREA.AREA_ID " +
						  "       AND BQC.APPLIANCE_TYPE_CODE = AI.APPLIANCE_ID AND  AI.AREA_ID=MST_AREA.Area_Id " +
						  (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
				else

				  sql=   "SELECT * " +
						  "  FROM (SELECT ROWNUM serial, tmp1.* " +
						  "          FROM (  SELECT BQC.PID, " +
						  "                         BQC.CUSTOMER_ID, " +
						  "                         CPI.FULL_NAME, " +
						  "                         BQC.APPLIANCE_TYPE_CODE, " +
						  "                         AI.APPLIANCE_NAME, " +
						  "                         BQC.OLD_APPLIANCE_QNT, " +
						  "                         BQC.NEW_APPLIANCE_QNT, " +
						  "                         BQC.NEW_PERMANENT_DISCON_QNT, " +
						  "                         BQC.NEW_TEMPORARY_DISCONN_QNT, " +
						  "                         BQC.NEW_INCREASED_QNT, " +
						  "                           BQC.NEW_RECONN_QNT_4M_TEMPORARY " +
						  "                         + BQC.NEW_RECONN_QNT_4M_TEMP_HALF " +
						  "                            NEW_RECONN_QNT_4M_TEMPORARY, " +
						  "                         BQC.NEW_RECONN_QNT_4M_PERMANENT, " +
						  "                         BQC.DISCONN_CAUSE, " +
						  "                         BQC.TOTAL_TDISCONNECTED_QNT, " +
						  "                         BQC.TOTAL_PDISCONNECTED_QNT, " +
						  "                         TRUNC (EFFECTIVE_DATE) EFFECTIVE_DATE, " +
						  "                         TO_CHAR (EFFECTIVE_DATE, 'dd-MM-YYYY') " +
						  "                            EFFECTIVE_DATE_VIEW, " +
						  "                         REMARKS, " +
						  "                         MST_AREA.AREA_ID, " +
						  "                         MST_AREA.AREA_NAME " +
						  "                    FROM BURNER_QNT_CHANGE BQC, " +
						  "                         CUSTOMER_PERSONAL_INFO CPI, " +
						  "                         MST_AREA, " +
						  "                         CUSTOMER, " +
						  "                         APPLIANCE_INFO AI " +
						  "                   WHERE     BQC.CUSTOMER_ID = CPI.CUSTOMER_ID " +
						  "                         AND CUSTOMER.CUSTOMER_ID = CPI.CUSTOMER_ID " +
						  "                         AND BQC.APPLIANCE_TYPE_CODE = AI.APPLIANCE_ID AND  AI.AREA_ID=MST_AREA.Area_Id" +
						  "                         AND CUSTOMER.AREA = MST_AREA.AREA_ID " +
						  (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+	
						  "                          " +
						  "               ) tmp1) tmp2 " +
						  " WHERE serial BETWEEN ? AND ? " ;
				
		   
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
					bChangeDTO=new BurnerQntChangeDTO();
					
					bChangeDTO.setPid(r.getString("PID"));
					bChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					bChangeDTO.setCustomer_name(r.getString("FULL_NAME"));
					bChangeDTO.setAppliance_id(r.getString("APPLIANCE_TYPE_CODE"));
					bChangeDTO.setAppliance_name(r.getString("APPLIANCE_NAME"));
					bChangeDTO.setOld_double_burner_qnt(r.getString("OLD_APPLIANCE_QNT"));
					bChangeDTO.setNew_permanent_disconnected_burner_qnt(r.getString("NEW_PERMANENT_DISCON_QNT"));
					bChangeDTO.setNew_temporary_disconnected_burner_qnt(r.getString("NEW_TEMPORARY_DISCONN_QNT"));
					bChangeDTO.setNew_incrased_burner_qnt(r.getString("NEW_INCREASED_QNT"));
					bChangeDTO.setNew_reconnected_burner_qnt(r.getString("NEW_RECONN_QNT_4M_TEMPORARY"));
					bChangeDTO.setNew_reconnected_burner_qnt_permanent(r.getString("NEW_RECONN_QNT_4M_PERMANENT"));
					bChangeDTO.setNew_double_burner_qnt(r.getString("NEW_APPLIANCE_QNT"));
					bChangeDTO.setOld_pdisconnected_burner_qnt(r.getString("TOTAL_PDISCONNECTED_QNT"));
					bChangeDTO.setOld_tdisconnected_burner_qnt(r.getString("TOTAL_TDISCONNECTED_QNT"));
					bChangeDTO.setEffective_date(r.getString("EFFECTIVE_DATE_VIEW"));
					bChangeDTO.setRemarks(r.getString("REMARKS"));

					burnerChangeList.add(bChangeDTO);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return burnerChangeList;
	}
	public BurnerQntChangeDTO getApplianceInfoById(String pid)
	{		
		BurnerQntChangeDTO bqChangeDTO=null;
		Connection conn = ConnectionManager.getConnection();
		
		String sql= "SELECT BQC.PID, BQC.CUSTOMER_ID,CPI.FULL_NAME,BQC.OLD_DOUBLE_BURNER_QNT,BQC.NEW_DOUBLE_BURNER_QNT,BQC.NEW_PERMANENT_DISCON_QNT,BQC.NEW_TEMPORARY_DISCONN_QNT,  " +
					"BQC.OLD_DOUBLE_BURNER_QNT_BILLCALL,BQC.NEW_DOUBLE_BURNER_QNT_BILLCAL,BQC.TOTAL_PDISCONNECTED_BURNER_QNT,BQC.TOTAL_TDISCONNECTED_BURNER_QNT,"+
					" BQC.DISCONN_CAUSE,BQC.NEW_INCREASED_QNT,BQC.NEW_RECONN_QNT,BQC.REMARKS, BQC.INSERT_BY, TO_CHAR (BQC.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE " +
					" FROM BURNER_QNT_CHANGE BQC,CUSTOMER_PERSONAL_INFO CPI " +
					" WHERE BQC.CUSTOMER_ID=CPI.CUSTOMER_ID  and PID= ?";
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, pid);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					bqChangeDTO=new BurnerQntChangeDTO();
					bqChangeDTO.setPid(r.getString("PID"));
					bqChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					bqChangeDTO.setCustomer_name(r.getString("FULL_NAME"));					
					bqChangeDTO.setOld_double_burner_qnt(r.getString("OLD_DOUBLE_BURNER_QNT"));
					bqChangeDTO.setOld_double_burner_qnt_billcal(r.getString("OLD_DOUBLE_BURNER_QNT_BILLCALL"));
					bqChangeDTO.setNew_permanent_disconnected_burner_qnt(r.getString("NEW_PERMANENT_DISCON_QNT"));
					bqChangeDTO.setNew_permanent_disconnected_cause(r.getString("DISCONN_CAUSE"));
					bqChangeDTO.setNew_temporary_disconnected_burner_qnt(r.getString("NEW_TEMPORARY_DISCONN_QNT"));
					bqChangeDTO.setNew_incrased_burner_qnt(r.getString("NEW_INCREASED_QNT"));
					bqChangeDTO.setNew_reconnected_burner_qnt(r.getString("NEW_RECONN_QNT"));
					bqChangeDTO.setNew_double_burner_qnt(r.getString("NEW_DOUBLE_BURNER_QNT"));
					bqChangeDTO.setNew_double_qnt_billcal(r.getString("NEW_DOUBLE_BURNER_QNT_BILLCAL"));
					bqChangeDTO.setOld_pdisconnected_burner_qnt(r.getString("TOTAL_PDISCONNECTED_BURNER_QNT"));
					bqChangeDTO.setOld_tdisconnected_burner_qnt(r.getString("TOTAL_TDISCONNECTED_BURNER_QNT"));
					bqChangeDTO.setEffective_date(r.getString("EFFECTIVE_DATE"));
					bqChangeDTO.setRemarks(r.getString("REMARKS"));
				   
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return bqChangeDTO;	 	
	}	
	public BurnerQntChangeDTO getBurnerQntChangeInfo(String pid)
	{		
		BurnerQntChangeDTO bqChangeDTO=null;
		Connection conn = ConnectionManager.getConnection();
		
		String sql=
				"SELECT BQC.PID, " +
				"       BQC.CUSTOMER_ID, " +
				"       CPI.FULL_NAME, " +
				"       BQC.OLD_APPLIANCE_QNT, " +
				"       BQC.NEW_APPLIANCE_QNT, " +
				"       NVL(BQC.NEW_PERMANENT_DISCON_QNT,0) NEW_PERMANENT_DISCON_QNT, " +
				"       NVL(BQC.NEW_TEMPORARY_DISCONN_QNT,0) NEW_TEMPORARY_DISCONN_QNT, " +
				"       BQC.OLD_APPLIANCE_QNT_BILLCALL, " +
				"       BQC.NEW_APPLIANCE_QNT_BILLCAL, " +
				"       NVL(BQC.TOTAL_PDISCONNECTED_QNT,0) TOTAL_PDISCONNECTED_QNT, " +
				"       NVL(BQC.TOTAL_TDISCONNECTED_QNT,0) TOTAL_TDISCONNECTED_QNT, " +
				"       BQC.DISCONN_TYPE,"+
				"       BQC.DISCONN_CAUSE, " +
				"       BQC.NEW_INCREASED_QNT, " +
				"       BQC.RECONNECTION_CAUSE, " +
				"       NVL(BQC.NEW_RECONN_QNT_4M_TEMPORARY,0) " +
				"       + NVL(BQC.NEW_RECONN_QNT_4M_TEMP_HALF,0) " +
			    "       NEW_RECONN_QNT_4M_TEMPORARY, " +				
				"       NVL(BQC.NEW_RECONN_QNT_4M_PERMANENT,0) NEW_RECONN_QNT_4M_PERMANENT, " +				
				"       BQC.NEW_RECONN_QNT_4M_TEMPORARY, " +
				"       BQC.NEW_RECONN_QNT_4M_PERMANENT, " +
				"       BQC.NEW_RECONN_QNT_4M_TEMP_HALF, " +
				"       BQC.REMARKS, " +
				"       BQC.INSERT_BY, " +
				"       TO_CHAR (BQC.EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE " +
				"  FROM BURNER_QNT_CHANGE BQC, CUSTOMER_PERSONAL_INFO CPI " +
				" WHERE BQC.CUSTOMER_ID = CPI.CUSTOMER_ID AND PID =? " 
;
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, pid);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					bqChangeDTO=new BurnerQntChangeDTO();
					bqChangeDTO.setPid(r.getString("PID"));
					bqChangeDTO.setCustomer_id(r.getString("CUSTOMER_ID"));
					bqChangeDTO.setCustomer_name(r.getString("FULL_NAME"));					
					bqChangeDTO.setOld_double_burner_qnt(r.getString("OLD_APPLIANCE_QNT"));
					bqChangeDTO.setOld_double_burner_qnt_billcal(r.getString("OLD_APPLIANCE_QNT_BILLCALL"));
					bqChangeDTO.setNew_permanent_disconnected_burner_qnt(r.getString("NEW_PERMANENT_DISCON_QNT"));
					bqChangeDTO.setNew_permanent_disconnected_cause(r.getString("DISCONN_CAUSE"));
					bqChangeDTO.setDisconn_type(r.getString("DISCONN_TYPE"));
					bqChangeDTO.setNew_temporary_disconnected_burner_qnt(r.getString("NEW_TEMPORARY_DISCONN_QNT"));
					bqChangeDTO.setNew_incrased_burner_qnt(r.getString("NEW_INCREASED_QNT"));
					bqChangeDTO.setNew_reconnected_burner_qnt(r.getString("NEW_RECONN_QNT_4M_TEMPORARY"));
					bqChangeDTO.setNew_reconnected_burner_qnt_permanent(r.getString("NEW_RECONN_QNT_4M_PERMANENT"));
					bqChangeDTO.setNew_double_burner_qnt(r.getString("NEW_APPLIANCE_QNT"));
					bqChangeDTO.setNew_double_qnt_billcal(r.getString("NEW_APPLIANCE_QNT_BILLCAL"));
					bqChangeDTO.setOld_pdisconnected_burner_qnt(r.getString("TOTAL_PDISCONNECTED_QNT"));
					bqChangeDTO.setOld_tdisconnected_burner_qnt(r.getString("TOTAL_TDISCONNECTED_QNT"));
					bqChangeDTO.setEffective_date(r.getString("EFFECTIVE_DATE"));
					bqChangeDTO.setRemarks(r.getString("REMARKS"));
				   
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return bqChangeDTO;	 	
	}
	
	public ResponseDTO deleteBurnerQntChangeInfo(String burnerQntChangeId)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlChangeInfo="Select * from BURNER_QNT_CHANGE  Where PID=?";						  
		String sqlDeleteInfo="Delete BURNER_QNT_CHANGE Where PID=?";
		String sqlUpdateInfo=" Update CUSTOMER_APPLIANCE set QUANTITY=? Where CUSTOMER_ID=? and APPLIANCE_ID=?";
		
		PreparedStatement stmt = null;
		String old_single_burner="";
		String old_double_burner="";
		String customer_id="";
		String appliance_id="";
			try
			{
				stmt = conn.prepareStatement(sqlChangeInfo);
				stmt.setString(1,burnerQntChangeId);				
				ResultSet r =stmt.executeQuery();
				if (r.next())
				{
					old_double_burner=r.getString("OLD_APPLIANCE_QNT");
					customer_id=r.getString("CUSTOMER_ID");
					appliance_id=r.getString("APPLIANCE_TYPE_CODE");
				}
				
			
				stmt = conn.prepareStatement(sqlDeleteInfo);
				stmt.setString(1,burnerQntChangeId);
				stmt.execute();
				
			
				stmt = conn.prepareStatement(sqlUpdateInfo);
				stmt.setString(1,old_double_burner);
				stmt.setString(2,customer_id);
				stmt.setString(3,appliance_id);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Deleted Appliance Information.");
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
	
	
	public static ArrayList<CustomerApplianceDTO> getAllAppliance()
	{
		CustomerApplianceDTO ministry=null;
		ArrayList<CustomerApplianceDTO> applianceList=new ArrayList<CustomerApplianceDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "select * from APPLIANCE_RATE_HISTORY where APPLIANCE_ID IN (select distinct(APPLIANCE_ID) from APPLIANCE_RATE_HISTORY) order by APPLIANCE_ID";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					ministry=new CustomerApplianceDTO();
					ministry.setApplianc_id(r.getString("APPLIANCE_ID"));
					ministry.setApplianc_name(r.getString("APPLIANCE_NAME"));					
					applianceList.add(ministry);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return applianceList;
	}
	
	
	public static ArrayList<CustomerApplianceDTO> getAllAppliance(String area_id)
	{
		CustomerApplianceDTO ministry=null;
		ArrayList<CustomerApplianceDTO> applianceList=new ArrayList<CustomerApplianceDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql ="SELECT * " +
					"    FROM APPLIANCE_RATE_HISTORY " +
					"   WHERE     (APPLIANCE_ID, SLNO) IN " +
					"                (  SELECT APPLIANCE_ID, MAX (SLNO) " +
					"                     FROM APPLIANCE_RATE_HISTORY " +
					"                    WHERE     AREA_ID = '"+area_id+"' " +
					"                          AND Effective_From <= " +
					"                                 TO_DATE ('01-01-2018', 'dd-mm-YYYY') " +
					"                          AND (   Effective_To IS NULL " +
					"                               OR Effective_To >= " +
					"                                     TO_DATE ('01-01-2018', 'dd-mm-YYYY')) " +
					"                 GROUP BY APPLIANCE_ID) " +
					"         AND AREA_ID = '"+area_id+"' " +
					"ORDER BY APPLIANCE_ID ";

		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					ministry=new CustomerApplianceDTO();
					ministry.setApplianc_id(r.getString("APPLIANCE_ID"));
					ministry.setApplianc_name(r.getString("APPLIANCE_NAME"));					
					applianceList.add(ministry);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return applianceList;
	}
	
	
	public boolean canDeleteApplianceEntry(String pid)
	{		
		Connection conn = ConnectionManager.getConnection();
		String sql="select count(pid) TOTAL from BURNER_QNT_CHANGE " +
				"where pid>? and (CUSTOMER_ID,APPLIANCE_TYPE_CODE) IN (select CUSTOMER_ID,APPLIANCE_TYPE_CODE from BURNER_QNT_CHANGE where pid=?) ";


		PreparedStatement stmt = null;
		ResultSet r = null;
		   
		try
			{
				stmt = conn.prepareStatement(sql);
			    stmt.setString(1, pid);
			    stmt.setString(2, pid);
			    
				r = stmt.executeQuery();
				if (r.next())
				{									
					return r.getInt("TOTAL")>0?false:true;				
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return false;	 	
	}
	
	public ArrayList<CustomerApplianceDTO> getRaizerDisconnectionListInfo(String customer_id)
	{		
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		ArrayList<CustomerApplianceDTO> applianceList=new ArrayList<CustomerApplianceDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";

				sql="SELECT APPLIANCE_TYPE_CODE,APPLIANCE_NAME,NVL(NEW_TEMPORARY_DISCONN_QNT,0)+NVL(NEW_PERMANENT_DISCON_QNT,0) DISCONN_QNT,EFFECTIVE_DATE " +
						"FROM BURNER_QNT_CHANGE BQC,APPLIANCE_INFO AI " +
						"WHERE  BQC.APPLIANCE_TYPE_CODE=AI.APPLIANCE_ID  " +
						"AND RAIZER_DISS_STATUS=1  " +
						"AND CUSTOMER_ID=? " +
						"AND AREA_ID=? " ;



			

		PreparedStatement stmt = null;
		ResultSet r = null;
		CustomerApplianceDTO appliance=null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, customer_id);
				stmt.setString(2, loggedInUser.getArea_id());
				r = stmt.executeQuery();
				while (r.next())
				{				
					appliance=new CustomerApplianceDTO();
					appliance.setApplianc_id(r.getString("APPLIANCE_TYPE_CODE").trim());
					appliance.setApplianc_name(r.getString("APPLIANCE_NAME"));
					appliance.setApplianc_qnt(r.getString("DISCONN_QNT"));					
					appliance.setEffective_date(r.getString("EFFECTIVE_DATE"));
					applianceList.add(appliance);
		
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return applianceList;
	}
	public ResponseDTO reconnectRaizer(CustomerApplianceDTO app)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String sqlUpdate="";
		OracleCallableStatement stmt = null;
		int response_code=0;
		
			try
			{
				
				
				//System.out.println("===>>Procedure : [Save_burner_management_info] START");            
	            stmt = (OracleCallableStatement) conn.prepareCall("{ call raizar_reconnection(?,?,?,?,?,?)  }");
	            //System.out.println("==>>Procedure : [Save_burner_management_info] END");

	            stmt.setString(1, app.getCustomer_id()); 
	            stmt.setString(2, app.getEffective_date()); 
	            stmt.setString(3,app.getRemarks());
	            stmt.setString(4,app.getInsert_by());
	           
	            stmt.registerOutParameter(5, java.sql.Types.INTEGER);
	            stmt.registerOutParameter(6, java.sql.Types.VARCHAR);
	            
	            stmt.executeUpdate();
	            response_code = stmt.getInt(5);
	            response.setMessasge(stmt.getString(6));
	            if(response_code == 1){
	            	response.setResponse(true);
	            }
	            else{
	            	response.setResponse(false);
	            }
				
				String cKey="CUSTOMER_INFO_"+app.getCustomer_id();
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
}

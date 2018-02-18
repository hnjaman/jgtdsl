package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.MeterTypeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

import com.google.gson.Gson;

public class ConnectionService {
	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
	public ResponseDTO saveConnectionInfo(CustomerConnectionDTO connection)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
		String parentConfirmSql="";
		String sql=" MERGE INTO CUSTOMER_CONNECTION USING dual " +
				   " ON (customer_id = ?) " +
				   " WHEN MATCHED THEN " +
				   " UPDATE SET CONNECTION_DATE=to_date(?,'dd-MM-YYYY'),STATUS=? "+
				   " WHEN NOT MATCHED THEN " +
				   " Insert(CUSTOMER_ID, ISMETERED, CONNECTION_TYPE, PARENT_CONNECTION, MIN_LOAD,MAX_LOAD,CONNECTION_DATE, MINISTRY_ID, VAT_REBATE,HHV_NHV,PAY_WITHIN_WO_SC,PAY_WITHIN_W_SC,STATUS,HAS_SUB_CONNECTION)" +
				   " Values(?,?,?,?,?,?,to_date(?,'dd-MM-YYYY HH24:MI'),?,?,?,?,?,?,?) ";
		
		String burnerInfoSql="INSERT INTO BURNER_QNT_CHANGE ( PID, CUSTOMER_ID,NEW_APPLIANCE_QNT,NEW_APPLIANCE_QNT_BILLCAL,EFFECTIVE_DATE, REMARKS,INSERT_BY, INSERT_DATE,OLD_APPLIANCE_QNT,OLD_APPLIANCE_QNT_BILLCALL,APPLIANCE_TYPE_CODE ) VALUES (SQN_CNG_BURNER_QNT.NEXTVAL,?,?,?,to_date(?,'dd-MM-YYYY'),?,?,SYSDATE,?,?,?)";
		//String customer_appliance_sql="INSERT INTO CUSTOMER_APPLIANCE ( CUSTOMER_ID, APPLIANCE_ID,QUANTITY)VALUES (?,?,?)";
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,connection.getCustomer_id());
				stmt.setString(2,connection.getConnection_date());
				stmt.setString(3,connection.getStatus_str());
				
				stmt.setString(4,connection.getCustomer_id());
				stmt.setString(5,connection.getIsMetered_str());
				stmt.setString(6,connection.getConnection_type_str());
				stmt.setString(7,connection.getParent_connection());
				stmt.setString(8,connection.getMin_load());
				stmt.setString(9,connection.getMax_load());
				stmt.setString(10,connection.getConnection_date());
				stmt.setString(11,connection.getMinistry_id());
				stmt.setDouble(12,connection.getVat_rebate());
				stmt.setFloat(13, connection.getHhv_nhv());
				stmt.setInt(14, connection.getPay_within_wo_sc());
				stmt.setInt(15, connection.getPay_within_w_sc());
				stmt.setString(16,connection.getStatus_str());
				stmt.setString(17,"N");
			
				
				
				stmt.executeUpdate();
				if(connection.getParent_connection()!=null && !connection.getParent_connection().equalsIgnoreCase(""))
				{
					 parentConfirmSql="UPDATE CUSTOMER_CONNECTION SET HAS_SUB_CONNECTION = 'Y' WHERE customer_id ='"+connection.getParent_connection()+"'";
					 stmt = conn.prepareStatement(parentConfirmSql);
					 stmt.executeUpdate();
				}
				if(connection.getIsMetered_str().equals("0"))
				{
					String appliance_info_str=connection.getAppliance_info_str();
					if(appliance_info_str.length()>0)
								appliance_info_str=appliance_info_str.substring(0, appliance_info_str.length()-1);
					String[] appliance_arr;
					if(appliance_info_str.equalsIgnoreCase(""))
						appliance_arr=new String[0];
					else 
						appliance_arr=appliance_info_str.split("@");				


					String[] applianceID = new String[appliance_arr.length];
					String[] applianceName = new String[appliance_arr.length];
					String[] applianceQnt = new String[appliance_arr.length];
					for(int i=0;i<appliance_arr.length;i++){
						String[] appliance_detail_arr;
						appliance_detail_arr=appliance_arr[i].split("#");
						applianceID[i]=appliance_detail_arr[0];
						applianceName[i]=appliance_detail_arr[1];
						applianceQnt[i]=appliance_detail_arr[2];
					}
					String dayOfmonth=connection.getConnection_date().substring(0,2);
					stmt = conn.prepareStatement(burnerInfoSql);
					for(int i=0;i<applianceID.length;i++){
						stmt.setString(1,connection.getCustomer_id());
						stmt.setInt(2,Integer.valueOf(applianceQnt[i]));
						stmt.setInt(3,Integer.valueOf(applianceQnt[i]));
						stmt.setString(4,connection.getConnection_date());
						stmt.setString(5,"'Balance Transfer'");
						stmt.setString(6,loggedInUser.getUserName());
						stmt.setInt(7,dayOfmonth.equals("01")?Integer.valueOf(applianceQnt[i]):0);
						stmt.setInt(8,dayOfmonth.equals("01")?Integer.valueOf(applianceQnt[i]):0);
						stmt.setString(9,applianceID[i]);
						stmt.addBatch();

					}
					
					stmt.executeBatch();
					//stmt = conn.prepareStatement(customer_appliance_sql);
//					for(int i=0;i<applianceID.length;i++){
//						
//						stmt.setString(1,connection.getCustomer_id());
//						stmt.setString(2,applianceID[i]);
//						stmt.setInt(3,Integer.valueOf(applianceQnt[i]));
//						stmt.addBatch();
//					}
//					stmt.executeBatch();
				}
					
				//transactionManager.commit();
				response.setMessasge("Successfully Saved Connection Information.");
				response.setResponse(true);
				
				String cKey="CUSTOMER_INFO_"+connection.getCustomer_id();
				CacheUtil.clear(cKey);
				
			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			try {
				transactionManager.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;

	}


	

	public static DisconnectDTO getLatestDisconnectInfo(String customer_id,String meter_id)
	{
		DisconnectDTO disconnInfo=null;
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		if(meter_id==null || meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
			   sql="SELECT disconn_info_nonmetered.*,to_char(DISCONNECT_DATE,'dd-MM-YYYY HH24:MI') DISCONNECT_DATE_FORMATTED " +
			   "  FROM disconn_info_nonmetered " +
			   " WHERE customer_id = ? " +
			   "   AND (pid, disconnect_date) IN ( " +
			   "          SELECT pid, disconnect_date " +
			   "            FROM disconn_info_nonmetered " +
			   "           WHERE customer_id = ? " +
			   "             AND disconnect_date IN (SELECT MAX (disconnect_date) " +
			   "                                       FROM disconn_info_nonmetered " +
			   "                                      WHERE customer_id = ?))";

		else
			sql="SELECT d.*,r.*,to_char(DISCONNECT_DATE,'dd-MM-YYYY HH24:MI') DISCONNECT_DATE_FORMATTED " +
			"  FROM disconn_info_metered d, meter_reading r " +
			" WHERE d.customer_id = ? " +
			"   AND (pid, disconnect_date) IN ( " +
			"          SELECT pid, disconnect_date " +
			"            FROM DISCONN_INFO_METERED " +
			"           WHERE customer_id = ? " +
			"             AND pid IN ( " +
			"                         SELECT MAX (pid) " +
			"                           FROM disconn_info_metered " +
			"                          WHERE customer_id = ? " +
			"                                AND meter_id = ?)) " +
			"   AND d.reading_id = r.reading_id ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(meter_id==null || meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
				{
					stmt.setString(1, customer_id);
					stmt.setString(2, customer_id);
					stmt.setString(3, customer_id);
				}
				else
				{
					stmt.setString(1, customer_id);
					stmt.setString(2, customer_id);
					stmt.setString(3, customer_id);
					stmt.setString(4, meter_id);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					disconnInfo=new DisconnectDTO();
					disconnInfo.setPid(r.getString("PID"));
					disconnInfo.setCustomer_id(r.getString("CUSTOMER_ID"));
					disconnInfo.setDisconnect_cause(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")]);
					disconnInfo.setDisconnect_cause_str(String.valueOf(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getId()));
					disconnInfo.setDisconnect_cause_name(DisconnCause.values()[r.getInt("DISCONNECT_CAUSE")].getLabel());
					disconnInfo.setDisconnect_type(DisconnType.values()[r.getInt("DISCONNECT_TYPE")]);
					disconnInfo.setDisconnect_type_str(String.valueOf(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getId()));
					disconnInfo.setDisconnect_type_name(DisconnType.values()[r.getInt("DISCONNECT_TYPE")].getLabel());
					disconnInfo.setDisconnect_date(r.getString("DISCONNECT_DATE_FORMATTED"));
					disconnInfo.setRemarks(r.getString("REMARKS"));
					disconnInfo.setInsert_by(r.getString("INSERT_BY"));
					disconnInfo.setInsert_date(r.getString("INSERT_DATE"));
					if(meter_id!=null && !meter_id.equalsIgnoreCase(Utils.EMPTY_STRING))
					{
						disconnInfo.setMeter_id(r.getString("METER_ID"));
						disconnInfo.setMeter_reading(r.getString("CURRENT_READING"));
					}
					disconnInfo.toString();
					
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return disconnInfo;
	}
	
	
}

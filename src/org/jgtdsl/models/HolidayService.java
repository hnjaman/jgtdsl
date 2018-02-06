package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.AreaDTO;
import org.jgtdsl.dto.HolidayDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.ZoneDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class HolidayService {
	
	
	public ArrayList<HolidayDTO> getHolidayList(String from,String to)
	{
		HolidayDTO holiday=null;
		ArrayList<HolidayDTO> holidayList=new ArrayList<HolidayDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql=" Select Holiday_Id,HOLIDAY_CAUSE,HOLIDAY_TYPE,to_char(Holiday_Date,'dd-MM-YYYY') Holiday_Date_, " +
				   " DECODE (holiday_type, 1, 'Weekend',2, 'Public Holiday') Holiday_Type_Name, "+
				   " to_char(Holiday_Date,'ddMMYYYY') DATE_ID  " +
				   " ,to_char(Holiday_Date,'Month, YYYY') Month_Year " +
				   " From HOLIDAYS Where TO_DATE(TO_CHAR(HOLIDAY_DATE,'MM-YYYY'),'MM-YYYY')>=TO_DATE(?,'MM-YYYY')  " +
				   " AND TO_DATE(TO_CHAR(HOLIDAY_DATE,'MM-YYYY'),'MM-YYYY')<=TO_DATE(?,'MM-YYYY') order by holiday_date";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, from);
				stmt.setString(2, to);
				r = stmt.executeQuery();
				while (r.next())
				{
					holiday=new HolidayDTO();
					holiday.setHoliday_id(r.getString("Holiday_Id"));
					holiday.setHoliday_cause(r.getString("HOLIDAY_CAUSE"));
					holiday.setHoliday_date(r.getString("Holiday_Date_"));
					holiday.setHoliday_date_id(r.getString("DATE_ID"));
					holiday.setHoliday_type(r.getString("HOLIDAY_TYPE"));
					holiday.setHoliday_type_name(r.getString("Holiday_Type_Name"));					
					holiday.setMonth_year(r.getString("MONTH_YEAR"));
					holidayList.add(holiday);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return holidayList;
	}
	
	public static HolidayDTO getHoliday(String holidayId)
	{
		HolidayDTO holiday=null;
		Connection conn = ConnectionManager.getConnection();
		String sql=" Select Holiday_Id,HOLIDAY_CAUSE,HOLIDAY_TYPE,to_char(Holiday_Date,'dd-MM-YYYY') Holiday_Date_, " +
				   " DECODE (holiday_type, 1, 'Weekend',2, 'Public Holiday') Holiday_Type_Name, "+
				   " to_char(Holiday_Date,'ddMMYYYY') DATE_ID  " +
				   " ,to_char(Holiday_Date,'Month, YYYY') Month_Year " +
				   " From HOLIDAYS Where Holiday_Id=? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, holidayId);
				r = stmt.executeQuery();
				while (r.next())
				{
					holiday=new HolidayDTO();
					holiday.setHoliday_id(r.getString("Holiday_Id"));
					holiday.setHoliday_cause(r.getString("HOLIDAY_CAUSE"));
					holiday.setHoliday_date(r.getString("Holiday_Date_"));
					holiday.setHoliday_date_id(r.getString("DATE_ID"));
					holiday.setHoliday_type(r.getString("HOLIDAY_TYPE"));
					holiday.setHoliday_type_name(r.getString("Holiday_Type_Name"));					
					holiday.setMonth_year(r.getString("MONTH_YEAR"));

				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return holiday;
	}
	
	public static ResponseDTO saveHoliday(HolidayDTO holiday)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int resp_code=0;
		String resp_msg=Utils.EMPTY_STRING;
		try {
       	 
   			//System.out.println("===>>Procedure : [CreateHoliday] START");            
            stmt = (OracleCallableStatement) conn.prepareCall("{ call CreateHoliday(?,?,?,?,?,?,?)  }");
            //System.out.println("==>>Procedure : [CreateHoliday] END");            
            if(holiday.getTo_date()==null || holiday.getTo_date().equalsIgnoreCase(""))
            	holiday.setTo_date(holiday.getFrom_date());
            
            stmt.setString(1, holiday.getFrom_date());
            stmt.setString(2, holiday.getTo_date());
            stmt.setString(3, holiday.getHoliday_cause());
            stmt.setString(4, holiday.getHoliday_type());
            stmt.setString(5, holiday.getWeekDay());

            stmt.registerOutParameter(6, java.sql.Types.INTEGER);
            stmt.registerOutParameter(7, java.sql.Types.VARCHAR);
            stmt.executeUpdate();
            resp_code = stmt.getInt(6);
            resp_msg = stmt.getString(7);
            if(resp_code==1){
			response.setMessasge("Successfully Saved Holiday Information.");
			response.setResponse(true);
            }
            else{
            	response.setMessasge(resp_msg);
        		response.setResponse(false);
            }
		} 
		catch (Exception e){e.printStackTrace();
		response.setMessasge(e.getMessage());
		response.setResponse(false);
		}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
 		
        
        return response;
        

	}
	public static ResponseDTO deleteHoliday(String holidayId)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;

		String sql=" Delete HOLIDAYS Where HOLIDAY_ID in ("+holidayId+")";
			try
			{
				stmt = conn.prepareStatement(sql);
				//stmt.setString(1,holidayId);
				int resp = stmt.executeUpdate();
				if(resp>=1){
					response.setMessasge("Successfully deleted selected holidays.");
					response.setResponse(true);					
				}
			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return response;
	 }
	
	public static ResponseDTO updateHoliday(HolidayDTO holiday)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		String sql=" Update HOLIDAYS Set Holiday_Date=to_date(?,'DD-MM-YYYY'), holiday_cause=?, holiday_type=? Where Holiday_Id=?";
		int resp=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,holiday.getHoliday_date());
				stmt.setString(2,holiday.getHoliday_cause());
				stmt.setString(3,holiday.getHoliday_type());
				stmt.setString(4,holiday.getHoliday_id());
				
				resp = stmt.executeUpdate();
				if(resp==1){
					response.setMessasge("Successfully Saved Holiday Information.");
					response.setResponse(true);					
				}

			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	return response;
	}
}

package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.AreaDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.ZoneDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class AreaService {

	public static ArrayList<AreaDTO> getAreaList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		String cKey="AREA_"+Utils.constructCacheKey(index,offset,total,whereClause,sortFieldName,sortOrder);
		ArrayList<AreaDTO> areaList=CacheUtil.getListFromCache(cKey,AreaDTO.class);
		if(areaList!=null)
			return areaList;
		else
			areaList=new ArrayList<AreaDTO>();
		
		
		AreaDTO area=null;
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from MST_AREA  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from MST_AREA "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	  "    )tmp1 " +
				  	  "    )tmp2   " +
				  	  "  Where serial Between ? and ? ";
		   
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
					area=new AreaDTO();
					area.setArea_id(r.getString("AREA_ID"));
					area.setArea_name(r.getString("AREA_NAME"));
					area.setDescription(r.getString("DESCRIPTION"));
					area.setZones(r.getString("ZONES")==null?"":r.getString("ZONES"));
					area.setZones_name(ZoneService.getZonesNameFromZones(r.getString("ZONES")));
					area.setStatus(r.getInt("STATUS"));
					areaList.add(area);
				}
				CacheUtil.setListToCache(cKey,areaList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return areaList;
	}
	
	public String addArea(String data)
	{
		Gson gson = new Gson();  
		AreaDTO areaDTO = gson.fromJson(data, AreaDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Insert Into MST_AREA Values(?,?,?,?,?)";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,areaDTO.getArea_id());
				stmt.setString(2,areaDTO.getArea_name());
				stmt.setString(3,areaDTO.getDescription());
				stmt.setString(4,areaDTO.getZones());
				stmt.setInt(5,areaDTO.getStatus());
				
				response = stmt.executeUpdate();
				CacheUtil.clearStartWith("AREA_");
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX+AC.MST_AREA);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_CREATE_ERROR_PREFIX+AC.MST_AREA);

	}
	
	public String deleteArea(String areaId)
	{
		 JSONParser jsonParser = new JSONParser();		
		 JSONObject jsonObject=null;
		 String aId=null;;
		 try{
			 jsonObject= (JSONObject) jsonParser.parse(areaId);
		 }
		 catch(Exception ex){
			 ex.printStackTrace();
			 return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());			 
		 }
		aId=(String)jsonObject.get("id");
		Connection conn = ConnectionManager.getConnection();
		String sql=" Delete MST_AREA Where AREA_ID=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,aId);
				response = stmt.executeUpdate();
				CacheUtil.clearStartWith("AREA_");
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		

	 		if(response==1)
		 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX+AC.MST_AREA);
		 	else
		 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_DELETE_ERROR_PREFIX+AC.MST_AREA);

	}
	public String updateArea(String data)
	{
		Gson gson = new Gson();  
		AreaDTO areaDTO = gson.fromJson(data, AreaDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Update MST_AREA Set AREA_NAME=?,DESCRIPTION=?,ZONES=?,STATUS=? Where AREA_ID=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,areaDTO.getArea_name());
				stmt.setString(2,areaDTO.getDescription());
				stmt.setString(3,areaDTO.getZones());
				stmt.setInt(4,areaDTO.getStatus());				
				stmt.setString(5,areaDTO.getArea_id());
				
				response = stmt.executeUpdate();
				CacheUtil.clearStartWith("AREA_");
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX+AC.MST_AREA);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_UPDATE_ERROR_PREFIX+AC.MST_AREA);

	}
	
	
	public String getNextId(String data)
	{  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" select lpad(max(to_number(area_id))+1,2,'0') ID from mst_area";
		String response="";
		PreparedStatement stmt = null;
		ResultSet r = null;
			
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					response=r.getString("ID");
				}
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return Utils.getJsonString(AC.STATUS_OK, response);

	}
	public ArrayList<AreaDTO> getAreaList()
	{
	   return getAreaList(0, 0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}
	
	public ArrayList<ZoneDTO> getZoneList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		ZoneDTO zone=null;
		ArrayList<ZoneDTO> zoneList=new ArrayList<ZoneDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY MST_ZONE."+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from MST_ZONE  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from MST_ZONE  "+(whereClause.equalsIgnoreCase("")?"":(" Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	  "    )tmp1 " +
				  	  "    )tmp2   " +
				  	  "  Where serial Between ? and ? ";
		   
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
					zone=new ZoneDTO();
					zone.setArea_id(r.getString("AREA_ID"));
					zone.setZone_id(r.getString("zone_id"));
					zone.setZone_name(r.getString("zone_name"));
					
					zoneList.add(zone);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return zoneList;
	}
}

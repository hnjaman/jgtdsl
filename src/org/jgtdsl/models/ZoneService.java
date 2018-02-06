package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.jgtdsl.dto.ZoneDTO;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

public class ZoneService {
	
	public ArrayList<ZoneDTO> getZoneList(String areaId)
	{
		ArrayList<ZoneDTO> zoneList=CacheUtil.getListFromCache("ZONE_"+Utils.null2Empty(areaId),ZoneDTO.class);
		if(zoneList!=null)
			return zoneList;
		else
			zoneList=new ArrayList<ZoneDTO>();

		
		ZoneDTO zone=null;
		String sql=Utils.EMPTY_STRING;
		Connection conn = ConnectionManager.getConnection();
		if(Utils.isNullOrEmpty(areaId))
			sql = " Select * from MST_ZONE  Order by View_Order";
		else
			sql = "SELECT   * " +
			"    FROM mst_zone " +
			"   WHERE zone_id IN ( " +
			"               WITH TEST AS " +
			"                    (SELECT zones " +
			"                       FROM mst_area " +
			"                      WHERE area_id = '"+areaId+"') " +
			"               SELECT     REGEXP_SUBSTR (zones, '[^,]+', 1, ROWNUM) SPLIT " +
			"                     FROM TEST " +
			"               CONNECT BY LEVEL <= LENGTH (REGEXP_REPLACE (zones, '[^,]+')) " +
			"                                   + 1) " +
			"ORDER BY view_order ";
		
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					zone=new ZoneDTO();
					zone.setZone_id(r.getString("ZONE_ID"));
					zone.setZone_name(r.getString("ZONE_NAME"));
					zoneList.add(zone);
				}
				CacheUtil.setListToCache("ZONE_"+Utils.null2Empty(areaId),zoneList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		//		zone=new ZoneDTO("N","North");zoneList.add(zone);
			//		zone=new ZoneDTO("S","South");zoneList.add(zone);
			//		zone=new ZoneDTO("E","East");zoneList.add(zone);
			//		zone=new ZoneDTO("W","West");zoneList.add(zone);
		
		return zoneList;
	}
	

	
	public static String getZonesNameFromZones(String zones)
	{
		StringBuilder zonesName= new StringBuilder(Utils.EMPTY_STRING);
		if(!Utils.isNullOrEmpty(zones)){
			HashMap<String, String> zoneMap = getZoneMap();
			String[] zoneArray=zones.split(",");
			for(String zone:zoneArray){
				zonesName.append(zoneMap.get(zone)).append(",");
			}
		}
		return zonesName.toString();
	}
	
	public static HashMap<String, String> getZoneMap(){
		
		HashMap<String, String> zoneMap=(HashMap<String, String>)CacheUtil.getObjFromCache("ZONE_MAP");
		
		if(zoneMap!=null)
			return zoneMap;
		else
			zoneMap=new HashMap<String, String>();
		
		ZoneService zoneService=new ZoneService();
		ArrayList<ZoneDTO> zoneList = zoneService.getZoneList(Utils.EMPTY_STRING);
		for (ZoneDTO zone : zoneList) {
			zoneMap.put(zone.getZone_id(), zone.getZone_name());
		}
		CacheUtil.setObjToCache("ZONE_MAP",zoneMap);
		
		return zoneMap;

	}

}

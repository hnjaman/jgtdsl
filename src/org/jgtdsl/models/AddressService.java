package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

public class AddressService {
	
	public static ArrayList<AddressDTO> getAllDivision()
	{
		ArrayList<AddressDTO> divisionList=CacheUtil.getListFromCache("DIVISION_ALL",AddressDTO.class);
		if(divisionList!=null)
			return divisionList;
		else
			divisionList=new ArrayList<AddressDTO>();
		
		AddressDTO division=null;
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select division_id,division_name from Division order by division_name";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					division=new AddressDTO();
					division.setDivision_id(r.getString("DIVISION_ID"));
					division.setDivision_name(r.getString("DIVISION_NAME"));					
					divisionList.add(division);
				}
				CacheUtil.setListToCache("DIVISION_ALL",divisionList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return divisionList;
	}
	
	public ArrayList<AddressDTO> getDistrict(String divisionId)
	{
		AddressDTO district;
		ArrayList<AddressDTO> districtList=CacheUtil.getListFromCache("DISTRICTS_of_DIV:"+Utils.null2Empty(divisionId),AddressDTO.class);
		if(districtList!=null)
			return districtList;
		else
			districtList=new ArrayList<AddressDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql=Utils.EMPTY_STRING;
		if(Utils.isNullOrEmpty(divisionId))
			sql = " Select DIST_CODE,DIST_NAME,DIST_ID,DIVISION_ID from DISTRICT order by DIST_NAME";
		else
			sql = " Select DIST_CODE,DIST_NAME,DIST_ID,DIVISION_ID from DISTRICT Where Division_Id='"+divisionId+"' order by DIST_NAME";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					district=new AddressDTO();
					district.setDistrict_code(r.getString("DIST_CODE"));
					district.setDistrict_name(r.getString("DIST_NAME"));
					district.setDistrict_id(r.getString("DIST_ID"));
					district.setDivision_id(r.getString("DIVISION_ID"));
					districtList.add(district);
				}
				CacheUtil.setListToCache("DISTRICTS_of_DIV:"+Utils.null2Empty(divisionId),districtList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return districtList;
	}
	
	public ArrayList<AddressDTO> getUpazila(String districtId)
	{
		AddressDTO upazila=null;
		ArrayList<AddressDTO> upazilaList=CacheUtil.getListFromCache("UPAZILAS_of_DIST:"+Utils.null2Empty(districtId),AddressDTO.class);
		if(upazilaList!=null)
			return upazilaList;
		else
			upazilaList=new ArrayList<AddressDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql=Utils.EMPTY_STRING;
		if(Utils.isNullOrEmpty(districtId))
			sql=" Select UPAZILA_ID,DISTRICT_ID,UPAZILA_NAME from UPAZILA order by UPAZILA_NAME";
		else
			sql = " Select UPAZILA_ID,DISTRICT_ID,UPAZILA_NAME from UPAZILA Where district_id='"+districtId+"' order by UPAZILA_NAME";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					upazila=new AddressDTO();
					upazila.setUpazila_id(r.getString("UPAZILA_ID"));
					upazila.setDistrict_id(r.getString("DISTRICT_ID"));
					upazila.setUpazila_name(r.getString("UPAZILA_NAME"));
					upazilaList.add(upazila);
				}
				CacheUtil.setListToCache("UPAZILAS_of_DIST:"+Utils.null2Empty(districtId),upazilaList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return upazilaList;
	}
	

}

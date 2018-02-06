package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.DesignationDTO;
import org.jgtdsl.dto.RoleDTO;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

public class DesignationService {

	public ArrayList<DesignationDTO> getDesignationList()
	{
		ArrayList<DesignationDTO> designationList=CacheUtil.getListFromCache("ALL_DESIGNATION",DesignationDTO.class);
		if(designationList!=null)
			return designationList;
		else
			designationList=new ArrayList<DesignationDTO>();

		
		DesignationDTO designation=null;
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select * from MST_DESIGNATION  Order by VIEW_ORDER ";
		   
		PreparedStatement stmt = null;
		ResultSet r = null;
		try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					designation=new DesignationDTO();
					designation.setDesignation_id(r.getString("DESIGNATION_ID"));
					designation.setDesignation_name(r.getString("DESIGNATION_NAME"));
					designation.setShort_term(r.getString("SHORT_TERM"));
					designation.setView_order(r.getString("VIEW_ORDER"));
					designationList.add(designation);
				}
				CacheUtil.setListToCache("ALL_DESIGNATION",designationList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return designationList;
	}
	
	
}

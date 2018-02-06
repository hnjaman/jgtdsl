package org.jgtdsl.actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.RoleDTO;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

public class RoleService {

	public ArrayList<RoleDTO> getRoleList()
	{
		
		ArrayList<RoleDTO> roleList=CacheUtil.getListFromCache("ALL_ROLE",RoleDTO.class);
		if(roleList!=null)
			return roleList;
		else
			roleList=new ArrayList<RoleDTO>();
		
		RoleDTO role=null;
		Connection conn = ConnectionManager.getConnection();
		String sql=" Select * from MST_ROLE  Order by Role_Name ";
		   
	   PreparedStatement stmt = null;
	   ResultSet r = null;
		try
		{
			stmt = conn.prepareStatement(sql);
			r = stmt.executeQuery();
			while (r.next())
			{
				role=new RoleDTO();
				role.setRole_id(r.getString("ROLE_ID"));
				role.setRole_name(r.getString("ROLE_NAME"));
				roleList.add(role);
			}
			CacheUtil.setListToCache("ALL_ROLE",roleList);
		} 
		catch (Exception e){e.printStackTrace();}
 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}stmt = null;conn = null;}
				
		return roleList;
	}
	
}

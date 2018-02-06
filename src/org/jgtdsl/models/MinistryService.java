package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.MinistryDTO;
import org.jgtdsl.utils.connection.ConnectionManager;

public class MinistryService {

	public static ArrayList<MinistryDTO> getAllMinistry()
	{
		MinistryDTO ministry=null;
		ArrayList<MinistryDTO> ministryList=new ArrayList<MinistryDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = " Select MINISTRY_ID,MINISTRY_NAME from MST_MINISTRY order by Ministry_Name";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					ministry=new MinistryDTO();
					ministry.setMinistry_id(r.getString("MINISTRY_ID"));
					ministry.setMinistry_name(r.getString("MINISTRY_NAME"));					
					ministryList.add(ministry);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return ministryList;
	}
}

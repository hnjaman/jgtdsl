package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.OrgDepartmentDTO;
import org.jgtdsl.dto.OrgDivisionDTO;
import org.jgtdsl.dto.OrgSectionDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

public class OrganizationService {

	public ArrayList<OrgDivisionDTO> getDivisionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		OrgDivisionDTO division=null;
		String cKey="ORG_DIVISION_"+Utils.constructCacheKey(index,offset,total,whereClause,sortFieldName,sortOrder);
		ArrayList<OrgDivisionDTO> divisionList=CacheUtil.getListFromCache(cKey,OrgDivisionDTO.class);
		if(divisionList!=null)
			return divisionList;
		else
			divisionList=new ArrayList<OrgDivisionDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from ORG_DIVISION  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from ORG_DIVISION "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					division=new OrgDivisionDTO();
					division.setDivision_id(r.getString("DIVISION_ID"));
					division.setDivision_name(r.getString("DIVISION_NAME"));
					
					divisionList.add(division);
				}
				CacheUtil.setListToCache(cKey,divisionList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return divisionList;
	}
	
	public String getNextDivisionId()
	{  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" select lpad(max(to_number(DIVISION_ID))+1,2,'0') ID from ORG_DIVISION";
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
	
	public ArrayList<OrgDepartmentDTO> getDepartmentList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		OrgDepartmentDTO department=null;
		String cKey="ORG_DEPT_"+Utils.constructCacheKey(index,offset,total,whereClause,sortFieldName,sortOrder);
		ArrayList<OrgDepartmentDTO> departmentList=CacheUtil.getListFromCache(cKey,OrgDepartmentDTO.class);
		if(departmentList!=null)
			return departmentList;
		else
			departmentList=new ArrayList<OrgDepartmentDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from ORG_DEPARTMENT  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from ORG_DEPARTMENT "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					department=new OrgDepartmentDTO();
					department.setDepartment_id(r.getString("DEPARTMENT_ID"));
					department.setDepartment_name(r.getString("DEPARTMENT_NAME"));
					
					departmentList.add(department);
				}
				CacheUtil.setListToCache(cKey,departmentList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return departmentList;
	}
	
	public String getNextDepartmentId()
	{  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" select lpad(max(to_number(DEPARTMENT_ID))+1,2,'0') ID from ORG_DEPARTMENT";
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
	
	public ArrayList<OrgSectionDTO> getSectionList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		OrgSectionDTO section=null;
		String cKey="ORG_SECTION_"+Utils.constructCacheKey(index,offset,total,whereClause,sortFieldName,sortOrder);
		ArrayList<OrgSectionDTO> sectionList=CacheUtil.getListFromCache(cKey,OrgSectionDTO.class);
		if(sectionList!=null)
			return sectionList;
		else
			sectionList=new ArrayList<OrgSectionDTO>();

		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from ORG_SECTION  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from ORG_SECTION "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					section=new OrgSectionDTO();
					section.setSection_id(r.getString("SECTION_ID"));
					section.setSection_name(r.getString("SECTION_NAME"));
					
					sectionList.add(section);
				}
				CacheUtil.setListToCache(cKey,sectionList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return sectionList;
	}
	
	public String getNextSectionId()
	{  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" select lpad(max(to_number(SECTION_ID))+1,2,'0') ID from ORG_SECTION";
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
}

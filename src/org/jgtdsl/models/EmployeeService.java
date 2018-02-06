package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.jgtdsl.dto.EmployeeDTO;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

public class EmployeeService {
	public static ArrayList<EmployeeDTO> getEmployeeList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		String cKey="EMPLOYEE_"+Utils.constructCacheKey(index,offset,total,whereClause,sortFieldName,sortOrder);
		ArrayList<EmployeeDTO> empList=CacheUtil.getListFromCache(cKey,EmployeeDTO.class);
		if(empList!=null)
			return empList;
		else
			empList=new ArrayList<EmployeeDTO>();


		EmployeeDTO employee=null;
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from MST_EMPLOYEE  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from MST_EMPLOYEE "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					employee=new EmployeeDTO();
					employee.setEmp_id(r.getString("EMP_ID"));
					employee.setArea_id(r.getString("AREA_ID"));
					employee.setFull_name(r.getString("FULL_NAME"));
					employee.setGender(r.getString("GENDER"));
					employee.setDesignation(r.getString("DESIGNATION"));
					employee.setStatus(r.getInt("STATUS"));
					empList.add(employee);

					
				}
				CacheUtil.setListToCache(cKey,empList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return empList;
	}


	public ArrayList<EmployeeDTO> getEmployeeList()
	{
			return getEmployeeList(0, 0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}
	
	public static String getEmpNameFromEmpId(String commaSeperatedIds)
	{
		StringBuilder empNames= new StringBuilder(Utils.EMPTY_STRING);
		if(!Utils.isNullOrEmpty(commaSeperatedIds)){
			HashMap<String, String> employeeMap = getEmployeeMap();
			String[] empIdArray=commaSeperatedIds.split(",");
			for(String employee:empIdArray){
				empNames.append(employeeMap.get(employee.trim())).append(", ");
			}
			if(empNames.length()>0)
				empNames.deleteCharAt(empNames.length()-2);

		}
		return empNames.toString();
	}
	
	public static HashMap<String, String> getEmployeeMap(){
		
		HashMap<String, String> empMap=(HashMap<String, String>)CacheUtil.getObjFromCache("EMPLOYEE_MAP");
		
		if(empMap!=null)
			return empMap;
		else
			empMap=new HashMap<String, String>();
		
		ArrayList<EmployeeDTO> empList = getEmployeeList(0, 0, Utils.EMPTY_STRING, Utils.EMPTY_STRING, Utils.EMPTY_STRING, 0);
		for (EmployeeDTO emp : empList) {
			empMap.put(emp.getEmp_id(), emp.getFull_name());
		}
		CacheUtil.setObjToCache("EMPLOYEE_MAP",empMap);
		
		return empMap;

	}
}

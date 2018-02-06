package org.jgtdsl.actions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.JqGridData;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.UserService;
import org.jgtdsl.utils.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GridRecordFetcher extends BaseAction {
	private static final long serialVersionUID = -361604224171850103L;

	// Total pages
	private Integer total = 0;
	// get how many rows we want to have into the grid - rowNum attribute in the
	// grid
	private Integer rows = 0;
	// Get the requested page. By default grid sets this to 1.
	private Integer page = 0;
	// All Record
	private Integer records = 0;
	// sorting order ascending or descending
	private String sord;
	// get index row - i.e. user click to sort
	private String sidx;
	private boolean _search;
	private String filters;
	private String service;
	private String method;
	private String extraFilter;

	public String execute() {
		int start = rows * page - rows;
		start = start + 1;
				
				
		String where = "";
		if (_search == true && filters != null)
			where = getWhereClause(filters);

		String eFilter = extraFilter(extraFilter);

		if (where.equalsIgnoreCase("") && !eFilter.equalsIgnoreCase(""))
			where = " " + eFilter;
		else if (!where.equalsIgnoreCase("") && !eFilter.equalsIgnoreCase(""))
			where += " And " + eFilter;

		Class[] methodParams = new Class[6];
		methodParams[0] = Integer.TYPE;
		methodParams[1] = Integer.TYPE;
		methodParams[2] = String.class;
		methodParams[3] = String.class;
		methodParams[4] = String.class;
		methodParams[5] = Integer.TYPE;
		List<?> totalList = null;
		List<?> dataList = null;
		try {
			Class<?> cls_obj = Class.forName(this.service);
			Method method = cls_obj.getMethod(this.method, methodParams);
			Object obj = cls_obj.newInstance();
			totalList = (List<?>) method.invoke(obj, start, rows, where, sidx, sord, 0);
			dataList = (List<?>) method.invoke(obj, start, rows, where, sidx, sord, 9);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		int totalCount = totalList.size();
		int totalPages = 0;

		if (totalCount > 0) {
			totalPages = (int) Math.ceil(new Float(totalCount)
					/ new Float(rows));
		} else {
			totalPages = 0;
		}
		if (page > totalPages)
			page = totalPages;

		JqGridData<?> gridData = new JqGridData(totalPages, page, totalCount,dataList);
		// System.out.println("Grid Data: " + gridData.getJsonString());
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("json");
			response.getWriter().write(gridData.getJsonString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getWhereClause(String filterJsonQuery){
		
		//String filter="{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"userId\",\"op\":\"eq\",\"data\":\"ifti\"},{\"field\":\"designation\",\"op\":\"bw\",\"data\":\"Software\"}]}";
		JSONParser parser = new JSONParser();  
		String where="";
		  try {  
		  
		   Object obj = parser.parse(filterJsonQuery);  
		  
		   JSONObject filters = (JSONObject) obj;
		   JSONArray rules = (JSONArray)filters.get("rules");
		   String groupOperation = (String)filters.get("groupOp");
		   List<String> whereArray = new ArrayList<String>();
		   
		   for (Object rule_ : rules) {
			   JSONObject rule = (JSONObject) rule_;
			   String fieldName =(String) rule.get("field");
			   String fieldData =(String) rule.get("data");
			   String op =(String) rule.get("op");
			   String fieldOperation="";
			   
			   if(op.equalsIgnoreCase("eq")){
				  
				   if(fieldName.contains("date"))
					   fieldOperation= " = to_date('"+fieldData+"','dd-MM-YYYY')";
				   else
					   fieldOperation= " = '"+fieldData+"'";  
			   }
			   else if(op.equalsIgnoreCase("ne"))
				   fieldOperation= " != '"+fieldData+"'";
			   
			   else if(op.equalsIgnoreCase("lt")){
				   if(fieldName.contains("date"))
					   fieldOperation= " < to_date('"+fieldData+"','dd-MM-YYYY')";
				   else
				   	   fieldOperation= " < '"+fieldData+"'";
			   }
			   else if(op.equalsIgnoreCase("gt")){
				   if(fieldName.contains("date"))
					   fieldOperation= " > to_date('"+fieldData+"','dd-MM-YYYY')";
				   else
				   	   fieldOperation= " > '"+fieldData+"'";
			   }
			   
			   else if(op.equalsIgnoreCase("le")){
				   if(fieldName.contains("date"))
					   fieldOperation= " <= to_date('"+fieldData+"','dd-MM-YYYY')";
				   else
					   fieldOperation= " <= '"+fieldData+"'";
			   }
			   else if(op.equalsIgnoreCase("ge")){
				   if(fieldName.contains("date"))
					   fieldOperation= " >= to_date('"+fieldData+"','dd-MM-YYYY')";
				   else
					   fieldOperation= " >= '"+fieldData+"'";				   
			   }
			   else if(op.equalsIgnoreCase("eqMonth")){
				 
					   fieldOperation= " = "+fieldData+"";				   
			   }
			   else if(op.equalsIgnoreCase("eqYear")){
					 
				   fieldOperation= " = '"+fieldData+"'";				   
		       }
			   
			   else if(op.equalsIgnoreCase("nu"))
				   fieldOperation= " = '' ";
			   else if(op.equalsIgnoreCase("nn"))
				   fieldOperation= " != '' ";
			   else if(op.equalsIgnoreCase("in"))
				   fieldOperation= " IN ("+fieldData+")";
			   else if(op.equalsIgnoreCase("ni"))
				   fieldOperation= " NOT IN '"+fieldData+"";
			   else if(op.equalsIgnoreCase("bw"))
				   {fieldName="lower("+fieldName+")";fieldOperation= " LIKE lower('"+fieldData+"%')";}
			   else if(op.equalsIgnoreCase("bn"))
			   {fieldName="lower("+fieldName+")";fieldOperation= " NOT LIKE lower('"+fieldData+"%')";}
			   else if(op.equalsIgnoreCase("ew"))
			   {fieldName="lower("+fieldName+")";fieldOperation= " LIKE lower('%"+fieldData+"')";}
			   else if(op.equalsIgnoreCase("en"))
			   {fieldName="lower("+fieldName+")";fieldOperation= " Not LIKE lower('%"+fieldData+"')";}
			   else if(op.equalsIgnoreCase("cn"))
			   {fieldName="lower("+fieldName+")";fieldOperation= " LIKE lower('%"+fieldData+"%')";}
			   else if(op.equalsIgnoreCase("nc"))
			   {fieldName="lower("+fieldName+")";fieldOperation= " NOT LIKE lower('%"+fieldData+"%')";}
			   
			   if(fieldOperation != "") {				   
					   whereArray.add(fieldName+" "+fieldOperation);
			   }
		   }
		   String[] array = whereArray.toArray(new String[whereArray.size()]);
		   if (whereArray.size()>0) {
			   where += Utils.join(" "+groupOperation+" ", array);
	        } else {
	            where = "";
	        }
		   
		   //System.out.println(where);
		  }
		  catch(Exception ex)
		  {
			  ex.printStackTrace();
		  }
		  
		  return where;
	}

	public String extraFilter(String filterType) {

		if (filterType == null)
			return "";

		if (filterType.equalsIgnoreCase("area")) {
			return "Area_Id='" + ((UserDTO) session.get("user")).getArea_id()
					+ "'";
		}

		return "";
	}

	/*
	 * public String getServerMessage(){ String
	 * message="{\"status\":\"ERROR\",\"message\":\"Hello Ifti\"}";
	 * HttpServletResponse response = ServletActionContext.getResponse(); try{
	 * response.setContentType("json"); response.getWriter().write(message); }
	 * catch(Exception e) {e.printStackTrace();}
	 * 
	 * return null; }
	 */
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRecords() {
		return records;
	}

	public void setRecords(Integer records) {
		this.records = records;

		if (this.records > 0 && this.rows > 0) {
			this.total = (int) Math.ceil((double) this.records
					/ (double) this.rows);
		} else {
			this.total = 0;
		}
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public boolean is_search() {
		return _search;
	}

	public void set_search(boolean search) {
		_search = search;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getExtraFilter() {
		return extraFilter;
	}

	public void setExtraFilter(String extraFilter) {
		this.extraFilter = extraFilter;
	}

}

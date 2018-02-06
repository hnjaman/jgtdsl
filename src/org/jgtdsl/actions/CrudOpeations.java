package org.jgtdsl.actions;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.utils.AC;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CrudOpeations extends BaseAction implements SessionAware{

	private static final long serialVersionUID = -9132058336255818952L;
	private Map<String,Object> session;
    private String data;
    private String service;
    private String method;
    private String id;

     public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
     public String execute()
     {
		JSONObject json=null;
		Class[] methodParams = new Class[1];	
        methodParams[0] = String.class;   
        
		try{
				json = (JSONObject)new JSONParser().parse(data);
	            Class<?> cls_obj = Class.forName(this.service);
	            Method method = cls_obj.getMethod(this.method,methodParams);
	            Object obj = cls_obj.newInstance();
	            String resp=(String)method.invoke(obj,data);
	            setJsonResponse(resp);
	   	        
		}
		catch(Exception ex){
			 setJsonResponse(AC.STATUS_ERROR,ex.getMessage());		}
             return null;
     }
     
	public static void main(String args[]){
		//JSONObject json=(JSONObject)new JSONParser().parse("{id:10}");
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map getSession() {
		return session;
	}
	public void setSession(Map session) {
		this.session = session;
	}
}

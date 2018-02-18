package org.jgtdsl.actions;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.jgtdsl.dto.AutoCompleteObject;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.AreaService;
import org.jgtdsl.models.BankBranchService;
import org.jgtdsl.models.BurnerQntChangeService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.UserService;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;

import com.opensymphony.xwork2.ActionContext;

public class CheckValidity extends BaseAction{


	private static final long serialVersionUID = -3942507757291865362L;
	private static final String failedResult="failed";
	private static final String successResult="authenticated";
	private static final String resetPassword="changePassword";
	
	private UserDTO user;
	private UserService userService;
	private String defaultUrl;
	private String theme;
	private String service;
    private String method;
    private String fieldValue1;
    private String fieldValue2;
    private String humanReadableFieldName1;
    private String area;
    
    private static final Logger logger = LogManager.getLogger(CheckValidity.class.getName());
    
	public String execute()
	{		
		if(this.isPostRequest() && user.getUserId()!=null && user.getPassword()!=null)
		{
			userService=new UserService();
			UserDTO loggedInUser=userService.validateLogin(user.getUserId(), user.getPassword());
			
			if(loggedInUser!=null)
			{
				if(session instanceof SessionMap)
				{
					((SessionMap<String,Object>)session).invalidate();
					session=ActionContext.getContext().getSession();
				}
				session.put("authenticated", new Boolean(true));
				session.put("user", loggedInUser);
				session.put("role", loggedInUser.getRole_name());
				setTheme(loggedInUser);
				//check here for bank list
				//if is added for admin role who can access all banks of all area ~Prince~Feb 17
				if(loggedInUser.getDesignation_id().equals("02")){
					session.put("USER_BANK_LIST", BankBranchService.getBankList(0,0,"bank.status=1 "+"","bank.AREA_ID","ASC",999999));				
					session.put("USER_AREA_LIST", userService.getUserAreaList(loggedInUser.getUserId()));
					session.put("USER_AREA", AreaService.getAreaList(0,0,"status=1 and AREA_ID='"+loggedInUser.getArea_id()+"'",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
				}else{
					session.put("USER_BANK_LIST", BankBranchService.getBankList(0,0,"bank.status=1 and bank.AREA_ID='"+loggedInUser.getArea_id()+"'","BANK_NAME","ASC",0));				
					session.put("USER_AREA_LIST", userService.getUserAreaList(loggedInUser.getUserId()));
					session.put("USER_AREA", AreaService.getAreaList(0,0,"status=1 and AREA_ID='"+loggedInUser.getArea_id()+"'",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
				}

				session.put("ALL_APPLIANCE",BurnerQntChangeService.getAllAppliance(loggedInUser.getArea_id()));
				
				
				//ServletActionContext.getServletContext().setAttribute("USER_AREA_LIST_"+loggedInUser.getArea_id(),AreaService.getAreaList(0,0,"status=1 and AREA_ID='"+loggedInUser.getArea_id()+"'",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
				//ServletActionContext.getServletContext().setAttribute("USER_BANK_LIST",BankBranchService.getBankList(0,0,"bank.status=1 and AREA_ID='"+loggedInUser.getArea_id()+"'","BANK_NAME","ASC",0));				
				
				
				logger.debug("User <<"+loggedInUser.getUserId()+">> | <<"+loggedInUser.getRole_name()+">> Successfully Logged in");
				
				if(user.getPassword().equalsIgnoreCase(Utils.USER_DEFAULT_PASSWORD)){
					user.setOld_password(user.getPassword());
					return resetPassword;
				}
				else					
					return successResult;
					
			}
		}

		
		addFieldError( "err_login", "Invalid Userid or Password." );
		return failedResult;
	}
	
	public String setUserArea(){
		HttpServletResponse response = ServletActionContext.getResponse();
		session.put("USER_BANK_LIST", BankBranchService.getBankList(0,0,"bank.status=1 and AREA_ID='"+area+"'","BANK_NAME","ASC",0));				
		session.put("USER_AREA", AreaService.getAreaList(0,0,"status=1 and AREA_ID='"+area+"'",Utils.EMPTY_STRING,Utils.EMPTY_STRING,0));
		
		UserDTO loggedInUser= (UserDTO)session.get("user");
		loggedInUser.setArea_id(area);		
		session.put("user", loggedInUser);
		
		try{
			response.setContentType("text/html");
	    	 response.getWriter().write("success");
	          }
	        catch(Exception e) {e.printStackTrace();}
	        	        	    
	     
		return null;
	}
	
	
	//Common method to validate an id for duplicacy.
	//User will provide the Service, Method and value(Which it want to validate)
	//humanReadableFieldName1 is the human Readable Field which will be shown in the return message.
	
	public String validateId(){
		Class[] methodParams = new Class[1];
		methodParams[0] = String.class;
		boolean valid=false;
		try{
            Class<?> cls_obj = Class.forName(this.service);
            Method method = cls_obj.getMethod(this.method,methodParams);
            Object obj = cls_obj.newInstance();
            valid=(Boolean) method.invoke(obj,fieldValue1);
	
        }
        catch(Exception ex){
        	ex.printStackTrace();
        }
        try{
	    	 response.setContentType("json");
	    	 response.getWriter().write(Utils.getJsonString(valid==true?"OK":"ERROR", valid==true?"":"Given "+humanReadableFieldName1+" already exist in the System."));
	          }
	        catch(Exception e) {e.printStackTrace();}
	        
	        
	     
	     
		return null;
	}
	
	public void setTheme(UserDTO user){
		defaultUrl=user.getDefault_url();
		String[] urlArray=defaultUrl.split("\\?");
		String[] paramArray=null;
		if(urlArray.length>0){
			defaultUrl=urlArray[0];
			paramArray=urlArray[1].split("=");
		}
		theme=paramArray[1];
	}
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}
	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}
	
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getFieldValue1() {
		return fieldValue1;
	}


	public void setFieldValue1(String fieldValue1) {
		this.fieldValue1 = fieldValue1;
	}

	public String getFieldValue2() {
		return fieldValue2;
	}


	public void setFieldValue2(String fieldValue2) {
		this.fieldValue2 = fieldValue2;
	}

	public String getHumanReadableFieldName1() {
		return humanReadableFieldName1;
	}

	public void setHumanReadableFieldName1(String humanReadableFieldName1) {
		this.humanReadableFieldName1 = humanReadableFieldName1;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
}

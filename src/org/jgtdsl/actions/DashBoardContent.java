package org.jgtdsl.actions;

import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.utils.Utils;

import com.opensymphony.xwork2.ActionContext;


public class DashBoardContent extends BaseAction implements SessionAware{

	private static final long serialVersionUID = 4465283622425381257L;
	private Map<String,Object> session;
	private String defaultUrl;
	private String theme;
	
	public String execute(){
		
		this.session=ActionContext.getContext().getSession();
		String role=(String)session.get("role");		
		return role.replaceAll("\\s","-").toLowerCase();
	}
	
	public String dashBoard(){
		
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		defaultUrl=loggedInUser.getDefault_url();
		String[] urlArray=defaultUrl.split("\\?");
		String[] paramArray=null;
		if(urlArray.length>0){
			defaultUrl=urlArray[0];
			paramArray=urlArray[1].split("=");
		}
		if(Utils.isNullOrEmpty(theme))
			theme=paramArray[1];
		
		return SUCCESS;
		
	}

	public Map getSession() {
		return session;
	}
	public void setSession(Map session) {
		this.session = session;
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
	
}


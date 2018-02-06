package org.jgtdsl.actions;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.IpAddressDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements Serializable,SessionAware,ServletRequestAware,ServletContextAware {
	   
	private static final long serialVersionUID = 2095420014308409079L;    
    protected String msg;
    protected Map<String,Object> session;
    public HttpServletResponse response = ServletActionContext.getResponse();
    public HttpServletRequest request;
    public IpAddressDTO ipAddressDTO = new IpAddressDTO();
    
    /* In the execute() method of your Strust 2 action,
     * this method can be called to ensure a POST request
     * was used.  For example:
     * public String execute() {
     *      if(isPostRequest()) {
     *          //do some stuff
     *          return SUCCESS;
     *      }
     *      return ERROR;
     */

    public HttpServletRequest getRequest() {
		return this.request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}


	public boolean isPostRequest() {
		HttpServletRequest req = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		if(req!=null && req.getMethod().equalsIgnoreCase("post"))
			return true;
		return false;
	}
    

	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
    
	
	public void setJsonResponse(ResponseDTO resp){
		
		 try{
 	    	 response.setContentType("json");
 	    	 response.getWriter().write(Utils.getJsonString(resp.isResponse()==true?AC.STATUS_OK:AC.STATUS_ERROR,resp.getMessasge(), resp.getDialogCaption()));
 	          }
 	        catch(Exception e) {e.printStackTrace();}
	}
	
	public void setJsonResponse(String status,String msg){
		
		 try{
  	    	 response.setContentType("json");
  	    	 response.getWriter().write(Utils.getJsonString(status, msg));
  	          }
  	        catch(Exception e) {e.printStackTrace();}
	}
	
	public void setJsonResponse(String jsonSTring){
		
		 try{
  	    	 response.setContentType("json");
  	    	 response.getWriter().write(jsonSTring);
  	          }
  	        catch(Exception e) {e.printStackTrace();}
	}
	
	public void setTextResponse(String textString){
		
		 try{
	    	 response.setContentType("html/text");
	    	 response.getWriter().write(textString);
	          }
	        catch(Exception e) {e.printStackTrace();}
	}
	public void setHtmlResponse(String textString){
		
		 try{
	    	 response.setContentType("text/html");
	    	 response.getWriter().write(textString);
	          }
	        catch(Exception e) {e.printStackTrace();}
	}
	
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Map getSession() {
		return session;
	}
	
	//public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	//}
	
	@SuppressWarnings("unchecked")
	  public void setSession(Map session) {
//	    this.session = session;
	    this.session = ActionContext.getContext().getSession();
	  }


	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		 request = arg0;
		 setIpAddressDTO();
	}


	public void setServletContext(ServletContext arg0) {
		// TODO Auto-generated method stub
		
	}
	
	  public IpAddressDTO getIpAddressDTO() {
		return ipAddressDTO;
	}

	public void setIpAddressDTO(IpAddressDTO ipAddressDTO) {
		this.ipAddressDTO = ipAddressDTO;
	}

	public void setIpAddressDTO() {

		    ipAddressDTO.setxForward(request.getHeader("X-Forwarded-For") == null ? "" : request.getHeader("X-Forwarded-For"));
		    ipAddressDTO.setVia(request.getHeader("Via") == null ? "" : request.getHeader("Via"));
		    ipAddressDTO.setRemoteAddress(request.getRemoteAddr() == null ? "" : request.getRemoteAddr());

		  }
	
	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

}

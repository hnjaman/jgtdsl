package org.jgtdsl.actions;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.UserService;
import org.jgtdsl.utils.Utils;

public class UserAccount extends BaseAction implements SessionAware{

	private static final long serialVersionUID = 1774430713063109575L;
	private UserDTO user;
	
	public String changePasswordHome()
	{
		return SUCCESS;
	}
	public String changePassword()
	{
		UserService userService=new UserService();
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		
		if(Utils.isNullOrEmpty(user.getUserId()))
			user.setUserId(loggedInUser.getUserId());
		
		ResponseDTO response=userService.checkPasswordChangePrecondition(user);
		if(response.isResponse()==true)
			response=userService.changePassword(user);
			
		setJsonResponse(response);
		return null;
	}
	public String changeTheme()
	{
		UserService userService=new UserService();
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		ResponseDTO response=userService.changeTheme(loggedInUser.getUserId(), user.getDefault_url());
			
		setJsonResponse(response);
		return null;
	}
	public String updateAccountInfo()
	{
		UserService userService=new UserService();
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		user.setUserId(loggedInUser.getUserId());
		ResponseDTO response=userService.updateAccountInfo(user);
		if(response.isResponse()==true)
			userService.updateLoggedInUserAccountInfo(user);
		setJsonResponse(response);
		return null;
	}
	
	public String resetUserPassword()
	{
		UserService userService=new UserService();
		ResponseDTO response=userService.resetPassword(user);
		setJsonResponse(response);
		return null;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	
}

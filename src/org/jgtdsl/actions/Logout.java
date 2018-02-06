package org.jgtdsl.actions;

import org.apache.struts2.dispatcher.SessionMap;

import com.opensymphony.xwork2.ActionContext;

public class Logout extends BaseAction
{
	
	private static final long serialVersionUID = 676279166081085432L;
	
	public String execute()
	{
		if(session instanceof SessionMap)
		{
			((SessionMap<String,Object>)session).clear();
			((SessionMap<String,Object>)session).invalidate();
			this.session=ActionContext.getContext().getSession();
		}
		return "success";
	}
	
}

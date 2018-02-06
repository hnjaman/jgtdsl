package org.jgtdsl.actions;


public class Login extends BaseAction
{

	private static final long serialVersionUID = 676279166081085432L;
	
	public String execute()
	{
		
		
		Object authenticationObject = session.get("authenticated");
		
		if((authenticationObject!=null && authenticationObject instanceof Boolean && authenticationObject.equals(Boolean.TRUE)))			
			return "alreadyLoggedIn";
		else
			return "newLogin";
	}

	
}

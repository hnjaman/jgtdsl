package org.jgtdsl.interceptors;


import java.util.Collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.TextParseUtil;
import java.io.Serializable;

public class RolesInterceptor extends AbstractInterceptor implements Serializable {
    private static final long serialVersionUID = 1251620329914521805L;

	public static final String roleSessionField = "role";
	private Map<String, Set> roleActions = Collections.EMPTY_MAP;
	private static final String AuthorizationRequiredResult = "authorization_required";
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final String actionName = invocation.getProxy().getActionName();
		Map session = invocation.getInvocationContext().getSession();
		
		Object userRole = session.get(RolesInterceptor.roleSessionField);
        
		if(hasSufficientRole(userRole, actionName)) {
			return invocation.invoke();
        }
		
		return AuthorizationRequiredResult;
		
	}

	public void setRoleActions(String roleActionsParam) {
		StringTokenizer roleActionsParamTokenizer = new StringTokenizer(roleActionsParam,";");
		this.roleActions=new HashMap<String, Set>(roleActionsParamTokenizer.countTokens());
		
		while(roleActionsParamTokenizer.hasMoreTokens()) {
			String roleActionArray[] = roleActionsParamTokenizer.nextToken().trim().split(":");
			
			if(roleActionArray.length == 2) {
				String role = roleActionArray[0].toLowerCase();
				Set actions = TextParseUtil.commaDelimitedStringToSet(roleActionArray[1]);
				this.roleActions.put(role,actions);
			}
		}
	}
	
	public boolean hasSufficientRole(Object userRole, String actionName) {		
		
		if(roleActions.containsKey("*") && roleActions.get("*").contains(actionName))
			return true;
		
		if(userRole !=null && userRole instanceof String) {
			String userRoleString = ((String)userRole).toLowerCase();
			
			if(roleActions.containsKey(userRoleString) && 
					roleActions.get(userRoleString).contains(actionName))
				return true;
		}
		
		return false;
	}
	
	public String getRoleSessionField() {
		return roleSessionField;
	}

}

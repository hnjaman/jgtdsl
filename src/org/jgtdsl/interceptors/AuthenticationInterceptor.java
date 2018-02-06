package org.jgtdsl.interceptors;


import java.util.Collections;
import java.util.Map;
import java.util.Set;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.TextParseUtil;
import java.io.Serializable;

public class AuthenticationInterceptor extends AbstractInterceptor implements Serializable {
    private static final long serialVersionUID = -1784784153643651554L;

	public static final String authenticationSessionField = new String("authenticated");
	private static final String authenticationRequiredResult = "authentication_required";
	private Set excludeActions = Collections.EMPTY_SET;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map session = invocation.getInvocationContext().getSession();
		String actionName = invocation.getProxy().getActionName();
		
		Object authenticationObject = session.get(authenticationSessionField);
		
		if(excludeActions.contains(actionName) || 
				(authenticationObject!=null && authenticationObject instanceof Boolean &&
					authenticationObject.equals(Boolean.TRUE))) {
			
			return invocation.invoke();	
		}
		
		return authenticationRequiredResult;
		
	}

	public void setExcludeActions(String values) {
		if (values != null) {
			this.excludeActions = TextParseUtil.commaDelimitedStringToSet(values);
		}
	}
	
}

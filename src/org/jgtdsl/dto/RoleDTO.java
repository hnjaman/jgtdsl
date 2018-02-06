package org.jgtdsl.dto;

import com.google.gson.Gson;

public class RoleDTO {
	
	private String role_id;
	private String role_name;
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String roleId) {
		role_id = roleId;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String roleName) {
		role_name = roleName;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }


}

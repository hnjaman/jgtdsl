package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MeterTypeDTO {

	private String type_id;
	private String type_name;
	private String description;
	private int status;
	private int view_order;
	
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String typeId) {
		type_id = typeId;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String typeName) {
		type_name = typeName;
	}
	public String getDescription() {
		//return description;
		return description.replaceAll("[\\r\\n]+", "<br/>");
	}
	public void setDescription(String description) {
		this.description = description==null?"":description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getView_order() {
		return view_order;
	}
	public void setView_order(int viewOrder) {
		view_order = viewOrder;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	/*
	public String toString() {
        return "{\"type_id\":\"" + type_id + "\", \"type_name\":\"" + type_name + "\", \"description\":\"" + description.replaceAll("[\\r\\n]+", "<br/>") + "\", \"status\":\"" + status + "\"}";
    }
    */

}

package org.jgtdsl.dto;

public class DepositTypeDTO {
	private String type_id;
	private String type_name_eng;
	private String type_name_ban;
	private String description;
	private String view_order;
	private String status;
	
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String typeId) {
		type_id = typeId;
	}
	public String getType_name_eng() {
		return type_name_eng;
	}
	public void setType_name_eng(String typeNameEng) {
		type_name_eng = typeNameEng;
	}
	public String getType_name_ban() {
		return type_name_ban;
	}
	public void setType_name_ban(String typeNameBan) {
		type_name_ban = typeNameBan;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getView_order() {
		return view_order;
	}
	public void setView_order(String viewOrder) {
		view_order = viewOrder;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString() {
        return "{\"type_id\":\"" + type_id + "\", \"type_name_eng\":\"" + type_name_eng + "\", \"description\":\"" + (description==null?"":description.replaceAll("[\\r\\n]+", "<br/>")) + "\", \"view_order\":\"" + view_order + "\",\"status\":\"" + status + "\"}";
    }

}
package org.jgtdsl.dto;

import com.google.gson.Gson;

public class CustomerGridDTO {
	
	private String customer_id;
	private String full_name;
	private String father_name;
	private String area_name;
	private String category_name;
	private String category_id;
	//Actually it is the connection status
	private String connection_status_name;
	private String connection_status;
	private String mobile;
	private String created_on;
	private String phone;
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String fullName) {
		full_name = fullName;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String areaName) {
		area_name = areaName;
	}
	
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String categoryName) {
		category_name = categoryName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}	
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String categoryId) {
		category_id = categoryId;
	}

	public String getConnection_status_name() {
		return connection_status_name;
	}
	public void setConnection_status_name(String connectionStatusName) {
		connection_status_name = connectionStatusName;
	}

	public String getConnection_status() {
		return connection_status;
	}
	public void setConnection_status(String connectionStatus) {
		connection_status = connectionStatus;
	}
	
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String createdOn) {
		created_on = createdOn;
	}
	
	public String getFather_name() {
		return father_name;
	}
	public void setFather_name(String fatherName) {
		father_name = fatherName;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }

	
	
}

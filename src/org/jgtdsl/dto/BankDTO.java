package org.jgtdsl.dto;

import java.io.Serializable;

public class BankDTO implements Serializable{

	private static final long serialVersionUID = 7131319823229734248L;
	private String bank_id;
	private String bank_name;
	private String address;
	private String phone;
	private String fax;
	private String email;
	private String url;
	private String description;
	private int status;
	
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bankId) {
		bank_id = bankId;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bankName) {
		bank_name = bankName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String toString() {
        return "{\"bank_id\":\"" + bank_id + "\", \"bank_name\":\"" + bank_name + "\", \"address\":\"" + address + "\",\"phone\":\"" + phone + "\",\"fax\":\"" + fax + "\", \"email\":\"" + email + "\", \"url\":\"" + url + "\", \"description\":\"" + (description==null?"":description.replaceAll("[\\r\\n]+", "<br/>")) + "\", \"status\":\"" + status + "\"}";
    }
}

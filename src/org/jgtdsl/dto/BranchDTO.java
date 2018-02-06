package org.jgtdsl.dto;

public class BranchDTO {

	private String area_id;
	private String bank_id;
	private String branch_id;
	private String bank_name;
	private String branch_name;
	private String address;
	private String cperson;
	private String phone;
	private String mobile;
	private String fax;
	private String email;
	private String description;
	private int status;
	
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bankId) {
		bank_id = bankId;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branchId) {
		branch_id = branchId;
	}
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branchName) {
		branch_name = branchName;
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
	
	public String getCperson() {
		return cperson;
	}
	public void setCperson(String cperson) {
		this.cperson = cperson;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String toString() {
        return "{\"area_id\":\"" + area_id + "\",\"bank_id\":\"" + bank_id + "\",\"branch_id\":\"" + branch_id + "\",\"bank_name\":\"" + bank_name + "\", \"branch_name\":\"" + branch_name + "\", \"address\":\"" + (address==null?"":address) + "\", \"cperson\":\"" + cperson + "\", \"phone\":\"" + phone + "\", \"mobile\":\"" + mobile + "\", \"fax\":\"" + fax + "\", \"email\":\"" + email + "\",\"description\":\"" + (description==null?"":description.replaceAll("[\\r\\n]+", "<br/>")) + "\", \"status\":\"" + status + "\"}";
    }
	
}

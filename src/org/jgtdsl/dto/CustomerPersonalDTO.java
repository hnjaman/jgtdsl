package org.jgtdsl.dto;

import com.google.gson.Gson;

public class CustomerPersonalDTO {
	
	private String customer_id;
	private String full_name;
	private String customer_name;
	private String father_name;
	private String mother_name;
	private String gender;
	private String email;
	private String phone;
	private String mobile;
	private String fax;
	private String tin;
	private String freedom_fighter;
	private String national_id;
	private String passport_no;
	private String license_number;
	private String vat_reg_no;
	private String img_url;
	private String proprietor_name;
	private String organization_name;
	

	public String getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}


	public String getCustomer_name() {
		return customer_name;
	}


	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}


	public String getFull_name() {
		return full_name;
	}


	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}


	public String getFather_name() {
		return father_name;
	}


	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}


	public String getMother_name() {
		return mother_name;
	}


	public void setMother_name(String mother_name) {
		this.mother_name = mother_name;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
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


	public String getTin() {
		return tin;
	}


	public void setTin(String tin) {
		this.tin = tin;
	}


	public String getFreedom_fighter() {
		return freedom_fighter;
	}


	public void setFreedom_fighter(String freedom_fighter) {
		this.freedom_fighter = freedom_fighter;
	}


	public String getNational_id() {
		return national_id;
	}


	public void setNational_id(String national_id) {
		this.national_id = national_id;
	}


	public String getPassport_no() {
		return passport_no;
	}


	public void setPassport_no(String passport_no) {
		this.passport_no = passport_no;
	}


	public String getLicense_number() {
		return license_number;
	}


	public void setLicense_number(String license_number) {
		this.license_number = license_number;
	}


	public String getVat_reg_no() {
		return vat_reg_no;
	}


	public void setVat_reg_no(String vat_reg_no) {
		this.vat_reg_no = vat_reg_no;
	}


	public String getImg_url() {
		return img_url;
	}


	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}


	public String getProprietor_name() {
		return proprietor_name;
	}


	public void setProprietor_name(String proprietor_name) {
		this.proprietor_name = proprietor_name;
	}


	public String getOrganization_name() {
		return organization_name;
	}


	public void setOrganization_name(String organization_name) {
		this.organization_name = organization_name;
	}


	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

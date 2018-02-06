package org.jgtdsl.dto;

import java.io.Serializable;

import com.google.gson.Gson;

public class UserDTO implements  Serializable{
	
	private static final long serialVersionUID = 4176120745182751980L;
	
	private String userId;
	private String old_password;
	private String password;
	private String confirm_password;	
	private String encrypted_password;
	private String userName;
	private String role_id;
	private String role_name;
	private String org_division_id;
	private String org_division_name;
	private String department_id;
	private String department_name;	
	private String section_id;
	private String section_name;
	private String division_id;
	private String division_name;
	private String district_id;
	private String district_name;
	private String upazila_id;
	private String upazila_name;
	private String area_id;
	private String area_name;
	private String designation_id;
	private String designation_name;
	private String mobile;
	private String status;
	private String userImg;
	private String created_on;
	private String created_by;
	private String last_login_on;
	private String email_address;
	private String default_url;
	private String tmp_id;
	
	private int slNo;
	
	public UserDTO(){}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm_password() {
		return confirm_password;
	}

	public void setConfirm_password(String confirmPassword) {
		confirm_password = confirmPassword;
	}

	public String getEncrypted_password() {
		return encrypted_password;
	}

	public void setEncrypted_password(String encryptedPassword) {
		encrypted_password = encryptedPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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

	public String getDivision_id() {
		return division_id;
	}

	public void setDivision_id(String divisionId) {
		division_id = divisionId;
	}

	public String getDivision_name() {
		return division_name;
	}

	public void setDivision_name(String divisionName) {
		division_name = divisionName;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String departmentId) {
		department_id = departmentId;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String departmentName) {
		department_name = departmentName;
	}

	public String getSection_id() {
		return section_id;
	}

	public void setSection_id(String sectionId) {
		section_id = sectionId;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String sectionName) {
		section_name = sectionName;
	}
	

	public String getDesignation_id() {
		return designation_id;
	}

	public void setDesignation_id(String designationId) {
		designation_id = designationId;
	}

	public String getDesignation_name() {
		return designation_name;
	}

	public void setDesignation_name(String designationName) {
		designation_name = designationName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String createdOn) {
		created_on = createdOn;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String createdBy) {
		created_by = createdBy;
	}

	public String getLast_login_on() {
		return last_login_on;
	}

	public void setLast_login_on(String lastLoginOn) {
		last_login_on = lastLoginOn;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String emailAddress) {
		email_address = emailAddress;
	}

	public String getDefault_url() {
		return default_url;
	}

	public void setDefault_url(String defaultUrl) {
		default_url = defaultUrl;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTmp_id() {
		return tmp_id;
	}

	public void setTmp_id(String tmpId) {
		tmp_id = tmpId;
	}
	
	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String areaId) {
		area_id = areaId;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String areaName) {
		area_name = areaName;
	}
	
	public String getOld_password() {
		return old_password;
	}

	public void setOld_password(String oldPassword) {
		old_password = oldPassword;
	}
	
	public String getOrg_division_id() {
		return org_division_id;
	}

	public void setOrg_division_id(String orgDivisionId) {
		org_division_id = orgDivisionId;
	}

	public String getOrg_division_name() {
		return org_division_name;
	}

	public void setOrg_division_name(String orgDivisionName) {
		org_division_name = orgDivisionName;
	}

	public String getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(String districtId) {
		district_id = districtId;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String districtName) {
		district_name = districtName;
	}

	public String getUpazila_id() {
		return upazila_id;
	}

	public void setUpazila_id(String upazilaId) {
		upazila_id = upazilaId;
	}

	public String getUpazila_name() {
		return upazila_name;
	}

	public void setUpazila_name(String upazilaName) {
		upazila_name = upazilaName;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

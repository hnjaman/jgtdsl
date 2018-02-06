package org.jgtdsl.dto;

import com.google.gson.Gson;

public class AddressDTO {

	private String division_id;
	private String division_name;
	
	private String district_id;
	private String district_name;
	private String district_code;
	
	private String upazila_id;
	private String upazila_name;
	
	private String road_house_no;
	private String post_office;
	private String post_code;
	private String address_line1;
	private String address_line2;	
	
	
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
	public String getDistrict_code() {
		return district_code;
	}
	public void setDistrict_code(String districtCode) {
		district_code = districtCode;
	}
	public String getRoad_house_no() {
		return road_house_no;
	}
	public void setRoad_house_no(String roadHouseNo) {
		road_house_no = roadHouseNo;
	}
	public String getPost_office() {
		return post_office;
	}
	public void setPost_office(String postOffice) {
		post_office = postOffice;
	}
	public String getPost_code() {
		return post_code;
	}
	public void setPost_code(String postCode) {
		post_code = postCode;
	}
	public String getAddress_line1() {
		return address_line1;
	}
	public void setAddress_line1(String addressLine1) {
		address_line1 = addressLine1;
	}
	public String getAddress_line2() {
		return address_line2;
	}
	public void setAddress_line2(String addressLine2) {
		address_line2 = addressLine2;
	}
	

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
	
}

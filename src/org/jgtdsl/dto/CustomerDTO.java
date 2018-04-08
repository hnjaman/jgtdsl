package org.jgtdsl.dto;

import com.google.gson.Gson;

public class CustomerDTO {

	private String customer_id;
	private String customer_category;
	private String area;
	private String zone;
	private CustomerPersonalDTO personalInfo; // //
	private AddressDTO addressInfo;
	private CustomerConnectionDTO connectionInfo;
	private DisconnectDTO latestDisconnectInfo;
	private String address;
	/* Extra attribute */
	private String area_name;
	private String zone_name;
	private String customer_category_name;
	private String app_sl_no;
	private String app_date;
	private String Inserted_by;
	private String customer_name;

	private int single_burner;
	private int double_burner;
	private int other_burner;

	public int getSingle_burner() {
		return single_burner;
	}

	public void setSingle_burner(int single_burner) {
		this.single_burner = single_burner;
	}

	public int getDouble_burner() {
		return double_burner;
	}

	public void setDouble_burner(int double_burner) {
		this.double_burner = double_burner;
	}

	public int getOther_burner() {
		return other_burner;
	}

	public void setOther_burner(int other_burner) {
		this.other_burner = other_burner;
	}

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

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public CustomerPersonalDTO getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(CustomerPersonalDTO personalInfo) {
		this.personalInfo = personalInfo;
	}

	public AddressDTO getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressDTO addressInfo) {
		this.addressInfo = addressInfo;
	}

	public CustomerConnectionDTO getConnectionInfo() {
		return connectionInfo;
	}

	public void setConnectionInfo(CustomerConnectionDTO connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public DisconnectDTO getLatestDisconnectInfo() {
		return latestDisconnectInfo;
	}

	public void setLatestDisconnectInfo(DisconnectDTO latestDisconnectInfo) {
		this.latestDisconnectInfo = latestDisconnectInfo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getZone_name() {
		return zone_name;
	}

	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}

	public String getCustomer_category_name() {
		return customer_category_name;
	}

	public void setCustomer_category_name(String customer_category_name) {
		this.customer_category_name = customer_category_name;
	}

	public String getApp_sl_no() {
		return app_sl_no;
	}

	public void setApp_sl_no(String app_sl_no) {
		this.app_sl_no = app_sl_no;
	}

	public String getApp_date() {
		return app_date;
	}

	public void setApp_date(String app_date) {
		this.app_date = app_date;
	}

	public String getInserted_by() {
		return Inserted_by;
	}

	public void setInserted_by(String inserted_by) {
		Inserted_by = inserted_by;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}

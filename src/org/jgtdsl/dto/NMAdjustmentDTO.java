package org.jgtdsl.dto;

import org.jgtdsl.enums.Month;

import com.google.gson.Gson;

public class NMAdjustmentDTO {
	
	private String bill_id;
	private String customer_id;
	private String customer_categoryID;
	private String customer_name;
	private String Category_name;
	private String category_type;
	private String area_id;
	private String area_name;
	private String address;
	private String isMetered;
	private String double_burner;
	private String ff_quata;
	private double contractul_load;
	private double min_load;
	private Month billing_month;
	private String billing_month_str;
	private String bill_year;
	
	
	
	
	
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}
	public Month getBilling_month() {
		return billing_month;
	}
	public void setBilling_month(Month billing_month) {
		this.billing_month = billing_month;
	}
	public String getBilling_month_str() {
		return billing_month_str;
	}
	public void setBilling_month_str(String billing_month_str) {
		this.billing_month_str = billing_month_str;
	}
	public String getFf_quata() {
		return ff_quata;
	}
	public void setFf_quata(String ff_quata) {
		this.ff_quata = ff_quata;
	}
	public double getContractul_load() {
		return contractul_load;
	}
	public void setContractul_load(double contractul_load) {
		this.contractul_load = contractul_load;
	}
	public double getMin_load() {
		return min_load;
	}
	public void setMin_load(double min_load) {
		this.min_load = min_load;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_categoryID() {
		return customer_categoryID;
	}
	public void setCustomer_categoryID(String customer_categoryID) {
		this.customer_categoryID = customer_categoryID;
	}
	public String getCategory_name() {
		return Category_name;
	}
	public void setCategory_name(String category_name) {
		Category_name = category_name;
	}
	public String getCategory_type() {
		return category_type;
	}
	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIsMetered() {
		return isMetered;
	}
	public void setIsMetered(String isMetered) {
		this.isMetered = isMetered;
	}
	public String getDouble_burner() {
		return double_burner;
	}
	public void setDouble_burner(String double_burner) {
		this.double_burner = double_burner;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	

}

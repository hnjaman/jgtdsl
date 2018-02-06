package org.jgtdsl.dto;

import com.google.gson.Gson;


public class MinistryBillDTO {

	private String customer_id;
	private String full_name;
	private int is_metered;
	private String bill_id;
	private double bill_amount;
	private String ministry_id;
	private String ministry_name;
	
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getfull_name() {
		return full_name;
	}
	public void setfull_name(String full_name) {
		this.full_name = full_name;
	}
	public int getIs_metered() {
		return is_metered;
	}
	public void setIs_metered(int is_metered) {
		this.is_metered = is_metered;
	}
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}
	public double getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(double bill_amount) {
		this.bill_amount = bill_amount;
	}
	public String getMinistry_id() {
		return ministry_id;
	}
	public void setMinistry_id(String ministry_id) {
		this.ministry_id = ministry_id;
	}
	public String getMinistry_name() {
		return ministry_name;
	}
	public void setMinistry_name(String ministry_name) {
		this.ministry_name = ministry_name;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

package org.jgtdsl.dto;

import com.google.gson.Gson;

public class ClarificationDTO {

	
	private String category_id;
	private String is_meter;
	private String number_of_customer;
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getIs_meter() {
		return is_meter;
	}
	public void setIs_meter(String is_meter) {
		this.is_meter = is_meter;
	}
	public String getNumber_of_customer() {
		return number_of_customer;
	}
	public void setNumber_of_customer(String number_of_customer) {
		this.number_of_customer = number_of_customer;
	}
	
	
	
	
	
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
	
	
}

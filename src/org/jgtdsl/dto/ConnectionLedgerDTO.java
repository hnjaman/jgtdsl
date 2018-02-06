package org.jgtdsl.dto;

import com.google.gson.Gson;

public class ConnectionLedgerDTO {
	
	private String customer_id;
	private String event_date;
	private String description;
	private String single_burner;
	private String double_burner;
	private String min_load;
	private String max_load;
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getEvent_date() {
		return event_date;
	}
	public void setEvent_date(String eventDate) {
		event_date = eventDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSingle_burner() {
		return single_burner;
	}
	public void setSingle_burner(String singleBurner) {
		single_burner = singleBurner;
	}
	public String getDouble_burner() {
		return double_burner;
	}
	public void setDouble_burner(String doubleBurner) {
		double_burner = doubleBurner;
	}
	

	public String getMin_load() {
		return min_load;
	}
	public void setMin_load(String minLoad) {
		min_load = minLoad;
	}
	public String getMax_load() {
		return max_load;
	}
	public void setMax_load(String maxLoad) {
		max_load = maxLoad;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }		
}

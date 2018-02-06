package org.jgtdsl.dto;

import com.google.gson.Gson;

public class SupplyOffDTO {
	
	private String off_for;
	private String customer_id;
	private String full_name;
	private String area_id;
	private String customer_category;
	private String billing_month;
	private String billing_year;
	private String from_date;
	private String to_date;
	private String remarks;
	
	private String month_year;
	private String days;
	
	public String getOff_for() {
		return off_for;
	}
	public void setOff_for(String offFor) {
		off_for = offFor;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}
	public String getBilling_month() {
		return billing_month;
	}
	public void setBilling_month(String billingMonth) {
		billing_month = billingMonth;
	}
	public String getBilling_year() {
		return billing_year;
	}
	public void setBilling_year(String billingYear) {
		billing_year = billingYear;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String fromDate) {
		from_date = fromDate;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String toDate) {
		to_date = toDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getMonth_year() {
		return month_year;
	}
	public void setMonth_year(String monthYear) {
		month_year = monthYear;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}		
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String fullName) {
		full_name = fullName;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

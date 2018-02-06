package org.jgtdsl.dto;

public class LoadExceedReportDTO {
	private String customer_id;
	private String full_name;
	private String customer_category_id;
	private String customer_category_name;
	private String billing_month;
	private String billing_year;
	private float max_load;
	private float pmax_laod;
	private float actual_consumption;
	private float difference;
	private float percent_usage;
	
	
	
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_category_id() {
		return customer_category_id;
	}
	public void setCustomer_category_id(String customer_category_id) {
		this.customer_category_id = customer_category_id;
	}
	public String getCustomer_category_name() {
		return customer_category_name;
	}
	public void setCustomer_category_name(String customer_category_name) {
		this.customer_category_name = customer_category_name;
	}
	public String getBilling_month() {
		return billing_month;
	}
	public void setBilling_month(String billing_month) {
		this.billing_month = billing_month;
	}
	public String getBilling_year() {
		return billing_year;
	}
	public void setBilling_year(String billing_year) {
		this.billing_year = billing_year;
	}
	public float getMax_load() {
		return max_load;
	}
	public void setMax_load(float max_load) {
		this.max_load = max_load;
	}
	public float getPmax_laod() {
		return pmax_laod;
	}
	public void setPmax_laod(float pmax_laod) {
		this.pmax_laod = pmax_laod;
	}
	public float getActual_consumption() {
		return actual_consumption;
	}
	public void setActual_consumption(float actual_consumption) {
		this.actual_consumption = actual_consumption;
	}

	public float getDifference() {
		return difference;
	}
	public void setDifference(float difference) {
		this.difference = difference;
	}
	public float getPercent_usage() {
		return percent_usage;
	}
	public void setPercent_usage(float percent_usage) {
		this.percent_usage = percent_usage;
	}
	
	

}

package org.jgtdsl.dto;

import com.google.gson.Gson;

public class BillAdjustmentDTO {
	private String customer_id;
	private String bill_month;
	private String bill_year;
	private String issue_date;
	private String due_date;
	private double bill_amount;
	private double surcharge_amount;
	private double meter_rent;
	private double total_amount;
	private double total_consumption;
	private String payment_status;
	
	
	
	
	public String getPayment_status() {
		return payment_status;
	}
	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	
	public String getBill_month() {
		return bill_month;
	}
	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public double getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(double bill_amount) {
		this.bill_amount = bill_amount;
	}
	public double getSurcharge_amount() {
		return surcharge_amount;
	}
	public void setSurcharge_amount(double surcharge_amount) {
		this.surcharge_amount = surcharge_amount;
	}
	public double getMeter_rent() {
		return meter_rent;
	}
	public void setMeter_rent(double meter_rent) {
		this.meter_rent = meter_rent;
	}
	public double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}
	public double getTotal_consumption() {
		return total_consumption;
	}
	public void setTotal_consumption(double total_consumption) {
		this.total_consumption = total_consumption;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	

}

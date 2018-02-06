package org.jgtdsl.dto;

public class SurchargeDTO {

	private String customer_id;
	private String pay_date;
	private String surcharge_rate;
	private String bill_id;	
	private String surcharge_amount;
	
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public String getPay_date() {
		return pay_date;
	}
	public void setPay_date(String payDate) {
		pay_date = payDate;
	}
	public String getSurcharge_rate() {
		return surcharge_rate;
	}
	public void setSurcharge_rate(String surchargeRate) {
		surcharge_rate = surchargeRate;
	}
	public String getSurcharge_amount() {
		return surcharge_amount;
	}
	public void setSurcharge_amount(String surchargeAmount) {
		surcharge_amount = surchargeAmount;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	
	
}

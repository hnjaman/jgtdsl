package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MBillGridDTO {
	
	private String customer_id;
	private String full_name;
	private double actual_consumption;
	private double meter_rent;
	private double total_bill_amount;
	
	private double actual_payable_amount;
	private double collected_payable_amount;
	private double non_collected_payable_amount;
	
	private String status;
	private String bill_id;
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String fullName) {
		full_name = fullName;
	}
	
	public double getActual_consumption() {
		return actual_consumption;
	}
	public void setActual_consumption(double actualConsumption) {
		actual_consumption = actualConsumption;
	}
	public double getMeter_rent() {
		return meter_rent;
	}
	public void setMeter_rent(double meterRent) {
		meter_rent = meterRent;
	}
	public double getTotal_bill_amount() {
		return total_bill_amount;
	}
	public void setTotal_bill_amount(double totalBillAmount) {
		total_bill_amount = totalBillAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	
	public double getActual_payable_amount() {
		return actual_payable_amount;
	}
	public void setActual_payable_amount(double actualPayableAmount) {
		actual_payable_amount = actualPayableAmount;
	}
	public double getCollected_payable_amount() {
		return collected_payable_amount;
	}
	public void setCollected_payable_amount(double collectedPayableAmount) {
		collected_payable_amount = collectedPayableAmount;
	}
	public double getNon_collected_payable_amount() {
		return non_collected_payable_amount;
	}
	public void setNon_collected_payable_amount(double nonCollectedPayableAmount) {
		non_collected_payable_amount = nonCollectedPayableAmount;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

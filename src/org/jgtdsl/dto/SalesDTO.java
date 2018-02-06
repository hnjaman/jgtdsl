package org.jgtdsl.dto;

import com.google.gson.Gson;

public class SalesDTO {

	private String categoryID;
	private String categoryType;
	private String customerCategory;
	private double actualExceptmin;
	private double actualConsumption;
	private double billingUnit;
	private double difference;
	private double totalActualConsumption;
	private double rate;
	private double valueOfTotalActualConsumption;
	private double minimumCharge;
	private double meterRent;
	private double nHVhHV;
	private double totalBillAmount;
	public String getCustomerCategory() {
		return customerCategory;
	}
	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}
	public double getActualExceptmin() {
		return actualExceptmin;
	}
	public void setActualExceptmin(double actualExceptmin) {
		this.actualExceptmin = actualExceptmin;
	}
	public double getActualConsumption() {
		return actualConsumption;
	}
	public void setActualConsumption(double actualConsumption) {
		this.actualConsumption = actualConsumption;
	}
	public double getBillingUnit() {
		return billingUnit;
	}
	public void setBillingUnit(double billingUnit) {
		this.billingUnit = billingUnit;
	}
	public double getDifference() {
		return difference;
	}
	public String getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	public void setDifference(double difference) {
		this.difference = difference;
	}
	public double getTotalActualConsumption() {
		return totalActualConsumption;
	}
	public void setTotalActualConsumption(double totalActualConsumption) {
		this.totalActualConsumption = totalActualConsumption;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getValueOfTotalActualConsumption() {
		return valueOfTotalActualConsumption;
	}
	public void setValueOfTotalActualConsumption(
			double valueOfTotalActualConsumption) {
		this.valueOfTotalActualConsumption = valueOfTotalActualConsumption;
	}
	public double getMinimumCharge() {
		return minimumCharge;
	}
	public void setMinimumCharge(double minimumCharge) {
		this.minimumCharge = minimumCharge;
	}
	public double getMeterRent() {
		return meterRent;
	}
	public void setMeterRent(double meterRent) {
		this.meterRent = meterRent;
	}
	public double getnHVhHV() {
		return nHVhHV;
	}
	public void setnHVhHV(double nHVhHV) {
		this.nHVhHV = nHVhHV;
	}
	public double getTotalBillAmount() {
		return totalBillAmount;
	}
	public void setTotalBillAmount(double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
	
	
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

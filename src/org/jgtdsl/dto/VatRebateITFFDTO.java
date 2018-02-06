package org.jgtdsl.dto;

public class VatRebateITFFDTO {

	private String customerName;
	private double gasConsumption;
	private String customerId;
	private double billAmount;
	private double vatAmount;
	private double rebateAmount;
	private double taxAmount;
	private int burnerQty;
	private String customerCategory;
	private String month;
	private String year;
	private String chalanNo;
	private String chalanDate;
	
	
	
	public String getChalanNo() {
		return chalanNo;
	}
	public void setChalanNo(String chalanNo) {
		this.chalanNo = chalanNo;
	}
	public String getChalanDate() {
		return chalanDate;
	}
	public void setChalanDate(String chalanDate) {
		this.chalanDate = chalanDate;
	}
	public double getRebateAmount() {
		return rebateAmount;
	}
	public void setRebateAmount(double rebateAmount) {
		this.rebateAmount = rebateAmount;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCustomerCategory() {
		return customerCategory;
	}
	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public double getGasConsumption() {
		return gasConsumption;
	}
	public void setGasConsumption(double gasConsumption) {
		this.gasConsumption = gasConsumption;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}
	public double getVatAmount() {
		return vatAmount;
	}
	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public int getBurnerQty() {
		return burnerQty;
	}
	public void setBurnerQty(int burnerQty) {
		this.burnerQty = burnerQty;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}

package org.jgtdsl.dto;

public class MonthlyCollectionCustomerTypeWiseDTO {

	private String customer_Id;
	private String customerType;
	private Double monthly_Gas_Bill;
	private Double surcharge;
	private Double sucurity;
	private Double fee;
	
	
	public String getCustomer_Id() {
		return customer_Id;
	}
	public void setCustomer_Id(String customer_Id) {
		this.customer_Id = customer_Id;
	}
	public Double getMonthly_Gas_Bill() {
		return monthly_Gas_Bill;
	}
	public void setMonthly_Gas_Bill(Double monthly_Gas_Bill) {
		this.monthly_Gas_Bill = monthly_Gas_Bill;
	}
	public Double getSurcharge() {
		return surcharge;
	}
	public void setSurcharge(Double surcharge) {
		this.surcharge = surcharge;
	}
	public Double getSucurity() {
		return sucurity;
	}
	public void setSucurity(Double sucurity) {
		this.sucurity = sucurity;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	
	
	
	
}

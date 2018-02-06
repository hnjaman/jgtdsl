package org.jgtdsl.dto;

public class CategoryWiseGasConsumptionDTO {
	
	
	
	private String customerId;
	private String organizationName;
	private String particulars;
	private String transDate;
	private String bankBranch;
	
	private double gasBill;
	private double surcharge;
	private double fess;
	private double sd;
	
	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public double getGasBill() {
		return gasBill;
	}
	public void setGasBill(double gasBill) {
		this.gasBill = gasBill;
	}
	public double getSurcharge() {
		return surcharge;
	}
	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
	}
	public double getFess() {
		return fess;
	}
	public void setFess(double fess) {
		this.fess = fess;
	}
	public double getSd() {
		return sd;
	}
	public void setSd(double sd) {
		this.sd = sd;
	}
	
	
	
	

}

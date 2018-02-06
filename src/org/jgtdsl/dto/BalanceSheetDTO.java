package org.jgtdsl.dto;

import com.google.gson.Gson;

public class BalanceSheetDTO {

	private String customerID;
	private String customerName;
	private String customer_category;
	private String area_id;
	private String issueDate;
	private String dueDate;
	private double debit;
	private double credit;
	private double balance;
	private double sales;
	private double meterRent;
	private String address;
	private String meterNo;
	private String particular;
	private double surcharge;
	private double consumedGas;
	private double collectedSurcharge;
	private double adjustmentAmt;
	private double othersAmt;
	private String securityDate;
	private double securityDebit;
	private double securityCredit;
	private String propriateName;
	private String connectionDate;
	private String dissconnectionDate;
	private double securityOpening;
	
	private String customerAddress;
	private double previousBalance;
	private double billAmount;
	private double collectedAmount;
	private double endingBalance;
	private String customerCategoryName;
	private double chalanIT;
	private double hhv;
	private double vateRebate;
	private String subCategory;
	private double burner;
	private double waiver;
	
	public double getWaiver() {
		return waiver;
	}
	public void setWaiver(double waiver) {
		this.waiver = waiver;
	}
	public double getBurner() {
		return burner;
	}
	public void setBurner(double burner) {
		this.burner = burner;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public double getChalanIT() {
		return chalanIT;
	}
	public void setChalanIT(double chalanIT) {
		this.chalanIT = chalanIT;
	}
	public double getHhv() {
		return hhv;
	}
	public void setHhv(double hhvNHV) {
		this.hhv = hhvNHV;
	}
	public double getVateRebate() {
		return vateRebate;
	}
	public void setVateRebate(double vateRebate) {
		this.vateRebate = vateRebate;
	}
	public String getCustomerCategoryName() {
		return customerCategoryName;
	}
	public void setCustomerCategoryName(String customerCategoryName) {
		this.customerCategoryName = customerCategoryName;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public double getPreviousBalance() {
		return previousBalance;
	}
	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}
	public double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}
	public double getCollectedAmount() {
		return collectedAmount;
	}
	public void setCollectedAmount(double collectedAmount) {
		this.collectedAmount = collectedAmount;
	}
	public double getEndingBalance() {
		return endingBalance;
	}
	public void setEndingBalance(double endingBalance) {
		this.endingBalance = endingBalance;
	}
	public double getSecurityOpening() {
		return securityOpening;
	}
	public void setSecurityOpening(double securityOpening) {
		this.securityOpening = securityOpening;
	}
	public String getConnectionDate() {
		return connectionDate;
	}
	public void setConnectionDate(String connectionDate) {
		this.connectionDate = connectionDate;
	}
	public String getDissconnectionDate() {
		return dissconnectionDate;
	}
	public void setDissconnectionDate(String dissconnectionDate) {
		this.dissconnectionDate = dissconnectionDate;
	}
	public String getPropriateName() {
		return propriateName;
	}
	public void setPropriateName(String propriateName) {
		this.propriateName = propriateName;
	}
	public double getCollectedSurcharge() {
		return collectedSurcharge;
	}
	public String getSecurityDate() {
		return securityDate;
	}
	public void setSecurityDate(String securityDate) {
		this.securityDate = securityDate;
	}
	public double getSecurityDebit() {
		return securityDebit;
	}
	public void setSecurityDebit(double securityDebit) {
		this.securityDebit = securityDebit;
	}
	public double getSecurityCredit() {
		return securityCredit;
	}
	public void setSecurityCredit(double securityCredit) {
		this.securityCredit = securityCredit;
	}
	public void setCollectedSurcharge(double collectedSurcharge) {
		this.collectedSurcharge = collectedSurcharge;
	}
	public double getAdjustmentAmt() {
		return adjustmentAmt;
	}
	public void setAdjustmentAmt(double adjustmentAmt) {
		this.adjustmentAmt = adjustmentAmt;
	}
	public double getOthersAmt() {
		return othersAmt;
	}
	public void setOthersAmt(double othersAmt) {
		this.othersAmt = othersAmt;
	}
	public String getCustomerID() {
		return customerID;
	}
	public double getSurcharge() {
		return surcharge;
	}
	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public double getDebit() {
		return debit;
	}
	public void setDebit(double debit) {
		this.debit = debit;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getSales() {
		return sales;
	}
	public void setSales(double sales) {
		this.sales = sales;
	}
	public double getMeterRent() {
		return meterRent;
	}
	public void setMeterRent(double meterRent) {
		this.meterRent = meterRent;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMeterNo() {
		return meterNo;
	}
	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}
	public String getParticular() {
		return particular;
	}
	public void setParticular(String particular) {
		this.particular = particular;
	}
	public double getConsumedGas() {
		return consumedGas;
	}
	public void setConsumedGas(double consumedGas) {
		this.consumedGas = consumedGas;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

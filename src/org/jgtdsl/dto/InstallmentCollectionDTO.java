package org.jgtdsl.dto;

import com.google.gson.Gson;

public class InstallmentCollectionDTO {
	
	private String customerId;
	private String customerName;
	private String customerType;
	private String customerCategory;
	private String isMetered;
	private String isMeteredName;
	private String mobile;
	private String phone;
	
	private String installmentId;
	private String installmentSerial;
	private String installmentDescription;
	private String installmentBillMonth;
	private String installmentBillYear;
	private double principal;
	private double surcharge;
	private double meterRent;
	private double total;
	private String collectionStatus;
	private String collectionStatusName;
	private double collectedAmount;
	private double taxAmount;
	private String collectionDate;
	private String bankId;
	private String bankName;
	private String branchId;
	private String branchName;
	private String accountNo;
	private String accountName;
	private String insertedBy;
	private String insertedOn;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getCustomerCategory() {
		return customerCategory;
	}
	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}
	public String getIsMetered() {
		return isMetered;
	}
	public void setIsMetered(String isMetered) {
		this.isMetered = isMetered;
	}
	public String getIsMeteredName() {
		return isMeteredName;
	}
	public void setIsMeteredName(String isMeteredName) {
		this.isMeteredName = isMeteredName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInstallmentId() {
		return installmentId;
	}
	public void setInstallmentId(String installmentId) {
		this.installmentId = installmentId;
	}
	public String getInstallmentSerial() {
		return installmentSerial;
	}
	public void setInstallmentSerial(String installmentSerial) {
		this.installmentSerial = installmentSerial;
	}
	public String getInstallmentDescription() {
		return installmentDescription;
	}
	public void setInstallmentDescription(String installmentDescription) {
		this.installmentDescription = installmentDescription;
	}
	public String getInstallmentBillMonth() {
		return installmentBillMonth;
	}
	public void setInstallmentBillMonth(String installmentBillMonth) {
		this.installmentBillMonth = installmentBillMonth;
	}
	
	public String getInstallmentBillYear() {
		return installmentBillYear;
	}
	public void setInstallmentBillYear(String installmentBillYear) {
		this.installmentBillYear = installmentBillYear;
	}
	public double getPrincipal() {
		return principal;
	}
	public void setPrincipal(double principal) {
		this.principal = principal;
	}
	public double getSurcharge() {
		return surcharge;
	}
	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
	}
	public double getMeterRent() {
		return meterRent;
	}
	public void setMeterRent(double meterRent) {
		this.meterRent = meterRent;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getCollectionStatus() {
		return collectionStatus;
	}
	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}
	public String getCollectionStatusName() {
		return collectionStatusName;
	}
	public void setCollectionStatusName(String collectionStatusName) {
		this.collectionStatusName = collectionStatusName;
	}
	public double getCollectedAmount() {
		return collectedAmount;
	}
	public void setCollectedAmount(double collectedAmount) {
		this.collectedAmount = collectedAmount;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public String getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getInsertedBy() {
		return insertedBy;
	}
	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}
	public String getInsertedOn() {
		return insertedOn;
	}
	public void setInsertedOn(String insertedOn) {
		this.insertedOn = insertedOn;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

package org.jgtdsl.dto;

import com.google.gson.Gson;

public class InstallmentDTO {
	
	private String installmentId;
	private String agreementId;
	private String serial;
	private String description;
	private String dueDate;
	private String installmentBillingMonth;
	private String installmentBillingMonthName;
	private String installmentBillingYear;
	private double principal;
	private double surcharge;
	private double meterRent;
	private double total;
	private String segments;
	private int status;
	private String statusName;
	private String generatedBy;
	
	private String customerId;
	private String customerName;
		

	public String getInstallmentId() {
		return installmentId;
	}

	public void setInstallmentId(String installmentId) {
		this.installmentId = installmentId;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getInstallmentBillingMonth() {
		return installmentBillingMonth;
	}

	public void setInstallmentBillingMonth(String installmentBillingMonth) {
		this.installmentBillingMonth = installmentBillingMonth;
	}

	public String getInstallmentBillingMonthName() {
		return installmentBillingMonthName;
	}

	public void setInstallmentBillingMonthName(
			String installmentBillingMonthName) {
		this.installmentBillingMonthName = installmentBillingMonthName;
	}

	public String getInstallmentBillingYear() {
		return installmentBillingYear;
	}

	public void setInstallmentBillingYear(String installmentBillingYear) {
		this.installmentBillingYear = installmentBillingYear;
	}

	public String getSegments() {
		return segments;
	}

	public void setSegments(String segments) {
		this.segments = segments;
	}

	

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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


	public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}

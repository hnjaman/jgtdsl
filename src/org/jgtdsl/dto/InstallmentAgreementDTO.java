package org.jgtdsl.dto;

import com.google.gson.Gson;

public class InstallmentAgreementDTO {
	
	private String agreementId;
	private String customerId;
	private String customerName;
	private String startFrom;
	private int 	totalInstallment;
	private String agreementDate;
	private String notes;
	private String insertedOn;
	private String insertedBy;
	private int status;
	private String statusName;
	

	public String getAgreementId() {
		return agreementId;
	}


	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
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


	public String getStartFrom() {
		return startFrom;
	}


	public void setStartFrom(String startFrom) {
		this.startFrom = startFrom;
	}


	public int getTotalInstallment() {
		return totalInstallment;
	}


	public void setTotalInstallment(int totalInstallment) {
		this.totalInstallment = totalInstallment;
	}


	public String getAgreementDate() {
		return agreementDate;
	}


	public void setAgreementDate(String agreementDate) {
		this.agreementDate = agreementDate;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	public String getInsertedOn() {
		return insertedOn;
	}


	public void setInsertedOn(String insertedOn) {
		this.insertedOn = insertedOn;
	}


	public String getInsertedBy() {
		return insertedBy;
	}


	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
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


	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

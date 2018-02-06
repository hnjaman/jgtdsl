package org.jgtdsl.dto.ipg;

import com.google.gson.Gson;

public class BillInfo {

	private String billId;
	private String billMonthYear;
	private String status;
	private String billAmount;
	private String surcharge;
	private String totalBillAmount;
	private String bankBranchInfo;
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillMonthYear() {
		return billMonthYear;
	}
	public void setBillMonthYear(String billMonthYear) {
		this.billMonthYear = billMonthYear;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getSurcharge() {
		return surcharge;
	}
	public void setSurcharge(String surcharge) {
		this.surcharge = surcharge;
	}
	public String getTotalBillAmount() {
		return totalBillAmount;
	}
	public void setTotalBillAmount(String totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
	public String getBankBranchInfo() {
		return bankBranchInfo;
	}
	public void setBankBranchInfo(String bankBranchInfo) {
		this.bankBranchInfo = bankBranchInfo;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

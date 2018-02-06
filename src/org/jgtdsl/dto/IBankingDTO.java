package org.jgtdsl.dto;

public class IBankingDTO {
	private String transactionId;
	private String transactionDate;
	private String fromAccount;
	private String toAccount;
	private String referenceId;
	private String billMonthYear;
	private String collectedAmount;
	private String counter;
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getBillMonthYear() {
		return billMonthYear;
	}
	public void setBillMonthYear(String billMonthYear) {
		this.billMonthYear = billMonthYear;
	}
	public String getCollectedAmount() {
		return collectedAmount;
	}
	public void setCollectedAmount(String collectedAmount) {
		this.collectedAmount = collectedAmount;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	
	
}

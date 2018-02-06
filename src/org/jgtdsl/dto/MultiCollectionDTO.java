package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MultiCollectionDTO {
	
	private String customer_id;
	private String bank_id;
	private String branch_id;
	private String account_no;
	private String collection_date;
	private String pending_bills_str;
	private String advanced_bills_str;
	private String bill_month;
	private String bill_year;
	private String current_bill_month;
	private String current_bill_year;
	private Double collection_amount; //current month bill collection
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bankId) {
		bank_id = bankId;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branchId) {
		branch_id = branchId;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String accountNo) {
		account_no = accountNo;
	}

	public String getCollection_date() {
		return collection_date;
	}
	public void setCollection_date(String collectionDate) {
		collection_date = collectionDate;
	}
	public String getPending_bills_str() {
		return pending_bills_str;
	}
	public void setPending_bills_str(String pendingBillsStr) {
		pending_bills_str = pendingBillsStr;
	}
	public String getAdvanced_bills_str() {
		return advanced_bills_str;
	}
	public void setAdvanced_bills_str(String advancedBillsStr) {
		advanced_bills_str = advancedBillsStr;
	}
	
	public String getCurrent_bill_month() {
		return current_bill_month;
	}
	public void setCurrent_bill_month(String current_bill_month) {
		this.current_bill_month = current_bill_month;
	}
	public String getCurrent_bill_year() {
		return current_bill_year;
	}
	public void setCurrent_bill_year(String current_bill_year) {
		this.current_bill_year = current_bill_year;
	}
	public Double getcollection_amount() {
		return collection_amount;
	}
	public void setcollection_amount(Double collection_amount) {
		this.collection_amount = collection_amount;
	}
	
	public String getBill_month() {
		return bill_month;
	}
	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}
	public String toString() {
		Gson gosn=new Gson();
		return gosn.toJson(this);
	}

}

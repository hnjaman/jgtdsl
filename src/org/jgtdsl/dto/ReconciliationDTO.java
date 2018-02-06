package org.jgtdsl.dto;

import com.google.gson.Gson;

public class ReconciliationDTO {
	
	private String cause_id;
	private String cause_name;
	private String  bank_id;
	private String branch_id;
	private String account_no;
	private String collection_month;
	private String collection_year;
	private String add_comments;
	private String add_amount;
	private String lessComment;
	private String lessAmount;
	private String total_amount;
	private String opening_balance;
	private String addAccount;
	private String lessAccount;
	
	
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}
	public String getCollection_month() {
		return collection_month;
	}
	public void setCollection_month(String collection_month) {
		this.collection_month = collection_month;
	}
	public String getCollection_year() {
		return collection_year;
	}
	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}
	public String getAdd_comments() {
		return add_comments;
	}
	public void setAdd_comments(String add_comments) {
		this.add_comments = add_comments;
	}
	public String getAdd_amount() {
		return add_amount;
	}
	public void setAdd_amount(String add_amount) {
		this.add_amount = add_amount;
	}
	public String getLessComment() {
		return lessComment;
	}
	public void setLessComment(String lessComment) {
		this.lessComment = lessComment;
	}
	public String getLessAmount() {
		return lessAmount;
	}
	public void setLessAmount(String lessAmount) {
		this.lessAmount = lessAmount;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getOpening_balance() {
		return opening_balance;
	}
	public void setOpening_balance(String opening_balance) {
		this.opening_balance = opening_balance;
	}
	public String getAddAccount() {
		return addAccount;
	}
	public void setAddAccount(String addAccount) {
		this.addAccount = addAccount;
	}
	public String getLessAccount() {
		return lessAccount;
	}
	public void setLessAccount(String lessAccount) {
		this.lessAccount = lessAccount;
	}
	public String getCause_id() {
		return cause_id;
	}
	public void setCause_id(String cause_id) {
		this.cause_id = cause_id;
	}
	public String getCause_name() {
		return cause_name;
	}
	public void setCause_name(String cause_name) {
		this.cause_name = cause_name;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }

}

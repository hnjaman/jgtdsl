package org.jgtdsl.dto;

import com.google.gson.Gson;

public class AccountDTO {
	
	private String bank_id;
	private String branch_id;
	private String bank_name;
	private String branch_name;
	private String account_no;
	private String account_type;
	private String account_name;
	private String ac_opening_date;
	private String opening_balance;
	private String opening_date;
	private String description;
	private int status;
	private int a;
	BankDTO bank;
	BranchDTO branch;
	
	
	public BankDTO getBank() {
		return bank;
	}
	public void setBank(BankDTO bank) {
		this.bank = bank;
	}
	public BranchDTO getBranch() {
		return branch;
	}
	public void setBranch(BranchDTO branch) {
		this.branch = branch;
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
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bankName) {
		bank_name = bankName;
	}
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branchName) {
		branch_name = branchName;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String accountNo) {
		account_no = accountNo;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String accountType) {
		account_type = accountType;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String accountName) {
		account_name = accountName;
	}
	public String getAc_opening_date() {
		return ac_opening_date;
	}
	public void setAc_opening_date(String acOpeningDate) {
		ac_opening_date = acOpeningDate;
	}
	
	public String getOpening_balance() {
		return opening_balance;
	}
	public void setOpening_balance(String openingBalance) {
		opening_balance = openingBalance;
	}
	public String getOpening_date() {
		return opening_date;
	}
	public void setOpening_date(String openingDate) {
		opening_date = openingDate;
	}
	public String getDescription() {
		return (description==null?"":description.replaceAll("[\\r\\n]+", "<br/>"));
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

//	public String toString() {
//        return "{\"bank_id\":\"" + bank_id + "\",\"branch_id\":\"" + branch_id + "\",\"bank_name\":\"" + bank_name==null?"":bank_name + "\", \"branch_name\":\"" + branch_name==null?"":branch_name + "\", \"account_no\":\"" + account_no + "\", \"account_type\":\"" + account_type + "\", \"account_name\":\"" + account_name + "\", \"ac_opening_date\":\"" + ac_opening_date + "\", \"opening_balance\":\"" + opening_balance + "\", \"opening_date\":\"" + opening_date + "\",\"description\":\"" +   + "\", \"status\":\"" + status + "\"}";
//    }
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }	
}

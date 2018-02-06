package org.jgtdsl.dto;

import java.util.ArrayList;

import org.jgtdsl.enums.DepositPurpose;
import org.jgtdsl.enums.DepositType;

import com.google.gson.Gson;

public class DepositDTO {

	private String deposit_id;
	private String customer_id;
	private String customer_name;
	private DepositPurpose deposit_purpose;	
	private String str_deposit_purpose;
	private String total_deposit;
	private String bank;
	private String bank_name;
	private String branch;
	private String branch_name;
	private String account_no;
	private String account_name;
	private String deposit_date;	
	private String valid_from;
	private String valid_to;
	private String inserted_on;
	private String inserted_by;
	private String remarks_on_bg;
	
	private String str_deposit_type;
	private DepositType deposit_type;
	private String customer_category;
	private String customer_category_name;
	
	private String expire_in;
	private String entry_date;
	private String old_expire_date;
	private String new_expire_date;
	private String pId;
	
	private ArrayList<DepositDtlDTO> depositDetail=new ArrayList<DepositDtlDTO>();
	
	CustomerDTO cusDto=new CustomerDTO();
	
	
	
	public String getpId() {
		return pId;
	}


	public void setpId(String pId) {
		this.pId = pId;
	}


	public String getEntry_date() {
		return entry_date;
	}


	public void setEntry_date(String entry_date) {
		this.entry_date = entry_date;
	}


	public String getOld_expire_date() {
		return old_expire_date;
	}


	public void setOld_expire_date(String old_expire_date) {
		this.old_expire_date = old_expire_date;
	}


	public String getNew_expire_date() {
		return new_expire_date;
	}


	public void setNew_expire_date(String new_expire_date) {
		this.new_expire_date = new_expire_date;
	}


	public String getRemarks_on_bg() {
		return remarks_on_bg;
	}


	public void setRemarks_on_bg(String remarks_on_bg) {
		this.remarks_on_bg = remarks_on_bg;
	}


	public String getCustomer_category() {
		return customer_category;
	}


	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}


	public String getCustomer_category_name() {
		return customer_category_name;
	}


	public void setCustomer_category_name(String customer_category_name) {
		this.customer_category_name = customer_category_name;
	}


	public CustomerDTO getCusDto() {
		return cusDto;
	}


	public void setCusDto(CustomerDTO cusDto) {
		this.cusDto = cusDto;
	}


	public String getDeposit_id() {
		return deposit_id;
	}


	public void setDeposit_id(String depositId) {
		deposit_id = depositId;
	}


	public String getCustomer_name() {
		return customer_name;
	}


	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}


	public String getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}


	public DepositPurpose getDeposit_purpose() {
		return deposit_purpose;
	}


	public void setDeposit_purpose(DepositPurpose depositPurpose) {
		deposit_purpose = depositPurpose;
	}


	public String getStr_deposit_purpose() {
		return str_deposit_purpose;
	}


	public void setStr_deposit_purpose(String strDepositPurpose) {
		str_deposit_purpose = strDepositPurpose;
	}

	public String getBank() {
		return bank;
	}


	public void setBank(String bank) {
		this.bank = bank;
	}


	public String getBank_name() {
		return bank_name;
	}


	public void setBank_name(String bankName) {
		bank_name = bankName;
	}


	public String getBranch() {
		return branch;
	}


	public void setBranch(String branch) {
		this.branch = branch;
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


	public String getAccount_name() {
		return account_name;
	}


	public void setAccount_name(String accountName) {
		account_name = accountName;
	}


	public String getDeposit_date() {
		return deposit_date;
	}


	public void setDeposit_date(String depositDate) {
		deposit_date = depositDate;
	}


	public String getValid_from() {
		return valid_from;
	}


	public void setValid_from(String validFrom) {
		valid_from = validFrom;
	}


	public String getValid_to() {
		return valid_to;
	}


	public void setValid_to(String validTo) {
		valid_to = validTo;
	}


	public String getInserted_on() {
		return inserted_on;
	}


	public void setInserted_on(String insertedOn) {
		inserted_on = insertedOn;
	}


	public String getInserted_by() {
		return inserted_by;
	}


	public void setInserted_by(String insertedBy) {
		inserted_by = insertedBy;
	}


	public ArrayList<DepositDtlDTO> getDepositDetail() {
		return depositDetail;
	}


	public void setDepositDetail(ArrayList<DepositDtlDTO> depositDetail) {
		this.depositDetail = depositDetail;
	}


	public String getTotal_deposit() {
		return total_deposit;
	}


	public void setTotal_deposit(String totalDeposit) {
		total_deposit = totalDeposit;
	}


	public String getStr_deposit_type() {
		return str_deposit_type;
	}


	public void setStr_deposit_type(String strDepositType) {
		str_deposit_type = strDepositType;
	}


	public DepositType getDeposit_type() {
		return deposit_type;
	}


	public void setDeposit_type(DepositType depositType) {
		deposit_type = depositType;
	}


	public String getExpire_in() {
		return expire_in;
	}


	public void setExpire_in(String expireIn) {
		expire_in = expireIn;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

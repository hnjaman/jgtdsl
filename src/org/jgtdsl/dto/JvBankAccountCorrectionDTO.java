package org.jgtdsl.dto;

import com.google.gson.Gson;

public class JvBankAccountCorrectionDTO {

	private String customer_id;
	private String bill_month;
	private String bill_year;
	
	private String new_bank_id;
	private String new_branch_id;
	private String new_account_no;
	
	private String narration;	
	private String inserted_by;
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getBill_month() {
		return bill_month;
	}
	public void setBill_month(String billMonth) {
		bill_month = billMonth;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String billYear) {
		bill_year = billYear;
	}
	public String getNew_bank_id() {
		return new_bank_id;
	}
	public void setNew_bank_id(String newBankId) {
		new_bank_id = newBankId;
	}
	public String getNew_branch_id() {
		return new_branch_id;
	}
	public void setNew_branch_id(String newBranchId) {
		new_branch_id = newBranchId;
	}
	
	public String getNew_account_no() {
		return new_account_no;
	}
	public void setNew_account_no(String newAccountNo) {
		new_account_no = newAccountNo;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public String getInserted_by() {
		return inserted_by;
	}
	public void setInserted_by(String insertedBy) {
		inserted_by = insertedBy;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

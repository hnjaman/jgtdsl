package org.jgtdsl.dto;

import com.google.gson.Gson;

public class JvCustomerAccountCorrectionDTO {

	private String old_customer_id;
	private String new_customer_id;
	private String bill_month;
	private String bill_year;
	private String narration;
	private String new_bill_month;
	private String new_bill_year;
	
	private String inserted_by;
	
	
	
	public String getNew_bill_month() {
		return new_bill_month;
	}
	public void setNew_bill_month(String new_bill_month) {
		this.new_bill_month = new_bill_month;
	}
	public String getNew_bill_year() {
		return new_bill_year;
	}
	public void setNew_bill_year(String new_bill_year) {
		this.new_bill_year = new_bill_year;
	}
	public String getOld_customer_id() {
		return old_customer_id;
	}
	public void setOld_customer_id(String oldCustomerId) {
		old_customer_id = oldCustomerId;
	}
	public String getNew_customer_id() {
		return new_customer_id;
	}
	public void setNew_customer_id(String newCustomerId) {
		new_customer_id = newCustomerId;
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

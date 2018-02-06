package org.jgtdsl.dto;

import com.google.gson.Gson;

public class DuesSurchargeDTO extends CollectionDTO {

	private String due_date;
	private String days_elapsed;
	private String ibill_type; // 1= normal bill, 2= installment bill . Used by Security Adjustment dues bill list. 

	
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String dueDate) {
		due_date = dueDate;
	}
	public String getDays_elapsed() {
		return days_elapsed;
	}
	public void setDays_elapsed(String daysElapsed) {
		days_elapsed = daysElapsed;
	}
	
	
	public String getIbill_type() {
		return ibill_type;
	}
	public void setIbill_type(String ibill_type) {
		this.ibill_type = ibill_type;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }	
}

package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MeterRentChangeDTO {

	private String pid;
	private String old_rent;
	private String new_rent;
	private String customer_id;
	private String customer_name;
	private String meter_id;
	private String meter_sl_no;
	private String effective_date;
	private String remarks;	
	private String insert_by;
	private String insert_date;
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOld_rent() {
		return old_rent;
	}

	public void setOld_rent(String oldRent) {
		old_rent = oldRent;
	}

	public String getNew_rent() {
		return new_rent;
	}

	public void setNew_rent(String newRent) {
		new_rent = newRent;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}

	public String getMeter_sl_no() {
		return meter_sl_no;
	}

	public void setMeter_sl_no(String meterSlNo) {
		meter_sl_no = meterSlNo;
	}

	public String getEffective_date() {
		return effective_date;
	}

	public void setEffective_date(String effectiveDate) {
		effective_date = effectiveDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInsert_by() {
		return insert_by;
	}

	public void setInsert_by(String insertBy) {
		insert_by = insertBy;
	}

	public String getInsert_date() {
		return insert_date;
	}

	public void setInsert_date(String insertDate) {
		insert_date = insertDate;
	}
	
	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
	
}

package org.jgtdsl.dto;

import com.google.gson.Gson;

public class CustomerTypeChangeDTO {
	
	private String pid;
	private String old_meter_status;
	private String new_meter_status;
	private String customer_id;
	private String change_date;
	private String remarks;
	private String inserted_by;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getOld_meter_status() {
		return old_meter_status;
	}
	public void setOld_meter_status(String old_meter_status) {
		this.old_meter_status = old_meter_status;
	}
	public String getNew_meter_status() {
		return new_meter_status;
	}
	public void setNew_meter_status(String new_meter_status) {
		this.new_meter_status = new_meter_status;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getChange_date() {
		return change_date;
	}
	public void setChange_date(String change_date) {
		this.change_date = change_date;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getInserted_by() {
		return inserted_by;
	}
	public void setInserted_by(String inserted_by) {
		this.inserted_by = inserted_by;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	

}

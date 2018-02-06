package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MeterReplacementDTO {

	private String pid;
	
	private String customer_id;
	private String customer_name;
	private String old_meter_id;
	private String old_meter_sl_no;
	private String new_meter_id;
	private String new_meter_sl_no;
	private String replacement_date;
	
	private CustomerMeterDTO oldMeter;
	private CustomerMeterDTO newMeter;
	private String insert_by;
	private String insert_on;
	
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	
	public CustomerMeterDTO getOldMeter() {
		return oldMeter;
	}

	public void setOldMeter(CustomerMeterDTO oldMeter) {
		this.oldMeter = oldMeter;
	}

	public CustomerMeterDTO getNewMeter() {
		return newMeter;
	}

	public void setNewMeter(CustomerMeterDTO newMeter) {
		this.newMeter = newMeter;
	}

	public String getInsert_by() {
		return insert_by;
	}

	public void setInsert_by(String insertBy) {
		insert_by = insertBy;
	}

	public String getInsert_on() {
		return insert_on;
	}

	public void setInsert_on(String insertOn) {
		insert_on = insertOn;
	}
	
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}

	public String getOld_meter_id() {
		return old_meter_id;
	}

	public void setOld_meter_id(String oldMeterId) {
		old_meter_id = oldMeterId;
	}

	public String getOld_meter_sl_no() {
		return old_meter_sl_no;
	}

	public void setOld_meter_sl_no(String oldMeterSlNo) {
		old_meter_sl_no = oldMeterSlNo;
	}

	public String getNew_meter_id() {
		return new_meter_id;
	}

	public void setNew_meter_id(String newMeterId) {
		new_meter_id = newMeterId;
	}

	public String getNew_meter_sl_no() {
		return new_meter_sl_no;
	}

	public void setNew_meter_sl_no(String newMeterSlNo) {
		new_meter_sl_no = newMeterSlNo;
	}

	public String getReplacement_date() {
		return replacement_date;
	}

	public void setReplacement_date(String replacementDate) {
		replacement_date = replacementDate;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }	
}

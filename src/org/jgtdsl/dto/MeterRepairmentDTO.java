package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MeterRepairmentDTO {
	
	private String pid;
	private String meter_id;
	private String prev_reading;
	private String prev_reading_date;
	private String curr_reading;
	private String curr_reading_date;
	private String repaired_by;
	private String reading_id;
	private String remarks;
	
	private String customer_name;
	private String customer_id;
	private String meter_sl_no;
	private String measurement_type_str;
	private String insert_by;
	private String insert_date;
	
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getMeter_id() {
		return meter_id;
	}
	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}
	public String getPrev_reading() {
		return prev_reading;
	}
	public void setPrev_reading(String prevReading) {
		prev_reading = prevReading;
	}
	public String getPrev_reading_date() {
		return prev_reading_date;
	}
	public void setPrev_reading_date(String prevReadingDate) {
		prev_reading_date = prevReadingDate;
	}
	public String getCurr_reading() {
		return curr_reading;
	}
	public void setCurr_reading(String currReading) {
		curr_reading = currReading;
	}
	public String getCurr_reading_date() {
		return curr_reading_date;
	}
	public void setCurr_reading_date(String currReadingDate) {
		curr_reading_date = currReadingDate;
	}
	public String getRepaired_by() {
		return repaired_by;
	}
	public void setRepaired_by(String repairedBy) {
		repaired_by = repairedBy;
	}
	public String getReading_id() {
		return reading_id;
	}
	public void setReading_id(String readingId) {
		reading_id = readingId;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	
	public String getMeter_sl_no() {
		return meter_sl_no;
	}
	public void setMeter_sl_no(String meterSlNo) {
		meter_sl_no = meterSlNo;
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
	public String getMeasurement_type_str() {
		return measurement_type_str;
	}
	public void setMeasurement_type_str(String measurementTypeStr) {
		measurement_type_str = measurementTypeStr;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

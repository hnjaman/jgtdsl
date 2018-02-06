package org.jgtdsl.dto;

import org.jgtdsl.enums.LoadChangeType;

import com.google.gson.Gson;

public class LoadPressureChangeDTO {

	private String pid;
	private String customer_id;
	private LoadChangeType change_type;
	private String change_type_str;
	private String change_type_name;
	
	private String effective_date;
	private String meter_id;
	private String meter_sl_no;
	private MeterReadingDTO reading;
	
	private String old_min_load;
	private String old_max_load;
	private String new_min_load;
	private String new_max_load;
	
	private String old_pressure;
	private String new_pressure;
	
	private String old_rent;
	private String new_rent;
	
	private String change_by;
	private String change_by_name;
	
	private String reading_id;
	private String remarks;
	private String insert_by;
	private String insert_date;

	private CustomerDTO customer;
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}	
	public LoadChangeType getChange_type() {
		return change_type;
	}
	public void setChange_type(LoadChangeType changeType) {
		change_type = changeType;
	}	
	
	public String getEffective_date() {
		return effective_date;
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
	public String getChange_type_str() {
		return change_type_str;
	}
	public void setChange_type_str(String changeTypeStr) {
		change_type_str = changeTypeStr;
	}
	public String getChange_type_name() {
		return change_type_name;
	}
	public void setChange_type_name(String changeTypeName) {
		change_type_name = changeTypeName;
	}
	public String getOld_min_load() {
		return old_min_load;
	}
	public void setOld_min_load(String oldMinLoad) {
		old_min_load = oldMinLoad;
	}
	public String getOld_max_load() {
		return old_max_load;
	}
	public void setOld_max_load(String oldMaxLoad) {
		old_max_load = oldMaxLoad;
	}
	public String getNew_min_load() {
		return new_min_load;
	}
	public void setNew_min_load(String newMinLoad) {
		new_min_load = newMinLoad;
	}
	public String getNew_max_load() {
		return new_max_load;
	}
	public void setNew_max_load(String newMaxLoad) {
		new_max_load = newMaxLoad;
	}
	public String getOld_pressure() {
		return old_pressure;
	}
	public void setOld_pressure(String oldPressure) {
		old_pressure = oldPressure;
	}
	public String getNew_pressure() {
		return new_pressure;
	}
	public void setNew_pressure(String newPressure) {
		new_pressure = newPressure;
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
	
	public String getMeter_id() {
		return meter_id;
	}
	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}
	public MeterReadingDTO getReading() {
		return reading;
	}
	public void setReading(MeterReadingDTO reading) {
		this.reading = reading;
	}
	public void setEffective_date(String effectiveDate) {
		effective_date = effectiveDate;
	}
	
	public String getChange_by() {
		return change_by;
	}
	public void setChange_by(String changeBy) {
		change_by = changeBy;
	}
	public String getChange_by_name() {
		return change_by_name;
	}
	public void setChange_by_name(String changeByName) {
		change_by_name = changeByName;
	}
	
	public String getReading_id() {
		return reading_id;
	}
	public void setReading_id(String readingId) {
		reading_id = readingId;
	}
	
	public String getMeter_sl_no() {
		return meter_sl_no;
	}
	public void setMeter_sl_no(String meterSlNo) {
		meter_sl_no = meterSlNo;
	}
	
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

package org.jgtdsl.dto;

import com.google.gson.Gson;

public class ReconnectDTO {
	
	private String pid;
	private String customer_id;
	private String customer_name;
	private String meter_id;
	private String meter_sl_no;
	private String meter_reading;
	private String reconnect_by;
	private String reconnect_date;
	private String remarks;
	private String insert_by;
	private String insert_date;
	private String disconnect_id;
	private DisconnectDTO disconnectionInfo;
	
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
	public String getMeter_id() {
		return meter_id;
	}
	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}
	public String getMeter_reading() {
		return meter_reading;
	}
	public void setMeter_reading(String meterReading) {
		meter_reading = meterReading;
	}
	
	public String getReconnect_by() {
		return reconnect_by;
	}
	public void setReconnect_by(String reconnectBy) {
		reconnect_by = reconnectBy;
	}
	public String getReconnect_date() {
		return reconnect_date;
	}
	public void setReconnect_date(String reconnectDate) {
		reconnect_date = reconnectDate;
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
	public String getDisconnect_id() {
		return disconnect_id;
	}
	public void setDisconnect_id(String disconnectId) {
		disconnect_id = disconnectId;
	}
	
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}
	
	public String getMeter_sl_no() {
		return meter_sl_no;
	}
	public void setMeter_sl_no(String meterSlNo) {
		meter_sl_no = meterSlNo;
	}
	
	public DisconnectDTO getDisconnectionInfo() {
		return disconnectionInfo;
	}
	public void setDisconnectionInfo(DisconnectDTO disconnectionInfo) {
		this.disconnectionInfo = disconnectionInfo;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }

	
}
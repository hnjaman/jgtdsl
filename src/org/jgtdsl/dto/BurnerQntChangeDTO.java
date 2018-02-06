package org.jgtdsl.dto;

import com.google.gson.Gson;

public class BurnerQntChangeDTO {

	private String pid;
	private String customer_id;
	private String appliance_id;
	private String appliance_name;
	private String customer_name;
	private String old_single_burner_qnt;
	private String old_double_burner_qnt;
	private String new_single_burner_qnt;
	private String new_double_burner_qnt;
	
	
	private String  old_pdisconnected_burner_qnt;//total permanently disconnected burner
	private String  old_double_burner_qnt_billcal;
	private String  new_double_qnt_billcal;
	
	private String  old_tdisconnected_burner_qnt;//total temporary disconnected burner
	private String  old_tdisconnected_half_burner_qnt;
	private String  new_double_burner_billcal_qnt;
	private String  new_permanent_disconnected_burner_qnt;
	private String  isTempToPerDiss;
	private String  new_permanent_disconnected_cause;
	private String  disconn_type;
	
	private String  new_temporary_disconnected_burner_qnt;
	private String  new_incrased_burner_qnt;
	private String  new_reconnected_burner_qnt;
	private String  new_reconnected_burner_qnt_permanent;
	private String  reconnection_cause;
	
	private String effective_date;
	private String remarks;
	private String insert_by;
	private String insert_date;
	
	
	
	
	public String getAppliance_name() {
		return appliance_name;
	}
	public void setAppliance_name(String appliance_name) {
		this.appliance_name = appliance_name;
	}
	public String getAppliance_id() {
		return appliance_id;
	}
	public void setAppliance_id(String appliance_id) {
		this.appliance_id = appliance_id;
	}
	public String getOld_tdisconnected_half_burner_qnt() {
		return old_tdisconnected_half_burner_qnt;
	}
	public void setOld_tdisconnected_half_burner_qnt(
			String old_tdisconnected_half_burner_qnt) {
		this.old_tdisconnected_half_burner_qnt = old_tdisconnected_half_burner_qnt;
	}
	public String getIsTempToPerDiss() {
		return isTempToPerDiss;
	}
	public void setIsTempToPerDiss(String isTempToPerDiss) {
		this.isTempToPerDiss = isTempToPerDiss;
	}
	public String getNew_reconnected_burner_qnt_permanent() {
		return new_reconnected_burner_qnt_permanent;
	}
	public void setNew_reconnected_burner_qnt_permanent(
			String new_reconnected_burner_qnt_permanent) {
		this.new_reconnected_burner_qnt_permanent = new_reconnected_burner_qnt_permanent;
	}
	public String getOld_pdisconnected_burner_qnt() {
		return old_pdisconnected_burner_qnt;
	}
	public void setOld_pdisconnected_burner_qnt(String old_pdisconnected_burner_qnt) {
		this.old_pdisconnected_burner_qnt = old_pdisconnected_burner_qnt;
	}
	public String getOld_double_burner_qnt_billcal() {
		return old_double_burner_qnt_billcal;
	}
	public String getNew_double_qnt_billcal() {
		return new_double_qnt_billcal;
	}
	public void setNew_double_qnt_billcal(String new_double_qnt_billcal) {
		this.new_double_qnt_billcal = new_double_qnt_billcal;
	}
	public void setOld_double_burner_qnt_billcal(
			String old_double_burner_qnt_billcal) {
		this.old_double_burner_qnt_billcal = old_double_burner_qnt_billcal;
	}
	public String getOld_tdisconnected_burner_qnt() {
		return old_tdisconnected_burner_qnt;
	}
	public void setOld_tdisconnected_burner_qnt(String old_tdisconnected_burner_qnt) {
		this.old_tdisconnected_burner_qnt = old_tdisconnected_burner_qnt;
	}
	public String getNew_double_burner_billcal_qnt() {
		return new_double_burner_billcal_qnt;
	}
	public void setNew_double_burner_billcal_qnt(
			String new_double_burner_billcal_qnt) {
		this.new_double_burner_billcal_qnt = new_double_burner_billcal_qnt;
	}
	public String getNew_permanent_disconnected_burner_qnt() {
		return new_permanent_disconnected_burner_qnt;
	}
	public void setNew_permanent_disconnected_burner_qnt(
			String new_permanent_disconnected_burner_qnt) {
		this.new_permanent_disconnected_burner_qnt = new_permanent_disconnected_burner_qnt;
	}
	public String getNew_permanent_disconnected_cause() {
		return new_permanent_disconnected_cause;
	}
	public void setNew_permanent_disconnected_cause(
			String new_permanent_disconnected_cause) {
		this.new_permanent_disconnected_cause = new_permanent_disconnected_cause;
	}
	public String getNew_temporary_disconnected_burner_qnt() {
		return new_temporary_disconnected_burner_qnt;
	}
	public void setNew_temporary_disconnected_burner_qnt(
			String new_temporary_disconnected_burner_qnt) {
		this.new_temporary_disconnected_burner_qnt = new_temporary_disconnected_burner_qnt;
	}
	public String getNew_incrased_burner_qnt() {
		return new_incrased_burner_qnt;
	}
	public void setNew_incrased_burner_qnt(String new_incrased_burner_qnt) {
		this.new_incrased_burner_qnt = new_incrased_burner_qnt;
	}
	public String getNew_reconnected_burner_qnt() {
		return new_reconnected_burner_qnt;
	}
	public void setNew_reconnected_burner_qnt(String new_reconnected_burner_qnt) {
		this.new_reconnected_burner_qnt = new_reconnected_burner_qnt;
	}
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
	public String getOld_single_burner_qnt() {
		return old_single_burner_qnt;
	}
	public void setOld_single_burner_qnt(String oldSingleBurnerQnt) {
		old_single_burner_qnt = oldSingleBurnerQnt;
	}
	public String getOld_double_burner_qnt() {
		return old_double_burner_qnt;
	}
	public void setOld_double_burner_qnt(String oldDoubleBurnerQnt) {
		old_double_burner_qnt = oldDoubleBurnerQnt;
	}
	public String getNew_single_burner_qnt() {
		return new_single_burner_qnt;
	}
	public void setNew_single_burner_qnt(String newSingleBurnerQnt) {
		new_single_burner_qnt = newSingleBurnerQnt;
	}
	public String getNew_double_burner_qnt() {
		return new_double_burner_qnt;
	}
	public void setNew_double_burner_qnt(String newDoubleBurnerQnt) {
		new_double_burner_qnt = newDoubleBurnerQnt;
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
	
	public String getReconnection_cause() {
		return reconnection_cause;
	}
	public void setReconnection_cause(String reconnection_cause) {
		this.reconnection_cause = reconnection_cause;
	}
	
	public String getDisconn_type() {
		return disconn_type;
	}
	public void setDisconn_type(String disconn_type) {
		this.disconn_type = disconn_type;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

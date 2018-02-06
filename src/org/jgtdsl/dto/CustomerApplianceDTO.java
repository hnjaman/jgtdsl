package org.jgtdsl.dto;


import com.google.gson.Gson;


public class CustomerApplianceDTO {

	private String customer_id;
	private String applianc_id;
	private String applianc_name;
	private String applianc_qnt;
	private String applianc_qnt_billcal;
	private String applianc_perm_diss;
	private String applianc_temp_diss;
	private String applianc_partial_diss;
	private String applianc_status;
	private String insert_by;
	private String effective_date;
	private String remarks;
	private String disconn_type;
	private String disconnected_cause;
	

	
	public String getDisconn_type() {
		return disconn_type;
	}


	public void setDisconn_type(String disconn_type) {
		this.disconn_type = disconn_type;
	}


	public String getDisconnected_cause() {
		return disconnected_cause;
	}


	public void setDisconnected_cause(String disconnected_cause) {
		this.disconnected_cause = disconnected_cause;
	}


	public String getEffective_date() {
		return effective_date;
	}


	public void setEffective_date(String effective_date) {
		this.effective_date = effective_date;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}


	public String getInsert_by() {
		return insert_by;
	}


	public void setInsert_by(String insert_by) {
		this.insert_by = insert_by;
	}


	public String getApplianc_qnt_billcal() {
		return applianc_qnt_billcal;
	}


	public void setApplianc_qnt_billcal(String applianc_qnt_billcal) {
		this.applianc_qnt_billcal = applianc_qnt_billcal;
	}


	public String getApplianc_perm_diss() {
		return applianc_perm_diss;
	}


	public void setApplianc_perm_diss(String applianc_perm_diss) {
		this.applianc_perm_diss = applianc_perm_diss;
	}


	public String getApplianc_temp_diss() {
		return applianc_temp_diss;
	}


	public void setApplianc_temp_diss(String applianc_temp_diss) {
		this.applianc_temp_diss = applianc_temp_diss;
	}


	public String getApplianc_partial_diss() {
		return applianc_partial_diss;
	}


	public void setApplianc_partial_diss(String applianc_partial_diss) {
		this.applianc_partial_diss = applianc_partial_diss;
	}


	public String getApplianc_id() {
		return applianc_id;
	}


	public void setApplianc_id(String applianc_id) {
		this.applianc_id = applianc_id;
	}


	public String getApplianc_name() {
		return applianc_name;
	}


	public void setApplianc_name(String applianc_name) {
		this.applianc_name = applianc_name;
	}


	public String getApplianc_qnt() {
		return applianc_qnt;
	}


	public void setApplianc_qnt(String applianc_qnt) {
		this.applianc_qnt = applianc_qnt;
	}


	public String getApplianc_status() {
		return applianc_status;
	}


	public void setApplianc_status(String applianc_status) {
		this.applianc_status = applianc_status;
	}


	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

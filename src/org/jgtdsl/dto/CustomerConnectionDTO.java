package org.jgtdsl.dto;

import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.ConnectionType;
import org.jgtdsl.enums.MeteredStatus;

import com.google.gson.Gson;

public class CustomerConnectionDTO {

	private String customer_id;
	private String ministry_id;
	private String ministry_name;
	
	private MeteredStatus isMetered;
	private String isMetered_str;
	private String isMetered_name;
	
	private ConnectionType connection_type;
	private String connection_type_str;
	private String connection_type_name;
	
	private String parent_connection;
	private String min_load;
	private String max_load;
	private int single_burner_qnt;
	private int double_burner_qnt;
	private float double_burner_qnt_billcal;
	
	private float hhv_nhv;
	
	private String connection_date;
	private ConnectionStatus status;
	private String status_str;
	private String status_name;
	
	private double vat_rebate;
	
	private int pay_within_wo_sc;
	private int pay_within_w_sc;
	
	private int pay_within_wo_sc_range_default;
	private int pay_within_w_sc_range_default;
	
	private int pemanently_disconnected_burner_qnt;
	private int temporary_disconnected_burner_qnt;
	
	private String type_change_date;
	private String type_change_remarks;
	private String appliance_info_str;
	
	
	

	
	
	
	public String getAppliance_info_str() {
		return appliance_info_str;
	}

	public void setAppliance_info_str(String appliance_info_str) {
		this.appliance_info_str = appliance_info_str;
	}

	public float getDouble_burner_qnt_billcal() {
		return double_burner_qnt_billcal;
	}

	public void setDouble_burner_qnt_billcal(float double_burner_qnt_billcal) {
		this.double_burner_qnt_billcal = double_burner_qnt_billcal;
	}

	public int getPemanently_disconnected_burner_qnt() {
		return pemanently_disconnected_burner_qnt;
	}

	public void setPemanently_disconnected_burner_qnt(
			int pemanently_disconnected_burner_qnt) {
		this.pemanently_disconnected_burner_qnt = pemanently_disconnected_burner_qnt;
	}

	public int getTemporary_disconnected_burner_qnt() {
		return temporary_disconnected_burner_qnt;
	}

	public void setTemporary_disconnected_burner_qnt(
			int temporary_disconnected_burner_qnt) {
		this.temporary_disconnected_burner_qnt = temporary_disconnected_burner_qnt;
	}

	public int getPay_within_wo_sc_range_default() {
		return pay_within_wo_sc_range_default;
	}

	public void setPay_within_wo_sc_range_default(int pay_within_wo_sc_range_default) {
		this.pay_within_wo_sc_range_default = pay_within_wo_sc_range_default;
	}

	public int getPay_within_w_sc_range_default() {
		return pay_within_w_sc_range_default;
	}

	public void setPay_within_w_sc_range_default(int pay_within_w_sc_range_default) {
		this.pay_within_w_sc_range_default = pay_within_w_sc_range_default;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getMinistry_id() {
		return ministry_id;
	}

	public void setMinistry_id(String ministryId) {
		ministry_id = ministryId;
	}

	public String getMinistry_name() {
		return ministry_name;
	}

	public void setMinistry_name(String ministryName) {
		ministry_name = ministryName;
	}

	public MeteredStatus getIsMetered() {
		return isMetered;
	}

	public void setIsMetered(MeteredStatus isMetered) {
		this.isMetered = isMetered;
	}

	public String getIsMetered_str() {
		return isMetered_str;
	}

	public void setIsMetered_str(String isMeteredStr) {
		isMetered_str = isMeteredStr;
	}

	public String getIsMetered_name() {
		return isMetered_name;
	}

	public void setIsMetered_name(String isMeteredName) {
		isMetered_name = isMeteredName;
	}

	public ConnectionType getConnection_type() {
		return connection_type;
	}

	public void setConnection_type(ConnectionType connectionType) {
		connection_type = connectionType;
	}

	public String getConnection_type_str() {
		return connection_type_str;
	}

	public void setConnection_type_str(String connectionTypeStr) {
		connection_type_str = connectionTypeStr;
	}

	public String getConnection_type_name() {
		return connection_type_name;
	}

	public void setConnection_type_name(String connectionTypeName) {
		connection_type_name = connectionTypeName;
	}

	public String getParent_connection() {
		return parent_connection;
	}

	public void setParent_connection(String parentConnection) {
		parent_connection = parentConnection;
	}

	public String getMin_load() {
		return min_load;
	}

	public void setMin_load(String minLoad) {
		min_load = minLoad;
	}

	public String getMax_load() {
		return max_load;
	}

	public void setMax_load(String maxLoad) {
		max_load = maxLoad;
	}

	public int getSingle_burner_qnt() {
		return single_burner_qnt;
	}

	public void setSingle_burner_qnt(int singleBurnerQnt) {
		single_burner_qnt = singleBurnerQnt;
	}

	public int getDouble_burner_qnt() {
		return double_burner_qnt;
	}

	public void setDouble_burner_qnt(int doubleBurnerQnt) {
		double_burner_qnt = doubleBurnerQnt;
	}

	
	public float getHhv_nhv() {
		return hhv_nhv;
	}

	public void setHhv_nhv(float hhvNhv) {
		hhv_nhv = hhvNhv;
	}

	public String getConnection_date() {
		return connection_date;
	}

	public void setConnection_date(String connectionDate) {
		connection_date = connectionDate;
	}

	public ConnectionStatus getStatus() {
		return status;
	}

	public void setStatus(ConnectionStatus status) {
		this.status = status;
	}

	public String getStatus_str() {
		return status_str;
	}

	public void setStatus_str(String statusStr) {
		status_str = statusStr;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String statusName) {
		status_name = statusName;
	}

	public double getVat_rebate() {
		return vat_rebate;
	}

	public void setVat_rebate(double vatRebate) {
		vat_rebate = vatRebate;
	}

	public int getPay_within_wo_sc() {
		return pay_within_wo_sc;
	}

	public void setPay_within_wo_sc(int payWithinWoSc) {
		pay_within_wo_sc = payWithinWoSc;
	}

	public int getPay_within_w_sc() {
		return pay_within_w_sc;
	}

	public void setPay_within_w_sc(int payWithinWSc) {
		pay_within_w_sc = payWithinWSc;
	}

	public String getType_change_date() {
		return type_change_date;
	}

	public void setType_change_date(String type_change_date) {
		this.type_change_date = type_change_date;
	}

	public String getType_change_remarks() {
		return type_change_remarks;
	}

	public void setType_change_remarks(String type_change_remarks) {
		this.type_change_remarks = type_change_remarks;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }

}
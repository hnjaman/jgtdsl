package org.jgtdsl.dto;

import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnType;

import com.google.gson.Gson;

public class DisconnectDTO {

	private String pid;
	private String customer_id;
	private String customer_name;
	private String meter_id;
	private String meter_sl_no;
	private String meter_reading;
	private DisconnCause disconnect_cause;
	private String disconnect_cause_str;
	private String disconnect_cause_name;
	
	private DisconnType disconnect_type;	
	private String disconnect_type_str;	
	private String disconnect_type_name;
	
	private String disconnect_by;
	private String disconnect_by_name;
	private String disconnect_date;
	private String remarks;
	private String insert_by;
	private String insert_date;

	private MeterReadingDTO reading;
	private String reading_id;
	private CustomerDTO customer;
	
	private double minimumLoadSCM;
	private double disconnDateReading;
	private double previousReading;
	private double readingDifference;
	private double psig;
	private double pressureFactor;
	private double actualGasConsumption;
	private double meterRent;
	private double partialBill;
	private double dueGasBill;
	private double totalReceivable;
	private String dueMonth;
	private double burnerQnty;
	private String customerCategory;
	private String customerCategoryType;
	private String customerCategoryName;
	
	
	
	
	public double getMinimumLoadSCM() {
		return minimumLoadSCM;
	}
	public void setMinimumLoadSCM(double minimumLoadSCM) {
		this.minimumLoadSCM = minimumLoadSCM;
	}
	public double getDisconnDateReading() {
		return disconnDateReading;
	}
	public void setDisconnDateReading(double disconnDateReading) {
		this.disconnDateReading = disconnDateReading;
	}
	public double getPreviousReading() {
		return previousReading;
	}
	public void setPreviousReading(double previousReading) {
		this.previousReading = previousReading;
	}
	public double getReadingDifference() {
		return readingDifference;
	}
	public void setReadingDifference(double readingDifference) {
		this.readingDifference = readingDifference;
	}
	public double getPsig() {
		return psig;
	}
	public void setPsig(double psig) {
		this.psig = psig;
	}
	public double getPressureFactor() {
		return pressureFactor;
	}
	public void setPressureFactor(double pressureFactor) {
		this.pressureFactor = pressureFactor;
	}
	public double getActualGasConsumption() {
		return actualGasConsumption;
	}
	public void setActualGasConsumption(double actualGasConsumption) {
		this.actualGasConsumption = actualGasConsumption;
	}
	public double getMeterRent() {
		return meterRent;
	}
	public void setMeterRent(double meterRent) {
		this.meterRent = meterRent;
	}
	public double getPartialBill() {
		return partialBill;
	}
	public void setPartialBill(double partialBill) {
		this.partialBill = partialBill;
	}
	public double getDueGasBill() {
		return dueGasBill;
	}
	public void setDueGasBill(double dueGasBill) {
		this.dueGasBill = dueGasBill;
	}
	public double getTotalReceivable() {
		return totalReceivable;
	}
	public void setTotalReceivable(double totalReceivable) {
		this.totalReceivable = totalReceivable;
	}
	public String getDueMonth() {
		return dueMonth;
	}
	public void setDueMonth(String dueMonth) {
		this.dueMonth = dueMonth;
	}
	public double getBurnerQnty() {
		return burnerQnty;
	}
	public void setBurnerQnty(double burnerQnty) {
		this.burnerQnty = burnerQnty;
	}
	public String getCustomerCategory() {
		return customerCategory;
	}
	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}
	public String getCustomerCategoryType() {
		return customerCategoryType;
	}
	public void setCustomerCategoryType(String customerCategoryType) {
		this.customerCategoryType = customerCategoryType;
	}
	public String getCustomerCategoryName() {
		return customerCategoryName;
	}
	public void setCustomerCategoryName(String customerCategoryName) {
		this.customerCategoryName = customerCategoryName;
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
	public DisconnCause getDisconnect_cause() {
		return disconnect_cause;
	}
	public void setDisconnect_cause(DisconnCause disconnectCause) {
		disconnect_cause = disconnectCause;
	}
	public DisconnType getDisconnect_type() {
		return disconnect_type;
	}
	public void setDisconnect_type(DisconnType disconnectType) {
		disconnect_type = disconnectType;
	}
	public String getDisconnect_by() {
		return disconnect_by;
	}
	public void setDisconnect_by(String disconnectBy) {
		disconnect_by = disconnectBy;
	}
	public String getDisconnect_date() {
		return disconnect_date;
	}
	public void setDisconnect_date(String disconnectDate) {
		disconnect_date = disconnectDate;
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
	
	public String getDisconnect_cause_str() {
		return disconnect_cause_str;
	}
	public void setDisconnect_cause_str(String disconnectCauseStr) {
		disconnect_cause_str = disconnectCauseStr;
	}
	public String getDisconnect_cause_name() {
		return disconnect_cause_name;
	}
	public void setDisconnect_cause_name(String disconnectCauseName) {
		disconnect_cause_name = disconnectCauseName;
	}
	public String getDisconnect_type_str() {
		return disconnect_type_str;
	}
	public void setDisconnect_type_str(String disconnectTypeStr) {
		disconnect_type_str = disconnectTypeStr;
	}
	public String getDisconnect_type_name() {
		return disconnect_type_name;
	}
	public void setDisconnect_type_name(String disconnectTypeName) {
		disconnect_type_name = disconnectTypeName;
	}
	public MeterReadingDTO getReading() {
		return reading;
	}
	public void setReading(MeterReadingDTO reading) {
		this.reading = reading;
	}	
	public String getMeter_sl_no() {
		return meter_sl_no;
	}
	public void setMeter_sl_no(String meterSlNo) {
		meter_sl_no = meterSlNo;
	}
	
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}
	
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	
	public String getDisconnect_by_name() {
		return disconnect_by_name;
	}
	public void setDisconnect_by_name(String disconnectByName) {
		disconnect_by_name = disconnectByName;
	}
	
	public String getReading_id() {
		return reading_id;
	}
	public void setReading_id(String readingId) {
		reading_id = readingId;
	}
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }	
}
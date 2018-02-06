package org.jgtdsl.dto;

import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.enums.MeterStatus;

import com.google.gson.Gson;


public class CustomerMeterDTO {

	private String comm_customer_id;
	private String customer_id;
	private String meter_id;
	private String meter_sl_no;
	private String meter_mfg;
	private String meter_mfg_name;
	private String meter_year;
	private String evc_sl_no;
	private String evc_year;
	private String evc_model;
	private String evc_model_name;
	private String meter_type;
	private String meter_type_name;
	private String conn_size;
	private String g_rating;
	private String g_rating_name;
	private MeterMeasurementType measurement_type;
	private String measurement_type_str;
	private String measurement_type_name;
	private String max_reading;
	private String ini_reading;
	private String pressure;
	private String temperature;
	private String meter_rent;
	private String installed_by;
	private String installed_by_str;
	private String installed_date;
	private MeterStatus status;
	private String status_str;
	private String status_name;
	private String remarks;
	private String insert_by;
	private String insert_date;
	private double vat_rebate;
	private String unit;
	
	private MeterReadingDTO reading;
	private DisconnectDTO latestDisconnectInfo;
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public String getComm_customer_id() {
		return comm_customer_id;
	}

	public void setComm_customer_id(String comm_customer_id) {
		this.comm_customer_id = comm_customer_id;
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

	public String getMeter_mfg() {
		return meter_mfg;
	}

	public void setMeter_mfg(String meterMfg) {
		meter_mfg = meterMfg;
	}

	public String getMeter_mfg_name() {
		return meter_mfg_name;
	}

	public void setMeter_mfg_name(String meterMfgName) {
		meter_mfg_name = meterMfgName;
	}

	public String getMeter_year() {
		return meter_year;
	}

	public void setMeter_year(String meterYear) {
		meter_year = meterYear;
	}

	public String getEvc_sl_no() {
		return evc_sl_no;
	}

	public void setEvc_sl_no(String evcSlNo) {
		evc_sl_no = evcSlNo;
	}

	public String getEvc_year() {
		return evc_year;
	}

	public void setEvc_year(String evcYear) {
		evc_year = evcYear;
	}

	public String getEvc_model() {
		return evc_model;
	}

	public void setEvc_model(String evcModel) {
		evc_model = evcModel;
	}

	public String getEvc_model_name() {
		return evc_model_name;
	}

	public void setEvc_model_name(String evcModelName) {
		evc_model_name = evcModelName;
	}

	public String getMeter_type() {
		return meter_type;
	}

	public void setMeter_type(String meterType) {
		meter_type = meterType;
	}

	public String getMeter_type_name() {
		return meter_type_name;
	}

	public void setMeter_type_name(String meterTypeName) {
		meter_type_name = meterTypeName;
	}

	public String getConn_size() {
		return conn_size;
	}

	public void setConn_size(String connSize) {
		conn_size = connSize;
	}

	public String getG_rating() {
		return g_rating;
	}

	public void setG_rating(String gRating) {
		g_rating = gRating;
	}

	public String getG_rating_name() {
		return g_rating_name;
	}

	public void setG_rating_name(String gRatingName) {
		g_rating_name = gRatingName;
	}

	public MeterMeasurementType getMeasurement_type() {
		return measurement_type;
	}

	public void setMeasurement_type(MeterMeasurementType measurementType) {
		measurement_type = measurementType;
	}

	public String getMeasurement_type_str() {
		return measurement_type_str;
	}

	public void setMeasurement_type_str(String measurementTypeStr) {
		measurement_type_str = measurementTypeStr;
	}

	public String getMeasurement_type_name() {
		return measurement_type_name;
	}

	public void setMeasurement_type_name(String measurementTypeName) {
		measurement_type_name = measurementTypeName;
	}

	public String getMax_reading() {
		return max_reading;
	}

	public void setMax_reading(String maxReading) {
		max_reading = maxReading;
	}

	public String getIni_reading() {
		return ini_reading;
	}

	public void setIni_reading(String iniReading) {
		ini_reading = iniReading;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getMeter_rent() {
		return meter_rent;
	}

	public void setMeter_rent(String meterRent) {
		meter_rent = meterRent;
	}

	public String getInstalled_by() {
		return installed_by;
	}

	public void setInstalled_by(String installedBy) {
		installed_by = installedBy;
	}

	public String getInstalled_by_str() {
		return installed_by_str;
	}

	public void setInstalled_by_str(String installedByStr) {
		installed_by_str = installedByStr;
	}

	public String getInstalled_date() {
		return installed_date;
	}

	public void setInstalled_date(String installedDate) {
		installed_date = installedDate;
	}

	public MeterStatus getStatus() {
		return status;
	}

	public void setStatus(MeterStatus status) {
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public MeterReadingDTO getReading() {
		return reading;
	}

	public void setReading(MeterReadingDTO reading) {
		this.reading = reading;
	}
	
	public DisconnectDTO getLatestDisconnectInfo() {
		return latestDisconnectInfo;
	}

	public void setLatestDisconnectInfo(DisconnectDTO latestDisconnectInfo) {
		this.latestDisconnectInfo = latestDisconnectInfo;
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

	public double getVat_rebate() {
		return vat_rebate;
	}

	public void setVat_rebate(double vat_rebate) {
		this.vat_rebate = vat_rebate;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

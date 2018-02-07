package org.jgtdsl.dto;

import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.enums.ReadingPurpose;

import com.google.gson.Gson;

public class MeterReadingDTO {

	private String reading_id;
	private String customer_id;
	private String customer_name;
	private String address;
	private String meter_id;
	private String meter_sl_no;
	private ReadingPurpose reading_purpose;
	private String reading_purpose_str;
	private String reading_purpose_name;
	private int billing_month;
	private int billing_year;
	private long prev_reading;
	private String prev_reading_date;
	
	private long curr_reading;
	
	private String curr_reading_date;
	private double hhv_nhv;
	private MeterMeasurementType measurement_type;
	private String measurement_type_str;
	private String measurement_type_name;
	private int tariff_id;
	private int latest_tariff_id;
	private double rate;
	private double latest_rate;
	private long difference;
	private float min_load;
	private float max_load;
	private double percent;
	private double pMin_load; //Proportional Min Load
	private double pMax_load; //Proportional Max Load
	private double actual_consumption;
	private double total_consumption;
	private double meter_rent;
	private double pressure;
	private double pressure_factor;
	private double temperature;
	private double temperature_factor;
	private double pressure_latest;
	private double pressure_factor_latest;
	private double temperature_latest;
	private double temperature_factor_latest; //latest pre/temp is used when pressure is change middle of the moth to get the latest one pressure
	private String remarks;
	private String insert_date;
	private String insert_by;
	private String month_year;
	private String bill_id;
	private String month;
	private String year;
	private String prevReadingDate;
	private String currentReadinDate;
	private int total_reading_count;
	private int current_reading_index;
	private int burnerQty;
	private String unit;
	private double individual_gas_bill;//for average bill
	
	private CustomerDTO customer;
	
	
	
	
	
	public double getIndividual_gas_bill() {
		return individual_gas_bill;
	}





	public void setIndividual_gas_bill(double individual_gas_bill) {
		this.individual_gas_bill = individual_gas_bill;
	}





	public String getReading_id() {
		return reading_id;
	}





	public void setReading_id(String reading_id) {
		this.reading_id = reading_id;
	}





	public String getCustomer_id() {
		return customer_id;
	}





	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}





	public String getCustomer_name() {
		return customer_name;
	}





	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}





	public String getAddress() {
		return address;
	}





	public void setAddress(String address) {
		this.address = address;
	}





	public String getMeter_id() {
		return meter_id;
	}





	public void setMeter_id(String meter_id) {
		this.meter_id = meter_id;
	}





	public String getMeter_sl_no() {
		return meter_sl_no;
	}





	public void setMeter_sl_no(String meter_sl_no) {
		this.meter_sl_no = meter_sl_no;
	}





	public ReadingPurpose getReading_purpose() {
		return reading_purpose;
	}





	public void setReading_purpose(ReadingPurpose reading_purpose) {
		this.reading_purpose = reading_purpose;
	}





	public String getReading_purpose_str() {
		return reading_purpose_str;
	}





	public void setReading_purpose_str(String reading_purpose_str) {
		this.reading_purpose_str = reading_purpose_str;
	}





	public String getReading_purpose_name() {
		return reading_purpose_name;
	}





	public void setReading_purpose_name(String reading_purpose_name) {
		this.reading_purpose_name = reading_purpose_name;
	}





	public int getBilling_month() {
		return billing_month;
	}





	public void setBilling_month(int billing_month) {
		this.billing_month = billing_month;
	}





	public int getBilling_year() {
		return billing_year;
	}





	public void setBilling_year(int billing_year) {
		this.billing_year = billing_year;
	}










	public long getPrev_reading() {
		return prev_reading;
	}





	public void setPrev_reading(long prev_reading) {
		this.prev_reading = prev_reading;
	}





	public long getCurr_reading() {
		return curr_reading;
	}





	public void setCurr_reading(long curr_reading) {
		this.curr_reading = curr_reading;
	}





	public String getPrev_reading_date() {
		return prev_reading_date;
	}





	public void setPrev_reading_date(String prev_reading_date) {
		this.prev_reading_date = prev_reading_date;
	}











	public String getCurr_reading_date() {
		return curr_reading_date;
	}





	public void setCurr_reading_date(String curr_reading_date) {
		this.curr_reading_date = curr_reading_date;
	}





	public double getHhv_nhv() {
		return hhv_nhv;
	}





	public void setHhv_nhv(double hhv_nhv) {
		this.hhv_nhv = hhv_nhv;
	}





	public MeterMeasurementType getMeasurement_type() {
		return measurement_type;
	}





	public void setMeasurement_type(MeterMeasurementType measurement_type) {
		this.measurement_type = measurement_type;
	}





	public String getMeasurement_type_str() {
		return measurement_type_str;
	}





	public void setMeasurement_type_str(String measurement_type_str) {
		this.measurement_type_str = measurement_type_str;
	}





	public String getMeasurement_type_name() {
		return measurement_type_name;
	}





	public void setMeasurement_type_name(String measurement_type_name) {
		this.measurement_type_name = measurement_type_name;
	}





	public int getTariff_id() {
		return tariff_id;
	}





	public void setTariff_id(int tariff_id) {
		this.tariff_id = tariff_id;
	}





	public int getLatest_tariff_id() {
		return latest_tariff_id;
	}





	public void setLatest_tariff_id(int latest_tariff_id) {
		this.latest_tariff_id = latest_tariff_id;
	}





	public double getRate() {
		return rate;
	}





	public void setRate(double rate) {
		this.rate = rate;
	}





	public double getLatest_rate() {
		return latest_rate;
	}





	public void setLatest_rate(double latest_rate) {
		this.latest_rate = latest_rate;
	}





	public long getDifference() {
		return difference;
	}





	public void setDifference(long difference) {
		this.difference = difference;
	}


	public double getPercent() {
		return percent;
	}





	public void setPercent(double percent) {
		this.percent = percent;
	}
	
	public double getpMin_load() {
		return pMin_load;
	}





	public void setpMin_load(double pMin_load) {
		this.pMin_load = pMin_load;
	}





	public double getpMax_load() {
		return pMax_load;
	}





	public float getMin_load() {
		return min_load;
	}





	public float getMax_load() {
		return max_load;
	}





	public void setpMax_load(double pMax_load) {
		this.pMax_load = pMax_load;
	}





	public void setMin_load(float min_load) {
		this.min_load = min_load;
	}





	public void setMax_load(float max_load) {
		this.max_load = max_load;
	}





	public double getActual_consumption() {
		return actual_consumption;
	}





	public void setActual_consumption(double actual_consumption) {
		this.actual_consumption = actual_consumption;
	}





	public double getTotal_consumption() {
		return total_consumption;
	}





	public void setTotal_consumption(double total_consumption) {
		this.total_consumption = total_consumption;
	}





	public double getMeter_rent() {
		return meter_rent;
	}





	public void setMeter_rent(double meter_rent) {
		this.meter_rent = meter_rent;
	}





	public double getPressure() {
		return pressure;
	}





	public void setPressure(double pressure) {
		this.pressure = pressure;
	}





	public double getPressure_factor() {
		return pressure_factor;
	}





	public void setPressure_factor(double pressure_factor) {
		this.pressure_factor = pressure_factor;
	}





	public double getTemperature() {
		return temperature;
	}





	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}





	public double getTemperature_factor() {
		return temperature_factor;
	}





	public void setTemperature_factor(double temperature_factor) {
		this.temperature_factor = temperature_factor;
	}





	public double getPressure_latest() {
		return pressure_latest;
	}





	public void setPressure_latest(double pressure_latest) {
		this.pressure_latest = pressure_latest;
	}





	public double getPressure_factor_latest() {
		return pressure_factor_latest;
	}





	public void setPressure_factor_latest(double pressure_factor_latest) {
		this.pressure_factor_latest = pressure_factor_latest;
	}





	public double getTemperature_latest() {
		return temperature_latest;
	}





	public void setTemperature_latest(double temperature_latest) {
		this.temperature_latest = temperature_latest;
	}





	public double getTemperature_factor_latest() {
		return temperature_factor_latest;
	}





	public void setTemperature_factor_latest(double temperature_factor_latest) {
		this.temperature_factor_latest = temperature_factor_latest;
	}





	public String getRemarks() {
		return remarks;
	}





	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}





	public String getInsert_date() {
		return insert_date;
	}





	public void setInsert_date(String insert_date) {
		this.insert_date = insert_date;
	}





	public String getInsert_by() {
		return insert_by;
	}





	public void setInsert_by(String insert_by) {
		this.insert_by = insert_by;
	}





	public String getMonth_year() {
		return month_year;
	}





	public void setMonth_year(String month_year) {
		this.month_year = month_year;
	}





	public String getBill_id() {
		return bill_id;
	}





	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}





	public String getMonth() {
		return month;
	}





	public void setMonth(String month) {
		this.month = month;
	}





	public String getYear() {
		return year;
	}





	public void setYear(String year) {
		this.year = year;
	}





	public String getPrevReadingDate() {
		return prevReadingDate;
	}





	public void setPrevReadingDate(String prevReadingDate) {
		this.prevReadingDate = prevReadingDate;
	}





	public String getCurrentReadinDate() {
		return currentReadinDate;
	}





	public void setCurrentReadinDate(String currentReadinDate) {
		this.currentReadinDate = currentReadinDate;
	}





	public int getTotal_reading_count() {
		return total_reading_count;
	}





	public void setTotal_reading_count(int total_reading_count) {
		this.total_reading_count = total_reading_count;
	}





	public int getCurrent_reading_index() {
		return current_reading_index;
	}





	public void setCurrent_reading_index(int current_reading_index) {
		this.current_reading_index = current_reading_index;
	}





	public int getBurnerQty() {
		return burnerQty;
	}





	public void setBurnerQty(int burnerQty) {
		this.burnerQty = burnerQty;
	}





	public String getUnit() {
		return unit;
	}





	public void setUnit(String unit) {
		this.unit = unit;
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

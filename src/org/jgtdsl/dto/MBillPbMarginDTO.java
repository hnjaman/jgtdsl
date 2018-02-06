package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MBillPbMarginDTO {
    private String bill_id;
	private double gas_bill;
	private double min_load_bill;
	private double meter_rent;
	private double hhv_nhv_bill;
	private double adjustment;
	private String adjustment_comments;
	private double surcharge_percentage;
	private double surcharge_amount;
	private double others;
	private String other_comments;
	private double vat_rebate_percent;
	private double vat_rebate_amount;
	private double total_amount;
	
	public String getBill_id() {
		return bill_id;
	}

	public void setBill_id(String billId) {
		bill_id = billId;
	}

	public double getGas_bill() {
		return gas_bill;
	}

	public void setGas_bill(double gasBill) {
		gas_bill = gasBill;
	}

	public double getMin_load_bill() {
		return min_load_bill;
	}

	public void setMin_load_bill(double minLoadBill) {
		min_load_bill = minLoadBill;
	}

	public double getMeter_rent() {
		return meter_rent;
	}

	public void setMeter_rent(double meterRent) {
		meter_rent = meterRent;
	}

	public double getHhv_nhv_bill() {
		return hhv_nhv_bill;
	}

	public void setHhv_nhv_bill(double hhvNhvBill) {
		hhv_nhv_bill = hhvNhvBill;
	}

	public double getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(double adjustment) {
		this.adjustment = adjustment;
	}
	
	public double getSurcharge_percentage() {
		return surcharge_percentage;
	}

	public void setSurcharge_percentage(double surchargePercentage) {
		surcharge_percentage = surchargePercentage;
	}

	public double getSurcharge_amount() {
		return surcharge_amount;
	}

	public void setSurcharge_amount(double surchargeAmount) {
		surcharge_amount = surchargeAmount;
	}

	public double getOthers() {
		return others;
	}

	public void setOthers(double others) {
		this.others = others;
	}

	public double getVat_rebate_percent() {
		return vat_rebate_percent;
	}

	public void setVat_rebate_percent(double vatRebatePercent) {
		vat_rebate_percent = vatRebatePercent;
	}

	public double getVat_rebate_amount() {
		return vat_rebate_amount;
	}

	public void setVat_rebate_amount(double vatRebateAmount) {
		vat_rebate_amount = vatRebateAmount;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double totalAmount) {
		total_amount = totalAmount;
	}

	public String getAdjustment_comments() {
		return adjustment_comments;
	}

	public void setAdjustment_comments(String adjustment_comments) {
		this.adjustment_comments = adjustment_comments;
	}
	

	public String getOther_comments() {
		return other_comments;
	}

	public void setOther_comments(String other_comments) {
		this.other_comments = other_comments;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

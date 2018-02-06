package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MBillGovtMarginDTO {
	private String bill_id;
	private double vat_amount;
	private double sd_amount;
	private double others_amount;
	private double total_amount;
	
	
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public double getVat_amount() {
		return vat_amount;
	}
	public void setVat_amount(double vatAmount) {
		vat_amount = vatAmount;
	}
	public double getSd_amount() {
		return sd_amount;
	}
	public void setSd_amount(double sdAmount) {
		sd_amount = sdAmount;
	}
	public double getOthers_amount() {
		return others_amount;
	}
	public void setOthers_amount(double othersAmount) {
		others_amount = othersAmount;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double totalAmount) {
		total_amount = totalAmount;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

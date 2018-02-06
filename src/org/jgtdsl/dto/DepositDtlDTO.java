package org.jgtdsl.dto;

public class DepositDtlDTO {

	private String deposit_id;
	private String type_id;
	private String type_name_eng;
	private double amount;
	
	public String getDeposit_id() {
		return deposit_id;
	}
	public void setDeposit_id(String depositId) {
		deposit_id = depositId;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String typeId) {
		type_id = typeId;
	}
	public String getType_name_eng() {
		return type_name_eng;
	}
	public void setType_name_eng(String typeNameEng) {
		type_name_eng = typeNameEng;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}

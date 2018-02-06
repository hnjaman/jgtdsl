package org.jgtdsl.dto;

import com.google.gson.Gson;

public class DepositLedgerDTO {

	private String trans_id;
	private String deposit_id;
	private String deposit_date;
	private String description;
	private String security_amount;
	private String debit_amount;
	private String credit_amount;
	private String balance_amount;
	private String status;
	private String expire_date;
	private String deposit;
	private String deposit_type;
	
	
	public String getDeposit_type() {
		return deposit_type;
	}


	public void setDeposit_type(String deposit_type) {
		this.deposit_type = deposit_type;
	}


	public String getTrans_id() {
		return trans_id;
	}


	public void setTrans_id(String transId) {
		trans_id = transId;
	}


	public String getDeposit_id() {
		return deposit_id;
	}


	public void setDeposit_id(String depositId) {
		deposit_id = depositId;
	}


	public String getDeposit_date() {
		return deposit_date;
	}


	public void setDeposit_date(String depositDate) {
		deposit_date = depositDate;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getDebit_amount() {
		return debit_amount;
	}


	public void setDebit_amount(String debitAmount) {
		debit_amount = debitAmount;
	}


	public String getCredit_amount() {
		return credit_amount;
	}


	public void setCredit_amount(String creditAmount) {
		credit_amount = creditAmount;
	}


	public String getBalance_amount() {
		return balance_amount;
	}


	public void setBalance_amount(String balanceAmount) {
		balance_amount = balanceAmount;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	

	
	public String getSecurity_amount() {
		return security_amount;
	}


	public void setSecurity_amount(String securityAmount) {
		security_amount = securityAmount;
	}


	public String getExpire_date() {
		return expire_date;
	}


	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}


	public String getDeposit() {
		return deposit;
	}


	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}


	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }		
}

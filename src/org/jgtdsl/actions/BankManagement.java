package org.jgtdsl.actions;

import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.BankTransactionService;
import org.jgtdsl.models.MeterRentService;

import com.google.gson.Gson;

public class BankManagement extends BaseAction {
	private static final long serialVersionUID = 1L;
	private String bank_id;
	private String branch_id;
	private String account_no;
	private String month;
	private String year;
	private String trans_type;
	

	public String getOpeningBalance()
	{
		BankTransactionService bankTransaction=new BankTransactionService();
		
		Double opening_balance=bankTransaction.getOpeningBalance(bank_id,branch_id,account_no,month,year,trans_type);
		
		Gson gson = new Gson();
		String json = gson.toJson(opening_balance);
		setJsonResponse(json);
		
		return null;
	}


	public String getBank_id() {
		return bank_id;
	}


	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}


	public String getBranch_id() {
		return branch_id;
	}


	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}


	public String getAccount_no() {
		return account_no;
	}


	public void setAccount_no(String account_no) {
		this.account_no = account_no;
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


	public String getTrans_type() {
		return trans_type;
	}


	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}

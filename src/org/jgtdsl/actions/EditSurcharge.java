package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.CustomerLedgerDTO;
import org.jgtdsl.models.LedgerService;

import com.google.gson.Gson;

public class EditSurcharge extends BaseAction{
	private static final long serialVersionUID = -5599690018556916999L;
	private CustomerLedgerDTO cl;
	private String customer_id;
	private int month;
	private int year;
	public CustomerLedgerDTO getCl() {
		return cl;
	}
	public void setCl(CustomerLedgerDTO cl) {
		this.cl = cl;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
    public String getNMLedgerByMonthYear(){
		
		LedgerService customerLedger=new LedgerService();
		ArrayList<CustomerLedgerDTO> customerList=customerLedger.getNMLedgerByMonthYear(customer_id,month,year);		
		Gson gson = new Gson();
		String json = gson.toJson(customerList);
		setJsonResponse(json);
        return null;
	}
    
    public String updateNMSurcharge(){
		
		//this is a test comment
		LedgerService customerLedger=new LedgerService();
		String msg = customerLedger.updateNMSurcharge(cl);	
		msg = "<h2>Successfully Updated surcharge. "+msg+"<h2>";
		Gson gson = new Gson();
		String json = gson.toJson(msg);
		setJsonResponse(json);
        return null;
	}
}

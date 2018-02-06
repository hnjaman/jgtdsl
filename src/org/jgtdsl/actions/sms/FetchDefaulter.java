package org.jgtdsl.actions.sms;

import java.util.ArrayList;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerSmsDTO;
import org.jgtdsl.models.SMSService;

public class FetchDefaulter extends BaseAction{
	private static final long serialVersionUID = 938407232506252205L;

	private String areaId;
	private String customerCategory;
	private String billMonth;
	private String billYear;
	private String criteriaType;
	private String monthNumber;
	private String dueDate;
	private ArrayList<CustomerSmsDTO> custList;
	
	public String execute()
	{
		
		SMSService smsService =new SMSService();
		
		String str=smsService.saveSMSDefaulter(areaId, customerCategory, billMonth, billYear, monthNumber, dueDate);
				
		
		if(str.equalsIgnoreCase("success"))
		{
			custList=smsService.getSMSDefaulter(areaId, customerCategory, billMonth, billYear);
			
		}
				
		
		return "success";	
	}


	public String getAreaId() {
		return areaId;
	}


	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}


	public String getCustomerCategory() {
		return customerCategory;
	}


	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}


	public String getBillMonth() {
		return billMonth;
	}


	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}


	public String getBillYear() {
		return billYear;
	}


	public void setBillYear(String billYear) {
		this.billYear = billYear;
	}


	public String getCriteriaType() {
		return criteriaType;
	}


	public void setCriteriaType(String criteriaType) {
		this.criteriaType = criteriaType;
	}


	public String getMonthNumber() {
		return monthNumber;
	}


	public void setMonthNumber(String monthNumber) {
		this.monthNumber = monthNumber;
	}


	public String getDueDate() {
		return dueDate;
	}


	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}


	public ArrayList<CustomerSmsDTO> getCustList() {
		return custList;
	}


	public void setCustList(ArrayList<CustomerSmsDTO> custList) {
		this.custList = custList;
	}
	
	
	
}

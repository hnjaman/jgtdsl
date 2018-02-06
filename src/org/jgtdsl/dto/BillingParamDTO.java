package org.jgtdsl.dto;

import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.enums.Month;

import com.google.gson.Gson;

public class BillingParamDTO {

	private String bill_for; //individual_customer,category_wise,area_wise
	private String customer_id;
	private String billing_year;
	private Month billing_month;
	private String billing_month_str;
	private String billing_month_name;

	private MeteredStatus isMetered;
	private String isMetered_str;
	private String isMetered_name;
	
	private String customer_category;
	private String customer_category_name;
	
	private String processed_by;
	private String processed_on;
	private String area_id;	
	private String area_name;
	private String issue_date;
	private String bill_generation_date;
	private String remarks;
	
	private String bill_id;
	private String reProcess;
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getBilling_year() {
		return billing_year;
	}
	public void setBilling_year(String billingYear) {
		billing_year = billingYear;
	}
	public Month getBilling_month() {
		return billing_month;
	}
	public void setBilling_month(Month billingMonth) {
		billing_month = billingMonth;
	}
	public String getBilling_month_str() {
		return billing_month_str;
	}
	public void setBilling_month_str(String billingMonthStr) {
		billing_month_str = billingMonthStr;
	}
	public String getBilling_month_name() {
		return billing_month_name;
	}
	public void setBilling_month_name(String billingMonthName) {
		billing_month_name = billingMonthName;
	}
	public MeteredStatus getIsMetered() {
		return isMetered;
	}
	public void setIsMetered(MeteredStatus isMetered) {
		this.isMetered = isMetered;
	}
	public String getIsMetered_str() {
		return isMetered_str;
	}
	public void setIsMetered_str(String isMeteredStr) {
		isMetered_str = isMeteredStr;
	}
	public String getIsMetered_name() {
		return isMetered_name;
	}
	public void setIsMetered_name(String isMeteredName) {
		isMetered_name = isMeteredName;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}
	public String getCustomer_category_name() {
		return customer_category_name;
	}
	public void setCustomer_category_name(String customerCategoryName) {
		customer_category_name = customerCategoryName;
	}
	public String getProcessed_by() {
		return processed_by;
	}
	public void setProcessed_by(String processedBy) {
		processed_by = processedBy;
	}
	public String getProcessed_on() {
		return processed_on;
	}
	public void setProcessed_on(String processedOn) {
		processed_on = processedOn;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String areaName) {
		area_name = areaName;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issueDate) {
		issue_date = issueDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getBill_for() {
		return bill_for;
	}
	public void setBill_for(String billFor) {
		bill_for = billFor;
	}
	
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	
	public String getBill_generation_date() {
		return bill_generation_date;
	}
	public void setBill_generation_date(String billGenerationDate) {
		bill_generation_date = billGenerationDate;
	}
	
	public String getReProcess() {
		return reProcess;
	}
	public void setReProcess(String reProcess) {
		this.reProcess = reProcess;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

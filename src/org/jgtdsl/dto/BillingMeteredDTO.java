package org.jgtdsl.dto;

import java.util.ArrayList;

import org.jgtdsl.enums.BillStatus;

public class BillingMeteredDTO {

	private String bill_id;
	private String customer_id;	
	private String month;
	private String month_name;
	private String month_year;
	private String issue_date;
	private String due_date;
	private BillStatus bill_satus;
	private String bill_status_str;
	private String bill_status_name;
	
	private CustomerInfoDTO4Bill customer_info;
	private ArrayList<MeterReadingDTO> readingList;
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMonth_name() {
		return month_name;
	}
	public void setMonth_name(String monthName) {
		month_name = monthName;
	}
	public String getMonth_year() {
		return month_year;
	}
	public void setMonth_year(String monthYear) {
		month_year = monthYear;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issueDate) {
		issue_date = issueDate;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String dueDate) {
		due_date = dueDate;
	}
	
	public CustomerInfoDTO4Bill getCustomer_info() {
		return customer_info;
	}
	public void setCustomer_info(CustomerInfoDTO4Bill customerInfo) {
		customer_info = customerInfo;
	}
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public ArrayList<MeterReadingDTO> getReadingList() {
		return readingList;
	}
	public void setReadingList(ArrayList<MeterReadingDTO> readingList) {
		this.readingList = readingList;
	}	
	public BillStatus getBill_satus() {
		return bill_satus;
	}
	public void setBill_satus(BillStatus billSatus) {
		bill_satus = billSatus;
	}
	public String getBill_status_str() {
		return bill_status_str;
	}
	public void setBill_status_str(String billStatusStr) {
		bill_status_str = billStatusStr;
	}
	public String getBill_status_name() {
		return bill_status_name;
	}
	public void setBill_status_name(String billStatusName) {
		bill_status_name = billStatusName;
	}
	
}

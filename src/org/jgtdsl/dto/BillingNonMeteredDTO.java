package org.jgtdsl.dto;

import org.jgtdsl.enums.BillStatus;
import org.jgtdsl.enums.Month;

import com.google.gson.Gson;

public class BillingNonMeteredDTO {

	private String bill_id;
	private String customer_id;
	private String customer_name;
	private Month month;
	private int month_id;
	private String str_month;
	private int year;
	private int single_burner_qnt;
	private float single_burner_rate;
	private int double_burner_qnt;
	private float double_burner_rate;
	private float bill_amount;
	private float actual_billed_amount;
	private float collected_billed_amount;
	private float actual_surcharge_amount;	
	private float surcharge_per_collection;
	private float collected_surcharge_amount;
	private float actual_payable_amount;
	private float collected_payable_amount;
	private float paid_amount;
	private float due_amount;
	private String issue_date;
	private String due_date;
	private String prepared_on;
	private String prepared_by;
	private BillStatus status;
	private String str_status;
	private String bill_type;
	private String paid_status;
	private int  status_id;
	private int surcharge_amount;
	
	
	public String getPaid_status() {
		return paid_status;
	}
	public void setPaid_status(String paid_status) {
		this.paid_status = paid_status;
	}
	public float getPaid_amount() {
		return paid_amount;
	}
	public void setPaid_amount(float paid_amount) {
		this.paid_amount = paid_amount;
	}
	public float getDue_amount() {
		return due_amount;
	}
	public void setDue_amount(float due_amount) {
		this.due_amount = due_amount;
	}
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	
	public Month getMonth() {
		return month;
	}
	public void setMonth(Month month) {
		this.month = month;
	}
	public int getMonth_id() {
		return month_id;
	}
	public void setMonth_id(int monthId) {
		month_id = monthId;
	}
	public String getStr_month() {
		return str_month;
	}
	public void setStr_month(String strMonth) {
		str_month = strMonth;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getSingle_burner_qnt() {
		return single_burner_qnt;
	}
	public void setSingle_burner_qnt(int singleBurnerQnt) {
		single_burner_qnt = singleBurnerQnt;
	}
	public float getSingle_burner_rate() {
		return single_burner_rate;
	}
	public void setSingle_burner_rate(float singleBurnerRate) {
		single_burner_rate = singleBurnerRate;
	}
	public int getDouble_burner_qnt() {
		return double_burner_qnt;
	}
	public void setDouble_burner_qnt(int doubleBurnerQnt) {
		double_burner_qnt = doubleBurnerQnt;
	}
	public float getDouble_burner_rate() {
		return double_burner_rate;
	}
	public void setDouble_burner_rate(float doubleBurnerRate) {
		double_burner_rate = doubleBurnerRate;
	}
	public float getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(float billAmount) {
		bill_amount = billAmount;
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
	public String getPrepared_on() {
		return prepared_on;
	}
	public void setPrepared_on(String preparedOn) {
		prepared_on = preparedOn;
	}
	public String getPrepared_by() {
		return prepared_by;
	}
	public void setPrepared_by(String preparedBy) {
		prepared_by = preparedBy;
	}
	public BillStatus getStatus() {
		return status;
	}
	public void setStatus(BillStatus status) {
		this.status = status;
	}
	public String getStr_status() {
		return str_status;
	}
	public void setStr_status(String strStatus) {
		str_status = strStatus;
	}
	public int getStatus_id() {
		return status_id;
	}
	public void setStatus_id(int statusId) {
		status_id = statusId;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}

	public float getActual_billed_amount() {
		return actual_billed_amount;
	}
	public void setActual_billed_amount(float actual_billed_amount) {
		this.actual_billed_amount = actual_billed_amount;
	}
	public int getSurcharge_amount() {
		return surcharge_amount;
	}
	public void setSurcharge_amount(int surchargeAmount) {
		surcharge_amount = surchargeAmount;
	}
	
	
	public float getCollected_billed_amount() {
		return collected_billed_amount;
	}
	public void setCollected_billed_amount(float collected_billed_amount) {
		this.collected_billed_amount = collected_billed_amount;
	}

	public float getCollected_surcharge_amount() {
		return collected_surcharge_amount;
	}
	public void setCollected_surcharge_amount(float collected_surcharge_amount) {
		this.collected_surcharge_amount = collected_surcharge_amount;
	}


	public float getActual_surcharge_amount() {
		return actual_surcharge_amount;
	}
	public void setActual_surcharge_amount(float actual_surcharge_amount) {
		this.actual_surcharge_amount = actual_surcharge_amount;
	}
	public float getActual_payable_amount() {
		return actual_payable_amount;
	}
	public void setActual_payable_amount(float actual_payable_amount) {
		this.actual_payable_amount = actual_payable_amount;
	}
	public float getCollected_payable_amount() {
		return collected_payable_amount;
	}
	public void setCollected_payable_amount(float collected_payable_amount) {
		this.collected_payable_amount = collected_payable_amount;
	}
	
	public float getSurcharge_per_collection() {
		return surcharge_per_collection;
	}
	public void setSurcharge_per_collection(float surcharge_per_collection) {
		this.surcharge_per_collection = surcharge_per_collection;
	}
	
	public String getBill_type() {
		return bill_type;
	}
	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}
	@Override
	public String toString() {
		Gson gosn=new Gson();
		return gosn.toJson(this);
	}
}

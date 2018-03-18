package org.jgtdsl.dto;

import java.util.ArrayList;

import org.jgtdsl.enums.BillStatus;

import com.google.gson.Gson;

public class MBillDTO {

	private String bill_id;
	private String billed_amount;
	private String invoice_no;
	private int bill_month;
	private String bill_month_name;
	private int bill_year;
	private String customer_id;
	private String customer_name;
	private String proprietor_name;
	private String customer_category;
	private String customer_category_name;
	private String customer_type;
	private String area_id;
	private String area_name;
	private String address;
	private String phone;
	private String mobile;
	private String issue_date;
	private String last_pay_date_wo_sc;
	private String last_pay_date_w_sc;
	private String last_disconn_reconn_date;
	private double monthly_contractual_load;
	private double minimum_load;
	private double actual_gas_consumption;
	private double other_consumption;
	private double mixed_consumption;
	private double hhv_nhv_adj_qnt;
	private double billed_consumption;
	private double payable_amount;
	private String amount_in_word;
	private String prepared_by;
	private String prepared_date; 
	private BillStatus bill_status;
	private String bill_status_str;
	private String bill_status_name;
	
	private MBillGovtMarginDTO govtMarginDTO;
	private MBillPbMarginDTO pbMarginDTO;
	private MeterReadingDTO  readingDTO;
	
	private ArrayList<MeterReadingDTO> readingList;
	
	public String getBilled_amount() {
		return billed_amount;
	}
	public void setBilled_amount(String billed_amount) {
		this.billed_amount = billed_amount;
	}
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public String getInvoice_no() {
		return invoice_no;
	}
	public void setInvoice_no(String invoiceNo) {
		invoice_no = invoiceNo;
	}
	public int getBill_month() {
		return bill_month;
	}
	public void setBill_month(int billMonth) {
		bill_month = billMonth;
	}
	public String getBill_month_name() {
		return bill_month_name;
	}
	public void setBill_month_name(String billMonthName) {
		bill_month_name = billMonthName;
	}
	public int getBill_year() {
		return bill_year;
	}
	public void setBill_year(int billYear) {
		bill_year = billYear;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issueDate) {
		issue_date = issueDate;
	}
	public String getLast_pay_date_wo_sc() {
		return last_pay_date_wo_sc;
	}
	public void setLast_pay_date_wo_sc(String lastPayDateWoSc) {
		last_pay_date_wo_sc = lastPayDateWoSc;
	}
	public String getLast_pay_date_w_sc() {
		return last_pay_date_w_sc;
	}
	public void setLast_pay_date_w_sc(String lastPayDateWSc) {
		last_pay_date_w_sc = lastPayDateWSc;
	}
	public String getLast_disconn_reconn_date() {
		return last_disconn_reconn_date;
	}
	public void setLast_disconn_reconn_date(String lastDisconnReconnDate) {
		last_disconn_reconn_date = lastDisconnReconnDate;
	}
	public double getMonthly_contractual_load() {
		return monthly_contractual_load;
	}
	public void setMonthly_contractual_load(double monthlyContractualLoad) {
		monthly_contractual_load = monthlyContractualLoad;
	}
	public double getMinimum_load() {
		return minimum_load;
	}
	public void setMinimum_load(double minimumLoad) {
		minimum_load = minimumLoad;
	}
	public double getActual_gas_consumption() {
		return actual_gas_consumption;
	}
	public void setActual_gas_consumption(double actualGasConsumption) {
		actual_gas_consumption = actualGasConsumption;
	}
	public double getOther_consumption() {
		return other_consumption;
	}
	public void setOther_consumption(double otherConsumption) {
		other_consumption = otherConsumption;
	}
	public double getMixed_consumption() {
		return mixed_consumption;
	}
	public void setMixed_consumption(double mixedConsumption) {
		mixed_consumption = mixedConsumption;
	}
	public double getHhv_nhv_adj_qnt() {
		return hhv_nhv_adj_qnt;
	}
	public void setHhv_nhv_adj_qnt(double hhvNhvAdjQnt) {
		hhv_nhv_adj_qnt = hhvNhvAdjQnt;
	}
	public double getBilled_consumption() {
		return billed_consumption;
	}
	public void setBilled_consumption(double billedConsumption) {
		billed_consumption = billedConsumption;
	}
	public double getPayable_amount() {
		return payable_amount;
	}
	public void setPayable_amount(double payableAmount) {
		payable_amount = payableAmount;
	}
	public String getAmount_in_word() {
		return amount_in_word;
	}
	public void setAmount_in_word(String amountInWord) {
		amount_in_word = amountInWord;
	}
	public String getPrepared_by() {
		return prepared_by;
	}
	public void setPrepared_by(String preparedBy) {
		prepared_by = preparedBy;
	}
	public String getPrepared_date() {
		return prepared_date;
	}
	public void setPrepared_date(String preparedDate) {
		prepared_date = preparedDate;
	}	
	public MBillGovtMarginDTO getGovtMarginDTO() {
		return govtMarginDTO;
	}
	public void setGovtMarginDTO(MBillGovtMarginDTO govtMarginDTO) {
		this.govtMarginDTO = govtMarginDTO;
	}
	public MBillPbMarginDTO getPbMarginDTO() {
		return pbMarginDTO;
	}
	public void setPbMarginDTO(MBillPbMarginDTO pbMarginDTO) {
		this.pbMarginDTO = pbMarginDTO;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}
	public String getProprietor_name() {
		return proprietor_name;
	}
	public void setProprietor_name(String proprietorName) {
		proprietor_name = proprietorName;
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
	public String getCustomer_type() {
		return customer_type;
	}
	public void setCustomer_type(String customerType) {
		customer_type = customerType;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}	
	public MeterReadingDTO getReadingDTO() {
		return readingDTO;
	}
	public void setReadingDTO(MeterReadingDTO readingDTO) {
		this.readingDTO = readingDTO;
	}
	
	public ArrayList<MeterReadingDTO> getReadingList() {
		return readingList;
	}
	public void setReadingList(ArrayList<MeterReadingDTO> readingList) {
		this.readingList = readingList;
	}
	
	public BillStatus getBill_status() {
		return bill_status;
	}
	public void setBill_status(BillStatus billStatus) {
		bill_status = billStatus;
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
	

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

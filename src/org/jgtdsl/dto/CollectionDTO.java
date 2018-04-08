package org.jgtdsl.dto;

import com.google.gson.Gson;

public class CollectionDTO {

	private String scroll_no;
	private String month_from;
	private String month_to;
	private String year_from;
	private String year_to;
	
	private String collection_id;
	private String customer_id;
	private String area_id;
	private String area_name;
	private String category_id;
	private String category_name;
	private String customer_name;
	private String father_name;
	private String address;
	private String mobile;
	private String phone;
	
	private String bill_id;
	private String invoice_no;
	private String bill_month;
	private String bill_month_name;
	private int bill_month_value;
	private String bill_year;
	private String bill_month_year;
	private String is_metered;
	private String remarks;
	private String bank_id;
	private String bank_name;
	private String branch_id;
	private String branch_name;
	private String account_no;
	private String account_name;
	private String collection_date;
	private String collection_date_f1;
	
	private double billed_amount;
	private double adjustment_amount;	
	private double adjustment_consumption;
	private double total_consumption;
	private double surcharge_actual;
	private double surcharge_collected;
	
	private String pending_bills_str;
	private String advanced_bills_str;
	private String bill_type;
	private String paid_status;
	private double advanced_amount;
	private String isMetere;
	private String isMeter_str;

	
	
	private double vat_rebate_amount;
	private double payable_amount;
	private double surcharge_amount_metered;
	private double tax_amount=0.0;
	private String tax_no;
	private String tax_date;
	private double net_payable_amount;	
	private double collection_amount;	
	private String adjustment_comment;
	
	/*** NonMeter Specific Fields**/
	private double actual_billed_amount;
	private double collected_billed_amount;
	private double actual_surcharge_amount;
	private double collected_surcharge_amount;
	private double actual_payable_amount;
	private double collected_payable_amount;	
	private double surcharge_amount;
	private double surcharge_per_collection;
	private int    is_codeless=0;
	
	/*** Common Amount **/
	private double collected_amount;
	private double remaining_amount;
	
	private CustomerDTO customer;
	private String inserted_by;
	private String inserted_on;

	private String statusId;
	private String statusName;
	
	private String double_burner_qnt;
	private String from_month;
	private String from_year;
	private String to_month;
	private String to_year;
	
	
	
	
	public String getScroll_no() {
		return scroll_no;
	}

	public void setScroll_no(String scroll_no) {
		this.scroll_no = scroll_no;
	}

	public String getMonth_from() {
		return month_from;
	}

	public void setMonth_from(String month_from) {
		this.month_from = month_from;
	}

	public String getMonth_to() {
		return month_to;
	}

	public void setMonth_to(String month_to) {
		this.month_to = month_to;
	}

	public String getYear_from() {
		return year_from;
	}

	public void setYear_from(String year_from) {
		this.year_from = year_from;
	}

	public String getYear_to() {
		return year_to;
	}

	public int getBill_month_value() {
		return bill_month_value;
	}

	public void setBill_month_value(int i) {
		this.bill_month_value = i;
	}

	public void setYear_to(String year_to) {
		this.year_to = year_to;
	}

	public int getIs_codeless() {
		return is_codeless;
	}

	public void setIs_codeless(int is_codeless) {
		this.is_codeless = is_codeless;
	}

	public String getIsMetere() {
		return isMetere;
	}

	public void setIsMetere(String isMetere) {
		this.isMetere = isMetere;
	}

	public String getIsMeter_str() {
		return isMeter_str;
	}

	public void setIsMeter_str(String isMeter_str) {
		this.isMeter_str = isMeter_str;
	}

	public String getPaid_status() {
		return paid_status;
	}

	public void setPaid_status(String paid_status) {
		this.paid_status = paid_status;
	}

	
	
	




	public String getFrom_month() {
		return from_month;
	}

	public void setFrom_month(String from_month) {
		this.from_month = from_month;
	}

	public String getFrom_year() {
		return from_year;
	}

	public void setFrom_year(String from_year) {
		this.from_year = from_year;
	}

	public String getTo_month() {
		return to_month;
	}

	public void setTo_month(String to_month) {
		this.to_month = to_month;
	}

	public String getTo_year() {
		return to_year;
	}

	public void setTo_year(String to_year) {
		this.to_year = to_year;
	}

	public String getPending_bills_str() {
		return pending_bills_str;
	}

	public void setPending_bills_str(String pending_bills_str) {
		this.pending_bills_str = pending_bills_str;
	}

	public String getAdvanced_bills_str() {
		return advanced_bills_str;
	}

	public void setAdvanced_bills_str(String advanced_bills_str) {
		this.advanced_bills_str = advanced_bills_str;
	}

	public String getDouble_burner_qnt() {
		return double_burner_qnt;
	}

	public void setDouble_burner_qnt(String double_burner_qnt) {
		this.double_burner_qnt = double_burner_qnt;
	}

	public String getTax_no() {
		return tax_no;
	}

	public void setTax_no(String tax_no) {
		this.tax_no = tax_no;
	}

	public String getTax_date() {
		return tax_date;
	}

	public void setTax_date(String tax_date) {
		this.tax_date = tax_date;
	}

	public double getTotal_consumption() {
		return total_consumption;
	}

	public void setTotal_consumption(double total_consumption) {
		this.total_consumption = total_consumption;
	}



	public double getAdjustment_consumption() {
		return adjustment_consumption;
	}

	public void setAdjustment_consumption(double adjustment_consumption) {
		this.adjustment_consumption = adjustment_consumption;
	}

	public double getSurcharge_amount_metered() {
		return surcharge_amount_metered;
	}

	public void setSurcharge_amount_metered(double surcharge_amount_metered) {
		this.surcharge_amount_metered = surcharge_amount_metered;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getBill_id() {
		return bill_id;
	}

	public void setBill_id(String billId) {
		bill_id = billId;
	}

	public String getIs_metered() {
		return is_metered;
	}

	public void setIs_metered(String isMetered) {
		is_metered = isMetered;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bankId) {
		bank_id = bankId;
	}
	
	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branchId) {
		branch_id = branchId;
	}

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String accountNo) {
		account_no = accountNo;
	}

	public String getCollection_date() {
		return collection_date;
	}

	public void setCollection_date(String collectionDate) {
		collection_date = collectionDate;
	}

	public double getSurcharge_amount() {
		return surcharge_amount;
	}

	public void setSurcharge_amount(double surchargeAmount) {
		surcharge_amount = surchargeAmount;
	}

	public double getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(double taxAmount) {
		tax_amount = taxAmount;
	}	
	public double getVat_rebate_amount() {
		return vat_rebate_amount;
	}

	public void setVat_rebate_amount(double vatRebateAmount) {
		vat_rebate_amount = vatRebateAmount;
	}
	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public String getBill_month() {
		return bill_month;
	}

	public void setBill_month(String billMonth) {
		bill_month = billMonth;
	}

	public String getBill_year() {
		return bill_year;
	}

	public void setBill_year(String billYear) {
		bill_year = billYear;
	}
	
	public String getBill_month_year() {
		return bill_month_year;
	}

	public void setBill_month_year(String billMonthYear) {
		bill_month_year = billMonthYear;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bankName) {
		bank_name = bankName;
	}

	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branchName) {
		branch_name = branchName;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String accountName) {
		account_name = accountName;
	}

	public String getCollection_date_f1() {
		return collection_date_f1;
	}

	public void setCollection_date_f1(String collectionDateF1) {
		collection_date_f1 = collectionDateF1;
	}
	
	public String getCollection_id() {
		return collection_id;
	}

	public void setCollection_id(String collectionId) {
		collection_id = collectionId;
	}
	
	public double getBilled_amount() {
		return billed_amount;
	}

	public void setBilled_amount(double billedAmount) {
		billed_amount = billedAmount;
	}

	public double getPayable_amount() {
		return payable_amount;
	}

	public void setPayable_amount(double payableAmount) {
		payable_amount = payableAmount;
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

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String categoryId) {
		category_id = categoryId;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String categoryName) {
		category_name = categoryName;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String fatherName) {
		father_name = fatherName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBill_month_name() {
		return bill_month_name;
	}

	public void setBill_month_name(String billMonthName) {
		bill_month_name = billMonthName;
	}
	
	public double getNet_payable_amount() {
		return net_payable_amount;
	}

	public void setNet_payable_amount(double netPayableAmount) {
		net_payable_amount = netPayableAmount;
	}

	public double getCollection_amount() {
		return collection_amount;
	}

	public void setCollection_amount(double collectionAmount) {
		collection_amount = collectionAmount;
	}
	
	public double getAdjustment_amount() {
		return adjustment_amount;
	}

	public void setAdjustment_amount(double adjustmentAmount) {
		adjustment_amount = adjustmentAmount;
	}
	
	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoiceNo) {
		invoice_no = invoiceNo;
	}
	
	public String getAdjustment_comment() {
		return adjustment_comment;
	}

	public void setAdjustment_comment(String adjustmentComment) {
		adjustment_comment = adjustmentComment;
	}
	
	public String getInserted_by() {
		return inserted_by;
	}

	public void setInserted_by(String insertedBy) {
		inserted_by = insertedBy;
	}

	public String getInserted_on() {
		return inserted_on;
	}

	public void setInserted_on(String insertedOn) {
		inserted_on = insertedOn;
	}
	
	public double getSurcharge_actual() {
		return surcharge_actual;
	}

	public void setSurcharge_actual(double surchargeActual) {
		surcharge_actual = surchargeActual;
	}

	public double getSurcharge_collected() {
		return surcharge_collected;
	}

	public void setSurcharge_collected(double surchargeCollected) {
		surcharge_collected = surchargeCollected;
	}
	
	public double getActual_payable_amount() {
		return actual_payable_amount;
	}

	public void setActual_payable_amount(double actualPayableAmount) {
		actual_payable_amount = actualPayableAmount;
	}

	public double getCollected_payable_amount() {
		return collected_payable_amount;
	}

	public void setCollected_payable_amount(double collectedPayableAmount) {
		collected_payable_amount = collectedPayableAmount;
	}

	public double getActual_billed_amount() {
		return actual_billed_amount;
	}

	public void setActual_billed_amount(double actualBilledAmount) {
		actual_billed_amount = actualBilledAmount;
	}

	public double getCollected_billed_amount() {
		return collected_billed_amount;
	}

	public void setCollected_billed_amount(double collectedBilledAmount) {
		collected_billed_amount = collectedBilledAmount;
	}

	public double getActual_surcharge_amount() {
		return actual_surcharge_amount;
	}

	public void setActual_surcharge_amount(double actualSurchargeAmount) {
		actual_surcharge_amount = actualSurchargeAmount;
	}

	public double getCollected_surcharge_amount() {
		return collected_surcharge_amount;
	}

	public void setCollected_surcharge_amount(double collectedSurchargeAmount) {
		collected_surcharge_amount = collectedSurchargeAmount;
	}

	public double getCollected_amount() {
		return collected_amount;
	}

	public void setCollected_amount(double collectedAmount) {
		collected_amount = collectedAmount;
	}
	

	public double getSurcharge_per_collection() {
		return surcharge_per_collection;
	}

	public void setSurcharge_per_collection(double surcharge_per_collection) {
		this.surcharge_per_collection = surcharge_per_collection;
	}

	public double getRemaining_amount() {
		return remaining_amount;
	}

	public void setRemaining_amount(double remainingAmount) {
		remaining_amount = remainingAmount;
	}
	
	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	

	public String getBill_type() {
		return bill_type;
	}

	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}

	

	public double getAdvanced_amount() {
		return advanced_amount;
	}

	public void setAdvanced_amount(double advanced_amount) {
		this.advanced_amount = advanced_amount;
	}

	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }


}

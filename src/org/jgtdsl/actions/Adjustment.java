package org.jgtdsl.actions;
import org.jgtdsl.dto.BillAdjustmentDTO;
import org.jgtdsl.dto.DuesSurchargeDTO;
import org.jgtdsl.dto.OthersDto;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.BillingService;

import com.google.gson.Gson;

public class Adjustment extends BaseAction {

	//testing svn
	private static final long serialVersionUID = 4216989970426874378L;
	public  String bill_id;
	public  String customer_id;
	public  double payable_amount;
	private double adjustment_amount;
	private String adjustment_sign;
	private double new_payable_amount;
	private double total_consumption;
	private double adjustment_consumption;
	private String consumption_sign;
	private double new_total_consumption;
	private String remarks;
	private String metered_status;
	
	private String others_comments;
	private String others_amount;
	private String total_others_amount;
	private String total_others_comment;
	private BillAdjustmentDTO billAdjustmentDTO;
	
	private String customer_name;
	private String father_name;
	private String address;
	private String customer_category;
	private String meter_status;
	private String bill_month;
	private String bill_year;
	private String issue_date;
	private String due_date;
	private double bill_amount;
	private double surcharge_amount;
	private double meter_rent;
	private double total_amount;
	private String payment_status;
	
	
	public String getBillInfoForAdjustment(){
// checking
		BillingService billService=new BillingService();
		DuesSurchargeDTO bill=billService.getBillInfoForAdjustment(bill_id,customer_id);
		Gson gson = new Gson();
		String json = gson.toJson(bill);
		setJsonResponse(json);
        return null;
	}
	public String getBillInfoForNonMeterSalesAdjustment(){

		BillingService billService=new BillingService();
		DuesSurchargeDTO bill=billService.getBillInfoForNonMeterSalesAdjustment(bill_id,customer_id);
		Gson gson = new Gson();
		String json = gson.toJson(bill);
		setJsonResponse(json);
        return null;
	}
	
	public String saveAdjustmentInfo(){

		BillingService billService=new BillingService();
		double adj_amount=Double.valueOf(adjustment_sign+adjustment_amount);
		ResponseDTO response=billService.updateAdjustmentInfo(bill_id, metered_status, adj_amount, remarks);
		setJsonResponse(response);
        return null;
	}
	public String saveNonMeterSalesAdjustmentInfo(){

		BillingService billService=new BillingService();
		double adj_amount=Double.valueOf(adjustment_sign+adjustment_amount);
		double adj_consumtion=Double.valueOf(consumption_sign+adjustment_consumption);
		ResponseDTO response=billService.updateNonMeterSalesAdjustmentInfo(bill_id, metered_status,payable_amount,adj_amount,new_payable_amount,total_consumption,adj_consumtion,new_total_consumption,remarks);
		setJsonResponse(response);
        return null;
	}
	public String saveOthersAmountForBilling(){

		BillingService billService=new BillingService();
		ResponseDTO response=billService.saveOthersAmountForBilling(bill_id,customer_id, others_comments, others_amount,  Double.valueOf(total_others_amount),total_others_comment);
		setJsonResponse(response);
        return null;
	}
	
	public String getOthersAmountInfo(){

		BillingService billService=new BillingService();
		OthersDto others=billService.getOthersInfoForBill(bill_id,customer_id);
		Gson gson = new Gson();
		String json = gson.toJson(others);
		setJsonResponse(json);
        return null;
	}
	
	public String saveSalesAdjustmentInfo(){
		BillingService billService=new BillingService();
		ResponseDTO response =billService.saveSalesAdjustmentBilling(customer_id,bill_month,bill_year,issue_date,due_date,bill_amount,surcharge_amount, meter_rent,total_consumption, payment_status);
		setJsonResponse(response);
		return null;
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

	

	public double getPayable_amount() {
		return payable_amount;
	}
	public void setPayable_amount(double payable_amount) {
		this.payable_amount = payable_amount;
	}
	public double getAdjustment_amount() {
		return adjustment_amount;
	}
	public void setAdjustment_amount(double adjustment_amount) {
		this.adjustment_amount = adjustment_amount;
	}
	public double getNew_payable_amount() {
		return new_payable_amount;
	}
	public void setNew_payable_amount(double new_payable_amount) {
		this.new_payable_amount = new_payable_amount;
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
	public String getConsumption_sign() {
		return consumption_sign;
	}
	public void setConsumption_sign(String consumption_sign) {
		this.consumption_sign = consumption_sign;
	}
	public double getNew_total_consumption() {
		return new_total_consumption;
	}
	public void setNew_total_consumption(double new_total_consumption) {
		this.new_total_consumption = new_total_consumption;
	}
	public String getAdjustment_sign() {
		return adjustment_sign;
	}

	public void setAdjustment_sign(String adjustmentSign) {
		adjustment_sign = adjustmentSign;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getMetered_status() {
		return metered_status;
	}

	public void setMetered_status(String meteredStatus) {
		metered_status = meteredStatus;
	}

	public String getOthers_comments() {
		return others_comments;
	}

	public void setOthers_comments(String othersComments) {
		others_comments = othersComments;
	}

	public String getOthers_amount() {
		return others_amount;
	}

	public void setOthers_amount(String othersAmount) {
		others_amount = othersAmount;
	}

	public String getTotal_others_amount() {
		return total_others_amount;
	}

	public void setTotal_others_amount(String totalOthersAmount) {
		total_others_amount = totalOthersAmount;
	}

	public String getTotal_others_comment() {
		return total_others_comment;
	}

	public void setTotal_others_comment(String totalOthersComment) {
		total_others_comment = totalOthersComment;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getFather_name() {
		return father_name;
	}
	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}
	public String getMeter_status() {
		return meter_status;
	}
	public void setMeter_status(String meter_status) {
		this.meter_status = meter_status;
	}
	public String getBill_month() {
		return bill_month;
	}
	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public double getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(double bill_amount) {
		this.bill_amount = bill_amount;
	}
	public double getSurcharge_amount() {
		return surcharge_amount;
	}
	public void setSurcharge_amount(double surcharge_amount) {
		this.surcharge_amount = surcharge_amount;
	}
	public double getMeter_rent() {
		return meter_rent;
	}
	public void setMeter_rent(double meter_rent) {
		this.meter_rent = meter_rent;
	}
	public double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}
	public String getPayment_status() {
		return payment_status;
	}
	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}
	public BillAdjustmentDTO getBillAdjustmentDTO() {
		return billAdjustmentDTO;
	}
	public void setBillAdjustmentDTO(BillAdjustmentDTO billAdjustmentDTO) {
		this.billAdjustmentDTO = billAdjustmentDTO;
	}
	
}

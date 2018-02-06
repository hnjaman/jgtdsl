package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.BillingNonMeteredDTO;
import org.jgtdsl.dto.BillingParamDTO;
import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.TariffService;

public class Billing extends BaseAction{
	
	private static final long serialVersionUID = -4026297230147889320L;
	private BillingParamDTO bill_parameter;
	private String bill_id;
	private String customer_category;
	private String area_id;
	private String customer_id;
	private String billing_month;
	private String billing_year;
	private String bill_for;
	private String isMetered;
	private  ArrayList<BillingNonMeteredDTO> duesList;
	private  ArrayList<BurnerQntChangeDTO> eventList;
	
	String isDedaulterOrNot;
	public String billCreationHome()
	{
		if(bill_parameter.getIsMetered_str().equalsIgnoreCase("installment"))
			return "installment";
		else if(bill_parameter.getIsMetered_str().equalsIgnoreCase("ministry"))
			return "ministry";
		else if(Integer.parseInt(bill_parameter.getIsMetered_str())==MeteredStatus.METERED.getId())
			return "metered";
		else if(Integer.parseInt(bill_parameter.getIsMetered_str())==MeteredStatus.NONMETERED.getId())
			return "nonmetered";
		else
			return "error";
	}
	public String processBill(){
		
		BillingService billingService=new BillingService();
		UserDTO loggedInUser=(UserDTO)session.get("user");
		bill_parameter.setProcessed_by(loggedInUser.getUserId());
		ResponseDTO response=billingService.processBill(bill_parameter);
		setJsonResponse(response);
		return null;
	}
	public String showBillEvents(){	
		BillingService billingService=new BillingService();
		eventList=billingService.getNonMeterEventList(bill_id);		
		return SUCCESS;
	}
	public String getApproxTotalBillingCount(){		
		BillingService billingService=new BillingService();
		ResponseDTO response=billingService.getApproxTotalBillingCount(bill_parameter);
		setJsonResponse(response);
		return null;
	}
	public String getProcessedTotalBillingCount(){		
		BillingService billingService=new BillingService();
		ResponseDTO response=billingService.getProcessedTotalBillingCount(bill_parameter);
		setJsonResponse(response);
		return null;
	}
	
	
	public String unlockDatabase(){
		
		BillingService billingService=new BillingService();
		UserDTO loggedInUser=(UserDTO)session.get("user");		
		ResponseDTO response=billingService.unlockDatabase(isMetered,loggedInUser.getArea_id());
		setJsonResponse(response);
		return null;
	}
	public String getDuesBill(){		
		BillingService billingService=new BillingService();
		duesList=billingService.getDuesBill(customer_id);
	    return SUCCESS;
	}
	public String getDuesListByString(){		
		BillingService billingService=new BillingService();
		String duesListByString=billingService.getDuesBillListByString(customer_id);
		setJsonResponse(duesListByString);
        return null;		
	}
			
	public String approveBillByBillId(){
		BillingService bs=new BillingService();
		BillingParamDTO billingParam=new BillingParamDTO();
		billingParam.setBill_id(bill_id);
		billingParam.setIsMetered_str(isMetered);
		ResponseDTO response=bs.approveBill(billingParam);
		setJsonResponse(response);
		return null;
	}
	public String approveAllBill(){
		BillingService bs=new BillingService();
		BillingParamDTO billingParam=new BillingParamDTO();
		billingParam.setIsMetered_str(isMetered);
		billingParam.setBill_id("-99"); // Minus value indicate that this request will not treated as a approve by bill id.
		billingParam.setBill_for(bill_for);
		billingParam.setCustomer_id(customer_id);
		billingParam.setArea_id(area_id);
		billingParam.setCustomer_category(customer_category);
		billingParam.setBilling_month_str(billing_month);
		billingParam.setBilling_year(billing_year);
		ResponseDTO response=bs.approveBill(billingParam);
		setJsonResponse(response);
		return null;
	}

	public BillingParamDTO getBill_parameter() {
		return bill_parameter;
	}

	public void setBill_parameter(BillingParamDTO billParameter) {
		bill_parameter = billParameter;
	}
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getBilling_month() {
		return billing_month;
	}
	public void setBilling_month(String billingMonth) {
		billing_month = billingMonth;
	}
	public String getBilling_year() {
		return billing_year;
	}
	public void setBilling_year(String billingYear) {
		billing_year = billingYear;
	}
	public String getBill_for() {
		return bill_for;
	}
	public void setBill_for(String billFor) {
		bill_for = billFor;
	}
	public String getIsMetered() {
		return isMetered;
	}
	public void setIsMetered(String isMetered) {
		this.isMetered = isMetered;
	}
	public ArrayList<BillingNonMeteredDTO> getDuesList() {
		return duesList;
	}
	public void setDuesList(ArrayList<BillingNonMeteredDTO> duesList) {
		this.duesList = duesList;
	}
	public String getIsDedaulterOrNot() {
		return isDedaulterOrNot;
	}
	public void setIsDedaulterOrNot(String isDedaulterOrNot) {
		this.isDedaulterOrNot = isDedaulterOrNot;
	}
	public ArrayList<BurnerQntChangeDTO> getEventList() {
		return eventList;
	}
	public void setEventList(ArrayList<BurnerQntChangeDTO> eventList) {
		this.eventList = eventList;
	}
	

}

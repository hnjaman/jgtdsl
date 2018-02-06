package org.jgtdsl.reports;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.DuesSurchargeDTO;
import org.jgtdsl.dto.OthersDto;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.ReconciliationService;

import com.google.gson.Gson;

public class Reconcilation extends BaseAction {

	private static final long serialVersionUID = 4216989970426874378L;
	private String  bank_id;
	private String branch_id;
	private String account_no;
	private String collection_month;
	private String collection_year;
	private String add_comments;
	private String add_amount;
	private String lessComment;
	private String lessAmount;
	private String total_amount;
	private String opening_balance;
	private String addAccount;
	private String lessAccount;



	
	public String saveReconcilationInfo(){

		
	
		
		ResponseDTO response=ReconciliationService.saveReconcilationInfo(bank_id,branch_id,account_no,collection_month,collection_year,add_comments,add_amount,lessComment,lessAmount,addAccount,lessAccount,opening_balance,total_amount);
		setJsonResponse(response);
		
		/*ResponseDTO response=;
		response.setMessasge("Successfully Saved Adjustment Information.");
		response.setResponse(true);*/
        return null;
	}
	
	
	
	public String isReconiliatedOrNot(){
		String isReconiliatedOrNot=ReconciliationService.isReconiliatedOrNot(bank_id,branch_id,account_no,collection_month,collection_year);
		Gson gson = new Gson();
		String json = gson.toJson(isReconiliatedOrNot);
		setJsonResponse(json);
        return null;
		
	}
	
	public String getClosingBalance(){
		Double balance=ReconciliationService.getBankBookClosingBalance(bank_id,branch_id,account_no,collection_month,collection_year);
		Gson gson = new Gson();
		String json = gson.toJson(balance);
		setJsonResponse(json);
        return null;
		
	}
	
	public String deleteReconcilation(){
		ResponseDTO response=ReconciliationService.deleteReconsilation(bank_id,branch_id,account_no,collection_month,collection_year);
		setJsonResponse(response);
		return null;
		
	}
	
	
	



	public String reconciliationHome(){
		return SUCCESS;
	}

	
	
	
	
	


	public String getOpening_balance() {
		return opening_balance;
	}





	public void setOpening_balance(String opening_balance) {
		this.opening_balance = opening_balance;
	}





	public String getLessAccount() {
		return lessAccount;
	}



	public void setLessAccount(String lessAccount) {
		this.lessAccount = lessAccount;
	}



	public String getAddAccount() {
		return addAccount;
	}



	public void setAddAccount(String addAccount) {
		this.addAccount = addAccount;
	}

	






	public String getAdd_comments() {
		return add_comments;
	}





	public void setAdd_comments(String add_comments) {
		this.add_comments = add_comments;
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

	public String getCollection_month() {
		return collection_month;
	}

	public void setCollection_month(String collection_month) {
		this.collection_month = collection_month;
	}

	public String getCollection_year() {
		return collection_year;
	}

	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}



	public String getAdd_amount() {
		return add_amount;
	}

	public void setAdd_amount(String add_amount) {
		this.add_amount = add_amount;
	}

	public String getLessComment() {
		return lessComment;
	}

	public void setLessComment(String lessComment) {
		this.lessComment = lessComment;
	}

	public String getLessAmount() {
		return lessAmount;
	}

	public void setLessAmount(String lessAmount) {
		this.lessAmount = lessAmount;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	


	
}

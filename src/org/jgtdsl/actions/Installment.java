package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.DuesSurchargeDTO;
import org.jgtdsl.dto.InstallmentAgreementDTO;
import org.jgtdsl.dto.InstallmentDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.InstallmentService;

import com.google.gson.Gson;

public class Installment extends BaseAction {

	private static final long serialVersionUID = -5225874350833454959L;
	public String agreement_id;
	public String customer_id;
	public String bill_ids;
	public String installments;
	public InstallmentAgreementDTO agreement;
	public ArrayList<InstallmentDTO> installmentList;
	private ArrayList<DuesSurchargeDTO> installmentBillList;
	
	
	public String saveInstallments(){
		InstallmentService installmentService=new InstallmentService();		
		ResponseDTO response=installmentService.saveInstallments(agreement,customer_id,bill_ids,installments);
		setJsonResponse(response);
        return null;
        
	}
	
	public String getBillInstallments(){
		
		InstallmentService installmentService=new InstallmentService();
		agreement=installmentService.getInstallmentAgreement(agreement_id);
		installmentList=installmentService.getInstallments(agreement_id);
		Gson gson = new Gson();
		String json = gson.toJson(installmentList==null?"{}":installmentList);
		BillingService billingService=new BillingService();
		
		installmentBillList=billingService.getInstallmentBillList(agreement_id);
		String billList=gson.toJson(installmentBillList==null?"{}":installmentBillList);
		json="{\"agreement\":"+agreement+",\"installments\":"+json+",\"bills\":"+billList+"}";
		setJsonResponse(json);
        return null;	
	}


	
	public String getAgreement_id() {
		return agreement_id;
	}

	public void setAgreement_id(String agreementId) {
		agreement_id = agreementId;
	}

	public ArrayList<InstallmentDTO> getInstallmentList() {
		return installmentList;
	}

	public void setInstallmentList(ArrayList<InstallmentDTO> installmentList) {
		this.installmentList = installmentList;
	}

	public InstallmentAgreementDTO getAgreement() {
		return agreement;
	}

	public void setAgreement(InstallmentAgreementDTO agreement) {
		this.agreement = agreement;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getBill_ids() {
		return bill_ids;
	}

	public void setBill_ids(String billIds) {
		bill_ids = billIds;
	}

	public String getInstallments() {
		return installments;
	}

	public void setInstallments(String installments) {
		this.installments = installments;
	}

	public ArrayList<DuesSurchargeDTO> getInstallmentBillList() {
		return installmentBillList;
	}

	public void setInstallmentBillList(
			ArrayList<DuesSurchargeDTO> installmentBillList) {
		this.installmentBillList = installmentBillList;
	}

		
	
}

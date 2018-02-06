package org.jgtdsl.actions;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.InstallmentService;
import org.jgtdsl.models.SecurityAdjustmentService;

public class SecurityAdjustment extends BaseAction{

	private static final long serialVersionUID = -8072412222879273900L;
	private String customerId;
	private double securityAmount;
	private double OtherAmount;
	private String adjustmentMode;
	private double totalAdjustableAmount;
	private String comment;
	private String collectionDate;
	private String refundOther;
	private String refundBank;
	private String refundBranch;
	private String refundAccount;
	private String deductOther;
	private String deductBank;
	private String deductBranch;
	private String deductAccount;
	private String addOther;
	private String addBank;
	private String addBranch;
	private String addAccount;
	private String adjustmentBillStr;
	
	
	public String getSecurityBalance()
	{
		SecurityAdjustmentService securityAdjustmentService=new SecurityAdjustmentService();
		String balance=securityAdjustmentService.getSecurityDepositBalance(customerId);
		setJsonResponse(balance);
        return null;
        
	}
	
	
	public String saveSecurityAdjustment()
	{
		UserDTO loggedInUser=(UserDTO)session.get("user");
		SecurityAdjustmentService securityAdjustmentService=new SecurityAdjustmentService();		
		ResponseDTO response=securityAdjustmentService.saveSecurityAdjustment(customerId, securityAmount, OtherAmount, adjustmentMode, totalAdjustableAmount,
				comment, collectionDate, refundOther, refundBank, refundBranch, refundAccount, deductOther, deductBank, deductBranch, 
				deductAccount, addOther, addBank, addBranch, addAccount, adjustmentBillStr, loggedInUser.getUserId());
		setJsonResponse(response);
        return null;        
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getSecurityAmount() {
		return securityAmount;
	}

	public void setSecurityAmount(double securityAmount) {
		this.securityAmount = securityAmount;
	}

	public double getOtherAmount() {
		return OtherAmount;
	}

	public void setOtherAmount(double otherAmount) {
		OtherAmount = otherAmount;
	}

	public String getAdjustmentMode() {
		return adjustmentMode;
	}

	public void setAdjustmentMode(String adjustmentMode) {
		this.adjustmentMode = adjustmentMode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}

	public String getRefundOther() {
		return refundOther;
	}

	public void setRefundOther(String refundOther) {
		this.refundOther = refundOther;
	}

	public String getRefundBank() {
		return refundBank;
	}

	public void setRefundBank(String refundBank) {
		this.refundBank = refundBank;
	}

	public String getRefundBranch() {
		return refundBranch;
	}

	public void setRefundBranch(String refundBranch) {
		this.refundBranch = refundBranch;
	}

	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getDeductOther() {
		return deductOther;
	}

	public void setDeductOther(String deductOther) {
		this.deductOther = deductOther;
	}

	public String getDeductBank() {
		return deductBank;
	}

	public void setDeductBank(String deductBank) {
		this.deductBank = deductBank;
	}

	public String getDeductBranch() {
		return deductBranch;
	}

	public void setDeductBranch(String deductBranch) {
		this.deductBranch = deductBranch;
	}

	public String getDeductAccount() {
		return deductAccount;
	}

	public void setDeductAccount(String deductAccount) {
		this.deductAccount = deductAccount;
	}

	public String getAddOther() {
		return addOther;
	}

	public void setAddOther(String addOther) {
		this.addOther = addOther;
	}

	public String getAddBank() {
		return addBank;
	}

	public void setAddBank(String addBank) {
		this.addBank = addBank;
	}

	public String getAddBranch() {
		return addBranch;
	}

	public void setAddBranch(String addBranch) {
		this.addBranch = addBranch;
	}

	public String getAddAccount() {
		return addAccount;
	}

	public void setAddAccount(String addAccount) {
		this.addAccount = addAccount;
	}

	public String getAdjustmentBillStr() {
		return adjustmentBillStr;
	}

	public void setAdjustmentBillStr(String adjustmentBillStr) {
		this.adjustmentBillStr = adjustmentBillStr;
	}


	public double getTotalAdjustableAmount() {
		return totalAdjustableAmount;
	}


	public void setTotalAdjustableAmount(double totalAdjustableAmount) {
		this.totalAdjustableAmount = totalAdjustableAmount;
	}


	
	
	
}
package org.jgtdsl.actions;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.AuthorizationService;

public class Authorization extends BaseAction {

	private static final long serialVersionUID = -978583962839915195L;
	private String bank_id;
	private String branch_id;
	private String account_no;
	private String trans_date;
	
	public String authorizeTransaction()
	{
		ResponseDTO response;
		AuthorizationService authService=new AuthorizationService();
		response=authService.autorizeTransaction(bank_id, branch_id, account_no, trans_date);
		setJsonResponse(response);
		return null;
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

	public String getTrans_date() {
		return trans_date;
	}

	public void setTrans_date(String transDate) {
		trans_date = transDate;
	}

	
}

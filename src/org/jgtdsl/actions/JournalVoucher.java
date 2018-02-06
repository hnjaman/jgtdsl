package org.jgtdsl.actions;

import org.jgtdsl.dto.JvBankAccountCorrectionDTO;
import org.jgtdsl.dto.JvCustomerAccountCorrectionDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.JournalVoucherService;

public class JournalVoucher extends BaseAction {

	private static final long serialVersionUID = -978583962839915195L;
	private JvCustomerAccountCorrectionDTO customerAccountCorrection;
	private JvBankAccountCorrectionDTO bankAccountCorrection;
	
	public String saveCustomerAccountCorrection()
	{
		ResponseDTO response;
		customerAccountCorrection.setInserted_by(((UserDTO)session.get("user")).getUserId());
		JournalVoucherService jvService=new JournalVoucherService();
		response=jvService.saveCustomerAccountCorrection(customerAccountCorrection);
		setJsonResponse(response);
		return null;
	}
	public String saveBankAccountCorrection()
	{
		ResponseDTO response;
		bankAccountCorrection.setInserted_by(((UserDTO)session.get("user")).getUserId());
		JournalVoucherService jvService=new JournalVoucherService();
		response=jvService.saveBankAccountCorrection(bankAccountCorrection);
		setJsonResponse(response);
		return null;
	}	
	
	

	public JvCustomerAccountCorrectionDTO getCustomerAccountCorrection() {
		return customerAccountCorrection;
	}

	public void setCustomerAccountCorrection(
			JvCustomerAccountCorrectionDTO customerAccountCorrection) {
		this.customerAccountCorrection = customerAccountCorrection;
	}
	public JvBankAccountCorrectionDTO getBankAccountCorrection() {
		return bankAccountCorrection;
	}
	public void setBankAccountCorrection(
			JvBankAccountCorrectionDTO bankAccountCorrection) {
		this.bankAccountCorrection = bankAccountCorrection;
	}
	
}

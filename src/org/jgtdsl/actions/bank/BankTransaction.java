package org.jgtdsl.actions.bank;

import java.util.ArrayList;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.BankDepositWithdrawDTO;
import org.jgtdsl.dto.BillingParamDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.TransactionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.BankTransactionService;
import org.jgtdsl.models.BillingService;

public class BankTransaction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private BankDepositWithdrawDTO bankDepositWithdraw;
	private ArrayList<TransactionDTO> transactionList;
	private String transaction_id;
	private String whereClause;

	public String saveBankTransaction(){
		
		BankTransactionService bankTransaction=new BankTransactionService();
		bankDepositWithdraw.setInserted_by(((UserDTO)session.get("user")).getUserId());		
		ResponseDTO response=null;
		response=bankTransaction.saveBankTransaction(bankDepositWithdraw);
			
		setJsonResponse(response);		
		return null;
	}
	public String transactionAuthorization(){
		UserDTO loggedInUser=(UserDTO)session.get("user");
		BankTransactionService bankTransaction=new BankTransactionService();
		transactionList=bankTransaction.getUnAuthCount(loggedInUser.getArea_id());
        return SUCCESS;
	}
	
	
	public String deleteBankTransaction(){
		BankTransactionService bankTransaction=new BankTransactionService();
		ResponseDTO response=bankTransaction.deleteBankTransaction(transaction_id);
		setJsonResponse(response);
		return null;
	}
	public String getTotalDebitCredit(){
		BankTransactionService bankTransaction=new BankTransactionService();		
		setTextResponse(bankTransaction.getTotalDebitCredit(whereClause));
		return null;
	}
	public BankDepositWithdrawDTO getBankDepositWithdraw() {
		return bankDepositWithdraw;
	}

	public void setBankDepositWithdraw(BankDepositWithdrawDTO bankDepositWithdraw) {
		this.bankDepositWithdraw = bankDepositWithdraw;
	}
		
	public ArrayList<TransactionDTO> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(ArrayList<TransactionDTO> transactionList) {
		this.transactionList = transactionList;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transactionId) {
		transaction_id = transactionId;
	}
	public String getWhereClause() {
		return whereClause;
	}
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}
	
}

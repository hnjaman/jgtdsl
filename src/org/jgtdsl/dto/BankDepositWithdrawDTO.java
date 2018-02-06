package org.jgtdsl.dto;

import org.jgtdsl.enums.BankAccountTransactionMode;
import org.jgtdsl.enums.BankAccountTransactionType;

import com.google.gson.Gson;

public class BankDepositWithdrawDTO {

	private String trans_id;
	private String trans_date;
	
	private String depositWithdawId;
	
	private String source_bank_id;
	private String source_branch_id;
	private String source_account_no;
	private String source_bank_name;
	private String source_branch_name;
	private String source_account_name;
	
	private String source_transaction_date;
	private BankAccountTransactionType transaction_type;
	private String transaction_type_str;
	private String transaction_type_name;
	private String transaction_amount;
	private BankAccountTransactionMode transaction_mode;
	private String transaction_mode_str;
	private String transaction_mode_name;	
	private String source_transaction_particulars;
	private String payment_particulars;

	
	private String target_bank_id;
	private String target_branch_id;
	private String target_account_no;
	private String target_bank_name;
	private String target_branch_name;
	private String target_account_name;
	
	private String target_transaction_date;
	private String target_transaction_particulars;
	
	private String inserted_by;
	private String inserted_on;
	
	private String debit;
	private String credit;
	private String status;
	
	
	private String collection_month;
	private String collection_year;
	
	
	
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
	public String getPayment_particulars() {
		return payment_particulars;
	}
	public void setPayment_particulars(String payment_particulars) {
		this.payment_particulars = payment_particulars;
	}
	public String getDepositWithdawId() {
		return depositWithdawId;
	}
	public void setDepositWithdawId(String depositWithdawId) {
		this.depositWithdawId = depositWithdawId;
	}
	public String getSource_bank_id() {
		return source_bank_id;
	}
	public void setSource_bank_id(String sourceBankId) {
		source_bank_id = sourceBankId;
	}
	public String getSource_branch_id() {
		return source_branch_id;
	}
	public void setSource_branch_id(String sourceBranchId) {
		source_branch_id = sourceBranchId;
	}
	public String getSource_account_no() {
		return source_account_no;
	}
	public void setSource_account_no(String sourceAccountNo) {
		source_account_no = sourceAccountNo;
	}
	public String getSource_transaction_date() {
		return source_transaction_date;
	}
	public void setSource_transaction_date(String sourceTransactionDate) {
		source_transaction_date = sourceTransactionDate;
	}
	
	public String getTransaction_amount() {
		return transaction_amount;
	}
	public void setTransaction_amount(String transactionAmount) {
		transaction_amount = transactionAmount;
	}
	public String getSource_transaction_particulars() {
		return source_transaction_particulars;
	}
	public void setSource_transaction_particulars(
			String sourceTransactionParticulars) {
		source_transaction_particulars = sourceTransactionParticulars;
	}
	public String getTarget_bank_id() {
		return target_bank_id;
	}
	public void setTarget_bank_id(String targetBankId) {
		target_bank_id = targetBankId;
	}
	public String getTarget_branch_id() {
		return target_branch_id;
	}
	public void setTarget_branch_id(String targetBranchId) {
		target_branch_id = targetBranchId;
	}
	public String getTarget_account_no() {
		return target_account_no;
	}
	public void setTarget_account_no(String targetAccountNo) {
		target_account_no = targetAccountNo;
	}
	public String getTarget_transaction_date() {
		return target_transaction_date;
	}
	public void setTarget_transaction_date(String targetTransactionDate) {
		target_transaction_date = targetTransactionDate;
	}
	public String getTarget_transaction_particulars() {
		return target_transaction_particulars;
	}
	public void setTarget_transaction_particulars(
			String targetTransactionParticulars) {
		target_transaction_particulars = targetTransactionParticulars;
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
	public BankAccountTransactionType getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(BankAccountTransactionType transactionType) {
		transaction_type = transactionType;
	}
	public String getTransaction_type_str() {
		return transaction_type_str;
	}
	public void setTransaction_type_str(String transactionTypeStr) {
		transaction_type_str = transactionTypeStr;
	}
	public String getTransaction_type_name() {
		return transaction_type_name;
	}
	public void setTransaction_type_name(String transactionTypeName) {
		transaction_type_name = transactionTypeName;
	}
	
	public String getTransaction_mode_str() {
		return transaction_mode_str;
	}
	public void setTransaction_mode_str(String transactionModeStr) {
		transaction_mode_str = transactionModeStr;
	}
	public String getTransaction_mode_name() {
		return transaction_mode_name;
	}
	public void setTransaction_mode_name(String transactionModeName) {
		transaction_mode_name = transactionModeName;
	}
	public void setTransaction_mode(BankAccountTransactionMode transactionMode) {
		transaction_mode = transactionMode;
	}
	
	public BankAccountTransactionMode getTransaction_mode() {
		return transaction_mode;
	}
	
	public String getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(String transId) {
		trans_id = transId;
	}
	public String getTrans_date() {
		return trans_date;
	}
	public void setTrans_date(String transDate) {
		trans_date = transDate;
	}
	
	public String getSource_bank_name() {
		return source_bank_name;
	}
	public void setSource_bank_name(String sourceBankName) {
		source_bank_name = sourceBankName;
	}
	public String getSource_branch_name() {
		return source_branch_name;
	}
	public void setSource_branch_name(String sourceBranchName) {
		source_branch_name = sourceBranchName;
	}
	public String getSource_account_name() {
		return source_account_name;
	}
	public void setSource_account_name(String sourceAccountName) {
		source_account_name = sourceAccountName;
	}
	public String getTarget_bank_name() {
		return target_bank_name;
	}
	public void setTarget_bank_name(String targetBankName) {
		target_bank_name = targetBankName;
	}
	public String getTarget_branch_name() {
		return target_branch_name;
	}
	public void setTarget_branch_name(String targetBranchName) {
		target_branch_name = targetBranchName;
	}
	public String getTarget_account_name() {
		return target_account_name;
	}
	public void setTarget_account_name(String targetAccountName) {
		target_account_name = targetAccountName;
	}
	
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }	
}

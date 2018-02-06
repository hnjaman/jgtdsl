package org.jgtdsl.dto;

import com.google.gson.Gson;

public class BankBookDTO {

	private String collection_date;
	private String collection_year;
	private double meter_rent;
	private double total_revenue;
	private double actual_revenue;
	private double suecharge;
	private double interest;
	private double collection_month;
	private String bank_id;
	private String branch_id;
	private String account_no;
	private double miscellaneous;
	private String particular;
	private double credit;
	private String trans_date;
	private String trans_type;
	private double debit;
	private double hhv;
	
	private double totalCollection;
	private double securityDeposit;
	private double saleOfStore;
	private double connectionFee;
	private double commissionFee;
	private double distributionFee;
	private double serviceCharge;
	private double penalties;
	private double othersIncome;
	private double interestIncome;
	private double fundTransfer;
	private double pipeLineConstruction;
	private double loadIncrease;
	private double disconnectionFee;
	private double reconnectionFee;
	private double additionalBill;
	private double nameChange;
	private double burnerShifting;
	private double raizerShiftingFee;
	private double consultingFee;
	private double loadDecreaseFee;
	private double fundReceive;
	private double meterMaintaince;
	private double legalFee;
	private double SalesOfApplication;
	 private double cashBank;
	 private double bankGuaranty;
	 private double fdr;
	 private double psp;
	 private double others;
	 private String customerCategoryId;
	 private String customerCategoryName;
	 private String customerCategoryType;
	 private String customerName;
	 private String customerCode;
	 private String status;
	
	
	
	public double getDebit() {
		return debit;
	}
	public void setDebit(double debit) {
		this.debit = debit;
	}
	public String getCollection_date() {
		return collection_date;
	}
	public void setCollection_date(String collection_date) {
		this.collection_date = collection_date;
	}
	public String getCollection_year() {
		return collection_year;
	}
	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}
	public double getMeter_rent() {
		return meter_rent;
	}
	public void setMeter_rent(double meter_rent) {
		this.meter_rent = meter_rent;
	}
	public double getTotal_revenue() {
		return total_revenue;
	}
	public void setTotal_revenue(double total_revenue) {
		this.total_revenue = total_revenue;
	}
	public double getActual_revenue() {
		return actual_revenue;
	}
	public void setActual_revenue(double actual_revenue) {
		this.actual_revenue = actual_revenue;
	}
	public double getSuecharge() {
		return suecharge;
	}
	public void setSuecharge(double suecharge) {
		this.suecharge = suecharge;
	}
	public double getInterest() {
		return interest;
	}
	public void setInterest(double interest) {
		this.interest = interest;
	}
	public double getCollection_month() {
		return collection_month;
	}
	public void setCollection_month(double collection_month) {
		this.collection_month = collection_month;
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
	public double getMiscellaneous() {
		return miscellaneous;
	}
	public void setMiscellaneous(double miscellaneous) {
		this.miscellaneous = miscellaneous;
	}
	public String getParticular() {
		return particular;
	}
	public void setParticular(String particular) {
		this.particular = particular;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double transfer_amount) {
		this.credit = transfer_amount;
	}
	public String getTrans_date() {
		return trans_date;
	}
	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}
	public String getTrans_type() {
		return trans_type;
	}
	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}
	public String toString() {         
        Gson gson = new Gson();
      return gson.toJson(this);
    }
	public double getTotalCollection() {
		return totalCollection;
	}
	public void setTotalCollection(double totalCollection) {
		this.totalCollection = totalCollection;
	}
	public double getSecurityDeposit() {
		return securityDeposit;
	}
	public void setSecurityDeposit(double securityDeposit) {
		this.securityDeposit = securityDeposit;
	}
	public double getSaleOfStore() {
		return saleOfStore;
	}
	public void setSaleOfStore(double saleOfStore) {
		this.saleOfStore = saleOfStore;
	}
	public double getConnectionFee() {
		return connectionFee;
	}
	public void setConnectionFee(double connectionFee) {
		this.connectionFee = connectionFee;
	}
	public double getCommissionFee() {
		return commissionFee;
	}
	public void setCommissionFee(double commissionFee) {
		this.commissionFee = commissionFee;
	}
	public double getDistributionFee() {
		return distributionFee;
	}
	public void setDistributionFee(double distributionFee) {
		this.distributionFee = distributionFee;
	}
	public double getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public double getPenalties() {
		return penalties;
	}
	public void setPenalties(double penalties) {
		this.penalties = penalties;
	}
	public double getOthersIncome() {
		return othersIncome;
	}
	public void setOthersIncome(double othersIncome) {
		this.othersIncome = othersIncome;
	}
	public double getInterestIncome() {
		return interestIncome;
	}
	public void setInterestIncome(double interestIncome) {
		this.interestIncome = interestIncome;
	}
	public double getFundTransfer() {
		return fundTransfer;
	}
	public void setFundTransfer(double fundTransfer) {
		this.fundTransfer = fundTransfer;
	}
	public double getPipeLineConstruction() {
		return pipeLineConstruction;
	}
	public void setPipeLineConstruction(double pipeLineConstruction) {
		this.pipeLineConstruction = pipeLineConstruction;
	}
	public double getLoadIncrease() {
		return loadIncrease;
	}
	public void setLoadIncrease(double loadIncrease) {
		this.loadIncrease = loadIncrease;
	}
	public double getDisconnectionFee() {
		return disconnectionFee;
	}
	public void setDisconnectionFee(double disconnectionFee) {
		this.disconnectionFee = disconnectionFee;
	}
	public double getReconnectionFee() {
		return reconnectionFee;
	}
	public void setReconnectionFee(double reconnectionFee) {
		this.reconnectionFee = reconnectionFee;
	}
	public double getAdditionalBill() {
		return additionalBill;
	}
	public void setAdditionalBill(double additionalBill) {
		this.additionalBill = additionalBill;
	}
	public double getNameChange() {
		return nameChange;
	}
	public void setNameChange(double nameChange) {
		this.nameChange = nameChange;
	}
	public double getBurnerShifting() {
		return burnerShifting;
	}
	public void setBurnerShifting(double burnerShifting) {
		this.burnerShifting = burnerShifting;
	}
	public double getRaizerShiftingFee() {
		return raizerShiftingFee;
	}
	public void setRaizerShiftingFee(double raizerShiftingFee) {
		this.raizerShiftingFee = raizerShiftingFee;
	}
	public double getConsultingFee() {
		return consultingFee;
	}
	public void setConsultingFee(double consultingFee) {
		this.consultingFee = consultingFee;
	}
	public double getLoadDecreaseFee() {
		return loadDecreaseFee;
	}
	public void setLoadDecreaseFee(double loadDecreaseFee) {
		this.loadDecreaseFee = loadDecreaseFee;
	}
	public double getFundReceive() {
		return fundReceive;
	}
	public void setFundReceive(double fundReceive) {
		this.fundReceive = fundReceive;
	}
	public double getMeterMaintaince() {
		return meterMaintaince;
	}
	public void setMeterMaintaince(double meterMaintaince) {
		this.meterMaintaince = meterMaintaince;
	}
	public double getLegalFee() {
		return legalFee;
	}
	public void setLegalFee(double legalFee) {
		this.legalFee = legalFee;
	}
	public double getSalesOfApplication() {
		return SalesOfApplication;
	}
	public void setSalesOfApplication(double salesOfApplication) {
		SalesOfApplication = salesOfApplication;
	}
	public double getCashBank() {
		return cashBank;
	}
	public void setCashBank(double cashBank) {
		this.cashBank = cashBank;
	}
	public double getBankGuaranty() {
		return bankGuaranty;
	}
	public void setBankGuaranty(double bankGuaranty) {
		this.bankGuaranty = bankGuaranty;
	}
	public double getFdr() {
		return fdr;
	}
	public void setFdr(double fdr) {
		this.fdr = fdr;
	}
	public double getPsp() {
		return psp;
	}
	public void setPsp(double psp) {
		this.psp = psp;
	}
	public double getOthers() {
		return others;
	}
	public void setOthers(double others) {
		this.others = others;
	}
	public String getCustomerCategoryId() {
		return customerCategoryId;
	}
	public void setCustomerCategoryId(String customerCategoryId) {
		this.customerCategoryId = customerCategoryId;
	}
	public String getCustomerCategoryName() {
		return customerCategoryName;
	}
	public void setCustomerCategoryName(String customerCategoryName) {
		this.customerCategoryName = customerCategoryName;
	}
	public String getCustomerCategoryType() {
		return customerCategoryType;
	}
	public void setCustomerCategoryType(String customerCategoryType) {
		this.customerCategoryType = customerCategoryType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getHhv() {
		return hhv;
	}
	public void setHhv(double hhv) {
		this.hhv = hhv;
	}
	
	
}

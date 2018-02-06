package org.jgtdsl.dto;

import com.google.gson.Gson;

public class JournalVoucherDTO {
	
	private String date;
	private String particulars;
	private String lf;
	private String controlCode;
	private String customerCategory;
	private double debit;
	private double credit;
	private double avalue;
	
	private double powerReceivable;
	private double cngReceivable;
	private double indReceivable;
	private double comReceivable;
	private double domReceivable;
	private double powerMeterReceivable;
	private double cngMeterReceivable;
	private double comMeterReceivable;
	private double domMeterReceivable;
	private double gasBill;
	private double meterRent;
	
	
	private double bgWhelHead;
	private double bgSDVAT;
	private double bgREBATE;
	private double sgWellHead;
	private double sgSDVAT;
	private double sgREBATE;
	
	private double transmissionMargin;
	private double PDFMargin;
	private double bapexMargin;
	private double dWhelheadMargin;
	private double gdFund;
	
	private double bgfcl;
	private double sgfl;
	private double vat_revate;

	private double sales;
	

	
	private double sd;
	private double vat;
	private double wellHead;
	private String month;
	private String year;
	private String lastDay;
	private String purchaseMonth;
	private String purchaseYear;
	private String byBankMonth;
	private String byBankyear;
	private String transactionDate;
	private double purchase;
	
	private double netPayable;
	private double incomeTax;
	private double vatRebate;
	private double ratio;
	private double bill;
	private double systemGain;
	private double gtcl;
	private double aValue;
	
	
	public double getBgfcl() {
		return bgfcl;
	}



	public void setBgfcl(double bgfcl) {
		this.bgfcl = bgfcl;
	}



	public double getSgfl() {
		return sgfl;
	}



	public void setSgfl(double sgfl) {
		this.sgfl = sgfl;
	}



	public double getVat_revate() {
		return vat_revate;
	}



	public void setVat_revate(double vat_revate) {
		this.vat_revate = vat_revate;
	}



	public double getBgWhelHead() {
		return bgWhelHead;
	}



	public void setBgWhelHead(double bgWhelHead) {
		this.bgWhelHead = bgWhelHead;
	}



	public double getBgSDVAT() {
		return bgSDVAT;
	}



	public void setBgSDVAT(double bgSDVAT) {
		this.bgSDVAT = bgSDVAT;
	}



	public double getBgREBATE() {
		return bgREBATE;
	}



	public void setBgREBATE(double bgREBATE) {
		this.bgREBATE = bgREBATE;
	}



	public double getSgWellHead() {
		return sgWellHead;
	}



	public void setSgWellHead(double sgWellHead) {
		this.sgWellHead = sgWellHead;
	}



	public double getSgSDVAT() {
		return sgSDVAT;
	}



	public void setSgSDVAT(double sgSDVAT) {
		this.sgSDVAT = sgSDVAT;
	}



	public double getSgREBATE() {
		return sgREBATE;
	}



	public void setSgREBATE(double sgREBATE) {
		this.sgREBATE = sgREBATE;
	}



	public double getTransmissionMargin() {
		return transmissionMargin;
	}



	public void setTransmissionMargin(double transmissionMargin) {
		this.transmissionMargin = transmissionMargin;
	}



	public double getPDFMargin() {
		return PDFMargin;
	}



	public void setPDFMargin(double pDFMargin) {
		PDFMargin = pDFMargin;
	}



	public double getBapexMargin() {
		return bapexMargin;
	}



	public void setBapexMargin(double bapexMargin) {
		this.bapexMargin = bapexMargin;
	}



	public double getdWhelheadMargin() {
		return dWhelheadMargin;
	}



	public void setdWhelheadMargin(double dWhelheadMargin) {
		this.dWhelheadMargin = dWhelheadMargin;
	}



	public double getGdFund() {
		return gdFund;
	}



	public void setGdFund(double gdFund) {
		this.gdFund = gdFund;
	}



	public double getMeterRent() {
		return meterRent;
	}



	public void setMeterRent(double meterRent) {
		this.meterRent = meterRent;
	}



	public double getGasBill() {
		return gasBill;
	}



	public void setGasBill(double gasBill) {
		this.gasBill = gasBill;
	}



	public String getCustomerCategory() {
		return customerCategory;
	}



	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}



	public double getPowerReceivable() {
		return powerReceivable;
	}



	public void setPowerReceivable(double powerReceivable) {
		this.powerReceivable = powerReceivable;
	}



	public double getCngReceivable() {
		return cngReceivable;
	}



	public void setCngReceivable(double cngReceivable) {
		this.cngReceivable = cngReceivable;
	}



	public double getIndReceivable() {
		return indReceivable;
	}



	public void setIndReceivable(double indReceivable) {
		this.indReceivable = indReceivable;
	}



	public double getComReceivable() {
		return comReceivable;
	}



	public void setComReceivable(double comReceivable) {
		this.comReceivable = comReceivable;
	}



	public double getDomReceivable() {
		return domReceivable;
	}



	public void setDomReceivable(double domReceivable) {
		this.domReceivable = domReceivable;
	}



	public double getPowerMeterReceivable() {
		return powerMeterReceivable;
	}



	public void setPowerMeterReceivable(double powerMeterReceivable) {
		this.powerMeterReceivable = powerMeterReceivable;
	}



	public double getCngMeterReceivable() {
		return cngMeterReceivable;
	}



	public void setCngMeterReceivable(double cngMeterReceivable) {
		this.cngMeterReceivable = cngMeterReceivable;
	}



	public double getComMeterReceivable() {
		return comMeterReceivable;
	}



	public void setComMeterReceivable(double comMeterReceivable) {
		this.comMeterReceivable = comMeterReceivable;
	}



	public double getDomMeterReceivable() {
		return domMeterReceivable;
	}



	public void setDomMeterReceivable(double domMeterReceivable) {
		this.domMeterReceivable = domMeterReceivable;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getParticulars() {
		return particulars;
	}



	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}



	public String getLf() {
		return lf;
	}



	public void setLf(String lf) {
		this.lf = lf;
	}



	public String getControlCode() {
		return controlCode;
	}



	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}



	public double getDebit() {
		return debit;
	}



	public void setDebit(double debit) {
		this.debit = debit;
	}



	public double getCredit() {
		return credit;
	}



	public void setCredit(double credit) {
		this.credit = credit;
	}



	public double getAvalue() {
		return avalue;
	}



	public void setAvalue(double avalue) {
		this.avalue = avalue;
	}



	public double getSales() {
		return sales;
	}



	public void setSales(double sales) {
		this.sales = sales;
	}



	public double getSd() {
		return sd;
	}



	public void setSd(double sd) {
		this.sd = sd;
	}



	public double getVat() {
		return vat;
	}



	public void setVat(double vat) {
		this.vat = vat;
	}



	public double getWellHead() {
		return wellHead;
	}



	public void setWellHead(double wellHead) {
		this.wellHead = wellHead;
	}



	public String getMonth() {
		return month;
	}



	public void setMonth(String month) {
		this.month = month;
	}



	public String getYear() {
		return year;
	}



	public void setYear(String year) {
		this.year = year;
	}



	public String getLastDay() {
		return lastDay;
	}



	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}



	public String getPurchaseMonth() {
		return purchaseMonth;
	}



	public void setPurchaseMonth(String purchaseMonth) {
		this.purchaseMonth = purchaseMonth;
	}



	public String getPurchaseYear() {
		return purchaseYear;
	}



	public void setPurchaseYear(String purchaseYear) {
		this.purchaseYear = purchaseYear;
	}



	public String getByBankMonth() {
		return byBankMonth;
	}



	public void setByBankMonth(String byBankMonth) {
		this.byBankMonth = byBankMonth;
	}



	public String getByBankyear() {
		return byBankyear;
	}



	public void setByBankyear(String byBankyear) {
		this.byBankyear = byBankyear;
	}



	public String getTransactionDate() {
		return transactionDate;
	}



	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}



	public double getPurchase() {
		return purchase;
	}



	public void setPurchase(double purchase) {
		this.purchase = purchase;
	}



	public double getNetPayable() {
		return netPayable;
	}



	public void setNetPayable(double netPayable) {
		this.netPayable = netPayable;
	}



	public double getIncomeTax() {
		return incomeTax;
	}



	public void setIncomeTax(double incomeTax) {
		this.incomeTax = incomeTax;
	}



	public double getVatRebate() {
		return vatRebate;
	}



	public void setVatRebate(double vatRebate) {
		this.vatRebate = vatRebate;
	}



	public double getRatio() {
		return ratio;
	}



	public void setRatio(double ratio) {
		this.ratio = ratio;
	}



	public double getBill() {
		return bill;
	}



	public void setBill(double bill) {
		this.bill = bill;
	}



	public double getSystemGain() {
		return systemGain;
	}



	public void setSystemGain(double systemGain) {
		this.systemGain = systemGain;
	}



	public double getGtcl() {
		return gtcl;
	}



	public void setGtcl(double gtcl) {
		this.gtcl = gtcl;
	}



	public double getaValue() {
		return aValue;
	}



	public void setaValue(double aValue) {
		this.aValue = aValue;
	}



	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

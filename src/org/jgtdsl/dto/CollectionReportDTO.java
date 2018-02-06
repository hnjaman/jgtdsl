package org.jgtdsl.dto;

public class CollectionReportDTO {
private String area_name;
private String area_id;

private String category_name;

private String category_id;
private String category_type;
private Double opening_balance; 
private Double adjustment;
private Double curr_sales;
private Double curr_surcharge;		//
private Double account_receivable;
private Double gas_bill;			//
private Double meter_rent;
private Double coll_surcharge;
private Double income_tax;
private Double vat_rebate;
private Double hhv_nhv;
private Double total_collection;
private Double previous_due;
private Double avg_monthly_sales;
private Double avg_due;

/* all category type wise collection report
 * 
 * sujon
 * */
private Double fees;
private Double security;





public Double getFees() {
	return fees;
}
public void setFees(Double fees) {
	this.fees = fees;
}
public Double getSecurity() {
	return security;
}
public void setSecurity(Double security) {
	this.security = security;
}
public String getArea_name() {
	return area_name;
}
public void setArea_name(String area_name) {
	this.area_name = area_name;
}
public String getArea_id() {
	return area_id;
}
public void setArea_id(String area_id) {
	this.area_id = area_id;
}
public String getCategory_name() {
	return category_name;
}
public void setCategory_name(String category_name) {
	this.category_name = category_name;
}
public String getCategory_id() {
	return category_id;
}
public void setCategory_id(String category_id) {
	this.category_id = category_id;
}
public String getCategory_type() {
	return category_type;
}
public void setCategory_type(String category_type) {
	this.category_type = category_type;
}
public Double getOpening_balance() {
	return opening_balance;
}
public void setOpening_balance(Double opening_balance) {
	this.opening_balance = opening_balance;
}
public Double getAdjustment() {
	return adjustment;
}
public void setAdjustment(Double adjustment) {
	this.adjustment = adjustment;
}
public Double getCurr_sales() {
	return curr_sales;
}
public void setCurr_sales(Double curr_sales) {
	this.curr_sales = curr_sales;
}
public Double getCurr_surcharge() {
	return curr_surcharge;
}
public void setCurr_surcharge(Double curr_surcharge) {
	this.curr_surcharge = curr_surcharge;
}
public Double getAccount_receivable() {
	return account_receivable;
}
public void setAccount_receivable(Double account_receivable) {
	this.account_receivable = account_receivable;
}
public Double getGas_bill() {
	return gas_bill;
}
public void setGas_bill(Double gas_bill) {
	this.gas_bill = gas_bill;
}
public Double getMeter_rent() {
	return meter_rent;
}
public void setMeter_rent(Double meter_rent) {
	this.meter_rent = meter_rent;
}
public Double getColl_surcharge() {
	return coll_surcharge;
}
public void setColl_surcharge(Double coll_surcharge) {
	this.coll_surcharge = coll_surcharge;
}
public Double getIncome_tax() {
	return income_tax;
}
public void setIncome_tax(Double income_tax) {
	this.income_tax = income_tax;
}
public Double getVat_rebate() {
	return vat_rebate;
}
public void setVat_rebate(Double vat_rebate) {
	this.vat_rebate = vat_rebate;
}
public Double getTotal_collection() {
	return total_collection;
}
public void setTotal_collection(Double total_collection) {
	this.total_collection = total_collection;
}
public Double getPrevious_due() {
	return previous_due;
}
public void setPrevious_due(Double previous_due) {
	this.previous_due = previous_due;
}
public Double getAvg_monthly_sales() {
	return avg_monthly_sales;
}
public void setAvg_monthly_sales(Double avg_monthly_sales) {
	this.avg_monthly_sales = avg_monthly_sales;
}
public Double getAvg_due() {
	return avg_due;
}
public void setAvg_due(Double avg_due) {
	this.avg_due = avg_due;
}
public Double getHhv_nhv() {
	return hhv_nhv;
}
public void setHhv_nhv(Double hhv_nhv) {
	this.hhv_nhv = hhv_nhv;
}



}

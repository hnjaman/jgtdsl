package org.jgtdsl.dto;

import org.jgtdsl.enums.BurnerCategory;
import org.jgtdsl.enums.MeteredStatus;

public class TariffDTO {

	private int tariff_id;
	private String tariff_no;
	private String customer_category_id;
	private String customer_category_name;
	//private String meter_status;
	private MeteredStatus meter_status;
	private String str_meter_status;
	private BurnerCategory burner_category;
	private String str_burner_category;
	private float price;
	private String description;
	private String entryDate;
	private int is_default;

	private String month;
	private String year;
	private float   bgfcl;
	private float   sgfl;
	private float   pb;
	private float   vat;
	private float   sd;
	private float   others;
	private float   pdf;
	private float   bapex;
	private float   wellhead;
	private float   dwellhead;
	private float   trasmission;
	private float   gdfund;
	private float   distribution;
	private float   avalue;
	
	private String effective_from;
	private String effective_to;
	
	
	
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
	public float getBgfcl() {
		return bgfcl;
	}
	public void setBgfcl(float bgfcl) {
		this.bgfcl = bgfcl;
	}
	public float getSgfl() {
		return sgfl;
	}
	public void setSgfl(float sgfl) {
		this.sgfl = sgfl;
	}
	public float getPb() {
		return pb;
	}
	public void setPb(float pb) {
		this.pb = pb;
	}
	public float getSd() {
		return sd;
	}
	public void setSd(float sd) {
		this.sd = sd;
	}
	public float getOthers() {
		return others;
	}
	public void setOthers(float others) {
		this.others = others;
	}
	public float getPdf() {
		return pdf;
	}
	public void setPdf(float pdf) {
		this.pdf = pdf;
	}
	public float getBapex() {
		return bapex;
	}
	public void setBapex(float bapex) {
		this.bapex = bapex;
	}
	public float getWellhead() {
		return wellhead;
	}
	public void setWellhead(float wellhead) {
		this.wellhead = wellhead;
	}
	public float getDwellhead() {
		return dwellhead;
	}
	public void setDwellhead(float dwellhead) {
		this.dwellhead = dwellhead;
	}
	public float getTrasmission() {
		return trasmission;
	}
	public void setTrasmission(float trasmission) {
		this.trasmission = trasmission;
	}
	public float getGdfund() {
		return gdfund;
	}
	public void setGdfund(float gdfund) {
		this.gdfund = gdfund;
	}
	public float getDistribution() {
		return distribution;
	}
	public void setDistribution(float distribution) {
		this.distribution = distribution;
	}
	public float getAvalue() {
		return avalue;
	}
	public void setAvalue(float avalue) {
		this.avalue = avalue;
	}
	public String getEffective_from() {
		return effective_from;
	}
	public void setEffective_from(String effective_from) {
		this.effective_from = effective_from;
	}
	public String getEffective_to() {
		return effective_to;
	}
	public void setEffective_to(String effective_to) {
		this.effective_to = effective_to;
	}
	public float getVat() {
		return vat;
	}
	public void setVat(float vat) {
		this.vat = vat;
	}
	public int getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(int tariffId) {
		tariff_id = tariffId;
	}
	public String getTariff_no() {
		return tariff_no;
	}
	public void setTariff_no(String tariffNo) {
		tariff_no = tariffNo;
	}
	public String getCustomer_category_id() {
		return customer_category_id;
	}
	public void setCustomer_category_id(String customerCategoryId) {
		customer_category_id = customerCategoryId;
	}
	public String getCustomer_category_name() {
		return customer_category_name;
	}
	public void setCustomer_category_name(String customerCategoryName) {
		customer_category_name = customerCategoryName;
	}
	
	public MeteredStatus getMeter_status() {
		return meter_status;
	}
	public void setMeter_status(MeteredStatus meterStatus) {
		meter_status = meterStatus;
	}
	public String getStr_meter_status() {
		return str_meter_status;
	}
	public void setStr_meter_status(String strMeterStatus) {
		str_meter_status = strMeterStatus;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public int getIs_default() {
		return is_default;
	}
	public void setIs_default(int isDefault) {
		is_default = isDefault;
	}	
	public BurnerCategory getBurner_category() {
		return burner_category;
	}
	public void setBurner_category(BurnerCategory burnerCategory) {
		burner_category = burnerCategory;
	}
	public String getStr_burner_category() {
		return str_burner_category;
	}
	public void setStr_burner_category(String strBurnerCategory) {
		str_burner_category = strBurnerCategory;
	}
	public String toString() {
        return "{\"tariff_id\":\"" + tariff_id + "\", \"tariff_no\":\"" + tariff_no + "\", \"customer_category_id\":\"" + customer_category_id + "\", \"customer_category_name\":\"" + customer_category_name + "\", \"meter_status\":\"" + meter_status.getId() + "\", \"str_meter_status\":\"" + meter_status.getId() + "\", \"burner_category\":\"" + (burner_category==null?"":burner_category.getId())+"\",\"str_burner_category\":\"" +(burner_category==null?"":burner_category.getId())+ "\", \"price\":\"" + price + "\",\"pb\":\"" + pb + "\",\"sd\":\"" + sd + "\",\"vat\":\"" + vat + "\",\"pdf\":\"" + pdf + "\",\"bapex\":\"" + bapex + "\",\"wellhead\":\"" + wellhead + "\",\"dwellhead\":\"" + dwellhead + "\",\"trasmission\":\"" + trasmission + "\",\"gdfund\":\"" + gdfund + "\",\"distribution\":\"" + distribution + "\",\"avalue\":\"" + avalue + "\",\"effective_from\":\"" + effective_from + "\",\"effective_to\":\"" + effective_to + "\",  \"description\":\"" + (description==null?"":description.replaceAll("[\\r\\n]+", "<br/>")) + "\",\"entryDate\":\"" + entryDate + "\", \"is_default\":\"" + is_default + "\"}";
    }

	
}

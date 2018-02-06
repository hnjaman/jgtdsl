package org.jgtdsl.dto;

import com.google.gson.Gson;

public class GasPurchaseDTO {
private String month;
private String year;
private Double total_bgfcl;
private Double total_sgfl;
private Double total_ioc;
private Double total_gtcl;
private Double bgfcl_ratio;
private Double sgfl_ratio;
private Double ioc_ratio;
private Double bgfcl_power_gvt;
private Double bgfcl_power_pvt;
private Double bgfcl_captive_gvt;
private Double bgfcl_captive_pvt;
private Double bgfcl_cng_gvt;
private Double bgfcl_cng_pvt;
private Double bgfcl_industry_gvt;
private Double bgfcl_industry_pvt;
private Double bgfcl_comm_gvt;
private Double bgfcl_comm_pvt;
private Double bgfcl_dom_meter_gvt;
private Double bgfcl_dom_meter_pvt;
private Double bgfcl_dom_nmeter_gvt;
private Double bgfcl_dom_nmeter_pvt;
private Double bgfcl_fertilizer_gvt;
private Double bgfcl_fertilizer_pvt;
private Double bgfcl_tea_gvt;
private Double bgfcl_tea_pvt;

private Double sgfl_power_gvt;
private Double sgfl_power_pvt;
private Double sgfl_captive_gvt;
private Double sgfl_captive_pvt;
private Double sgfl_cng_gvt;
private Double sgfl_cng_pvt;
private Double sgfl_industry_gvt;
private Double sgfl_industry_pvt;
private Double sgfl_comm_gvt;
private Double sgfl_comm_pvt;
private Double sgfl_dom_meter_gvt;
private Double sgfl_dom_meter_pvt;
private Double sgfl_dom_nmeter_gvt;
private Double sgfl_dom_nmeter_pvt;
private Double sgfl_fertilizer_gvt;
private Double sgfl_fertilizer_pvt;
private Double sgfl_tea_gvt;
private Double sgfl_tea_pvt;

private Double ioc_power_gvt;
private Double ioc_power_pvt;
private Double ioc_captive_gvt;
private Double ioc_captive_pvt;
private Double ioc_cng_gvt;
private Double ioc_cng_pvt;
private Double ioc_industry_gvt;
private Double ioc_industry_pvt;
private Double ioc_comm_gvt;
private Double ioc_comm_pvt;
private Double ioc_dom_meter_gvt;
private Double ioc_dom_meter_pvt;
private Double ioc_dom_nmeter_gvt;
private Double ioc_dom_nmeter_pvt;
private Double ioc_fertilizer_gvt;
private Double ioc_fertilizer_pvt;
private Double ioc_tea_gvt;
private Double ioc_tea_pvt;
////////////////////////////////////
///calculation of various margin////
////////////////////////////////////
private double bgfcl_power;
private double bgfcl_captive;
private double bgfcl_industrial;
private double bgfcl_commercial;
private double bgfcl_Domestic;
private double bgfcl_cng;
private double bgfcl_tea;
private double bgfcl_fertilizer;
private double sgfl_power;
private double sgfl_captive;
private double sgfl_industrial;
private double sgfl_commercial;
private double sgfl_Domestic;
private double sgfl_cng;
private double sgfl_tea;
private double sgfl_fertilizer;
private double ioc_power;
private double ioc_captive;
private double ioc_industrial;
private double ioc_commercial;
private double ioc_Domestic;
private double ioc_cng;
private double ioc_tea;
private double ioc_fertilizer;
private String purchase_from;
/////////////////////////////////
//////Calculation Total//////////
////////////////////////////////
private double total_power;
private double total_captive;
private double total_industrial;
private double total_commercial;
private double total_domestic;
private double total_cng;
private double total_tea;
private double total_fertilizer;






public Double getBgfcl_ratio() {
	return bgfcl_ratio;
}




public void setBgfcl_ratio(Double bgfcl_ratio) {
	this.bgfcl_ratio = bgfcl_ratio;
}




public Double getSgfl_ratio() {
	return sgfl_ratio;
}




public void setSgfl_ratio(Double sgfl_ratio) {
	this.sgfl_ratio = sgfl_ratio;
}




public Double getIoc_ratio() {
	return ioc_ratio;
}




public void setIoc_ratio(Double ioc_ratio) {
	this.ioc_ratio = ioc_ratio;
}




public double getTotal_power() {
	return total_power;
}




public void setTotal_power(double total_power) {
	this.total_power = total_power;
}




public double getTotal_captive() {
	return total_captive;
}




public void setTotal_captive(double total_captive) {
	this.total_captive = total_captive;
}




public double getTotal_industrial() {
	return total_industrial;
}




public void setTotal_industrial(double total_industrial) {
	this.total_industrial = total_industrial;
}




public double getTotal_commercial() {
	return total_commercial;
}




public void setTotal_commercial(double total_commercial) {
	this.total_commercial = total_commercial;
}




public double getTotal_domestic() {
	return total_domestic;
}




public void setTotal_domestic(double total_domestic) {
	this.total_domestic = total_domestic;
}




public double getTotal_cng() {
	return total_cng;
}




public void setTotal_cng(double total_cng) {
	this.total_cng = total_cng;
}




public double getTotal_tea() {
	return total_tea;
}




public void setTotal_tea(double total_tea) {
	this.total_tea = total_tea;
}




public double getTotal_fertilizer() {
	return total_fertilizer;
}




public void setTotal_fertilizer(double total_fertilizer) {
	this.total_fertilizer = total_fertilizer;
}




public double getBgfcl_power() {
	return bgfcl_power;
}




public void setBgfcl_power(double bgfcl_power) {
	this.bgfcl_power = bgfcl_power;
}




public double getBgfcl_captive() {
	return bgfcl_captive;
}




public void setBgfcl_captive(double bgfcl_captive) {
	this.bgfcl_captive = bgfcl_captive;
}




public double getBgfcl_industrial() {
	return bgfcl_industrial;
}




public void setBgfcl_industrial(double bgfcl_industrial) {
	this.bgfcl_industrial = bgfcl_industrial;
}




public double getBgfcl_commercial() {
	return bgfcl_commercial;
}




public void setBgfcl_commercial(double bgfcl_commercial) {
	this.bgfcl_commercial = bgfcl_commercial;
}




public double getBgfcl_Domestic() {
	return bgfcl_Domestic;
}




public void setBgfcl_Domestic(double bgfcl_Domestic) {
	this.bgfcl_Domestic = bgfcl_Domestic;
}




public double getBgfcl_cng() {
	return bgfcl_cng;
}




public void setBgfcl_cng(double bgfcl_cng) {
	this.bgfcl_cng = bgfcl_cng;
}




public double getBgfcl_tea() {
	return bgfcl_tea;
}




public void setBgfcl_tea(double bgfcl_tea) {
	this.bgfcl_tea = bgfcl_tea;
}




public double getBgfcl_fertilizer() {
	return bgfcl_fertilizer;
}




public void setBgfcl_fertilizer(double bgfcl_fertilizer) {
	this.bgfcl_fertilizer = bgfcl_fertilizer;
}




public double getSgfl_power() {
	return sgfl_power;
}




public void setSgfl_power(double sgfl_power) {
	this.sgfl_power = sgfl_power;
}




public double getSgfl_captive() {
	return sgfl_captive;
}




public void setSgfl_captive(double sgfl_captive) {
	this.sgfl_captive = sgfl_captive;
}




public double getSgfl_industrial() {
	return sgfl_industrial;
}




public void setSgfl_industrial(double sgfl_industrial) {
	this.sgfl_industrial = sgfl_industrial;
}




public double getSgfl_commercial() {
	return sgfl_commercial;
}




public void setSgfl_commercial(double sgfl_commercial) {
	this.sgfl_commercial = sgfl_commercial;
}




public double getSgfl_Domestic() {
	return sgfl_Domestic;
}




public void setSgfl_Domestic(double sgfl_Domestic) {
	this.sgfl_Domestic = sgfl_Domestic;
}




public double getSgfl_cng() {
	return sgfl_cng;
}




public void setSgfl_cng(double sgfl_cng) {
	this.sgfl_cng = sgfl_cng;
}




public double getSgfl_tea() {
	return sgfl_tea;
}




public void setSgfl_tea(double sgfl_tea) {
	this.sgfl_tea = sgfl_tea;
}




public double getSgfl_fertilizer() {
	return sgfl_fertilizer;
}




public void setSgfl_fertilizer(double sgfl_fertilizer) {
	this.sgfl_fertilizer = sgfl_fertilizer;
}




public double getIoc_power() {
	return ioc_power;
}




public void setIoc_power(double ioc_power) {
	this.ioc_power = ioc_power;
}




public double getIoc_captive() {
	return ioc_captive;
}




public void setIoc_captive(double ioc_captive) {
	this.ioc_captive = ioc_captive;
}




public double getIoc_industrial() {
	return ioc_industrial;
}




public void setIoc_industrial(double ioc_industrial) {
	this.ioc_industrial = ioc_industrial;
}




public double getIoc_commercial() {
	return ioc_commercial;
}




public void setIoc_commercial(double ioc_commercial) {
	this.ioc_commercial = ioc_commercial;
}




public double getIoc_Domestic() {
	return ioc_Domestic;
}




public void setIoc_Domestic(double ioc_Domestic) {
	this.ioc_Domestic = ioc_Domestic;
}




public double getIoc_cng() {
	return ioc_cng;
}




public void setIoc_cng(double ioc_cng) {
	this.ioc_cng = ioc_cng;
}




public double getIoc_tea() {
	return ioc_tea;
}




public void setIoc_tea(double ioc_tea) {
	this.ioc_tea = ioc_tea;
}




public double getIoc_fertilizer() {
	return ioc_fertilizer;
}




public void setIoc_fertilizer(double ioc_fertilizer) {
	this.ioc_fertilizer = ioc_fertilizer;
}




public String getPurchase_from() {
	return purchase_from;
}




public void setPurchase_from(String purchase_from) {
	this.purchase_from = purchase_from;
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




public Double getTotal_bgfcl() {
	return total_bgfcl;
}




public void setTotal_bgfcl(Double total_bgfcl) {
	this.total_bgfcl = total_bgfcl;
}




public Double getTotal_sgfl() {
	return total_sgfl;
}




public void setTotal_sgfl(Double total_sgfl) {
	this.total_sgfl = total_sgfl;
}




public Double getTotal_ioc() {
	return total_ioc;
}




public void setTotal_ioc(Double total_ioc) {
	this.total_ioc = total_ioc;
}




public Double getTotal_gtcl() {
	return total_gtcl;
}




public void setTotal_gtcl(Double total_gtcl) {
	this.total_gtcl = total_gtcl;
}




public Double getBgfcl_power_gvt() {
	return bgfcl_power_gvt;
}




public void setBgfcl_power_gvt(Double bgfcl_power_gvt) {
	this.bgfcl_power_gvt = bgfcl_power_gvt;
}




public Double getBgfcl_power_pvt() {
	return bgfcl_power_pvt;
}




public void setBgfcl_power_pvt(Double bgfcl_power_pvt) {
	this.bgfcl_power_pvt = bgfcl_power_pvt;
}




public Double getBgfcl_captive_gvt() {
	return bgfcl_captive_gvt;
}




public void setBgfcl_captive_gvt(Double bgfcl_captive_gvt) {
	this.bgfcl_captive_gvt = bgfcl_captive_gvt;
}




public Double getBgfcl_captive_pvt() {
	return bgfcl_captive_pvt;
}




public void setBgfcl_captive_pvt(Double bgfcl_captive_pvt) {
	this.bgfcl_captive_pvt = bgfcl_captive_pvt;
}




public Double getBgfcl_cng_gvt() {
	return bgfcl_cng_gvt;
}




public void setBgfcl_cng_gvt(Double bgfcl_cng_gvt) {
	this.bgfcl_cng_gvt = bgfcl_cng_gvt;
}




public Double getBgfcl_cng_pvt() {
	return bgfcl_cng_pvt;
}




public void setBgfcl_cng_pvt(Double bgfcl_cng_pvt) {
	this.bgfcl_cng_pvt = bgfcl_cng_pvt;
}




public Double getBgfcl_industry_gvt() {
	return bgfcl_industry_gvt;
}




public void setBgfcl_industry_gvt(Double bgfcl_industry_gvt) {
	this.bgfcl_industry_gvt = bgfcl_industry_gvt;
}




public Double getBgfcl_industry_pvt() {
	return bgfcl_industry_pvt;
}




public void setBgfcl_industry_pvt(Double bgfcl_industry_pvt) {
	this.bgfcl_industry_pvt = bgfcl_industry_pvt;
}




public Double getBgfcl_comm_gvt() {
	return bgfcl_comm_gvt;
}




public void setBgfcl_comm_gvt(Double bgfcl_comm_gvt) {
	this.bgfcl_comm_gvt = bgfcl_comm_gvt;
}




public Double getBgfcl_comm_pvt() {
	return bgfcl_comm_pvt;
}




public void setBgfcl_comm_pvt(Double bgfcl_comm_pvt) {
	this.bgfcl_comm_pvt = bgfcl_comm_pvt;
}




public Double getBgfcl_dom_meter_gvt() {
	return bgfcl_dom_meter_gvt;
}




public void setBgfcl_dom_meter_gvt(Double bgfcl_dom_meter_gvt) {
	this.bgfcl_dom_meter_gvt = bgfcl_dom_meter_gvt;
}




public Double getBgfcl_dom_meter_pvt() {
	return bgfcl_dom_meter_pvt;
}




public void setBgfcl_dom_meter_pvt(Double bgfcl_dom_meter_pvt) {
	this.bgfcl_dom_meter_pvt = bgfcl_dom_meter_pvt;
}




public Double getBgfcl_dom_nmeter_gvt() {
	return bgfcl_dom_nmeter_gvt;
}




public void setBgfcl_dom_nmeter_gvt(Double bgfcl_dom_nmeter_gvt) {
	this.bgfcl_dom_nmeter_gvt = bgfcl_dom_nmeter_gvt;
}




public Double getBgfcl_dom_nmeter_pvt() {
	return bgfcl_dom_nmeter_pvt;
}




public void setBgfcl_dom_nmeter_pvt(Double bgfcl_dom_nmeter_pvt) {
	this.bgfcl_dom_nmeter_pvt = bgfcl_dom_nmeter_pvt;
}




public Double getBgfcl_fertilizer_gvt() {
	return bgfcl_fertilizer_gvt;
}




public void setBgfcl_fertilizer_gvt(Double bgfcl_fertilizer_gvt) {
	this.bgfcl_fertilizer_gvt = bgfcl_fertilizer_gvt;
}




public Double getBgfcl_fertilizer_pvt() {
	return bgfcl_fertilizer_pvt;
}




public void setBgfcl_fertilizer_pvt(Double bgfcl_fertilizer_pvt) {
	this.bgfcl_fertilizer_pvt = bgfcl_fertilizer_pvt;
}




public Double getBgfcl_tea_gvt() {
	return bgfcl_tea_gvt;
}




public void setBgfcl_tea_gvt(Double bgfcl_tea_gvt) {
	this.bgfcl_tea_gvt = bgfcl_tea_gvt;
}




public Double getBgfcl_tea_pvt() {
	return bgfcl_tea_pvt;
}




public void setBgfcl_tea_pvt(Double bgfcl_tea_pvt) {
	this.bgfcl_tea_pvt = bgfcl_tea_pvt;
}




public Double getSgfl_power_gvt() {
	return sgfl_power_gvt;
}




public void setSgfl_power_gvt(Double sgfl_power_gvt) {
	this.sgfl_power_gvt = sgfl_power_gvt;
}




public Double getSgfl_power_pvt() {
	return sgfl_power_pvt;
}




public void setSgfl_power_pvt(Double sgfl_power_pvt) {
	this.sgfl_power_pvt = sgfl_power_pvt;
}




public Double getSgfl_captive_gvt() {
	return sgfl_captive_gvt;
}




public void setSgfl_captive_gvt(Double sgfl_captive_gvt) {
	this.sgfl_captive_gvt = sgfl_captive_gvt;
}




public Double getSgfl_captive_pvt() {
	return sgfl_captive_pvt;
}




public void setSgfl_captive_pvt(Double sgfl_captive_pvt) {
	this.sgfl_captive_pvt = sgfl_captive_pvt;
}




public Double getSgfl_cng_gvt() {
	return sgfl_cng_gvt;
}




public void setSgfl_cng_gvt(Double sgfl_cng_gvt) {
	this.sgfl_cng_gvt = sgfl_cng_gvt;
}




public Double getSgfl_cng_pvt() {
	return sgfl_cng_pvt;
}




public void setSgfl_cng_pvt(Double sgfl_cng_pvt) {
	this.sgfl_cng_pvt = sgfl_cng_pvt;
}




public Double getSgfl_industry_gvt() {
	return sgfl_industry_gvt;
}




public void setSgfl_industry_gvt(Double sgfl_industry_gvt) {
	this.sgfl_industry_gvt = sgfl_industry_gvt;
}




public Double getSgfl_industry_pvt() {
	return sgfl_industry_pvt;
}




public void setSgfl_industry_pvt(Double sgfl_industry_pvt) {
	this.sgfl_industry_pvt = sgfl_industry_pvt;
}




public Double getSgfl_comm_gvt() {
	return sgfl_comm_gvt;
}




public void setSgfl_comm_gvt(Double sgfl_comm_gvt) {
	this.sgfl_comm_gvt = sgfl_comm_gvt;
}




public Double getSgfl_comm_pvt() {
	return sgfl_comm_pvt;
}




public void setSgfl_comm_pvt(Double sgfl_comm_pvt) {
	this.sgfl_comm_pvt = sgfl_comm_pvt;
}




public Double getSgfl_dom_meter_gvt() {
	return sgfl_dom_meter_gvt;
}




public void setSgfl_dom_meter_gvt(Double sgfl_dom_meter_gvt) {
	this.sgfl_dom_meter_gvt = sgfl_dom_meter_gvt;
}




public Double getSgfl_dom_meter_pvt() {
	return sgfl_dom_meter_pvt;
}




public void setSgfl_dom_meter_pvt(Double sgfl_dom_meter_pvt) {
	this.sgfl_dom_meter_pvt = sgfl_dom_meter_pvt;
}




public Double getSgfl_dom_nmeter_gvt() {
	return sgfl_dom_nmeter_gvt;
}




public void setSgfl_dom_nmeter_gvt(Double sgfl_dom_nmeter_gvt) {
	this.sgfl_dom_nmeter_gvt = sgfl_dom_nmeter_gvt;
}




public Double getSgfl_dom_nmeter_pvt() {
	return sgfl_dom_nmeter_pvt;
}




public void setSgfl_dom_nmeter_pvt(Double sgfl_dom_nmeter_pvt) {
	this.sgfl_dom_nmeter_pvt = sgfl_dom_nmeter_pvt;
}




public Double getSgfl_fertilizer_gvt() {
	return sgfl_fertilizer_gvt;
}




public void setSgfl_fertilizer_gvt(Double sgfl_fertilizer_gvt) {
	this.sgfl_fertilizer_gvt = sgfl_fertilizer_gvt;
}




public Double getSgfl_fertilizer_pvt() {
	return sgfl_fertilizer_pvt;
}




public void setSgfl_fertilizer_pvt(Double sgfl_fertilizer_pvt) {
	this.sgfl_fertilizer_pvt = sgfl_fertilizer_pvt;
}




public Double getSgfl_tea_gvt() {
	return sgfl_tea_gvt;
}




public void setSgfl_tea_gvt(Double sgfl_tea_gvt) {
	this.sgfl_tea_gvt = sgfl_tea_gvt;
}




public Double getSgfl_tea_pvt() {
	return sgfl_tea_pvt;
}




public void setSgfl_tea_pvt(Double sgfl_tea_pvt) {
	this.sgfl_tea_pvt = sgfl_tea_pvt;
}




public Double getIoc_power_gvt() {
	return ioc_power_gvt;
}




public void setIoc_power_gvt(Double ioc_power_gvt) {
	this.ioc_power_gvt = ioc_power_gvt;
}




public Double getIoc_power_pvt() {
	return ioc_power_pvt;
}




public void setIoc_power_pvt(Double ioc_power_pvt) {
	this.ioc_power_pvt = ioc_power_pvt;
}




public Double getIoc_captive_gvt() {
	return ioc_captive_gvt;
}




public void setIoc_captive_gvt(Double ioc_captive_gvt) {
	this.ioc_captive_gvt = ioc_captive_gvt;
}




public Double getIoc_captive_pvt() {
	return ioc_captive_pvt;
}




public void setIoc_captive_pvt(Double ioc_captive_pvt) {
	this.ioc_captive_pvt = ioc_captive_pvt;
}




public Double getIoc_cng_gvt() {
	return ioc_cng_gvt;
}




public void setIoc_cng_gvt(Double ioc_cng_gvt) {
	this.ioc_cng_gvt = ioc_cng_gvt;
}




public Double getIoc_cng_pvt() {
	return ioc_cng_pvt;
}




public void setIoc_cng_pvt(Double ioc_cng_pvt) {
	this.ioc_cng_pvt = ioc_cng_pvt;
}




public Double getIoc_industry_gvt() {
	return ioc_industry_gvt;
}




public void setIoc_industry_gvt(Double ioc_industry_gvt) {
	this.ioc_industry_gvt = ioc_industry_gvt;
}




public Double getIoc_industry_pvt() {
	return ioc_industry_pvt;
}




public void setIoc_industry_pvt(Double ioc_industry_pvt) {
	this.ioc_industry_pvt = ioc_industry_pvt;
}




public Double getIoc_comm_gvt() {
	return ioc_comm_gvt;
}




public void setIoc_comm_gvt(Double ioc_comm_gvt) {
	this.ioc_comm_gvt = ioc_comm_gvt;
}




public Double getIoc_comm_pvt() {
	return ioc_comm_pvt;
}




public void setIoc_comm_pvt(Double ioc_comm_pvt) {
	this.ioc_comm_pvt = ioc_comm_pvt;
}




public Double getIoc_dom_meter_gvt() {
	return ioc_dom_meter_gvt;
}




public void setIoc_dom_meter_gvt(Double ioc_dom_meter_gvt) {
	this.ioc_dom_meter_gvt = ioc_dom_meter_gvt;
}




public Double getIoc_dom_meter_pvt() {
	return ioc_dom_meter_pvt;
}




public void setIoc_dom_meter_pvt(Double ioc_dom_meter_pvt) {
	this.ioc_dom_meter_pvt = ioc_dom_meter_pvt;
}




public Double getIoc_dom_nmeter_gvt() {
	return ioc_dom_nmeter_gvt;
}




public void setIoc_dom_nmeter_gvt(Double ioc_dom_nmeter_gvt) {
	this.ioc_dom_nmeter_gvt = ioc_dom_nmeter_gvt;
}




public Double getIoc_dom_nmeter_pvt() {
	return ioc_dom_nmeter_pvt;
}




public void setIoc_dom_nmeter_pvt(Double ioc_dom_nmeter_pvt) {
	this.ioc_dom_nmeter_pvt = ioc_dom_nmeter_pvt;
}




public Double getIoc_fertilizer_gvt() {
	return ioc_fertilizer_gvt;
}




public void setIoc_fertilizer_gvt(Double ioc_fertilizer_gvt) {
	this.ioc_fertilizer_gvt = ioc_fertilizer_gvt;
}




public Double getIoc_fertilizer_pvt() {
	return ioc_fertilizer_pvt;
}




public void setIoc_fertilizer_pvt(Double ioc_fertilizer_pvt) {
	this.ioc_fertilizer_pvt = ioc_fertilizer_pvt;
}




public Double getIoc_tea_gvt() {
	return ioc_tea_gvt;
}




public void setIoc_tea_gvt(Double ioc_tea_gvt) {
	this.ioc_tea_gvt = ioc_tea_gvt;
}




public Double getIoc_tea_pvt() {
	return ioc_tea_pvt;
}




public void setIoc_tea_pvt(Double ioc_tea_pvt) {
	this.ioc_tea_pvt = ioc_tea_pvt;
}




public String toString() {         
    Gson gson = new Gson();
	return gson.toJson(this);
}


}

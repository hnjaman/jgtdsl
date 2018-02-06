package org.jgtdsl.actions;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.enums.BurnerCategory;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.models.TariffService;
import org.jgtdsl.utils.AC;

public class Tariff extends BaseAction implements SessionAware{

	private static final long serialVersionUID = -1812990550368904350L;
	private Map<String,Object> session;
	private String target_date;
	private String customer_id;
	private String meter_status;
	private TariffDTO tariffdto=new TariffDTO();
	private String postdata;
	private int tariff_id;
	private String tariff_no;
	private String customer_category_id;
	private String customer_category_name;
	//private String meter_status;
	private MeteredStatus meter_status1;
	private String str_meter_status;
	private BurnerCategory burner_category;
	private String str_burner_category;
	private float price;
	private String description;
	private String entryDate;
	private int is_default;
	private float pb;
	private float vat;
	private float sd;
	private float others;
	private float pdf;
	private float bapex;
	private float wellhead;
	private float dwellhead;
	private float trasmission;
	private float gdfund;
	private float distribution;
	private String effective_from;
	private String effective_to;
	private String data;
	

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getPostdata() {
		return postdata;
	}
	public void setPostdata(String postdata) {
		this.postdata = postdata;
	}
	public String getGasPrice()
	{
		TariffService ts=new TariffService();
		String resp="";
		if(meter_status.equalsIgnoreCase("Metered"))
			resp=ts.getMeteredGasPrice(customer_id, target_date);
		else if(meter_status.equalsIgnoreCase("NonMetered"))
			resp=ts.getNonMeterGasPrice(customer_id, target_date);
		
		try{
	         setJsonResponse(resp);
		   }
		catch(Exception ex)
		   {
			 setJsonResponse(AC.STATUS_ERROR,ex.getMessage());		
		   }
	        
		return null;
		
	}
	public String editRariff()
	{
		TariffService ts=new TariffService();
		//System.out.println(data);
		//System.out.println(tariffdto.getPrice());
	
	
	
	        
		return null;
		
	}
	
	
	public String getTarget_date() {
		return target_date;
	}
	public void setTarget_date(String targetDate) {
		target_date = targetDate;
	}
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public Map getSession() {
		return session;
	}
	
	public String getMeter_status() {
		return meter_status;
	}

	public void setMeter_status(String meterStatus) {
		meter_status = meterStatus;
	}

	public void setSession(Map session) {
		this.session = session;
	}
	public TariffDTO getTariffdto() {
		return tariffdto;
	}
	public void setTariffdto(TariffDTO tariffdto) {
		this.tariffdto = tariffdto;
	}
	public String getCustomer_category_id() {
		return customer_category_id;
	}
	public void setCustomer_category_id(String customer_category_id) {
		this.customer_category_id = customer_category_id;
	}
	public String getCustomer_category_name() {
		return customer_category_name;
	}
	public void setCustomer_category_name(String customer_category_name) {
		this.customer_category_name = customer_category_name;
	}
	public MeteredStatus getMeter_status1() {
		return meter_status1;
	}
	public void setMeter_status1(MeteredStatus meter_status1) {
		this.meter_status1 = meter_status1;
	}
	public BurnerCategory getBurner_category() {
		return burner_category;
	}
	public void setBurner_category(BurnerCategory burner_category) {
		this.burner_category = burner_category;
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
	public void setIs_default(int is_default) {
		this.is_default = is_default;
	}
	public float getPb() {
		return pb;
	}
	public void setPb(float pb) {
		this.pb = pb;
	}
	public float getOthers() {
		return others;
	}
	public void setOthers(float others) {
		this.others = others;
	}
	public float getBapex() {
		return bapex;
	}
	public void setBapex(float bapex) {
		this.bapex = bapex;
	}
	public float getDwellhead() {
		return dwellhead;
	}
	public void setDwellhead(float dwellhead) {
		this.dwellhead = dwellhead;
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
	public int getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(int tariff_id) {
		this.tariff_id = tariff_id;
	}
	public String getTariff_no() {
		return tariff_no;
	}
	public void setTariff_no(String tariff_no) {
		this.tariff_no = tariff_no;
	}
	public String getStr_meter_status() {
		return str_meter_status;
	}
	public void setStr_meter_status(String str_meter_status) {
		this.str_meter_status = str_meter_status;
	}
	public String getStr_burner_category() {
		return str_burner_category;
	}
	public void setStr_burner_category(String str_burner_category) {
		this.str_burner_category = str_burner_category;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getVat() {
		return vat;
	}
	public void setVat(float vat) {
		this.vat = vat;
	}
	public float getSd() {
		return sd;
	}
	public void setSd(float sd) {
		this.sd = sd;
	}
	public float getPdf() {
		return pdf;
	}
	public void setPdf(float pdf) {
		this.pdf = pdf;
	}
	public float getWellhead() {
		return wellhead;
	}
	public void setWellhead(float wellhead) {
		this.wellhead = wellhead;
	}
	public float getTrasmission() {
		return trasmission;
	}
	public void setTrasmission(float trasmission) {
		this.trasmission = trasmission;
	}

	
}

package org.jgtdsl.actions;

import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.EVCModelDTO;
import org.jgtdsl.dto.EmployeeDTO;
import org.jgtdsl.dto.MeterGRatingDTO;
import org.jgtdsl.dto.MeterMfgDTO;
import org.jgtdsl.dto.MeterRepairmentDTO;
import org.jgtdsl.dto.MeterTypeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.models.ConnectionService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DisconnectionService;
import org.jgtdsl.models.MeterRepairmentService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Meter extends BaseAction {

	private static final long serialVersionUID = -7297060697093111818L;
	private String customer_id;
	private String meter_id;
	private ArrayList<MeterMfgDTO>  mfgList;
	private ArrayList<MeterTypeDTO> meterTypeList;
	private ArrayList<MeterGRatingDTO> gRatingList;
	private ArrayList<EVCModelDTO> evcModelList;
	
	private MeterRepairmentDTO repair;
	private CustomerMeterDTO meter;

	
	
	public String saveMeterRepairmentInfo()
	{		
		MeterRepairmentService mrService=new MeterRepairmentService();
		repair.setInsert_by(((UserDTO)session.get("user")).getUserId());		
		ResponseDTO response=null;
		if(repair.getPid()==null || repair.getPid().equalsIgnoreCase(""))
			response=mrService.saveMeterRepairmentInfo(repair);
		else
			response=mrService.saveMeterRepairmentInfo(repair);
			
		setJsonResponse(response);		

		return null;
		
	}
	public String getRepairInfo()
	{
		Gson gson = new Gson();
		String json = gson.toJson(MeterService.getRepairInfo(repair.getPid()));
		setJsonResponse(json);
        return null;
        				
	}	
	
	// meterListGrid.js
	
	public String getMeterListAsJson()
	{		
		MeterService ms=new MeterService();
		CustomerService cs=new CustomerService();
		try{
			CustomerMeterDTO meter=new CustomerMeterDTO();
			ArrayList<CustomerMeterDTO> meterList=ms.getCustomerMeterList(customer_id,Utils.EMPTY_STRING,Utils.EMPTY_STRING);
			if(meter==null)
			{
				setJsonResponse(AC.STATUS_ERROR,"No meter available for the selected customer.");		
			}
			else
			{
		        setJsonResponse(meterList.toString());
			}
	   	        
		}
		catch(Exception ex){
			 setJsonResponse(AC.STATUS_ERROR,ex.getMessage());		
		}
        return null;
	     
	}
	
	public String getMeterInfoAsJson()
	{		
		MeterService ms=new MeterService();
		CustomerService cs=new CustomerService();
		try{
			CustomerMeterDTO meter=new CustomerMeterDTO();
			meter=ms.getCustomerMeterList(customer_id,meter_id,"").get(0);
			if(meter==null)
			{
				setJsonResponse(AC.STATUS_ERROR,"Not a Valid Customer Id or Meter No.");		
			}
			else
			{
		        //String resp=meter.toString();
				Gson gson = new Gson();
				JsonElement jsonElement = gson.toJsonTree(meter);
				jsonElement.getAsJsonObject().addProperty("min_load", cs.getCustomerInfo(meter.getCustomer_id()).getConnectionInfo().getMin_load());
				jsonElement.getAsJsonObject().addProperty("max_load", cs.getCustomerInfo(meter.getCustomer_id()).getConnectionInfo().getMax_load());
				jsonElement.getAsJsonObject().addProperty("hhv_nhv", cs.getCustomerInfo(meter.getCustomer_id()).getConnectionInfo().getHhv_nhv());
		        setJsonResponse(gson.toJson(jsonElement));
			}
	   	        
		}
		catch(Exception ex){
			 setJsonResponse(AC.STATUS_ERROR,ex.getMessage());		
		}
        return null;
	     
	}
	
	public String getMeterWithDisconnectInfoAsJson()
	{
		MeterService ms=new MeterService();
		CustomerService cs=new CustomerService();
			try{
				CustomerMeterDTO meter=new CustomerMeterDTO();
				DisconnectDTO latestDisconnectInfo=ConnectionService.getLatestDisconnectInfo(customer_id, meter_id);
				meter=ms.getCustomerMeterList(customer_id,meter_id,"").get(0);
				meter.setLatestDisconnectInfo(latestDisconnectInfo);
				if(meter==null)
				{
					setJsonResponse(AC.STATUS_ERROR,"Not a Valid Customer Id or Meter No.");		
				}
				else
				{
					Gson gson = new Gson();
					JsonElement jsonElement = gson.toJsonTree(meter);
					jsonElement.getAsJsonObject().addProperty("min_load", cs.getCustomerInfo(customer_id).getConnectionInfo().getMin_load());
					jsonElement.getAsJsonObject().addProperty("max_load", cs.getCustomerInfo(customer_id).getConnectionInfo().getMax_load());
					jsonElement.getAsJsonObject().addProperty("hhv_nhv", cs.getCustomerInfo(customer_id).getConnectionInfo().getHhv_nhv());
			        setJsonResponse(gson.toJson(jsonElement));
				}
		   	        
			}
			catch(Exception ex){
				 setJsonResponse(AC.STATUS_ERROR,ex.getMessage());		
			}
	        return null;
	     
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}
	public ArrayList<MeterMfgDTO> getMfgList() {
		return mfgList;
	}
	public void setMfgList(ArrayList<MeterMfgDTO> mfgList) {
		this.mfgList = mfgList;
	}
	public ArrayList<MeterTypeDTO> getMeterTypeList() {
		return meterTypeList;
	}
	public void setMeterTypeList(ArrayList<MeterTypeDTO> meterTypeList) {
		this.meterTypeList = meterTypeList;
	}
	public ArrayList<MeterGRatingDTO> getgRatingList() {
		return gRatingList;
	}
	public void setgRatingList(ArrayList<MeterGRatingDTO> gRatingList) {
		this.gRatingList = gRatingList;
	}
	public MeterRepairmentDTO getRepair() {
		return repair;
	}
	public void setRepair(MeterRepairmentDTO repair) {
		this.repair = repair;
	}

	public ArrayList<EVCModelDTO> getEvcModelList() {
		return evcModelList;
	}

	public void setEvcModelList(ArrayList<EVCModelDTO> evcModelList) {
		this.evcModelList = evcModelList;
	}
	
	
	
	
	public CustomerMeterDTO getMeter() {
		return meter;
	}

	public void setMeter(CustomerMeterDTO meter) {
		this.meter = meter;
	}
	
}

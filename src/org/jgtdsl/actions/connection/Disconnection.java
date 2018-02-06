package org.jgtdsl.actions.connection;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DisconnectionService;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class Disconnection extends BaseAction {

	private static final long serialVersionUID = -7106617121688349210L;
	private String pId; //Disconnect Id
	private String meter_id;
	private String reading_id;
	private String customer_id;
	private MeterReadingDTO reading;
	private DisconnectDTO disconn;
	
	/* For METERED Customer */
	
	public String meterDisconnectionHome()
	{
		return SUCCESS;
	}
	
	public String saveMeterDisconnInfo()
	{
		DisconnectionService disconnService=new DisconnectionService();
		disconn.setInsert_by(((UserDTO)session.get("user")).getUserId());
		
		ResponseDTO response=null;
		if(disconn.getPid()==null || disconn.getPid().equalsIgnoreCase(""))
			response=disconnService.saveMeterDisconnInfo(disconn,reading);
		else
			response=disconnService.updateMeterDisconnInfo(disconn,reading);
			
		setJsonResponse(response);		

		return null;
	}
	
	public String getMeterDisconnInfo()
	{
		
		DisconnectionService disConnService=new DisconnectionService();
		DisconnectDTO disconnInfo=disConnService.getMeterDisconnectionInfo(pId,meter_id);
		
		Gson gson = new Gson();
		String json = gson.toJson(disconnInfo);
		setJsonResponse(json);
		
		return null;
	}
	
	public String deleteMeterDisconnInfo(){
		DisconnectionService disconnService=new DisconnectionService();
		ResponseDTO response=disconnService.deleteMeterDisconnInfo(pId);
		
		setJsonResponse(response);		

		return null;
	}
	
	/* For Non-METERED Customer */
	public String nonMeterDisconnectionHome()
	{
		return SUCCESS;
	}
	public String saveNonMeterDisconnInfo()
	{
		DisconnectionService disconnService=new DisconnectionService();
		disconn.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		if(disconn.getPid()==null || disconn.getPid().equalsIgnoreCase(""))
			response=disconnService.saveNonMeterDisconnInfo(disconn);
		else
			response=disconnService.updateNonMeterDisconnInfo(disconn);
			
		setJsonResponse(response);		

		return null;
	}
	
	public String getNonMeterDisconnInfo()
	{
		
		DisconnectionService disConnService=new DisconnectionService();
		CustomerService customerService=new CustomerService();
		DisconnectDTO disconnInfo=disConnService.getNonMeterDisconnectionInfo(pId,customer_id);
		
//		Gson gson = new Gson();
//		String json = gson.toJson(disconnInfo);
//		setJsonResponse(json);
		
		
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(disconnInfo);
		jsonElement.getAsJsonObject().addProperty("customer",gson.toJson(customerService.getCustomerInfo(disconnInfo.getCustomer_id())));
        setJsonResponse(gson.toJson(jsonElement));
        
		
		return null;
	}
	
	public String deleteNonMeterDisconnInfo(){
		DisconnectionService disconnService=new DisconnectionService();
		ResponseDTO response=disconnService.deleteNonMeterDisconnInfo(pId);
		
		setJsonResponse(response);		

		return null;
	}


	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public MeterReadingDTO getReading() {
		return reading;
	}

	public void setReading(MeterReadingDTO reading) {
		this.reading = reading;
	}

	public DisconnectDTO getDisconn() {
		return disconn;
	}

	public void setDisconn(DisconnectDTO disconn) {
		this.disconn = disconn;
	}

	public String getReading_id() {
		return reading_id;
	}

	public void setReading_id(String readingId) {
		reading_id = readingId;
	}

	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	
}

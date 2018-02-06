package org.jgtdsl.actions.connection;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.LoadPressureChangeDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.DisconnectionService;
import org.jgtdsl.models.LoadPressureChangeService;

import com.google.gson.Gson;

public class HhvVatRebateChange extends BaseAction {

	private static final long serialVersionUID = -3237836996850000269L;
	private MeterReadingDTO reading;
	private LoadPressureChangeDTO lpChange;
	private String pId;
	private String meter_id;
	
	public String hhvVatRebateChangeHome()
	{
		return SUCCESS;
	}
	
	public String saveLoadPressureChangeInfo()
	{
		LoadPressureChangeService lpChangeService=new LoadPressureChangeService();
		lpChange.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		if(lpChange.getPid()==null || lpChange.getPid().equalsIgnoreCase(""))
			response=lpChangeService.saveLoadPressureChange(lpChange,reading);
		else
			response=lpChangeService.updateLoadPressurechangeInfo(lpChange,reading);
			
		setJsonResponse(response);		

		return null;
	}

	public String getLoadPressureChangeInfo()
	{
		
		LoadPressureChangeService lpChangeService=new LoadPressureChangeService();
		LoadPressureChangeDTO lpChange=lpChangeService.getLoadPressureChangeInfo(pId,meter_id);
		
		Gson gson = new Gson();
		String json = gson.toJson(lpChange);
		setJsonResponse(json);
		
		return null;
	}
	
	
	public String deleteLoadPressureChagneInfo(){
		LoadPressureChangeService lpChangeService=new LoadPressureChangeService();
		ResponseDTO response=lpChangeService.deleteLoadPressureChangeInfo(pId);
		
		setJsonResponse(response);		

		return null;
	}

	public MeterReadingDTO getReading() {
		return reading;
	}

	public void setReading(MeterReadingDTO reading) {
		this.reading = reading;
	}

	public LoadPressureChangeDTO getLpChange() {
		return lpChange;
	}

	public void setLpChange(LoadPressureChangeDTO lpChange) {
		this.lpChange = lpChange;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}
	
	
	
}

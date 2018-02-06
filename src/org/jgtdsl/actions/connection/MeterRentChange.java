package org.jgtdsl.actions.connection;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.MeterRentService;

import com.google.gson.Gson;

public class MeterRentChange extends BaseAction {

	private static final long serialVersionUID = 500474690324632119L;
	private String pId; //reconnect Id
	private String meter_id;
	private MeterRentChangeDTO rentChange;
	
	
	public String meterRentChangeHome()
	{
		return SUCCESS;
	}

	public String saveMeterRentChangeInfo()
	{
		MeterRentService mRentService=new MeterRentService();
		rentChange.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		
		if(rentChange.getPid()==null || rentChange.getPid().equalsIgnoreCase(""))
			response=mRentService.saveMeterRentChangeInfo(rentChange);
		else
			response=mRentService.udpateMeterRentChangeInfo(rentChange);
			
		setJsonResponse(response);		

		return null;
	}
	
	public String getMeterRentChangeInfo()
	{
		
		MeterRentService mRentService=new MeterRentService();
		MeterRentChangeDTO rentChagneInfo=mRentService.getMeterRentChangeInfo(pId);
		
		Gson gson = new Gson();
		String json = gson.toJson(rentChagneInfo);
		setJsonResponse(json);
		
		return null;
	}

	public String deleteMeterRentChagneInfo(){
		MeterRentService mRentService=new MeterRentService();
		ResponseDTO response=mRentService.deleteMeterRentChangeInfo(pId);
		
		setJsonResponse(response);
		return null;
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

	public MeterRentChangeDTO getRentChange() {
		return rentChange;
	}

	public void setRentChange(MeterRentChangeDTO rentChange) {
		this.rentChange = rentChange;
	}
	
	
}

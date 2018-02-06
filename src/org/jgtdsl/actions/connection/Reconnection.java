package org.jgtdsl.actions.connection;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.ReconnectDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.ReconnectionService;

import com.google.gson.Gson;


public class Reconnection extends BaseAction {

	private static final long serialVersionUID = 3748610871389779512L;
	private String pId; //reconnect Id
	private String meter_id;
	private ReconnectDTO reconn;
	private DisconnectDTO disconn;
	
	/* For METERED Customer */
	
	public String meterReconnectionHome()
	{
		return SUCCESS;
	}
	
	public String saveMeterReconnInfo()
	{
		ReconnectionService reconnService=new ReconnectionService();
		reconn.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		
		if(reconn.getPid()==null || reconn.getPid().equalsIgnoreCase(""))
			response=reconnService.saveMeterReconnectInfo(reconn,disconn);
		else
			response=reconnService.updateMeterReconnInfo(reconn,disconn);
			
		setJsonResponse(response);		

		return null;
	}
	public String getMeterReconnInfo()
	{
		
		ReconnectionService reConnService=new ReconnectionService();
		ReconnectDTO reconnInfo=reConnService.getMeterReconnectionInfo(pId,meter_id);
		
		Gson gson = new Gson();
		String json = gson.toJson(reconnInfo);
		setJsonResponse(json);
		
		return null;
	}

	public String deleteMeterReconnInfo(){
		ReconnectionService reconnService=new ReconnectionService();
		ResponseDTO response=reconnService.deleteMeterReconnInfo(pId);
		
		setJsonResponse(response);
		return null;
	}
	
	
	/* For Non-METERED Customer */
	
	public String nonMeterReconnectionHome()
	{
		return SUCCESS;
	}
	
	public String saveNonMeterReconnInfo()
	{
		ReconnectionService reconnService=new ReconnectionService();
		reconn.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		
		if(reconn.getPid()==null || reconn.getPid().equalsIgnoreCase(""))
			response=reconnService.saveNonMeterReconnectInfo(reconn,disconn);
		else
			response=reconnService.updateNonMeterReconnInfo(reconn,disconn);
			
		setJsonResponse(response);		

		return null;
	}
	
	public String getNonMeterReconnInfo()
	{
		
		ReconnectionService reConnService=new ReconnectionService();
		ReconnectDTO reconnInfo=reConnService.getNonMeterReconnectionInfo(pId);
		
		Gson gson = new Gson();
		String json = gson.toJson(reconnInfo);
		setJsonResponse(json);
		
		return null;
	}
	public String deleteNonMeterReconnInfo(){
		ReconnectionService reconnService=new ReconnectionService();
		ResponseDTO response=reconnService.deleteNonMeterReconnInfo(pId);
		
		setJsonResponse(response);
		return null;
	}
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public ReconnectDTO getReconn() {
		return reconn;
	}

	public void setReconn(ReconnectDTO reconn) {
		this.reconn = reconn;
	}

	public DisconnectDTO getDisconn() {
		return disconn;
	}

	public void setDisconn(DisconnectDTO disconn) {
		this.disconn = disconn;
	}

	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}
	

}

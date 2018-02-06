package org.jgtdsl.actions.connection;

import java.util.ArrayList;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.BurnerQntChangeService;
import org.jgtdsl.models.MeterRentService;

import com.google.gson.Gson;

public class BurnerQntChange  extends BaseAction {

	private static final long serialVersionUID = -8986098148135098574L;
	private String pId; //Burner Change Id
	private String customer_id;
	private String appliance_id; 
	private BurnerQntChangeDTO bqc;
	private CustomerApplianceDTO app;
	ArrayList<CustomerApplianceDTO> raizerDisconnectionList=new ArrayList<CustomerApplianceDTO>();

	
	public String burnerQntChangeHome()
	{
		return SUCCESS;
	}

	public String saveBurnerQntChangeInfo()
	{
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		bqc.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		
		if(bqc.getPid()==null || bqc.getPid().equalsIgnoreCase(""))
			response=burnerQntService.saveBurnerQntChangeInfo(bqc);
		else
			response=burnerQntService.updateBurnerQntChangeInfo(bqc);
			
		setJsonResponse(response);		

		return null;
	}
	public String saveNewApplianceInfo()
	{
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		app.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		
		
		response=burnerQntService.saveNewApplianceInfo(app);
		
		
			
		setJsonResponse(response);		

		return null;
	}

	public String getBurnerQntChangeInfo()
	{
		
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		BurnerQntChangeDTO burnerQntChange=burnerQntService.getBurnerQntChangeInfo(pId);
		
		Gson gson = new Gson();
		String json = gson.toJson(burnerQntChange);
		setJsonResponse(json);
		
		return null;
	}
	public String dissconnectRaizer(){
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		app.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=burnerQntService.dissconnectRaizer(app);	
		setJsonResponse(response);
		return null;
	}
	public String reconnectRaizer(){
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		app.setInsert_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=burnerQntService.reconnectRaizer(app);	
		setJsonResponse(response);
		return null;
	}
	public String deleteBurnerQntChagneInfo(){
		BurnerQntChangeService burnerQntService=new BurnerQntChangeService();
		
		boolean canDelete=burnerQntService.canDeleteApplianceEntry(pId);
		if(canDelete==false){
			ResponseDTO response=new ResponseDTO();
			response.setResponse(false);
			response.setMessasge("You can't delete this Entry. As this reading has been Not Latest One");
			setJsonResponse(response);
			return null;
		}
		ResponseDTO response=burnerQntService.deleteBurnerQntChangeInfo(pId);
		
		setJsonResponse(response);
		return null;
	}
	public String getRaizerDisconnectionListInfo()	
	{
		BurnerQntChangeService brnService=new BurnerQntChangeService();
		raizerDisconnectionList=brnService.getRaizerDisconnectionListInfo(customer_id);		
		return SUCCESS;
	}
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public BurnerQntChangeDTO getBqc() {
		return bqc;
	}

	public void setBqc(BurnerQntChangeDTO bqc) {
		this.bqc = bqc;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getAppliance_id() {
		return appliance_id;
	}

	public void setAppliance_id(String appliance_id) {
		this.appliance_id = appliance_id;
	}

	public CustomerApplianceDTO getApp() {
		return app;
	}

	public void setApp(CustomerApplianceDTO app) {
		this.app = app;
	}

	public ArrayList<CustomerApplianceDTO> getRaizerDisconnectionList() {
		return raizerDisconnectionList;
	}

	public void setRaizerDisconnectionList(
			ArrayList<CustomerApplianceDTO> raizerDisconnectionList) {
		this.raizerDisconnectionList = raizerDisconnectionList;
	}

	
	
}

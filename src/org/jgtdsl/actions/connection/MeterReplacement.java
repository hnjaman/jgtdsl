package org.jgtdsl.actions.connection;

import java.util.ArrayList;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.EVCModelDTO;
import org.jgtdsl.dto.MeterGRatingDTO;
import org.jgtdsl.dto.MeterMfgDTO;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.MeterReplacementDTO;
import org.jgtdsl.dto.MeterTypeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.MeterInformationService;
import org.jgtdsl.models.MeterRentService;
import org.jgtdsl.models.MeterReplacementService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.utils.Utils;

import com.google.gson.Gson;

public class MeterReplacement extends BaseAction {

	private static final long serialVersionUID = 4949543267364711284L;
	private String pId;
	private String customer_id;
	private ArrayList<MeterMfgDTO>  mfgList;
	private ArrayList<MeterTypeDTO> meterTypeList;
	private ArrayList<MeterGRatingDTO> gRatingList;
	private ArrayList<EVCModelDTO> evcModelList;
	private CustomerMeterDTO newMeter;
	private CustomerMeterDTO oldMeter;
	
	public void prepareList(){		
		MeterService meterService=new MeterService();
		mfgList=meterService.getMfgList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		meterTypeList=meterService.getMeterTypeList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
		gRatingList=meterService.getGRatingList(0,0,Utils.EMPTY_STRING,"VIEW_ORDER","ASC",0);
		evcModelList=meterService.getEvcModelList(0,0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}
	
	public String meterReplacementHome(){
		prepareList();
		return SUCCESS;
	}

	public String saveMeterReplacementInfo(){
		
		MeterReplacementService mrService=new MeterReplacementService();
		newMeter.setInsert_by(((UserDTO)session.get("user")).getUserId());		
		ResponseDTO response=null;
		response=mrService.saveMeterReplacementInfo(oldMeter,newMeter);
			
		setJsonResponse(response);		

		return null;
	}
	public String getMeterReplacementInfo()
	{
		
		MeterReplacementService mrService=new MeterReplacementService();
		MeterReplacementDTO replacementInfo=mrService.getMeterReplacementInfo(pId);
		
		Gson gson = new Gson();
		String json = gson.toJson(replacementInfo);
		setJsonResponse(json);
		
		return null;
	}

	public String deleteMeterReplacementInfo(){
		MeterReplacementService mrService=new MeterReplacementService();		
		ResponseDTO response=mrService.deleteReplacementInfo(pId);
		setJsonResponse(response);		
		return null;
	}
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
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
	public ArrayList<EVCModelDTO> getEvcModelList() {
		return evcModelList;
	}
	public void setEvcModelList(ArrayList<EVCModelDTO> evcModelList) {
		this.evcModelList = evcModelList;
	}

	public CustomerMeterDTO getNewMeter() {
		return newMeter;
	}

	public void setNewMeter(CustomerMeterDTO newMeter) {
		this.newMeter = newMeter;
	}

	public CustomerMeterDTO getOldMeter() {
		return oldMeter;
	}

	public void setOldMeter(CustomerMeterDTO oldMeter) {
		this.oldMeter = oldMeter;
	}
	
	
}

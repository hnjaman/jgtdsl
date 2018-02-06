package org.jgtdsl.actions.connection;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.CustomerTypeChangeService;
import org.jgtdsl.models.MeterRentService;

import com.google.gson.Gson;

public class CustomerTypeChange extends BaseAction {

	private static final long serialVersionUID = 500474690324632119L;
	private String pId; //reconnect Id
	private String meter_id;
	private MeterRentChangeDTO rentChange;
	private CustomerDTO customer;
	
	
	public String customerTypeChangeHome()
	{
		return SUCCESS;
	}

	public String saveCustomerTypeChangeInfo()
	{
		CustomerTypeChangeService mTypeService=new CustomerTypeChangeService();
		customer.setInserted_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
		
        response=mTypeService.saveCustomerTypeChangeInfo(customer);
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
	
	
	
	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
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

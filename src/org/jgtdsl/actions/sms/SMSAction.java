package org.jgtdsl.actions.sms;

import java.util.ArrayList;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.CustomerSmsDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.AdjustmentCollectionService;
import org.jgtdsl.models.SMSService;

import com.google.gson.Gson;



public class SMSAction extends BaseAction{

	private static final long serialVersionUID = -6056989883561883108L;
	
	private ArrayList<CustomerSmsDTO> custList;
	private String areaId;
	private String customerCategory;
	private String billMonth;
	private String billYear;
	
	public String execute()
	{
		
		SMSService smsService =new SMSService();
		
		custList=smsService.getSMSSendDefaulter(areaId, customerCategory, billMonth, billYear);
		
		
		
		for(int i=0; i<custList.size(); i++)
		{
			if(i%50==0)
				try{Thread.sleep(2000);}catch(Exception e){e.printStackTrace();};
			CustomerSmsDTO tmp = custList.get(i);
			
			SmsSender smsSender=new SmsSender();
			smsSender.setCustomerID(tmp.getCustomerId());
			smsSender.setMobile(tmp.getMobileNo());
			smsSender.setText(tmp.getTextSMS());
			smsSender.setBillMonth(billMonth);
			smsSender.setBillYear(billYear);
			
			smsSender.sendSMSSSL();
			//Thread thread = new Thread(smsSender);
			//thread.start();
						
		}
		try{Thread.sleep(8000);}catch(Exception e){e.printStackTrace();};
		
		custList=smsService.getSMSDefaulter(areaId, customerCategory, billMonth, billYear);
		
		
		return "success";
	}

	public String getProcessedSMSCount()
	{
		SMSService smsService= new SMSService();
		ResponseDTO response=smsService.getCountSMS(areaId, customerCategory, billMonth, billYear);
		
		setJsonResponse(response);
        return null;        
        
	}
	public String getTotalCustomerToSendSMS(){
		SMSService smsService= new SMSService();
		ResponseDTO response=smsService.getCountCustomer(areaId, customerCategory, billMonth, billYear);
		
		setJsonResponse(response);
		
		return null;
	}
	
	
	public ArrayList<CustomerSmsDTO> getCustList() {
		return custList;
	}

	public void setCustList(ArrayList<CustomerSmsDTO> custList) {
		this.custList = custList;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}

	public String getBillMonth() {
		return billMonth;
	}

	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}

	public String getBillYear() {
		return billYear;
	}

	public void setBillYear(String billYear) {
		this.billYear = billYear;
	}
	
	
	
	
}

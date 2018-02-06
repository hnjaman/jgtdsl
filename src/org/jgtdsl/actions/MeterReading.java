package org.jgtdsl.actions;

import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.MeterRentChangeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.MeterStatus;
import org.jgtdsl.models.MeterReadingService;
import org.jgtdsl.models.MeterRentService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.models.UserService;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;

import com.google.gson.Gson;

public class MeterReading extends BaseAction
{
	private static final long serialVersionUID = 5748017876458138941L;
	private ArrayList<CustomerMeterDTO> meterList=new ArrayList<CustomerMeterDTO>();
	private CustomerDTO customer;
	private String customer_id;
	private String billing_month;
	private String billing_year;
	private String meter_id;
	private String reading_purpose;
	private String area;
	private String customer_category;
	private String reading_date;
	private MeterReadingDTO reading;
	private String direction;
	private String reading_id;
	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	String index_key="reading_index_"+loggedInUser.getUserId();
	
	
	public String meterReadingHome(){
		CacheUtil.setObjToCache(index_key, "0");
		return SUCCESS;
	}
	public String singleReadingIntryForm(){
		MeterService ms=new MeterService();
		meterList=ms.getCustomerMeterList(customer_id, Utils.EMPTY_STRING,"And CUSTOMER_METER.STATUS="+MeterStatus.CONNECTED.getId());;
		return SUCCESS;
	}
	
	public String saveMeterReading()
	{
		MeterReadingService mrService=new MeterReadingService();
		ResponseDTO response=null;
		
		if(reading.getReading_id()==null || reading.getReading_id().equalsIgnoreCase(""))
			response=mrService.saveMeterReading(loggedInUser.getUserId(),reading);
		else
			response=mrService.updateMeterReading(loggedInUser.getUserId(),reading);
		
		setJsonResponse(response);
		return null;	
	}
	
	public String deleteMeterReading()
	{
		MeterReadingService mrService=new MeterReadingService();
		boolean canDelete=mrService.canDeleteReadingEntry(reading_id);
		if(canDelete==false){
			ResponseDTO response=new ResponseDTO();
			response.setResponse(false);
			response.setMessasge("You can't delete this reading. As this reading has been included in the Bill");
			setJsonResponse(response);
			return null;
		}
		ResponseDTO response=mrService.deleteMeterReading(reading_id);
		setJsonResponse(response);
		return null;	
	}
	
	//Get Meter Reading Information for a particular reading id
	public String getMeterReadingInfo()
	{
		MeterReadingDTO mrDTO=new MeterReadingDTO();
		mrDTO.setReading_id(this.reading_id);
		
		MeterReadingService mrs=new MeterReadingService();
		setJsonResponse(mrs.getMeterReading(loggedInUser.getUserId(),mrDTO).get(0).toString());
        return null; 
	}
	//Use for next previous reading entry......
	public String fetchReadingEntry(){
		
		String curr_index=(String)CacheUtil.getObjFromCache(index_key);
		//System.out.println(curr_index);
		MeterReadingService mrService=new MeterReadingService();
		MeterReadingDTO reading=null;
		int ind=0;
		
		
		String cat ="";
		String cKey="";
		ArrayList<MeterReadingDTO> readingList=null;
		int cind=0;
		if(!customer_id.equals(""))
		{
			cat=customer_id.substring(2, 4);
			setCustomer_category(cat);	
			String cKeySuffix=loggedInUser.getUserId()+"_"+(reading_id==null?"":reading_id)+"_"+""+"_"+billing_month+"_"+billing_year+"_"+reading_purpose+"_"+loggedInUser.getArea_id()+"_"+cat+"_"+reading_date+"_"+meter_id;
			cKey="CUSTOMER_LIST_READING_ENTRY_"+cKeySuffix;
			
			readingList=(ArrayList<MeterReadingDTO>)CacheUtil.getListFromCache(cKey, MeterReadingDTO.class);
			
			
			for(int i = 0; i < readingList.size(); ++i) {
		        if(readingList.get(i).getCustomer().getCustomer_id().equals(customer_id)) {
		        	cind=i;
		        	break;
		        }    	
		    }
						
		}
		//ArrayList<MeterReadingDTO> readingList=getListForReadingEntry(user_id,reading_id,customer_id,meter_id,billing_month,billing_year,reading_purpose,area,customer_category,reading_date);
		
			if(direction.equalsIgnoreCase("next"))
				ind=Integer.parseInt(curr_index)+1;
			else if(direction.equalsIgnoreCase("previous"))
				ind=Integer.parseInt(curr_index)-1;	
			else if(!customer_id.equals(""))
				ind=cind;	
			else if(direction.equalsIgnoreCase("start") && customer_id.equals("")){
				ind=0;
				CacheUtil.clear("CUSTOMER_LIST_READING_ENTRY_"+loggedInUser.getUserId());
			}
			else if(direction.equalsIgnoreCase("end"))
				ind=999999999;
	
		reading=mrService.getReadingEntry(loggedInUser.getUserId(),ind,index_key,reading_id,"",meter_id,billing_month,billing_year,reading_purpose,loggedInUser.getArea_id(),customer_category,reading_date);
		if(reading!=null) 			
			CacheUtil.setObjToCache(index_key, String.valueOf(ind));
		
		setJsonResponse(reading==null?"{}":reading.toString());					
		return null;
	}
	
	/*-- Used for Meter Disconnection and Load and Pressure Change Form ----*/
	public String fetchMeterReading(){
	MeterReadingService mrs=new MeterReadingService();
		setJsonResponse(mrs.getMeterReading(loggedInUser.getUserId(),reading).toString());
        return null;             
	}
	
	public ArrayList<CustomerMeterDTO> getMeterList() {
		return meterList;
	}
	public void setMeterList(ArrayList<CustomerMeterDTO> meterList) {
		this.meterList = meterList;
	}
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public MeterReadingDTO getReading() {
		return reading;
	}

	public void setReading(MeterReadingDTO reading) {
		this.reading = reading;
	}

	
	public String getBilling_month() {
		return billing_month;
	}

	public void setBilling_month(String billingMonth) {
		billing_month = billingMonth;
	}

	public String getBilling_year() {
		return billing_year;
	}

	public void setBilling_year(String billingYear) {
		billing_year = billingYear;
	}

	public String getMeter_id() {
		return meter_id;
	}

	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}

	public String getReading_purpose() {
		return reading_purpose;
	}

	public void setReading_purpose(String readingPurpose) {
		reading_purpose = readingPurpose;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}

	public String getReading_date() {
		return reading_date;
	}

	public void setReading_date(String readingDate) {
		reading_date = readingDate;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getReading_id() {
		return reading_id;
	}
	public void setReading_id(String readingId) {
		reading_id = readingId;
	}
	
	
}

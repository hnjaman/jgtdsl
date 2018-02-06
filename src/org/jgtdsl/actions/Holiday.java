package org.jgtdsl.actions;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.HolidayDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.HolidayService;

import com.google.gson.Gson;

public class Holiday extends BaseAction
{
	private static final long serialVersionUID = 5748017876458138941L;
	private String from;
	private String to;
	private HolidayDTO holiday;
	private String holidayId;
	private ArrayList<HolidayDTO> holidayList;
	
	public String getHolidays()
	{
		HolidayService holidayService=new HolidayService();
		holidayList=holidayService.getHolidayList(from,to);
		Gson gson = new Gson();
		String json = gson.toJson(holidayList);
		setJsonResponse(json);
        return null;
             
	}

	
	public String saveHoliday()
	{
		ResponseDTO response=null;		
		response=HolidayService.saveHoliday(holiday);
		setJsonResponse(response);
		return null;	
	}
	
	public String fetchHolidayList()
	{
		HolidayService holidayService=new HolidayService();
		holidayList=holidayService.getHolidayList(from,to);
		return SUCCESS;
	}
	
	public String deleteHoliday()
	{
		ResponseDTO response=null;		
		response=HolidayService.deleteHoliday(holidayId);
		setJsonResponse(response);
		return null;	
	}
	
	public String updateHome()
	{
		holiday=HolidayService.getHoliday(holidayId);
		return SUCCESS;
	}
	public String updateHoliday()
	{
		ResponseDTO response=null;
		response=HolidayService.updateHoliday(holiday);
		setJsonResponse(response);
		return null;
	}
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}


	public HolidayDTO getHoliday() {
		return holiday;
	}


	public void setHoliday(HolidayDTO holiday) {
		this.holiday = holiday;
	}


	public ArrayList<HolidayDTO> getHolidayList() {
		return holidayList;
	}


	public void setHolidayList(ArrayList<HolidayDTO> holidayList) {
		this.holidayList = holidayList;
	}


	public String getHolidayId() {
		return holidayId;
	}


	public void setHolidayId(String holidayId) {
		this.holidayId = holidayId;
	}


	


}

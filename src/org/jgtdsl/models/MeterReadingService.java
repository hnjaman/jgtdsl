package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

public class MeterReadingService {

	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
	public ArrayList<MeterReadingDTO> getSavedReadingList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		MeterReadingDTO reading=null;
		ArrayList<MeterReadingDTO> readingList=new ArrayList<MeterReadingDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		 if(whereClause.contains("customer_id"))
		 {
			 whereClause = new StringBuffer (whereClause).insert(0,"mr.").toString();
		 }
		 if(whereClause.contains("customer_name"))
		 {
			 whereClause=whereClause.replace("customer_name", "VC.FULL_NAME");
		 }
			 
		
		
		
		
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		
		if(total==0)
				  sql = " Select count(reading_id) TOTAL "+
				  	    " From METER_READING MR,MVIEW_CUSTOMER_INFO VC,CUSTOMER_METER CM "+
                        " Where MR.CUSTOMER_ID=VC.CUSTOMER_ID  AND MR.METER_ID=CM.METER_ID AND READING_PURPOSE NOT IN (0,1) " +
				  		"  "+(whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select reading_id,MR.customer_id,VC.FULL_NAME, MR.meter_id,meter_sl_no,difference,actual_consumption,total_consumption,insert_date,to_char(CURR_READING_DATE,'DD-MM-YYYY')  insert_date_formatted ,reading_purpose " +
					  " From METER_READING MR,MVIEW_CUSTOMER_INFO VC,CUSTOMER_METER CM " +
					  " Where MR.CUSTOMER_ID=VC.CUSTOMER_ID  AND MR.METER_ID=CM.METER_ID AND READING_PURPOSE NOT IN (0,1) " +
					  (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	  "    )tmp1 " +
				  	  "    )tmp2   " +
				  	  "  Where serial Between ? and ? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(total!=0)
				{
				stmt.setInt(1, index);
				stmt.setInt(2, index+offset);
				}
				r = stmt.executeQuery();
				if(total==0 && r.next())
				{				
					readingList = new ArrayList<MeterReadingDTO>(r.getInt("TOTAL"));
				}
				else{
					while (r.next())
					{
						reading=new MeterReadingDTO();
						
						reading.setReading_id(r.getString("READING_ID"));
						reading.setCustomer_id(r.getString("CUSTOMER_ID"));
						reading.setCustomer_name(r.getString("FULL_NAME"));
						reading.setMeter_id(r.getString("METER_ID"));
						reading.setMeter_sl_no(r.getString("METER_SL_NO"));
						reading.setDifference(r.getLong("DIFFERENCE"));
						reading.setActual_consumption(r.getFloat("ACTUAL_CONSUMPTION"));
						reading.setTotal_consumption(r.getFloat("TOTAL_CONSUMPTION"));
						reading.setInsert_date(r.getString("insert_date_formatted"));
						reading.setReading_purpose_str(ReadingPurpose.values()[r.getInt("reading_purpose")].getLabel());
						
						readingList.add(reading);
					}
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return readingList;
	}
	
	public MeterReadingDTO getReadingEntry(String user_id,int index,String index_key,String reading_id,String customer_id,String meter_id,String billing_month,String billing_year,String reading_purpose,String area,String customer_category,String reading_date){
		
		ArrayList<MeterReadingDTO> readingList=getListForReadingEntry(user_id,reading_id,customer_id,meter_id,billing_month,billing_year,reading_purpose,area,customer_category,reading_date);
		MeterReadingDTO reading=null;
		if(index==999999999)
			index=readingList.size()-1;
		
		if(readingList.size()>0 && index<readingList.size() && index>=0){			
			reading=readingList.get(index);
			reading.setTotal_reading_count(readingList.size());
			reading.setCurrent_reading_index(index+1);
		}
		else{
			if(index<0)
				CacheUtil.setObjToCache(index_key, String.valueOf("-1"));
			else
				CacheUtil.setObjToCache(index_key, String.valueOf(readingList.size()));
		}
		return reading;
	}
	
	
	public ArrayList<MeterReadingDTO> getReadingHistoryList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		MeterReadingDTO reading=null;
		ArrayList<MeterReadingDTO> readingList=new ArrayList<MeterReadingDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		
		if(total==0)
				  sql =   " Select  to_char(CURR_READING_DATE,'DD-MON-YY') INSERT_DATE,READING_ID, " +
						  " to_char(to_date(concat(LPAD(to_char(billing_month), 2, '0'),to_char(billing_year)),'MM,YYYY'),'MON, YYYY') month_year, " +
						  " prev_reading,curr_reading,difference from  METER_READING mr " +
						  "  "+(whereClause.equalsIgnoreCase("")?"Where READING_PURPOSE NOT IN (0,1) ":("Where ( "+whereClause+" AND READING_PURPOSE NOT IN (0,1))"))+" ";
		else
			      sql=    " Select * from ( " +
					  	  " Select rownum serial,tmp1.* from " +
					  	  " (   Select  to_char(CURR_READING_DATE,'DD-MON-YY') INSERT_DATE,READING_ID, " +
						  " to_char(to_date(concat(LPAD(to_char(billing_month), 2, '0'),to_char(billing_year)),'MM,YYYY'),'MON, YYYY') month_year, " +
						  " prev_reading,curr_reading,difference from  METER_READING mr " +
					  	  " "+(whereClause.equalsIgnoreCase("")?"Where READING_PURPOSE NOT IN (0,1) ":("Where ( "+whereClause+") AND READING_PURPOSE NOT IN (0,1)"))+" "+orderByQuery+			  	  
					  	  "    )tmp1 " +
					  	  "    )tmp2   " +
					  	  "  Where serial Between ? and ? ";
		   
		   PreparedStatement stmt = null;
		   ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				if(total!=0)
				{
				stmt.setInt(1, index);
				stmt.setInt(2, index+offset);
				}
				r = stmt.executeQuery();
				while (r.next())
				{
					reading=new MeterReadingDTO();
					reading.setReading_id(r.getString("READING_ID"));
					reading.setInsert_date(r.getString("INSERT_DATE"));
					reading.setMonth_year(r.getString("MONTH_YEAR"));
					reading.setPrev_reading(r.getLong("PREV_READING"));
					reading.setCurr_reading(r.getLong("CURR_READING"));
					reading.setDifference(r.getLong("DIFFERENCE"));
					
					readingList.add(reading);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return readingList;
	}
	
	public ResponseDTO saveMeterReading(String user_id,MeterReadingDTO reading)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		CustomerService customerService=new CustomerService();
		CustomerDTO customer=customerService.getCustomerInfo(reading.getCustomer_id());
		
		String select_query="select * from customer_meter where customer_id= ? AND METER_ID= ? ";
		
		PreparedStatement unit_type=null;
		ResultSet r=null;
		
		
		String sql=" Insert into METER_READING(READING_ID,CUSTOMER_ID,METER_ID,TARIFF_ID,RATE,billing_month, " +
				   " billing_year,READING_PURPOSE,PREV_READING,PREV_READING_DATE,CURR_READING,CURR_READING_DATE,DIFFERENCE, " +
				   " HHV_NHV,MIN_LOAD,MAX_LOAD,PMIN_LOAD,PMAX_LOAD,ACTUAL_CONSUMPTION,TOTAL_CONSUMPTION,METER_RENT,PRESSURE,PRESSURE_FACTOR,TEMPERATURE,TEMPERATURE_FACTOR,REMARKS,INSERT_BY) " +
				   " Values(SQN_METER_READING.nextval,?,?,?,?,?,?,?,?,to_date(?,'dd-MM-YYYY'),?,to_date(?,'dd-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
		PreparedStatement stmt = null;
		int totalDayDiff=Utils.getDateDiffInDays(reading.getPrev_reading_date(), reading.getCurr_reading_date());
		int totalDayDiff4mConnDateToReadingDate=Utils.getDateDiffInDays(customer.getConnectionInfo().getConnection_date(), reading.getCurr_reading_date());
		
		float propMinLoad=0.0f;
		float propMaxLoad=0.0f;
		
		// for jalalabad. without event partial load = min/max load
		if(hasEvent(reading.getCustomer_id(),reading.getBilling_month(),reading.getBilling_year())){
			
			if(hasEventRec(reading.getCustomer_id(),reading.getBilling_month(),reading.getBilling_year()))
				totalDayDiff=totalDayDiff+1;
			 propMinLoad=Utils.getProportionalLoad(reading.getMin_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());
			 propMaxLoad=Utils.getProportionalLoad(reading.getMax_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());
			
		}else{
			propMinLoad=reading.getMin_load();
			propMaxLoad=reading.getMax_load();
		}
		
		
		
		if((totalDayDiff4mConnDateToReadingDate<=365&&(customer.getCustomer_category().equals("05")||customer.getCustomer_category().equals("06")||customer.getCustomer_category().equals("07")||customer.getCustomer_category().equals("08")))||reading.getReading_purpose_str().equals("10"))
		{ // this block is for that minimujm bill is not applicable for industry and captive within one year and for reading purpose 10 which means reading on actual.
			// this block should be furnished later with another methods
			propMinLoad=0;
		}
		if(totalDayDiff<=0 && !(reading.getReading_purpose_str().equals("3")||reading.getReading_purpose_str().equals("8"))){
			response.setMessasge("<font color='green'>Invalid day difference.</font>");
			response.setResponse(false);
			return response;
		}
			try
			{
				String unit="";
				double total_consumption=0.0;
				double actual_consumption=0.0;
				
				unit_type = conn.prepareStatement(select_query);				
				unit_type.setString(1,reading.getCustomer_id());
				unit_type.setString(2,reading.getMeter_id());
				r = unit_type.executeQuery();
				if (r.next()){
					unit=r.getString("UNIT");
				}
				
				if(unit.equals("CM")){
					actual_consumption=reading.getActual_consumption();
					total_consumption=reading.getTotal_consumption();
				}else if(unit.equals("FT")){
					actual_consumption=round((reading.getActual_consumption()/35.3147),2);
					total_consumption=round((reading.getTotal_consumption()/35.3147),2);
				}
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, reading.getCustomer_id());
				stmt.setString(2, reading.getMeter_id());
				stmt.setInt(3, reading.getTariff_id());
				stmt.setDouble(4, reading.getRate());
				stmt.setInt(5, reading.getBilling_month());
				stmt.setInt(6, reading.getBilling_year());
				stmt.setString(7, reading.getReading_purpose_str());
				stmt.setDouble(8, reading.getPrev_reading());
				stmt.setString(9, reading.getPrev_reading_date());
				stmt.setDouble(10, reading.getCurr_reading());
				stmt.setString(11, reading.getCurr_reading_date());
				stmt.setDouble(12, reading.getDifference());
				stmt.setDouble(13, reading.getHhv_nhv());
				stmt.setFloat(14, reading.getMin_load());
				stmt.setFloat(15, reading.getMax_load());
				stmt.setFloat(16, propMinLoad);
				stmt.setFloat(17, propMaxLoad);
				stmt.setDouble(18, actual_consumption);
				stmt.setDouble(19, total_consumption);
				stmt.setDouble(20, reading.getMeter_rent());
				stmt.setDouble(21, reading.getPressure());
				stmt.setDouble(22, round(reading.getPressure_factor_latest(),2));
				stmt.setDouble(23, reading.getTemperature());
				stmt.setDouble(24, reading.getTemperature_factor());
				stmt.setString(25, reading.getRemarks());
				stmt.setString(26, loggedInUser.getUserName());
				stmt.executeUpdate();
				response.setMessasge("<font color='green'>Successfully Saved Meter Reading Information.</font>");
				response.setResponse(true);
				
				CacheUtil.clearStartWith("CUSTOMER_LIST_READING_ENTRY_"+user_id);

			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;


	}
	
	public ResponseDTO updateMeterReading(String user_id,MeterReadingDTO reading)
	{
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		String sql=" Update METER_READING Set CURR_READING=?,CURR_READING_DATE=to_date(?,'dd-MM-YYYY HH:MI'),DIFFERENCE=?,MIN_LOAD=?,MAX_LOAD=?,PMIN_LOAD=?,PMAX_LOAD=?,ACTUAL_CONSUMPTION=?,TOTAL_CONSUMPTION=?,REMARKS=? " +
				   "  Where READING_ID=? ";
		
//		int totalDayDiff=Utils.getDateDiffInDays(reading.getPrev_reading_date(), reading.getCurr_reading_date());
//		float propMinLoad=Utils.getProportionalLoad(reading.getMin_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());
//		float propMaxLoad=Utils.getProportionalLoad(reading.getMax_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());

		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
		//System.out.println("Prev Reading Date :"+reading.getPrev_reading_date()+"Curr Reading Date :"+ reading.getCurr_reading_date());
		int totalDayDiff=Utils.getDateDiffInDays(reading.getPrev_reading_date(), reading.getCurr_reading_date());
		//System.out.println("Day Diff:"+totalDayDiff);
		float propMinLoad=Utils.getProportionalLoad(reading.getMin_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());
		//System.out.println("Min Load : "+reading.getMin_load()+", Total Day Diff :"+totalDayDiff+", Billing Month :"+reading.getBilling_month()+", Billing Year :"+reading.getBilling_year());
		float propMaxLoad=Utils.getProportionalLoad(reading.getMax_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());
		//System.out.println("Max Load : "+reading.getMax_load()+", Total Day Diff :"+totalDayDiff+", Billing Month :"+reading.getBilling_month()+", Billing Year :"+reading.getBilling_year());
		
		//System.out.println("Prop Min Load:"+propMinLoad);
		//System.out.println("Prop Max Load:"+propMaxLoad);
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
		
		if(totalDayDiff<=0){
			response.setMessasge("<font color='green'>Invalid day difference.</font>");
			response.setResponse(false);
			return response;
		}
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setDouble(1, reading.getCurr_reading());
				stmt.setString(2, reading.getCurr_reading_date());
				stmt.setDouble(3, reading.getDifference());
				stmt.setFloat(4, reading.getMin_load());
				stmt.setFloat(5, reading.getMax_load());
				stmt.setFloat(6, propMinLoad);
				stmt.setFloat(7, propMaxLoad);				
				stmt.setDouble(8, reading.getActual_consumption());
				stmt.setDouble(9, reading.getTotal_consumption());
				stmt.setString(10, reading.getRemarks());
				stmt.setString(11, reading.getReading_id());
				stmt.executeUpdate();
				response.setMessasge("<font color='green'>Successfully Edited Meter Reading Information.</font>");
				response.setResponse(true);
				
				CacheUtil.clear("CUSTOMER_LIST_READING_ENTRY_"+user_id);

			} 
			catch (Exception e){e.printStackTrace();
			response.setMessasge(e.getMessage());
			response.setResponse(false);
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		return response;


	}
	
	public ArrayList<MeterReadingDTO> getListForReadingEntry(String user_id,String reading_id,String customer_id,String meter_id,String billing_month,String billing_year,String reading_purpose,String area,String customer_category,String reading_date)
	{
		ArrayList<MeterReadingDTO> readingList=null;
		MeterReadingDTO reading=null;		
		CustomerMeterDTO cus_meter=null;
		Connection conn = ConnectionManager.getConnection();
		
		String cKeySuffix=user_id+"_"+(reading_id==null?"":reading_id)+"_"+(customer_id==null?"":customer_id)+"_"+billing_month+"_"+billing_year+"_"+reading_purpose+"_"+area+"_"+customer_category+"_"+reading_date+"_"+meter_id;
		String cKey="CUSTOMER_LIST_READING_ENTRY_"+cKeySuffix;
		
		readingList=(ArrayList<MeterReadingDTO>)CacheUtil.getListFromCache(cKey, MeterReadingDTO.class);
		if(readingList!=null)
			return readingList;
		readingList=new ArrayList<MeterReadingDTO>();
		
		String sql=getQueryStringForMeterReading(reading_id,customer_id,meter_id,billing_month,billing_year,reading_purpose,area,customer_category,reading_date);
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					reading=new MeterReadingDTO();
					CustomerDTO customer=new CustomerDTO();
					customer.setCustomer_id(r.getString("CUSTOMER_ID"));
					customer.setArea(r.getString("AREA_ID"));
					customer.setCustomer_category(r.getString("CATEGORY_ID"));
					CustomerPersonalDTO personal=new CustomerPersonalDTO();
					personal.setFull_name(r.getString("FULL_NAME"));
					personal.setMobile(r.getString("MOBILE"));
					personal.setCustomer_id(r.getString("CUSTOMER_ID"));
					AddressDTO address=new AddressDTO();
					customer.setPersonalInfo(personal);
					address.setDivision_name(r.getString("DIVISION_NAME"));
					address.setDistrict_name(r.getString("DIST_NAME"));
					address.setUpazila_name(r.getString("UPAZILA_NAME"));
					address.setPost_code(r.getString("POST_CODE"));
					address.setPost_office(r.getString("POST_OFFICE"));
					address.setRoad_house_no(r.getString("ROAD_HOUSE_NO"));
					address.setAddress_line1(r.getString("ADDRESS_LINE1"));
					address.setAddress_line2(r.getString("ADDRESS_LINE2"));
					customer.setAddressInfo(address);
					
					reading.setMeter_id(r.getString("METER_ID")==null?"":r.getString("METER_ID"));
					reading.setMeter_sl_no(r.getString("METER_SL_NO"));
					reading.setMeasurement_type_name(MeterMeasurementType.values()[r.getInt("MEASUREMENT_TYPE")].getLabel());
					reading.setMeasurement_type_str(r.getString("MEASUREMENT_TYPE"));
					reading.setReading_purpose_str(r.getString("MR_READING_PURPOSE"));
					reading.setUnit(r.getString("UNIT"));
					
					if(!Utils.isNullOrEmpty(r.getString("tariff_id_price"))){
						String[] id_price_arr=r.getString("tariff_id_price").split(",");
						if(id_price_arr.length>0){
							reading.setLatest_tariff_id(Integer.valueOf(id_price_arr[0]));
							reading.setLatest_rate(Float.valueOf(id_price_arr[1]));
						}
					}
					
				
					if(r.getString("READING_ID")==null){						
						
						if(!Utils.isNullOrEmpty(r.getString("tariff_id_price"))){
							String[] id_price_arr=r.getString("tariff_id_price").split(",");
							if(id_price_arr.length>0){
								reading.setTariff_id(Integer.valueOf(id_price_arr[0]));
								reading.setRate(Float.valueOf(id_price_arr[1]));
							}
						}
						
						if(!Utils.isNullOrEmpty(r.getString("pre_reading"))){
							String[] reading_date_arr=r.getString("pre_reading").split(",");
							if(reading_date_arr.length>0){
								reading.setPrev_reading(Long.valueOf(reading_date_arr[0]));
								//reading.setPrev_reading(Long.parseLong(reading_date_arr[0]));
								reading.setPrev_reading_date(reading_date_arr[1]);
							}
						}
						reading.setPressure(r.getFloat("PRESSURE"));
						reading.setTemperature(r.getFloat("TEMPERATURE"));
						//reading.setPressure_factor(Double.valueOf(Utils.getPresusreFactor(reading.getPressure())));
						reading.setPressure_factor(Double.valueOf(r.getFloat("PRESSURE")));
						reading.setTemperature_factor(Utils.getTemperatureFactor(reading.getTemperature()));
						reading.setCurr_reading_date(reading_date);
						
						
						
					}
					else{
						
						reading.setReading_id(r.getString("READING_ID"));
						reading.setPrev_reading(r.getLong("MR_PREV_READING"));						
						reading.setPrev_reading_date(r.getString("MR_PREV_READING_DATE"));
						reading.setCurr_reading(r.getLong("MR_CURR_READING"));
						reading.setCurr_reading_date(r.getString("MR_CURR_READING_DATE"));
						
						reading.setDifference(r.getLong("MR_DIFFERENCE"));
						reading.setHhv_nhv(r.getFloat("MR_HHV_NHV"));
						reading.setMin_load(r.getFloat("MR_MIN_LOAD"));
						reading.setMax_load(r.getFloat("MR_MAX_LOAD"));
						reading.setHhv_nhv(r.getFloat("MR_HHV_NHV"));
						reading.setActual_consumption(r.getFloat("MR_ACTUAL_CONSUMPTION"));
						reading.setTotal_consumption(r.getFloat("MR_TOTAL_CONSUMPTION"));
						reading.setPressure(r.getFloat("MR_PRESSURE"));
						reading.setPressure_factor(r.getFloat("MR_PRESSURE_FACTOR"));
						reading.setTemperature(r.getFloat("MR_TEMPERATURE"));
						reading.setTemperature_factor(r.getFloat("MR_TEMPERATURE_FACTOR"));
						reading.setTariff_id(r.getInt("MR_TARIFF_ID"));
						reading.setLatest_tariff_id(r.getInt("MR_TARIFF_ID"));
						reading.setRate(r.getFloat("MR_RATE"));
						reading.setUnit(r.getString("UNIT"));
						  
					}
					
					
					reading.setPressure_latest(r.getFloat("PRESSURE"));
					//reading.setPressure_factor_latest(Double.valueOf(Utils.getPresusreFactor(r.getFloat("PRESSURE"))));
					reading.setPressure_factor_latest(Double.valueOf(r.getFloat("PRESSURE")));
					reading.setTemperature_latest(r.getFloat("TEMPERATURE"));
					reading.setTemperature_factor_latest(Utils.getTemperatureFactor(r.getFloat("TEMPERATURE")));
					
					reading.setMeter_rent(r.getFloat("MR_METER_RENT"));
					
					
					
					
					reading.setBill_id(r.getString("BILL_ID"));
					reading.setRemarks(r.getString("MR_REMARKS"));
					reading.setUnit(r.getString("UNIT"));
					CustomerConnectionDTO connection =new CustomerConnectionDTO();
					connection.setMin_load(r.getString("MIN_LOAD"));
					connection.setMax_load(r.getString("MAX_LOAD"));
					connection.setHhv_nhv(r.getFloat("HHV_NHV"));										
					connection.setStatus_name(ConnectionStatus.values()[r.getInt("CONNECTION_STATUS")].getLabel());
					connection.setIsMetered_name(MeteredStatus.values()[r.getInt("ISMETERED")].getLabel());
					customer.setConnectionInfo(connection);
					
					reading.setCustomer(customer);
					
					readingList.add(reading);
				}
				CacheUtil.setListToCache(cKey,readingList);
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return readingList;
	}
	
	public String getQueryStringForMeterReading(String reading_id,String customer_id,String meter_id,String billing_month,String billing_year,String reading_purpose,String area,String customer_category,String reading_date){
		
		
		if(reading_id!=null && !reading_id.equalsIgnoreCase(""))
			return "Select tmp1.*,BILL_ID From (  " +
			"Select MR.READING_ID,MR.CUSTOMER_ID,AREA_ID,AREA_NAME,CATEGORY_ID,CI.CATEGORY_NAME,FULL_NAME,MOBILE, CM.UNIT,  " +
			"CI.CONNECTION_STATUS,CM.STATUS,CI.ISMETERED,METER_TYPE,CM.METER_SL_NO,MEASUREMENT_TYPE,CI.MIN_LOAD,CI.MAX_LOAD,CI.HHV_NHV,  " +
			"MR.METER_ID,MR.READING_PURPOSE MR_READING_PURPOSE,  " +
			"MR.TARIFF_ID MR_TARIFF_ID,MR.RATE MR_RATE,  " +
			"PREV_READING MR_PREV_READING,TO_CHAR(PREV_READING_DATE,'DD-MM-YYYY') MR_PREV_READING_DATE ,CURR_READING MR_CURR_READING,TO_CHAR(CURR_READING_DATE,'DD-MM-YYYY') MR_CURR_READING_DATE,  " +
			"DIFFERENCE MR_DIFFERENCE,MR.HHV_NHV MR_HHV_NHV,MR.MIN_LOAD MR_MIN_LOAD,MR.MAX_LOAD MR_MAX_LOAD,ACTUAL_CONSUMPTION MR_ACTUAL_CONSUMPTION,TOTAL_CONSUMPTION MR_TOTAL_CONSUMPTION,  " +
			"MR.METER_RENT MR_METER_RENT,MR.PRESSURE MR_PRESSURE,MR.PRESSURE_FACTOR MR_PRESSURE_FACTOR,MR.TEMPERATURE MR_TEMPERATURE,MR.TEMPERATURE_FACTOR MR_TEMPERATURE_FACTOR,  " +
			"MR.REMARKS MR_REMARKS,TARIFF_ID,RATE,getTariffInfo(MR.CUSTOMER_ID,TO_CHAR (CURR_READING_DATE, 'DD-MM-YYYY')) tariff_id_price,CM.PRESSURE PRESSURE,CM.TEMPERATURE TEMPERATURE,  " +
			"DIVISION_NAME,DIST_NAME,UPAZILA_NAME,POST_CODE,POST_OFFICE,ROAD_HOUSE_NO,ADDRESS_LINE1,ADDRESS_LINE2 "+
			"From Meter_Reading MR, " +
			"MVIEW_CUSTOMER_INFO CI, " +
			"Customer_Meter CM  " +
			"Where MR.READING_ID= "+reading_id +
			"AND CI.CUSTOMER_ID=MR.CUSTOMER_ID  " +
			"AND CI.CUSTOMER_ID=CM.CUSTOMER_ID  " +
			"AND CM.METER_ID=MR.METER_ID )tmp1   " +
			"LEFT OUTER JOIN billing_reading_map brm  " +
			"ON tmp1.READING_ID = brm.READING_ID order by customer_id asc";

		String wCustomer_id=Utils.EMPTY_STRING;
		String wMeter_id=Utils.EMPTY_STRING;
		String wArea_id=Utils.EMPTY_STRING;
		String wCustomer_category=Utils.EMPTY_STRING;
		String wReading_purpose=Utils.EMPTY_STRING;
		String wBilling_month=Utils.EMPTY_STRING;
		String wBilling_year=Utils.EMPTY_STRING;
		
		if(!Utils.isNullOrEmpty(customer_id))
			wCustomer_id=" and CI.customer_id='"+customer_id+"'";
		if(!Utils.isNullOrEmpty(meter_id))
			wMeter_id=" and CM.meter_id="+meter_id;
		if(!Utils.isNullOrEmpty(area))
			wArea_id=" and area_id='"+area+"'";
		if(!Utils.isNullOrEmpty(customer_category))
			wCustomer_category=" and CI.CATEGORY_ID='"+customer_category+"'";
		if(!Utils.isNullOrEmpty(reading_purpose))
			wReading_purpose=" and MR.reading_purpose='"+reading_purpose+"'";
		else
			wReading_purpose=" and nvl(MR.reading_purpose,0)>=2 ";
		
		if(!Utils.isNullOrEmpty(billing_month))
			wBilling_month=" and MR.billing_month='"+billing_month+"'";
		if(!Utils.isNullOrEmpty(billing_year))
			wBilling_year=" and MR.billing_year='"+billing_year+"'";
		
		return "SELECT CI.customer_id, " +
		"       AREA_ID,AREA_NAME,"+
		"       category_id, " +
		"       category_name, " +
		"       CI.FULL_NAME, " +
		"       CI.mobile, " +
		"       CI.MIN_LOAD, " +
		"       CI.MAX_LOAD, " +
		"       CI.CONNECTION_STATUS, " +
		"       CI.ISMETERED, " +
		"       CI.ADDRESS, " +
		"       CM.METER_TYPE, " +
		"       CM.METER_RENT, " +
		"       CM.PRESSURE, " +
		"       CM.TEMPERATURE, " +
		"       CM.meter_rent MR_METER_RENT, " +
		"       CM.meter_sl_no, " +
		"       CM.MEASUREMENT_TYPE, " +
		"       CM.meter_id, " +
		"       MR.READING_PURPOSE, CM.UNIT, " +
		"       getTariffInfo(CI.customer_id,'"+reading_date+"') tariff_id_price, " +
		"       getPrevReadingInfo(CM.meter_id,'"+reading_date+"') pre_reading, " +
		"       BRM.BILL_ID, " +
		"       MR.READING_ID, " +
		"       MR.PREV_READING MR_PREV_READING, " +
		"       TO_CHAR (MR.PREV_READING_DATE, 'DD-MM-YYYY') MR_PREV_READING_DATE, " +
		"       MR.CURR_READING MR_CURR_READING, " +
		"       TO_CHAR (MR.CURR_READING_DATE, 'DD-MM-YYYY') MR_CURR_READING_DATE, " +
		"       MR.DIFFERENCE MR_DIFFERENCE, " +
		"       CI.HHV_NHV, " +
		"       MR.HHV_NHV MR_HHV_NHV, " +
		"       MR.MIN_LOAD MR_MIN_LOAD, " +
		"       MR.MAX_LOAD MR_MAX_LOAD, " +
		"       MR.ACTUAL_CONSUMPTION MR_ACTUAL_CONSUMPTION, " +
		"       MR.TOTAL_CONSUMPTION MR_TOTAL_CONSUMPTION, " +
		"       MR.METER_RENT MR_METER_RENT, " +
		"       MR.PRESSURE MR_PRESSURE, " +
		"       MR.PRESSURE_FACTOR MR_PRESSURE_FACTOR, " +
		"       MR.TEMPERATURE MR_TEMPERATURE, " +
		"       MR.TEMPERATURE_FACTOR MR_TEMPERATURE_FACTOR, " +
		"       MR.REMARKS MR_REMARKS, " +
		"       MR.TARIFF_ID MR_TARIFF_ID, " +
		"       MR.RATE MR_RATE,getTariffInfo(MR.CUSTOMER_ID,TO_CHAR (CURR_READING_DATE, 'DD-MM-YYYY')) tariff_id_price,CM.PRESSURE PRESSURE,CM.TEMPERATURE TEMPERATURE, " +
		"       MR.READING_PURPOSE MR_READING_PURPOSE, " +
		"       DIVISION_NAME,DIST_NAME,UPAZILA_NAME,POST_CODE,POST_OFFICE,ROAD_HOUSE_NO,ADDRESS_LINE1,ADDRESS_LINE2"+
		"  FROM MVIEW_CUSTOMER_INFO CI " +
		"       INNER JOIN " +
		"       customer_meter CM " +
		"  on (CI.CUSTOMER_ID=CM.CUSTOMER_ID "+wMeter_id+
		"       And TO_DATE(TO_CHAR(CM.INSTALLED_DATE,'MM-YYYY'),'MM-YYYY')<=to_date('"+StringUtils.leftPad(billing_month, 2, "0")+"-"+billing_year+"','MM-YYYY') "+
        "   ) "+
	    wCustomer_id+wArea_id+wCustomer_category+
		"AND CM.STATUS=01     AND CI.CONNECTION_STATUS=01       LEFT OUTER JOIN " +
		"       meter_reading MR " +
		"  on (CI.customer_id=MR.CUSTOMER_ID"+wBilling_month+wBilling_year+wReading_purpose+"  and MR.meter_id=CM.meter_id) "+
		"       LEFT OUTER JOIN billing_reading_map BRM " +
		"          ON MR.READING_ID = BRM.READING_ID order by CI.customer_id asc";
		
		
		
	}
	
	
	public ArrayList<MeterReadingDTO> getMeterReading(String user_id,MeterReadingDTO  reading)
	{
		return getListForReadingEntry(user_id,reading.getReading_id(),reading.getCustomer_id(),reading.getMeter_id(),String.valueOf(reading.getBilling_month()),String.valueOf(reading.getBilling_year()) ,reading.getReading_purpose_str(),Utils.EMPTY_STRING,Utils.EMPTY_STRING,reading.getCurr_reading_date());
	}
	
	public PreparedStatement getStatementForReadingUpdate(Connection conn,MeterReadingDTO reading){
		
		PreparedStatement stmt = null;
		
		String sqlReading=" Update METER_READING Set " +
				"CURR_READING=?,CURR_READING_DATE=to_date(?,'dd-MM-YYYY'),DIFFERENCE=?,ACTUAL_CONSUMPTION=?,TOTAL_CONSUMPTION=?,REMARKS=? Where READING_ID=?" ;
		
		try {
			stmt = conn.prepareStatement(sqlReading);
			stmt.setDouble(1,reading.getCurr_reading());
			stmt.setString(2,reading.getCurr_reading_date());
			stmt.setDouble(3,reading.getDifference());
			stmt.setDouble(4,reading.getActual_consumption());
			stmt.setDouble(5,reading.getTotal_consumption());
			stmt.setString(6,reading.getRemarks());
			stmt.setString(7,reading.getReading_id());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stmt;
	}
	
	public ResponseDTO deleteMeterReading(String reading_id)
	{		
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();

		//Validation goes here....						  
		String sql="Delete METER_READING Where READING_ID=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,reading_id);				
				stmt.execute();
				
				response.setMessasge("<font color='green'>Successfully Deleted Meter Reading Information.</font>");
				response.setResponse(true);				
			} 
			catch (Exception e){e.printStackTrace();response.setResponse(false);response.setMessasge(e.getMessage());}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return response;
	 	
	}
	
	
	
	public boolean hasEvent(String customer_id, int bmonth, int byear )
	{		
		Connection conn = ConnectionManager.getConnection();
		String yearMonth=String.valueOf(byear)+String.format("%02d", bmonth);
		String sql="select Count(*) cn from ( " +
				" select CUSTOMER_ID from DISCONN_METERED where CUSTOMER_ID='"+customer_id+"' and to_char(DISCONNECT_DATE,'YYYYMM')='"+ yearMonth+"'"+
				" union all " +
				" select CUSTOMER_ID from RECONN_METERED where CUSTOMER_ID='"+customer_id+"' and to_char(RECONNECT_DATE,'YYYYMM')='"+ yearMonth+"') " ;

		PreparedStatement stmt = null;
		ResultSet r = null;
		   
		try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				if (r.next())
				{									
					return r.getInt("cn")>0?true:false;				
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return false;	 	
	}
	
	
	public boolean hasEventDis(String customer_id, int bmonth, int byear )
	{		
		Connection conn = ConnectionManager.getConnection();
		String yearMonth=String.valueOf(byear)+String.format("%02d", bmonth);
		String sql="select Count(*) cn from ( " +
				" select CUSTOMER_ID from DISCONN_METERED where CUSTOMER_ID='"+customer_id+"' and to_char(DISCONNECT_DATE,'YYYYMM')='"+ yearMonth+"')";
				

		PreparedStatement stmt = null;
		ResultSet r = null;
		   
		try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				if (r.next())
				{									
					return r.getInt("cn")>0?true:false;				
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return false;	 	
	}
	
	
	public boolean hasEventRec(String customer_id, int bmonth, int byear )
	{		
		Connection conn = ConnectionManager.getConnection();
		String yearMonth=String.valueOf(byear)+String.format("%02d", bmonth);
		String sql="select Count(*) cn from ( " +				
				" select CUSTOMER_ID from RECONN_METERED where CUSTOMER_ID='"+customer_id+"' and to_char(RECONNECT_DATE,'YYYYMM')='"+ yearMonth+"') " ;

		PreparedStatement stmt = null;
		ResultSet r = null;
		   
		try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				if (r.next())
				{									
					return r.getInt("cn")>0?true:false;				
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return false;	 	
	}
	
	public boolean canDeleteReadingEntry(String reading_id)
	{		
		Connection conn = ConnectionManager.getConnection();
		String sql="Select Count(Reading_Id) TOTAL from BILLING_READING_MAP Where Reading_Id=?";

		PreparedStatement stmt = null;
		ResultSet r = null;
		   
		try
			{
				stmt = conn.prepareStatement(sql);
			    stmt.setString(1, reading_id);
			    
				r = stmt.executeQuery();
				if (r.next())
				{									
					return r.getInt("TOTAL")>0?false:true;				
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return false;	 	
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
		
}


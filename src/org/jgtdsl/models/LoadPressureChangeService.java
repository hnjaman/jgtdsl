package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.actions.connection.LoadPressureChange;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.LoadPressureChangeDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.LoadChangeType;
import org.jgtdsl.enums.MeterMeasurementType;
import org.jgtdsl.enums.ReadingPurpose;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

public class LoadPressureChangeService {

	public ResponseDTO saveLoadPressureChange(LoadPressureChangeDTO lpChange,MeterReadingDTO reading)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		int totalDayDiff=Utils.getDateDiffInDays(reading.getPrev_reading_date(), reading.getCurr_reading_date());
		double propMinLoad=Utils.getProportionalLoad(reading.getMin_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());
		double propMaxLoad=Utils.getProportionalLoad(reading.getMax_load(), totalDayDiff,reading.getBilling_month(),reading.getBilling_year());
		
		if(totalDayDiff<0 && !reading.getReading_purpose_str().equals("3")){
			response.setMessasge("<font color='green'>Invalid day difference.</font>");
			response.setResponse(false);
			return response;
		}
		
		
		//response=validateDisconnInfo(disconn,reading);
//		if(response.isResponse()==false)
//			return response;

		String sqlReading="Insert into METER_READING(READING_ID, CUSTOMER_ID, METER_ID, TARIFF_ID, RATE, BILLING_MONTH, " +
				          " BILLING_YEAR, READING_PURPOSE, PREV_READING,PREV_READING_DATE, CURR_READING, CURR_READING_DATE, " +
				          " DIFFERENCE, HHV_NHV,MIN_LOAD, MAX_LOAD, ACTUAL_CONSUMPTION,TOTAL_CONSUMPTION, METER_RENT,PRESSURE, PRESSURE_FACTOR,  " +
				          " TEMPERATURE, TEMPERATURE_FACTOR, REMARKS,INSERT_BY,PMIN_LOAD,PMAX_LOAD) " +
				          " Values(SQN_METER_READING.nextval,?,?,?,?,?, " +
				          "?,?,?,to_date(?,'dd-MM-YYYY'),?,to_date(?,'dd-MM-YYYY'), " +
				          "?,?,?,?,?,?,?,?,?,?, " +
				          "?,?,?,?,?)";
						  
		String sqlReadingId="Select max(Reading_Id) Reading_Id from METER_READING WHERE  Reading_Purpose=? "+
						    "And customer_id=? and meter_id=? ";
		String sqlInsert=" Insert Into LOAD_PRESSURE_CHANGE(PID, CUSTOMER_ID, METER_ID, CHANGE_TYPE, OLD_MIN_LOAD,OLD_MAX_LOAD, NEW_MIN_LOAD, " +
						 " NEW_MAX_LOAD, OLD_PRESSURE, NEW_PRESSURE,CHANGED_BY, EFFECTIVE_DATE, READING_ID, REMARKS, INSERT_BY) " +
						 " Values(SQN_CNG_LOAD_PRESSURE.nextval,?,?,?,?,?,?,?,?,?,?,to_date(?,'dd-MM-YYYY'),?,?,?)";
		
		String updateCusConnection="update customer_connection set MIN_LOAD=?,MAX_LOAD=? where customer_id=?";
		String updateCusMeter="update customer_meter set PRESSURE=? where customer_id=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlReading);
				stmt.setString(1,reading.getCustomer_id());
				stmt.setString(2,reading.getMeter_id());
				stmt.setInt(3,reading.getTariff_id());
				stmt.setDouble(4,reading.getRate());
				stmt.setInt(5,reading.getBilling_month());
				stmt.setInt(6,reading.getBilling_year());
				stmt.setInt(7,ReadingPurpose.GENERAL_BILLING.getId());
				stmt.setDouble(8,reading.getPrev_reading());
				stmt.setString(9,reading.getPrev_reading_date());
				stmt.setDouble(10,reading.getCurr_reading());
				stmt.setString(11,lpChange.getEffective_date());
				stmt.setDouble(12,reading.getDifference());
				stmt.setDouble(13,reading.getHhv_nhv());
				stmt.setDouble(14,reading.getMin_load());
				stmt.setDouble(15,reading.getMax_load());
				stmt.setDouble(16,reading.getActual_consumption());
				stmt.setDouble(17,reading.getTotal_consumption());
				stmt.setDouble(18,reading.getMeter_rent());
				stmt.setDouble(19,reading.getPressure());
				stmt.setDouble(20,reading.getPressure_factor());
				stmt.setDouble(21,reading.getTemperature());
				stmt.setDouble(22,reading.getTemperature_factor());
				stmt.setString(23,reading.getRemarks());
				stmt.setString(24,lpChange.getInsert_by());
				stmt.setDouble(25,propMinLoad);
				stmt.setDouble(26,propMaxLoad);
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlReadingId);
				stmt.setInt(1,ReadingPurpose.GENERAL_BILLING.getId());
				stmt.setString(2,reading.getCustomer_id());
				stmt.setString(3,reading.getMeter_id());
				ResultSet r =stmt.executeQuery();
				int reading_id=0;
				
				if (r.next())
				{
					reading_id=r.getInt("READING_ID");
				}
				
				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,reading.getCustomer_id());
				stmt.setString(2,reading.getMeter_id());
				stmt.setString(3,lpChange.getChange_type_str());
				stmt.setString(4,lpChange.getOld_min_load());
				stmt.setString(5,lpChange.getOld_max_load());
				
				if(lpChange.getChange_type_str().equalsIgnoreCase("1")){
					stmt.setNull(6,java.sql.Types.NULL);
					stmt.setNull(7,java.sql.Types.NULL);
				}
				else {
					stmt.setString(6,lpChange.getNew_min_load());
					stmt.setString(7,lpChange.getNew_max_load());
				}
				stmt.setString(8,lpChange.getOld_pressure());				
				if(lpChange.getChange_type_str().equalsIgnoreCase("0"))
					stmt.setNull(9, java.sql.Types.NULL);
				else
					stmt.setString(9,lpChange.getNew_pressure());	
				
				stmt.setString(10,lpChange.getChange_by());
				
				stmt.setString(11,lpChange.getEffective_date());
				stmt.setInt(12,reading_id);
				stmt.setString(13,lpChange.getRemarks());
				stmt.setString(14,lpChange.getInsert_by());				
				stmt.execute();
				
				
					stmt = conn.prepareStatement(updateCusMeter);
					stmt.setString(1,lpChange.getNew_pressure().equals("")?lpChange.getOld_pressure():lpChange.getNew_pressure());
					stmt.setString(2,reading.getCustomer_id());
					stmt.execute();
					stmt = conn.prepareStatement(updateCusConnection);
					stmt.setString(1,lpChange.getNew_min_load().equals("")?lpChange.getOld_min_load():lpChange.getNew_min_load());
					stmt.setString(2,lpChange.getNew_max_load().equals("")?lpChange.getOld_max_load():lpChange.getNew_max_load());
					stmt.setString(3,reading.getCustomer_id());
					stmt.execute();
				
				

				transactionManager.commit();
				
				response.setMessasge("Successfully Saved Load-Pressure Change Information.");
				response.setResponse(true);
				String cKey="CUSTOMER_INFO_"+lpChange.getCustomer_id();
				CacheUtil.clear(cKey);
				
			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return response;
	 	
	}
	
	public ResponseDTO updateLoadPressurechangeInfo(LoadPressureChangeDTO lpChange,MeterReadingDTO reading)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		MeterReadingService mrService=new MeterReadingService();
		
//		response=validateDisconnInfo(disconn,reading);
//		if(response.isResponse()==false)
//			return response;
						  
		String sqlLPchange=" Update LOAD_PRESSURE_CHANGE " +
							  " Set "+
							  " CHANGE_TYPE=?,OLD_MIN_LOAD=?,OLD_MAX_LOAD=?,NEW_MIN_LOAD=?,NEW_MAX_LOAD=?,OLD_PRESSURE=?, " +
							  " NEW_PRESSURE=?,CHANGED_BY=?,EFFECTIVE_DATE=to_date(?,'dd-MM-YYYY'),REMARKS=?,INSERT_BY=?"+
							  " Where PID =?";
		
		PreparedStatement stmt = null;
			try
			{
				if(reading.getBill_id()==null || reading.getBill_id().equalsIgnoreCase("")){
					stmt=mrService.getStatementForReadingUpdate(conn, reading);
					stmt.execute();
				}
				
				stmt = conn.prepareStatement(sqlLPchange);
				stmt.setString(1,lpChange.getChange_type_str());
				
				stmt.setString(2,lpChange.getOld_min_load());
				stmt.setString(3,lpChange.getOld_max_load());
				
				if(lpChange.getChange_type_str().equalsIgnoreCase("1")){
					stmt.setNull(4,java.sql.Types.NULL);
					stmt.setNull(5,java.sql.Types.NULL);
				}
				else {
					stmt.setString(4,lpChange.getNew_min_load());
					stmt.setString(5,lpChange.getNew_max_load());
				}
				stmt.setString(6,lpChange.getOld_pressure());				
				if(lpChange.getChange_type_str().equalsIgnoreCase("0"))
					stmt.setNull(7, java.sql.Types.NULL);
				else
					stmt.setString(7,lpChange.getNew_pressure());	
				
				
				stmt.setString(8,lpChange.getChange_by());
				stmt.setString(9,lpChange.getEffective_date());
				stmt.setString(10,lpChange.getRemarks());			
				stmt.setString(11,lpChange.getInsert_by());
				stmt.setString(12,lpChange.getPid());
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Updated Load-Pressure Change Information.");
				response.setResponse(true);
				String cKey="CUSTOMER_INFO_"+lpChange.getCustomer_id();
				CacheUtil.clear(cKey);
				
			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return response;
	 	
	}
	
	public ArrayList<LoadPressureChangeDTO> getLoadPressureChangeList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		LoadPressureChangeDTO lpChange=null;
		ArrayList<LoadPressureChangeDTO> lpChangeList=new ArrayList<LoadPressureChangeDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT PID,LPC.CUSTOMER_ID,CPI.FULL_NAME,LPC.METER_ID, CM.METER_SL_NO,CHANGE_TYPE,CHANGED_BY, " +
					    " TO_CHAR (EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,LPC.REMARKS,READING_ID, " +
					    " OLD_MIN_LOAD,OLD_MAX_LOAD,NEW_MIN_LOAD,NEW_MAX_LOAD,OLD_PRESSURE,NEW_PRESSURE,MST_AREA.AREA_ID,MST_AREA.AREA_NAME " +
					    " FROM LOAD_PRESSURE_CHANGE LPC,CUSTOMER_METER CM,CUSTOMER_PERSONAL_INFO CPI,MST_AREA,CUSTOMER " +
					    " WHERE LPC.METER_ID=CM.METER_ID AND CPI.CUSTOMER_ID=LPC.CUSTOMER_ID  "+
					    " AND CUSTOMER.CUSTOMER_ID=CPI.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
					    (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (  SELECT PID,LPC.CUSTOMER_ID,CPI.FULL_NAME,LPC.METER_ID, CM.METER_SL_NO,CHANGE_TYPE,CHANGED_BY, " +
				  	  " TO_CHAR (EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,LPC.REMARKS,READING_ID, " +
				  	  " OLD_MIN_LOAD,OLD_MAX_LOAD,NEW_MIN_LOAD,NEW_MAX_LOAD,OLD_PRESSURE,NEW_PRESSURE,MST_AREA.AREA_ID,MST_AREA.AREA_NAME " +
				  	  " FROM LOAD_PRESSURE_CHANGE LPC,CUSTOMER_METER CM,CUSTOMER_PERSONAL_INFO CPI,MST_AREA,CUSTOMER " +
				  	  " WHERE LPC.METER_ID=CM.METER_ID AND CPI.CUSTOMER_ID=LPC.CUSTOMER_ID  "+
				  	  " AND CUSTOMER.CUSTOMER_ID=CPI.CUSTOMER_ID  AND CUSTOMER.AREA=MST_AREA.AREA_ID   "+
				  	  (whereClause.equalsIgnoreCase("")?"":(" And ( "+whereClause+")"))+" "+orderByQuery+			  	  
				  	  " )tmp1 " +
				  	  " )tmp2   " +
				  	  " Where serial Between ? and ? ";
		   
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
					lpChange=new LoadPressureChangeDTO();
					lpChange.setPid(r.getString("PID"));
					lpChange.setCustomer_id(r.getString("CUSTOMER_ID"));
					lpChange.setMeter_id(r.getString("METER_ID"));
					lpChange.setMeter_sl_no(r.getString("METER_SL_NO"));
					lpChange.setChange_type_str(r.getString("CHANGE_TYPE"));
					lpChange.setChange_type_name(LoadChangeType.values()[r.getInt("CHANGE_TYPE")].getLabel());
					lpChange.setOld_min_load(r.getString("OLD_MIN_LOAD"));
					lpChange.setOld_max_load(r.getString("OLD_MAX_LOAD"));
					lpChange.setNew_min_load(r.getString("NEW_MIN_LOAD"));
					lpChange.setNew_max_load(r.getString("NEW_MAX_LOAD"));
					lpChange.setOld_pressure(r.getString("OLD_PRESSURE"));
					lpChange.setNew_pressure(r.getString("NEW_PRESSURE"));
					lpChange.setChange_by(r.getString("CHANGED_BY"));
					lpChange.setEffective_date(r.getString("EFFECTIVE_DATE"));
					lpChange.setReading_id(r.getString("READING_ID"));
					lpChange.setRemarks(r.getString("REMARKS"));
				
					lpChangeList.add(lpChange);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return lpChangeList;
	}
	
	public LoadPressureChangeDTO getLoadPressureChangeInfo(String lpChange_id,String meter_id)
	{		
		LoadPressureChangeDTO lpChange=null;
		MeterReadingDTO reading=new MeterReadingDTO();
		CustomerDTO customer=new CustomerDTO();
		CustomerService customerService=new CustomerService();
		
		Connection conn = ConnectionManager.getConnection();
		String whereCondition="";
		if(lpChange_id==null)
			whereCondition=" ( Select max(pid) from LOAD_PRESSURE_CHANGE Where Meter_Id=? ) ";
		else
			whereCondition="? ";
		
		String sql=" Select tmp1.*,brm.BILL_ID from  " +
				   " (Select LPC.PID,LPC.CUSTOMER_ID, " +
				   " LPC.METER_ID, CM.METER_SL_NO,CHANGE_TYPE,CHANGED_BY, TO_CHAR (EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE,LPC.REMARKS,LPC.READING_ID, OLD_MIN_LOAD,OLD_MAX_LOAD,NEW_MIN_LOAD,NEW_MAX_LOAD,OLD_PRESSURE,NEW_PRESSURE, " +
				   " MR.METER_ID BILLING_METER_ID,MR.METER_RENT,MR.BILLING_MONTH, MR.BILLING_YEAR,MR.READING_PURPOSE, " +
				   " MR.PREV_READING,to_char(MR.PREV_READING_DATE,'dd-MM-YYYY') PREV_READING_DATE,MR.CURR_READING,to_char(MR.CURR_READING_DATE,'dd-MM-YYYY') CURR_READING_DATE, " +
				   " MR.DIFFERENCE,MR.HHV_NHV,CM.MEASUREMENT_TYPE,MR.TARIFF_ID,MR.RATE,MR.ACTUAL_CONSUMPTION,MR.TOTAL_CONSUMPTION, " +
				   " MR.MIN_LOAD,MR.MAX_LOAD,MR.REMARKS READING_REMARKS,MR.PRESSURE,MR.PRESSURE_FACTOR,MR.TEMPERATURE,MR.TEMPERATURE_FACTOR " +
				   " From LOAD_PRESSURE_CHANGE LPC,CUSTOMER C,CUSTOMER_PERSONAL_INFO CPI,METER_READING MR,CUSTOMER_METER CM " +
				   " Where PID=  " +whereCondition+
				   " And LPC.READING_ID=MR.READING_ID And C.CUSTOMER_ID=CPI.CUSTOMER_ID And  LPC.CUSTOMER_ID=C.CUSTOMER_ID   " +
				   " And CM.CUSTOMER_ID=LPC.CUSTOMER_ID And CM.METER_ID=LPC.METER_ID )tmp1 Left Outer Join BILLING_READING_MAP brm "+
				   " on tmp1.READING_ID=brm.READING_ID";
			
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				
				if(lpChange_id==null)
					stmt.setString(1, meter_id);
				else
					stmt.setString(1, lpChange_id);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					lpChange=new LoadPressureChangeDTO();
					lpChange.setPid(r.getString("PID"));
					lpChange.setCustomer_id(r.getString("CUSTOMER_ID"));
					lpChange.setMeter_id(r.getString("METER_ID"));
					lpChange.setMeter_sl_no(r.getString("METER_SL_NO"));
					lpChange.setChange_type_name(LoadChangeType.values()[r.getInt("CHANGE_TYPE")].getLabel());
					lpChange.setChange_type_str(r.getString("CHANGE_TYPE"));
					lpChange.setOld_min_load(r.getString("OLD_MIN_LOAD"));
					lpChange.setOld_max_load(r.getString("OLD_MAX_LOAD"));
					lpChange.setNew_min_load(r.getString("NEW_MIN_LOAD"));
					lpChange.setNew_max_load(r.getString("NEW_MAX_LOAD"));
					lpChange.setOld_pressure(r.getString("OLD_PRESSURE"));
					lpChange.setNew_pressure(r.getString("NEW_PRESSURE"));
					lpChange.setChange_by(r.getString("CHANGED_BY"));
					lpChange.setChange_by_name(EmployeeService.getEmpNameFromEmpId(r.getString("CHANGED_BY")));
					lpChange.setEffective_date(r.getString("EFFECTIVE_DATE"));					
					lpChange.setReading_id(r.getString("READING_ID"));
					lpChange.setRemarks(r.getString("REMARKS"));
					
					
					customer=customerService.getCustomerInfo(r.getString("CUSTOMER_ID"));
					lpChange.setCustomer(customer);
					
					reading.setReading_id(r.getString("READING_ID"));
				    reading.setMeter_id(r.getString("METER_ID"));
				    reading.setMeter_sl_no(r.getString("METER_SL_NO"));
				    reading.setMeter_rent(r.getFloat("METER_RENT"));
				    reading.setBilling_month(r.getInt("BILLING_MONTH"));
					reading.setBilling_year(r.getInt("BILLING_YEAR"));
				    reading.setReading_purpose_str(r.getString("READING_PURPOSE"));
				    reading.setPrev_reading(r.getLong("PREV_READING"));
				    reading.setPrev_reading_date(r.getString("PREV_READING_DATE"));
				    reading.setCurr_reading(r.getLong("CURR_READING"));
				    reading.setCurr_reading_date(r.getString("CURR_READING_DATE"));
				    reading.setDifference(r.getLong("DIFFERENCE"));
				    reading.setHhv_nhv(r.getFloat("HHV_NHV"));
				    reading.setMeasurement_type_str(r.getString("MEASUREMENT_TYPE"));
				    reading.setMeasurement_type_name(MeterMeasurementType.values()[r.getInt("MEASUREMENT_TYPE")].getLabel());
				    reading.setTariff_id(r.getInt("TARIFF_ID")==0?null:r.getInt("TARIFF_ID"));
				    reading.setRate(r.getFloat("RATE")==0?null:r.getFloat("RATE"));
				    
				    reading.setActual_consumption(r.getFloat("ACTUAL_CONSUMPTION"));
				    reading.setTotal_consumption(r.getFloat("TOTAL_CONSUMPTION"));
				    reading.setMin_load(r.getFloat("MIN_LOAD"));
				    reading.setMax_load(r.getFloat("MAX_LOAD"));
				    reading.setRemarks(r.getString("READING_REMARKS"));
				    reading.setBill_id(r.getString("BILL_ID"));
				    
				    reading.setPressure(r.getFloat("PRESSURE"));
				    reading.setPressure_factor(r.getFloat("PRESSURE_FACTOR"));
				    reading.setTemperature(r.getFloat("TEMPERATURE"));
				    reading.setTemperature_factor(r.getFloat("TEMPERATURE_FACTOR"));
				    
				    lpChange.setReading(reading);
					
				   
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
	

	 		return lpChange;
	 	
	}
	public ResponseDTO deleteLoadPressureChangeInfo(String change_id)
	{		
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();

		String sqlReading="Select * from LOAD_PRESSURE_CHANGE  Where PID=?";						  
		String sqlDisconnect="Delete LOAD_PRESSURE_CHANGE Where PID=?";
		String sqlUpdate=" Update CUSTOMER_METER set status=1 where customer_id=? and meter_ID=?";
		String sqlDeleteReading= "Delete METER_READING Where READING_ID=?";
		
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sqlReading);
				stmt.setString(1,change_id);				
				ResultSet r =stmt.executeQuery();
				String meter_id="";
				String customer_id="";
				String reading_id="";
				
				if (r.next())
				{
					meter_id=r.getString("METER_ID");
					customer_id=r.getString("CUSTOMER_ID");
					reading_id=r.getString("READING_ID");
				}
				
				
				stmt = conn.prepareStatement(sqlDisconnect);
				stmt.setString(1,change_id);
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlUpdate);
				stmt.setString(1,customer_id);
				stmt.setString(2,meter_id);
				stmt.execute();
				
				stmt = conn.prepareStatement(sqlDeleteReading);
				stmt.setString(1,reading_id);
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Deleted Load-Pressure Change Information.");
				response.setResponse(true);				
			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return response;
	 	
	}	
	
}

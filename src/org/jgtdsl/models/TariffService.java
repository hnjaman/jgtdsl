package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.enums.BurnerCategory;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class TariffService {

	public ArrayList<TariffDTO> getTariffList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		TariffDTO tariff=null;
		ArrayList<TariffDTO> tariffList=new ArrayList<TariffDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(whereClause.contains("customer_category_id"))
		{
			whereClause=whereClause.replace("customer_category_id", "CATEGORY_NAME");
		}
	
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select mt.* from MST_TARIFF mt,MST_CUSTOMER_CATEGORY mcc where mcc.CATEGORY_ID=mt.CUSTOMER_CATEGORY_ID "+(whereClause.equalsIgnoreCase("")?"":(" AND ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select mcc.CATEGORY_NAME,mt.* FROM MST_TARIFF mt,MST_CUSTOMER_CATEGORY mcc where mcc.CATEGORY_ID=mt.CUSTOMER_CATEGORY_ID  "+(whereClause.equalsIgnoreCase("")?"":(" AND ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					tariff=new TariffDTO();
					tariff.setTariff_id(r.getInt("TARIFF_ID"));
					tariff.setTariff_no(r.getString("TARIFF_NO"));
					tariff.setCustomer_category_id(r.getString("CUSTOMER_CATEGORY_ID"));
					tariff.setMeter_status(MeteredStatus.values()[r.getInt("METER_STATUS")]);
					tariff.setStr_meter_status(MeteredStatus.values()[r.getInt("METER_STATUS")].getLabel());					
					
					if(r.getInt("BURNER_CATEGORY")!=0){
					tariff.setBurner_category(BurnerCategory.values()[r.getInt("BURNER_CATEGORY")-1]);
					tariff.setStr_burner_category(BurnerCategory.values()[r.getInt("BURNER_CATEGORY")-1].getLabel());					
					}
					tariff.setPrice(r.getFloat("PRICE"));
					tariff.setDescription(r.getString("DESCRIPTION"));
					tariff.setEntryDate(r.getString("ENTRY_DATE"));			
					tariff.setPb(r.getFloat("PB"));
					tariff.setVat(r.getFloat("VAT"));
					tariff.setSd(r.getFloat("SD"));
					tariff.setPdf(r.getFloat("PDF"));
					tariff.setBapex(r.getFloat("BAPEX"));
					tariff.setWellhead(r.getFloat("WELLHEAD"));
					tariff.setDwellhead(r.getFloat("DWELLHEAD"));
					tariff.setTrasmission(r.getFloat("TRNSMISSION"));
					tariff.setGdfund(r.getFloat("GDFUND"));
					tariff.setDistribution(r.getFloat("DISTRIBUTION"));
					tariff.setAvalue(r.getFloat("AVALUE"));
					tariff.setEffective_from(r.getString("EFFECTIVE_FROM"));
					tariff.setEffective_to(r.getString("EFFECTIVE_TO"));
					
		
					//tariff.setIs_default(r.getInt("IS_DEFAULT"));
					tariffList.add(tariff);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return tariffList;
	}
	public ArrayList<TariffDTO> getTariffList()
	{
		return getTariffList(0, 0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}
	
	public String addTariff(String data)
	{
		Gson gson = new Gson();  
		TariffDTO tariff = gson.fromJson(data, TariffDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Insert Into MST_TARIFF(TARIFF_ID,TARIFF_NO,CUSTOMER_CATEGORY_ID,METER_STATUS,BURNER_CATEGORY,PRICE,PB, VAT, SD, PDF, BAPEX,  WELLHEAD, DWELLHEAD, TRNSMISSION,GDFUND,DISTRIBUTION,AVALUE,EFFECTIVE_FROM,DESCRIPTION,IS_DEFAULT)" +
				" Values(SQN_TARIFFID.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'dd-MM-YYYY'),?,?)";
		int response=0;
		int i=1;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(i++,tariff.getTariff_no());
				stmt.setString(i++,tariff.getCustomer_category_id());
				stmt.setString(i++,tariff.getStr_meter_status());
				stmt.setString(i++,tariff.getStr_burner_category());
				stmt.setFloat(i++,tariff.getPrice());
				stmt.setFloat(i++,tariff.getPb());
				stmt.setFloat(i++,tariff.getVat());
				stmt.setFloat(i++,tariff.getSd());
				stmt.setFloat(i++,tariff.getPdf());
				stmt.setFloat(i++,tariff.getBapex());
				stmt.setFloat(i++,tariff.getWellhead());
				stmt.setFloat(i++,tariff.getDwellhead());
				stmt.setFloat(i++,tariff.getTrasmission());
				stmt.setFloat(i++,tariff.getGdfund());
				stmt.setFloat(i++,tariff.getDistribution());
				stmt.setFloat(i++,tariff.getAvalue());
				stmt.setString(i++,tariff.getEffective_from());
				stmt.setString(i++,tariff.getDescription());
				stmt.setInt(i++,tariff.getIs_default());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX+AC.MST_TARIFF);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_CREATE_ERROR_PREFIX+AC.MST_TARIFF);

	}
	
	public String updateTariff(String data)
	{
		Gson gson = new Gson();  
		TariffDTO tariffDTO = gson.fromJson(data, TariffDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Update MST_TARIFF Set TARIFF_NO=?,CUSTOMER_CATEGORY_ID=?,METER_STATUS=?,PRICE=?,DESCRIPTION=?,IS_DEFAULT=? Where TARIFF_ID=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,tariffDTO.getTariff_no());
				stmt.setString(2,tariffDTO.getCustomer_category_id());
				stmt.setString(3,tariffDTO.getStr_meter_status());
				stmt.setFloat(4,tariffDTO.getPrice());
				stmt.setString(5,tariffDTO.getDescription());
				stmt.setInt(6,tariffDTO.getIs_default());
				stmt.setInt(7,tariffDTO.getTariff_id());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX+AC.MST_TARIFF);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_UPDATE_ERROR_PREFIX+AC.MST_TARIFF);

	}
	
	public String deleteTariff(String tariffId)
	{
		 JSONParser jsonParser = new JSONParser();		
		 JSONObject jsonObject=null;
		 String tId=null;;
		 try{
			 jsonObject= (JSONObject) jsonParser.parse(tariffId);
		 }
		 catch(Exception ex){
			 ex.printStackTrace();
			 return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());			 
		 }
		tId=(String)jsonObject.get("id");
		Connection conn = ConnectionManager.getConnection();
		String sql=" Delete MST_TARIFF Where TARIFF_ID=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,tId);
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		

	 		if(response==1)
		 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX+AC.MST_TARIFF);
		 	else
		 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_DELETE_ERROR_PREFIX+AC.MST_TARIFF);

	}
	
	public String getMeteredGasPrice(String customer_id,String target_date)
	{
		Connection conn = ConnectionManager.getConnection();
		String sql="Select Tariff_Id,Price from CUSTOMER,MST_TARIFF " +
		"Where Customer.CUSTOMER_CATEGORY=Mst_Tariff.CUSTOMER_CATEGORY_ID " +
		"And Customer_Id=? and Meter_Status=1 " +
		"And Effective_From<=to_date(?,'dd-MM-YYYY HH24:MI:SS') " +
		"And (Effective_To is Null or Effective_To>=to_date(?,'dd-MM-YYYY HH24:MI:SS')) ";

		String response="";
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, customer_id);
				stmt.setString(2, target_date);
				stmt.setString(3, target_date);
				r = stmt.executeQuery();
				while (r.next())
				{
					response="{\"tariff_id\":\""+r.getString("TARIFF_ID")+"\",\"price\":\""+r.getString("PRICE")+"\"}";//+"\":\"","price":""}";
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return response;		
	}
	public String getTariffRateForDomestic(String customer_id) {
		Connection conn = ConnectionManager.getConnection();
		String sql = "SELECT (PRICE*DOUBLE_BURNER_QNT_BILLCAL) BILL_AMOUNT  FROM MST_TARIFF,CUSTOMER_CONNECTION Where Meter_Status=0  And Customer_Category_Id='01' "
				+ "AND Burner_Category =2  And EFFECTIVE_TO is Null AND CUSTOMER_id=? "
				+ "ORDER BY Burner_Category asc";

		StringBuffer BurnerPriceForDomesticPVT = new StringBuffer();
		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {

				BurnerPriceForDomesticPVT.append(r.getString("BILL_AMOUNT"));
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return BurnerPriceForDomesticPVT.toString();

	}
	public String getNonMeterGasPrice(String customer_id,String target_date)
	{
		Connection conn = ConnectionManager.getConnection();
		String sql="Select Tariff_Id,Price,Burner_Category from CUSTOMER,MST_TARIFF  " +
		"        Where Customer.CUSTOMER_CATEGORY=Mst_Tariff.CUSTOMER_CATEGORY_ID  " +
		"        And Customer_Id=? and Meter_Status=0 " +
		"        And Effective_From<=to_date(?,'dd-MM-YYYY HH24:MI:SS')  " +
		"        And (Effective_To is Null or Effective_To>=to_date(?,'dd-MM-YYYY HH24:MI:SS')) ";


		String response="";
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, customer_id);
				stmt.setString(2, target_date);
				stmt.setString(3, target_date);
				r = stmt.executeQuery();
				while (r.next())
				{
					
					if(r.getInt("BURNER_CATEGORY")==1)
						response+="\"single_tariff\":{\"tariff_id\":\""+r.getString("TARIFF_ID")+"\",\"price\":\""+r.getString("PRICE")+"\"},";
					else if(r.getInt("BURNER_CATEGORY")==2)
						response+="\"double_tariff\":{\"tariff_id\":\""+r.getString("TARIFF_ID")+"\",\"price\":\""+r.getString("PRICE")+"\"},";
				}
				if(!response.equalsIgnoreCase(""))
					response="{"+response.substring(0, response.length()-1)+"}";
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return response;		
	}	
}

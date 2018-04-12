package org.jgtdsl.models;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.AutoCompleteObject;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerGridDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.MeterTypeDTO;
import org.jgtdsl.dto.MinistryDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.ConnectionType;
import org.jgtdsl.enums.MeteredStatus;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class CustomerService {
	
	public ArrayList<MinistryDTO> getMinistryCategory() throws SQLException{
		MinistryDTO ministryDTO= null;
		ArrayList<MinistryDTO> MinistryList = new ArrayList<MinistryDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql=	" SELECT * "
				    +" FROM MST_MINISTRY mins "
				    +" ORDER BY MINS.MINISTRY_NAME ";
		
		Statement st= conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		while(rs.next()){
			ministryDTO  = new MinistryDTO();
			ministryDTO.setMinistry_id(rs.getString("MINISTRY_ID"));
			ministryDTO.setMinistry_name(rs.getString("MINISTRY_NAME"));			
			MinistryList.add(ministryDTO);
		}		
		
		try{
			
		}catch(Exception E){
			E.printStackTrace();
		}
		
		return MinistryList;
	}

	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		CustomerCategoryDTO category=null;
		ArrayList<CustomerCategoryDTO> categoryList=new ArrayList<CustomerCategoryDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";		
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " Select * from MST_CUSTOMER_CATEGORY  "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( Select * from MST_CUSTOMER_CATEGORY "+(whereClause.equalsIgnoreCase("")?"":("Where ( "+whereClause+")"))+" "+orderByQuery+			  	  
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
					category=new CustomerCategoryDTO();
					category.setCategory_id(r.getString("CATEGORY_ID"));
					category.setCategory_name(r.getString("CATEGORY_NAME"));
					category.setCategory_type(r.getString("CATEGORY_TYPE"));
					category.setDescription(r.getString("DESCRIPTION"));
					category.setStatus(r.getInt("STATUS"));
					categoryList.add(category);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return categoryList;
	}
	
	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList()
	{
		return getCustomerCategoryList(0, 0,Utils.EMPTY_STRING,Utils.EMPTY_STRING,Utils.EMPTY_STRING,0);
	}	
	
	public boolean validateCategoryId(String categoryId)  //Primary Key Validation
	{	
		Connection conn = ConnectionManager.getConnection();
		int total=0;
		boolean response=false;
		String sql="Select count(CATEGORY_ID) TOTAL from MST_CUSTOMER_CATEGORY where CATEGORY_ID=?";

		PreparedStatement stmt = null;
		ResultSet r = null;
		   
		try
			{
				stmt = conn.prepareStatement(sql);
			    stmt.setString(1, categoryId);
			    
				r = stmt.executeQuery();
				if (r.next())
				{
					total=r.getInt("TOTAL");
					if(total>0)
						response=false;
					else
						response=true;
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return response;
	}

	public String addCustomerCategory(String data)
	{
		Gson gson = new Gson();  
		CustomerCategoryDTO ccDTO = gson.fromJson(data, CustomerCategoryDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Insert Into MST_CUSTOMER_CATEGORY Values(?,?,?,?,?)";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,ccDTO.getCategory_id());
				stmt.setString(2,ccDTO.getCategory_name());
				stmt.setString(3,ccDTO.getCategory_type());
				stmt.setString(4,ccDTO.getDescription());
				stmt.setInt(5,ccDTO.getStatus());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_CREATE_OK_PREFIX+AC.MST_CUSTOMER_CATEGORY);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_CREATE_ERROR_PREFIX+AC.MST_CUSTOMER_CATEGORY);

	}
	
	public String deleteCustomerCategory(String categoryId)
	{
		 JSONParser jsonParser = new JSONParser();		
		 JSONObject jsonObject=null;
		 String catId=null;;
		 try{
			 jsonObject= (JSONObject) jsonParser.parse(categoryId);
		 }
		 catch(Exception ex){
			 ex.printStackTrace();
			 return Utils.getJsonString(AC.STATUS_ERROR, ex.getMessage());			 
		 }
		catId=(String)jsonObject.get("id");
		Connection conn = ConnectionManager.getConnection();
		String sql=" Delete MST_CUSTOMER_CATEGORY Where Category_Id=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,catId);
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		

	 		if(response==1)
		 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_DELETE_OK_PREFIX+AC.MST_CUSTOMER_CATEGORY);
		 	else
		 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_DELETE_ERROR_PREFIX+AC.MST_CUSTOMER_CATEGORY);

	}
	public String updateCustomerCategory(String data)
	{
		Gson gson = new Gson();  
		CustomerCategoryDTO ccDTO = gson.fromJson(data, CustomerCategoryDTO.class);  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" Update MST_CUSTOMER_CATEGORY Set Category_Name=?, Category_Type=?,Description=?,Status=? Where Category_id=?";
		int response=0;
		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,ccDTO.getCategory_name());
				stmt.setString(2,ccDTO.getCategory_type());
				stmt.setString(3,ccDTO.getDescription());
				stmt.setInt(4,ccDTO.getStatus());
				stmt.setString(5,ccDTO.getCategory_id());
				
				response = stmt.executeUpdate();
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(response==1)
	 		return Utils.getJsonString(AC.STATUS_OK, AC.MSG_UPDATE_OK_PREFIX+AC.MST_CUSTOMER_CATEGORY);
	 	else
	 		return Utils.getJsonString(AC.STATUS_ERROR, AC.MSG_UPDATE_ERROR_PREFIX+AC.MST_CUSTOMER_CATEGORY);

	}
	
	public static boolean validateCustomerId(String customerId)
	{
		Connection conn = ConnectionManager.getConnection();
		String sql="Select count(*) total from CUSTOMER Where Customer_Id=?";
		int total=0;
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,customerId);
				r = stmt.executeQuery();
				
				if (r.next())
				{
					total=r.getInt("TOTAL");
				}
			} 
			catch (Exception e){e.printStackTrace();
			return false;
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 	if(total>0)
	 		return false;
	 	else
	 		return true;

	}
	
	public static ResponseDTO createNewCustomer(CustomerDTO customer,CustomerPersonalDTO personalInfo,AddressDTO addressInfo)
	{
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String customerSql=" Insert Into CUSTOMER(CUSTOMER_ID,APP_SL_NO,CUSTOMER_CATEGORY,AREA,ZONE,APPLICATION_DATE) Values(?,?,?,?,?,to_date(?,'dd-MM-YYYY'))";
		String personalSql=" Insert Into CUSTOMER_PERSONAL_INFO(CUSTOMER_ID,FULL_NAME,FATHER_NAME,MOTHER_NAME,GENDER,EMAIL,PHONE, " +
						   " MOBILE,FAX,FREEDOM_FIGHTER,NATIONAL_ID,PASSPORT_NO,LICENSE_NUMBER,VAT_REG_NO,PROPRIETOR_NAME,ORGANIZATION_NAME,TIN_NO) " +
						   " Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String addressSql=" Insert Into CUSTOMER_ADDRESS(CUSTOMER_ID,DIVISION,DISTRICT,UPAZILA,ROAD_HOUSE_NO,POST_OFFICE,POST_CODE,ADDRESS_LINE1,ADDRESS_LINE2) " +
						  " Values(?,?,?,?,?,?,?,?,?) ";

		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(customerSql);
				stmt.setString(1,customer.getCustomer_id());
				stmt.setString(2,customer.getApp_sl_no());
				stmt.setString(3,customer.getCustomer_category());
				stmt.setString(4,customer.getArea());
				stmt.setString(5,customer.getZone());		
				stmt.setString(6,customer.getApp_date());	
				stmt.execute();
				stmt = conn.prepareStatement(personalSql);
				stmt.setString(1,customer.getCustomer_id());
				stmt.setString(2,personalInfo.getFull_name());
				stmt.setString(3,personalInfo.getFather_name());
				stmt.setString(4,personalInfo.getMother_name());
				stmt.setString(5,personalInfo.getGender());
				stmt.setString(6,personalInfo.getEmail());
				stmt.setString(7,personalInfo.getPhone());
				stmt.setString(8,personalInfo.getMobile());
				stmt.setString(9,personalInfo.getFax());
				stmt.setString(10,customer.getCustomer_category().equalsIgnoreCase("01")?personalInfo.getFreedom_fighter():"");
				stmt.setString(11,personalInfo.getNational_id());
				stmt.setString(12,personalInfo.getPassport_no());
				stmt.setString(13,personalInfo.getLicense_number());
				stmt.setString(14,personalInfo.getVat_reg_no());							
				stmt.setString(15,personalInfo.getProprietor_name());
				stmt.setString(16,personalInfo.getOrganization_name());
				stmt.setString(17,personalInfo.getTin());
				stmt.execute();
				stmt = conn.prepareStatement(addressSql);
				stmt.setString(1,customer.getCustomer_id());
				stmt.setString(2,addressInfo.getDivision_id());
				stmt.setString(3,addressInfo.getDistrict_id());
				stmt.setString(4,addressInfo.getUpazila_id());
				stmt.setString(5,addressInfo.getRoad_house_no());
				stmt.setString(6,addressInfo.getPost_office());
				stmt.setString(7,addressInfo.getPost_code());
				stmt.setString(8,addressInfo.getAddress_line1());
				stmt.setString(9,addressInfo.getAddress_line2());
				stmt.execute();
				
				transactionManager.commit();
				
				response.setMessasge("Successfully Created a new Customer");
				response.setResponse(true);
				
// saving photo as customer_id.jpg				
		 		String filePath=(String)ServletActionContext.getRequest().getSession().getAttribute("photo_new");
		 		if(filePath!=null){
		 				ServletActionContext.getRequest().getSession().setAttribute("photo_new",null);
		 				try{
		 					String 
		 					root_path=(String) ServletActionContext.getServletContext().getAttribute("PHOTO_DIR")+"customer\\";	
		 					Utils.copyFile(new File(filePath),new File(root_path+customer.getCustomer_id()+".jpg"));
		 				}
		 				catch(Exception ex){ex.printStackTrace();}
		 				
		 			}

			 	
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
	
	

	public CustomerDTO getCustomerInfo(String customer_id)
	{
		String cKey="CUSTOMER_INFO_"+customer_id;
		CustomerDTO customer=null;
		/*customer=(CustomerDTO)CacheUtil.getObjFromCache(cKey);
		if(customer!=null)
			return customer;*/

		
		Connection conn = ConnectionManager.getConnection();
		
		String sql=" Select * From MVIEW_CUSTOMER_INFO Where Customer_Id=? ";
		
		PreparedStatement stmt = null;
		ResultSet r = null;
		CustomerPersonalDTO personalInfo=null;
		AddressDTO addressInfo=null;
		CustomerConnectionDTO connectionInfo=null;
		
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,customer_id);
				r = stmt.executeQuery();
				
				if (r.next())
				{
					customer=new CustomerDTO();
					personalInfo=new CustomerPersonalDTO();
					addressInfo=new  AddressDTO();
					connectionInfo=new CustomerConnectionDTO();
					
					personalInfo.setCustomer_id(r.getString("CUSTOMER_ID"));
					personalInfo.setFull_name(r.getString("FULL_NAME"));
					personalInfo.setCustomer_name(r.getString("FULL_NAME"));
					personalInfo.setFather_name(r.getString("FATHER_NAME"));
					personalInfo.setMother_name(r.getString("MOTHER_NAME"));
					personalInfo.setGender(r.getString("GENDER"));
					personalInfo.setEmail(r.getString("EMAIL"));
					personalInfo.setPhone(r.getString("PHONE"));
					personalInfo.setMobile(r.getString("MOBILE"));
					personalInfo.setFax(r.getString("FAX"));
					personalInfo.setFreedom_fighter(r.getString("FREEDOM_FIGHTER"));
					personalInfo.setNational_id(r.getString("NATIONAL_ID"));
					personalInfo.setPassport_no(r.getString("PASSPORT_NO"));
					personalInfo.setLicense_number(r.getString("LICENSE_NUMBER"));
					personalInfo.setVat_reg_no(r.getString("VAT_REG_NO"));
					personalInfo.setProprietor_name(r.getString("PROPRIETOR_NAME"));
					personalInfo.setOrganization_name(r.getString("ORGANIZATION_NAME"));
					personalInfo.setTin(r.getString("TIN_NO"));
					personalInfo.setMinistry_id(r.getString("MINISTRY_ID"));
					personalInfo.setMinistry_name(r.getString("MINISTRY_NAME"));
					
					if(r.getString("FREEDOM_FIGHTER")==null){
						personalInfo.setFreedom_fighter("N");
					}else{
						personalInfo.setFreedom_fighter(r.getString("FREEDOM_FIGHTER"));
					}
					
					customer.setCustomer_id(r.getString("CUSTOMER_ID"));
					customer.setApp_sl_no(r.getString("APP_SL_NO"));
					customer.setCustomer_category_name(r.getString("CATEGORY_NAME"));
					customer.setArea_name(r.getString("AREA_NAME"));
					customer.setCustomer_category(r.getString("CATEGORY_ID"));
					customer.setArea(r.getString("AREA_ID"));
					customer.setAddress(r.getString("ADDRESS"));
//					customer.setZone(r.getString("ZONE"));
					
					addressInfo.setPost_code(r.getString("POST_CODE"));
					addressInfo.setPost_office(r.getString("POST_OFFICE"));
					addressInfo.setRoad_house_no(r.getString("ROAD_HOUSE_NO"));
					addressInfo.setDivision_id(r.getString("DIVISION_ID"));
					addressInfo.setDistrict_id(r.getString("DIST_ID"));
					addressInfo.setUpazila_id(r.getString("UPAZILA_ID"));
					
					addressInfo.setDivision_name(r.getString("DIVISION_NAME"));
					addressInfo.setDistrict_name(r.getString("DIST_NAME"));
					addressInfo.setUpazila_name(r.getString("UPAZILA_NAME"));
					
					
					
					addressInfo.setAddress_line1(r.getString("ADDRESS_LINE1"));
					addressInfo.setAddress_line2(r.getString("ADDRESS_LINE2"));					
						
					//connectionInfo.setMinistry_id(r.getString("MINISTRY_ID"));
					//connectionInfo.setMinistry_name(r.getString("MINISTRY_NAME"));
					if(r.getString("CONNECTION_TYPE")!=null){
						connectionInfo.setConnection_type(ConnectionType.values()[r.getInt("CONNECTION_TYPE")]);
						connectionInfo.setConnection_type_str(String.valueOf(ConnectionType.values()[r.getInt("CONNECTION_TYPE")].getId()));
						connectionInfo.setConnection_type_name(ConnectionType.values()[r.getInt("CONNECTION_TYPE")].getLabel());
					}
					connectionInfo.setParent_connection(r.getString("PARENT_CONNECTION"));
					connectionInfo.setMin_load(r.getString("MIN_LOAD"));
					connectionInfo.setMax_load(r.getString("MAX_LOAD"));
					connectionInfo.setConnection_date(r.getString("CONNECTION_DATE"));
					if(r.getString("CONNECTION_STATUS")!=null){
					connectionInfo.setStatus(ConnectionStatus.values()[r.getInt("CONNECTION_STATUS")]);
					connectionInfo.setStatus_str(String.valueOf(ConnectionStatus.values()[r.getInt("CONNECTION_STATUS")].getId()));
					connectionInfo.setStatus_name(ConnectionStatus.values()[r.getInt("CONNECTION_STATUS")].getLabel());
					}
					if(r.getString("ISMETERED")!=null){
					connectionInfo.setIsMetered(MeteredStatus.values()[r.getInt("ISMETERED")]);
					connectionInfo.setIsMetered_str(String.valueOf(MeteredStatus.values()[r.getInt("ISMETERED")].getId()));
					connectionInfo.setIsMetered_name(MeteredStatus.values()[r.getInt("ISMETERED")].getLabel());
					}
					connectionInfo.setVat_rebate(r.getDouble("VAT_REBATE"));
					connectionInfo.setHhv_nhv(r.getFloat("HHV_NHV"));
					connectionInfo.setPay_within_wo_sc(r.getInt("PAY_WITHIN_WO_SC"));
					connectionInfo.setPay_within_w_sc(r.getInt("PAY_WITHIN_W_SC"));

					
					customer.setPersonalInfo(personalInfo);	
					customer.setConnectionInfo(connectionInfo);
					customer.setAddressInfo(addressInfo);
					
					CacheUtil.setObjToCache(cKey,customer);
				}
			} 
			catch (Exception e){e.printStackTrace();
			
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return customer;

	}
	
	public CustomerConnectionDTO getDefaultSurchargePayWithin(String customer_cat)
	{	
		
		String cKey="DEFAULT_SURCHARGE_INFO_"+customer_cat;
		CustomerConnectionDTO surchargePayWithinInfo=null;
		surchargePayWithinInfo=(CustomerConnectionDTO)CacheUtil.getObjFromCache(cKey);
		if(surchargePayWithinInfo!=null)
			return surchargePayWithinInfo;
		
		Connection conn = ConnectionManager.getConnection();
		
		String sql= "SELECT * FROM MST_CUSTOMER_CATEGORY where CATEGORY_NAME=?";
		
		
		PreparedStatement stmt = null;
		ResultSet r = null;
			try
			{
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, customer_cat);
				
				r = stmt.executeQuery();
				if (r.next())
				{
					surchargePayWithinInfo=new CustomerConnectionDTO();
					surchargePayWithinInfo.setPay_within_wo_sc_range_default(Integer.valueOf(r.getString("WITHOUT_SURCHARGE")));
					surchargePayWithinInfo.setPay_within_w_sc_range_default(Integer.valueOf(r.getString("WITH_SURCHARGE")));
					CacheUtil.setObjToCache(cKey,surchargePayWithinInfo);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		 return surchargePayWithinInfo;	 	
	}
	
	public ArrayList<CustomerGridDTO> getNewlyAppliedCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total){
		
		String wClause="";
		if(whereClause!=null && !whereClause.equalsIgnoreCase(""))
			wClause=whereClause+" And CONNECTION_STATUS=2";
		else
			wClause="CONNECTION_STATUS=2";
		
		sortFieldName=" CREATED_ON  ";
		sortOrder=" Desc";
		
		return getCustomerList(index, offset,wClause,sortFieldName,sortOrder,total);
	}
	public ArrayList<CustomerGridDTO> getCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		CustomerGridDTO customer=null;
		ArrayList<CustomerGridDTO> customerList=new ArrayList<CustomerGridDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT * From MVIEW_CUSTOMER_INFO  "+(whereClause.equalsIgnoreCase("")?"":(" Where "+whereClause+" "))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( SELECT * From MVIEW_CUSTOMER_INFO  "+(whereClause.equalsIgnoreCase("")?"":(" Where "+whereClause+" "))+" "+orderByQuery+			  	  
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
					customer=new CustomerGridDTO();
					
					customer.setCustomer_id(r.getString("CUSTOMER_ID"));
					customer.setCategory_id(r.getString("CATEGORY_ID"));
					customer.setFull_name(r.getString("FULL_NAME"));
					customer.setFather_name(r.getString("FATHER_NAME"));
					customer.setArea_name(r.getString("ADDRESS_LINE1"));
					customer.setCategory_name(r.getString("CATEGORY_NAME"));
					customer.setMobile(r.getString("MOBILE"));
					customer.setPhone(r.getString("PHONE"));
					customer.setConnection_status(r.getString("CONNECTION_STATUS"));
					customer.setConnection_status_name(ConnectionStatus.values()[r.getInt("CONNECTION_STATUS")].getLabel());
					customer.setCreated_on(r.getString("CREATED_ON"));

					customerList.add(customer);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return customerList;
	}
	
	public ArrayList<CustomerGridDTO> getMeterCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		CustomerGridDTO customer=null;
		ArrayList<CustomerGridDTO> customerList=new ArrayList<CustomerGridDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = "  Select * from MVIEW_CUSTOMER_INFO "+(whereClause.equalsIgnoreCase("")?"":(" Where "+whereClause+" "))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (Select * from MVIEW_CUSTOMER_INFO   "+(whereClause.equalsIgnoreCase("")?"":(" Where "+whereClause+" "))+" "+orderByQuery+			  	  
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
					customer=new CustomerGridDTO();
					
					customer.setCustomer_id(r.getString("CUSTOMER_ID"));
					customer.setFull_name(r.getString("FULL_NAME"));
					customer.setFather_name(r.getString("FATHER_NAME"));
					customer.setArea_name(r.getString("AREA_NAME"));
					customer.setCategory_name(r.getString("CATEGORY_NAME"));
					customer.setCategory_id(r.getString("CATEGORY_ID"));
					customer.setMobile(r.getString("MOBILE"));
					customer.setConnection_status(r.getString("CONNECTION_STATUS"));
					customer.setConnection_status_name(ConnectionStatus.values()[r.getInt("CONNECTION_STATUS")].getLabel());
					customer.setCreated_on(r.getString("CREATED_ON"));


					customerList.add(customer);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return customerList;
	}
	
	public ArrayList<CustomerGridDTO> getMeteredDisconnCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total){
		String wCaluse="";
		if(whereClause==null || whereClause.equalsIgnoreCase(""))
			wCaluse="  CONNECTION_STATUS=0  And ISMETERED=1 ";
		else
			wCaluse=whereClause+"  And CONNECTION_STATUS=0 And ISMETERED=1  ";
		return getMeterCustomerList(index, offset,wCaluse,sortFieldName,sortOrder,total);
	}
	
	public ArrayList<CustomerGridDTO> getMeteredConnCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total){
		String wCaluse="";
		if(whereClause==null || whereClause.equalsIgnoreCase(""))
			wCaluse="  CONNECTION_STATUS=1  And ISMETERED=1 ";
		else
			wCaluse=whereClause+"  And CONNECTION_STATUS=1  And ISMETERED=1 ";
		return getMeterCustomerList(index, offset,wCaluse,sortFieldName,sortOrder,total);
	}
	
	public ArrayList<CustomerGridDTO> getMeteredCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total){
		String wCaluse="";
		if(whereClause==null || whereClause.equalsIgnoreCase(""))
			wCaluse="  CONNECTION_STATUS!=2 And ISMETERED=1 ";
		else
			wCaluse=whereClause+"  And CONNECTION_STATUS!=2 And ISMETERED=1 ";
		return getMeterCustomerList(index, offset,wCaluse,sortFieldName,sortOrder,total);
	}
	
	public ArrayList<CustomerGridDTO> getNonMeteredCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total,String connectionStatus)
	{
		CustomerGridDTO customer=null;
		ArrayList<CustomerGridDTO> customerList=new ArrayList<CustomerGridDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = " SELECT customer_id,full_name,FATHER_NAME,category_name,area_name,mobile,connection_status,CREATED_ON From MVIEW_CUSTOMER_INFO CUSTOMER Where ISMETERED=0   " +
				  	      (whereClause.equalsIgnoreCase("")?"":(" And ( "+"customer."+whereClause+")"))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " ( SELECT customer_id,full_name,FATHER_NAME,category_name,area_name,mobile,connection_status,CREATED_ON From MVIEW_CUSTOMER_INFO CUSTOMER Where ISMETERED=0 "+
				  	    (whereClause.equalsIgnoreCase("")?"":(" And ( "+"customer."+whereClause+")"))+" "+orderByQuery+			  	  
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
					customer=new CustomerGridDTO();
					
					customer.setCustomer_id(r.getString("CUSTOMER_ID"));
					customer.setFull_name(r.getString("FULL_NAME"));
					customer.setFather_name(r.getString("FATHER_NAME"));
					customer.setArea_name(r.getString("AREA_NAME"));
					customer.setCategory_name(r.getString("CATEGORY_NAME"));
					customer.setMobile(r.getString("MOBILE"));
					customer.setConnection_status(r.getString("CONNECTION_STATUS"));
					customer.setConnection_status_name(ConnectionStatus.values()[r.getInt("CONNECTION_STATUS")].getLabel());
					customer.setCreated_on(r.getString("CREATED_ON"));


					customerList.add(customer);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return customerList;
	}
	
	public ArrayList<CustomerGridDTO> getNonMeteredDisconnCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total){
		
		return getNonMeteredCustomerList(index, offset,whereClause,sortFieldName,sortOrder,total," And status=0");
	}
	
	public ArrayList<CustomerGridDTO> getNonMeteredConnCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total){
		
		return getNonMeteredCustomerList(index, offset,whereClause,sortFieldName,sortOrder,total," And status=1");
	}
	
	public ArrayList<CustomerGridDTO> getNonMeteredCustomerList(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total){
		
		return getNonMeteredCustomerList(index, offset,whereClause,sortFieldName,sortOrder,total,"");
	}
	
	public ArrayList<AutoCompleteObject> getCustomerListForAutoComplete(String type,String area_id)
	{
		ArrayList<AutoCompleteObject> customerList=new ArrayList<AutoCompleteObject>();
		String area_query=" And AREA_ID="+area_id;
		if(area_id!=null && !area_id.equalsIgnoreCase("")){
			
		}
		String sql="";
		if(type.equalsIgnoreCase("ALL"))
			sql = "Select customer_id from MVIEW_CUSTOMER_INFO  Where 1=1 "+area_query+"  order by customer_id";
		
		else if(type.equalsIgnoreCase("METERED"))
			sql = "Select customer_id From MVIEW_CUSTOMER_INFO Where ISMETERED=1 "+area_query+" order by customer_id";
		else if(type.equalsIgnoreCase("METERED_CONNECTED"))
			sql = "Select customer_id From MVIEW_CUSTOMER_INFO Where ISMETERED=1 And CONNECTION_STATUS=1 "+area_query+" order by customer_id";
		else if(type.equalsIgnoreCase("METERED_DISCONNECTED"))
			sql = "Select customer_id From MVIEW_CUSTOMER_INFO Where ISMETERED=1 And CONNECTION_STATUS=0 "+area_query+" order by customer_id";
		
		else if(type.equalsIgnoreCase("NONMETERED"))
			sql = "Select customer_id From MVIEW_CUSTOMER_INFO Where ISMETERED=0 "+area_query+" order by customer_id";
		else if(type.equalsIgnoreCase("NONMETERED_CONNECTED"))
			sql = "Select customer_id From MVIEW_CUSTOMER_INFO Where ISMETERED=0 And CONNECTION_STATUS=1 "+area_query+" order by customer_id";
		else if(type.equalsIgnoreCase("NONMETERED_DISCONNECTED"))
			sql = "Select customer_id From MVIEW_CUSTOMER_INFO Where ISMETERED=0 And CONNECTION_STATUS=0 "+area_query+" order by customer_id";
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet r = null;
		
		try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					customerList.add(new AutoCompleteObject(r.getString("CUSTOMER_ID"), r.getString("CUSTOMER_ID")));
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return customerList;
	}
	public ArrayList<AutoCompleteObject> getCustomerListWithNameForAutoComplete()
	{
		ArrayList<AutoCompleteObject> customerList=new ArrayList<AutoCompleteObject>();
		String sql = "Select customer_id,full_name from CUSTOMER_PERSONAL_INFO order by customer_id";
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmt = null;
		ResultSet r = null;
		
		try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					customerList.add(new AutoCompleteObject( r.getString("CUSTOMER_ID")+" ["+r.getString("FULL_NAME")+"]",r.getString("CUSTOMER_ID")));
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return customerList;
	}
	public ArrayList<CustomerGridDTO> ownershipChangeHistory(int index, int offset,String whereClause,String sortFieldName,String sortOrder,int total)
	{
		CustomerGridDTO customer=null;
		ArrayList<CustomerGridDTO> customerList=new ArrayList<CustomerGridDTO>();
		
		Connection conn = ConnectionManager.getConnection();
		String sql="";
		String orderByQuery="";
		if(sortFieldName!=null && !sortFieldName.equalsIgnoreCase(""))
			orderByQuery=" ORDER BY "+sortFieldName+" " +sortOrder+" ";
		if(total==0)
				  sql = "  Select history.*,AREA_NAME,CATEGORY_NAME,CATEGORY_ID,to_char(INSERTED_ON,'dd-MM-YYYY') INSERTED_ON_EXT from OWNERSHIP_HISTORY history,MVIEW_CUSTOMER_INFO mvc "+(whereClause.equalsIgnoreCase("")?"":(" Where   "+whereClause+" And mvc.customer_id=history.customer_id  "))+" ";
		else
				  sql=" Select * from ( " +
				  	  " Select rownum serial,tmp1.* from " +
				  	  " (Select history.*,AREA_NAME,CATEGORY_NAME,CATEGORY_ID,to_char(INSERTED_ON,'dd-MM-YYYY') INSERTED_ON_EXT from OWNERSHIP_HISTORY history ,MVIEW_CUSTOMER_INFO mvc   "+(whereClause.equalsIgnoreCase("")?"":(" Where "+whereClause+" And mvc.customer_id=history.customer_id  "))+" "+orderByQuery+			  	  
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
					customer=new CustomerGridDTO();
					
					customer.setCustomer_id(r.getString("CUSTOMER_ID"));
					customer.setFull_name(r.getString("FULL_NAME"));
					customer.setFather_name(r.getString("FATHER_NAME"));
					customer.setArea_name(r.getString("AREA_NAME"));
					customer.setCategory_name(r.getString("CATEGORY_NAME"));
					customer.setCategory_id(r.getString("CATEGORY_ID"));
					customer.setMobile(r.getString("MOBILE"));
					customer.setCreated_on(r.getString("INSERTED_ON_EXT"));

					customerList.add(customer);
				}
			} 
			catch (Exception e){e.printStackTrace();}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		
		
		return customerList;
	}
	
	public static ResponseDTO updateCustomerInfo(CustomerDTO customer,CustomerPersonalDTO personalInfo,AddressDTO addressInfo)
	{  	
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String personalInfoSql=" Update CUSTOMER_PERSONAL_INFO Set PROPRIETOR_NAME=?,ORGANIZATION_NAME=?FULL_NAME=?,FATHER_NAME=?,MOTHER_NAME=?,GENDER=?,FREEDOM_FIGHTER=?, " +
				"EMAIL=?,MOBILE=?,PHONE=?,FAX=?,NATIONAL_ID=?,PASSPORT_NO=?,LICENSE_NUMBER=?,VAT_REG_NO=? Where customer_id=?";
		String addressSql="Update CUSTOMER_ADDRESS Set Division=?,District=?,Upazila=?,Road_House_No=?,Post_Office=?,Post_Code=?,Address_Line1=?,Address_Line2=? Where Customer_Id=?";
		
		PreparedStatement stmt = null;
		try
		{
			stmt = conn.prepareStatement(personalInfoSql);
			stmt.setString(1,customer.getCustomer_id());
			stmt.setString(2,personalInfo.getProprietor_name());
			stmt.setString(3,personalInfo.getOrganization_name());
			stmt.setString(4,personalInfo.getFull_name());
			stmt.setString(5,personalInfo.getFather_name());
			stmt.setString(6,personalInfo.getMother_name());
			stmt.setString(7,personalInfo.getGender());
			stmt.setString(8,personalInfo.getFreedom_fighter());
			stmt.setString(9,personalInfo.getEmail());
			stmt.setString(10,personalInfo.getMobile());
			stmt.setString(11,personalInfo.getPhone());
			stmt.setString(12,personalInfo.getFax());
			stmt.setString(13,personalInfo.getNational_id());
			stmt.setString(14,personalInfo.getPassport_no());
			stmt.setString(15,personalInfo.getLicense_number());			
			stmt.setString(16,personalInfo.getVat_reg_no());
			stmt.setString(17,customer.getCustomer_id());
			
			stmt.execute();
			stmt = conn.prepareStatement(addressSql);
			stmt.setString(1,addressInfo.getDivision_id());
			stmt.setString(2,addressInfo.getDistrict_id());
			stmt.setString(3,addressInfo.getUpazila_id());
			stmt.setString(4,addressInfo.getRoad_house_no());
			stmt.setString(5,addressInfo.getPost_office());
			stmt.setString(6,addressInfo.getPost_code());
			stmt.setString(7,addressInfo.getAddress_line1());
			stmt.setString(8,addressInfo.getAddress_line2());
			stmt.setString(9,customer.getCustomer_id());
			stmt.executeUpdate();
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Updated Customer Information.");
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
	
	
	public static ResponseDTO updateOwnerShipInfo(CustomerDTO customer,CustomerPersonalDTO personalInfo)
	{  	
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String ownershipBackup=" Insert Into OWNERSHIP_HISTORY  " +
				" Select cpi.*,'' INSERTED_BY,sysdate from CUSTOMER_PERSONAL_INFO cpi Where Customer_Id=?";
		String newOwnershipInfo=" Update CUSTOMER_PERSONAL_INFO Set FULL_NAME=?,FATHER_NAME=?,MOTHER_NAME=?,GENDER=?,MOBILE=?,PHONE=?,FAX=?,NATIONAL_ID=?,PASSPORT_NO=?,EMAIL=?,TIN_NO=?,ORGANIZATION_NAME=? Where customer_id=?";
		PreparedStatement stmt = null;
		try
		{
			stmt = conn.prepareStatement(ownershipBackup);
			stmt.setString(1,customer.getCustomer_id());
			stmt.execute();
			stmt = conn.prepareStatement(newOwnershipInfo);
			stmt.setString(1,personalInfo.getFull_name());
			stmt.setString(2,personalInfo.getFather_name());
			stmt.setString(3,personalInfo.getMother_name());
			stmt.setString(4,personalInfo.getGender());
			stmt.setString(5,personalInfo.getMobile());
			stmt.setString(6,personalInfo.getPhone());
			stmt.setString(7,personalInfo.getFax());
			stmt.setString(8,personalInfo.getNational_id());
			stmt.setString(9,personalInfo.getPassport_no());
			//inserted on sept 19 ~Prince
			stmt.setString(10,personalInfo.getEmail());
			stmt.setString(11,personalInfo.getTin());
			stmt.setString(12,personalInfo.getOrganization_name());
			//
			stmt.setString(13,customer.getCustomer_id());
			stmt.executeUpdate();
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Updated Ownership Information.");
			response.setResponse(true);
			
			
			
			
			// saving photo as customer_id.jpg				
	 		String filePath=(String)ServletActionContext.getRequest().getSession().getAttribute("photo_new");
	 		if(filePath!=null){
	 				ServletActionContext.getRequest().getSession().setAttribute("photo_new",null);
	 				try{
	 					String 
	 					root_path=(String) ServletActionContext.getServletContext().getAttribute("PHOTO_DIR")+"customer\\";	
	 					Utils.copyFile(new File(filePath),new File(root_path+customer.getCustomer_id()+".jpg"));
	 				}
	 				catch(Exception ex){ex.printStackTrace();}
	 				
	 			}
			
			
			
			

		 	
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
	
	public static ResponseDTO updateCustomerInformation(CustomerDTO customer,CustomerPersonalDTO personalInfo,AddressDTO address)
	{  	
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		String personalInfoSql=" Update CUSTOMER_PERSONAL_INFO Set FULL_NAME=?, FATHER_NAME=?, MOTHER_NAME=?, " +
							   " GENDER=?, EMAIL=?, PHONE=?, MOBILE=?, FAX=?, NATIONAL_ID=?, PASSPORT_NO=?, LICENSE_NUMBER=?,VAT_REG_NO=?,FREEDOM_FIGHTER=? Where CUSTOMER_ID=?";
		String personalInfoSqlforMinistry="update CUSTOMER_CONNECTION set MINISTRY_ID=? Where CUSTOMER_ID=?";
		String addressInfoSql=" Update CUSTOMER_ADDRESS Set DIVISION=?,DISTRICT=?,UPAZILA=?,ROAD_HOUSE_NO=?,POST_OFFICE=?,POST_CODE=?,ADDRESS_LINE1=?,ADDRESS_LINE2=?,ZONE_ID=? Where Customer_Id=?";
		PreparedStatement stmt = null;
		
		
		try
		{
			
			stmt = conn.prepareStatement(personalInfoSql);
			stmt.setString(1,personalInfo.getFull_name());
			stmt.setString(2,personalInfo.getFather_name());
			stmt.setString(3,personalInfo.getMother_name());
			stmt.setString(4,personalInfo.getGender());
			stmt.setString(5,personalInfo.getEmail());
			stmt.setString(6,personalInfo.getPhone());
			stmt.setString(7,personalInfo.getMobile());
			stmt.setString(8,personalInfo.getFax());
			stmt.setString(9,personalInfo.getNational_id());
			stmt.setString(10,personalInfo.getPassport_no());
			stmt.setString(11,personalInfo.getLicense_number());
			stmt.setString(12,personalInfo.getVat_reg_no());
			stmt.setString(13,personalInfo.getFreedom_fighter());
			stmt.setString(14,customer.getCustomer_id());
			stmt.execute();
			
			stmt = conn.prepareStatement(personalInfoSqlforMinistry);
			stmt.setString(1, personalInfo.getMinistry_id());
			stmt.setString(2,customer.getCustomer_id());
			stmt.execute();
			
			stmt = conn.prepareStatement(addressInfoSql);
			stmt.setString(1,address.getDivision_id());
			stmt.setString(2,address.getDistrict_id());
			stmt.setString(3,address.getUpazila_id());
			stmt.setString(4,address.getRoad_house_no());
			stmt.setString(5,address.getPost_office());
			stmt.setString(6,address.getPost_code());
			stmt.setString(7,address.getAddress_line1());
			stmt.setString(8,address.getAddress_line2());
			stmt.setString(9,address.getZone_id());
			stmt.setString(10,customer.getCustomer_id());			
			stmt.execute();			
			
			transactionManager.commit();
			
			response.setMessasge("Successfully Updated Customer Information.");
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
	
	public String getNextId(String data)
	{  	
		Connection conn = ConnectionManager.getConnection();
		String sql=" select lpad(max(to_number(CATEGORY_ID))+1,2,'0') ID from MST_CUSTOMER_CATEGORY";
		String response="";
		PreparedStatement stmt = null;
		ResultSet r = null;
			
			try
			{
				stmt = conn.prepareStatement(sql);
				r = stmt.executeQuery();
				while (r.next())
				{
					response=r.getString("ID");
				}
			} 
			catch (Exception e){e.printStackTrace();
			return Utils.getJsonString(AC.STATUS_ERROR, e.getMessage());
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
	 		
	 		return Utils.getJsonString(AC.STATUS_OK, response);

	}

}

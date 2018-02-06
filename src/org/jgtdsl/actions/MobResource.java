package org.jgtdsl.actions;


import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;


import org.apache.struts2.StrutsStatics;

import oracle.jdbc.driver.OracleCallableStatement;


import org.jgtdsl.dto.APIData;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.MobBillDTO;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.CustomerService;

import org.json.simple.JSONValue;
import org.json.JSONObject;



import org.json.JSONException;
import org.json.JSONObject;
import org.jgtdsl.utils.connection.ConnectionManager;


public class MobResource extends BaseAction{
	
	private static final long serialVersionUID = 1793004616091761918L;
	private String customerId;
	private double paidAmount;
	private String bankName;
	private String transactionId;
	private String mobileNo;
	
	private String userName;
	private String password;
	
	
	

	private static int SUCCESS=1;
	 private static int DUPLICATEAPPLCANT=9;
	 private static int MobileAccountMismatch=100;
	
	 
//	 private static List<BankDTO> bank= new ArrayList<BankDTO>( Arrays.asList(
//			 new BankDTO("10247010","GP"),
//			 new BankDTO("10247011","ROCKET")
//			 ));
	
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.0");
	
	ArrayList<MobBillDTO> mobBilDTO=new ArrayList<MobBillDTO>();
	
	
	public String getBillInfo()
	{

		CustomerService customerService=new CustomerService();
		CustomerDTO customer=customerService.getCustomerInfo(customerId);		
		BillingService billingService=new BillingService();		
		MobBillDTO mob = new MobBillDTO();	//ok	
		
		mob= billingService.getMobBillInfo(customerId);

		 
		
		
		 Map<String, Object> map = new HashMap<String, Object>();			/////
		 
		 //V put(K key, V value);
		 
		 // CustomerDTO/CustomerPersonalDTO/getFull_name
		 map.put("customerName", customer.getPersonalInfo().getFull_name());
		
		 // BillingService/getMobBillInfo
		 map.put("monYear", mob.getMonyear());
		 map.put("billAmount", mob.getBill_amount());
		 map.put("surcharge", mob.getSurcharge());
		 map.put("billCount", mob.getBillcount());
		 
		 map.put("payableAmount", mob.getBill_amount()+mob.getSurcharge());
		 
		 
	
		 try{
   	    	 response.setContentType("json");
   	    	 response.getWriter().write(JSONValue.toJSONString(map));
   	          }
   	        catch(Exception e) {e.printStackTrace();}
		
	
		return null;
	}
	
	
	
	
	
	public String postPayInfo()
	{
		CustomerService customerService=new CustomerService();
		CustomerDTO customer=customerService.getCustomerInfo(customerId);
		BillingService billingService=new BillingService();	
		MobBillDTO mob = new MobBillDTO();	//ok	
		mob= billingService.getMobBillInfo(customerId);
		//APIData scd = new APIData();
		
		String ipAddress = request.getHeader("X-Forwarded-For") == null ? request.getRemoteAddr() : request.getHeader("X-Forwarded-For");
		
		JSONObject jso=null;

		String ip=ipAddressDTO.getxForward();
		String via=ipAddressDTO.getVia();
		String ra=ipAddressDTO.getRemoteAddress();
		
			
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String statusCode = "";
		String status = "";
		String description="";
		
		
		
		if(customer==null)
		{
			statusCode ="9001";
			status = "Failed";
			description="Invalid Customer Id";
		}
		else
		{
			Connection conn = ConnectionManager.getConnection();
			OracleCallableStatement stmt = null;
			try {
				
				 stmt = (OracleCallableStatement) conn.prepareCall("{ call Save_MPG_Bill(?,?,?,?,?,?,?,?,?,?,?)  }");
		         
				 stmt.setString(1, customerId); 
				 stmt.setString(2, bankName); 
				 stmt.setString(3, transactionId); 
				 stmt.setString(4, mobileNo); 
				 stmt.setDouble(5, paidAmount); 
				 stmt.setString(6, userName); 
				 stmt.setString(7, password); 
				 stmt.setString(8, ipAddress); 
				 stmt.registerOutParameter(9, java.sql.Types.VARCHAR);	//oStatusCode
				 stmt.registerOutParameter(10, java.sql.Types.VARCHAR);	//oStatus
				 stmt.registerOutParameter(11, java.sql.Types.VARCHAR);	// oDescription
				 stmt.executeUpdate();
				 
				statusCode =stmt.getString(9);
				status = stmt.getString(10);
				description=stmt.getString(11);
				
			}
			catch (Exception e){e.printStackTrace();
					statusCode ="505";
					status = "Failed";
					description="Internal Error";
			}
	 		finally{try{stmt.close();ConnectionManager.closeConnection(conn);} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}
		}
		
		 map.put("statusCode", statusCode);
		 map.put("status", status);
		 map.put("description", description);
		 //map.put("description", description+" IP:"+ip+" VIA:"+via+" RA:"+ra+" IPADD:"+ipAddress);
		
	/*String str=null;
		

		 if(this.customerId==null){
			 jso=scd.billIdMissing();
	        }
	        else if(mobileNo==null){
	        	jso= scd.InvalidParameter();
	        }
	        else if(customerId.length()!=9 || mobileNo.length()!=11){
	        	jso= scd.InvalidParameter();
	        }
	        else if(paidAmount<mob.getBill_amount()+mob.getSurcharge()) 
	        	jso= scd.amountMismatch();
	        
//	        if(this.mobileNo.startsWith("880")){
//	            this.mobileNo= this.mobileNo.substring(2) ;
//	        }else if(!this.mobileNo.startsWith("0")){
//	            this.mobileNo="0"+this.mobileNo;
//	        }
	        int res=0;
	        
	        // checkvalidity, res contain status of validation
	        
//	        DBConnector db = new DBConnector();
//	        int res = db.checkValidity(Integer.valueOf(this.applicantID), this.mobileNo);
//	        
	        if(res ==0 || res==99){
	        	jso= scd.invalidBillID();
	        }else if(res == 1){
	            
	            // update application
	            
	            // if successful then return, otherwise dont return now
	        	jso= scd.successful();
	        }else if(res == DUPLICATEAPPLCANT){
	        	jso= scd.duplicateApplicantID();
	        }else if(res == MobileAccountMismatch){
	        	jso= scd.MobileAccountMismatch();
	        }
	        //else if(this.app.)
	        
	        // check invalid applicant 
	        // check duplicate applicant
	        // check mobile+account mismatch
	        else
	        	jso= scd.unsuccessful();
		
	        try{
	   	    	 response.setContentType("json");
	   	    	 response.getWriter().write(jso.toString());
	   	          }
	   	        catch(Exception e) {e.printStackTrace();}
			
		*/
		 
		 try{
   	    	 response.setContentType("json");
   	    	 response.getWriter().write(JSONValue.toJSONString(map));
   	          }
   	        catch(Exception e) {e.printStackTrace();}
		 
		 
		 
		 
			return null;
		
		
		
	}
	
	
	
	/*
	 * 
	 * 
	 * 
	 *
	public String mobGetResponse()
	{
		HttpServletRequest req = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		JSONObject o = new JSONObject(req.getParameter("content"));
		
		 try {        
	            JSONObject apiData = new JSONObject(content);
	            APIData scd = new APIData();
	        
	           // boolean succesfulParse=scd.ConstructAPIDataFromJson(apiData);
	
	            if (!scd.ConstructAPIDataFromJson(apiData)) {
	                String re=scd.ParameterMissing().toString();	                
	                System.out.println(re);
	                return re;
	            }
	            
	            String statusResult = scd.process().toString();	            
	            System.out.println(statusResult);
	            return statusResult;
	            
	            
		 } catch (Exception ex) {
	            //log.info("Exception: " + ex);
	            System.out.println(ex);
	        }
	
		return null;
		
	}
*/

	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public double getPaidAmount() {
		return paidAmount;
	}


	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}





	public String getMobileNo() {
		return mobileNo;
	}





	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}





	public String getUserName() {
		return userName;
	}





	public void setUserName(String userName) {
		this.userName = userName;
	}





	public String getPassword() {
		return password;
	}





	public void setPassword(String password) {
		this.password = password;
	}


	

	
	
	

}

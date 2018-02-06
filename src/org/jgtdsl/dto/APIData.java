package org.jgtdsl.dto;
import org.json.JSONException;
import org.json.JSONObject;

public class APIData {

   /** mandatory **/
    
   
    String customerID;
    String billID;
    double amount;
    String TXNID;
    String mobileNo;
    
    /** mandatory **/
    
    /** optional **/
    String userName;
    String pass;
    /** optional **/
    
    /** our response **/
    String statusCode;
    String status;
    String description;
    /** our response **/
    
    private static String ORGName="";
    private static int SUCCESS=1;
    private static int DUPLICATEAPPLCANT=9;
    private static int MobileAccountMismatch=100;
    private static int AMOUNTTOBEPAID=500;
    
    
    public APIData(){
    
    }
  
    
	public APIData(String customerID, Integer amount, String TXNID) {
		this.customerID = customerID;
		this.TXNID = TXNID;
		// this.accountNo=accountNo;
		this.amount = amount;
		// this.organizationName=organizationName;
	}

	public APIData(String customerID, Integer amount, String billID,
			String TXNID) {
		this.customerID = customerID;
		this.TXNID = TXNID;
		this.billID = billID;
		this.amount = amount;
		// this.organizationName=organizationName;
	}

	public APIData(String organizationName, String applicantID,
			String accountNo, Integer amount, String monthStart,
			String monthEnd, String userName, String pass, String TXNID) {
		this.customerID = applicantID;
		this.billID = accountNo;
		this.TXNID = TXNID;
		this.amount = amount;

		this.userName = userName;
		this.pass = pass;
		// this.organizationName=organizationName;
	}

	public boolean ConstructAPIDataFromJson(JSONObject js) {
		System.out.println(js);
		try {

			this.customerID = js.getString("customerID");
			this.billID = js.getString("billID");
			this.amount = js.getDouble("PayableAmount");
			this.TXNID = js.getString("TransactionID");
			// this.organizationName=js.getString("OrganizationName");

			if (js.has("UserName")) {
				this.userName = js.getString("UserName");
			}
			if (js.has("Passwd")) {
				this.pass = js.getString("Passwd");
			}
			if (js.has("MobileNo")) {
				this.mobileNo = js.getString("MobileNo");
			}

			return true;
		} catch (JSONException ex) {
			ex.printStackTrace();
			return false;
		}

	}

	public JSONObject successful() throws JSONException {
		this.statusCode = "200";
		this.status = "Success";
		this.description = "Payment Done";
		return constructJson();
	}

	public JSONObject billIdMissing() throws JSONException {
		this.statusCode = "9003";
		this.status = "Failed";
		this.description = "Bill ID missing";
		return constructJson();
	}

	public JSONObject invalidBillID() throws JSONException {
		this.statusCode = "9004";
		this.status = "Failed";
		this.description = "Invalid Bill ID";
		return constructJson();
	}

	public JSONObject duplicateApplicantID() throws JSONException {
		this.statusCode = "9005";
		this.status = "Failed";
		this.description = "Bill already Paid";
		return constructJson();
	}

	public JSONObject amountMismatch() throws JSONException {
		this.statusCode = "9006";
		this.status = "Failed";
		this.description = "Paid amount is less than required";
		return constructJson();
	}

	public JSONObject organizationMismatch() throws JSONException {
		this.statusCode = "9007";
		this.status = "Failed";
		this.description = "Organization name mismatch";
		return constructJson();
	}

	public JSONObject ParameterMissing() throws JSONException {
		this.statusCode = "9000";
		this.status = "Failed";
		this.description = "Parameter missing";
		return constructJson();
	}

	public JSONObject MobileAccountMismatch() throws JSONException {
		this.statusCode = "9008";
		this.status = "Failed";
		this.description = "Mobile No and Account ID mismatch";
		return constructJson();
	}

	public JSONObject InvalidParameter() throws JSONException {
		this.statusCode = "9009";
		this.status = "Failed";
		this.description = "One or more parameter invalid";
		return constructJson();
	}
	
	public JSONObject unsuccessful() throws JSONException {
		this.statusCode = "505";
		this.status = "Failed";
		this.description = "Internal Error";
		return constructJson();
	}
	

	JSONObject constructJson() throws JSONException {
		JSONObject jsonData = new JSONObject();
		// jsonData.put("UserName",this.userName);
		// jsonData.put("PassWd",this.pass);
		// jsonData.put("OrganizationName",this.organizationName);
		//jsonData.put("BillID", this.billID);
		// jsonData.put("customerId",this.customerId);
		// jsonData.put("MonthStart",this.monthStart);
		// jsonData.put("MonthEnd",this.monthEnd);
		//jsonData.put("CollectedAmount", String.valueOf(this.amount));
		//jsonData.put("CustomerID", this.customerID);
		//jsonData.put("TransactionID", this.TXNID);

		jsonData.put("StatusCode", this.statusCode);
		jsonData.put("Status", this.status);
		jsonData.put("Description", this.description);
		jsonData.put("MobileNo", this.mobileNo);
		return jsonData;
	}

	
	public JSONObject process(){
	        if(this.billID==null){
	            return billIdMissing();
	        }
	        else if(mobileNo==null){
	            return InvalidParameter();
	        }
	        else if(customerID.length()!=9 || mobileNo.length()!=11){
	            return InvalidParameter();
	        }
	        else if(this.amount<AMOUNTTOBEPAID) return amountMismatch();
	        
	        if(this.mobileNo.startsWith("880")){
	            this.mobileNo= this.mobileNo.substring(2) ;
	        }else if(!this.mobileNo.startsWith("0")){
	            this.mobileNo="0"+this.mobileNo;
	        }
	        int res=0;
	        
	        // checkvalidity, res contain status of validation
	        
//	        DBConnector db = new DBConnector();
//	        int res = db.checkValidity(Integer.valueOf(this.applicantID), this.mobileNo);
//	        
	        if(res ==0 || res==99){
	            return invalidBillID();
	        }else if(res == SUCCESS){
	            
	            // update application
	            
	            // if successful then return, otherwise dont return now
	            return successful();
	        }else if(res == DUPLICATEAPPLCANT){
	            return duplicateApplicantID();
	        }else if(res == MobileAccountMismatch){
	            return MobileAccountMismatch();
	        }
	        //else if(this.app.)
	        
	        // check invalid applicant 
	        // check duplicate applicant
	        // check mobile+account mismatch
	        return unsuccessful();
	        
	        
	        
	        
	        
	    }
	
	
	
	
}

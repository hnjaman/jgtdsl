
var jsEnum = { //not really an enum, just an object that serves a similar purpose
		GRID_RECORED_FETCHER :"gridRecordFetcher",
		ACCOUNT_SERVICE : "org.jgtdsl.models.AccountService",
		ACCOUNT_LIST : "getAccountList",
  		ACCOUNT_ADD : "addAccount",
  		ACCOUNT_UPDATE : "updateAccount",
  		ACCOUNT_DELETE : "deleteAccount",  
  		AREA_SERVICE : "org.jgtdsl.models.AreaService",
  		AREA_LIST : "getAreaList",
  		AREA_UPDATE : "updateArea",
  		AREA_ADD : "addArea",
  		AREA_DELETE : "deleteArea",
  		BANK_SERVICE : "org.jgtdsl.models.BankBranchService",
  		BANK_LIST : "getBankList",
  		BANK_UPDATE : "updateBank",
  		BANK_ADD : "addBank",
  		BANK_DELETE : "deleteBank",
  		BRANCH_LIST : "getBranchList", 
  		BRANCH_ADD : "addBranch",
  		BRANCH_UPDATE : "updateBranch",
  		BRANCH_DELETE : "deleteBranch",
  		CRUD_ACTION : "crudOperations",
  		COLLECTION_SERVICE : "org.jgtdsl.models.CollectionService",
  		ADJUSTMENT_COLLECTION_SERVICE : "org.jgtdsl.models.AdjustmentCollectionService",
  		COLLECTION_LIST : "getCollectionList",
  		ADVANCED_COLLECTION_LIST: "getAdvancedCollectionList",
  		DISCONNECTION_SERVICE: "org.jgtdsl.models.DisconnectionService",
  		METER_DISCONNECTION_LIST: "getMeterDisconnectionList",
  		NONMETER_DISCONNECTION_LIST: "getNonMeterDisconnectionList",
  		RECONNECTION_SERVICE: "org.jgtdsl.models.ReconnectionService",
  		METER_RECONNECTION_LIST: "getMeterReconnectionList",
  		NON_METER_RECONNECTION_LIST: "getNonMeterReconnectionList",  		
  		CRUD_ACTION : "crudOperations",
  		NEXT_ID_ACTION: "crudOperations",
  		NEXT_ID_METHOD: "getNextId",
  		NEXT_ID_BANK: "getNextBankId",
  		NEXT_ID_BRANCH: "getNextBranchId",
  		CUSTOMER_SERVICE : "org.jgtdsl.models.CustomerService",
  		CUSTOMER_CATEGORY_LIST : "getCustomerCategoryList",
  		CUSTOMER_CATEGORY_UPDATE : "updateCustomerCategory",
  		CUSTOMER_CATEGORY_ADD : "addCustomerCategory",
  		CUSTOMER_CATEGORY_DELETE : "deleteCustomerCategory",
  		DEPOSIT_SERVICE : "org.jgtdsl.models.DepositService",  
  		SD_EXP_LIST : "getCandidatesForBankGuaranteeExpire",
  		DEPOSIT_TYPE_LIST : "getDepositTypeList",
  		DEPOSIT_TYPE_ADD : "addDepositType",
  		DEPOSIT_TYPE_UPDATE : "updateDepositType",
  		DEPOSIT_TYPE_DELETE : "deleteDepositType",  
  		FORM_BOTTOM_INFO : "Fields marked with (*) are required",  
  		METER_SERVICE : "org.jgtdsl.models.MeterService", 
  		METER_LIST:"getMeterList",
  		APPLIANCE_LIST:"getCustomerApplianceList",
  		DISCONNECED_METER_LIST:"getDisconnectedMeterList",
  		METER_TYPE_LIST : "getMeterTypeList",
  		METER_TYPE_ADD : "addMeterType",
  		METER_TYPE_UPDATE : "updateMeterType",
  		METER_TYPE_DELETE : "deleteMeterType",
  		METER_READING_SERVICE : "org.jgtdsl.models.MeterReadingService",
  		METER_READING_LIST: "getSavedReadingList",
  		METER_READING_HISTORY_LIST:"getReadingHistoryList",
  		METER_RENT_SERVICE:"org.jgtdsl.models.MeterRentService",
  		METER_RENT_CHANGE_LIST: "getMeterRentChangeList",
  		BG_EXPIRE_CHANGE_LIST: "getBGExpireChangHistory",
  		TYPE_CHANGE_SERVICE:"org.jgtdsl.models.CustomerTypeChangeService",
  		TYPE_CHANGE_LIST: "getCustomerTypeChangeList",
  		READ_ONLY : "readonly",
  		STAR : "&nbsp;<font color='red'>*</font>",
  		TARIFF_SERVICE : "org.jgtdsl.models.TariffService",
  		TARIFF_LIST : "getTariffList",
  		TARIFF_UPDATE : "updateTariff",
  		TARIFF_ADD : "addTariff",
  		TARIFF_DELETE : "deleteTariff",
  		CUSTOMER_SERVICE : "org.jgtdsl.models.CustomerService",
  		CUSTOMER_LIST : "getCustomerList",
  		NEWLY_APPLIED_CUSTOMER_LIST : "getNewlyAppliedCustomerList",
  		METERED_CUSTOMER_LIST : "getMeteredCustomerList",
  		METER_DISCONNECTED_CUSTOMER_LIST : "getMeteredDisconnCustomerList",
  		METER_CONNECTED_CUSTOMER_LIST : "getMeteredConnCustomerList",
  		NON_METERED_CUSTOMER_LIST : "getNonMeteredCustomerList",
  		NON_METERED_CONNECTED_CUSTOMER_LIST : "getNonMeteredConnCustomerList",
  		NON_METERED_DISCONNECTED_CUSTOMER_LIST : "getNonMeteredDisconnCustomerList",
  		
  		LOAD_PRESSURE_CHANGE_SERVICE: "org.jgtdsl.models.LoadPressureChangeService",
  		LOAD_PRESSURE_CHANGE_LIST: "getLoadPressureChangeList",
  		  		
  		OWNERSHIP_CHANGE_HISTORY_LIST: "ownershipChangeHistory",
  		
  		BURNER_QNT_CHANGE_SERVICE : "org.jgtdsl.models.BurnerQntChangeService",
  		BURNER_QNT_CHANGE_LIST: "getBurnerQntChangeList",
  		BANK_GARANTIE_EXPIRE_LIST: "getBankGarantieExpireInfo",
  		
  		METER_REPLACEMENT_SERVICE:"org.jgtdsl.models.MeterReplacementService",
  		METER_REPLACEMENT_LIST: "getMeterReplacementList",
  		
  		USER_SERVICE : "org.jgtdsl.models.UserService",
  		USER_ADD : "addUser",
  		USER_UPDATE : "updateUser",
  		USER_DELETE : "deleteUser",
  		USER_LIST : "getUserList",
  		VALIDATE_USER:"validateUserId",
  		SUPPLYOFF_SERVICE : "org.jgtdsl.models.SupplyOffService",
  		SUPPLYOFF_LIST : "getSupplyOffList",
  	    
		BANK_TRANSACTION_SERVICE : "org.jgtdsl.models.BankTransactionService",
  		UNAUTH_TRANSACTION_LIST : "getUnAuthorizedTransactions",
  		AUTH_TRANSACTION_LIST : "getAuthorizedTransactions",
  	    COLLECTION_HISTORY: "getCollectionHistoryByDate",
  	    ADVANCED_HISTORY: "getAdvancedBillingInfo"
  		
  		
  	  		
};

var sBox = { //Select Box Data Fetcher Actions
		USER_ROLE:"fetchAllRoleTypes",
		CUSTOMER_CATEGORY:"fetchCustomerCategory",
		BANK:"fetchBanks",
		ALL_BANK:"fetchAllBanks",		
		BRANCH:"fetchBranches",
		ZONES:"fetchZones",
		BANK_ACCOUNT:"fetchAccounts",
		MOHOLLAH:"fetchMohollahs",
		BANK_ACCOUNT_TYPE:"fetchBankAccountTypes",
		DESIGNATION:"fetchDesignations",
		AREA:"fetchAreas",
		ZONE:"fetchZone",
		ORG_DIVISION:"fetchOrgDivisions",
		ORG_DEPARTMENT:"fetchOrgDepartments",
		ORG_SECTION:"fetchOrgSections",
		DIVISION:"fetchDivision",
		DISTRICT:"fetchDistrict",
		UPAZILA:"fetchUpazila",
		CUSTOMER_LIST:"fetchCustomerIdList.action?type=all",
		CUSTOMER_LIST_WITH_NAME:"fetchCustomerIdList.action?type=id_name",
  		METERED_CUSTOMER_LIST : "fetchCustomerIdList.action?type=metered",
  		METERED_CONNECTE_CUSTOMER_LIST : "fetchCustomerIdList.action?type=metered_conneced",
  		METERED_DISCONNECTED_CUSTOMER_LIST : "fetchCustomerIdList.action?type=metered_disconnected",
  		NON_METERED_CUSTOMER_LIST : "fetchCustomerIdList.action?type=nonmetered",
  		NON_METERED_CONNECTED_CUSTOMER_LIST : "fetchCustomerIdList.action?type=nonmetered_connected",
  		NON_METERED_DISCONNECTED_CUSTOMER_LIST : "fetchCustomerIdList.action?type=nonmetered_disconnected",
		NDF:"No Data Found"
}



var jsCaption= {		
		AREA_CENTER: "C",
		CAPTION_AREA: "Master Data : AREA/REGION",
		CAPTION_ACCOUNT: "Master Data : Bank Account Information",
		CAPTION_BANK: "Master Data : Bank Information",
		CAPTION_BURNER_CHANGE: "Burner Change Form",
		CAPTION_BRANCH: "Master Data : Branch Information",
		CAPTION_CUSTOMER_CATEGORY: "Master Data : jgtdsl Customer Category",
		CAPTION_DISCONNECTION: "Disconnection",
		CAPTION_NEW_CUSTOMER: "Add New Customer",
		CAPTION_DASHBOARD: "User Dashboard",
		CAPTION_METER_TYPE: "Master Data : Meter Type",
		CAPTION_TARIFF: "Master Data : Tariff Information",
		CAPTION_DEPOSIT_TYPE: "Master Data : Deposit Type",
		CAPTION_DEMAND_NOTE: "Demand Note",
		CAPTION_EMPTY_CONTENT: "Content Area",
		CAPTION_CONN_MANAGEMENT: "Connection Management",
		CAPTION_NEW_METER: "New Meter",
		CAPTION_DISCONNECTION: "Disconnection",
		CAPTION_LOAD_CHANGE:"Load Change",
		CAPTION_CUSTOMER_LIST: "jgtdsl Customer List",
		CAPTION_METER_READING_INDIVIDUAL: "Individual Meter Reading",
		CAPTION_BILLING_HOME: "Billing Home",
		CAPTION_USER:"User List",
		CAPTION_PASSWORD_CHANGE:"Change User Password"
}

var jqCaption= {
		
		ADD_ACCOUNT:"Add New Bank Account",
		ADD_AREA:"Add New Area",
		ADD_BANK:"Add New Bank",
		ADD_BRANCH:"Add New Branch",
		ADD_CUSTOMER_CATEGORY:"Add New Category",
		ADD_DEPOSIT_TYPE:"Add New Deposit Type",
		ADD_METER_TYPE:"Add New Meter Type",
		ADD_TARIFF:"Add New Tariff",
		ADD_USER:"Add New User",
		
		EDIT_ACCOUNT:"Edit Bank Account Information",
		EDIT_AREA:"Edit Area Information",
		EDIT_BANK:"Edit Bank Information",
		EDIT_BRANCH:"Edit Branch Information",
		EDIT_CUSTOMER_CATEGORY:"Edit Category Information",
		EDIT_DEPOSIT_TYPE:"Edit Deposit Type",
		EDIT_METER_TYPE:"Edit Meter Type",
		EDIT_TARIFF:"Edit Tariff",
		EDIT_USER:"Edit User Information",

		
		VIEW_ACCOUNT:"View Bank Account Information",
		VIEW_AREA:"View Area/Region",
		VIEW_BANK:"View Bank Information",
		VIEW_BRANCH:"View Branch Information",
		VIEW_CUSTOMER_CATEGORY:"View Category",
		VIEW_DEPOSIT_TYPE:"View Deposit Type",
		VIEW_METER_TYPE:"View Meter Type",
		VIEW_TARIFF:"View Tariff",
		VIEW_USER:"View User",
		
		LIST_ACCOUNT:"jgtdsl Meter Type",
		LIST_AREA:"jgtdsl Area/Region",
		LIST_BANK:"Bank List",
		LIST_BRANCH:"Branch List",
		LIST_CUSTOMER:"Customer List",
		LIST_CUSTOMER_CATEGORY:"jgtdsl Customer Category",		
		LIST_DEPOSIT_TYPE:"jgtdsl Deposit Type",
		LIST_METER_TYPE:"jgtdsl Meter Type",
		LIST_TARIFF:"Tariff/Rate",
		LIST_USER:"jgtdsl User List",
		LIST_SD_EXPIRE:"Security Deposit Expire List",
		UNAUTHORIZED_TRANSACTION:"Unauthorized Transaction List",
				
		DELETE_CONFIRMATION:"Delete Confirmation",
		INFORMATION:"Information"
		
}


var jsImg= {		
		AVATAR_80:"<img class='img-avatar' src='/jgtdsl_WEB/resources/images/avatar.png' alt='' >",
		SPINNER : "<img src='/jgtdsl_WEB/resources/images/loading/spinner.gif' alt='loading...' />",
		LOADING : "<img src='/jgtdsl_WEB/resources/images/loading/loading.gif' alt='loading...' />",
		SETTING : "<img src='/jgtdsl_WEB/resources/images/loading/setting.gif' alt='loading...' />",
		LOADING_MID : "<img src='/jgtdsl_WEB/resources/images/loading/loading_mid.gif' alt='loading...' />"
}

var jsVar={
		MANDATOR : "<font color='red' style='font-weight:bold;font-size:16px;'>&nbsp;&nbsp;*</font>",
		GH_DEDUCT:87,/*Grid Height Deduct */
		GW_DEDUCT:5,/*Grid Width Deduct */
		ROW:20,
		CRUD_WINDOW_WIDTH:600,/* For JQ Grid Add,Edit,View Window */
		SEARCH_WINDOW_WIDTH:500,/* For JQ Grid Search Window */
		PAGER_COMBO:[5,10,15,20,25,40,50],
		USER_PHOTO_URL:"http://localhost/jgtdsl_PHOTO/user/",
		CUSTOMER_PHOTO_URL:"http://localhost/jgtdsl_PHOTO/customer/",
		NO_PHOTO_URL:"http://localhost/jgtdsl_PHOTO/noPhoto.jpg",
		DEFAULT_PHOTO_URL:"http://localhost/jgtdsl_PHOTO/noPhoto.jpg",
		CURR_TIME:(new Date()).getTime(),
		P_FACTOR:parseFloat(14.73),
		T_FACTOR1:parseFloat(288.71),
		T_FACTOR2:parseFloat(273),
		OK:"OK",
		ERROR:"ERROR",
		EMPTY:"",
		USER_DEFAULT_PASSWORD:"12345",
		USER_PASSWORD_MASK:"xxxxxxxx",
		LOCAL_ACTION_STORAGE:"jgtdsl_action",
		MODAL_THEME:'black'
			
}

var msgDiv="<div style='color: red;min-height: 200px;padding: 20px;'> "+
	"<center>"+
		"<img src='/jgtdsl_WEB/resources/images/info.png' width='80' height='80'/><br/><br/>"+
		"<div id='msg_span'>MESSAGE</div>"+
	"</center>"+
"</div>";

var rBox="<div class='w-box-header'>"+
"<h4>Select an Option</h4>"+
"</div>"+
"<div class='w-box-content' style='padding: 10px;' id='content_div'>"+
	 "<center>"+
	 "<img src='/jgtdsl_WEB/resources/images/select.jpg' style='padding: 60px 0 60px 0;'/>"+                         
	 "</center>"+
"</div>"+
"</div>";


// Init Item For Select Box *Select Role*

var sBoxInit={
		BANK_LIST: ":Select Bank;",
		BRANCH_LIST: ":Select Branch;",
		ROLE_LIST: ":Select Role;",
		AREA_LIST: ":Select Area;",
		DIVISION_LIST: ":Select Division;",
		DEPARTMENT_LIST: ":Select Department;",
		SECTION_LIST: ":Select Section;",
		DISTRICT_LIST: ":Select District;",
		UPAZILA_LIST: ":Select Upazila;",
		DESIGNATION_LIST:":Select Designation;",
		BANK_ACCOUNT_TYPE_LIST:":Select Account Type;",
		CUSTOMER_CATEGORY:":Select Customer Category;"
			
}
var jsMsg={
		ERROR_NEXT_ID : "Error in getting next available Id."
}


var theme={
		smoothness:"silver",
		cupertino:"lightblue",
		redmond:"lightblue",
		uiLightness:"lightblue",
		flick:"silver"
}

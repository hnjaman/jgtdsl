autoSelect("area_id","category_id");

var branch_sbox = { targetElm :"branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var account_sbox = { targetElm :"account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
var baseBanks ="";

var allBanks={ "": "Select Bank", "01": "AGRANI BANK" , "02": "BASIC BANK LIMITED" , "03": "IFIC BANK LTD." , "04": "JANATA BANK" , "05": "RUPALI BANK LTD." , "06": "SOCIAL INVESTMENT BANK LTD." , "07": "SOCIAL ISLAMI BANK" , "08": "National Bank Ltd" , "09": "IFIC MOBILE BANK LIMITED" };


function showAllBanks(){
	
	$('#bank_id').html('');
	$('#account_id').html('');
	$.each(allBanks, function(key, value) {   
	     $('#bank_id')
	         .append($("<option></option>")
	                    .attr("value",key)
	                    .text(value)); 
	});
	branch_sbox.action_name="fetchBranches.action?type=1";
}

function showAreaBanks(){
	$('#bank_id').html('');
	$('#branch_id').html('');
	$('#account_id').html('');
	
	account_id
	$('#bank_id').html(baseBanks);
	branch_sbox.action_name="fetchBranches.action";
	
}

function getAccountInfo(){ 
	
	var cat=$("#category_id").val();
	var account_sbox_cat= { targetElm :"account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action?type='+cat,data_key:'branch_id'};
	fetchSelectBox(account_sbox_cat);
}


var customerCollectionListUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.ADJUSTMENT_COLLECTION_SERVICE+'&method='+jsEnum.COLLECTION_LIST;
$("#collection_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: customerCollectionListUrl,
   	jsonReader:{
    	repeatitems: false,
        id: "collection_id"
	},
    colNames: ['Dated','Collection Date', 'Bank Name','Account No', 'Bill Month','Bill Amount','Surcharge','Vat Rebate','Adjustment','Tax','Payable','Collection','Customer Id'],
    colModel: [{
	                name: 'collection_date_f1',
	                index: 'collection_date_f1',
	                width:40,
	                align:'center',
	                sorttype: 'date',
	                formatoptions: { srcformat: 'd-MON-Y'}
            	},
            	{
	                name: 'collection_date',
	                index: 'collection_date',
	                hidden : true
            	},
            	{
	                name: 'bank_name',
	                index: 'bank_name',
	                width:80,
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'account_no',
	                index: 'account_no',
	                width:40,
	                sorttype: "string",
	                align:'left',
	                search: true,
            	},
            	{
	                name: 'bill_month_year',
	                index: 'bill_month_year',
	                width:40,
	                align:'left',
	                sorttype: "string",
	                search: true
            	},
            	{
	                name: 'billed_amount',
	                index: 'billed_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'collected_surcharge_amount',
	                index: 'collected_surcharge_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'vat_rebate_amount',
	                index: 'vat_rebate_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	  {
	                name: 'adjustment_amount',
	                index: 'adjustment_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	  {
	                name: 'tax_amount',
	                index: 'tax_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},          	
            
                {
	                name: 'payable_amount',
	                index: 'payable_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'collection_amount',
	                index: 'collection_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'customer_id',
	                index: 'customer_id',
	                hidden:true,
	                search:false
            	}
                      
            	
        ],   	
	height: $("#collection_grid_div").height()-55,
	width: $("#collection_grid_div").width(),
   	pager: '#collection_grid_pager',
   	sortname: 'issue_date',
    sortorder: "desc",    
	caption: "List of Collection Detail",	
	footerrow: true,
	
	onSelectRow: function(id){ 
		var ret = $("#collection_grid").getRowData($("#collection_grid").jqGrid('getGridParam','selrow'));	
		fetchCollectionInfo(ret.customer_id,id);
    },	   	
	loadComplete: function () {
    	calculateFooterSum();
	},
	rowattr: function (rd) {
        if (rd.collection_amount != rd.payable_amount) {
            return {"class": "redBackgroundRow"};
        }
        
    }
}));

jQuery("#collection_grid").jqGrid('navGrid','#collection_grid_pager',footerButton,{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("collection_grid",["bank_name","account_no","bill_month_year"],"left");

// blockGrid("collection_grid");

function calculateFooterSum(){
	var billed_amount = jQuery("#collection_grid").jqGrid('getCol', 'billed_amount', false, 'sum');
	var surcharge_amount = jQuery("#collection_grid").jqGrid('getCol', 'surcharge_amount', false, 'sum');
	var tax_amount = jQuery("#collection_grid").jqGrid('getCol', 'tax_amount', false, 'sum');
	var vat_rebate_amount = jQuery("#collection_grid").jqGrid('getCol', 'vat_rebate_amount', false, 'sum');
	var payable_amount = jQuery("#collection_grid").jqGrid('getCol', 'payable_amount', false, 'sum');
	var collection_amount = jQuery("#collection_grid").jqGrid('getCol', 'collection_amount', false, 'sum');
	var adjustment_amount = jQuery("#collection_grid").jqGrid('getCol', 'adjustment_amount', false, 'sum');
	
	jQuery("#collection_grid").jqGrid('footerData','set', {collection_date_f1: 'Total:  '+$("#collection_grid").getGridParam("reccount"),billed_amount:billed_amount,
	surcharge_amount:surcharge_amount,vat_rebate_amount:vat_rebate_amount,adjustment_amount:adjustment_amount,tax_amount:tax_amount,payable_amount:payable_amount,collection_amount:collection_amount});
}




var getCollectionHistorybyDate=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.ADJUSTMENT_COLLECTION_SERVICE+'&method='+jsEnum.COLLECTION_HISTORY;// this will be used
var transactionUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.ADJUSTMENT_COLLECTION_SERVICE+'&method='+jsEnum.UNAUTH_TRANSACTION_LIST;
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url:transactionUrl,
   	jsonReader: {
		repeatitems: false,
		id: "customer_id"
	},
    colNames: ['Customer ID','Month/Year', 'Collection Amount'],
    colModel: [{
					name: 'customer_id',
					index: 'customer_id',
					width:80,
					align:'center',
					sorttype: 'string',
					search: true
            	},
            	{
					name: 'bill_month_year',
					index: 'bill_month_year',
					width:80,
					align:'center',
					sorttype: 'string',
					search: true
            	},
            	{
	                name: 'collection_amount',
	                index: 'collection_amount',
	                sorttype: "string",
	                width:80,
	                align:'right',
	                search: false
            	}],   
	height: $("#customer_grid_div").height()-102,
	width: $("#customer_grid_div").width()+10,
   	sortname: 'INSERTED_ON',
    sortorder: "desc",	
	pager: '#customer_grid_pager',
   	caption: "Collection History",
   	footerrow:true,
    userDataOnFooter:true,
	onSelectRow: function(id){ 
		
    },	   	
   	loadComplete: function () {
   		            getTotalCollectionByDateAccount();
                    var sum = $("#total_collection").html();
                    jQuery("#customer_grid").jqGrid('footerData','set', {customer_id:'' ,bill_month_year:'Total:', collection_amount: sum});
                }
}));




jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {}}),{},{},{},{multipleSearch:true});
jQuery("#customer_grid").jqGrid("setLabel","full_name","",{"text-align":"left","padding-left":"10px"});

var g_bankId,g_branchId,g_accountNo,g_collectionDate;
function getCollectionHistoryByDate(){

	g_bankId=$("#bank_id").val();
	g_branchId=$("#branch_id").val();
	g_accountNo=$("#account_id").val();
	g_collectionDate=$("#collection_date").val();
	
	if($("#bank_id").val()=="" || $("#branch_id").val()=="" || $("#account_id").val()==""||$("#collection_dates").val()=="")
		return;
	
	var ruleArray=[["bank_id","branch_id","account_no"],["eq","eq","eq"],[$("#bank_id").val(),$("#branch_id").val(),$("#account_id").val()]];
	

	if($("#collection_date").val()!=""){
		ruleArray[0].push("collection_date");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#collection_date").val());
	}

	var postData=getPostFilter("customer_grid",ruleArray);
	
	$("#customer_grid").jqGrid('setGridParam',{search: true,postData: postData,page:1,datatype:'json'});    	
	getTotalCollectionByDateAccount();
	reloadGrid("customer_grid");	
}


function loadCustomerList(){
	// var ruleArray=[["area_id"],["eq"],[$("#area_id").val()]];
	// var postdata=getPostFilter("customer_grid",ruleArray);
   	$("#customer_grid").jqGrid('setGridParam',{search: false,postData: {},page:1,datatype:'json'});    		
	reloadGrid("customer_grid");
	
}

loadCustomerList();      

function validateAndSaveCollection()
{
	var customer_type=$("#customerType").val();
	
	if(customer_type=="METERED"){
		//alert(customer_type);	
		var actual_billed_amount=$("#payable_amount").val();
		var collected_billed_amount=$("#collected_amount").val();
		if(actual_billed_amount!=collected_billed_amount){
			$amountCheckingConfirmation.dialog('open');
		}else{
			validateAndSaveCollectionFinally();
		}
	}
	else if(customer_type=="NONMETERED"){
		//alert(customer_type);
		var actual_billed_amount=$("#actual_billed_amount").val();
		var collected_billed_amount=$("#collected_amount").val();
		if(actual_billed_amount!=collected_billed_amount){
			$amountCheckingConfirmation.dialog('open');
		}else{
			validateAndSaveCollectionFinally();
		}
	}
	
	//validateAndSaveCollectionFinally();
}

function validateAndSaveCollectionFinally(){
	var isValid=true;
	isValid=validateField("customer_id","bank_id","branch_id","account_id","collected_amount");
	
	var mobilePhoneUpdate=false;
	if($("#mobile").val()!=$("#mobile_old").val() || $("#phone").val()!=$("#phone_old").val())
		mobilePhoneUpdate=true;
	
	//if($("#payable_amount").val()>$("#collection_amount").val() && $("#customerType").val()=="METERED"){
	//	alert("Collection Amount must be equal to Net Payble amount.");
	//	return;
	//}
	
	
	
	if(isValid==true)	 {
	var form = document.getElementById('billCollectionForm');
	disableButton("btn_save");
	var formData = new FormData(form);
	  $.ajax({
	    url: 'saveBillCollectionAdj.action?mobilePhoneUpdate='+mobilePhoneUpdate,
	    type: 'POST',
	    data: formData,
	    async: false,
	    cache: false,
	    contentType: false,
	    processData: false,
	    success: function (response) {
	    if(response.status=="OK")
	    {
	       collectionForm(clearField);		
	       getTotalCollectionByDateAccount();
	       focusNext("customer_code");
	    }
	    	$("#msg_div").html(response.message);
	       // $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    	enableButton("btn_save");	    
	    }
	    
	  });
	
	}
}


var $amountCheckingConfirmation = $('<div id="dialog-confirm"></div>')
.html("<p> "+
 	"Actual Amount And Collected Amount is Not Same.Want to Collect??"+
	"<div id='del_holiday'></div> "+
   "</p>")
.dialog({
		title: 'Amount Mismatch',
		resizable: false,
		autoOpen: false,
		height:150,
		width:450,
		modal: true,
		buttons: {
				"Yes": {text:"Yes","class":'btn btn-danger',click:function() {
					validateAndSaveCollectionFinally();
					$( this ).dialog( "close" );
				}},
				"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
					$( this ).dialog( "close" );
				}},
		}
	});

unbindKeyPress();



$("#collection_date").val(getCurrentDate());
focusNext("bank_id");
function getBillingInfoAgainstMonthYear()
{
	var customer_id=$("#customer_id").val();
	bill_month=$("#bill_month").val();
	bill_year=$("#bill_year").val();
	var collection_date=$("#collection_date").val();
	getBillingInfo(customer_id,bill_month,bill_year,collection_date);
}

function getBillingInfo(customer_id,bill_month,bill_year,collection_date)
{
	
 	$.ajax({
   		  type: 'POST',
   		  url: 'getBillInfo4CollectionAdj.action',
   		  data: { customer_id:customer_id,bill_month:bill_month,bill_year:bill_year,collection_date:collection_date},
   		  success:function(data){   			  	
   			  	var fields = ["customer_name","father_name","address","customerType","phone","mobile","billed_amount","bill_month","bill_year","surcharge_amount","vat_rebate_amount","collection_amount","tax_amount","tax_date","tax_no","remarks","payable_amount"];	
   			  	clearField.apply(this,fields);
   			
   			  	$("#burnerDiv").hide();			
   			  	$("#divBurnerQnt").html("");
   			 
	   			if(typeof data.customer === "undefined"){
	   					$("#msg_div").html("Invalid Customer Code.");	   					
	   					collectionForm(clearField);	
	   			    	collectionForm(disableField);
				    	enableField("customer_id","customer_code");
	   			    	return;
	   			}
   			
   				collectionForm(enableField);
   				
   				$("#msg_div").html("");   				
				$("#customer_name").val(data.customer.personalInfo.full_name);
				//$("#father_name").val(data.customer.personalInfo.father_name);
				$("#mobile").val(data.customer.personalInfo.mobile);
				$("#phone").val(data.customer.personalInfo.phone);
				
				$("#mobile_old").val(data.customer.personalInfo.mobile);
				$("#phone_old").val(data.customer.personalInfo.phone);
				
				
				$("#customerType").val(data.customer.connectionInfo.isMetered);
				$("#isMetered_str").val(data.customer.connectionInfo.isMetered_str);
				
				
				$("#address").val(getCustomerAddress(data.customer.addressInfo));
				
				if(typeof data.bill_id === "undefined"){
			    	$("#msg_div").html("No Pending bill to collect");
			    	$("#actual_billed_amount").val("0");
			    	$("#collected_billed_amount").val("0");
			    	$("#actual_surcharge_amount").val("0");
			    	$("#collected_surcharge_amount").val("0");
			    	$("#actual_payable_amount").val("0");
			    	$("#collected_payable_amount").val("0");
			    	$("#collected_amount").val("0");
			    	$("#surcharge_amount_meter").val();

			    	collectionForm(disableField);
			    	enableField("customer_id","customer_code","bill_month","bill_year");
			    	$("#bill_month").val(bill_month);
			    	$("#bill_year").val(bill_year);
			    	return;
				}
   			
				$("#billed_amount").val(data.billed_amount);
				$("#surcharge_amount").val(data.surcharge_amount);
				$("#adjustment_amount").val(data.adjustment_amount);				
				$("#vat_rebate_amount").val(data.vat_rebate_amount);				
				
				
				if(data.customer.connectionInfo.isMetered=="NONMETERED"){
					hideElement("vat_rebate_div","tax_amount_div","adjustment_div","net_payable_amount","collection_amount_div","metered_bill_amount","metered_surcharge");
					showElement("non_metered_billed_amount","non_metered_surcharge_amount","non_metered_payable_amount","non_metered_payable_amount","non_metered_surcharge_collection_amount");
					$("#actual_billed_amount").val(data.actual_billed_amount);
					$("#collected_billed_amount").val(data.collected_billed_amount);
					$("#actual_surcharge_amount").val(data.actual_surcharge_amount);
					$("#surcharge_per_collection").val(data.surcharge_per_collection);
					$("#collected_surcharge_amount").val(data.collected_surcharge_amount);
					$("#actual_payable_amount").val(data.actual_payable_amount);
					//alert(data.actual_payable_amount);
					$("#collected_payable_amount").val(data.collected_payable_amount);
					$("#surcharge_amount").val(data.actual_surcharge_amount-data.collected_surcharge_amount);
					$("#collected_amount").val(data.actual_billed_amount-data.collected_billed_amount);
					$("#surcharge_amount_meter").val(data.actual_surcharge_amount-data.collected_surcharge_amount);
					
					$("#burnerDiv").show();
					$("#divBurnerQnt").html(data.double_burner_qnt);
					//$("#divBurnerQnt").html(data.customer.connectionInfo.double_burner_qnt_billcal);
					//alert(data.statusId);
					if(data.statusId==2){
						disableField("collected_amount");
					}
					
   		  		}
				else{
					hideElement("non_metered_billed_amount","non_metered_surcharge_amount","non_metered_payable_amount","non_metered_payable_amount","non_metered_surcharge_collection_amount");
					showElement("vat_rebate_div","tax_amount_div","adjustment_div","net_payable_amount","collection_amount_div","metered_bill_amount","metered_surcharge");
					$("#payable_amount").val(data.payable_amount);
					$("#collection_amount").val(data.payable_amount);
					$("#collected_amount").val(data.payable_amount);
					
					$("#burnerDiv").hide();				
				}
				
				$("#bill_month").val(data.bill_month);
				$("#bill_year").val(data.bill_year);
				$("#bill_id").val(data.bill_id);
				
				
				
				focusNext("collected_amount");
				
				if($("#isMetered_str").val()==1)
					disableButton("btn_multi_coll");
				else
					enableButton("btn_multi_coll");
		  },
   		  error:function(){
   		  }
   	});
}
function fetchCollectionInfo(customer_id,collection_id)
{
 	$.ajax({
   		  type: 'POST',
   		  url: 'getCollectionInfoAdj.action?collection.customer_id='+customer_id+'&collection.collection_id='+collection_id,
   		  success:function(data){
			if(typeof data.collection_id === "undefined"){
				alert("Error Occured.");
			}
			else{
				setCollectionInfo(data);
				disableButton("btn_save");
				enableButton("btn_edit","btn_delete");
				getTotalCollectionByDateAccount();
			}
   		  },
   		  error:function(){
   			$("#btn_save").removeAttr("disabled");
   		  }
   	});
}
function setCollectionInfo(data){
	//alert("ifti");
	$("#collection_id").val(data.collection_id);
	$("#customer_id").val(data.customer_id);
	$("#area_id").val(data.customer.area);
	$("#category_id").val(data.customer.customer_category);
	$("#customer_name").val(data.customer.personalInfo.full_name);
	//$("#father_name").val(data.customer.personalInfo.father_name);
	$("#customerType").val(data.customer.connectionInfo.isMetered);
	//alert(data.customer.connectionInfo.isMetered);
	if(data.customer.connectionInfo.isMetered=="NONMETERED"){
		hideElement("vat_rebate_div","tax_amount_div","adjustment_div","net_payable_amount","collection_amount_div","metered_bill_amount","metered_surcharge");
		showElement("non_metered_billed_amount","non_metered_surcharge_amount","non_metered_payable_amount","non_metered_payable_amount","non_metered_surcharge_collection_amount");
		$("#actual_billed_amount").val(data.actual_billed_amount);
		$("#collected_billed_amount").val(data.collected_billed_amount);
		$("#actual_surcharge_amount").val(data.actual_surcharge_amount);
		$("#collected_surcharge_amount").val(data.collected_surcharge_amount);
		$("#surcharge_per_collection").val(data.surcharge_per_collection);
		$("#actual_payable_amount").val(data.actual_payable_amount);		
		$("#collected_payable_amount").val(data.collected_payable_amount);
		$("#surcharge_amount").val(data.actual_surcharge_amount-data.collected_surcharge_amount);
		//$("#collected_amount").val(data.actual_billed_amount-data.collected_billed_amount);
 		}
	else{
		hideElement("non_metered_billed_amount","non_metered_surcharge_amount","non_metered_payable_amount","non_metered_payable_amount","non_metered_surcharge_collection_amount");
		showElement("vat_rebate_div","tax_amount_div","adjustment_div","net_payable_amount","collection_amount_div","metered_bill_amount","metered_surcharge");
		$("#payable_amount").val(data.payable_amount);
		$("#collection_amount").val(data.payable_amount);
		$("#collected_amount").val(data.payable_amount);
		
	}
	
	$("#address").val(data.customer.address);
	$("#mobile").val(data.customer.personalInfo.mobile);
	$("#phone").val(data.customer.personalInfo.phone);
	$("#bank_id").val(data.bank_id);
	
	clearSelectBox("branch_id");
	addOption("branch_id",data.branch_id,data.branch_name);
	autoSelect("branch_id");
	
	clearSelectBox("account_id");
	addOption("account_id",data.account_no,data.account_no);
	autoSelect("account_id");

	$("#bill_month").val(data.bill_month);
	$("#bill_year").val(data.bill_year);
	
	$("#billed_amount").val(data.billed_amount);
	$("#collection_amount").val(data.collection_amount);
	$("#surcharge_amount").val(data.surcharge_amount);
	$("#collection_date").val(data.collection_date);
	$("#vat_rebate_amount").val(data.vat_rebate_amount);
	$("#tax_amount").val(data.tax_amount);
	$("#tax_no").val(data.tax_no);
	$("#tax_date").val(data.tax_date);
	$("#remarks").val(data.remarks);	
	$("#adjustment_amount").val(data.adjustment_amount);
	$("#payable_amount").val(data.payable_amount);
	
	disableField("bank_id","branch_id","account_id","bill_month","bill_year","surcharge_amount","surcharge_amount","vat_rebate_amount",
	"billed_amount","tax_amount","tax_amount","payable_total","collection_date","remarks","collection_amount",
	"area_id","category_id","customer_id","customer_code","customer_name","father_name","address","customerType","phone","mobile","tax_no","tax_date","tax_no");
	
}

function fetchCollectionList(customer_id){

	var ruleArray=[["bill.customer_id"],["eq"],[customer_id]];
	var postdata=getPostFilter("collection_grid",ruleArray);
	$("#collection_grid").jqGrid('setGridParam',{search: true,postData: postdata,url:customerCollectionListUrl,page:1,datatype:'json'}); 
	$("#collection_grid").jqGrid("clearGridData", true);
	$("#collection_grid").trigger("reloadGrid");
	gridColumnHeaderAlignment("collection_grid",["bank_name","account_no","bill_month_year"],"left");
}

function multiDeleteModal() {
	
	var rowId =$("#customer_grid").jqGrid('getGridParam','selrow');
	if(!rowId){
		alert("Please Select a Customer");
		return;
	}	
	var rowData = jQuery("#list").getRowData(rowId);
	var customer_id = rowData['customer_id']; 
	
	var customer_id='';
	$.nsWindow.open({
		title: 'Delete Collection',
		width: 660,
		height: 500,
		dataUrl: 'multiCollectionDeleteAdj.action?customer_id='+customer_id,
		theme:jsVar.MODAL_THEME
    });
}

function multiCollectionModal(){
	$.nsWindow.open({
		movable:true,
		title: 'Multi Month Collection Entry',
		width: 1200,
		height: 700,
		dataUrl: 'multiMonthCollectionAdj.action?customer_id='+$("#customer_id").val()+'&collection_date='+$("#collection_date").val(),
		theme:jsVar.MODAL_THEME
    });  
}
function currentMonthCollection(){
	$.nsWindow.open({
		movable:true,
		title: 'Current Month Collection Entry',
		width: 800,
		height: 230,
		dataUrl: 'currentMonthBillCollectionAdj.action?customer_id='+$("#customer_id").val()+'&collection_date='+$("#collection_date").val(),
		theme:jsVar.MODAL_THEME
    });
    
}
$("#bill_month").val(getCurrentMonth());
$("#bill_year").val(getCurrentYear());

function cancelButtonPressed(){
unBlockGrid("collection_grid");
}
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide(); }
	}));

function collectionForm(plainFieldMethod){
	
	//var fields = ["customer_id","customer_code","mobile","bank_id","branch_id","account_id","billed_amount","bill_month","bill_year","surcharge_amount","collection_date","vat_rebate_amount","collection_amount","tax_amount","remarks","payable_amount","adjustment_amount"];	
	//var fields = ["customer_id","customer_code","mobile","bank_id","branch_id","account_id","billed_amount","bill_month","bill_year","surcharge_amount","collection_date","vat_rebate_amount","collection_amount","tax_amount","remarks","payable_amount","adjustment_amount"];
	var fields = ["customer_id","customer_code","mobile","billed_amount","bill_month","bill_year","surcharge_amount","vat_rebate_amount","collection_amount","tax_amount","tax_date","tax_no","remarks","payable_amount","adjustment_amount","collected_amount","actual_payable_amount","actual_billed_amount","actual_surcharge_amount","surcharge_per_collection","collected_surcharge_amount","collected_billed_amount","collected_payable_amount"];
	
	plainFieldMethod.apply(this,fields);
}

function showShortCuts(){
	var shortcuts="<div style='text-align:left;margin-left:20px;'>"+
	"<b>Show Collection Records:</b> Ctrl+Enter(Return) <br/>"+
	"<b>Fetch Latest Pending Bill(For Collection):</b> Enter Key while the cursor is on the code field< <br/>"+
	"<b>Save Collection Info:</b> Press Enter key while the cursor is on the Collection Amount Field <br/>"+
	"<b>Add New:</b> F1 <br/>"+
	"<b>Load Collection History:</b> F2 <br/>"+
	"</div>";
	
	$.jgrid.info_dialog("Keyboard Shortcuts",shortcuts,jqDialogClose,jqDialogParam);
}


$('#customer_code').keyup(function(e){
    if(e.keyCode == 13)
    {
    	var afterSlashCharCounts = $("#customer_code").val().substr($("#customer_code").val().indexOf("/") + 1).length;
    	if ($("#customer_code").val().indexOf("/") >= 0) {
    		 if (afterSlashCharCounts == 1) {
    		  $("#customer_code").val(lpad($("#customer_code").val(), 7));
    		 } else if (afterSlashCharCounts == 2) {
    		  $("#customer_code").val(lpad($("#customer_code").val(), 8));
    		 } else {
    		  $("#customer_code").val(lpad($("#customer_code").val(), 9));
    		 }

    		} else {
    		 $("#customer_code").val(lpad($("#customer_code").val(), 5));
    		}
    	
        var customer_id=$("#area_id").val()+$("#category_id").val()+$("#customer_code").val();
        $("#customer_id").val(customer_id);
        bill_month="";
    	bill_year="";
    	var collection_date=$("#collection_date").val();
        getBillingInfo(customer_id,bill_month,bill_year,collection_date);
        fetchCollectionList($('#customer_id').val());
		
    }
});

$('#collected_amount').keyup(function(e){
    if(e.keyCode == 13)
    {
    	validateAndSaveCollection();
		
    }
});

$('#remarks').keyup(function(e){
    if(e.keyCode == 13)
    {
    	validateAndSaveCollection();
		
    }
});

$(document).unbind('keydown');$("input").unbind('keydown');
$(document).bind('keydown', 'ctrl+m', function(){multiCollectionModal();});
$("input").bind('keydown', 'ctrl+m', function(){multiCollectionModal();});
$(document).bind('keydown', 'ctrl+c', function(){currentMonthCollection();});
$("input").bind('keydown', 'ctrl+c', function(){currentMonthCollection();});
$(document).bind('keydown', 'ctrl+return', function(){fetchCollectionList($("#customer_id").val());});
$("input").bind('keydown', 'ctrl+return', function(){fetchCollectionList($("#customer_id").val());});

$(document).bind('keydown', 'f1', function(){prepareCollectionForm();});
$("input").bind('keydown', 'f1', function(){prepareCollectionForm();});

$(document).bind('keydown', 'f2', function(){fetchCollectionList($('#customer_id').val());});
$("input").bind('keydown', 'f2', function(){fetchCollectionList($('#customer_id').val());});

$(document).bind('keydown', 'insert', function(){ focusNext("customer_code");});
$("input").bind('keydown', 'insert', function(){ focusNext("customer_code");});

$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){getBillingInfo($("#customer_id").val());},
}));

function prepareCollectionForm(){
	collectionForm(clearField);
	collectionForm(enableField);
	enableField("customer_code","bank_id","branch_id","account_id","collection_date","category_id");
	clearField("customer_code");
	focusNext("customer_code");
	clearField("customer_name","father_name","address","customerType","actual_billed_amount","collected_billed_amount","actual_surcharge_amount","collected_surcharge_amount","actual_payable_amount","collected_payable_amount");
}

var $delete = $('<div id="dialog-confirm"></div>')
.html("<p> "+
 	"Are you sure you want to delete the selected Collection Information? "+
	"<div id='del_holiday'></div> "+
   "</p>")
.dialog({
		title: 'Collection Delete Confirmation',
		resizable: false,
		autoOpen: false,
		height:150,
		width:450,
		modal: true,
		buttons: {
				"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
					deleteCollection();
					$( this ).dialog( "close" );
					//alert("123");
				}},
				"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
					$( this ).dialog( "close" );
				}},
		}
	});

function deleteCollection(){
	if($("#collection_id").val()=="")
	  $("#msg_div").html("<font color='red'>Invalid Request.</font>");
	else{
		$("#btn_delete").hide();

		  $.ajax({
		    url: 'deleteBillingCollectionAdj.action',
		    type: 'POST',
		    data: {"collection.collection_id":$("#collection_id").val(),"collection.customer_id":$("#customer_id").val(),
		    	"collection.actual_billed_amount":$("#actual_billed_amount").val(),"collection.collected_billed_amount":$("#collected_billed_amount").val(),
		    	"collection.actual_surcharge_amount":$("#actual_surcharge_amount").val(),"collection.collected_surcharge_amount":$("#collected_surcharge_amount").val(),
		    	"collection.actual_payable_amount":$("#actual_payable_amount").val(),"collection.collected_payable_amount":$("#collected_payable_amount").val(),"collection.surcharge_per_collection":$("#surcharge_per_collection").val()},
		    cache: false,
		    success: function (response) {
		   // $dialog.dialog("close");
		    //alert(response.status);
		    
		    if(response.status=="OK")
		    {	
		    	//alert("sdh");
		    	$("#btn_delete").show();
		    	//Delete the corresponding record if it is present in the grid....	
		       $('#collection_grid').jqGrid('delRowData',$("#collection_id").val());
		       calculateFooterSum();
		       prepareCollectionForm();
		       clearField("phone");
			   $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
			   enableButton("btn_save");
			   disableButton("btn_edit","btn_delete");
			   
		    }
		    else{
		    	$("#msg_div").html(response.message);
		    }
		    }
		    
		  });
	}
}  
function getTotalCollectionByDateAccount(){

	if($("#collection_date").val()=="" || $("#account_id").val()=="")
		return;
	
	$.ajax({
 		  type: 'POST',
 		  url: 'getTotalCollectionByDateAccountAdj.action?collection.collection_date='+$("#collection_date").val()+"&collection.account_no="+$("#account_id").val(),
 		  success:function(data){
			$("#total_collection").html(data.total_collection);
 		  },
 		  error:function(){
 			$("#btn_save").removeAttr("disabled");
 		  }
 	});
	
	
}
function selectvalue(e){
    e = e || event;

    var key = e.which || e.keyCode;

    if(!e.shiftKey && key >= 48 && key <= 57){
        var option = this.options[key - 48];
        if(option){
            option.selected = "selected";
        }
    }
}

function recalculateCollection(tax_amount){
	
	var cAmount=parseFloat($("#payable_amount").val(),10)-parseFloat(tax_amount,10);
	$("#collected_amount").val(cAmount);
	
}
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide();getBillingInfoAgainstMonthYear();getCollectionHistoryByDate();}}));

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "tax_date",
    trigger    : "tax_date",
    onSelect   : function() { this.hide();}}));




function getSurchargeInfo(){
	 	$.ajax({
	   		  type: 'POST',
	   		  url: 'getCollectionInfoAdj.action?collection_date='+customer_id,
	   		  success:function(data){
				if(typeof data.collection_id === "undefined"){
					alert("Error Occured.");
				}
				else{
					setCollectionInfo(data);
					disableButton("btn_save");
					enableButton("btn_edit","btn_delete");
				}
	   		  },
	   		  error:function(){
	   			$("#btn_save").removeAttr("disabled");
	   		  }
	   	});
}
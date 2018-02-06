autoSelect("area_id","category_id");


var branch_sbox = { targetElm :"branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var account_sbox = { targetElm :"account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
var baseBanks ="";



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
	
	
	$('#bank_id').html(baseBanks);
	branch_sbox.action_name="fetchBranches.action";
	
}

function getAccountInfo(){ 
	
	var cat=$("#category_id").val();
	var account_sbox_cat= { targetElm :"account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action?type='+cat,data_key:'branch_id'};
	fetchSelectBox(account_sbox_cat);
}

									//gridRecordFetcher						//org.jgtdsl.models.CollectionService//getCollectionList
var customerCollectionListUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method='+jsEnum.COLLECTION_LIST;
$("#collection_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: customerCollectionListUrl,
   	jsonReader:{
    	repeatitems: false,
        id: "collection_id"
	},
	colNames: ['Customer Code', 'Customer Name','Bill Month','Bill Year','Gas Bill (TK.)','Surcharge','Payment Date','Bank Code','Status','User Name'],
    colModel: [{
            		name: 'customer_id',
	                index: 'customer_id',
	                width:40,
	                sorttype: 'string',
	                align:'center',
	                search:true,
            	},
            	{
	                name: 'customer_name',
	                index: 'customer_name',
	                width:40,
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'bill_month',
	                index: 'bill_month',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
	                name: 'bill_year',
	                index: 'bill_year',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
	                name: 'collection_amount',
	                index: 'collection_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'surcharge_amount',
	                index: 'surcharge_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
            		name: 'collection_date',
	                index: 'collection_date',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
            		name: 'bank_id',
	                index: 'bank_id',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
            		name: 'status',
	                index: 'status',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
            		name: 'inserted_by',
	                index: 'inserted_by',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
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




var getCollectionHistorybyDate=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method='+jsEnum.COLLECTION_HISTORY;// this will be used
var transactionUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method='+jsEnum.UNAUTH_TRANSACTION_LIST;
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
	
	//alert("hh");
	if($("#collection_date").val()!=""){
		ruleArray[0].push("collection_date");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#collection_date").val());
	}

	var postData=getPostFilter("collection_grid",ruleArray);
	
	$("#collection_grid").jqGrid('setGridParam',{search: true,postData: postData,page:1,datatype:'json'});    	
	$("#collection_grid").jqGrid("clearGridData", true);
	$("#collection_grid").trigger("reloadGrid");
	getTotalCollectionByDateAccount();
	//reloadGrid("collection_grid");	
	
	
	
}


function loadCustomerList(){
	// var ruleArray=[["area_id"],["eq"],[$("#area_id").val()]];
	// var postdata=getPostFilter("customer_grid",ruleArray);
   	$("#customer_grid").jqGrid('setGridParam',{search: false,postData: {},page:1,datatype:'json'});    		
	reloadGrid("customer_grid");
	
}

//loadCustomerList();      

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
			//getCollectionHistoryByDate();
		}
	}
	else if(customer_type=="NONMETERED"){
		//setPendingBillsStr();
		//alert('METERED');	
		//validateAndSaveCollectionNonMeter();

	}
	
	//validateAndSaveCollectionFinally();
}
function validateAndSaveCollectionNonMeter(){
	var isValid=true;
	isValid=validateField("customer_id","bank_id","branch_id","account_id");	
	
	if(isValid==true)	 {
	var form = document.getElementById('billCollectionForm');
	disableButton("btn_save");
	var formData = new FormData(form);
	  $.ajax({
	    url: 'saveMultiMonthCollection.action',
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


function validateAndSaveCollectionFinally(){
	var isValid=true;
	//isValid=validateField("customer_id","bank_id","branch_id","account_id","collected_amount");
	
	
	if($("#collected_amount").val()<= 0){
		cbColor($("#collected_amount"),"e");
		alert("collected Amount can not be Zero or less than Zero");
		isValid=false;
  	}else{
  		cbColor($("#collected_amount"),"v");
  	  isValid=true;
  	  
  	  // 
  	  
	isValid=validateField("customer_id","bank_id","branch_id","account_id");
	
//	why ???????????????
	
	var mobilePhoneUpdate=false;
	if($("#mobile").val()!=$("#mobile_old").val() || $("#phone").val()!=$("#phone_old").val())
		mobilePhoneUpdate=true;
	

	
	if(isValid==true)	 {
	var form = document.getElementById('billCollectionForm');
	disableButton("btn_save");
	var formData = new FormData(form);
	  $.ajax({
	    url: 'saveBillCollection.action?mobilePhoneUpdate='+mobilePhoneUpdate,
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
	       //getTotalCollectionByDateAccount();	
	       getCollectionHistoryByDate();
	       focusNext("customer_code");
	    }
	    	$("#msg_div").html(response.message);
	       // $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    	enableButton("btn_save");	    
	    }
	    
	  });
	
	}
  	  
  	  
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

var pending_bill_id=new Array();
var pending_bill_month=new Array();
var pending_bill_year=new Array();
var pending_bill_actual_bill_amount=new Array();
var pending_bill_collected_bill_amount=new Array();
var pending_bill_actual_surcharge_amount=new Array();
var pending_bill_surcharge_per_collection=new Array();
var pending_bill_collected_surcharge_amount=new Array();
var pending_bill_actual_payable_amount=new Array();
var pending_bill_collected_payable_amount=new Array();
var pending_bill_type=new Array();
var pending_paid_status=new Array();
function clearArray(){
	 pending_bill_id=[];
	 pending_bill_month=[];
	 pending_bill_year=[];
	 pending_bill_actual_bill_amount=[];
	 pending_bill_collected_bill_amount=[];
	 pending_bill_actual_surcharge_amount=[];
	 pending_bill_surcharge_per_collection=[];
	 pending_bill_collected_surcharge_amount=[];
	 pending_bill_actual_payable_amount=[];
	 pending_bill_collected_payable_amount=[];
	 pending_bill_type=[];
	 pending_paid_status=[];
}
$("#pending_grid").GridUnload();
$("#pending_grid").jqGrid({
    data: [],
    datatype: "local",
    colNames: ['Bill Id','Surcharge_per_coll','Bill Month',"Act.Bill","Coll.Bill","Act.Surcharge","Coll.Surcharge","Act Payable","Coll. Payable","Surcharge","Collection Amount",'bill_mon','Bill Type','Paid Status'],
    colModel: [
    	{name:'bill_id',index:'bill_id', hidden:true},
    	{name:'surcharge_per_collection',index:'surcharge_per_collection', hidden:true},
        {name:'bill_month',index:'bill_month', width:100},   	    	
    	{name: 'actual_billed_amount',index: 'actual_billed_amount',align:'right'},
    	{name: 'collected_billed_amount',index: 'collected_billed_amount',align:'right'},
    	{name: 'actual_surcharge_amount',index: 'actual_surcharge_amount',align:'right'},
    	{name: 'collected_surcharge_amount',index: 'collected_surcharge_amount',align:'right'},
    	{name: 'actual_payable_amount',index: 'actual_payable_amount',align:'right'},
    	{name: 'collected_payable_amount',index: 'collected_payable_amount',align:'right'},
    	{name: 'collected_surcharge',index: 'collected_surcharge',align:'right'},
    	{name: 'collection_amount',index: 'collection_amount',align:'right'},
    	
    	{name:'bill_mon',index:'bill_mon', hidden:true},
    	{name: 'bill_type',index: 'bill_type',hidden:true},
    	{name: 'paid_status',index: 'paid_status',hidden:true}
    	
    	],
    pager: '#pending_grid_pager',
    width: 1130,
    height:384,  
    rownumbers: true,
    multiselect: true,
    rowattr: function (rd) {
        if (rd.paid_status === "YTBP") {
        	 return {
                 "class": "ui-state-disabled ui-jqgrid-disablePointerEvents disconnectedMeterRow"
             };
        }
        
    },
    loadComplete: function () {
    	calculatePendingFooterSum();
    	
	},
	footerrow: true,
	onSelectRow : function (rowid, status) {
      var elem = document.activeElement;
      if (elem.id) { 
    	  calculatePendingBillTotal();
    	}
    },
    onSelectAll: function(aRowids, status) {
    	calculatePendingBillTotal();
	}
});
function calculatePendingFooterSum(){
	var sum_actual_billed_amount = jQuery("#pending_grid").jqGrid('getCol', 'actual_billed_amount', false, 'sum');
	var sum_collected_billed_amount = jQuery("#pending_grid").jqGrid('getCol', 'collected_billed_amount', false, 'sum');
	var sum_actual_surcharge_amount = jQuery("#pending_grid").jqGrid('getCol', 'actual_surcharge_amount', false, 'sum');
	var sum_collected_surcharge_amount = jQuery("#pending_grid").jqGrid('getCol', 'collected_surcharge_amount', false, 'sum');
	var sum_actual_payable_amount = jQuery("#pending_grid").jqGrid('getCol', 'actual_payable_amount', false, 'sum');
	var sum_collected_payable_amount = jQuery("#pending_grid").jqGrid('getCol', 'collected_payable_amount', false, 'sum');
	
	jQuery("#pending_grid").jqGrid('footerData','set', {bill_month: 'Total:  '+$("#pending_grid").getGridParam("reccount"),actual_billed_amount:sum_actual_billed_amount,collected_billed_amount:sum_collected_billed_amount,actual_surcharge_amount:sum_actual_surcharge_amount,collected_surcharge_amount:sum_collected_surcharge_amount,actual_payable_amount:sum_actual_payable_amount,collected_payable_amount:sum_collected_payable_amount,collection_amount:"<input type='text'  disabled='disabled' style='text-align:right;width:80px;' id='total_pending_collection_amount'/>",collected_surcharge:"<input type='text'  disabled='disabled' style='text-align:right;width:80px;' id='total_pending_surcharge_amount'/>"});
}
function calculatePendingBillTotal() {
    var grid = $("#pending_grid");
    var rowKey = grid.getGridParam("selrow");
    var selectedIDs = grid.getGridParam("selarrrow");
    //alert(selectedIDs[i]);
    var total_pending_amount=0;
    var total_surcharge_amount=0
    for (var i = 0; i < selectedIDs.length; i++) {
        total_pending_amount += parseFloat($("#pending_"+selectedIDs[i]).val(),10);
        total_surcharge_amount += parseFloat($("#surcharge_"+selectedIDs[i]).val(),10);
	}           
	$("#total_pending_collection_amount").val(total_pending_amount);   
	$("#total_pending_surcharge_amount").val(total_surcharge_amount);     
}
$("#pending_grid_pager").hide();

addCollectionRow();

function addCollectionRow(){
 
 var pending_bill_data="";
 var collected_amount="";
 var collected_surcharge=""; 
 var bill_type="";
 for(var i=0;i<pending_bill_month.length;i++)
	{
	    /* alert(pending_bill_actual_bill_amount[i]);
	    alert(pending_bill_actual_bill_amount[i]); */
		var collection_bill_amount=pending_bill_actual_bill_amount[i]-pending_bill_collected_bill_amount[i];
		var collection_surcharge_amount=pending_bill_actual_surcharge_amount[i]-pending_bill_collected_surcharge_amount[i];
		actual_surcharge_amount="<input type='text' onkeyup='calculatePendingFooterSum()' id='act_surcharge_"+pending_bill_id[i]+"' value='"+pending_bill_actual_surcharge_amount[i]+"' disabled='disabled' style='width:100px;text-align:right;' >";
		surcharge_per_collection="<input type='text' onkeyup='calculatePendingFooterSum()' id='surcharge_per_coll_"+pending_bill_id[i]+"' value='"+pending_bill_surcharge_per_collection[i]+"' disabled='disabled' style='width:100px;text-align:right;' >";
		bill_type="<input type='text' onkeyup='calculatePendingFooterSum()' id='bill_type_"+pending_bill_id[i]+"' value='"+pending_bill_type[i]+"' disabled='disabled' style='width:100px;text-align:right;' >";
		actual_payable_amount="<input type='text' onkeyup='calculatePendingFooterSum()' id='act_payable_"+pending_bill_id[i]+"' value='"+pending_bill_actual_payable_amount[i]+"' disabled='disabled' style='width:100px;text-align:right;' >";
		collected_amount="<input type='text' onkeyup='calculatePendingFooterSum()' id='pending_"+pending_bill_id[i]+"' value='"+collection_bill_amount+"' style='width:102px;text-align:right;' >";
		collected_surcharge="<input type='text' onkeyup='calculatePendingFooterSum()' id='surcharge_"+pending_bill_id[i]+"' value='"+collection_surcharge_amount+"' style='width:102px;text-align:right;' />";
	   
		bill_mon="<input type='hidden' id='bill_month_"+pending_bill_id[i]+"' value='"+pending_bill_month[i]+"#"+pending_bill_year[i]+"'  />";        
		
				        
		pending_bill_data = [{"bill_id":pending_bill_id[i],"surcharge_per_collection":surcharge_per_collection,"bill_month":pending_bill_month[i]+", "+pending_bill_year[i],"actual_billed_amount":pending_bill_actual_bill_amount[i],"collected_billed_amount":pending_bill_collected_bill_amount[i],"actual_surcharge_amount":actual_surcharge_amount,"collected_surcharge_amount":pending_bill_collected_surcharge_amount[i],"actual_payable_amount":actual_payable_amount,"collected_payable_amount":pending_bill_collected_payable_amount[i],"collection_amount":collected_amount,"collected_surcharge":collected_surcharge,"bill_mon":bill_mon,"bill_type":bill_type,"paid_status":pending_paid_status[i]}];
		jQuery("#pending_grid").jqGrid('addRowData',pending_bill_id[i],pending_bill_data[0],"last"); 
	}  
	calculatePendingFooterSum();
}

function setPendingBillsStr(){
	
	$("#pending_bills_str").val("");
	var grid = $("#pending_grid");
    var rowKey = grid.getGridParam("selrow");

    var selectedIDs = grid.getGridParam("selarrrow");
    var pendingBills="";
    var advancedBills="";
    for (var i = 0; i < selectedIDs.length; i++) {
    
    	//alert($("#bill_month_"+selectedIDs[i]).val());
    	var bill_type=$("#bill_type_"+selectedIDs[i]).val();
    	if(bill_type=="R"){
    		pendingBills+=selectedIDs[i]+"#"+parseFloat($("#pending_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#surcharge_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#act_surcharge_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#act_payable_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#surcharge_per_coll_"+selectedIDs[i]).val(),10)+"#"+$("#bill_month_"+selectedIDs[i]).val()+"#"+$("#bill_type_"+selectedIDs[i]).val();
    	    pendingBills=pendingBills+"@";
    	}else if(bill_type=="A"){
    		advancedBills+=$("#bill_month_"+selectedIDs[i]).val()+"#"+parseFloat($("#pending_"+selectedIDs[i]).val(),10);
   	        advancedBills=advancedBills+"@";
    	}
	    
	}        
	//console.log(pendingBills);
	$("#pending_bills_str").val(pendingBills);
	$("#advanced_bills_str").val(advancedBills);
	
}
function getBillingInfo(customer_id,bill_month,bill_year,collection_date)
{
	
	//testing
	$("#billed_amount").val("");
	$("#adjustment_amount").val("");
	$("#surcharge_amount").val("");
	$("#vat_rebate_amount").val("");
	$("#payable_amount").val("");
	$("#collected_amount").val("");
	// end of testing
	// ~ Prince ~Dec 6
	
	
 	$.ajax({
   		  type: 'POST',
   		  url: 'getBillInfo4Collection.action',
   		  data: { customer_id:customer_id,bill_month:bill_month,bill_year:bill_year,collection_date:collection_date},
   		  success:function(data){   			  	
   			  	var fields = ["customer_name","father_name","address","customerType","phone","mobile","billed_amount","bill_month","bill_year","surcharge_amount","vat_rebate_amount","collection_amount","tax_amount","tax_date","tax_no","remarks","payable_amount"];	
   			  	
   			 if( (data[0].customer.personalInfo === "") || (data[0].customer.personalInfo === null)){
   				 alert("abcd");
					$("#msg_div").html("Invalid Customer Code.");
					collectionForm(clearField);	
			    	collectionForm(disableField);
			    	clearField.apply(this,fields);
			    	enableField("customer_id","customer_code");
			    	$('#customer_code').focus();
			    	return;
			}
   			  	
   			  	clearField.apply(this,fields);
   			    clearArray();
   			   
   			for(var i in data)
 			{
    			
    			pending_bill_id[i]=data[i].bill_id;
 				pending_bill_month[i]=data[i].bill_month_name;
 				pending_bill_year[i]=data[i].bill_year;
 				pending_bill_actual_bill_amount[i]=data[i].actual_billed_amount;
 				pending_bill_collected_bill_amount[i]=data[i].collected_billed_amount;
 				pending_bill_actual_surcharge_amount[i]=data[i].actual_surcharge_amount;
 				pending_bill_surcharge_per_collection[i]=data[i].surcharge_per_collection;
 				pending_bill_collected_surcharge_amount[i]=data[i].collected_surcharge_amount;
 				pending_bill_actual_payable_amount[i]=data[i].actual_payable_amount;
 				pending_bill_collected_payable_amount[i]=data[i].collected_payable_amount;
 				pending_bill_type[i]=data[i].bill_type;
 				pending_paid_status[i]=data[i].paid_status;
 			}
   			
   			jQuery("#pending_grid").jqGrid("clearGridData");
   			addCollectionRow();
   			var top_rowid = $('#pending_grid tr:nth-child(2)').attr('id'); 
   			$("#pending_grid").setSelection(top_rowid, true);
   			focusNext("btn_save");
   	
   			  	$("#burnerDiv").hide();			
   			  	$("#divBurnerQnt").html("");
   			  	
   			/* if ((data[0].customer_id === "") || (data[0].customer_id === null)|| (data[0].customer_id === "undefined")){
					$("#msg_div").html("Invalid Customer Code.");	   					
					//advancedCollectionForm(clearField);	
					clearField.apply(this,fields);
					collectionForm(disableField);
					enableField("customer_id","customer_code");
		    	 $('#customer_code').focus();
			    	return;
			}*/
   			 
	   			
   			
   				collectionForm(enableField);
   				$("#msg_div").html("");   				
				$("#customer_name").val(data[0].customer.personalInfo.full_name);
				//$("#father_name").val(data.customer.personalInfo.father_name);
				$("#mobile").val(data[0].customer.personalInfo.mobile);
				$("#phone").val(data[0].customer.personalInfo.phone);
				
				$("#mobile_old").val(data[0].customer.personalInfo.mobile);
				$("#phone_old").val(data[0].customer.personalInfo.phone);
				
				
				$("#customerType").val(data[0].customer.connectionInfo.isMetered);
				$("#isMetered_str").val(data[0].customer.connectionInfo.isMetered_str);
				
				
				$("#address").val(getCustomerAddress(data[0].customer.addressInfo));
				
				if(typeof data[0].bill_id === "undefined"){
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
   			
				$("#billed_amount").val(data[0].billed_amount);
				$("#surcharge_amount").val(data[0].surcharge_amount);
				$("#adjustment_amount").val(data[0].adjustment_amount);				
				$("#vat_rebate_amount").val(data[0].vat_rebate_amount);				
				
				
				if(data[0].customer.connectionInfo.isMetered=="NONMETERED"){
					/*hideElement("vat_rebate_div","tax_amount_div","adjustment_div","net_payable_amount","collection_amount_div","metered_bill_amount","metered_surcharge");
					showElement("non_metered_billed_amount","non_metered_surcharge_amount","non_metered_payable_amount","non_metered_payable_amount","non_metered_surcharge_collection_amount");
					*/
					hideElement("meter_info_div");
					//showElement("nonmeter_info_div");
					
					$("#actual_billed_amount").val(data[0].actual_billed_amount);
					$("#collected_billed_amount").val(data[0].collected_billed_amount);
					$("#actual_surcharge_amount").val(data[0].actual_surcharge_amount);
					$("#surcharge_per_collection").val(data[0].surcharge_per_collection);
					$("#collected_surcharge_amount").val(data[0].collected_surcharge_amount);
					$("#actual_payable_amount").val(data[0].actual_payable_amount);
					//alert(data[0].actual_payable_amount);
					$("#collected_payable_amount").val(data[0].collected_payable_amount);
					$("#surcharge_amount").val(data[0].actual_surcharge_amount-data[0].collected_surcharge_amount);
					$("#collected_amount").val(data[0].actual_billed_amount-data[0].collected_billed_amount);
					$("#surcharge_amount_meter").val(data[0].actual_surcharge_amount-data[0].collected_surcharge_amount);
					
					$("#burnerDiv").show();
					$("#divBurnerQnt").html(data[0].double_burner_qnt);
					//$("#divBurnerQnt").html(data[0].customer.connectionInfo.double_burner_qnt_billcal);
					//alert(data[0].statusId);
					if(data[0].statusId==2){
						disableField("collected_amount");
					}
					
   		  		}
				else{
					//alert("hello!");
					/*hideElement("non_metered_billed_amount","non_metered_surcharge_amount","non_metered_payable_amount","non_metered_payable_amount","non_metered_surcharge_collection_amount");
					showElement("vat_rebate_div","tax_amount_div","adjustment_div","net_payable_amount","collection_amount_div","metered_bill_amount","metered_surcharge");
					*/
					
					hideElement("nonmeter_info_div");
					showElement("meter_info_div");
					$("#payable_amount").val(data[0].payable_amount);
					$("#collection_amount").val(data[0].payable_amount);
					$("#collected_amount").val(data[0].payable_amount);
					
					$("#burnerDiv").hide();				
				}
				
				$("#bill_month").val(data[0].bill_month);
				$("#bill_year").val(data[0].bill_year);
				$("#bill_id").val(data[0].bill_id);
				
				
				
				focusNext("collected_amount");
				$('#collected_amount').select();
				
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
   		  url: 'getCollectionInfo.action?collection.customer_id='+customer_id+'&collection.collection_id='+collection_id,
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
		alert("now i'm in else");
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
		dataUrl: 'multiCollectionDelete.action?customer_id='+customer_id,
		theme:jsVar.MODAL_THEME
    });
}


function addMultiCollection(){
showElement("from_bill_month_div","to_bill_month_div");	
focusNext("from_month");
}
function getNumberOfMonth(from_month,from_year,to_month,to_year){
	var currMonth=from_month;
 	var currYear=from_year;
 	var totalMonth=0;
 	while (true) {
 		totalMonth=totalMonth+1;
  		if (currMonth == to_month && to_year==currYear) {
 			break;
 		}
 		if (currMonth ==12) {
 			currMonth=0;
 			currYear=currYear+1;
 		} 		
 	   	   currMonth=currMonth+1;
 	}
 	return totalMonth;
}

function getMultiCollectionInfo(){
var from_month_year=parseInt($("#from_year").val()+$("#from_month").val().lpad("0", 2));
var to_month_year=parseInt($("#to_year").val()+$("#to_month").val().lpad("0", 2));
 if(to_month_year<from_month_year){
	 alert("To Bill Month Year should be greater than From Bill Month");
	 focusNext("from_month");
	 return;
 }
to_month_year=(to_month_year==0?from_month_year:to_month_year);

var collection_date=$("#collection_date").val();
var collection_month_year=parseInt(collection_date.substr(6,4)+collection_date.substr(3,2));

var from_month=parseInt($("#from_month").val().lpad("0", 2));
var from_year=parseInt($("#from_year").val());
var to_month=parseInt($("#to_month").val().lpad("0", 2));
var to_year=parseInt($("#to_year").val());
				
var numberOfMonth=getNumberOfMonth(from_month,from_year,to_month,to_year);
alert(numberOfMonth);
var r_coll_start_flag=parseInt(0);
var r_coll_from_month_year=0;
var r_coll_to_month_year=0;

var a_coll_start_flag=0;
var a_coll_from_month_year=0;
var a_coll_to_month_year=0;

var c_coll_start_flag=0;
var c_coll_from_month_year=0;
var c_coll_to_month_year=0;

var curr_month_year=from_month_year;

for(var i=1;i<=numberOfMonth;i++){
	
	if(curr_month_year<collection_month_year && r_coll_start_flag==0){
		r_coll_start_flag=1;
		r_coll_from_month_year=curr_month_year;
	
	}else if(curr_month_year==collection_month_year){
		c_coll_start_flag=1;
		c_coll_from_month_year=curr_month_year;
		
		c_coll_to_month_year=curr_month_year;
		
			if(r_coll_start_flag==1){
				r_coll_to_month_year=curr_month_year-1;
				
			}
	}else if(curr_month_year>collection_month_year){
		if(a_coll_start_flag==0){
			a_coll_start_flag=1;
			a_coll_from_month_year=curr_month_year;	
			
		}
		if(a_coll_start_flag==1 && i==numberOfMonth){
			a_coll_to_month_year=curr_month_year;
			
		}
		
	}
	if(c_coll_start_flag==0 && a_coll_start_flag==0 && i==numberOfMonth){
		r_coll_to_month_year=curr_month_year;
		
	}
	
	var curr_month=parseInt(String(curr_month_year).substring(4, 6));
	
	if(curr_month==12){
		
		var nxtMonth="01";
		var nxtYear=parseInt(String(curr_month_year).substring(0, 4))+1;
		
		var nxtMonthYear=String(nxtYear)+nxtMonth;
		curr_month_year=parseInt(nxtMonthYear);
		
	}else{
		
		curr_month_year=curr_month_year+1;		
	}
	
}
/*alert("sssssssssssssss");
alert(r_coll_from_month_year);
alert(r_coll_to_month_year);
alert(c_coll_from_month_year);
alert(c_coll_to_month_year);
alert(a_coll_from_month_year);
alert(a_coll_to_month_year);*/

  $.ajax({
		  type: 'POST',
		  url: 'multiCollection.action?customer_id='+$("#customer_id").val()+'&collection_date='+$("#collection_date").val()+'&r_coll_from_month_year='+r_coll_from_month_year+'&r_coll_to_month_year='+r_coll_to_month_year+'&c_coll_from_month_year='+c_coll_from_month_year+'&c_coll_to_month_year='+c_coll_to_month_year+'&a_coll_from_month_year='+a_coll_from_month_year+'&a_coll_to_month_year='+a_coll_to_month_year,
		  success:function(data){
			  for(var i in data)
	 			{
	    			
	    			pending_bill_id[i]=data[i].bill_id;
	 				pending_bill_month[i]=data[i].str_month;
	 				pending_bill_year[i]=data[i].year;
	 				pending_bill_actual_bill_amount[i]=data[i].actual_billed_amount;
	 				pending_bill_collected_bill_amount[i]=data[i].collected_billed_amount;
	 				pending_bill_actual_surcharge_amount[i]=data[i].actual_surcharge_amount;
	 				pending_bill_surcharge_per_collection[i]=data[i].surcharge_per_collection;
	 				pending_bill_collected_surcharge_amount[i]=data[i].collected_surcharge_amount;
	 				pending_bill_actual_payable_amount[i]=data[i].actual_payable_amount;
	 				pending_bill_collected_payable_amount[i]=data[i].collected_payable_amount;
	 				pending_bill_type[i]=data[i].bill_type;
	 				pending_paid_status[i]=data[i].paid_status;
	 			}
	   			jQuery("#pending_grid").jqGrid("clearGridData");
	   			addCollectionRow();
	   			var ids = jQuery("#pending_grid").jqGrid('getDataIDs');
	   			for (var i = 0; i < ids.length; i++) 
	   			{
	   			    var rowId = ids[i];
	   			    $("#pending_grid").setSelection(rowId, true);

	   			}
	   			calculatePendingBillTotal();
	   			
	   			focusNext("btn_save");
		  },
		  error:function(){
			
		  }
	});
	
}
function currentMonthCollection(){
	$.nsWindow.open({
		movable:true,
		title: 'Current Month Collection Entry',
		width: 800,
		height: 230,
		dataUrl: 'currentMonthBillCollection.action?customer_id='+$("#customer_id").val()+'&collection_date='+$("#collection_date").val(),
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

// here is the code for creating customer id from customer code by pressing enter key
// ~ Dec 06

$('#customer_code').keyup(function(e){
    if(e.keyCode == 13)
    {
    	
    	var fields = ["customer_name","customerType","collection_amount"];
    	clearField.apply(this,fields);
    	
    	//var afterSlashCharCounts = $("#customer_code").val().substr($("#customer_code").val().indexOf("/") + 1).length;
    	/*if ($("#customer_code").val().indexOf("/") >= 0) {
    		
    		 if (afterSlashCharCounts == 1) {
    		  $("#customer_code").val(lpad($("#customer_code").val(), 7));
    		 } else if (afterSlashCharCounts == 2) {
    		  $("#customer_code").val(lpad($("#customer_code").val(), 8));
    		 } else {
    		  $("#customer_code").val(lpad($("#customer_code").val(), 9));
    		 }

    		} else {
    			
    		 $("#customer_code").val(lpad($("#customer_code").val(), 9));
    		 
    		}*/
    	
        var customer_id=$("#customer_code").val();
    	

    	
        if(customer_id.substring(2,4)!=='01' && customer_id.substring(2,4)!=='09' ){
    	
    	
        $("#customer_id").val(customer_id);
        bill_month="";
    	bill_year="";
    	var collection_date=$("#collection_date").val();
    	
    	/*if ((data[0].customer_id === "") || (data[0].customer_id === null)|| (data[0].customer_id === "undefined")){
			$("#msg_div").html("Invalid Customer Code.");	   					
			//advancedCollectionForm(clearField);	
			clearField.apply(this,fields);
			collectionForm(disableField);
			enableField("customer_id","customer_code");
    	 $('#customer_code').focus();
	    	return;
	}*/
		 getBillingInfo(customer_id,bill_month,bill_year,collection_date);
	
    	
       
        //fetchCollectionList($('#customer_id').val());
        //getCollectionHistoryByDate();
		
    }else{
    	
    	alert("Please enter meter-customer only.");
    }
        
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
$('#from_month').keyup(function(e){
    if(e.keyCode == 13)
    {
    	focusNext("from_year");
		
    }
});
$('#from_year').keyup(function(e){
    if(e.keyCode == 13)
    {
    	focusNext("to_month");
		
    }
});
$('#to_month').keyup(function(e){
    if(e.keyCode == 13)
    {
    	focusNext("to_year");
		
    }
});
$('#to_year').keyup(function(e){
    if(e.keyCode == 13)
    {
    	getMultiCollectionInfo();
		
    }
});
$('#btn_save').keyup(function(e){
    if(e.keyCode == 13)
    {
    	validateAndSaveCollection();
		
    }
});


$(document).unbind('keydown');$("input").unbind('keydown');
$(document).bind('keydown', 'ctrl+m', function(){addMultiCollection();});
$("input").bind('keydown', 'ctrl+m', function(){addMultiCollection();});
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
		    url: 'deleteBillingCollection.action',
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
 		  url: 'getTotalCollectionByDateAccount.action?collection.collection_date='+$("#collection_date").val()+"&collection.account_no="+$("#account_id").val(),
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
	   		  url: 'getCollectionInfo.action?collection_date='+customer_id,
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
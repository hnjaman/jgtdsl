jQuery("#customer_grid")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DEPOSIT_SERVICE+'&method='+jsEnum.SD_EXP_LIST,
        jsonReader: {
            repeatitems: false,
            //id: "customer_id",
            depositId: "deposit_id"
        },
        colNames: ['Deposit Id', 'Customer Id', 'Customer Name','Bank','Branch','Account','Valid Till','Total Deposit','Expire In (Days)'],
        colModel: [{
	                name: 'deposit_id',
	                index: 'deposit_id',
	                hidden:false
            	},
            	{
	                name: 'customer_id',
	                index: 'customer_id',
	                hidden:false
            	},
            	{
            		name: 'customer_name',
	                index: 'customer_name',
	                sorttype: "String"
	                
                },
                {
            		name: 'bank_name',
	                index: 'bank_name',
	                sorttype: "string"
                },
                {
            		name: 'branch_name',
	                index: 'branch_name',
	                sorttype: "string"
                },
                {
            		name: 'account_name',
	                index: 'account_name',
	                sorttype: "string"
                },
                {
            		name: 'valid_to',
	                index: 'valid_to',
	                sorttype: "string",
	                align:'center'
	                	
                }
                ,
                {
            		name: 'total_deposit',
	                index: 'total_deposit',
	                sorttype: "string",
	                align:'right'
                }
                ,
                {
            		name: 'expire_in',
	                index: 'expire_in',
	                sorttype: "string",
	                align:'right'
            	}
        ],
        caption: jqCaption.LIST_SD_EXPIRE,
        sortname: 'expire_in',
        height: $("#customer_grid_div").height()-70,
    	width: $("#customer_grid_div").width()-10,
       	pager: '#customer_grid_pager',
       //	sortname: 'customer_id',
        sortorder: "asc",
    	caption: "Customer List Going To Be Expired",
        onSelectRow: function(depositId){ 
        	//alert(depositId);
        	var rowData = $("#customer_grid").getRowData(depositId);
        	var deposit_id=rowData.deposit_id;
        	var old_date=rowData.valid_to;
        	var customerId=rowData.customer_id;
        	

        	//alert(deposit_id+" "+customerId);
        	
        	
        	
        	$("#deposit_id").val(deposit_id);
    		$("#old_expire_date").val(old_date);
    		$("#customer_id").val(customerId);
    		enableButton("btn_save");
    		getCustomerInfo("comm",customerId);	
    		setActiveTabToMeterRentChangeHistory(customerId,deposit_id)
    		
       }
    }).navGrid('#customer_grid_pager',{edit: false, add: false, del: false, search: true, refresh:true,view:false},{},{},{},{},{});


jQuery("#customer_grid").jqGrid (
        "navButtonAdd","#gridPager",
         {
        	
        	 
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
        		window.location="securityDepositExpireReport.action";
         }
         });

$("#meterRent_change_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DEPOSIT_SERVICE+'&method=getBGExpireChangHistory'+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pId"
	},
   colNames: ['PID','Depsosit Id','Customer Id', 'Old Expire Date','New Expire Date','Entry Date','Remarks','Insert By'],
   colModel: [{
		       name: 'pId',
		       index: 'pId',
		       width:70,
		       align:'center',
		       sorttype: 'string',
		       hidden:true
			},
			{
		       name: 'deposit_id',
		       index: 'deposit_id',
		       width:150,
		       sorttype: 'string',
		       search: true
			},
			{
			       name: 'customer_id',
			       index: 'customer_id',
			       width:150,
			       sorttype: 'string',
			       align:'center',
			       search: true
			},
			{
		       name: 'old_expire_date',
		       index: 'old_expire_date',
		       sorttype: "string",
		       align:'center',
		       search: true,
			},
			{
		       name: 'new_expire_date',
		       index: 'new_expire_date',
		       sorttype: "string",
		       align:'center',
		       search: true,
			},
			{
		       name: 'entry_date',
		       index: 'entry_date',
		       sorttype: "string",
		       align:'center',
		       search: true,
			},
			{
		       name: 'remarks_on_bg',
		       index: 'remarks_on_bg',
		       sorttype: "string",
		       search: true,
			},
			{
			       name: 'inserted_by',
			       index: 'inserted_by',
			       sorttype: "string",
			       align:'center',
			       search: true,
			}
        ],
	//datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
   	pager: '#meterRent_change_history_this_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Customer's BG Chagne History",
	onSelectRow: function(id){ 
		
		var rowData = $("#meterRent_change_history_this_grid").getRowData(id);
		
		$("#pid").val(rowData.pId);
		$("#deposit_id").val(rowData.deposit_id);
		$("#customer_id").val(rowData.customer_id);
		$("#old_expire_date").val(rowData.old_expire_date);
		$("#new_expire_date").val(rowData.new_expire_date);
		$("#entry_date").val(rowData.entry_date);
		$("#remarks_on_bg").val(rowData.remarks_on_bg);
		enableButton("btn_delete");
		disableField("btn_save");
		bankGarantieExpireExtentionForm(enableField);
		bankGarantieExpireExtentionForm(readOnlyField);
		getCustomerInfo("comm",rowData.customer_id);
		
		
		
   }
}));
jQuery("#meterRent_change_history_this_grid").jqGrid('navGrid','#meterRent_change_history_this_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {setActiveTabToMeterRentChangeHistory("");}}),{},{},{},
		{
		multipleSearch:true,
		onSearch: function () {	
				
			   var ruleArray=[["METER_RENT_CHANGE.CUSTOMER_ID"],["eq"],[$("#comm_customer_id").val()]];
			   var oldRules=["customer_id"];
			   var newRules=["METER_RENT_CHANGE.customer_id"];			   
			   modifyGridPostData("meterRent_change_history_this_grid",ruleArray,oldRules,newRules);	
		   }
		
		});
gridColumnHeaderAlignment("left","meterRent_change_history_this_grid",["remarks"]);

//[START : BASIC INITIALIZATION & OVERRIDE] Common settings and override of some configuration for this particular interface
	//Disable Reading Form "customer_id" as we will not let the user to use this field in this interface.
$("#comm_customer_id").unbind();
$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("comm",$('#comm_customer_id').val());reloadBankGarantieExpireInfoForCustomer($('#comm_customer_id').val());}
}));

var $dialog = $('<div id="dialog-confirm"></div>')
.html("<p> "+
 	"Are you sure you want to delete the BG Information?"+
	"<div id='del_holiday'></div> "+
   "</p>")
.dialog({
		title: 'BG Delete Confirmation',
		resizable: false,
		autoOpen: false,
		height:150,
		width:450,
		modal: true,
		buttons: {
				"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
					deleteBankGarantieExpireChangeInfo(); 
					//$( this ).dialog( "close" );
				}},
				"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
					$( this ).dialog( "close" );
				}},
		}
	});
function deleteBankGarantieExpireChangeInfo(){
	//alert("");
	$.ajax({
	    url: 'deleteBankGarantieExpireChangeInfo.action',
	    type: 'POST',
	    data: {deposit_id:$("#deposit_id").val()},
	    cache: false,
	    success: function (response) {
	    		meterRentChangeForm(clearField);	
		    	$dialog.dialog("close");
		    	$('#meterReconnForm').trigger("reset");
		    	$('#bgCustomerInfo').trigger("reset");
		    	//alert(response.message);
		    	reloadGrid("customer_grid");
		    	reloadGrid("meterRent_change_history_this_grid");
		    	reloadGrid("meterRent_change_history_all_grid");
		    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    },
	    error: function(response) {
           // alert("error");
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
        },
	    
	  });
}

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "entry_date",
    trigger    : "entry_date"}));
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "new_expire_date",
    trigger    : "new_expire_date"}));

//[END : BASIC INITIALIZATION & OVERRIDE]

function setActiveTabToMeterRentChangeHistory(customer_id,deposit_id){
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);

	var customerId=customer_id;
	if(customer_id=="") //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadMeterRentChangeHistory(customerId,deposit_id);
    
}

function reloadMeterRentChangeHistory(customer_id,deposit_id){
    var ruleArray=[["BG_EXPIRE_CHANGE_HISTORY.CUSTOMER_ID","DEPOSIT_ID"],["eq","eq"],[customer_id,deposit_id]];
	var postdata=getPostFilter("meterRent_change_history_this_grid",ruleArray);
    $("#meterRent_change_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("meterRent_change_history_this_grid");
}

function reloadBankGarantieExpireInfoForCustomer(){
	
}


function reloadBankGarantieExpireInfoForCustomer(customer_id){
	//alert("reloadBankGarantieExpireInfoForCustomer");
    var ruleArray=[["deposit.customer_id"],["eq"],[customer_id]];
	var postdata=getPostFilter("customer_grid",ruleArray);
    $("#customer_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("customer_grid");
}
function reloadBankGarantieExpireInfoForSearch(){
    var days=$("#days").val();
    if (days==""){
    	days="90";
    }
    var ruleArray=[["(valid_to - TRUNC (SYSDATE))"],["le"],[days]];
	var postdata=getPostFilter("customer_grid",ruleArray);
    $("#customer_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("customer_grid");
}
$('#days').keyup(function(e){
    if(e.keyCode == 13)
    {
    	reloadBankGarantieExpireInfoForSearch();
		
    }
});

function validateAndSaveGankGarantieExpireExtentionInfo(){
	
	
	var validate=false;
	
	validate=validateGankGarantieExpireExtentionInfo();
	if(validate==true){	    
		saveGankGarantieExpireExtentionInfo();							
	}	
}

function validateGankGarantieExpireExtentionInfo(){
	
	var isValid=false;	
    isValid=validateField("entry_date","old_expire_date","new_expire_date","deposit_id");		
	return isValid;
}
function saveGankGarantieExpireExtentionInfo(){
	
	bankGarantieExpireExtentionForm(enableField);
	bankGarantieExpireExtentionForm(readOnlyField);
	var deposit_id=$("#deposit_id").val();
	
	var formData = new FormData($('form')[0]);
	 $.ajax({
		    url: 'saveGankGarantieExpireExtentionInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  	  
		      if(response.status=="OK"){
		    	  bankGarantieExpireExtentionForm(clearField);
		    	  bankGarantieExpireExtentionForm(removeReadOnlyField);
		    	  bankGarantieExpireExtentionForm(disableField);
		    	  disableButton("btn_save","btn_delete");
		    	  reloadMeterRentChangeHistory($("#comm_customer_id").val(),deposit_id);
		    	  reloadGrid("customer_grid");
		      }
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}
function cancelButtonPressed(){
	clearRelatedData();
	bankGarantieExpireExtentionForm(disableField);
	disableButton("btn_add","btn_edit","btn_save","btn_delete");
	unBlockGrid("meter_grid");
	resetSelection("meter_grid","customer_grid","meterRent_change_history_this_grid","meterRent_change_history_all_grid");
}
function addButtonPressed(){
	if($("#comm_customer_id").val()==""){
		var message="Please select a customer.";
		showDialog("Information",message);
	}
	else{
		bankGarantieExpireExtentionForm(enableField);
		disableField("meter_sl_no","old_rent");
		enableButton("btn_save");
		disableButton("btn_add");	
	}
}
function editButtonPressed(){	
	bankGarantieExpireExtentionForm(enableField);
	disableField("meter_sl_no","old_rent");
	enableButton("btn_save");
	disableButton("btn_edit");
}
function bankGarantieExpireExtentionForm(plainFieldMethod){
	var fields=["deposit_id","entry_date","old_expire_date","new_expire_date","remarks"];	
	plainFieldMethod.apply(this,fields);	
}
function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
	clearGridData("meter_grid");
	bankGarantieExpireExtentionForm(clearField);
}
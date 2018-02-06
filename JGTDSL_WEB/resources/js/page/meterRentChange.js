//<<Customer Grid>>: List of Metered Customer
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.METERED_CUSTOMER_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
	colNames: ['Customer Id', 'Customer Name','Father Name','Category', 'Area','Mobile','Status','Created On'],
    colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-5,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Metered Customers",
	onSelectRow: function(id){ 
		getCustomerInfo("comm",id);		
		loadMeters(id,meterLoadPreOperation);
		meterRentChangeForm(clearField);
		meterRentChangeForm(disableField);
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Meter Rent History for the selected Customer>>
$("#meterRent_change_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_RENT_SERVICE+'&method='+jsEnum.METER_RENT_CHANGE_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Customer Name','Meter No.', 'Old Rent','New Rent','Remarks'],
   colModel: [{
		       name: 'customer_id',
		       index: 'customer_id',
		       width:70,
		       align:'center',
		       sorttype: 'string',
		       search: false
			},
			{
		       name: 'customer_name',
		       index: 'customer_name',
		       width:200,
		       sorttype: 'string',
		       search: true
			},
			{
		       name: 'meter_sl_no',
		       index: 'meter_sl_no',
		       sorttype: "string",
		       search: true,
			},
			{
		       name: 'old_rent',
		       index: 'old_rent',
		       sorttype: "string",
		       search: true,
			},
			{
		       name: 'new_rent',
		       index: 'new_rent',
		       sorttype: "string",
		       search: true,
			},
			{
		       name: 'remarks',
		       index: 'remarks',
		       sorttype: "string",
		       search: true,
			}
        ],
	//datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
   	pager: '#meterRent_change_history_this_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Customer's Meter Rent Chagne History",
	onSelectRow: function(id){ 
		
		var rowData = $("#meterRent_change_history_this_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchMeterRentChangeInfo(id);
		
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



//<<Meter Rent History for the all Customer>>
$("#meterRent_change_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_RENT_SERVICE+'&method='+jsEnum.METER_RENT_CHANGE_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Customer Name','Meter No.', 'Old Rent','New Rent','Remarks'],
   colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:70,
	                align:'center',
	                sorttype: 'string',
	                search: true
            	},
            	{
	                name: 'customer_name',
	                index: 'customer_name',
	                width:200,
	                sorttype: 'string',
	                search: true
            	},
            	{
	                name: 'meter_sl_no',
	                index: 'meter_sl_no',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'old_rent',
	                index: 'old_rent',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'new_rent',
	                index: 'new_rent',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'remarks',
	                index: 'remarks',
	                sorttype: "string",
	                search: true,
            	}
        ],
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
   	pager: '#meterRent_change_history_all_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "All Customers Meter Rent Change History",
	onSelectRow: function(id){ 
		
		var rowData = $("#meterRent_change_history_all_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchMeterRentChangeInfo(id);
   }
}));
jQuery("#meterRent_change_history_all_grid").jqGrid('navGrid','#meterRent_change_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","meterRent_change_history_all_grid",["customer_name","remarks"]);

//<<Meter list grid>>: It holds all the meters of the selected customer
$("#meter_grid").jqGrid( "setGridParam", { onSelectRow: null } );
$('#meter_grid').on('jqGridSelectRow', function (event, id, selected) {
	meterRentChangeForm(clearField);
	var rowData = $("#meter_grid").getRowData(id);
	setMeterInfo(rowData);	
	enableButton("btn_add");
	disableButton("btn_edit","btn_delete");	
	var customer_id=getFieldValueFromSelectedGridRow("meter_grid","customer_id");
	setActiveTabToMeterRentChangeHistory(customer_id);
	
});

//[START : BASIC INITIALIZATION & OVERRIDE] Common settings and override of some configuration for this particular interface
 	//Disable Reading Form "customer_id" as we will not let the user to use this field in this interface.
	$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
			serviceUrl: sBox.METERED_CUSTOMER_LIST,
	    	onSelect:function (){getCustomerInfo("comm",$('#comm_customer_id').val());loadMeters($('#comm_customer_id').val());}
	}));

	var $dialog = $('<div id="dialog-confirm"></div>')
	.html("<p> "+
	 	"Are you sure you want to delete the Meter rent Change Information?"+
		"<div id='del_holiday'></div> "+
	   "</p>")
	.dialog({
			title: 'Meter Rent Change Delete Confirmation',
			resizable: false,
			autoOpen: false,
			height:150,
			width:450,
			modal: true,
			buttons: {
					"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
						deleteMeterRentChangeInfo();          
					}},
					"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
						$( this ).dialog( "close" );
					}},
			}
		});

	Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "effective_date",
	    trigger    : "effective_date"}));
	
//[END : BASIC INITIALIZATION & OVERRIDE]

function setActiveTabToMeterRentChangeHistory(customer_id){
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);

	var customerId=customer_id;
	if(customer_id=="") //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadMeterRentChangeHistory(customerId);
    
}

function reloadMeterRentChangeHistory(customer_id){

    var ruleArray=[[" METER_RENT_CHANGE.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("meterRent_change_history_this_grid",ruleArray);
    $("#meterRent_change_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("meterRent_change_history_this_grid");
   	reloadGrid("meterRent_change_history_all_grid");
}



function setMeterInfo(meter_row){
	$("#meter_id").val(meter_row.meter_id);
	$("#meter_sl_no").val(meter_row.meter_sl_no);
	$("#old_rent").val(meter_row.meter_rent);
	$("#customer_id").val(meter_row.customer_id);
		
}
function fetchMeterRentChangeInfo(change_id){
	
	meterRentChangeForm(disableField);
	blockGrid("meter_grid");	
	$.ajax({
		    url: 'getMeterRentChangeInfo.action',
		    type: 'POST',
		    data: {pId:change_id},
		    cache: false,
		    success: function (response) {
		    	setRentChangeInfo(response);		    			    	
		    	disableButton("btn_add","btn_save");
		    	enableButton("btn_edit","btn_delete");
		    
		    }
		    
		  });	
}

function setRentChangeInfo(data){
	$("#meter_id").val(data.meter_id);
	$("#customer_id").val(data.customer_id);
	$("#pid").val(data.pid);
	$("#remarks").val(data.remarks);
	$("#new_rent").val(data.new_rent);
	$("#old_rent").val(data.old_rent);
	$("#effective_date").val(data.effective_date);
	$("#meter_sl_no").val(data.meter_sl_no);	
}
function validateAndSaveMeterRentChangeInfo(){
	
	var validate=false;
	
	validate=validateRentChangeInfo();
	if(validate==true){	    
		saveMeterRentChangeInfo();							
	}	
}


function validateRentChangeInfo(){
	var isValid=false;	
    isValid=validateField("meter_sl_no","effective_date","old_rent","new_rent");		
    if(isValid==true){
    	isValid=isPositiveNumber($("#new_rent").val());
    	if(isValid==false){
    		alert("New rent must be a zero or a numeric value.");
    		return isValid;
    	}
    }
	return isValid;
}


function saveMeterRentChangeInfo(){
	
	meterRentChangeForm(enableField);
	meterRentChangeForm(readOnlyField);
	
	var formData = new FormData($('form')[0]);
	 $.ajax({
		    url: 'saveMeterRentChangeInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  	  
		      if(response.status=="OK"){
		    	  meterRentChangeForm(clearField);
		    	  meterRentChangeForm(removeReadOnlyField);
		    	  meterRentChangeForm(disableField);
		    	  disableButton("btn_save","btn_add","btn_delete");
		    	  reloadMeterRentChangeHistory($("#comm_customer_id").val());
		      }
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}
function deleteMeterRentChangeInfo(){
	$.ajax({
	    url: 'deleteMeterRentChagneInfo.action',
	    type: 'POST',
	    data: {pId:$("#pid").val()},
	    cache: false,
	    success: function (response) {
	    	meterRentChangeForm(clearField);	    
	    	$dialog.dialog("close");
	    	reloadGrid("meterRent_change_history_this_grid");
	    	reloadGrid("meterRent_change_history_all_grid");
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	  });
}

function cancelButtonPressed(){
	clearRelatedData();
	meterRentChangeForm(disableField);
	disableButton("btn_add","btn_edit","btn_save","btn_delete");
	unBlockGrid("meter_grid");
	resetSelection("meter_grid","customer_grid","meterRent_change_history_this_grid","meterRent_change_history_all_grid");
}



//take a js date and format it to dd-mm-yyyy. Updated in sept 14,2017 
function dateToDMY(date) {
    var d = date.getDate();
    var m = date.getMonth() + 1;
    var y = date.getFullYear();
    return '' + (d <= 9 ? '0' + d : d) + '-' + (m<=9 ? '0' + m : m) + '-' + y ;
}
//End of : take a js date and format it to dd-mm-yyyy. Updated in sept 14,2017



function addButtonPressed(){
	if($("#comm_customer_id").val()==""){
		var message="Please select a customer.";
		showDialog("Information",message);
	}
	else{
//default value is set to first date of the month.updated on sept 14,2017
		var date = new Date();
		var d = new Date(date.getFullYear(), date.getMonth(), 1);
		var firstday = dateToDMY(d);
		$("#effective_date").val(firstday);
//END of : default value is set to first date of the month.updated on sept 14,2017		
		meterRentChangeForm(enableField);
		disableField("meter_sl_no","old_rent");
		enableButton("btn_save");
		disableButton("btn_add");	
	}
}
function editButtonPressed(){	
	meterRentChangeForm(enableField);
	disableField("meter_sl_no","old_rent");
	enableButton("btn_save");
	disableButton("btn_edit");
}
function meterRentChangeForm(plainFieldMethod){
	var fields=["meter_sl_no","effective_date","old_rent","new_rent","remarks","pid","customer_id","meter_id"];	
	plainFieldMethod.apply(this,fields);	
}
function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
	clearGridData("meter_grid");
	meterRentChangeForm(clearField);
}

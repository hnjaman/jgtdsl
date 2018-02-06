//<<Customer Grid>>: It holds all the Metered Customer List
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.METERED_CUSTOMER_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
	colNames: ['Customer Id', 'Customer Name','Father Name', 'Category', 'Area','Mobile','Status','Created On'],
	colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()+10,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Available Customers",
	onSelectRow: function(id){ 
		$("#customer_id").val(id);
		getCustomerInfo("comm",id);		
		loadMeters(id);
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true,refresh:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Disconnect History for the selected Customer>>: It holds disconnection history for the selected customer.
$("#disconn_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DISCONNECTION_SERVICE+'&method='+jsEnum.METER_DISCONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Meter No.', 'Disconn. Cause','Disconn. Type', 'Disconn Date','Remarks'],
   colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:100,
	                align:'center',
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
	                name: 'disconnect_cause_name',
	                index: 'disconnect_cause_name',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'disconnect_type_name',
	                index: 'disconnect_type_name',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'disconnect_date',
	                index: 'disconnect_date',
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
   	pager: '#disconn_history_this_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Customer's Disconnection History",
	onSelectRow: function(id){ 
		
		fetchDisconnectionInfo(id);
		
   }
}));
jQuery("#disconn_history_this_grid").jqGrid('navGrid','#disconn_history_this_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {setActiveTabToCustomerDisconnHistory("");}}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","disconn_history_this_grid",["full_name","disconnect_cause_name","disconnect_type_name","remarks"]);

//<<Disconnect History for the all Customer>>: It holds disconnection history for all customer
$("#disconn_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DISCONNECTION_SERVICE+'&method='+jsEnum.METER_DISCONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Customer Name','Meter No.', 'Disconn. Cause','Disconn. Type', 'Disconn Date','Remarks'],
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
	                name: 'disconnect_cause_name',
	                index: 'disconnect_cause_name',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'disconnect_type_name',
	                index: 'disconnect_type_name',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'disconnect_date',
	                index: 'disconnect_date',
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
   	pager: '#disconn_history_all_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Customer's Disconnection History",
	onSelectRow: function(id){ 		
		var rowData = $("#disconn_history_all_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);		
		loadMeters(rowData.customer_id);
		fetchDisconnectionInfo(id);
		
   }
}));
jQuery("#disconn_history_all_grid").jqGrid('navGrid','#disconn_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","disconn_history_all_grid",["full_name","customer_name","disconnect_cause_name","disconnect_type_name","remarks"]);


//[START : BASIC INITIALIZATION & OVERRIDE] Common settings and override of some configuration for this particular interface
 	//Disable Reading Form "customer_id" as we will not let the user to use this field in this interface.
	disableField("customer_id");
	//Remove the address information of the customer here. Due to space issue.
	$("#common_address_row").remove();
	
	$("#disconnect_by").unbind();
	$("#disconnect_by").chosen({no_results_text: "Oops, nothing found!",width: "64%"});

	//Recreate the button block and override it. Because we will not use the default buttons of Reading Entry Form.
	
	var buttons="<table width='100%'>"+
	"<tr>"+
	"	<td width='55%' align='left'>"+
	"		<button class='btn btn-beoro-3' type='button' id='btn_edit' onclick='editButtonPressed()' disabled='disabled'><span class='splashy-application_windows_edit'></span>"+
	"		Edit</button>"+
	"		<button class='btn btn-beoro-3' type='button' id='btn_save' onclick='validateAndSaveMeterDisconnInfo()' disabled='disabled'><span class='splashy-document_letter_okay'></span>"+
	"		Save</button>"+
	"		<button class='btn btn-beoro-3'  type='button' id='btn_cancel' onclick='cancelButtonPressed()'><span class='splashy-error'></span>"+
	"		Cancel</button>"+
	"	</td>"+
	"	<td width='5%'></td>"+
	"	<td width='40%' align='right'>"+
	"		<button class='btn btn-beoro-3' type='button' id='btn_delete' onclick=\"$dialog.dialog('open');\" disabled='disabled'><span class='splashy-gem_remove'></span>"+
	"		Delete</button>"+
	"		<button class='btn btn-beoro-3'  type='button' id='btn_close' onclick=\"callAction('blankPage.action')\"><i class='splashy-folder_classic_remove'></i>"+
	"		Close</button>	"+
	"	</td>"+
	"</tr>"+
	"</table>";
	$("#reading_button_div").html(buttons);
	
	$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
			serviceUrl: sBox.METERED_CUSTOMER_LIST,
	    	onSelect:function (){getCustomerInfo("comm",$('#comm_customer_id').val());$("#customer_id").val($('#comm_customer_id').val());loadMeters($('#comm_customer_id').val());}
	}));
	
	var $dialog = $('<div id="dialog-confirm"></div>')
	.html("<p> "+
	 	"Are you sure you want to delete the Meter Disconnection Information? "+
		"<div id='del_holiday'></div> "+
	   "</p>")
	.dialog({
			title: 'Meter Disconnection Information Delete Confirmation',
			resizable: false,
			autoOpen: false,
			height:150,
			width:450,
			modal: true,
			buttons: {
					"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
						deleteDisconnInfo();          
					}},
					"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
						$( this ).dialog( "close" );
					}},
			}
		});
	
	Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "disconnect_date",
	    trigger    : "disconnect_date",
	    onSelect   : function() { this.hide(); 
	                 $("#curr_reading_date").val($("#disconnect_date").val());
//code added for setting month according to disconn date ~sept 17 ~ Prince
                     var arr = $("#disconnect_date").val().split('-');
	                 var m=arr[1];
	                 $("#billing_month").val(Number(m));
//End of: code added for setting month according to disconn date
	                 
	                 fetchGasPrice($("#disconnect_date").val());
	                 getReadingInfo4MD();}}));	
	


//Load the available meters for the selected customer [either via typing customer code in the auto complete box or via selecting the customer grid] 	
function loadMeters(customer_id){
   	var ruleArray=[["customer_id"],["eq"],[customer_id]];
	var postdata=getPostFilter("meter_grid",ruleArray);
   	$("#meter_grid").jqGrid('setGridParam',{search: true,postData: postdata,url:meterListUrl,page:1,datatype:'json'});    		
	reloadGrid("meter_grid");
}


//As soon as the customer select the meter from meter grid, the corresponding meter reading form will be loaded with some
// prefilled field so that the user can entry the meter reading information at the time of disconnection.
$("#meter_grid").jqGrid( "setGridParam", { onSelectRow: null } );
$('#meter_grid').on('jqGridSelectRow', function (event, id, selected) {
	
	disconnInfoForm(clearField,clearChosenField);
	getCustomerInfo("",$("#comm_customer_id").val());	
	
	getMeterList4MD(id);  //getMeterListForMeterDisconnect => 4MD->ForMeterDisconnect
	
});

function getMeterList4MD(meter_id){
	$("#reading_purpose_str").val("2");
	$("#billing_month").val(getCurrentMonth());
	$("#billing_year").val(getCurrentYear());
	getMeterList(meter_id,getReadingInfo4MD);	
}

function getReadingInfo4MD(){
	fetchReadingInfo(readingCallBack4MD);	
}

function readingCallBack4MD(data){
	readingCallBack(data);
	changeReadingFormMode("data_entry_mode","2");
	//enableField("billing_month","billing_year");
	disableField("curr_reading_date");
	
	var customer_id=getFieldValueFromSelectedGridRow("meter_grid","customer_id");
	setActiveTabToCustomerDisconnHistory(customer_id);
	$("#billing_month").prop('disabled', false);
	$("#billing_year").prop('disabled', false);
	enableButton("btn_save");
}

function setActiveTabToCustomerDisconnHistory(customer_id){
	
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);
    
	var customerId=customer_id;
	if(customer_id=="" || customer_id==null) //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadDisconnHistory(customerId);
    
}

function reloadDisconnHistory(customer_id){
	
	var ruleArray=[["DISCONN_METERED.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("meter_grid",ruleArray);
    $("#disconn_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});  
    reloadGrid("disconn_history_this_grid");
    reloadGrid("disconn_history_all_grid");
}

function fetchDisconnectionInfo(disconnection_id){
	
	disconnInfoForm(disableField,disableChosenField);
	blockGrid("meter_grid");
	
	  $.ajax({
		    url: 'getMeterDisconnInfo.action',
		    type: 'POST',
		    data: {pId:disconnection_id},
		    cache: false,
		    success: function (response) {
		    	setDisconnInfo(response);
		    	setReadingInfo(response.reading);
		    	/** Need to move from here.../*/
		    	$("#reading_purpose_str").val(response.reading.reading_purpose_str);
		    	$("#billing_month").val(response.reading.billing_month);
		    	$("#billing_year").val(response.reading.billing_year);
		    	$("#reading_id").val(response.reading.reading_id);
		    	/**/
		    	setCustomerInfo("",response.customer);		    	
		    	changeReadingFormMode("disable_mode",null);
		    	disableButton("btn_save");
		    	
		    	if (response.reading.bill_id === undefined ){		    		
		    		enableButton("btn_delete","btn_edit");
		    		
		    	}
		    	else{
		    		disableButton("btn_delete","btn_edit");
		    	}
		    }
		    
		  });	
}

function setDisconnInfo(data){
	$("#disconnect_cause_str").val(data.disconnect_cause_str);
	$("#disconnect_type_str").val(data.disconnect_type_str);
	$("#disconnect_remarks").val(data.remarks);
	$("#disconnect_date").val(data.disconnect_date);
	setChosenData("disconnect_by",data.disconnect_by);	
	$("#pid").val(data.pid);
}

function validateAndSaveMeterDisconnInfo(){
	
	var validateDiconnInfo=false;
	var validateReadingInfo=false;
	
	validateDiconnInfo=validateDisconnInfo();
	validateReadingInfo=validateMeterReadingInfo();
	if(validateDiconnInfo==true && validateReadingInfo==true){
	
		saveMeterDisconnInfo();
	}	
}

function validateDisconnInfo(){
	
	var isValid=validateField("disconnect_cause_str","disconnect_type_str","disconnect_date");	
	if($("#disconnect_by").chosen().val()==null){	
		 cbColor($("#disconnect_by_chosen"),"e");
		 isValid=false;
	 }
	else
		cbColor($("#disconnect_by_chosen"),"v");
	
	return isValid;
}
function saveMeterDisconnInfo(){
	
	changeReadingFormMode("data_submit_mode",null);
	enableField("customer_id");
	
	var formData = new FormData($('form')[0]);
	var readingData = jQuery(document.forms['meterReadingForm']).serializeArray();
	for (var i=0; i<readingData.length; i++)
		formData.append(readingData[i].name, readingData[i].value);
	
	
	
	  $.ajax({
		    url: 'saveMeterDisconnInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  		if(response.status=="OK"){
			  		changeReadingFormMode("disable_mode",null);
			  		disableField("customer_id");
			  		disableField("disconnect_cause_str","disconnect_type_str","disconnect_remarks","disconnect_date");
			  		disableChosenField("disconnect_by");
			   		reloadGrid("meter_grid");
			   		reloadDisconnHistory($("#comm_customer_id").val());
			   		enableButton("btn_edit");
			   		disableButton("btn_save");
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

function cancelButtonPressed(){
	clearRelatedData();
	unBlockGrid("meter_grid");
	clearGridData("meter_grid");
	resetSelection("meter_grid","customer_grid","disconn_history_this_grid","disconn_history_all_grid");
}

function editButtonPressed(){	
	changeReadingFormMode("data_entry_mode","2");
	enableField("billing_month","billing_year");
	disableField("curr_reading_date");
	enableField("disconnect_cause_str","disconnect_type_str","disconnect_remarks","disconnect_date","disconnect_by");
	enableChosenField("disconnect_by");	
	
	enableButton("btn_save");
	disableButton("btn_edit");
	
}

function deleteDisconnInfo(){
	$.ajax({
	    url: 'deleteMeterDisconnInfo.action',
	    type: 'POST',
	    data: {pId:$("#pid").val()},
	    cache: false,
	    success: function (response) {
	    	//Clean forms data
	    	disconnInfoForm(clearField,clearChosenField);
	    	clearMeterReadingForm();
	    		    	
	    	//Reload Grid data
	    	loadMeters($("#comm_customer_id").val());
	    	reloadGrid("disconn_history_this_grid");
	    	reloadGrid("disconn_history_all_grid");
	    	
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	  });
}

function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);	
	clearField.apply(this,customer_info_field);	
	clearGridData("meter_grid");
	disconnInfoForm(clearField,clearChosenField);
	clearMeterReadingForm();
}

function disconnInfoForm(plainFieldMethod,chosenFieldMethod){
	
	var fields = ["disconnect_cause_str","disconnect_type_str","disconnect_remarks","disconnect_date","disconnect_customer_id","pid"];
	var chosen_field=["disconnect_by"];
		 
	plainFieldMethod.apply(this,fields);
	if(chosenFieldMethod!=null)
		chosenFieldMethod.apply(this,chosen_field);	
	
}
$("#curr_reading_date").val(getCurrentDate());
$("#disconnect_date").val(getCurrentDate());

/*
$("#billing_month").removeAttr('onchange'); 
$("#billing_year").removeAttr('onchange'); 

$("#billing_month").change(function(){  });
$("#billing_year").change(function(){ }); 
*/
/* Tanmoy's Code
function setValueToModalParameters(){
	$("#parameter_month").val($("#billing_month").val()==""?getCurrentMonth():$("#billing_month").val());
	$("#parameter_year").val($("#billing_year").val()==""?getCurrentYear():$("#billing_year").val()); 	 	
	$("#parameter_reading_date").val($("#curr_reading_date").val()==""?getCurrentDate():$("#curr_reading_date").val());
}

dialog.dialog("open");
setValueToModalParameters();
*/
 
//[START:GRID BLOCK]Grid data initialization and loading done in this code block
//<<Customer Grid>>: It holds all the disconnected meter customer
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.NON_METERED_DISCONNECTED_CUSTOMER_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
	colNames: ['Customer Id', 'Customer Name','Father Name', 'Category', 'Area','Mobile','Status','Created On'],
	colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width(),
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Non-Metered Disconnected Customers",
	onSelectRow: function(id){ 
		getCustomerInfo("comm",id);		
		fetchDisconnectionInfo(id);
		setActiveTabToCustomerReconnHistory(id);
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Reconnect History for the selected Customer>>: It holds reconnection history for the selected customer.
$("#reconn_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.RECONNECTION_SERVICE+'&method='+jsEnum.NON_METER_RECONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id', 'Reconn. Date','Remarks'],
   colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:100,
	                align:'center',
	                sorttype: 'string',
	                search: true
            	},
            	{
	                name: 'reconnect_date',
	                index: 'reconnect_date',
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
   	pager: '#reconn_history_this_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Customer's Reconnection History",
	onSelectRow: function(id){ 
		
		var rowData = $("#reconn_history_this_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchReconnectionInfo(id);
		
   }
}));
jQuery("#reconn_history_this_grid").jqGrid('navGrid','#reconn_history_this_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {setActiveTabToCustomerReconnHistory("");}}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","reconn_history_this_grid",["remarks"]);

//<<Disconnect History for the all Customer>>: It holds disconnection history for all customer
$("#reconn_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.RECONNECTION_SERVICE+'&method='+jsEnum.NON_METER_RECONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Customer Name', 'Reconn. Date','Remarks'],
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
	                name: 'reconnect_date',
	                index: 'reconnect_date',
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
   	pager: '#reconn_history_all_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "All Customer's Reconnection History",
	onSelectRow: function(id){ 
		
		var rowData = $("#reconn_history_all_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchReconnectionInfo(id);
   }
}));
jQuery("#reconn_history_all_grid").jqGrid('navGrid','#reconn_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","reconn_history_all_grid",["customer_name","remarks"]);

//<<Meter list grid>>: It holds all the meters of the selected customer
//This grid code is available in meterListGrid.js file( as it will be needed by several user interfaces

//[END:GRID BLOCK]


//[START : BASIC INITIALIZATION & OVERRIDE] Common settings and override of some configuration for this particular interface
 	//Disable Reading Form "customer_id" as we will not let the user to use this field in this interface.

	$("#disconnect_by").unbind();
	$("#disconnect_by").chosen({no_results_text: "Oops, nothing found!",width: "64%"});
	
	$("#reconnect_by").unbind();
	$("#reconnect_by").chosen({no_results_text: "Oops, nothing found!",width: "64%"});
	
	$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
			serviceUrl: sBox.NON_METERED_DISCONNECTED_CUSTOMER_LIST,
	    	onSelect:function (){getCustomerInfo("comm",$('#comm_customer_id').val());loadMeters($('#comm_customer_id').val());}
	}));
	
	var $dialog = $('<div id="dialog-confirm"></div>')
	.html("<p> "+
	 	"Are you sure you want to delete the Non-Meter Disconnection Information? "+
		"<div id='del_holiday'></div> "+
	   "</p>")
	.dialog({
			title: 'Disconnection Information Delete Confirmation',
			resizable: false,
			autoOpen: false,
			height:150,
			width:450,
			modal: true,
			buttons: {
					"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
						deleteReconnInfo();          
					}},
					"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
						$( this ).dialog( "close" );
					}},
			}
		});


	Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "reconnect_date",
	    trigger    : "reconnect_date"}));
	
//[END : BASIC INITIALIZATION & OVERRIDE]

function setActiveTabToCustomerReconnHistory(customer_id){
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);
    
	var customerId=customer_id;
	if(customer_id=="") //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadReconnHistory(customer_id);
    
}

function reloadReconnHistory(customer_id){

    var ruleArray=[["RECONN_NONMETERED.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("reconn_history_this_grid",ruleArray);
    $("#reconn_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
    reloadGrid("reconn_history_this_grid");
}


function fetchDisconnectionInfo(customer_id){
	  $.ajax({
		    url: 'getNonMeterDisconnInfo.action',
		    type: 'POST',
		    data: {customer_id:customer_id},
		    cache: false,
		    success: function (response) {
		    	
		    	setDisconnInfo(response);
		    	disableButton("btn_save");

		    }
		    
		  });	
}

function fetchReconnectionInfo(reconnection_id){
	
	reconnectForm(disableField,disableChosenField);
	$.ajax({
		    url: 'getNonMeterReconnInfo.action',
		    type: 'POST',
		    data: {pId:reconnection_id},
		    cache: false,
		    success: function (response) {
		    	setDisconnInfo(response.disconnectionInfo);
		    	setReconnInfo(response);
		    	
		    	disableButton("btn_add","btn_save");
		    	enableButton("btn_edit");
		    }
		    
		  });	
}



function setDisconnInfo(data){
	disconnectForm(clearField,clearChosenField);
	reconnectForm(clearField,clearChosenField);
	
	$("#disconnect_cause_str").val(data.disconnect_cause_str);
	$("#disconnect_type_str").val(data.disconnect_type_str);
	$("#disconnect_remarks").val(data.remarks);
	$("#disconnect_date").val(data.disconnect_date);
	$("#meter_sl_no").val(data.meter_sl_no);
	
	$("#disconn_id").val(data.pid);
	$("#disconn_customer_id").val(data.customer_id);
	setChosenData("disconnect_by",data.disconnect_by);	
}
function setReconnInfo(data){
	
	$("#reconnect_date").val(data.reconnect_date);
	$("#reconn_meter_reading").val(data.meter_reading);
	$("#reconn_remarks").val(data.remarks);
	$("#reconn_id").val(data.pid);
	
	setChosenData("reconnect_by",data.reconnect_by);
	
}

function validateAndSaveReconnInfo(){
	
	var validate=false;
	
	validate=validateReconnInfo();
	if(validate==true){
	    
		saveReconnInfo();
			
	}	
}

function validateReconnInfo(){
	
	var isValid=false;	
    isValid=validateField("reconnect_date");				
	return isValid;
}

function saveReconnInfo(){
	
	enableField("disconnect_date");
	readOnlyField("disconnect_date");
	
	var formData = new FormData($('form')[0]);
	 $.ajax({
		    url: 'saveNonMeterReconnInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  	  
		      if(response.status=="OK"){
		    	  disconnectForm(clearField,clearChosenField);
		    	  reconnectForm(clearField,clearChosenField);
		    	  
		    	  reconnectForm(disableField,disableChosenField);
		    	  removeReadOnlyField("disconnect_date","reconnect_date");
		    		
		    	  clearCustomerInfoForm("comm");
		    	  reloadGrid("customer_grid");
		    	  disableButton("btn_save","btn_add");
		      }
		  		
		  	  
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

function cancelDisconnectMeter(){
	getGridBlockUI("meter_grid").unblock();
}

function editButtonPressed(){	
	
	enableField("reconnect_date","reconn_meter_reading","reconn_remarks");
	enableChosenField("reconnect_by");
	enableButton("btn_save");
	disableButton("btn_edit");
	
}

//One can delete a reconnect Meter information if there exist no
//meter reading  information after the meter disconnection reading information.
function deleteReconnInfo(){
	$.ajax({
	    url: 'deleteMeterReconnInfo.action',
	    type: 'POST',
	    data: {pId:$("#reconn_id").val()},
	    cache: false,
	    success: function (response) {
	    	//Clean forms data
	    	disconnectForm(clearField,clearChosenField);
	    	reconnectForm(clearField,clearChosenField);
	    		    	
	    	//Reload Grid data
	    	reloadGrid("disconn_history_this_grid");
	    	reloadGrid("disconn_history_all_grid");
	    	
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	  });
}


function addButtonPressed(){
	reconnectForm(enableField,enableChosenField);
	enableButton("btn_save");
	disableButton("btn_add");
	
}
function disconnectForm(plainFieldMethod,chosenFieldMethod){
	var fields=["disconnect_cause_str","disconnect_type_str","disconnect_remarks","disconnect_date"];
	var chosen_field=["disconnect_by"];
	
	plainFieldMethod.apply(this,fields);	
	chosenFieldMethod.apply(this,chosen_field);
}

function reconnectForm(plainFieldMethod,chosenFieldMethod){
	var fields=["reconnect_date","reconn_meter_reading","reconn_remarks"];
	var chosen_field=["reconnect_by"];
	
	plainFieldMethod.apply(this,fields);	
	chosenFieldMethod.apply(this,chosen_field);
}

function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
	clearReconnectForm();
	clearDisconnInfoForm();
	
}

reconnectForm(disableField,disableChosenField);

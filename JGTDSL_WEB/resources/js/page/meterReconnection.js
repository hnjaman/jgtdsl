//<<Customer Grid>>: It holds all the disconnected meter customer
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.METER_DISCONNECTED_CUSTOMER_LIST+'&extraFilter=area',
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
	caption: "List of Meter Disconnected Customers",
	onSelectRow: function(id){ 
		getCustomerInfo("comm",id);		
		loadDisconnectedMeters(id);
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Reconnect History for the selected Customer>>: It holds reconnection history for the selected customer.
$("#reconn_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.RECONNECTION_SERVICE+'&method='+jsEnum.METER_RECONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Meter No.', 'Reconn. Date','Remarks'],
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
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.RECONNECTION_SERVICE+'&method='+jsEnum.METER_RECONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Customer Name','Meter No.', 'Reconn. Date','Remarks'],
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


//[START : BASIC INITIALIZATION & OVERRIDE] Common settings and override of some configuration for this particular interface
 	//Disable Reading Form "customer_id" as we will not let the user to use this field in this interface.

	$("#disconnect_by").unbind();
	$("#disconnect_by").chosen({no_results_text: "Oops, nothing found!",width: "64%"});
	
	$("#reconnect_by").unbind();
	$("#reconnect_by").chosen({no_results_text: "Oops, nothing found!",width: "64%"});
	
	$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
			serviceUrl: sBox.METERED_DISCONNECTED_CUSTOMER_LIST,
	    	onSelect:function (){
	    		getCustomerInfo("comm",$('#comm_customer_id').val());
	    		loadDisconnectedMeters($('#comm_customer_id').val());}
	}));
	
	var $dialog = $('<div id="dialog-confirm"></div>')
	.html("<p> "+
	 	"Are you sure you want to delete the Meter Reconnection Information? "+
		"<div id='del_holiday'></div> "+
	   "</p>")
	.dialog({
			title: 'Meter Reconnection Information Delete Confirmation',
			resizable: false,
			autoOpen: false,
			height:150,
			width:450,
			modal: true,
			buttons: {
					"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
						deleteMeterReconnInfo();          
					}},
					"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
						$( this ).dialog( "close" );
					}},
			}
		});
	


	Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "reconnect_date",
	    trigger    : "reconnect_date"}));
	


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

	var reConnUrl= jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.RECONNECTION_SERVICE+'&method='+jsEnum.METER_RECONNECTION_LIST;
    var ruleArray=[["RECONN_METERED.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("reconn_history_this_grid",ruleArray);
    $("#reconn_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json',url:reConnUrl});    	
   	reloadGrid("reconn_history_this_grid");
}

//Load the available meters for the selected customer [either via typing customer code in the auto complete box or via selecting the customer grid] 	
function loadDisconnectedMeters(customer_id){
   	var ruleArray=[["customer_id","status"],["eq","eq"],[customer_id,"0"]];
	var postdata=getPostFilter("meter_grid",ruleArray);
	var meterListUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_SERVICE+'&method='+jsEnum.DISCONNECED_METER_LIST;
	
	var newRowAttr= function (rd) {}
	var newB4SelectRow=function(rowid, e) {return true;}
	var newOnSelectRow=function(id){fetchDisconnectionInfo(id);enableButton("btn_add");disableButton("btn_edit","btn_delete");}
	
	$("#meter_grid").jqGrid( "setGridParam", { rowattr:null,beforeSelectRow:null,onSelectRow: null } );
   	$("#meter_grid").jqGrid('setGridParam',{search: true,postData: postdata,url:meterListUrl,page:1,datatype:'json',rowattr:newRowAttr,beforeSelectRow:newB4SelectRow,onSelectRow:newOnSelectRow});    	
   	$("#meter_grid").jqGrid('setCaption', 'List of Disconnected Meters');	
	reloadGrid("meter_grid");
}

$("#meter_grid").jqGrid( "setGridParam", { onSelectRow: null } );
$('#meter_grid').on('jqGridSelectRow', function (event, id, selected) {
	reconnInfoForm(clearField,clearChosenField);
	disconnInfoForm(clearField,clearChosenField);
	
	var customer_id=getFieldValueFromSelectedGridRow("meter_grid","customer_id");
	setActiveTabToCustomerReconnHistory(customer_id);
	
});

function fetchDisconnectionInfo(meter_id){
	

	  $.ajax({
		    url: 'getMeterDisconnInfo.action',
		    type: 'POST',
		    data: {meter_id:meter_id},
		    cache: false,
		    success: function (response) {
		    	setDisconnInfo(response);
		    }
		    
		  });	
}

function fetchReconnectionInfo(reconnection_id){
	
	reconnInfoForm(disableField,disableChosenField);
	disableChosenField("reconnect_by");
	blockGrid("meter_grid");
	
	$.ajax({
		    url: 'getMeterReconnInfo.action',
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
	$("#disconnect_cause_str").val(data.disconnect_cause_str);
	$("#disconnect_type_str").val(data.disconnect_type_str);
	$("#disconnect_remarks").val(data.remarks);
	$("#disconnect_date").val(data.disconnect_date);
	$("#meter_sl_no").val(data.meter_sl_no);
	
	if(typeof data.reading === 'undefined'){
		$("#meter_reading").val(data.meter_reading);
		$("#disconn_reading_id").val(data.reading_id);
		
	}
	else{
		$("#meter_reading").val(data.reading.curr_reading);
		$("#disconn_reading_id").val(data.reading_id);
		
	}
	
	$("#disconn_id").val(data.pid);
	$("#disconn_customer_id").val(data.customer_id);
	$("#disconn_meter_id").val(data.meter_id);
	
	setChosenData("disconnect_by",data.disconnect_by);	
}
function setReconnInfo(data){
	
	$("#reconnect_date").val(data.reconnect_date);
	$("#reconn_meter_reading").val(data.meter_reading);
	$("#reconn_remarks").val(data.remarks);
	$("#reconn_id").val(data.pid);
	
	setChosenData("reconnect_by",data.reconnect_by);
	
}

function validateAndSaveMeterReconnInfo(){
	
	var validate=false;
	
	validate=validateReconnInfo();
	if(validate==true){
	    
		if(isValidPositiveNumber($("#reconn_meter_reading").val())==true && $("#reconn_meter_reading").val()>=$("#meter_reading").val())
				saveMeterReconnInfo();
		else{
			alert("Please provide valid meter reading information");
		}
			
	}	
}

function validateReconnInfo(){
	
	var isValid=false;	
    isValid=validateField("reconnect_date","reconn_meter_reading");				
	return isValid;
}

function saveMeterReconnInfo(){
	
	enableField("disconnect_date","disconn_id","disconn_reading_id","disconn_customer_id","disconn_meter_id");
	readOnlyField("disconnect_date");
	
	var formData = new FormData($('form')[0]);
	 $.ajax({
		    url: 'saveMeterReconnInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  	  
		      if(response.status=="OK"){
		    	  disconnInfoForm(clearField,clearChosenField);
		    	  disconnInfoForm(disableField,disableChosenField);
		    	  
		    	  reconnInfoForm(clearField,clearChosenField);		    	  
		    	  removeReadOnlyField("disconnect_date");
		    	  
		    	  disableField("disconnect_date");
		    		
		    	  clearCustomerInfoForm("comm");
		    	  reloadGrid("meter_grid");
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
function deleteMeterReconnInfo(){
	$.ajax({
	    url: 'deleteMeterReconnInfo.action',
	    type: 'POST',
	    data: {pId:$("#reconn_id").val()},
	    cache: false,
	    success: function (response) {
	    	//Clean forms data
	    	disconnInfoForm(clearField,clearChosenField);
	    		    	
	    	//Reload Grid data
	    	loadMeters($("#comm_customer_id").val());
	    	reloadGrid("disconn_history_this_grid");
	    	reloadGrid("disconn_history_all_grid");
	    	
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	  });
}



function addButtonPressed(){
	reconnInfoForm(enableField,enableChosenField);
	enableButton("btn_save");
	disableButton("btn_add");
	
}
function disconnInfoForm(plainFieldMethod,chosenFieldMethod){
	
	var fields = ["disconnect_cause_str","disconnect_type_str","disconnect_date","meter_sl_no","meter_reading","disconnect_remarks","disconn_id","disconn_reading_id","disconn_customer_id","disconn_meter_id"];
	var chosen_field=["disconnect_by"];
	disconnect_by
	plainFieldMethod.apply(this,fields);
	if(chosenFieldMethod!=null)
		chosenFieldMethod.apply(this,chosen_field);	
	
}
function reconnInfoForm(plainFieldMethod,chosenFieldMethod){

	var fields = ["reconnect_date","reconn_meter_reading","reconn_remarks","reconn_id"];
	var chosen_field=["reconnect_by"];
	
	plainFieldMethod.apply(this,fields);
	if(chosenFieldMethod!=null)
		chosenFieldMethod.apply(this,chosen_field);	
	
}
function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
	clearGridData("meter_grid");
	reconnInfoForm(clearField,clearChosenField);
	disconnInfoForm(disableField,disableChosenField);
	
}

reconnInfoForm(disableField,disableChosenField);
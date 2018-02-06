var cType="0:Load Change;1:Pressure Change;2:Load & Pressure Change";  //Change Type

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
	width: $("#customer_grid_div").width()+10,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Metered Customers",
	onSelectRow: function(id){ 
		$("#customer_id").val(id);
		getCustomerInfo("comm",id,setLoadInfo);		
		loadMeters(id);
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Load-Pressure Change History for the selected Customer>>
$("#lpChange_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.LOAD_PRESSURE_CHANGE_SERVICE+'&method='+jsEnum.LOAD_PRESSURE_CHANGE_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Meter No.','Change Type', 'Change Date','Remarks'],
   colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:100,
	                align:'center',
	                sorttype: 'string',
	                search: false
            	},
            	{
	                name: 'meter_sl_no',
	                index: 'meter_sl_no',
	                sorttype: "string",
	                search: true
            	},
            	{
	                name: 'change_type_str',
	                index: 'change_type_str',
	                sorttype: "string",
	                search: true,
	                edittype: "select",
	                formatter: "select",
	                stype:"select", 
	                editoptions: { value: cType },
	                searchoptions: { 
	                	value: cType, 
	                	defaultValue: "0" 
	                }
            	},
            	{
	                name: 'effective_date',
	                index: 'effective_date',
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
   	pager: '#lpChange_history_this_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Customer's Load-Pressure Change History",
	onSelectRow: function(id){ 		
		getLPchangeInfo(id);
		
   }

}));
jQuery("#lpChange_history_this_grid").jqGrid('navGrid','#lpChange_history_this_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {setActiveTabToCustomerLPchangeHistory("");}}),{},{},{},
		{
		multipleSearch:true,
		onSearch: function () {	
			
			   var ruleArray=[["LPC.CUSTOMER_ID"],["eq"],[$("#comm_customer_id").val()]];
			   var oldRules=["change_type_str"];
			   var newRules=["change_type"];			   
			   modifyGridPostData("lpChange_history_this_grid",ruleArray,oldRules,newRules);	
		   }		
		}

);
gridColumnHeaderAlignment("left","lpChange_history_this_grid",["full_name","disconnect_cause_name","disconnect_type_name","remarks"]);

//<<Load Pressure Change History for the all Customer>>
$("#lpChange_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.LOAD_PRESSURE_CHANGE_SERVICE+'&method='+jsEnum.LOAD_PRESSURE_CHANGE_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Customer Name','Meter No.','Change Type', 'Change Date','Remarks'],
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
	                name: 'change_type_name',
	                index: 'change_type_name',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'effective_date',
	                index: 'effective_date',
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
   	pager: '#lpChange_history_all_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Load-Pressure Change History",
	onSelectRow: function(id){ 		
		var rowData = $("#lpChange_history_all_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);		
		loadMeters(rowData.customer_id);
		getLPchangeInfo(id);
		
   }
}));
jQuery("#lpChange_history_all_grid").jqGrid('navGrid','#lpChange_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","lpChange_history_all_grid",["full_name","customer_name","disconnect_cause_name","disconnect_type_name","remarks"]);

	disableField("customer_id");
	//Remove the address information of the customer here. Due to space issue.
	$("#common_fh_row").remove();
	$("#common_address_row").remove();
	
	$("#change_by").unbind();
	$("#change_by").chosen({no_results_text: "Oops, nothing found!",width: "71%"});

	//Recreate the button block and override it. Because we will not use the default buttons of Reading Entry Form.
	
	var buttons="<table width='100%'>"+
	"<tr>"+
	"	<td width='55%' align='left'>"+
	"		<button class='btn btn-beoro-3' type='button' id='btn_edit' onclick='editButtonPressed()' disabled='disabled'><span class='splashy-application_windows_edit'></span>"+
	"		Edit</button>"+
	"		<button class='btn btn-beoro-3' type='button' id='btn_save' onclick='validateAndSaveLpChangeInfo()' disabled='disabled'><span class='splashy-document_letter_okay'></span>"+
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
	    	onSelect:function (){
				getCustomerInfo("comm",$('#comm_customer_id').val(),setLoadInfo);
				$("#customer_id").val($('#comm_customer_id').val());
				loadMeters($('#comm_customer_id').val());
			}
	}));
	
	function setLoadInfo(data){
		$("#old_min_load").val(data.connectionInfo.min_load);
		$("#old_max_load").val(data.connectionInfo.max_load);		
	}

	var $dialog = $('<div id="dialog-confirm"></div>')
	.html("<p> "+
	 	"Are you sure you want to delete the Load/Pressure Change Information? "+
		"<div id='del_holiday'></div> "+
	   "</p>")
	.dialog({
			title: 'Load/Pressure Change Delete Confirmation',
			resizable: false,
			autoOpen: false,
			height:150,
			width:450,
			modal: true,
			buttons: {
					"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
						deleteLoadPressureChangeInfo();          
					}},
					"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
						$( this ).dialog( "close" );
					}},
			}
		});
		
	Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "effective_date",
	    trigger    : "effective_date",
	    onSelect   : function() { this.hide(); $("#curr_reading_date").val($("#effective_date").val());fetchGasPrice($("#effective_date").val());}
	}));
	

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
	
	loadPressureChangeForm(clearField,clearChosenField);
	getCustomerInfo("",$("#comm_customer_id").val(),setLoadInfo);	
	
	getMeterList4LPchange(id);  //getMeterListForLoadPressurechange => 4LP->ForLoadPressure
	
});

function getMeterList4LPchange(meter_id){
	$("#reading_purpose_str").val("2");
	$("#curr_reading_date").val(getCurrentDate());	
	$("#effective_date").val(getCurrentDate());
	$("#billing_month").val(getCurrentMonth());
	$("#billing_year").val(getCurrentYear());
	getMeterList(meter_id,getReadingInfo4LPchange);	
}

function getReadingInfo4LPchange(){
	//disableField("meter_id","reading_purpose_str","curr_reading_date");
	fetchReadingInfo(readingCallBack4LPchange);	
}

function readingCallBack4LPchange(data){
	readingCallBack(data);
	if(data.length>0){
	    var reading=data[0];
	    $("#old_pressure").val(reading.pressure);
	}
	changeReadingFormMode("data_entry_mode","2");
	enableField("billing_month","billing_year");
	disableField("curr_reading_date");
	
	var customer_id=getFieldValueFromSelectedGridRow("meter_grid","customer_id");
	setActiveTabToCustomerLPchangeHistory(customer_id);
	
	enableButton("btn_save");
}

function setActiveTabToCustomerLPchangeHistory(customer_id){
	
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);
    
	var customerId=customer_id;
	if(customer_id=="") //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadLoadPressureChangeHistory(customerId);
    
}

function reloadLoadPressureChangeHistory(customer_id){
	var ruleArray=[["LPC.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("lpChange_history_this_grid",ruleArray);
    $("#lpChange_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    
    reloadGrid("lpChange_history_this_grid");
    reloadGrid("lpChange_history_all_grid");
}

function getLPchangeInfo(change_id){
	
	loadPressureChangeForm(clearField,clearChosenField);
	loadPressureChangeForm(disableField,disableChosenField);
	blockGrid("meter_grid");
	
	  $.ajax({
		    url: 'getLoadPressureChangeInfo.action',
		    type: 'POST',
		    data: {pId:change_id},
		    cache: false,
		    success: function (response) {

		    	setLoadPressureInfo(response);
		    	setReadingInfo(response.reading);
		    	setCustomerInfo("",response.customer);
		    	changeReadingFormMode("disable_mode",null);
		    	disableButton("btn_save");
		    	
		    	
		    	if (response.reading.bill_id === undefined ){		    		
		    		enableButton("btn_delete","btn_edit");		    		
		    	}
		    	else{
				    disableButton("btn_delete","btn_edit");
		    	}
		    	
		    	
		    	$("#reading_id").val(response.reading.reading_id);
		    	/*		    	
		    	/// Need to move from here...
		    	$("#reading_purpose_str").val(response.reading.reading_purpose_str);
		    	$("#billing_month").val(response.reading.billing_month);
		    	$("#billing_year").val(response.reading.billing_year);
		    	
		    	///
		    	*/
		    }
		    
		  });	
}

function setLoadPressureInfo(data){
	$("#change_type_str").val(data.change_type_str);
	$("#effective_date").val(data.effective_date);
	$("#old_pressure").val(data.old_pressure);
	$("#new_pressure").val(data.new_pressure);
	$("#old_min_load").val(data.old_min_load);
	$("#old_max_load").val(data.old_max_load);
	$("#new_min_load").val(data.new_min_load);
	$("#new_max_load").val(data.new_max_load);
	$("#lp_remarks").val(data.remarks);	
	setChosenData("change_by",data.change_by);	
	$("#pid").val(data.pid);
}

function validateAndSaveLpChangeInfo(){
	
	var validateLP=false;
	var validateReading=false;
	
	validateLP=validateLoadPressure();
	validateReading=validateMeterReadingInfo();
	if(validateLP==true && validateReading==true){
	
		saveLoadPressureChangeInfo();
	}	
}

function validateLoadPressure(){
	
	var isValid=validateField("change_type_str","effective_date");	
	if(isValid==true){
		if(change_type_str=="0"){
			isValid=validateField("new_min_load","new_max_load");	
		}
		else if(change_type_str=="1"){
			isValid=validateField("new_pressure");	
		}
		else if(change_type_str=="2"){
			isValid=validateField("new_min_load","new_max_load","new_pressure");	
		}
	}
	return isValid;
}


function saveLoadPressureChangeInfo(){
	
	changeReadingFormMode("data_submit_mode",null);
	enableField("customer_id");
	
	var formData = new FormData($('form')[0]);
	var readingData = jQuery(document.forms['meterReadingForm']).serializeArray();
	for (var i=0; i<readingData.length; i++)
		formData.append(readingData[i].name, readingData[i].value);
	
	
	
	  $.ajax({
		    url: 'saveLoadPressureChangeInfo.action',
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
			  		loadPressureChangeForm(disableField,disableChosenField);		  		

			  		reloadLoadPressureChangeHistory($("#comm_customer_id").val());
			   		enableButton("btn_edit");
			   		disableButton("btn_save");
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

function cancelButtonPressed(){
	unBlockGrid("meter_grid");
	clearRelatedData();
	resetSelection("meter_grid","customer_grid","lpChange_history_this_grid","lpChange_history_all_grid");
}

function editButtonPressed(){	
	changeReadingFormMode("data_entry_mode","2");
	enableField("billing_month","billing_year");
	disableField("curr_reading_date");
	
	loadPressureChangeForm(enableField,enableChosenField);
	
	enableButton("btn_save");
	disableButton("btn_edit");
	
}

function deleteLoadPressureChangeInfo(){
	$.ajax({
	    url: 'deleteLoadPressureChagneInfo.action',
	    type: 'POST',
	    data: {pId:$("#pid").val()},
	    cache: false,
	    success: function (response) {
	    	//Clean forms data
	    	$dialog.dialog("close");
	    	loadPressureChangeForm(clearField,clearChosenField);
	    	clearMeterReadingForm();
	    		    	
	    	//Reload Grid data
	    	reloadGrid("#lpChange_history_this_grid");
	    	reloadGrid("#lpChange_history_all_grid");
	    	
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	  });
}

/** New **/
function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);	
	clearField.apply(this,customer_info_field);	
	clearGridData("meter_grid");
	loadPressureChangeForm(clearField,clearChosenField);
	clearMeterReadingForm();
	
}

function loadPressureChangeForm(plainFieldMethod,chosenFieldMethod){
	var fix_field=["change_type_str","effective_date","lp_remarks"];
	var entry_field=["old_pressure","new_pressure","old_min_load","old_max_load","new_min_load","new_max_load"];
	var chosen_field=["change_by"];
	
	plainFieldMethod.apply(this,fix_field);	
	plainFieldMethod.apply(this,entry_field);
	chosenFieldMethod.apply(this,chosen_field);
}

function controlChangeType(change_type){
	
	if(change_type==""){
		$("#load_change_row").hide();
		$("#pressure_change_row").hide();
	}
	else if(change_type=="0"){
		$("#load_change_row").show();
		$("#pressure_change_row").hide();
	}
	else if(change_type=="1"){
		$("#load_change_row").hide();
		$("#pressure_change_row").show();
	}
	else if(change_type=="2"){
		$("#load_change_row").show();
		$("#pressure_change_row").show();
	}
}


/*
$("#billing_month").removeAttr('onchange'); 
$("#billing_year").removeAttr('onchange'); 
$("#billing_month").change(function(){ });
$("#billing_year").change(function(){}); 
*/

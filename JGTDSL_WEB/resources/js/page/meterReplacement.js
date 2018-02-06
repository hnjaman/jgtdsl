var old_fields=["meter_sl_no","meter_mfg","meter_year","measurement_type_str","meter_type","g_rating","conn_size","max_reading","ini_reading","pressure","temperature","meter_rent","installed_by_str","meter_remarks","installed_date","evc_sl_no","evc_model","evc_year"];
var new_fields=["new_meter_sl_no","new_meter_mfg","new_meter_year","new_measurement_type_str","new_meter_type","new_g_rating","new_conn_size","new_max_reading","new_ini_reading","new_pressure","new_temperature","new_meter_rent","new_installed_by","new_meter_remarks","new_installed_date","new_evc_sl_no","new_evc_model","new_evc_year"];
var new_fields_without_evc=["new_meter_sl_no","new_meter_mfg","new_meter_year","new_measurement_type_str","new_meter_type","new_g_rating","new_conn_size","new_max_reading","new_ini_reading","new_pressure","new_temperature","new_meter_rent","new_installed_by","new_meter_remarks","new_installed_date"];

//<<List of Metered Customer>>
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.METERED_CUSTOMER_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
	colNames: ['Customer Id', 'Customer Name','Father Name', 'Category', 'Area','Mobile','Status','Created On'],
	colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-52,
	width: $("#customer_grid_div").width()-24,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Metered Customers",
	onSelectRow: function(id){ 
		getCustomerInfo("comm",id);		
		loadMeters(id,meterLoadPreOperation);
		enableButton("btn_add","btn_edit","btn_delete");
		setActiveTabToReplacementHistory(id);
   }
}));

jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("collection_grid",["full_name"],"left");


//<<Meter Replacement History for the selected Customer>>
$("#replacement_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_REPLACEMENT_SERVICE+'&method='+jsEnum.METER_REPLACEMENT_LIST+'&extraFilter=area',
 	jsonReader: {
          repeatitems: false,
          id: "pid"
	},
 colNames: ['Customer Id','Old Meter SL.', 'New Meter SL.','Replacement Date'],
 colModel: [{
		       name: 'customer_id',
		       index: 'customer_id',
		       width:70,
		       align:'center',
		       sorttype: 'string',
		       search: false
			},
			{
		       name: 'old_meter_sl_no',
		       index: 'old_meter_sl_no',
		       width:200,
		       sorttype: 'string',
		       search: true
			},
			{
		       name: 'new_meter_sl_no',
		       index: 'new_meter_sl_no',
		       sorttype: "string",
		       search: true,
			},
			{
		       name: 'replacement_date',
		       index: 'replacement_date',
		       sorttype: "string",
		       search: true,
			}
      ],
	//datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
 	pager: '#replacement_history_this_grid_pager',
 	sortname: 'TMP1.customer_id',
 	sortorder: "asc",
	caption: "Customer's Meter Replacement History",
	onSelectRow: function(id){ 
		
		var rowData = $("#replacement_history_this_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchReplacementInfo(id);
		
 }
}));
jQuery("#replacement_history_this_grid").jqGrid('navGrid','#replacement_history_this_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {setActiveTabToReplacementHistory("");}}),{},{},{},
		{
		multipleSearch:true,
		onSearch: function () {	
				
			   var ruleArray=[["METER_RENT_CHANGE.CUSTOMER_ID"],["eq"],[$("#comm_customer_id").val()]];
			   var oldRules=["customer_id"];
			   var newRules=["METER_RENT_CHANGE.customer_id"];			   
			   modifyGridPostData("meterRent_change_history_this_grid",ruleArray,oldRules,newRules);	
		   }
		
		});
gridColumnHeaderAlignment("left","replacement_history_this_grid",["remarks"]);



//<<Meter Rent History for the all Customer>>
$("#replacement_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_REPLACEMENT_SERVICE+'&method='+jsEnum.METER_REPLACEMENT_LIST+'&extraFilter=area',
 	jsonReader: {
          repeatitems: false,
          id: "pid"
	},
	colNames: ['Customer Id','Old Meter SL.', 'New Meter SL.','Replacement Date'],
	 colModel: [{
			       name: 'customer_id',
			       index: 'customer_id',
			       width:70,
			       align:'center',
			       sorttype: 'string',
			       search: false
				},
				{
			       name: 'old_meter_sl_no',
			       index: 'old_meter_sl_no',
			       width:200,
			       sorttype: 'string',
			       search: true
				},
				{
			       name: 'new_meter_sl_no',
			       index: 'new_meter_sl_no',
			       sorttype: "string",
			       search: true,
				},
				{
			       name: 'replacement_date',
			       index: 'replacement_date',
			       sorttype: "string",
			       search: true,
				}
	      ],
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
 	pager: '#replacement_history_all_grid_pager',
 	sortname: 'TMP1.customer_id',
  	sortorder: "asc",
	caption: "All Customers Replacement History",
	onSelectRow: function(id){ 
		
		var rowData = $("#replacement_history_all_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchReplacementInfo(id);
 }
}));
jQuery("#replacement_history_all_grid").jqGrid('navGrid','#replacement_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","replacement_history_all_grid",["customer_name","remarks"]);

$("#new_installed_by").unbind();
$("#new_installed_by").chosen({no_results_text: "Oops, nothing found!",width: "55.5%"});
$("#common_address_row").remove();
$("#comm_customer_id").unbind();
$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	
    	onSelect:function (){getCustomerInfo("comm",$("#comm_customer_id").val());
    	loadMeters($("#comm_customer_id").val(),meterLoadPreOperation);
    	enableButton("btn_add","btn_edit","btn_delete");
		},
}));

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "new_installed_date",
    trigger    : "new_installed_date"}));

var $dialog = $('<div id="dialog-confirm"></div>')
.html("<p> "+
 	"Are you sure you want to delete the Meter Replacement Information?"+
	"<div id='del_holiday'></div> "+
   "</p>")
.dialog({
		title: 'Meter Replacement Delete Confirmation',
		resizable: false,
		autoOpen: false,
		height:150,
		width:450,
		modal: true,
		buttons: {
				"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
					deleteMeterReplacementInfo();          
				}},
				"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
					$( this ).dialog( "close" );
				}},
		}
	});

function deleteMeterReplacementInfo(){
	$.ajax({
	    url: 'deleteMeterReplacementInfo.action',
	    type: 'POST',
	    data: {pId:$("#pid").val()},
	    cache: false,
	    success: function (response) {
	    	oldMeterInfoForm(clearField);
	    	newMeterInfoForm(clearField,clearChosenField);
	    	$dialog.dialog("close");
	    	reloadGrid("replacement_history_this_grid");
	    	reloadGrid("replacement_history_all_grid");
	    	reloadGrid("meter_grid");
	    	unBlockGrid("meter_grid");
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	  });
}


function setActiveTabToReplacementHistory(customer_id){
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);

	var customerId=customer_id;
	if(customer_id=="") //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadReplacementHistory(customerId);
    
}

function reloadReplacementHistory(customer_id){

    var ruleArray=[[" TMP1.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("replacement_history_this_grid",ruleArray);
    $("#replacement_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("replacement_history_this_grid");
   	reloadGrid("replacement_history_all_grid");
}
function fetchReplacementInfo(replacement_id){
	
	blockGrid("meter_grid");	
	$.ajax({
		    url: 'getMeterReplacementInfo.action',
		    type: 'POST',
		    data: {pId:replacement_id},
		    cache: false,
		    success: function (response) {
		    	setReplacementInfo(response);	
		    	$("#pid").val(response.pid);
		    	disableButton("btn_add","btn_save");
		    	enableButton("btn_edit","btn_delete");
		    
		    }
		    
		  });	
}

function setReplacementInfo(data){
	setMeterInfo(data.oldMeter);
	setMeterInfo(data.newMeter,"new");
	
}

function validateAndSaveMeterReplacementInfo(){
	
	var validate=false;
	
	validate=validateMeterReplacementInfo();
	if(validate==true){	    
		saveMeterReplacementInfo();							
	}	
}


function validateMeterReplacementInfo(){
	  var isValid; 	  
	  var validationFields;
	  
	  if($("#new_measurement_type_str").val()=="1"){
		  validationFields=$.extend( true, {}, new_fields);
		  validationFields=Object.keys(validationFields).map(function(k) { return validationFields[k] }); // Making the object to array object
		  validationFields.push("comm_customer_id");
	  	  isValid=validateField.apply(this,validationFields);
	  }
	  else{
		  validationFields = $.extend( true, {}, new_fields_without_evc ); // Deep Copy of object in JavaScript	  
		  validationFields=Object.keys(validationFields).map(function(k) { return validationFields[k] }); // Making the object to array object
		  validationFields.push("comm_customer_id");
	  	  isValid=validateField.apply(this,validationFields);
	  	  clearField("evc_sl_no","evc_year","evc_model");
	  	 }
	 	  	  	
	  $("#customer_id").val($("#comm_customer_id").val());
	  return isValid;
}


function saveMeterReplacementInfo()
{
	
	
	
	
	
	$("#customer_id").val($("#comm_customer_id").val());
	var formData = new FormData($('form')[0]);
	  $.ajax({
	    url: 'saveMeterReplacementInfo.action',
	    type: 'POST',
	    data: formData,
	    async: false,
	    cache: false,
	    contentType: false,
	    processData: false,
	    success: function (response) {
	    
	    if(response.status=="OK"){
	    	  newMeterInfoForm(clearField,clearChosenField);
	    	  newMeterInfoForm(disableField,disableChosenField);
	    	  disableButton("btn_save","btn_add","btn_delete");
	    	  reloadGrid("meter_grid");
	    	  reloadReplacementHistory($("#customer_id").val($("#comm_customer_id").val()));
	      }
	   
	   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		
	    
	    }
	  });
			
	
}


function showHideEvc(prefix,measurementType){
	if(measurementType=="" || measurementType=="0")
   		$("#"+prefix+"evc_div").hide();
 	else
 		$("#"+prefix+"evc_div").show();
}


function addButtonPressed(){
	if($("#comm_customer_id").val()==""){
		var message="Please select a customer.";
		showDialog("Information",message);
	}
	else{
		
		newMeterInfoForm(clearField,clearChosenField);
		newMeterInfoForm(enableField,enableChosenField);
		
		enableButton("btn_save");
		disableButton("btn_add","btn_edit");	
	}
}

function cancelButtonPressed(){
	clearRelatedData();
	newMeterInfoForm(disableField,disableChosenField);
	disableButton("btn_add","btn_edit","btn_save","btn_delete");
	unBlockGrid("meter_grid");
	resetSelection("meter_grid","customer_grid");
}

function clearRelatedData(){
	clearField.apply(this,comm_customer_info_field);	
	clearGridData("meter_grid");
	oldMeterInfoForm(clearField);
	newMeterInfoForm(clearField,clearChosenField)	
	disableField("btn_edit","btn_delete");
	clearChosenField("installed_by");
}

function oldMeterInfoForm(plainFieldMethod){
			 
	plainFieldMethod.apply(this,old_fields);
}
function newMeterInfoForm(plainFieldMethod,chosenFieldMethod){
	
	var chosen_field=["new_installed_by"];
		 
	plainFieldMethod.apply(this,new_fields);
	if(chosenFieldMethod!=null)
		chosenFieldMethod.apply(this,chosen_field);	
}
focusNext("comm_customer_id");
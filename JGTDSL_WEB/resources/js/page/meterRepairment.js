jQuery("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.METERED_CUSTOMER_LIST,
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
	colNames: ['Customer Id', 'Customer Name','Father Name', 'Category', 'Area','Mobile','Status','Created On'],
	colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-110,
	width: $("#customer_grid_div").width()+10,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
   	sortorder: "asc",
	caption: "List of Available Metered Customers",
	onSelectRow: function(id){ 
		getCustomerInfo("comm",id);
		$("#customer_id").val($("#comm_customer_id").val());
		loadMeters(id,meterLoadPreOperation);
		meterRepairmentForm(clearField,clearChosenField);
		meterRepairmentForm(disableField,disableChosenField);
   }
}));

jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',{del:false,add:false,edit:false},{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","remarks"]);


//<<Meter Repairment History for the selected Customer>>
$("#meterRent_change_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_REPAIRMENT_SERVICE+'&method='+jsEnum.METER_RENT_CHANGE_LIST,
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


//<<Meter Repairment History for the all Customer>>
$("#meterRent_change_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_RENT_SERVICE+'&method='+jsEnum.METER_RENT_CHANGE_LIST,
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


function setActiveTabToMeterRentChangeHistory(customer_id){
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);

	var customerId=customer_id;
	if(customer_id=="") 
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadMeterRepairmentHistory(customerId);
    
}

function reloadMeterRepairmentHistory(customer_id){

    var ruleArray=[[" METER_RENT_CHANGE.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("meterRent_change_history_this_grid",ruleArray);
    $("#repairment_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("repairment_history_this_grid");
   	reloadGrid("repairment_history_all_grid");
}
    
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "prev_reading_date",
    trigger    : "prev_reading_date"}));

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "curr_reading_date",
    trigger    : "curr_reading_date"}));
$("#repaired_by").unbind();
$("#repaired_by").chosen({no_results_text: "Oops, nothing found!",width: "78.5%"});


unbindKeyPress();

function validateAndSubmitRepairment()
{
	var validate=false;
	
	validate=validateRepairmentInfo();
	if(validate==true){	    
		saveRepairmentInfo();							
	}		
}

function validateRepairmentInfo(){
	var isValid=false;	
    isValid=validateField("prev_reading","prev_reading_date","curr_reading","curr_reading_date");	
    if(isValid==true){    	
    	if($("#meter_id").val()==""){
    		alert("Please select a meter.");
    		return false;
    	}
    }
    
	return isValid;
}


function saveRepairmentInfo(){
	var formData = new FormData($('form')[0]);
	 $.ajax({
		    url: 'saveMeterRepairmentInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  	  
		      if(response.status=="OK"){
		    	  meterRepairmentForm(clearField,clearChosenField);
		    	  meterRepairmentForm(disableField,disableChosenField);
		    	  disableButton("btn_save","btn_add","btn_delete");
		    	  reloadMeterRepairmentHistory($("#comm_customer_id").val());
		      }
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

$("#comm_customer_id").unbind();
$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
    	onSelect:function (){getCustomerInfo($('#comm_customer_id').val());loadMeters($('#comm_customer_id').val(),meterLoadPreOperation);$("#customer_id").val($("#comm_customer_id").val());},
}));

$("#curr_reading_date").val(getCurrentDate());

function callRepairment(id){
		loadRepairmentInfo(id);
		
		disableButton("btn_add_repair");
		enableButton("btn_edit_repair");
		disableField("curr_reading","curr_reading_date","repairment_remarks","repaired_by");
		$("#repaired_by").trigger("chosen:updated");

}
function loadRepairmentInfo(repair_id){

	$.ajax({
   		  type: 'POST',
   		  url: 'getRepairInfo.action?repair.repair_id='+repair_id,
   		  success:function(data){
			if(typeof data.repair_id === "undefined")
			{
			  alert("Sorry, this is not either a valid customer or a customer whose connection has not yet been established.");
			}
			else
			{
				$("#prev_reading").val(data.prev_reading);
				$("#prev_reading_date").val(data.prev_reading_date);
				$("#curr_reading").val(data.curr_reading);
				$("#curr_reading_date").val(data.curr_reading_date);
				$("#repairment_remarks").val(data.remarks);
				
				var repaired_by_array=data.repaired_by.split(", ");
				$('#repaired_by').val(repaired_by_array).trigger('chosen:updated');
				
				$("#repair_id").val(data.repair_id);
				$("#reading_id").val(data.reading_id);
			}
						
   		  },
   		  error:function(){
   			
   		  }
   	});

}

function editRepairInfo()
{
		disableButton("btn_add_repair","btn_edit_repair");
		enableButton("btn_save");
		enableField("curr_reading","curr_reading_date","repairment_remarks","repaired_by");
		$("#repaired_by").trigger("chosen:updated");
}
//$("#repaired_by").attr("disabled", "disabled").trigger("chosen:updated");

/***********/
function addButtonPressed(){
	if($("#comm_customer_id").val()==""){
		var message="Please select a customer.";
		showDialog("Information",message);
	}
	else{
		meterRepairmentForm(enableField,enableChosenField);
		enableButton("btn_save");
		disableButton("btn_add");	
	}
}
function meterInfoForm(plainFieldMethod){
	var meter_info_field = ["meter_sl_no","meter_rent","meter_year","measurement_type_str","conn_size","max_reading","ini_reading","pressure","temperature"];
	plainFieldMethod.apply(this,meter_info_field);
}
function meterRepairmentForm(plainFieldMethod,chosenFieldMethod){
	var meter_repair_field = ["prev_reading","prev_reading_date","curr_reading","curr_reading_date","repaired_by","repairment_remarks","meter_id","repair_id","reading_id","measurement_type_str","customer_id"];
	var chosen_field=["repaired_by"];
	plainFieldMethod.apply(this,meter_repair_field);
	if(chosenFieldMethod!=null)
		chosenFieldMethod.apply(this,chosen_field);	
}
function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
	clearGridData("meter_grid");
	meterInfoForm(clearField);
	meterRepairmentForm(clearField,clearChosenField);
}
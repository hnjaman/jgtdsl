//<<Customer Grid>>: List of Metered Customer
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.CUSTOMER_LIST+'&extraFilter=area',
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
		setTimeout(setMeterStatus,200);
		getDueMonthList(id);
		setActiveTabToCustomerTypeChangeHistory(id);
		CustomerTypeChangeForm(clearField);
		//CustomerTypeChangeForm(disableField);
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Meter Rent History for the selected Customer>>
$("#type_change_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.CustomerTypeChangeService&method=getCustomerTypeChangeList',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['pid','Customer Code','Old Status', 'New Status','Change Date','Remarks'],
   colModel: [{
		       name: 'pid',
		       index: 'pid',
		       width:70,
		       align:'center',
		       sorttype: 'string',
		       search: false,
		       hidden: true
			},
			{
			   name: 'customer_id',
			   index: 'customer_id',
			   sorttype: "string",
			   search: true,
			},
			{
		       name: 'old_meter_status',
		       index: 'old_meter_status',
		       width:200,
		       sorttype: 'string',
		       search: true
			},
			{
		       name: 'new_meter_status',
		       index: 'new_meter_status',
		       sorttype: "string",
		       search: true,
			},
		
			{
		       name: 'change_date',
		       index: 'change_date',
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
   	pager: '#type_change_history_this_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "Customer's Meter Rent Chagne History",
	onSelectRow: function(id){ 
		
		var rowData = $("#type_change_history_this_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		fetchMeterRentChangeInfo(id);
		
   }
}));
jQuery("#type_change_history_this_grid").jqGrid('navGrid','#type_change_history_this_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {setActiveTabToCustomerTypeChangeHistory("");}}),{},{},{},
		{
		multipleSearch:true,
		onSearch: function () {	
				
			   var ruleArray=[["CUSTOMER_ID"],["eq"],[$("#comm_customer_id").val()]];
			   var oldRules=["customer_id"];
			   var newRules=["customer_id"];			   
			   modifyGridPostData("type_change_history_this_grid",ruleArray,oldRules,newRules);	
		   }
		
		});
gridColumnHeaderAlignment("left","type_change_history_this_grid",["remarks"]);



//<<Meter Rent History for the all Customer>>
$("#type_change_history_all_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.CustomerTypeChangeService&method=getCustomerTypeChangeList',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
	colNames: ['pid','Customer Code','Old Status', 'New Status','Change Date','Remarks'],
	   colModel: [{
			       name: 'pid',
			       index: 'pid',
			       width:70,
			       align:'center',
			       sorttype: 'string',
			       search: false,
			       hidden: true
				},
				{
				   name: 'customer_id',
				   index: 'customer_id',
				   sorttype: "string",
				   search: true,
				},
				{
			       name: 'old_meter_status',
			       index: 'old_meter_status',
			       width:200,
			       sorttype: 'string',
			       search: true
				},
				{
			       name: 'new_meter_status',
			       index: 'new_meter_status',
			       sorttype: "string",
			       search: true,
				},
			
				{
			       name: 'change_date',
			       index: 'change_date',
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
		
		var rowData = $("#type_change_history_all_grid").getRowData(id);
		getCustomerInfo("comm",rowData.customer_id);
		
   }
}));
jQuery("#type_change_history_all_grid").jqGrid('navGrid','#meterRent_change_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","type_change_history_all_grid",["customer_name","remarks"]);






//[START : BASIC INITIALIZATION & OVERRIDE] Common settings and override of some configuration for this particular interface
 	//Disable Reading Form "customer_id" as we will not let the user to use this field in this interface.
	$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
			serviceUrl: sBox.CUSTOMER_LIST,
	    	onSelect:function ()
	    	{getCustomerInfo("comm",$('#comm_customer_id').val());
	    	 setTimeout(setMeterStatus,200);
	         setActiveTabToCustomerTypeChangeHistory($('#comm_customer_id').val());
	         getDueMonthList($('#comm_customer_id').val());
	         }
	
	
	}));
	
	
	
	function setMeterStatus(customer_id)
	{
		//alert(customer_id);
		//alert($('#comm_customer_id').val());
		$("#customer_id").val($('#comm_customer_id').val());
		//alert($('#customer_id').val());
		var isMetered=$("#comm_isMetered_name").val();
        
		if(isMetered=="Metered")
			{
			 $("#isMetered").val("0");
			 $("#burner_qnt_div").show();
			 $("#max_min_load_div").hide();
			 $("#vat_rebate_div").hide();
			 $("#hhv_nhv_div").hide();
			 $("#pay_within_div").hide();
			 $("#btn_save").text("Metered to Non-Metered");
			 
			}else
				{
				
				 $("#isMetered").val("1");
				 $("#max_min_load_div").show();
				 $("#burner_qnt_div").hide();
				 $("#vat_rebate_div").show();
				 $("#hhv_nhv_div").show();
				 $("#pay_within_div").show();
				 $("#btn_save").text("Non-Metered to Metered");
				}
		
		
	}
	
	
	function checkConnectionType(connectionType)
	{
	 if(connectionType==0)
	   $("#parent_connection_div").hide();
	 else
	   $("#parent_connection_div").show();
	} 
	
	
	
	
	
	
	
	
	


	Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "effective_date",
	    trigger    : "effective_date"}));
	
//[END : BASIC INITIALIZATION & OVERRIDE]

function setActiveTabToCustomerTypeChangeHistory(customer_id){
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);

	var customerId=customer_id;
	if(customer_id=="") //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadCustomerTypeChangeHistory(customerId);
    
}

function reloadCustomerTypeChangeHistory(customer_id){

    var ruleArray=[["ctch.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("type_change_history_this_grid",ruleArray);
    $("#type_change_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("type_change_history_this_grid");
   	reloadGrid("typet_change_history_all_grid");
}







function validateAndSaveCustomerTypeChangeInfo(){
	
	var validate=true;
	
	validate=validateTypeChangeInfo();
	if(validate==true){	    
		saveCustomerTypeChangeInfo();							
	}	
}


function validateTypeChangeInfo(){
	var isMetered=$("#isMetered").val();
	var isValid=false;	
    isValid=validateField("connection_type","effective_date","remarks");		
    if(isValid==true){
    	if(isMetered=="0")
    		{
    		isValid=validateField("double_burner_qnt");
    		return isValid;
    		}
    	else
    		{
    		isValid=validateField("min_load","max_load");	
    		return isValid;
    		}
    		
    	}
    
	return isValid;
}


function saveCustomerTypeChangeInfo(){
	
	//CustomerTypeChangeForm(enableField);
	//CustomerTypeChangeForm(readOnlyField);
	
	var formData = new FormData($('form')[0]);
	 $.ajax({
		    url: 'saveCustomerTypeChangeInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  	  
		      if(response.status=="OK"){
		    	  CustomerTypeChangeForm(clearField);
		    	  CustomerTypeChangeForm(removeReadOnlyField);
		    	  CustomerTypeChangeForm(disableField);
		    	  disableButton("btn_save");
		    	  reloadCustomerTypeChangeHistory($("#comm_customer_id").val());
		      }
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}


function cancelButtonPressed(){
	clearRelatedData();
	CustomerTypeChangeForm(disableField);
	disableButton("btn_add","btn_edit","btn_save","btn_delete");
	unBlockGrid("meter_grid");
	resetSelection("meter_grid","customer_grid","type_change_history_this_grid","meterRent_change_history_all_grid");
}


function CustomerTypeChangeForm(plainFieldMethod){
	var fields=["ministry_id","connection_type","parent_connection","isMetered","min_load","max_load","single_burner_qnt","double_burner_qnt","vat_rebate","hhv_nhv","effective_date","remarks"];	
	plainFieldMethod.apply(this,fields);	
}
function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
	clearGridData("meter_grid");
	CustomerTypeChangeForm(clearField);
}

//$("#TypeChangeInfo .overlay").fadeToggle("fast");

function getDueMonthList(customer_id)
{
$.ajax({
url: "getDuesListByString.action?customer_id="+customer_id,
	dataType: 'text',		    
type: 'POST',
async: true,
cache: false,
success: function (response){
	$("#dues_list").val(response);
	var length=response.length;
	if(length>2)
	{
		CustomerTypeChangeForm(disableField);
		disableButton("btn_save");
	}else
	{
		CustomerTypeChangeForm(enableField);
		enableButton("btn_save");
	}
	
}    
});
}

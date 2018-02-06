//<<Customer Grid>>: It holds all NonMetered Customer List
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.NON_METERED_CUSTOMER_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
   colNames: ['Customer Id', 'Customer Name','Father Name', 'Category', 'Area','Mobile','Status','Created On'],
	colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-12,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Available Customers",
	onSelectRow: function(id){ 
		getCustomerInfo("comm",id);		
		$("#disconnect_customer_id").val(id);
		
		var rowData = $("#customer_grid").getRowData(id);
		if(rowData.status=="Disconnected")	
		{
			handleDisconnectedCustomer();
			
		}
		else
			enableButton("btn_save");
		
		reloadDisconnHistory(id);
		setActiveTabToCustomerDisconnHistory(id);
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Disconnect History for the selected Customer>>: It holds disconnection history for the selected customer.
$("#disconn_history_this_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DISCONNECTION_SERVICE+'&method='+jsEnum.NONMETER_DISCONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id', 'Disconn. Cause','Disconn. Type', 'Disconn Date','Remarks'],
   colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:100,
	                align:'center',
	                sorttype: 'string',
	                search: true
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
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DISCONNECTION_SERVICE+'&method='+jsEnum.NONMETER_DISCONNECTION_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id','Customer Name', 'Disconn. Cause','Disconn. Type', 'Disconn Date','Remarks'],
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
	                align:'center',
	                sorttype: 'string',
	                search: true
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

   }
}));
jQuery("#disconn_history_all_grid").jqGrid('navGrid','#disconn_history_all_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true,sopt:['eq','lt','gt','le','ge','cn']});
gridColumnHeaderAlignment("left","disconn_history_this_grid",["full_name","customer_name","disconnect_cause_name","disconnect_type_name","remarks"]);


function setActiveTabToCustomerDisconnHistory(customer_id){
	
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);
    
	var customerId=customer_id;
	if(customer_id=="") //for grid pager refresh button click;
		customerId=$("#comm_customer_id").val();
	
	if(customerId!="")
		reloadDisconnHistory(customerId);
    
}
function reloadDisconnHistory(customer_id){
	
	
	var ruleArray=[["RECONN_METERED.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("meter_grid",ruleArray);
    $("#disconn_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    
    reloadGrid("disconn_history_this_grid");
    reloadGrid("disconn_history_all_grid");
}

//[START : BASIC INITIALIZATION & OVERRIDE] Common settings and override of some configuration for this particular interface
 		
	$("#disconnect_by").unbind();
	$("#disconnect_by").chosen({no_results_text: "Oops, nothing found!",width: "64%"});

	//Recreate the button block and override it. Because we will not use the default buttons of Reading Entry Form.
	
	var buttons="<div class='formSep' style='padding-top: 2px;padding-bottom: 2px;'><div id='aDiv' style='height: 0px;'></div></div>"+
	"<div class='formSep sepH_b' style='padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;'>	"+
	"<table width='100%'>"+
	"<tr>"+
	"	<td width='55%' align='left'>"+
	"		<button class='btn btn-beoro-3' type='button' id='btn_edit' onclick='editButtonPressed()' disabled='disabled'><span class='splashy-application_windows_edit'></span>"+
	"		Edit</button>"+
	"		<button class='btn btn-beoro-3' type='button' id='btn_save' onclick='validateAndSaveDisconnInfo()' disabled='disabled'><span class='splashy-document_letter_okay'></span>"+
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
	"</table>"+
	"</div>";
	//$("#reading_button_div").html(buttons);
	
	$("#non_meter_disconn_button_div").replaceWith(buttons);
	
		
	$("#comm_customer_id").unbind();
	$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
			serviceUrl: sBox.NON_METERED_CUSTOMER_LIST,
	    	onSelect:function (){
				getCustomerInfo("comm",$('#comm_customer_id').val(),customerInfoCallback);
				$("#disconnect_customer_id").val($('#comm_customer_id').val());
			}
	}));
	function customerInfoCallback(data){
		if(data.connectionInfo.status=="DISCONNECTED")
			handleDisconnectedCustomer();
		reloadDisconnHistory(data.customer_id);
		setActiveTabToCustomerDisconnHistory(data.customer_id);
	}

	var $dialog = $('<div id="dialog-confirm"></div>')
	.html("<p> "+
	 	"Are you sure you want to delete the Non-Meter Disconnection Information?"+
		"<div id='del_holiday'></div> "+
	   "</p>")
	.dialog({
			title: 'Disconnection Information Delete Confirmation',
			resizable: false,
			autoOpen: false,
			height:180,
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
	    onSelect   : function() { this.hide(); $("#curr_reading_date").val($("#disconnect_date").val());}
	}));	
	
	
//[END : BASIC INITIALIZATION & OVERRIDE]

function fetchDisconnectionInfo(disconnection_id){
	
	disableField("disconnect_cause_str","disconnect_type_str","disconnect_remarks","disconnect_date");
	disableChosenField("disconnect_by");
	
	
	  $.ajax({
		    url: 'getNonMeterDisconnInfo.action',
		    type: 'POST',
		    data: {pId:disconnection_id},
		    cache: false,
		    success: function (response) {
		    	setDisconnInfo(response);
		    	setCustomerInfo("",response.customer);		    	
		    	disableButton("btn_save");
		    	enableButton("btn_edit","btn_delete");
		    	
		    }
		    
		  });	
}

function setDisconnInfo(data){
	$("#disconnect_cause_str").val(data.disconnect_cause_str);
	$("#disconnect_type_str").val(data.disconnect_type_str);
	$("#disconnect_remarks").val(data.remarks);
	$("#disconnect_date").val(data.disconnect_date);
	$("#disconnect_customer_id").val(data.customer_id);
	$("#pid").val(data.pid);
	setChosenData("disconnect_by",data.disconnect_by);	
}

function validateAndSaveDisconnInfo(){
	
	var validate=false;	
	validate=validateDisconnInfo();
	if(validate==true){
	
		saveDisconnInfo();
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
//==>>Need to Correct it
function saveDisconnInfo(){
	
	var formData = new FormData($('form')[0]);	

	$.ajax({
		    url: 'saveNonMeterDisconnInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  		if(response.status=="OK"){
			  		disconnInfoForm(disableField,disableChosenField) 
			   		reloadDisconnHistory($("#comm_customer_id").val());
			   		enableButton("btn_edit");
			   		disableButton("btn_save");
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}
function reloadDisconnHistory(customer_id){
	
	var ruleArray=[["DISCONN_NONMETERED.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("disconn_history_this_grid",ruleArray);
    $("#disconn_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
    reloadGrid("disconn_history_this_grid");
    reloadGrid("disconn_history_all_grid");
}


function cancelButtonPressed(){
	clearRelatedData();
	resetSelection("meter_grid","customer_grid","disconn_history_this_grid","disconn_history_all_grid");
	
}

function editButtonPressed(){	
	enableField("disconnect_cause_str","disconnect_type_str","disconnect_remarks","disconnect_date","disconnect_by");
	$("#disconnect_by").trigger("chosen:updated");
	enableButton("btn_save");
	disableButton("btn_edit");
	
}

function deleteDisconnInfo(){
	$.ajax({
	    url: 'deleteNonMeterDisconnInfo.action',
	    type: 'POST',
	    data: {pId:$("#pid").val()},
	    cache: false,
	    success: function (response) {
	    	
	    	$dialog.dialog("close");
	    	
	    	if(response.status=="OK"){
		    	//Clean forms data
	    		disconnInfoForm(clearField,clearChosenField);
		    	disconnInfoForm(enableField,enableChosenField);
		    	enableButton("btn_save");
		    	disableButton("btn_edit","btn_delete");
		    		    	
		    	reloadGrid("disconn_history_this_grid");
		    	reloadGrid("disconn_history_all_grid");
	    	}
	    	
	    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    }
	    
	});
}


function disconnInfoForm(plainFieldMethod,chosenFieldMethod){
	
	var fields = ["disconnect_cause_str","disconnect_type_str","disconnect_remarks","disconnect_date","disconnect_customer_id","pid"];
	var chosen_field=["disconnect_by"];
		 
	plainFieldMethod.apply(this,fields);
	if(chosenFieldMethod!=null)
		chosenFieldMethod.apply(this,chosen_field);	
	
}

function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);	
	disconnInfoForm(clearField,clearChosenField);
}


function handleDisconnectedCustomer(){
	disconnInfoForm(disableField,disableChosenField);
	disableButton("btn_edit","btn_save","btn_delete");
	
	var info="<center>"+
				"<img src='/JGTDSL_WEB/resources/images/disconnect.png' width='200' height='200'/>"+
				"<br/><br/>"+
				"<div style='font-size:15px;'>This customer is currently <font color='red'><b>Disconnected</b><font>.</div>";
			 "</center>"
	$("#rightSpan").html(info);
}







var p_customer_id="";
var p_month="";
var p_year="";
var p_area_id="";
var p_customer_category="";



$("#bill_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.BillingService&method=getNonMeteredBilledCustomerList'+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "bill_id"
	},
   colNames: ['Customer Id', 'Customer Name','Act. Payable Amount','Coll. Payable Amount','Unpaid','Status','Approve','Events', 'Download'],
   colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:60,
	                align:'center',
	                sorttype: 'string',
	                search: true
            	},
            	{
	                name: 'full_name',
	                index: 'full_name',
	                width:150,
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'actual_payable_amount',
	                index: 'actual_payable_amount',
	                sorttype: "string",
	                align:'right',
	                width:60,
	                search: true,
            	},
            	{
	                name: 'collected_payable_amount',
	                index: 'collected_payable_amount',
	                sorttype: "string",
	                align:'right',
	                width:60,
	                search: true,
            	},
            	{
	                name: 'non_collected_payable_amount',
	                index: 'non_collected_payable_amount',
	                sorttype: "string",
	                align:'right',
	                width:60,
	                search: true,
            	},
            	{
	                name: 'status',
	                index: 'status',
	                sorttype: "string",
	                align:'center',
	                width:80,
	                search: true,
            	},
            	{ 
            		name: 'approve', 
            		width: 20, 
            		align:'center',
            		formatter:function(cellvalue, options, rowObject){
            		      if(rowObject.status=='Waiting for Approval')
            		    	  return "<span class='ui-icon ui-icon-circle-check' style='margin-left:12px;'></span>"
            		      else
            		    	  return "<span class='ui-icon ui-icon-check' style='margin-left:12px;'></span>";
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                            if(rowObject.status=='Waiting for Approval')
                            	return ' onClick="approveBillByBillId('+rowObject.bill_id+')"';
                           	else
                           		return '';
                    }
                }, 
            	{ 
            		name: 'View', 
            		width: 20, 
            		align:'center',
            		formatter:function(){
            			return "<span class='ui-icon ui-icon-zoomout' style='margin-left:3px;'></span>"
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                    	return ' onClick="fetchBillingEvents('+rowId+')"';
                    }
                },
                { 
            		name: 'Download', 
            		width: 20, 
            		align:'center',
            		formatter:function(){
                          return "<span class='ui-icon ui-icon-circle-arrow-s' style='margin-left:3px;'></span>"
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                            return ' onClick="window.location=\'downloadNonMeteredBill.action?download_type=S&bill_id='+rowObject.bill_id+'\'"';
                    }
                }
        ],
	datatype: 'local',
	height: $("#bill_grid_div").height()-80,
	width: $("#bill_grid_div").width()-2,
   	pager: '#bill_grid_pager',
   	sortname: 'bill_id',
    sortorder: "desc",
	caption: "Non-Metered Customers List(Billed)"
}));

jQuery("#bill_grid").jqGrid('navGrid','#bill_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {reloadBillGrid();}}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","bill_grid",["full_name"]);


$('#bill_grid').jqGrid('navGrid','#bill_grid_pager')
    .navButtonAdd('#bill_grid_pager',{
        caption:"<b><font color='green'>Approve all (Non-Approved) Bill</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
        buttonicon:"ui-icon-check", 
        id: "icon_approve_all",
        onClickButton: function(){
    		approveAllBill();
        }
});

//-----

function fetchBillingEvents(billId){
	$.nsWindow.open({
		movable:true,
		title: 'Billing Events',
		width: 600,
		height: 400,
		dataUrl: 'showBillEvents.action?bill_id='+billId,
		theme:jsVar.MODAL_THEME
    });
    
}
//-----

function reloadBillGrid(){
    var ruleArray=[["bill_month","bill_year"],["eq","eq"],[$("#billing_month").val(),$("#billing_year").val()]];
    var $grid = $('#bill_grid');
    var caption_extra="";
    
	if($("#billing_month").val()=="" || $("#billing_year").val()==""){
		clearGridData("bill_grid");
		$grid.jqGrid('setCaption','Please Select Month, Year');
		return;
	}
	
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="individual_customer" && $("#customer_id").val()!=""){
		
		ruleArray[0].push("customer_id");
		ruleArray[1].push("in");
		var formattedCustomerIdString=$("#customer_id").val().replace(/,/g , "','");
		ruleArray[2].push("'"+formattedCustomerIdString+"'");
		caption_extra=". Customer Id - "+$("#customer_id").val();
		p_customer_id=$("#customer_id").val();
	}
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="area_wise"){
		if($("#area_id").val()=="") return;
		
		ruleArray[0].push("area_id");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#area_id").val());
		caption_extra=". "+$("#area_id option:selected").text();
		
		p_area_id=$("#area_id").val();
	}
	
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="category_wise")		{								
	   if($("#area_id").val()=="" || $("#customer_category").val()=="") return;
	   
	    ruleArray[0].push("area_id");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#area_id").val());
		
	    ruleArray[0].push("customer_category");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#customer_category").val());
		caption_extra=". "+$("#area_id option:selected").text()+" ["+$("#customer_category option:selected").text()+"] ";
		
		p_area_id=$("#area_id").val();
		p_customer_category=$("#customer_category").val();
		
	}
	
	p_month=$("#billing_month").val();
	p_year=$("#billing_year").val();
	
	var postdata=getPostFilter("bill_grid",ruleArray);
   	$("#bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    		
	reloadGrid("bill_grid");
	
	$grid.jqGrid('setCaption', 'Non-Metered Customers List(Billed) - '+$("#billing_month option:selected").text()+', '+$("#billing_year").val()+caption_extra);
}
/*
function downloadBill(downloadType){
	window.location='downloadMeteredBill.action?download_type='+downloadType+'&customer_id='+p_customer_id+'&bill_month='+p_month+'&bill_year='+p_year+'&area_id='+p_area_id+'&customer_category='+p_customer_category;
		
}*/

Calendar.setup({
        inputField : "bill_generation_date",
        trigger    : "bill_generation_date",
		eventName : "focus",
        onSelect   : function() { this.hide()},
        showTime   : 12,
        dateFormat : "%d-%m-%Y",
		showTime : true
      });
      

function approveBillByBillId(bill_id)
{
 	$.ajax({
	  type: 'POST',
	  url: 'approveBillByBillId.action?isMetered=0',
	  data: {bill_id:bill_id},
	  success:function(data)
	  {	    
		alert(data.message);
		//refreshTable1();
		reloadBillGrid();
	  },
	  error:function (xhr, ajaxOptions, thrownError) {
	      alert(xhr.status);
	      alert(thrownError);
      }
	});
	
}

function approveAllBill(){
	
	$("#icon_approve_all").hide();
	var isValid=valiateFields();
	if(isValid==false)
	{
			alert("Please select necessary values.");
			return;
			
	}
	$.ajax({
		  type: 'POST',
		  url: 'approveAllBill.action?isMetered=0',
		  data: {"bill_for":$("input[name=bill_parameter\\.bill_for]:checked").val(),"customer_id":$("#customer_id").val(),area_id:$("#area_id").val(),customer_category:$("#customer_category").val(),billing_month:$("#billing_month").val(),billing_year:$("#billing_year").val()},
		  success:function(data)
		  {	    
			alert(data.message);
			//refreshTable1();
			$("#icon_approve_all").show();
		  },
		  error:function (xhr, ajaxOptions, thrownError) {
			  $("#icon_approve_all").show();
		      alert(xhr.status);
		      alert(thrownError);
		      reloadBillGrid();
	      }
		});
}

function validateAndProcessBilling()
{
	var refreshIntervalId=0;
 var isValid=valiateFields();
 if(isValid==true)	 {
	if(document.getElementById("reprocess").checked==true)
		$("#reprocess").val("Y");
	else
		$("#reprocess").val("N");
	
	var form = document.getElementById('billProcessForm');
	var formData = new FormData(form);
	
	$("#stat_div").show();
	$("#loading_div").html(jsImg.LOADING_MID+"<br/><br/><font style='color:white;font-weight:bold'>Please wait. We are preparing the bill(s) for you.</font>");
	  $.ajax({
	    url: 'processBill.action',
	    type: 'POST',
	    data: formData,
	    async: true,
	    cache: false,
	    contentType: false,
	    processData: false,
	    success: function (response) {
	    if(response.status=="OK")
	    {
	    	
	       var customer_data = [{"customer_id":$("#customer_id").val(),"full_name":$("#customer_name").val(),"category_name":$.trim($("#customer_category option:selected").text()),"area_name":$.trim($("#area_id option:selected").text()),"mobile":$("#mobile").val(),"status":$("#connection_status").val() }];
		   $("#customer_table").jqGrid('addRowData', $("#customer_id").val(),customer_data[0] , "first");
		   cleanAllFields();				     
	    }	
	    clearInterval(refreshIntervalId);   
	    $("#loading_div").html(response.message);
	    fetchProgressInfoForBilling();
	    $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    
	       
	    }
	    
	  });
	   
	  if($('input[name=bill_parameter\\.bill_for]:checked', '#billProcessForm').val()!="individual_customer"){
		  fetchApproxTotalBill();
		  refreshIntervalId = setInterval(fetchProgressInfoForBilling, 5000);

		  
		  
		  
	  }
	
	}
}

function fetchApproxTotalBill(){
	$("#approx_total_div").html(jsImg.LOADING_MID);
	  $.ajax({
	    url: 'getApproxTotalBillingCount.action',
	    type: 'POST',
	    data: {"bill_parameter.bill_for":$('input[name=bill_parameter\\.bill_for]:checked', '#billProcessForm').val(),"bill_parameter.area_id":$("#area_id").val(),"bill_parameter.customer_category":$("#customer_category").val(),"bill_parameter.billing_month_str":$("#billing_month").val(),"bill_parameter.billing_year":$("#billing_year").val(),"bill_parameter.isMetered_str":$("#isMeter").val()},	    
	    async: true,
	    cache: false,
	    success: function (response) {
	    	$("#approx_total_div").html(response.message);
	    }
	    
	  });
}

function fetchProgressInfoForBilling()
{
	$("#processed_total_div").html(jsImg.LOADING_MID);
	  $.ajax({
	    url: 'getProcessedTotalBillingCount.action',
	    type: 'POST',
	    data: {"bill_parameter.bill_for":$('input[name=bill_parameter\\.bill_for]:checked', '#billProcessForm').val(),"bill_parameter.area_id":$("#area_id").val(),"bill_parameter.customer_category":$("#customer_category").val(),"bill_parameter.billing_month_str":$("#billing_month").val(),"bill_parameter.billing_year":$("#billing_year").val(),"bill_parameter.isMetered_str":$("#isMeter").val()},	    
	    async: true,
	    cache: false,
	    success: function (response) {
	    	$("#processed_total_div").html(response.message);
	    }
	    
	  });
}
function valiateFields(){
	 var isValid=true;
	 var bill_for=getRadioCheckedValue("bill_parameter\\.bill_for"); 
	 
	 if(bill_for=="area_wise")
	 	isValid=validateField("area_id","bill_generation_date");
	 else if(bill_for=="category_wise")
	 	isValid=validateField("area_id","customer_category","bill_generation_date");
	 else if(bill_for=="individual_customer")
	 	isValid=validateField("customer_id","bill_generation_date");

	 if(isValid==true)
	 	isValid=validateField("billing_month","billing_year");
	 
	 return isValid;
}
$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.NON_METERED_CUSTOMER_LIST,
    	onSelect:function (){
    		getCustomerInfo("",$('#customer_id').val());
    	},
}));

$("#billing_month").val(getCurrentMonth());
$("#billing_year").val(getCurrentYear());
//$("#bill_generation_date").val(getCurrentDate());
reloadBillGrid();

function checkType(type){
	if(type=="area_wise")
	{
		
	 disableChosenField("customer_id");
	 disableField("customer_category");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 enableField("area_id");
	}
	else if(type=="by_category"){
		
	 disableChosenField("customer_id");
	 enableField("customer_category","area_id");
	 autoSelect("customer_category","area_id");
	}
	else if(type=="individual"){
		
	 enableChosenField("customer_id");
	 disableField("customer_category","area_id");
	 resetSelectBoxSelectedValue("customer_category","area_id");
	}
}		
function cleanAllFields(){}									

function unlockDatabase()
{
disableButton("btn_unlockDB");
$.ajax({
    url: 'unlockDatabase.action',
    type: 'POST',
    data: {isMetered:$("#isMeter").val()},
    cache: false,
    success: function (response) {
    	enableButton("btn_unlockDB");
    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
    }
    
  });

}
	
var refund_branch_sbox = { sourceElm:"refund_bank_id",targetElm :"refund_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var refund_account_sbox = { sourceElm:"refund_branch_id",targetElm :"refund_account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};

var bill_adj_deduct_branch_sbox = { sourceElm:"bill_adj_deduct_bank_id",targetElm :"bill_adj_deduct_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var bill_adj_deduct_account_sbox = { sourceElm:"bill_adj_deduct_branch_id",targetElm :"bill_adj_deduct_account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};

var bill_adj_add_branch_sbox = { sourceElm:"bill_adj_add_bank_id",targetElm :"bill_adj_add_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var bill_adj_add_account_sbox = { sourceElm:"bill_adj_add_branch_id",targetElm :"bill_adj_add_account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};


$("#comm_customer_id").unbind();
$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("comm",$('#comm_customer_id').val(),getSecurityBalance);}
}));


function clearRelatedData(){	
	clearField.apply(this,comm_customer_info_field);		
}

function getSecurityBalance(){

		$.ajax({
			    url: "getSecurityBalance.action?customerId="+$('#comm_customer_id').val(),
			    type: 'GET',
			    //data: {customerId:$('#comm_customer_id').val()},
			    cache: false,
			    success: function (response) {
		  
			    	$("#security").html(response.security);
			    	$("#security_hidden").val(response.security);
			    	$("#others").html(response.others);			    
			    	$("#others_hidden").val(response.others);
			    
			    }
			    
			  });	
	
}
/*~~~~~------~~~~~~*/
//Load Dues Bills
/*~~~~~------~~~~~~*/

function loadDuesList(){
 	var ruleArray=[["customer_id"],["eq"],[$("#comm_customer_id").val()]];
	var postdata=getPostFilter("dues_bill_grid",ruleArray);
 	$("#dues_bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,datatype:'json'});
 	reloadGrid("dues_bill_grid");
}

var duesGrid=$("#dues_bill_grid");

duesGrid.jqGrid({
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.BillingService&method=getOnlyDueBillList',
 	jsonReader: {
          repeatitems: false,
          id: "bill_id"
	},
  colNames: ['Bill Id','Month','Year','Bill Amount','Collected Amount', 'Due Amount','Adjust Amount','Surcharge','Bill Type'],
  colModel: [{    name: 'bill_id',
		            index: 'bill_id',
		            width:30,
		            align:'center',
		            sorttype: 'string',
		            search: true,	
		            key: true
		    	},
                 {
	                name: 'bill_month_name',
	                index: 'bill_month_name',
	                width:30,
	                align:'center',
	                sorttype: 'string',
	                search: true
          	},
          	 {
	                name: 'bill_year',
	                index: 'bill_year',
	                width:30,
	                align:'center',
	                sorttype: 'string',
	                search: true
          	},
		    	{
		            name: 'payable_amount',
		            index: 'payable_amount',
		            width:30,
		            align:'center',
		            sorttype: 'string',
		            search: true,
		    	},
		    	{
		            name: 'collected_amount',
		            index: 'collected_amount',
		            width:30,
		            align:'center',
		            sorttype: 'string',
		            search: true,
		    	},
		    	{
		            name: 'remaining_amount',
		            index: 'remaining_amount',
		            width:30,
		            align:'center',
		            sorttype: 'string',
		            search: true,
		    	},
		    	{
	                name: 'adjust_amount',
	                index: 'adjust_amount',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" maxlength="8" id="adjust_amount_'+rowObject.bill_id+'" style="width:70px;height:10px;text-align:right;" onblur="calculateTotalAjustment('+rowObject.bill_id+',\'blurEvent\')"/>'+
        					   '<input type="hidden" id="bill_id_'+rowObject.bill_id+'" value="'+rowObject.bill_id+'" />'+
        					   '<input type="hidden" id="bill_type_'+rowObject.bill_id+'" value="'+rowObject.bill_type+'" />';
    				} 
            	},
            	{
	                name: 'surcharge',
	                index: 'surcharge',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" maxlength="8" id="surcharge_'+rowObject.bill_id+'" style="width:70px;height:10px;text-align:right;"/>';
    				} 
            	},
            	{
	                name: 'bill_type',
	                index: 'bill_type',
	                hidden:true
          	},
      ],
	datatype: 'local',
	multiselect: true,
	height: $("#dues_div").height(),
	width: $("#dues_div").width(),
 	rowNum:1000,
	rownumWidth: 40,
 	pager: '#dues_bill_grid_pager',
	caption: "Dues bill list for the selected customer",
	footerrow:true, 
	userDataOnFooter:true, 	
	onSelectRow: function(rowId){ calculateTotalAjustment(rowId,"checkEvent"); },
	onSelectAll:function(aRowids, status) {
		calculateTotalAjustment(null,"checkEvent");
	},
	loadComplete: function () {

	
	}
});

duesGrid.jqGrid('navGrid','#dues_bill_grid_pager',$.extend({},footerButton,{search:false,refresh:false}),{},{},{},{multipleSearch:true});


function setAdjustmentMode(mode){
	
	if(mode=="refund"){
		$("#refundFieldset").show(); 
		$("#collectionFieldset").hide(); 
	}
	else if(mode=="withBill"){
		$("#refundFieldset").hide();
		$("#collectionFieldset").show();
		loadDuesList();
		
	}						
}
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collectionDate",
    trigger    : "collectionDate"}));

function checkAdjustmentAmount(){
	var adjSecurity=parseInt($("#adjustment_security").val());
	var adjOthers=parseInt($("#adjustment_others").val());
	if(isNaN(adjSecurity)) adjSecurity=0;
	if(isNaN(adjOthers)) adjOthers=0;
	
	
	var totalAvailableAmount=parseInt($("#security_hidden").val())+parseInt($("#others_hidden").val());
	var totalAdjustmentAmount= adjSecurity+adjOthers;

	
	
	if(adjSecurity>parseInt($("#security_hidden").val()) || adjOthers>parseInt($("#others_hidden").val())){
		$("#msg1Div").html("Total Adjusted amount cannot be greater than total available amount.<br/>");
		$("#totalAdjustableAmount").val(0);
	}
	else{
	$("#msg1Div").html(""); 
	$("#totalAdjustableAmount").val(totalAdjustmentAmount);	
	}
	
	showHideSaveButton();
		
}

function setOtherOption(){
	if(document.getElementById("otherCheckBox_refund").checked)
	{
		$("#refund_bank_id").val("");
		$('#refund_branch_id option').remove();
		$('#refund_account_id option').remove();
	}
}
function setDeductOtherOption(){
	if(document.getElementById("otherCheckBox_deduct").checked)
	{
		$("#bill_adj_deduct_bank_id").val("");
		$('#bill_adj_deduct_branch_id option').remove();
		$('#bill_adj_deduct_account_id option').remove();
	}
}
function setAddOtherOption(){
	if(document.getElementById("otherCheckBox_add").checked)
	{
		$("#bill_adj_add_bank_id").val("");
		$('#bill_adj_add_branch_id option').remove();
		$('#bill_adj_add_account_id option').remove();
	}
}

function calculateTotalAjustment(rowId,type) {	
	var totalBill=0;
	var totalCollected=0;
	var totalDues=0;
	var totalAdjustAmount=0;
	var rows = duesGrid.getDataIDs();
	var rowId="";
	billIds="";
	
		for(a=0;a<rows.length;a++)
		 {	
			
			row=duesGrid.getRowData(rows[a]);
			rowId=row.bill_id;       
		    
		    if($("#jqg_dues_bill_grid_"+rowId).prop('checked')==true) 
		    { 
		    	 var rowData = duesGrid.getRowData(rowId);	
		    		
				totalBill=parseFloat(totalBill)+parseFloat(rowData["payable_amount"]);
				totalCollected=parseFloat(totalCollected)+parseFloat(rowData["collected_amount"]);
				totalDues=parseFloat(totalDues)+parseFloat(rowData["remaining_amount"]);		
				if(type=="checkEvent")
					$("#adjust_amount_"+rowId).val(parseFloat(rowData["remaining_amount"]));
				
				totalAdjustAmount = parseFloat(totalAdjustAmount)+  parseFloat($("#adjust_amount_"+rowData["bill_id"]).val());				

		    }
		    else{
		    	$("#adjust_amount_"+rowId).val(0);
		    }
		}

		$("#totalAdjustAmount").val(totalAdjustAmount);
		duesGrid.jqGrid("footerData", "set", {bill_year: "Total:", payable_amount: totalBill,collected_amount: totalCollected,remaining_amount: totalDues,adjust_amount:""});
		$("#adjust_amount_undefined").val(totalAdjustAmount);
		$("#adjust_amount_undefined").prop('disabled', true); 
		validateAdjustmentAmount(); 
		showHideSaveButton();
	}

function validateAdjustmentAmount(){
	
	var allowedAmount=parseInt($("#totalAdjustableAmount").val());
	var selectedTotalAmount=parseInt($("#adjust_amount_undefined").val());
	
	if(isNaN(allowedAmount)) allowedAmount=0;
	if(isNaN(selectedTotalAmount)) selectedTotalAmount=0;
	
	
	if(selectedTotalAmount>allowedAmount){
		$("#msg2Div").html("Adjusted amount is greater than the provided amount.<br/>");
		hasError=true;
	}
	else{
		$("#msg2Div").html("");
	}	
}

function showHideSaveButton(){
	if($("#msg1Div").html()=="" && $("#msg2Div").html()=="")
		$("#btn_save").prop('disabled', false);
	
	else
		$("#btn_save").prop('disabled', true);
		
}

function validateSecurityAdjustment(){	
	
	if($("#comm_customer_id").val()==""){
		alert("Select a customer.");
		return;
	}
	
	if($("#collectionDate").val()==""){
		alert("Provide Date.");
		return;
	}
	if($(parseInt($("#totalAdjustableAmount").val()))<=0){
		alert("Total Adjust must be greater than zero.");
		return;
	}	
	
	
	if(document.getElementById("adjustmentMode_refund").checked){	
		if(!document.getElementById("otherCheckBox_refund").checked){
			
			if($("#refund_bank_id").val()=="" || $("#refund_branch_id").val()=="" || $("#refund_account_id").val()==""){
				alert("Provide Bank, Branch and Account Information Correctly.");
				return;
			}
		}
	}
	else if(document.getElementById("adjustmentMode_withBill").checked){		
		if(!document.getElementById("otherCheckBox_deduct").checked){			
			if($("#bill_adj_deduct_bank_id").val()=="" || $("#bill_adj_deduct_branch_id").val()=="" || $("#bill_adj_deduct_account_id").val()==""){
				alert("Provide Deduct Bank, Branch and Account Information Correctly.");
				return;
			}
		}
		if(!document.getElementById("otherCheckBox_add").checked){			
			if($("#bill_adj_add_bank_id").val()=="" || $("#bill_adj_add_branch_id").val()=="" || $("#bill_adj_add_account_id").val()==""){
				alert("Provide Collection Bank, Branch and Account Information Correctly.");
				return;
			}
		}
		
		if(parseFloat($("#adjust_amount_undefined").val())<=0){
			alert("Adjustable amount cannot be zero.");
			return;
		}
		
	}
	
	saveSecurityAdjustment();
	
}

function saveSecurityAdjustment(){
	
	var otherRefund=0;
	var otherDeduct=0;
	var otherAdd=0;
	var errorMessage="";
	var adjustmentModeVal=0;
	
	if(document.getElementById("otherCheckBox_refund").checked)
		otherRefund=1;
	if(document.getElementById("otherCheckBox_deduct").checked)
		otherDeduct=1;
	if(document.getElementById("otherCheckBox_add").checked)
		otherAdd=1;
	if(document.getElementById("adjustmentMode_refund").checked)
		adjustmentModeVal=1;
	else
		adjustmentModeVal=2;
	
		
	
	var rows = jQuery("#dues_bill_grid").getDataIDs();
	var row;
	var adjustmentBillStr="";  

	for(var a=0;a<rows.length;a++)
	 {	
	    row=jQuery("#dues_bill_grid").getRowData(rows[a]);
	    if($("#jqg_dues_bill_grid_"+row.bill_id).prop('checked')==true) 
	    { 
	    	adjustmentBillStr=adjustmentBillStr+row.bill_type+"#"+row.bill_id+"#"+$("#adjust_amount_"+row.bill_id).val()+"#"+$("#surcharge_"+row.bill_id).val()+"@";
	    }
	}
	
	if(adjustmentBillStr.length>0)
		adjustmentBillStr=adjustmentBillStr.substring(0,adjustmentBillStr.length-1);
	
	if(document.getElementById("adjustmentMode_withBill").checked && adjustmentBillStr==""){
		alert("Please select atleast one bill to adjust.");
		return;
	}
	disableButton("btn_save");
	
	  $.ajax({
		    url: 'saveSecurityAdjustment.action',
		    type: 'POST',
		    data: {customerId:$("#comm_customer_id").val(),securityAmount:$("#adjustment_security").val(),OtherAmount:$("#adjustment_others").val(),
				  adjustmentMode:adjustmentModeVal,totalAdjustableAmount:$("#totalAdjustableAmount").val(),
				  comment:$("#comment").val(),collectionDate:$("#collectionDate").val(),
				  refundOther:otherRefund,refundBank:$("#refund_bank_id").val(),refundBranch:$("#refund_branch_id").val(),refundAccount:$("#refund_account_id").val(),
				  deductOther:otherDeduct,deductBank:$("#bill_adj_deduct_bank_id").val(),deductBranch:$("#bill_adj_deduct_branch_id").val(),deductAccount:$("#bill_adj_deduct_account_id").val(),
				  addOther:otherAdd,addBank:$("#bill_adj_add_bank_id").val(),addBranch:$("#bill_adj_add_branch_id").val(),addAccount:$("#bill_adj_add_account_id").val(),
				  adjustmentBillStr:adjustmentBillStr},
		    cache: false,
		    success: function (response) {
		    	//alert(response.message);
		    	if((response.message=="success")){
		    		//alert("1");		    	
		    	$.jgrid.info_dialog(response.dialogCaption,"Successfully Saved Security Adjustment Information",jqDialogClose,jqDialogParam);		    	
		    	document.getElementById("adjustmentMode_refund").checked=true;
		    	document.getElementById("otherCheckBox_deduct").checked=false;
		    	document.getElementById("otherCheckBox_add").checked=false;
		    	setAdjustmentMode('refund');
		    	clearFormData();	
		    	}
		    	else{
		    		//alert("2");
		    		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
		    	}
		    	enableButton("btn_save");
		    },
		    error: function (response) {alert(response);enableButton("btn_save");}
		    
		  });
	  
	  
	   /*setDisconnInfo(response);
	setCustomerInfo("",response.customer);		    	
	disableButton("btn_save");
	enableButton("btn_edit","btn_delete");
	*/
}

function clearFormData(){
	getSecurityBalance();
	clearField("adjustment_security","adjustment_others","totalAdjustableAmount","comment", "collectionDate");
	if(adjustmentModeVal=="2")
		loadDuesList();
}

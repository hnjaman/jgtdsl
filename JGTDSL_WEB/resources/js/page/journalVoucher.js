var z_branch_sbox = { sourceElm:"z_bank_id",targetElm :"z_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var z_account_sbox = { sourceElm:"z_branch_id",targetElm :"z_account_no",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};

function clearRelatedData(){clearCollectionInfo();clearZCollectionInfo();}

function changeVoucherType(correctionType){
 $("#div_customerCorrection").hide();
 $("#div_accountCorrection").hide();
 $("#div_"+correctionType).show();
}
$("#old_customer_id").unbind();
$("#old_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("old",$('#old_customer_id').val());fetchCollectionInfo();}
}));
$("#new_customer_id").unbind();
$("#new_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("new",$('#new_customer_id').val());fetchCollectionInfo();}
}));
$("#z_customer_id").unbind();
$("#z_customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("z",$('#z_customer_id').val());fetchZCollectionInfo();}
}));


function changeTheme()
{
    var default_theme=$("#default_url").val();
	$("#wait_div").html(jsImg.LOADING);
 	var formData = new FormData($('form')[0]);
 	
		  $.ajax({
		    url: 'changeTheme.action',
		    type: 'POST',
		    data: formData,
		    async: true,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		    			$("#wait_div").html(response.message);
		    			window.location=default_theme+"&action=themeSelection";
		    },
		    error: function (response) {$("#wait_div").html("");}		    
		  });
}

function setMonth(){
	$("#new_bill_month").val(getSelectedText("bill_month"));
	fetchCollectionInfo();
}

function setYear(){
	$("#new_bill_year").val(getSelectedText("bill_year"));
	fetchCollectionInfo();
}

function fetchCollectionInfo()
{
    clearCollectionInfo();
	if($("#old_customer_id").val()=="" || $("#bill_month").val()=="" || $("#bill_year").val()=="")
		return;
		
 	$.ajax({
   		  type: 'POST',
   		  url: 'getCollectionInfoByCustomerIdBillingMonthYear.action?collection.customer_id='+$("#old_customer_id").val()+'&collection.bill_month='+$("#bill_month").val()+'&collection.bill_year='+$("#bill_year").val(),
   		  success:function(data){
			if(typeof data.collection_id === "undefined"){
				clearCollectionInfo();
			}
			else
				setCollectionInfo(data);
   		  },
   		  error:function(){
   			$("#btn_save").removeAttr("disabled");
   		  }
   	});
}

function fetchZCollectionInfo()
{
    clearZCollectionInfo();
	if($("#z_customer_id").val()=="" || $("#z_bill_month").val()=="" || $("#z_bill_year").val()=="")
		return;
		
 	$.ajax({
   		  type: 'POST',
   		  url: 'getCollectionInfoByCustomerIdBillingMonthYear.action?collection.customer_id='+$("#z_customer_id").val()+'&collection.bill_month='+$("#z_bill_month").val()+'&collection.bill_year='+$("#z_bill_year").val(),
   		  success:function(data){
			if(typeof data.collection_id === "undefined"){
				clearZCollectionInfo();
			}
			else
				setZCollectionInfo(data);
   		  },
   		  error:function(){
   			$("#btn_save").removeAttr("disabled");
   		  }
   	});
}

function clearCollectionInfo(){
	clearField("collection_bank","collection_branch","collection_account","collection_amount","collection_date");
}
function clearZCollectionInfo(){
	clearField("z_collection_bank","z_collection_branch","z_collection_account","z_collection_amount","z_collection_date");
}

function setCollectionInfo(data){
 $("#collection_bank").val(data.bank_name);
 $("#collection_branch").val(data.branch_name);
 $("#collection_account").val(data.account_name);
 $("#collection_amount").val(data.collection_amount);
 $("#collection_date").val(data.collection_date_f1);
}

function setZCollectionInfo(data){
	 $("#z_collection_bank").val(data.bank_name);
	 $("#z_collection_branch").val(data.branch_name);
	 $("#z_collection_account").val(data.account_name);
	 $("#z_collection_amount").val(data.collection_amount);
	 $("#z_collection_date").val(data.collection_date_f1);
	}

var fields=["old_customer_id","old_full_name","bill_month","bill_year","new_customer_id","new_full_name","new_bill_month","new_bill_year","collection_bank","collection_branch","collection_account","collection_amount","collection_date","narration"];
var all_fields=fields.slice();

function resetCustomerAccountForm(){
	clearField.apply(this,all_fields);
}

function saveCustomerCorrection(){	
	//alert("sss");
	var isValid=validateField.apply(this,all_fields);
	//alert(isValid);
	if(isValid==false) return;
	
	if($("#old_customer_id").val()==$("#new_customer_id").val())
	{
		alert("Both Customer Id are equal.");
		return;
	}
	
	disableButton("btn_save");
	var formData = new FormData($('form')[0]);

		
	  $.ajax({
		    url: 'saveCustomerAccountCorrection.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  		if(response.status=="OK"){
		  			alert(response.message);
			  		clearField.apply(this,all_bank_fields);
			   		enableButton("btn_save");
			   		alert(response.message);
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
}

var z_fields=["z_customer_id","z_full_name","z_bill_month","z_bill_year","z_collection_date","z_collection_amount","z_collection_bank","z_collection_branch","z_collection_account","z_bank_id","z_branch_id","z_account_no","z_narration"];
var z_all_fields=z_fields.slice();

function saveBankAccountCorrection(){	
	var isValid=validateField.apply(this,z_all_fields);
	if(isValid==false) return;
	
	
	disableButton("btn_save");
	var formData = new FormData($('form')[0]);

		
	  $.ajax({
		    url: 'saveBankAccountCorrection.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  		if(response.status=="OK"){
			  		clearField.apply(this,z_all_fields);
			   		enableButton("btn_save");
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
}

function resetBankAccountForm(){
	clearField.apply(this,z_all_fields);
}


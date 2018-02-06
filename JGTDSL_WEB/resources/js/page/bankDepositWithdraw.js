var source_bank_fields=["source_bank_id","source_branch_id","source_account_no","source_transaction_date","transaction_type","transaction_amount","source_transaction_particulars"];
var all_bank_fields=source_bank_fields.slice();
all_bank_fields.push("target_bank_id","target_branch_id","target_account_no","target_transaction_date","target_transaction_particulars");

setupCalendar("source_transaction_date","target_transaction_date","search_from_date","search_to_date");

function toggleCheckBox(){
		$("#transfer_account_div .overlay").fadeToggle("fast");
		
		if(document.getElementById("transfer_checkbox").checked==true)
			$("#transaction_type").val("4");
		else
			$("#transaction_type").val("");
							
}

function changeTransactionType(transactionType){

	
	if(transactionType=="2"){		
		showElement("payment_particulars");
		hideElement("receive_particulars","margin_payment_particulars","month_year_div");
		readOnlyField("source_transaction_particulars");
	}
	else if(transactionType=="3"){
		showElement("receive_particulars");
		hideElement("payment_particulars","margin_payment_particulars","month_year_div");
		readOnlyField("source_transaction_particulars");
	}
	else if(transactionType=="6"){
		showElement("margin_payment_particulars","month_year_div");
		hideElement("payment_particulars","receive_particulars");
		readOnlyField("source_transaction_particulars");
	}
	else{
		hideElement("receive_particulars","payment_particulars","margin_payment_particulars","month_year_div");
		$("#source_transaction_particulars").val("");
		removeReadOnlyField("source_transaction_particulars");
	}
	
	if(transactionType=="4"){
		$("#transfer_account_div .overlay").fadeOut("fast");
		$('#transfer_checkbox').prop('checked', true);
	}		
	else{
		$("#transfer_account_div .overlay").fadeIn("fast");
		$('#transfer_checkbox').prop('checked', false);
	}
}

function validateAndSaveTransaction(){
	
	var validateTransactionInfo=false;	
	validateTransactionInfo=valiateTransaction();
	if(validateTransactionInfo==true){
		saveBankTransaction();
	}	
}

function valiateTransaction(){
	var isValid;
	var validationFields;
	var transaction_type=$("#transaction_type").val();
	resetErrorBorder("target_bank_id","target_branch_id","target_account_no","target_transaction_date","target_transaction_particulars");
	
	if(transaction_type=="4"){
		  validationFields=$.extend( true, {}, all_bank_fields);
		  validationFields=Object.keys(validationFields).map(function(k) { return validationFields[k] }); // Making the object to array object
	  	  isValid=validateField.apply(this,validationFields);
	  	  
	}
	else{
		  validationFields=$.extend( true, {}, source_bank_fields);
		  validationFields=Object.keys(validationFields).map(function(k) { return validationFields[k] }); // Making the object to array object
	  	  isValid=validateField.apply(this,validationFields);
	}

	return isValid;
}
function saveBankTransaction(){
	
	disableButton("btn_save");
	var formData = new FormData($('form')[0]);

		
	  $.ajax({
		    url: 'saveBankTransaction.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  		if(response.status=="OK"){	  		
			  		clearField("source_transaction_date","transaction_amount","transaction_mode","target_bank_id","target_branch_id","target_account_no","target_transaction_date","target_transaction_particulars");
			  		enableButton("btn_save");
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

function cancelButtonPressed(){
	clearRelatedData();
	unBlockGrid("meter_grid");
	clearGridData("meter_grid");
	resetSelection("meter_grid","customer_grid","disconn_history_this_grid","disconn_history_all_grid");
}

var transactionUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BANK_TRANSACTION_SERVICE+'&method=getBankTransactionList'+'&extraFilter=area';
$("#transaction_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: transactionUrl,
   	jsonReader:{
    	repeatitems: false,
        id: "trans_id"
	},
    colNames: ['Trans. Id','Trans. Date', 'Particulars','Debit','Credit','Trans. Type'],
    colModel: [{
			        name: 'trans_id',
			        index: 'trans_id',
			        sorttype: "string",
			        hidden:true
				},
            	{
	                name: 'trans_date',
	                index: 'trans_date',
	                align:'center',
	                width:70,
	                sorttype: 'date'
            	},
            	{
	                name: 'source_transaction_particulars',
	                index: 'source_transaction_particulars',
	                width:200,
	                sorttype: "string",
	                align:'left',
	                search: true,
            	},            	
            	{
	                name: 'debit',
	                index: 'debit',
	                sorttype: "string",
	                align:'right',
	                width:80,
	                search: true,
            	},
            	{
	                name: 'credit',
	                index: 'credit',
	                sorttype: "string",
	                align:'right',
	                width:80,
	                search: true,
            	},
            	{
	                name: 'transaction_type_name',
	                index: 'transaction_type_name',
	                sorttype: "string",
	                align:'left',
	                search: true,
            	},
                      
            	
        ],   	
    height: $("#transaction_grid_div").height()-80,
    width: $("#transaction_grid_div").width(),       	
   	pager: '#transaction_grid_pager',
   	sortname: 'trans_date',
    sortorder: "asc",    
	caption: "Bank Deposit-Withdraw History",	
	footerrow: true,
	
	onSelectRow: function(id){ 
		fetchCollectionInfo(id);
    },	   	
	loadComplete: function () {
		var total_debit = jQuery("#transaction_grid").jqGrid('getCol', 'debit', false, 'sum');
		var total_credit = jQuery("#transaction_grid").jqGrid('getCol', 'credit', false, 'sum');
	
		
		jQuery("#transaction_grid").jqGrid('footerData','set', {source_transaction_particulars: 'Total:  ',debit:total_debit,credit:total_credit});
	}/*,
	rowattr: function (rd) {
        if (rd.collection_amount != rd.payable_amount) {
            return {"class": "redBackgroundRow"};
        }
        
    }*/
}));

function searchTransaction(){	
	var validate=validateField("search_bank_id","search_from_date","search_to_date");
	if(validate==false) return;
	
	var ruleArray=[["Ledger.bank_id","Ledger.Trans_date","Ledger.Trans_date"],["eq","ge","le"],[$("#search_bank_id").val(),$("#search_from_date").val(),$("#search_to_date").val()]];
	
	if($("#search_transaction_type").val()!=""){
		ruleArray[0].push("ledger.trans_type");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#search_transaction_type").val());
	}
	if($("#search_branch_id").val()!=""){
		ruleArray[0].push("ledger.branch_id");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#search_branch_id").val());
	}
	if($("#search_account_no").val()!=""){
		ruleArray[0].push("ledger.account_no");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#search_account_no").val());
	}
	
	var postData=getPostFilter("transaction_grid",ruleArray);
	
	$("#transaction_grid").jqGrid('setGridParam',{search: true,postData: {},page:1,datatype:'json'});    		
	reloadGrid("transaction_grid");	
}

function resetSearchForm(){
	clearField("search_bank_id","search_branch_id","search_account_no","searc_transaction_type","search_from_date","search_to_date");
}
$("#search_from_date").val(getCurrentDate());
$("#search_to_date").val(getCurrentDate());

function updateParticulars(elementId){	
	$("#source_transaction_particulars").val($("#"+elementId+" option:selected").text());	
}


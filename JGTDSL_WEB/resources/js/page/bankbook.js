var g_bankId,g_branchId,g_accountNo,g_transDate,g_month,g_year,g_transtype;
$('#cb_transaction_grid').bind( "click" );

var transactionUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BANK_TRANSACTION_SERVICE+'&method='+jsEnum.AUTH_TRANSACTION_LIST;
$("#transaction_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: transactionUrl,
   	jsonReader:{
    	repeatitems: false,
        id: "trans_id"
	},
    colNames: ['Trans. Id','Trans. Date', 'Particulars','Reconcilation Reason','Debit','Credit','Balance','Trans. Type'],
    colModel: [
            	{
			        name: 'trans_id',
			        index: 'trans_id',
			        width:40,
			        align:'center',
			        sorttype: "string"
				},
			
				{
	                name: 'trans_date',
	                index: 'trans_date',
	                width:50,
	                align:'center',
	                sorttype: 'date',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="trans_date_'+rowObject.trans_id+'" value="'+rowObject.trans_date+'" disabled="disabled" style="width:120px;height:10px;text-align:right;"/>';
    				}
            	},
            	{
	                name: 'particulars',
	                index: 'particulars',
	                width:200,
	                sorttype: "string",
	                align:'left',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="particulars_'+rowObject.trans_id+'" value="'+rowObject.particulars+'" disabled="disabled" style="width:300px;height:10px;text-align:right;"/>';
    				}
            	},
            	{
	                name: 'recon_cause',
	                index: 'recon_cause',
	                width:200,
	                sorttype: "string",
	                align:'left',
	                search: true,
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="recon_cause_'+rowObject.trans_id+'" value="'+rowObject.recon_cause+'" disabled="disabled" style="width:300px;height:10px;text-align:right;"/>';
    				}
            	},
            	{
	                name: 'debit',
	                index: 'debit',
	                width:120,
	                sorttype: "string",
	                align:'right',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="debit_amount_'+rowObject.trans_id+'" value="'+rowObject.debit+'" disabled="disabled" style="width:120px;height:10px;text-align:right;"/>'+
        					  '<input type="hidden" id="trans_id'+rowObject.trans_id+'" value="'+rowObject.trans_id+'" />';
    				}
            	},
            	{
	                name: 'credit',
	                index: 'credit',
	                width:120,
	                sorttype: "string",
	                align:'right',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="credit_amount_'+rowObject.trans_id+'" value="'+rowObject.credit+'" disabled="disabled" style="width:120px;height:10px;text-align:right;"/>';
    				}
            	},
            	{
	                name: 'balance',
	                index: 'balance',
	                width:120,
	                sorttype: "string",
	                align:'right',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="balance_amount_'+rowObject.trans_id+'" value="'+rowObject.balance+'" disabled="disabled" style="width:70px;height:10px;text-align:right;"/>';
    				}
            	},
            	{
	                name: 'trans_type',
	                index: 'trans_type',
	                width:100,
	                align:'left',
	                sorttype: "string",
	                search: true,
	                edittype: "select",
					formatter:"select",					
                    stype:"select",
                    editoptions: transaction_type,
                  	
            	}
                      
            	
        ],   	
	height: $("#contentPanel").height()-$("#top_div").height()-240,
   	pager: '#transaction_grid_pager',
   	sortname: 'trans_date',
    sortorder: "asc",    
	caption: jqCaption.UNAUTHORIZED_TRANSACTION,	
	footerrow: true,
    userDataOnFooter:true, 	
	multiselect: true,
	rowNum:120,
    rowList:[10,20,30],
	onSelectRow: function(rowId){ handleSelectedRow(rowId); },
	loadComplete: function () {
	     
	        handleSelectedRow();
	}
}));
jQuery("#transaction_grid").jqGrid('navGrid','#transaction_grid_pager',$.extend({},footerButton,{search:false,refresh:false}),{},{},{},{multipleSearch:false});
jQuery("#transaction_grid").jqGrid('footerData','set', {trans_id: 'Total:', debit:"",credit: "",balance:""});
function handleSelectedRow() {
	
	
	
	var total_debit=0;
	var total_credit=0;
	var total_balance=0;
	var closing_balance=0;
	var reconcilation_amount=0;
	var rows = jQuery("#transaction_grid").getDataIDs();
		for(a=0;a<rows.length;a++)
		 {	
		    row=jQuery("#transaction_grid").getRowData(rows[a]);
		  
		    closing_balance=parseFloat(closing_balance)+parseFloat($("#debit_amount_"+row.trans_id).val());
		    if($("#jqg_transaction_grid_"+row.trans_id).prop('checked')==true) 
		    { 
		    	$("#recon_cause_"+row.trans_id).removeAttr('disabled');
		    	reconcilation_amount=parseFloat(reconcilation_amount)+parseFloat($("#debit_amount_"+row.trans_id).val())-parseFloat($("#credit_amount_"+row.trans_id).val());
		    }else
		    	{
		    	$("#recon_cause_"+row.trans_id).attr('disabled', 'disabled');
		    	total_debit=parseFloat(total_debit)+parseFloat($("#debit_amount_"+row.trans_id).val());
		    	total_credit=parseFloat(total_credit)+parseFloat($("#credit_amount_"+row.trans_id).val());
		    	total_balance=parseFloat(total_balance)+parseFloat($("#balance_amount_"+row.trans_id).val());
				
		    	
		    	}
		}
		
		//alert(total_debit);
		/*$("#debit_amount_undefined").val(total_debit.toFixed(2));
		$("#crdit_amount_undefined").val(total_credit.toFixed(2));
		$("#balance_amount_undefined").val(total_balance.toFixed(2));*/
		
		// below one also right
		jQuery("#transaction_grid").jqGrid('footerData','set', {trans_id: 'Total:', debit:total_debit.toFixed(2),credit:total_credit.toFixed(2),balance:total_balance.toFixed(2)});
		
		$("#closing_balance").val(closing_balance.toFixed(2));		
		$("#reconcilation").val(reconcilation_amount.toFixed(2));
		

	}

	  $('#cb_transaction_grid').on("click", function() {
	    handleSelectedRow();
	});
	  
jQuery("#transaction_grid").jqGrid('navGrid','#transaction_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {fetchUnAuthorizedTransaction();}}),{},{},{},{multipleSearch:true});

$('#transaction_grid').jqGrid('navGrid','#transaction_grid_pager')
    .navButtonAdd('#transaction_grid_pager',{
        caption:"<b><font color='green'>Authorized Transactions</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
        buttonicon:"ui-icon-check", 
        id: "icon_approve_all",
        onClickButton: function(){
    	authorizeTransactions();
        }
});


function fetchUnAuthorizedTransaction(){

	g_bankId=$("#bank_id").val();
	g_branchId=$("#branch_id").val();
	g_accountNo=$("#account_id").val();
	g_month=$("#billing_month").val();
	g_year=$("#billing_year").val();;
	g_transtype=$("#transaction_type").val();
	
	if($("#bank_id").val()=="" || $("#branch_id").val()=="" || $("#account_id").val()=="")
		return;
	
	var ruleArray=[["Ledger.bank_id","Ledger.branch_id","Ledger.account_no"],["eq","eq","eq"],[$("#bank_id").val(),$("#branch_id").val(),$("#account_id").val()]];
	

	if($("#bill_year").val()!=""){
		ruleArray[0].push("to_char(trans_date,'YYYY')");
		ruleArray[1].push("eqYear");
		ruleArray[2].push($("#billing_year").val());
	}
	if($("#bill_month").val()!=""){
		ruleArray[0].push("to_char(trans_date,'mm')");
		ruleArray[1].push("eqMonth");
		ruleArray[2].push($("#billing_month").val());
	}
	
	if($("#transaction_type").val()!=""){
		ruleArray[0].push("trans_type");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#transaction_type").val());
	}

	
	var postData=getPostFilter("transaction_grid",ruleArray);
	
	$("#transaction_grid").jqGrid('setGridParam',{search: true,postData: postData,page:1,datatype:'json'});    		
	reloadGrid("transaction_grid");
	getOpeningBalance(g_bankId,g_branchId,g_accountNo,g_month,g_year,g_transtype);

}

function calculateDifference()
{
	var balance_staement=$("#balance_statement").val();
	var closing_balance=$("#closing_balance").val();
	var difference=parseFloat(closing_balance)-parseFloat(balance_staement);
	$("#difference").val(difference.toFixed(2));
}

function getOpeningBalance(g_bankId,g_branchId,g_accountNo,g_month,g_year,g_transtype){

	alert("getOpeningBalance");
	$.ajax({
		    url: 'getOpeningBalance.action',
		    type: 'POST',
		    data: {bank_id:g_bankId,branch_id:g_branchId,account_no:g_accountNo,month:g_month,year:g_year,trans_type:g_transtype},
		    cache: false,
		    success: function (response) {
	  
		    	$("#opening_balance").val(response);
		    	
		    
		    }
		    
		  });	
}
function addMoreReconcilation(){
	$.nsWindow.open({
		movable:true,
		title: 'Multi Month Collection Entry',
		width: 1200,
		height: 700,
		dataUrl: 'addMoreReconcilationHome.action',
		theme:jsVar.MODAL_THEME
    });
    
}
function confirmBankBook(){
	
	var rows = jQuery("#transaction_grid").getDataIDs();
    var billInfo="";
    var bill_ids="";
	for(var a=0;a<rows.length;a++)
	 {	
	    row=jQuery("#transaction_grid").getRowData(rows[a]);
	    if($("#jqg_transaction_grid_"+row.trans_id).prop('checked')==true) 
	    { 
	    	billInfo+=row.trans_id+"#"+$("#recon_cause_"+row.trans_id).val()+"@";
	    	bill_ids+=row.trans_id+",";
	    }
	}

	alert(billInfo);
	alert(bill_ids);
}

function showUnauthTransactions(bank_id,branch_id,branch_name,account_no,account_name){
	
	$("#bank_id").val(bank_id);	
	clearSelectBox("branch_id","account_id");
	addOption("branch_id",branch_id,branch_name);
	addOption("account_id",account_no,account_name);
	$("#branch_id").val(branch_id);	
	$("#account_id").val(account_no);	
	$("#transaction_date").val("");
	fetchUnAuthorizedTransaction();
}

function authorizeTransactions(){

	if(g_bankId=="" || g_branchId=="" || g_accountNo==""){
		alert("Please select a bank account for transaction authorization.");
		return;
	}
	if(g_transDate==""){
		alert("You must select a date for transaction authorization");
		return;
	}
	
//	/disableButton("btn_authorization");
    var postData={"bank_id":g_bankId,"branch_id":g_branchId,"account_no":g_accountNo,"trans_date":g_transDate};		
    $.ajax({
		    url: 'authorizeTransaction.action',
		    type: 'POST',
		    data: postData,	
		    success: function (response) {
		  		if(response.status=="OK"){
		  			//enableButton("btn_save");
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}
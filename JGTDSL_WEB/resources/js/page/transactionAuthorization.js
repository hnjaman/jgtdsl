var g_bankId,g_branchId,g_accountNo,g_transDate;

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "transaction_date",
    trigger    : "transaction_date",
    onSelect   : function() { this.hide();}}));

$("#transaction_date").val(getCurrentDate());

var transactionUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BANK_TRANSACTION_SERVICE+'&method='+jsEnum.UNAUTH_TRANSACTION_LIST;
$("#transaction_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: transactionUrl,
   	jsonReader:{
    	repeatitems: false,
        id: "trans_id"
	},
    colNames: ['Trans. Id','CustomerID','Trans. Date', 'Particulars','Debit','Credit','Ref Id','Trans. Type','Reject'],
    colModel: [{
			        name: 'trans_id',
			        index: 'trans_id',
			        width:40,
			        align:'center',
			        sorttype: "string",
			        search: true,
				},{
			        name: 'customer_id',
			        index: 'customer_id',
			        width:40,
			        align:'center',
			        sorttype: "string",
			        search: true,
				},
            	{
	                name: 'trans_date',
	                index: 'trans_date',
	                width:50,
	                align:'center',
	                
            	},
            	{
	                name: 'particulars',
	                index: 'particulars',
	                width:200,
	                sorttype: "string",
	                align:'left',
	                search: true,
            	},
            	{
	                name: 'debit',
	                index: 'debit',
	                width:120,
	                sorttype: "string",
	                align:'right',
	                search: true,
            	},
            	{
	                name: 'credit',
	                index: 'credit',
	                width:120,
	                sorttype: "string",
	                align:'right',
	                search: true,
            	},
            	{
	                name: 'ref_id',// collection_id or bill_id
	                index: 'ref_id',
	                width:120,
	                sorttype: "string",
	                align:'center',
	                search: true
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
                    editoptions: transaction_type
                    	
            	},
            	{
	                name: 'reject',
	                index: 'reject',
	                width:100,
	                align:'center',
	                fixed: true,
	                formatter: function (cellvalue, options, rowObject) {
	                    return "<div id='loader' style='display:none'>deleting..</div><img src='/JGTDSL_WEB/resources/images/delete_16.png' id='deleteBtn' alt='reject' style='cursor:pointer;' onclick=\"deleteBankTransaction("+rowObject.trans_id+")\" />";
	                }
            	}
                      
            	
        ],   	
	height: $("#contentPanel").height()-$("#top_div").height()-120,
   	pager: '#transaction_grid_pager',
   	sortname: 'trans_date',
    sortorder: "asc",    
	caption: jqCaption.UNAUTHORIZED_TRANSACTION,	
	footerrow: true,
	
	onSelectRow: function(id){ 
	//	fetchCollectionInfo(id);
    },	   	
	loadComplete: function () {    	    	

    	//getTotalDebitCredit();
    	
	}
}));



jQuery("#transaction_grid").jqGrid('navGrid','#transaction_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {fetchUnAuthorizedTransaction();}}),{},{},{},{multipleSearch:true});

$('#transaction_grid').jqGrid('navGrid','#transaction_grid_pager')
    .navButtonAdd('#transaction_grid_pager',{
        caption:"<b><font color='green'>Authorize Transactions</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
        buttonicon:"ui-icon-check", 
        id: "icon_approve_all",
        onClickButton: function(){
    	authorizeTransactions();
        }
});

function deleteBankTransaction(trans_id){
	  	
	  var postData={"transaction_id":trans_id};		
	    $.ajax({
			    url: 'deleteBankTransaction.action',
			    type: 'POST',
			    data: postData,	
			    beforeSend: function() {
			        $('#loader').show();
			        $('#deleteBtn').hide();
			     },
			     complete: function(){
			        $('#loader').hide();
			        $('#deleteBtn').hide();
			     },
			    success: function (response) {
			  		if(response.status=="OK"){
			  			$('#transaction_grid').jqGrid('delRowData',trans_id);
			  			getTotalDebitCredit();
			  			fetchUnAuthorizedTransaction();
			  		}
			   		   
			    }		    
			  });			 
}
function getTotalDebitCredit(trans_id){
	  var whereClause="";
	  if($("#bank_id").val()!="") whereClause=whereClause+" bank_id='"+$("#bank_id").val()+"' And ";
	  if($("#branch_id").val()!="") whereClause=whereClause+" branch_id='"+$("#branch_id").val()+"' And ";
	  if($("#account_id").val()!="") whereClause=whereClause+" account_no='"+$("#account_id").val()+"' And ";
	  if($("#transaction_type").val()!="") whereClause=whereClause+" trans_type="+$("#transaction_type").val()+" And ";
	  if($("#transaction_date").val()!="")
		  {
		  whereClause=whereClause+" trans_date=to_date('"+$("#transaction_date").val()+"','dd-MM-YYYY') And ";
		  } 
	  else
		  {
		  whereClause=whereClause+" trans_date=to_date('"+getCurrentDate()+"','dd-MM-YYYY') And ";
		  }
	
	  
	  if(whereClause.length>0)
		  whereClause=whereClause.substring(0, whereClause.length-4);
	  
	  console.log(whereClause);
	  
	  var postData={"whereClause":whereClause};		
	    $.ajax({
			    url: 'getTotalDebitCredit.action',
			    type: 'GET',
			    data: postData,	
			    success: function (response) {
	    			var debit_credit_arr=response.split("#");
		  			jQuery("#transaction_grid").jqGrid('footerData','set', {particulars: 'Total:  ',debit:debit_credit_arr[0],credit:debit_credit_arr[1]});
			    }		    
			  });
}

function fetchUnAuthorizedTransaction(){

	g_bankId=$("#bank_id").val();
	g_branchId=$("#branch_id").val();
	g_accountNo=$("#account_id").val();
	g_transDate=$("#transaction_date").val();
	
	if($("#bank_id").val()=="" || $("#branch_id").val()=="" || $("#account_id").val()=="")
		return;
	
	var ruleArray=[["Ledger.bank_id","Ledger.branch_id","Ledger.account_no"],["eq","eq","eq"],[$("#bank_id").val(),$("#branch_id").val(),$("#account_id").val()]];
	
	if($("#transaction_date").val()!=""){
		ruleArray[0].push("trans_date");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#transaction_date").val());
	}
	
	if($("#transaction_type").val()!=""){
		ruleArray[0].push("trans_type");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#transaction_type").val());
	}
	
	var postData=getPostFilter("transaction_grid",ruleArray);
	$("#transaction_grid").jqGrid('setGridParam',{search: true,postData: postData,page:1,datatype:'json'});    		
	reloadGrid("transaction_grid");
	getTotalDebitCredit();
	

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
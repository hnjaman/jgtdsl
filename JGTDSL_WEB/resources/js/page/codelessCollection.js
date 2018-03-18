
autoSelect("area_id","category_id");
//$("#collection_date").val(getCurrentDate());

function cancelButtonPressed(){
	unBlockGrid("collection_grid");
}

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide();getCollectionHistoryByDate();}}));

var customerCollectionListUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method='+jsEnum.COLLECTION_LIST;
var DAILY_CODELESS_COLLECTION_LIST="getAccountwiseDailyCodelessCollectionList";
var transactionUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method='+DAILY_CODELESS_COLLECTION_LIST;

$("#collection_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url:transactionUrl,
   	jsonReader: {
		repeatitems: false,
		id: "scroll_no"
	},
	colNames: ['Scroll','Month(From)','Month(To)','Gas Bill','Surcharge','Customer Code', 'Customer Name','Address ','Bank Code','Status','User Name'],
    colModel: [{
	                name: 'scroll_no',
	                index: 'scroll_no',
	                width:40,
	                align:'center',
	                sorttype: 'string',
	                search: true,
            	},
            	
            	{
	                name: 'from_month',
	                index: 'from_month',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
	                name: 'to_month',
	                index: 'to_month',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
	                name: 'advanced_amount',
	                index: 'advanced_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'surcharge_amount',
	                index: 'surcharge_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
            		name: 'customer_id',
	                index: 'customer_id',
	                width:40,
	                sorttype: 'string',
	                align:'center',
	                search:true,
            	},
            	{
	                name: 'customer_name',
	                index: 'customer_name',
	                width:40,
	                sorttype: "string",
	                search: true,
            	},
            	{
            		name: 'address',
	                index: 'address',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
            		name: 'bank_id',
	                index: 'bank_id',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
            		name: 'status',
	                index: 'status',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	},
            	{
            		name: 'inserted_by',
	                index: 'inserted_by',
	                width:40,
	                sorttype: "string",
	                align:'center',
	                search: true,
            	}
                      
            	
        ],   
	height: $("#collection_grid_div").height()-102,
	width: $("#collection_grid_div").width()+10,
   	sortname: 'COLLECTION_ID',
    sortorder: "desc",	
	pager: '#collection_grid_pager',
   	caption: "Daily Collection History",
   	footerrow:true,
    userDataOnFooter:true,
	onSelectRow: function(id){ 
		var rowData = jQuery('#collection_grid').jqGrid ('getRowData', id);
		//here values will be set to the codeless collection form
		var strfm =rowData.from_month;
		var fm = strfm.split("-");
		var strtm =rowData.to_month;
		var tm = strtm.split("-");
		$("#from_month").val($.trim(fm[0]));
		//$("#from_month").prop('readonly', true);
		
		$("#from_year").val($.trim(fm[1]));
		//$("#from_year").prop('readonly', true);
		
		$("#to_month").val($.trim(tm[0]));
		//$("#to_month").prop('readonly', true);
		
		$("#to_year").val($.trim(tm[1]));
		//$("#to_year").prop('readonly', true);
		
		$("#advanced_amount").val(rowData.advanced_amount);
		$("#advanced_amount").prop('disabled', true);
		
		$("#surcharge_amount").val(rowData.surcharge_amount);
		$("#surcharge_amount").prop('disabled', true);
		
		$("#bank_id").prop('disabled', true);
		$("#branch_id").prop('disabled', true);
		$("#account_id").prop('disabled', true);
		$("#collection_date").prop('disabled', true);
		$("#customer_name").prop('disabled', true);
		$("#address").prop('disabled', true);
		$("#btn_save_codeless").prop('disabled', true);
		$("#btn_save_advance").prop('disabled', false);
		
		
		
		$("#customer_id").val($.trim(rowData.customer_id));
		$("#is_codeless").val(rowData.is_codeless);
		$("#customer_name").val(rowData.customer_name);
		$("#address").val(rowData.address);
		$("#scroll_no").val(rowData.scroll_no);
		//added later
		$('#customer_id').focus();
    	$('#customer_id').select();
		

		
    },	   	
   	loadComplete: function () {
   		           }
}));

jQuery("#collection_grid").jqGrid('navGrid','#collection_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {}}),{},{},{},{multipleSearch:true});
jQuery("#collection_grid").jqGrid("setLabel","full_name","",{"text-align":"left","padding-left":"10px"});

function calculateFooterSum(){	
	var advanced_amount = jQuery("#collection_grid").jqGrid('getCol', 'advanced_amount', false, 'sum');	
	jQuery("#collection_grid").jqGrid('footerData','set', {collection_date_f1: 'Total:  '+$("#collection_grid").getGridParam("reccount"),advanced_amount:advanced_amount});
}



$('#from_month').keyup(function(e){
	$("#to_month").val($("#from_month").val());
    if(e.keyCode == 13)
    {
    	document.getElementById("from_year").focus();
    	document.getElementById("from_year").select();
		
    }
});

$('#from_year').keyup(function(e){
	
	$("#to_year").val($("#from_year").val());
	
    if(e.keyCode == 13)    {      	
    	$('#to_month').focus();
    	$('#to_month').select();
    }
});

$('#to_month').keyup(function(e){
	
    if(e.keyCode == 13)    {      	
    	$('#to_year').focus();
    	$('#to_year').select();
    }
});

$('#to_year').keyup(function(e){
	
    if(e.keyCode == 13)    {     	
    	if ($('#advanced_amount').attr('disabled')) {
    		$('#advanced_amount').removeAttr('disabled');
        }
    	$('#advanced_amount').focus();
    }
});


$('#advanced_amount').keyup(function(e){
    if(e.keyCode == 13)									// prevent null amount collection, sujon 7 jan 18
    {
    	if($('#advanced_amount').val()>0){
    		$('#surcharge_amount').focus();
    		enableField("btn_save");
    	}
    	else{
        	$('#advanced_amount').focus();
        	disableField("btn_save");
    	}
    }
    
});

$('#surcharge_amount').keyup(function(e){
	if(e.keyCode == 13){
		$('#customer_id').focus();	
	}
});

$('#customer_id').keyup(function(e){
	
	if(e.keyCode == 13){
		var isOthersDisabled = $('#customer_name').prop('disabled');
		if(isOthersDisabled){
			//save advanced collection
			saveAdvancedCollection(2);
		}
		else{
			$('#customer_name').focus();
		}
		
	}
});

$('#customer_name').keyup(function(e){
	if(e.keyCode == 13){
		$('#address').focus();
	}
});

$('#address').keyup(function(e){
	if(e.keyCode == 13){
		$(this).val( $(this).val().replace( /\r?\n/gi, '' ) );
		saveAdvancedCollection(1);
		$('#from_month').focus();
		var fields = ["customer_name","customer_id","address","customerType","advanced_amount","from_month","to_month","surcharge_amount"];
    	clearField.apply(this,fields);
    	$("#customer_id").val($("#area_id").val()+"01");
	}
});

function collectionForm(plainFieldMethod){	
	var fields = ["customer_id","customer_code","bill_month","advanced_amount"];
	
	plainFieldMethod.apply(this,fields);
}


function saveAdvancedCollection(x){
	$("#is_codeless").val(x);
	var isValid=true;
	isValid=validateField("bank_id","branch_id","account_id","collection_date","advanced_amount","from_month","from_year","to_month","to_year");
	
	if(isValid==true)	 {
		var form = document.getElementById('codelessCollectionForm');
		disableButton("btn_save_codeless");
		var formData = new FormData(form);
		  $.ajax({
		    url: 'saveAdvancedCollection.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		    if(response.status=="OK")
		    {
		    	//clearing form
		    	
		    	//
		    	$("#from_month").prop('readonly', false);
				$("#from_year").prop('readonly', false);
				$("#to_month").prop('readonly', false);
				$("#to_year").prop('readonly', false);
				$("#advanced_amount").prop('disabled', false);
				$("#surcharge_amount").prop('disabled', false);				
				$("#bank_id").prop('disabled', false);
				$("#branch_id").prop('disabled', false);
				$("#account_id").prop('disabled', false);
				$("#collection_date").prop('disabled', false);
				$("#customer_name").prop('disabled', false);
				$("#address").prop('disabled', false);
				$("#btn_save_codeless").prop('disabled', false);
				$("#btn_save_advance").prop('disabled', true);
				
		    	//end of: clearing form
				
		    	
		    	
				var fields = ["customer_name","customer_id","address","customerType","advanced_amount","from_month","to_month","from_year","to_year","surcharge_amount"];
		    	clearField.apply(this,fields);
		       getCollectionHistoryByDate();
		    }
		    	$("#msg_div").html(response.message);		       
		    	enableButton("btn_save_codeless");	    
		    }		    
		  });		
		}
}


function getTotalCollectionByDateAccount(){

	if($("#collection_date").val()=="" || $("#account_id").val()=="")
		return;
	
	$.ajax({
 		  type: 'POST',
 		  url: 'getTotalCollectionByDateAccount.action?collection.collection_date='+$("#collection_date").val()+"&collection.account_no="+$("#account_id").val(),
 		  success:function(data){
			$("#total_collection").html(data.total_collection);
 		  },
 		  error:function(){
 			$("#btn_save").removeAttr("disabled");
 		  }
 	});
	
	
}

var g_bankId,g_branchId,g_accountNo,g_collectionDate;



function getCollectionHistoryByDate(){

	g_bankId=$("#bank_id").val();
	g_branchId=$("#branch_id").val();
	g_accountNo=$("#account_id").val();
	g_collectionDate=$("#collection_date").val();
	
	if($("#bank_id").val()=="" || $("#branch_id").val()=="" || $("#account_id").val()==""||$("#collection_dates").val()=="")
		return;
	
	var ruleArray=[["bank_id","branch_id","account_no","STATUS"],["eq","eq","eq","eq"],[$("#bank_id").val(),$("#branch_id").val(),$("#account_id").val(),0]];
	

	if($("#collection_date").val()!=""){
		ruleArray[0].push("collection_date");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#collection_date").val());
	}

	var postData=getPostFilter("collection_grid",ruleArray);
	
	$("#collection_grid").jqGrid('setGridParam',{search: true,postData: postData,page:1,datatype:'json'});    	
	getTotalCollectionByDateAccount();
	reloadGrid("collection_grid");	
}



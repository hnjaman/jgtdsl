
autoSelect("area_id","category_id");
//$("#collection_date").val(getCurrentDate());

function cancelButtonPressed(){
	unBlockGrid("collection_grid");
}

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide();getCollectionHistoryByDate();}}));



//dues months for collection
//function popdivcoll (result){
//	alert(result+" ");
//	//alert("  "+r[0]);
//    $('#dueListbyStringcoll').val(result);
//}
//end




$('#customer_code').keyup(function(e){
	
    if(e.keyCode == 13)
    {
    	    	
    	var fields = ["customer_name","customerType","advanced_amount","from_month","to_month","applienceAndDue"];
    	clearField.apply(this,fields);
    	
        var customer_id=$("#customer_code").val();
        
        if(customer_id.substring(2,4)==='01' || customer_id.substring(2,4)==='09' ){
        	//due months for collection
        	$('#dueListbyStringcoll').val("testing");
           //  $.post("getDuesListByString.action", {'customer_id': customer_id},popdivcoll);

        	$.ajax({
        		url: "getDuesListByString.action",
        		data: { customer_id : customer_id },
        		dataType: 'text',		    
        		type: 'POST',
        		success: function (response){
        			$("#dueListbyStringcoll").val(response);
        		}    
        		});
             
             
        	
        	ajaxLoad("applienceAndDue","getApplianceInfoPlain.action?customer_id="+customer_id);
        	//$("#applienceAndDue").empty();
        	//$( "#appTable" ).removeClass( "table table-bordered" );
             
        	//end
	
             
             
             
             
        
        $("#customer_id").val(customer_id);
        bill_month="";
    	bill_year="";    	
    	getCustInformation(customer_id);       
    	getCollectionHistoryByDate();
        
        if ($('#advanced_amount').attr('disabled')) {
    		$('#advanced_amount').removeAttr('disabled');
        } 
        $('#from_month').focus();
    }else{
    	alert("Please enter non-metered customer only");
    }
		
    }
    
});




function getCustInformation(customer_id)
{
	
 	$.ajax({
   		  type: 'POST',
   		  url: 'getCustInfo4Collection.action',
   		  data: { customer_id:customer_id},
   		  success:function(data){ 
   	
   			if ((data[0].customer_id === "") || (data[0].customer_id === null)){
					$("#msg_div").html("Invalid Customer Code.");				
					clearField.apply(this,fields);
					advancedCollectionForm(disableField);
					enableField("customer_id","customer_code");
		    	 $('#customer_code').focus();
			    	return;
			}
   			
   		   			  
				$("#customer_name").val(data[0].customer_name);
				$("#customerType").val(data[0].isMeter_str);
							
				
		  }
   	});
}

var customerCollectionListUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method='+jsEnum.COLLECTION_LIST;


var DAILY_COLLECTION_LIST="getAccountwiseDailyCollectionList";
var transactionUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method='+DAILY_COLLECTION_LIST;

$("#collection_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url:transactionUrl,
   	jsonReader: {
		repeatitems: false,
		id: "customer_id"
	},
	colNames: ['Scroll','Customer Code', 'Customer Name','Month(From)','Month(To)','Gas Bill','Surcharge','Payment Date','Bank Code','Status','User Name'],
    colModel: [{
	                name: 'scroll',
	                index: 'scroll',
	                width:40,
	                align:'center',
	                sorttype: 'string',
	                search: true,
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
            		name: 'collection_date',
	                index: 'collection_date',
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
    		//saveAdvancedCollection();
    		$('#surcharge_amount').focus();
    		enableField("btn_save");
    	}
    	else{
    		//alert("Bill Amount can not be Null or Zero");
        	$('#advanced_amount').focus();
        	disableField("btn_save");
    	}
    }
    
});

$('#surcharge_amount').keyup(function(e){
	if(e.keyCode == 13){
		saveAdvancedCollection();
//		$('#customer_name').clear();
//		$('#area_id').clear();
//		$('#customerType').clear();
		$('#customer_code').focus();
		
		var fields = ["customer_name","customer_id","customerType","advanced_amount","from_month","to_month","surcharge_amount"];
    	clearField.apply(this,fields);
    	
    	$("#dueListbyStringcoll").val(" ");
    	
		
	}
});


function collectionForm(plainFieldMethod){	
	var fields = ["customer_id","customer_code","bill_month","advanced_amount"];
	
	plainFieldMethod.apply(this,fields);
}


function saveAdvancedCollection(){
	var isValid=true;
	isValid=validateField("customer_id","bank_id","branch_id","account_id","collection_date");
	
	if(isValid==true)	 {
		var form = document.getElementById('advancedCollectionForm');
		disableButton("btn_save");
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
		       collectionForm(clearField);
		       getCollectionHistoryByDate();
		    }
		    	$("#msg_div").html(response.message);		       
		    	enableButton("btn_save");	    
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
	
	var ruleArray=[["bcm.bank_id","bcm.branch_id","bcm.account_no"],["eq","eq","eq"],[$("#bank_id").val(),$("#branch_id").val(),$("#account_id").val()]];
	

	if($("#collection_date").val()!=""){
		ruleArray[0].push("bcm.collection_date");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#collection_date").val());
	}

	var postData=getPostFilter("collection_grid",ruleArray);
	
	$("#collection_grid").jqGrid('setGridParam',{search: true,postData: postData,page:1,datatype:'json'});    	
	getTotalCollectionByDateAccount();
	reloadGrid("collection_grid");	
}



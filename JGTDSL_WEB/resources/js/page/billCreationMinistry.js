var p_customer_id="";
var p_month="";
var p_year="";
var p_area_id="";
var p_customer_category="";
var p_bill_id="";

$("#bill_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.BillingService&method=getMinistryBilledCustomerList',
   	jsonReader: {
            repeatitems: false,
            id: "bill_id"
	},
   colNames: ['Bill ID','Customer Id', 'Customer Name','Total Bill Amount','Is Metered','Ministry Name'],
   colModel: 	[{
			       name: 'bill_id',
			       index: 'bill_id',
			       width:60,
			       align:'center',
			       sorttype: 'string',
			       search: true
				},
              
				{
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
	                name: 'bill_amount',
	                index: 'bill_amount',
	                sorttype: "string",
	                align:'right',
	                width:80,
	                search: true,
            	},      
            	{
	                name: 'is_metered',
	                index: 'is_metered',
	                sorttype: "string",
	                align:'center',
	                width:30,
	                search: false,
            	},
            	{
	                name: 'ministry_name',
	                index: 'ministry_name',
	                sorttype: "string",
	                align:'left',
	                width:110,
	                search: false,
            	},
//            	 {
//                    name: 'Select',
//                    width: 30,
//                    align: "center",
//                    editoptions: { value: "True:False" },
//                    editrules: { required: true },
//                    formatter: "checkbox",
//                    formatoptions: { disabled: false , defaultValue: "True"},
//                    editable: true  }
            	
            	],
	datatype: 'local',
	height: $("#bill_grid_div").height()-80,
	width: $("#bill_grid_div").width()-2,
   	pager: '#bill_grid_pager',
   	sortname: 'bill_id',
    sortorder: "desc",
    multiselect: true,
	caption: "Ministry Customers List(Billed)",
	
	/*
    beforeSelectRow : function(rowid, e) {
    	p_bill_id=rowid;
	alert(rowid);
	var rowData = jQuery('#bill_grid')
			.jqGrid('getRowData', rowid);
	alert(rowData.bill_amount);
	var $self = $(this), 
	iCol = $.jgrid.getCellIndex($(e.target).closest("td")[0]), 
	cm = $self.jqGrid("getGridParam","colModel");
	if (cm[iCol].name === "Select" && e.target.tagName.toUpperCase() === "INPUT") {
		// set local grid data
		alert(JSON.stringify(rowData));
	}
	$('#bill_grid').attr('checked', true);
	return true; // allow selection
}*/
}));

//get checked bill id

$("#biiId").click(function(){
	 var grid = $("#bill_grid");
     var rowKey = grid.getGridParam("selrow");

     if (!rowKey)
         alert("Nothing is selected");
     else {
         var selectedIDs = grid.getGridParam("selarrrow");
         var result = "";
         for (var i = 0; i < selectedIDs.length; i++) {
             result += selectedIDs[i] + ",";
         }

         alert(result);
     }                
});


jQuery("#bill_grid").jqGrid('navGrid','#bill_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {reloadBillGrid();}}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","bill_grid",["full_name"]);




function reloadBillGrid(){
	
    var ruleArray=[["bill_month","bill_year","area_id","CC.MINISTRY_ID"],["eq","eq","eq","eq"],[$("#billing_month").val(),$("#billing_year").val(),$('#area_id').val(),$('#MINISTRY_ID').val()]];
    var $grid = $('#bill_grid');
    var caption_extra="";
    
    if($("#billing_month").val()=="" || $("#billing_year").val()==""){
		clearGridData("bill_grid");
		$grid.jqGrid('setCaption','Please Select Month, Year');
		return;
	}
    
	caption_extra=". "+$("#area_id option:selected").text()+" ["+$("#MINISTRY_ID option:selected").text()+"] ";
	
	
	var postdata=getPostFilter("bill_grid",ruleArray);
   	$("#bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    		
	reloadGrid("bill_grid");
	
	$grid.jqGrid('setCaption', 'Ministry Customers List(Billed) - '+$("#billing_month option:selected").text()+', '+$("#billing_year").val()+caption_extra);
}

 
function validateAndProcessBilling()
{

 var isValid=valiateFields();

 
 
 if(isValid==true)	 {
	 $("#depositDetailDiv").show();
	var form = document.getElementById('billProcessForm');
	var formData = new FormData(form);
	  $.ajax({
	    url: 'processBill.action',
	    type: 'POST',
	    data: formData,
	    async: false,
	    cache: false,
	    contentType: false,
	    processData: false,
	    success: function (response) {
	    $("#depositDetailDiv").hide();
	    if(response.status=="OK")
	    {
	       var customer_data = [{"customer_id":$("#customer_id").val(),"full_name":$("#customer_name").val(),"category_name":$.trim($("#customer_category option:selected").text()),"area_name":$.trim($("#area_id option:selected").text()),"mobile":$("#mobile").val(),"status":$("#connection_status").val() }];
		   $("#customer_table").jqGrid('addRowData', $("#customer_id").val(),customer_data[0] , "first");
		   cleanAllFields();				     
	    }	
	       $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    
	    }
	    
	  });
	
	}
}

function valiateFields(){
	 var isValid=true;
	 var bill_for=getRadioCheckedValue("bill_parameter\\.bill_for"); 
	 
	 if(bill_for=="area_wise")
	 	isValid=validateField("area_id","issue_date");
	 else if(bill_for=="category_wise")
	 	isValid=validateField("area_id","customer_category","issue_date");
	 else if(bill_for=="individual_customer")
	 	isValid=validateField("customer_id","issue_date");

	 if(isValid==true)
	 	isValid=validateField("billing_month","billing_year");
	 
	 return isValid;
}
$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.METERED_CUSTOMER_LIST,
    	onSelect:function (){
    		getCustomerInfo("",$('#customer_id').val());
    	},
}));

$("#billing_month").val(getCurrentMonth());
$("#billing_year").val(getCurrentYear());
//$("#issue_date").val(getCurrentDate());
reloadBillGrid();

	
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
	

$('#cb_dues_bill_grid').unbind( "click" );
jQuery("#dues_bill_grid").jqGrid({
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.BillingService&method=getMeteredCustomerDueBillList',
   	jsonReader: {
            repeatitems: false,
            id: "bill_id"
	},
    colNames: ['Bill_id','Month','Year','Bill Amount','Due Date','Days','Surcharge','New Payable Amount'],
    colModel: [{    name: 'bill_id',
		            index: 'bill_id',
		            width:30,
		            align:'center',
		            sorttype: 'string',
		            search: true,		            
		    	},
                   {
	                name: 'bill_month',
	                index: 'bill_month',
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
		            search: true,
		    	},
            	{
	                name: 'billed_amount',
	                index: 'billed_amount',
	                width:40,
	                sorttype: "string",
	                align:'right',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="billed_amount_'+rowObject.bill_id+'" value="'+rowObject.billed_amount+'" disabled="disabled" style="width:70px;height:10px;text-align:right;"/>'+
        					  '<input type="hidden" id="bill_id_'+rowObject.bill_id+'" value="'+rowObject.bill_id+'" />';
    				}
            	},
            	{
	                name: 'due_date',
	                index: 'due_date',
	                width:40,
	                align:'center',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'days_elapsed',
	                index: 'days_elapsed',
	                sorttype: "string",
	                width:30,
	                align:'center',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="days_'+rowObject.bill_id+'" disabled="disabled" style="width:35px;height:10px;text-align:right;"/>';
    				}
            	},
            	{
	                name: 'surcharge',
	                index: 'surcharge',
	                sorttype: "string",
	                width:40,
	                align:'right',
	                formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="surcharge_amount_'+rowObject.bill_id+'" disabled="disabled" style="width:70px;height:10px;text-align:right;"/>';
    				}
	                
            	},
                {
                    name: 'new_payable_amount',
                    index: 'new_payable_amount',
                    sorttype: "string",
                    width:60,
                    align:'right',
                    formatter: function (cellValue, option,rowObject) {
        				return '<input type="text" id="new_amount_'+rowObject.bill_id+'" disabled="disabled" style="width:100px;height:10px;text-align:right;"/>';
    				}
                    
            	}
        ],
	datatype: 'local',
	multiselect: true,
	height: $("#right_div").height()-240,
	width: $("#right_div").width(),
   	rowNum:100,
	rownumWidth: 40,
   	pager: '#dues_bill_grid_pager',
   	sortname: 'bill_year,bill_month',
    sortorder: "asc",
	caption: "Dues bill list for the selected customer",
	footerrow:true, 
    userDataOnFooter:true, 	
	onSelectRow: function(rowId){ handleSelectedRow(rowId); },
	loadComplete: function () {
	calculateSurcharge();
	
	}
});

jQuery("#dues_bill_grid").jqGrid('navGrid','#dues_bill_grid_pager',$.extend({},footerButton,{search:false,refresh:false}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","dues_bill_grid",["full_name","remarks"]);

function calculateSurcharge(){
var rows = $("#dues_bill_grid").getDataIDs();

	for(a=0;a<rows.length;a++)
	 {
	    row=$("#dues_bill_grid").getRowData(rows[a]); 
	    var billed_amount=$("#billed_amount_"+row.bill_id).val();	    
	    $("#days_"+row.bill_id).val(dayDifference(row.due_date,$("#pay_date").val()));
	    var surcharge= Math.round((billed_amount*12*$("#days_"+row.bill_id).val())/(100*365)); ;
	    $("#surcharge_amount_"+row.bill_id).val(surcharge);	
		var new_total_amount= Math.round(parseFloat(surcharge)+parseFloat(billed_amount));
		$("#new_amount_"+row.bill_id).val(new_total_amount.toFixed(2));
	
	}
	jQuery("#dues_bill_grid").jqGrid('footerData','set', {bill_month: 'Total:', billed_amount:"",surcharge: "",new_payable_amount:""});
	
}
function handleSelectedRow() {
	
/// Bellow comment is temporary solution. Pay Date should be send as parameter with onSelecCustomer_id
	
/*if(parseInt($("#days_"+row.bill_id).val(),10)<=0){
	$("#jqg_dues_bill_grid_"+row.bill_id).prop('checked', false);
	return;
}*/

var totalBill=0;
var totalSurcharge=0;
var totalNewBill=0;
var saveButtonFlag=false;
var rows = jQuery("#dues_bill_grid").getDataIDs();

	for(a=0;a<rows.length;a++)
	 {	
	    row=jQuery("#dues_bill_grid").getRowData(rows[a]);
	    if($("#jqg_dues_bill_grid_"+row.bill_id).prop('checked')==true) 
	    { 
			totalBill=parseFloat(totalBill)+parseFloat($("#billed_amount_"+row.bill_id).val());
			totalSurcharge=parseFloat(totalSurcharge)+parseFloat($("#surcharge_amount_"+row.bill_id).val());
			totalNewBill=parseFloat(totalNewBill)+parseFloat($("#new_amount_"+row.bill_id).val());
		    saveButtonFlag=true;
	    }
	}
	$("#billed_amount_undefined").val(totalBill.toFixed(2));
	$("#surcharge_amount_undefined").val(totalSurcharge.toFixed(2));
	$("#new_amount_undefined").val(totalNewBill.toFixed(2));
	
	if(saveButtonFlag==true)
		enableButton("btn_save");
	else
		disableButton("btn_save");


}

  $('#cb_dues_bill_grid').on("click", function() {
    handleSelectedRow();
});



$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.METERED_CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("",$('#customer_id').val(),loadDuesList);},
}));


Calendar.setup({
     inputField : "pay_date",
     trigger    : "pay_date",
	 eventName : "focus",
	 onSelect   : function() { this.hide();calculateSurcharge();handleSelectedRow(); },
	 dateFormat : "%d-%m-%Y"
});

$("#pay_date").val(getCurrentDate());

function loadDuesList(){
   	var ruleArray=[["customer_id","status"],["eq","eq"],[$("#customer_id").val(),"1"]];
	var postdata=getPostFilter("dues_bill_grid",ruleArray);
   	$("#dues_bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,datatype:'json'});
	reloadGrid("dues_bill_grid");
}

function saveAndDownloadBill(){
	
	var rows = jQuery("#dues_bill_grid").getDataIDs();
    var billInfo="";
    var bill_ids="";
    
	for(var a=0;a<rows.length;a++)
	 {	
	    row=jQuery("#dues_bill_grid").getRowData(rows[a]);
	    if($("#jqg_dues_bill_grid_"+row.bill_id).prop('checked')==true) 
	    { 
	    	billInfo+=$("#customer_id").val()+"#"+$("#pay_date").val()+"#"+$("#surcharge_rate").val()+"#"+$("#bill_id_"+row.bill_id).val()+"#"+$("#surcharge_amount_"+row.bill_id).val()+"@";
	    	bill_ids+="'"+$("#bill_id_"+row.bill_id).val()+"'"+",";	    	
	    
	    }
	}
	
//	var myGrid = $('#dues_bill_grid'),
////	selRowId = myGrid.jqGrid ('getGridParam', 'selrow'),
////    bill_month = myGrid.jqGrid ('getCell', selRowId, 'bill_month_value');
////	bill_year = myGrid.jqGrid ('getCell', selRowId, 'bill_year');
////	
//	
//	
//	selIds = myGrid.jqGrid("getGridParam", "selarrrow"), i, n,
//	bill_month = [];
//	bill_year=[];
//	for (i = 0, n = selIds.length; i < n; i++) {
//		
//		bill_month.push(myGrid.jqGrid("getCell", selIds[i], "bill_month_value"));
//		bill_year.push(myGrid.jqGrid("getCell", selIds[i], "bill_year"));
//		
//		if(bill_month[i].length<2){
//			bill_month[i]="0"+bill_month[i];
//		}
//	}
//	bill_year.join(",");
//	bill_month.join(",");
//	
	
	

	$("#surcharge_bills").val(billInfo);
	var formData = new FormData($('form')[0]);	

	if(bill_ids.length>0){
		bill_ids=bill_ids.substring(0, bill_ids.length-1);
	}
	disableButton("btn_save");
	$.ajax({
		    url: 'saveSurchargeInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
				
		    	enableButton("btn_save");
		    	
		  		if(response.status=="OK"){
		  			
		  			//window.location="downloadMeteredBill.action?download_type=M&bill_id="+bill_ids+"&customer_id="+$("#customer_id").val()+"&customer_category=02"+"&bill_month="+bill_month+"&bill_year="+bill_year;
		  			window.location="downloadMeteredBill.action?download_type=M&bill_id="+bill_ids;
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

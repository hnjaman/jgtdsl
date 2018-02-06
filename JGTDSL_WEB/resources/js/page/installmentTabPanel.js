// Agreement and Installment Bills Tab
$("#tabbed-nav-1").zozoTabs({
            theme: "crystal",
            orientation: "horizontal",
            position: "top-left",
            size: "medium",
            animation: {
                easing: "easeInOutExpo",
                duration: 400,
                effects: "slideH"
            },
            defaultTab: "tab1"
        });

/*~~~~~------~~~~~~*/
//Load Dues Bills
/*~~~~~------~~~~~~*/

function loadDuesList(){
   	var ruleArray=[["customer_id"],["eq"],[$("#customer_id").val()]];
	var postdata=getPostFilter("dues_bill_grid",ruleArray);
   	$("#dues_bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,datatype:'json'});
   	reloadGrid("dues_bill_grid");
   	loadInstallmentHistory(); // Load installment history for the selected customer
}

var duesGrid=$("#dues_bill_grid");

duesGrid.jqGrid({
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.BillingService&method=getDueBillList',
   	jsonReader: {
            repeatitems: false,
            id: "bill_id"
	},
    colNames: ['Bill_id','Month','Year','Bill Amount','Collected Amount', 'Due Amount'],
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
		    	}                    
        ],
	datatype: 'local',
	multiselect: true,
	height: $("#tabbed-nav").height()-130,
	width: $("#tabbed-nav").width(),
   	rowNum:1000,
	rownumWidth: 40,
   	pager: '#dues_bill_grid_pager',
	caption: "Dues bill list for the selected customer",
	footerrow:true, 
    userDataOnFooter:true, 	
	onSelectRow: function(rowId){ calculateTotalForSelectedDuesBills(rowId); },
	onSelectAll:function(aRowids, status) {
		calculateTotalForSelectedDuesBills(null);
	},
	loadComplete: function () {

	
	}
});

duesGrid.jqGrid('navGrid','#dues_bill_grid_pager',$.extend({},footerButton,{search:false,refresh:false}),{},{},{},{multipleSearch:true});
//gridColumnHeaderAlignment("left","dues_bill_grid",["full_name","remarks"]);

function calculateTotalForSelectedDuesBills(rowId) {

	var totalBill=0;
	var totalCollected=0;
	var totalDues=0;
	var rows = duesGrid.getDataIDs();
	var rowId="";
	billSelectBox="<select id='bill' class='segment_input'>";
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

				billSelectBox+="<option value='"+rowData["bill_id"]+"'>"+rowData["bill_month_name"]+" "+rowData["bill_year"]+"</option>";
				billIds+=rowData["bill_id"]+",";
		    }
		}
		billSelectBox+="</select>";
		duesGrid.jqGrid("footerData", "set", {bill_year: "Total:", payable_amount: totalBill,collected_amount: totalCollected,remaining_amount: totalDues});
		
		if(billIds.length>0)
			billIds=billIds.substring(0,billIds.length-1);
		
	}


/*~~~~~------~~~~~~------~~~~~~------~~~*/
//Load Customer's Installment Agreements
/*~~~~~------~~~~~~------~~~~~~------~~~*/

function loadInstallmentHistory(){
 	var ruleArray=[["BILL_INSTALLMENT.customer_id"],["eq"],[$("#customer_id").val()]];
	var postdata=getPostFilter("installment_history_this_grid",ruleArray);
 	$("#installment_history_this_grid").jqGrid('setGridParam',{search: true,postData: postdata,datatype:'json'});
 	reloadGrid("installment_history_this_grid");
}

var installmentHistoryGrid=$("#installment_history_this_grid");

installmentHistoryGrid.jqGrid({
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.InstallmentService&method=getInstallmentHistory',
 	jsonReader: {
          repeatitems: false,
          id: "agreement_id"
	},
  colNames: ['Agreement Id','Customer Id','Customer Name','Agreement Date','Start From', 'Note', 'Status'],
  colModel: [{      name: 'agreementId',
		            index: 'agreementId',
		            hidden:true,
		            key: true
		    	},
                 {
	                name: 'customerId',
	                index: 'customerId',
	                width:30,
	                align:'center',
	                sorttype: 'string',
	                search: false
          	},
          	 {
	                name: 'customerName',
	                index: 'customerName',
	                hidden:true
          	},
		    	{
		            name: 'agreementDate',
		            index: 'agreementDate',
		            width:50,
		            align:'center',
		            sorttype: 'string',
		            search: true,
		    	},
		    	{
		            name: 'startFrom',
		            index: 'startFrom',
		            width:50,
		            align:'center',
		            sorttype: 'string',
		            search: true,
		    	},
		    	{
		            name: 'notes',
		            index: 'notes',
		            width:50,
		            align:'center',
		            sorttype: 'string',
		            search: true,
		    	}  ,
		    	{
		            name: 'status',
		            index: 'status',
		            width:50,
		            align:'center',
		            sorttype: 'string',
		            search: true,
		    	}    
      ],
	datatype: 'local',
	height: $("#tabbed-nav").height()-130,
	width: $("#tabbed-nav").width(),
	rownumWidth: 40,
 	pager: '#installment_history_this_grid_pager',
	caption: "Installment List for the selected customer",
	onSelectRow: function(rowId){getBillInstallments(rowId); }
});

//duesGrid.jqGrid('navGrid','#dues_bill_grid_pager',$.extend({},footerButton,{search:false,refresh:false}),{},{},{},{multipleSearch:true});

//installment_history_all_grid
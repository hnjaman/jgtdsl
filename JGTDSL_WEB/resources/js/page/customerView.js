$( "m.man" ).replaceWith( "<font color='red' style='font-weight:bold;font-size:16px;'>&nbsp;&nbsp;*</font>" );

//$("a[href*=#sw-basic-step-1]").attr("selected","done");
if(step!=0){
$("a[href*=#sw-basic-step-1]").attr("class","done");
$("a[href*=#sw-basic-step-1]").attr("isdone","1");
}
if(step!=1){
$("a[href*=#sw-basic-step-2]").attr("class","done");
$("a[href*=#sw-basic-step-2]").attr("isdone","1");
}
if(step!=2){
$("a[href*=#sw-basic-step-3]").attr("class","done");
$("a[href*=#sw-basic-step-3]").attr("isdone","1");
}
if(step!=3){
$("a[href*=#sw-basic-step-4]").attr("class","done");
$("a[href*=#sw-basic-step-4]").attr("isdone","1");
}
if(step!=4){
$("a[href*=#sw-basic-step-5]").attr("class","done");
$("a[href*=#sw-basic-step-5]").attr("isdone","1");
}

$(".btn.btn-inverse.buttonFinish").remove();

var txt="3";
ajaxLoad("applianceInfo","getApplianceInfoPlain.action?customer_id="+$("#customer_id").val());
var customerLedgerUrl="getCustomerLedger.action?customer_id="+$("#customer_id").val();
$("#customer_ledger_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: customerLedgerUrl,
   	jsonReader:{
    	repeatitems: true,
        id: "issue_paid_date"
	},
    colNames: ['Payment_Date','Description', 'Bank', 'Volum of Gas Sold(m'+txt.sup()+')', 'Sales(Tk.)','Debit Surcharge(Tk.)' ,'Debit(Tk.)', 'Credit Surcharge(Tk.)','Credit(Tk.)','Balance(Tk.)','Bill Due Date'],
    colModel: [{
	                name: 'issue_paid_date',
	                index: 'issue_paid_date',
	                width:40,
	                align:'center',
	                sorttype: 'date'
            	},
            	{
	                name: 'particulars',
	                index: 'particulars',
	                align:'left',
	                width:40
            	},
            	{
	                name: 'bank_name',
	                index: 'bank_name',
	                align:'left',
	                width:70
            	},
            	{
	                name: 'gas_sold',
	                index: 'gas_sold',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'sales_amount',
	                index: 'sales_amount',
	                sorttype: "number",
	                align:'right',
	                width:35
            	},
            	{
	                name: 'surcharge',
	                index: 'surcharge',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'debit_amount',
	                index: 'debit_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	{
	                name: 'credit_surcharge',
	                index: 'credit_surcharge',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	  {
	                name: 'credit_amount',
	                index: 'credit_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	  {
	                name: 'balance_amount',
	                index: 'balance_amount',
	                sorttype: "number",
	                align:'right',
	                width:40
            	},
            	  {
	                name: 'due_date',
	                index: 'due_date',
	                width:40,
	                align:'center',
	                sorttype: 'date'
	                
            	}
                      
            	
        ],   	
    height: $("#wizard").height(),
    width: $("#sw-basic-step-5").width(),
   	pager: '#customer_ledger_grid_pager',
	caption: "Customer Ledger",	
	footerrow: true,
	scroll: true,
	viewrecords: true,
	rowNum:1500,
    rowList:[10,20,30],
    loadonce: true,
	loadComplete: function () {
		calculateCustomerLedgerSum();

		jQuery("#customer_ledger_grid").jqGrid('setSelection',"8333665");
		
	},
    datatype: 'json'
})).navGrid('#customer_ledger_grid_pager',$.extend(footerButton,{search:false,refresh:true,view:false}),{},{},{},{},{});;
jQuery("#customer_ledger_grid_pager").setGridParam({rowNum:10}).trigger("reloadGrid");

function getGridRowHeight (targetGrid) {
    var height = null; // Default
    try{
        height = jQuery(targetGrid).find('tbody').find('tr:first').outerHeight();
    }
    catch(e){
     //catch and just suppress error
    }
    return height;
}

function scrollToRow (targetGrid, id) {
    var rowHeight = getGridRowHeight(targetGrid) || 23; // Default height    
    var index=$(targetGrid).getGridParam("reccount");        
    $(targetGrid).closest(".ui-jqgrid-bdiv").scrollTop(rowHeight * index);
}


function calculateCustomerLedgerSum(){
	var total_debit_amount = jQuery("#customer_ledger_grid").jqGrid('getCol', 'debit_amount', false, 'sum');
	var total_credit_amount = jQuery("#customer_ledger_grid").jqGrid('getCol', 'credit_amount', false, 'sum');
	var total_balance = total_debit_amount-total_credit_amount;
	//var abc=$("#customer_ledger_grid").find(">tbody>tr.jqgrow").filter(":last");
	var rows = jQuery("#customer_ledger_grid").getDataIDs();
	var legnth=rows.length-1;
	row=jQuery("#customer_ledger_grid").getRowData(rows[legnth]);
	jQuery("#customer_ledger_grid").jqGrid('footerData','set', {particulars: 'Total: '+$("#customer_ledger_grid").getGridParam("reccount"),debit_amount:total_debit_amount,credit_amount:total_credit_amount,balance_amount:row.balance_amount});
}

var color='';
$('#customer_ledger_grid').jqGrid('navGrid','#customer_ledger_grid_pager');


$('#customer_ledger_grid').jqGrid('navGrid','#customer_ledger_grid_pager')
.navButtonAdd('#customer_ledger_grid_pager',{
    caption:"<b><font color="+$("#isDefauleter").val()+">Dues</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
    buttonicon:"ui-icon-suitcase", 
    id: "icon_download_approve_bills",
    onClickButton: function(){
          showDuesBill();							
    }
}).navButtonAdd('#customer_ledger_grid_pager',{
    caption:"", 
    buttonicon:"ui-icon-carat-1-e", 
    id: "zoom_in",
    onClickButton: function(){
    	increaseSize("customer_ledger_grid");
    }
}).navButtonAdd('#customer_ledger_grid_pager',{
    caption:"", 
    buttonicon:"ui-icon-carat-1-w", 
    id: "zoom_out",
    onClickButton: function(){
    	decreaseSize("customer_ledger_grid");
    }
}).navButtonAdd('#customer_ledger_grid_pager',{
    caption:"<b><font color='purple'>Print</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
    buttonicon:"ui-icon-print", 
    id: "print_ledger",
    onClickButton: function(){
    	window.location='printLedger.action?customer_id='+$("#customer_id").val();
    }
});


//processCustomerLedgerBalance
function showDuesBill(){

	$.nsWindow.open({
		movable:true,
		title: "<b><font size='4'>Due List</font>",
		width: 800,
		height: 500,
		dataUrl: 'getDueBillList.action?customer_id='+$("#customer_id").val(),
		theme:jsVar.MODAL_THEME
    });
    
}
function increaseSize(id) {

	$("#second_ledger").fadeOut();
	//$("#customer_ledger_div").width('98%');
    if (grid = $('.ui-jqgrid-btable:visible')) {
        grid.each(function(index) {
            //gridId = $(this).attr('id');
            gridParentWidth = $("#ledger_div").width()-30;
            $('#' + id).setGridWidth(gridParentWidth);
        });
    };
}

function decreaseSize(id) {

	$("#second_ledger").fadeIn();
	//$("#customer_ledger_div").width('50%');
    if (grid = $('.ui-jqgrid-btable:visible')) {
        grid.each(function(index) {
            //gridId = $(this).attr('id');
           
           
            gridParentWidth = $("#sw-basic-step-5").width()/2;
            $('#' + id).setGridWidth(gridParentWidth);
        });
    };
}


var customerLedgerUrl="getDepositLedger.action?customer_id="+$("#customer_id").val();
$("#deposit_ledger_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: customerLedgerUrl,
   	jsonReader:{
    	repeatitems: false,
        id: "trans_id"
	},
    colNames: ['Date','Amount(Tk.)', 'Deposit Type','Bank Expire','Deposit or Withdraw'],
    colModel: [{
	                name: 'deposit_date',
	                index: 'deposit_date',
	                width:50,
	                align:'center',
	                sorttype: 'date'
            	},
            	{
	                name: 'debit_amount',
	                index: 'debit_amount',
	                align:'right',
	                width:40
            	},
            	{
	                name: 'deposit_type',
	                index: 'deposit_type',
	                sorttype: "string",
	                align:'center',
	                width:40
            	},
               	{
	                name: 'expire_date',
	                index: 'expire_date',
	                sorttype: "string",
	                align:'center',
	                width:40
            	},
            	  {
	                name: 'description',
	                index: 'description',
	                sorttype: "string",
	                align:'center',
	                width:40
            	}
        ],   	
	height: $("#wizard").height(),
	width: $("#sw-basic-step-5").width(),
   	pager: '#deposit_ledger_grid_pager',
	caption: "Deposit Ledger",	
	footerrow: true,
	sortname: 'trans_id',
	sortorder: 'asc',
	scroll: true,
	viewrecords: true,
	rowNum:500,
    rowList:[10,20,30],
    loadonce: true,
	loadComplete: function () {
		calculateDepositLedgerSum();
	},
    datatype: 'json'
}));
jQuery("#deposit_ledger_grid_pager").setGridParam({rowNum:10}).trigger("reloadGrid");
function calculateDepositLedgerSum(){
	var total_security_amount = jQuery("#deposit_ledger_grid").jqGrid('getCol', 'security_amount', false, 'sum');
	var total_debit_amount = jQuery("#deposit_ledger_grid").jqGrid('getCol', 'debit_amount', false, 'sum');
	var total_credit_amount = jQuery("#deposit_ledger_grid").jqGrid('getCol', 'credit_amount', false, 'sum');
	var total_balance_amount=jQuery("#deposit_ledger_grid").jqGrid('getCol', 'balance_amount', false, 'sum');
	
	//console.log($("#deposit_ledger_grid").find(">tbody>tr.jqgrow").filter(":last"));
	//var abc=$("#deposit_ledger_grid").find(">tbody>tr.jqgrow").filter(":last");
	
	var rows = jQuery("#deposit_ledger_grid").getDataIDs();
	var legnth=rows.length-1;
	row=jQuery("#deposit_ledger_grid").getRowData(rows[legnth]);
	
	
	
	
	jQuery("#deposit_ledger_grid").jqGrid('footerData','set', {description: 'Total: '+$("#deposit_ledger_grid").getGridParam("reccount"),security_amount:total_security_amount,debit_amount:total_debit_amount,credit_amount:total_credit_amount,balance_amount:row.balance_amount});
}

setTimeout(function(){ scrollToRow("#customer_ledger_grid"); }, 1500);

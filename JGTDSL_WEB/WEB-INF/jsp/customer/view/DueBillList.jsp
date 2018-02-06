<%@ taglib prefix="s" uri="/struts-tags"%>	

<script type="text/javascript">
	var pending_bill_id=new Array();
	var pending_bill_month=new Array();
	var pending_bill_year=new Array();
	var pending_bill_payable_amount=new Array();
	var pending_bill_paid_amount=new Array();
	var pending_bill_due_amount=new Array();

</script>

<s:iterator value="duesList" status="indx">
	<script type="text/javascript">
		pending_bill_id[<s:property value="%{#indx.index}" />]="<s:property value='bill_id' />";
		pending_bill_month[<s:property value="%{#indx.index}" />]="<s:property value='str_month' />";
		pending_bill_year[<s:property value="%{#indx.index}" />]="<s:property value='year' />";
		pending_bill_payable_amount[<s:property value="%{#indx.index}" />]="<s:property value='actual_payable_amount' />";
		pending_bill_paid_amount[<s:property value="%{#indx.index}" />]="<s:property value='paid_amount' />";
		pending_bill_due_amount[<s:property value="%{#indx.index}" />]="<s:property value='due_amount' />";
	</script>
</s:iterator>
									
<table id="pending_grid"></table>
<div id="pending_grid_pager"></div>
<button class="btn btn-beoro-3"  type="button" id="close_btn" onclick="closeModal()">
		<span class="splashy-application_windows_edit"></span>
		Close</button>

<script type="text/javascript">
$("#pending_grid").GridUnload();


$("#pending_grid").jqGrid({
    data: [],
    datatype: "local",
    colNames: ['Bill Id','Bill Month',"Payable Amount","Paid Amount","Due Amount"],
    colModel: [
    	{name:'bill_id',index:'bill_id', hidden:true},
        {name:'bill_month',index:'bill_month', width:100},
    	{name: 'payable_amount',index: 'payable_amount',align:'right'},
    	{name: 'paid_amount',index: 'paid_amount',align:'right'},
    	{name: 'due_amount',index: 'due_amount',align:'right'}
    	
    	],
    pager: '#pending_grid_pager',
    width: 778,
    height:384,  
    rownumbers: true,
    multiselect: false,
    loadComplete: function () {
    	calculatePendingFooterSum();
	},
	footerrow: true,
	onSelectRow : function (rowid, status) {
      var elem = document.activeElement;
      if (elem.id) { 
      		calculateTotalBill();
    	}
    },
    onSelectAll: function(aRowids, status) {
    	calculateTotalBill();
	}
});
function calculatePendingFooterSum(){
	var payable_amount = jQuery("#pending_grid").jqGrid('getCol', 'payable_amount', false, 'sum');
	var paid_amount = jQuery("#pending_grid").jqGrid('getCol', 'paid_amount', false, 'sum');
	var due_amount = jQuery("#pending_grid").jqGrid('getCol', 'due_amount', false, 'sum');
	
	
	jQuery("#pending_grid").jqGrid('footerData','set', {bill_month: 'Total:  '+$("#pending_grid").getGridParam("reccount"),payable_amount:payable_amount,paid_amount:paid_amount,due_amount:due_amount});
}

$("#pending_grid_pager").hide();


function addCollectionRow(){
 
 var pending_bill_data="";
 var collected_amount="";
 var collected_surcharge=""; 
 for(var i=0;i<pending_bill_month.length;i++)
	{
	pending_bill_data = [{"bill_id":pending_bill_id[i],"bill_month":pending_bill_month[i]+", "+pending_bill_year[i],"payable_amount":pending_bill_payable_amount[i],"paid_amount":pending_bill_paid_amount[i],"due_amount":pending_bill_due_amount[i]}];
		jQuery("#pending_grid").jqGrid('addRowData',pending_bill_id[i],pending_bill_data[0],"last"); 
	}  
	calculatePendingFooterSum();
}
addCollectionRow();


</script>
<%@ taglib prefix="s" uri="/struts-tags"%>	

<script type="text/javascript">
	var pending_bill_id=new Array();
	var pending_bill_month=new Array();
	var pending_bill_year=new Array();
	var pending_bill_actual_bill_amount=new Array();
	var pending_bill_collected_bill_amount=new Array();
	var pending_bill_actual_surcharge_amount=new Array();
	var pending_bill_surcharge_per_collection=new Array();
	var pending_bill_collected_surcharge_amount=new Array();
	var pending_bill_actual_payable_amount=new Array();
	var pending_bill_collected_payable_amount=new Array();

</script>

<s:iterator value="duesList" status="indx">
	<script type="text/javascript">
		pending_bill_id[<s:property value="%{#indx.index}" />]="<s:property value='bill_id' />";
		pending_bill_month[<s:property value="%{#indx.index}" />]="<s:property value='str_month' />";
		pending_bill_year[<s:property value="%{#indx.index}" />]="<s:property value='year' />";
		pending_bill_actual_bill_amount[<s:property value="%{#indx.index}" />]="<s:property value='actual_billed_amount' />";
		pending_bill_collected_bill_amount[<s:property value="%{#indx.index}" />]="<s:property value='collected_billed_amount' />";
		pending_bill_actual_surcharge_amount[<s:property value="%{#indx.index}" />]="<s:property value='actual_surcharge_amount' />";
		pending_bill_surcharge_per_collection[<s:property value="%{#indx.index}" />]="<s:property value='surcharge_per_collection' />";
		pending_bill_collected_surcharge_amount[<s:property value="%{#indx.index}" />]="<s:property value='collected_surcharge_amount' />";
		pending_bill_actual_payable_amount[<s:property value="%{#indx.index}" />]="<s:property value='actual_payable_amount' />";
		pending_bill_collected_payable_amount[<s:property value="%{#indx.index}" />]="<s:property value='collected_payable_amount' />";
	</script>
</s:iterator>
									
<table id="pending_grid"></table>
<div id="pending_grid_pager"></div>

<script type="text/javascript">
$("#pending_grid").GridUnload();


$("#pending_grid").jqGrid({
    data: [],
    datatype: "local",
    colNames: ['Bill Id','Surcharge_per_coll','Bill Month',"Act.Bill","Coll.Bill","Act.Surcharge","Coll.Surcharge","Act Payable","Coll. Payable","Surcharge","Collection Amount",'bill_mon'],
    colModel: [
    	{name:'bill_id',index:'bill_id', hidden:true},
    	{name:'surcharge_per_collection',index:'surcharge_per_collection', hidden:true},
        {name:'bill_month',index:'bill_month', width:100},   	    	
    	{name: 'actual_billed_amount',index: 'actual_billed_amount',align:'right'},
    	{name: 'collected_billed_amount',index: 'collected_billed_amount',align:'right'},
    	{name: 'actual_surcharge_amount',index: 'actual_surcharge_amount',align:'right'},
    	{name: 'collected_surcharge_amount',index: 'collected_surcharge_amount',align:'right'},
    	{name: 'actual_payable_amount',index: 'actual_payable_amount',align:'right'},
    	{name: 'collected_payable_amount',index: 'collected_payable_amount',align:'right'},
    	{name: 'collected_surcharge',index: 'collected_surcharge',align:'right'},
    	{name: 'collection_amount',index: 'collection_amount',align:'right'},
    	{name:'bill_mon',index:'bill_mon', hidden:true}
    	
    	],
    pager: '#pending_grid_pager',
    width: 1130,
    height:384,  
    rownumbers: true,
    multiselect: true,
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
	var sum_actual_billed_amount = jQuery("#pending_grid").jqGrid('getCol', 'actual_billed_amount', false, 'sum');
	var sum_collected_billed_amount = jQuery("#pending_grid").jqGrid('getCol', 'collected_billed_amount', false, 'sum');
	var sum_actual_surcharge_amount = jQuery("#pending_grid").jqGrid('getCol', 'actual_surcharge_amount', false, 'sum');
	var sum_collected_surcharge_amount = jQuery("#pending_grid").jqGrid('getCol', 'collected_surcharge_amount', false, 'sum');
	var sum_actual_payable_amount = jQuery("#pending_grid").jqGrid('getCol', 'actual_payable_amount', false, 'sum');
	var sum_collected_payable_amount = jQuery("#pending_grid").jqGrid('getCol', 'collected_payable_amount', false, 'sum');
	
	jQuery("#pending_grid").jqGrid('footerData','set', {bill_month: 'Total:  '+$("#pending_grid").getGridParam("reccount"),actual_billed_amount:sum_actual_billed_amount,collected_billed_amount:sum_collected_billed_amount,actual_surcharge_amount:sum_actual_surcharge_amount,collected_surcharge_amount:sum_collected_surcharge_amount,actual_payable_amount:sum_actual_payable_amount,collected_payable_amount:sum_collected_payable_amount,collection_amount:"<input type='text'  disabled='disabled' style='text-align:right;width:80px;' id='total_pending_collection_amount'/>",collected_surcharge:"<input type='text'  disabled='disabled' style='text-align:right;width:80px;' id='total_pending_surcharge_amount'/>"});
}

$("#pending_grid_pager").hide();

addCollectionRow();

function addCollectionRow(){
 
 var pending_bill_data="";
 var collected_amount="";
 var collected_surcharge=""; 
 for(var i=0;i<pending_bill_month.length;i++)
	{
	    /* alert(pending_bill_actual_bill_amount[i]);
	    alert(pending_bill_actual_bill_amount[i]); */
		var collection_bill_amount=pending_bill_actual_bill_amount[i]-pending_bill_collected_bill_amount[i];
		var collection_surcharge_amount=pending_bill_actual_surcharge_amount[i]-pending_bill_collected_surcharge_amount[i];
		actual_surcharge_amount="<input type='text' onkeyup='calculateTotalBill()' id='act_surcharge_"+pending_bill_id[i]+"' value='"+pending_bill_actual_surcharge_amount[i]+"' disabled='disabled' style='width:100px;text-align:right;' >";
		surcharge_per_collection="<input type='text' onkeyup='calculateTotalBill()' id='surcharge_per_coll_"+pending_bill_id[i]+"' value='"+pending_bill_surcharge_per_collection[i]+"' disabled='disabled' style='width:100px;text-align:right;' >";
		actual_payable_amount="<input type='text' onkeyup='calculateTotalBill()' id='act_payable_"+pending_bill_id[i]+"' value='"+pending_bill_actual_payable_amount[i]+"' disabled='disabled' style='width:100px;text-align:right;' >";
		collected_amount="<input type='text' onkeyup='calculateTotalBill()' id='pending_"+pending_bill_id[i]+"' value='"+collection_bill_amount+"' style='width:102px;text-align:right;' >";
		collected_surcharge="<input type='text' onkeyup='calculateTotalBill()' id='surcharge_"+pending_bill_id[i]+"' value='"+collection_surcharge_amount+"' style='width:102px;text-align:right;' />";
		        
		bill_mon="<input type='hidden' id='bill_month_"+pending_bill_id[i]+"' value='"+pending_bill_month[i]+"#"+pending_bill_year[i]+"'  />";        
		
				        
		pending_bill_data = [{"bill_id":pending_bill_id[i],"surcharge_per_collection":surcharge_per_collection,"bill_month":pending_bill_month[i]+", "+pending_bill_year[i],"actual_billed_amount":pending_bill_actual_bill_amount[i],"collected_billed_amount":pending_bill_collected_bill_amount[i],"actual_surcharge_amount":actual_surcharge_amount,"collected_surcharge_amount":pending_bill_collected_surcharge_amount[i],"actual_payable_amount":actual_payable_amount,"collected_payable_amount":pending_bill_collected_payable_amount[i],"collection_amount":collected_amount,"collected_surcharge":collected_surcharge,"bill_mon":bill_mon}];
		jQuery("#pending_grid").jqGrid('addRowData',pending_bill_id[i],pending_bill_data[0],"last"); 
	}  
	calculatePendingFooterSum();
}



</script>
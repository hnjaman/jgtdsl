<%@ taglib prefix="s" uri="/struts-tags"%>

	
<script type="text/javascript">
var multiColl_branch_sbox = { sourceElm:"multiColl_bank_id",targetElm :"multiColl_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var multiColl_account_sbox = { sourceElm:"multiColl_branch_id",targetElm :"multiColl_account_no",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
</script>
<style type="text/css">
.ui-datepicker-calendar{
display:none;
}
</style>
<form name="multiCollForm" id="multiCollForm">	
<input type="hidden" id="multiColl_customer_id" name="multiColl.customer_id"  />
<div class="content-bg">
<jsp:include page="MultiMonthHeader.jsp" />

<div id="customer_grid_div" style="margin-top: 10px;height: 510px;"> 
			<div id="tabbed-nav">
	            <ul>
	                <li><a>Pending Bills</a></li>
	                <li><a>Advanced Payment</a></li>
	            </ul>
            <div>
                <div>
					<!-- Pending billing list goes here  -->
					<jsp:include page="MultiMonthPendingBills.jsp" />
                </div>
                <div>
                    <jsp:include page="MultiMonthAdvancedBills.jsp" />
                </div>                
            </div>
        </div>       
<jsp:include page="MultiMonthTotalAmount.jsp" />
</div>

<jsp:include page="MultiMonthFooter.jsp" />
</div>
<input type="hidden" id="pending_bills_str" name="multiColl.pending_bills_str" value="" />
<input type="hidden" id="advanced_bills_str" name="multiColl.advanced_bills_str" value="" />
</form>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript">
$("#multiColl_bank_id").val($("#bank_id").val());
fetchSelectBox(multiColl_branch_sbox);
$("#multiColl_branch_id").val($("#branch_id").val());
fetchSelectBox(multiColl_account_sbox);
$("#multiColl_account_no").val($("#account_id").val());
$("#multiColl_collection_date").val($("#collection_date").val());

//$("#multiColl_collection_date").val(getCurrentDate());
/*
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "multiColl_collection_date",
    trigger    : "multiColl_collection_date",
    onSelect   : function() { this.hide(); }
	}));
	*/

	$('#from_date').datepicker(monthYearCalOptions);
	$('#to_date').datepicker(monthYearCalOptions);
	
	
function calculateAdvancePaymentFooterSum(){
	//var bill_amount = jQuery("#pending_grid").jqGrid('getCol', 'bill_amount', false, 'sum');
	var total_advance=0;
		$(".advance").each(function() {
                    total_advance += Number($(this).val());
                });	
	jQuery("#advance_grid").jqGrid('footerData','set', {bill_month: 'Total:  '+$("#advance_grid").getGridParam("reccount"),bill_amount:"<input type='text' id='total_advance' value='"+total_advance+"'  disabled='disabled' style='text-align:right;width:80px;' />"});
}	

function calculateTotalBill(){
	calcualteAdvanceBillTotal();
	calculatePendingBillTotal();
	
	var total_amount=0;
	total_amount = Number($("#total_pending_collection_amount").val())+Number($("#total_pending_surcharge_amount").val())+Number($("#total_advance").val());
	$("#total_amount").html(total_amount)
}

function calcualteAdvanceBillTotal(){
	calculateAdvancePaymentFooterSum();
}

function calculatePendingBillTotal() {
    var grid = $("#pending_grid");
    var rowKey = grid.getGridParam("selrow");
    var selectedIDs = grid.getGridParam("selarrrow");
    //alert(selectedIDs[i]);
    var total_pending_amount=0;
    var total_surcharge_amount=0
    for (var i = 0; i < selectedIDs.length; i++) {
        total_pending_amount += parseFloat($("#pending_"+selectedIDs[i]).val(),10);
        total_surcharge_amount += parseFloat($("#surcharge_"+selectedIDs[i]).val(),10);
	}           
	$("#total_pending_collection_amount").val(total_pending_amount);   
	$("#total_pending_surcharge_amount").val(total_surcharge_amount);     
}

	
function valiateAndSaveMultiCollection(){	
	var isValidate=validateField("multiColl_bank_id","multiColl_branch_id","multiColl_account_no","multiColl_collection_date");
	
	if(isValidate==false)
		return;
		
	setPendingBillsStr();
	setAdvanceBillsStr();
	
	$("#multiColl_customer_id").val(window.parent.document.getElementById('customer_id').value);
	
	
	var form = document.getElementById('multiCollForm');
	var formData = new FormData(form);
	disableButton("btn_save");
  	$.ajax({
    url: "saveMultiMonthCollection.action",
    type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response){
    
     enableButton("btn_save");
     closeModal();
     getTotalCollectionByDateAccount();
	 $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, jqDialogOptions);  
	 collectionForm(clearField);		 
     focusNext("customer_code");
    }
    });   
	
	return;	
}

function setPendingBillsStr(){
		
	$("#pending_bills_str").val("");
	var grid = $("#pending_grid");
    var rowKey = grid.getGridParam("selrow");

    var selectedIDs = grid.getGridParam("selarrrow");
    var pendingBills="";
    for (var i = 0; i < selectedIDs.length; i++) {
    
    	//alert($("#bill_month_"+selectedIDs[i]).val());
    	
	    pendingBills+=selectedIDs[i]+"#"+parseFloat($("#pending_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#surcharge_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#act_surcharge_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#act_payable_"+selectedIDs[i]).val(),10)+"#"+parseFloat($("#surcharge_per_coll_"+selectedIDs[i]).val(),10)+"#"+$("#bill_month_"+selectedIDs[i]).val();
	    pendingBills=pendingBills+"@";
	}        
	//console.log(pendingBills);
	$("#pending_bills_str").val(pendingBills);
	
}	

function setAdvanceBillsStr(){
		
	$("#advanced_bills_str").val("");
	var advancedBills="";
	$(".advance").each(function() {
	     var advanceId=$(this).attr("id").replace("advance_", "");
	     var advanceIdArr=advanceId.split("_");
	     var month=Number(monthNames.indexOf(advanceIdArr[0]))+1;
	     var year=advanceIdArr[1];
	     
		 advancedBills+=month+"#"+year+"#"+Number($(this).val());
	     advancedBills=advancedBills+"@";
    })
    //console.log(advancedBills);            
	$("#advanced_bills_str").val(advancedBills);

}	

</script>
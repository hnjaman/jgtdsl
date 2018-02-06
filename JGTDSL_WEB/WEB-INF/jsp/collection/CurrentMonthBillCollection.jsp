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

<div id="customer_grid_div" style="margin-top: 10px;height: 300px;"> 
			<div id="tabbed-nav">	           
            <div>
                <div>
                    <jsp:include page="CurrentMonthBillCollectionInfo.jsp" />
                </div>                
            </div>
        </div>       
</div>
</div>
<input type="hidden" id="current_bill_month" name="multiColl.current_bill_month" value="" />
<input type="hidden" id="current_bill_year" name="multiColl.current_bill_year" value="" />
</form>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript">

$("#multiColl_bank_id").val($("#bank_id").val());
fetchSelectBox(multiColl_branch_sbox);
$("#multiColl_branch_id").val($("#branch_id").val());
fetchSelectBox(multiColl_account_sbox);
$("#multiColl_account_no").val($("#account_id").val());
$("#multiColl_collection_date").val($("#collection_date").val());
	
function saveCurrentMonthBillWithCollection(){	

	var isValidate=validateField("multiColl_bank_id","multiColl_branch_id","multiColl_account_no","multiColl_collection_date");
	
	if(isValidate==false)
		return;
	
	
	$("#multiColl_customer_id").val(window.parent.document.getElementById('customer_id').value);
	
	
	var form = document.getElementById('multiCollForm');
	var formData = new FormData(form);
	disableButton("btn_save");
  	$.ajax({
    url: "saveCurrentMonthBillWithCollection.action",
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

$("#rate").keyup(function(e){
    if(e.keyCode == 13)
    {
    	
		saveCurrentMonthBillWithCollection();
    }
});  

</script>
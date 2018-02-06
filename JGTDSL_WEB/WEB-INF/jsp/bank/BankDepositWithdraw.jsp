<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	//Overlay Example : http://jsfiddle.net/andresilich/g5jer/
	
	navCache("bankDepositWithdrawHome.action");
	setTitle("Bank Deposit/Withdraw");
	
	var source_branch_sbox = { sourceElm:"source_bank_id", targetElm :"source_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
	var target_branch_sbox = { sourceElm:"target_bank_id", targetElm :"target_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action?type=1',data_key:'bank_id'};
	var search_branch_sbox = { sourceElm:"search_bank_id", targetElm :"search_branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
	
	var source_account_sbox = { sourceElm:"source_branch_id", targetElm :"source_account_no",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
	var target_account_sbox = { sourceElm:"target_branch_id", targetElm :"target_account_no",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
	var search_account_sbox = { sourceElm:"search_branch_id", targetElm :"search_account_no",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
</script>
<style type="text/css">
.overlay {
    position:absolute;
    top:0;
    left:0;
    right:0;
    bottom:0;
    background-color:rgba(0, 0, 0, 0.65);
    background: url(data:;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAABl0RVh0U29mdHdhcmUAUGFpbnQuTkVUIHYzLjUuNUmK/OAAAAATSURBVBhXY2RgYNgHxGAAYuwDAA78AjwwRoQYAAAAAElFTkSuQmCC) repeat scroll transparent\9;
    z-index:9999;
    color:white;
}

.overlay {
    text-align: center;
}
 
.overlay:before {
    content: '';
    display: inline-block;
    height: 100%;
    vertical-align: middle;
    margin-right: -0.25em;
}
.txt {
    display: inline-block;
    vertical-align: middle;
    padding: 10px 15px;
    position:relative;
    font-weight:bold;
}
</style>
<div style="height: 98%;width: 50%;float: left;">	

	<div style="width: 100%; height: 70%;">
		<div class="row-fluid">
			<div class="span12">
				<jsp:include page="SourceTargetBankAccount.jsp" />	
			</div>
		</div>	
	</div>
	
	<div id="search_div" style="width: 100%;height:30%;">
		<div class="row-fluid" style="height: 50%;">
			<jsp:include page="CustomSearchBox.jsp" />
		</div>
	</div>
	
</div>


<div style="height: 92%;width: 49%;float: left;margin-left: 10px;" id="transaction_grid_div"> 
		<table id="transaction_grid"></table>
		<div id="transaction_grid_pager"></div>
</div>


<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/bankDepositWithdraw.js"></script>

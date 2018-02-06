<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
.row-fluid + .row-fluid {
    margin-top: 5px !important;
}
.dAmount
{
 text-align: right;
}
</style>
<div id="sw-basic-step-3">
<h2 class="StepTitle">Security/Other Deposit</h2>
<div class="row-fluid">
  <div class="span9">
	<form id="depositForm" name="depositForm">
	<input type="hidden" name="customer_id" id="customer_id" value="<s:property value='customer.customer_id' />" />
  	<div class="w-box">
	  <div class="w-box-header"><h4>Deposit Entry Form</h4></div>
	  <div class="w-box-content cnt_a" id="depositDetailDiv">
		<%@ include file="NewDepositForm.jsp" %>					
	   </div>
	</div>
	</form>
	</div>
    <div class="span3">
		<div class="w-box">
			<div class="w-box-header">
				<h4>Deposit History</h4>
			</div>
			<div class="w-box-content cnt_a" style="padding: 3px;">
				<div class="row-fluid">
	            	<div class="span12" id="depositListTbl">
						
					</div>
				</div>
			</div>
		</div>
   </div>
</div>
</div>

<script type="text/javascript">
var customer_id=$("#customer_id").val();
var actionUrl=sBase+"getSecurityAndOtherDepositList.action?customer_id="+customer_id;
$("#depositListTbl").html(jsImg.SETTING).load(actionUrl);      
</script>
                                          
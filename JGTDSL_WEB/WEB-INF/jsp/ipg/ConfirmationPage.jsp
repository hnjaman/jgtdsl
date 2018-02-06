<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Confirm your transaction</title>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!--<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery-latest.js"></script>-->

</head>
<body>

<form id='frm_confirm_transaction' action="./payIpgBill.action" method="POST">
<div class='container'>
	<h1 align='center'>Confirm Your Transaction</h1>
	<div class='row'><div class='col-sm-12'>
		<h4>Selected Bills:</h4>
		<div class='table-responsive'>
		<table class='table table-bordered table-striped'>
			<thead></thead>
				<tr>
					<th align=center>Month</th>
					<th align=center>Amount</th>
					<th align=center>Surcharge</th>
				</tr>
			
			
			<tbody>
				<s:iterator value="selectedBillList" id="bill">
					<tr>
						<td align=left><s:property value="dueMonth"/></td>
						<td align=right><s:property value="dueAmount"/></td>
						<td align=right><s:property value="dueSurcharge"/></td>
					</tr>
				</s:iterator>
			
				<br>
				<br>
					<tr>
						<td align=center colspan=1><strong>Total Amount</strong></td>
						<td align=right colspan=2><strong><s:property value="totalAmount"/></strong></td>
					</tr>
			
		</table>
		</div>
	</div></div>
	
<input type="hidden" name="selectedBills" value="<s:property value='selectedBills' />" />
	<div class='row'>
		<label class='col-sm-4 col-xs-6'><h4>Payment Method:</h4></label>
		<div class='col-sm-4 col-xs-6'>
			<img src="./resources/images/ipg/<s:property value='selectedPaymentMethod.imagUrl' />" class='img-responsive' /><br>
			<s:property value="paymentMethod.name"/>
		</div>
	</div>

<input type="hidden" name="paymentMethodId" value="<s:property value='selectedPaymentMethod.id' />" /><br/>


<input type="hidden" name="totalAmount" value="<s:property value='totalAmount'/>" />
<input type="hidden" name="customerId" value='<s:property value="customerId"/>' />


<br/><br/>

	<div class='btn-group'>
	<input class='btn btn-success' type="button" value="Pay now" onclick='confirm_transaction(this)' />
	<input class='btn btn-danger' type="button" value="Cancel"/>
	</div>
</div>

</form>

</body></html>

<script>
	function confirm_transaction(o)
	{
		o.disabled=true;
		document.getElementById('frm_confirm_transaction').submit();
	}

</script>
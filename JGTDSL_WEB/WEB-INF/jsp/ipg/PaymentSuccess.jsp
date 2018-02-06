Payment Success.....

<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!--<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery-latest.js"></script>-->

</head>
<body>

<form id='frm_confirm_transaction' >
<s:property value="transID"/>
<input type="text" name="transId" value="<s:property value='transId'/>" />
<input type="text" name="totalAmount" value="<s:property value='totalAmount'/>" />
<input type="text" name="customerId" value='<s:property value="customerId"/>' />


</form>

</body></html>


<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="/JGTDSL_WEB/resources/css/template/report.css" rel="stylesheet" type="text/css" />
<script  type="text/javascript">
	navCache("smsHome");
	setTitle("SMS Panel");
</script>
<div class="row-fluid">							
	<div class="span3">	
		<div class="report">
		  <div class="box-collection">
		    <ul class="column">
							
					
					<li class="boxV" onclick="ajaxLoad('sms_input_div','smsDefalter.action')"><span>SMS For Defaulter</span> </li>
					
				
    		</ul>
  		</div>
	</div>
	</div>
	<div class="span9" style="padding-top: 30px;">
			<div id="sms_input_div"></div>
	</div>
</div>



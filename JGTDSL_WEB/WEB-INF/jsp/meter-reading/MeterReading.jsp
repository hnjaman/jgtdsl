<script  type="text/javascript">
	navCache("meterReadingHome.action");
	setTitle("Meter Reading");
	var entry_type="";
</script>
<style type="text/css">
	.mr_lable{width: 25.5%;}
	.mr_text{width: 68.5%;}
	.mr_select{width: 72%;}
	.mr_month{width: 40%;}
	.mr_year{width: 30.5%;}
	
	.mr_text1{width: 35.5%;}
	.mr_text2{width: 28.5%;}
	
	.mr_textarea{width:68.5%}
	
	.mr_address_label{width:12.5%}
	.mr_address_textarea{width:84.5%}
	
	.mr_customer_name_label{width:19%}
	.mr_customer_name_text{width:76%}
	.mr_customer_type_label{width:40%}
	.mr_customer_type_text{width:50%}
	
	.mr_1row_label{width:40%}
	.mr_1row_select{width:56%}
 	.mr_1row_text{width:12%}
	
</style>

<link href="/JGTDSL_WEB/resources/css/page/meterReading.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"  src="/JGTDSL_WEB/resources/js/page/jqGridDialog.js"></script>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="meter-reading">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Meter Reading</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
     				<form id="meterReadingForm" name="meterReadingForm" style="margin-bottom: 1px;">
     					<%@ include file="MeterReadingEntryForm.jsp" %>
     				</form>																		
				</div>
				<table width="100%" border="0">
					<tr>
						<td width="30%" align="left"><div id="count_div" class="count_div"></div></td>
						<td width="70%" align="right"><div id="msg_div" class="msg_div"></div></td>
					</tr>
				</table>
				
			</div>
		</div>
	</div>
</div>

<!--  
<div class="reading-history" id="reading_history_grid_div">
	<table id="reading_history_grid"></table>
	<div id="reading_history_grid_pager" ></div>
</div>

-->


<p style="clear: both;margin-top: 5px;"></p>

<div class="reading-list" id="reading_grid_div"> 
<table id="reading_grid"></table>
<div id="reading_grid_pager" ></div>
</div>


<%@ include file="ParameterModal.jsp" %>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterReading.js"></script>	

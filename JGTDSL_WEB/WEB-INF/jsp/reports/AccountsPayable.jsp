<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("accountsPayableHome.action");
	setTitle("Monthly Various Margin Report Generation");
</script>
<link href="/JGTDSL_WEB/resources/css/page/meterReading.css" rel="stylesheet" type="text/css" />
<style>
input[type="radio"], input[type="checkbox"]
{
margin-top: -3px !important;
}
.alert{
padding-top: 4px !important;
padding-bottom: 4px !important;
}
.ui-icon, .ui-widget-content .ui-icon {
    cursor: pointer;
}
.sFont{
font-size: 12px;
}
</style>
<div class="meter-reading" style="width: 50%;height: 50%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Various Margin Report</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     				<form id="billProcessForm" name="billProcessForm" action="accountsPayableInfo.action" style="margin-bottom: 1px;">
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Month<m class='man'/></label>
								<select name="bill_month" id="billing_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6">
								<label style="width: 40%">Year<m class='man'/></label>
								<select name="bill_year" id="billing_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">						   			     
						   			 <button class="btn" type="submit">Generate Report</button>	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
					</form>																	
				</div>
			</div>
		</div>
	</div>
</div>

  
<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/salesReport.js"></script>	

<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("securityAddjustHome.action");
	setTitle("Security Adjustment Notice ");
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
    				<h4 id="rightSpan_caption">Security Notice </h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     				<form id="billProcessForm" name="billProcessForm" action="securityAdjustInfo.action" style="margin-bottom: 1px;">
										
						
						<div class="row-fluid">
							<div class="span12" style="margin-top: 4px;">
								<label style="width: 19.5%">Customer ID <m class='man'/></label>
								<input type="text" name="customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 10%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								<input type="text" name="" id="customer_id_x" disabled="" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 36.5%;margin-top: -5px;"/>
								
						  	</div>
						</div>
						
						
						
						<div class="row-fluid" id="from_to_date_div">							
							 <div class="span6" id="fromDateSpan">
				    			<label id="fromDateLabel">Date</label>
								<input type="text" name="from_date" id="from_date" style="width: 54%" value="<s:property value='from_date' />"/>
				  			</div>
						</div>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">
						   			  <input type="hidden" name="category_name" id="category_name" value="DOMESTIC(PVT)" />
						   			     
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

<script type="text/javascript">
Calendar.setup({
    inputField : "from_date",
    trigger    : "from_date",
	eventName : "focus",
    //onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  
  $("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){
    		getCustomerInfo("",$('#customer_id').val());
    	},
}));
</script>

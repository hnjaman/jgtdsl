<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("totalSecurityHome.action");
	setTitle("Total Security Deposit Generation");
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
    				<h4 id="rightSpan_caption">Total Security Deposit</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     				<form id="billProcessForm" name="billProcessForm" action="totalSecurityInfo.action" style="margin-bottom: 1px;">
						
						<div class="row-fluid" >
							<div class="span12">
								<div class="alert alert-info">
									<table width="50%" align="center">
										<tr>
											<td width="100%" align="right" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="date_wise" id="date_wise" name="report_for" onclick="checkType(this.id)"/> Periodic&nbsp;&nbsp;&nbsp;
												<input type="radio" value="month_wise" id="month_wise" name="report_for" onclick="checkType(this.id)" /> Month Wise&nbsp;&nbsp;&nbsp;
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>							
						<div class="row-fluid" id="month_year_div" style="display:none">							
							<div class="span6" id="month_div">									    
								<label style="width: 40%">Billing Month<m class='man'/></label>
								<select name="bill_month" id="billing_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6" id="year_div">
								<label style="width: 40%">Billing Year<m class='man'/></label>
								<select name="bill_year" id="billing_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						<div class="row-fluid" id="from_to_date_div" style="display: none">							
							 <div class="span6" id="fromDateSpan">
				    			<label id="fromDateLabel">From Date</label>
								<input type="text" name="from_date" id="from_date" style="width: 54%" value="<s:property value='from_date' />"/>
				  			</div>
				  			<div class="span6" id="toDateSpan">
				    			<label id="toDateLabel">To Date</label>
								<input type="text" name="to_date" id="to_date" style="width: 54%" value="<s:property value='to_date' />"/>
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

function checkType(type){
	

	if(type=="month_wise")
	{
	 hideElement("from_to_date_div");
	 showElement("month_year_div");
	}
	else if(type=="date_wise"){
	 hideElement("month_year_div");
	 showElement("from_to_date_div");
	}
	}

Calendar.setup({
    inputField : "to_date",
    trigger    : "to_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  Calendar.setup({
    inputField : "from_date",
    trigger    : "from_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
</script>



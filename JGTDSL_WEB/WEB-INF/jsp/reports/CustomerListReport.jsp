<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("custListHome.action");
	setTitle("Customer List Information");
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
    				<h4 id="rightSpan_caption">Customer List</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     				<form id="billProcessForm" name="billProcessForm" action="custListInfo.action" style="margin-bottom: 1px;">
						<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="100%" align="center">
										<tr>
											<td width="100%" align="right" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="area_wise" id="area_wise" name="report_for" onclick="checkType(this.id)"/> Area Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" value="category_wise" id="by_category" name="report_for" onclick="checkType(this.id)" /> Category Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" value="summary" id="by_summary" name="report_for" onclick="checkType(this.id)" /> Summary&nbsp;&nbsp;&nbsp;
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>
					
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Region/Area</label>
								<select id="area_id"  style="width: 56%;" disabled="disabled"  name="area">
									<option value="" selected="selected">Select Area</option>
									<s:iterator value="%{#session.USER_AREA_LIST}" id="areaList">
										<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
								</s:iterator>
								</select>									      
							</div>
							<div class="span6">
								<label style="width: 40%">Category<m class='man'/></label>
								<select id="customer_category" style="width: 56%;" disabled="disabled"  name="customer_category">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>       
							</div>  
						</div>
						
						<div class="row-fluid" id="meter_nonMeter">							
							<div class="span6">									    
								<label style="width: 40%">Type</label>
								<select id="customer_type" style="width: 56%;"   name="customer_type" onchange="fetchDisconnectType()">
									<option value="" >Select Category</option>									
										<option value="01"  selected="selected">Meter</option>
										<option value="02" >Non-Meter</option>		
								</select>  								      
							</div>
							<!-- <div class="span6">
								<label style="width: 40%">Status<m class='man'/></label>
								<select id="customer_status" style="width: 56%;"   name="customer_status" onchange="fetchDisconnectType()">
									<option value="" >Select Status</option>									
										<option value="01"  selected="selected">Active</option>
										<option value="02" >Inactive</option>		
								</select>       
							</div>  --> 
						</div>					
						<div class="row-fluid" id="from_to_date_div">							
							 <div class="span6" id="fromDateSpan">
				    			<label id="fromDateLabel">Select Date</label>
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

function checkType(type){
	if(type=="area_wise")
	{
	 disableChosenField("customer_id");
	 disableField("customer_category");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 enableField("area_id");
	 showElement("meter_nonMeter");
	}
	else if(type=="by_category"){
	 disableChosenField("customer_id");
	 enableField("customer_category","area_id");
	 autoSelect("customer_category","area_id");
	 showElement("meter_nonMeter");
	}
	else if(type=="individual"){
	 enableChosenField("customer_id");
	 disableField("customer_category","area_id");
	 resetSelectBoxSelectedValue("customer_category","area_id");
	}else if(type=="by_summary"){
		disableField("customer_category");
		 resetSelectBoxSelectedValue("customer_category");
		 autoSelect("area_id");
		 disableField("area_id");
		 hideElement("meter_nonMeter");
	}
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
	}

</script>



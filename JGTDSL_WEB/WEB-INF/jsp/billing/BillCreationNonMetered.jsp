<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("billCreationHome.action?bill_parameter.isMetered_str=0");
	setTitle("Bill Creation for Non-Metered Customer");
</script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/popupModal/ns-window.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/popupModal/css/ns-window.css" rel="stylesheet" />
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
<div class="meter-reading" style="width: 50%;height: 50%;float: left;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Bill Creation(Non-Metered)</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
     				<form id="billProcessForm" name="billProcessForm" style="margin-bottom: 1px;">
						<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="100%" align="center">
										<tr>
											<td width="100%" align="right" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="area_wise" id="area_wise" name="bill_parameter.bill_for" onclick="checkType(this.id)"/> All Bill&nbsp;&nbsp;&nbsp;	<!-- Area wise to All Bill -->
												<input type="radio" value="category_wise" id="by_category" name="bill_parameter.bill_for" onclick="checkType(this.id)"/> Category Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" value="individual_customer" id="individual" name="bill_parameter.bill_for" onclick="checkType(this.id)" checked="checked"/> Individual
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Customer ID <m class='man'/></label>
								<input type="text" name="bill_parameter.customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 36.5%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 36.5%;margin-top: -5px;"/>
						  	</div>
						</div>
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Region/Area</label>
								<select id="area_id"  style="width: 56%;" disabled="disabled"  name="bill_parameter.area_id">
									<option value="" disabled selected hidden selected="selected">Select Area</option>
									<s:iterator value="%{#session.USER_AREA}" id="areaList">
										<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
								</s:iterator>
								</select>									      
							</div>
							<!--  <div class="span6">
								<label style="width: 40%">Category<m class='man'/></label>
								<select id="customer_category" style="width: 56%;" disabled="disabled"  name="bill_parameter.customer_category">
									<option value="" selected="selected">Select Category</option>									
										<option value="01" >Domestic (PVT.)</option>
										<option value="02" >Domestic (GOVT.)</option>									
								</select>      
							</div> 
							-->
							
							<div class="span6">
								<label style="width: 40%">Category<m class='man'/></label>
								<select id="customer_category" style="width: 56%;" disabled="disabled"  name="bill_parameter.customer_category">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_NON_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>      
							</div>  
							
							
							
							
						</div>
						
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Billing Month<m class='man'/></label>
								<select name="bill_parameter.billing_month_str" id="billing_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6">
								<label style="width: 40%">Billing Year<m class='man'/></label>
								<select name="bill_parameter.billing_year" id="billing_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Bill Generation Date<m class='man'/></label>
								<input type="text" style="width: 51%"  name="bill_parameter.bill_generation_date" id="bill_generation_date" />
							</div>
							<div class="span6"></div>
						</div>
						<div class="row-fluid">							
							<div class="span12">									    
								<label style="width: 19.5%">Remarks</label>
								<textarea rows="1" style="width: 76%" name="bill_parameter.remarks"></textarea>
								<input type="hidden" name="bill_parameter.isMetered_str" value="0" id="isMeter"/>
							</div>
						</div>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		<td style="width: 30%" align="left">
						   			 <button class="btn btn-primary" type="button" id="btn_reload_grid" onclick="reloadBillGrid()">Reload Grid</button>
						   			 <button class="btn btn-primary" type="button" id="btn_unlockDB" onclick="unlockDatabase()">Unlock</button>
						   		</td>
						   		<td style="width: 70%" align="right">
						   		
						   		<s:if test='%{#session.user.role_name=="Super Admin"}' >
						   		<input type="checkbox" id="reprocess" name="bill_parameter.reProcess" value="Y"/> <font style="color: blue;font-size: 12px;">Re-process  Non-Approved Bills&nbsp;&nbsp;</font>
						   		</s:if>
						   		<s:else>
						   		<input type="checkbox" id="reprocess" name="bill_parameter.reProcess" value="Y" style="display: none;"/>
						   		</s:else>
						   		      
						   			 <button class="btn btn-beoro-3" type="button" id="btn_process_billing" onclick="validateAndProcessBilling()">Process Billing</button>    	
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

<div style="width: 47%;text-align: center;float: left;padding-top:20px;margin-left: 5px;display: none;" id="stat_div">
 <table width="100%" border="0">
 	<tr>
 	 <td style="text-align: left;padding-left: 10px;padding-bottom: 20px;background-color: #387C44;color: white;"  id="loading_div"></td>
 	</tr>
 	<tr>
 	 <td style="text-align: left;padding-left: 10px;padding-top: 20px;padding-bottom: 20px;background-color: #CCFFFF"  id="approx_total_div"></td>
 	</tr>
 	<tr>
 	 <td style="text-align: left;padding-left: 10px;padding-top: 20px;padding-bottom: 20px;background-color: #483C32;color: white;"  id="processed_total_div"></td>
 	</tr>
 </table>
</div>


  
<p style="clear: both;margin-top: 5px;"></p>

<div id="bill_grid_div" style="width: 99%;height: 48%;"> 
	<table id="bill_grid"></table>
	<div id="bill_grid_pager"></div>
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/billCreationNonMetered.js"></script>	

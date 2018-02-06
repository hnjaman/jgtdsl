<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("supplyOffHome.action");
	setTitle("Gas Suppy Off Information");
</script>
<style>
input[type="radio"], input[type="checkbox"]
{
margin-top: -3px !important;
}
.alert{
padding-top: 4px !important;
padding-bottom: 4px !important;
}
.supply-off{
	width: 65%;
	float: left;
	height: 62%;
}
.reading-list{
	clear: both;
	height: 36%;
	width: 99%;
}
</style>

<div class="supply-off" style="width: 50%;height: 45%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Supply Off</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
     				<form id="supplyOffForm" name="supplyOffForm" style="margin-bottom: 1px;">
						<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="100%" align="center">
										<tr>
											<td width="100%" align="right">
												<input type="radio" name="supplyOff.off_for"  value="area_wise" id="area_wise" name="off_for" onclick="checkType(this.id)"/> Area Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" name="supplyOff.off_for"  value="category_wise" id="category_wise" name="off_for" onclick="checkType(this.id)"/> Category Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" name="supplyOff.off_for"  value="individual_customer" id="individual" name="off_for" onclick="checkType(this.id)" checked="checked"/> Individual
											</td>											
										</tr>
									</table>                                     									 								
                                </div>                                
							</div>
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Customer ID <m class='man'/></label>
								<input type="text" name="supplyOff.customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 36.5%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 36.5%;margin-top: -5px;"/>
						  	</div>
						</div>
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Region/Area</label>
								<select name="supplyOff.area_id" id="area_id"  style="width: 56%;" disabled="disabled">
									<option value="" selected="selected">Select Area</option>
									<s:iterator value="%{#session.USER_AREA}" id="categoryList">
										<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
								</s:iterator>
								</select>									      
							</div>
							<div class="span6">
								<label style="width: 40%">Category<m class='man'/></label>
								<select name="supplyOff.customer_category" id="customer_category" style="width: 56%;" disabled="disabled">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>      
							</div>  
						</div>
						
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Billing Month<m class='man'/></label>
								<select name="supplyOff.billing_month" id="billing_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6">
								<label style="width: 40%">Billing Year<m class='man'/></label>
								<select name="supplyOff.billing_year" id="billing_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">From Date<m class='man'/></label>
								<input type="text" style="width: 51%"  name="supplyOff.from_date" id="from_date"/>
							</div>
							<div class="span6">									    
								<label style="width: 40%">To Date<m class='man'/></label>
								<input type="text" style="width: 51%"  name="supplyOff.to_date" id="to_date"/>								
							</div>
						</div>
						
						<div class="row-fluid">							
							<div class="span12">									    
								<label style="width: 19.5%">Remarks</label>
								<textarea rows="1" style="width: 76%" name="supplyOff.remarks" id="remarks"></textarea>
							</div>
						</div>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">										    
						    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveSuppyOff()" id="btn_save">Save Supply off Information</button>    	
							<button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>									
						</div>
					</form>																	
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 
Commenting it . We will see/implement this later....
<jsp:include page="SupplyOffInfo.jsp" />
 -->
<p style="clear: both;margin-top: 5px;"></p>

<div class="customer-list" id="customer_grid_div" style="width: 99%;height: 53%"> 
	<table id="customer_grid"></table>
	<div id="customer_grid_pager" ></div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/supplyOff.js"></script>
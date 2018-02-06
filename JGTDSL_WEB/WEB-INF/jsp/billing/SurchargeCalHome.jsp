<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("surchargeCalHome.action");
	setTitle("Surcharge Calculation(Metered)");
</script>
<style> input[type="checkbox"] {margin-top: -5px;} </style>
<form action="saveSurchargeInfo.action" name="surchargeForm" id="surchargeForm">
<div style="width: 40%;height: 98%;float: left;" id="left_div">	
	<div class="row-fluid">
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Customer Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">						
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 40%">Customer Code</label>
								<input type="text" name="customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 10%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 10%;margin-top: -5px;"/>
						  	</div>	
						  	<div class="span6">									    
								<label style="width: 40%">Customer Type</label>
								<input type="text" id="customer_type" style="width: 52%"  value="Meterred" readonly="readonly"/>									      
							</div>
						 </div>
						 <div class="row-fluid">
							<div class="span6">
								<label style="width: 40%">Region/Area</label>	
								<select id="area"  style="width: 60%;" disabled="disabled">
									<option value="" selected="selected">Select Area</option>
									<s:iterator value="%{#session.USER_AREA}" id="areaList">
										<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
									</s:iterator>
								</select>									      
								
						  	</div>	
						  	<div class="span6">
								<label style="width: 40%">Category</label>	
								<select id="customer_category" style="width: 58%;" disabled="disabled">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>																	
						  	</div>
						 </div>

						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Customer Name</label>	
								<input type="text" style="width: 77%"  id="full_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">F/H Name</label>	
								<input type="text" style="width: 77%"  id="father_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Mobile</label>	
								<input type="text" style="width: 77%"  id="mobile" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Address</label>	
								<textarea rows="4" cols="" style="width: 77%;" id="customer_address" disabled="disabled"></textarea>									
								
						  	</div>	
						 </div>

						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<!-- 
								<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSubmitReplacement()" >Adjustment Bill/Invoice</button>
							 -->
						</div> 
						 
				</div>
		 </div>
		</div>
	</div>
	
		<div class="row-fluid">
		<div style="width: 100%;float: left;margin-top: 5px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Pay Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">						
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19%">Pay Date</label>
								<input type="text" style="width: 21%"  id="pay_date"   name="pay_date" />
						  	</div>	
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19%">Surcharge @</label>
								<input type="text" style="width: 21%"  id="surcharge_rate"  name="surcharge_rate" value="12" disabled="disabled"/> %
						  	</div>	
						</div>
				</div>
			</div>
		</div>
		</div>
</div>
<input type="hidden" name="surcharge_bills" id="surcharge_bills" />
</form>
<div style="width: 58%;height: 96%;float: left;margin-left: 10px;" id="right_div">
	<table id="dues_bill_grid"></table>
	<div id="dues_bill_grid_pager" ></div>
	<div style="text-align: right;padding-top: 4px;">
		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveAndDownloadBill()" disabled="disabled">Save Surcharge and Print Bill</button>
	</div>	
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/surchargeCalculation.js"></script>
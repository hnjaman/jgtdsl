<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("securityAdjustmentHome.action");
	setTitle("Security Deposit Adjustment");
</script>

<div id="customer_meter_div" style="height: 60%;width: 99%;">
	<div class="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 57%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
				<div class="row-fluid">
			<div class="span12" id="rightSpan">
				<div class="w-box">
					<div class="w-box-header">
						<h4 id="rightSpan_caption">
							Current Security & Others Balance (from Security Ledger)
						</h4>
					</div>
		<div class="w-box-content" style="padding: 10px;height: 120px;" id="content_div">
			<form id="meterForm" name="meterForm" style="margin-bottom: 1px;">
				<div class="row-fluid">
					<div class="span12" style="text-align: center;">
					
					<table style="width: 80%;border: 1px solid #ccc;" align="center">
						<tr style="background-color: white;">
							<td width="50%"  style="text-align: center;font-weight: bold;">Security</td>
							<td width="50%" style="text-align: center;font-weight: bold;">Others</td>
						</tr>
						<tr>
							<td style="text-align: center;font-weight: bold;font-size: 22px;padding: 10px;height: 25px;"><span id="security"></span>
							<input type="hidden" id="security_hidden" />
							</td>
							<td style="text-align: center;font-weight: bold;font-size: 22px;padding: 10px;"><span id="others"></span>
							<input type="hidden" id="others_hidden" />
							
							</td>
						</tr>
						<tr>
							<td style="text-align: center;font-weight: bold;font-size: 22px;padding: 10px;">
								<input type="text" id="adjustment_security" style="width: 100px;font-size: 22px;font-weight: bold;text-align: center;" maxlength="8" onchange="checkAdjustmentAmount()"/>
							</td>
							<td style="text-align: center;font-weight: bold;font-size: 22px;padding: 10px;">
								<input type="text" id="adjustment_others" style="width: 100px;font-size: 22px;font-weight: bold;text-align: center;" maxlength="8" onchange="checkAdjustmentAmount()"/>
							</td>
						</tr>
					</table> 
					<br/>
					<span id="msg1Div" style="color:red"></span><br/>
					<span id="msg2Div" style="color:red"></span>
						
					</div>
					
				</div>
				

				
						</form>
					</div>
				</div>
			</div>
		</div>
		

	</div>

	<div style="width: 51%; height: 99%;float: left;margin-left: 1%;">

	<div class="row-fluid">
			<div class="span12" id="rightSpan">
				<div class="w-box">
					<div class="w-box-header">
						<h4 id="rightSpan_caption">
							Adjustment Detail
						</h4>
					</div>
		<div class="w-box-content" style="padding: 10px;" id="content_div">
			<form id="meterForm" name="meterForm" style="margin-bottom: 1px;">
				<div class="row-fluid">
					<div class="span12">
						<label style="width: 40%">Adjustment Amount</label>
						<b>(-)</b>&nbsp;<input type="text" id="totalAdjustableAmount" 
						style="width: 40%;font-weight: bold;" maxlength="30" tabindex="101" disabled="disabled" />
					</div>	
					<div class="span12">
						<label style="width: 40%">Adjustment Mode</label>
						<div style="float: left;"><input type="radio" id="adjustmentMode_refund" name="adjustmentMode" value="1" onclick="setAdjustmentMode('refund')" checked="checked"/>&nbsp;</div>
						<div style="float:left;padding-right: 10px;margin-top: 3px;">Refund</div>
						<div style="float: left;"><input type="radio" id="adjustmentMode_withBill" name="adjustmentMode" value="2" onclick="setAdjustmentMode('withBill')"/>&nbsp;</div>
						<div style="float:left;padding-right: 10px;margin-top: 3px;">Adjustment with arrear Bill</div>
					</div>	
					<div class="span12">
						<label style="width: 40%">Comment</label>
						<input type="text" id="comment" style="width: 40%" maxlength="30" tabindex="101" />
					</div>	
					<div class="span12">
						<label style="width: 40%">Date</label>
						<input type="text"  id="collectionDate" style="width: 40%" maxlength="30" tabindex="101" />
					</div>

					<fieldset class="span12 control" style="width: 90%;padding-left: 10px;" id="refundFieldset">
					<legend style="text-align: left;color: red;">Check's Bank Account Info</legend>
					<div class="span12 control">
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Bank</label>
								<select id="refund_bank_id"   style="width: 40.5%;"   onchange="fetchSelectBox(refund_branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(refund_branch_sbox)">
											<option value="" selected="selected">Select Bank</option>
											<s:iterator value="%{#session.USER_BANK_LIST}">
												<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
										</s:iterator>
							 </select>
							 &nbsp;<input id="otherCheckBox_refund" type="checkbox" onclick="setOtherOption()"/>&nbsp;Others
							 
							</div>
						</div>
					</div>	
					<div class="span12 control">
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Branch</label>
								<select id="refund_branch_id"  style="width: 54.5%;"  onchange="fetchSelectBox(refund_account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(refund_account_sbox)">
											<option value="" selected="selected">Select Branch</option>
										</select> 
							</div>
						</div>
					</div>
					<div class="span12 control">
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Account</label>
								<select id="refund_account_id" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
											<option value="" selected="selected">Select Account</option>
										</select> 
							</div>
						</div>
					</div>	
					</fieldset>		
					
					<fieldset class="span12 control" style="width: 90%;padding-left: 10px; display: none;" id="collectionFieldset">
					<legend style="text-align: left;color: red;">Adjustment</legend>
					<div class="span12 control">
						<div class="row-fluid">
							<div class="span12">
							
								<table  width="95%" style="border: 1px dotted #ddd;" border="1"> 
									<tr>
										<td width="50%" style="text-align: center;">Deduct From</td>
										<td width="50%" style="text-align: center;">Add To</td>
									</tr>
									<tr>
										<td>
											<div class="row-fluid">
												<div class="span12">
													<label style="width: 22%">Bank</label>
													<select id="bill_adj_deduct_bank_id"   style="width: 60.5%;"   onchange="fetchSelectBox(bill_adj_deduct_branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(bill_adj_deduct_branch_sbox)">
																<option value="" selected="selected">Select Bank</option>
																<s:iterator value="%{#session.USER_BANK_LIST}">
																	<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
															</s:iterator>
												 </select>
												 &nbsp;<input id="otherCheckBox_deduct" type="checkbox" onclick="setDeductOtherOption()"/><font style="font-size: 10px;">Others</font>
												 
												</div>
											</div>
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 22%">Branch</label>
														<select id="bill_adj_deduct_branch_id"  style="width: 77%;"  onchange="fetchSelectBox(bill_adj_deduct_account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(bill_adj_deduct_account_sbox)">
																	<option value="" selected="selected">Select Branch</option>
																</select> 
													</div>
												</div>
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 22%">Account</label>
														<select id="bill_adj_deduct_account_id"  onkeypress="selectvalue.apply(this, arguments)" style="width: 77%;">
																	<option value="" selected="selected">Select Account</option>
																</select> 
													</div>
												</div>
										</td>
										<td>
											<div class="row-fluid">
												<div class="span12">
													<label style="width: 22%">Bank</label>
													<select id="bill_adj_add_bank_id"   style="width: 60.5%;"   onchange="fetchSelectBox(bill_adj_add_branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(bill_adj_add_branch_sbox)">
																<option value="" selected="selected">Select Bank</option>
																<s:iterator value="%{#session.USER_BANK_LIST}">
																	<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
															</s:iterator>
												 </select>
												 &nbsp;<input id="otherCheckBox_add" type="checkbox" onclick="setAddOtherOption()"/><font style="font-size: 10px;">Others</font>
												 
												</div>
											</div>
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 22%">Branch</label>
														<select id="bill_adj_add_branch_id"  style="width: 77%;"  onchange="fetchSelectBox(bill_adj_add_account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(bill_adj_add_account_sbox)">
																	<option value="" selected="selected">Select Branch</option>
																</select> 
													</div>
												</div>
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 22%">Account</label>
														<select id="bill_adj_add_account_id" onkeypress="selectvalue.apply(this, arguments)" style="width: 77%;">
																	<option value="" selected="selected">Select Account</option>
																</select> 
													</div>
												</div>
										</td>
									</tr>
								</table>
								
							 
							</div>
						</div>
					</div>	
					
					
					</fieldset>	
					
				</div>


				<div class="formSep" style="padding-top: 2px; padding-bottom: 2px;">
					<div id="aDiv" style="height: 0px;"></div>
				</div>

				<div class="formSep sepH_b"
					style="padding-top: 3px; margin-bottom: 0px; padding-bottom: 2px;">
					<table width="100%">
						<tr>
							<td width="55%" align="left">
								<button class="btn btn-beoro-3" disabled="disabled" type="button" id="btn_save" onclick="validateSecurityAdjustment()"><span class="splashy-document_letter_okay"></span>
								    Save</button>							
							</td>

							<td width="5%">
								
							</td>
							<td width="40%" align="right">
								<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>
									Close</button>						
							</td>
						</tr>
					</table>

							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="row-fluid" style="width:100%;height: 200px;padding-top: 20px;" id="dues_div">
	   	 
		         <div>
						<table id="dues_bill_grid" ></table>
						<div id="dues_bill_grid_pager" ></div>
		         </div>
		         <input type="hidden" id="totalAdjustAmount" />
		         				
	</div>
	
	</div>
</div>

<div id="customer_grid_div" style="height: 38%;width: 99%;">
 	<table id="customer_grid"></table>
	<div id="customer_grid_pager" ></div>
</div>
 
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/securityAdjustment.js"></script>
<script>

</script>



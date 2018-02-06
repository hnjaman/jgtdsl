<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("adjustmentCollectionHome.action");
	setTitle("Adjustment Bill Collection Information");
</script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/popupModal/ns-window.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/popupModal/css/ns-window.css" rel="stylesheet" />
<link href="/JGTDSL_WEB/resources/css/page/billCollection.css" rel="stylesheet" type="text/css" />
<style>
	input[type="radio"], input[type="checkbox"]{
		margin-top: -3px !important;
	}
	.alert{
		padding-top: 4px !important;
		padding-bottom: 4px !important;
	}
	.ui-icon, .ui-widget-content .ui-icon {
    	cursor: pointer;
	}

#ui-datepicker-div {z-index:99999999999 !important;}
</style>

<div class="collection-form">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Bill Collection Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
     				<form id="billCollectionForm" name="billCollectionForm">						
						<div class="row-fluid">
							<div class="span6">
								<!-- Start -->						
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Bank</label>
										<select id="bank_id" name="collection.bank_id"  style="width: 54.5%;"   onchange="fetchSelectBox(branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(branch_sbox)">
											<option value="" selected="selected">Select Bank</option>
											<s:iterator value="%{#session.USER_BANK_LIST}">
												<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
										</s:iterator>
										</select>
										<input type="hidden" name="collection.customer.connectionInfo.isMetered_str" value="" id="isMetered_str" />
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Branch</label>
										<select id="branch_id" name="collection.branch_id" style="width: 54.5%;"  onchange="fetchSelectBox(account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
											<option value="" selected="selected">Select Branch</option>
										</select>  
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Account</label>
										<select id="account_id" name="collection.account_no" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
											<option value="" selected="selected">Select Account</option>
										</select> 
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Billing Month</label>
										<span id="metered_bill_month_year" >
										  <select name="collection.bill_month" id="bill_month" style="width: 25%;margin-left: 0px;" onchange="getBillingInfoAgainstMonthYear()" onkeypress="selectvalue.apply(this, arguments),getBillingInfoAgainstMonthYear()" disabled="disabled">
										       	<option value="">Select Month</option>           
										        <s:iterator  value="%{#application.MONTHS}">   
										   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
												</s:iterator>
									       </select>
									       
									       
									       <select name="collection.bill_year" id="bill_year" style="width: 25%;margin-left:4.5%;" onchange="getBillingInfoAgainstMonthYear()" onkeypress="selectvalue.apply(this, arguments),getBillingInfoAgainstMonthYear()" disabled="disabled">
										       	<option value="">Year</option>
										       	<s:iterator  value="%{#application.YEARS}" id="year">
										            <option value="<s:property/>"><s:property/></option>
												</s:iterator>
									       </select>
								       </span>
								       <span id="nonmetered_bill_month_year" style="display:none;">
								       		<select style="width: 25%;margin-left: 0px;">
										       	<option value="">Select Bill Month</option>           
										        <s:iterator  value="%{#application.MONTHS}">   
										   			<option value="<s:property value='id'/>"><s:property value="label"/>, </option>
												</s:iterator>
									       </select>
								       </span>
									</div>
								</div>
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Phone</label>
										<input type="text" style="width: 51%;height: 18px;overflow-y: visible; " id="phone" name="collection.phone" />
										<input type="hidden" id="phone_old"  />
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Customer ID </label>
										<input type="text" name="collection.customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 16%;margin-top: -4px;" value="<s:property value='customer_id' />" disabled="disabled"/>
										<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 16%;margin-top: -5px;"/>
										<input type="hidden" name="collection.collection_id" id="collection_id" /> 
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Region/Area</label>
										<select id="area_id"  style="width: 54.5%;" disabled="disabled">
											<option value="" selected="selected">Select Area</option>
											<s:iterator value="%{#session.USER_AREA}" >
												<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
										</s:iterator>
										</select>	 
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Customer Name</label>
										<input type="text" style="width: 51%"  name="bill_parameter.issue_date" id="customer_name"  disabled="disabled"/>
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Address</label>
										<textarea style="width: 51%;height: 35px;overflow-y: visible; " id="address" readonly="readonly"></textarea>
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Collection Date</label>
										<input type="text" name="collection.collection_date" id="collection_date" style="width: 51%"/>
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Remarks</label>
										<input type="text" name="collection.remarks" id="remarks" style="width: 51%"/>		
										
										
										<s:if test='%{#session.user.userName=="sgazi"}' >
										<br/>										
										<a href="#" onclick="showAllBanks()" style="color:white">Show All Banks</a>	| <a href="#" onclick="showAreaBanks()" style="color:white">Show Area Banks</a>
										</s:if>
										
									</div>
								</div>
								<div class="row-fluid" id="burnerDiv" style="display: none;">
									<div class="span12">
									<label style="width: 40%">Double Burner(Qnt)</label>
										<div id="divBurnerQnt" style="width: 40%;height: 50px;width: 51%;font-size: 40px;font-weight:bold;margin-left: 41%;border: 3px dashed black;text-align: center;padding-top: 25px;">
										</div>
																											
									</div>
								</div>								
								<!-- End -->
							</div>
							<div class="span6">
								<!-- Start -->
							<div class="row-fluid">								
							  	<div class="span12">									    
									<label style="width: 40%">Code</label>
									<input type="text" id="customer_code" tabindex="1" style="width: 44%"/>
									&nbsp;&nbsp;
									<i class="fa fa-th-list" style="cursor: pointer;border: 1px dotted green;" onclick="fetchCollectionList($('#customer_id').val());"> </i>
									<input type="hidden" id="bill_id" name="collection.bill_id"/>
									
								</div>
							</div>
							<div class="row-fluid">														
								<div class="span12">
									<label style="width: 40%">Category</label>
									<select id="category_id" onclick="getAccountInfo()" style="width: 54.5%;">
										<option value="" selected="selected">Select Category</option>
										<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" >
											<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
										</s:iterator>
									</select>      
								</div>  
							</div>
												
							<div class="row-fluid">														
								<div class="span12">									    
									<label style="width: 40%">Customer Type</label>
									<input type="text" style="width: 51%;color: green;font-weight: bold;"  name="bill_parameter.issue_date" id="customerType" disabled="disabled" />
								</div>							
							</div>
						
							<div class="row-fluid">														
								<div class="span12">									    
									<label style="width: 40%">Mobile</label>
									<input type="text" style="width: 51%;background-color:aqua;color: black;font-weight: bold;"  id="mobile" name="collection.mobile" />
									<input type="hidden" id="mobile_old"  />
								</div>
							</div>
						
							<div class="row-fluid" id="metered_bill_amount">														
								<div class="span12">
									<label style="width: 40%">Bill Amount</label>
									<input type="text" id="billed_amount" name="collection.billed_amount" style="text-align: right;font-weight: bold;color: red;width: 51%" readonly="readonly"/>  				
								</div>
							</div>
						
						
							<div class="row-fluid" id="metered_surcharge">														
								<div class="span12">
									<label style="width: 40%">Surcharge Amount</label>
									<input type="text" name=""  id="surcharge_amount" name="collection.surcharge_amount_metered" tabindex="3" style="text-align: right;color: blue;width: 51%" readonly="readonly"/>
								</div>  
								
								
							</div>
							<div class="row-fluid" id="vat_rebate_div">														
								<div class="span12">
									<label style="width: 40%">Vat Rebate</label>
									<input type="text" name="collection.vat_rebate_amount" id="vat_rebate_amount" tabindex="4" style="text-align: right;width: 51%" readonly="readonly"/>
								</div>
							</div>
						
							<div class="row-fluid" id="tax_amount_div">						    
								<div class="span12">
									<label style="width: 40%">Tax Amount/ Chalan No/ Chalan Date</label>
									<input type="text" name="collection.tax_amount" id="tax_amount" tabindex="5" style="text-align: right;width: 15%;"  value="0" onkeyup="recalculateCollection(this.value)"/>
									<input type="text" name="collection.tax_no" id="tax_no" tabindex="2" style="text-align: right;font-weight: bold;color: green;width: 15%;"/>
									<input type="text" name="collection.tax_date" id="tax_date" tabindex="2" style="text-align: right;font-weight: bold;color: green;width: 15%;"/>
								</div>
															
							</div>
							<div class="row-fluid" id="adjustment_div">						    
								<div class="span12">
									<label style="width: 40%">Adjustment</label>
									<input type="text" name="collection.adjustment_amount" id="adjustment_amount" tabindex="5" style="text-align: right;width: 51%"  value="0" readonly="readonly"/>
								</div>	
															
							</div>
							<div class="row-fluid" id="net_payable_amount">
								<div class="span12">
									<label style="width: 40%">Net Payable Amount</label>
									<input type="text" name="collection.payable_amount" id="payable_amount" style="text-align: right;font-weight: bold;color: red;width: 51%" readonly="readonly"/>
								</div>													
							</div>
							
							<div class="row-fluid" id="non_metered_billed_amount">
								<div class="span12">
									<label style="width: 40%">Act./Coll. Billed Amount</label>								
									<input type="text" name="collection.actual_billed_amount" id="actual_billed_amount" tabindex="2" readonly="readonly" style="text-align: right;font-weight: bold;color: white;width: 23%; background-color: green;"/>
									<input type="text" name="collection.collected_billed_amount" id="collected_billed_amount" tabindex="2" readonly="readonly" style="text-align: right;font-weight: bold;color: white;width: 24%; background-color: green;"/>
								</div>
															
							</div>
							<div class="row-fluid" id="non_metered_surcharge_amount">
								<div class="span12">
									<label style="width: 40%">Act./Coll. Surcharge</label>								
									<input type="text" name="collection.actual_surcharge_amount" id="actual_surcharge_amount" tabindex="2" readonly="readonly" style="text-align: right;font-weight: bold;color: black;width: 23%; background-color: #e57ef5;"/>
									<input type="hidden" name="collection.surcharge_per_collection" id=surcharge_per_collection tabindex="2" readonly="readonly" style="text-align: right;font-weight: bold;color: black;width: 23%; background-color: #e57ef5;"/>
									<input type="text" name="collection.collected_surcharge_amount" id="collected_surcharge_amount" tabindex="2" readonly="readonly" style="text-align: right;font-weight: bold;color: black;width: 24%; background-color: #e57ef5;"/>
								</div>
															
							</div>
							<div class="row-fluid" id="non_metered_payable_amount">
								<div class="span12">
									<label style="width: 40%">Act./Coll. Payable Amount</label>								
									<input type="text" name="collection.actual_payable_amount" id="actual_payable_amount" tabindex="2" readonly="readonly" style="text-align: right;font-weight: bold;color: black;width: 23%; background-color: yellow;"/>
									<input type="text" name="collection.collected_payable_amount" id="collected_payable_amount" tabindex="2" style="text-align: right;font-weight: bold;color: black;width: 24%; background-color: yellow;"/>
								</div>														
							</div>
							<div class="row-fluid" id="non_metered_surcharge_collection_amount">
								<div class="span12">
									<label style="width: 40%">Surcharge Amount</label>								
									<input type="text" name="collection.surcharge_amount" id="surcharge_amount_meter" tabindex="2" style="text-align: right;font-weight: bold;color: black;width: 51%; background-color: #c67544;"/>
								</div>														
							</div>
	
							<div class="row-fluid">
								<div class="span12">
									<label style="width: 40%">Collection Amount</label>								
									<input type="text" name="collection.collected_amount" id="collected_amount" tabindex="2" style="text-align: right;font-weight: bold;color: black;width: 51%; background-color: #c67544;"/>
								</div>														
							</div>
							<!-- End -->
							</div>
						</div>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">										
						    <table width="100%">
						    <tr>
						    	<td width="35%" align="left">								
									<div id="msg_div" class="count_div" style="text-align: left;"></div>
										<div style="width: 100%;margin-top: 5px;">
	    <div>
	    <button class="btn btn-beoro-3" type="button" id="btn_refresh" onclick="getTotalCollectionByDateAccount();getCollectionHistoryByDate();">
				<span class="splashy-refresh"></span>
			</button>
		<button class="btn btn-beoro-3"  type="button" id="btn_multi_coll" title="Multi Collection" style="padding: 2px 8px;margin-left: 10px;" onclick="multiCollectionModal()" >							       
				<span class="splashy-calendar_week_add"></span>
		</button>
			
		<%-- 	<button class="btn btn-beoro-3" type="button" id="btn_multi_delete" onclick="multiDeleteModal()" disabled="disabled" >
				<span class="splashy-box_remove"></span>
				  	Multi Delete
			</button> --%>	
	    </div>
		<div class="alert alert-error" style="width: 88%;float: left;margin-top: 7px;">
			<strong>Total Deposit :</strong> <span id="total_collection" style="font-size:20px;color:blue;font-weight:bold;"></span> 
		</div>
		
			
	</div>	
						    	</td>
						    	<td width="5%"></td>
						    	<td width="60%" align="right">
						    		<button class='btn btn-beoro-3' type='button' id='btn_next' onclick="showShortCuts()"><span class='splashy-sprocket_dark'></span></button>
							    	<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveCollection()" id="btn_save"><span class="splashy-document_letter_okay"></span>Save Collection</button> 
									<!-- 
							    	<button class="btn btn-beoro-3" type="button" id="btn_parameter"><span class="splashy-help"></span> Help</button>
							    	 -->
							    	<%--  <button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="editButtonPressed()" disabled="disabled">
										<span class="splashy-application_windows_edit"></span> Edit
									 </button> --%>
							    	 <button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="$delete.dialog('open');" disabled="disabled">
											<span class="splashy-gem_remove"></span> Delete
									</button>
									<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>Close</button>
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

<div class="customer-grid" id="customer_grid_div">
	<table id="customer_grid"></table>
	<div id="customer_grid_pager" ></div>
	
	<div style="margin-top: 5px;display: none;">
 		<select style="width:35%">
 			 <option></option>
 		</select>
		<input type="text" style="width: 50%;margin-left: 5px;"/> 
		<button class="btn btn-beoro-3"  type="button" id="btn_cancel" style="padding: 2px 10px;">
			<span class="splashy-zoom"></span>
		</button>
	</div>	
</div>

<p style="clear: both;margin-top: 5px;clear: both;"></p>

<div class="collection-list" id="collection_grid_div"> 
	<table id="collection_grid"></table>
	<div id="collection_grid_pager" ></div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/adjustmentcollection.js"></script>
<script type="text/javascript">

var baseBanks= $("#bank_id").html();
</script>
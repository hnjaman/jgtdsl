<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("billCollectionHome.action");
	setTitle("Meter Customers Bill Collection Information");
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
.collection-form{
	float: left;
	width: 100%;
	height: 67%;
}
#info_entry_div{
  float: left;
 
}
#ui-datepicker-div {z-index:99999999999 !important;}
</style>

<div class="collection-form">

     <form id="billCollectionForm" name="billCollectionForm">	
     	<div class="w-box">
		  <div class="w-box-header">
    				<h4 id="rightSpan_caption">Meter Bill Collection Information</h4>
		 </div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">					
						<div style="width: 30%;height: 100%;float: left;">
							<div >
								<!-- Start -->
								<!--  <div class="row-fluid">														
								<div class="span12">
									<label style="width: 40%">Category</label>
									<select id="category_id" onclick="getAccountInfo()" style="width: 54.5%;">
										<option value="" selected="selected">Select Category</option>
										<s:iterator value="%{#application.ACTIVE_METERED_CUSTOMER_CATEGORY}" >
											<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
										</s:iterator>
									</select>      
								</div>  
							</div> -->
								
								
								
								
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
										<label style="width: 40%">Collection Date</label>
										<input type="text" name="collection.collection_date" id="collection_date" style="width: 51%"/>
									</div>
								</div>	
								
								<div class="row-fluid">								
							  	<div class="span12">									    
									<label style="width: 40%">Code</label>
									<input type="text" id="customer_code"  style="width: 44%"/>
									&nbsp;&nbsp;
									<i class="fa fa-th-list" style="cursor: pointer;border: 1px dotted green;" onclick="fetchCollectionList($('#customer_id').val());"> </i>
									<input type="hidden" id="bill_id" name="collection.bill_id"/>
									
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
										<label style="width: 40%">Customer ID </label>
										<input type="text" name="collection.customer_id" id="customer_id" style="font-weight: bold;color: #3b5894; z-index: 2; background: transparent;width: 51%;margin-top: 2px;" value="<s:property value='customer_id' />" disabled="disabled"/>
										<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 14%;margin-top: -5px;"/>
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
										<label style="width: 40%">Billing Month</label>
										<span id="metered_bill_month_year" >
										  <select name="collection.bill_month" id="bill_month" style="width: 28%;height:25px;margin-left: 0px;" onchange="getBillingInfoAgainstMonthYear()" onkeypress="selectvalue.apply(this, arguments),getBillingInfoAgainstMonthYear()" disabled="disabled">
										       	<option value="">Select Month</option>           
										        <s:iterator  value="%{#application.MONTHS}">   
										   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
												</s:iterator>
									       </select>
									       
									       
									       <select name="collection.bill_year" id="bill_year" style="width: 25%;margin-left:1.5%;" onchange="getBillingInfoAgainstMonthYear()" onkeypress="selectvalue.apply(this, arguments),getBillingInfoAgainstMonthYear()" disabled="disabled">
										       	<option value="">Year</option>
										       	<s:iterator  value="%{#application.YEARS}" id="year">
										            <option value="<s:property/>"><s:property/></option>
												</s:iterator>
									       </select>
								       </span>
								      
									</div>
							
							
						</div>
								
															
								<!-- End -->
							</div>
						<div >
								<!-- Start of meter or nometer info-->
							
							
								
					
							<!-- End -->
						</div>
				</div>
							 <div  id="info_entry_div" style="width:50%;padding-left: 10px ">	
		                   	    <div id="meter_info_div" style="display:none">
								 <%@ include file="billCollectionMeterInfo.jsp" %>
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
				</div>
			
			</div>
			
	 
			
		
  </form>
</div>																	
		



<p style="clear: both;margin-top: 5px;clear: both;"></p>

<div class="collection-list" id="collection_grid_div"> 
	<table id="collection_grid"></table>
	<div id="collection_grid_pager" ></div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/collection.js"></script>
<script type="text/javascript">

var baseBanks= $("#bank_id").html();
</script>
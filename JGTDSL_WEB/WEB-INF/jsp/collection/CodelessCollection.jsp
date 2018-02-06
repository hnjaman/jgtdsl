<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("codelessCollectionHome.action");						// ** COLLECTION NON METERED
	setTitle("Codeless Customers Bill Collection Information");
</script>


<!-- not sure about this includes -->
<link rel="stylesheet" href="/JGTDSL_WEB/resources/thirdParty/smart-wizard/styles/smart_wizard.css">
<script src="/JGTDSL_WEB/resources/thirdParty/smart-wizard/js/jquery.smartWizard.js"></script>
<script src="/JGTDSL_WEB/resources/thirdParty/jTPS/jTPS.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/jTPS/jTPS.css" rel="stylesheet" />
<!--end of : not sure about this includes -->



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


<div class="collection-form"  >

     <form id="codelessCollectionForm" name="codelessCollectionForm">	
     	<div class="w-box">
		  <div class="w-box-header">
    				<h4 id="rightSpan_caption">Codeless Collection Information</h4>
		 </div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">					
						<div style="width: 50%;height: 100%;float: left;">
							<div >
								<!-- Start -->
								<div class="bank_info" id="bank_info">
								<!--<div class="row-fluid">														
								<div class="span12">
									<label style="width: 40%">Category</label>
									<select id="category_id" onclick="getAccountInfo()" style="width: 54.5%;">
										<option value="" selected="selected">Select Category</option>
										<s:iterator value="%{#application.ACTIVE_NON_CUSTOMER_CATEGORY}" >
											<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
										</s:iterator>
									</select>      
								</div>  
							</div>-->
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Bank</label>
										<select id="bank_id" name="collection.bank_id"  style="width: 54.5%;"   onchange="fetchSelectBox(branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(branch_sbox)">
											<option value="" selected="selected">Select Bank</option>
											<s:iterator value="%{#session.USER_BANK_LIST}">
												<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
										</s:iterator>
										</select>
										<input type="hidden" name="collection.customer.connectionInfo.isMetered_str" value="" id="isMetered_str" />
									</div>
									<div class="span6">
										<label style="width: 41%">Region/Area</label>
										<select id="area_id"  style="width: 54.5%;" disabled="disabled" >
											<option value="" selected="selected">Select Area</option>
											<s:iterator value="%{#session.USER_AREA_LIST}" >
												<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
												<input type="hidden" name="collection.area_id"  value="<s:property value="area_id" />" >
										</s:iterator>
										</select>	 
									</div>
									
									
								</div>	
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Branch</label>
										<select id="branch_id" name="collection.branch_id" style="width: 54.5%;"  onchange="fetchSelectBox(account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
											<option value="" selected="selected">Select Branch</option>
										</select>  
									</div>
									
									
									
								</div>	
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Account</label>
										<select id="account_id" name="collection.account_no" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
											<option value="" selected="selected">Select Account</option>
										</select> 
									</div>
								</div>
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Collection Date</label>
										<input type="text" name="collection.collection_date" id="collection_date" style="width: 51%"/>
									</div>
								</div>
								
							</div>	
							<div class="collection_info" id="collection_info">	
							
							<div class="row-fluid" id="from_bill_month_div">
					        	<div class="span12">
					         		<label style="width: 20%">From Month Year</label>        
					         			<input type="text" name="collection.from_month" id="from_month" tabindex="1" style="text-align: right;font-weight: bold;color: black;width: 11.2%; background-color: #FFFFF;"/>
					         			<input type="text" name="collection.from_year" id="from_year" tabindex="2" style="text-align: right;font-weight: bold;color: black;width: 11.2%; background-color: #FFFFF;"/>
					        	</div>              
					       </div>
					       <div class="row-fluid" id="to_bill_month_div">
					        	<div class="span12">
					         		<label style="width: 20%">To Month Year</label>        
					         			<input type="text" name="collection.to_month" id="to_month" tabindex="3" style="text-align: right;font-weight: bold;color: black;width: 11.2%; background-color: #FFFFF;"/>
					         			<input type="text" name="collection.to_year" id="to_year" tabindex="4" style="text-align: right;font-weight: bold;color: black;width: 11.2%; background-color: #FFFFF;"/>
					        	</div>
					        </div>	
								
						<div class="row-fluid">								
							<div class="row-fluid" id="advanced_div" >
								<div class="span12">
									<label style="width: 20%">Bill Amount</label>								
									<input type="text" name="collection.advanced_amount" id="advanced_amount" tabindex="3" style="text-align: right;font-weight: bold;color: blue;width: 45% background-color: #FFFFF;"/>
								</div>
																			
							</div>
						</div>
						<div class="row-fluid" id="surcharge_div" >
								<div class="span12">	
									<label style="width: 20%">Surcharge Amount</label>	
									<input type="text" name="collection.surcharge_amount" id="surcharge_amount" tabindex="2" style="text-align: right;font-weight: bold;color: blue;width: 51% background-color: #FFFFF;"/>
								</div>												
							</div>
						<div class="row-fluid" >
							<div class="span12">
										<label style="width: 20%">Customer ID </label>
										<input type="text" name="collection.customer_id" id="customer_id" tabindex="2" style="width: 51% background-color: #FFFFF;"/>
										<input type="hidden" name="collection.is_codeless" id="is_codeless" value=1  tabindex="2" style="width: 51% background-color: #FFFFF;"/>
										<input type="hidden" name="collection.scroll_no" id="scroll_no" value=0  tabindex="2" style="width: 51% background-color: #FFFFF;"/>
									</div>
						</div>
						
						<div class="row-fluid" >
							<div class="span12">
										<label style="width: 20%">Customer Name </label>
										<input type="text" name="collection.customer_name" id="customer_name" tabindex="2" style="width: 76% background-color: #FFFFF;"/>
										
									</div>
						</div>
							
							<div class="row-fluid" id="common_address_row">							
					<div class="span12">									    
						<label style="width: 20%">Address</label>
						<textarea rows="1" style="width: 76%" name="collection.address" id="address"></textarea>
					</div>
				</div>
						</div>		
															
								<!-- End -->
							</div>
						<div >
							
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
															<%-- 	<button class="btn btn-beoro-3" type="button" id="btn_multi_delete" onclick="multiDeleteModal()" disabled="disabled" >
																	<span class="splashy-box_remove"></span>
																	  	Multi Delete
																</button> --%>	
											    </div>
												<div class="alert alert-error" style="width: 88%;float: left;margin-top: 7px;">
													<strong>Total Bank Deposit :</strong> <span id="total_collection" style="font-size:20px;color:blue;font-weight:bold;"></span> 
												</div>
								
									
							                  </div>	
										</td>
									    <td width="5%"></td>
									    <td width="60%" align="left">
												    		<button class='btn btn-beoro-3' type='button' id='btn_next' onclick="showShortCuts()"><span class='splashy-sprocket_dark'></span></button>
													    	<button class="btn btn-beoro-3" type="button" id="btn_save_codeless" onclick="saveAdvancedCollection(1)" ><span class="splashy-document_letter_okay"></span>Save </br>Codeless</br>Collection</button> 
													    	<button class="btn btn-beoro-3" type="button" id="btn_save_advance" onclick="saveAdvancedCollection(2)" disabled="disabled" ><span class="splashy-document_letter_okay"></span>Save Collection</button>
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

<!--  
<div class="month_help">
        <div class="w-box">
			 <div class="w-box-header">
				<h4>Deposit History</h4>
			 </div>
			<div class="w-box-content cnt_a" style="padding: 3px;">
				<div class="row-fluid">
	            	<div class="span12" id="depositListTbl">
						
					</div>
				</div>
			</div>
		</div>
</div>
  	
  
  
	

--> 
   
 </div>
<p style="clear: both;margin-top: 5px;clear: both;"></p>

<div class="collection-list" id="collection_grid_div"> 
	<table id="collection_grid"></table>
	<div id="collection_grid_pager" ></div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/codelessCollection.js"></script>


<!-- 
<script type="text/javascript">
var customer_id=$("#customer_id").val();
var actionUrl=sBase+"getSecurityAndOtherDepositList.action?customer_id="+'020100720';
$("#depositListTbl").html(jsImg.SETTING).load(actionUrl);      
</script>
 -->




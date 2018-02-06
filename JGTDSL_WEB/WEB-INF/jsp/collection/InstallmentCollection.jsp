<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("installmentCollectionHome.action");
	setTitle("Installment Collection Information");
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
.row-fluid + .row-fluid{
	margin-top: 0 !important;
}
.collection-form{
	float: left;
	width: 99%;
	height: 52%;
}
#billCollectionForm{
	margin-bottom:1px;
}
.tabbed-div{
	float: left;
	width: 99%;
	height: 46%;
}
.customerInfo{
background: #CCFFCC;
padding: 4px;
padding-left: 8px;
}
.installmentInfo{
background: #FFFFCC;
padding: 4px;
padding-left: 8px;
}
.collectionInfo{
background: #99FFFF;
padding: 4px;
padding-left: 8px;
}

.textInput{
text-align: left;
font-weight: bold;
width: 51%;;
}

.numberInput{
text-align: right;
font-weight: bold;
width: 51%;;
}
</style>

<div class="collection-form">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Installment Collection Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
     				<form id="billCollectionForm" name="billCollectionForm">
     				
     				    <input type="hidden" name="collectionDetailStr" id="collectionDetailStr" />
     					<div class="row-fluid">
     						<div class="span12">	
	     						<fieldset>
									<legend align="center">Installment Invoice Number</legend>
									<div class="row-fluid">
										<div class="span12" style="text-align: center;">									    											
											<input type="text" class="textInput" name="collection.installmentId" id="installmentId"  style="width: 200px;border: 2px dotted red;text-align: center;" onblur="getInstallmentCollectionInfo(this.value)"/>  
										</div>
									</div>
								</fieldset>
							</div>
     					</div>
     				
				
     					<div class="row-fluid" style="margin-top: 5px;">
							<div class="span4 customerInfo">									    
								<label style="width: 40%">Customer Code</label>
								<input type="text" class="textInput" id="customer_id" name="collection.customerId" disabled="disabled" />  
							</div>
						  	<div class="span4 installmentInfo">									    
								<label style="width: 40%">Installment Id</label>
								<input type="text" class="textInput" id="installment_id" disabled="disabled" />  				
							</div>
							<div class="span4 collectionInfo">									    
								<label style="width: 40%">Collection Date</label>
								<input type="text" tabindex="999" class="textInput" id="collection_date" name="collection.collectionDate" readonly="readonly"/>  				
							</div>
						</div>
						<div class="row-fluid">
							<div class="span4 customerInfo">									    
								<label style="width: 40%">Customer Name</label>
								<input type="text" class="textInput" id="customer_name" disabled="disabled" />  
							</div>		
							<div class="span4 installmentInfo">									    
								<label style="width: 40%">Serial</label>
								<input type="text" class="textInput" id="installment_serial" disabled="disabled" />  				
							</div>	
							<div class="span4 collectionInfo">									    
								<label style="width: 40%">Bank</label>
								<select id="bank_id" id="bank_id" name="collection.bankId"  style="width: 54.5%;"   onchange="fetchSelectBox(branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(branch_sbox)">
											<option value="" selected="selected">Select Bank</option>
											<s:iterator value="%{#session.USER_BANK_LIST}">
												<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
										</s:iterator>
										</select>  				
							</div>			  	
						</div>	
						<div class="row-fluid">
							<div class="span4 customerInfo">									    
								<label style="width: 40%">Customer Type</label>
								<input type="text" class="textInput" id="customer_type" disabled="disabled" />  
							</div>		
							<div class="span4 installmentInfo">									    
								<label style="width: 40%">Month, Year</label>
								<input type="text" class="textInput" id="installment_month_year" disabled="disabled" />  				
							</div>	
							<div class="span4 collectionInfo">									    
								<label style="width: 40%">Branch</label>
								<select id="branch_id" name="collection.branchId" style="width: 54.5%;"  onchange="fetchSelectBox(account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
									<option value="" selected="selected">Select Branch</option>
								</select>  				
							</div>				  	
						</div>	
						<div class="row-fluid">
							<div class="span4 customerInfo">									    
								<label style="width: 40%">Customer Category</label>
								<input type="text" class="textInput" id="customer_category" disabled="disabled" />  
							</div>		
							<div class="span4 installmentInfo">									    
								<label style="width: 40%">Principal</label>
								<input type="text" class="numberInput" id="installment_principal" disabled="disabled" />  				
							</div>
							<div class="span4 collectionInfo">									    
								<label style="width: 40%">Account</label>
								<select id="account_id" name="collection.accountNo" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
										<option value="" selected="selected">Select Account</option>
								</select>   				
							</div>					  	
						</div>	
						<div class="row-fluid">
							<div class="span4 customerInfo">									    
								<label style="width: 40%">Is Metered?</label>
								<input type="text" class="textInput" id="is_metered_name" disabled="disabled" />  
							</div>			
							<div class="span4 installmentInfo">									    
								<label style="width: 40%">Surcharge</label>
								<input type="text" class="numberInput" id="installment_surcharge" disabled="disabled" />  				
							</div>	
							<div class="span4 collectionInfo">									    
								<label style="width: 40%">Collected Amount</label>
								<input type="text" class="numberInput" id="collected_amount" name="collection.collectedAmount" onblur="showCollectionDetailTab()"/>  				
							</div>			  	
						</div>	
						<div class="row-fluid">
							<div class="span4 customerInfo">									    
								<label style="width: 40%">Mobile</label>
								<input type="text" class="textInput" id="mobile" disabled="disabled" />  
							</div>				
							<div class="span4 installmentInfo">									    
								<label style="width: 40%">Meter-Rent</label>
								<input type="text" class="numberInput" id="installment_meter_rent" disabled="disabled" />  				
							</div>
							<div class="span4 collectionInfo">									    
								<label style="width: 40%">Tax Amount</label>
								<input type="text" class="numberInput" id="tax_amount" name="collection.taxAmount" onblur="showCollectionDetailTab()"/>  				
							</div>		  	
						</div>	
						<div class="row-fluid">
							<div class="span4 customerInfo">									    
								<label style="width: 40%">Phone</label>
								<input type="text" class="textInput" id="phone" disabled="disabled" />  
							</div>				
							<div class="span4 installmentInfo">									    
								<label style="width: 40%">Total</label>
								<input type="text" class="numberInput" id="installment_total" disabled="disabled" />  				
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
						    	</td>
						    	<td width="5%"></td>
						    	<td width="60%" align="right">
							    	<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveInstallmentCollection()" id="btn_save">
							    		<span class="splashy-document_letter_okay"></span>
							    		Save Collection
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


<p style="clear: both;margin-top: 5px;clear: both;"></p>

<div class="row-fluid tabbed-div" id="collection_grid_div">
		 <div id="tabbed-nav" style="height: 100%">
            <ul>
                <li><a>Installment Segments</a></li>
                <li><a>Collection List</a></li>
            </ul>
	         <div>
		         <div style="text-align: center;">
						<table id="installment_segment_table" class="hor-minimalist-b" style="border:1px solid #C0C0C0;width: 80%;margin-left: 10px;margin-top: 10px;" align="center">  
					       	<thead>  
					        	<tr>  
					            	<th scope="col" width="10%" style="text-align:center;">Bill Id</th>
					            	<th scope="col" width="15%" style="text-align:left;">Month, Year</th>  
					                <th scope="col"  width="19%" style="text-align:right;">Principal</th> 
					                <th scope="col"  width="19%" style="text-align:right;">Surcharge</th>  
					                <th scope="col"  width="19%" style="text-align:right;">Meter-Rent</th>
					                <th scope="col"  width="19%" style="text-align:right;">Total</th>   
					            </tr>  
					        </thead> 
					        <tbody>  
					        </tbody>
					    </table>
		         </div>
		         
		         <div style="text-align: center;">
						<table id="installment_collection_dtl_table" class="hor-minimalist-b" style="border:1px solid #C0C0C0;width: 80%;margin-left: 10px;margin-top: 10px;" align="center">  
					       	<thead>  
					        	<tr>  
					            	<th scope="col" width="10%" style="text-align:center;">Bill Id</th>
					            	<th scope="col" width="15%" style="text-align:left;">Month, Year</th>  
					                <th scope="col"  width="15%" style="text-align:right;">Principal</th> 
					                <th scope="col"  width="15%" style="text-align:right;">Surcharge</th>  
					                <th scope="col"  width="15%" style="text-align:right;">Meter-Rent</th>
					                <th scope="col"  width="15%" style="text-align:right;">Tax</th>
					                <th scope="col"  width="15%" style="text-align:right;">Total</th>   
					            </tr>  
					        </thead> 
					        <tbody>  
					        </tbody>
					    </table>
		         </div>
		         
		         <div><!-- 
		                <table id="installment_history_this_grid"></table>
						<div id="installment_history_this_grid_pager" ></div> -->
		         </div>		         
			</div>
		</div>	
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/installmentCollection.js"></script>

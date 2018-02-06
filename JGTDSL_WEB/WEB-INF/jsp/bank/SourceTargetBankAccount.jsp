<%@ taglib prefix="s" uri="/struts-tags"%>

<form id="bankForm" name="bankForm">	
<div class="w-box">
	<div class="w-box-header">
 				<h4 id="rightSpan_caption">Bank Deposit/Withdraw</h4>
	</div>
	<div class="w-box-content" style="padding: 10px;" id="content_div">		
		<fieldset>
			<legend>Source Bank Info</legend>
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Bank Name</label>
								<select id="source_bank_id" name="bankDepositWithdraw.source_bank_id" style="width: 64%" onchange="fetchSelectBox(source_branch_sbox)">
						                <option value="" selected="selected">Select Bank</option>
							        <s:iterator value="%{#session.USER_BANK_LIST}">
										<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
									</s:iterator>       
						        </select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Branch Name</label>
						        <select id="source_branch_id" name="bankDepositWithdraw.source_branch_id" style="width: 64%" onchange="fetchSelectBox(source_account_sbox)">  
						                <option value="" selected="selected">Select Branch</option>     
						        </select>								      
							</div>						  	
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 14.5%">Account Name</label> 
						        <select id="source_account_no"  name="bankDepositWithdraw.source_account_no" style="width: 82.5%">  
						                <option value="" selected="selected">Select Account</option>    
						        </select>								
						  	</div>						  						  
						</div>
						
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Dated</label>
								<input type="text" style="width: 59%" id="source_transaction_date" name="bankDepositWithdraw.source_transaction_date"/>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Trans. Type</label>
						        <select id="transaction_type" name="bankDepositWithdraw.transaction_type_str" style="width: 64%" onchange="changeTransactionType(this.value)">  
						               <option value="">Select Type</option>
							        	<s:iterator  value="%{#application.TRANSACTION_TYPE}">
							             <s:if test='%{id!=0 && id!=1}'>   
							   				<option value="<s:property value='id'/>"><s:property value="label"/></option>
							   			 </s:if>
									    </s:iterator>            
									     
						        </select>								      
							</div>						  	
						</div>
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Amount</label>
								<input type="text" id="transaction_amount" name="bankDepositWithdraw.transaction_amount" style="width: 59%" />
						  	</div>
						  	<div class="span6">								
								<label style="width: 30%">Purpose</label>
						        <select id="receive_particulars" name="bankDepositWithdraw.payment_particulars" style="width: 64%;display:none;" onchange="updateParticulars('receive_particulars')">  
						                <option value="">Select Purpose</option> 
						                <s:iterator value="%{#application.BANK_RECEIVE_PARTICULARS}">
											<option value="<s:property value='id'/>"><s:property value="label" /></option>
										</s:iterator>    							        
						        </select>					      
						        <select id="payment_particulars" name="bankDepositWithdraw.payment_particulars" style="width: 64%;display:none;" onchange="updateParticulars('payment_particulars')">  
						                <option value="">Select Purpose</option> 
						                <s:iterator value="%{#application.BANK_PAYMENT_PARTICULARS}">
											<option value="<s:property value='id'/>"><s:property value="label" /></option>
										</s:iterator>    							        
						        </select>
						        <select id="margin_payment_particulars" name="bankDepositWithdraw.payment_particulars" style="width: 64%;display:none;" onchange="updateParticulars('margin_payment_particulars')">  
						                <option value="">Select Purpose</option> 
						                <s:iterator value="%{#application.BANK_MARGIN_PAYMENT_PARTICULARS}">
											<option value="<s:property value='id'/>"><s:property value="label" /></option>
										</s:iterator>    							        
						        </select>
							</div>						  	
						</div>
						
						<div class="row-fluid" id="month_year_div" style="display:none;">							
							<div class="span6">									    
								<label style="width: 30%">Month<m class='man'/></label>
								<select name="bankDepositWithdraw.collection_month" id="collection_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6">
								<label style="width: 30%">Year<m class='man'/></label>
								<select name="bankDepositWithdraw.collection_year" id="collection_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 14.5%">Particulars</label>
								<input type="text" id="source_transaction_particulars" name="bankDepositWithdraw.source_transaction_particulars" style="width: 81%" />
						  	</div>						  						  
						</div>
					
		</fieldset>
		<p style="text-align: right;padding-right: 20px;color: blue;">
			<input type="checkbox" id="transfer_checkbox" onclick="toggleCheckBox()"/>
			Transfer to another A/C
		</p>
		<fieldset id="transfer_account_div" style="position:relative;">
			<legend>Target Bank Info</legend>			
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Bank Name</label>
								<select id="target_bank_id" name="bankDepositWithdraw.target_bank_id" style="width: 64%" onchange="fetchSelectBox(target_branch_sbox)">
						                <option value="" selected="selected">Select Bank</option>
							        <s:iterator value="%{#application.ALL_BANK}">
										<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
									</s:iterator>       
						        </select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Branch Name</label>
						        <select  id="target_branch_id" name="bankDepositWithdraw.target_branch_id"  style="width: 64%" onchange="fetchSelectBox(target_account_sbox)">  
						                <option value="" selected="selected">Select Branch</option>     
						        </select>								      
							</div>						  	
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 14.5%">Account Name</label> 
						        <select id="target_account_no"  name="bankDepositWithdraw.target_account_no"  style="width: 82.5%">  
						                <option value="" selected="selected">Select Account</option>    
						        </select>								
						  	</div>						  						  
						</div>
						
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Dated</label>
								<input type="text" style="width: 59%" id="target_transaction_date" name="bankDepositWithdraw.target_transaction_date"/>
						  	</div>
						  	<div class="span6">									    
															      
							</div>						  	
						</div>
						
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 14.5%">Particulars</label>
								<textarea rows="1" style="width: 81%" id="target_transaction_particulars" name="bankDepositWithdraw.target_transaction_particulars"></textarea>
						  	</div>						  						  
						</div>
	
				 		<div class="overlay">
				        	<div class="txt">Target Bank Account</div>
				    	</div>
		</fieldset>	
						
						<div class="formSep" style="padding-top: 15px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
							
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
									<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>
									Close</button>
						    	</td>

						    	<td width="5%">
						    		
						    	</td>
						    	<td width="40%" align="right">
						    		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveTransaction()"><span class="splashy-document_letter_okay"></span>
								    Save</button>
								    <button class="btn btn-beoro-3" type="button" id="btn_reset" onclick="resetTransactionForm()"><span class="splashy-document_letter_okay"></span>
								    Reset</button>															    
						    	</td>
						    </tr>
						    </table>											
						</div>	
										
	</div>
</div>
</form>			
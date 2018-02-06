<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="row-fluid">
   <div class="span3"></div>
   <div class="span6">
       <div class="w-box">
           <div class="w-box-header"><h4>Customer & Collection Information</h4></div>
           <div class="w-box-content cnt_a">
              			<div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Customer Id</label>
								<input type="text" onblur="checkInput(this.id)" id="z_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 24%;margin-top: -4px;"  tabindex="1" name="bankAccountCorrection.customer_id"/>
								<input type="text" name="" id="customer_id_z" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 24%;margin-top: -5px;"/>						
						  	</div>
						 </div>	

						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Customer Name</label>
								<input type="text" style="width: 51%"  id="z_full_name" disabled="disabled"/>
						  	</div>
						 </div>
						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Billing Month</label>
								<select id="z_bill_month" style="width: 53.2%;" onchange="fetchZCollectionInfo()" name="bankAccountCorrection.bill_month">
								       	<option value="">Select Month</option>           
								        <s:iterator  value="%{#application.MONTHS}">   
								   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
										</s:iterator>
							    </select>
						  	</div>
						 </div>	
						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Billing Year</label>
								<select id="z_bill_year" style="width: 53.2%;" onchange="fetchZCollectionInfo()" name="bankAccountCorrection.bill_year">
								       	<option value="">Select Year</option>
								       	<s:iterator  value="%{#application.YEARS}">
								            <option value="<s:property/>"><s:property/></option>
										</s:iterator>
							    </select>
						  	</div>
						 </div>
						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Collection Date</label>
								<input type="text" style="width: 51%"  id="z_collection_date" disabled="disabled"/>
						  	</div>
						 </div>	
						  <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Collection Amount</label>
								<input type="text" style="width: 51%"  id="z_collection_amount" disabled="disabled"/>
						  	</div>
						 </div>				  	
                   </div>
               </div>
           </div>
    <div class="span3"></div>
</div>

<div id="aDiv" style="height: 10px;"></div>
<div class="row-fluid">
   <div class="span1"></div>	
   <div class="span4">
   		<div class="w-box">
			<div class="w-box-header">
 				<h4 id="rightSpan_caption">Old Bank Information</h4>
			</div>
			<div class="w-box-content" style="padding: 10px;" id="content_div">		
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Bank Name</label>
								<input type="text" style="width: 51%"  id="z_collection_bank" disabled="disabled"/>
						  	</div>
						 </div>	
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Branch Name</label>
								<input type="text" style="width: 51%"  id="z_collection_branch" disabled="disabled"/>								
						  	</div>
						 </div>
						  	<div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%">Account Name</label>
								<input type="text" style="width: 51%"  id="z_collection_account" disabled="disabled"/>
						  	</div>
					   </div>							  	
			</div>
		</div>
   </div>
   <div class="span2" style="font-size: 150px;line-height: 100px;text-align: center;padding: 20px 0;font-weight: 700;font-style: normal;">
   	&#10165;
   </div>
   <div class="span4">
   		<div class="w-box">
			<div class="w-box-header">
 				<h4 id="rightSpan_caption">New Bank Information</h4>
			</div>
			<div class="w-box-content" style="padding: 10px;" id="content_div">		
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Bank Name</label>
								<select id="z_bank_id" style="width: 54%" onchange="fetchSelectBox(z_branch_sbox)" name="bankAccountCorrection.new_bank_id">
						                <option value="" selected="selected">Select Bank</option>
							        <s:iterator value="%{#session.USER_BANK_LIST}">
										<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
									</s:iterator>       
						        </select>
						  	</div>
						</div>	
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Branch Name</label>	
								<select id="z_branch_id" style="width: 54%" onchange="fetchSelectBox(z_account_sbox)" name="bankAccountCorrection.new_branch_id">  
						                <option value="" selected="selected">Select Branch</option>     
						        </select>						
						  	</div>
						 </div>
						<div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%">Account Name</label>
								 <select id="z_account_no"  style="width:54%" name="bankAccountCorrection.new_account_no">  
						                <option value="" selected="selected">Select Account</option>    
						        </select>	
						  	</div>
					   </div>	
				  						  
						</div>
			</div>
		</div>
   <div class="span1"></div>
</div>

<div id="aDiv" style="height: 10px;"></div>

<div class="row-fluid">
   <div class="span3"></div>
   <div class="span6">
       <div class="w-box">
           <div class="w-box-header"><h4>Narration</h4></div>
           <div class="w-box-content cnt_a" style="text-align: center;">
              			<div class="row-fluid">
						  	<div class="span12">
								<textarea rows="3" style="width: 70%;" id="z_narration" name="bankAccountCorrection.narration"></textarea>
						  	</div>
						 </div>	
						 <div class="formSep">
							<div id="aDiv" style="height: 5px;"></div>
						  </div>
						  <div class="formSep sepH_b">    
							    <span id="wait_div" style="float: left;margin-top: 6px;font-size: 13px;color: red;padding-left: 15px;"></span>							    							    
							    <button class="btn btn-danger" onclick="callAction('blankPage.action')">Cancel</button> 						
							    <button class="btn btn-beoro-3" type="button" onclick="resetBankAccountForm()" id="btn_save">Reset</button>
							    <button class="btn btn-beoro-3" type="button" onclick="saveBankAccountCorrection()" id="btn_save">Save Correction Info.</button>		      
						  </div>
						 		  	
                   </div>
               </div>
           </div>
    <div class="span3"></div>
</div>
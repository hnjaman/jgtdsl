<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="row-fluid">
   <div class="span1"></div>	
   <div class="span4">
   		<div class="w-box">
			<div class="w-box-header">
 				<h4 id="rightSpan_caption">Old Collection Information</h4>
			</div>
			<div class="w-box-content" style="padding: 10px;" id="content_div">		
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Customer Id</label>
								<input type="text" onblur="checkInput(this.id)" id="old_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 15.3%;margin-top: -4px;"  tabindex="1"  name="customerAccountCorrection.old_customer_id"/>
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 12%;margin-top: -5px;"/>
						
						  	</div>
						</div>	
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Customer Name</label>	
								<input type="text" style="width: 51%"  id="old_full_name" disabled="disabled"/>							
						  	</div>
						 </div>
						<div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%">Billing Month</label>
								<select id="bill_month" style="width: 54%;" onchange="setMonth()" name="customerAccountCorrection.bill_month">
								       	<option value="">Select Month</option>           
								        <s:iterator  value="%{#application.MONTHS}">   
								   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
										</s:iterator>
							    </select>
						  	</div>
					   </div>	
					   <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%">Billing Year</label>
								<select id="bill_year" style="width: 54%;" onchange="setYear()" name="customerAccountCorrection.bill_year">
								       	<option value="">Select Year</option>
								       	<s:iterator  value="%{#application.YEARS}">
								            <option value="<s:property/>"><s:property/></option>
										</s:iterator>
							    </select>
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
 				<h4 id="rightSpan_caption">New Collection Information</h4>
			</div>
			<div class="w-box-content" style="padding: 10px;" id="content_div">		
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Customer Id</label>
								<input type="text" onblur="checkInput(this.id)" id="new_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 15.3%;margin-top: -4px;"  tabindex="1" name="customerAccountCorrection.new_customer_id"/>
								<input type="text" name="" id="customer_id_y" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 12%;margin-top: -5px;"/>
						
						  	</div>
						 </div>	
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 40%">Customer Name</label>
								<input type="text" style="width: 51%"  id="new_full_name" disabled="disabled"/>									
						  	</div>
						 </div>
						  	<div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%">Billing Month</label>
								
								<select id="new_bill_month" style="width: 54%;" name="customerAccountCorrection.new_bill_month">
								       	<option value="">Select Month</option>           
								        <s:iterator  value="%{#application.MONTHS}">   
								   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
										</s:iterator>
							    </select>
						  	</div>
					   </div>	
					   <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%">Billing Year</label>
								<select id="new_bill_year" style="width: 54%;"  name="customerAccountCorrection.new_bill_year">
								       	<option value="">Select Year</option>
								       	<s:iterator  value="%{#application.YEARS}">
								            <option value="<s:property/>"><s:property/></option>
										</s:iterator>
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
           <div class="w-box-header"><h4>Collection Information</h4></div>
           <div class="w-box-content cnt_a" style="text-align: center;">
              			<div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Bank</label>
								<input type="text" style="width: 51%"  id="collection_bank" disabled="disabled"/>
						  	</div>
						 </div>	

						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Branch</label>
								<input type="text" style="width: 51%"  id="collection_branch" disabled="disabled"/>
						  	</div>
						 </div>
						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Account</label>
								<input type="text" style="width: 51%"  id="collection_account" disabled="disabled"/>
						  	</div>
						 </div>	
						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Amount</label>
								<input type="text" style="width: 51%"  id="collection_amount" disabled="disabled"/>
						  	</div>
						 </div>
						 <div class="row-fluid">
						  	<div class="span12">
								<label style="width: 40%;padding-left: 10px;">Collection Date</label>
								<input type="text" style="width: 51%"  id="collection_date" disabled="disabled"/>
						  	</div>
						 </div>				  	
                   </div>
               </div>
           </div>
    <div class="span3"></div>
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
								<textarea rows="3" style="width: 70%;" id="narration"></textarea>
						  	</div>
						 </div>	
						 <div class="formSep">
							<div id="aDiv" style="height: 5px;"></div>
						  </div>
						  <div class="formSep sepH_b">    
							    <span id="wait_div" style="float: left;margin-top: 6px;font-size: 13px;color: red;padding-left: 15px;"></span>							    							    
							    <button class="btn btn-danger" onclick="callAction('blankPage.action')">Cancel</button> 						
							    <button class="btn btn-beoro-3" type="button" onclick="resetCustomerAccountForm()" id="btn_reset">Reset</button>
							    <button class="btn btn-beoro-3" type="button" onclick="saveCustomerCorrection()" id="btn_save">Save Correction Info.</button>		      
						  </div>
						 		  	
                   </div>
               </div>
           </div>
    <div class="span3"></div>
</div>
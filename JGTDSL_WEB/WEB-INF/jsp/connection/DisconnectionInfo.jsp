<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="w-box">
				<div class="w-box-header">
		  				<h4 id="rightSpan_caption">Disconnection Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">						
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Cause</label>								
								<select name="disconn.disconnect_cause_str" id="disconnect_cause_str" style="width: 64%">
						                <option value="">Disconnect Cause</option>
							        <s:iterator  value="%{#application.DISCONN_CAUSE}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
						        </select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Type</label>
						        <select name="disconn.disconnect_type_str" id="disconnect_type_str" style="width: 64%">  
						                <option value="">Disconnect Type</option>           
							        <s:iterator  value="%{#application.DISCONN_TYPE}" id="abc">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
						        </select>								      
							</div>						  	
						</div>
						<div class="row-fluid">
							 <div class="span6">
								<label style="width: 30%">Disconn. By</label>
						        <select data-placeholder="Choose an employee..." class="chosen-select" multiple name="disconn.disconnect_by" id="disconnect_by" style="width: 64%;display:none;">        
						        	<s:iterator value="%{#application.ALL_EMPLOYEE}" id="empList">
						            	<option value="<s:property value="emp_id" />" >
						                	<s:property value="full_name" />
						                </option>
									</s:iterator>
								</select>
						  	</div> 
						  	<div class="span6">									    
								<label style="width: 30%">Remarks</label>
								<input type="text" style="width: 59%" name="disconn.remarks" id="disconnect_remarks"/>	
							</div>						  	
						</div>
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Disconn. Date</label>
						        <input type="text" style="width: 59%" id="disconnect_date" name="disconn.disconnect_date"/>
								<input type="hidden" name="disconn.customer_id" id="disconnect_customer_id" />	
								<input type="hidden" name="disconn.pid" id="pid" />						      
						  	</div>
						  	<div class="span6">									    

							</div>						  	
						</div>
						<div id="non_meter_disconn_button_div"> 
						</div>
				</div>
			</div>
<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="meterReconnForm" name="meterReconnForm">
<div class="w-box">
	<div class="w-box-header">
 				<h4 id="rightSpan_caption">Disconnection-Reconnection Information</h4>
	</div>
	<div class="w-box-content" style="padding: 10px;" id="content_div">		
		<fieldset>
			<legend>Disconnection Info</legend>
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Cause</label>
								<select id="disconnect_cause_str" style="width: 64%" disabled="disabled">
						                <option value="">Disconnect Cause</option>
							        <s:iterator  value="%{#application.DISCONN_CAUSE}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
						        </select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Type</label>
						        <select id="disconnect_type_str" style="width: 64%" disabled="disabled">  
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
						        <select data-placeholder="Choose an employee..." class="chosen-select" multiple name="disconn.disconnect_by" id="disconnect_by" style="width: 64%;display:none;" disabled="disabled">        
						        	<s:iterator value="%{#application.ALL_EMPLOYEE}" id="empList">
						            	<option value="<s:property value="emp_id" />" >
						                	<s:property value="full_name" />
						                </option>
									</s:iterator>
								</select>
								
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Disconn. Date</label>
								<input type="text" style="width: 59%" name="disconn.disconnect_date" id="disconnect_date" disabled="disabled"/>							      
							</div>						  	
						</div>
						
						<div class="row-fluid">							
						  	<div class="span12">									    
								<label style="width: 14.5%">Remarks</label>
								<textarea rows="1" style="width: 80%" id="disconnect_remarks" disabled="disabled"></textarea> 							      
							</div>						  	
						</div>
						<input type="hidden" name="reconn.disconnect_id" id="disconn_id"/>
						<input type="hidden" name="disconn.customer_id" id="disconn_customer_id"/>
						
						
 
		</fieldset>
		<p></p>
		<fieldset>
			<legend>Reconnection Info</legend>			
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Reconn. By</label>
						        <select data-placeholder="Choose an employee..." class="chosen-select" multiple name="reconn.reconnect_by" id="reconnect_by" style="width: 64%;display:none;">        
						        	<s:iterator value="%{#application.ALL_EMPLOYEE}" id="empList">
						            	<option value="<s:property value="emp_id" />" >
						                	<s:property value="full_name" />
						                </option>
									</s:iterator>
								</select>
						  	</div>
						  	<div class="span6">
								<label style="width: 30%">Reconn. Date</label>
						        <input type="text" style="width: 59%" id="reconnect_date" name="reconn.reconnect_date"/>
						  	</div>					  	
						</div>
						
						<div class="row-fluid">							
						  	<div class="span12">									    
								<label style="width: 14.5%">Remarks</label>
								<textarea rows="1" style="width: 80%" id="reconn_remarks"  name="reconn.remarks"  disabled="disabled"></textarea> 							      
							</div>						  	
						</div>
						<input type="hidden" name="reconn.pid" id="reconn_id"/>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
							
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
									<button class="btn btn-beoro-3" type="button" id="btn_add" onclick="addButtonPressed()"><span class="splashy-add_small"></span>
									Add</button>
								    <button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="editButtonPressed()" disabled="disabled"><span class="splashy-application_windows_edit"></span>
								    Edit</button>
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveReconnInfo()" disabled="disabled"><span class="splashy-document_letter_okay"></span>
								    Save</button>
							    	<button class="btn btn-beoro-3"  type="button" id="btn_cancel" onclick="cancelMeterInfo()"><span class="splashy-error"></span>
							    	Cancel</button>
						    	</td>

						    	<td width="5%">
						    		
						    	</td>
						    	<td width="40%" align="right">
						    		<button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="$dialog.dialog('open');" disabled="disabled"><span class="splashy-gem_remove"></span>
						    		Delete</button>
									<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>
									Close</button>
						    	
						    	</td>
						    </tr>
						    </table>											
						</div>
 
		</fieldset>		
										
	</div>
</div>
</form>			
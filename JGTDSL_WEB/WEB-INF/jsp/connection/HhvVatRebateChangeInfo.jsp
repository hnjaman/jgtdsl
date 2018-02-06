<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="w-box">
				<div class="w-box-header">
		  				<h4 id="rightSpan_caption">Change Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">						
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 28%">Change Type</label>								
								<select name="lpChange.change_type_str" id="change_type_str" style="width: 71%" onchange="controlChangeType(this.value)"> 
						                <option value="">Select Change Type</option>
						                <option value="0">Hhv/nhv Change</option>
						                <option value="1">Vat Rebate Change</option>
						                <option value="2"  selected="selected">Hhv/nhv  and Vat Rebate Change</option>
						        </select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 28%">Change Date</label>
						        <input type="text" style="width: 66%" id="effective_date" name="lpChange.effective_date"/>							      
							</div>						  	
						</div>
						
						<div class="row-fluid" id="hhv_change_row">
							<div class="span6">
								<label style="width: 28%">Old HHV</label>
						        <input type="text" style="width: 66%" name="lpChange.old_hhv" id="old_hhv"  readonly="readonly"/>	
													      
						  	</div>
						  	<div class="span6">
								<label style="width: 28%">New HHV</label>
						        <input type="text" style="width: 66%" name="lpChange.new_hhv" id="new_hhv"/>					      
						  	</div>						  	
						</div>
						<div class="row-fluid" id="vat_rebate_change_row">
							<div class="span6">
								<label style="width: 28%">Old Vat Rebate</label>
						        <input type="text" style="width: 66%" name="lpChange.old_vat_rebate" id="old_vat_rebate"  readonly="readonly"/>	
													      
						  	</div>
						  	<div class="span6">
								<label style="width: 28%">New Vat Rebate</label>
						        <input type="text" style="width: 66%" name="lpChange.new_vat_rebate" id="new_vat_rebate"/>					      
						  	</div>						  	
						</div>
					
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 28%">Changed By</label>
						        <select data-placeholder="Choose an employee..." class="chosen-select" multiple name="lpChange.change_by" id="change_by" style="width: 71%;display:none;">        
						        	<s:iterator value="%{#application.ALL_EMPLOYEE}" id="empList">
						            	<option value="<s:property value="emp_id" />" >
						                	<s:property value="full_name" />
						                </option>
									</s:iterator>
								</select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 28%">Remarks</label>
								<input type="text" style="width: 66%" name="lpChange.remarks" id="lp_remarks"/>	
								<input type="hidden" name="lpChange.customer_id" id="lpChange_customer_id" />	
								<input type="hidden" name="lpChange.pid" id="pid" />	
							</div>						  	
						</div>
				</div>
			</div>
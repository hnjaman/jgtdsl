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
						                <option value="0">Load Change</option>
						                <option value="1">Pressure Change</option>
						                <option value="2"  selected="selected">Load and Pressure Change</option>
						        </select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 28%">Change Date</label>
						        <input type="text" style="width: 66%" id="effective_date" name="lpChange.effective_date"/>							      
							</div>						  	
						</div>
						
						<div class="row-fluid" id="pressure_change_row">
							<div class="span6">
								<label style="width: 28%">Old Pressure</label>
						        <input type="text" style="width: 66%" name="lpChange.old_pressure" id="old_pressure"  readonly="readonly"/>	
													      
						  	</div>
						  	<div class="span6">
								<label style="width: 28%">New Pressure</label>
						        <input type="text" style="width: 66%" name="lpChange.new_pressure" id="new_pressure"/>					      
						  	</div>						  	
						</div>
						<div class="row-fluid"  id="load_change_row">
							<div class="span6">
								<label style="width: 28%">Old Load</label>
						        <input type="text" name="lpChange.old_min_load" id="old_min_load" style="width: 18%" readonly="readonly" value="<s:property value='customer.connectionInfo.min_load' />"/>
									<input type="text" style="width: 6%;color: green;" value="mn" disabled="disabled"/>
								<input type="text" name="lpChange.old_max_load" id="old_max_load" style="width: 17%" readonly="readonly" value="<s:property value='customer.connectionInfo.max_load' />"/>
									<input type="text" style="width: 7%;color: red;" value="mx" disabled="disabled" />	
													      
						  	</div>
						  	<div class="span6">
								<label style="width: 28%">New Load</label>						     
						         <input type="text" name="lpChange.new_min_load" id="new_min_load" style="width: 18%" />
									<input type="text"  style="width: 6%;color: green;" value="mn" disabled="disabled"/>
								<input type="text" name="lpChange.new_max_load" id="new_max_load" style="width: 17%" />
									<input type="text" style="width: 7%;color: red;" value="mx" disabled="disabled" />				      
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
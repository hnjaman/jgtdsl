<%@ taglib prefix="s" uri="/struts-tags"%>
	<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Meter Information(New)</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
     				<form id="replaceMeterForm" name="replaceMeterForm" style="margin-bottom: 1px;">						
						<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Meter(Serial) No</label>
        <input type="text" name="newMeter.meter_sl_no" id="new_meter_sl_no" style="width: 51%" maxlength="30" tabindex="101" disabled="disabled"/>
        
    </div>
    <div class="span6">
        <label style="width: 40%">Manufacturer</label>
        <select name="newMeter.meter_mfg" id="new_meter_mfg" style="width: 56%" tabindex="102" disabled="disabled">
            <option value="" selected="selected">Select Manufacturer</option>
            <s:iterator value="mfgList">
                <option value="<s:property value="mfg_id" />" >
                    <s:property value="mfg_name" />
                </option>
            </s:iterator>
        </select>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Year</label>
        <select name="newMeter.meter_year" id="new_meter_year" style="width: 56%" tabindex="102" disabled="disabled">
            <option value="" selected="selected">Select Year</option>
   	        <s:iterator  value="%{#application.YEARS}" id="year">
	            <option value="<s:property/>"><s:property/></option>
			</s:iterator>
        </select>
    </div>
    <div class="span6">
        <label style="width: 40%">Measurement</label>
        <select name="newMeter.measurement_type_str" id="new_measurement_type_str" style="width: 56%" onchange="showHideEvc('new_',this.value)" disabled="disabled">
    		<option value="">Select Measurement Type</option>
	    	<s:iterator  value="%{#application.METER_MEASUREMENT_TYPE}">   
	   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
			</s:iterator>
		</select>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Meter Model</label> <!-- Meter Type to Meter Model -->
        <select name="newMeter.meter_type" id="new_meter_type" style="width: 56%" disabled="disabled">
    		<option value="">Select Measurement Type</option>
	    	<s:iterator  value="meterTypeList">   
	   			<option value="<s:property value='type_id'/>"><s:property value="type_name"/></option>
			</s:iterator>
		</select>
    </div>
    
    
    <div class="span6">
						<label style="width: 40%">Unit</label>
						<input type="radio" name="newMeter.unit" value="CM" checked> CM
						<input type="radio" name="newMeter.unit" value="FT"> CFT<br>	
					</div>
   <!--  <div class="span6">
        <label style="width: 40%">G-Rating</label>
        <select name="newMeter.g_rating" id="new_g_rating" style="width: 56%" disabled="disabled">
    		<option value="">Select G-Rating</option>
	    	<s:iterator  value="gRatingList">   
	   			<option value="<s:property value='rating_id'/>"><s:property value="rating_name"/></option>
			</s:iterator>
		</select> 
    </div>          -->  
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Conn. Size</label>
        <select name="newMeter.conn_size" id="new_conn_size" style="width: 56%" disabled="disabled">
    		<option value="">Select Connection Size</option>
			<s:iterator  value="%{#application.CONNECTION_SIZE}" id="year">
	            <option value="<s:property/>"><s:property/></option>
			</s:iterator>
        </select>
    </div>
    <div class="span6">
        <label style="width: 40%">Max Reading</label>
        <input type="text" name="newMeter.max_reading" id="new_max_reading" style="width: 51%;" maxlength="15" tabindex="106" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Ini. Reading</label>
        <input type="text" name="newMeter.ini_reading" id="new_ini_reading" style="width: 51%" tabindex="107" disabled="disabled"/>
    </div>
    <div class="span6">
        <label style="width: 40%">Pressure Factor</label>
        <input type="text" name="newMeter.pressure" id="new_pressure" style="width: 51%" maxlength="5" tabindex="108" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Temperature</label>
        <input type="text" name="newMeter.temperature" id="new_temperature" style="width: 51%" tabindex="107" disabled="disabled"/>
    </div>
    <div class="span6">
        <label style="width: 40%">Meter Rent</label>
        <input type="text" name="newMeter.meter_rent" id="new_meter_rent" style="width: 51%" maxlength="5" tabindex="108" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid" id="new_evc_div" style="display: none;">
	<div class="row-fluid">
		<div class="span6">
			<label style="width: 40%">EVC Serial
				<m class='man' />
			</label>
			<input type="text" name="newMeter.evc_sl_no" id="new_evc_sl_no" style="width: 51%;" maxlength="30" tabindex="105" /> 
		</div>
		<div class="span6">
			<label style="width: 40%">EVC Model
				<m class='man' />
			</label>
			<select name="newMeter.evc_model" id="new_evc_model" style="width: 55%" tabindex="102">
				<option value="" selected="selected">Select EVC Model</option>
				<s:iterator value="evcModelList">
					<option value="<s:property value="model_id" />">
						<s:property value="model_name" />
					</option>
				</s:iterator>
			</select>

		</div>
	</div>

	<div class="row-fluid">
		<div class="span6">
			<label style="width: 40%">EVC Year<m class='man' /></label>
			<select name="newMeter.evc_year" id="new_evc_year" style="width: 56%" tabindex="102">
				<option value="" selected="selected">Select Year</option>
				<s:iterator value="%{#application.YEARS}" id="year">
					<option value="<s:property/>">
						<s:property />
					</option>
				</s:iterator>
			</select>
		</div>
		<div class="span6">
			<label style="width: 30%"></label>
		</div>
	</div>
</div>
<div class="row-fluid">   
     <div class="span6">
        <label style="width: 40%">Installed By</label>
        <select data-placeholder="Choose an employee..." class="chosen-select" multiple name="newMeter.installed_by" id="new_installed_by" style="width: 64%;display:none;" disabled="disabled">        
        	<s:iterator value="%{#application.ALL_EMPLOYEE}" id="empList">
            	<option value="<s:property value="emp_id" />" >
                	<s:property value="full_name" />
                </option>
			</s:iterator>
		</select>
    </div>
    <div class="span6">
        <label style="width: 40%">Installed On</label>
        <input type="text" name="newMeter.installed_date" id="new_installed_date" style="width: 51%" maxlength="5" tabindex="108" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span12">
        <label style="width: 19.5%">Comments</label>
        <input type="text" name="newMeter.remarks" id="new_meter_remarks" style="width: 76%" disabled="disabled"/>
        <input type="hidden" name="oldMeter.meter_id" id="meter_id" value="" />
        <input type="hidden" name="newMeter.customer_id" id="customer_id" value="" />
        <input type="hidden" id="pid"/>
    </div>
</div>
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
							
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
									<button class="btn btn-beoro-3" type="button" id="btn_add" onclick="addButtonPressed()" disabled="disabled"><span class="splashy-add_small"></span>
									Replace</button>
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveMeterReplacementInfo()" disabled="disabled"><span class="splashy-document_letter_okay"></span>
								    Save</button>
							    	<button class="btn btn-beoro-3"  type="button" id="btn_cancel" onclick="cancelButtonPressed()"><span class="splashy-error"></span>
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
					</form>																	
				</div>
			</div>	
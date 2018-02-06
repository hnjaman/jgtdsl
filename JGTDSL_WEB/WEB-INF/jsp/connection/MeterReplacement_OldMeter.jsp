<%@ taglib prefix="s" uri="/struts-tags"%>
	<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Meter Information(Old)</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">						
						<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Meter(Serial) No</label>
        <input type="text" id="meter_sl_no" style="width: 51%" disabled="disabled"/>
        
    </div>
    <div class="span6">
        <label style="width: 40%">Manufacturer</label>
        <select  id="meter_mfg" style="width: 56%" disabled="disabled">
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
        <select id="meter_year" style="width: 56%" disabled="disabled">
            <option value="" selected="selected">Select Year</option>
   	        <s:iterator  value="%{#application.YEARS}" id="year">
	            <option value="<s:property/>"><s:property/></option>
			</s:iterator>
        </select>
    </div>
    <div class="span6">
        <label style="width: 40%">Measurement</label>
        <select id="measurement_type_str" style="width: 56%" onchange="showHideEvc('',this.value)" disabled="disabled">
    		<option value="">Select Measurement Type</option>
	    	<s:iterator  value="%{#application.METER_MEASUREMENT_TYPE}">   
	   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
			</s:iterator>
		</select>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Meter Type</label>
        <select id="meter_type" style="width: 56%" disabled="disabled">
    		<option value="">Select Measurement Type</option>
	    	<s:iterator  value="meterTypeList">   
	   			<option value="<s:property value='type_id'/>"><s:property value="type_name"/></option>
			</s:iterator>
		</select>
    </div>
    <div class="span6">
        <label style="width: 40%">G-Rating</label>
        <select id="g_rating" style="width: 56%" disabled="disabled">
    		<option value="">Select G-Rating</option>
	    	<s:iterator  value="gRatingList">   
	   			<option value="<s:property value='rating_id'/>"><s:property value="rating_name"/></option>
			</s:iterator>
		</select> 
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Conn. Size</label>
        <select id="conn_size" style="width: 56%" disabled="disabled">
    		<option value="">Select Connection Size</option>
			<s:iterator  value="%{#application.CONNECTION_SIZE}" id="year">
	            <option value="<s:property/>"><s:property/></option>
			</s:iterator>
        </select>
    </div>
    <div class="span6">
        <label style="width: 40%">Max Reading</label>
        <input type="text" id="max_reading" style="width: 51%;" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Ini. Reading</label>
        <input type="text" id="ini_reading" style="width: 51%" disabled="disabled"/>
    </div>
    <div class="span6">
        <label style="width: 40%">Pressure Factor</label>
        <input type="text" id="pressure" style="width: 51%" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span6">
        <label style="width: 40%">Temperature</label>
        <input type="text" id="temperature" style="width: 51%" disabled="disabled"/>
    </div>
    <div class="span6">
        <label style="width: 40%">Meter Rent</label>
        <input type="text" id="meter_rent" style="width: 51%" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid" id="evc_div" style="display: none;">
	<div class="row-fluid">
		<div class="span6">
			<label style="width: 40%">EVC Serial
				<m class='man' />
			</label>
			<input type="text" name="meter.evc_sl_no" id="evc_sl_no" style="width: 51%;" maxlength="5" tabindex="105"  disabled="disabled"/>
		</div>
		<div class="span6">
			<label style="width: 40%">EVC Model
				<m class='man' />
			</label>
			<select name="meter.evc_model" id="evc_model" style="width: 55%" tabindex="102" disabled="disabled">
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
			<select name="meter.evc_year" id="evc_year" style="width: 56%" tabindex="102" disabled="disabled">
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
   <!--  <div class="span6">
        <label style="width: 40%">Installed By</label>
        <input type="text" id="installed_by_str" style="width: 51%" disabled="disabled"/>
    </div> -->
    <div class="span6">
        <label style="width: 40%">Installed On</label>
        <input type="text" id="installed_date" style="width: 51%" disabled="disabled"/>
    </div>           
</div>
<div class="row-fluid">   
    <div class="span12">
        <label style="width: 19.5%">Comments</label>
        <input type="text" id="meter_remarks" style="width: 76%" disabled="disabled" />
        <input type="hidden" name="meter_id"  value="" />
    </div>
</div>
						
						
																	
				</div>
			</div>	
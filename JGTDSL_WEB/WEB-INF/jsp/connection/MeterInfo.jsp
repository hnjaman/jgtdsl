<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="span12" id="rightSpan">
	<div class="w-box">
		<div class="w-box-header">
			<h4 id="rightSpan_caption">
				Meter Information
			</h4>
		</div>
		<div class="w-box-content" style="padding: 10px;" id="content_div">
			<form id="meterForm" name="meterForm" style="margin-bottom: 1px;">
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">Meter(Serial) No</label>
						<input type="text" name="meter.meter_sl_no" id="meter_sl_no" style="width: 51%" maxlength="30" tabindex="101" />
					</div>
					
					<div class="span6">
						<label style="width: 40%">Manufacturer</label>
						<select name="meter.meter_mfg" id="meter_mfg" style="width: 56%" tabindex="102">
							<option value="" selected="selected">Select Manufacturer </option>
							<s:iterator value="mfgList">
								<option value="<s:property value="mfg_id" />">
									<s:property value="mfg_name" />
								</option>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">Year</label>
						<select name="meter.meter_year" id="meter_year" style="width: 56%" tabindex="102">
							<option value="" selected="selected">Select Year</option>
							<s:iterator value="%{#application.YEARS}" id="year">
								<option value="<s:property/>">
									<s:property />
								</option>
							</s:iterator>
						</select>
					</div>
					<div class="span6">
						<label style="width: 40%">Measurement</label>
						<select name="meter.measurement_type_str" id="measurement_type_str" style="width: 56%" onchange="showHideEvc(this.value)">
							<option value="">Select Measurement Type</option>
							<s:iterator value="%{#application.METER_MEASUREMENT_TYPE}">
								<option value="<s:property value='id'/>">
									<s:property value="label" />
								</option>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">Meter Type</label>
						<select name="meter.meter_type" id="meter_type" style="width: 56%">
							<option value="">Select Meter Type</option>
							<s:iterator value="meterTypeList">
								<option value="<s:property value='type_id'/>">
									<s:property value="type_name" />
								</option>
							</s:iterator>
						</select>
					</div>
					<div class="span6">
						<label style="width: 40%">G-Rating</label>
						<select name="meter.g_rating" id="g_rating" style="width: 56%">
							<option value="">Select G-Rating</option>
							<s:iterator value="gRatingList">
								<option value="<s:property value='rating_id'/>">
									<s:property value="rating_name" />
								</option>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">Conn. Size(inch)</label>
						<select name="meter.conn_size" id="conn_size" style="width: 56%">
							<option value="">Select Connection Size</option>
							<s:iterator value="%{#application.CONNECTION_SIZE}" id="year">
								<option value="<s:property/>">
									<s:property />
								</option>
							</s:iterator>
						</select>
					</div>
					<div class="span6">
						<label style="width: 40%">Max Reading</label>
						<input type="text" name="meter.max_reading" id="max_reading" style="width: 51%;" maxlength="15" tabindex="106" />
					</div>
				</div>
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">Ini. Reading</label>
						<input type="text" name="meter.ini_reading" id="ini_reading" style="width: 51%" tabindex="107" />
					</div>
					<div class="span6">
						<label style="width: 40%">Pressure Factor</label>
						<input type="text" name="meter.pressure" id="pressure" style="width: 51%" maxlength="5" tabindex="108" />
					</div>
				</div>
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">Temperature</label>
						<input type="text" name="meter.temperature" id="temperature" style="width: 51%" tabindex="107" />
					</div>
					<div class="span6">
						<label style="width: 40%">Meter Rent</label>
						<input type="text" name="meter.meter_rent" id="meter_rent" style="width: 51%" maxlength="20" tabindex="108" />
					</div>
				</div>
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">VAT Rebate</label>
						<input type="text" name="meter.vat_rebate" id="vat_rebate" style="width: 51%" maxlength="20" tabindex="108" />
					</div>
					
					<div class="span6">
						<label style="width: 40%">Unit</label>
						<input type="radio" name="meter.unit" value="CM"> CM
						<input type="radio" name="meter.unit" value="FT"> CFT<br>	
					</div>
			
					
					
				</div>
				<div class="row-fluid" id="evc_div" style="display: none;">
					<div class="row-fluid">
						<div class="span6">
							<label style="width: 40%">EVC Serial
								<m class='man' />
							</label>
							<input type="text" name="meter.evc_sl_no" id="evc_sl_no" style="width: 51%;" maxlength="50" tabindex="105" />
						</div>
						<div class="span6">
							<label style="width: 40%">EVC Model
								<m class='man' />
							</label>
							<select name="meter.evc_model" id="evc_model" style="width: 55%" tabindex="102">
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
							<select name="meter.evc_year" id="evc_year" style="width: 56%" tabindex="102">
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
						<label style="width: 40%">Installed By
							<m class='man' />
						</label>
						<select class="chosen-select" multiple name="meter.installed_by" id="installed_by" style="width: 51%"
							tabindex="109">
																			
							<s:iterator  value="%{#application.USERROLE}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
							</s:iterator>
							
							
						</select>

					</div> 
					<div class="span6">
						<label style="width: 40%">Install Date
							<m class='man' />
						</label>
						<input type="text" name="meter.installed_date" id="installed_date" style="width: 51%; height: 26px;" tabindex="107" />
					</div>
				</div>

				<div class="row-fluid">
					<div class="span12">
						<label style="width: 19.5%">Comments</label>
						<textarea rows="1" style="width: 76%; height: 26px;" name="meter.remarks" id="meter_remarks"></textarea>
						<input type="hidden" name="meter.meter_id" id="meter_id" />
						<input type="hidden" name="meter.customer_id" id="customer_id" />						
					</div>
				</div>

				<div class="formSep" style="padding-top: 2px; padding-bottom: 2px;">
					<div id="aDiv" style="height: 0px;"></div>
				</div>

				<div class="formSep sepH_b"
					style="padding-top: 3px; margin-bottom: 0px; padding-bottom: 2px;">
					<table width="100%">
						<tr>
							<td width="55%" align="left">
								<button class="btn btn-beoro-3" type="button" id="btn_add" onclick="addButtonPressed()" disabled="disabled">
									<span class="splashy-add_small"></span> Add
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="editButtonPressed()" disabled="disabled">
									<span class="splashy-application_windows_edit"></span> Edit
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveMeter()" disabled="disabled">
									<span class="splashy-document_letter_okay"></span> Save
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_cancel" onclick="cancelButtonPressed()">
									<span class="splashy-error"></span> Cancel
								</button>
							</td>

							<td width="5%">
								<button class="btn btn-primary" type="button" id="btn_cancel" onclick="loadNewMeterTestData()">
									T
								</button>
							</td>
							<td width="40%" align="right">
								<button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="$dialog.dialog('open');"
									disabled="disabled">
									<span class="splashy-gem_remove"></span> Delete
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_close" onclick="callAction('blankPage.action');">
									<i class="splashy-folder_classic_remove"></i> Close
								</button>

							</td>
						</tr>
					</table>

				</div>
			</form>
		</div>
	</div>
</div>
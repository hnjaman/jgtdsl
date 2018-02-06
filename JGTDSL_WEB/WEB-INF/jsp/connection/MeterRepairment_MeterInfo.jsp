<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="span12" id="rightSpan">
	<div class="w-box">
		<div class="w-box-header">
			<h4 id="rightSpan_caption">
				Meter Information
			</h4>
		</div>
		<div class="w-box-content" style="padding: 10px;" id="content_div">

			<div class="row-fluid">
				<div class="span6">
					<label style="width: 40%">
						Meter(Serial) No
					</label>
					<input type="text" name="meter.meter_sl_no" id="meter_sl_no"
						style="width: 51%" maxlength="10" tabindex="101"
						disabled="disabled" />

				</div>
				<div class="span6">
					<label style="width: 40%">
						Meter Rent
					</label>
					<input type="text" name="meter.meter_rent" id="meter_rent"
						style="width: 51%" maxlength="5" tabindex="108"
						disabled="disabled" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<label style="width: 40%">
						Year
					</label>
					<select name="meter.meter_year" id="meter_year" style="width: 56%"
						tabindex="102" disabled="disabled">
						<option value="" selected="selected">
							Select Year
						</option>
						<s:iterator value="%{#application.YEARS}" id="year">
							<option value="<s:property/>">
								<s:property />
							</option>
						</s:iterator>
					</select>
				</div>
				<div class="span6">
					<label style="width: 40%">
						Measurement
					</label>
					<select name="meter.measurement_type_str" id="measurement_type_str"
						style="width: 56%" onchange="showHideEvc(this.value)"
						disabled="disabled">
						<option value="">
							Select Measurement Type
						</option>
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
					<label style="width: 40%">
						Conn. Size
					</label>
					<select name="meter.conn_size" id="conn_size" style="width: 56%"
						disabled="disabled">
						<option value="">
							Select Connection Size
						</option>
						<s:iterator value="%{#application.CONNECTION_SIZE}">
							<option value="<s:property/>">
								<s:property />
							</option>
						</s:iterator>
					</select>
				</div>
				<div class="span6">
					<label style="width: 40%">
						Max Reading
					</label>
					<input type="text" name="meter.max_reading" id="max_reading"
						style="width: 51%;" maxlength="5" tabindex="106"
						disabled="disabled" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<label style="width: 40%">
						Ini. Reading
					</label>
					<input type="text" name="meter.ini_reading" id="ini_reading"
						style="width: 51%" tabindex="107" disabled="disabled" />
				</div>
				<div class="span6">
					<label style="width: 40%">
						Pressure
					</label>
					<input type="text" name="meter.pressure" id="pressure"
						style="width: 51%" maxlength="5" tabindex="108"
						disabled="disabled" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<label style="width: 40%">
						Temperature
					</label>
					<input type="text" name="meter.temperature" id="temperature"
						style="width: 51%" tabindex="107" disabled="disabled" />
				</div>
				<div class="span6">
				</div>
			</div>


		</div>
	</div>
</div>
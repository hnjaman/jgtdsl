<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="span12" id="rightSpan">
	<div class="w-box">
		<div class="w-box-header">
			<h4 id="rightSpan_caption">
				Repairment Information
			</h4>
		</div>
		<div class="w-box-content" style="padding: 10px;" id="content_div">
			<form id="repairmentForm" name="repairmentForm"
				style="margin-bottom: 1px;">
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">
							Prev. Reading
						</label>
						<input type="text" name="repair.prev_reading" id="prev_reading"
							style="width: 51%" maxlength="10" tabindex="101"
							disabled="disabled" />

					</div>
					<div class="span6">
						<label style="width: 40%">
							Prev R. Date
						</label>
						<input type="text" name="repair.prev_reading_date"
							id="prev_reading_date" style="width: 51%" maxlength="5"
							tabindex="108" disabled="disabled" />
					</div>

				</div>
				<div class="row-fluid">
					<div class="span6">
						<label style="width: 40%">
							Curr. Reading
						</label>
						<input type="text" name="repair.curr_reading" id="curr_reading"
							style="width: 51%" maxlength="10" tabindex="101"
							disabled="disabled" />

					</div>
					<div class="span6">
						<label style="width: 40%">
							Curr R. Date
						</label>
						<input type="text" name="repair.curr_reading_date"
							id="curr_reading_date" style="width: 51%" maxlength="5"
							tabindex="108" disabled="disabled" />
					</div>

				</div>
				<div class="row-fluid">
					<div class="span12">
						<label style="width: 19.5%">
							Repaired By
						</label>
						<select data-placeholder="Choose an employee..."
							class="chosen-select" multiple name="repair.repaired_by"
							id="repaired_by" style="width: 78.5%; display: none;">
							<s:iterator value="%{#application.ALL_EMPLOYEE}" id="empList">
								<option value="<s:property value="emp_id" />">
									<s:property value="full_name" />
								</option>
							</s:iterator>
						</select>

					</div>

				</div>
				<div class="row-fluid">
					<div class="span12">
						<label style="width: 19.5%">
							Comments
						</label>
						<textarea rows="1" style="width: 76%" id="repairment_remarks"
							name="repair.remarks" disabled="disabled" /></textarea>
						<input type="hidden" name="repair.meter_id" id="meter_id" />
						<input type="hidden" name="repair.meter_id" id="customer_id" />
						<input type="hidden" name="repair.measurement_type_str" id="measurement_type_str" />

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
								<button class="btn btn-beoro-3" type="button" id="btn_add"
									onclick="addButtonPressed()" disabled="disabled">
									<span class="splashy-add_small"></span> Add
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_edit"
									onclick="editButtonPressed()" disabled="disabled">
									<span class="splashy-application_windows_edit"></span> Edit
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_save"
									onclick="saveMeterReplacementInfo()" disabled="disabled">
									<span class="splashy-document_letter_okay"></span> Save
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_cancel"
									onclick="cancelButtonPressed()">
									<span class="splashy-error"></span> Cancel
								</button>
							</td>

							<td width="5%">

							</td>
							<td width="40%" align="right">
								<button class="btn btn-beoro-3" type="button" id="btn_delete"
									onclick="$dialog.dialog('open');"
									disabled="disabled">
									<span class="splashy-gem_remove"></span> Delete
								</button>
								<button class="btn btn-beoro-3" type="button" id="btn_close"
									onclick="callAction('blankPage.action')";>
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

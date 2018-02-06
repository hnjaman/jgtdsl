<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="meterReconnForm" name="meterReconnForm">
<div class="w-box">
	<div class="w-box-header">
 				<h4 id="rightSpan_caption">Meter Rent Chagne Information</h4>
	</div>
	<div class="w-box-content" style="padding: 10px;" id="content_div">		
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Meter No</label>
								<input type="text" style="width: 59%" name="rentChange.meter_sl_no" id="meter_sl_no" disabled="disabled"/>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Effective Date</label>
								<input type="text" style="width: 59%" name="rentChange.effective_date" id="effective_date" disabled="disabled"/>							      
							</div>							  	
						</div>
						
						
						<div class="row-fluid">
							<div class="span6">									    
								<label style="width: 30%">Old Rent</label>
								<input type="text" style="width: 59%" name="rentChange.old_rent" id="old_rent" disabled="disabled"/>							      
							</div>	
						  	<div class="span6">									    
								<label style="width: 30%">New Rent</label>
								<input type="text" style="width: 59%" name="rentChange.new_rent" id="new_rent" disabled="disabled" maxlength="8"/>							      
							</div>						  	
						</div>
						
						<div class="row-fluid">							
						  	<div class="span12">									    
								<label style="width: 14.5%">Remarks</label>
								<textarea rows="1" style="width: 80%" name="rentChange.remarks" id="remarks" disabled="disabled"></textarea> 							      
							</div>						  	
						</div>
						
						<input type="hidden" name="rentChange.pid" id="pid"/>
						<input type="hidden" name="rentChange.customer_id" id="customer_id"/>
						<input type="hidden" name="rentChange.meter_id" id="meter_id"/>
						
						<p></p>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
							
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
									<button class="btn btn-beoro-3" type="button" id="btn_add" onclick="addButtonPressed()" disabled="disabled"><span class="splashy-add_small"></span>
									Add</button>
								    <button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="editButtonPressed()" disabled="disabled"><span class="splashy-application_windows_edit"></span>
								    Edit</button>
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveMeterRentChangeInfo()" disabled="disabled"><span class="splashy-document_letter_okay"></span>
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
										
	</div>
</div>
</form>			
<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="meterReconnForm" name="meterReconnForm">
<div class="w-box">
	<div class="w-box-header">
 				<h4 id="rightSpan_caption">Bank Garantie Expire Date Extention Info</h4>
	</div>
	<div class="w-box-content" style="padding: 10px;" id="content_div">		
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Deposit Id</label>
								<input type="text" style="width: 59%" name="bgChange.deposit_id" id="deposit_id" disabled="disabled"/>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Entry Date</label>
								<input type="text" style="width: 59%" name="bgChange.entry_date" id="entry_date" />							      
							</div>							  	
						</div>
						
						
						<div class="row-fluid">
							<div class="span6">									    
								<label style="width: 30%">Old Expire Date</label>
								<input type="text" style="width: 59%" name="bgChange.old_expire_date" id="old_expire_date" disabled="disabled"/>							      
							</div>	
						  	<div class="span6">									    
								<label style="width: 30%">New Expire Date</label>
								<input type="text" style="width: 59%" name="bgChange.new_expire_date" id="new_expire_date" />							      
							</div>						  	
						</div>
						
						<div class="row-fluid">							
						  	<div class="span12">									    
								<label style="width: 14.5%">Remarks</label>
								<textarea rows="1" style="width: 80%" name="bgChange.remarks_on_bg" id="remarks_on_bg" /> 							      
							</div>						  	
						</div>
						
						<input type="hidden" name="bgChange.pId" id="pid"/>
						<input type="hidden" name="bgChange.customer_id" id="customer_id"/>
						
						
						<p/>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"/>
							
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">									  
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveGankGarantieExpireExtentionInfo()" disabled="disabled"><span class="splashy-document_letter_okay"/>
								    Save</button>
							    	<button class="btn btn-beoro-3" type="button" id="btn_cancel" onclick="cancelButtonPressed()"><span class="splashy-error"/>
							    	Cancel</button>
						    	</td>

						    	<td width="5%">
						    		
						    	</td>
						    	<td width="40%" align="right">
						    		<button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="$dialog.dialog('open');"><span class="splashy-gem_remove"/>
						    		Delete</button>
									<button class="btn btn-beoro-3" type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"/>
									Close</button>
						    	
						    	</td>
						    </tr>
						    </table>											
						</div>
										
	</div>
</div>
</form>			
<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css" media="Screen">
#childSection {
    padding: 0 10px;
    font: 12px Verdana, Arial, sans-serif;
    color: green;
    background-color: #ccc;
    text-transform: uppercase;
}


    </style>
<form id="meterReadingForm" name="meterReadingForm">
<div class="w-box">
	<div class="w-box-header">
 				<h4 id="rightSpan_caption">Reconcilation Info</h4>
	</div>
	<div class="w-box-content" style="padding: 10px;" id="content_div">	
	
		<fieldset>
			
						<div class="row-fluid">
							<table style="width: 100%">
										 	<tr valign="top">
												<th width="23.5%" style="font-weight: bold;">Balance as Per Bank Statement :</th>
												<td width="75%">	
														<div style="width:48%;float:left;">&nbsp;</div>
													<input type="text" style="width: 40%;text-align:right;padding-right:5px;font-weight: bold;" name="balance_bank_statement"  id="balance_bank_statement" /> &nbsp;
												</td>
											</tr>		
											
							</table>
							
					
						  					  	
						</div>
					
						
						
		</fieldset>
		<fieldset>
			<legend>Add</legend>
						<div class="row-fluid">
							
						<table  id="addTable" style="width: 100%">
						 	<tr valign="top">
								
								<td width="75%">
									<div style='width:40%;float:left;padding-left:5px;font-weight:bold;'>Title</div>
									<div style='width:40%;float:left;padding-left:8%;font-weight:bold;'>Amount</div>
									
									<a class="addCF" href="javascript:void(0);" style='margin-left:15px;font-weight: bold;font-weight: bold;font-size: 28px;'>+</a>
								</td>
							</tr>
							
						</table>
						
					
						  					  	
						</div>
					
						
						
		</fieldset>
		<p></p>
		
		<fieldset>
			<legend>Less</legend>			
						<div class="row-fluid">
						
						<table  id="lessTable" style="width: 100%">
						 	<tr valign="top">
								
								<td width="75%">
									<div style='width:40%;float:left;padding-left:5px;font-weight:bold;'>Title</div><legend>Balance As Per Bank Statement</legend>
									<div style='width:40%;float:left;padding-left:8%;font-weight:bold;'>Amount</div>
									
									<a class="lessCF" href="javascript:void(0);" style='margin-left:15px;font-weight: bold;font-size: 28px;'>+</a>
								</td>
							</tr>
							
						</table>
		
	
	
				
						</div>
						
						
		</fieldset>	
		
		<fieldset>
			<legend>Final Amount</legend>	
			<table style="width: 100%">
						 	<tr valign="top">
								<th width="23.5%" style="font-weight: bold;">Total Amount :</th>
								<td width="75%">	
										<div style="width:48%;float:left;">&nbsp;</div>
									<input type="text" style="width: 40%;text-align:right;padding-right:5px;font-weight: bold;" disabled="disabled" id="totalOthersAmount" /> &nbsp;
								</td>
							</tr>			
			</table>
		</fieldset>	
			
		<fieldset>
			<legend>Balance As Per Bank Book</legend>	
			<table style="width: 100%">
							<tr valign="top">
												<th width="23.5%" style="font-weight: bold;">Balance As Per Bank Book :</th>
												<td width="75%">	
													<div style="width:48%;float:left;">&nbsp;</div>
													<input type="text" style="width: 40%;text-align:right;padding-right:5px;font-weight: bold;"  disabled="disabled" id="openingBalance" /> &nbsp;
												</td>
							</tr>		

			</table>
		</fieldset>	
		<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
									<%-- <button class="btn btn-beoro-3" type="button" id="btn_add" onclick="addButtonPressed()" disabled="disabled"><span class="splashy-add_small"></span>
									Add</button>
								    <button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="editButtonPressed()" disabled="disabled"><span class="splashy-application_windows_edit"></span>
								    Edit</button> --%>
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveReconcilationInfoAndPrint()" ><span class="splashy-document_letter_okay"></span>
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



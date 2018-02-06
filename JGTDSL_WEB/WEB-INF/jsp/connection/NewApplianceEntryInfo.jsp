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
<form id="newApplianceInfo" name="newApplianceInfo">
<div class="w-box">
	<div class="w-box-header">
 				<h4 id="rightSpan_caption">New Appliance Entry</h4>
	</div>
	<div class="w-box-content" style="padding: 10px;" id="content_div">	
			<input type="hidden" style="width: 25%" id="appliance_info_customer_id" name="app.customer_id" />
			
	
		<fieldset>
			<legend>New Appliance Info</legend>			
			
	
            <div style="margin-top: 25px">
						<div class="row-fluid">		
						    <div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Appliance Nname</label>
						        <select name="app.applianc_id" id="appliance_list" style="width: 20%;">
			                                <option value="" selected="selected">N/A</option>
			                                <s:iterator value="%{#application.ALL_APPLIANCE}" id="appliance_list">
			                                    <option value="<s:property value="applianc_id" />">
			                                        <s:property value="applianc_name"/>
			                                    </option>
			                                </s:iterator>
	                            </select>
							</div>							
						  	<div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Appliance Qnt</label>
								<input type="text" name="app.applianc_qnt" id="appliance_qnt" style="width: 20%"/> 							      
							</div>						  	
						</div>
			</div>
			

			
			<div style="margin-top: 25px">
						<div class="row-fluid">		
						    <div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Effective From</label>
						        <input type="text" style="width: 30%" id="connection_date" name="app.effective_date" />
							</div>							
						  	<div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Remarks</label>
								<textarea rows="1" style="width: 59%" id="remarks" name="app.remarks" ></textarea> 							      
							</div>						  	
						</div>
			</div>			
						
						
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveNewAppliance()"><span class="splashy-document_letter_okay"></span>
								    Save New Connection</button>
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
 
		</fieldset>						
						
	</div>
</div>
</form>			
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
 				<h4 id="rightSpan_caption">Burner Increase-Decrease</h4>
	</div>
	<div class="w-box-content" style="padding: 10px;" id="content_div">	
			<input type="hidden" style="width: 25%" id="old_double_burner_qnt" name="bqc.old_double_burner_qnt" />
			<input type="hidden" style="width: 25%" id=old_pdisconnected_burner_qnt name="bqc.old_pdisconnected_burner_qnt" />
			<input type="hidden" style="width: 25%" id="old_tdisconnected_burner_qnt" name="bqc.old_tdisconnected_burner_qnt" />
			<input type="hidden" style="width: 25%" id="old_double_burner_qnt_billcal" name="bqc.old_double_burner_qnt_billcal" />
			<input type="hidden" style="width: 25%" id="old_tdisconnected_half_burner_qnt" name="bqc.old_tdisconnected_half_burner_qnt" />
			<input type="hidden" style="width: 25%" id="appliance_id" name="bqc.appliance_id" />
		<div style="margin-left:180px">
			<input type="checkbox" id="isIncraseSelected" >  <font color="blue"> <b>Increase</b></font></input>
	        <input type="checkbox" id="isDisconnSelected" ><font color="red"><b> Disconnection</b></font></input>
	        <input type="checkbox" id="isReconnSelected" ><font color="green"><b> Reconnection</b></font></input>				
                                
		</div>	
		<div class="container">
		
		</div>	
		<fieldset>
			<legend>New Burner Info</legend>			
						<div class="row-fluid">
								  	<div class="span6">									    
										<label style="width: 30%;padding-left: 10px">Double Burner</label>
								        <input type="text" style="width: 25%" id="new_double_burner_qnt" name="bqc.new_double_burner_qnt" disabled="disabled" maxlength="2"/>	
								        <input type="hidden" style="width: 30%" id="new_double_qnt_billcal" name="bqc.new_double_qnt_billcal" disabled="disabled" maxlength="2"/>						      
									</div>
							
						<%-- 	<div class="span4">
							<label style="width: 60%; padding-left: 10px">Management Type:</label>
									<select id="management_type"  style="width: 30%;"   onchange="setManagementType()"name="management_type" ">
									<option value="" selected="selected">Select Type</option>									
										<option value="01"  >Increase</option>
										<option value="02" >Decrease</option>
										<option value="03" >Reconnection</option>											
								   </select> 
							</div> --%>
														  	
						</div>

		<div id="incraseDiv">
			<fieldset>
			<legend id="childSection"><b>Increase</b></legend>
						<div class="row-fluid">
							<div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Increase</label>
						        <input type="text" style="width: 30%" id="new_incrased_burner_qnt" name="bqc.new_incrased_burner_qnt"  onkeyup="calculateDoubleBurner(this.id)" maxlength="2"/>							      
							</div>							  	
						</div>
			</fieldset>
		</div>
		<div id="disconnectionDiv">				
		<fieldset>		
		<legend id="childSection"><b>Disconnection</b></legend>	
						<div class="row-fluid">
						
						  <div class="span4">
							<label style="width: 30%; padding-left: 10px">Type:</label>
									<select id="disconn_type"  style="width: 30%;"   onchange="setDisconnType(this.id);calculateDoubleBurner(this.id)" name="bqc.disconn_type" ">
									<option value="" selected="selected">Select Type</option>									
										<option value="01"  >Permanent</option>
										<option value="02" >Temporary</option>	
										<option value="03" >From Temporary To Permanent</option>									
								   </select> 
							</div>
							<div class="span4">
								<label style="width: 30%;padding-left: 10px">Cause</label>								
								<select name="bqc.new_permanent_disconnected_cause" onchange="setDisconnType(this.id);calculateDoubleBurner(this.id)" id="new_permanent_disconnected_cause"  style="width: 30%">
						                <option value="">Disconnect Cause</option>
							        <s:iterator  value="%{#application.DISCONN_CAUSE_NONMETER}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
						        </select>
						  	</div>
							<div class="span4">									    
								<label style="width: 30%;padding-left: 10px">Qnt.</label>
						        <input type="text" style="width: 25%" id="burner_qnt_disconn" name="" onkeyup="setDisconnType(this.id);calculateDoubleBurner(this.id)"  maxlength="2"/>							      
								<!-- <input type="checkbox" id="isTempToPerDiss" name="bqc.isTempToPerDiss" value="1" ></input> -->
							</div>	
							    <input type="hidden" style="width: 35%" id="new_temporary_disconnected_burner_qnt" name="bqc.new_temporary_disconnected_burner_qnt" onkeyup="calculateDoubleBurner(this.id)" disabled="disabled" maxlength="2"/>							      
						        <input type="hidden" style="width: 35%" id="new_permanent_disconnected_burner_qnt" name="bqc.new_permanent_disconnected_burner_qnt" disabled="disabled" onkeyup="calculateDoubleBurner(this.id)" maxlength="2"/>							      					  	
						</div>
							
											  
						        
							
			</fieldset>	
		</div>	
			

			
			<div id="reconnectionDiv">
			<fieldset>	
			<legend id="childSection"><b>Reconnection</b></legend>		
						<div class="row-fluid">
						    <div class="span4">
							<label style="width: 30%; padding-left: 10px">From:</label>
									<select id="reconn_from" onchange="setReconnFrom(this.id);calculateDoubleBurner(this.id)" style="width: 30%;"   name="reconn_from" ">
									<option value="" selected="selected">Select Type</option>									
										<option value="01"  >Permanent</option>
										<option value="02" >Temporary</option>										
								   </select> 
							</div>
								<div class="span4">
								<label style="width: 30%;padding-left: 10px">Cause</label>								
								<select name="bqc.reconnection_cause" onchange="setReconnFrom(this.id);calculateDoubleBurner(this.id)" id="reconnection_cause" style="width: 30%">
						                <option value="">Select</option>
							        <s:iterator  value="%{#application.DISCONN_CAUSE_NONMETER}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
						        </select>
						  	</div>
							<div class="span4">									    
								<label style="width: 30%; padding-left: 10px">Qnt.</label>
						        <input type="text" style="width: 25%" id="burner_qnt_reconn" name="" onkeyup="setReconnFrom(this.id);calculateDoubleBurner(this.id)"  maxlength="2"/>							      
							</div>	
							<input type="hidden" style="width: 30%" id="new_reconnected_burner_qnt" name="bqc.new_reconnected_burner_qnt" onkeyup="calculateDoubleBurner(this.id)" disabled="disabled" maxlength="2"/>							  
						    <input type="hidden" style="width: 30%" id="new_reconnected_burner_qnt_permanent" name="bqc.new_reconnected_burner_qnt_permanent" onkeyup="calculateDoubleBurner(this.id)" disabled="disabled" maxlength="2"/>							      
						    
							
							
								
							
						  						  	
						</div>
			</fieldset>	
			</div>		
			<div style="margin-top: 25px">
						<div class="row-fluid">		
						    <div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Effective From</label>
						        <input type="text" style="width: 30%" id="effective_date" name="bqc.effective_date"/>
							</div>							
						  	<div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Remarks</label>
								<textarea rows="1" style="width: 59%" id="remarks" name="bqc.remarks" ></textarea> 							      
							</div>						  	
						</div>
			</div>			
						<input type="hidden" name="bqc.pid" id="pid"/>
						<input type="hidden" name="bqc.customer_id" id="customer_id"/>
						<input type="hidden"  id="double_burner_qnt_hidden"/>
						<input type="hidden"  id="double_burner_qnt_billcal_hidden""/>
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
									
								    
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveBurnerQntChange()" ><span class="splashy-document_letter_okay"></span>
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
 
		</fieldset>						
						
	</div>
</div>
</form>			
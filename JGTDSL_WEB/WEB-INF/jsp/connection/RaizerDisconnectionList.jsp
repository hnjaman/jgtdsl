<%@ taglib prefix="s" uri="/struts-tags"%>
 
<table class="table table-bordered">
    <thead>
        <tr>
            <th>SL</th>
            <th>Appliance ID</th>
            <th>Appliance Name</th>
            <th>Disconnected Qnt</th>
            <th>Disconnection Date</th>
        </tr>
    </thead>
    <tbody>
       <s:if test="%{raizerDisconnectionList.size!=0}">
			<s:iterator value="raizerDisconnectionList" status="indx">
		        <tr>
		            <td><s:property value="#indx.count" /></td>
		            <td><s:property value="applianc_id" /></td>
		            <td><s:property value="applianc_name" /></td>
		            <td><s:property value="applianc_qnt" /></td>
		            <td><s:property value="effective_date" /></td>
		        </tr>
			</s:iterator>
		</s:if>
    </tbody>
</table>
<br/><br/>

						<div class="row-fluid">		
						    <div class="span6">									    
								<label style="width: 30%;padding-left: 10px">Effective From</label>
						        <input type="text" style="width: 30%" id="recon_date" name="app.effective_date" />
							</div>							
						  	<div class="span6">									    
								<label style="width: 30%;padding-left: 4px">Remarks</label>
								<textarea rows="1" style="width: 60%" id="remarks" name="app.remarks" ></textarea> 							      
							</div>						  	
						</div>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
								    <button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndReconnectRaizer()"><span class="splashy-document_letter_okay"></span>
								    Reconnect Raizer</button>
							    	<button class="btn btn-beoro-3"  type="button" id="btn_cancel" onclick="cancelButtonPressed()"><span class="splashy-error"></span>
							    	Cancel</button>
						    	</td>

						    	<td width="5%">
						    		
						    	</td>
						    	<td width="40%" align="right">
									<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>
									Close</button>
						    	
						    	</td>
						    </tr>
						    </table>											
						</div>
                        
<script type="text/javascript">
Calendar.setup($.extend(true, {}, calOptions,{
	    inputField : "recon_date",
	    trigger    : "recon_date"}));
</script>
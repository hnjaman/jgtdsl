<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="customerTypeChangeForm" name="customerTypeChangeForm">
<div class="w-box">
	<div class="w-box-content" style="padding: 10px;" id="content_div">		
						<div class="row-fluid">
							<div class="span12">
	                            <label style="width: 15%">Ministry Ref.</label>
	                        
	                             	<select name="customer.connectionInfo.ministry_id" id="ministry_id" style="width: 20%;">
		                                <option value="" selected="selected">N/A</option>
		                                <s:iterator value="%{#application.ALL_MINISTRY}" id="divisionList">
		                                    <option value="<s:property value="ministry_id" />" >
		                                        <s:property value="ministry_name" />
		                                    </option>
		                                </s:iterator>
		                            </select>
	                            
	                        </div>							  	
						</div>
					 <div class="row-fluid">
                        <div class="span12">
                            <label style="width: 15%">
                                Connection Type
                                    <m class='man'/>
                            </label>    
	                            <select name="customer.connectionInfo.connection_type_str" id="connection_type" style="width: 20%" onchange="checkConnectionType(this.value)">
						            <option value="">Select Type</option>           
							        <s:iterator  value="%{#application.CONNECTION_TYPE}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
				        		</select>                           
                        </div>
                    </div>
                    
                    
                    <div class="row-fluid" id="parent_connection_div">
	                        <div class="span12">
	                            <label style="width: 15%">Parent Connection<m class='man'/><font style="font-size: 8px">(cond.)</font></label>                          
	                            <input type="text" id="parent_connection" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 17%;margin-top: -4px;" name="customer.connectionInfo.parent_connection"/>                      
	                        </div>
	                </div>
	                
	                 <div class="row-fluid">
                        <div class="span12">
                        <label style="width: 15%">Metered Status<m class='man'/></label>
                          
                
                        	<select name="customer.connectionInfo.isMetered_str" id="isMetered" style="width: 39%" onchange="changeMeteredStatus(this.value)" readonly>
					            <option value="">Select Status</option>           
						        <s:iterator  value="%{#application.METERED_STATUS}">   
						   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
								</s:iterator>       
				        	</select>

                        </div>
                      </div>
                      
                     <div class="row-fluid" id="max_min_load_div">
                        <div class="span12">
                            <label style="width: 15%">Approved Min/Max Load<m class='man'/><font style="font-size: 8px">(cond.)</font></label>
	                        <input type="text" name="customer.connectionInfo.min_load" id="min_load" style="width: 12%" />     
							<input type="text" style="width: 4%;color: green;" value="min" disabled="disabled"/>
							<input type="text" name="customer.connectionInfo.max_load" id="max_load" style="width: 12%"/>
	          				<input type="text" style="width: 5%;color: red;" value="max" disabled="disabled" />							                       
                        </div>
                    </div>
                     <div class="row-fluid" id="burner_qnt_div">
	                        <div class="span12">
	                            <label style="width: 15%">Burner Quantity<m class='man'/><font style="font-size: 8px">(cond.)</font></label>                           
		                            	<input type="text" name="customer.connectionInfo.single_burner_qnt" id="single_burner_qnt" style="width: 8%" />
										<input type="text" style="width: 7%;color: green;" value="single" disabled="disabled"/>
										<input type="text" name="customer.connectionInfo.double_burner_qnt" id="double_burner_qnt" style="width: 8%"/>
										<input type="text" style="width: 8%;color: red;" value="double" disabled="disabled" />                                               
	                        </div>
	                   </div>
	                   
	                 <div class="row-fluid" id="vat_rebate_div">
                        <div class="span12">
                        <label style="width: 15%">VAT Rebate(%)</label>
                        	<input type="text" name="customer.connectionInfo.vat_rebate" id="vat_rebate" maxlength="2" style="width: 37%"/>        
                        </div>
                    </div>
                    
                     <div class="row-fluid" id="hhv_nhv_div">
                        <div class="span12">
	                        <label style="width: 15%">HHV/NHV Factor</label>
                        	<input type="text" name="customer.connectionInfo.hhv_nhv" id="hhv_nhv" maxlength="8" style="width: 37%"/>    
                        </div>
                    </div>
                    
                    <div class="row-fluid" id="pay_within_div">
                        <div class="span12">
                            <label style="width: 15%">Pay Within(No. of Days)<m class='man'/></label>
                            
	                        <input type="text" name="customer.connectionInfo.pay_within_wo_sc" id="pay_within_wo_sc" value="" style="width: 5%" />
							<input type="text" style="width: 11.5%;color: green;font-size: 12px;" value="Without SC" disabled="disabled"/>
					        <input type="text" name="customer.connectionInfo.pay_within_w_sc" id="pay_within_w_sc" value="" style="width: 5%"/> 
							<input type="text" style="width: 8.5%;color: red;font-size: 12px;" value="With SC" disabled="disabled" />							                       
                        </div>
                     
                    </div>
                    
						
					<div class="row-fluid">		
						<div class="span12">									    
								<label style="width: 15%">Effective Date</label>
								<input type="text" style="width: 59%" name="customer.connectionInfo.type_change_date" id="effective_date" />							      
					   </div>
					</div>   
					
						
						<div class="row-fluid">							
						  	<div class="span12">									    
								<label style="width: 15%">Remarks</label>
								<textarea rows="1" style="width: 80%" name="customer.connectionInfo.type_change_remarks" id="remarks" ></textarea> 							      
							</div>						  	
						</div>
						
						<input type="hidden" id="customer_id" name="customer.customer_id"/>
						
						<p></p>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
							
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	

						    	<td width="5%">
						    		
						    	</td>
						    	<td width="40%" align="right">
						    		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveCustomerTypeChangeInfo()" ><span class="splashy-document_letter_okay"></span>
								    Save</button>
									<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>
									Close</button>
						    	
						    	</td>
						    </tr>
						    </table>											
						</div>
					  			
	</div>
	                    
							
</div>
</form>			
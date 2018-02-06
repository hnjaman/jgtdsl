<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="span12" >
	<div class="w-box">
		<div class="w-box-header">
  				<h4 id="rightSpan_caption">Search Transactions</h4>
		</div>
		<div class="w-box-content" style="padding: 10px;" id="content_div">						
				
					
				<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Bank Name</label>
								<select id="search_bank_id" style="width: 64%" onchange="fetchSelectBox(search_branch_sbox)">
						                <option value="" selected="selected">Select Bank</option>
							        <s:iterator value="%{#session.USER_BANK_LIST}">
										<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
									</s:iterator>       
						        </select>
						  	</div>
						  	<div class="span6">									    
								<label style="width: 30%">Branch Name</label>
						        <select id="search_branch_id" style="width: 64%" onchange="fetchSelectBox(search_account_sbox)">  
						                <option value="" selected="selected">Select Branch</option>     
						        </select>								      
							</div>						  	
				</div>												
				<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">Account Name</label> 
						        <select id="search_account_no" style="width: 64%">  
						                <option value="" selected="selected">Select Account</option>    
						        </select>								
						  	</div>		
						  	<div class="span6">
								<label style="width: 30%">Trans. Type</label> 
							       <select id="search_transaction_type"  style="width: 64%">  
						               <option value="">Select Type</option>
							        	<s:iterator  value="%{#application.TRANSACTION_TYPE}">
							             <s:if test='%{id!=0 && id!=1}'>   
							   				<option value="<s:property value='id'/>"><s:property value="label"/></option>
							   			 </s:if>
									    </s:iterator>            
									     
						        </select>								
						  	</div>					  						  
				</div>
				<div class="row-fluid">
							<div class="span6">
								<label style="width: 30%">From Date</label>
								<input type="text" style="width: 59%" id="search_from_date"/>
						  	</div>
						  	<div class="span6">
								<label style="width: 30%">To Date</label>
								<input type="text" style="width: 59%" id="search_to_date"/>
						  	</div>
			   </div>
				
		
			
				<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
					<div id="aDiv" style="height: 0px;"></div>
				</div>											
		
		<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;margin-top: 10px;">									
							<table width="100%">
						    <tr>
						    	<td width="55%" align="left">
									
						    	</td>

						    	<td width="5%">
						    		
						    	</td>
						    	<td width="40%" align="right" style="padding-right: 20px;">
						    		<button class="btn btn-beoro-3" type="button" id="btn_reset" onclick="resetSearchForm()"><span class="splashy-document_letter_okay"></span>
								    Reset</button>		
								    
						    		<button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="searchTransaction();"><span class="splashy-zoom"></span>
						    		Search</button>
									
						    	
						    	</td>
						    </tr>
						    </table>											
						</div>
						
			</div>
						
	</div>
</div>
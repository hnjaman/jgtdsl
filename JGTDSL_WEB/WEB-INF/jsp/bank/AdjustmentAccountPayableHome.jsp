<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("editCustomerInfoHome.action");
	setTitle("Customer Info Update");
</script>
<style type="text/css">
.span6.extra{
min-height: 33px !important;
}

</style>
<form id="customerInfoForm" name="customerInfoForm">

	<div style="width: 38%;height: 98%;float: left;margin-left: 15px;">			
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">            
               <div class="w-box">
                <div class="w-box-header">
                    <h4>Adjustment(Account Payable)</h4>
                </div>
                <div class="w-box-content cnt_a">
           
                
                		<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Month<m class='man'/></label>
								<select name="adjustmetAccountPayable.month" id="month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6">
								<label style="width: 40%">Year<m class='man'/></label>
								<select name="adjustmetAccountPayable.year" id="year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
               
                    <div class="row-fluid">
                        <div class="span12">
                            <label>BGFCL</label>
                            <input type="text" id="total_bgfcl" name="adjustmetAccountPayable.bgfcl"   style="width: 37%;"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>SGFL</label>
                            <input type="text" id="total_sgfl" name="adjustmetAccountPayable.sgfl"   style="width: 37%;"/>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>PDF</label>
                            <input type="text" id="total_ioc" name="adjustmetAccountPayable.pdf"    style="width: 37%;"/>
                        </div>
                    </div>
                     <div class="row-fluid">
                        <div class="span12">
                            <label>BAPEX</label>
                            <input type="text" id="total_ioc" name="adjustmetAccountPayable.bapex"    style="width: 37%;"/>
                        </div>
                    </div>
                     <div class="row-fluid">
                        <div class="span12">
                            <label>GTCL</label>
                            <input type="text" id="total_ioc" name="adjustmetAccountPayable.trasmission"    style="width: 37%;"/>
                        </div>
                    </div>
                     <div class="row-fluid">
                        <div class="span12">
                            <label>Deficit Wellhead</label>
                            <input type="text" id="total_ioc" name="adjustmetAccountPayable.dwellhead"    style="width: 37%;"/>
                        </div>
                    </div>
                     <div class="row-fluid">
                        <div class="span12">
                            <label>Gas Development Fund</label>
                            <input type="text" id="total_ioc" name="adjustmetAccountPayable.gdfund"    style="width: 37%;"/>
                        </div>
                    </div>
                     <div class="row-fluid">
                        <div class="span12">
                            <label>Asset Value of Gas</label>
                            <input type="text" id="total_ioc" name="adjustmetAccountPayable.avalue"    style="width: 37%;"/>
                        </div>
                    </div>
                                    
                   <div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
								<div id="aDiv" style="height: 0px;"></div>
				  </div>
                  	<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;" id="reading_button_div">	
 							<table width="100%" border="0">
							 	<tr>
							 		<td width="30%" align="left">
							 			
							 				
							 		</td>
							 		<td width="70%" align="right">
							    		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveAdjustmentAccountPayable()" id="btn_save"><span class="splashy-document_letter_okay"></span>Save</button> 
										<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>Close</button>
							 		</td>
							 	</tr>
 							</table>									        									
				 		</div>
                  
                </div>                               
</div>

</div>

</form>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/adjustmetAccountPayable.js"></script>
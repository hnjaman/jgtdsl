<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="gbox_gridTable" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 99%;">
<div id="gview_gridTable" class="ui-jqgrid-view">
	<div class="ui-jqgrid-titlebar ui-jqgrid-caption ui-widget-header ui-corner-top ui-helper-clearfix" style="padding-left: 8px;">
		<span class="ui-jqgrid-title">Select Bank-Branch-Account Info</span>
	</div>
	<div class="ui-jqgrid-bdiv" style="width: 99%;height: 100%;padding: 5px;">
			<div class="row-fluid" style="margin-top: 10px;">
					<div class="span12">
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 20%;padding-left: 10px;">Bank</label>
								<select id="multiColl_bank_id" name="multiColl.bank_id"  style="width: 54.5%;" onchange="fetchSelectBox(multiColl_branch_sbox)">
									<option value="" selected="selected">Select Bank</option>
									<s:iterator value="%{#session.USER_BANK_LIST}">
										<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
								</s:iterator>
								</select>						
						  	</div>
						  	<div class="span6">									    
								<label style="width: 20%">Branch</label>
								<select id="multiColl_branch_id" name="multiColl.branch_id" style="width: 54.5%;" onchange="fetchSelectBox(multiColl_account_sbox)">
									<option value="" selected="selected">Select Branch</option>
								</select>  						
							</div>
						</div>				                                            					                                            
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 20%;padding-left: 10px;">Account</label>
								<select id="multiColl_account_no" name="multiColl.account_no" style="width: 54.5%;">
									<option value="" selected="selected">Select Account</option>
								</select>								
						  	</div>
						  	<div class="span6">
								<label style="width: 20%;">Date</label>
								<input type="text" id="multiColl_collection_date" name="multiColl.collection_date" tabindex="1" style="width: 60%"/>							
						  	</div>
						</div>				                                            					                                            
					</div>
			</div>
	</div>		
</div>
</div>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("transactionAuthorization.action");
	setTitle("Bank Transaction Authorization");
</script>
<div id="top_div">
<fieldset style="width: 98%;margin-left: 10px;">
	<legend>Unauthorized Transactions Summary</legend>
	
	<div style="min-height: 40px;padding: 5px;">
		<s:iterator value="transactionList">
			<div class="span4">				
				<span style="background-color:red;color:white;padding:5px;font-weight:bold;font-size:16px;margin-right:10px;cursor: pointer;" 
				 onclick="showUnauthTransactions('<s:property value="bank_id"/>','<s:property value="branch_id"/>','<s:property value="branch_name"/>','<s:property value="account_no"/>','<s:property value="account_name"/>')">
					[<s:property value="unauth_count"/>]
				</span> 
				<s:property value="bank_name"/>,<s:property value="branch_name"/>,<s:property value="account_no"/> 
			</div>
		</s:iterator>
	</div>
</fieldset>		
<fieldset style="width: 98%;margin-left: 10px;margin-top: 5px;">
	<legend>Search Transaction</legend>
	
	<div class="row-fluid" style="min-height: 70px;">
		<div class="row-fluid">
			<div class="span4">
					<label style="width: 30%">Bank</label>								
					<select id="bank_id" style="width: 64%;" onchange="fetchSelectBox(branch_sbox);clearGridData('transaction_grid');">
		                <option value="" selected="selected">Select Bank</option>
						<s:iterator value="%{#session.USER_BANK_LIST}">
							<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
						</s:iterator>    
		        </select>
		  	</div>
		  	<div class="span4">
					<label style="width: 30%">Branch</label>								
					<select id="branch_id" style="width: 64%;" onchange="fetchSelectBox(account_sbox);clearGridData('transaction_grid');">
		                <option value="" selected="selected">Select Branch</option>       
		        	</select>
		  	</div>
		  	<div class="span4">
					<label style="width: 30%">Account</label>								
					<select id="account_id" style="width: 64%;" onchange="clearGridData('transaction_grid');">
		                <option value="" selected="selected">Select Account</option>    
		        	</select>
		  	</div>				  	
		</div>
		<div class="row-fluid">
			<div class="span4">
					<label style="width: 30%">Trans. Date</label>								
					<input type="text" style="width: 56%" id="transaction_date"/>
					&nbsp;
					<i class="fa fa-eraser" style="cursor: pointer;" onclick="$('#transaction_date').val('');"> </i>
		  	</div>
		  	<div class="span4">
					<label style="width: 30%">Trans. Type</label>								
					<select id="transaction_type" style="width: 64%" onchange="changeTransactionType(this.value)">  
		               <option value="">Select Type</option>
			        	<s:iterator  value="%{#application.TRANSACTION_TYPE}">
			   				<option value="<s:property value='id'/>"><s:property value="label"/></option>
					    </s:iterator>            									     
					</select>
		  	</div>
		  	<div class="span4">
					<label style="width: 30%"></label>								
					<button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="fetchUnAuthorizedTransaction();"><span class="splashy-zoom"></span>
					Search</button>
		  	</div>			  	
		</div>		
	</div>
</fieldset>		
</div>

<div id="gridWrapper" style="margin-top: 10px;">
		<table id="transaction_grid"></table>
		<div id="transaction_grid_pager"></div>
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/jqGridDefault.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/transactionAuthorization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/jqGridCommon.js"></script>		
<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("bankbook.action");
	setTitle("Bank Book");
</script>
<div id="top_div">
	
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
					<div class="row-fluid" id="month_year_div">							
							<div class="span4" id="month_div">									    
								<label style="width: 30%">Billing Month<m class='man'/></label>
								<select name="bill_month" id="billing_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span4" id="year_div">
								<label style="width: 30%">Billing Year<m class='man'/></label>
								<select name="bill_year" id="billing_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
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
						</div>
		<div class="row-fluid">
		 
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

<div style="width: 100%;height: 5%;float: left;margin-left: 10px;">			
		<div style="width: 100%;float: left;margin-top: 0px;">
		<div class="row-fluid">
		 
		  	<div class="span4">
					<label style="width: 30%">Opening Balance<m class='man'/></label>
			        <input type="text" id="opening_balance" name="opening_balance" style="width: 35%;"/>
	       </div>
	       	<div class="span4">
					<label style="width: 30%">Closing Balance<m class='man'/></label>
			        <input type="text" id="closing_balance" name="closing_balance" style="width: 35%;"/>
	       </div>
	
	       <div class="span4">
					<label style="width: 30%">Balance As Bank St<m class='man'/></label>
			        <input type="text" id="balance_statement" name="balance_statement" onkeyup="calculateDifference()" style="width: 35%;"/>
	       </div>
	    </div>
	   <div class="row-fluid">    
	       <div class="span4">
					<label style="width: 30%">Diffence<m class='man'/></label>
			        <input type="text" id="difference" name="difference" style="width: 35%;"/>
	       </div>
	        <div class="span4">
					<label style="width: 30%">Reconcilation<m class='man'/></label>
			        <input type="text" id="reconcilation" name="reconcilation" style="width: 35%;"/>
	       </div>
	       <div class="span4">
					<button class="btn btn-beoro-3" type="button" id="btn_more_reconcilation" onclick="addMoreReconcilation()" >More Reconcilation</button>
		  </div>	
	       <div class="span4">
					<button class="btn btn-beoro-3" type="button" id="btn_confirm" onclick="confirmBankBook()" >Confirm</button>
		  </div>							  	
		</div>	
		
		
		
		    
		</div>
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/jqGridDefault.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/bankbook.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/jqGridCommon.js"></script>		
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/popupModal/ns-window.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/popupModal/css/ns-window.css" rel="stylesheet" />
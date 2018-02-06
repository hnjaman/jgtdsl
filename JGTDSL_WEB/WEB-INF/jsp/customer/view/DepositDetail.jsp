<%@ taglib prefix="s" uri="/struts-tags"%>
		<input type="hidden" id="deposit_id" value="<s:property value='deposit.deposit_id'/>" />
		<div class="row-fluid">
		          <div class="span4">
						<label>Deposit Type</label>
						<b>:</b> <s:property value='deposit.deposit_type.label'/>
				  </div>
				  <div class="span4">
				    <label>From Date</label>
					<b>:</b> <s:property value="deposit.valid_from"/>
				  </div>
				  <div class="span4">
				    <label>To Date</label>
					<b>:</b> <s:property value="deposit.valid_to"/>
				  </div>
		</div>
			
			<div class="row-fluid">
	        	  <div class="span4">
						<label>Bank Name</label>
						<b>:</b> <s:property value="deposit.bank_name"/>
				  </div>
				  <div class="span4">
						<label>Branch Name</label>
						<b>:</b> <s:property value="deposit.branch_name"/>
				  </div>	
				  <div class="span4">
					  <label>Account</label>
					  <b>:</b> <s:property value="deposit.account_name"/>					  
				  </div>								                               
			</div>
			
			<!--
			<s:set var="tFlag" value="false" />
			<s:iterator value="deposit.depositDetail" status="idx">
				<s:if test="#idx.count%3==0">
				   <s:set var="tFlag" value="true" />
				   <s:if test="#idx.count!=1">
				       <div class="span4">
					     <label><s:property value="type_name_eng" /></label>
						 <b>:</b> <s:if test="amount!=0"><s:property value='amount' /></s:if>
				  	   </div>
					   </div>
					   <s:if test="depositTypeList.size!=#idx.count">
					   <div class="row-fluid">
					   </s:if>
				   </s:if>
				</s:if>
				<s:if test="#idx.count%3!=0">
				    <s:set var="tFlag" value="false" />
					<s:if test="#idx.count==1">
					<div class="row-fluid">
					</s:if>
				 <div class="span4">
					  <label><s:property value="type_name_eng" /></label>
					  <b>:</b> <s:if test="amount!=0"><s:property value='amount' /></s:if>
				  </div>
				</s:if>				
			</s:iterator>
			<s:if test="#tFlag==false">
			 	</div>
			</s:if>
			
			  -->
			
			
			<div class="row-fluid">	         	  
                  <div class="span4">
                  	<label style="color: blue;">Dated</label>
                  	<b>:</b> <s:property value="deposit.deposit_date"/>
                  </div>
                  <div class="span4">
						<label style="color: blue;">Deposit Purpose</label>
						<b>:</b> <s:property value='deposit.deposit_purpose.label'/>
				  </div>
                  <div class="span4">
                  	<label style="color: blue;">Total</label>
                  	<b>:</b> <s:property value="deposit.total_deposit"/>
                  </div>							                               
			</div>
			
			<div class="row-fluid" style="margin-top: 0px;">	        	  
                  <div class="span12" style="text-align: right;">
                  	<button class="btn btn-beoro-3" type="button" onclick="$('#depositDetailDiv').html(jsImg.SETTING).load('getNewDepositForm.action');"><span class="splashy-application_windows_okay"></span>&nbsp;&nbsp;Add New</button>
                  	<button class="btn btn-beoro-3" type="button" onclick="$('#depositDetailDiv').html(jsImg.SETTING).load('getDepositFormEditMode.action?deposit_id=<s:property value="deposit.deposit_id"/>');"><span class="splashy-application_windows_edit"></span>&nbsp;&nbsp;Edit</button>                  	
                  	<button class="btn btn-beoro-3" type="button" onclick="deleteDeposit(<s:property value='deposit.deposit_id'/>)"><span class="splashy-gem_remove"></span>&nbsp;&nbsp;Delete</button>                                                    
                  </div>									                               
			</div>
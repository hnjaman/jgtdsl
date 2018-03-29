<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/popupModal/ns-window.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/popupModal/css/ns-window.css" rel="stylesheet" />
<%@ taglib prefix="s" uri="/struts-tags"%>
			<div class="row-fluid">
		          <div class="span4">
						<label>Deposit Type</label>
						<select name="deposit.str_deposit_type" id="deposit_type_id" style="width: 59%" onchange="controlDepositType(this.value)">
				                <option value="">Deposit Type</option>
					        <s:iterator  value="%{#application.DEPOSIT_TYPE}">   
					   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
							</s:iterator>       
				        </select>
				        <input type="hidden" id="deposit_id" name="deposit.deposit_id" value="" />
				  </div>
				  <div class="span4" id="fromDateSpan">
				    <label id="fromDateLabel">From Date</label>
					<input type="text" name="deposit.valid_from" id="valid_from" style="width: 54%" value="<s:property value='deposit.valid_from' />"/>
				  </div>
				  <div class="span4" id="toDateSpan">
				    <label id="toDateLabel">To Date</label>
					<input type="text" name="deposit.valid_to" id="valid_to" style="width: 54%" value="<s:property value='deposit.valid_to' />"/>
				  </div>
			</div>
			<div class="row-fluid" id="account_info_BG">	
							<label style="width: 10%">Remarks On BG</label>						
						  	<div class="span4">									    
								<textarea rows="1" style="width: 80%" name="deposit.remarks_on_bg" id="remarks""></textarea> 							      
							</div>						  	
			</div>
			<div class="row-fluid" id="account_info">
	        	  <div class="span4">
						<label>Bank Name</label>
						<select name="deposit.bank" id="bank_id" style="width: 59%" onchange="fetchSelectBox(branch_sbox)">
				            <option value="" selected="selected">Select Bank</option>
				            <s:iterator  value="%{#session.USER_BANK_LIST}">
				                <option value="<s:property value="bank_id" />" >
				                    <s:property value="bank_name" />
				                </option>
				            </s:iterator>
				        </select>
				  </div>
				  <div class="span4">
						<label>Branch Name</label>
						<select name="deposit.branch" id="branch_id"  style="width: 59%" onchange="fetchSelectBox(account_sbox);" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
                                <option value="" selected="selected">Select Branch</option>
                        </select>
				  </div>	
				  <div class="span4">
					  <label>Account</label>
					  <select name="deposit.account_no" id="account_id" onkeypress="selectvalue.apply(this, arguments)"  style="width: 59%">
                                <option value="" selected="selected">Select Account</option>
                        </select>
				  </div>								                               
			</div>
			 <!--
			<s:set var="tFlag" value="false" />
			<s:iterator value="depositTypeList" status="idx">
				<s:if test="#idx.count%3==0">
				   <s:set var="tFlag" value="true" />
				   <s:if test="#idx.count!=1">
				       <div class="span4">
					     <label><s:property value="type_name_eng" /></label>
					     <input type="hidden" name="deposit.depositDetail[<s:property value='#idx.index' />].type_id" value="<s:property value='type_id' />"/>
					     <input type="text" name="deposit.depositDetail[<s:property value='#idx.index' />].amount" id="item<s:property value='type_id' />" style="width: 54%" class="dAmount"/>
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
 				      <input type="hidden" name="deposit.depositDetail[<s:property value='#idx.index' />].type_id" value="<s:property value='type_id' />"/>
					  <input type="text" name="deposit.depositDetail[<s:property value='#idx.index' />].amount" id="item<s:property value='type_id' />" style="width: 54%" class="dAmount"/>
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
                  	<input type="text" name="deposit.deposit_date" id="deposit_date" style="width: 54%;" value="<s:property value='deposit.deposit_date' />"/>
                  </div>
                  <div class="span4">
						<label style="color: blue;">Deposit Purpose</label>
						<select name="deposit.str_deposit_purpose" id="deposit_purpose_id" style="width: 59%">
				                <option value="">Deposit Type</option>
					        <s:iterator  value="%{#application.DEPOSIT_PURPOSE}">   
					   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
							</s:iterator>       
				        </select>
				  </div>
                  <div class="span4">
                  	<label style="color: blue;">Total</label>
                  	<input type="text" name="deposit.total_deposit" id="total_deposit" style="width: 54%;border: 1px dotted magenta;text-align: right;font-weight: bold;"/>
                  </div>							                               
			</div>
			
			<div class="row-fluid" style="margin-top: 0px;">	        	  
                  <div class="span12" style="text-align: right;">
                  	<button class="btn btn-beoro-3" type="button" onclick="$('#depositDetailDiv').html(jsImg.SETTING).load('getNewDepositForm.action');"><span class="splashy-error"></span>&nbsp;&nbsp;Cancel</button>
                  	<button class="btn btn-beoro-3" type="button" onclick="resetDepositForm()"><span class="splashy-diamonds_3"></span>&nbsp;&nbsp;Reset</button>
                  	<%-- <button class="btn btn-beoro-3" type="button" onclick="totalSecurityDeposit()"><span class="splashy-diamonds_3"></span>&nbsp;&nbsp;Total Security Deposite</button> --%>
                  	<button class="btn btn-beoro-3" type="button" onclick="submitDepositInfo()" id="btn_save"><span class="splashy-add_small"></span>&nbsp;&nbsp;Submit Deposit Info.</button>
                  	<button class="btn btn-beoro-3" type="button" onclick="editDepositInfo()" id="btn_update"><span class="splashy-application_windows_okay"></span>&nbsp;&nbsp;Update Deposit Info.</button>
                  	<!-- <button class="btn btn-beoro-3" type="button" onclick="setTestData()">Test Data</button> -->                  	
                  </div>									                               
			</div>
			
<script type="text/javascript">
var branch_sbox = { targetElm :"branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var account_sbox = { targetElm :"account_id", zeroIndex : 'Select Account', action_name:'fetchAccounts.action',data_key:'branch_id'};

$("#deposit_type_id").val(deposit_id_temp);
if($("#deposit_type_id").val()=="0")
{
$("#fromDateSpan").hide();
$("#toDateSpan").hide();
}
var bank=bank_temp;
$("#bank_id").val(bank);
fetchSelectBox(branch_sbox);
var branch=branch_temp;
$("#branch_id").val(branch);
fetchSelectBox(account_sbox);
$("#account_id").val(account_temp);



	
	$('.dAmount').keyup(calculateTotal);
    var totalItem=<s:property value="depositTypeList.size" />
	Calendar.setup({
        inputField : "valid_from",
        trigger    : "valid_from",
		eventName : "focus",
        onSelect   : function() { this.hide();},
        showTime   : 12,
        dateFormat : "%d-%m-%Y",
		showTime : true
		//onBlur: focusNext
      });
      Calendar.setup({
        inputField : "valid_to",
        trigger    : "valid_to",
		eventName : "focus",
        onSelect   : function() { this.hide();},        
        showTime   : 12,
        dateFormat : "%d-%m-%Y",
		showTime : true
		//onBlur: focusNext		
      });
      Calendar.setup({
        inputField : "deposit_date",
        trigger    : "deposit_date",
		eventName : "focus",
        onSelect   : function() { this.hide();},        
        showTime   : 12,
        dateFormat : "%d-%m-%Y",
		showTime : true
		//onBlur: focusNext		
      });
      
function totalSecurityDeposit()
{
alert($("#customer_id").val());
	$.nsWindow.open({
		movable:true,
		title: 'Multi Month Collection Entry',
		width: 800,
		height: 700,
		dataUrl: 'multiMonthCollection.action?customer_id='+$("#customer_id").val(),
		theme:jsVar.MODAL_THEME
    });
    
}
     
function calculateTotal()
{ 
    var total=0;
    var floatVal;
    $(".dAmount").each(function(){
    	floatVal = parseFloat($(this).val());
            if (!isNaN(floatVal)) {
       			total+=parseFloat(floatVal);         
        	}
	});        
	$("#total_deposit").val(total);         
}

function validateDepositForm(){

  var isValid=true;
  
  if($("#deposit_type_id").val()=='1'||$("#deposit_type_id").val()=='3')/// Bank Guarantee + FDR
  {
   isValid=validateField("deposit_type_id","deposit_date","deposit_purpose_id","total_deposit");
  }else
  {
   isValid=validateField("deposit_type_id","bank_id","branch_id","account_id","deposit_date","deposit_purpose_id","total_deposit");
  }
 
  if(isValid==false)return;
  
  if($("#deposit_type_id").val()!=0){
  	isValid=validateField("valid_from","valid_to");
  }
  
  return isValid;
}
$('form#depositForm').unbind("submit");
$("form#depositForm").submit(function(event){
  event.preventDefault();
  var formData = new FormData($(this)[0]);
 
  $.ajax({
    url: 'saveDeposit.action',
    type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response) {

	  $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, 
		      $.extend(true, {}, jqDialogParam,{
	      			onClose: function () {
		                        if(response.status=="OK") {
			                    	$('.ui-widget-overlay').unbind( "click" );
			                    	var customer_id=$("#customer_id").val();
									var actionUrl=sBase+"getSecurityAndOtherDepositList.action?customer_id="+customer_id;
									$("#depositListTbl").html(jsImg.SETTING).load(actionUrl);	
									resetDepositForm(); 
								}                  	
		                        return true; // allow closing
		             }}));    
    }
    
  });
 
  return false;
});

function submitDepositInfo()
{

 if(validateDepositForm()==true){
 
 $('form#depositForm').submit();
 }
  	
}
function editDepositInfo(){
   if(validateDepositForm()==true){
   	enableField("deposit_type_id");
   	var form = document.getElementById('depositForm');
	var formData = new FormData(form);
   	
    $.ajax({
    url: 'editDeposit.action',
    type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response) {    
    	      $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, 
		      $.extend(true, {}, jqDialogParam,{
	      			onClose: function () {
		                        if(response.status=="OK") {
			                    	$('.ui-widget-overlay').unbind( "click" );
									var actionUrl=sBase+"getSecurityAndOtherDepositList.action?customer_id="+customer_id;
									$("#depositListTbl").html(jsImg.SETTING).load(actionUrl);	
									//Load edit form here....
								}                  	
		                        return true; // allow closing
		             }}));
		    
		    disable("deposit_type_id");    
    }
    });
  }
}

function deleteDeposit(){

  $.ajax({
    url: 'deleteDeposit.action',
    type: 'POST',
    data: {"deposit_id":$("#deposit_id").val()},
    success: function (response) {

      $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, 
      $.extend(true, {}, jqDialogParam,{
      			onClose: function () {
	                        if(response.status=="OK") {
		                    	$('.ui-widget-overlay').unbind( "click" );
								var actionUrl=sBase+"getSecurityAndOtherDepositList.action?customer_id="+customer_id;
								$("#depositListTbl").html(jsImg.SETTING).load(actionUrl);	
								$('#depositDetailDiv').html(jsImg.SETTING).load('getNewDepositForm.action');
							}                  	
	                        return true; // allow closing
	                    }}));
    
    }    
  });  
}
      
function resetDepositForm()
{ 
	$('.dAmount').val(""); 
	$("#total_deposit").val("");  
	//$('select').val("");
}

function controlDepositType(depositType){
 if(depositType=="0"){
 	hideElement("fromDateSpan","toDateSpan","account_info_BG");
 	showElement("account_info");	}
 else{
 	showElement("fromDateSpan","toDateSpan","account_info");
 	hideElement("account_info_BG");
 	}
 	
 if(depositType=="1"||depositType=="3"){
 	hideElement("account_info");
 	showElement("account_info_BG");
		 	if(depositType=="1"){
		 	$("#account_info_BG").find('label').text('Remarks on BG');
		 	}else if(depositType=="3"){
		 	$("#account_info_BG").find('label').text('Remarks on FDR');
		 	}
 	
 }	
}

hideElement("account_info_BG");



</script>	
 <script>
<s:if test="form_mode=='edit'">

	<s:iterator value="deposit.depositDetail">				  	
	    if(<s:property value='amount' />!=0)			 
	        $("#item<s:property value='type_id' />").val(<s:property value='amount' />);
				  
	</s:iterator>
	$("#deposit_purpose_id").val("<s:property value='deposit.deposit_purpose.id' />");
	$("#deposit_type_id").val("<s:property value='deposit.deposit_type.id' />");
	$("#bank_id").val("<s:property value='deposit.bank' />");
	
	addOption("branch_id","<s:property value='deposit.branch' />","<s:property value='deposit.branch_name' />");
	$("#branch_id").val("<s:property value='deposit.branch' />");
	addOption("account_id","<s:property value='deposit.account_no' />","<s:property value='deposit.account_name' />");
	$("#account_id").val("<s:property value='deposit.account_no' />");
	
	$("#btn_update").show();
	$("#btn_save").hide();	
	$("#deposit_id").val("<s:property value='deposit.deposit_id' />");
	
	disableField("deposit_type_id","deposit_purpose_id","deposit_date");
	
	controlDepositType($("#deposit_type_id").val());
</s:if>		
<s:else>
	$("#btn_update").hide();
	$("#btn_save").show();
</s:else>
function setTestData()
{
	$('.dAmount').val("100"); 
	$("#deposit_type_id").val(0);
	$("#deposit_purpose_id").val(0);
	$("#valid_from").val("01-01-2015");
	$("#valid_to").val("01-01-2015");
	$("#deposit_date").val("01-01-2015");
}
</script>	
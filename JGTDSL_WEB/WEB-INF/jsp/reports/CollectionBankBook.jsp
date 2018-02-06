<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("bankBookReportHome.action");
	setTitle("BankBook Information");
</script>
<link href="/JGTDSL_WEB/resources/css/page/meterReading.css" rel="stylesheet" type="text/css" />
<style>
input[type="radio"], input[type="checkbox"]
{
margin-top: -3px !important;
}
.alert{
padding-top: 4px !important;
padding-bottom: 4px !important;
}
.ui-icon, .ui-widget-content .ui-icon {
    cursor: pointer;
}
.sFont{
font-size: 12px;
}


</style>

<div id="customer_meter_div" style="width: 99%;height: 53%;">
	<div id="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="meter-reading"  style="width: 100%;height: 50%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">BankBook Statement</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     				<form id="billProcessForm" name="billProcessForm" action="bankBookInfo.action" style="margin-bottom: 1px;">					
						<div class="row-fluid">							
							
						<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Bank</label>
										<select id="bank_id" name="bank_id"  style="width: 54.5%;"   onchange="fetchSelectBox(branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(branch_sbox)">
											<option value="" selected="selected">Select Bank</option>
											<s:iterator value="%{#session.USER_BANK_LIST}">
												<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
										</s:iterator>
										</select>
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Branch</label>
										<select id="branch_id" name="branch_id" style="width: 54.5%;"  onchange="fetchSelectBox(account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
											<option value="" selected="selected">Select Branch</option>
										</select>  
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Account</label>
										<select id="account_id" name="account_no" onchange="isReconiliatedOrNot()" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
											<option value="" selected="selected">Select Account</option>
										</select> 
									</div>
								</div>
						
							 
						</div>
						
						<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="50%" align="center">
										<tr>
											<td width="100%" align="right" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="date_wise" id="date_wise" name="report_for" onclick="checkType(this.id)"/> Date Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" value="month_wise" id="month_wise" name="report_for" onclick="checkType(this.id)" /> Month Wise&nbsp;&nbsp;&nbsp;
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>
						
						
						
						<div class="row-fluid" id="month_year_div">							
							<div class="span6" id="month_div">									    
								<label style="width: 40%">Collection Month<m class='man'/></label>
								<select name="collection_month" id="collection_month" onchange="isReconiliatedOrNot()" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6" id="year_div">
								<label style="width: 40%">Collection Year<m class='man'/></label>
								<select name="collection_year" id="collection_year" onchange="isReconiliatedOrNot()" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						<div class="row-fluid" id="from_to_date_div">							
							 <div class="span6" id="fromDateSpan">
				    			<label id="fromDateLabel">From Date</label>
								<input type="text" name="from_date" id="from_date" style="width: 54%" />
				  			</div>
				  			<div class="span6" id="toDateSpan">
				    			<label id="toDateLabel">To Date</label>
								<input type="text" name="to_date" id="to_date" style="width: 54%" />
				  			</div>
						</div>
						
					
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">
						   		     <button class="btn btn-success"   id="btn_reconciliation" onclick="callAction('reconciliationReportHome.action?bank_id='+$('#bank_id').val()+'&branch_id='+$('#branch_id').val()+'&account_no='+$('#account_id').val()+'&collection_month='+$('#collection_month').val()+'&collection_year='+$('#collection_year').val())">Reconciliation</button>				   			     
						   			 <button class="btn btn-primary" type="button" id="btn_add_reconciliation" onclick="addReconciliationInfo()" >Add Reconciliation</button>
						   			 <button class="btn" type="submit">Generate Report</button>	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
					</form>																	
				</div>
			</div>
		</div>
	</div>
</div>
   		
	</div>
	<div id="reconciliationInfoDiv"style="width: 51%; height: 99%;float: left;margin-left: 1%;">
		<div class="row-fluid">
			<div class="span12" id="rightSpan">					
					<jsp:include page="../reports/ReconciliationInfo.jsp" />	
			</div>
		</div>	
	</div>
</div>




<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript">

hideElement("from_to_date_div","month_div","year_div");    
 Calendar.setup({
    inputField : "to_date",
    trigger    : "to_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  Calendar.setup({
    inputField : "from_date",
    trigger    : "from_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
    
   

function checkType(type){
	if(type=="area_wise")
	{
	 disableChosenField("customer_id");
	 disableField("customer_category");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 enableField("area_id");
	}
	else if(type=="by_category"){
	 disableChosenField("customer_id");
	 enableField("customer_category","area_id");
	 autoSelect("customer_category","area_id");
	}
	else if(type=="individual"){
	 enableChosenField("customer_id");
	 disableField("customer_category","area_id");
	 resetSelectBoxSelectedValue("customer_category","area_id");
	}
			
		 if(type=="month_wise")
		{
		hideElement("from_to_date_div");
		showElement("month_div","year_div");
		}else if(type=="year_wise")
			{			
			hideElement("from_to_date_div","month_div");
			showElement("year_div");
			}else if(type=="date_wise")
			{
				
				hideElement("month_div","year_div");
				showElement("from_to_date_div");
				
				}	
}	

$(document).ready(function(){
/* 	$(".addCF").click(function(){
		$("#addTable").append('<tr valign="top"><td width="75%"><div class="select-editable"><select name="othersComment[]" onchange="this.nextElementSibling.value=this.value" "><option value="">Year</option><s:iterator  value="%{#application.YEARS}" id="year"><option value="<s:property/>"><s:property/></option></s:iterator>    </select>    <input type="text"  id ="title" name="format" value="" /> </div> <input type="text" name="othersAmount[]" value=""  style="width:15%;text-align:right;padding-right:0px;" onkeyup="calcuateTotalOthersAmount()"/> &nbsp; <a href="javascript:void(0);" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></div></td></tr>');
	}); */
	
	$(".addCF").click(function(){
		$("#addTable").append('<tr valign="top"><td width="75%"><div class="select-editable"><select name="othersComment[]"  style="width:60%;" ><option value="">Cause</option> <s:iterator  value="%{#application.ALL_RECONCILIATION_CAUSE_ADD}">   <option value="<s:property value='label'/>"><s:property value="label"/></option></s:iterator></select>     <select name="addAccount[]" id="collection_year" style="width: 8%;">	<option value="na">select</option><s:iterator value="%{#application.YEARS}" id="year"><option value="<s:property/>"><s:property/></option></s:iterator></select> <input type="text" name="othersAmount[]" value=""  style="width:15%;text-align:right;padding-right:0px;" onkeyup="calcuateTotalOthersAmount()"/> &nbsp; <a href="javascript:void(0);" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></div></td></tr>');
	});
    $("#addTable").on('click','.remCF',function(){
        $(this).parent().parent().remove();
        calcuateTotalOthersAmount();
    });
    
    $(".lessCF").click(function(){
		$("#lessTable").append('<tr valign="top"><td width="75%"><div class="select-editable"><select name="lessComment[]"  style="width:60%;" ><option value="">Cause</option> <s:iterator  value="%{#application.ALL_RECONCILIATION_CAUSE_LESS}">   <option value="<s:property value='label'/>"><s:property value="label"/></option></s:iterator></select>     <select name="lessAccount[]" id="collection_year" style="width: 8%;">	<option value="na">select</option><s:iterator value="%{#application.YEARS}" id="year"><option value="<s:property/>"><s:property/></option></s:iterator></select> <input type="text" name="lessAmount[]" value=""  style="width:15%;text-align:right;padding-right:0px;" onkeyup="calcuateTotalOthersAmount()"/> &nbsp; <a href="javascript:void(0);" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></div></td></tr>');
	});
    $("#lessTable").on('click','.remCF',function(){
        $(this).parent().parent().remove();
        calcuateTotalOthersAmount();
    });
});


function calcuateTotalOthersAmount(){
	var othersComment = $("select[name='othersComment\\[\\]']").map(function(){return $(this).val();}).get();
	var othersAmount = $("input[name='othersAmount\\[\\]']").map(function(){return $(this).val();}).get();
	var lessComment = $("select[name='lessComment\\[\\]']").map(function(){return $(this).val();}).get();
	var lessAmount = $("input[name='lessAmount\\[\\]']").map(function(){return $(this).val();}).get();
	
	
	var totalAmount=parseFloat($("#balance_bank_statement").val());
	var totalComments="";
	for(var i=0;i<othersAmount.length;i++)
		totalAmount=totalAmount+parseFloat(othersAmount[i]==""?0:othersAmount[i]);
	
	for(var i=0;i<lessAmount.length;i++)
		totalAmount=totalAmount-parseFloat(lessAmount[i]==""?0:lessAmount[i]);	
	$("#totalOthersAmount").val(totalAmount);	
	
	console.log(othersComment);
	console.log(othersComment.length);
	for(var i=0;i<othersComment.length;i++)
		totalComments=totalComments+' '+othersComment[i]+(othersComment[i]==""?"":",");
	//$("#totalComment").html(totalComments);
	
	/*console.log(lessComment);
	console.log(lessComment.length);
	for(var i=0;i<lessComment.length;i++)
		totalComments=totalComments+' '+lessComment[i]+(lessComment[i]==""?"":","); */
	$("#totalComment").html(totalComments);
}

function saveReconcilationInfoAndPrint(){
	var othersComment = $("select[name='othersComment\\[\\]']").map(function(){return $(this).val();}).get();
	var othersAmount = $("input[name='othersAmount\\[\\]']").map(function(){return $(this).val();}).get();
	var lessComment = $("select[name='lessComment\\[\\]']").map(function(){return $(this).val();}).get();
	var lessAmount = $("input[name='lessAmount\\[\\]']").map(function(){return $(this).val();}).get();
	var addAccount=$("select[name='addAccount\\[\\]']").map(function(){return $(this).val();}).get();
	var lessAccount=$("select[name='lessAccount\\[\\]']").map(function(){return $(this).val();}).get();
	calcuateTotalOthersAmount();
	var postdata={bank_id:$("#bank_id").val(),branch_id:$("#branch_id").val(),account_no:$("#account_id").val(),collection_month:$("#collection_month").val(),collection_year:$("#collection_year").val(),add_comments:othersComment.join("#ifti#"),add_amount:othersAmount.join("#ifti#"),lessComment:lessComment.join("#ifti#"),lessAmount:lessAmount.join("#ifti#"),addAccount:addAccount.join("#ifti#") ,lessAccount:lessAccount.join("#ifti#"),opening_balance:$("#openingBalance").val(),total_amount:$("#balance_bank_statement").val()}
	
	$.ajax({
		    url: 'saveReconcilationInfo.action',
		    type: 'POST',
		    data: postdata,
		    cache: false,
		    success: function (response) {
		    	      $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, {
	                    zIndex: 1500,
	                    width:450,
	                    /*
	                     beforeOpen: function () {
				            centerCustomerInfoDialog($("#customer_id").val());
        				},
        				*/
	                     afterOpen:disableOnClick,
	                     onClose: function () {
	                     
	                      $('.ui-widget-overlay').unbind( "click" );
	                    	callAction('reconciliationReportHome.action?bank_id='+$('#bank_id').val()+'&branch_id='+$('#branch_id').val()+'&account_no='+$('#account_id').val()+'&collection_month='+$('#collection_month').val()+'&collection_year='+$('#collection_year').val())
	                        return true; // allow closing
	                    }
            });
		    }
		    
		  });	
		  
}

function isReconiliatedOrNot(change_id){
	
	$("#reconciliationInfoDiv").hide();
	if($("#bank_id").val()==""||$("#branch_id").val()==""||$("#account_id").val()==""||$("#collection_month").val()==""||$("#collection_year").val()=="")
	{
	return;
	}
	var postdata={bank_id:$("#bank_id").val(),branch_id:$("#branch_id").val(),account_no:$("#account_id").val(),collection_month:$("#collection_month").val(),collection_year:$("#collection_year").val()}
	$.ajax({
		    url: 'isReconiliatedOrNot.action',
		    type: 'POST',
		    data: postdata,
		    cache: false,
		    success: function (response) {

		    	if(response=="no")
		    	{
		    	$("#btn_add_reconciliation").show();
		    	$("#btn_reconciliation").hide();
		    	
		    	}else
		    	{
		    	$("#btn_add_reconciliation").hide();
		    	$("#btn_reconciliation").show();
		    	}
		    	
		    }
		    
		  });	
}

$("#reconciliationInfoDiv").hide();
$("#btn_add_reconciliation").hide();
$("#btn_reconciliation").hide();

function addReconciliationInfo(){
$("#reconciliationInfoDiv").show();

	var postdata={bank_id:$("#bank_id").val(),branch_id:$("#branch_id").val(),account_no:$("#account_id").val(),collection_month:$("#collection_month").val(),collection_year:$("#collection_year").val()}
	$.ajax({
		    url: 'getClosingBalance.action',
		    type: 'POST',
		    data: postdata,
		    cache: false,
		    success: function (response) {		    
		      $("#openingBalance").val(response);   	
		    }
		    
		  });
}
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide();}}));
    

    
    
</script>	



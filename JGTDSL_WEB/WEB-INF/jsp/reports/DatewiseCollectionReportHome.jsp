<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("collectionStatementReportHome.action");
	setTitle("Bank Wise Collection Statement");
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
<div class="meter-reading" style="width: 80%;height: 50%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Collection Statement</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     				<form id="billProcessForm" name="billProcessForm" action="collectionStatementInfo.action" style="margin-bottom: 1px;">					
						<div class="row-fluid">							
							
						<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="85%" align="center">
										<tr>
											<td width="100%" align="center" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="security" id="security" name="report_for" onclick="checkType(this.id)"/> Security&nbsp;&nbsp;&nbsp;
												<input type="radio" value="date_wise" id="date_wise" name="report_for" onclick="checkType(this.id)"/> Date Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" value="month_wise" id="month_wise" name="report_for" onclick="checkType(this.id)" /> Month Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" value="month_wiseDetails" id="month_wiseDetails" name="report_for" onclick="checkType(this.id)" /> Month Wise Details&nbsp;&nbsp;&nbsp;
												<input type="radio" value="bank_wise" id="bank_wise" name="report_for" onclick="checkType(this.id)" /> Bank Wise&nbsp;&nbsp;&nbsp;
												<input type="radio" value="all_bank_wise_monthly" id="all_bank_wise_monthly" name="report_for" onclick="checkType(this.id)" /> All Bank&nbsp;&nbsp;&nbsp;
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>	
							
						<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Bank</label>
										<select id="bank_id" name="bank_id"  style="width: 54.5%;"   onchange="fetchSelectBox(branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(branch_sbox)">
											<option value="" disabled selected hidden selected="selected">Select Bank</option>
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
											<option value="" disabled selected hidden selected="selected">Select Branch</option>
										</select>  
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Account</label>
										<select id="account_id" name="account_no" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
											<option value="" selected="selected">Select Account</option>
										</select> 
									</div>
								</div>
						
							 
						</div>

						<div class="row-fluid" id="month_year_div">							
							<div class="span6" id="month_div">									    
								<label style="width: 40%">Collection Month<m class='man'/></label>
								<select name="collection_month" id="collection_month" style="width: 56%;margin-left: 0px;" >
							       	<option value="" disabled selected hidden></option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6" id="year_div">
								<label style="width: 40%">Collection Year<m class='man'/></label>
								<select name="collection_year" id="collection_year" style="width: 56%;">
							       	<option value="" disabled selected hidden></option>
							       	
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						
						<div class="row-fluid" id="date_div">							
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 40%">Collection Date</label>
									<input type="text" name="collection_date" id="collection_date" style="width: 25%" />
								</div>
							</div>
						</div>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">				   			     
						   			 <button class="btn" id="btn_save" type="submit" onclick="generateReport()">Generate Report</button>	
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

  
<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript">

hideElement("month_div","year_div","date_div");
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
	
	if(type=="security"){
		hideElement("date_div");
		showElement("month_div","year_div");
		disableField("bank_id");
		disableField("branch_id");
		disableField("account_id");
	}
	else if(type=="month_wise"){
		hideElement("date_div");
		showElement("month_div","year_div");
		disableField("bank_id");
		disableField("branch_id");
		disableField("account_id");
		//enableField("bank_id");
		//enableField("branch_id");
		//enableField("account_id");	
		//enableButton("btn_save");
	}
	else if(type=="date_wise"){
		hideElement("month_div","year_div");
		showElement("date_div");
		enableField("bank_id");
		enableField("branch_id");
		enableField("account_id");	
		//enableButton("btn_save");	
	}
	else if(type=="month_wiseDetails"){
		hideElement("date_div");
		showElement("month_div","year_div");
		enableField("bank_id");
		enableField("branch_id");
		enableField("account_id");
		enableButton("btn_save");
	}
	else if(type=="bank_wise"){
		hideElement("date_div");
		showElement("month_div","year_div");
		disableField("bank_id");
		disableField("branch_id");
		disableField("account_id");
		
		
		if($("#collection_month").val()=="Select Month" || $("#collection_year").val()=="year")
		{
			disableButton("btn_save");
		}else{
			enableButton("btn_save");
		}
		
	}
	else if(type="all_bank_wise_monthly"){
		disableField("bank_id");
		disableField("branch_id");
		disableField("account_id");
		showElement("month_div","year_div");
	}
}	

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide();}}));
    
 /*
 *  Report Validation 
 * sujon
 
function generateReport()
{
  var isValid=true;

	if($("#collection_month").val()!="" && $("#collection_year").val()!=""){
		cbColor($("#collection_month"),"e");
		cbColor($("#collection_year"),"e");
		isValid=false;
  	}else{
  	  isValid=true;
  	}
  	
	  if(isValid==true)
  			$('form#billProcessForm').submit();
}


$('form#billProcessForm').unbind("submit");

$("form#billProcessForm").submit(function(event){
	
  event.preventDefault();
    
  });
 
  return false;
});
*/

</script>	

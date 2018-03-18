<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("clearnessCertificateHome.action");
	setTitle("Customer Balance Sheet");
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
    				<h4 id="rightSpan_caption">Clearness Certificate Of Customer</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				    	
					<div id="download_div">
     				<form id="billProcessForm" name="billProcessForm" action="clearnessCertificateInfo.action" style="margin-bottom: 1px;">
						
						
							<div class="row-fluid">							
								<div class="span6">									    
									<label style="width: 40%">Region/Area</label>
									<select id="area_id"  style="width: 56%;" disabled="disabled"  name="area">
										<option value="" selected="selected">Select Area</option>
										<s:iterator value="%{#session.USER_AREA_LIST}" id="areaList">
											<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
									</s:iterator>
									</select>									      
								</div>
								
							</div>
							
							
							
						<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="50%" align="center">
										<tr>
											<td width="100%" align="right" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="individual_wise" id="individual_wise" name="download_type" checked onclick="checkType(this.id)"/>&nbsp;&nbsp;&nbsp;Individual&nbsp;&nbsp;&nbsp;
												<input type="radio" value="category_wise" id="category_wise" name="download_type" onclick="checkType(this.id)" />&nbsp;&nbsp;&nbsp;Category&nbsp;&nbsp;&nbsp;
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>
						
						<div class="row-fluid" id="individual_code_div" >
							<div class="span12" style="margin-top: 4px;">
								<label style="width: 40%">Customer ID <m class='man'/></label>
								<input type="text" name="customer_id" id="customer_id" style="font-weight: bold;color: #3b5894; z-index: 2; background: transparent;width: 20%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								
								
						  	</div>
						</div>
						<div class="row-fluid" id="customer_type_div" style="display: none">							
							<div class="span12">									    
								<label style="width: 40%">Type</label>
								<select id="customer_Type" style="width: 56%;"   name="customer_type" ">
									<option value="" >Select Category</option>									
										<option value="1"  selected="selected">Meter</option>
										<option value="0" >Non-Meter</option>		
								</select>  								      
							</div>	 
						</div>
						<div class="row-fluid" id="category_div" style="display: none">
								<label style="width: 40%">Category<m class='man'/></label>
								<select id="customer_category" style="width: 56%;" disabled="disabled"  name="customer_category">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>       
						</div> 					
						<div class="row-fluid" id="range_code_div"  style="display:none;margin-top: 4px;">
							<div class="span12" style="margin-top: 12px;">
								<label style="width: 40%">From Cus_Code <m class='man'/></label>
								<input type="text" name="from_customer_id" id="from_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 10%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								
								
						  	</div>
						  	<div class="span12" style="margin-left: -1px;margin-top: 6px">
								<label style="width: 40%">To Cus_code <m class='man'/></label>
								<input type="text" name="to_customer_id" id="to_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 10%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								
								
						  	</div>
						</div>
						
						<div class="row-fluid" id="month_year_div">							
							<div class="row-fluid" id="month_div">									    
								<label style="width: 40%">Calendar Month<m class='man'/></label>
								<select name="collection_month" id="collection_month" style="width: 40%;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="row-fluid" id="year_div">
								<label style="width: 40%">Calendar Year<m class='man'/></label>
								<select name="calender_year" id="calender_year" onchange="isReconiliatedOrNot()" style="width: 40%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>
							<br/>							
							<div class="row-fluid" id="officer_nsme" >
								<div class="span12" style="margin-top: 4px;">
									<label style="width: 40%">Officer's Name <m class='man'/></label>
									<input type="text" name="officer_name" id="officer_name" style="font-weight: bold;color: #3b5894; z-index: 2; background: transparent;width: 30%;margin-top: -4px;" value="" tabindex="1"/>								
						  		</div>
							</div>
							<div class="row-fluid" id="designation" >
								<div class="span12" style="margin-top: 4px;">
									<label style="width: 40%">Officer's Designation <m class='man'/></label>
									<input type="text" name="officer_desig" id="officer_desig" style="font-weight: bold;color: #3b5894; z-index: 2; background: transparent;width: 30%;margin-top: -4px;" value="" tabindex="1"/>								
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
						   			  <input type="hidden" name="category_name" id="category_name" value="DOMESTIC(PVT)" />
						   			     
						   			 <button class="btn" type="submit">Generate Report</button>	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
						
							
				
					</form>	
					</div>
                   <div id="approve_div" style="display:none">
       					<jsp:include page="ApproveCC.jsp" />
					</div>																	
				</div>
			</div>
		</div>
	</div>
</div>

  
<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript">
autoSelect("area_id");
enableField("area_id");
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
  
$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){
    		getCustomerInfo("",$('#customer_id').val());
    	},
}));

function checkType(type){
	if(type=="ccDownload")
	{
	 autoSelect("area_id");
	 enableField("area_id");
	 hideElement("approve_div");
	 showElement("download_div");
	 showElement("print_download");
	}else if(type=="ccApprove"){
	 hideElement("download_div");
	 hideElement("print_download");
	 showElement("approve_div");
	}else if(type=="individual_wise")
	{
	hideElement("approve_div","category_div","range_code_div","customer_type_div","calender_year_div");
	showElement("download_div","individual_code_div","print_download");
	}else if(type=="category_wise"){
	 hideElement("approve_div","individual_code_div");
	 showElement("download_div","category_div","range_code_div","customer_type_div","calender_year_div","print_download");
	 autoSelect("customer_category");
	 enableField("customer_category");
	}
};

</script>

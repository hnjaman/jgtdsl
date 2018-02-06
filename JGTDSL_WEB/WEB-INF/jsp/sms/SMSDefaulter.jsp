<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("smsDefalter.action");
	setTitle("Defauler Customer Information");
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
    				<h4 id="rightSpan_caption">Defaulter Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     										
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Region/Area</label>
								<select id="area_id"  style="width: 56%;" name="area_id">
									<option value="" selected="selected">Select Area</option>
									<s:iterator value="%{#session.USER_AREA_LIST}" id="areaList">
										<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
								</s:iterator>
								</select>									      
							</div>
							<div class="span6">
								<label style="width: 40%">Category<m class='man'/></label>
								<select id="customer_category" style="width: 56%;"  name="customer_category">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>       
							</div>  
						</div>
						
					
						<div class="row-fluid" id="monthyear_div">							
							<div class="span6" id="monthdiv">									    
								<label style="width: 40%">Billing Month<m class='man'/></label>
								<select name="bill_month" id="bill_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6" id="yeardiv">
								<label style="width: 40%">Billing Year<m class='man'/></label>
								<select name="bill_year" id="bill_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Due Date<m class='man'/></label>
								<input type="text" style="width: 52%" name="due_date" id="due_date"  maxlength="30"/>						      
							</div>
							<div class="span6">
								<label style="width: 40%">Due Month<m class='man'/></label>
								<input type="text" style="width: 52%" name="month_number" id="month_number"  maxlength="2"/>							      
							    
							</div>  
						</div>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">
						   			 
						   			     
						   			 <button class="btn btn-primary" type="button" onclick="fetchDefalterList()">Search</button>	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
																				
				</div>
			</div>
		</div>
	</div>
</div>

<div style="width: 47%;text-align: center;float: left;padding-top:20px;margin-left: 5px;display: none;" id="stat_div">
<table>
	<tr>
		<td style="text-align: left;padding-left: 10px;padding-bottom: 20px;background-color: #387C44;color: white;"  id="loading_div"></td>
	</tr>
	<tr>
 		<td style="text-align: left;padding-left: 10px;padding-top: 20px;padding-bottom: 20px;background-color: #8491A8"  id="total_div"></td>
	</tr>
	<tr>
 	 	<td style="text-align: left;padding-left: 10px;padding-top: 20px;padding-bottom: 20px;background-color: #CCFFFF"  id="counting_div"></td>
	</tr>
</table>
</div>
 
 
 
 <div id="detailDiv">

</div>

	
<script type="text/javascript">

    function fetchDefalterList() {

        if($("#area_id").val()==""){
            alert("Select a Region/Area");return;
        }

		if($("#customer_category").val()==""){
            alert("Select a Category");return;
        }
		
		if($("#bill_month").val()==""){
            alert("Select a Billing Month");return;
        }
		if($("#bill_year").val()==""){
            alert("Select a Billing Year");return;
        }
		
		$("#detailDiv").html("");
		$("#stat_div").show();
		$("#loading_div").html(jsImg.LOADING_MID+"<br/><br/><font style='color:white;font-weight:bold'>Please wait. Searching the defaulter list for you.</font>");
		
		
		
        $.ajax({
            type    : "POST",
            url     : "fetchDefaulter",
            dataType: 'text',
            async   : false,
            data    : {
                areaId: $("#area_id").val(), customerCategory: $("#customer_category").val(), billMonth: $("#bill_month").val(),
				billYear: $("#bill_year").val(), dueDate: $("#due_date").val(), monthNumber: $("#month_number").val()
            }
        	}).done(function (msg) {
        			$("#stat_div").hide();
                    $("#detailDiv").html(msg);
                })
                .always(function () {
                    //$('#sw-val-step-3').unmask();
                })
                .fail(function (data) {
                    if (data.responseCode)
                        alert(data.responseCode);
                });

		
    }   //End of fetchInformation

</script>	


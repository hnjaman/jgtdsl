<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("deleteCollection.action");
	setTitle("Delete Daily Collection");
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
		<div class="span9" id="rightSpan">
			
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Delete Collection</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
				
     									
						<div class="row-fluid">							

							
						<div class="row-fluid">
									<div class="span9">
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
									<div class="span9">
										<label style="width: 40%">Branch</label>
										<select id="branch_id" name="branch_id" style="width: 54.5%;"  onchange="fetchSelectBox(account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
											<option value="" disabled selected hidden selected="selected">Select Branch</option>
										</select>  
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span9">
										<label style="width: 40%">Account</label>
										<select id="account_id" name="account_no" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
											<option value="" selected="selected">Select Account</option>
										</select> 
									</div>
								</div>
						
							 
						</div>
						
						<div class="row-fluid" id="date_div">							
							 <div class="row-fluid">
								<div class="span9">
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
						   		
						   		<td style="width: 60%" align="right">				   			     
						   			 <button class="btn" id="btn_save" onclick="fetchWrongCollectionList()">Search</button>	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
					
					
				
																				
				</div>
			</div>
	</div>
	
		<div style="width: 47%;text-align: center;float: left;padding-top:20px;margin-left: 5px;display: none;" id="stat_div">
			<table>
				<tr>
					<td style="text-align: left;padding-left: 10px;padding-bottom: 20px;background-color: #387C44;color: white;"  id="loading_div"></td>
				</tr>
			</table>
		</div>


		<div id="detailDiv">
		
		</div>
	
</div>

  
<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript">


// Delete Collection Grid 

  function fetchWrongCollectionList() {

        if($("#bank_id").val()==""){
            alert("Select a Bank");return;
        }

		if($("#branch_id").val()==""){
            alert("Select a branch");return;
        }
		
		if($("#collection_date").val()==""){
            alert("Enter a Collection Date");return;
        }
		
		$("#detailDiv").html("");
		$("#stat_div").show();
		$("#loading_div").html(jsImg.LOADING_MID+"<br/><br/><font style='color:white;font-weight:bold'>Please wait. Searching the Collection list </font>");
		
		
		
        $.ajax({
            type    : "POST",
            url     : "fetchCollection4Delete",
            dataType: 'text',
            async   : false,
            data    : {bank_id: $("#bank_id").val(), branch_id: $("#branch_id").val(), account_id: $("#account_id").val(),
				collection_date: $("#collection_date").val()
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

/*

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
	
	if(type=="month_wise"){
		hideElement("date_div");
		showElement("month_div","year_div");
		enableField("bank_id");
		enableField("branch_id");
		enableField("account_id");	
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
		//enableButton("btn_save");
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
*/
Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide();}}));
    

</script>	

<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="meter-reading" style="width: 80%;height: 50%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Defaulter Information</h4>
				</div>
				<div class="w-box-content"  id="content_div">
				
     										
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   
						   <input type="hidden" id="areaId" name="areaId" value="<s:property value='areaId'/>" />
						   <input type="hidden" id="customerCategory" name="customerCategory" value="<s:property value='customerCategory'/>" />
						   <input type="hidden" id="billMonth" name="billMonth" value="<s:property value='billMonth'/>" />
						   <input type="hidden" id="billYear" name="billYear" value="<s:property value='billYear'/>" />
						   
						   
						   <table width="100%" class="table-bordered" border="1">
						   
						   <thead>
							 
				                <tr >
				                    <th align="center">ID</th>
				                    <th align="center">Name</th>
				                    <th align="center">Due Amount</th>
				                    <th align="center">Total Months</th>
				                    <th align="center">SMS Text</th>
				                    <th align="center">SMS Send</th>
				                    				                    
				                </tr>              
				               
				                
				            </thead>
				            <tbody>
				             <s:iterator value="custList" >
				            	<tr >
				            		<td align="left"> <s:property value="customerId" />  </td>  
				            		<td align="left"> <s:property value="customerName" /> </td>   
				            		<td> <s:property value="totalAmount" /> </td>  
				            		<td align="center"> <s:property value="billcount" /> </td>  
				            		<td align="left"> <s:property value="textSMS" /> </td>  
				            		<td align="center"> <s:property value="status" /> </td>        		
				            	</tr>
				            	
				            </s:iterator>
				 	 		</tbody>
						   
						   
						   </table>
						   
						 </div>
						
						
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		
						   		<td style="width: 70%" align="right">
						   			    
						   			 <button class="btn btn-primary" type="button" onclick="sendSMSDefalterList()">Send SMS</button>	
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

 

	


<script type="text/javascript" >


function sendSMSDefalterList() {

	       		
		$("#detailDiv").html("");
		$("#stat_div").show();
		$("#loading_div").html(jsImg.LOADING_MID+"<br/><br/><font style='color:white;font-weight:bold'>Please wait. Send SMS to defaulter Customers.</font>");
		
		fetchTotalCustomerToSendSMS();
		
		var refreshIntervalId=0;
        $.ajax({
            type    : "POST",
            url     : "smsAction",
            dataType: 'text',
            async   : true,
            data    : {
                areaId: '<s:property  value="areaId"/>',
                customerCategory: '<s:property  value="customerCategory"/>',
                billMonth:  '<s:property  value="billMonth"/>',
                billYear: '<s:property  value="billYear"/>'		
            },
            success: function (response){
	    	
	    		clearInterval(refreshIntervalId);   
	    		$("#loading_div").html(response.message);
	    		fetchProgressInfoForSMS();
	    	}
        	}).done(function (msg) {
        			$("#stat_div").hide();
                    $("#detailDiv").html(msg);
                    //clearInterval(refreshIntervalId);
                    //fetchProgressInfoForSMS();
                    
                })
                .always(function () {
                    //clearInterval(refreshIntervalId);
                    //fetchProgressInfoForSMS();
                })
                .fail(function (data) {
                    if (data.responseCode)
                        alert(data.responseCode);
                });

		refreshIntervalId = setInterval(fetchProgressInfoForSMS, 5000);
		
		
    }

/* function sendSMSDefalterList() {

	       		
		$("#detailDiv").html("");
		$("#stat_div").show();
		$("#loading_div").html(jsImg.LOADING_MID+"<br/><br/><font style='color:white;font-weight:bold'>Please wait. Send SMS to defaulter Customers.</font>");
		
		var refreshIntervalId=0;
        $.ajax({
            type    : "POST",
            url     : "smsAction",
            dataType: 'text',
            data    :{
                areaId: $("#areaId").val(), customerCategory: $("#customerCategory").val(), billMonth: $("#billMonth").val(),
				billYear: $("#billYear").val()
            },
            async: true,
	    	success: function (response){
	    	
	    clearInterval(refreshIntervalId);   
	    $("#loading_div").html(response.message);
	    fetchProgressInfoForSMS();
	    	}
        	}).done(function (msg) {
        			$("#stat_div").hide();
                    $("#detailDiv").html(msg);
                    clearInterval(refreshIntervalId);
                    fetchProgressInfoForSMS();
                    
                })
                .always(function () {
                    clearInterval(refreshIntervalId);
                    fetchProgressInfoForSMS();
                })
                .fail(function (data) {
                    if (data.responseCode)
                        alert(data.responseCode);
                });

		refreshIntervalId = setInterval(fetchProgressInfoForSMS, 5000);
		
		
    }  */
    
/* function sendSMSDefalterList() {
		$("#detailDiv").html("");
		$("#stat_div").show();
		$("#loading_div").html(jsImg.LOADING_MID+"<br/><br/><font style='color:white;font-weight:bold'>Please wait. Send SMS to defaulter Customers.</font>");
		
		var refreshIntervalId=0;
		
		$.ajax({
	    url: "smsAction",
	    type: "POST",
	    dataType: 'text',
	     data    :{
                areaId: $("#areaId").val(), customerCategory: $("#customerCategory").val(), billMonth: $("#billMonth").val(),
				billYear: $("#billYear").val()
            },
		async: false,
	    success: function (response) {
	    	 clearInterval(refreshIntervalId);   
	    	$("#loading_div").html(response.message);
	    	fetchProgressInfoForSMS();
	    }
	    
	  });
		
	refreshIntervalId = setInterval(fetchProgressInfoForSMS, 5000);

}     */
    
    
    
function fetchProgressInfoForSMS()
{
	$("#counting_div").html(jsImg.LOADING_MID);
	
	  $.ajax({
	    url: "getProcessedTotalSMSCount",
	    type: "POST",
	    data: { areaId: '<s:property  value="areaId"/>',customerCategory: '<s:property  value="customerCategory"/>',billMonth:  '<s:property  value="billMonth"/>',billYear: '<s:property  value="billYear"/>'	},	    
	    async: true,
	    cache: false,
	    success: function (response) {
	    	$("#counting_div").html(response.message);
	    }
	    
	  });
}

function fetchTotalCustomerToSendSMS()
{
	$.ajax({
	    url: "getTotalCustomerToSendSMS",
	    type: "POST",
	    data: { areaId: '<s:property  value="areaId"/>',customerCategory: '<s:property  value="customerCategory"/>',billMonth:  '<s:property  value="billMonth"/>',billYear: '<s:property  value="billYear"/>'	},	    
	    async: true,
	    cache: false,
	    success: function (response) {
	    	$("#total_div").html(response.message);
	    }
	    
	  });


}

</script>	

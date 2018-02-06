<div id="gbox_gridTable" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 778px;" >
<div id="gview_gridTable" class="ui-jqgrid-view">
	<div class="ui-jqgrid-titlebar ui-jqgrid-caption ui-widget-header ui-corner-top ui-helper-clearfix" style="padding-left: 8px;">
		<span class="ui-jqgrid-title">Current Month Bill Collectiion</span>
	</div>
	<div class="ui-jqgrid-bdiv" style="width: 778px;height: 100%;padding: 5px;">
			<div class="row-fluid" style="margin-top: 10px;">
					<div class="span12">
					<%-- 			<div class="row-fluid">
									<div class="span12">
										<label style="width: 40%">Billing Month</label>
										<span id="metered_bill_month_year" >
										  <select name="multiColl.current_bill_month" id="current_bill_month" style="width: 25%;margin-left: 0px;" >
										       	<option value="">Select Month</option>           
										        <s:iterator  value="%{#application.MONTHS}">   
										   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
												</s:iterator>
									       </select>							       
									       <select name="multiColl.current_bill_year" id="current_bill_year" style="width: 25%;margin-left:4.5%;"" >
										       	<option value="">Year</option>
										       	<s:iterator  value="%{#application.YEARS}" id="year">
										            <option value="<s:property/>"><s:property/></option>
												</s:iterator>
									       </select>
								       </span>

									</div>
								</div> --%>		                                            					                                            
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 20%;padding-left: 10px;">Amount</label>
								<input type="text" name="multiColl.collection_amount" id="rate" tabindex="1" style="width: 60%"  "/>							
						  	</div>
						  	<div class="span6">									    
								<button class="btn btn-beoro-3"  type="button" id="btn_save_curr_coll" onclick="saveCurrentMonthBillWithCollection()">
								<span class="splashy-box_add"></span>
								Collect</button>   							
							</div>
						</div>				                                            					                                            
					</div>
			</div>
	</div>		
</div>
</div>
<script type="text/javascript">

var collection_date=$("#collection_date").val();
var mySplitResult = collection_date.split("-");
$("#current_bill_month").val(mySplitResult[1]);
$("#current_bill_year").val(mySplitResult[2]);


  $.ajax({
    url: "getTariffForDomesticCustomer.action?customer_id="+$('#customer_id').val(),
  	dataType: 'text',		    
    type: 'POST',
    async: true,
    cache: false,
	success: function (response){
    	var rateArr=response.split("#");
    	$("#rate").val(rateArr[0]);
    	$("#rate").focus();	
    }    
  });


</script>
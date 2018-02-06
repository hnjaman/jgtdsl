<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("salesAdjustmentHome.action");
	setTitle("Sales Adjustment");
</script>




<script  type="text/javascript">
/*	function add(){
	//alert("bu.....");
	//var bl = document.getElementById('bil');
	//var sur = document.getElementById('surcharge_amount');
	//var mr = document.getElementById('meter_amount');
	//var sum = parseFloat(bl.value)+parseFloat(sur.value)+parseFloat(mr.value);
	
	
	  var bl = document.getElementById('bil');
      var sur = document.getElementById('surcharge_amount');
      var mr = document.getElementById('meter_amount');
      
      
      var total=parseFloat(bl.value) + parseFloat(sur.value) + parseFloat(mr.value);
      
      
      document.getElementById('totalOthersAmount').value=total.toFixed(2);

	}
	
	
	
	
		function add1(){

	
	
	  var bl = document.getElementById('bil');
	  
	  alert(bl.value);
	  
      var sur = document.getElementById('surcharge_amount');
      var mr = document.getElementById('meter_amount');
      
      
      var total=parseFloat(bl.value) + parseFloat(sur.value) + parseFloat(mr.value);
      
      
      document.getElementById('totalOthersAmount').value=total.toFixed(2);

	}
	*/
</script> 






<div style="width: 40%;height: 98%;float: left;" id="left_div">
	<div class="row-fluid">
		<div style="width: 100%;float: left;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Bill/Invoice Adjustment/Other Amount</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Customer ID <m class='man'/></label>
								<input type="text" name="supplyOff.customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 29%;" value="<s:property value='customer_id' />" tabindex="1"/>
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 29%;"/>
						  	</div>
						</div>
						
						<div class="row-fluid">							
							<div class="span12">									    
								<label style="width: 19.5%">Region/Area</label>
							<select id="area_id" style="width: 79%;" disabled="disabled">
								<option value="" selected="selected">
									Select Area
								</option>
								<s:iterator value="%{#session.USER_AREA}"
									id="categoryList">
									<option value="<s:property value="area_id" />">
										<s:property value="area_name" />
									</option>
								</s:iterator>
							</select>
							</div>
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Select Type</label>
								
								<input type="radio" name="operationType" id="sales_adjustment" value="sales_adjustment" checked="checked" onclick="changeSelection(this.value)"/>&nbsp;&nbsp;Sales Adjustment
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;								
						  	</div>
						</div>

						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="loadUnpaidBillList()" >Get List of  Bill/Invoice</button>
						</div> 
				</div>
			</div>
		</div>
	</div>
	
	<div class="row-fluid">
		<div style="width: 100%;float: left;margin-top: 5px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Adjustment Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">						
						<div class="row-fluid">
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 23.5%">Customer Name</label>	
								<input type="text" style="width: 73%"  id="full_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 23.5%">F/H Name</label>	
								<input type="text" style="width: 73%"  id="father_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 23.5%">Address</label>										
								<textarea rows="1" cols="" style="width: 73%" id="customer_address" disabled="disabled"></textarea>
								
						  	</div>	
						 </div>
						 <div class="row-fluid" style="padding-top: 5px;">
							<div class="span6">
								<label style="width: 48%">Category</label>										
								<select id="customer_category" style="width: 52%;" disabled="disabled">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select> 
						  	</div>	
						  	<div class="span6">
								<label style="width: 40%">Customer Type</label>	
								<select id="isMetered_str" style="width: 59%" disabled="disabled">
						            <option value="">Select Status</option>           
							        <s:iterator  value="%{#application.METERED_STATUS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
					        	</select>																	
						  	</div>
						 </div>
						 <div class="row-fluid" id="month_year_div">							
							<div class="span6" id="month_div">									    
								<label style="width: 48%">Bill Month<m class='man'/></label>
								<select name="bill_month" id="bill_month" style="width: 50%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6" id="year_div">
								<label style="width: 40%">Bill Year<m class='man'/></label>
								<select name="bill_year" id="bill_year" style="width: 53%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
						</div>
						<div class="row-fluid" id="from_to_date_div">							
							 <div class="span6" id="fromDateSpan">
				    			<label id="fromDateLabel" style="width: 48%">Bill Issue Date</label>
								<input type="text" name="issue_date" id="issue_date" style="width: 45%" />
				  			</div>
				  			<div class="span6" id="toDateSpan">
				    			<label id="toDateLabel" style="width: 40%">Due Date</label>
								<input type="text" name="due_date" id="due_date" style="width: 53%" />
				  			</div>
						</div>
						
						<div class="row-fluid" id="sales_adjustment_div">							
							 <div class="span6" id="bill_amount">
				    			<label id="billAmt" style="width: 48%">Bill Amount</label>
								<input type="text" name="bill" id="bill" style="width: 45%" onkeyup="calBillAmount()" />
				  			</div>
				  			<div class="span6" id="check_box">
				    			<label id="billAmt" style="width: 40%">Payment Status</label>
								<select id="payment_status" style="width: 53%;"   name="payment_status" onchange="fetchDisconnectType()">
									<option value="" >Select status</option>									
										<option value="01" >Paid</option>
										<option value="02" >Unpaid</option>		
								</select>
				  			</div>
						</div>
						 <div id="sales_adjustment_div">
							 
							  <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Surcharge Amount</label>	
									<input type="text" style="width: 73%"  id="surcharge_amount" onkeyup="calBillAmount()"/>									
							  	</div>	
							 </div>
							  <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Meter Rent Amount</label>	
									<input type="text" style="width: 73%"  id="meter_amount" onkeyup="calBillAmount()"/>									
							  	</div>	
							 </div>
							 
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Total Bill Amount</label>	
									<input type="text" style="width: 73%"  id="t_Amount" readonly="readonly" />									
									
							  	</div>	
							 </div>
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Total Consumption</label>	
									<input type="text" style="width: 73%"  id="t_consumption"/>									
									
							  	</div>	
							 </div>
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Remarks</label>	
									<textarea rows="4" cols="" style="width: 73%;" id="remarks"></textarea>									
									
							  	</div>	
							 </div>
							 <div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
								<div id="aDiv" style="height: 0px;"></div>
							</div>
						
							<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
								<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveSalesAdjustment()">Adjustment Sales</button>
							</div> 
						 </div>
					
						
						 
				</div>
		 </div>
		</div>
	</div>
</div>

<div style="width: 58%;height: 98%;float: left;margin-left: 10px;" id="right_div">
	<table id="unpaid_bill_grid"></table>
	<div id="unpaid_bill_grid_pager" ></div>	
</div>

<script type="text/javascript">
$("#billing_month").val(getCurrentMonth());
$("#billing_year").val(getCurrentYear());

$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
    	onSelect:function (){getCustomerInfo("",$('#customer_id').val());loadUnpaidBillList();},
}));

function loadUnpaidBillList(){
	if($("#customer_id").val()=="") return;
   	var ruleArray=[["customer_id","status"],["eq","eq"],[$("#customer_id").val(),"1"]];
	var postdata=getPostFilter("unpaid_bill_grid",ruleArray);
   	$("#unpaid_bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,datatype:'json'});
	reloadGrid("unpaid_bill_grid");
}
					
$("#unpaid_bill_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.BillingService&method=getBillListNonMeterSalesAdjustment',
   	jsonReader: {
            repeatitems: false,
            id: "bill_id"
	},
   colNames: ['Customer Id','Bill Id','Bill Month', 'Bill Year','Bill Amount','Adjustment Amount', 'Adjustment Consumption','Payable Amount','Comments','↓'],
   colModel: [{
	                name: 'customer_id',
	                hidden:true
            	},
            	{
	                name: 'bill_id',
	                index: 'bill_id',
	                width:70,
	                align:'center',
	                sorttype: 'string',
	                search: true
            	},
            	{
	                name: 'bill_month',
	                index: 'bill_month',
	                sorttype: "string",
	                width:120,
	                search: true,
	                align:'center'
            	},
            	{
	                name: 'bill_year',
	                index: 'bill_year',
	                sorttype: "string",
	                width:150,
	                search: true,
	                align:'center'
            	},
            	{
	                name: 'billed_amount',
	                index: 'billed_amount',
	                sorttype: "string",
	                width:150,
	                search: true,
	                align:'right'
            	},
            	 
            	{
	                name: 'adjustment_amount',
	                index: 'adjustment_amount',
	                sorttype: "string",
	                width:150,
	                search: true,
	                align:'right'
            	},
            		{
	                name: 'adjustment_consumption',
	                index: 'adjustment_consumption',
	                sorttype: "string",
	                width:150,
	                search: true,
	                align:'right'
            	},
            	{
	                name: 'net_payable_amount',
	                index: 'net_payable_amount',
	                sorttype: "string",
	                width:250,
	                search: true,
	                align:'right'
            	},
            	{
	                name: 'adjustment_comment',
	                index: 'adjustment_comment',
	                sorttype: "string",
	                hidden:true
            	},
            	{ 
            		name: 'Download', 
            		width: 50, 
            		align:'center',
            		hidden:true,
            		formatter:function(){
                          return "<span class='ui-icon ui-icon-circle-arrow-s' style='margin-left:3px;cursor:pointer;'></span>"
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                            return ' onClick="window.location=\'downloadMeteredBill.action?download_type=S&bill_id='+rowObject.bill_id+'\'"';
                    }
                }
            	
        ],
	datatype: 'local',
	height: $("#right_div").height()-70,
	width: $("#right_div").width()+10,
   	pager: '#unpaid_bill_grid_pager',
   	sortname: 'bill_id',
    sortorder: "asc",
	caption: "Dues List/Invoice List",
	onSelectRow: function(id){ 
	
		fetchBillInfo(id,getFieldValueFromSelectedGridRow("unpaid_bill_grid","customer_id"));		
   }
}));
jQuery("#unpaid_bill_grid").jqGrid('navGrid','#unpaid_bill_grid_pager',$.extend({},footerButton,{search:true,refresh:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","unpaid_bill_grid",["full_name","category_name","area_name","mobile"]);

function adjustmentForm(plainFieldMethod){	
	var fields = ["full_name,father_name,address,customer_category,customer_type,invoice_number,month_year,arrear_bill,meter_rent,remarks"];
	plainFieldMethod.apply(this,fields);
}
function fetchBillInfo(bill_id,customer_id){
	
	  $.ajax({
		    url: 'getBillInfoForNonMeterSalesAdjustment.action',
		    type: 'POST',
		    data: {bill_id:bill_id,customer_id:customer_id},
		    cache: false,
		    success: function (response) {
		    	setBillInfo(response);
		    }
		    
		  });	
		  
		 $.ajax({
		    url: 'getOthersAmountInfo.action',
		    type: 'POST',
		    data: {bill_id:bill_id,customer_id:customer_id},
		    cache: false,
		    success: function (response) {
		    	setOthersInfo(response);
		    }
		    
		  }); 
}

function setBillInfo(bill){
	//$("#invoice_no").val(bill.invoice_no);
	$("#month_year").val(bill.bill_month+", "+bill.bill_year);
	$("#payable_amount").val(bill.net_payable_amount-bill.adjustment_amount);
	$("#adjustment_amount").val(Math.abs(bill.adjustment_amount));
	if(bill.adjustment_amount!=0){
		if(bill.adjustment_amount>0)
		 $("#adjustment_amount_sign").val("+");
		else
		 $("#adjustment_amount_sign").val("-");
	}
	$("#new_payable_amount").val(bill.net_payable_amount);
	
	$("#total_consumption").val(bill.total_consumption-bill.adjustment_consumption);
	$("#adjustment_consumption").val(Math.abs(bill.adjustment_consumption));
	if(bill.adjustment_consumption!=0){
		if(bill.adjustment_consumption>0)
		 $("#adjustment_consumption_sign").val("+");
		else
		 $("#adjustment_consumption_sign").val("-");
	}
	$("#new_total_consumption").val(bill.total_consumption);
	$("#bill_id").val(bill.bill_id);
	$("#remarks").val(bill.adjustment_comment);
}
function setOthersInfo(others){
$("#othersTable").find("tr:gt(0)").remove();

	var comments24=others.totalOthersCommentString;
	var amounts=others.totalOthersAmountString;
	comments24=comments24.toString();
	var commentsArr=comments24.split("#ifti#");
	var amountsArr=amounts.split("#ifti#");

	for(var i=0;i<commentsArr.length;i++)
	{
		$("#othersTable").append('<tr valign="top"><th scope="row" width="22%"></th><td width="75"><input type="text" name="othersComment[]" value="'+commentsArr[i]+'" style="width:40%" onblur="calcuateTotalOthersAmount()"/> &nbsp;&nbsp;&nbsp; <input type="text" name="othersAmount[]" value="'+amountsArr[i]+'"  style="width:40%;text-align:right;padding-right:5px;" onblur="calcuateTotalOthersAmount()"/> &nbsp; <a href="javascript:void(0);" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></td></tr>');
	}
	calcuateTotalOthersAmount();
	
}

function setNewPayableAmount(){
var newPayableAmount=0;
if($("#adjustment_amount_sign").val()=="") return;
if($("#adjustment_amount_sign").val()=="+")
 newPayableAmount=parseFloat($("#payable_amount").val(),10)+parseFloat($("#adjustment_amount").val(),10);
else if($("#adjustment_amount_sign").val()=="-")
 newPayableAmount=parseFloat($("#payable_amount").val(),10)-parseFloat($("#adjustment_amount").val(),10);	
 $("#new_payable_amount").val(newPayableAmount);
}

function setNewTotalConsumption(){
var newTotalConsumption=0;
if($("#adjustment_consumption_sign").val()=="") return;
if($("#adjustment_consumption_sign").val()=="+")
{
 newTotalConsumption=parseFloat($("#total_consumption").val())+parseFloat($("#adjustment_consumption").val());
} else if($("#adjustment_consumption_sign").val()=="-")
{
 newTotalConsumption=parseFloat($("#total_consumption").val())-parseFloat($("#adjustment_consumption").val());	
}

 $("#new_total_consumption").val(newTotalConsumption);
}

/* function saveSalesAdjustment(){
alert($("#bill_month").val());
alert($("#payment_status").val());

$.ajax({
		url: 'saveSalesAdjustmentInfo.action',
		    type: 'POST',
		    data: {customer_id:$("#customer_id").val(),bill_month:$("#bill_month").val(),bill_year:$("#bill_year").val(),issue_date:$("#issue_date").val(),due_date:$("#due_date"), bill_amount:$("#bill_amount").val(),surcharge_amount:$("#surcharge_amount").val(),meter_rent:$("#meter_amount").val(),total_amount:$("#t_amount").val(),total_consumption:$("#t_consumption").val(),payment_status:$("#payment_status").val()},
		    cache: false,		    
		    success: function (response) {
		    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);	
		    	reloadGrid("unpaid_bill_grid");
		    }

}); */
//}

function saveSalesAdjustment(){
	$.ajax({
		    url: 'saveSalesAdjustmentInfo.action',
		    type: 'POST',
		    data: {customer_id:$("#customer_id").val(),bill_month:$("#bill_month").val(),bill_year:$("#bill_year").val(),issue_date:$("#issue_date").val(),due_date:$("#due_date").val(), bill_amount:$("#bill").val(),surcharge_amount:$("#surcharge_amount").val(),meter_rent:$("#meter_amount").val(),total_consumption:$("#t_consumption").val(),payment_status:$("#payment_status").val()},
		    cache: false,
		    success: function (response) {
		    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);	
		    	reloadGrid("unpaid_bill_grid");
		    }
		    
		  });	
}
function changeSelection(selectionType){
	if(selectionType=="adjustment"){
	 $("#sales_adjustment_div").show();
	 $("#othersAmount_div").hide();
	 }
	else{
	 $("#sales_adjustment_div").hide();
	 $("#othersAmount_div").show();
	
	}
}
$(document).ready(function(){
		$("#othersTable").append('<tr valign="top"><th scope="row" width="22%"></th><td width="75"><input type="text" name="othersComment[]" value="" style="width:40%" onkeyup="calcuateBillAmount()"/> &nbsp;&nbsp;&nbsp; <input type="text" name="othersAmount[]" value=""  style="width:40%;text-align:right;padding-right:5px;" onkeyup="calcuateBillAmount()"/> &nbsp; <a href="javascript:void(0);" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></td></tr>');

    $("#othersTable").on('click','.remCF',function(){
        $(this).parent().parent().remove();
        calcuateTotalOthersAmount();
    });
});

function saveOthersAmount(){
	var othersComment = $("input[name='othersComment\\[\\]']").map(function(){return $(this).val();}).get();
	var othersAmount = $("input[name='othersAmount\\[\\]']").map(function(){return $(this).val();}).get();
	
	calcuateTotalOthersAmount();
	$.ajax({
		    url: 'saveOthersAmountForBilling.action',
		    type: 'POST',
		    data: {bill_id:$("#bill_id").val(),customer_id:$("#customer_id").val(),others_comments:othersComment.join("#ifti#"),others_amount:othersAmount.join("#ifti#"),total_others_amount:$("#totalOthersAmount").val(),total_others_comment:$("#totalComment").html()},
		    cache: false,
		    success: function (response) {
		    	$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);	
		    	reloadGrid("unpaid_bill_grid");
		    }
		    
		  });	
		  
}

function calcuateBillAmount(){
	var billAmt = $("#bill_amt").map(function(){return $(this).val();}).get();
	var surchargeAmt = $("#surcharge_amount").map(function(){return $(this).val();}).get();
	var meterRentAmt = $("#meter_amount").map(function(){return $(this).val();}).get();
	var totalAmount=0;
		totalAmount=parseFloat(billAmt)+parseFloat(surchargeAmt)+parseFloat(meterRentAmt);
	$("#totalOthersAmount").val(totalAmount);
}

function calcuateTotalOthersAmount(){
	var othersComment = $("input[name='othersComment\\[\\]']").map(function(){return $(this).val();}).get();
	var othersAmount = $("input[name='othersAmount\\[\\]']").map(function(){return $(this).val();}).get();
	var totalAmount=0;
	var totalComments="";
	for(var i=0;i<othersAmount.length;i++)
		totalAmount=totalAmount+parseFloat(othersAmount[i]==""?0:othersAmount[i]);
	$("#totalOthersAmount").val(totalAmount);	
	
	console.log(othersComment);
	console.log(othersComment.length);
	for(var i=0;i<othersComment.length;i++)
		totalComments=totalComments+' '+othersComment[i]+(othersComment[i]==""?"":",");
	$("#totalComment").html(totalComments);
}

focusNext("customer_id");

function calBillAmount(){	
	var totalAmount = parseFloat($("#bill").val(),10)+parseFloat($("#surcharge_amount").val(),10)+parseFloat($("#meter_amount").val(),10);
	
	$("#t_Amount").val(totalAmount);
}

Calendar.setup({
    inputField : "due_date",
    trigger    : "due_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  Calendar.setup({
    inputField : "issue_date",
    trigger    : "issue_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
</script>
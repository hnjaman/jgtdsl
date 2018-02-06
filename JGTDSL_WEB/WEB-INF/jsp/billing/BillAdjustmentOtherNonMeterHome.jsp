<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("billAdjustmentOtherHome.action");
	setTitle("Bill Adjustment");
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
						 <div class="row-fluid">
							<div class="span6">
								<label style="width: 48%">Invoice</label>	
								<input type="text" style="width: 46%"  id="invoice_no" disabled="disabled"/>
								<input type="hidden" id="bill_id" />										
								
						  	</div>	
						  	<div class="span6">
								<label style="width: 40%">Month, Year</label>	
								<input type="text" style="width: 54%"  id="month_year" disabled="disabled"/>																	
						  	</div>
						 </div>
						 <div id="sales_adjustment_div">
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Payable Amount</label>	
									<input type="text" style="width: 73%"  id="payable_amount" disabled="disabled"/>									
									
							  	</div>	
							 </div>
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Adjustment Amount</label>	
									<input type="button" name="+" value="+" onclick="$('#adjustment_amount_sign').val('+');setNewPayableAmount();" style="width: 32px;height: 26px;margin-bottom: 0px;font-weight: bold;border:2px solid red;" /> / 
									<input type="button" name="-" value="-" onclick="$('#adjustment_amount_sign').val('-');setNewPayableAmount();" style="width: 32px;height: 26px;margin-bottom: 0px;font-weight: bold;border:2px solid green;" />
									<input type="text" style="width: 4%;text-align: center;font-weight: bold;"  id="adjustment_amount_sign" readonly/>
									<input type="text" style="width: 51%"  id="adjustment_amount" onkeyup="setNewPayableAmount()"/>									
									
							  	</div>	
							 </div>
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%"><b>New</b> Payable Amount</label>	
									<input type="text" style="width: 73%"  id="new_payable_amount" readonly="readonly" />									
									
							  	</div>	
							 </div>
							  <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Total Consumption</label>	
									<input type="text" style="width: 73%"  id="total_consumption" disabled="disabled"/>									
									
							  	</div>	
							 </div>
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%">Adjustment Consumption</label>	
									<input type="button" name="+" value="+" onclick="$('#adjustment_consumption_sign').val('+');setNewTotalConsumption();"  style="width: 32px;height: 26px;margin-bottom: 0px;font-weight: bold;border:2px solid red;" /> / 
									<input type="button" name="-" value="-" onclick="$('#adjustment_consumption_sign').val('-');setNewTotalConsumption();" style="width: 32px;height: 26px;margin-bottom: 0px;font-weight: bold;border:2px solid green;" />
									<input type="text" style="width: 4%;text-align: center;font-weight: bold;"  id="adjustment_consumption_sign" readonly/>
									<input type="text" style="width: 51%"  id="adjustment_consumption" onkeyup="setNewTotalConsumption()"/>									
									
							  	</div>	
							 </div>
							 <div class="row-fluid">
								<div class="span12">
									<label style="width: 23.5%"><b>New</b> Total Consumption</label>	
									<input type="text" style="width: 73%"  id="new_total_consumption" readonly="readonly" />									
									
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
								<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveAdjustment()">Adjustment Bill/Invoice</button>
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


function saveAdjustment(){
	if($("#adjustment_amount_sign").val()=="" || $("#adjustment_amount").val()=="" || $("#adjustment_amount_sign").val()=="") return;
	
	$.ajax({
		    url: 'saveNonMeterSalesAdjustmentInfo.action',
		    type: 'POST',
		    data: {bill_id:$("#bill_id").val(),customer_id:$("#customer_id").val(),payable_amount:$("#payable_amount").val(),new_payable_amount:$("#new_payable_amount").val(),adjustment_sign:$("#adjustment_amount_sign").val(),adjustment_amount:$("#adjustment_amount").val(),total_consumption:$("#total_consumption").val(),adjustment_consumption:$("#adjustment_consumption").val(),consumption_sign:$("#adjustment_consumption_sign").val(),new_total_consumption:$("#new_total_consumption").val(),remarks:$("#remarks").val(),metered_status:$("#isMetered_str").val()},
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
	$(".addCF").click(function(){
		$("#othersTable").append('<tr valign="top"><th scope="row" width="22%"></th><td width="75"><input type="text" name="othersComment[]" value="" style="width:40%" onkeyup="calcuateTotalOthersAmount()"/> &nbsp;&nbsp;&nbsp; <input type="text" name="othersAmount[]" value=""  style="width:40%;text-align:right;padding-right:5px;" onkeyup="calcuateTotalOthersAmount()"/> &nbsp; <a href="javascript:void(0);" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></td></tr>');
	});
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
</script>
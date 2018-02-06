<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
<s:if test="customer.connectionInfo.isMetered.label==@org.jgtdsl.enums.MeteredStatus@NONMETERED.label">
//setPanelCaption("rightSpan_caption",'Billing Information for the Month of <s:property value="nmBilling.str_month"/>, <s:property value="nmBilling.year"/>');
</s:if>
<s:if test="customer.connectionInfo.isMetered.label==@org.jgtdsl.enums.MeteredStatus@METERED.label">
//setPanelCaption("rightSpan_caption",'Billing Information for the Month of <s:property value="mBilling.str_month"/>, <s:property value="mBilling.year"/>');
</s:if>
</script>
<style type="text/css">
.tabs-left > .nav-tabs > li > a:hover
{
border-color:transparent;
}
</style>

<form id="billingForm" name="billingForm">
<div style="margin-top: 20px;">
	<s:if test="customer.connectionInfo.isMetered.label==@org.jgtdsl.enums.MeteredStatus@METERED.label">
			<p class="formSep" style="padding: 0px;">
				<div style="float:left;width:50%">
				<label class="muted" style="width: 25%">Customer Id</label>
				<font style="font-size: 18px;font-weight: bold;"><s:property value="mBilling.customer_id"/></font>
				</div>
				<div style="float:left;width:50%;">
				<input type="hidden" name="mBilling.customer_id" value="<s:property value='mBilling.customer_id'/>" />
				<label class="muted" style="width: 25%;">Full Name</label>
				<s:property value="mBilling.customer_name"/>
				</div>
			</p>
			<p class="formSep" style="padding: 0px;">
				<div style="float:left;width:50%">
				<label class="muted" style="width: 25%">Category</label>
				<s:property value="customer.customer_category_name"/>
				</div>
				<div style="float:left;width:50%;">
				<label class="muted" style="width: 25%;">Area</label>
				<s:property value="customer.area_name"/>
				</div>
			</p>
				
			
				<div class="tabbable tabbable-bordered">
                     <ul class="nav nav-tabs" >
                         <li class="active"><a data-toggle="tab" href="#tb3_a">Summary</a></li>
                         <li><a data-toggle="tab" href="#tb3_b">Meter-1</a></li>
                     </ul>
                     <div class="tab-content">
                         <div id="tb3_a" class="tab-pane active">
                             <p>
                             	<table class="table table-striped table-bordered table-condensed table-hover">
                                                    <thead>
                                                        <tr>
                                                            <th>id</th>
                                                            <th>Meter</th>
                                                            <th>Duration</th>
                                                            <th>Consumption</th>
                                                            <th>Rate</th>
                                                            <th>Amount</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                    <s:iterator value="mBilling.readingList" status="idx">
													 	<tr>
                                                            <td><s:property value="#idx.count" /></td>
                                                            <td><s:property value="meter_no"/></td>
                                                            <td><s:property value="prev_reading_date"/> <br/>to<br/> <s:property value="curr_reading_date"/></td>
                                                            <td><s:property value="total_consumption"/></td>
                                                            <td style="text-align: right;"><s:property value="rate"/></td>
                                                            <td style="text-align: right;">
                                                            	<span id="total<s:property value='#idx.count' />"></span>
                                                            	<script type="text/javascript">
                                                            	  var amount=parseFloat(<s:property value="total_consumption"/>)*parseFloat(<s:property value="rate"/>);
                                                            	  $("#total<s:property value='#idx.count' />").html(amount);
                                                            	</script>
                                                            </td>
                                                        </tr>
                                                        
													</s:iterator>
														<tr>
															<td></td>
															<td colspan="3"><b>Vat Rebate</b></td>
															<td style="text-align: right;">10%</td>
															<td style="text-align: right;">1000</td>
														</tr>
														<tr>
															<td></td>
															<td colspan="3"><b>Payable Amount</b></td>
															<td style="text-align: right;"></td>
															<td style="text-align: right;">
															
															</td>
														</tr>
                                                        
                                                    </tbody>
                                                </table>
                             
                             </p>
                         </div>
                         <s:iterator value="mBilling.readingList">
                         <div id="tb3_b" class="tab-pane">                             
                             <pre class="sepH_c">                             
                             <table width="100%" style="border-collapse: collapse;" border="0">
                             	<tr>
                             		<td width="15%" class="tdS1">Meter</td><td width="20%"><s:property value='meter_no' /></td>
                             		<td width="15%" class="tdS1">M. Type</td><td width="20%"><s:property value='str_meter_type' /></td>
                             		<td width="10%" class="tdS1">Rate</td><td width="20%"><s:property value='rate' /></td>
                             	</tr>
                             	<tr>
                             		<td class="tdS1">Purpose</td><td><s:property value='str_bill_purpose' /></td>
                             		<td class="tdS1">Pre. Reading</td><td><s:property value='prev_reading_date' /></td>
                             		<td class="tdS1">Date</td><td><s:property value='prev_reading_date' /></td>
                             	</tr>
                             	<tr>
                             		<td class="tdS1">Curr. Reading</td><td><s:property value='curr_reading' /></td>
                             		<td class="tdS1">Date</td><td><s:property value='curr_reading_date' /></td>
                             		<td class="tdS1">Diff.</td><td><s:property value='difference' /></td>
                             	</tr>
                             	<tr>
                             		<td class="tdS1">HHV/NHV(+/-)</td><td><s:property value='hv' /></td>
                             		<td class="tdS1">Total Cons.</td><td><s:property value='total_consumption' /></td>
                             		<td class="tdS1"></td><td></td>
                             	</tr>
                             	<tr>
                             		<td class="tdS1">Actual Cons.</td><td><s:property value='actual_consumption' /></td>
                             		<td class="tdS1">Meter Rent</td><td><s:property value='meter_rent' /></td>
                             		<td class="tdS1">P. Factor</td><td><s:property value='pressure_factor' /></td>
                             	</tr>
                             	<tr>
                             		<td class="tdS1">T. Factor</td><td><s:property value='temperature_factor' /></td>
                             		<td class="tdS1">Remarks</td><td style="text-align: left;" colspan="2"><s:property value='remarks' /></td>                             		
                             	</tr>
                             	<tr>
                             		<td class="tdS1">Min. Cons.</td><td><s:property value='min_load' /></td>
                             		<td class="tdS1">Max. Cons.</td><td><s:property value='max_load' /></td>
                             		<td class="tdS1"></td><td></td>
                             	</tr>
                             </table>
                             </pre>
                         </div>
                         </s:iterator>
                     </div>
           		</div>
           		<p class="formSep" style="padding: 0px;padding-top: 5px;">
					<label class="muted" style="width: 25%"></label>
					<s:if test="mBilling.status==null">
						<button class="btn btn-primary">Initialize</button>
						<button class="btn btn-danger disabled" type="button">Process</button>
						<button class="btn btn-success disabled" type="button">Download (PDF)</button>
					</s:if>		
					<s:if test="mBilling.str_status==@org.jgtdsl.enums.BillStatus@INITIALIZED.label">
						<button class="btn btn-primary disabled" type="button">Initialize</button>
						<button class="btn btn-danger">Process</button>
						<button class="btn btn-success">Download (PDF)</button>
					</s:if>
					<s:if test="mBilling.str_status==@org.jgtdsl.enums.BillStatus@PROCESSED.label">
						<button class="btn btn-primary disabled" type="button">Initialize</button>
						<button class="btn btn-danger disabled" type="button">Process</button>
						<button class="btn btn-success">Download (PDF)</button>
					</s:if>
				</p>
	</s:if>
	<s:if test="customer.connectionInfo.isMetered.label==@org.jgtdsl.enums.MeteredStatus@NONMETERED.label">
		<p class="formSep" style="padding: 0px;">
			<label class="muted" style="width: 25%">Customer Id</label>
			<font style="font-size: 18px;font-weight: bold;"><s:property value="nmBilling.customer_id"/></font>
			<input type="hidden" name="nmBilling.customer_id" value="<s:property value='nmBilling.customer_id'/>" />
			
		</p>
		<p class="formSep" style="padding: 0px;">
			<label class="muted" style="width: 25%">Customer Name</label>
			<s:property value="nmBilling.customer_name"/>
		</p>	
	<s:if test="nmBilling.str_status==null">
		<p class="formSep" style="padding: 0px;">
			<label class="muted" style="width: 25%">Burner Quantity(Single)</label>
			<input type="text" value='<s:property value="nmBilling.single_burner_qnt"/>' name="nmBilling.single_burner_qnt" id='single_burner_qnt' style="width: 20px;text-align: center;" readonly="readonly"/>
			&nbsp;&nbsp;x&nbsp;&nbsp;
			<input type="text" value='<s:property value="nmBilling.single_burner_rate"/>' name="nmBilling.single_burner_rate" id='single_burner_rate' style="width: 40px;text-align: center;" readonly="readonly"/>
			&nbsp;&nbsp;=&nbsp;&nbsp;
			<input type="text" id='single_burner_amount' style="width: 60px;text-align: center;" readonly="readonly"/>
		</p>
		<p class="formSep" style="padding: 0px;">
			<label class="muted" style="width: 25%">Burner Quantity(Double)</label>
			<input type="text" value='<s:property value="nmBilling.double_burner_qnt"/>' name="nmBilling.double_burner_qnt" id='double_burner_qnt' style="width: 20px;text-align: center;" readonly="readonly"/>
			&nbsp;&nbsp;x&nbsp;&nbsp;
			<input type="text" value='<s:property value="nmBilling.double_burner_rate"/>' name="nmBilling.double_burner_rate"  id='double_burner_rate' style="width: 40px;text-align: center;" readonly="readonly"/>
			&nbsp;&nbsp;=&nbsp;&nbsp;
			<input type="text"  id='double_burner_amount' style="width: 60px;text-align: center;" readonly="readonly"/>
		</p>
		<p class="formSep" style="padding: 0px;">
			<label class="muted" style="width: 25%">Bill Amount</label>
			<input type="text" value='<s:property value="nmBilling.bill_amount"/>' name="nmBilling.bill_amount"  id='bill_amount' style="width: 60px;text-align: center;font-size: 18px;font-weight: bold;" readonly="readonly"/>
		</p>
		<p class="formSep" style="padding: 0px;">
			<label class="muted" style="width: 25%">Issue Date</label>
			<input type="text" value='' style="width: 18%;text-align: center;" id="issue_date" name="nmBilling.issue_date"/>
		</p>
		<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Status</label>
		<font style="font-size: 18px;font-weight: bold;">Not yet Initialized</font>
		<input type="hidden" name="nmBilling.str_status" value="" />
		<input type="hidden" name="nmBilling.str_month" value="<s:property value='nmBilling.month_id'/>" />
		<input type="hidden" name="nmBilling.year" value="<s:property value="nmBilling.year"/>" />
		
	</p>
	</s:if>
	<s:if test="nmBilling.str_status!=null">
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Burner Quantity</label>
		<s:property value="nmBilling.double_burner_qnt"/>
	</p>
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Rate</label>
		<s:property value="nmBilling.double_burner_rate"/>
	</p>
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Bill Amount</label>
		<font style="font-size: 18px;font-weight: bold;"><s:property value="nmBilling.bill_amount"/></font>
	</p>
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Issue Date</label>
		<s:property value="nmBilling.issue_date"/>
	</p>
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Due Date</label>
		<s:property value="nmBilling.due_date"/>
	</p>
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Prepared On</label>
		<s:property value="nmBilling.prepared_on"/>
	</p>
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Prepared By</label>
		<s:property value="nmBilling.prepared_by"/>
	</p>
	<p class="formSep" style="padding: 0px;">
		<label class="muted" style="width: 25%">Status</label>
		<font style="font-size: 18px;font-weight: bold;"><s:property value="nmBilling.str_status"/></font>
		<input type="hidden" name="nmBilling.str_status" value="<s:property value='nmBilling.status_id'/>" />
		<input type="hidden" name="nmBilling.str_month" value="<s:property value='nmBilling.month_id'/>" />
		<input type="hidden" name="nmBilling.year" value="<s:property value="nmBilling.year"/>" />
		<input type="hidden" name="nmBilling.bill_id" value="<s:property value="nmBilling.bill_id"/>" />
		
	</p>
	</s:if>
	
	<p class="formSep" style="padding: 0px;padding-top: 10px;">
		<label class="muted" style="width: 25%"></label>
		<s:if test="nmBilling.status==null">
			<button class="btn btn-primary">Initialize</button>
			<button class="btn btn-danger disabled" type="button">Process</button>
			<button class="btn btn-success disabled" type="button">Download (PDF)</button>
		</s:if>		
		<s:if test="nmBilling.str_status==@org.jgtdsl.enums.BillStatus@INITIALIZED.label">
			<button class="btn btn-primary disabled" type="button">Initialize</button>
			<button class="btn btn-danger">Process</button>
			<button class="btn btn-success">Download (PDF)</button>
		</s:if>
		<s:if test="nmBilling.str_status==@org.jgtdsl.enums.BillStatus@PROCESSED.label">
			<button class="btn btn-primary disabled" type="button">Initialize</button>
			<button class="btn btn-danger disabled" type="button">Process</button>
			<button class="btn btn-success">Download (PDF)</button>
		</s:if>
	</p>
	
	</s:if>	
</div>
</form>

<script type="text/javascript">
//setPanelCaption("rightSpan_caption",jsCaption.CAPTION_INFORMATION);

function fetchGasPrice(date)
{

 $.ajax({
   		  type: 'POST',
   		  url: 'getGasPrice.action',
   		  data: { meter_status:'NonMetered',target_date:date,customer_id:$("#customer_id").val()},
   		  success:function(data)
   		  {
   		  if(data==null)
			{
				alert("No Available Tariff exists.");
			}
			else
			{
				var single_tariff=data.single_tariff;
				var double_tariff=data.double_tariff;
		   		
		   		$("#single_burner_rate").val(single_tariff.price);
		   		$("#double_burner_rate").val(double_tariff.price);
		   		
		   		$("#single_burner_amount").val($("#single_burner_qnt").val()*$("#single_burner_rate").val());		   		
		   		$("#double_burner_amount").val($("#double_burner_qnt").val()*$("#double_burner_rate").val());
		   		$("#bill_amount").val(parseInt($("#single_burner_amount").val())+parseInt($("#double_burner_amount").val()));
   		    }			
   		  },
   		  error:function(){
   			//$('#aDiv').empty();
   			//$("#btn_save").removeAttr("disabled");
   		  }
   	});
   	
}

$('form#billingForm').unbind("submit");

$("form#billingForm").submit(function(event){

  event.preventDefault();
  var formData = new FormData($(this)[0]);
 
  $.ajax({
    url: 'saveBillingInfo.action',
    type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response) {

      
      $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, {
	                    zIndex: 1500,
	                    width:450,
	                     beforeOpen: centerInfoDialog,
	                     afterOpen:disableOnClick,
	                     onClose: function () {
	                    	$('.ui-widget-overlay').unbind( "click" );	                    	
	                    	//cleanAllField();
	                    	//var customer_id=$("#customer_id").val();
							//var actionUrl=sBase+"getSecurityAndOtherDepositList.action?customer_id="+customer_id;
							//$("#depositListTbl").html(jsImg.SETTING).load(actionUrl);	
							//resetDepositForm();                    	
	                        return true; // allow closing
	                    }
	                    
    });
    
    }
    
  });
 
  return false;
});



$('#rightSpan_caption').addClass('animated zoomIn');
Calendar.setup({
        inputField : "issue_date",
        trigger    : "issue_date",
		eventName : "focus",
        onSelect   : function() { this.hide();fetchGasPrice($("#issue_date").val());},
        showTime   : 12,
        dateFormat : "%d-%m-%Y %H:%M",
		showTime : false
      });
</script>

<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	navCache("editSurcharge.action");
	setTitle("Edit Surcharge (Non-meter)");
</script>
<link href="/JGTDSL_WEB/resources/css/page/meterReading.css"
	rel="stylesheet" type="text/css" />
<style>
input[type="radio"],input[type="checkbox"] {
	margin-top: -3px !important;
}

.alert {
	padding-top: 4px !important;
	padding-bottom: 4px !important;
}

.ui-icon,.ui-widget-content .ui-icon {
	cursor: pointer;
}

.sFont {
	font-size: 12px;
}
</style>
<div class="meter-reading" style="width: 100%;height: 50%;">
	<div class="row-fluid">
		<div class="span9" id="rightSpan">
			<div class="w-box-header">
				<h4 id="rightSpan_caption">Edit Surcharge (Non-meter)</h4>
			</div>
			<div class="w-box-content" style="padding: 10px;" id="content_div">
				<div class="row-fluid">
					<div class="row-fluid">
						<div class="span9">
							<label style="width: 40%">Customer ID</label> <input type="text"
								name="customer_id" id="customer_id" style="width: 54.5%" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span9">
							<label style="width: 40%">Year</label> <input type="text"
								name="year" id="year" style="width: 54.5%" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span9">
							<label style="width: 40%">Month</label> <select name="month"
								id="month" style="width: 56%;margin-left: 0px;">
								<option value="">Select Month</option>
								<s:iterator value="%{#application.MONTHS}">
									<option value="<s:property value='id'/>">
										<s:property value="label" />
									</option>
								</s:iterator>
							</select>
						</div>
					</div>

				</div>

				<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
					<div id="aDiv" style="height: 0px;"></div>
				</div>
				<div class="formSep sepH_b"
					style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">
					<table width="100%">
						<tr>

							<td style="width: 60%" align="right">
								<button class="btn" id="btn_save"
									onclick="fetchBillForUpdate()">Search</button>
								<button class="btn btn-danger" type="button" id="btn_cancel"
									onclick="callAction('blankPage.action')">Cancel</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="row-fluid" >
		<div class="span12" id="rightSpan">
			<div class="w-box-content" id="content_div">
				<form id="formForUpdateSurchargeNM" name="formForUpdateSurchargeNM">
					<table border="10" style="background-color:D1E9EF;">
						<TR>
							<TH rowspan="2">Bill ID</TH>
							<TH rowspan="2" style="width: 8%">Description</TH>
							<TH rowspan="2">Billed Amount</TH>
							<TH rowspan="2">Surcharge Amount</TH>
							<TH rowspan="2">Collected Billed Amount</TH>
							<TH rowspan="2">Collected Surcharge</TH>
							<TH rowspan="2">Due Date</TH>
							<TH rowspan="2" style="width: 18%">Bank, Branch</TH>
							<TH rowspan="2">Collection Date</TH>

						</TR>
						<TR></TR>
						<TR>
							<TD><INPUT TYPE="TEXT" NAME="cl.entry_type" id="entry_type"
								style="width: 90%; text-align:center;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.particulars" id="particulars"
								style="width: 90%; text-align:center;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.sales_amount" id="sales_amount"
								style="width: 90%; text-align:center;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.surcharge" id="surcharge"
								style="width: 90%; text-align:center;background-color: #00cc99;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.credit_amount" id="credit_amount"
								style="width: 90%; text-align:center;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.credit_surcharge" id="credit_surcharge"
								style="width: 90%; text-align:center;background-color: #00cc99;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.due_date" id="due_date"
								style="width: 90%; text-align:center;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.bank_name" id="bank_name"
								style="width: 95%; text-align:center;"></TD>
							<TD><INPUT TYPE="TEXT" NAME="cl.issue_paid_date" id="issue_paid_date"
								style="width: 90%; text-align:center;"></TD>

						</TR>
						
					</table>
					<div style="text-align:center;">
					<P>
						<INPUT  class="btn btn-primary" VALUE="Update" type="button" id="update_nm_ledger" onclick="updateNMSurcharge()">
					</P>
					</div>
				</FORM>
			</div>
		</div>
	</div>
	
	<div id="show_msg" style="text-align:center;"></div>

</div>


<p style="clear: both;margin-top: 5px;"></p>

<script type="text/javascript">
	var prev_surcharge=0, prev_coll_surcharge=0;
	function fetchBillForUpdate() {

		if ($("#customer_id").val() == "") {
			alert("Please enter customer id");
			return;
		}

		if ($("#month").val() == "") {
			alert("Please enter a valid month");
			return;
		}

		if ($("#year").val() == "") {
			alert("Please enter a valid year");
			return;
		}
		
		$("#show_msg").html("");
	
		

		$.ajax({
			type : "POST",
			url : "getNMLedgerByMonthYear",
			dataType : 'text',
			async : false,
			data : {
				customer_id : $("#customer_id").val(),
				year : $("#year").val(),
				month : $("#month").val()
			}
		}).done(function(msg) {
		    var info = JSON.parse(msg);
			
			$("#entry_type").val(info[0]["entry_type"]);
			$("#entry_type").prop('readonly', true);
			$("#particulars").val(info[0]["particulars"]);
			$("#particulars").prop('readonly', true);
			$("#sales_amount").val(info[0]["sales_amount"]);
			$("#sales_amount").prop('readonly', true);
			$("#surcharge").val(info[0]["surcharge"]);
			prev_surcharge=info[0]["surcharge"];
			$("#credit_amount").val(info[0]["credit_amount"]);
			$("#credit_amount").prop('readonly', true);
			$("#credit_surcharge").val(info[0]["credit_surcharge"]);
			prev_coll_surcharge=info[0]["credit_surcharge"];
			$("#due_date").val(info[0]["due_date"]);
			$("#due_date").prop('readonly', true);
			$("#bank_name").val(info[0]["bank_name"]);
			$("#bank_name").prop('readonly', true);
			$("#issue_paid_date").val(info[0]["issue_paid_date"]);
			$("#issue_paid_date").prop('readonly', true);
			
			
			
			
			
			$("#stat_div").hide();
			//$("#detailDiv").html(msg);
		}).always(function() {

		}).fail(function(data) {
			if (data.responseCode)
				alert(data.responseCode);
		});

	}
	
	//surcharge can only be a positive integer. 
	//If user enter any value other than positive integer-
	// We give an alert and show the prev-surcharge in the input field
	
	$("#surcharge").keyup(function(e){
	if(e.keyCode>= 96 && e.keyCode<=105){
	}else{
	alert("Plese enter a valid positive number");
	$("#surcharge").val(prev_surcharge);
	}
    });
    
    //credit-surcharge can only be a positive integer. 
	//If user enter any value other than positive integer-
	// We give an alert and show the prev-collected-surcharge in the input field
    
    $("#credit_surcharge").keyup(function(e){
	if(e.keyCode>= 96 && e.keyCode<=105){
	}else{
	alert("Plese enter a valid positive number");
	$("#credit_surcharge").val(prev_coll_surcharge);
	}
    });
	//checking for paste
	
	$("#surcharge").on('paste', function () {
    //var element = this;
    setTimeout(function () {
    //var text = $(element).val();
    alert("Input number from keyboard");
    $("#surcharge").val(prev_surcharge);
    }, 100);
    });
	
	$("#credit_surcharge").on('paste', function () {
    //var element = this;
    setTimeout(function () {
    //var text = $(element).val();
    alert("Input number from keyboard");
    $("#credit_surcharge").val(prev_coll_surcharge);
    }, 100);
    });



	//for update surcharge
	function updateNMSurcharge(){
		var form = document.getElementById('formForUpdateSurchargeNM');
		var formData = new FormData(form);
		  $.ajax({
		    url: 'updateNMSurcharge.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		        
		        
		        $("#show_msg").html(response);
		    	
		    	
				var fields = ["entry_type","issue_paid_date","sales_amount","surcharge","credit_surcharge","credit_amount","due_date","bank_name","particulars"];
		    	clearField.apply(this,fields);

		    
		    	//$("#msg_div").html(response.message);		       
		   }
		  });		
}
	
</script>

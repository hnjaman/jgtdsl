unbindKeyPress();

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "collection_date",
    trigger    : "collection_date",
    onSelect   : function() { this.hide(); }
	}));

function selectvalue(e){
    e = e || event;

    var key = e.which || e.keyCode;

    if(!e.shiftKey && key >= 48 && key <= 57){
        var option = this.options[key - 48];
        if(option){
            option.selected = "selected";
        }
    }
}
var collection;
var segmentList;
function getInstallmentCollectionInfo(installmentId){
	if(installmentId=="") return;
	$.ajax({
		  type: 'POST',
		  data:{"installmentId":installmentId},
		  url: 'installmentCollection.action',
		  success:function(data)
		  {
			collectionForm(clearField);
			collection=data.collection;
			$("#installmentId").val(collection.installmentId);
			
			$("#customer_id").val(collection.customerId);
			$("#customer_name").val(collection.customerName);
			$("#customer_type").val(collection.customerType);
			$("#customer_category").val(collection.customerCategory);
			$("#is_metered_name").val(collection.isMetered);
			$("#phone").val(collection.phone);
			$("#mobile").val(collection.mobile);
			
			$("#installment_id").val(collection.installmentId);
			$("#installment_serial").val(collection.installmentSerial);
			$("#installment_month_year").val(collection.installmentDescription);
			$("#installment_principal").val(collection.principal);
			$("#installment_surcharge").val(collection.surcharge);
			$("#installment_meter_rent").val(collection.meterRent);
			$("#installment_total").val(collection.total);

			
			segmentList=data.billSegments;
			$("#installment_segment_table").find("tr:gt(0)").remove();
			$("#installment_collection_dtl_table").find("tr:gt(0)").remove();
			
			for(var i=0;i<segmentList.length;i++){
				$("#installment_segment_table tbody").append(getBillSegmentRow(segmentList[i]));
			}
									
			if(collection.collectionStatusName=="Collected"){
			
				$("#collection_date").val(collection.collectionDate);
				$("#collected_amount").val(collection.collectedAmount);
				$("#tax_amount").val(collection.taxAmount);
				$("#btn_save").hide();
				
				$("#bank_id").val(collection.bankId);
				
				$('#branch_id').append($('<option>', {
				    value: collection.branchId,
				    text: collection.branchName
				}));
				$("#branch_id").val(collection.branchId);
				
				$('#account_id').append($('<option>', {
				    value: collection.accountNo,
				    text: collection.accountName
				}));
				$("#account_id").val(collection.accountNo);
				
				disableField("collected_amount","tax_amount","bank_id","branch_id","branch_id","account_id");
				
				var collectedSegmentList=data.collectedSegments;
				
				for(var i=0;i<collectedSegmentList.length;i++){
					$("#installment_collection_dtl_table tbody").append(getCollectedSegment(collectedSegmentList[i]));
				}
				
			}
			else if(collection.collectionStatusName=="Not-Collected"){
				$("#btn_save").show();
				enableField("collected_amount","tax_amount","bank_id","branch_id","branch_id","account_id");

				
				for(var i=0;i<segmentList.length;i++){
					$("#installment_collection_dtl_table tbody").append(getSegmentRowForCollection(segmentList[i],i));
				}
				$("#installment_collection_dtl_table tbody").append(getTotalRow(collection));
				
			}
		  },
		  error:function (xhr, ajaxOptions, thrownError) {
		      alert(xhr.status);
		      alert(thrownError);
	      }
		});		
}
function getBillSegmentRow(segment){
	
	return "<tr>"+
	         "<td style='text-align:center;'>"+segment.billId+"</td>"+
	         "<td style='text-align:left;'>"+segment.billMonthName+", "+segment.billYear+"</td>"+
	         "<td style='text-align:right;'>"+segment.principal+"</td>"+
	         "<td style='text-align:right;'>"+segment.surcharge+"</td>"+
	         "<td style='text-align:right;'>"+segment.meterRent+"</td>"+
	         "<td style='text-align:right;'>"+segment.total+"</td>"+
		   "</tr>";
}

function getSegmentRowForCollection(segment,index){
	
	return "<tr>"+
	         "<td style='text-align:center;'>"+segment.billId+"<input type='hidden' id='bill_"+index+"' name='bill[]' value='"+segment.billId+"'/><input type='hidden' id='segment_"+index+"' name='segment[]' value='"+segment.segmentId+"'/></td>"+
	         "<td style='text-align:left;'>"+segment.billMonthName+", "+segment.billYear+"</td>"+
	         "<td style='text-align:right;'><input type='text' id='principal_"+index+"' name='principal[]' class='numberInput' value='"+segment.principal+"' disabled='disabled'/><span class='label label-success'>"+segment.principal+"</span><input type='hidden' id='principal_h_"+index+"' class='numberInput' value='"+segment.principal+"'/></td>"+
	         "<td style='text-align:right;'><input type='text' id='surcharge_"+index+"' name='surcharge[]' class='numberInput' value='"+segment.surcharge+"' disabled='disabled' /><span class='label label-primary'>"+segment.surcharge+"</span><input type='hidden' id='surcharge_h_"+index+"' class='numberInput' value='"+segment.surcharge+"'/></td>"+
	         "<td style='text-align:right;'><input type='text' id='meterRent_"+index+"' name='meterRent[]' class='numberInput' value='"+segment.meterRent+"'  disabled='disabled'/><span class='label label-info'>"+segment.meterRent+"</span></td>"+
	         "<td style='text-align:right;'><input type='text' id='tax_"+index+"'  name='tax[]' class='numberInput'  disabled='disabled'/></td>"+
	         "<td style='text-align:right;'><input type='text' id='total_"+index+"' name='total[]'  class='numberInput' value='"+segment.total+"'  disabled='disabled'/><span class='label label-warning'>"+segment.total+"</span></td>"+
		   "</tr>";
}

function getCollectedSegment(segment){
	
	return "<tr>"+
	         "<td style='text-align:center;'>"+segment.billId+"</td>"+
	         "<td style='text-align:left;'>"+segment.billMonthName+", "+segment.billYear+"</td>"+
	         "<td style='text-align:right;'>"+segment.principal+"</td>"+
	         "<td style='text-align:right;'>"+segment.surcharge+"</td>"+
	         "<td style='text-align:right;'>"+segment.meterRent+"</td>"+
	         "<td style='text-align:right;'>"+segment.tax+"</td>"+
	         "<td style='text-align:right;'>"+segment.total+"</td>"+
		   "</tr>";
}

function getTotalRow(collection){
	return "<tr>"+
	         "<td style='text-align:right;' colspan='2'>Total</td>"+
	         "<td style='text-align:right;'><input type='text' id='principal_total'  class='numberInput' value='"+collection.principal+"' disabled='disabled'/><span class='label label-success'>"+collection.principal+"</span></td>"+
	         "<td style='text-align:right;'><input type='text' id='surcharge_total'  class='numberInput' value='"+collection.surcharge+"' disabled='disabled' /><span class='label label-primary'>"+collection.surcharge+"</span></td>"+
	         "<td style='text-align:right;'><input type='text' id='meterRent_total'  class='numberInput' value='"+collection.meterRent+"'  disabled='disabled'/><span class='label label-info'>"+collection.meterRent+"</span></td>"+
	         "<td style='text-align:right;'><input type='text' id='tax_total'  class='numberInput' value=''  disabled='disabled'/></td>"+
	         "<td style='text-align:right;'><input type='text' id='total_total'  class='numberInput' value='"+collection.total+"'  disabled='disabled'/><span class='label label-warning'>"+collection.total+"</span></td>"+
		   "</tr>";
}

function showCollectionDetailTab(){
	
	if($("#tabbed-nav").find(".z-active").attr("data-link")!="tab2")
		jQuery("#tabbed-nav").data('zozoTabs').select(1);
	
	var collected=parseFloat($("#collected_amount").val().trim());
	var tax=parseFloat($("#tax_amount").val().trim());
	
	if(collected=="" || isNaN(parseFloat($("#collected_amount").val().trim())))collected=0;
	if(tax=="" || isNaN(parseFloat($("#tax_amount").val().trim())))tax=0;
	
	
	if($("#tax_amount").val()!=""){
		//need to check tax amount total amount er theke boro thote parbe na
		
		var totalRow= $('#installment_collection_dtl_table tbody tr').length;
		for(var i=0;i< totalRow;i++){
			var taxAmount=($("#principal_h_"+i).val()/collection.principal)*tax;
			$("#tax_"+i).val(taxAmount);
			$("#principal_"+i).val($("#principal_h_"+i).val()-$("#tax_"+i).val());
		}
		
	}
	
	/*
	var principalTotal=parseFloat($("#principal_total").val().trim());
	var surchargeTotal=parseFloat($("#surcharge_total").val().trim());
	var meterRentTotal=parseFloat($("#meterRent_total").val().trim());
	var taxTotal=parseFloat($("#tax_total").val().trim());
	
	if(principalTotal=="" || isNaN(parseFloat($("#principal_total").val().trim())))principalTotal=0;
	if(surchargeTotal=="" || isNaN(parseFloat($("#surcharge_total").val().trim())))surchargeTotal=0;
	if(meterRentTotal=="" || isNaN(parseFloat($("#meterRent_total").val().trim())))meterRentTotal=0;
	if(taxTotal=="" || isNaN(parseFloat($("#tax_total").val().trim())))taxTotal=0;
	*/
	
	var collectedWithTax=collected+tax;
	var installmentTotal=parseFloat($("#installment_total").val());
	//var p_s_mr_t_total=principalTotal+surchargeTotal+meterRentTotal+taxTotal;
	var extraAmount=0;
	
	if(installmentTotal<collectedWithTax){
		extraAmount=collectedWithTax-installmentTotal;		
	}
	var newSurcharge=parseFloat($("#surcharge_h_0").val())+parseFloat(extraAmount);
	$("#surcharge_0").val(newSurcharge);
	
	
	calcuateTotalRow();	
}

function calcuateTotalRow(){
	
	var principal=0;
	var surcharge=0;
	var tax=0;
	var total=0;
	
	var tmpMeterRent=0;
	var tmpTax=0;
	
	
	
	for(var i=0;i<segmentList.length;i++){
	
		if($("#meterRent_"+i).val()=="" || isNaN(parseFloat($("#meterRent_"+i).val().trim())))
			tmpMeterRent=0;
		else
			tmpMeterRent=parseFloat($("#meterRent_"+i).val());
				
		if($("#tax_"+i).val()=="" || isNaN(parseFloat($("#tax_"+i).val().trim())))
			tmpTax=0;
		else
			tmpTax=parseFloat($("#tax_"+i).val());
		
		
		var rowTotal=parseFloat($("#principal_"+i).val())+parseFloat($("#surcharge_"+i).val())+tmpMeterRent+tmpTax;
		$("#total_"+i).val(rowTotal);
		principal+=parseFloat($("#principal_"+i).val());
		surcharge+=parseFloat($("#surcharge_"+i).val());
		
		tax+=parseFloat( ($("#tax_"+i).val()=="" || isNaN(parseFloat($("#tax_"+i).val().trim())))?"0":parseFloat($("#tax_"+i).val()) );
		
		total+=parseFloat($("#total_"+i).val());
	}
	$("#principal_total").val(principal);
	$("#surcharge_total").val(surcharge);
	$("#tax_total").val(tax);
	$("#total_total").val(total);
}
function validateAndSaveInstallmentCollection()
{
	var isValid=true;
	isValid=validateField("collection_date","bank_id","branch_id","account_id","collected_amount");
	
	var collected=parseFloat($("#collected_amount").val().trim());
	var tax=parseFloat($("#tax_amount").val().trim());
	
	if(collected=="" || isNaN(parseFloat($("#collected_amount").val().trim())))collected=0;
	if(tax=="" || isNaN(parseFloat($("#tax_amount").val().trim())))tax=0;
	
	console.log(collected+tax);
	if(parseFloat($("#installment_total").val())>collected+tax){
		isValid=false;
		alert("Collection Amount can't be less than the total installment amount.");
	}
	if(isValid==true)	 {
		
	prepareCollectionDetailStr();	
	var form = document.getElementById('billCollectionForm');
	disableButton("btn_save");
	
	enableField("customer_id","installment_id","installment_total");
	
	var formData = new FormData(form);
	  $.ajax({
	    url: 'saveInstallmentCollection.action',
	    type: 'POST',
	    data: formData,
	    async: false,
	    cache: false,
	    contentType: false,
	    processData: false,
	    success: function (response) {		
	    
		if(response.status=="OK")
	    {
		   collectionForm(clearField);	
		   

	    }
		disableField("customer_id","installment_id","installment_total");
	    $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    enableButton("btn_save");	    
	    }
	    
	  });
	
	}
}
function collectionForm(plainFieldMethod){	
	var fields = ["installmentId","customer_id","installment_id","collection_date","customer_name","installment_serial","bank_id","customer_type","installment_month_year","branch_id","customer_category","installment_principal","account_id","is_metered_name","installment_surcharge","collected_amount","mobile","installment_meter_rent","tax_amount","phone","installment_total"];	
	plainFieldMethod.apply(this,fields);
}

function prepareCollectionDetailStr(){
	
	var bill = $("input[name='bill\\[\\]']").map(function(){return $(this).val();}).get();
	var segment = $("input[name='segment\\[\\]']").map(function(){return $(this).val();}).get();
	var principal = $("input[name='principal\\[\\]']").map(function(){return $(this).val();}).get();
	var surcharge = $("input[name='surcharge\\[\\]']").map(function(){return $(this).val();}).get();
	var meterRent = $("input[name='meterRent\\[\\]']").map(function(){return $(this).val();}).get();
	var tax = $("input[name='tax\\[\\]']").map(function(){return $(this).val();}).get();
	var total = $("input[name='total\\[\\]']").map(function(){return $(this).val();}).get();
	
	var collectionDetailStr="";
	
	for(var i=0;i<principal.length;i++){
		collectionDetailStr+=bill[i]+"#"+segment[i]+"#"+principal[i]+"#"+surcharge[i]+"#"+(meterRent[i]=="NaN"?"0":meterRent[i])+"#"+(tax[i]=="NaN"?"0":tax[i])+"#"+(total[i]=="NaN"?"0":total[i])+"@";
	}
	
	if(collectionDetailStr.lenght>0)
		collectionDetailStr=collectionDetailStr.substring(0,collectionDetailStr.length-1);
	
	$("#collectionDetailStr").val(collectionDetailStr);
}

/*/|==> Get Available meter list for a individual customer <==|/*/
function getMeterList(meter_id,callBack)
{
	$.ajax({
   		  type: 'POST',
   		  url: 'getMeterListAsJson.action',
   		  data: {customer_id:$("#customer_id").val()},
   		  success:function(data){
   		  	  $('#meter_id').find('option:gt(0)').remove();
	   		  $.each( data, function( key, value ) {
	  			$("#meter_id").append( new Option(value.meter_sl_no,value.meter_id) )
			  });		
			  
			  if(data.length>0){
			    removeReadOnlyField("billing_month","billing_year","reading_purpose_str");
			    if(meter_id==null)
			    	$('#meter_id option:first-child').next().attr("selected", "selected");	
			    else
			    	$('#meter_id').val(meter_id);
			    
			    if(callBack!=null)
			    	callBack();
			  }	
			  else{			  	
			  	disableButton("btn_save");
			  	$.jgrid.info_dialog("Information","No Meter found for the selected Customer.",jqDialogClose,jqDialogParam);
			  }
   		  },
   		  error:function(){}
   	});
}

Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "curr_reading_date",
    trigger    : "curr_reading_date",
    onSelect   : function() { this.hide();focusDifference();fetchGasPrice($("#curr_reading_date").val());  }
	}));

function focusDifference(){      
	focusNext("difference"); 
}   
/*/|==> Fetch reading info. for an individual customer <==|/*/   
/*-- Used for Meter Disconnection and Load and Pressure Change Form ----*/
function fetchReadingInfo(callBack)
{
  if($("#billing_month").val()!="" && $("#billing_year").val()!="" && $("#reading_purpose_str").val()!="" && $("#meter_no").val()!="")
   {
		 $.ajax({
   		  type: 'POST',
   		  url: 'fetchMeterReading.action',
   		  data: { "reading.customer_id":$("#customer_id").val(),"reading.billing_month":$("#billing_month").val(),"reading.billing_year":$("#billing_year").val(),"reading.meter_id":$("#meter_id").val(),"reading.reading_purpose_str":$("#reading_purpose").val(),"reading.curr_reading_date":$("#curr_reading_date").val()},
   		  success:function(data)
   		  {	
   			disableField("billing_month","billing_year");
   			callBack(data);
   			
   			
   		  },
   		  error:function (xhr, ajaxOptions, thrownError) {
		        alert(xhr.status);
		        alert(thrownError);
		      }
   		});    
   } 
}
function readingCallBack(data){

	if(data.length>0){
	    var reading=data[0];
	    changeReadingFormMode("disable_mode");
	    setReadingInfo(reading);		
		controlFieldsByReadingPurpose();
		
	}	
	  	else{
	  		clearMeterReadingForm();
	  	}
}
function setReadingInfo(data)
{
	//alert(data.prev_reading);
	$("#unit").val(data.unit);
	$("#reading_id").val(data.reading_id);
	$("#meter_rent").val(data.meter_rent);
	$("#tariff_id").val(data.tariff_id);
	$("#rate").val(data.rate);
	$("#prev_reading").val(data.prev_reading);
	$("#prev_reading_date").val(data.prev_reading_date);
	$("#pressure").val(data.pressure);
	$("#temperature").val(data.temperature);
	$("#pressure_factor").val(data.pressure_factor);
	$("#temperature_factor").val(data.temperature_factor);
	$("#pressure_latest").val(data.pressure_latest);
	$("#temperature_latest").val(data.temperature_latest);
	$("#pressure_factor_latest").val(data.pressure_factor_latest);
	$("#temperature_factor_latest").val(data.temperature_factor_latest);
	if(typeof data.bill_id === "undefined" || data.bill_id=="")
		$("#bill_id").val("");
	else
		$("#bill_id").val(data.bill_id);
			
					
	$("#measurement_type_name").val(data.measurement_type_name);
	$("#measurement_type_str").val(data.measurement_type_str);
	$("#curr_reading_date").val(data.curr_reading_date);

	if(typeof data.reading_id === "undefined"){	
		$("#min_load").val(data.customer.connectionInfo.min_load);
		$("#max_load").val(data.customer.connectionInfo.max_load);
		$("#min_load_latest").val(data.customer.connectionInfo.min_load);
		$("#max_load_latest").val(data.customer.connectionInfo.max_load);
		$("#hhv_nhv").val(data.customer.connectionInfo.hhv_nhv);
		$("#connection_status").val(data.customer.connectionInfo.status_name);
	}
	else{
	
	$('#meter_id').find('option:gt(0)').remove();
	$("#meter_id").append( new Option(data.meter_sl_no,data.meter_id) );
	$("#meter_id").val(data.meter_id);
			  
	//$("#billing_month").val(data.billing_month);
	//$("#billing_year").val(data.billing_year);
	//$("#reading_purpose_str").val(data.reading_purpose_str);
	
	$("#curr_reading").val(data.curr_reading);	
	$("#difference").val(data.difference);
	$("#min_load").val(data.min_load);	
	$("#max_load").val(data.max_load);
	$("#min_load_latest").val(data.customer.connectionInfo.min_load);
	$("#max_load_latest").val(data.customer.connectionInfo.max_load);
	$("#hhv_nhv").val(data.hhv_nhv);
	$("#tariff_id").val(data.tariff_id);
	$("#latest_tariff_id").val(data.latest_tariff_id);
	$("#rate").val(data.rate);
	$("#latest_rate").val(data.latest_rate);
	$("#actual_consumption").val(data.actual_consumption);
	//$("#unit").val(data.unit);
	$("#total_consumption").val(data.total_consumption);
	$("#actual_consumption_t").val(data.actual_consumption);
	$("#total_consumption_t").val(data.total_consumption);
	$("#remarks").val(data.remarks);
		
	}
}
function calculateDifference(){
	if($("#curr_reading").val()=="")
	 return;
	$("#difference").val(getReadingDifference($("#prev_reading").val(),$("#curr_reading").val()));
	calculateActualConsumption();
	calculateTotalConsumption();
	//focusNext('remarks'); 
}
    
function calculateActualConsumption()
{
 $("#actual_consumption").val(getActualConsumption($("#difference").val(),$("#pressure_factor").val(),$("#temperature_factor").val()));
 $("#actual_consumption_t").val(getActualConsumptionT($("#difference").val(),$("#pressure_factor").val(),$("#temperature_factor").val()));
}

function calculateTotalConsumption()
{
 $("#total_consumption").val(getTotalConsumption($("#actual_consumption").val(),$("#hhv_nhv").val()));
 $("#total_consumption_t").val(getTotalConsumptionT($("#actual_consumption").val(),$("#hhv_nhv").val()));
}
function clearMeterReadingForm()
{	
  clearField("reading_id","customer_name","unit","customer_type","address","prev_reading","prev_reading_date","curr_reading","curr_reading_date","hhv_nhv","measurement_type_name","measurement_type_str","rate","difference","min_load","max_load","actual_consumption_t","total_consumption_t","actual_consumption","total_consumption","meter_rent","remarks","area_id","customer_category","billing_month","billing_year","reading_purpose_str");
  $('#meter_id').find('option:gt(0)').remove();  
} 
function cleanFieldsBeforeReadingSetting()
{
  clearField("reading_id","meter_rent","unit","prev_reading","prev_reading_date","curr_reading","hhv_nhv","measurement_type_name","measurement_type_str","rate","difference","min_load","max_load","actual_consumption_t","total_consumption_t","actual_consumption","total_consumption","remarks");
  $('#meter_id').find('option:gt(0)').remove();
  
} 




function getPressureFactor(pressure)
{
 if(pressure!="")	
 	return ((parseFloat(pressure)+jsVar.P_FACTOR)/jsVar.P_FACTOR);
 return "";
}
function getTemperatureFactor(temperature)
{
 if(temperature!="")
 	return (jsVar.T_FACTOR1/(jsVar.T_FACTOR2+temperature));
 return "";
}
function getReadingDifference(prev_reading,current_reading)
{
	 var pReading=parseFloat(prev_reading);
	 var cReading=parseFloat(current_reading);
	 var difference=cReading-pReading;
	 //alert(pReading+"####"+cReading+"###"+difference+"###"+parseFloat(difference.toFixed(2)));
	 
	 return parseFloat(difference.toFixed(2));
}

function getActualConsumption(difference,pressureFactor,temperatureFactor)
{

	 var diff=parseFloat(difference);
	 var pFactor=pressureFactor;
	 var tFactor=temperatureFactor;
     //var actual_consumption=diff*pFactor*tFactor;
	 var actual_consumption=diff*pFactor;
     return actual_consumption;
}
function getTotalConsumption(actualConsumption,hhv_nhv){

	if(parseFloat(hhv_nhv)==0)
		return actualConsumption;
	else{
		var hhv_nhv_adjustment_qnt=parseFloat(actualConsumption)*(parseFloat(hhv_nhv)-1);
		hhv_nhv_adjustment_qnt=parseFloat(hhv_nhv_adjustment_qnt);
		var total_consumption=parseFloat(actualConsumption)+hhv_nhv_adjustment_qnt;
		total_consumption=parseFloat(total_consumption);
		return total_consumption;
	}
		
}

function getActualConsumptionT(difference,pressureFactor,temperatureFactor)
{

	 var diff=parseFloat(difference);
	 var pFactor=pressureFactor;
	 var tFactor=temperatureFactor;
     //var actual_consumption=diff*pFactor*tFactor;
	 var actual_consumption=diff*pFactor;
	 if($("#unit").val().toUpperCase()==="FT"){
		 actual_consumption= actual_consumption/35.3147; 
		}
     return actual_consumption;
}
function getTotalConsumptionT(actualConsumption,hhv_nhv){

	if(parseFloat(hhv_nhv)==0)
		return actualConsumption;
	else{
		var hhv_nhv_adjustment_qnt=parseFloat(actualConsumption)*(parseFloat(hhv_nhv)-1);
		hhv_nhv_adjustment_qnt=parseFloat(hhv_nhv_adjustment_qnt);
		var total_consumption=parseFloat(actualConsumption)+hhv_nhv_adjustment_qnt;
		total_consumption=parseFloat(total_consumption);
		if($("#unit").val().toUpperCase()==="FT"){
			total_consumption= total_consumption/35.3147; 
		}
		return total_consumption;
	}
		
}

function getTotalConsumptionView(actualConsumption,hhv_nhv){

	if(parseFloat(hhv_nhv)==0)
		return actualConsumption;
	else{
		var hhv_nhv_adjustment_qnt=parseFloat(actualConsumption)*(parseFloat(hhv_nhv)-1);
		hhv_nhv_adjustment_qnt=parseFloat(hhv_nhv_adjustment_qnt);
		var total_consumption=parseFloat(actualConsumption)+hhv_nhv_adjustment_qnt;
		total_consumption=parseFloat(total_consumption);
		return total_consumption;
	}
		
}

function fetchGasPrice(date)
{
 $.ajax({
   		  type: 'POST',
   		  url: 'getGasPrice.action',
   		  data: { meter_status:'Metered',target_date:date,customer_id:$("#customer_id").val()},
   		  success:function(data)
   		  {
   		  if(typeof data.tariff_id === "undefined")
			{
				alert("No Available Tariff exists.");
			}
			else
			{
		   		$("#tariff_id").val(data.tariff_id);
		   		$("#rate").val(data.price);
   		    }			
   		  },
   		  error:function(){
   			//$('#aDiv').empty();
   			//$("#btn_save").removeAttr("disabled");
   		  }
   	});
   	
}

//Available Modes
//disable_mode,data_entry_mode,data_submit_mode
function changeReadingFormMode(mode){
	var always_disable_fields=["meter_rent","prev_reading_date","prev_reading","curr_reading_date","difference","rate","measurement_type_name","total_consumption","total_consumption_t","min_load","max_load"];
	var always_editable_fields=["remarks"];
	var conditional_fields=["meter_id","billing_month","billing_year","reading_purpose_str","curr_reading","actual_consumption","actual_consumption_t"];
	
	always_disable_fields=always_disable_fields.slice();
	always_editable_fields=always_editable_fields.slice();
	conditional_fields=conditional_fields.slice();
	
	if(mode=="disable_mode"){
		disableField.apply(this,always_disable_fields);
		disableField.apply(this,always_editable_fields);
		disableField.apply(this,conditional_fields);
	}
	else if(mode=="data_submit_mode"){
		enableField.apply(this,always_disable_fields);
		enableField.apply(this,always_editable_fields);
		enableField.apply(this,conditional_fields);
		
		
		readOnlyField.apply(this,always_disable_fields);
		readOnlyField.apply(this,always_editable_fields);
		readOnlyField.apply(this,conditional_fields);

	}
	else if(mode=="data_entry_mode"){
		
		removeReadOnlyField.apply(this,always_disable_fields);
		removeReadOnlyField.apply(this,always_editable_fields);
		removeReadOnlyField.apply(this,conditional_fields);
		
		disableField.apply(this,always_disable_fields);		
		disableField.apply(this,conditional_fields);
		
		enableField.apply(this,always_editable_fields);
		
		controlFieldsByReadingPurpose();	
	}
}

function controlFieldsByReadingPurpose()
{	
	enableField("reading_purpose_str");
	var reading_purpose=$("#reading_purpose_str").val();
	disableField("reading_purpose_str");
	if(reading_purpose==2){
		//General Billing
		if($("#measurement_type_str").val() == 1 || $("#customer_category").val()==13 ){
			enableField("actual_consumption","remarks");
			$('#actual_consumption').show();
			$('#actual_consumption').focus();
			$('#actual_consumption_t').hide();
		}
		else{
			enableField("curr_reading");			
			$('#curr_reading').focus();
		}
		
	}
	else if(reading_purpose==3){
		//Average Billing
		enableField("actual_consumption");
		$("#curr_reading").val($("#prev_reading").val());
		$('#actual_consumption').show();
		$('#actual_consumption').focus();	
		$('#actual_consumption_t').hide();
	}
	else if(reading_purpose==6){
		//Bill on Maximum Load
		enableField("remarks");
		$("#curr_reading").val($("#prev_reading").val());
		$("#actual_consumption").val($("#max_load").val());
		$("#total_consumption").val($("#max_load").val());
		$("#actual_consumption_t").val($("#min_load").val());
		$("#total_consumption_t").val($("#min_load").val());
		$('#remarks').focus();
	}
	else if(reading_purpose==7){//PROPORTIONAL_BILL(5,"Proportional Bill")
	}
	else if(reading_purpose==8){
		//ADJUSTMENT_BILL(6,"Adjustment Bill")
		enableField("actual_consumption");
		$('#actual_consumption').show();
		$('#actual_consumption').focus();
		$('#actual_consumption_t').hide();
		
	}else if(reading_purpose==9){
		//Bill on Minimum Load
		enableField("remarks");
		$("#curr_reading").val($("#prev_reading").val());
		$("#actual_consumption").val($("#min_load").val());
		$("#total_consumption").val($("#min_load").val());
		$("#actual_consumption_t").val($("#min_load").val());
		$("#total_consumption_t").val($("#min_load").val());
		$("#difference").val("0");
		$('#remarks').focus();
	}else if(reading_purpose==10){
		//Bill on Actual Load
		if($("#measurement_type_str").val() == 1 || $("#customer_category").val()==13){
			enableField("actual_consumption","remarks");
			$('#actual_consumption').show();
			$('#actual_consumption').focus();
			$('#actual_consumption_t').hide();
			
		}
		else{
			enableField("curr_reading");			
			$('#curr_reading').focus();
		}
	}
	
	
}
function validateMeterReadingInfo(){
	var reading_purpose=$("#reading_purpose_str").val();
	
	var isValid=false;
	
	 if($("#measurement_type_name").val()=="EVC" || $("#customer_category").val()=='13' ||reading_purpose==8){
			// 11 = Power (PVT.) and 12= Power (GOVT.)
				isValid=validateField("customer_id","meter_id","reading_purpose_str","reading_purpose_str",
						"billing_year","prev_reading","prev_reading_date","curr_reading_date",
						"rate","total_consumption","actual_consumption");
				
	}
	 else if($("#measurement_type_name").val()=="Normal"){
		 
		if(reading_purpose==3||reading_purpose==6){
			isValid=validateField("customer_id","meter_id","reading_purpose_str","reading_purpose_str",
					"billing_year","prev_reading","prev_reading_date","curr_reading_date",
					"rate","total_consumption","actual_consumption");
		}else
			isValid=validateField("customer_id","meter_id","reading_purpose_str","reading_purpose_str",
				"billing_year","prev_reading","prev_reading_date","curr_reading","curr_reading_date",
				"difference","rate","total_consumption","actual_consumption");	
	}
	 
	 var c = $("#curr_reading").val();
	 var p = $("#prev_reading").val();
	 var cc = parseFloat(c);
	 var pp = parseFloat(p);
	 
	 //alert(cc);
	 
	// if($("#curr_reading").val()<$("#prev_reading").val()){
	if(cc<pp){
		 alert("Current reading can't be less than previous reading");
		 //alert(cc+" "+pp);
		 isValid=false;
	 }
	 else{
		 isValid=true;
	 }
	 
/*	 if($("#measurement_type_name").val()=="EVC")
		 {
		  $("#prev_reading").val();
		  $("#curr_reading").val();
		  
		 }*/
	 
	 
	 
	 
		
	return isValid;
}

function showShortCuts(){
	var shortcuts="<div style='text-align:left;margin-left:20px;'>"+
	"<b>Next:</b> Ctrl+> <br/>"+
	"<b>Previous:</b> Ctrl+< <br/>"+
	"<b>First Record:</b> Ctrl+M <br/>"+
	"<b>Last Record:</b> Ctrl+? <br/>"+
	"<b>Edit:</b> F2 <br/>"+
	"<b>Add New:</b> F1 <br/>"+
	"<b>Meter Reset:</b> F3 <br/>"+
	"</div>";
	
	$.jgrid.info_dialog("Keyboard Shortcuts",shortcuts,jqDialogClose,jqDialogParam);
}
var $dialog = $('<div id="dialog-confirm"></div>')
			.html("<p> "+
			 	"Are you sure you want to delete selected reading information?"+
				"<div id='del_holiday'></div> "+
			   "</p>")
			.dialog({
					title: 'Meter Reading Delete Confirmation',
					resizable: false,
					autoOpen: false,
					height:255,
					width:450,
					modal: true,
					buttons: {
							"Delete": {text:"Delete","class":'btn btn-danger',click:function() {
							deleteMeterReading();          
							$( this ).dialog( "close" );
							}},
							"Cancel": {text:"Cancel","class":'btn btn-beoro-3',click:function() {
							$( this ).dialog( "close" );
							}},
					}
				});


function deleteMeterReading(){
	//alert($("#bill_id").val());
	if($("#bill_id").val()!="")
	  $("#msg_div").html("<font color='red'>Sorry, you can't delete this Meter Reading.</font>");
	else{

		  $.ajax({
		    url: 'deleteMeterReading.action',
		    type: 'POST',
		    data: {reading_id:$("#reading_id").val()},
		    cache: false,
		    success: function (response) {
		    $dialog.dialog("close");
		    if(response.status=="OK")
		    {		    
			   //Delete the corresponding record if it is present in the grid....		    	
			   cleanFieldsBeforeReadingSetting();
			   $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
			   
		    }
		       changeReadingFormMode("disable_mode",null);
		       $("#msg_div").html(response.message);
			   $('#msg_div').addClass('animated fadeInUp');			
		       fetchReadingEntry('next');
		    }
		    
		  });
	}
}  
function clearRelatedData(){	
	//clearMeterReadingForm();
	clearCustomerInfoForm("");
	clearGridData("reading_history");
}

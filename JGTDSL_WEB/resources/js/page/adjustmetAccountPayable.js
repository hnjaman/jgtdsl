function validateAndSaveCustomerInfo(){
	updateCustomerInfo();
/*	var validate=false;
	
	validate=validateCustomerInfo();
	if(validate==true ){
		updateCustomerInfo();
	}	*/
}

function validateCustomerInfo(){
	return validateField("full_name","father_name","mother_name","gender","mobile","division_id","division_id","upazila_id","road_house_no");	
}

function saveAdjustmentAccountPayable(){
	
	alert("updateCustomerInfo");
	var formData = new FormData($('form')[0]);
    $.ajax({
		    url: 'saveAdjustmentAccountPayable.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  				   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

function calculateGTCL()
{
	var bgfcl=parseFloat($("#total_bgfcl").val()==""?parseInt("0"):$("#total_bgfcl").val());
	var sgfl=parseFloat($("#total_sgfl").val()==""?parseInt("0"):$("#total_sgfl").val());
	var ioc=parseFloat($("#total_ioc").val()==""?parseInt("0"):$("#total_ioc").val());
	var total_gtcl=bgfcl+sgfl+ioc;
	$("#total_gtcl").val(total_gtcl);

}
$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){
	
				//clearField.apply(this,old_customer_fields);
				getCustomerInfoforEdit($('#customer_id').val());
		}
}));

function getCustomerInfoforEdit(customer_id)
{

    if(customer_id=="") return;
	$.ajax({
   		  type: 'POST',
   		  url: 'getCustomerInfoAsJson.action',
   		  data: { customer_id:customer_id},
   		  success:function(data){
			if(typeof data.personalInfo === "undefined")
			{
				//clearCustomerInfoForm(field_prefix);
			    alert("Sorry, this is not either a valid customer or a customer whose connection has not yet been established.");
			}
			else
			{
				setCustomerInfos(data);	 
				if(typeof callback !== 'undefined'){
					callback(data);
				}
			}
						
   		  },
   		  error:function(){
   			
   		  }
   	});
}

function setCustomerInfos(data){
	

	
	//clearCustomerInfoForm(field_prefix);
	
	var personal=data.personalInfo;
	var address=data.addressInfo;
	var connection=data.connectionInfo;
	
	if(typeof data.personalInfo === "undefined")
	{}
	else{
		$("#area_id").val(data.area);
		$("#full_name").val(personal.full_name);
		$("#father_name").val(personal.father_name);
		$("#mother_name").val(personal.mother_name);	
		$("#mobile").val(personal.mobile);
		$("#phone").val(personal.phone);
		$("#fax").val(personal.fax);
		$("#national_id").val(personal.national_id);
		$("#gender").val(personal.gender);
		$("#email").val(personal.email);		
	
		$("#customer_category_name").val(data.customer_category_name);
		$("#area_name").val(data.area_name);
		$("#app_sl_no").val(data.app_sl_no);
	
		//Address
		//var divStr=(address.division_name=="" || typeof address.division_name === "undefined" )?"":"Division : "+address.division_name;
		var distStr=(address.district_id=="" || typeof address.district_id === "undefined" )?"":address.district_id;
		var uzillaStr=(address.upazila_id=="" || typeof address.upazila_id === "undefined" )?"":address.upazila_id;
		
		 fetchSelectBox(district_sbox);
		setTimeout(function(){ $("#district_id").val(distStr);fetchSelectBox(upazila_sbox);}, 1000);
		setTimeout(function(){ $("#upazila_id").val(uzillaStr);}, 3000);
		
		var roadStr=(address.road_house_no=="" || typeof address.road_house_no === "undefined" )?"":address.road_house_no;
		var postOfficeStr=(address.post_office=="" || typeof address.post_office === "undefined" )?"":address.post_office;
		var postCodeStr=(address.post_code=="" || typeof address.post_code === "undefined" )?"":address.post_code;		
		var adr1Str=(address.address_line1=="" || typeof address.address_line1 === "undefined" )?"":address.address_line1;
		var adr2Str=(address.address_line2=="" || typeof address.address_line2 === "undefined" )?"":address.address_line2;
		
		$("#road_house_no").val(roadStr);
		$("#post_office").val(postOfficeStr);
		$("#post_code").val(postCodeStr);
		$("#address_line1").val(adr1Str);
		$("#address_line2").val(adr2Str);
		
		
	
	}
}


function validateAndSaveCustomerInfo(){
	
	var validate=false;
	
	validate=validateCustomerInfo();
	if(validate==true ){
		updateCustomerInfo();
	}	
}

function validateCustomerInfo(){
	return validateField("full_name","father_name","mother_name","gender","mobile","division_id","division_id","upazila_id","road_house_no");	
}

function updateCustomerInfo(){
	
	
	var formData = new FormData($('form')[0]);
    $.ajax({
		    url: 'updateCustomerInfo.action',
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
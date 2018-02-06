<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="/JGTDSL_WEB/resources/thirdParty/smart-wizard/styles/smart_wizard.css">
<script src="/JGTDSL_WEB/resources/thirdParty/smart-wizard/js/jquery.smartWizard.js"></script>
<script  type="text/javascript">
	navCache("newCustomer.action");
	setTitle("Create New Customer");
</script>
<style type="text/css">
#newCustomerForm{height: 100%;}
.w-box{height: 100% !important;}
.row-fluid{height: 100% !important;}
.span12{height: 92% !important;}
.swMain{height: 93% !important;width: 99% !important;}
.swMain .stepContainer{height: 92% !important;}	
.swMain div.actionBar {
    margin: -18px 0 0 !important;
}
.swMain .msgBox{
min-width: 45% !important;
margin: 0px !important;
}
.swMain{
margin-top: 10px;
}
.alert{
padding-bottom: 0px !important;
}
.w-box-header{
 background: linear-gradient(to bottom, #f7fbfc 0%, #d9edf2 40%, #add9e4 100%) repeat scroll 0 0 rgba(0, 0, 0, 0) !important;
    color: #004b5e !important;
    text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1) !important;
    border: 1px solid #add9e4 !important;
}    
label{
    display: inline-block !important;
    float: left !important;
    clear: left !important;
    width: 40%;
    text-align: left !important;
}
input[type=text] {
  display: inline-block !important;
  float: left !important;
  border: 1px solid #add9e4;
}
select {
  display: inline-block !important;
  float: left !important;
  border: 1px solid #add9e4;
  color: #333 !important;
}
.row-fluid + .row-fluid {
    margin-top: 6px !important;
}
select, textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input {
    border-radius: 0px !important;
}
#customer_wizard {
width: 99.9% !important;
}
</style>
<!-- wizard -->
<div class="row-fluid" id="customer_wizard" style="display: none;">
                    <div class="span12" style="width: 99.5%;">
                    <form id="newCustomerForm" name="newCustomerForm">
<div class="w-box">
                            <div class="w-box-header">
                                <h4>Customer Information</h4>
                            </div>
                            <div class="w-box-content" style="height: 103%;"> <!-- w-box-content in theme, common.js, constant.js, beoro.css -->
                                <div class="row-fluid">
                                    <div class="span12">
                                        <div id="wizard" class="swMain">
                                            <ul>
                                                <li>
                                                    <a href="#sw-basic-step-1">
                                                        <span class="stepNumber">1</span>
                                                        <span class="stepDesc">
                                                           Step 1<small>Personal Information</small>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#sw-basic-step-2">
                                                        <span class="stepNumber">2</span>
                                                        <span class="stepDesc">
                                                           Step 2<small>Address Information</small>
                                                        </span>
                                                    </a>
                                                </li>
                                            </ul>
                                            <%@ include file="PersonalInfo.jsp" %>
                                            <%@ include file="AddressInfo.jsp" %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>	
                        </form>
                        		</div>
                        		</div>
                       		
<!-- wizard end -->
			
<script type="text/javascript">
	function clearMsgBox()
	{
		$(".msgBox").css("display", "none");
	};	
			
	function validateStep1(){
		  isValid = true;

		  if(validateCustomerId()==false)
		  	{cbColor($("#id_category"),"e");cbColor($("#id_area"),"e");cbColor($("#id_code"),"e");isValid=false;}
		  else
		  {cbColor($("#id_category"),"v");cbColor($("#id_area"),"v");cbColor($("#id_code"),"v");}
		  if($.trim($("#gender").val())=="")
		  	{cbColor($("#gender"),"e");isValid=false;}
		  else cbColor($("#gender"),"v");
		  if($("#customer_category").val()!="01" && $("#customer_category").val()!="02")
		  {
		    if($.trim($("#proprietor_name").val())=="")
		  	{cbColor($("#proprietor_name"),"e");isValid=false;}
		    else cbColor($("#proprietor_name"),"v");		   
		  }
		  
		  isValid=validateField("full_name","father_name","mother_name","gender","mobile","customer_category","area_id");	
		  
      		
		return isValid;
	}
	function validateStep2(){
		  isValid = true;

		  isValid=validateField("division_id","division_id","upazila_id","road_house_no");
		  	
		  
		return isValid;
	}	
	function validateStep3(){
		  isValid = true;

		  if($.trim($("#min_load").val())=="")
		  	{cbColor($("#min_load"),"e");isValid=false;}
		  else cbColor($("#min_load"),"v");
		  if($.trim($("#max_load").val())=="")
		  	{cbColor($("#max_load"),"e");isValid=false;}
		  else cbColor($("#max_load"),"v");
		  if($.trim($("#meter_no").val())=="")
		  	{cbColor($("#meter_no"),"e");isValid=false;}
		  else cbColor($("#meter_no"),"v");
		  if($.trim($("#meter_no").val())=="")
		  	{cbColor($("#meter_no"),"e");is
	Valid = false;
		} else
			cbColor($("#meter_no"), "v");
		if ($.trim($("#meter_no").val()) == "") {
			cbColor($("#meter_no"), "e");
			isValid = false;
		} else
			cbColor($("#meter_no"), "v");
		if ($.trim($("#meter_type").val()) == "") {
			cbColor($("#meter_type"), "e");
			isValid = false;
		} else
			cbColor($("#meter_type"), "v");
		if ($.trim($("#meter_category").val()) == "") {
			cbColor($("#meter_category"), "e");
			isValid = false;
		} else
			cbColor($("#meter_category"), "v");

		return isValid;
	}
	function validateSteps(step) {
		//return true;
		var isStepValid = true;
		// validate step 1
		if (step == 1) {
			clearMsgBox();
			if (validateStep1() == false) {
				isStepValid = false;
				$('#wizard').smartWizard(
						'showMessage',
						'<font color="red">Please correct the errors in step'
								+ step + ' and click next.</font>');
				$('#wizard').smartWizard('setError', {
					stepnum : step,
					iserror : true
				});
			} else {
				$('#wizard').smartWizard('setError', {
					stepnum : step,
					iserror : false
				});
			}
		}
		if (step == 2) {
			clearMsgBox();
			if (validateStep2() == false) {
				isStepValid = false;
				$('#wizard').smartWizard(
						'showMessage',
						'<font color="red">Please correct the errors in step'
								+ step + ' and click next.</font>');
				$('#wizard').smartWizard('setError', {
					stepnum : step,
					iserror : true
				});
			} else {
				$('#wizard').smartWizard('setError', {
					stepnum : step,
					iserror : false
				});
			}
		}
		if (step == 3) {
			clearMsgBox();
			if (validateStep3() == false) {
				isStepValid = false;
				$('#wizard').smartWizard(
						'showMessage',
						'<font color="red">Please correct the errors in step'
								+ step + ' and click next.</font>');
				$('#wizard').smartWizard('setError', {
					stepnum : step,
					iserror : true
				});
			} else {
				$('#wizard').smartWizard('setError', {
					stepnum : step,
					iserror : false
				});
			}
		}

		return isStepValid;
	}

	$(document).ready(function() {
		$("#customer_wizard").show();
		$('#wizard').smartWizard({
			transitionEffect : 'slideleft',
			onLeaveStep : leaveAStepCallback,
			onFinish : onFinishCallback,
			enableFinishButton : false
		});
		//transitionEffect: 'fade', // Effect on navigation, none/fade/slide/slideleft
		//https://github.com/mstratman/jQuery-Smart-Wizard
		function leaveAStepCallback(obj) {
			var step_num = obj.attr('rel');
			//return true;
			return validateSteps(step_num);
		}

		function onFinishCallback() {
			if (validateAllSteps()) {
				$('form').submit();
			}
		}
	});

	function validateAllSteps() {
		var isStepValid = true;

		if (validateStep1() == false) {
			isStepValid = false;
			$('#wizard').smartWizard('setError', {
				stepnum : 1,
				iserror : true
			});
		} else {
			$('#wizard').smartWizard('setError', {
				stepnum : 1,
				iserror : false
			});
		}

		if (validateStep2() == false) {
			isStepValid = false;
			$('#wizard').smartWizard('setError', {
				stepnum : 3,
				iserror : true
			});
		} else {
			$('#wizard').smartWizard('setError', {
				stepnum : 3,
				iserror : false
			});
		}

		if (!isStepValid) {
			$('#wizard').smartWizard('showMessage',
					'Please correct the errors in the steps and continue');
		}

		return isStepValid;
	}

	function setCategory(category) {
		$("#id_category").val(category);
		if (category == "01" || category == "02") {
			$("#div_proprietor").hide();
			$("#proprietor_name").val(jsVar.EMPTY);
			$("#div_bl").hide();
			$("#license_number").val(jsVar.EMPTY);
			$("#div_vat").hide();
			$("#vat_reg_no").val(jsVar.EMPTY);

			$("#div_nid").show();
			$("#div_passport").show();

			if (category == "01")
				$("#div_freedom_fighter").show();
			else
				$("#div_freedom_fighter").hide();

		} else {
			$("#div_proprietor").show();
			$("#div_bl").show();
			$("#div_vat").show();

			$("#div_nid").hide();
			$("#national_id").val(jsVar.EMPTY);
			$("#div_passport").hide();
			$("#passport_no").val(jsVar.EMPTY);
		}
	}
	function setArea(area) {
		$("#id_area").val(area);
	}
	
	
	function validateCustomerId() {
		
		//if(codeId==""){
			//alert("Customer ID Can Not Be EMPTY");
		//}
		var customer_id = $("#id_area").val() + $("#id_category").val()+ $("#id_code").val();
		if($.trim(customer_id)=="")
		  		return false;
		
			
			

		var validate = $.ajax({
			url : "validateCustomerId.action",
			data : {customer_id : customer_id},
			async : false,
			success : function(data, result) {
						if (!result)
						alert('Failure to retrieve Branch List.');
					  }
		}).responseText;
		
		if (validate == "true") {
			$("#customer_id").val(customer_id);
			return true;
		} else
			return false;

	}

	$("form#newCustomerForm")
			.submit(
					function(event) {

						//disable the default form submission
						event.preventDefault();

						//grab all form data  
						var formData = new FormData($(this)[0]);

						$
								.ajax({
									url : 'createNewCustomer.action',
									type : 'POST',
									data : formData,
									async : false,
									cache : false,
									contentType : false,
									processData : false,
									success : function(response) {

										$.jgrid
												.info_dialog(
														response.dialogCaption,
														response.message,
														$.jgrid.edit.bClose,
														{
															zIndex : 1500,
															width : 450,
															beforeOpen : function() {
																centerCustomerInfoDialog($(
																		"#customer_id")
																		.val());
															},
															afterOpen : disableOnClick,
															onClose : function() {
																if (response.status != "ERROR") {
																	$(
																			"#contentPanel")
																			.html(
																					"<center><img  src='/JGTDSL_WEB/resources/images/emptyContent.png' /></center>");

																	$(
																			'.ui-widget-overlay')
																			.unbind(
																					"click");
																}
																return true; // allow closing
															}

														});

									}

								});

						return false;
					});

	Calendar.setup($.extend(true, {}, calOptions, {
		inputField : "application_date",
		trigger : "application_date",
		onSelect : function() {
			this.hide();
		}
	}));

	$("m.man")
			.replaceWith(
					"<font color='red' style='font-weight:bold;font-size:16px;'>&nbsp;&nbsp;*</font>");
	$("#customer_photo").html("<img src='"+jsVar.DEFAULT_PHOTO_URL+"' />");
	function setTestData() {
		var idCode = Math.floor((Math.random() * 99999) + 11111);
		$("#id_area").val("01");
		$("#id_category").val("01");
		$("#id_code").val(idCode);
		$("#ismetered").val("1");
		$("#customer_category").val("01");
		$("#area_id").val("01");
		$("#zone").val("01");
		$("#full_name").val("Ifta Khirul");
		$("#father_name").val("Md. Nurnabi Sarkar");
		$("#mother_name").val("Fatima Khatu");
		$("#email").val("ifticse_kuet@hotmail.com");
		$("#phone").val("87986787");
		$("#mobile").val("01755625837");
		$("#fax").val("fax");
		$("#national_id").val("nationalid");
		$("#passport_no").val("passport");
		$("#license_number").val("blicense");
		$("#tin").val("tin number");
		$("#division_id").val("30");
		$("#district_id").val("26");
		$("#upazila_id").val("154");
		$("#road_house_no").val("34/1 Dilu road");
		$("#post_office").val("Ramna");
		$("#post_code").val("1000");
		$("#address_line1").val("Flat # C-6");
		$("#address_line2").val("Summerlin");
	}
</script>
var fields = [ "meter_sl_no", "meter_mfg", "meter_year",
		"measurement_type_str", "meter_type", "g_rating", "conn_size",
		"max_reading", "ini_reading", "temperature", "pressure", "meter_rent","vat_rebate",
		"meter_remarks", "installed_date" ];
var fields_with_evc = fields.slice();
fields_with_evc.push("evc_sl_no", "evc_year", "evc_model");

$("#customer_grid").jqGrid(
		$.extend(true, {}, scrollPagerGridOptions, {
			url : jsEnum.GRID_RECORED_FETCHER + '?service='
					+ jsEnum.CUSTOMER_SERVICE + '&method='
					+ jsEnum.METERED_CUSTOMER_LIST + '&extraFilter=area',
			jsonReader : {
				repeatitems : false,
				id : "customer_id"
			},
			colNames : [ 'Customer Id', 'Customer Name', 'Father Name', 'Category', 'Area', 'Mobile', 'Status', 'Created On' ],
			colModel : customerGridColModel,
			datatype : 'json',
			height : $("#customer_grid_div").height() - 70,
			width : $("#customer_grid_div").width() + 10,
			pager : '#customer_grid_pager',
			sortname : 'customer_id',
			sortorder : "asc",
			caption : "List of Metered Customers",
			onSelectRow : function(id) {
				getCustomerInfo("comm", id);
				loadMeters(id, preOperationWrapper);
				clearField.apply(this, fields_with_evc);
				enableButton("btn_add", "btn_edit", "btn_delete");
			}
		}));

jQuery("#customer_grid").jqGrid('navGrid', '#customer_grid_pager',
		$.extend({}, footerButton, {
			search : true
		}), {}, {}, {}, {
			multipleSearch : true
		});
gridColumnHeaderAlignment("collection_grid", [ "full_name" ], "left");

function preOperationWrapper() {
	meterLoadPreOperation(disableMeterInfoForm);

}
function disableMeterInfoForm() {
	meterInfoForm(disableField, disableChosenField);
}

function validateAndSaveMeter() {
	var isValid;
	var validationFields;

	if ($("#measurement_type_str").val() == "1") {
		validationFields = $.extend(true, {}, fields_with_evc);
		validationFields = Object.keys(validationFields).map(function(k) {
			return validationFields[k]
		}); // Making the object to array object
		validationFields.push("comm_customer_id");
		validationFields.splice(validationFields.indexOf("meter_remarks"), 1);

		isValid = validateField.apply(this, validationFields);
	} else {
		validationFields = $.extend(true, {}, fields); // Deep Copy of object
														// in JavaScript
		validationFields = Object.keys(validationFields).map(function(k) {
			return validationFields[k]
		}); // Making the object to array object
		validationFields = validationFields.slice();
		validationFields.push("comm_customer_id");
		validationFields.splice(validationFields.indexOf("meter_remarks"), 1);
		isValid = validateField.apply(this, validationFields);
		clearField("evc_sl_no", "evc_year", "evc_model");
	}
	if ($("#installed_by").chosen().val() == null) {
		cbColor($("#installed_by_chosen"), "e");
		isValid = false;
	} else
		cbColor($("#installed_by_chosen"), "v")

	if (isValid == false)
		return;
	$("#customer_id").val($("#comm_customer_id").val());

	meterInfoForm(enableField, enableChosenField);
	meterInfoForm(readOnlyField, null);
	disableButton("btn_save");
	var formData = new FormData($('form')[0]);
	$.ajax({
		url : "saveMeterInfo.action",
		type : 'POST',
		data : formData,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(response) {
			enableButton("btn_save");
			meterInfoForm(removeReadOnlyField, null);
			meterInfoForm(clearField, clearChosenField);
			meterInfoForm(enableField, enableChosenField);
			reloadGrid("meter_grid");

			$.jgrid.info_dialog(response.dialogCaption, response.message,
					$.jgrid.edit.bClose, jqDialogOptions);

		}
	});

}

function showHideEvc(measurementType) {
	if (measurementType == "" || measurementType == "0")
		$("#evc_div").hide();
	else
		$("#evc_div").show();
}

$("#installed_by").unbind();
$("#installed_by").chosen({
	no_results_text : "Oops, nothing found!",
	width : "56%"
});

$("#comm_customer_id").unbind();
$("#comm_customer_id").autocomplete($.extend(true, {}, acMCustomerOption, {
	serviceUrl : sBox.METERED_CUSTOMER_LIST,
	onSelect : function() {
		clearField.apply(this, fields_with_evc);
		getCustomerInfo("comm", $('#comm_customer_id').val());
		loadMeters($('#comm_customer_id').val(), preOperationWrapper);
		enableButton("btn_add");
	}
}));

Calendar.setup($.extend(true, {}, calOptions, {
	inputField : "installed_date",
	trigger : "installed_date"
}));

var $dialog = $('<div id="dialog-confirm"></div>').html(
		"<p> " + "Are you sure you want to delete the selected Meter? "
				+ "<div id='del_holiday'></div> " + "</p>").dialog({
	title : 'Meter Delete Confirmation',
	resizable : false,
	autoOpen : false,
	height : 150,
	width : 450,
	modal : true,
	buttons : {
		"Delete" : {
			text : "Delete",
			"class" : 'btn btn-danger',
			click : function() {
				deleteMeter();
			}
		},
		"Cancel" : {
			text : "Cancel",
			"class" : 'btn btn-beoro-3',
			click : function() {
				$(this).dialog("close");
			}
		},
	}
});

function deleteMeter() {

	$.ajax({
		url : 'deleteMeter.action?meter.meter_id=' + $("#meter_id").val(),
		type : 'POST',
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(response) {

			$dialog.dialog('close');
			meterInfoForm(clearField, clearChosenField);
			meterInfoForm(disableField, disableChosenField);
			reloadGrid("meter_grid");
			$.jgrid.info_dialog(response.dialogCaption, response.message,
					jqDialogClose, jqDialogParam);

		}
	});
}

function addButtonPressed() {
	if ($("#comm_customer_id").val() == "") {
		var message = "Please select a customer.";
		showDialog("Information", message);
	} else {
		meterInfoForm(clearField, clearChosenField);
		meterInfoForm(enableField, enableChosenField);
		clearField("customer_id", "meter_id");
	}
	
	//adding default value on temperature and pressure. updated on 13 September
	$("#pressure").val(1);
	$("#temperature").val(1);
	// End of- adding default value on temperature and pressure. updated on 13 September
	
	enableButton("btn_save");
	disableButton("btn_add", "btn_edit", "btn_delete");
}
function editButtonPressed() {
	meterInfoForm(enableField, enableChosenField);
	disableField("meter_sl_no", "evc_sl_no", "installed_date", "meter_rent",
			"pressure", "temperature");
	enableButton("btn_save");
	disableButton("btn_edit", "btn_add");
}

function cancelButtonPressed() {
	clearRelatedData();
	meterInfoForm(disableField, disableChosenField);
	disableButton("btn_add", "btn_edit", "btn_save", "btn_delete");
	unBlockGrid("meter_grid");
	resetSelection("meter_grid", "customer_grid");
}

function clearRelatedData() {
	clearField.apply(this, comm_customer_info_field);
	clearField.apply(this, fields_with_evc);
	clearGridData("meter_grid");
	disableField("btn_edit", "btn_delete");
	clearChosenField("installed_by");
}

function meterInfoForm(plainFieldMethod, chosenFieldMethod) {

	var entry_field_evc = [ "evc_sl_no", "evc_year", "evc_model" ];
	var chosen_field = [ "installed_by" ];

	plainFieldMethod.apply(this, fields);
	plainFieldMethod.apply(this, entry_field_evc);
	if (chosenFieldMethod != null)
		chosenFieldMethod.apply(this, chosen_field);
}
function clearRelatedData() {
	clearField.apply(this, comm_customer_info_field);
	clearGridData("meter_grid");
	meterInfoForm(clearField, clearChosenField);
}
$("#installed_date").val(getCurrentDate());
// focusNext("comm_customer_id");
meterInfoForm(disableField, disableChosenField);

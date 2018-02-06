var ajax_load="<img src='/JGTDSL_WEB/resources/images/loading/loading.gif' alt='loading...' />";
var sBase = location.href.substr(0, location.href.lastIndexOf("/") + 1);
var active_inactive={ value: "1:Active;0:Inactive" };
var transaction_type={ value: "0:Security/Other Deposit;1:Sales Collection;2:Bank Payment;3:Bank Receive;4:Bank Transfer"};  // From BankAccountTransactionType Enum

$.blockUI.defaults.overlayCSS.opacity = 0.15;
$.blockUI.defaults.overlayCSS.cursor = "pointer";

function replaceAll(find, replace, str) {
	  return str.replace(new RegExp(find, 'g'), replace);
}

function isValidPositiveNumber(number){
	if(isNaN(number) || number <= 0)  
	   return false;
	else
	   return true;
}

//AutoComplete Meter Customer Option
var acMCustomerOption={
    	minChars:2,    	
        serviceUrl: sBox.CUSTOMER_LIST,
        onHint: function (hint) {
            $('#customer_id_x').val(hint);
        },
        lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
            var re = new RegExp('\\b' + $.Autocomplete.utils.escapeRegExChars(queryLowerCase), 'gi');
            return re.test(suggestion.value);            
        },
        onChange: function(event,ui){
        	alert("ifti");
        	  $(this).val((ui.item ? ui.item.id : ""));
        	}
}

/*
$(document).ready(function() {
	    $.sessionTimeout({
	        warnAfter: 1800000,
	        redirAfter: 2700000,
	        logoutUrl: "logout.action",
	        redirUrl: "logout.action",
	        keepAliveUrl: "dashBoard.action"
	    });	  	    
	});
*/	

	function centerInfoDialog()
	{
	    var $infoDlg = $("#info_dialog");
	    var $parentDiv = $infoDlg.parent();
	    var dlgWidth = $infoDlg.width();
	    var parentWidth = $parentDiv.width();
	
	    $infoDlg[0].style.left = Math.round((parentWidth - dlgWidth) / 2) + "px";
	}


	function ajaxCalls() {
	    $(document).ajaxStart(function() {
	        $(document).bind("ajaxComplete", function(event, xmlHttprequest, ajaxOptions) {
	            if (xmlHttprequest.status == 200 && xmlHttprequest.responseText.match(/SessionTimeOut/)) {
	                alert("Your Session has been expired. You will redirect to login page.");
	                this.location = 'logout.action'; //put login url here
	                return false;
	            }
	        });
	    });
	}
	ajaxCalls();
	function loadPageUsingAjax(pageAction)
	{
		var actionUrl=sBase+pageAction;
		$("#contentPanel").html(ajax_load).load(actionUrl);
	}
	function ajaxLoad(targetDiv,pageAction)
	{
		var actionUrl=sBase+pageAction;
		$("#"+targetDiv).html(ajax_load).load(actionUrl);
	}
	
	function setPanelCaption(panelArea,caption)
	{
			$("#"+panelArea).html(caption); 
	}		
	
/*** callActionExtended created due to provide an easy fix for customer switch inside view customer
 * interface. It is a duplicated function. Later on we will refactor this(If possible).
 */
	function callActionExtended(pageAction,step){
		var actionUrl=sBase+pageAction;
		$("#tmp_span").html("<script type='text/javascript'>var step="+step+";</script>");
		
		$("#contentPanel").html(ajax_load).load(actionUrl);
		//2
		}
function saveBankInformation(){
	
	console.log($("#bank_id").val());
	var bank=$("#bank_id").val()==""?"01":$("#bank_id").val();
	var branch=$("#branch_id").val()==""?"0105":$("#branch_id").val();
	var account=$("#account_id").val();
	var deposit=$("#deposit_type_id").val();
	$("#deposite_bank_branch_span").html("<script type='text/javascript'>var bank_temp=\""+bank+"\"; var branch_temp=\""+branch+"\"; var account_temp=\""+account+"\"; var deposit_id_temp=\""+deposit+"\"; </script>");
}

	
function callAction(url){
	loadPageUsingAjax(url);
	}


function cbColor(element,type)
{
	 if(type=="e"){
		 element.css("border", "3px dashed #e67300");
	 	 element.css("border-radius", "5px");
	 }
		 
	 else if(type=="v"){
		 //element.css("border", "3px dashed #FFFFFF");
		 //element.css("border-radius", "5px");
	 }
		 
}

function getCurrentYear(){
	return (new Date).getFullYear();
}
function getCurrentMonth(){
	return (new Date).getMonth()+1;
}

function getRadioCheckedValue(radio_name){
	return $('input[name="'+radio_name+'"]:checked').val();
}
Number.prototype.padLeft = function(base,chr){
	   var  len = (String(base || 10).length - String(this).length)+1;
	   return len > 0? new Array(len).join(chr || '0')+this : this;
	}
function getCurrentDateTime(){
	
	var d = new Date,
    dformat = [ d.getDate().padLeft(),
    		    (d.getMonth()+1).padLeft(), 
                d.getFullYear()].join('-')+
                ' ' +
              [ d.getHours().padLeft(),
                d.getMinutes().padLeft()].join(':');
	/* original 
	 *  dformat = [ (d.getMonth()+1).padLeft(),
                    d.getDate().padLeft(),
                    d.getFullYear()].join('-')+
                    ' ' +
                  [ d.getHours().padLeft(),
                    d.getMinutes().padLeft(),
                    d.getSeconds().padLeft()].join(':');
                    
                    format :  MM/dd/yyyy HH:mm:ss
	 */
	return dformat;
	
}
function getCurrentDate(){
	
	var d = new Date,
    dformat = [ d.getDate().padLeft(),
    		    (d.getMonth()+1).padLeft(), 
                d.getFullYear()].join('-');
	/* original 
	 *  dformat = [ (d.getMonth()+1).padLeft(),
                    d.getDate().padLeft(),
                    d.getFullYear()].join('-')+
                    ' ' +
                  [ d.getHours().padLeft(),
                    d.getMinutes().padLeft(),
                    d.getSeconds().padLeft()].join(':');
                    
                    format :  MM/dd/yyyy HH:mm:ss
	 */
	return dformat;
	
}

function dayDifference(fromDate,toDate){
	
	    var fromDateArr = fromDate.split('-');
	    var toDateArr = toDate.split('-');
	    
	    var fDate= new Date(fromDateArr[2], fromDateArr[1]-1, fromDateArr[0]);
	    var tDate= new Date(toDateArr[2], toDateArr[1]-1, toDateArr[0]);
	    return (tDate-fDate)/(1000*60*60*24);
}
//Asynchronous File Upload
function afu() {
	
	
	
	
    $
        .ajaxFileUpload({
        	url: 'fileUploader.action?customerId=new&userType=customer',
            secureuri: false,
            fileElementId: 'upload',
            dataType: 'JSON',
            success: function(data, status) {
                if (data == 'Not a Valid type') {
                    alert("Not a Valid type");
                    // return false;
                } else if (data == "largefile") {
                    alert("File size smaller than 10KB is allowed.");
                    // return false;
                } else {
                    if (navigator.appName == 'Opera') {

                        document.getElementById('customer_photo').innerHTML = "";
                        document.getElementById('customer_photo').innerHTML = "<img src='/JGTDSL_WEB/"+data+"' />";
                    } else {
                        document.getElementById('customer_photo').innerHTML = "";
                        document.getElementById('customer_photo').innerHTML = "<img src='/JGTDSL_WEB/"+data+"' />";
                        //document.getElementById('customer_photo').src = "/JGTDSL_WEB/" + data + "";
                    }
                }

            },
            error: function(data, status, e) {
                alert(e);
            }
        });
}

var zone_sbox = { targetElm :"zone",zeroIndex : 'Select Zone',action_name:'fetchZone.action',data_key:'area_id'};
var district_sbox = { targetElm :"district_id",zeroIndex : 'Select District',action_name:'fetchDistrict.action',data_key:'division_id'};
var upazila_sbox = { targetElm :"upazila_id",zeroIndex : 'Select Upazila',action_name:'fetchUpazila.action',data_key:'district_id'};

var branch_sbox = { targetElm :"branch_id",zeroIndex : 'Select Branch',action_name:'fetchBranches.action',data_key:'bank_id'};
var account_sbox = { targetElm :"account_id",zeroIndex : 'Select Account',action_name:'fetchAccounts.action',data_key:'branch_id'};
var zone_sbox= {targetElm :"zone_id",zeroIndex : 'Select Moholla',action_name:'fetchZones.action',data_key:'area_id'};

function fetchSelectBox(obj)
{
 var sourceElm=obj.sourceElm;
 var targetElm=obj.targetElm;
 var zeroIndex=obj.zeroIndex;
 var action_name=obj.action_name;
 var data_key=obj.data_key;
 var data_value;

 if(typeof sourceElm !== "undefined")
	 data_value=$("#"+sourceElm).val();
 else
	 data_value=$("#"+data_key).val();

 $("#"+targetElm).empty();
 if(data_value=="")
  return null;
 else
  {		
	 if(targetElm!="account_id"){
		$("#"+targetElm).append( '<option value="">'+zeroIndex+'</option>' ); 
	 }
	
	var data_obj={};
	data_obj[data_key]=data_value;
	var objList = $.ajax({
		    url: action_name,
		    data: data_obj,
		    async: false,
		    success: function(data, result) {
		        if (!result)
		            alert('Failure to retrieve List.');
		    }
		}).responseText;
	
	if(objList.length==0) return;
	if(objList.indexOf("<html>")>=0) return;
	
	var targetList=objList.split(";");
	
	for(var i=0;i<targetList.length; i=i+1 )
	{
		var option=targetList[i].split(":");
		//var option=targetList[i];
		
//		if(zone[0]==branchId)
//			$("#zoneId").append( '<option value="' + branch[0] + '" selected="selected">' + branch[1] + '</option>' );
//		else			
			$("#"+targetElm).append( '<option value="' + option[0] + '">' + option[1] + '</option>' );
	}
	
  }
}

function centerCustomerInfoDialog(customer_id)
{
	/** Commented by Ifti (Request by Rubayet sir)
	 *  We want that the user should go to demand note page after creating a customer.
	 */
	/*
	$('<a href="#">New Customer<span class="ui-icon ui-icon-person"></span></a>')
            .click(function() {
            	$('#info_dialog').hide();
            	$(".ui-widget-overlay").hide();
                callAction('newCustomer.action');
                 
            }).addClass("fm-button ui-state-default ui-corner-all fm-button-icon-left")
              .prependTo("div#info_id>div.ui-widget-content.ui-helper-clearfix"); 
 	*/
$('<a href="#">Demand Note<span class="ui-icon ui-icon-note"></span></a>')
            .click(function() {            	
            	$('#info_dialog').hide();
            	$(".ui-widget-overlay").hide();
                callAction('demandNoteDataEntry.action?customer_id='+customer_id);
                
            }).addClass("fm-button ui-state-default ui-corner-all fm-button-icon-left")
              .prependTo("div#info_id>div.ui-widget-content.ui-helper-clearfix");              

              
 var $infoDlg = $("#info_dialog");
 var $parentDiv = $("#contentPanel");
 var dlgWidth = $infoDlg.width();
 var dlgHeight = $infoDlg.height();
 var parentWidth = $parentDiv.width();
 var parentHeight = $parentDiv.height();

 $infoDlg[0].style.left = Math.round((parentWidth - dlgWidth) / 2) + "px";
 $infoDlg[0].style.top = Math.round((parentHeight - dlgHeight) / 2) + "px"; 
}

function disableOnClick()
{
  $('.ui-widget-overlay').unbind( "click" );  
  $('.ui-widget-overlay').on("click", function() {
    //Close the dialog
});
}

function emptyCheck(elementId)
{
	if($.trim($("#"+elementId).val())=="")
  	 {cbColor($("#"+elementId),"e");return false;}
    else 
	 {cbColor($("#"+elementId),"v");return true;}
}
function disableField()
{
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).attr("disabled", true);  
	  }
}
function enableField()
{
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).attr("disabled", false);  
	  }
}

function copyField(fromField,toField){
	$("#"+toField).val($("#"+fromField).val());
}
function readOnlyField()
{
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).attr("readonly", true);
	    $("#"+arguments[i]).attr("onfocus", "this.blur()");
	  }
}
function removeReadOnlyField()
{
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).attr("readonly", false);
	    $("#"+arguments[i]).removeAttr("onfocus");
	  }
}

function disableButton()
{
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).prop("disabled", true);  
	  }
}
function enableButton()
{
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).prop("disabled", false);  
	  }
}
function isEmpty(elementId)
{
	if($.trim($("#"+elementId).val())=="")
  	 return true;
    else 
	 return false;
}
function reloadGrid(gridId){
	$("#"+gridId).trigger("reloadGrid");
}
function reloadGridWithData(gridData,gridId){
	var grid = jQuery("#"+gridId);
	clearGridData(gridId);
	if( grid.get(0).p.treeGrid ) {
	    grid.get(0).addJSONData({
	        total: 1,
	        page: 1,
	        records: gridData.length,
	        rows: gridData
	    });
	}
	else {
	    grid.jqGrid('setGridParam', {
	        datatype: 'local',
	        data: gridData,
	        rowNum: gridData.length
	    });
	}
	reloadGrid(gridId);
}

function getAllJqGridRows(gridId){
	return $('#'+gridId).getGridParam('data');
	}

$.fn.safeUrl=function(args){

	var that=this;
	var img = new Image();
	   img.src = args.wanted;
	if(img.height == 0)
		   $(that).attr('src',args.rm);
	else
		$(that).attr('src',args.wanted);
	  /*
	  var that=this;
	  if($(that).attr('data-safeurl') && $(that).attr('data-safeurl') === 'found'){
	        return that;
	  }else{
	    $.ajax({
	    url:args.wanted,
	    type:'HEAD',
	    error:
	        function(){
	            $(that).attr('src',args.rm)
	        },
	    success:
	        function(){
	             $(that).attr('src',args.wanted)
	             $(that).attr('data-safeurl','found');
	        }
	      });
	   }


	 return that;
	 */
	   return that;
};

function resetCmColor(aId)
{ 
 $("#cm_new_meter").css("background-color","white");
 $("#cm_disconnect").css("background-color","white");
 $("#cm_reconnect").css("background-color","white");
 $("#cm_burner").css("background-color","white");
 $("#cm_load").css("background-color","white");
 $("#"+aId).css("background-color","#d9edf2");
}

function setInitItemForSelectBox(initItem,selectBox)
{
	if(selectBox.length>0)
		selectBox=initItem.concat(selectBox);
	return selectBox;	
}
function showElement(){
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).show();
	  }
}
function hideElement(){
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).hide();
	  }
}
function hideElementByClass(){
	for (var i = 0; i < arguments.length; i++) {
	    $("."+arguments[i]).hide();
	  }
}
function clearField()
{	 
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).val("");
	  }
}
function clearChosenField(){
	
	for (var i = 0; i < arguments.length; i++) {
	    $('#'+arguments[i]).val("").trigger('chosen:updated');
	  }
}
function setChosenData(field_id,comma_seperated_value){
	
	if (typeof comma_seperated_value !== "undefined" && comma_seperated_value !== null && comma_seperated_value != ""){		
		$("#"+field_id).val((comma_seperated_value).split(", ")).trigger('change');
		$('#'+field_id).trigger('chosen:updated');
	}
}
function disableChosenField(){
	
	for (var i = 0; i < arguments.length; i++) {
	    $('#'+arguments[i]).attr("disabled", "disabled").trigger("chosen:updated");
	  }
}
function enableChosenField(){
	
	for (var i = 0; i < arguments.length; i++) {
	    $('#'+arguments[i]).attr("disabled", false).trigger("chosen:updated");
	  }
}
function clearHtml()
{	 
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).html("");
	  }
}
function setupCalendar(){
	
	for (var i = 0; i < arguments.length; i++) {
			Calendar.setup($.extend(true, {}, calOptions,{
		    inputField : arguments[i],
		    trigger    : arguments[i],
		    onSelect   : function() { this.hide();}}));
	  }
}

function isEmpty(variable)
{
	if (variable === undefined || variable === null)
	    return true;
	else 
		return false;
}

function showResponseMessageBox(response)
{
    if(response.status=="OK")
		$("#rightSpan").html(getResponseMessageBox(response.message));
	
	$('.ui-widget-overlay').unbind( "click" );
}
function getResponseMessageBox(message)
{
	return '<div class="w-box">'+
    '<div class="w-box-header">'+
        '<h4>Confirmation Message</h4>'+
    '</div>'+
    '<div class="w-box-content" style="padding: 10px;" id="content_div">'+
         '<center>'+message+                            
         '</center>'+
    '</div>'+
    '</div>';
}
function focusNext(next_focus_field_id)
{      
	//var ntabindex = parseFloat($("#installed_date").attr('tabindex'));       
	//ntabindex++;
	$(window).focus(); 
	$('#'+next_focus_field_id).focus(); 
}
function showMandatoryStar(){
	$( "m.man" ).replaceWith( jsVar.MANDATOR );
}

//jqGrid related Functions used by all jqGrids in the application
//This is for jqGrid Table header title label change and header text alignment.
function gridColumnHeaderAlignment(align,grid_id,field_array)
{	 
	for (var i = 0; i < field_array.length; i++) {
	    jQuery("#"+grid_id).jqGrid("setLabel",field_array[i],"",{"text-align":align});
	  }
}

function brToNewLine(cellValue, options,rowObject) {
	 return cellValue.replace(/\<br\/\>/g,'\n');
}


function showInfoDialog(message){
	$.jgrid.info_dialog(jqCaption.INFORMATION, message,$.jgrid.edit.bClose,
		{
			zIndex: 1500,
			width:400,	
			onClose: function () {
		    	return true; // allow closing
			}
		});
	}
//No use of it right now. We can remove that later on
function showError(message){
	var erroBlock = getErrorBlockHTML(message),
	infoTR = $("table#TblGrid_"+grid[0].id+">tbody>tr.tinfo"),
	infoTD = infoTR.children("td.topinfo");
	infoTD.html(erroBlock);
	infoTR.show();
}
function jqGridDataPostResponseHandler(response){		
	    var result="";
	    try{
	    	result = JSON.parse(response.responseText);
	    }
	    catch(e){
	    	return [false,'Server Exception occured. Please contact with Sytem Admin.',null];
	    }
	    
		var message=result.message;
		var status=result.status;
		if(status=="ERROR"){
			//showError(response.message);
			return [false,message,null];
		}
		else{
			showInfoDialog(message);
			return [true,message,null];
		}
}		
function jqGridDataDeleteResponseHandler(response){		
		var result = JSON.parse(response.responseText);
		$.jgrid.info_dialog(jqCaption.INFORMATION, result.message,$.jgrid.edit.bClose, {
	           zIndex: 1500,
	           width:400,
	           onClose: function () {
	               return true; // allow closing
	           }		
		  });		
		if(result.status=="ERROR")
			return [false,result.message,null];
		else
			return [true,result.message,null];
}

function formatErrorText(data){
   alert("Yahoo!! I'm in errorTextFormat method");
   if (data.responseText.substr(0, 6) == "<html ") {
        return jQuery(data.responseText).html();
    }
    else {
        return "Status: '" + data.statusText + "'. Error code: " + data.status;
    }
}
function getPostFilter(grid_id,ruleArray){
	
	var rules=getRuleArrayAsString(ruleArray);
	var filters='{"groupOp":"AND","rules":['+rules+']}';
	var postdata = $("#"+grid_id).jqGrid('getGridParam', 'postData');
	jQuery.extend(postdata,{filters:filters});
	return postdata;
}
function getRuleArrayAsString(ruleArray){
	
	var fieldArray=ruleArray[0];
	var operatorArray=ruleArray[1];
	var dataArray=ruleArray[2];
	
	var rules='';
	for(var i=0;i<fieldArray.length;i++){		
		rules+='{"field":"'+fieldArray[i]+'","op":"'+operatorArray[i]+'","data":"'+dataArray[i]+'"},';
	}
	if(rules.length>0)
 		rules=rules.substring(0, rules.length-1);
	
	return rules;
}

var scrollPagerGridOptions={
		datatype: 'local',
		rowNum:120,
		scroll:1,
		loadonce:false,
		mtype: "GET",
		rownumbers: true,
		rownumWidth: 35,
		gridview: true,
		viewrecords: true,
		footerrow: false
		};


var footerButton={edit: false, add: false, del: false, search: false, refresh:false,view:false};

function getGridBlockUI(grid_id){
	return jQuery("#"+grid_id).closest('.ui-jqgrid');
}
var uiBlockOption={
    message: "",
    css: {}
};

function blockGrid(grid_id){	
	getGridBlockUI(grid_id).block(uiBlockOption); 
	resetSelection("meter_grid");
}
function unBlockGrid(grid_id){
	getGridBlockUI(grid_id).unblock();
}
function resetSelection(){
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).jqGrid('resetSelection');
	  }
	
}
function clearGridData(){
	for (var i = 0; i < arguments.length; i++) {
		if($("#"+arguments[i]).jqGrid('getGridParam', 'reccount')>0)
			$("#"+arguments[i]).jqGrid('clearGridData');
	  }	
}
var jqDialogOptions={
        zIndex: 1500,
        width:450,
        beforeOpen: centerInfoDialog,
        afterOpen:disableOnClick,
        onClose: function () {
            return true; 
        }
};

//End of jqGrid Functions..

function updateTips( t ) {
    tips
      .text( t )
      .addClass( "ui-state-highlight" );
    setTimeout(function() {
      tips.removeClass( "ui-state-highlight", 1500 );
    }, 500 );
  }

function unbindKeyPress(){
	$(document).unbind("keypress");
}

function validateField(){
	var isValid=true;
	var element;
	for (var i = 0; i < arguments.length; i++) {
		element=$("#"+arguments[i]);
		if(element && $.trim(element.val())=="")
		{cbColor(element,"e");isValid=false;}
		 else cbColor(element,"v");	  
	  }
	return isValid;
}
function resetErrorBorder(){
	for (var i = 0; i < arguments.length; i++) {
		cbColor($("#"+arguments[i]),"v"); 	  
	  }
}
function autoSelect(){	
	for (var i = 0; i < arguments.length; i++) {
	 var length = $('#'+arguments[i]+' > option').length;	
     if(length>=2){
     	$('#'+arguments[i]+' option:first-child').next().prop("selected", true);
     }
	}
}
function resetSelectBoxSelectedValue()
{	 
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]+" option:selected").prop("selected",false);
	  }
}
function getSelectedText(selectBox_Id){
	return $("#"+selectBox_Id+" option:selected").text();
}

function navCache(action)
{
	$.jStorage.set(jsVar.LOCAL_ACTION_STORAGE,action);	
}
function lpad(value, padding) {
    var zeroes = "0";
    
    for (var i = 0; i < padding; i++) { zeroes += "0"; }
    
    return (zeroes + value).slice(padding * -1);
}

function clearSelectBox()
{
	for (var i = 0; i < arguments.length; i++) {
	    $("#"+arguments[i]).find('option:gt(0)').remove();
	  }
}
function addOption(select_box_id,val,txt){
	$('#'+select_box_id).append($('<option>', {
    	value: val,
    	text: txt
	}));

}
function closeModal(){
	var body = $(document.body);
	body.find('.nswindowOverlay,.nswindowContainer').hide();
}

function checkInput(input_id){
	var cid;
	var input_value=$("#"+input_id).val();
	var validInput=false;
	$('.autocomplete-suggestion').each(function(i, obj) {
		
		cid=replaceAll('</strong>','',replaceAll('<strong>','',$(this).html()));
		
		if(input_value==cid){validInput=true;}
	});
	if(validInput==false) {
		$("#"+input_id).val("");
		clearRelatedData();	 //Each page should implement this method to meet that pages requirement.
							 //Each page will clear its related data when wrong id is provided in the customer id field.
	}
}
function setTitle(page_title){
	$("#page_title").html(page_title);
}
	
/* Set Json Data to Customer Personal Info Form */
var comm_customer_info_field=["comm_customer_id","comm_isMetered_name","comm_area","comm_customer_category","comm_full_name","comm_father_name","comm_customer_address"];
var customer_info_field=["customer_id","isMetered_name","area","area_id","customer_category","full_name","father_name","customer_address","mobile"];

function getFieldValueFromSelectedGridRow(grid_id,field_name){
	
	var rowId =$("#"+grid_id).jqGrid('getGridParam','selrow');  
	var rowData = jQuery("#"+grid_id).getRowData(rowId);
	return rowData[field_name]; 
	
}
function getCustomerInfo(field_prefix,customer_id,callback)
{

    if(customer_id=="") return;
	$.ajax({
   		  type: 'POST',
   		  url: 'getCustomerInfoAsJson.action',
   		  data: { customer_id:customer_id},
   		  success:function(data){
			if(typeof data.personalInfo === "undefined")
			{
				clearCustomerInfoForm(field_prefix);
			    alert("Sorry, this is not either a valid customer or a customer whose connection has not yet been established.");
			}
			else
			{
				setCustomerInfo(field_prefix,data);	 
				if(typeof callback !== 'undefined'){
					callback(data);
				}
			}
						
   		  },
   		  error:function(){
   			
   		  }
   	});
}

function setCustomerInfo(field_prefix,data){
	
	var prefix="";
	if(field_prefix!=""){
		prefix=field_prefix+"_";
	}
	
	clearCustomerInfoForm(field_prefix);
	
	var personal=data.personalInfo;
	var address=data.addressInfo;
	var connection=data.connectionInfo;
	
	if(typeof data.personalInfo === "undefined")
	{}
	else{
	$("#"+prefix+"customer_id").val(personal.customer_id);
	if($("#"+prefix+"isMetered_name")) $("#"+prefix+"isMetered_name").val(connection.isMetered_name);
	if($("#"+prefix+"area_id")) $("#"+prefix+"area_id").val(data.area);
	if($("#"+prefix+"customer_category")) $("#"+prefix+"customer_category").val(data.customer_category);
	if($("#"+prefix+"full_name")) $("#"+prefix+"full_name").val(personal.full_name);
	if($("#"+prefix+"father_name")) $("#"+prefix+"father_name").val(personal.father_name);
	if($("#"+prefix+"mother_name")) $("#"+prefix+"mother_name").val(personal.mother_name);
	
	if($("#"+prefix+"mobile")) $("#"+prefix+"mobile").val(personal.mobile);
	if($("#"+prefix+"phone")) $("#"+prefix+"phone").val(personal.phone);
	if($("#"+prefix+"fax")) $("#"+prefix+"fax").val(personal.fax);
	if($("#"+prefix+"national_id")) $("#"+prefix+"national_id").val(personal.national_id);
	if($("#"+prefix+"gender")) $("#"+prefix+"gender").val(personal.gender);
	if($("#"+prefix+"isMetered_str")) $("#"+prefix+"isMetered_str").val(connection.isMetered_str);	
	if($("#"+prefix+"passport_no")) $("#"+prefix+"passport_no").val(personal.passport_no);
	//inserted on sept 19 ~Prince
	if($("#"+prefix+"email")) $("#"+prefix+"email").val(personal.email);
	if($("#"+prefix+"tin")) $("#"+prefix+"tin").val(personal.tin);
	if($("#"+prefix+"organization_name")) $("#"+prefix+"organization_name").val(personal.organization_name);
	//for image
	if($("#"+prefix+"img_photo")) $("#"+prefix+"img_photo").attr("src", personal.img_url);
	//
	$("#"+prefix+"customer_address").val(getCustomerAddress(address));
	}
}
function getCustomerAddress(address){
	var ads="";
	
	var divStr=(address.division_name=="" || typeof address.division_name === "undefined" )?"":"Division : "+address.division_name;
	var distStr=(address.district_name=="" || typeof address.district_name === "undefined" )?"":"District : "+address.district_name;
	var uzillaStr=(address.upazila_name=="" || typeof address.upazila_name === "undefined" )?"":"Upazila : "+address.upazila_name;
	var postOfficeStr=(address.post_office=="" || typeof address.post_office === "undefined" )?"":"Post Office : "+address.post_office;
	var postCodeStr=(address.post_code=="" || typeof address.post_code === "undefined" )?"":"Post Code : "+address.post_code;
	var roadStr=(address.road_house_no=="" || typeof address.road_house_no === "undefined" )?"":"Road/Street # : "+address.road_house_no;
	var adr1Str=(address.address_line1=="" || typeof address.address_line1 === "undefined" )?"":address.address_line1;
	var adr2Str=(address.address_line2=="" || typeof address.address_line2 === "undefined" )?"":address.address_line2;
	
	var addressBuffer = [adr1Str,adr2Str,roadStr,postCodeStr,postOfficeStr,uzillaStr,distStr,divStr];
	addressBuffer=addressBuffer.filter(Boolean);
	/*
	ads="Division : "+address.division_name+", District :"+address.district_name+", Upazila :"+address.upazila_name;
	ads+=", Post Office :"+address.post_office+", Post Code :"+address.post_code+", Road/Street # :"+address.road_house_no;
	ads+=", "+address.address_line1+", "+address.address_line2;
	*/
	return addressBuffer.join(", ");
}
function clearCustomerInfoForm(field_prefix){
	var prefix="";
	if(field_prefix!=""){
		prefix=field_prefix+"_";
	}
	clearField(prefix+"customer_id",prefix+"isMetered_name",prefix+"area",prefix+"customer_category",prefix+"full_name",prefix+"father_name",prefix+"customer_address");
	
	if($(".img-holder"))
		$(".img-holder").html(jsImg.AVATAR_80);	 
}

//Commonly used for jqGrid filter feature. This method will be called during onSearch Function
function modifyGridPostData(grid_id,static_rules,filter_fields_old,filter_fields_new){
	
	//Appending static filter rule with dynamic filter rules	
    var i, l, rules, rule, $grid = $('#'+grid_id),
        postData = $grid.jqGrid('getGridParam', 'postData');
    
    var ruleArray=static_rules;
    var filters = $.parseJSON(postData.filters);
    if (filters && typeof filters.rules !== 'undefined' && filters.rules.length > 0) {
        rules = filters.rules;
        for (i = 0; i < rules.length; i++) {
            rule = rules[i];
            
			   var elm_index=$.inArray(rule.field, filter_fields_old);
            if (elm_index>=0) {
                rule.field = filter_fields_new[elm_index];
            }
			   
			   
            ruleArray[0].push(rule.field);
            ruleArray[1].push(rule.op);
            ruleArray[2].push(rule.data);
        }		           
    }		       
		var postdata=getPostFilter(grid_id,ruleArray);
		$("#"+grid_id).jqGrid('setGridParam',{postData: postdata});
}

//Calendar Common Options
var calOptions={
		eventName : "focus",
	    onSelect   : function() { this.hide();},
	    showTime   : 12,
	    dateFormat : "%d-%m-%Y",
		showTime : false,
		onBlur: function() { this.hide(); }
};

//jqGridDialog
function showDialog(title,message){
	$.jgrid.info_dialog(title,message,jqDialogClose,jqDialogParam);
}

//Common column model for all customer grid
//Customer Category
var cCategory="01:Non Metered Domestic;02:Commercial;03:Domestic Metered(Government);04:Industry;05:Tea Estate;06:Captive Power;07:CNG;08:Fertilizer;09:Non Metered Domestic(Govt);10:Domestic Metered;13:Power;15:Seasonal";  
var cStatus="0:Disconnected;1:Connected;2:Newly Applied";

var customerGridColModel=[
		{
            name: 'customer_id',
            index: 'customer_id',
            width:70,
            align:'center',
            sorttype: 'string',
            search: true
    	},
    	{
            name: 'full_name',
            index: 'full_name',
            width:160,
            sorttype: "string",
            search: true,
    	},
    	{
            name: 'father_name',
            index: 'father_name',
            width:160,
            sorttype: "string",
            search: true,
    	},
    	{
            name: 'category_id',
            index: 'category_id',
            width:100,
            sorttype: "string",
            search: true,
            stype:"select",
            edittype: "select",
            formatter: "select",
            editoptions: { value: cCategory },
            searchoptions: { 
            	value: cCategory, 
            	defaultValue: "01" 
            }
    	},
    	{
            name: 'area_name',
            index: 'area_name',
            width:150,
            sorttype: "string",
            search: true,
    	},
    	{
            name: 'phone',
            index: 'phone',
            width:50,
            sorttype: "string",
            search: true,
    	},
        {
            name: 'connection_status',
            index: 'connection_status',
            sorttype: "string",
            width:60,
            align:'center',
            stype:"select",
            edittype: "select",
            formatter: "select",
            editoptions: { value: cStatus },
            searchoptions: { 
            	value: cStatus, 
            	defaultValue: "01" 
            }
    	},
    	{
            name: 'created_on',
            index: 'created_on',
            sorttype: "string",
            hidden: false,
    	}
		
];

var statusJson="1:Active;0:Inactive";

var monthYearCalOptions={
	     changeMonth: true,
	     changeYear: true,
	     dateFormat: 'MM yy',
	     onClose: function() {
	        var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
	        var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
	        $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
	     },

	     beforeShow: function() {

	       if ((selDate = $(this).val()).length > 0)
	       {
	          iYear = selDate.substring(selDate.length - 4, selDate.length);
	          iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5),
	                   $(this).datepicker('option', 'monthNames'));
	          $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
	          $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
	       }
	    }
	  };

var monthNames = [ "January", "February", "March", "April", "May", "June",
                   "July", "August", "September", "October", "November", "December" ];



//Date Months array between two given months in January 2015 format
function monthsBetweenTwoMonth(from, to) {
    var arr = [];
    var datFrom = new Date('1 ' + from);
    var datTo = new Date('1 ' + to);
    var fromYear =  datFrom.getFullYear();
    var toYear =  datTo.getFullYear();
    var diffYear = (12 * (toYear - fromYear)) + datTo.getMonth();

    for (var i = datFrom.getMonth(); i <= diffYear; i++) {
        arr.push(monthNames[i%12] + " " + Math.floor(fromYear+(i/12)));
    }        

    return arr;
}

function calcuateEndMonthYear(fromMonthYear,totalNumberOfMonth){
	var parts=fromMonthYear.split(", ");
	var endDate = new Date(parts[1],monthNames.indexOf(parts[0]),1);
	endDate.setMonth(endDate.getMonth() + (parseInt(totalNumberOfMonth)-1));
	var endDateString= monthNames[endDate.getMonth()]+", "+endDate.getFullYear();
	return endDateString;

}

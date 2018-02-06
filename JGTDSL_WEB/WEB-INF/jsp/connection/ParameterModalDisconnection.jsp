<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="modal_div1"></div>
<script type="text/javascript">
var dialog, form;
var param_month,param_year,allFields,tips;
      
if($('#dialog-form').length==0){
var modal_html="<div id='dialog-form' title='Common default values'>"+
 "<p class='validateTips'>Month-Year are required</p>"+
 "<form>"+
      "<div class='row-fluid'>    "+
		   "<div class='span12'>"+
		       "<label style='width: 25.5%'>Month-Year</label>"+
				"<select id='parameter_month' style='width: 40%;margin-left: 0px;'>"+
		       	"<option value=''>Select Month</option>           "+
		        "<s:iterator  value='%{#application.MONTHS}'>   "+
		   		"	<option value='<s:property value='id'/>'><s:property value='label'/></option>"+
				"</s:iterator>"+
		       "</select>"+
		       "<select id='parameter_year' style='width: 30.5%;margin-left: 5px;'>"+
		       	"<option value=''>Year</option>"+
		       	"<s:iterator  value='%{#application.YEARS}' id='year'>"+
		         "   <option value='<s:property/>'><s:property/></option>"+
				"</s:iterator>"+
		       "</select>"+
		   "</div>"+
	  "</div>"+	  
	  "<div class='row-fluid'>    "+
		"   <div class='span12'>"+
		 "      <label style='width: 25.5%'>R. Date</label>"+
		 	"	<input type='text' id='parameter_reading_date'  style='width: 67.5%'  />"+
		   "</div>"+
	  "</div>"+
	  "<!-- Allow form submission with keyboard without duplicating the dialog button -->"+
      "<input type='submit' tabindex='-1' style='position:absolute; top:-1000px'>"+
      "</form>"+
"</div>";

$("#modal_div1").html(modal_html);
param_month = $( "#parameter_month" ),
param_year = $( "#parameter_month" ),
param_reading_purpose = $( "#parameter_reading_purpose" ),
allFields = $( [] ).add( param_month ).add( param_year ).add(param_reading_purpose),
tips = $( ".validateTips" );

Calendar.setup({
        inputField : "parameter_reading_date",
        trigger    : "parameter_reading_date",
		eventName : "focus",
        onSelect   : function() { this.hide()},
        dateFormat : "%d-%m-%Y",
		showTime : false
      });
      

dialog = $( "#dialog-form" ).dialog({
  autoOpen: false,
  height: 375,
  width: 350,
  modal: true,
  buttons: {
    "Next": setReadingCommonValues,
    Cancel: function() {
      changeReadingFormMode("disable_mode");
      dialog.dialog( "close" );
      
    }
  },
  close: function() {
    form[ 0 ].reset();
    allFields.removeClass( "ui-state-error" );
  },
  beforeClose: function(event, ui) {
  	//focusNext("customer_id");
  	}
});
 	      
}

function setReadingCommonValues(){  
  $("#reading_date").val($("#parameter_reading_date").val());
  changeReadingFormMode("disable_mode");
  var valid = true;
  allFields.removeClass( "ui-state-error" );
if(param_month.val()==""){
 	valid=false
 	param_month.addClass( "ui-state-error" );
 	updateTips( "You must select a valid Month" );
 }
else if(param_year.val()==""){
 	valid=false
 	param_year.addClass( "ui-state-error" );
 	updateTips( "You must select a valid Year" );
 }
else if(param_reading_purpose.val()==""){
 	valid=false
 	param_reading_purpose.addClass( "ui-state-error" );
 	updateTips( "You must select billing purpose" );
 } 
 
else{
	valid=true;
  	$("#billing_month").val($("#parameter_month").val());
	$("#billing_year").val($("#parameter_year").val());
	$("#area_id").val($("#parameter_area_id").val());
	$("#curr_reading_date").val($("#parameter_reading_date").val());
	$("#disconnect_date").val($("#parameter_reading_date").val());
	    	   	    
	dialog.dialog("close");
}    	
	return valid;
}
</script>
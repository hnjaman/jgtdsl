$("#reading_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_READING_SERVICE+'&method='+jsEnum.METER_READING_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "reading_id"
	},
   colNames: ['Reading Id', 'Customer Id', 'Customer Name','Meter SL', 'Actual Con.','Total Con.','Insert Date','Reading Purpose'],
   colModel: [{
	                name: 'reading_id',
	                index: 'reading_id',
	                hidden: true
            	},
            	{
	                name: 'customer_id',
	                index: 'customer_id',
	                sorttype: "string",
	                width:50,
	                align:'center',
	                search: true
	                
            	},
            	{
	                name: 'customer_name',
	                index: 'customer_name',
	                width:200,
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'meter_sl_no',
	                index: 'meter_sl_no',
	                sorttype: "string",
	                width:100,
	                search: true,
            	},
            	{
	                name: 'actual_consumption',
	                index: 'actual_consumption',
	                sorttype: "integer",
	                width:100,
	                align:'right'
            	},
                {
                    name: 'total_consumption',
                    index: 'total_consumption',
                    sorttype: "integer",
                    width:100,
                    align:'right'
            	},
                {
                    name: 'insert_date',
                    index: 'insert_date',
                    sorttype: "string",
                    width:60,
                    align:'center'
            	},
                {
            		
                    name: 'reading_purpose_str',
                    index: 'reading_purpose_str',
                    sorttype: "string",
                    width:60,
                    align:'center'
            	}
        ],
	//datatype: 'json',
	height: $("#reading_grid_div").height()-80,
	width: $("#reading_grid_div").width(),
   	pager: '#reading_grid_pager',
   	sortname: 'insert_date',
    sortorder: "desc",
	caption: "List of Meter Reading",
	onSelectRow: function(id){ 
		var rowData = $("#reading_grid").getRowData(id);
		getCustomerInfo("",rowData.customer_id);	
		fetchMeterReadingInfo(id);
		
   }
}));
jQuery("#reading_grid").jqGrid('navGrid','#reading_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {reloadReadingGrid();}}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","reading_grid",["full_name","category_name","area_name","mobile"]);
/*
function reloadReadingGrid(){
	var ruleArray=[[" METER_RENT_CHANGE.CUSTOMER_ID"],["eq"],[customer_id]];
	var postdata=getPostFilter("meterRent_change_history_this_grid",ruleArray);
    $("#reading_grid_pager").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("reading_grid");
}*/
function reloadReadingGrid(){
	
	
	var ruleArray=[];
	var ruleOpArray=[];
	var ruleNameArray=[];
	var ruleValueArray=[];

	if($("#billing_month").val()!="")
		{ruleNameArray.push("mr.billing_month");ruleOpArray.push("eq");ruleValueArray.push($("#billing_month").val());}
	if($("#billing_year").val()!="")
		{ruleNameArray.push("mr.billing_year");ruleOpArray.push("eq");ruleValueArray.push($("#billing_year").val());}
	if($("#area").val()!="")
	 	{ruleNameArray.push("vc.area_id");ruleOpArray.push("eq");ruleValueArray.push($("#area_id").val());}
	if($("#customer_category").val()!="")
	 	{ruleNameArray.push("vc.category_id");ruleOpArray.push("eq");ruleValueArray.push($("#customer_category").val());}
	 	
	var ruleArray=[ruleNameArray,ruleOpArray,ruleValueArray];
	var postdata=getPostFilter("reading_grid",ruleArray);
    $("#reading_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    	
   	reloadGrid("reading_grid");
   	clearSearchOptions();
   	
}
function clearSearchOptions(){
    $("#reading_grid").jqGrid('setGridParam', { search: true, postData: { "filters": ""} }).trigger("reloadGrid");
}

//Reading History Grid
/*
jQuery("#reading_history_grid").jqGrid({
   	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_READING_SERVICE+'&method='+jsEnum.METER_READING_HISTORY_LIST,
   	jsonReader: {
            repeatitems: false,
            id: "reading_id"
	},
    colNames: ['Reading Id','Date', 'Month-Year','Previous','Present','Difference'],
    colModel: [{
					name: 'reading_id',
					index: 'reading_id',
					hidden: true
            	},
            	{
					name: 'insert_date',
					index: 'insert_date',
					width:120,
					align:'center',
					sorttype: 'string',
					search: true
            	},
            	{
	                name: 'month_year',
	                index: 'month_year',
	                sorttype: "string",
	                width:110,
	                align:'center',
	                search: true,
            	},{
	                name: 'prev_reading',
	                index: 'prev_reading',
	                align:'right',
	                sorttype: 'number',
	                formatter: 'number',
	                search: true
            	},{
	                name: 'curr_reading',
	                index: 'curr_reading',
	                align:'right',
	                sorttype: 'number',
	                formatter: 'number',
	                search: true
            	},{
	                name: 'difference',
	                index: 'difference',
	                align:'right',
	                sorttype: 'number',
	                formatter: 'number',
	                search: true
            	}
        ],   	
	datatype: "local",
	height: $("#reading_history_grid_div").height()-102,
	width: $("#reading_history_grid_div").width()+10,
   	rowNum:200,
	rowList : [20,30,50],
	scroll:1,
	loadonce:false,
   	mtype: "GET",
	rownumWidth: 40,
	gridview: true,
   	pager: '#reading_history_grid_pager',
   	sortname: 'CURR_READING_DATE',
    viewrecords: true,
    sortorder: "desc",
	caption: "Meter Reading Details",
	footerrow: true,
	loadComplete: function () {
                    var sum = jQuery("#reading_history_grid").jqGrid('getCol', 'difference', false, 'sum');
                    jQuery("#reading_history_grid").jqGrid('footerData','set', {month_year: 'Total:', difference: sum});
                },
    onSelectRow: function(id){ 
		fetchMeterReadingInfo(id);
   	}
	
});
jQuery("#reading_history_grid").jqGrid('navGrid','#reading_history_grid_pager',$.extend({},footerButton,{search:true,refresh:true}),{},{},{},{multipleSearch:true});

$("#reading_history_grid_pager_left").css("width","50%");
//Add Show all button on reading history table
$('#reading_history_grid').jqGrid('navGrid','#reading_history_grid_pager')
    .navButtonAdd('#reading_history_grid_pager',{
        caption:"All", 
        buttonicon:"ui-icon-clipboard", 
        id: "lightbulb1",
        onClickButton: function(){
        	showReadingHistory("all");
        }
}).navButtonAdd('#reading_history_grid_pager',{
        caption:"Only this year", 
        buttonicon:"ui-icon-clipboard", 
        id: "lightbulb2",
        onClickButton: function(){        
			showReadingHistory("this_year");
        }
});   
*/
// end of reading-history grid

function fetchMeterReadingInfo(reading_id){
	$.ajax({
		    url: 'getMeterReadingInfo.action',
		    type: 'POST',
		    data: {reading_id:reading_id},
		    cache: false,
		    success: function (response) {
		    	setReadingInfo(response);	    			    	
		    	changeReadingFormMode("disable_mode");
		    	//disableButton("btn_add","btn_save");
		    	//enableButton("btn_edit","btn_delete");
		    
		    }
		    
		  });	
}


function showReadingHistory(type){
	if($("#customer_id").val()=="")
		alert("Please select a customer to view records.");
	else{
	    var ruleArray;
	    if(type=="all")
	    	ruleArray=[[" mr.customer_id"],["eq"],[$("#customer_id").val()]];
	    else if(type=="this_year")
	        ruleArray=[[" mr.customer_id","mr.billing_year"],["eq","eq"],[$("#customer_id").val(),$("#billing_year").val()]];
		var postdata=getPostFilter("reading_history_grid",ruleArray);
	    $("#reading_history_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'}); 
	    reloadGrid("reading_history_grid");
	    $("#reading_history_grid").jqGrid('setCaption', 'Meter Reading Details for '+$("#full_name").val());
	}
}

    
    
form = dialog.find( "form" ).on( "submit", function( event ) {
  event.preventDefault();
  setReadingCommonValues();
});
     
 	

$( "#btn_parameter" ).on( "click", function() {
	dialog.dialog( "open" );     
	setValueToModalParameters(); 	
});
    
function setValueToModalParameters(){
	$("#parameter_month").val($("#billing_month").val()==""?getCurrentMonth():$("#billing_month").val());
	$("#parameter_year").val($("#billing_year").val()==""?getCurrentYear():$("#billing_year").val()); 	
	$("#parameter_area_id").val($("#area_id").val()==""?"01":$("#area_id").val());
	$("#parameter_customer_category").val($("#customer_category").val()==""?"01":$("#customer_category").val());
	$("#parameter_reading_purpose").val($("#reading_purpose_str").val()==""?"2":$("#reading_purpose_str").val());    	
	$("#parameter_reading_date").val($("#curr_reading_date").val()==""?getCurrentDate():$("#curr_reading_date").val());
}


	dialog.dialog("open");
	setValueToModalParameters();

function fetchReadingEntry(direction){
	
var customerId="";
if(entry_type=="customer")
customerId=$("#customer_id").val();
 $.ajax({
	  type: 'POST',
	  url: 'fetchReadingEntry.action',
	  data: {direction:direction,customer_id:customerId,meter_id:"",billing_month:$("#billing_month").val(),billing_year:$("#billing_year").val(),reading_purpose:$("#reading_purpose_str").val(),area:$("#area_id").val(),customer_category:$("#customer_category").val(),reading_date:$("#reading_date").val()},
	  success:function(data)
	  {	    
	    cleanFieldsBeforeReadingSetting();
		if(typeof data.customer === "undefined"){
			clearField("full_name","isMetered_name","customer_address");
			cleanFieldsBeforeReadingSetting();
			changeReadingFormMode("disable_mode");
			$("#msg_div").html("<font color='red'>You have inserted all meter reading data for the selected category.</font>");	
			
			if(direction=="next")
			$("#btn_next").attr("tabindex",-2).focus();
			else
			$("#btn_prev").attr("tabindex",-1).focus();
			
			$("#count_div").html("");
			$("#customer_id").val("");
			return;
		}

		$('#meter_id').find('option:gt(0)').remove();	
		$("#customer_id").val(data.customer.customer_id);			
		$("#meter_id").append( new Option(data.meter_sl_no,data.meter_id) );
		$("#meter_id").val(data.meter_id);
		setCustomerInfo("",data.customer);		
		setReadingInfo(data);
		showReadingHistory("all");
		
		if(typeof data.reading_id === "undefined" || data.reading_id == ""){
			changeReadingFormMode("data_entry_mode");
			disableButton("btn_delete");
			enableButton("btn_save");
			
		}
		else{
			changeReadingFormMode("disable_mode");
			enableButton("btn_delete");
			disableButton("btn_save");
			if(direction=="next")
			$("#btn_next").attr("tabindex",-2).focus();
			else
			$("#btn_prev").attr("tabindex",-1).focus();
			
			
		}
		
		
		$("#count_div").html("Showing <b>"+data.current_reading_index+"</b> of <b>"+data.total_reading_count+"</b> records.");
		$('#count_div').addClass('animated fadeInUp');	
		$("#msg_div").html("");
	  },
	  error:function (xhr, ajaxOptions, thrownError) {
	      alert(xhr.status);
	      alert(thrownError);
      }
	});
}
  


function validateAndSubmitReading()
{
 var isValid=validateMeterReadingInfo();

	if(isValid==true)	 {
		
		changeReadingFormMode("data_submit_mode",null);
		
	var form = document.getElementById('meterReadingForm');
	var formData = new FormData(form);
	//alert(formData);
	  $.ajax({
	    url: 'saveMeterReading.action',
	    type: 'POST',
	    data: formData,
	    async: false,
	    cache: false,
	    contentType: false,
	    processData: false,
	    success: function (response) {
	    if(response.status=="OK")
	    {
	       var customer_data = [{"customer_id":$("#customer_id").val(),"full_name":$("#customer_name").val(),"category_name":$.trim($("#customer_category option:selected").text()),"area_name":$.trim($("#area_id option:selected").text()),"mobile":$("#mobile").val(),"status":$("#connection_status").val() }];
		   $("#customer_table").jqGrid('addRowData', $("#customer_id").val(),customer_data[0] , "first");
		   //clearMeterReadingForm();
		   cleanFieldsBeforeReadingSetting();				     
	    }
	       changeReadingFormMode("disable_mode",null);	
	       $("#msg_div").html(response.message);
		   $('#msg_div').addClass('animated fadeInUp');			
	       fetchReadingEntry('next');
	    }
	    
	  });
	
	}
}
function addNewMeterReading(){
enableButton("btn_save");
changeReadingFormMode("data_entry_mode");
$("#prev_reading").val($("#curr_reading").val());
clearField("curr_reading","actual_consumption","total_consumption","remarks","bill_id","reading_id");
$("#prev_reading_date").val($("#curr_reading_date").val());
$("#curr_reading_date").val($("#reading_date").val());
$("#min_load").val($("#min_load_latest").val());	
$("#max_load").val($("#max_load_latest").val());
$("#pressure").val($("#pressure_latest").val());
$("#pressure_factor").val($("#pressure_factor_latest").val());
$("#temperature").val($("#temperature_latest").val());
$("#temperature_factor").val($("#temperature_factor_latest").val());
$("#tariff_id").val($("#latest_tariff_id").val());
$("#rate").val($("#latest_rate").val());
}
function editMeterReading(){
	if($("#bill_id").val()==""){
		 enableButton("btn_save");
		 changeReadingFormMode("data_entry_mode");
		}
	else
	 	$("#msg_div").html("<font color='red'>Sorry, you can't edit this Meter Reading. Cause: Billing done for this reading.</font>");
}
function resetMeter(){
	enableButton("btn_save");
	changeReadingFormMode("data_entry_mode");
	clearField("curr_reading","actual_consumption","total_consumption","remarks","bill_id","reading_id");
	$("#prev_reading").val("0");
	$("#prev_reading_date").val($("#curr_reading_date").val());
	$("#curr_reading_date").val($("#reading_date").val());
	$("#min_load").val($("#min_load_latest").val());	
	$("#max_load").val($("#max_load_latest").val());
	$("#pressure").val($("#pressure_latest").val());
	$("#pressure_factor").val($("#pressure_factor_latest").val());
	$("#temperature").val($("#temperature_latest").val());
	$("#temperature_factor").val($("#temperature_factor_latest").val());
	}
function handleEnterKeyPress(event){

    if(typeof event === 'undefined'){
    	//called when mouse click for submit reading entry happen
    	
    	validateAndSubmitReading();
    	}
    	
	var keycode = (event.keyCode ? event.keyCode : event.which);
	
	if(keycode == '13'){	
			
		if($(".ui-dialog").is(":visible")){
			setReadingCommonValues();
			return;
		}
		validateAndSubmitReading();
	}
}


unbindKeyPress();
$(document).keypress(handleEnterKeyPress);


$(document).unbind('keydown');$("input").unbind('keydown');

$(document).bind('keydown', 'ctrl+.', function(){fetchNextReading();});
$("input").bind('keydown', 'ctrl+.', function(){fetchNextReading();});
function fetchNextReading(){
    clearHtml("msg_div");    
	fetchReadingEntry('next');
}

$(document).bind('keydown', 'ctrl+,', function(){fetchPreviousReading();});
$("input").bind('keydown', 'ctrl+,', function(){fetchPreviousReading();});
function fetchPreviousReading(){
	clearHtml("msg_div"); 
	fetchReadingEntry('previous');
}

$(document).bind('keydown', 'ctrl+M', function(){fetchFirstReading();});
$("input").bind('keydown', 'ctrl+M', function(){fetchFirstReading();});
function fetchFirstReading(){
    clearHtml("msg_div");    
	fetchReadingEntry('start');
}

$(document).bind('keydown', 'ctrl+/', function(){fetchLastReading();});
$("input").bind('keydown', 'ctrl+/', function(){fetchLastReading();});
function fetchLastReading(){
	clearHtml("msg_div"); 
	fetchReadingEntry('end');
}



$(document).bind('keydown', 'f1', function(){addNewMeterReading();});
$("input").bind('keydown', 'f1', function(){addNewMeterReading();});

$(document).bind('keydown', 'f2', function(){editMeterReading();});
$("input").bind('keydown', 'f2', function(){editMeterReading();});

$(document).bind('keydown', 'f3', function(){resetMeter();});
$("input").bind('keydown', 'f3', function(){resetMeter();});

$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.METERED_CUSTOMER_LIST,
		onSelect:function (){
			getCustomerInfo("",$("#customer_id").val());
			entry_type="customer";
			fetchReadingEntry('start');
		}
}));

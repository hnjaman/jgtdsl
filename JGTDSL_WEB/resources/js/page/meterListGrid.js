var meterListUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_SERVICE+'&method='+jsEnum.METER_LIST;
$("#meter_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
   	url: meterListUrl,
   	jsonReader: {
            repeatitems: false,
            id: "meter_id"
	},

    colNames: ["Meter No","Installed on","Status","Customer Id","Meter Rent","VAT Rebate","Meter Mfg.","Year","Meter Type","conn_size","G-Rating","Measurement Type","Max. Reading","Ini.Reading","Pressure","Temperature","Remarks","Meter Id","Installed by","Installed by name","EVC Serial","EVC Year","EVC Model"],
    colModel: [
            	{
	                name: 'meter_sl_no',
	                index: 'meter_sl_no',
	                sorttype: "string",
	                width:110,
	                align:'center',
	                search: true,
            	},{
	                name: 'installed_date',
	                index: 'installed_date',
	                align:'right',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true
            	},{
	                name: 'status_name',
	                index: 'status_name',
	                align:'right',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true
            	},
            	{name: 'customer_id',index: 'customer_id',hidden: true},
            	{name: 'meter_rent',index: 'meter_rent',hidden: true},
            	{name: 'vat_rebate',index: 'vat_rebate',hidden: true},
            	{name: 'meter_mfg',index: 'meter_mfg',hidden: true},
            	{name: 'meter_year',index: 'meter_year',hidden: true},
            	{name: 'meter_type',index: 'meter_type',hidden: true},
            	{name: 'conn_size',index: 'conn_size',hidden: true},
            	{name: 'g_rating',index: 'g_rating',hidden: true},
            	{name: 'measurement_type_str',index: 'measurement_type_str',hidden: true},
            	{name: 'max_reading',index: 'max_reading',hidden: true},
            	{name: 'ini_reading',index: 'ini_reading',hidden: true},
            	{name: 'pressure',index: 'pressure',hidden: true},
            	{name: 'temperature',index: 'temperature',hidden: true},
            	{name: 'remarks',index: 'remarks',hidden: true},
            	{name: 'meter_id',index: 'meter_id',hidden: true},
            	{name: 'installed_by',index: 'installed_by',hidden: true},
            	{name: 'installed_by_str',index: 'installed_by_str',hidden: true},
            	{name: 'evc_sl_no',index: 'evc_sl_no',hidden: true},
            	{name: 'evc_year',index: 'evc_year',hidden: true},
            	{name: 'evc_model',index: 'evc_model',hidden: true}            	
        ],   	
	datatype: "local",
	height: $("#available_meter_grid_div").height()-70,
	width: $("#available_meter_grid_div").width(),
   	pager: '#meter_grid_pager',
   	sortname: 'meter_id',
    sortorder: "asc",
	caption: "List of Available Meters",
	onSelectRow: function(id){ 
		getMeterInfo(id,null);
   },
   ondblClickRow: function(rowid) {
    	return;
    },
   gridComplete: function(){
			$("#meter_grid tr").eq(1).trigger("click");			
	},
	rowattr: function (rd) {
	    if (rd.status_name == "Disconnected") {
	        return {"class": "disconnectedMeterRow"};
	    }
    },
    beforeSelectRow: function(rowid, e) {
    	
        var rowData = jQuery('#meter_grid').jqGrid ('getRowData', rowid);  
    	if (rowData.status_name == "Disconnected") {
	    	return false;
	    }
    	else
    		return true;
    }
}));
jQuery("#meter_grid").jqGrid('navGrid','#meter_grid_pager',footerButton,{},{},{},{multipleSearch:true});



function getMeterInfo(meter_id,callBack)
{
	$.ajax({
   		  type: 'POST',
   		  url: 'getMeterInfoAsJson.action',
   		  data: { meter_id:meter_id},
   		  success:function(data){
   			  if(callBack!=null)
   				  callBack(data);						
   		  },
   		  error:function(){
   			if($("#btn_save"))
   				$("#btn_save").removeAttr("disabled");
   			alert("Error during Ajax Call inside getMeterInfo Method.");
   		  }
   	});
}




/* Set Json Data to Meter Info Form */
function setMeterInfo(data,prefix){
	var pFix="";
	if(prefix!=null)
		pFix=prefix+"_";
	
	if($("#"+pFix+"meter_id")) $("#"+pFix+"meter_id").val(data.meter_id);
	if($("#"+pFix+"meter_sl_no")) $("#"+pFix+"meter_sl_no").val(data.meter_sl_no);
	if($("#"+pFix+"meter_mfg")) $("#"+pFix+"meter_mfg").val(data.meter_mfg);
	if($("#"+pFix+"meter_year")) $("#"+pFix+"meter_year").val(data.meter_year);
	if($("#"+pFix+"measurement_type_str")) $("#"+pFix+"measurement_type_str").val(data.measurement_type_str);
	
	if($("#"+pFix+"meter_type")) $("#"+pFix+"meter_type").val(data.meter_type);
	if($("#"+pFix+"g_rating")) $("#"+pFix+"g_rating").val(data.g_rating);
	if($("#"+pFix+"conn_size")) $("#"+pFix+"conn_size").val(data.conn_size);
	if($("#"+pFix+"max_reading")) $("#"+pFix+"max_reading").val(data.max_reading);
	if($("#"+pFix+"ini_reading")) $("#"+pFix+"ini_reading").val(data.ini_reading);
	if($("#"+pFix+"pressure")) $("#"+pFix+"pressure").val(data.pressure);
	if($("#"+pFix+"temperature")) $("#"+pFix+"temperature").val(data.temperature);
	if($("#"+pFix+"meter_rent")) $("#"+pFix+"meter_rent").val(data.meter_rent);
	if($("#"+pFix+"vat_rebate")) $("#"+pFix+"vat_rebate").val(data.vat_rebate);
	if($("#"+pFix+"meter_remarks")) $("#"+pFix+"meter_remarks").val(data.remarks);
	if($("#"+pFix+"installed_date")) $("#"+pFix+"installed_date").val(data.installed_date);	
	if($("#"+pFix+"installed_by")) setChosenData(pFix+"installed_by",data.installed_by);
	if($("#"+pFix+"installed_by_str")) setChosenData(pFix+"installed_by_str",data.installed_by_str);
	
	if($("#"+pFix+"measurement_type_str") && $("#"+pFix+"measurement_type_str").val()==1){
		
		if($("#"+pFix+"evc_div")) $("#"+pFix+"evc_div").show();
		if($("#"+pFix+"evc_sl_no")) $("#"+pFix+"evc_sl_no").val(data.evc_sl_no);
		if($("#"+pFix+"evc_model")) $("#"+pFix+"evc_model").val(data.evc_model);
		if($("#"+pFix+"evc_year")) $("#"+pFix+"evc_year").val(data.evc_year);
	}
	else{
		
		if($("#"+pFix+"evc_div")) {$("#"+pFix+"evc_div").hide();
		clearField(pFix+"evc_sl_no",pFix+"evc_model",pFix+"evc_year");}
	}
}

function loadMeters(customer_id,meterLoadPreOperation){
   	var ruleArray=[["customer_id"],["eq"],[customer_id]];
	var postdata=getPostFilter("meter_grid",ruleArray);
	
	if(meterLoadPreOperation!=null)
		meterLoadPreOperation();
	
   	$("#meter_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});
   	    	
   	$("#meter_grid").jqGrid('setCaption', 'List of Meters');
   	reloadGrid("meter_grid");
}

function meterLoadPreOperation(onClickCallBack){
	var newRowAttr= function (rd) {}
	var newB4SelectRow=function(rowid, e) {return true;}
	var newOnSelectRow=function(id){
			var rowData = $("#meter_grid").getRowData(id);
			setMeterInfo(rowData);			
			//enableButton("btn_add");
			//disableButton("btn_edit","btn_delete");
			enableButton("btn_add","btn_edit","btn_delete");
			
			if(onClickCallBack!=null)
				onClickCallBack();
	}
	
	$("#meter_grid").jqGrid( "setGridParam", { rowattr:null,beforeSelectRow:null,onSelectRow: null } );
	$("#meter_grid").jqGrid('setGridParam',{rowattr:newRowAttr,beforeSelectRow:newB4SelectRow,onSelectRow:newOnSelectRow});
}
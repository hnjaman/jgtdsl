var meterListUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_SERVICE+'&method=getApplianceList';
$("#meter_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
   	url: meterListUrl,
   	jsonReader: {
            repeatitems: false,
            id: "applianc_id"
	},

    colNames: ["Appliance ID","Appliance Name","Appliance Qnt","Appliance Qnt BillCal","Perm Diss","Temp. Diss","Partial Diss"],
    colModel: [
            	{
	                name: 'applianc_id',
	                index: 'applianc_id',
	                sorttype: "string",
	                width:110,
	                align:'center',
	                search: true,
            	},{
	                name: 'applianc_name',
	                index: 'applianc_name',
	                align:'right',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true
            	},{
	                name: 'applianc_qnt',
	                index: 'applianc_qnt',
	                align:'center',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true
            	},{
	                name: 'applianc_qnt_billcal',
	                index: 'applianc_qnt_billcal',
	                align:'center',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true,hidden:true
            	},{
	                name: 'applianc_perm_diss',
	                index: 'applianc_perm_diss',
	                align:'center',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true
            	},{
	                name: 'applianc_temp_diss',
	                index: 'applianc_temp_diss',
	                align:'right',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true
            	},{
	                name: 'applianc_partial_diss',
	                index: 'applianc_partial_diss',
	                align:'center',
	                sorttype: 'string',
	                formatter: 'string',
	                search: true
            	}
        ],   	
	datatype: "local",
	height: $("#available_appliance_grid_div").height()-70,
	width: $("#available_appliance_grid_div").width(),
   	pager: '#meter_grid_pager',
   	sortname: 'meter_id',
    sortorder: "asc",
	caption: "List of Available Appliance",				//*********** Title 
	onSelectRow: function(id){ 
		var rowData = $("#meter_grid").getRowData(id);
		$("#appliance_id").val(rowData.applianc_id);
		$("#old_double_burner_qnt").val(rowData.applianc_qnt);
		$("#double_burner_qnt_hidden").val(rowData.applianc_qnt);
		$("#old_double_burner_qnt_billcal").val(rowData.applianc_qnt_billcal);	
		$("#double_burner_qnt_billcal_hidden").val(rowData.applianc_qnt_billcal);		
		$("#old_pdisconnected_burner_qnt").val(rowData.applianc_perm_diss);
		$("#old_tdisconnected_burner_qnt").val(rowData.applianc_temp_diss);
		$("#old_tdisconnected_half_burner_qnt").val(rowData.applianc_partial_diss);
		$("#BurnerQntChangeInfo").show();
		$("#newApllianceEntryDiv").hide();
		$("#raizerDisconnectionDiv").hide();
		$("#customer_id").val($("#comm_customer_id").val())

		
		
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

function loadMeters(customer_id){
   	var ruleArray=[["customer_id"],["eq"],[customer_id]];
	var postdata=getPostFilter("meter_grid",ruleArray);
   	$("#meter_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});   	    	
   	$("#meter_grid").jqGrid('setCaption', 'List of Avialable Appliance');
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
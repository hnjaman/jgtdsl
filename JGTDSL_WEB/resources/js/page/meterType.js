var editParams={
		width: jsVar.CRUD_WINDOW_WIDTH,
        editCaption: jqCaption.EDIT_METER_TYPE,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
        {
			if (postdata.id === '_empty')
				postdata.id = null;
            	return {data: JSON.stringify(postdata), service:jsEnum.METER_SERVICE,  method:jsEnum.METER_TYPE_UPDATE};
        },
        afterSubmit: jqGridDataPostResponseHandler,
		errorTextFormat: formatErrorText
}
var addParams={
		closeAfterAdd:true,
		width: jsVar.CRUD_WINDOW_WIDTH,
        addCaption: jqCaption.ADD_METER_TYPE,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
           {
				if (postdata.id === '_empty')
   					postdata.id = null;
   				return {data: JSON.stringify(postdata), service:jsEnum.METER_SERVICE,  method:jsEnum.METER_TYPE_ADD};
           },
        afterShowForm: function(frm) {
        	   disableOnClick();
        	   $("#type_name").focus();
        },
        beforeShowForm: function( formId ) {
	   		   $('#type_id', formId ).attr( jsEnum.READ_ONLY, false );   
	   		   $.ajax({type: 'POST',url: jsEnum.NEXT_ID_ACTION,
		   		  data: { service:jsEnum.METER_SERVICE,method:jsEnum.NEXT_ID_METHOD,data: '{\"id\":\"\"}'},
		   		  success:function(data){if(data.status=="OK")$('#type_id', formId).val(data.message);else{$("#type_id").focus();alert(jsMsg.ERROR_NEXT_ID);}},
		   		  error:function(){}
	   		   });
   		},
   		afterSubmit: jqGridDataPostResponseHandler, 
   		errorTextFormat: formatErrorText
   	}

var deleteParams={
		beforeShowForm:function(form) {
			ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
			$("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Type Id :<b>"+ret.type_id+'</b><br>Type Name: <b>'+ret.type_name+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
		    rp_ge.url = jsEnum.CRUD_ACTION+"?" +
		        $.param({
		        	service: jsEnum.METER_SERVICE,
		        	method: jsEnum.METER_TYPE_DELETE,
		            data: '{\"id\":\"'+ret.type_id+'\"}'
		        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}

var viewParams ={
		width: 600,
		caption: jqCaption.VIEW_METER_TYPE
}

jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.METER_SERVICE+'&method='+jsEnum.METER_TYPE_LIST,
        jsonReader: {
            repeatitems: false,
            id: "type_id"
        },
        colNames: ['ID', 'Type Name', 'Description', 'Status'],
        colModel: [{
		                name: 'type_id',
		                index: 'type_id',
		                sorttype: 'integer',
		                align:'center',
		                width:30,
		                editable: true,
		                search: true,
		                editoptions: { readonly: jsEnum.READ_ONLY,maxlength: 2 },
		        		addoptions: { readonly: false },
		        		formoptions: {elmsuffix: jsEnum.STAR},
		                editrules: {required: true}
            	  },
            	  {
		                name: 'type_name',
		                index: 'type_name',
		                sorttype: "string",
		                editable: true,
		                search: true,
		                editrules: {required: true},
		                formoptions: {elmsuffix: jsEnum.STAR}
            	  },
            	  {
	                    name: 'description',
	                    index: 'description',
	                    sorttype: "string",
	                    edittype: "textarea",
	                    editable: true,
	                    editoptions: { rows: "5",cols:"23" },
	                    formatter: brToNewLine,
	                    search:false
            	  },            	
            	  {
	                    name: 'status',
	                    index: 'status',
	                    align:'center',
	                    width:60,
	                    sorttype: "string",
	                    editable: true,
	                    edittype: "select",
	                    formatter:"select",
	                    editrules: {required: true},
	                    editoptions: active_inactive,
	                    formoptions: {elmsuffix: jsEnum.STAR},
	                    searchoptions: { 
	                    	value: statusJson, 
	                    	defaultValue: "1" 
	                    },
	                    stype:"select"
            	  }
        ],
        caption: jqCaption.LIST_METER_TYPE,
        sortname: 'type_id'     
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);

gridColumnHeaderAlignment("gridTable",["type_name","max_reading","max_reading"],"left");

jQuery("#gridTable").jqGrid (
        "navButtonAdd","#gridPager",
         {
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
        		window.location="meterTypeReport.action";
            }
}); 
	
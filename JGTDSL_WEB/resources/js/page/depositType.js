var editParams={
		width: jsVar.CRUD_WINDOW_WIDTH,
        editCaption: jqCaption.EDIT_DEPOSIT_TYPE,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
        {
			if (postdata.id === '_empty')
		        postdata.id = null;
	            return {data: JSON.stringify(postdata), service:jsEnum.DEPOSIT_SERVICE,  method:jsEnum.DEPOSIT_TYPE_UPDATE};
        },
        afterSubmit: jqGridDataPostResponseHandler,
		errorTextFormat: formatErrorText
}
var addParams={
		closeAfterAdd:true,
        width: jsVar.CRUD_WINDOW_WIDTH,
        addCaption: jqCaption.ADD_DEPOSIT_TYPE,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata){
	   		if (postdata.id === '_empty')
	   	        postdata.id = null;
	               return {data: JSON.stringify(postdata), service:jsEnum.DEPOSIT_SERVICE,  method:jsEnum.DEPOSIT_TYPE_ADD};
         },
         afterShowForm: function(frm) {
        	disableOnClick();
          	$("#type_name_eng").focus();
         },
         beforeShowForm: function( formId ) {
   		   $('#type_id', formId ).attr( jsEnum.READ_ONLY, false ); 
   		   $.ajax({type: 'POST',url: jsEnum.NEXT_ID_ACTION,
	   		  data: { service:jsEnum.DEPOSIT_SERVICE,method:jsEnum.NEXT_ID_METHOD,data: '{\"id\":\"\"}'},
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
		   $("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Type Id :<b>"+ret.type_id+'</b><br>Type Name: <b>'+ret.type_name_eng+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {		
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));	    
		    rp_ge.url = jsEnum.CRUD_ACTION+"?" +
		        $.param({
		        	service: jsEnum.DEPOSIT_SERVICE,
		        	method: jsEnum.DEPOSIT_TYPE_DELETE,
		            data: '{\"id\":\"'+ret.type_id+'\"}'
		        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}

var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH,
		caption: jqCaption.VIEW_DEPOSIT_TYPE
}

jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DEPOSIT_SERVICE+'&method='+jsEnum.DEPOSIT_TYPE_LIST,
        jsonReader: {
            repeatitems: false,
            id: "type_id"
        },
        colNames: ['ID','Type Name','Description','View Order','Status'],
        colModel: [{
		                name: 'type_id',
		                index: 'type_id',
		                width:30,
		                sorttype: 'integer',
		                align:'center',
		                editable: true,
		                search: true,
		                editoptions: { readonly: jsEnum.READ_ONLY,maxlength: 2 },
		        		addoptions: { readonly: false },
		        		formoptions: {elmsuffix: jsEnum.STAR},
		                editrules: {required: true}
            	  },
            	  {
		                name: 'type_name_eng',
		                index: 'type_name_eng',
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
	                    editoptions: { rows: "5",cols:"24" },
	                    formatter:brToNewLine,
	                    search:false
            	  }, 
            	  {
		                name: 'view_order',
		                index: 'view_order',
		                sorttype: "string",
		                editable: true,
		                align:'center',
		                width:50,
		                editrules: {required: true},
		                formoptions: {elmsuffix: jsEnum.STAR},
		                search:false
            	  },          	
            	  {
	                    name: 'status',
	                    index: 'status',
	                    sorttype: "string",
	                    editable: true,
	                    width:50,
	                    align:'center',
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
        caption: jqCaption.LIST_DEPOSIT_TYPE,
        sortname: 'type_id'
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);

gridColumnHeaderAlignment("gridTable",["type_name_eng","description"],"left");
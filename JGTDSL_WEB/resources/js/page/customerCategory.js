var editParams={
		 width: jsVar.CRUD_WINDOW_WIDTH,
         editCaption: jqCaption.EDIT_CUSTOMER_CATEGORY,
         url: jsEnum.CRUD_ACTION,
         serializeEditData: function (postdata) 
         {
	 		if (postdata.id === '_empty')
	 	        postdata.id = null;
	             return {data: JSON.stringify(postdata), service:jsEnum.CUSTOMER_SERVICE,  method:jsEnum.CUSTOMER_CATEGORY_UPDATE};
         },
         afterSubmit: jqGridDataPostResponseHandler,
         errorTextFormat: formatErrorText
}
var addParams={
		closeAfterAdd:true,
        width: jsVar.CRUD_WINDOW_WIDTH,
        addCaption: jqCaption.ADD_CUSTOMER_CATEGORY,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
        {
    		if (postdata.id === '_empty')
    	        postdata.id = null;
                return {data: JSON.stringify(postdata), service:jsEnum.CUSTOMER_SERVICE,  method:jsEnum.CUSTOMER_CATEGORY_ADD};
        },
        afterShowForm: function(frm) {
    		disableOnClick();
       		$("#category_name").focus();
        },
        beforeShowForm: function( formId ) {
		   $('#category_id', formId ).attr( jsEnum.READ_ONLY, false );    
		   $.ajax({type: 'POST',url: jsEnum.NEXT_ID_ACTION,
	   		  data: { service:jsEnum.CUSTOMER_SERVICE,method:jsEnum.NEXT_ID_METHOD,data: '{\"id\":\"\"}'},
	   		  success:function(data){if(data.status=="OK")$('#category_id', formId).val(data.message);else{$("#category_id").focus();alert(jsMsg.ERROR_NEXT_ID);}},
	   		  error:function(){}
		   });
		},
		afterSubmit: jqGridDataPostResponseHandler, 
   		errorTextFormat: formatErrorText
}

var deleteParams={
		beforeShowForm:function(form) {    
    		ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
    		$("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Category Id :<b>"+ret.category_id+'</b><br>Category Name: <b>'+ret.category_name+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {		
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));	    
		    rp_ge.url = jsEnum.CRUD_ACTION+"?" +
		        $.param({
		        	service: jsEnum.CUSTOMER_SERVICE,
		        	method: jsEnum.CUSTOMER_CATEGORY_DELETE,
		            data: '{\"id\":\"'+ret.category_id+'\"}'
		        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}

var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH,
		caption: jqCaption.VIEW_CUSTOMER_CATEGORY
}
jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.CUSTOMER_CATEGORY_LIST,
        jsonReader: {
            repeatitems: false,
            id: "category_id"
        },
        colNames: ['ID', 'Category Name', 'Type','Description', 'Status'],
        colModel: [{
		                name: 'category_id',
		                index: 'category_id',
		                width:30,
		                align:'center',
		                sorttype: 'integer',
		                editable: true,
		                search: true,
		                editoptions: { readonly: jsEnum.READ_ONLY,maxlength: 2 },
		        		addoptions: { readonly: false },
		        		formoptions: {elmsuffix: jsEnum.STAR},
		                editrules: {required: true}
        		   },
        		   {
		                name: 'category_name',
		                index: 'category_name',
		                sorttype: "string",
		                editable: true,
		                search: true,
		                editrules: {required: true},
		                formoptions: {elmsuffix: jsEnum.STAR}
        		   },
        		   {
	                    name: 'category_type',
	                    index: 'category_type',
	                    sorttype: "string",
	                    width:60,
	                    align:'center',
	                    editable: true,
	                    edittype: "select",
	                    formatter:"select",
	                    editrules: {required: true},
	                    editoptions: { value: "GOVT:GOVT;PVT:PVT"},
	                    formoptions: {elmsuffix: jsEnum.STAR},
	                    searchoptions: { 
	                    	value: "GOVT:GOVT;PVT:PVT", 
	                    	defaultValue: "1" 
	                    },
	                    stype:"select"
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
	                    sorttype: "string",
	                    width:60,
	                    align:'center',
	                    editable: true,
	                    edittype: "select",
	                    editrules: {required: true},
	                    editoptions: active_inactive,
						formatter:"select",
						formoptions: {elmsuffix: jsEnum.STAR},
						searchoptions: { 
	                    	value: statusJson, 
	                    	defaultValue: "1" 
	                    },
	                    stype:"select"
        		   }
        ],
        caption: jqCaption.LIST_CUSTOMER_CATEGORY,
        sortname: 'category_id'   
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);


gridColumnHeaderAlignment("gridTable",["category_name","description"],"left");

jQuery("#gridTable").jqGrid (
        "navButtonAdd","#gridPager",
         {
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
        		window.location="customerCategoryReport.action";
          }
});
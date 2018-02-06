var bankList = $.ajax({
    url: sBox.ALL_BANK,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
bankList=setInitItemForSelectBox(sBoxInit.BANK_LIST,bankList);

var areaList = $.ajax({
    url: sBox.AREA,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
areaList=setInitItemForSelectBox(sBoxInit.AREA_LIST,areaList);

var editParams={
		width: jsVar.CRUD_WINDOW_WIDTH,
        editCaption: jqCaption.EDIT_BRANCH,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
        {
		if (postdata.id === '_empty')
	        postdata.id = null;
            return {data: JSON.stringify(postdata), service:jsEnum.BANK_SERVICE,  method:jsEnum.BRANCH_UPDATE};
        },
        afterSubmit: jqGridDataPostResponseHandler,
		errorTextFormat: formatErrorText
}
var addParams={
	   closeAfterAdd:true,
       width: jsVar.CRUD_WINDOW_WIDTH,
       addCaption: jqCaption.ADD_BRANCH,
       url: jsEnum.CRUD_ACTION,
       serializeEditData: function (postdata) {
   				if (postdata.id === '_empty')
   					postdata.id = null;
   				return {data: JSON.stringify(postdata), service:jsEnum.BANK_SERVICE,  method:jsEnum.BRANCH_ADD};
       },
       afterShowForm: function(frm) {
        	 disableOnClick();
        	 $("#bank_id").focus();
       },
       beforeShowForm: function( formId ) {
   		   	$('#branch_id', formId ).attr( jsEnum.READ_ONLY, false );  
   		   	$.ajax({type: 'POST',url: jsEnum.NEXT_ID_ACTION,
	   		  data: { service:jsEnum.BANK_SERVICE,method:jsEnum.NEXT_ID_BRANCH,data: '{\"id\":\"\"}'},
	   		  success:function(data){if(data.status=="OK")$('#branch_id', formId).val(data.message);else{$("#branch_id").focus();alert(jsMsg.ERROR_NEXT_ID);}},
	   		  error:function(){}
   		   	});
   		},
   		afterSubmit: jqGridDataPostResponseHandler, 
   		errorTextFormat: formatErrorText
}

var deleteParams={
		beforeShowForm:function(form) {
	   		ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
	   		$("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Branch Id :<b>"+ret.branch_id+'</b><br>Branch Name: <b>'+ret.branch_name+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {			
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));		    
		    rp_ge.url = jsEnum.CRUD_ACTION+"?" +
		        $.param({
		        	service: jsEnum.BANK_SERVICE,
		        	method: jsEnum.BRANCH_DELETE,
		            data: '{\"id\":\"'+ret.branch_id+'\"}'
		        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}

var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH,
		caption: jqCaption.VIEW_BRANCH
}

jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BANK_SERVICE+'&method='+jsEnum.BRANCH_LIST,
        jsonReader: {
            repeatitems: false,
            id: "type_id"
        },
        colNames: ['ID','Area Name','Bank Name','Branch Name','Address','Cont. Person','Phone','Mobile','Fax','Email','Description','Status'],
        colModel: [{
		                name: 'branch_id',
		                index: 'branch_id',
		                sorttype: 'integer',
		                align:'center',
		                width:80,
		                editable: true,
		                search: true,
		                editoptions: { readonly: jsEnum.READ_ONLY,maxlength: 4 },
		        		addoptions: { readonly: false },
		        		formoptions: {elmsuffix: jsEnum.STAR},
		                editrules: {required: true}
            	   },
            	   {
		                name: 'area_id',
		                index: 'area_id',
		                sorttype: "string",
		                edittype: "select",
		                formatter:"select",
		                editable: true,
		                search: true,
		                editrules: {required: true},
		                editoptions: {value: areaList},
		                formoptions: {elmsuffix: jsEnum.STAR}
            	   },
            	   {
		                name: 'bank_id',
		                index: 'bank_id',
		                sorttype: "string",
		                edittype: "select",
		                formatter:"select",
		                editable: true,
		                search: true,
		                editrules: {required: true},
		                editoptions: {value: bankList},
		                formoptions: {elmsuffix: jsEnum.STAR}
            	  },
            	  {
	                    name: 'branch_name',
	                    index: 'branch_name',
	                    sorttype: "string",
	                    editable: true,
	                    search: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR}
            	  },            	
            	  {
	                    name: 'address',
	                    index: 'address',
	                    sorttype: "string",
	                    edittype: "textarea",
	                    editable: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR},
	                    formatter: brToNewLine
            	 },
            	 {
	                    name: 'cperson',
	                    index: 'cperson',
	                    sorttype: "string",
	                    editable: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR}
            	},
            	{
	                    name: 'phone',
	                    index: 'phone',
	                    sorttype: "string",
	                    editable: true,
	                    editrules: {required: true},                    
	                    formoptions: {elmsuffix: jsEnum.STAR}
                },
                {
	                    name: 'mobile',
	                    index: 'mobile',
	                    sorttype: "string",                    
	                    editable: true,hidden: true,
	                    editrules: {required: false}                    
            	},
            	{
	                    name: 'fax',
	                    index: 'fax',
	                    sorttype: "string",                    
	                    editable: true,hidden: true,
	                    editrules: {required: false}                    
            	},
            	{
	                    name: 'email',
	                    index: 'email',
	                    sorttype: "string",                    
	                    editable: true,hidden: true,
	                    editrules: {required: false}                    
            	},            	
            	{
	                    name: 'description',
	                    index: 'description',
	                    sorttype: "string",                    
	                    editable: true,hidden: true,
	                    editrules: {required: false}                   
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
        caption: jqCaption.LIST_BRANCH,
        sortname: 'branch_id'  
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);

gridColumnHeaderAlignment("gridTable",["bank_id","branch_name","cperson","phone"],"left");

jQuery("#gridTable").jqGrid (
		"navButtonAdd","#gridPager",
		 {
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
				window.location="bankInformation.action";
		        }
		 }); 
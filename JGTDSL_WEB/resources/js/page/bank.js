var editParams={
		width: jsVar.CRUD_WINDOW_WIDTH,
        editCaption: jqCaption.EDIT_BANK,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
        {
			if (postdata.id === '_empty')
		        postdata.id = null;
	            return {data: JSON.stringify(postdata), service:jsEnum.BANK_SERVICE,  method:jsEnum.BANK_UPDATE};
        }		
}
var addParams={
		closeAfterAdd:true,
		width: jsVar.CRUD_WINDOW_WIDTH+200,
		closeAfterAdd:true,	
		url: jsEnum.CRUD_ACTION,
	    serializeEditData: function (postdata) 
	    {
	   		if (postdata.id === '_empty')
	   	        postdata.id = null;
	               return {data: JSON.stringify(postdata), service:jsEnum.BANK_SERVICE,  method:jsEnum.BANK_ADD};
	    },
	    afterShowForm: function(frm) {
	        disableOnClick();
	        $("#bank_name").focus();
	    },
	    beforeShowForm: function( formId ) {
		   $('#bank_id', formId ).attr( jsEnum.READ_ONLY, false );       
		   $.ajax({type: 'POST',url: jsEnum.NEXT_ID_ACTION,
	   		  data: { service:jsEnum.BANK_SERVICE,method:jsEnum.NEXT_ID_BANK,data: '{\"id\":\"\"}'},
	   		  success:function(data){if(data.status=="OK")$('#bank_id', formId).val(data.message);else{$("#bank_id").focus();alert(jsMsg.ERROR_NEXT_ID);}},
	   		  error:function(){}
		   });
		}
}
var deleteParams={
		beforeShowForm:function(form) {
			ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
			$("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Bank Id :<b>"+ret.bank_id+'</b><br>Bank Name: <b>'+ret.bank_name+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {		
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));	    
		    rp_ge.url = jsEnum.CRUD_ACTION+"?" +
		        $.param({
		        	service: jsEnum.BANK_SERVICE,
		        	method: jsEnum.BANK_DELETE,
		            data: '{\"id\":\"'+ret.bank_id+'\"}'
		        });
		}		
}


var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH,
		caption: jqCaption.VIEW_BANK
}

jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BANK_SERVICE+'&method=getAllBankList',
        jsonReader: {
            repeatitems: false,
            id: "type_id"
        },
        colNames: ['ID', 'Bank Name', 'Phone','Address','Fax','Email','URL','Description','Status'],
        colModel: [{
	                name: 'bank_id',
	                index: 'bank_id',
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
	                name: 'bank_name',
	                index: 'bank_name',
	                sorttype: "string",
	                editable: true,
	                search: true,
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
                    name: 'address',
                    index: 'address',
                    sorttype: "string",
                    edittype: "textarea",
                    editable: true,
                    editoptions: { rows: "5",cols:"23" },
                    editrules: {required: true},
                    formoptions: {elmsuffix: jsEnum.STAR},
                    formatter: brToNewLine
            	},
            	{
                    name: 'fax',
                    index: 'fax',
                    sorttype: "string",                    
                    editable: true,hidden: true,
                    editrules: {required: false},                    
            	},
            	{
                    name: 'email',
                    index: 'email',
                    sorttype: "string",                    
                    editable: true,hidden: true,
                    editrules: {required: false},                    
            	},
            	{
                    name: 'url',
                    index: 'url',
                    sorttype: "string",                    
                    editable: true,hidden: true,
                    editrules: {required: false},                    
            	},
            	{
                    name: 'description',
                    index: 'description',
                    sorttype: "string",                    
                    editable: true,hidden: true,
                    editrules: {required: false},                    
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
        caption: jqCaption.LIST_BANK,
        sortname: 'bank_name'
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);


gridColumnHeaderAlignment("gridTable",["bank_name","phone","address"],"left");

jQuery("#gridTable").jqGrid (
        "navButtonAdd","#gridPager",
         {
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
        		window.location="bankInformationReport.action";
         }
         });
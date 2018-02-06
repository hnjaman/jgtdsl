var zoneList = $.ajax({
    url: sBox.ZONE,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
//zoneList is a simple String, now we need to convert it to Json Object.
zoneList=zoneList.replace(/;/g, ',');
zoneList=zoneList.replace(/:/g, '":"');
zoneList=zoneList.replace(/,/g, '","');
zoneList = '{"' + zoneList + '"}';
zoneList=jQuery.parseJSON(zoneList);

var editParams={
		width: jsVar.CRUD_WINDOW_WIDTH,
		editCaption: jqCaption.EDIT_AREA,
		url: jsEnum.CRUD_ACTION,
		serializeEditData: function (postdata) 
		{
			if (postdata.id === '_empty')
		       postdata.id = null;
		        return {data: JSON.stringify(postdata), service:jsEnum.AREA_SERVICE,  method:jsEnum.AREA_UPDATE};
		},
		afterSubmit: jqGridDataPostResponseHandler,
		errorTextFormat: formatErrorText
}
var addParams={
		closeAfterAdd:true,
		width: jsVar.CRUD_WINDOW_WIDTH,
		addCaption: jqCaption.ADD_AREA,
		url: jsEnum.CRUD_ACTION,	
		serializeEditData: function (postdata) 
		{
    		if (postdata.id === '_empty')
    	        postdata.id = null;
                return {data: JSON.stringify(postdata), service:jsEnum.AREA_SERVICE,  method:jsEnum.AREA_ADD};
        },
        afterShowForm: function(frm) {
    		disableOnClick();
       		$("#area_name").focus();
        },
        beforeShowForm: function( formId ) {
		   $('#area_id', formId ).attr( jsEnum.READ_ONLY, false );        
		   $.ajax({type: 'POST',url: jsEnum.NEXT_ID_ACTION,
		   		  data: { service:jsEnum.AREA_SERVICE,method:jsEnum.NEXT_ID_METHOD,data: '{\"id\":\"\"}'},
		   		  success:function(data){if(data.status=="OK")$('#area_id', formId).val(data.message);else{$("#area_id").focus();alert(jsMsg.ERROR_NEXT_ID);}},
		   		  error:function(){}
		   });
		},
		afterSubmit: jqGridDataPostResponseHandler, 
   		errorTextFormat: formatErrorText
}

var deleteParams={
		beforeShowForm:function(form) {    
	    	ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
	    	$("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Area Id :<b>"+ret.area_id+'</b><br>Area Name: <b>'+ret.area_name+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {		
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));	    
		    rp_ge.url = jsEnum.CRUD_ACTION+"?" +
		        $.param({
		        	service: jsEnum.AREA_SERVICE,
		        	method: jsEnum.AREA_DELETE,
		            data: '{\"id\":\"'+ret.area_id+'\"}'
		        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}


var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH,
		caption: jqCaption.VIEW_AREA
}
jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.AREA_SERVICE+'&method='+jsEnum.AREA_LIST,
        jsonReader: {
            repeatitems: false,
            id: "category_id"
        },
        colNames: ['ID', 'Area Name','Description','Zones', 'Status'],
        colModel: [{
		                name: 'area_id',
		                index: 'area_id',
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
		                name: 'area_name',
		                index: 'area_name',
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
	                    name: 'zones',
	                    index: 'zones',
	                    sorttype: "string",
	                    editable: true,
	                    edittype: "select",
	                    editoptions: {
		                        multiple: true,
		                        size: 4,
		                        value:zoneList
		                        //value: {'N': 'North', 'S': 'South', 'E': 'East', 'W': 'West'} //Static way to declare the value
	                    	},
	                    formatter:"select",
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
        caption: jqCaption.LIST_AREA,
        sortname: 'area_id',
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);

	jQuery("#gridTable").jqGrid("navButtonAdd","#gridPager",
	{
         caption: "", buttonicon: "ui-icon-print", title: "Download Report",
         onClickButton: function() {
			window.location="areaReport.action";
        }
     });
        
gridColumnHeaderAlignment("gridTable",["area_name","description"],"left");
//src="https://code.jquery.com/jquery-1.12.4.js";
//src="https://code.jquery.com/ui/1.12.1/jquery-ui.js";

var customerCategory = $.ajax({
    url: sBox.CUSTOMER_CATEGORY,
    async: false,
    success: function(data, result) { 
	    if (!result) alert(sBox.NDF);
	}
}).responseText;
customerCategory=setInitItemForSelectBox(sBoxInit.CUSTOMER_CATEGORY ,customerCategory);

var editParams={
		 width: jsVar.CRUD_WINDOW_WIDTH,
         editCaption: jqCaption.EDIT_TARIFF,
         url: jsEnum.CRUD_ACTION,
         onInitializeForm: setFormEvents,
         serializeEditData: function (postdata) 
         {
	 		if (postdata.id === '_empty')
	 	        postdata.id = null;
	             return {data: JSON.stringify(postdata), service:jsEnum.TARIFF_SERVICE,  method:jsEnum.TARIFF_UPDATE};
         },
         afterSubmit: jqGridDataPostResponseHandler,
         errorTextFormat: formatErrorText
}
var addParams={
		closeAfterAdd:true,
        width: jsVar.CRUD_WINDOW_WIDTH,
        addCaption: jqCaption.ADD_TARIFF,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
        {
			if (postdata.id === '_empty')
		        postdata.id = null;
				postdata.tariff_id='-1';
	            return {data: JSON.stringify(postdata), service:jsEnum.TARIFF_SERVICE,  method:jsEnum.TARIFF_ADD};
        },
        afterShowForm: function(frm) {
     	   disableOnClick();
     	   $("#tariff_id").focus();
        },
        beforeShowForm: function( formId ) {
		   $('#tariff_id', formId ).attr( jsEnum.READ_ONLY, false );        

		},
		afterSubmit: jqGridDataPostResponseHandler, 
   		errorTextFormat: formatErrorText
}

var deleteParams={
		beforeShowForm:function(form) {    
		    ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
		    $("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Tariff Id :<b>"+ret.tariff_id+'</b><br>Tariff No: <b>'+ret.tariff_no+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {			
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));		    
		    rp_ge.url = jsEnum.CRUD_ACTION+"?" +
		        $.param({
		        	service: jsEnum.TARIFF_SERVICE,
		        	method: jsEnum.TARIFF_DELETE,
		            data: '{\"id\":\"'+ret.tariff_id+'\"}'
		        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}


var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH,
		caption: jqCaption.VIEW_TARIFF
}
jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.TARIFF_SERVICE+'&method='+jsEnum.TARIFF_LIST,
        jsonReader: {
            repeatitems: false,
            id: "tariff_id"
        },
        colNames: ['ID','Tariff No', 'Category','Meter Status','Burner Category','Description','Price','PB','Vat','Sd','Pdf','Bapex','WH','DWH','Transmission','GDfund','Distribution','Avalue','From','To','Default'],
        colModel: [{
		                name: 'tariff_id',
		                index: 'tariff_id',
		                editable: true,
		                hidden:true,
		                edithidden:false
        		  },
        		  {
		                name: 'tariff_no',
		                index: 'tariff_no',
		                width:60,
		                sorttype: "string",
		                editable: true,
		                search: true,
		                editrules: {required: true},
		                formoptions: {elmsuffix: jsEnum.STAR}
        		  }, 
        		  {            	
	                    name: 'customer_category_id',
	                    index: 'customer_category_id',
	                    sorttype: "string",
	                    edittype: "select",
	                    formatter:"select",
	                    editable: true,                    
	                    editrules: {required: true},
	                    editoptions: {value: customerCategory},
	                    search: true,
	                    formoptions: {elmsuffix: jsEnum.STAR}                    
        		  },
        		  {
	        			name: 'str_meter_status',
	                    index: 'str_meter_status',
	                    align: "left",
	                    sorttype: "string",
	                    editable: true,
	    	            editoptions: { value: ":Select Meter Status;1:Metered;0:Non-Metered" },
	    	            edittype: "select",
	    				formatter:"select",					
	    	            formoptions: {elmsuffix: jsEnum.STAR},
	    	            editrules: {required: true}
        		  },
        		  {
	        			name: 'str_burner_category',
	                    index: 'str_burner_category',
	                    hidden:true,
	                    align: "left",
	                    sorttype: "string",
	                    editable: true,
	    	            editoptions: { value: "1:Single Burner;2:Double Burner" },
	    	            edittype: "select",
	    				formatter:"select",					
	    	            formoptions: {elmsuffix: jsEnum.STAR},
	    	            editrules: {required: true}
        		  },                             
        		  {
	                    name: 'description',
	                    hidden:true,
	                    index: 'description',
	                    sorttype: "string",
	                    edittype: "textarea",
	                    editable: true,
	                    editoptions: { rows: "5",cols:"23" },
	                    editrules: {required: false},
	                    formatter: brToNewLine                   
        		  },   
        		  {
	                    name: 'price',
	                    index: 'price',
	                    sorttype: "string",
	                    align:'right',
	                    width:50,
	                    editable: true,
	                    search: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR}
        		  }, 
        		  {
	                    name: 'pb',
	                    index: 'pb',
	                    sorttype: "string",
	                    align:'right',
	                    width:50,
	                    editable: true,
	                    search: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR}
      		     },
      		     {
		                  name: 'vat',
		                  index: 'vat',
		                  sorttype: "string",
		                  align:'right',
		                  width:50,
		                  editable: true,
		                  search: true,
		                  editrules: {required: true},
		                  formoptions: {elmsuffix: jsEnum.STAR}
		         },{
	                  name: 'sd',
	                  index: 'sd',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'pdf',
	                  index: 'pdf',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'bapex',
	                  index: 'bapex',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'wellhead',
	                  index: 'wellhead',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'dwellhead',
	                  index: 'dwellhead',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'trasmission',
	                  index: 'trasmission',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'gdfund',
	                  index: 'gdfund',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'distribution',
	                  index: 'distribution',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'avalue',
	                  index: 'avalue',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'effective_from',
	                  index: 'effective_from',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: true},
	                  editoptions: {  dataInit: function(el) { setTimeout(function() { $(el).datepicker(); }, 200); } } ,
	                  formoptions: {elmsuffix: jsEnum.STAR}
	             },
	             {
	                  name: 'effective_to',
	                  index: 'effective_to',
	                  sorttype: "string",
	                  align:'right',
	                  width:50,
	                  editable: true,
	                  search: true,
	                  editrules: {required: false},
	                  editoptions: {  dataInit: function(el) { setTimeout(function() { $(el).datepicker(); }, 200); } } 
	                 
	             },
        		  {
	                    name: 'is_default',
	                    index: 'is_default',
	                    sorttype: "string",
	                    width:60,
	                    align:'center',
	                    editable: true,
	                    edittype: "select",
	                    editrules: {required: true},
	                    editoptions: { value: "1:Yes;0:No" },
						formatter:"select",					
	                    formoptions: {elmsuffix: jsEnum.STAR}
        		  }
        ],
        caption: jqCaption.LIST_TARIFF,
        sortname: 'tariff_id'       
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);


function setFormEvents(formId){	
	$('#str_meter_status', formId).unbind();
	$("#str_meter_status").on('change',function(e){
	var meter_status=$("#str_meter_status").val();
	if(meter_status==1)
		$('#str_burner_category').attr('disabled', 'true');
	else
		$('#str_burner_category').removeAttr('disabled');	
	});	
}

//for Calendar

//Calendar.setup($.extend(true, {}, calOptions, {
	//inputField : "effective_from",
	//trigger : "effective_from"
//}));
//

$("#effective_from").datepicker();

gridColumnHeaderAlignment("gridTable",["tariff_no","customer_category_id","str_meter_status","str_burner_category","description"],"left");
gridColumnHeaderAlignment("gridTable",["price"],"right");

jQuery("#gridTable").jqGrid (
        "navButtonAdd","#gridPager",
         {
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
        		window.location="tariffRateReport.action";
         }
});

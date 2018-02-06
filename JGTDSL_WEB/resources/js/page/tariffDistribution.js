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
        colNames: ['ID','Tariff No', 'Category','Meter Status','Burner Category','Description','Price','PB','VAT','SD','PDF','Bapex','Wellhead','Dwellhead','Trasmission','Gdfund','Distribution','Effective From','Effective To','Action'],
        colModel: [{
		                name: 'tariff_id',
		                index: 'tariff_id',
		                editable: true,
		                hidden:true,
		                edithidden:true
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
	                    width:80,
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
	                    width:50,
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
	                    width:50,
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
	                    index: 'description',
	                    width:50,
	                    sorttype: "string",
	                    edittype: "textarea",
	                    editable: false,
	                    editoptions: { rows: "5",cols:"23" },
	                    formatter: brToNewLine                   
        		  },   
        		  {
        			  	name: 'price',
		                 index: 'price',
		                 sorttype: "float",
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
	                    sorttype: "float",
	                    align:'right',
	                    width:50,
	                    editable: true,
	                    search: true,
	                    editrules: {required: true,custom:true, custom_func:mypricecheck},
	                    formoptions: {elmsuffix: jsEnum.STAR}
        		  },
        		  {
	                    name: 'vat',
	                    index: 'vat',
	                    sorttype: "float",
	                    align:'right',
	                    width:50,
	                    editable: true,
	                    search: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR}
        		  },
        		  {
	                    name: 'sd',
	                    index: 'sd',
	                    sorttype: "float",
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
	                    sorttype: "float",
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
	                    sorttype: "float",
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
	                    sorttype: "float",
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
	                    sorttype: "float",
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
	                    sorttype: "float",
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
	                    sorttype: "float",
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
		                 sorttype: "float",
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
		                 sorttype: "float",
		                 align:'right',
		                 width:50,
		                 editable: true,
		                 search: true,
		                 editrules: {required: true},
		                 formoptions: {elmsuffix: jsEnum.STAR}   ,
		                 
      			  },   
      			  {
	                    name: 'effective_to',
	                    index: 'effective_to',
	                    sorttype: "string",
	                    align:'right',
	                    width:50,
	                    editable: true,
	                    search: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR}
     		  }, 
        		  
        		  {
     			 
                 name: "actions",
                 width: 100,
                 editable: false,
                 formatter: "actions",
                 formatoptions: {
                     keys: true,
                     editOptions: {
                    	 
                        
                     },
                     addOptions: {
                    	 url:"editTariffMargin.action"
                     },
                     delOptions: {}
                 }  
	                    
        		  }
        ],
        editurl:jsEnum.CRUD_ACTION,
        

        serializeRowData: function(postdata){
            return { data: JSON.stringify(postdata), service:jsEnum.TARIFF_SERVICE,  method:jsEnum.TARIFF_UPDATE };
         },
        height: $("#gridWrapper").height()+520,
    	width: $("#gridWrapper").width(),
        caption: jqCaption.LIST_TARIFF,
        beforeSubmit:validate_add,
        sortname: 'tariff_id'       
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);



function mypricecheck(value, colname) {
	
	alert(value);
	alert(colname);
	 var rowID = $("#gridTable").jqGrid('getGridParam', 'selrow');
	var vat = $("#gridTable #" + rowID + "_vat").val();
	var sd = $("#gridTable #" + rowID + "_sd").val();
	var vat_amount=parseFloat(vat);
	var sd_amount=parseFloat(sd);
	var sum=vat_amount+sd_amount;
	
	alert(vat);
	alert(sd);
	alert(sum);
	
	if (value!=sum) 
	   return [false,"Please enter value between 0 and 20"];
	else 
	   return [true,""];
	}

function validate_add(posdata, obj)
{
	alert("validate_add");
   if(posdata.PASSWORD==null || posdata.PASSWORD=="" || posdata.PASSWORD==undefined)
    return [false, "Please enter the pasword"];


return [true, ""];
}
/*serializeEditData: function (postdata) 
{
	 
	if (postdata.id === '_empty')
        postdata.id = null;
        return {data: JSON.stringify(postdata), service:jsEnum.TARIFF_SERVICE,  method:jsEnum.TARIFF_UPDATE};
}*/

/*jQuery("#gridWrapper").jqGrid({
    url: 'processBill.action',
    datatype: 'json',
    ajaxGridOptions: { contentType: 'application/json; charset=utf-8' },
    mtype: 'POST',
    success: function (response) {
    	setRentChangeInfo(response);		    			    	
    	disableButton("btn_add","btn_save");
    	enableButton("btn_edit","btn_delete");
    
    }
});*/



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

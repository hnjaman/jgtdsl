$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
		url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.SUPPLYOFF_SERVICE+'&method='+jsEnum.SUPPLYOFF_LIST+'&extraFilter=area',
	   	jsonReader: {
	            repeatitems: false,
	            id: "customer_id"
		},	
		colNames: ['Customer Id', 'Customer Name','Billing Month', 'From','To','Remarks'],
		   colModel: [{
			                name: 'customer_id',
			                index: 'customer_id',
			                width:70,
			                align:'center',
			                sorttype: 'string',
			                search: true
		            	},
		            	{
			                name: 'full_name',
			                index: 'full_name',
			                sorttype: "string",
			                search: true,
		            	},
		            	{
			                name: 'month_year',
			                index: 'month_year',
			                sorttype: "string",
			                search: true,
		            	},
		            	{
			                name: 'from_date',
			                index: 'from_date',
			                sorttype: "string",
			                search: true,
		            	},
		            	{
			                name: 'to_date',
			                index: 'to_date',
			                sorttype: "string",
			                search: true,
		            	},
		                {
		                    name: 'remarks',
		                    index: 'remarks',
		                    sorttype: "string",
		                    width:60,
		                    align:'center'
		            	}
		        ],
			datatype: 'json',
			height: $("#customer_grid_div").height()-80,
			width: $("#customer_grid_div").width(),
		   	pager: '#customer_grid_pager',
		   	sortname: 'customer_id',
		    sortorder: "asc",
			caption: "List of Available Customers",
			onSelectRow: function(id){ 
				
		   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true,refresh:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


function validateAndSaveSuppyOff()
{
 var isValid=true;
 var off_for=getRadioCheckedValue("off_for"); 
 
 if(off_for=="area_wise")
 	isValid=validateField("area_id");
 else if(off_for=="category_wise")
 	isValid=validateField("area_id","customer_category");
 else if(off_for=="individual_customer")
 	isValid=validateField("customer_id");

 if(isValid==true)
 	isValid=validateField("billing_month","billing_year","from_date","to_date");
 
 enableField("area_id","customer_category");
 readOnlyField("area_id","customer_category");
 
 if(isValid==true)	 {

	var form = document.getElementById('supplyOffForm');
	var formData = new FormData(form);
	  $.ajax({
		  url: 'saveSupplyOff.action',
	    type: 'POST',
	    data: formData,
	    async: false,
	    cache: false,
	    contentType: false,
	    processData: false,
	    success: function (response) {
		  
		  disableField("area_id","category_id");
		  removeReadOnlyField("area_id","customer_category");
		  
	    if(response.status=="OK")
	    {
	      				     
	    }
	     	
	       $.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);
	    
	    }
	    
	  });
	
	}
}


$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.NON_METERED_CUSTOMER_LIST,
    	onSelect:function (){getCustomerInfo("",$('#customer_id').val());},
}));

$("#billing_month").val(getCurrentMonth());
$("#billing_year").val(getCurrentYear());
$("#from_date").val(getCurrentDate());
$("#to_date").val(getCurrentDate());

Calendar.setup({
    inputField : "from_date",
    trigger    : "from_date",
	eventName : "focus",
    onSelect   : function() { this.hide()},
	showTime : false,
	dateFormat : "%d-%m-%Y"
  });
Calendar.setup({
    inputField : "to_date",
    trigger    : "to_date",
	eventName : "focus",
    onSelect   : function() { this.hide()},
	showTime : false,
	dateFormat : "%d-%m-%Y"
  });      



function checkType(type){
	if(type=="area_wise")
	{
	 disableChosenField("customer_id");
	 disableField("customer_category");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 enableField("area_id");
	}
	else if(type=="category_wise"){
	 disableChosenField("customer_id");
	 enableField("customer_category","area_id");
	 autoSelect("customer_category","area_id");
	}
	else if(type=="individual"){
	 enableChosenField("customer_id");
	 disableField("customer_category","area_id");
	 resetSelectBoxSelectedValue("customer_category","area_id");
	}
}		
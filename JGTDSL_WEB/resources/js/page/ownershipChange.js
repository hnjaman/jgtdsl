var fields=["old_full_name","old_father_name","old_mother_name","old_gender","freedom_fighter","old_mobile","old_phone","old_fax","old_national_id","old_passport_no","old_email","old_tin" ,"old_organization_name"];
var old_customer_fields=fields.slice();

var newFields=["full_name","father_name","mother_name","gender","mobile","phone","fax","national_id","passport_no","email","tin","organization_name"];
var new_customer_fields=newFields.slice();
$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){
	
				clearField.apply(this,old_customer_fields);
				getCustomerInfo("old",$('#customer_id').val());
		}
}));

function validateAndSaveChangeOwnershipInfo(){
	
	var validate=false;
	
	validate=validateOwnershipInfo();
	if(validate==true ){
		saveOwnershipnfo();
		
	}	
}

function validateOwnershipInfo(){
	
	var isValid=validateField("full_name","gender");	
	return isValid;
}
function saveOwnershipnfo(){
	
	
	var formData = new FormData($('form')[0]);
	
	
	
	  $.ajax({
		    url: 'updateOwnershipInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		  		if(response.status=="OK"){
		  			clearField.apply(this,old_customer_fields);
		  			clearField("customer_id");
		  			clearField.apply(this,new_customer_fields);
		  			$("#old_img_photo").attr("src"," ");
		  			$("#customer_photo").empty();
		  			
		  		}
		   
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}


//<<Customer Grid>>: List of Metered Customer
$("#customer_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.CUSTOMER_LIST+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "customer_id"
	},
	colNames: ['Customer Id', 'Customer Name','Father Name','Category', 'Area','Mobile','Status','Created On'],
    colModel: customerGridColModel,
	datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-5,
   	pager: '#customer_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Customers",
	onSelectRow: function(id){ 
		var ruleArray=[["history.customer_id"],["eq"],[id]];
		var postdata=getPostFilter("ownership_change_history_grid",ruleArray);
	   	$("#ownership_change_history_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    		
		reloadGrid("ownership_change_history_grid");
   }
}));
jQuery("#customer_grid").jqGrid('navGrid','#customer_grid_pager',$.extend({},footerButton,{search:true}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","customer_grid",["full_name","category_name","area_name","mobile"]);


//<<Ownership Change History>>
$("#ownership_change_history_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.OWNERSHIP_CHANGE_HISTORY_LIST,
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
   colNames: ['Customer Id', 'Customer Name','Father Name','Category', 'Mobile','Date'],
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
		       width:160,
		       sorttype: "string",
		       search: true,
			},
			{
		       name: 'father_name',
		       index: 'father_name',
		       width:160,
		       sorttype: "string",
		       search: true,
			},
			{
		       name: 'category_id',
		       index: 'category_id',
		       width:100,
		       sorttype: "string",
		       search: true,
		       stype:"select",
		       edittype: "select",
		       formatter: "select",
		       editoptions: { value: cCategory },
		       searchoptions: { 
		       	value: cCategory, 
		       	defaultValue: "01" 
		       }
			},
			{
		       name: 'mobile',
		       index: 'mobile',
		       width:80,
		       sorttype: "string",
		       search: true,
			},
			{
		       name: 'created_on',
		       index: 'created_on',
		       sorttype: "string"
			}
        ],
	//datatype: 'json',
	height: $("#customer_grid_div").height()-70,
	width: $("#customer_grid_div").width()-10,
   	pager: '#ownership_change_history_grid_pager',
   	sortname: 'INSERTED_ON',
    sortorder: "asc",
	caption: "Ownership Change History"
}));

var bankList = $.ajax({
    url: sBox.BANK,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
bankList=setInitItemForSelectBox(sBoxInit.BANK_LIST,bankList);

var branchList = $.ajax({
    url: sBox.BRANCH,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
branchList=setInitItemForSelectBox(sBoxInit.BRANCH_LIST,branchList);


var accountTypeList = $.ajax({
    url: sBox.BANK_ACCOUNT_TYPE,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
accountTypeList=setInitItemForSelectBox(sBoxInit.BANK_ACCOUNT_TYPE_LIST,accountTypeList);


var editParams={
		width: jsVar.CRUD_WINDOW_WIDTH,
        editCaption: jqCaption.EDIT_ACCOUNT,
        url: jsEnum.CRUD_ACTION,       
        onInitializeForm: setFormEvents,
        serializeEditData: function (postdata) 
        {
			if (postdata.id === '_empty')
		        postdata.id = null;
	            return {data: JSON.stringify(postdata), service:jsEnum.ACCOUNT_SERVICE,  method:jsEnum.ACCOUNT_UPDATE};
        },
        afterSubmit: jqGridDataPostResponseHandler,
		errorTextFormat: formatErrorText
}
var addParams={
		closeAfterAdd:true,
        width: jsVar.CRUD_WINDOW_WIDTH,
        addCaption: jqCaption.ADD_ACCOUNT,
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
        {
   			if (postdata.id === '_empty')
   				postdata.id = null;
               	return {data: JSON.stringify(postdata), service:jsEnum.ACCOUNT_SERVICE,  method:jsEnum.ACCOUNT_ADD};
        },
        beforeShowForm: function( formId ) {
   		   $('#account_no', formId ).attr( jsEnum.READ_ONLY, false );        

        },
        onInitializeForm: setFormEvents,
        afterSubmit: jqGridDataPostResponseHandler, 
        errorTextFormat: formatErrorText
}

var deleteParams={
		beforeShowForm:function(form) {   
			ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
			$("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>Account No. :<b>"+ret.account_no+'</b><br>Account Name: <b>'+ret.account_name+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {		
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));	    
			rp_ge.url = jsEnum.CRUD_ACTION+"?" +
	        $.param({
	        	service: jsEnum.ACCOUNT_SERVICE,
	        	method: jsEnum.ACCOUNT_DELETE,
	            data: '{\"id\":\"'+ret.account_no+'\"}'
	        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}


var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH,
		caption: jqCaption.VIEW_ACCOUNT
}

jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.ACCOUNT_SERVICE+'&method='+jsEnum.ACCOUNT_LIST,
        jsonReader: {
            repeatitems: false,
            id: "type_id"
        },
        colNames: ['AC No.','Account Name', 'Bank Name','Branch Name', 'Account Type'],
        colModel: [{
		                name: 'account_no',
		                index: 'account_no',
		                sorttype: 'integer',
		                editable: true,
		                width:100,
		                search: true,
		                editoptions: { readonly: jsEnum.READ_ONLY,maxlength: 10 },
		        		addoptions: { readonly: false },
		        		formoptions: {elmsuffix: jsEnum.STAR},
		                editrules: {required: true}
            		},
            		{
	                    name: 'account_name',
	                    index: 'account_name',
	                    sorttype: "string",
	                    edittype: "textarea",
	                    editable: true,
	                    editoptions: { rows: "5",cols:"23" },
	                    editrules: {required: true}
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
	                    name: 'branch_id',
	                    index: 'branch_id',
	                    sorttype: "string",
	                    edittype: "select",
	                    formatter:"select",
	                    editable: true,
	                    search: true,
	                    editrules: {required: true},
	                    editoptions: {value: branchList},
	                    formoptions: {elmsuffix: jsEnum.STAR}
                	},
                	{
	                    name: 'account_type',
	                    index: 'account_type',
	                    sorttype: "string",
	                    edittype: "select",
	                    formatter:"select",
	                    width:100,
	                    editable: true,
	                    editrules: {required: true},
	                    formoptions: {elmsuffix: jsEnum.STAR},
	                    editoptions: {value: accountTypeList}                    
                	}              
        ],
        caption: jqCaption.LIST_ACCOUNT,
        sortname: 'account_no'        
    }).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);


function setFormEvents(formId){
	var bankId=$("#bank_id").val();	
	var branch_id=$("#branch_id").val();
	resetBranchList(bankId,branch_id);
	$('#bank_id', formId).unbind();
	
	$("#bank_id").on('change',function(e){
		bankId=$("#bank_id").val();
		branchId="";
		resetBranchList(bankId,branchId);		
	});
	
function resetBranchList(bankId,branchId){
	$("#branch_id").empty();			
	$("#branch_id").append('<option value="">Select Branch</option>' );
	var objBranchList = $.ajax({
		    url: sBox.BRANCH+"?bank_id="+bankId,
		    async: false,
		    success: function(data, result) {
		        if (!result)
		            alert('Failure to retrieve Branch List.');
		    }
		}).responseText;

	if(objBranchList!=""){
		var branchList=objBranchList.split(";");
	
		for(var i=0;i<branchList.length; i=i+1 )
		{
			var branch=branchList[i].split(":");
			if(branch[0]==branchId)
				$("#branch_id").append( '<option value="' + branch[0] + '" selected="selected">' + branch[1] + '</option>' );
			else			
				$("#branch_id").append( '<option value="' + branch[0] + '">' + branch[1] + '</option>' );
		}
	} //End if
}
}	

gridColumnHeaderAlignment("gridTable",["account_name","bank_id","branch_id","account_type"],"left");
//A custom validate method. Only for Example
//Below is the example how can we use custom validate method through column Model.
/* editrules: {
 		required: true,
		custom: true,
		custom_func: validateUserId
	}
*/	
function customValidateMethod(value, colname) {
		if(value>20)
			return [false, data.message];
		else
			return [true, ""];
}

// role List

var userRoles = $.ajax({
    url: sBox.USER_ROLE,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
userRoles=setInitItemForSelectBox(sBoxInit.ROLE_LIST,userRoles);


// area List

var areaList = $.ajax({
    url: sBox.AREA,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
areaList=setInitItemForSelectBox(sBoxInit.AREA_LIST,areaList);


// designation List

var designationList = $.ajax({
    url: sBox.DESIGNATION,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
designationList=setInitItemForSelectBox(sBoxInit.DESIGNATION_LIST,designationList);


//Division List

var orgDivisionList = $.ajax({
    url: sBox.ORG_DIVISION,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
orgDivisionList=setInitItemForSelectBox(sBoxInit.DIVISION_LIST,orgDivisionList);

// department List

var departmentList = $.ajax({
    url: sBox.ORG_DEPARTMENT,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
departmentList=setInitItemForSelectBox(sBoxInit.DEPARTMENT_LIST,departmentList);

// section List

var sectionList = $.ajax({
    url: sBox.ORG_SECTION,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
sectionList=setInitItemForSelectBox(sBoxInit.SECTION_LIST,sectionList);

// division List

var divisionList = $.ajax({
    url: sBox.DIVISION,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
divisionList=setInitItemForSelectBox(sBoxInit.DIVISION_LIST,divisionList);

//district List

var districtList=$.ajax({
    url: sBox.DISTRICT,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
districtList=setInitItemForSelectBox(sBoxInit.DISTRICT_LIST,districtList);

//upazila List

var upazilaList=$.ajax({
    url: sBox.UPAZILA,
    async: false,
    success: function(data, result) { 
        if (!result) alert(sBox.NDF);
    }
}).responseText;
upazilaList=setInitItemForSelectBox(sBoxInit.UPAZILA_LIST,upazilaList);


// user info edit option

var editParams={
		width: jsVar.CRUD_WINDOW_WIDTH+200,
        editCaption: jqCaption.EDIT_USER,
        url: jsEnum.CRUD_ACTION,
        afterShowForm: function(frm) {
			disableOnClick();
    		$('#password', frm ).val(jsVar.USER_PASSWORD_MASK);
        },
        beforeShowForm: function( formId ) { 
 		   //var dlgDiv = $("#editmodgridTable");
 		   //dlgDiv[0].style.top  = "0px";
        	$('#userId', formId).unbind();
        	setRowSpan(formId,4);
 		},
        beforeInitData: function() {
            var cm = jQuery("#gridTable").jqGrid('getColProp','userImg');
            selRowId = jQuery("#gridTable").jqGrid('getGridParam','selrow');
            
        	var img = new Image();
     	    img.src = jsVar.USER_PHOTO_URL + selRowId + '.jpg';
	     	if(img.height != 0)	     	
	     	    cm.editoptions.src = jsVar.USER_PHOTO_URL + selRowId + '.jpg?t='+jsVar.CURR_TIME;
	     	else
	     	    cm.editoptions.src = jsVar.NO_PHOTO_URL;
        },
        serializeEditData: function (postdata) 
        {
        	if(postdata.id === '_empty')
        		postdata.id = null;
            	return {data: JSON.stringify(postdata), service:jsEnum.USER_SERVICE,  method:jsEnum.USER_UPDATE};
        },
        afterclickPgButtons: function(buttonName, formId, pos) {
        	setFormEventsForEditNavigator(formId);
        	//This event fires when user press the "Next" or "Previous" Icon at the footer of the edit Popup window.
            var cm = jQuery("#gridTable").jqGrid('getColProp', 'userImg');
            selRowId = jQuery("#gridTable").jqGrid('getGridParam', 'selrow');
            var img = new Image();
     	    img.src = jsVar.USER_PHOTO_URL + selRowId + '.jpg';
	     	if(img.height != 0)	     	
	     		$("#userImg").attr("src",jsVar.USER_PHOTO_URL + selRowId + ".jpg?t="+jsVar.CURR_TIME);
	     	else
	     		$("#userImg").attr("src",jsVar.NO_PHOTO_URL);	     	
            
            $('#password', formId ).val(jsVar.USER_PASSWORD_MASK);
        },
        onInitializeForm: function(formId) {        	
        	setFormEvents(formId);        	
            $(formId).attr('method', 'POST');
            $(formId).attr('action', '');
            $(formId).attr('enctype', 'multipart/form-data');
            //$(".navButton").hide();
        },
        afterSubmit: jqGridDataPostResponseHandler,
		errorTextFormat: formatErrorText
}


// new user add option

var addParams={
		closeAfterAdd:true,
		width: jsVar.CRUD_WINDOW_WIDTH+200,        
        afterShowForm: function(frm) {
			disableOnClick();
			jQuery("#gridTable").jqGrid('resetSelection');
    		$("#userImg").attr("src",jsVar.NO_PHOTO_URL);    		
    		$('#password', frm ).val(jsVar.USER_DEFAULT_PASSWORD);
    		$("#userId").focus();
        },
        onInitializeForm: setFormEvents,
        addCaption: jqCaption.ADD_USER,		// var jqCaption
        url: jsEnum.CRUD_ACTION,
        serializeEditData: function (postdata) 
            {
    		if (postdata.id === '_empty')
    	        postdata.id = null;
                return {data: JSON.stringify(postdata), service:jsEnum.USER_SERVICE,  method:jsEnum.USER_ADD};   // constant.js USER_SERVICE : "org.jgtdsl.models.UserService",  USER_ADD : "addUser",
        },
        beforeShowForm: function( formId ) {
		   $('#userId', formId ).attr( jsEnum.READ_ONLY, false ); 
		   //var dlgDiv = $("#editmodgridTable");
		   //dlgDiv[0].style.top  = "0px";    
		   setRowSpan(formId,4);
		},
		afterSubmit: jqGridDataPostResponseHandler, 
   		errorTextFormat: formatErrorText
}

//delete user option

var deleteParams={
		beforeShowForm:function(form) {
			ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));
			$("td.delmsg",form).html("Are you sure you want to delete the selected record ?<br>User Id :<b>"+ret.userId+'</b><br>User Name: <b>'+ret.userName+"</b>");            
		},
		onclickSubmit: function (rp_ge, postdata) {		
			var ret = $("#gridTable").getRowData($("#gridTable").jqGrid('getGridParam','selrow'));	    
			rp_ge.url = jsEnum.CRUD_ACTION+"?" +	
	        $.param({
	        	service: jsEnum.USER_SERVICE,
	        	method: jsEnum.USER_DELETE,
	            data: '{\"id\":\"'+ret.userId+'\"}'
	        });
		},
		afterSubmit: jqGridDataDeleteResponseHandler
}


// view user option

var viewParams ={
		width: jsVar.CRUD_WINDOW_WIDTH+200,
		caption: jqCaption.VIEW_USER,
		beforeShowForm: function(formId) {
			$('#v_password', formId ).html("<span>&nbsp;"+jsVar.USER_PASSWORD_MASK+"(<a style='color:blue;' href='javascript:void(0)' onclick=\"resetPassword('"+jQuery("#gridTable").jqGrid('getGridParam','selrow')+"')\">Reset Password<a>)</span>&nbsp;&nbsp;<span id='wait_div'></span>");
        },
        afterclickPgButtons: function(buttonName, formId, pos) {
        	//This event fires when user press the "Next" or "Previous" Icon at the footer of the edit Popup window.
            $('#v_password').html("<span>&nbsp;"+jsVar.USER_PASSWORD_MASK+"(<a style='color:blue;' href='javascript:void(0)' onclick=\"resetPassword('"+jQuery("#gridTable").jqGrid('getGridParam','selrow')+"')\">Reset Password<a>)</span>"+"</span>&nbsp;&nbsp;<span id='wait_div'></span>");
        }
}

jQuery("#gridTable")
.jqGrid({
    url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.USER_SERVICE+'&method='+jsEnum.USER_LIST,
    jsonReader: {
        repeatitems: false,
        id: "userId"
    },
    
  
// parameter field
    
    colNames: ['Photo','', 'User Id','Password', 'User Name','Role','Designation','Email','Mobile','Status'],
    
    colModel: [{
        name: 'userImg',
        width: 60,
        align: 'center',
        search: false,
        editable: true,
        sortable: false,
        hidden: false,
        edittype: 'image',
        editoptions: {src: ''},
        editable: true,
        editrules: {edithidden: true},
        formatter: function(cellValue, options,
            rowObject) {
        	var img = new Image();
     	    img.src = jsVar.USER_PHOTO_URL + cellValue + '.jpg';
	     	if(img.height != 0)
	     		return '<img src="'+jsVar.USER_PHOTO_URL + cellValue + '.jpg?t='+jsVar.CURR_TIME+'" width="20" height="25"/>';
	     	else
	     		return '<img src="'+jsVar.DEFAULT_PHOTO_URL+'" width="20" height="25"/>';
        },
        formoptions: {rowpos: 1, colpos: 2},
	    },
	    {
	        name: "upload",
	        index: "upload",
	        edittype: "file",
	        width:30,
	        editable: true,
	        hidden: true,
	        editrules: {
	            edithidden: true
	        },
	        editoptions: {
	            dataUrl: "selectdata.php?q=builder",
	            dataEvents: [{
	                type: 'change',
	                fn: function(e) {
	                    afu();
	                }
	            }]
	        },
	        formoptions: {rowpos: 5, colpos: 2},
	    },
	   {
	    name: 'userId',
        index: 'userId',
        width:150,
        align:'left',
        sorttype: 'string',
        editable: true,
        search: true,
        editoptions: { readonly: jsEnum.READ_ONLY },
		addoptions: { readonly: false },
		formoptions: {elmsuffix: jsEnum.STAR,rowpos: 1, colpos: 1},
        editrules: {required: true},
	   }, 
	   {
		    name: 'password',
	        index: 'password',
	        width:150,
	        align:'left',
	        sorttype: 'string',	        	        
			addoptions: { readonly: false },
			formoptions: {elmsuffix: jsEnum.STAR,rowpos: 2, colpos: 1},
	        editable: true,hidden: true,
            editrules: {required: true},
            editoptions: { readonly: jsEnum.READ_ONLY },
	   },	   
        {
	        name: 'userName',
	        index: 'userName',
	        sorttype: "string",
	        width:250,
	        editable: true,
	        search: true,
	        editrules: {required: true},
	        formoptions: {elmsuffix: jsEnum.STAR,rowpos: 3, colpos: 1}
	    }, 
	    {
	        name: 'role_name',
	        index: 'role_name',
	        sorttype: "string",
	        editable: true,
	        search: false,
	        edittype: "select",
	        editrules: {required: true},
	        formoptions: {elmsuffix: jsEnum.STAR,rowpos: 4, colpos: 1}
	    },
	    
//	    {  
//	    	name: 'role_name', 
//	    	index: 'role_name', 
//	    	editable: true, 
//	    	edittype: 'custom', 
//	    	editoptions: 
//	        { 
//	            custom_element: createRadioButton, 
//	            custom_value: extractFromRadioButton  
//	        } 
//        },
//	    {
//	        name: 'area_name',
//	        index: 'area_name',
//	        sorttype: "string",
//	        edittype: "select",
//	        editable: true,hidden: true,
//            editrules: {required: true},
//	        formoptions: {elmsuffix: jsEnum.STAR,rowpos: 5, colpos: 1}
//	    },
//	    {
//	        name: 'org_division_name',
//	        index: 'org_division_name',
//	        sorttype: "string",
//	        edittype: "select",
//	        editable: true,hidden: true,
//	        formoptions: {rowpos: 6, colpos: 1}
//	    },
//	    {
//	        name: 'department_name',
//	        index: 'department_name',
//	        sorttype: "string",
//	        edittype: "select",
//	        editable: true,hidden: true,
//	        formoptions: {rowpos: 6, colpos: 2}
//	    },
//	    {
//	        name: 'section_name',
//	        index: 'section_name',
//	        sorttype: "string",
//	        edittype: "select",
//	        editable: true,hidden: true,
//	        formoptions: {rowpos: 7, colpos: 1}
//	    },
	    {
	        name: 'designation_name',
	        index: 'designation_name',
	        align:'left',
	        sorttype: "string",
	        editable: true,hidden: true,
	        edittype: "select",
	        formoptions: {rowpos: 7, colpos: 2}
	    },
//	     {
//	        name: 'division_name',
//	        index: 'division_name',
//	        sorttype: "string",
//	        edittype: "select",
//	        editable: true,hidden: true,
//	        formoptions: {rowpos: 8, colpos: 1}
//	     },
//	     {
//	        name: 'district_name',
//	        index: 'district_name',
//	        sorttype: "string",
//	        edittype: "select",
//	        editable: true,hidden: true,
//	        formoptions: {rowpos: 8, colpos: 2}
//	     },
//	     {
//	        name: 'upazila_name',
//	        index: 'upazila_name',
//	        sorttype: "string",
//	        edittype: "select",
//	        editable: true,hidden: true,
//	        formoptions: {rowpos: 9, colpos: 1}
//	     },
	    {
	        name: 'email_address',
	        index: 'email_address',
	        align:'left',
	        sorttype: "string",
	        editable: true,hidden: true,
	        formoptions: {rowpos: 9, colpos: 2}
	        //editrules: {email: true}
	    }, 
	    {
	        name: 'mobile',
	        index: 'mobile',
	        sorttype: "string",
	        editable: true,hidden: true,
	        formoptions: {rowpos: 10, colpos: 1}
	    }, 
	    {
	        name: 'status',
	        index: 'status',
	        align: "left",
	        sorttype: "float",
	        width:50,
	        editable: true,
	        editoptions: active_inactive,
	        stype:"select",
	        edittype: "select",
			formatter:"select",					
	        formoptions: {elmsuffix: jsEnum.STAR,rowpos: 10, colpos: 2},
	        editrules: {required: true},
	        searchoptions: { 
            	value: statusJson, 
            	defaultValue: "1" 
            }
	     }	        
	],
    caption: jqCaption.LIST_USER,
    loadComplete: function() {
        $("#gridTable").setColProp('role_name', {
            editoptions: {
                value: userRoles
            }
        });
//        $("#gridTable").setColProp('area_name', {
//            editoptions: {
//                value: areaList
//            }
//        });
//        $("#gridTable").setColProp('org_division_name', {
//            editoptions: {
//                value: orgDivisionList
//            }
//        });
//        $("#gridTable").setColProp('department_name', {
//            editoptions: {
//                value: departmentList
//            }
//        });
//        $("#gridTable").setColProp('section_name', {
//            editoptions: {
//                value: sectionList
//            }
//        });
        $("#gridTable").setColProp('designation_name', {
            editoptions: {
                value: designationList
            }
        });
//        $("#gridTable").setColProp('division_name', {
//            editoptions: {
//                value: divisionList
//            }
//        });
//        $("#gridTable").setColProp('district_name', {
//            editoptions: {
//                value: districtList
//            }
//        });
//        $("#gridTable").setColProp('upazila_name', {
//            editoptions: {
//                value: upazilaList
//            }
//        });
        
        
    },
    sortname: 'userId',
    ondblClickRow: function(rowid) {
    	 jQuery(this).jqGrid('viewGridRow', rowid,
                 {width:jsVar.CRUD_WINDOW_WIDTH,closeOnEscape:true});
    	 $('#v_password').html("<span>&nbsp;"+jsVar.USER_PASSWORD_MASK+"</span>");
    	 $("#nData").on('click',function(e){
    		 $('#v_password').html("<span>&nbsp;"+jsVar.USER_PASSWORD_MASK+"</span>");	
    	 });
    	 $("#pData").on('click',function(e){
    		 $('#v_password').html("<span>&nbsp;"+jsVar.USER_PASSWORD_MASK+"</span>");	
    	 });
    }        
}).navGrid('#gridPager',{},editParams,addParams,deleteParams,{},viewParams);
//edit,add,delete,search,view -- This is the sequence....



// Download Report icon on footer

jQuery("#gridTable").jqGrid (
        "navButtonAdd","#gridPager",
         {
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
        		window.location="userReport.action";
            }
}); 

//Asynchronous File Upload
function afu() {
	var userId=jQuery("#gridTable").jqGrid('getGridParam', 'selrow');
	if(userId==null)
		userId="new";
	
    $
        .ajaxFileUpload({
            url: 'fileUploader.action?userId='+userId+'&userType=user',
            secureuri: false,
            fileElementId: 'upload',
            dataType: 'JSON',            
            success: function(data, status) {

                if (data == 'Not a Valid type') {
                    alert("Not a Valid type");
                    // return false;
                } else if (data == "largefile") {
                    alert("File size smaller than 150KB is allowed.");
                    // return false;
                } else {
                    if (navigator.appName == 'Opera') {

                        document.getElementById('userImg').innerHTML = "";
                        document.getElementById('userImg').innerHTML = data;
                    } else {
                        document.getElementById('userImg').innerHTML = "";
                        document.getElementById('userImg').value = "/JGTDSL_WEB/" + data + "";
                        document.getElementById('userImg').src = "/JGTDSL_WEB/" + data + "";
                    }
                }
                 $("#upload").change(afu);

            },
            error: function(data, status, e) {
                alert(e);
            }
        });
}

function setFormEventsForEditNavigator(formId){

	var selRowId = jQuery("#gridTable").jqGrid('getGridParam','selrow');	
	var deptName = jQuery('#gridTable').jqGrid ('getCell', selRowId, 'department_name');
	var secName = jQuery('#gridTable').jqGrid ('getCell', selRowId, 'section_name');

	var distName = jQuery('#gridTable').jqGrid ('getCell', selRowId, 'district_name');
	var upazilaName = jQuery('#gridTable').jqGrid ('getCell', selRowId, 'upazila_name');

	var orgDivision=$("#org_division_name").val();
	resetDepartmentList(orgDivision,"");	
	$("#department_name option").filter(function() {
	    return $(this).text() == deptName; 
	}).prop('selected', true);
	
	var department=$("#department_name").val();
	resetSectionList(department,"");	
	$("#section_name option").filter(function() {
	    return $(this).text() == secName; 
	}).prop('selected', true);
	
	var division=$("#division_name").val();
	resetDistrictList(division,"");	
	$("#district_name option").filter(function() {
	    return $(this).text() == distName; 
	}).prop('selected', true);
	
	var district=$("#district_name").val();
	resetUpazilaList(district,"");	
	$("#upazila_name option").filter(function() {
	    return $(this).text() == upazilaName; 
	}).prop('selected', true);
	
}

function setFormEvents(formId){	
	var orgDivision=$("#org_division_name").val();	
	var department=$("#department_name").val();
	var section=$("#section_name").val();	
	var division=$("#division_name").val();	
	var district=$("#district_name").val();
	var upazila=$("#upazila_name").val();	
	
	resetDepartmentList(orgDivision,department);	
	resetSectionList(department,section);
	resetDistrictList(division,district);	
	resetUpazilaList(district,upazila);
	
	$('#userId', formId).unbind();	
	$('#org_division_name', formId).unbind();
	$('#department_name', formId).unbind();
	$('#division_name', formId).unbind();
	$('#district_name', formId).unbind();
	
	
	$("#org_division_name").on('change',function(e){
		orgDivision=$("#org_division_name").val();
		department="";
		resetDepartmentList(orgDivision,department);		
	});
	
	$("#department_name").on('change',function(e){
		department=$("#department_name").val();
		section="";
		resetSectionList(department,section);		
	});

	$("#division_name").on('change',function(e){
		division=$("#division_name").val();
		district="";
		resetDistrictList(division,district);		
	});
	
	$("#district_name").on('change',function(e){
		district=$("#district_name").val();
		upazila="";
		resetUpazilaList(district,upazila);		
	});
	
	$("#userId").on('blur',function(e){	
		var userId=$("#userId").val();
		$.ajax({
			url:"validateId.action",						//  validateId.action
			async :false,
			data:{service:jsEnum.USER_SERVICE, method: jsEnum.VALIDATE_USER,fieldValue1:userId,humanReadableFieldName1:"User Id" },
			success: function(response){
				
				if(response.status=="ERROR"){
					$(".ui-state-error").html(response.message)				    
					$("#FormError").show();			
				}
				else{
					$(".ui-state-error").html("");
				    $("#FormError").hide();
				}			
			},
			error:function(xhr)
				{
					alert("An error occured: ");
				}			 
			});
	});	


}


function resetDepartmentList(division,department_id){
	$("#department_name").empty();			
	$("#department_name").append( '<option value="">Select Department</option>' );
	if(division=="")
		return;
	var objDepartmentList = $.ajax({
		    url: sBox.ORG_DEPARTMENT+"?division_id="+division,
		    async: false,
		    success: function(data, result) { 
		        if (!result) alert(sBox.NDF);
		    }
		}).responseText;

	if(objDepartmentList!=""){
		var departmentList=objDepartmentList.split(";");
		for(var i=0;i<departmentList.length; i=i+1 )
		{
			var depatment=departmentList[i].split(":");
			if(depatment[0]==department_id)
				$("#department_name").append( '<option value="' + depatment[0] + '" selected="selected">' + depatment[1] + '</option>' );
			else			
				$("#department_name").append( '<option value="' + depatment[0] + '">' + depatment[1] + '</option>' );
		}
	} //End if
}

function resetSectionList(department,section_id){
	$("#section_name").empty();			
	$("#section_name").append( '<option value="">Select Section</option>' );
	if(department=="")
		return;
	var objSectionList = $.ajax({
		    url: sBox.ORG_SECTION+"?department_id="+department,
		    async: false,
		    success: function(data, result) { 
		        if (!result) alert(sBox.NDF);
		    }
		}).responseText;

	if(objSectionList!=""){
		var sectionList=objSectionList.split(";");
		for(var i=0;i<sectionList.length; i=i+1 )
		{
			var section=sectionList[i].split(":");
			if(section[0]==section_id)
				$("#section_name").append( '<option value="' + section[0] + '" selected="selected">' + section[1] + '</option>' );
			else			
				$("#section_name").append( '<option value="' + section[0] + '">' + section[1] + '</option>' );
		}
	} //End if
}

function resetDistrictList(division,district_id){
	$("#district_name").empty();			
	$("#district_name").append( '<option value="">Select District</option>' );
	if(division=="")
		return;
	var objDistrictList = $.ajax({
		    url: sBox.DISTRICT+"?division_id="+division,
		    async: false,
		    success: function(data, result) { 
		        if (!result) alert(sBox.NDF);
		    }
		}).responseText;

	if(objDistrictList!=""){
		var districtList=objDistrictList.split(";");
		for(var i=0;i<districtList.length; i=i+1 )
		{
			var district=districtList[i].split(":");
			if(district[0]==district_id)
				$("#district_name").append( '<option value="' + district[0] + '" selected="selected">' + district[1] + '</option>' );
			else			
				$("#district_name").append( '<option value="' + district[0] + '">' + district[1] + '</option>' );
		}
	} //End if
}

function resetUpazilaList(district,upazila_id){
	$("#upazila_name").empty();			
	$("#upazila_name").append( '<option value="">Select Upazila</option>' );
	if(isEmpty(district))
		return;
	var objUpazilaList = $.ajax({
		    url: sBox.UPAZILA+"?district_id="+district,
		    async: false,
		    success: function(data, result) { 
		        if (!result) alert(sBox.NDF);
		    }
		}).responseText;

	if(objUpazilaList!=""){
		var upazilaList=objUpazilaList.split(";");
		for(var i=0;i<upazilaList.length; i=i+1 )
		{
			var upazila=upazilaList[i].split(":");
			if(upazila[0]==upazila_id)
				$("#upazila_name").append( '<option value="' + upazila[0] + '" selected="selected">' + upazila[1] + '</option>' );
			else			
				$("#upazila_name").append( '<option value="' + upazila[0] + '">' + upazila[1] + '</option>' );
		}
	} //End if
}



function resetPassword(userId)
{
	$("#wait_div").html(jsImg.LOADING);
	$.ajax({
	    url: 'resetUserPassword.action?user.userId='+userId,			// resetUserPassword action
	    type: 'POST',
	    processData: false,
	    contentType:false,
	    async: true,
	    cache: false,
	    success: function (response) {
					alert(response.message);
					$("#wait_div").html(jsVar.EMPTY);
	    },
	  error: function (response) {alert(response);$("#wait_div").html("");}		    
	});

}

function setRowSpan(formId,rowSpan)
{
	   var $formRows = formId.find(".FormData");
       $formRows.eq(0).children("td.DataTD").eq(1).attr("rowspan", ""+rowSpan); //.css("text-align", "center");
       for(var i=1;i<rowSpan;i++){
    	   $formRows.eq(i).children("td.DataTD").eq(1).hide();
       }
       //$formRows.eq(2).children("td.DataTD").eq(1).hide();
       //$formRows.eq(3).children("td.DataTD").eq(1).hide();
}

gridColumnHeaderAlignment("gridTable",["userId","userName","role_name","designation_name"],"left");
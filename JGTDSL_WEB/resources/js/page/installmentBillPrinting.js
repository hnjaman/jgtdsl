var p_customer_id="";
var p_month="";
var p_year="";
var p_area_id="";
var p_customer_category="";


$("#bill_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: jsEnum.GRID_RECORED_FETCHER+'?service=org.jgtdsl.models.BillingService&method=getInstallmentBillList'+'&extraFilter=area',
   	jsonReader: {
            repeatitems: false,
            id: "installmentId"
	},
   colNames: ['Installment Id','Customer Id', 'Customer Name','Bill Amount', 'Surcharge','Meter Rent','Total','Status',''],
   colModel: [{
			       name: 'installmentId',
			       index: 'installmentId',
			       width:60,
			       align:'center',
			       sorttype: 'string',
			       search: true
				},{
			        name: 'customerId',
			        index: 'customerId',
			        width:60,
			        align:'center',
			        sorttype: 'string',
			        search: true
				},
            	{
	                name: 'customerName',
	                index: 'customerName',
	                width:150,
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'principal',
	                index: 'principal',
	                width:80,
	                sorttype: "string",
	                align:'right',
	                search: true,
            	},
            	{
	                name: 'surcharge',
	                index: 'surcharge',
	                width:80,
	                align:'right',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'meterRent',
	                index: 'meterRent',
	                sorttype: "string",
	                align:'right',
	                width:80,
	                search: true,
            	},
            	{
	                name: 'total',
	                index: 'total',
	                sorttype: "string",
	                align:'right',
	                width:80,
	                search: true,
            	},
            	{
	                name: 'statusName',
	                index: 'statusName',
	                sorttype: "string",
	                align:'center',
	                width:80,
	                search: true,
            	},         	
            	{ 
            		name: 'Download', 
            		width: 12, 
            		align:'center',
            		formatter:function(){
                          return "<span class='ui-icon ui-icon-circle-arrow-s' style='margin-left:3px;'></span>"
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {

                            return ' onClick="window.location=\'downloadInstallmentBill.action?download_type=S&installment_id='+rowObject.installmentId+'\'"';
                    }
                }
        ],
	datatype: 'local',
	height: $("#bill_grid_div").height()-80,
	width: $("#bill_grid_div").width()-2,
   	pager: '#bill_grid_pager',
   	sortname: 'installment_id',
    sortorder: "desc",
	caption: "Installment Bills"
}));

jQuery("#bill_grid").jqGrid('navGrid','#bill_grid_pager',$.extend({},footerButton,{search:true,refresh:true,beforeRefresh: function () {reloadBillGrid();}}),{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment("left","bill_grid",["customerName"]);



$('#bill_grid').jqGrid('navGrid','#bill_grid_pager')
    .navButtonAdd('#bill_grid_pager',{
        caption:"<b><font color='blue'>Download Bills</font></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", 
        buttonicon:"ui-icon-print", 
        id: "icon_download_approve_bills",
        onClickButton: function(){
				downloadBill('A');
        }
});

function downloadBill(downloadType){
	//reloadBillGrid();
	var validate=valiateFields();
	if(validate==true)
		window.location='downloadInstallmentBill.action?download_type='+downloadType+'&customer_id='+p_customer_id+'&bill_month='+p_month+'&bill_year='+p_year+'&area_id='+p_area_id+'&customer_category='+p_customer_category;		
}

function valiateFields(){
	 var isValid=true;
	 var bill_for=getRadioCheckedValue("bill_parameter\\.bill_for"); 
	 
	 
	 if(bill_for=="area_wise")
	 	isValid=validateField("area_id");
	 else if(bill_for=="category_wise")
	 	isValid=validateField("area_id","customer_category");
	 else if(bill_for=="individual_customer")
	 	isValid=validateField("customer_id");

	 if(isValid==true)
	 	isValid=validateField("billing_month","billing_year");
	 
	 return isValid;
}

function reloadBillGrid(){
	
    var ruleArray=[["bill_month","bill_year"],["eq","eq"],[$("#billing_month").val(),$("#billing_year").val()]];
    var $grid = $('#bill_grid');
    var caption_extra="";
    
	if($("#billing_month").val()=="" || $("#billing_year").val()==""){
		clearGridData("bill_grid");
		$grid.jqGrid('setCaption','Please Select Month, Year');
		return;
	}
	
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="individual_customer" && $("#customer_id").val()!=""){
		
		ruleArray[0].push("customer.customer_id");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#customer_id").val());
		caption_extra=". Customer Id - "+$("#customer_id").val();
		p_customer_id=$("#customer_id").val();
	}
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="area_wise"){
		if($("#area_id").val()=="") return;
		
		ruleArray[0].push("area");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#area_id").val());
		caption_extra=". "+$("#area_id option:selected").text();
		
		p_area_id=$("#area_id").val();
	}
	
	else if($("input[name=bill_parameter\\.bill_for]:checked").val()=="category_wise")		{								
	   if($("#area_id").val()=="" || $("#customer_category").val()=="") return;
	   
	    ruleArray[0].push("area");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#area_id").val());
		
	    ruleArray[0].push("customer_category");
		ruleArray[1].push("eq");
		ruleArray[2].push($("#customer_category").val());
		caption_extra=". "+$("#area_id option:selected").text()+" ["+$("#customer_category option:selected").text()+"] ";
		
		p_area_id=$("#area_id").val();
		p_customer_category=$("#customer_category").val();
		
	}
	
	p_month=$("#billing_month").val();
	p_year=$("#billing_year").val();
	
	var postdata=getPostFilter("bill_grid",ruleArray);
   	$("#bill_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});    		
	reloadGrid("bill_grid");
	
	
	$grid.jqGrid('setCaption', 'Installment Bills - '+$("#billing_month option:selected").text()+', '+$("#billing_year").val()+caption_extra);
}

$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.METERED_CUSTOMER_LIST,
    	onSelect:function (){
    		getCustomerInfo("",$('#customer_id').val());
    	},
}));

$("#billing_month").val(getCurrentMonth());
$("#billing_year").val(getCurrentYear());
reloadBillGrid();

Calendar.setup({
    inputField : "issue_date",
    trigger    : "issue_date",
	eventName : "focus",
    onSelect   : function() { this.hide()},
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
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
	else if(type=="by_category"){
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

	
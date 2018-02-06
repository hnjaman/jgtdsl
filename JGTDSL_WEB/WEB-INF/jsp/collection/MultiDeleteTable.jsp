<div class="content-bg">
<div id="gridWrapper">
		<table id="customer_collection_grid"></table>
		<div id="customer_collection_grid_pager"></div>
</div>
<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">
<table width="100%">
<tr>
	<td  width="60%" align="left">
		<button class="btn btn-beoro-3" type="button" id="btn_parameter" onclick="validateAndSaveCollection()">
		<span class="splashy-document_letter_okay"></span>
		Save</button>
		<button class="btn btn-beoro-3"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">
		<span class="splashy-application_windows_edit"></span>
		Edit</button>
	</td>
	<td  width="40%" align="right">
		<button class="btn btn-beoro-3"  type="button" id="close_btn" onclick="closeModal()">
		<span class="splashy-application_windows_edit"></span>
		Close</button>
	
	</td>
</tr>
</table>					
					
															

</div>
</div>
<script type="text/javascript">
var recordUrl=jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.COLLECTION_SERVICE+'&method=getCollectionList';
$("#customer_collection_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions,{
   	url:recordUrl,
   	jsonReader: {
            repeatitems: false,
            id: "collection_id"
	},
    colNames: ['Dated', 'Bill of Month', 'Remarks', 'Bill Amount'],
    colModel: [{
					name: 'collection_date_f1',
					index: 'collection_date_f1',
					width:80,
					align:'center',
					sorttype: 'string',
					search: true
            	},
            	{
	                name: 'bill_month_year',
	                index: 'bill_month_year',
	                sorttype: "string",
	                width:200,
	                align:'left',
	                search: true
            	},
            	{
					name: 'remarks',
					index: 'remarks',
					width:80,
					align:'center',
					sorttype: 'string',
					search: true
            	},
            	{
					name: 'grand_total',
					index: 'grand_total',
					width:80,
					align:'center',
					sorttype: 'string',
					search: true
            	}
            	
        ],   	
    multiselect: true,
	height: 345,
	width: 648,
	footerrow: false,
   	pager: '#customer_collection_grid_pager',
   	sortname: 'customer_id',
    sortorder: "asc",
	caption: "List of Collection Data",
	loadComplete: function () {
                    var sum = jQuery("#collection_table").jqGrid('getCol', 'difference', false, 'sum');
                    jQuery("#collection_table").jqGrid('footerData','set', {month_year: 'Total:', difference: sum});
                }
	
}));

jQuery("#customer_collection_grid").jqGrid('navGrid','#customer_collection_grid_pager',{del:false,add:false,edit:false},{},{},{},{multipleSearch:true});
gridColumnHeaderAlignment('customer_collection_grid',["bill_month_year"],"left");


var ruleArray=[["customer_id"],["eq"],["<%=(String)request.getParameter("customer_id")%>"]];
var postdata=getPostFilter("customer_collection_grid",ruleArray);

$("#customer_collection_grid").jqGrid('setGridParam',{search: true,postData: postdata,url:recordUrl,page:1,datatype:'json'});    	
$("#customer_collection_grid").trigger("reloadGrid");
</script>
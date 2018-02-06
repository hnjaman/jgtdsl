jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.NEWLY_APPLIED_CUSTOMER_LIST+'&extraFilter=area',
        jsonReader: {
            repeatitems: false,
            id: "customer_id"
        },        
        colNames: ['Customer Id', 'Customer Name','Category', 'Area','Mobile','Status'],
        colModel: [{
	                name: 'customer_id',
	                index: 'customer_id',
	                width:100,
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
	                name: 'category_name',
	                index: 'category_name',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'area_name',
	                index: 'area_name',
	                sorttype: "string",
	                search: true,
            	},
            	{
	                name: 'mobile',
	                index: 'mobile',
	                sorttype: "string",
	                search: true,
            	},
                {
                    name: 'status',
                    index: 'status',
                    sorttype: "string",
                    width:60,
                    align:'center',
                    search:false
            	}
        ],
        caption: jqCaption.LIST_CUSTOMER,
        ondblClickRow: function(rowid) {
        	callAction('demandNoteDataEntry.action?mode="S"&customer_id='+rowid);
        }        
    }).navGrid('#gridPager',$.extend(footerButton,{search:true,refresh:true}),{},{},{},{},{});

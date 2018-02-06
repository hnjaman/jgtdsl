jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.CUSTOMER_LIST+'&extraFilter=area',
        jsonReader: {
            repeatitems: false,
            id: "customer_id"
        },
        colNames: ['Customer Id', 'Customer Name','Father Name','Category', 'Area','Mobile','Status','Created On'],
        colModel: customerGridColModel,
        caption: jqCaption.LIST_CUSTOMER,
	    sortname: 'connection_status desc,created_on ',
        sortorder: "desc",
        ondblClickRow: function(rowid) {
        	callAction('demandNoteDataEntry.action?mode="S"&customer_id='+rowid);
        }           
    }).navGrid('#gridPager',$.extend(footerButton,{search:true,refresh:true}),{},{},{},{},{});



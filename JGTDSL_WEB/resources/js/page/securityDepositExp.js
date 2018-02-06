
jQuery("#gridTable")
    .jqGrid({
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.DEPOSIT_SERVICE+'&method='+jsEnum.SD_EXP_LIST,
        jsonReader: {
            repeatitems: false,
            id: "type_id"
        },
        colNames: ['Deposit Id', 'Customer Id', 'Customer Name','Bank','Branch','Account','Valid Till','Total Deposit','Expire In (Days)'],
        colModel: [{
	                name: 'deposit_id',
	                index: 'deposit_id',
	                //hidden:true
            	},
            	{
	                name: 'customer_id',
	                index: 'customer_id',
	                //hidden:true
            	},
            	{
            		name: 'customer_name',
	                index: 'customer_name',
	                sorttype: "String"
	                
                },
                {
            		name: 'bank_name',
	                index: 'bank_name',
	                sorttype: "string"
                },
                {
            		name: 'branch_name',
	                index: 'branch_name',
	                sorttype: "string"
                },
                {
            		name: 'account_name',
	                index: 'account_name',
	                sorttype: "string"
                },
                {
            		name: 'valid_to',
	                index: 'valid_to',
	                sorttype: "string",
	                align:'center'
	                	
                }
                ,
                {
            		name: 'total_deposit',
	                index: 'total_deposit',
	                sorttype: "string",
	                align:'right'
                }
                ,
                {
            		name: 'expire_in',
	                index: 'expire_in',
	                sorttype: "string",
	                align:'right'
            	}
        ],
        caption: jqCaption.LIST_SD_EXPIRE,
        sortname: 'expire_in'
    }).navGrid('#gridPager',{edit: false, add: false, del: false, search: true, refresh:true,view:false},{},{},{},{},{});


jQuery("#gridTable").jqGrid (
        "navButtonAdd","#gridPager",
         {
        	
        	 
             caption: "", buttonicon: "ui-icon-print", title: "Download Report",
             onClickButton: function() {
        		window.location="securityDepositExpireReport.action";
         }
         });

jQuery("#gridTable")
    .jqGrid({		//"gridRecordFetcher"				//"org.jgtdsl.models.CustomerService"				// extraFilter in org.jgtdsl.actions.GridRecordFetcher 
        url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.CUSTOMER_SERVICE+'&method='+jsEnum.CUSTOMER_LIST+'&extraFilter=area',
        jsonReader: {
            repeatitems: false,
            id: "customer_id"
        },
        colNames: ['Customer Id','Customer Name','Father Name','Category', 'Area','Phone','Status','Created On'],
        colModel: customerGridColModel,	// common.js 
        caption: jqCaption.LIST_CUSTOMER,	//constant.js
		rowattr: function (rd) {
            if (rd.connection_status === "0") {
                return {"class": "redBackgroundRow"};
            }
            else if (rd.connection_status === "2") {
                return {"class": "newlyAppliedRow"};	//common.css
            }
        },
        sortname: 'connection_status desc,created_on ',
        sortorder: "desc",
        ondblClickRow: function(rowid) {
        	callAction('viewCustomer.action?customer_id='+rowid); // customer.java
        },
        
    }).navGrid('#gridPager',$.extend(
    		$.jgrid.search, {
    		    // ... some other default which you use
    		    afterRedraw: function (p) {
    		        var $form = $(this), formId = this.id, // fbox_list
    		            bindKeydown = function () {
    		                $form.find("td.data>.input-elm").keydown(function (e) {
    		                    if (e.which === $.ui.keyCode.ENTER) {
    		                        $(e.target).change();
    		                        $("#" + $.jgrid.jqID(formId) + "_search").click();
    		                    }
    		                });
    		            },
    		            oldOnChange = p.onChange,
    		            myOnChange = function (param) {
    		                var $input = $form.find("td.data>.input-elm"), events;
    		                oldOnChange.call(this, param);
    		                if ($input.length > 0) {
    		                    events = $._data($input[0], "events");
    		                    if (events && !events.keydown) {
    		                        bindKeydown();
    		                    }
    		                }
    		            };
    		        p.onChange = myOnChange;
    		        bindKeydown.call(this);
    		    }
    		},    		
    		footerButton,{search:true,refresh:true}),{},{},{},{},{});

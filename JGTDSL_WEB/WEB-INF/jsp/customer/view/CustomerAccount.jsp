<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css" media="screen">
    th.ui-th-column div{
        white-space:normal !important;
        height:auto !important;
        padding:2px;
    }
    .z-tabs > .z-container > .z-content > .z-content-inner{
    	padding-left: 0px !important;
    }
    
</style>
<div id="sw-basic-step-5" style="overflow: auto;width: 100%;height: 100%">
    <h4 class="StepTitle">Customer's Account</h4>
    <div class="row-fluid" id="ledger_div" style="width: 100%;">
       <!--  <div class="span6" style="width: 50%;" id="customer_ledger_div">
        	<div>
					<table id="customer_ledger_grid"></table>
					<div id="customer_ledger_grid_pager" ></div>
			</div>
        </div>
         -->
        <div class="span12" style="width: 100%;height: 30%" id="second_ledger"> 
			<div id="tabbed-nav">
	            <ul>	                
	            	 <li><a><font color="#004b5e" style="font-weight: bold;">Customer Ledger</font></a></li>
	                <li><a><font color="#004b5e" style="font-weight: bold;">Security Ledger</font></a></li>
	                <li><a><font color="#004b5e" style="font-weight: bold;">Connection Ledger</font></a></li>
	            </ul>
	            <div>
	            	<div>
						<table id="customer_ledger_grid"></table>
						<div id="customer_ledger_grid_pager" ></div>
					</div>
	                <div>
	                    <table id="deposit_ledger_grid"></table>
						<div id="deposit_ledger_grid_pager" ></div>
	                </div>
	                <div>
						<table id="connection_ledger_grid"></table>
						<div id="connection_ledger_grid_pager" ></div>
	                </div>                
	            </div>
        	</div>
		</div>
     </div>
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript">
var column1,column2;
var name1,index1,name2,index2;


<s:if test="%{customer.connectionInfo.isMetered != null}">

<s:if test="customer.connectionInfo.isMetered.label == @org.jgtdsl.enums.MeteredStatus@METERED.label">
column1="Min Load";column2="Max Load";
name1="min_load";index1="min_load";
name2="max_load";index2="max_load";

var connectionLedgerUrl="getConnectionLedger.action?customer_id="+$("#customer_id").val();
$("#connection_ledger_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	url: connectionLedgerUrl,
   	jsonReader:{
        id: "event_date"
	},
    colNames: ['Date','Description', column1,column2],
    colModel: [{
	                name: 'event_date',
	                index: 'event_date',
	                width:50,
	                align:'center',
	                sorttype: 'date'
            	},
            	{
	                name: 'description',
	                index: 'description',
	                width:50,
	                hidden : false
            	},
            	{
	                name: name1,
	                index: index1,
	                sorttype: "number",
	                align:'right',
	                width:50
            	},
            	{
	                name: name2,
	                index: index2,
	                sorttype: "number",
	                align:'right',
	                width:50
            	}
        ],   	
	height: 500,
	//width: 900,
   	pager: '#connection_ledger_grid_pager',
	caption: "Connection Ledger",	
    datatype: 'json'
}));
</s:if>
<s:if test="customer.connectionInfo.isMetered.label == @org.jgtdsl.enums.MeteredStatus@NONMETERED.label">
column1="Single";column2="Double";
name1="single_burner";index1="single_burner";
name2="double_burner";index2="double_burner";

$("#connection_ledger_grid").jqGrid($.extend(true, {}, scrollPagerGridOptions, {
	//url: jsEnum.GRID_RECORED_FETCHER+'?service='+jsEnum.BURNER_QNT_CHANGE_SERVICE+'&method='+jsEnum.BURNER_QNT_CHANGE_LIST,//+'&extraFilter=area',
   	url:"getConnectionLedgerGrid.action?customer_id="+$("#customer_id").val(),
   	jsonReader: {
            repeatitems: false,
            id: "pid"
	},
	colNames: ['Old(Double)','New Permanent Disconnection','New Temporary Disconnection','Increase','Reconnection/Temp','Reconnection/Permanent','New(Double)','Effective Date'],
			colModel : [  {
				name : 'old_double_burner_qnt',
				index : 'old_double_burner_qnt',
				sorttype : "string",
				width : 50,
				align : 'center',
				search : true
			}, {
				name : 'new_permanent_disconnected_burner_qnt',
				index : 'new_permanent_disconnected_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'new_temporary_disconnected_burner_qnt',
				index : 'new_temporary_disconnected_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'new_incrased_burner_qnt',
				index : 'new_incrased_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'

			}, {
				name : 'new_reconnected_burner_qnt',
				index : 'new_reconnected_burner_qnt',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			},{
				name : 'new_reconnected_burner_qnt_permanent',
				index : 'new_reconnected_burner_qnt_permanent',
				sorttype : "string",
				width : 70,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'new_double_burner_qnt',
				index : 'new_double_burner_qnt',
				sorttype : "string",
				width : 50,
				align : 'center',
				search : true,
				classes : 'new_qnt'
			}, {
				name : 'effective_date',
				index : 'effective_date',
				sorttype : "string",
				search : true
			}],
	// datatype: 'json',
	//height: $("#wizard").height(),
	//width: $("#sw-basic-step-5").width(),
	height:500,
   	pager: '#connection_ledger_grid_pager',
	caption: "Connection Ledger",
	sortname: 'effective_date,pid',
    sortorder: "asc",
    datatype: 'json',

}));
/*
setInterval(reloadBurnerQntChangeHistory($("#customer_id").val()),10000);
/*

function reloadBurnerQntChangeHistory(customer_id){
    var ruleArray=[["BQC.CUSTOMER_ID"],["eq"],[customer_id]];
    var postdata=getPostFilter("connection_ledger_grid",ruleArray);
    $("#connection_ledger_grid").jqGrid('setGridParam',{search: true,postData: postdata,page:1,datatype:'json'});   
   	reloadGrid("connection_ledger_grid");
}

jQuery("#connection_ledger_grid").jqGrid('navGrid','#connection_ledger_grid_pager',$.extend({},footerButton,{search:true,refresh:true}),{},{},{},
		{
		multipleSearch:true,
		onSearch: function () {	
				
			   var ruleArray=[["BURNER_QNT_CHANGE.CUSTOMER_ID"],["eq"],[$("#customer_id").val()]];
			   var oldRules=["customer_id"];
			   var newRules=["BURNER_QNT_CHANGE.customer_id"];			   
			   modifyGridPostData("meterRent_change_history_this_grid",ruleArray,oldRules,newRules);	
		   }
		
		});*/

</s:if>
</s:if>
  $.ajax({
    url: "getDuesListByString.action?customer_id="+$('#customer_id').val(),
  	dataType: 'text',		    
    type: 'POST',
    async: true,
    cache: false,
	success: function (response){
    	$("#dueListbyString").val(response);   	
    }    
  });

</script>



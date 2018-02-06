<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("meterRepairmentHome.action");
	setTitle("Meter Repairment");
</script>
<link href="/JGTDSL_WEB/resources/css/page/meterRepair.css" rel="stylesheet" type="text/css" />

<div class="page-left-top" style="width: 50%;height: 52%;">
	<div class="row-fluid" style="height: 65%;">
		<jsp:include page="../common/CustomerInfo.jsp" />
	</div>
	<div style="width: 100%;height: 45%" id="available_meter_grid_div">
		<jsp:include page="../common/MeterGrid.jsp" />
   </div>
</div>

<div id="reading_table_div" style="width: 48%;height: 62%;float: left;margin-left: 1%;border: none;">
	<div class="row-fluid">
		<jsp:include page="MeterRepairment_MeterInfo.jsp" />
	</div>	
	<div class="row-fluid" style="padding-top: 15px;">
		<jsp:include page="MeterRepairment_RepairmentInfo.jsp" />
	</div>	
</div>

<p style="clear: both;margin-top: 5px;"></p>

<div class="page-bottom" id="customer_grid_div" style="width: 99%;height: 36%"> 
<div id="tabbed-nav">
            <ul>
                <li><a>All Customer</a></li>
                <li><a>Repairment History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <li><a>Repairment History(<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
					<table id="repairment_history_this_grid"></table>
					<div id="repairment_history_this_grid_pager" ></div>
                </div>
                <div>
                    <table id="repairment_history_all_grid"></table>
					<div id="repairment_history_all_grid" ></div>
                </div>
            </div>

        </div>
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterListGrid.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterRepairment.js"></script>

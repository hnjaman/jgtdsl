<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("meterReplacementHome.action");
	setTitle("Meter Change/Replacement");
</script>

<div style="width: 99%;height: 20%;" id="top_div">
	<div class="row-fluid" style="height: 100%;">
		<div style="width: 49%;float: left;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
		
		<div style="width: 49%;float: left;margin-left: 10px;height: 110%;" id="available_meter_grid_div">
			<jsp:include page="../common/MeterGrid.jsp" />
		</div>
	</div>
</div>

<div style="width: 99%;height: 30%;clear: both;padding-top: 5px;" id="mid_div">
	<div class="row-fluid">
		<div style="width: 49%;float: left;margin-top: 5px;">				
			<jsp:include page="MeterReplacement_OldMeter.jsp" />
		</div>
		<div style="width: 50%;float: left;margin-left: 10px;">
			<jsp:include page="MeterReplacement_NewMeter.jsp" />	
		</div>
	</div>
</div>

<div id="customer_grid_div" style="height: 24%;width: 99%;">

	<div id="tabbed-nav">
            <ul>
                <li><a>All Customer</a></li>
                <li><a>Replacement History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <li><a>Replacement History (<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="replacement_history_this_grid"></table>
					<div id="replacement_history_this_grid_pager" ></div>
                </div>
                <div>
                    <table id="replacement_history_all_grid"></table>
					<div id="replacement_history_all_grid_pager" ></div>
                </div>
                
            </div>
        </div>
</div>


<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterListGrid.js"></script> 
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterReplacement.js"></script>

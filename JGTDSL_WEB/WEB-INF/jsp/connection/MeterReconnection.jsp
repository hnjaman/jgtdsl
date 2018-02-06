<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("meterReconnectionHome.action");
	setTitle("Reconnection Information(Metered)");
</script>

<div id="customer_meter_div" style="height: 55%;width: 99%;">
	<div id="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 62%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
		<div style="height: 35%;width: 100%;margin-top: -10px;" id="available_meter_grid_div">
			<jsp:include page="../common/MeterGrid.jsp" />
   		</div>
   		
	</div>

<div style="width: 51%; height: 99%;float: left;margin-left: 1%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">					
				<jsp:include page="MeterReconnectionInfo.jsp" />	
		</div>
	</div>	
</div>
</div>


<div id="customer_grid_div" style="height: 39%;width: 99%;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>Meter Disconnected Customer</a></li>
                <li><a>Reconnect. History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <li><a>Reconnect. History (<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="reconn_history_this_grid"></table>
					<div id="reconn_history_this_grid_pager" ></div>
                </div>
                <div>
                    <table id="reconn_history_all_grid"></table>
					<div id="reconn_history_all_grid_pager" ></div>
                </div>
            </div>

        </div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterListGrid.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterReconnection.js"></script>

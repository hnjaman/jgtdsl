<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("burnerQntChangeHome.action");
	setTitle("Appliance Information");
</script>
<style type="text/css">
        .new_qnt {
            background-color: #E0FFFF !important;
            background-image: none;
        }
</style>


<div id="customer_meter_div" style="width: 99%;height: 53%;">
	<div id="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 62%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
		<div class="row-fluid">
			<div class="span12" id="rightSpan">					
					<button type="button" style="margin-left:120px" onclick="newApplianceEntryDivDisplaying()" class="btn btn-primary">New Applliance Connection</button>
                    <button type="button" onclick="raizerDisconnectionDivDisplaying()" class="btn btn-danger">Raizer Disconnection</button>	
                    <button type="button" onclick="raizerReconnectionDivDisplaying()" class="btn btn-success">Raizer Reconnection</button>
			</div>
		</div>	
		<div style="height: 35%;width: 100%;margin-top: 5px;" id="available_appliance_grid_div">
			<jsp:include page="../common/ApplianceGrid.jsp" />
   		</div>
   		
	</div>

	<div style="width: 51%; height: 99%;float: left;margin-left: 0%;">
		<div class="row-fluid">
			<div class="span12" id="BurnerQntChangeInfo" style="display:none">					
					<jsp:include page="BurnerQntChangeInfo.jsp" />	
			</div>
			<div class="span12" id="newApllianceEntryDiv" style="display:none">					
					<jsp:include page="NewApplianceEntryInfo.jsp" />	
			</div>
			<div class="span12" id="raizerDisconnectionDiv" style="display:none">					
					<jsp:include page="RaizerDisconnection.jsp" />	
			</div>
			<div class="span12" id="raizerReconnectionDiv" style="display:none">					
					<jsp:include page="RaizerReconnection.jsp" />	
			</div>
		</div>	
	</div>
</div>


<div id="customer_grid_div" style="height: 49%;width: 99%; margin-top:30px;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>Non-Metered Customer List</a></li>
                <li><a>Burner Change History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <li><a>Burner Change History (<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="burnerQnt_change_history_this_grid"></table>
					<div id="burnerQnt_change_history_this_grid_pager" ></div>
                </div>
                <div>
                    <table id="burnerQnt_change_history_all_grid"></table>
					<div id="burnerQnt_change_history_all_grid_pager" ></div>
                </div>
            </div>

        </div>
</div>


<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/BurnerQntChange.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/applianceListGrid.js"></script>

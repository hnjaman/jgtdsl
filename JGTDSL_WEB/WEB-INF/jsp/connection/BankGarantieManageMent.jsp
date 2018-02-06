<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("bankGarantieManagementHome.action");
	setTitle("Bank Garnatie Change Information");
</script>

<div id="customer_meter_div" style="height: 55%;width: 99%;">
	<div id="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 62%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
	
	</div>

<div style="width: 51%; height: 99%;float: left;margin-left: 1%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">					
				<jsp:include page="BankGarantieExtentionInfo.jsp" />	
		</div>
	</div>	
	<div class="row-fluid"  style="min-height: 33%;width: 70%;margin-top: 0px;margin-left: 0%;">		
   				
				<jsp:include page="BankGarantieExpireSearchInfo.jsp" />	
							
	</div>
</div>

</div>


<div id="customer_grid_div" style="height: 39%;width: 99%;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>Customer List (<font color="red" style="font-weight: bold;">Expire List</font>)</a></li>
                <li><a>Expire Change History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="meterRent_change_history_this_grid"></table>
					<div id="meterRent_change_history_this_grid_pager" ></div>
                </div>               
            </div>

        </div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/bankGarantieManagement.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/jqGridDefault.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/jqGridCommon.js"></script>	

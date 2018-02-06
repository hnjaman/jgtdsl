<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("customerTypeChangeHome.action");
	setTitle("Customer Type Change Information");
</script>
<style type="text/css">
.overlay {
    position:absolute;
    top:0;
    left:0;
    right:0;
    bottom:0;
    background-color:rgba(0, 0, 0, 0.65);
    background: url(data:;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAgY0hSTQAAeiYAAICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAABl0RVh0U29mdHdhcmUAUGFpbnQuTkVUIHYzLjUuNUmK/OAAAAATSURBVBhXY2RgYNgHxGAAYuwDAA78AjwwRoQYAAAAAElFTkSuQmCC) repeat scroll transparent\9;
    z-index:9999;
    color:white;
}

.overlay {
    text-align: center;
}
 
.overlay:before {
    content: '';
    display: inline-block;
    height: 100%;
    vertical-align: middle;
    margin-right: -0.25em;
}
.txt {
    display: inline-block;
    vertical-align: middle;
    padding: 10px 15px;
    position:relative;
    font-weight:bold;
}
</style>
<div id="customer_meter_div" style="height: 55%;width: 99%;">
	<div id="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 62%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
		<div style="height: 35%;width: 100%;margin-top: -10px;" id="available_meter_grid_div">
			<div class="row-fluid" id="common_address_row">							
					<div class="span12">									    
						<label style="width: 8%;"><font color="green" style="font-weight: bold;">Dues List</font></label>
						<textarea rows="1" style="width: 76%" id="dues_list" disabled="disabled"></textarea>
					</div>
           </div>
   		</div>
	</div>

<div style="width: 51%; height: 99%;float: left;margin-left: 1%;">
	<div class="row-fluid" id="TypeChangeInfo">
		<div class="span12" id="rightSpan" >					
				<jsp:include page="CustomerTypeChangeInfo.jsp" />									
		</div>
	</div>	
</div>
</div>


<div id="customer_grid_div" style="height: 39%;width: 99%;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>Customer List</a></li>
                <li><a>Type Change History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <li><a>Type Change History(<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="type_change_history_this_grid"></table>
					<div id="type_change_history_this_grid_pager" ></div>
                </div>
                <div>
                    <table id="type_change_history_all_grid"></table>
					<div id="type_change_history_all_grid_pager" ></div>
                </div>
            </div>

        </div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterListGrid.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/customerTypeChange.js"></script>

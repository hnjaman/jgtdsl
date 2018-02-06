<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("loadPressureChangeHome.action");
	setTitle("Load-Pressure Change Information");
</script>
<style type="text/css">

	/* Style to fit the meter reading form in this meter disconnection interface */
	.mr_lable{width: 35%;}
	.mr_text{width: 60%;}
	.mr_select{width: 64%;}
	.mr_month{width: 36%;}
	.mr_year{width: 26.5%;}
	
	.mr_text1{width: 30.5%;}
	.mr_text2{width: 23.5%;}
	
	.mr_textarea{width:60.5%}
	
	.mr_address_label{width:17%}
	.mr_address_textarea{width:80.5%}
	
		
	.mr_customer_name_label{width:26%}
	.mr_customer_name_text{width:70%}
	.mr_customer_type_label{width:45%}
	.mr_customer_type_text{width:47%}
	
	.mr_1row_label{width:52%}
	.mr_1row_select{width:48%}
 	.mr_1row_text{width:7%}
 	
</style>


<div id="customer_meter_div" style="height: 67%;width: 99%;">
	<div id="customer_info" style="float:left; width: 48%;height:100%;">
		<div class="row-fluid" style="height: 32%;">
			<jsp:include page="../common/CustomerInfo.jsp" />
		</div>
		<div style="height: 30%;width: 100%;margin-top: -5px;" id="available_meter_grid_div">
			<jsp:include page="../common/MeterGrid.jsp" />
   		</div>
   		<div style="min-height: 33%;width: 100%;margin-top: 15px;">		
   			<form id="lpChangeInfoForm" name="lpChangeInfoForm">	
				<jsp:include page="LoadPressureChangeInfo.jsp" />	
			</form>				
		</div>
	</div>

<div style="width: 51%; height: 99%;float: left;margin-left: 1%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Meter Reading Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
					<form id="meterReadingForm" name="meterReadingForm">
     					<jsp:include page="../meter-reading/MeterReadingEntryForm.jsp" />	
     				</form>															
				</div>
			</div>
		</div>
	</div>	
</div>
</div>


<div id="customer_grid_div" style="height: 27%;width: 99%;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>All Customer(Metered)</a></li>
                <li><a>Load/Pressure Change History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <li><a>Load/Pressure Change History (<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="lpChange_history_this_grid"></table>
					<div id="lpChange_history_this_grid_pager" ></div>
                </div>
                <div>
                    <table id="lpChange_history_all_grid"></table>
					<div id="lpChange_history_all_grid_pager" ></div>
                </div>
            </div>

        </div>
</div>


<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterListGrid.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/loadPressureChange.js"></script>

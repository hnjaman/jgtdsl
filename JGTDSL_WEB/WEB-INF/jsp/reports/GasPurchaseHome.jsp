<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("editCustomerInfoHome.action");
	setTitle("Customer Info Update");
</script>
<style type="text/css">
.span6.extra{
min-height: 33px !important;
}

</style>
<form id="customerInfoForm" name="customerInfoForm">
<div class="row-fluid">
	<div style="width: 30%;height: 98%;float: left;">	
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Gas Purchase Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Power(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_power_gvt name="gasPurchase.bgfcl_power_gvt"/>									
								
						  	</div>	
						  	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Power(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_power_pvt name="gasPurchase.bgfcl_power_pvt"/>									
								
						  	</div>	
						  	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Captive Power(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_captive_gvt name="gasPurchase.bgfcl_captive_gvt"/>									
								
						  	</div>	
						 </div>
						   <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Captive Power(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_captive_pvt name="gasPurchase.bgfcl_captive_pvt"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">CNG(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_cng_gvt name="gasPurchase.bgfcl_cng_gvt"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">CNG(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_cng_pvt name="gasPurchase.bgfcl_cng_pvt"/>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Industry(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_industry_gvt name="gasPurchase.bgfcl_industry_gvt"/>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Industry(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_industry_pvt name="gasPurchase.bgfcl_industry_pvt"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Commercial(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_comm_gvt name="gasPurchase.bgfcl_comm_gvt"/>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Commercial(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_comm_pvt name="gasPurchase.bgfcl_comm_pvt"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Domestic-Meter(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_dom_meter_gvt name="gasPurchase.bgfcl_dom_meter_gvt"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Domestic-Meter(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_dom_meter_pvt name="gasPurchase.bgfcl_dom_meter_pvt"/>									
								
						  	</div>	
						 </div>
						 	 <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Domestic-Non-Meter(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_dom_nmeter_gvt name="gasPurchase.bgfcl_dom_nmeter_gvt"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Domestic-Non-Meter(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_dom_nmeter_pvt name="gasPurchase.bgfcl_dom_nmeter_pvt"/>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Fertilizer(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_fertilizer_gvt name="gasPurchase.bgfcl_fertilizer_gvt"/>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Fertilizer(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_fertilizer_pvt name="gasPurchase.bgfcl_fertilizer_pvt"/>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Tea-State(GVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_tea_gvt name="gasPurchase.bgfcl_tea_gvt"/>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12 extra">
								<label style="width: 30%">Tea-State(PVT)</label>	
								<input type="text" style="width: 65%"  id=bgfcl_tea_pvt name="gasPurchase.bgfcl_tea_pvt"/>									
								
						  	</div>	
						 </div>
				
						 
						 <div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
								<div id="aDiv" style="height: 0px;"></div>
						 </div>
				 	
				</div>
		 </div>
		</div>
	</div>
	<div style="width: 38%;height: 98%;float: left;margin-left: 15px;">			
		<div style="width: 100%;float: left;margin-top: 0px;">
			<%@ include file="GasPurchaseSummery.jsp" %>
		</div>
	</div>
	

</div>

</form>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/gasPurchase.js"></script>
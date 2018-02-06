<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("installmentBillingHome.action");
	setTitle("Installment Bill");
</script>
<style> 
input[type="checkbox"] {margin-top: -5px;} 
.ui-datepicker-calendar{display:none;}
.hor-minimalist-b th{
padding:4px 8px !important;
}
.hor-minimalist-b td{
padding:4px 8px !important;
}
</style>

<div style="width: 35%;height: 98%;float: left;" id="left_div">	
	<div class="row-fluid" style="height: 22%;">
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Customer Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">						
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 40%">Customer Code</label>
								<input type="text" onblur="checkInput(this.id)"  name="customer_id" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 9%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1"/>
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 9%;margin-top: -5px;"/>
						  	</div>	
						  	<div class="span6">									    
								<label style="width: 40%">Customer Type</label>
								<input type="text" id="isMetered_name" style="width: 52%"  value="" readonly="readonly"/>									      
							</div>
						 </div>
						 <div class="row-fluid">
							<div class="span6">
								<label style="width: 40%">Region/Area</label>	
								<select id="area_id"  style="width: 60%;" disabled="disabled">
									<option value="" selected="selected">Select Area</option>
									<s:iterator value="%{#session.USER_AREA}" id="areaList">
										<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
									</s:iterator>
								</select>									      
								
						  	</div>	
						  	<div class="span6">
								<label style="width: 40%">Category</label>	
								<select id="customer_category" style="width: 58%;" disabled="disabled">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>																	
						  	</div>
						 </div>

						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Customer Name</label>	
								<input type="text" style="width: 77%"  id="full_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>						 
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Mobile</label>	
								<input type="text" style="width: 77%"  id="mobile" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>

						 
				</div>
		 </div>
		</div>
	</div>
	
	<div class="row-fluid" style="height: 45%;" >
	   	 <div id="tabbed-nav" style="height: 100%">
            <ul>
                <li><a>Dues List</a></li>
                <li><a>History (<font color="green" style="font-weight: bold;">For this customer</font>)</a></li>
                <!--
                <li><a>History (<font color="#E42217" style="font-weight: bold;">All Customer</font>)</a></li>
                 -->
            </ul>
	         <div>
		         <div>
						<table id="dues_bill_grid" ></table>
						<div id="dues_bill_grid_pager" ></div>
		         </div>
		         <div>
		                <table id="installment_history_this_grid"></table>
						<div id="installment_history_this_grid_pager" ></div>
		         </div>
		         <!-- 
		         <div>
		                <table id="installment_history_all_grid"></table>
						<div id="installment_history_all_grid_pager" ></div>
						all customer
		         </div>
		          -->
			</div>
		</div>					
	</div>
	
	<div class="row-fluid" style="height: 30%;margin-top: 4px;" >
	   	 <div id="tabbed-nav-1" style="height: 100%;margin-top: 2px;">
            <ul>
                <li><a>Agreement</a></li>
                <li><a>Installment Bills</a></li>
            </ul>
	         <div>
		         <div>
						<form name="agreementForm" id="agreementForm" >	
							<div class="row-fluid" style="height: 27%;">
							   <div style="width: 100%;float: left;margin-top: 5px;">
									<div class="w-box">
										<div class="w-box-header">
						    				<h4 id="rightSpan_caption">Installment Agreement Information</h4>
										</div>
										<div class="w-box-content" style="padding: 10px;" id="content_div">								
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 29%">Installment Start From</label>
														<input type="text" style="width: 40%"   id="agreement_start_month"   name="agreement.startFrom" readonly="readonly"/>
												  	</div>	
												</div>	
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 29%">Total Installment</label>
														<input type="text" style="width: 40%"  id="total_installment"   name="agreement.totalInstallment" maxlength="2"/>
												  	</div>	
												</div>				
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 29%">Agreement Date</label>
														<input type="text" style="width: 40%" id="agreement_date"   name="agreement.agreementDate" readonly="readonly"/>
												  	</div>	
												</div>
												<div class="row-fluid">
													<div class="span12">
														<label style="width: 29%">Notes</label>	
														<textarea rows="1" cols="" style="width: 67%;" id="notes" name="agreement.notes"></textarea>
												  	</div>	
												  	
												</div>
												<div class="formSep" style="padding-top: 1px;padding-bottom: 2px;">
													<div id="aDiv" style="height: 0px;"></div>
												</div>
												<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;">									
													<button class="btn btn-beoro-3" type="button" id="btn_agreement_next" onclick="PrepareInstallments()" style="font-weight: bold;">													
													Next&nbsp;<span class="splashy-pagination_1_next"></span>
													</button>													
												</div> 
												 
										</div>
									</div>
								</div>
							</div>
							</form>
		         </div>
		         <div>
		                <table id="installment_bill_table" class="hor-minimalist-b" style="border:1px solid #C0C0C0;width: 100%;margin-left: 2px;" >  
					       <thead>  
					        	<tr>  
					            	<th scope="col" width="10%" style="text-align:center;">Bill Id</th>
					            	<th scope="col" width="15%" style="text-align:center;">Month</th>  
					            	<th scope="col" width="15%" style="text-align:center;">Year</th>
					                <th scope="col"  width="19%" style="text-align:right;">Bill</th> 
					                <th scope="col"  width="19%" style="text-align:right;">Collected</th>  
					                <th scope="col"  width="19%" style="text-align:right;">Due</th>   
					            </tr>  
					        </thead> 
					        <tbody>  
					        </tbody>
					       </table>
		         </div>
		         
			</div>
		</div>					
	</div>
	
	
</div>

<div style="width: 63%;height: 96%;float: left;margin-left: 10px;" id="right_div">
		<div class="row-fluid">
		
		</div>
		<div class="row-fluid">
		    <table id="installment_grid"></table>
			<div id="pager"></div>
		</div>
		<div class="row-fluid">
		<!-- 
		<button id="add_installment">Add Installment</button>
		&nbsp;&nbsp;&nbsp;&nbsp;
		 -->
		 <button class="btn btn-beoro-3" type="button"  id="save_installment" style="float: right;display: none;">
		 	<span class="splashy-document_letter_okay"></span>Save Installment
		 	</button>
		</div>

</div>
<script type="text/javascript">
 Calendar.setup($.extend(true, {}, calOptions,{
    inputField : "agreement_date",
    trigger    : "agreement_date"}));
    
    $('#agreement_start_month').datepicker(monthYearCalOptions);
</script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/installment.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/installmentTabPanel.js"></script>
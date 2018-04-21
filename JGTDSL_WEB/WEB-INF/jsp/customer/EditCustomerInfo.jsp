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
	<div style="width: 60%;height: 98%;float: left;">	
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Old Customer Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Customer Id</label>	
								<input type="text" id="customer_id" name="customer.customer_id"  style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 18%;margin-top: -4px;" value="<s:property value="customer.customer_id"/>" />
								<input type="text" name="" id="customer_id_x"  style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 18%;margin-top: -5px;"/>
									
								
						  	</div>
						  	<div class="span6 extra">
								<label style="width: 30%">Application SN</label>	
								<input type="text" style="width: 65%"  id=app_sl_no name="customer.app_sl_no" />									
								
						  	</div>		
						 </div>
						  <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Customer Name</label>	
								<input type="text" style="width: 65%"  id=full_name name="personal.full_name"/>									
								
						  	</div>	
						  	<div class="span6 extra">
								<label style="width: 30%">Freedom Fighter</label>	
								<select name="personal.freedom_fighter" id="freedom_fighter"  style="width: 65%;">
			                        	<option value="" selected="selected">Select Yes or No</option>
			                            <option value="Y" >Yes</option>
			                            <option value="N" >No</option>
			                    </select>								
								
						  	</div>		
						 </div>
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Father Name</label>	
								<input type="text" style="width: 65%"  name="personal.father_name" id="father_name" />									
								
						  	</div>	
						  	<div class="span6 extra">
								<label style="width: 30%">Mother Name</label>	
								<input type="text" style="width: 65%"  name="personal.mother_name" id="mother_name" />									
								
						  	</div>		
						 </div>
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Category</label>	
								<input type="text" style="width: 65%" name="customer.customer_category"  id="customer_category_name" disabled="disabled"/>									
								
						  	</div>	
						  	<div class="span6 extra">
								<label style="width: 30%">Area/Region</label>	
								<input type="text" style="width: 65%" name="customer.area"   id="area_name" disabled="disabled"/>								
								
						  	</div>		
						 </div>
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Gender</label>	
								<select name="personal.gender" id="gender"  style="width: 69.5%;">
			                        	<option value="" selected="selected">Select Gender</option>
			                            <option value="M" >Male</option>
			                            <option value="F" >Female</option>
			                            <option value="O" >Others</option>
			                    </select>								
								
						  	</div>	
						  	<div class="span6 extra">
								<label style="width: 30%">Email</label>	
								<input type="text" style="width: 65%"  name="personal.email" id="email" />									
								
						  	</div>		
						 </div>
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Phone</label>	
								<input type="text" style="width: 65%"  name="personal.phone" id="phone"/>									
								
						  	</div>	
						  	<div class="span6 extra">
								<label style="width: 30%">Mobile</label>	
								<input type="text" style="width: 65%"   name="personal.mobile" id="mobile"/>									
								
						  	</div>		
						 </div>
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Fax</label>	
								<input type="text" style="width: 65%"  name="personal.fax" id="fax" />									
								
						  	</div>	
						  	<div class="span6 extra">
								<label style="width: 30%">National ID</label>	
								<input type="text" style="width: 65%"  name="personal.national_id" id="national_id"/>									
								
						  	</div>		
						 </div>
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Passport</label>	
								<input type="text" style="width: 65%"  name="personal.passport_no" id="passport_no"  />									
								
						  	</div>	
						  	<div class="span6 extra">
			                    <label style="width: 30%">Ministry</label>	
			                    <select name="personal.ministry_id" id="ministry_id"  style="width: 65%;">
			                        <option value="" selected="selected">Select Ministry</option>
			                        <s:iterator value="%{#application.ALL_MINISTRY}" id="ministryList">
			                            <option value="<s:property value="ministry_id" />" >
			                                <s:property value="ministry_name" />
			                            </option>
			                        </s:iterator>
			                    </select>
						  	</div>		
						 </div>
						 
						 <div class="row-fluid">
							<div class="span6 extra">
								<label style="width: 30%">Business License</label>	
								<input type="text" style="width: 65%"  name="personal.license_number" id="license_number"  />									
								
						  	</div>	
						  	<div class="span6 extra">
								<label style="width: 30%">Vat Reg. Number</label>	
								<input type="text" style="width: 65%"  name="personal.vat_reg_no" id="vat_reg_no" />									
								
						  	</div>		
						 </div>
							
						 
						 <div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
								<div id="aDiv" style="height: 0px;"></div>
						 </div>


						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;" id="reading_button_div">	
 							<table width="100%" border="0">
							 	<tr>
							 		<td width="30%" align="left">
							 			
							 				
							 		</td>
							 		<td width="70%" align="right">
							    		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndSaveCustomerInfo()" id="btn_save"><span class="splashy-document_letter_okay"></span>Save</button> 
										<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>Close</button>
							 		</td>
							 	</tr>
 							</table>									        									
				 		</div>
				 	
				</div>
		 </div>
		</div>
	</div>
	
	<div style="width: 38%;height: 98%;float: left;margin-left: 15px;">	
	
		<div class="w-box" style="display: none;">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Customer Photo</h4>
				</div>
				<div class="w-box-content" id="content_div"  style="text-align: center;"> 
				<div class="row-fluid">
				<div class="span4"></div>
							<div class="span4" style="text-align: center;">
							<div style="width: 120px;height:150px;border: 1px solid black;"  id="customer_photo">                 				
           					</div><br/>
           					<input type="file" name="upload" id="upload" onchange="afu()" style="width: 98%;" />
							</div>
						<div class="span4"></div>
				</div>												
							
				</div>
		</div>
		
		<div style="width: 100%;float: left;margin-top: 0px;">
			<%@ include file="Address.jsp" %>
		</div>
	</div>
	

</div>

</form>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/editCustomerInfo.js"></script>
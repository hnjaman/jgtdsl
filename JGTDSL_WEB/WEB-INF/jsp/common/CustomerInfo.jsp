<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="bgCustomerInfo" name="bgCustomerInfo">
	<div class="span12" >
		<div class="w-box">
			<div class="w-box-header">
	  				<h4 id="rightSpan_caption">Customer Information</h4>
			</div>
			<div class="w-box-content" style="padding: 10px;" id="content_div">						
					<div class="row-fluid">
						<div class="span6">
							<label style="width: 40%">Customer ID</label>
							<input type="text"   onblur="checkInput(this.id)" id="comm_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 12%;margin-top: -4px;" value="<s:property value='customer_id' />" tabindex="1" />
							<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 12%;margin-top: -5px;"/>
					  	</div>
					  	<div class="span6">									    
							<label style="width: 40%">Customer Type</label>
							<input type="text" style="width: 51%"  id="comm_isMetered_name" disabled="disabled"/>								      
						</div>
					  	
					</div>
					<div class="row-fluid">							
						<div class="span6">									    
							<label style="width: 40%">Region/Area</label>
							<select id="comm_area_id"  style="width: 58.5%;" disabled="disabled">
								<option value="" selected="selected">Select Area</option>
								<s:iterator value="%{#application.ACTIVE_AREA}" id="categoryList">
									<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
							</s:iterator>
							</select>									      
						</div>
						<div class="span6">
							<label style="width: 40%">Category</label>
							<select id="comm_customer_category" style="width: 56%;" disabled="disabled">
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
							<input type="text" style="width: 76%"  id="comm_full_name" disabled="disabled"/>
						</div>
					</div>
					<div class="row-fluid" id="common_fh_row">							
						<div class="span12">									    
							<label style="width: 19.5%">F/H Name</label>
							<input type="text" style="width: 76%"  id="comm_father_name" disabled="disabled"/>
						</div>
					</div>
					
					<div class="row-fluid" id="common_address_row">							
						<div class="span12">									    
							<label style="width: 19.5%">Address</label>
							<textarea rows="1" style="width: 76%" id="comm_customer_address" disabled="disabled"></textarea>
						</div>
					</div>
				
					<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
						<div id="aDiv" style="height: 0px;"></div>
					</div>											
			</div>
		</div>
	</div>
</form>
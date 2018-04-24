<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="row-fluid">
	<div class="span6">
		<label class="mr_lable">Customer ID <m class='man'/></label>
		<input type="text" onblur="checkInput(this.id)"  name="reading.customer_id" id="customer_id" class="mr_1row_text"  style="font-weight: bold;color: #0000ff; z-index: 2; background: transparent;margin-top: -4px; width: 59%;" value="<s:property value='customer_id' />" tabindex="1"/>
		<!-- onblur="checkInput(this.id)"  -->
		<input type="text" name="" id="customer_id_x" disabled="disabled" class="mr_1row_text"  style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;margin-top: -5px;"/>
		
  	</div>
  	
  	
  	
	<div class="span6" id="areaCategory">									    
		
		<select id="area_id"  disabled="disabled" style="width: 30%;"> 
			<option value="" selected="selected">Select Area</option>
			<s:iterator value="%{#session.USER_AREA}" id="areaList">
				<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
		</s:iterator>
		</select>	
		<select id="customer_category"disabled="disabled" style="width: 30%;margin-left:8px;">
			<option value="" selected="selected">Select Category</option>
			<s:iterator value="%{#application.ACTIVE_METERED_CUSTOMER_CATEGORY}" id="categoryList">
				<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
			</s:iterator>
		</select>   								      
	</div>
</div>  

<div class="row-fluid">    
	<div class="span6">									    
		<label class="mr_lable">Customer Name</label>
		<input type="text" id="full_name" class="mr_customer_name_text" disabled="disabled" style="width: 59%"/>									      
	</div>
	<!--  
	<div class="span6">									    
		<label class="mr_lable">Customer Type</label>
		<input type="text" id="isMetered_name" class="mr_customer_type_text"  value="Meterred" disabled="disabled" style="width: 59%;"/>									      
	</div>
	-->
</div>

<div class="row-fluid">
	<div class="span12" style="padding-bottom: 3px;">
		<label class="mr_address_label">Address</label>
		<textarea id="customer_address" cols="" style="height: 36px;width: 80%;" class="mr_address_textarea"  disabled="disabled"></textarea>
	</div>
</div>

<div class="row-fluid">    
	<div class="span6">
		<label class="mr_lable">Meter No<m class='man'/></label>
		<select name="reading.meter_id" id="meter_id" class="mr_select" onchange="fetchReadingInfo(readingCallBack)"  style="width:62%;">
			<option value="" selected="selected">Select Meter</option>
			<s:iterator value="meterList">
				<option value="<s:property value="meter_id" />" ><s:property value="meter_id" /></option>
			</s:iterator>
		</select>
	</div>
   <div class="span6">
       <label class="mr_lable">Mtr. Rent</label>
       <input type="text" name="reading.meter_rent" id="meter_rent"  class="mr_text"  readonly="readonly" style="width: 59%;"/>
   </div>   
</div>


<!--  Billing Month should be removed  -->
 
<div class="row-fluid">    
   <div class="span6">
       <label class="mr_lable">Billing Month</label>
		<select name="reading.billing_month" id="billing_month" style="margin-left: 0px;width:31%;" class="mr_month"  disabled="disabled">
       	<option value="">Select Month</option>           
        <s:iterator  value="%{#application.MONTHS}">   
   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
		</s:iterator>
       </select>
       <select name="reading.billing_year" id="billing_year" style="margin-left: 5px;width:30%;" class="mr_year" disabled="disabled">
       	<option value="">Year</option>
       	<s:iterator  value="%{#application.YEARS}" id="year">
            <option value="<s:property/>"><s:property/></option>
		</s:iterator>
       </select>
   </div>   
   <div class="span6">
      <label class="mr_lable">Bill Purpose</label>
      <select class="mr_select" name="reading.reading_purpose_str" id="reading_purpose_str"  onchange="fetchReadingInfo(readingCallBack)" disabled="disabled" style="width:62%;">
        	<option value="">Select Bill Purpose</option>
        	<s:iterator  value="%{#application.READING_PURPOSE}">
            <s:if test='%{id!=0 && id!=1 && id!=4 && id!=5}'>   
   				<option value="<s:property value='id'/>"><s:property value="label"/></option>
   			</s:if>
		</s:iterator>            
      </select>
   </div>   
</div>	


								
<div class="row-fluid">      
   <div class="span6">
       <label class="mr_lable">Prev. Reading</label>
       <input type="text" name="reading.prev_reading" id="prev_reading" class="mr_text" disabled="disabled" style="width: 59%; font-weight: bold;color: #3b5894;  background: transparent;"/>									       
   </div>	 
   <div class="span6">
       <label class="mr_lable">P. Reading Date</label>
       <input type="text" name="reading.prev_reading_date" id="prev_reading_date"  class="mr_text" disabled="disabled" style="width: 59%;"/>
   </div>   
</div>	
								
<div class="row-fluid"> 
	<div class="span6">
       <label class="mr_lable">Current Reading</label>
       <input type="text" name="reading.curr_reading" id="curr_reading"  class="mr_text" onkeyUp="calculateDifference()" disabled="disabled" style="width: 59%;"/>
   </div>   
   <div class="span6">
       <label class="mr_lable">Disconn. Date</label>
       <input type="text" name="reading.curr_reading_date" id="curr_reading_date"  class="mr_text" disabled="disabled" style="width: 59%;"/>
       <input type="hidden" id="reading_date" value="" />
   </div>   
</div>

<div class="row-fluid">    
   <div class="span6">
       <label class="mr_lable">Difference</label>
       <input type="text" name="reading.difference" id="difference"  class="mr_text" disabled="disabled" style="width: 59%;"/>									       
   </div>
   <div class="span6">
       <label class="mr_lable">HHV/NHV(+/-)</label>
       <input type="text" name="reading.hhv_nhv" id="hhv_nhv"  class="mr_text"  style="width: 59%;"/>									       
   </div>
   

   
</div>

<div class="row-fluid"> 

	<div class="span6">
	       <label class="mr_lable">Pressure Factor</label>
	       <input type="text" name="reading.pressure_factor" id="pressure_factor"  class="mr_text" disabled="disabled" style="width: 59%;"/>									       
	   </div>
	
		<div class="span6">
	       <!--     <label class="mr_lable">Mtr. Type/Rate</label>      -->
	       <label class="mr_lable"></label>
	       <input type="hidden" name="reading.measurement_type_name" id="measurement_type_name"  class="mr_text1" disabled="disabled" style="width: 29%;"/>
	       <input type="hidden" name="reading.measurement_type_str" id="measurement_type_str" />
	       <input type="hidden" name="reading.rate" id="rate"  class="mr_text2" disabled="disabled" style="width: 25%;"/>
	       <input type="hidden" name="reading.tariff_id" id="tariff_id" />
	       <input type="hidden" name="reading.latest_tariff_id" id="latest_tariff_id" value=""/>
	       <input type="hidden" name="reading.latest_rate" id="latest_rate" />
	   	</div>   

</div>


<div class="row-fluid"> 
    							   
   <div class="span6">
   		<label class="mr_lable">Act./Total Cons.</label>
       <input type="text"  id="actual_consumption_t"   class="mr_text1" onchange="calculateTotalConsumption()" style="width: 29%;font-weight: bold;color: #3b5894; background: transparent;"/>       									      
       <input type="text" style="display:none; width:29%;font-weight: bold; background: transparent;"  name="reading.actual_consumption" id="actual_consumption"  >
       <input type="text"  id="total_consumption_t"   class="mr_text2"  style="width: 25%; font-weight: bold;color: #3b5894; background: transparent;"/>
		<input type="text" style="display:none"  name="reading.total_consumption" id="total_consumption"  >   
   </div>
   
   <div class="span6">
       <label class="mr_lable">Unit</label>
       <input type="text" name="reading.unit" id="unit"  class="mr_text1" disabled="disabled"  style="width: 29%;font-weight: bold;color: #3b5894;"/>       									      
   </div>
   
</div>

<div class="row-fluid">    
   <div class="span6">
       <label class="mr_lable">Min/Max Cons.</label>
       <input type="text" name="reading.min_load" id="min_load"  class="mr_text1" disabled="disabled" style="width: 29%;"/>
       <input type="text" name="reading.max_load" id="max_load"  class="mr_text2"  disabled="disabled" style="width: 25%;"/>
       <input type="hidden" name="reading.min_load_latest" id="min_load_latest"  class="mr_text1" disabled="disabled" style="width: 29%;"/>
       <input type="hidden" name="reading.max_load_latest" id="max_load_latest"  class="mr_text2"  disabled="disabled" style="width: 25%;"/>
   </div>
   
   <!-- 
   <div class="span6">
       <label class="mr_lable">Remarks</label>
       <input type="text" name="reading.remarks" id="remarks"  class="mr_textarea"  disabled="disabled" style="width: 59%;"/>									       
   </div>
    -->
</div>

<input type="hidden" name="reading.pressure" id="pressure" />
<input type="hidden" name="reading.temperature" id="temperature" />
<input type="hidden" name="reading.pressure_factor" id="pressure_factor" />
<input type="hidden" name="reading.temperature_factor" id="temperature_factor" />
<input type="hidden" name="reading.pressure_latest" id="pressure_latest" />
<input type="hidden" name="reading.temperature_latest" id="temperature_latest" />
<input type="hidden" name="reading.pressure_factor_latest" id="pressure_factor_latest" />
<input type="hidden" name="reading.temperature_factor_latest" id="temperature_factor_latest" />
<input type="hidden" name="reading.bill_id" id="bill_id" />
<input type="hidden" name="reading.reading_id" id="reading_id" />
<input type="hidden" id="mobile" />
<input type="hidden" id="connection_status" />
 

<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
	<div id="aDiv" style="height: 0px;"></div>
</div>

<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;" id="reading_button_div">	
 <table width="100%" border="0">
 	<tr>
 		<td width="30%" align="left">
 			<button class='btn btn-beoro-3' type='button' id='btn_prev' onclick="fetchReadingEntry('previous')">
 				<span class='splashy-arrow_large_left_outline'></span></button>
 				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 			<button class='btn btn-beoro-3' type='button' id='btn_next' onclick="fetchReadingEntry('next')">
 				<span class='splashy-arrow_large_right_outline'></span></button>
 			<button class='btn btn-beoro-3' type='button' id='btn_next' onclick="showShortCuts()">
 				<span class='splashy-sprocket_dark'></span></button>
 				
 		</td>
 		<td width="70%" align="right">
 			<button class="btn btn-beoro-3" type="button" id="btn_parameter"><span class="splashy-shield_star"></span>Change Parameter</button>
    		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="handleEnterKeyPress()" id="btn_save"><span class="splashy-document_letter_okay"></span>Save Reading</button> 
    		<button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="$dialog.dialog('open');" disabled="disabled"><span class="splashy-gem_remove"></span>Delete</button>	
			<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>Close</button>
 		</td>
 	</tr>
 </table>									    
    									
</div>

</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/meterReadingEntryForm.js"></script>					

<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sw-basic-step-4">
    <h4 class="StepTitle">Customer's Connection & Meter Information</h4>
    <div class="row-fluid">
        <div class="span6">
		    <form id="connectionForm" name="connectionForm">
		    <input type="hidden" name="customer.connectionInfo.customer_id" value="<s:property value='customer.customer_id' />" />
		    <input type="hidden" value="<s:property value="customer.customer_category_name"/>" id="customer_cat" />
            <div class="w-box">
                <div class="w-box-header">
                    <h4>Connection</h4>
                </div>
                <div class="w-box-content cnt_a">
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Ministry Ref.</label>
                            <s:if test="%{customer.connectionInfo.connection_type != null}">
	                            <s:property value="customer.connectionInfo.ministry_name"/>	                            
                            </s:if>                            
                            <s:else>
                             	<select name="customer.connectionInfo.ministry_id" id="ministry_id" style="width: 39%;">
	                                <option value="" selected="selected">N/A</option>
	                                <s:iterator value="%{#application.ALL_MINISTRY}" id="divisionList">
	                                    <option value="<s:property value="ministry_id" />" >
	                                        <s:property value="ministry_name" />
	                                    </option>
	                                </s:iterator>
	                            </select>
                            </s:else>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <label>
                                Connection Type
                                <m class='man'/>
                            </label>
                            
                            <s:if test="customer.connectionInfo.connection_type == null">
	                            <select name="customer.connectionInfo.connection_type_str" id="connection_type" style="width: 39%" onchange="checkConnectionType(this.value)">
						            <option value="">Select Type</option>           
							        <s:iterator  value="%{#application.CONNECTION_TYPE}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>       
				        		</select>
					        </s:if>
					        
                            <s:else>
                                 <s:property value="customer.connectionInfo.connection_type.label"/>
                            </s:else>
                            
                        </div>
                    </div>
                    
                    <s:if test="customer.connectionInfo.connection_type.label == @org.jgtdsl.enums.ConnectionType@SUB.label">
                    </s:if>
                    <div class="row-fluid" id="parent_connection_div">
	                        <div class="span12">
	                            <label>Parent Connection<m class='man'/><font style="font-size: 8px">(cond.)</font></label>
	                            <s:if test="%{customer.connectionInfo.parent_connection == null || customer.connectionInfo.parent_connection == ''}">
	                            	<input type="text" id="parent_connection" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 17%;margin-top: -4px;" name="customer.connectionInfo.parent_connection"/>
									<input type="text" name="" id="parent_connection_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 17%;margin-top: -5px;"/>	                            
	                            </s:if>
	                            <s:else>
	                            	<s:property value="customer.connectionInfo.parent_connection"/>
	                            </s:else>	                            
	                        </div>
	                </div>
	                
					
                    <div class="row-fluid">
                        <div class="span12">
                        <label>Metered Status<m class='man'/></label>
                          
                        <s:if test="%{customer.connectionInfo.isMetered == null}">
                        	<select name="customer.connectionInfo.isMetered_str" id="isMetered" style="width: 39%" onchange="changeMeteredStatus(this.value)">
					            <option value="">Select Status</option>           
						        <s:iterator  value="%{#application.METERED_STATUS}">  <%--startupResources --%>
						   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
								</s:iterator>       
				        	</select>
                        </s:if>
                        <s:else>
                        	<s:property value="customer.connectionInfo.isMetered.label"/>
                        </s:else>
				        
                        </div>
                    </div>
                    <div class="row-fluid" id="max_min_load_div">
                        <div class="span12">
                            <label>Approved Min/Max Load<m class='man'/><font style="font-size: 8px">(cond.)</font></label>
                            <s:if test="%{customer.connectionInfo.min_load == null || customer.connectionInfo.min_load == ''}">
	                            	<input type="text" name="customer.connectionInfo.min_load" id="min_load" style="width: 12%" />
                            </s:if>
                            <s:else>
                            	<input type="text" name="customer.connectionInfo.min_load" id="min_load" style="width: 12%" value="<s:property value='customer.connectionInfo.min_load' />" readonly="readonly"/>
                            </s:else>
                            
							<input type="text" style="width: 4%;color: green;" value="min" disabled="disabled"/>
							
							<s:if test="%{customer.connectionInfo.max_load == null || customer.connectionInfo.max_load == ''}">
	                            <input type="text" name="customer.connectionInfo.max_load" id="max_load" style="width: 12%"/>
                            </s:if>
                            <s:else>
                            	<input type="text" name="customer.connectionInfo.max_load" id="max_load" style="width: 12%" value="<s:property value='customer.connectionInfo.max_load' />" readonly="readonly"/>
                            </s:else>
                            
							<input type="text" style="width: 5%;color: red;" value="max" disabled="disabled" />							                       
                        </div>
                    </div>          

	                    <div class="row-fluid" id="appliance_info_div">
	                        <div class="span12">
	                            
	                            <s:if test="%{(customer.connectionInfo.single_burner_qnt == null || customer.connectionInfo.single_burner_qnt == '') && (customer.connectionInfo.double_burner_qnt == null || customer.connectionInfo.double_burner_qnt == '')}">
		                            	<!-- <input type="text" name="customer.connectionInfo.single_burner_qnt" id="single_burner_qnt" style="width: 8%" />
										<input type="text" style="width: 7%;color: green;" value="single" disabled="disabled"/>
										<input type="text" name="customer.connectionInfo.double_burner_qnt" id="double_burner_qnt" style="width: 8%"/>
										<input type="text" style="width: 8%;color: red;" value="double" disabled="disabled" /> -->
								<label>Appliance Info.<m class='man'/><font style="font-size: 8px">(cond.)</font></label>
										<select name="customer.connectionInfo.appliance_id" id="appliance_list" style="width: 20%;">
			                                <option value="" selected="selected">N/A</option>
			                                <s:iterator value="%{#session.ALL_APPLIANCE}" id="appliance_list">
			                                    <option value="<s:property value="applianc_id" />">
			                                        <s:property value="applianc_name"/>
			                                    </option>
			                                </s:iterator>
	                            		</select>
	                            		&nbsp;&nbsp;
	                            		<input type="text" name="customer.connectionInfo.appliance_qnt" id="appliance_qnt" style="width: 8%"/>
										&nbsp;&nbsp;&nbsp;
										<button type="button" id="btnInsert" class="btn btn-success" style=>Add</button>
	                            		<input type="hidden" name="customer.connectionInfo.appliance_info_str" id="appliance_info_str" style="width: 8%"/>
	                            </s:if>
	                            <s:else>
	                            	
	                            </s:else>	                            
	                        </div>
	                    </div>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Connection Date<m class='man'/></label>                                                        
                         	<s:if test="%{customer.connectionInfo.connection_date != null && customer.connectionInfo.connection_date != ''}">
   								<s:property value="customer.connectionInfo.connection_date"/>
   								<input type="hidden" name="customer.connectionInfo.connection_date" value="<s:property value='customer.connectionInfo.connection_date' />" />
							</s:if>
							<s:else>
   								<input type="text" name="customer.connectionInfo.connection_date" id="connection_date" style="width: 37%"/>
   								
							</s:else>
                        </div>
                    </div>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            <label>Connection Status<m class='man'/></label>
                            <s:if test="%{customer.connectionInfo.status_name == 'Newly Applied'}">
	                            <select name="customer.connectionInfo.status_str" id="status_id" style="width: 39%">  
						            	<option value="">Select Status</option>              
							   			<option value="1">Connected</option>
						        </select>
					        </s:if>					       
					        <s:if test="%{customer.connectionInfo.status_name != 'Newly Applied'}">					        
					         	<s:if test="customer.connectionInfo.status.label==@org.jgtdsl.enums.ConnectionStatus@DISCONNECTED.label">
					         		<span id="span_connection"><span class="label label-info">Disconnected</span></span>
					         		<input type="hidden" name="customer.connectionInfo.status_str" value="<s:property value='@org.jgtdsl.enums.ConnectionStatus@DISCONNECTED.id' />" />
					         		
					         	</s:if>
					         	<s:if test="customer.connectionInfo.status.label==@org.jgtdsl.enums.ConnectionStatus@CONNECTED.label">
					         		<span id="span_connection"><span class="label label-important">Connected</span></span>
					         		<input type="hidden" name="customer.connectionInfo.status_str" value="<s:property value='@org.jgtdsl.enums.ConnectionStatus@CONNECTED.id' />" />	
					         		
					           </s:if>				         	
					        </s:if>
                        </div>
                    </div>
                    <div class="row-fluid" id="vat_rebate_div">
                        <div class="span12">
                        <label>VAT Rebate(%)</label>
                        <s:if test="%{customer.connectionInfo.connection_type != null}">
                        	<s:property value="customer.connectionInfo.vat_rebate"/>
                        </s:if>
                        <s:else>
                        	<input type="text" name="customer.connectionInfo.vat_rebate" id="vat_rebate" maxlength="2" style="width: 37%"/>
                        </s:else>
                        </div>
                    </div>
                    <div class="row-fluid" id="hhv_nhv_div">
                        <div class="span12">
	                        <label>HHV/NHV Factor<m class='man'/></label>
	                        <s:if test="%{customer.connectionInfo.connection_type != null}">
                        	<s:property value="customer.connectionInfo.hhv_nhv"/>
                        </s:if>
                        <s:else>
                        	<input type="text" name="customer.connectionInfo.hhv_nhv" id="hhv_nhv" maxlength="8" style="width: 37%"/>
                        </s:else>
                        
                        </div>
                    </div>
                  
                  <div class="row-fluid" id="pay_within_div">
                        <div class="span12">
                            <label>Pay Within(No. of Days)<m class='man'/></label>
                            <s:if test="%{customer.connectionInfo.min_load == null || customer.connectionInfo.min_load == ''}">
	                            	<input type="text" name="customer.connectionInfo.pay_within_wo_sc" id="pay_within_wo_sc" value="" style="width: 5%" />
                            </s:if>
                            <s:else>
                            	   <input type="text" name="customer.connectionInfo.pay_within_wo_sc" id="pay_within_wo_sc" style="width: 5%" value="<s:property value='customer.connectionInfo.pay_within_wo_sc' />" readonly="readonly"/>
                            </s:else>
                            
							<input type="text" style="width: 11.5%;color: green;font-size: 12px;" value="Without SC" disabled="disabled"/>
							
							<s:if test="%{customer.connectionInfo.max_load == null || customer.connectionInfo.max_load == ''}">
	                            <input type="text" name="customer.connectionInfo.pay_within_w_sc" id="pay_within_w_sc" value="" style="width: 5%"/>
                            </s:if>
                            <s:else>
                            	<input type="text" name="customer.connectionInfo.pay_within_w_sc" id="pay_within_w_sc" style="width: 5%" value="<s:property value='customer.connectionInfo.pay_within_w_sc' />" readonly="readonly"/>
                            </s:else>
                            
							<input type="text" style="width: 8.5%;color: red;font-size: 12px;" value="With SC" disabled="disabled" />							                       
                        </div>
                     
                    </div>
                      
                 
                    <div class="row-fluid">
                        <div class="span11">
                        	<label></label>	                        	
                        	<s:if test="%{customer.connectionInfo.status_name == 'Newly Applied' && customer.connectionInfo.connection_date == null}">
								<button class="btn btn-beoro-3" type="button" style="margin-left: 20px;" id="btn_save" onclick="submitConnectionInfo()">Save Changes</button>								
							</s:if>
                    	</div>                    
                    </div>  
                    
                </div>
            </div>
            </form>
        </div>
        <div class="span6" id="non_meter_appliance_info_div" style="display:none">
            <div class="w-box">
                <div class="w-box-header">
                    <h4>Appliance Info</h4>                    
                </div>                
                <div class="w-box-content cnt_a">                                       
                    <div class="row-fluid">
                        <div class="span12" id="non_meter_div">
                        	<table id="tblDetails" class="table table-bordered" >
            <thead>
                <tr>
                    <th>
                        App.ID
                    </th>
                    <th>
                        App.Name
                    </th>
                    <th>
                        App.Qnt
                    </th>
                    <th>
                        (-)
                    </th>
                </tr>
            </thead>
            <tbody id="tblBody"><thead></thead></tbody>
        </table>
                    	</div>                    	
                    </div>                                        
                </div>
            </div>
        </div>
        
        <s:if test="customer.connectionInfo.isMetered.label==@org.jgtdsl.enums.MeteredStatus@NONMETERED.label">
        <div class="span6" id="non_meter_info_div">
            <div class="w-box">
                <div class="w-box-header">
                    <h4>Appliance List</h4>                    
                </div>                
                <div class="w-box-content cnt_a">                                       
                    <div class="row-fluid">
                        <div class="span12" id="nonmeter_div">
                    	</div>                    	
                    </div>                                        
                </div>
            </div>
        </div>
        
        <script type="text/javascript">
        	ajaxLoad("nonmeter_div","getApplianceInfo.action?customer_id=<s:property value='customer.customer_id' />");
        </script>
      
      </s:if>
      
      



 
      
      
      <s:if test="customer.connectionInfo.isMetered.label==@org.jgtdsl.enums.MeteredStatus@METERED.label">
        <div class="span6" id="meter_info_div">
            <div class="w-box">
                <div class="w-box-header">
                    <h4>Meter List</h4>                    
                </div>                
                <div class="w-box-content cnt_a">                                       
                    <div class="row-fluid">
                        <div class="span12" id="meter_div">
                    	</div>                    	
                    </div>                                        
                </div>
            </div>
        </div>
        
        <script type="text/javascript">
        	ajaxLoad("meter_div","getMeterInfo.action?customer_id=<s:property value='customer.customer_id' />");
        </script>
      
      </s:if>
    </div>
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/customerConnection.js"></script>
<script type="text/javascript">

<s:if test="%{customer.connectionInfo.connection_date == null || customer.connectionInfo.connection_date == ''}">
	  Calendar.setup({
        inputField : "connection_date",
        trigger    : "connection_date",
		eventName : "focus",
        onSelect   : function() { this.hide() },
        showTime   : 12,
        dateFormat : "%d-%m-%Y",
		showTime : true
      });
</s:if>      

      /*
      Calendar.setup({
        inputField : "installation_date",
        trigger    : "installation_date",
		eventName : "focus",
        onSelect   : function() { this.hide() },
        showTime   : 12,
        dateFormat : "%d-%m-%Y",
		showTime : false
      });
      */
      
function checkConnectionType(connectionType)
{
 if(connectionType==0)
   $("#parent_connection_div").hide();
 else
   $("#parent_connection_div").show();
}      

<s:if test='%{customer.connectionInfo.connection_type==null}'>
	if($("#parent_connection_div"))
		$("#parent_connection_div").hide();                    
</s:if>

<s:if test='%{customer.connectionInfo.connection_type.label=="Main-Connection"}'>
	$("#parent_connection_div").hide();                    
</s:if>


<s:if test="%{customer.connectionInfo.isMetered == null}">
  if($("#max_min_load_div"))
  	$("#max_min_load_div").hide();
  if($("#appliance_info_div"))
  	$("#appliance_info_div").hide();
  $("#appliance_info_div").hide();	  
  	
</s:if>
<s:if test="customer.connectionInfo.isMetered.label == @org.jgtdsl.enums.MeteredStatus@METERED.label">
 $("#max_min_load_div").show();
 $("#appliance_info_div").hide();
 $("#non_meter_appliance_info_div").hide();
 
 
  $("#vat_rebate_div").show();
 $("#hhv_nhv_div").show();
 $("#pay_within_div").show();
 
</s:if>
<s:if test="customer.connectionInfo.isMetered.label == @org.jgtdsl.enums.MeteredStatus@NONMETERED.label">
 $("#appliance_info_div").hide();
 $("#max_min_load_div").hide();
 $("#non_meter_appliance_info_div").hide();
 $("#vat_rebate_div").hide();
 $("#hhv_nhv_div").hide();
 $("#pay_within_div").hide();
</s:if>


function changeMeteredStatus(meteredStatus)
{

 if(meteredStatus==0){
  $("#appliance_info_div").show();
  $("#non_meter_appliance_info_div").show();
  $("#max_min_load_div").hide();
  $("#meter_info_div").hide();  
  
  $("#vat_rebate_div").hide();
  $("#hhv_nhv_div").hide();
  $("#pay_within_div").hide();
  }
 else if(meteredStatus==1){
  $("#appliance_info_div").hide();
  $("#non_meter_appliance_info_div").hide();
  $("#max_min_load_div").show();
  $("#meter_info_div").show();
  
  $("#vat_rebate_div").show();
  $("#hhv_nhv_div").show();
  $("#pay_within_div").show();
  getDefaultSurchargePayWithin();
  
  
  ajaxLoad("meter_div","getMeterInfo.action?customer_id=<s:property value='customer.customer_id' />");
  
  }
}

function getDefaultSurchargePayWithin()
{
  var customer_cat=$("#customer_cat").val();
  
  	$.ajax({
	    url: 'getDefaultSurchargePayWithin.action',
	    type: 'POST',
	    data: {customer_categoty:customer_cat},
	    cache: false,
	    success: function (response) {
	    	$("#pay_within_wo_sc").val(response.pay_within_wo_sc_range_default);
  			$("#pay_within_w_sc").val(response.pay_within_w_sc_range_default);
	    	
	    }
	    
	  });
  
}

function submitConnectionInfo()
{


  var isValid=true;
/*  if($("#ministry_id").length && $.trim($("#ministry_id").val())=="")
  	{cbColor($("#ministry_id"),"e");isValid=false;}
  else cbColor($("#ministry_id"),"v");
*/

//meter 
 if($("#isMetered").length && $("#isMetered").val()==1){
	  if($("#min_load") && $.trim($("#min_load").val())=="")
	  	{cbColor($("#min_load"),"e");isValid=false;}
	  else cbColor($("#min_load"),"v");
	  if($("#max_load") && $.trim($("#max_load").val())=="")
	  	{cbColor($("#max_load"),"e");isValid=false;}
	  else cbColor($("#max_load"),"v");
	  if($("#hhv_nhv") && $.trim($("#hhv_nhv").val())=="")
	  	{cbColor($("#hhv_nhv"),"e");isValid=false;}
	  else cbColor($("#hhv_nhv"),"v");
	  if($("#pay_within_wo_sc") && $.trim($("#pay_within_wo_sc").val())=="")
	  	{cbColor($("#pay_within_wo_sc"),"e");isValid=false;}
	  else cbColor($("#pay_within_wo_sc"),"v");
  	  if($("#pay_within_w_sc") && $.trim($("#pay_within_w_sc").val())=="")
	  	{cbColor($("#pay_within_w_sc"),"e");isValid=false;}
	  else cbColor($("#pay_within_w_sc"),"v"); 
	  
	   if(isValid==true)
  			$('#connectionForm').submit();
	  
  }
//end of meter







	if($("#tblBody tr").size()== 0){
		cbColor($("#appliance_list"),"e");
		cbColor($("#appliance_qnt"),"e");
		
		isValid=false;
  	}else{
  	  isValid=true;
  	}
	
	if($("#appliance_qnt").val()!=""){
	  	cbColor($("#appliance_list"),"v");
	  	cbColor($("#btnInsert"),"e");
	  	isValid=false;
  	}
  	

  if($("#connection_type").length && $.trim($("#connection_type").val())=="")
  	{cbColor($("#connection_type"),"e");isValid=false;}
  else cbColor($("#connection_type"),"v");
  if($("#connection_date").val()=="")
  	{cbColor($("#connection_date"),"e");isValid=false;}
  if($("#connection_type").val()==1 && $("#parent_connection").length && $.trim($("#parent_connection").val())=="")
  	{cbColor($("#parent_connection"),"e");isValid=false;}
  else cbColor($("#parent_connection"),"v");
  if($("#isMetered").length && $.trim($("#isMetered").val())=="")
  	{cbColor($("#isMetered"),"e");isValid=false;}
  else cbColor($("#isMetered"),"v");
 
//     alert(isValid);

	  if(isValid==true)
  			$('#connectionForm').submit();
}

$('#connectionForm').unbind("submit");

$("#connectionForm").submit(function(event){
	disableButton("btn_save");
  event.preventDefault();
  var formData = new FormData($(this)[0]);
 
  $.ajax({
    url: 'saveConnectionInfo.action',
    type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response) {

	  if(response.status=="OK")
	  	enableButton("btn_save");
      	callAction('viewCustomer.action?customer_id=<s:property value="customer.customer_id" />&selected=3');
      	
      $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, {
	                    zIndex: 1500,
	                    width:450,
	                     beforeOpen: centerInfoDialog,
	                     afterOpen:disableOnClick,
	                     onClose: function () {
	                    	$('.ui-widget-overlay').unbind( "click" );
	                    	//var customer_id=$("#customer_id").val();
							//var actionUrl=sBase+"getSecurityAndOtherDepositList.action?customer_id="+customer_id;
							//$("#depositListTbl").html(jsImg.SETTING).load(actionUrl);	
							//resetDepositForm();          
							          	
	                        return true; // allow closing
	                    }
	                    
    });
    
    },
    error: function (response) {
		       enableButton("btn_save");
		      }
    
  });
 
  return false;
});
//ajaxLoad('meter_div','newMeter.action?customer_id=<s:property value="customer.customer_id" />')
</script>
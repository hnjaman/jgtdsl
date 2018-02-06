<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/JGTDSL_WEB/resources/thirdParty/smart-wizard/styles/smart_wizard.css">
<script src="/JGTDSL_WEB/resources/thirdParty/smart-wizard/js/jquery.smartWizard.js"></script>
<script src="/JGTDSL_WEB/resources/thirdParty/jTPS/jTPS.js"></script>
<script  type="text/javascript">
	setTitle("Customer Information");
</script>
<link href="/JGTDSL_WEB/resources/thirdParty/jTPS/jTPS.css" rel="stylesheet" />

<style type="text/css">
#newCustomerForm{height: 100%;}
.w-box{height: 100% !important;}
.row-fluid{height: 100% !important;}
.span12{height: 92% !important;}
.swMain{height: 93% !important;width: 99% !important;}
.swMain .stepContainer{height: 92% !important;}	
.swMain div.actionBar {
    margin: -18px 0 0 !important;
}
.swMain .msgBox{
min-width: 45% !important;
margin: 0px !important;
}
.swMain{
margin-top: 10px;
}
.alert{
padding-bottom: 0px !important;
}
.w-box-header{
 background: linear-gradient(to bottom, #f7fbfc 0%, #d9edf2 40%, #add9e4 100%) repeat scroll 0 0 rgba(0, 0, 0, 0) !important;
    color: #004b5e !important;
    text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1) !important;
    border: 1px solid #add9e4 !important;
}    
label{
    display: inline-block !important;
    float: left !important;
    clear: left !important;
    width: 40%;
    text-align: left !important;
}
input[type=text] {
  display: inline-block !important;
  float: left !important;
  border: 1px solid #add9e4;
}
select {
  display: inline-block !important;
  float: left !important;
  border: 1px solid #add9e4;
  color: #333 !important;
}
.row-fluid + .row-fluid {
    margin-top: 6px !important;
}
select, textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input {
    border-radius: 0px !important;
}
#customer_wizard {
width: 99.9% !important;
}
</style>
<!-- wizard -->
<div class="row-fluid" id="customer_wizard" style="display: none;">
                    <div class="span12" style="width: 99.5%;">
<div class="w-box">
                            <div class="w-box-header" style='padding-bottom:4px;'>
                                <!--<h4>Customer Information</h4>-->
                                 <!-- <div style="text-align: right;margin-top:-25px;"> -->
                                                <table  style="width: 100%;" >
											      <tr>
											        <td>
											        <div style='float:left;'>
											          <font style="font-weight:bold;">Customer Code</font>
											             &nbsp;&nbsp;&nbsp;
											            <input type="text" value="<s:property value="customer.customer_id"/>" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; width: 12%;margin-top: 0px;" id="customer_id" autocomplete="off">
														<input type="text" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 12%;margin-top: -5px;" disabled="disabled" id="customer_id_x" name="">
											          </div>
											          <span style='float:left;margin-left:265px;color:#80002a;font-weight:bold;' id='div_full_name'></span>
											          <span style='float:left;margin-left:300px;font-weight:bold;' >Contract No:<span id='div_phone' style='color:#80002a;font-weight:bold;'></span></span>
											          <span style='float:left;margin-left:200px;font-weight:bold;' id='div_connection'>Connection Status:
											               <s:if test="%{customer.connectionInfo.status_name == 'Newly Applied'}">
											               <span id="span_connection" style='color:#ada811;font-weight:bold;'>Not Yet Connected</span>
											               </s:if>
											               <s:if test="%{customer.connectionInfo.status_name != 'Newly Applied'}">					        
													         	<s:if test="customer.connectionInfo.status.label==@org.jgtdsl.enums.ConnectionStatus@DISCONNECTED.label">
													         		<span id="span_connection" style='color:#FF0000;font-weight:bold;'>Disconnected</span>
													         		<input type="hidden" name="customer.connectionInfo.status_str" value="<s:property value='@org.jgtdsl.enums.ConnectionStatus@DISCONNECTED.id' />" />
												         		
													         	</s:if>
													         	<s:if test="customer.connectionInfo.status.label==@org.jgtdsl.enums.ConnectionStatus@CONNECTED.label">
													         		<span id="span_connection" style='color:#006400;font-weight:bold;'>Connected</span>
													         		<input type="hidden" name="customer.connectionInfo.status_str" value="<s:property value='@org.jgtdsl.enums.ConnectionStatus@CONNECTED.id' />" />	
													         		
													           </s:if>				         	
					        							</s:if>
											          </span>
											        </td>
											     </tr>
											  </table>
								<!-- </div> -->
                            </div>
                            <div class="w-box-content" style="height: 103%;">
                                <div class="row-fluid">
                                    <div class="span12">
                                        <div id="wizard" class="swMain">
                                            <ul>
                                                <li>
                                                    <a href="#sw-basic-step-1">
                                                        <span class="stepNumber">1</span>
                                                        <span class="stepDesc">
                                                           Step 1<small>Personal Information</small>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#sw-basic-step-2">
                                                        <span class="stepNumber">2</span>
                                                        <span class="stepDesc">
                                                           Step 2<small>Address Information</small>
                                                        </span>
                                                    </a>
                                                </li>
                                                <s:if test="#session.role=='Super Admin' || #session.role=='Manager' || #session.role=='Assistant Manager'">
                                                <li>
                                                    <a href="#sw-basic-step-3">
                                                        <span class="stepNumber">3</span>
                                                        <span class="stepDesc">
                                                           Step 3 title<small>Security & Other Deposit</small>
                                                        </span>
                                                    </a>
                                                </li>
                                                <li>
                                                    <a href="#sw-basic-step-4">
                                                        <span class="stepNumber">4</span>
                                                        <span class="stepDesc">
                                                           Step 4<small>Connection & Meter</small>
                                                        </span>
                                                     </a>
                                                </li>
                                                <li>
                                                    <a href="#sw-basic-step-5">
                                                        <span class="stepNumber">5</span>
                                                        <span class="stepDesc">
                                                           Step 5<small>Customer's Account</small>
                                                        </span>
                                                     </a>
                                                </li>
                                                <li>
                                                
                                                </li>
                                                </s:if>
                                            </ul>
                                           			
                                            <%@ include file="PersonalInfo.jsp" %>
                                            <%@ include file="AddressInfo.jsp" %>
                                            <%@ include file="../SecurityAndOtherDeposit.jsp" %>
                                            <%@ include file="../ConnectionAndMeterInfo.jsp" %>
                                            <%@ include file="CustomerAccount.jsp" %>
                                                                                        
                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>	
              		</div>
              	</div>
                       		
<!-- wizard end -->
			
<script type="text/javascript">    			
function showAStepCallback(obj, context) {
//	alert(context.toStep);
if(context.toStep==5)
 scrollToRow("#customer_ledger_grid"); 
}
		        
$(document).ready(function(){
	$("#customer_wizard").show();	
	var currentSelectSteps="<c:out value='${param.selected}' />";
	if(currentSelectSteps== null || currentSelectSteps=="")
	 currentSelectSteps=step;
	//<c:if test="${not empty param.selected}">,selected:<c:out value="${param.selected}" /></c:if>, 
	$('#wizard').smartWizard({selected:currentSelectSteps,transitionEffect:'slide',enableFinishButton:true,onShowStep: showAStepCallback});
	$("#div_full_name").html($("#span_full_name").html());
	$("#div_phone").html($("#span_phone").html());
	//$("#div_connection").html($("#span_connection").html());
	
	
	//transitionEffect: 'fade', // Effect on navigation, none/fade/slide/slideleft
	//https://github.com/mstratman/jQuery-Smart-Wizard
});
</script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/customerView.js"></script>
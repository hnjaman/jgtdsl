<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sw-basic-step-1" style="overflow: auto;">
    <h4 class="StepTitle">Customer's Personal Information</h4>
    <div class="row-fluid">
        <div class="span8">
            <div class="row-fluid">
                <div class="span6">
                    <label>
                        Customer Id                        
                    </label>
                    <b>:</b>
                    <!-- 
                    <a href="#" onclick="callAction('viewCustomer.action?customer_id=<s:property value="customer.customer_id"/>')"><s:property value="customer.customer_id"/></a>
                    -->
                    &nbsp;
                    <s:property value="customer.customer_id"/>
                    <!-- 
                    <input type="text" id="customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 12%;margin-top: -4px;" value="<s:property value="customer.customer_id"/>" />                    
					<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 12%;margin-top: -5px;"/>
					 -->
					 <input type="hidden" id="isDefauleter" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 12%;margin-top: -4px;" value="<s:property value="isDedaulterOrNot"/>" />
                </div>
                <div class="span6">
                    <label>
                        Application Serial No.
                    </label>
                    <b>:</b> <s:property value="customer.app_sl_no"/>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6">
                    <label>
                        Customer Category
                    </label>
                    <b>:</b> <s:property value="customer.customer_category_name"/>
                </div>
                <div class="span6">
                    <label>
                        Region/Area
                    </label>
                    <b>:</b> <s:property value="customer.area_name"/> (<s:property value="customer.zone_name"/>)
                </div>
                
            </div>
            <div class="row-fluid">                
                <div class="span6">
                    <label>Organization Name</label>                    
                    <b>:</b> <s:property value="customer.personalInfo.organization_name"/>
                    
                </div>
                <div class="span6" id="div_proprietor" style="display: none;">
                    <label>Proprietor Name</label>
                    <b>:</b> <s:property value="customer.personalInfo.proprietor_name"/>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6">
                    <label>Full Name</label>
                    <b>:</b> <span id='span_full_name'><s:property value="customer.personalInfo.full_name"/></span>
                </div>
                <div class="span6">
                    <label>Gender</label>
                    <b>:</b> <s:property value="customer.personalInfo.gender"/>
                </div>
            </div>
        </div>
        <div class="span4">
            <div class="row-fluid" style="display: true;">
                <div style="width: 110px;height:137px;border: 1px solid black;float: left;" class="span6" id="customer_photo">
                	<!-- <img id='img_photo' src="http://localhost/JGTDSL_PHOTO/customer/<s:property value='customer.customer_id'/>.jpg" /> -->
                	<img id='img_photo' />
                </div>
                 <div style="float: left;margin-top: 125px;display: none;">
							<p id='info'></p>
							<div id='outer_container' class="outer_container" >
								<a class="menu_button" href="#" title="Toggle"><span>Menu Toggle</span></a>
								<ul class="menu_option">								  
								  <li><a href="#" id="pie_load" onclick="pieMenuCall('new_meter')"><span class="pie_load">Item</span></a></li>
								  <li><a href="#" id="pie_reconnect" onclick="pieMenuCall('new_meter')"><span class="pie_reconnect">Item</span></a></li>
								  <li><a href="#" id="pie_disconnect" onclick="pieMenuCall('disconnect')"><span class="pie_disconnect">Item</span></a></li>
								  <li><a href="#" id="pie_burner" onclick="pieMenuCall('reconnect')"><span class="pie_burner">Item</span></a></li>
								  <li><a href="#" id="pie_meter" onclick="pieMenuCall('burner_change')"><span class="pie_meter">Item</span></a></li>
								  <!-- 
								  <li><a href="#" onclick="pieMenuCall('load_change')"><span style="background: url('/JGTDSL_WEB/resources/thirdParty/pieMenu/img/customer.png') no-repeat scroll center center / 16px 16px transparent;">Item</span></a></li>
								   -->
								</ul>
							</div>				
						</div>                
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4">
            <label>
                Father's Name                
            </label>
            <b>:</b> <s:property value="customer.personalInfo.father_name"/>
        </div>
        <div class="span4">
            <label>
                Mother's Name
            </label>
            <b>:</b> <s:property value="customer.personalInfo.mother_name"/>
        </div>
        <div class="span4">
            <label></label>                                                 	
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4">
            <label>Email</label>
            <b>:</b> <s:property value="customer.personalInfo.email"/>
        </div>
        <div class="span4">
            <label>Phone</label>
            <b>:</b> <span id='span_phone'><s:property value="customer.personalInfo.phone"/></span>
        </div>
        <div class="span4">
            <label>Mobile</label>
            <b>:</b> <s:property value="customer.personalInfo.mobile"/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4">
            <label>Fax</label>
            <b>:</b> <s:property value="customer.personalInfo.fax"/>
        </div>
        <div class="span4">
            <label>National Id</label>
            <b>:</b> <s:property value="customer.personalInfo.national_id"/>
        </div>
        <div class="span4">
            <label>Passport</label>
            <b>:</b> <s:property value="customer.personalInfo.passport_no"/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4" id="div_vat" style="display: none;">
            <label>Vat Reg. Number</label>
            <b>:</b> <s:property value="personal.vat_reg_no"/>
        </div>
        <div class="span4" id="div_bl" style="display: none;">
            <label>Business License</label>
            <b>:</b> <s:property value="customer.personalInfo.license_number"/>
        </div>
        <div class="span4" id="div_freedom_fighter" style="display: none;">
            <label>Freedom Fighter</label>
            <b>:</b> <s:property value="customer.personalInfo.freedom_fighter"/>
        </div>
        
    </div>
   
        
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/pieMenu.js"></script>
<script type="text/javascript">
//check this
$('#img_photo').safeUrl({wanted:"http://localhost/JGTDSL_PHOTO/customer/<s:property value='customer.customer_id'/>.jpg",rm:"http://localhost/JGTDSL_PHOTO/anon_user.png"});

$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){
    	var step=$('#wizard').smartWizard('currentStep')-1;
    	
    	saveBankInformation();
    	
    	callActionExtended('viewCustomer.action?customer_id='+$('#customer_id').val(),step);}
}));

$("#parent_connection").unbind();
$("#parent_connection").autocomplete($.extend(true, {}, acMCustomerOption,{
		serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){
    	//callAction('viewCustomer.action?customer_id='+$('#customer_id').val());
    	}
}));


<s:if test="customer.customer_category=='01'">
	$("#div_freedom_fighter").show();
</s:if>

<s:if test="customer.customer_category!='01' && customer.customer_category!='02'">
	$("#div_proprietor").show();
	$("#div_bl").show();
	$("#div_vat").show();
</s:if>
</script>
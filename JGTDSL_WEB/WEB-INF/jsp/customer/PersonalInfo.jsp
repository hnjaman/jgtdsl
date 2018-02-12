<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sw-basic-step-1" style="overflow: auto;">
    <h4 class="StepTitle">Customer's Personal Information</h4>
    <div class="row-fluid">
        <div class="span8">
            <div class="row-fluid">
                <div class="span6">
                    <label>
                        Region/Area
                        <m class='man'/>
                    </label>
                    <select name="customer.area" id="area_id"  style="width: 53.5%;" onchange="setArea(this.value);fetchSelectBox(zone_sbox);">
                        <option value="" selected="selected">Select Area</option>
                        <s:iterator value="%{#session.USER_AREA}" id="categoryList">
                            <option value="<s:property value="area_id" />" >
                                <s:property value="area_name" />
                            </option>
                        </s:iterator>
                    </select>
                </div>
                <div class="span6">
                    <label>
                        Customer Category
                        <m class='man'/>
                    </label>
                    <select name="customer.customer_category" id="customer_category"  style="width: 53.5%;" onchange="setCategory(this.value);">
                        <option value="" selected="selected">Select Category</option>
                        <s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
                            <option value="<s:property value="category_id" />" >
                                <s:property value="category_name" />
                            </option>
                        </s:iterator>
                    </select>
                    
                </div>                
            </div>
            <div class="row-fluid">
                <div class="span6">
                    <label>Moholla</label>
                    <select id="zone"  style="width: 53.5%;">
                        <option value="" selected="selected">Select Moholla</option>
                    </select>
                </div>
                <div class="span6">                    
                </div>
            </div>
            <div class="row-fluid">                
                <div class="span6">
                    <label>
                        Customer ID
                        <m class='man'/>
                    </label>
                    <input type="text" id="id_area" style="border: 1px solid #add9e4;width: 4%;font-weight: bold;margin-right: 10px;color: blue;" readonly="readonly" />
                    <input type="text" id="id_category" style="border: 1px solid #add9e4;width: 4%;font-weight: bold;margin-right: 10px;color: blue;" readonly="readonly" />
                    <input type="text" id="id_code" maxlength="7" style="border: 1px solid #add9e4;width: 30%;;font-weight: bold;color: blue;" />
                    <input type="hidden" name="customer.customer_id" id="customer_id" value="" />
                </div>
                <div class="span6">
                    <label>Application SN</label>
                    <input type="text" id=application_sn name="customer.app_sl_no" maxlength="100" style="width: 50%;"/>
                </div>
            </div>
            <div class="row-fluid">                
                <div class="span6">
                    <label>
                        Organization Name
                    </label>
                    <input type="text" id=organization_name name="personal.organization_name" maxlength="100" style="width: 50%;"/>
                </div>
                <div class="span6" id="div_proprietor" style="display: none;">
                    <label>
                        Proprietor Name
                    </label>
                    <input type="text" id=proprietor_name name="personal.proprietor_name" maxlength="100" style="width: 50%;"/>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6">
                    <label>
                        Full Name
                        <m class='man'/>
                    </label>
                    <input type="text" id=full_name name="personal.full_name" maxlength="100" style="width: 50%;"/>
                </div>
                <div class="span6">
                    <label>
                        Gender
                        <m class='man'/>
                    </label>
                    <select name="personal.gender" id="gender"  style="width: 53.5%;">
                        <option value="" selected="selected">Select Gender</option>
                            <option value="Male" >Male</option>
                            <option value="Female" >Female</option>
                            <option value="Others" >Others</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="span4">
            <div class="row-fluid" style="display: true;">
                <div style="width: 120px;height:150px;border: 1px solid black;" class="span6" id="customer_photo">  
                              
                </div>
                <div class="span6" style="padding-left: 5px;">
                    <input type="file" name="upload" id="upload" onchange="afu()" style="width: 98%;" />
                    <input type="button" name="reset" value="Clear" style="width:80px;" />
                </div>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4">
            <label>
                Father's Name
                <m class='man'/>
            </label>
            <input type="text" name="personal.father_name" id="father_name" maxlength="100" style="width: 50%;"/>
        </div>
        <div class="span4">
            <label>
                Mother's Name
                <m class='man'/>
            </label>
            <input type="text" name="personal.mother_name" id="mother_name" maxlength="100" style="width: 50%;"/>
        </div>
        <div class="span4" id="div_freedom_fighter" style="display: none;">
            <label>
                Freedom Fighter
                <m class='man'/>
            </label>
            <div style="width: 20%;float:left;">
            <input type="radio" value="Y" name="personal.freedom_fighter" />
            <div style="margin-left:20px;margin-top:-15px;">Yes</div>
            </div>
            <div style="width: 20%;float:left;">
            <input type="radio" value="N" name="personal.freedom_fighter" checked="checked" />
            <div style="margin-left:20px;margin-top:-15px;">No</div>
            </div>                                                 	
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4">
            <label>Email</label>
            <input type="text" name="personal.email" id="email"  maxlength="50" style="width: 50%;"/>
        </div>
        <div class="span4">
            <label>Phone</label>
            <input type="text" name="personal.phone" id="phone" maxlength="50" style="width: 50%;"/>
        </div>
        <div class="span4">
            <label>
                Mobile
                <m class='man'/>
            </label>
            <input type="text" name="personal.mobile" id="mobile" maxlength="11" style="width: 50%;"/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4">
                  	<label style="color: black;">Application Date</label>
                  	<input type="text" name="customer.app_date" id="application_date" style="width: 54%;""/>
                  </div>
        <div class="span4">
            <label>Fax</label>
            <input type="text" name="personal.fax" id="fax" maxlength="25" style="width: 50%;"/>
        </div>
        <div class="span4">
            <label>TIN No.</label>
            <input type="text" name="personal.tin" id="tin" maxlength="30" style="width: 50%;"/>
        </div>
        <div class="span4" id="div_nid" style="display: none;"> 
            <label>National ID</label>
            <input type="text" name="personal.national_id" id="national_id" maxlength="15" style="width: 50%;"/>
        </div>
        <div class="span4" id="div_bl" style="display: none;">
            <label>Business License</label>
            <input type="text" name="personal.license_number" id="license_number" maxlength="15" style="width: 50%;"/>
        </div>
        
        <div class="span4" id="div_passport" style="display: none;">
            <label>Passport</label>
            <input type="text" name="personal.passport_no" id="passport_no" maxlength="15" style="width: 50%;"/>
        </div>
        <div class="span4" id="div_vat" style="display: none;">
            <label>Vat Reg. Number</label>
            <input type="text" name="personal.vat_reg_no" id="vat_reg_no" maxlength="15" style="width: 50%;"/>
        </div>
    </div>
    <div class="row-fluid">
    <div class="span4">
    <!--<input type="button" onclick="setTestData()" value="Fill Test Data" />-->
    </div>
    </div>
    <!-- 
    <div class="row-fluid">
        <div class="span4">
            <label>Business License</label>
            <input type="text" name="personal.license_number" id="license_number" maxlength="15" />
        </div>
        <div class="span4">
            <label>TIN</label>
            <input type="text" name="personal.tin" id="tin" maxlength="15" />
        </div>
        <div class="span4">
            <label></label>
            <input type="button" onclick="setTestData()" value="Fill Test Data" />
        </div>
    </div>
     -->
</div>

<script type="text/javascript">
function numaVal(){
	if(frm.personal.mobile.value==""){
		alert("Phone Number Should Not Be Empty");
		frm.phone.focus();
		return false;
	}
	
	return true;
}
</script>
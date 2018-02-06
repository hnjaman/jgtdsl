<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
input[type="text"] {
    float: none !important;
    margin-bottom:0px !important;
}
.formSep{
	padding-bottom:0px !important;
}
</style>

<div class="row-fluid">
                    <div class="span3">                      
                    </div>
                    <div class="span6" style="margin-top: 5px;">
                        <div class="w-box">
                            <div class="w-box-header"><h4>User Account Information</h4></div>
                            <div class="w-box-content cnt_a" style="text-align: center;">
                                <form action="downloadDemandNote.action">
                                <div class="row-fluid">
                                    <div class="span12">
                                        <p class="formSep"><label class="muted">UserId : User Name</label> <input type="text" id="userId" style="width: 24%" value="<s:property value="%{#session.user.userId}" />" readonly="readonly"/> <input type="text" id="userName" name="user.userName" style="width: 23%" value="<s:property value="%{#session.user.userName}" />"/></p>
                                        <p class="formSep"><label class="muted">Area : Division</label> <input type="text" id="area_name" style="width: 24%" value="<s:property value="%{#session.user.area_name}" />" readonly="readonly"/> <input type="text" id="division_id" style="width: 23%" value="<s:property value="%{#session.user.org_division_name}" />" readonly="readonly"/></p>
										<p class="formSep"><label class="muted">Department : Section</label> 
                                        	<input type="text" id="department_id" style="width: 24%" value="<s:property value="%{#session.user.department_name}" />" readonly="readonly"/>
                                        	<input type="text" id="section_id" style="width: 23%" value="<s:property value="%{#session.user.section_name}" />" readonly="readonly"/>  
										</p>
                                        <p class="formSep"><label class="muted">Division : District</label> 
                                        	<input type="text" id="department_id" style="width: 24%" value="<s:property value="%{#session.user.division_name}" />" readonly="readonly"/>
                                        	<input type="text" id="section_id" style="width: 23%" value="<s:property value="%{#session.user.district_name}" />" readonly="readonly"/>  
										</p>
										<p class="formSep"><label class="muted">Upazila:</label> <input type="text" id="designation" name="user.designation_name" style="width: 50%" value="<s:property value="%{#session.user.upazila_name}" />" readonly="readonly" /></p>
                                        <p class="formSep"><label class="muted">Designation:</label> <input type="text" id="designation" name="user.designation_name" style="width: 50%" value="<s:property value="%{#session.user.designation_name}" />" readonly="readonly" /></p>
                                        <p class="formSep"><label class="muted">Mobile:</label> <input type="text" id="mobile" name="user.mobile" style="width: 50%" value="<s:property value="%{#session.user.mobile}" />"/></p>
                                        <p class="formSep"><label class="muted">Email:</label> <input type="text" id="email_address" name="user.email_address" style="width: 50%" value="<s:property value="%{#session.user.email_address}" />"/></p>
                                    </div>
                                </div>
                                 <div class="formSep">
								 	<div id="aDiv" style="height: 20px;"></div>
								 </div>
								<div class="formSep sepH_b">    
								    <span id="wait_div" style="float: left;margin-top: 6px;font-size: 13px;color: red;padding-left: 15px;"></span>
								    <button class="btn btn-beoro-3" type="button" onclick="updateAccountInfo()" id="btn_save">Update Info</button>
								    <button class="btn btn-danger" onclick="callAction('blankPage.action')">Cancel</button> 								      
								</div>
                                
								</form>
                            </div>
                        </div>
                    </div>
                    <div class="span3">
                    </div>
                    
                </div>
                
<script type="text/javascript">
function updateAccountInfo()
{
	$("#wait_div").html(jsImg.LOADING);
 	var formData = new FormData($('form')[0]);
		  $.ajax({
		    url: 'updateAccountInfo.action',
		    type: 'POST',
		    data: formData,
		    async: true,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		    			$("#wait_div").html(response.message);
		    },
		    error: function (response) {$("#wait_div").html("");}		    
		  });
}
</script>
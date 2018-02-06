<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
$.jStorage.set("JGTDSL_action","changePasswordHome.action");
</script>
<style type="text/css">
input[type="text"] {
    float: none !important;
    margin-bottom:0px !important;
}
.formSep{
	padding-bottom:0px !important;
}
</style>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/passwordChange.js"></script>
<div class="row-fluid">
                    <div class="span3">                      
                    </div>
                    <div class="span6" style="margin-top: 50px;">
                        <div class="w-box">
                            <div class="w-box-header"><h4>User Password Change</h4></div>
                            <div class="w-box-content cnt_a" style="text-align: center;">
                                <form action="downloadDemandNote.action">
                                <div class="row-fluid">
                                    <div class="span12">
                                        <p class="formSep"><label class="muted">Old Password:</label> <input type="text" id="old_password" name="user.old_password" style="width: 50%"/></p>
                                        <p class="formSep"><label class="muted">New Password:</label> <input type="text" id="password" name="user.password" style="width: 50%"/></p>
                                        <p class="formSep"><label class="muted">Confirm New Password:</label> <input type="text" id="confirm_password" name="user.confirm_password" style="width: 50%"/></p>
                                    </div>
                                </div>
                                 <div class="formSep">
								 	<div id="aDiv" style="height: 20px;"></div>
								 </div>
								<div class="formSep sepH_b">    
								    <span id="wait_div" style="float: left;margin-top: 6px;font-size: 13px;color: red;padding-left: 15px;"></span>
								    <button class="btn btn-beoro-3" type="button" onclick="changePassword('secondary')" id="btn_save">Change Password</button>
								    <button class="btn btn-danger" onclick="callAction('blankPage.action')">Cancel</button> 								      
								</div>
                                
								</form>
                            </div>
                        </div>
                        	<div class="alert alert-error" style="margin-top: 10px;">
                                    <strong>Password </strong> should contain the following combinations :
                                    <ul>
                                    	<li>At least one Upper case Character</li>
                                    	<li>At least one Lower case Character</li>
                                    	<li>At least one Number</li>
                                    	<li>Minimum 6 character or Maximum 15 long.</li>
                                    </ul>
                            </div>

                    </div>
                    <div class="span3">
                    </div>
                    
                </div>
                

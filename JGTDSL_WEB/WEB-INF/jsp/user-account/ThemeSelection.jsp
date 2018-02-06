<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("themeSelection.action");
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

<div class="row-fluid">
                    <div class="span3">                      
                    </div>
                    <div class="span6" style="margin-top: 50px;">
                        <div class="w-box">
                            <div class="w-box-header"><h4>Theme Selection</h4></div>
                            <div class="w-box-content cnt_a" style="text-align: center;">
                                <form>
                                <div class="row-fluid">
                                    <div class="span12">
                                        <p class="formSep"><label class="muted">Default Theme:</label> 
                                        <select style="width:50%;" name="user.default_url" id="default_url">
                                        	<option value="">Select a Theme</option>
                                        	<option value="userDashBoard.action?theme=smoothness" <s:if test="#session.user.default_url == 'userDashBoard.action?theme=smoothness'">selected='selected'</s:if>>Smoothness</option>
                                        	<option value="userDashBoard.action?theme=redmond" <s:if test="#session.user.default_url == 'userDashBoard.action?theme=redmond'">selected='selected'</s:if>>Redmond</option>
                                        	<option value="userDashBoard.action?theme=cupertino" <s:if test="#session.user.default_url == 'userDashBoard.action?theme=cupertino'">selected='selected'</s:if>>Cupertion</option>
                                        	<option value="userDashBoard.action?theme=flick" <s:if test="#session.user.default_url == 'userDashBoard.action?theme=flick'">selected='selected'</s:if>>Flick</option>
                                        	
                                        </select>
                                        </p>
                                    </div>
                                </div>
                                 <div class="formSep">
								 	<div id="aDiv" style="height: 20px;"></div>
								 </div>
								<div class="formSep sepH_b">    
								    <span id="wait_div" style="float: left;margin-top: 6px;font-size: 13px;color: red;padding-left: 15px;"></span>
								    <button class="btn btn-beoro-3" type="button" onclick="changeTheme()" id="btn_save">Update Default Theme</button>
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
function changeTheme()
{
    var default_theme=$("#default_url").val();
	$("#wait_div").html(jsImg.LOADING);
 	var formData = new FormData($('form')[0]);
 	
		  $.ajax({
		    url: 'changeTheme.action',
		    type: 'POST',
		    data: formData,
		    async: true,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		    			$("#wait_div").html(response.message);
		    			window.location=default_theme+"&action=themeSelection";
		    },
		    error: function (response) {$("#wait_div").html("");}		    
		  });
}
</script>
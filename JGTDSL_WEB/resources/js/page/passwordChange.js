function changePassword(changeType)
{
	$("#wait_div").html(jsImg.LOADING);
 	var formData = new FormData($('form')[0]);
		  $.ajax({
		    url: 'changePassword.action',
		    type: 'POST',
		    data: formData,
		    async: true,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		    		if(response.status=="OK")
		    			handleRedirect(changeType,response.message);
		    		else if(response.status=="ERROR"){}
		    			$("#wait_div").html(response.message);
		    },
		    error: function (response) {$("#wait_div").html("");}		    
		  });
}

function handleRedirect(changeType,message)
{
	if(changeType=='primary')
		window.location='userDashBoard.action?theme=cupertino';
	else
		clearPasswordChangeForm(message);	
}

function clearPasswordChangeForm(message)
{
	 clearField('old_password','password','confirm_password');
	 $("#wait_div").html(message)
}


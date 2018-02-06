<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--[if lt IE 7 ]> <html lang="en" class="ie6 ielt8"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="ie7 ielt8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="ie8"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!--> <html lang="en"> <!--<![endif]-->
<head>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/constants.js"></script>
<meta charset="utf-8">
<title>JGTDSL-Password Change Form</title>
<link href="/JGTDSL_WEB/resources/css/password_change.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery-latest.js"></script>
</head>
<body>
<div class="container">
	<section id="content">
		<form action="" name="passwordChangeForm">
			<h1>Change Password</h1>
			<div style="display: none;">
				<input type="text" placeholder="Username" id="username" name="user.userId" value="<s:property value="user.userId" />" />
			</div>
			<div>
				<input type="password" placeholder="Old Password" id="old_password" name="user.old_password" class="password" value="<s:property value='user.old_password'/>" />
			</div>
			<div>
				<input type="password" placeholder="New Password"  id="password" name="user.password" class="password" />
			</div>
			<div>
				<input type="password" placeholder="Confirm New Password" id="confirm_password"  name="user.confirm_password" class="password" />
			</div>
			<div>
			    <div style="width: 48%;float: left;">
				<input type="button" value="Change Password"  onclick="changePassword('primary')"/>
				</div>
				<div style="width: 46%;float: left;margin-top: 28px;font-size: 10px;color: red;padding-left: 15px;" id="wait_div"></div>
			</div>
		</form><!-- form -->
		<div class="button" style="display: none;">
			<a href="#">Download source file</a>
		</div><!-- button -->
	</section><!-- content -->
	
	<center>
	<div class="alert alert-error" style="margin-top: 20px;width: 400px;text-align: left;">
		<strong>Password </strong> should contain the following combinations :
	    <ul>
	       	<li>At least one Upper case Character</li>
	       	<li>At least one Lower case Character</li>
	       	<li>At least one Number</li>
	       	<li>Minimum 6 character or Maximum 15 long.</li>
	    </ul>
	</div>
	</center>
	
</div><!-- container -->
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/passwordChange.js"></script>
</body>
</html>
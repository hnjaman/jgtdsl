<%@ taglib prefix="s" uri="/struts-tags"%>
<div style="color: red;min-height: 200px;padding: 20px;">
	<center>
		<img src="/JGTDSL_WEB/resources/images/info.png" width="80" height="80"/><br/><br/>
		<div id="msg_span"><s:property value="msg"/></div>
	</center>
</div>
<script type="text/javascript">
setPanelCaption("rightSpan_caption",jsCaption.CAPTION_INFORMATION);
$('#msg_span').addClass('animated bounceInLeft');
hideMeterInfo();

//$('#msg_span').addClass('animated zoomInDown');
/*
   			window.setTimeout( function(){
                   $("#msg_span").removeClass('animated bounceInLeft');
               }, 1000); 
*/               
</script>
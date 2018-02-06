<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>

<head>
	<title>JGTDSL</title>
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery-latest.js"></script>
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery.blockUI.js"></script>	
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/bootstrap.js"></script>
	
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/beoro-common.js"></script>
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/jquery.actual.js"></script>
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/jquery.easing.1.3.min.js"></script>	
	
	<!--  Zozo ACcordion -->
	<link href="/JGTDSL_WEB/resources/thirdParty/zozoAccordion/css/font-awesome.css" rel="stylesheet" />
    <link href="/JGTDSL_WEB/resources/thirdParty/zozoAccordion/css/zozo.accordion.css" rel="stylesheet" />
    <!--[if lt IE 9]><script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
    <script src="/JGTDSL_WEB/resources/thirdParty/zozoAccordion/js/jquery.easing.min.js"></script>
    <script src="/JGTDSL_WEB/resources/thirdParty/zozoAccordion/js/zozo.accordion.js"></script>
    
    <link href="/JGTDSL_WEB/resources/thirdParty/zozoTabs/css/zozo.tabs.min.css" rel="stylesheet" />
    <script src="/JGTDSL_WEB/resources/thirdParty/zozoTabs/js/zozo.tabs.min.js"></script>	
	
	
	<link href="/JGTDSL_WEB/resources/css/beoro.css" rel="stylesheet" type="text/css" />
	<link href="/JGTDSL_WEB/resources/css/bootstrap.css" rel="stylesheet" type="text/css" />
	<link href="/JGTDSL_WEB/resources/css/bootstrap-responsive.css" rel="stylesheet" type="text/css" />
	<style type="text/css"> 		
		@import "/JGTDSL_WEB/resources/css/template/AccordionMenu.css";
	</style> 	
	<link href="/JGTDSL_WEB/resources/css/icsw2_32/icsw2_32.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="/JGTDSL_WEB/resources/thirdParty/pieMenu/css/piemenu.css" />
	<script src="/JGTDSL_WEB/resources/thirdParty/pieMenu/js/jquery.menu.js"></script>
	
	<link rel="stylesheet" href="/JGTDSL_WEB/resources/thirdParty/poshytip-1.2/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/poshytip-1.2/jquery.poshytip.js"></script>
		 
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/constants.js"></script>	 
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/common.js"></script>
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/utils.js"></script>
	<!--  Placed jquery-ui-latest.js here to make the button style perfect  -->
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/template/jquery-ui-latest.js"></script>
	<script type="text/javascript">
		
	var appLayout; // a var is required because this page utilizes: appLayout.allowOverflow() method
	$(document).ready(function () {
		appLayout = $('body').layout({
			// enable showOverflow on west-pane so popups will overlap north pane
			west__showOverflowOnHover: false,
			west__size:	260,
			south__spacing_open:0,
			north__spacing_open:0,
			east__initClosed: true,
			north: {
                    onclose_end: function () {
                        //alert('close North');
                        
                    },
                    onopen_end: function () {
                        //alert('open North');                        
                        //appLayout.disableClosable('north',true);
                        //$(".ui-layout-resizer-north").css("display","block");
                        
                        setInterval(function () {$("#northPanel").css( "zIndex", 99 );}, 1000);
                    },
                    spacing_open:0
                }
		//,	west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
		});
		appLayout.disableSlidable('north');
		appLayout.disableResizable('north');
		appLayout.disableClosable('north', true);
		
		appLayout.disableSlidable('south');
		appLayout.disableResizable('south');
		appLayout.disableClosable('south', true);
			
		appLayout.disableSlidable('east');
		appLayout.disableResizable('east');
		appLayout.disableClosable('east', true);
		
		//$.getScript('/JGTDSL_WEB/resources/js/page/constants.js');
		var searchParams = {
				width: jsVar.SEARCH_WINDOW_WIDTH,
				closeOnEscape: true,
				multipleSearch: true,
				closeAfterSearch: true,
				beforeShowSearch: function($form) {
				    $(".searchFilter table td .input-elm").attr('style','width:100px');
				    return true;
				 },
			 	afterRedraw: function($form) {
			   		$(".searchFilter table td .input-elm").attr('style','width:100px');
			   	return true;
			 	}
		}
		var deleteParms = {
				reloadAfterSubmit:true,
				jqModal:true,
				closeOnEscape:true,
				width:450,
				caption:jqCaption.DELETE_CONFIRMATION,
				afterSubmit: jqGridDataDeleteResponseHandler
		}				

		var editParams = { 
				recreateForm: true,
				closeAfterEdit:true,
				savekey: [true,13],
				mtype:'POST',				
				afterShowForm:function(frm) {disableOnClick();}, 
				afterSubmit: jqGridDataPostResponseHandler,
				errorTextFormat: formatErrorText,
				bottominfo: jsEnum.FORM_BOTTOM_INFO
		}
		
		$.extend($.jgrid.nav, {view: true,closeOnEscape:true});	
		$.extend($.jgrid.edit, editParams);					
		$.extend($.jgrid.view, { recreateForm: true });
		$.extend($.jgrid.search, searchParams);
		$.extend($.jgrid.del, deleteParms);

 	});

	</script>
	<style type="text/css">
	
.activityDiv ul {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.activityDiv ul li {
    display: table-row;
    height: 30px;
    font-size: 13px;
}    
.activityDiv ul li i {  
    margin: 2px 0 5px;
    padding: 0 4px 0 0;
    vertical-align: middle;
}
.z-accordion.vertical > .z-section > .z-header {
    font-size: 14px !important;
}
</style>	
	
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/jqGrid/grid.locale-en.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/jqGrid/jquery.jqGrid.min.js"></script>	
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/jqGridDialog.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/attrchange/attrchange.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/ajaxFileUpload/ajaxfileupload.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/jqGrid/ui.jqgrid.css" rel="stylesheet" type="text/css" />	
<% String theme=(String)request.getParameter("theme");%>
<link href="/JGTDSL_WEB/resources/css/themes/<%=theme%>/jquery-ui.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="/JGTDSL_WEB/resources/css/template/listGroup.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	.ui-widget-overlay{opacity:0.5 !important;}
</style>

<script src="/JGTDSL_WEB/resources/thirdParty/jsCal2/js/jscal2.js"></script>
<script src="/JGTDSL_WEB/resources/thirdParty/jsCal2/js/lang/en.js"></script>
<link rel="stylesheet" type="text/css" href="/JGTDSL_WEB/resources/thirdParty/jsCal2/css/jscal2.css" />
<link rel="stylesheet" type="text/css" href="/JGTDSL_WEB/resources/thirdParty/jsCal2/css/border-radius.css" />
<link rel="stylesheet" type="text/css" href="/JGTDSL_WEB/resources/thirdParty/jsCal2/css/steel/steel.css" />

<link rel="stylesheet" type="text/css" href="/JGTDSL_WEB/resources/thirdParty/chosen/chosen.css" />
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/chosen/chosen.jquery.js"></script>
<link rel="stylesheet" type="text/css" href="/JGTDSL_WEB/resources/css/animate/animate.css" />
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/autoComplete/jquery.autocomplete.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/autoComplete/styles.css" rel="stylesheet" />
<link href="/JGTDSL_WEB/resources/css/template/common.css" rel="stylesheet" type="text/css" />
<link href="/JGTDSL_WEB/resources/css/themes-ext/<%=theme%>/style.css" rel="stylesheet" type="text/css"/>
<link href="/JGTDSL_WEB/resources/images/splashy/splashy.css" rel="stylesheet" />

<script src="/JGTDSL_WEB/resources/thirdParty/jsonViewer/jquery.json-view.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/jsonViewer/jquery.json-view.css" rel="stylesheet" />
<link rel="shortcut icon" type="image/x-icon" href="/JGTDSL_WEB/resources/images/favicon.ico"/>
</head>
<body>

<%@ include file="../template/Header.jsp" %>


   <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
   <div class="ui-layout-west" style="padding:0px;overflow:hidden;">
		<!-- <div id="westPanelHeader">Activity</div> -->
		<%@ include file="../template/LeftNavigator.jsp" %>
	</div>

	<%@ include file="../template/Footer.jsp" %>
	
	<%@ include file="../template/RightPanel.jsp" %>
	
	<%@ include file="../template/CenterPanel.jsp" %>
	
	
	<script type="text/javascript">	
	$("#contentPanel").html(ajax_load).load("dashBoardContent.action");	
	</script>	
	<!--
	<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/jquerySessionTimeout/jquery.sessionTimeout.js"></script>
	-->
	<script src="/JGTDSL_WEB/resources/thirdParty/json2/json2.js"></script>
	<script src="/JGTDSL_WEB/resources/thirdParty/jStorage/jstorage.js"></script>
	<script src="/JGTDSL_WEB/resources/thirdParty/jqueryHotkeys/jquery.hotkeys.js"></script>
	

	
	
    
    <script type="text/javascript">        
        $(document).ready(function () {           
			$("#menu-accordion").zozoAccordion({
            theme: theme.<%=theme%>,
            //active: 0,
            sectionSpacing: 4,
            headerSize: 39,
            showIcons: true,
			sectionSpacing: 0,
			rememberState:true
        });	
        $("#northPanel").css("z-index", "1");	
        });
        //Which section is current open when page is loaded. Setting active to false (active: false) will collapse all sections. 
		//Setting active to a number (active: 1), it will open section based on the given index. Starting from zero. The third options is to use array (active:[0,1,3]) which means you can open multiple sections on page load.

    </script> 
	<script type="text/javascript">	
	
		jsVar.LOCAL_ACTION_STORAGE=jsVar.LOCAL_ACTION_STORAGE+"_"+'<s:property value="%{#session.user.userId}" />';
	    var value = $.jStorage.get(jsVar.LOCAL_ACTION_STORAGE);
		if(!value){
		    $.jStorage.set(jsVar.LOCAL_ACTION_STORAGE,value);
		}
		else
			callAction(value);
		
		//var action="<%=request.getParameter("action")%>";
		//if(action!="null")
		//	callAction(action);
		
	</script>
	<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/test.js"></script>
	<%@ include file="../security/SessionTimeOut.jsp" %>
</body>
</html>
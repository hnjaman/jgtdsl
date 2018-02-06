<div class="ui-layout-north" id="northPanel" style="padding: 0px;">
	<div class="ui-jqgrid-titlebar ui-jqgrid-caption ui-widget-header ui-corner-top ui-helper-clearfix" style="padding-left: 0px;">
	<img src="/JGTDSL_WEB/resources/images/banner1.png" style="float: left; height: 40px"/>
	<img src="/JGTDSL_WEB/resources/images/arrow1.png"  style="float: left;padding-top: 5px;padding-left: 5px;"> 
	<div id="page_title"  style="float: left;padding-top: 10px;padding-left: 4px;font-size: 20px;">User Home</div>
	
		<div  style="float: right;padding-top: 15px;font-size: 12px;padding-right: 10px;color: #004b5e;text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);font-weight: normal;font-size: 25px">
			<div style="float: left;"><b>Area : </b></div>
			<div style="float: left;">
			<s:if test="%{#session.USER_AREA_LIST.size()>1}">
			   <select id="user_area_id" autocomplete="off"  onchange="switchUserArea(this.value)">
					<s:iterator value="%{#session.USER_AREA_LIST}" id="areaList">
						<option value="<s:property value="area_id" />"  <s:if test="%{#session.user.area_id == area_id}">selected</s:if>> <s:property value="area_name" /> </option>
					</s:iterator>
				</select>	
			</s:if>
			<s:else>
			   <s:property value="%{#session.user.area_name}" />
			</s:else>
			</div>
			, 
			<b>Role :</b> <s:property value="%{#session.user.role_name}" />
			
			
			&nbsp;&nbsp;|&nbsp;&nbsp; 
			<img src="/JGTDSL_WEB/resources/images/user_24.png"/> <s:property value="%{#session.user.userName}" />&nbsp;&nbsp;|&nbsp;&nbsp; 
			<a href="logout.action"><img src="/JGTDSL_WEB/resources/images/logout.png" width="19" /></a>
		</div>		 		
	</div>
</div>
<!-- 
<div class="dropdown" style="float: right;">
	  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" style="padding: 1px 6px">
	    <span class="icon-cog"></span>
	  </button>
	  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
	    <li role="presentation"><a role="menuitem" tabindex="-1" href="dashBoard.action?theme=smoothness">Smoothness</a></li>
	    <li role="presentation"><a role="menuitem" tabindex="-1" href="dashBoard.action?theme=cupertino">Cupertino</a></li>
	    <li role="presentation"><a role="menuitem" tabindex="-1" href="dashBoard.action?theme=redmond">Redmond</a></li>			    
	    <li role="presentation" class="divider"></li>
	    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Separated link</a></li>
	  </ul>
</div>
 -->
 
 <script type="text/javascript">
 function switchUserArea(areaId)
{
  
  
        
 
		  $.ajax({
		    url: 'setUserArea.action?area='+areaId,
		    type: 'GET',
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {		    
		    	window.location.reload();
		    }
		    
		  });

}
 </script>
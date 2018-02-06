      	
<s:set var="loop" value="counter" />
<s:set var="index" value="index" />
<s:set var="p" value="pagesize" />
<s:set var="recordPerPage" value="recordPerPage" />
<s:set var="totalRecords" value="totalRecords" />



<s:set var="t" value="#index / #p" />
<s:set var="temp" value="#index-#p/2" />
<s:set var="endloop" value="#temp +#p-1" />

<s:if test="%{#temp<=0}">
	<s:set var="temp" value="1" />
	<s:set var="endloop" value="#p" />
</s:if>
<s:if test="%{#endloop>#loop}">
	<s:set var="endloop" value="#loop" />
</s:if>


<div class="pagination pgright">
<s:if test="%{#index==1}">
            	<a class="first disabled">&laquo; First</a>
                <a class="prev disabled">&lsaquo; Prev</a>
</s:if>
<s:else>
              	<a class="first" href="/AIR_WEB${requestScope['javax.servlet.forward.servlet_path']}?index=<s:property value="0" />">&laquo; First</a>
                <a class="prev" href="/AIR_WEB${requestScope['javax.servlet.forward.servlet_path']}?index=<s:property value="#index-1" />">&lsaquo; Prev</a>
</s:else>

<s:if test="%{#temp>1}">
 <span>...</span>
 </s:if>
 
<s:iterator var="counter" begin="#temp" end="#endloop" >
 <s:if test="%{#index==#counter}">
	<a href="/AIR_WEB${requestScope['javax.servlet.forward.servlet_path']}?index=<s:property value="#counter" />" class="current"><s:property value="#counter" /></a>
 </s:if>
 <s:else>
   <a href="/AIR_WEB${requestScope['javax.servlet.forward.servlet_path']}?index=<s:property value="#counter" />"><s:property value="#counter" /></a>
 </s:else>
</s:iterator>


 
	<s:if test="%{#totalRecords%#recordPerPage==0}">
		<s:if test="%{#endloop<#totalRecords/#recordPerPage}">
			 <span>...</span>
		 </s:if>
	</s:if>
	<s:else>
		 <s:if test="%{#endloop<#totalRecords/#recordPerPage+1}">
			 <span>...</span>
		 </s:if>
	</s:else>
				
                
<s:if test="%{#index==#endloop}">
            	<a class="next disabled">Next &rsaquo;</a>
				<a class="last disabled">Last &raquo;</a>
</s:if>
<s:else>
              	<a href="/AIR_WEB${requestScope['javax.servlet.forward.servlet_path']}?index=<s:property value="#index+1" />" class="next">Next &rsaquo;</a>
              	<s:if test="%{#totalRecords%#recordPerPage==0}">
				<a href="/AIR_WEB${requestScope['javax.servlet.forward.servlet_path']}?index=<s:property value="#totalRecords/#recordPerPage" />" class="last">Last &raquo;</a>
				</s:if>
				<s:else>
				<a href="/AIR_WEB${requestScope['javax.servlet.forward.servlet_path']}?index=<s:property value="#totalRecords/#recordPerPage+1" />" class="last">Last &raquo;</a>
				</s:else>
</s:else>

                
 </div><!--pagination-->
        
<div id="example_info" class="dataTables_info">Showing <s:property  value="#index*#recordPerPage-#recordPerPage+1" /> to <s:property  value="(#index*#recordPerPage-#recordPerPage)+#recordSize" /> of <s:property  value="totalRecords" /> entries</div>		
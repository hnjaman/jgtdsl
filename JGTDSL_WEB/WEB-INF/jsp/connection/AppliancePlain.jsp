<%@ taglib prefix="s" uri="/struts-tags"%>
 <input type="hidden" value="<s:property value='customer_id'/>" id="customer_id" />
       <table >
       
       <s:if test="%{applianceList.size!=0}">
			<s:iterator value="applianceList" status="indx">		        
		            <!-- <td><s:property value="#indx.count" /></td>-->	            
		           	<td style="border-left: 6px solid green; padding-left:3px;"><s:property value="applianc_name" /></td>
		            <td>: <s:property value="applianc_qnt" /> &nbsp;&nbsp; </td>
		            <!--<td>- <s:property value="applianc_status" /></td>-->		      
			</s:iterator>
		</s:if>
	</table>
                       
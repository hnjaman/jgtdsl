<%@ taglib prefix="s" uri="/struts-tags"%>
 <input type="hidden" value="<s:property value='customer_id'/>" id="customer_id" />
<table id="appTable" class="table table-bordered">
    <thead>
        <tr>
            <th>SL</th>           
            <th>Appliance Name</th>
            <th>Appliance Qnt</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
       <s:if test="%{applianceList.size!=0}">
			<s:iterator value="applianceList" status="indx">
		        <tr>
		            <td><s:property value="#indx.count" /></td>		            
		            <td><s:property value="applianc_name" /></td>
		            <td><s:property value="applianc_qnt" /></td>
		            <td><s:property value="applianc_status" /></td>
		        </tr>
			</s:iterator>
		</s:if>
    </tbody>
</table>



                        

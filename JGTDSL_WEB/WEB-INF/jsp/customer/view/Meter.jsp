<%@ taglib prefix="s" uri="/struts-tags"%>
 <input type="hidden" value="<s:property value='customer_id'/>" id="customer_id" />
<table class="table table-bordered">
    <thead>
        <tr>
            <th>SL</th>
            <th>Meter SL No</th>
            <th>Type</th>
            <th>Rent</th>
            <th>Status</th>
        </tr>
    </thead>
    <tbody>
       <s:if test="%{meterList.size!=0}">
			<s:iterator value="meterList" status="indx">
		        <tr>
		            <td><s:property value="#indx.count" /></td>
		            <td><s:property value="meter_sl_no" /></td>
		            <td><s:property value="measurement_type_name" /></td>
		            <td><s:property value="meter_rent" /></td>
		            <td><s:property value="status.label" /></td>
		        </tr>
			</s:iterator>
		</s:if>
    </tbody>
</table>
<br/><br/>


<button class="btn btn-beoro-3" type="button" id="btn_add" onclick="callAction('meterInformationHome?customer_id=<s:property value="customer_id"/>');">
	<span class="splashy-add_small"></span> Add New Meter
</button>
                        

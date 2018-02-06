<%@ taglib prefix="s" uri="/struts-tags"%>

<table style="width: 100%;border-collapse: collapse;border: 1px solid black;" border="1">
<tr>
 <td width="10%" style="text-align: center;">Old Qnt.</td>
 <td width="10%" style="text-align: center;">Perm. Disconn Qnt.</td>
 <td width="12%" style="text-align: center;">Tmp. Disconn Qnt.</td>
 <td width="12%" style="text-align: center;">Inc. Qnt.</td>
 <td width="12%" style="text-align: center;">Tmp. Reconn. Qnt.</td>
 <td width="12%" style="text-align: center;">Perm. Reconn Qnt.</td>
 <td width="12%" style="text-align: center;">New Qnt.</td> 
 <td width="20%" style="text-align: center;">Effective Date</td>
</tr>
<s:iterator value="eventList">	
	<tr>
	 <td style="text-align: center;"><s:property  value="old_double_burner_qnt"/></td>
	 <td style="text-align: center;"><s:property  value="new_permanent_disconnected_burner_qnt"/></td>
	 <td style="text-align: center;"><s:property  value="new_temporary_disconnected_burner_qnt"/></td>
	 <td style="text-align: center;"><s:property  value="new_incrased_burner_qnt"/></td>
	 <td style="text-align: center;"><s:property  value="new_reconnected_burner_qnt"/></td>
	 <td style="text-align: center;"><s:property  value="new_reconnected_burner_qnt_permanent"/></td>
	 <td style="text-align: center;"><s:property  value="new_double_burner_qnt"/></td> 
	 <td style="text-align: center;"><s:property  value="effective_date"/></td>
	</tr>
</s:iterator>
</table>
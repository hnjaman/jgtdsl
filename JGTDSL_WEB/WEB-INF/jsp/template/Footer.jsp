<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@page import="java.util.TimeZone"%>

	<div class="ui-layout-south"  id="footer">
	    <%
	    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"));
	     %>
		<div style="float:left;margin-top: -5px;">Date- Time : <%= DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date()) %></div>
		<div style="margin-top: -5px;">
		&copy; <%= new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()) %> JGTDSL. All rights Reserve.
		</div>
	</div>
	<span id="tmp_span"><script type='text/javascript'>var step=0;</script></span>
	<span id="deposite_bank_branch_span"><script type='text/javascript'>var bank_temp=0; var branch_temp=0; var account_temp=0;var deposit_id_temp=0;</script></span>

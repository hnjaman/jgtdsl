<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sw-basic-step-2">
    <h4 class="StepTitle">Customer's Address Information</h4>
    <div class="row-fluid">
        <div class="span6">
            <%@ include file="Address.jsp" %>
        </div>
    </div>
</div>
<script type="text/javascript">
fetchSelectBox(district_sbox);
</script>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("journalVoucher.action");
	setTitle("Journal Voucher");	
</script>


<div class="row-fluid">
   <div class="span3"></div>
   <div class="span6">
       <div class="w-box">
           <div class="w-box-header"><h4>Journal Voucher Type Selection</h4></div>
           <div class="w-box-content cnt_a" style="text-align: center;">
               <div class="row-fluid">
                   <div class="span12">
                       <p class="formSep"><label class="muted">Journal Type:</label> 
                       <select style="width:50%;" name="user.default_url" id="default_url" onchange="changeVoucherType(this.value)">
                       	<option value="">Select a Type</option>
                       	<option value="customerCorrection">Customer Correction</option>
                       	<option value="accountCorrection">Account Correction</option>                                        	
                       </select>
                       </p>
                    </div>
                       </div>
                        <div class="formSep">
							<div id="aDiv" style="height: 10px;"></div>
						</div>
                   </div>
               </div>
           </div>
    <div class="span3"></div>
</div>
<div id="aDiv" style="height: 10px;"></div>
<form name="jvForm">
<div id="div_customerCorrection" style="display: none;">
<jsp:include page="CustomerAccountCorrection.jsp" />	
</div>         

<div id="div_accountCorrection" style="display: none;">
<jsp:include page="BankAccountCorrection.jsp" />	
</div>    
</form>   
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/journalVoucher.js"></script> 
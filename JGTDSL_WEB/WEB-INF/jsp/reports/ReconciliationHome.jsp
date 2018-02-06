<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
input[type="text"] {
    float: none !important;
}
</style>
<div class="row-fluid">
                    <div class="span3">                      
                    </div>
                    <div class="span6" style="margin-top: 50px;">
                        <div class="w-box">
                            <div class="w-box-header"><h4>Dlownload Reconciliation Report</h4></div>
                            <div class="w-box-content cnt_a" style="text-align: center;">
                                <form action=reconciliationReport.action>
                                <img src="/JGTDSL_WEB/resources/images/download.png" width="64" height="64" />
									<p style="padding-top: 10px;"></p>
									
									<input type="text" name="bank_id" id="bank_id" style="text-align: center;color: maroon;font-weight: bold;"  value="<s:property value='bank_id' />" /><br/>
									<input type="text" name="branch_id" id="branch_id" style="text-align: center;color: maroon;font-weight: bold;"  value="<s:property value='branch_id' />" /><br/>
									<input type="text" name="account_no" id="account_no" style="text-align: center;color: maroon;font-weight: bold;"  value="<s:property value='account_no' />" /><br/>
									<input type="text" name="collection_month" id="collection_month" style="text-align: center;color: maroon;font-weight: bold;"  value="<s:property value='collection_month' />" /><br/>
									<input type="text" name="collection_year" id="collection_year" style="text-align: center;color: maroon;font-weight: bold;"  value="<s:property value='collection_year' />" /><br/>
									<button class="btn" id="download" type="submit" value="download">Download</button>				
								</form>
								    <button class="btn" id="btn_delete"  onclick="deleteReconcilation()" >Delete</button>
								  
								    
                            </div>
                        </div>
                    </div>
                    <div class="span3">
                    </div>
                    
                </div>
<script>

function deleteReconcilation(){
		  $.ajax({
		    url: 'deleteReconcilation.action',
		    type: 'POST',
		    data: {"bank_id":$("#bank_id").val(),"branch_id":$("#branch_id").val(),
		    	"account_no":$("#account_no").val(),"collection_month":$("#collection_month").val(),
		    	"collection_year":$("#collection_year").val()},
		    cache: false,
		    success: function (response) {
		    if(response.status=="OK")
		    {	
		        alert(response.message);
		        	    $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, {
	                    zIndex: 1500,
	                    width:450,
	                    /*
	                     beforeOpen: function () {
				            centerCustomerInfoDialog($("#customer_id").val());
        				},
        				*/
	                     afterOpen:disableOnClick,
	                     onClose: function () {
	                      $('.ui-widget-overlay').unbind( "click" );
	                      clearField("bank_id","branch_id","account_no","collection_month","collection_year");
	                        return true; // allow closing
	                    }
            });
		    	    
		    	    
		    	    
                
			   
		    }
		    
		    }
		    
		  });
	}

</script>


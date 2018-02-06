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
                            <div class="w-box-header">Demand Note</div>
                            <div class="w-box-content cnt_a" style="text-align: center;">
                                <img src="/JGTDSL_WEB/resources/images/data_entry.png" width="64" height="64" />
									<p style="padding-top: 10px;"></p>
									<input type="text" name="customer_id" id="customer_id" style="text-align: center;color: maroon;font-weight: bold;" value="<s:property value='customer_id' />" /><br/>
									<button class="btn" type="button" onclick="goNext()">Next >></button>
                            </div>
                        </div>
                    </div>
                    <div class="span3">
                    </div>
                    
                </div>
<script type="text/javascript">
function goNext()
{
var customer_id=$("#customer_id").val();
if(typeof customer_id === 'undefined'){
   customer_id="";
 }
 callAction('demandNoteDataEntry.action?customer_id='+customer_id);
}

</script>
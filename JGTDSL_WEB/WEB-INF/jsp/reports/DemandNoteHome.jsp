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
                            <div class="w-box-header"><h4>Dlownload Demand Note</h4></div>
                            <div class="w-box-content cnt_a" style="text-align: center;">
                                <form action="downloadDemandNote.action">
                                <img src="/JGTDSL_WEB/resources/images/download.png" width="64" height="64" />
									<p style="padding-top: 10px;"></p>
									
									<input type="text" name="customer_id" style="text-align: center;color: maroon;font-weight: bold;" value="<s:property value='customer_id' />" /><br/>
									<input type="hidden" value="<s:property value='demand_note_id'/>" name="demand_note_id" id="demand_note_id" />
									<button class="btn" type="submit">Download</button>
								</form>
                            </div>
                        </div>
                    </div>
                    <div class="span3">
                    </div>
                    
                </div>

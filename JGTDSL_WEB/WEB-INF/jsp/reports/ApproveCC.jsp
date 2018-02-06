<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="customerTypeChangeForm" name="customerTypeChangeForm">

	
							<div class="row-fluid">							
								<div class="span12">									    
									<label style="width: 40%">Region/Area</label>
									<select id="area_id"  style="width: 56%;"  name="area">
										<option value="" selected="selected">Select Area</option>
										<s:iterator value="%{#session.USER_AREA_LIST}" id="areaList">
											<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
									</s:iterator>
									</select>									      
								</div>
								
							</div>
							<div class="row-fluid">
							<div class="span12">
								<div class="alert alert-info">
									<table width="50%" align="center">
										<tr>
											<td width="100%" align="right" style="font-size: 12px;font-weight: bold;">
												<input type="radio" value="app_individual_wise" id="app_individual_wise" name="approve_type" checked onclick="checkTypeSub(this.id)"/>Individual&nbsp;&nbsp;&nbsp;
												<input type="radio" value="app_category_wise" id="app_category_wise" name="approve_type" onclick="checkTypeSub(this.id)" />Category&nbsp;&nbsp;&nbsp;
											</td>											
										</tr>
									</table>
                                </div>
                                
							</div>
						</div>
						<div class="row-fluid" id="app_individual_code_div" >
							<div class="span12" style="margin-top: 4px;">
								<label style="width: 40%">Customer ID <m class='man'/></label>
								<input type="text" name="approveCCdto.customerID" id="app_customer_id" style="font-weight: bold;color: #3b5894;position: absolute; z-index: 2; background: transparent;width: 10%;margin-top: -4px;"  tabindex="1"/>
								
								
						  	</div>
						</div>
						
					 <div class="row-fluid" id="app_category_div" style="display: none">
								<label style="width: 40%">Category<m class='man'/></label>
								<select id="customer_category" style="width: 56%;"   name="approveCCdto.category">
									<option value="" selected="selected">Select Category</option>
									<s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
										<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
									</s:iterator>
								</select>       
					  </div> 
                    
	
					<div class="row-fluid" id="app_issue_date_div">		
						<div class="span12">									    
								<label style="width: 40%">Issue Date</label>
								<input type="text" style="width: 30%" name="approveCCdto.payDate" id=issue_date />							      
					   </div>
					</div>   
					
						
						<div class="row-fluid" id="app_nonDistributedDiv" style="display:none">							
						  	<div class="span12">									    
								<label style="width: 40%">Remarks</label>
								<textarea rows="1" style="width: 50%" name="approveCCdto.nonDistributed" id="nonDistributed" ></textarea> 							      
							</div>						  	
						</div>
						
						<input type="hidden" id="customer_id" name="customer.customer_id"/>
						
						<p></p>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
							
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">									
							<table width="100%">
						    <tr>
						    	

						    	<td width="5%">
						    		
						    	</td>
						    	<td width="40%" align="right">
						    		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="validateAndApproveCC()" ><span class="splashy-document_letter_okay"></span>
								    Approve</button>
									<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>
									Close</button>
						    	
						    	</td>
						    </tr>
						    </table>											
						</div>
					  			
           
							

</form>			
<script type="text/javascript">
autoSelect("area_id");
enableField("area_id");
  Calendar.setup({
    inputField : "issue_date",
    trigger    : "issue_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });

  
function checkTypeSub(type){
alert();
 if(type=="app_individual_wise")
	{
	hideElement("app_nonDistributedDiv","app_category_div");
	showElement("app_individual_code_div","app_issue_date_div");
	 clearField("nonDistributed","customer_category");
	}else if(type=="app_category_wise"){
	 hideElement("app_individual_code_div");
	 showElement("app_nonDistributedDiv","app_category_div");
	 autoSelect("customer_category");
	 enableField("customer_category");
	 clearField("app_customer_id");
	}
};
function validateAndApproveCC(){
	
	var validate=true;
	//alert("1");
	//validate=validateTypeChangeInfo();
	if(validate==true){
	//alert("2");	    
		approveCC();							
	}	
}


function validateTypeChangeInfo(){
	
	var isValid=false;	
    isValid=validateField("area_id","customer_category","from_date");		
    return isValid;
}


function approveCC(){

	
	//CustomerTypeChangeForm(enableField);
	//CustomerTypeChangeForm(readOnlyField);
	var customer_id=$("#app_customer_id").val();
	var issue_date=$("#issue_date").val();
	var area=$("#area_id").val();
	var category=$("#customer_category").val();
	var nondistributed=$("#nonDistributed").val();
	var approve_type=$('input[name=approve_type]:checked').val();
	
	
	 
	 $.ajax({
		    url: 'approveCC.action',
		    type: 'POST',
		    data: { customer_id:$("#app_customer_id").val(),issue_date:$("#issue_date").val(),area:$("#area_id").val(),category:category,nondistributed:nondistributed,approve_type:approve_type},
		    cache: false,
		    success: function (response) {
		        clearField("nonDistributed","app_customer_id");
		   		$.jgrid.info_dialog(response.dialogCaption,response.message,jqDialogClose,jqDialogParam);		   
		    }		    
		  });
	
}

</script>
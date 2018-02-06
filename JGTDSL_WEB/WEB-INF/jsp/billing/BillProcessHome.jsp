<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
.muted{
margin-bottom:12px;
}
.individual_overlay { 
  display:none; 
  position:absolute; 
  background:#fff; 
  border: 1px solid #efefef;
}
.category_overlay{ 
  display:none; 
  position:absolute; 
  background:#fff; 
  border: 1px solid #efefef;
}
</style>
<div class="row-fluid">
   				<div class="span5">
   				<form id="billingParamForm" name="billingParamForm" style="margin-bottom: 0px;">
                        <div class="w-box">
                            <div class="w-box-header">
                                <h4>Billing</h4>
                            </div>
                            <div class="w-box-content cnt_a user_profile">
                                <div class="row-fluid">
                                    <div class="span2">
                                        <div class="img-holder">
                                            <img class="img-avatar" alt="" src="/JGTDSL_WEB/resources/images/taka.png">
                                        </div>
                                    </div>
                                    <div class="span10">
                                    	<p class="formSep"><label class="muted"></label> 
                                        <input type="radio" name="bill_param.bill_by" id="category" value="category" style="margin-top: -2px;" checked="checked" onclick="showHide()"/>&nbsp;Category Wise
                                        &nbsp;&nbsp;&nbsp;
                                        <input type="radio" name="bill_param.bill_by" id="individual" value="individual" style="margin-top: -2px;" onclick="showHide()"/>&nbsp;Individual
                                        </p>
                                        
                                        <p class="formSep" id="individual_div"><label class="muted">Customer Id</label>                                         
	                                        <input type="text" name="bill_param.customer_id" id="customer_id" style="position: absolute; z-index: 2; background: transparent;width: 15%;"/>
	            							<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 15%;"/>
	            							<div class="individual_overlay"></div>	        
                                        </p>                                        
                                        <div id="category_div">
                                        <p class="formSep" style="border-top:1px dashed #ddd;"><label class="muted">Meter Status:</label> 
                                        <select name="bill_param.str_is_metered" id="is_metered" style="width: 56%" onchange="fetchBillingInfo()">
								            <option value="">Select Status</option>           
									        <s:iterator  value="%{#application.METERED_STATUS}">   
									   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
											</s:iterator>       
							        	</select>
							        	
                                        </p>
                                        <p class="formSep"><label class="muted">Customer Category:</label>
                                        <select name="bill_param.customer_category" id="customer_category"  style="width: 56%;" onchange="fetchBillingInfo()">
					                        <option value="" selected="selected">Select Category</option>
					                        <s:iterator value="%{#application.ACTIVE_CUSTOMER_CATEGORY}" id="categoryList">
					                            <option value="<s:property value="category_id" />" >
					                                <s:property value="category_name" />
					                            </option>
					                        </s:iterator>
					                    </select>
                                        </p>
                                        <p class="formSep"><label class="muted">Area:</label>
                                        <select name="bill_param.area_id" id="area_id"  style="width: 56%;" onchange="fetchBillingInfo()">
					                        <option value="" selected="selected">Select Area</option>
					                        <s:iterator value="%{#application.ACTIVE_AREA}" id="categoryList">
					                            <option value="<s:property value="area_id" />" >
					                                <s:property value="area_name" />
					                            </option>
					                        </s:iterator>
					                    </select>
                                        </p>
                                        <div class="category_overlay"></div>
                                      </div>
                                        <p class="formSep" style="border-top:1px dashed #ddd;"><label class="muted">Billing Month:</label>
                                        <select name="bill_param.str_billing_month" id="str_billing_month" style="width: 56%;">
									       		<option value="">Select Month</option>           
									        <s:iterator  value="%{#application.MONTHS}">   
									   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
											</s:iterator>
										</select>
                                        </p>
                                        <p class="formSep"><label class="muted">Billing Year:</label>
                                          <select name="bill_param.billing_year" id="billing_year" style="width: 56%;" >
									       	<option value="2014">2014</option>
									       	<option value="2015">2015</option>
									       </select>
                                        </p>
                                        <p class="formSep">
                                       	    <label></label>
									    	<button class="btn btn-beoro-3" type="button" onclick="fetchBillingInfo()" id="btn_save">Fetch Information</button>
										</p>
                                        
                                        <div style="padding: 20px;">
                                        <img src="/JGTDSL_WEB/resources/images/billing1.png" width="206" height="150">
                                        
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>                         
					</form>	                     
                    </div>     
                    
                    <div class="span7" id="rightSpan">
                        <div class="w-box">
                            <div class="w-box-header">
                                <h4 id="rightSpan_caption">Billing Information</h4>
                            </div>
                            <div class="w-box-content" style="padding: 10px;" id="content_div">
                                 																			
                            </div>
                        </div>
                    </div>            
</div>   
<script type="text/javascript">
function fetchBillingInfo()
{
   var isValid=false;
   if(document.getElementById("individual").checked==true)
    {
     if(isEmpty("customer_id")==false && isEmpty("str_billing_month")==false && isEmpty("billing_year")==false) 
   		isValid=true;
    }
   else if(document.getElementById("category").checked==true)
   {
    if(isEmpty("is_metered")==false && isEmpty("customer_category")==false && isEmpty("str_billing_month")==false && isEmpty("billing_year")==false) 
   		isValid=true;
   }  
   

   if(isValid==true)
 	   {
         //grab all form data  
         var formData = new FormData($('form')[0]);
 
		  $.ajax({
		    url: 'getBillingInfo.action',
		    type: 'POST',
		    data: formData,
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
		    	$("#content_div").html(response);
		    }
		    
		  });
 	   } 

}s
    $('#customer_id').autocomplete({
    	minChars:2,
        serviceUrl: sBox.CUSTOMER_LIST,
        onHint: function (hint) {
            $('#customer_id_x').val(hint);
        },
        lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
            var re = new RegExp('\\b' + $.Autocomplete.utils.escapeRegExChars(queryLowerCase), 'gi');
            return re.test(suggestion.value);
        }
    });

	$t1=$("#individual_div");     
	$t2=$("#category_div");
	$(".individual_overlay").css({
	  background:'url("/JGTDSL_WEB/resources/images/overlay1.png")',
	  opacity : 0.5,
	  top     : $t1.offset().top-90,
	  width   : $t1.outerWidth(),
	  height  : $t1.outerHeight()+20,
	  zIndex:100
	});
	$(".category_overlay").css({
	  background:'url("/JGTDSL_WEB/resources/images/overlay1.png")',
	  opacity : 0.5,
	  top     : $t2.offset().top-80,
	  width   : $t2.outerWidth(),
	  height  : $t2.outerHeight()+10,
	  zIndex:100
	});
	
	function showHide(){
	if(document.getElementById("category").checked==false){
	  $(".category_overlay").fadeIn();
	  $(".individual_overlay").fadeOut();
	}
	else{
	  $(".category_overlay").fadeOut();
	  $(".individual_overlay").fadeIn();
	}
	}
	showHide();
	//$(".individual_overlay").fadeIn();
	//$(".category_overlay").fadeIn();
	
</script>
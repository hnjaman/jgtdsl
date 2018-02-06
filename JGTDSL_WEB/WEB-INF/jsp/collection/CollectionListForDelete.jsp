<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="meter-reading" style="width: 100%;height: 50%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Collection list for Delete Wrong Entry</h4>
				</div>
				<div class="w-box-content"  id="content_div">
				
     										
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   
						   <input type="hidden" id="areaId" name="areaId" value="<s:property value='areaId'/>" />
						   <input type="hidden" id="customerCategory" name="customerCategory" value="<s:property value='customerCategory'/>" />
						   <input type="hidden" id="billMonth" name="billMonth" value="<s:property value='billMonth'/>" />
						   <input type="hidden" id="billYear" name="billYear" value="<s:property value='billYear'/>" />
						   
						   
						   <table width="100%" class="table-bordered" border="1">
						   
						   <thead>
							 
				                <tr >
				                    <th align="center">Scroll No</th>
				                    <th align="center">Customer ID</th>
				                    <th align="center">Name</th>
				                    <th align="center">From Month</th>
				                    <th align="center">From year</th>
				                    <th align="center">To Month</th>
				                    <th align="center">To year</th>
				                    <th align="center">Collected Amount</th>
				                    <th align="center">Collected Surcharge</th>
				                    <th align="center">Bank Id</th>
				                    <th align="center">Branch Id</th>
				                    <th align="center">Delete</th>
				                    				                    
				                </tr>              
				               
				                
				            </thead>
				            <tbody>
				             <s:iterator value="collectionList" >
				            	<tr >
				            		<td id="scroll_no" align="center"> <s:property value="scroll_no" />  </td>  
				            		<td align="center"> <s:property value="customer_id" /> </td>   
				            		<td align="center"> <s:property value="customer_name" /> </td>  
				            		<td align="center"> <s:property value="month_from" /> </td>  
				            		<td align="center"> <s:property value="year_from" /> </td>  
				            		<td align="center"> <s:property value="month_to" /> </td>
				            		<td align="center"> <s:property value="year_to" /> </td>   
				            		<td align="center"> <s:property value="collection_amount" /> </td>  
				            		<td align="center"> <s:property value="surcharge_collected" /> </td>  
				            		<td align="center"> <s:property value="bank_id" /> </td>  
				            		<td align="center"> <s:property value="branch_id" /> </td>
				            		<td align="center"> <input type="button" class="btn btn-danger" id="btn_delete" onclick="deleteWrongCollection('<s:property value="scroll_no" />')" value="Delete"></td>            		
				            	</tr>
				            	
				            </s:iterator>
				 	 		</tbody>
						   
						   
						   </table>
						   
						 </div>
						
						
																				
				</div>
			</div>
		</div>
	</div>
</div>

 

	


<script type="text/javascript" >

function deleteWrongCollection(scrollno) {
	var	a=scrollno;
		//alert(a);
		
        $.ajax({
            type    : "POST",
            url     : "ExecuteDeleteCollection",
            dataType: 'text',
            async   : false,
            data    : {scroll_no: scrollno},
        	}).done(function (msg) {
						fetchWrongCollectionList();
                })
                .always(function () {
                    //$('#sw-val-step-3').unmask();
                })
                .fail(function (data) {
                    if (data.responseCode)
                        alert(data.responseCode);
                });
                	
    } 


	


</script>	

<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="w-box">
    <div class="w-box-header">
        
        <table style="width: 100%">
        <tr>
        <td width="50%" style="text-align: left"><h4>Holiday List</h4></td><td  width="50%" style="text-align: right">
        <input type="checkbox" id="checkAll" onclick="checkAll()" />
        </td>
        </tr>
        </table>
    </div>
    <div class="w-box-content">
        <div id="list_basic" class="jQ-list">
            <ul>
                        <s:set var="m_y" value="" />
                        <s:iterator value="holidayList" status="indx">
                        
                         <s:if test="#m_y!=month_year">
                           <s:if test='%{#m_y!=null && !#m_y.isEmpty()}'>                           
		                    </ul>
		                    </li>
                           </s:if>
                          
                          <li>
		                    <h4><s:property value='month_year' /></h4>		                    
		                    <ul> 								
                         </s:if>
                         
                         <s:set var="m_y" value="month_year" />                                                 
	                        <li>
	                            <span class="list-username"><a href="javascript:void(0)"> <s:property value="holiday_date"/></a></span>
	                            <span class="list-info"> <span><s:property value="holiday_type_name"/>:</span> <s:property value="holiday_cause"/></span>
	                            <div style='float:right;margin-top:-25px;'>
	                            <img src="/JGTDSL_WEB/resources/images/edit_16.png" style="cursor: pointer;" class='img_edit' onclick="updateHome(<s:property value="holiday_id"/>)"/>
	                            <img src="/JGTDSL_WEB/resources/images/delete_16.png" style='margin-left:15px;cursor: pointer;' class='img_del' onclick="deleteHoliday(<s:property value="holiday_id"/>,'<s:property value="holiday_date"/>','<s:property value="holiday_cause"/>','<s:property value="holiday_type_name"/>')" />
	                            <input type="checkbox" value="<s:property value="holiday_id"/>" style="margin-top: -5px;margin-left:15px;" id="hd<s:property value='#indx.index' />"/>
	                            </div>
	                        </li>  
                        </s:iterator>
                        <s:if test='%{#holidayList!=null && !#holidayList.isEmpty()}'>
                        </ul>
		                    </li>
		                </s:if>
			  </ul>
              		
        </div>
    </div>
</div>

<%@ include file="HolidayInfo.jsp" %>


<script src="/JGTDSL_WEB/resources/thirdParty/stickySectionHeaders/jquery.stickysectionheaders.min.js"></script>                        
<script type="text/javascript">
   var totalHoliday=<s:property value="%{holidayList.size()}"/>;
   
   
   var hId;
    beoro_holiday_list = {
        basic: function() {
            if($('#list_basic').length) {
                $('#list_basic').stickySectionHeaders({
                    stickyClass     : 'sticky_header',
                    headlineSelector: 'h4'
                });
            }
        }
    };
    beoro_holiday_list.basic();
    
    function checkAll()
    {
     if(document.getElementById("checkAll").checked==true)
      {
        for(var i=0;i<totalHoliday;i++)
         {
          if(document.getElementById("hd"+i))
          	document.getElementById("hd"+i).checked=true;          
         }
         
      }
     else
      {
        for(var i=0;i<totalHoliday;i++)
         {
          if(document.getElementById("hd"+i))
          	document.getElementById("hd"+i).checked=false;          
         }         
      }
    }
    
    function deleteBulkHoliday()
    {
	  hId="";
      for(var i=0;i<totalHoliday;i++)
         {
           if(document.getElementById("hd"+i) && document.getElementById("hd"+i).checked==true)
              hId+=document.getElementById("hd"+i).value+", ";
          
         }
      if(hId.length>1){
       	hId=hId.substr(0, hId.length-2);
       	deleteHolidayAjax();
       }
      else
      {
        alert("Please select holiday for delete operation.");
      }
      
    } 
    <s:if test="%{holidayList.size()>0}">
    $("#del_all").show();
    </s:if>
    <s:else>
    $("#del_all").hide();
    </s:else>
</script>

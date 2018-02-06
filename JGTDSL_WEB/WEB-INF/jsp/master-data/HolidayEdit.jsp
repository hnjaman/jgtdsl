<%@ taglib prefix="s" uri="/struts-tags"%>
<form id="holidayForm" name="holidayForm">
         <div class="w-box">
             <div class="w-box-header">
                 <h4>Create a Holiday</h4>
             </div>
             <div class="w-box-content cnt_a user_profile">
                 <div class="row-fluid">
                     <div class="span10">
						<p class="formSep"><label class="muted">Date:</label> <input type="text" id="from_date" name="holiday.holiday_date" style="width: 56%;margin-bottom:0px;" value="<s:property value='holiday.holiday_date' />" /></p>
						<p class="formSep"><label class="muted">Holiday Cause :</label> <input type="text" id="holiday_cause" name="holiday.holiday_cause" style="width: 56%;margin-bottom:0px;"  value="<s:property value='holiday.holiday_cause' />" /></p>
						<p class="formSep"><label class="muted">Holiday Type:</label> 
						<select id="holiday_type" name="holiday.holiday_type" style="width: 60%;margin-bottom:0px;">
						  <option value="">Select a Type</option>
						  <option value="1" <s:if test="holiday.holiday_type == 1">selected='selected'</s:if>>Weekend</option>
						  <option value="2" <s:if test="holiday.holiday_type == 2">selected='selected'</s:if>>Public Holiday</option>
						</select>						
						</p>
						<div class="formSep sepH_b">    
						    <input type="hidden" name="holiday.holiday_id" value="<s:property value='holiday.holiday_id' />" />
						    <button class="btn btn-beoro-3" type="button" onclick="updateHoliday()" id="btn_save">Update</button>  
						</div>	                    
            		</div>
     			</div>
 			</div>
		</div>
</form>	   
<%@ include file="HolidayInfo.jsp" %>  

<script type="text/javascript">
	 function saveHoliday()
	 {
 	   if(emptyCheck("from_date")==true && emptyCheck("holiday_cause")==true && emptyCheck("holiday_type")==true)
 	   {
	    var formData = new FormData($('form')[0]);
	 	$.ajax({
   		  type: 'POST',
   		  url: 'saveHoliday.action',
   		  data: formData,
	      async: false,
	      cache: false,
	      contentType: false,
	      processData: false,
   		  beforeSend:function(){
   			$("#btn_save").attr("disabled","disabled");
   		  },
   		  success:function(data){
						
   		  },
   		  error:function(){
   			$("#btn_save").removeAttr("disabled");
   		  }
   	});
   	}
}	
	  Calendar.setup({
        inputField : "from_date",
        trigger    : "from_date",
		eventName : "focus",
        onSelect   : function() { this.hide();focusToDate();  },
        dateFormat : "%d-%m-%Y",
		onBlur: focusToDate
      });
      Calendar.setup({
        inputField : "to_date",
        trigger    : "to_date",
		eventName : "focus",
        onSelect   : function() { this.hide();focusCause();  },
        dateFormat : "%d-%m-%Y",
		onBlur: focusCause
      });
      function focusToDate()
      {
        $(window).focus();
		$(document).focus(); 
        $('#to_date').focus();
      }
      function focusCause()
      {
        $(window).focus();
		$(document).focus(); 
        $('#holiday_cause').focus();
      }
      
     function updateHoliday()
	 {
	 	   if(emptyCheck("from_date")==true && emptyCheck("holiday_cause")==true && emptyCheck("holiday_type")==true)
	 	   {
			    var formData = new FormData($('form')[0]);
			 	$.ajax({
		   		  type: 'POST',
		   		  url: 'updateHoliday.action',
		   		  data: formData,
			      async: false,
			      cache: false,
			      contentType: false,
			      processData: false,
		   		  beforeSend:function(){
		   			$("#btn_save").attr("disabled","disabled");
		   		  },
		   		  success:function(data){	    
				    $(".bcal-date").css("background-color","#edf4fe");
				    $(".bcal-date").css("color","#000");				    
				    loadHolidays(cal_from,cal_to);
				    fetchHolidayList();			
		   		  },
		   		  error:function(){
		   			$("#btn_save").removeAttr("disabled");
		   		  }
		   		});
	   		}
	}	

      
</script>		 
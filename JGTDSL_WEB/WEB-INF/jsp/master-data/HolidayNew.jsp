<form id="holidayForm" name="holidayForm">
         <div class="w-box">
             <div class="w-box-header">
                 <h4>Create a Holiday</h4>
             </div>
             <div class="w-box-content cnt_a user_profile">
                 <div class="row-fluid">
                     <div class="span10">
						<p class="formSep"><label class="muted">Holiday Type <m class='man'/></label> 
						<select id="holiday_type" name="holiday.holiday_type" style="width: 60%;margin-bottom:0px;" onchange="hTypeAction(this.value)">
						  <option value="">Select a Type</option>
						  <option value="1">Weekend</option>
						  <option value="2">Public Holiday</option>
						</select>						
						</p>
						<p class="formSep"><label class="muted">From Date  <m class='man'/></label> <input type="text" id="from_date" name="holiday.from_date" style="width: 56%;margin-bottom:0px;" /></p>
						<p class="formSep"><label class="muted">To Date:</label> <input type="text" id="to_date" name="holiday.to_date" style="width: 56%;margin-bottom:0px;" /></p>
						<p class="formSep"><label class="muted">Holiday Cause  <m class='man'/></label> <input type="text" id="holiday_cause" name="holiday.holiday_cause" style="width: 56%;margin-bottom:0px;" /></p>
						<p class="formSep" id='weekDayP' style='display:none;'><label class="muted">Week Day  <m class='man'/></label> 
						<select id="weekDay" name="holiday.weekDay" style="width: 60%;margin-bottom:0px;">
						  <option value="">Select WeekDay</option>
						  <option value="sat">Saturday</option>
						  <option value="sun">Sunday</option>
						  <option value="mon">Monday</option>
						  <option value="tue">Tuesday</option>
						  <option value="wed">Wednesday</option>
						  <option value="thu">Thursday</option>
						  <option value="fri">Friday</option>
						</select>						
						</p>						
						<div class="formSep sepH_b">    							
						    <button class="btn btn-beoro-3" type="button" onclick="saveHoliday()" id="btn_save">Submit</button>
						    <button class="btn btn-danger" onclick="resetPanel()">Reset</button>    
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
	   var v1=emptyCheck("holiday_type");
	   var v2=emptyCheck("from_date");
	   var v3=emptyCheck("holiday_cause");
	   var v4=false;
	   var validation=false;
	   
	   if($("#holiday_type").val()==1){
	     var v4=emptyCheck("weekDay");
	     if(v1==true && v2 ==true && v3 == true && v4==true)
	       validation=true
	     }
	   else
	   if(v1==true && v2 ==true && v3 == true )
	       validation=true
	     
	   
 	   if(validation)
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
					if(data.status==jsVar.OK)
					 {
					  alert(data.message);
					  ajaxLoad('rightArea','getNewHolidayForm.action');
					  $(".bcal-date").css("background-color","#edf4fe");
					  $(".bcal-date").css("color","#000");
					  
					  if(typeof cal_from === 'undefined'){
							cal_from=fromStr;
							cal_to=toStr;
					  }				    
					  loadHolidays(cal_from,cal_to);
					  $("#btn_save").removeAttr("disabled");
					 }
					else{
					 alert(data.message)
					 $("#btn_save").removeAttr("disabled");
					 }	
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
      function hTypeAction(type)
	{
	 if(type==1)
	   {
	    $("#weekDayP").show();
	    if($("#holiday_cause").val()=="")
	     $("#holiday_cause").val("Weekend");
	    }	   
	 else
	   $("#weekDayP").hide();
	
	}
	$("#del_all").hide();
	showMandatoryStar();
</script>		 
<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("holidayHome.action");
	setTitle("Holiday Management");
</script>
<script type="text/javascript">
var cal_from;
var cal_to;
</script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/holiday.js"></script>
<link type="text/css" rel="stylesheet" href="/JGTDSL_WEB/resources/thirdParty/calendarJs/themes/sky-blue/calendar.css" />
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/calendarJs/calendar-1.5.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/jsHashTable/hashtable.js"></script>
<style type="text/css">
.bcal-container {
    background-color: transparent !important;
    border: none !important;
    box-shadow:none  !important;
    padding: 0px  !important;
}
.bcal-table{
margin: 15px !important;
}
</style>
<div class="row-fluid">
     <div class="span7" id="rightSpan">
         <div class="w-box">
             <div class="w-box-header">
                 <h4 id="rightSpan_caption">Holidays</h4>
             </div>
             <div class="w-box-content" style="padding: 10px;height: 500px;" id="content_div">
                  
             </div>
             
         </div>
         <div style="width: 100%;border-top: 1px solid white;height: 40px;margin-top: 4px;font-size: 11px">
             <div style="width: 50%;text-align: left;float: left;" >
             	<button class="btn btn-success" type="button" onclick="fetchHolidayList()">View List</button>
             	<button class="btn btn-info" type="button" onclick="ajaxLoad('rightArea','getNewHolidayForm.action')">Add New</button>
             	<button onclick="deleteBulkHoliday()" type="button" class="btn btn-danger" id="del_all" style="display: none;">Delete All</button>
             </div>
             <div style="width: 50%;text-align: right;float: left;">
	             <div style="width:16px;height:16px;background:#22C8FF;float: right;">&nbsp;</div>
	             <div style="float: right;margin-right: 5px;">Weekend</div>
	             <div style="width:16px;height:16px;background:#FF9122;float: right;margin-right: 10px;">&nbsp;</div>
	             <div style="float: right;margin-right: 5px;">Public Holiday</div>
             </div>
             
         </div>
     </div>            
  <div class="span5" id="rightArea">
   <%@ include file="HolidayNew.jsp" %>  
  </div>                  
</div>   
                       
<script type="text/javascript">
    var options = {replaceDuplicateKey: false};
    var holidaHashTable = new Hashtable(options);

       
		var cal_1 = new CalendarJS({
			element: 'content_div',
			inline: true,
			months: 6,
			month:0,
			dateFormat: 'm/d/Y',
			onSelect: function (element, selectedDate, date, cell) {
				
				if(holidaHashTable.get(date).type_id==1)
					$("#holiday_info").css('border', 'solid 2px #22C8FF');
				else
					$("#holiday_info").css('border', 'solid 2px #FF9122');
					
				$("#holiday_detail").html(holidaHashTable.get(date).cause);
				$("#holiday_date").html(holidaHashTable.get(date).date+" ("+holidaHashTable.get(date).type_name+") ");

				$("#hd_del_btn").on("click", function(){ 
				deleteHoliday(holidaHashTable.get(date).id,holidaHashTable.get(date).date,holidaHashTable.get(date).cause,holidaHashTable.get(date).type_name)
				 });
				
				$("#hd_edit_btn").on("click", function(){ 
					updateHome(holidaHashTable.get(date).id);
				});

				
				
				
 			 	$("#holiday_info").show();
				$('#holiday_info').addClass('animated fadeInDown');				
				window.setTimeout( function(){
				             $("#holiday_info").removeClass('animated fadeInDown');
				         }, 1000); 
			
			}
		});
		
		function loadHolidays(from,to)
		{
		  var cal_from=from;
		  var cal_to=to;
		  
		  if(typeof cal_from === 'undefined'){
			cal_from=fromStr;
			cal_to=toStr;
		  }
		  
		  $.ajax({
   		  type: 'POST',
   		  url: 'getHolidays.action',
   		  data: {from:cal_from,to:cal_to },
   		  success:function(data)
   		  {
			holidaHashTable = new Hashtable(options);
			for(var i in data)
			{
			alert(data[i].holiday_type);
			   if(data[i].holiday_type==1)
					$("#"+data[i].holiday_date_id).css("background-color","#22C8FF");
			   else
			        $("#"+data[i].holiday_date_id).css("background-color","#FF9122");

			   holidaHashTable.put(data[i].holiday_date_id,{"id":data[i].holiday_id,"cause":data[i].holiday_cause,"date":data[i].holiday_date,"type_id":data[i].holiday_type,"type_name":data[i].holiday_type_name});
			}		
   		  },
   		  error:function (xhr, ajaxOptions, thrownError) {
		        alert(xhr.status);
		        alert(thrownError);
		      }
   		});    
		 
		}
		function pad(input) {
				return (input + "").length === 2 ? input : "0" + input;
		}
		var _fm;var _tm;
		if(new Date().getMonth()<=5)
		  {_fm="01";_tm="06";}
		else
		  {_fm="07";_tm="12";}
		  
		var _y= new Date().getFullYear();
		
		var fromStr=_fm+"-"+_y;
		var toStr=_tm+"-"+_y;
		
		
		loadHolidays(fromStr,toStr);

    
    var $dialog = $('<div id="dialog-confirm"></div>')
			  .html("<p> "+
	 		  	"Are you sure you want to delete the following holiday?"+
	 			"<div id='del_holiday'></div> "+
	 		    "</p>")
			  .dialog({
					title: 'Holiday Delete Confirmation',
					resizable: false,
      				autoOpen: false,
      				height:255,
      				width:450,
      				modal: true,
      				buttons: {
       					"Delete": {text:"Delete",class:'btn btn-danger',click:function() {
							deleteHolidayAjax();          
        				}},
        				"Cancel": {text:"Cancel",class:'btn btn-beoro-3',click:function() {
          					$( this ).dialog( "close" );
        				}},
      				}
				});


  function deleteHoliday(holidayId,holidayDate,holidayCause,holidayType) {
    hId=holidayId;
    $("#del_holiday").html("<b>Holiday Date :</b> "+holidayDate+"<br/><b>Cause:</b> "+holidayCause+"<br/><b>Type:</b> "+holidayType+"<div id='del_loading' style='height:10px;'>&nbsp;</div>");
    $dialog.dialog('open');
  };
  function updateHome(holidayId)
  {
   ajaxLoad('rightArea','updateHome.action?holidayId='+holidayId);
  }		
  function deleteHolidayAjax()
  {
   	      $.ajax({
	   		  type: 'POST',
	   		  url: 'deleteHoliday.action',
	   		  data: { holidayId:hId},
	   		  beforeSend:function(){
	   			$('#del_loading').html(jsImg.SPINNER);
	   			
	   		  },
	   		  success:function(data){
				
			    $dialog.dialog('close');		    
			    $(".bcal-date").css("background-color","#edf4fe");
			    $(".bcal-date").css("color","#000");
			    
			    loadHolidays(cal_from,cal_to);
			    fetchHolidayList();
			    
	   		  },
	   		  error:function(){
	   			$dialog.dialog('close');
	   		  }
   		  });
   
  }
function fetchHolidayList(){
	if(typeof cal_from === 'undefined'){
		cal_from=fromStr;
		cal_to=toStr;
	}
	ajaxLoad('rightArea','fetchHolidayList.action?from='+cal_from+"&to="+cal_to);                
}
</script>		
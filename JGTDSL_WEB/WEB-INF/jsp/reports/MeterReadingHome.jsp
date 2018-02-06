<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
.muted{
margin-bottom:12px;
}
#tabs {
  overflow: hidden;
  width: 100%;
  margin: 0;
  padding: 0;
  list-style: none;
}

#tabs li {
  float: left;
  margin: 0 .5em 0 0;
}

#tabs a {
  position: relative;
  background: #ddd;
  background-image: linear-gradient(to bottom, #fff, #ddd);  
  padding: .7em 3.5em;
  float: left;
  text-decoration: none;
  color: #444;
  text-shadow: 0 1px 0 rgba(255,255,255,.8);
  border-radius: 5px 0 0 0;
  box-shadow: 0 2px 2px rgba(0,0,0,.4);
}

#tabs a:hover,
#tabs a:hover::after,
#tabs a:focus,
#tabs a:focus::after {
  background: #fff;
}

#tabs a:focus {
  outline: 0;
}

#tabs a::after {
  content:'';
  position:absolute;
  z-index: 1;
  top: 0;
  right: -.5em;  
  bottom: 0;
  width: 1em;
  background: #ddd;
  background-image: linear-gradient(to bottom, #fff, #ddd);  
  box-shadow: 2px 2px 2px rgba(0,0,0,.4);
  transform: skew(10deg);
  border-radius: 0 5px 0 0;  
}

#tabs #current a,
#tabs #current a::after {
  background: #fff;
  z-index: 3;
}

#content {
  background:#e6f0ff;
  padding: 2em;
  height: 500px;
  position: relative;
  z-index: 2; 
  border-radius: 0 5px 5px 5px;
  box-shadow: 0 -2px 3px -2px rgba(0, 0, 0, .5);
}

</style>

	
<div class="row-fluid">
   				<div class="span8">
                        <div class="w-box">
                            <div class="w-box-header">
                                <h4>Report List</h4>
                            </div>
                            <div >
                                <div class="row-fluid">
                                    
                                        
                                        
                                        <ul id="tabs">
										    <li><a href="#" name="tab1">Load Increase Info</a></li>
										    <li><a href="#" name="tab2">Two</a></li>
										    <li><a href="#" name="tab3">Three</a></li>
										    <li><a href="#" name="tab4">Four</a></li>    
										</ul>

										<div id="content"> 
										    <div id="tab1"><%@ include file="/WEB-INF/jsp/reports/NonMeterDisconnectionHome.jsp" %></div>
										    <div id="tab2">Two</div>
										    <div id="tab3">Three</div>
										    <div id="tab4">Four</div>
										</div>
			                         </div>
                                </div>
                            </div>
                        </div> 
						                     
                    </div>     
                  
</div>   

<script>
$(document).ready(function() {
    $("#content").find("[id^='tab']").hide(); // Hide all content
    $("#tabs li:first").attr("id","current"); // Activate the first tab
    $("#content #tab1").fadeIn(); // Show first tab's content
    
    $('#tabs a').click(function(e) {
        e.preventDefault();
        if ($(this).closest("li").attr("id") == "current"){ //detection for current tab
         return;       
        }
        else{             
          $("#content").find("[id^='tab']").hide(); // Hide all content
          $("#tabs li").attr("id",""); //Reset id's
          $(this).parent().attr("id","current"); // Activate this
          $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
        }
    });
});
</script>
<script type="text/javascript">
function getCustomerInfo(customer_id)
{
	$.ajax({
   		  type: 'POST',
   		  url: 'getCustomerInfoAsJson.action',
   		  data: { customer_id:customer_id},
   		  beforeSend:function(){
   			$('.img-holder').html('<div class="loading">'+jsImg.LOADING+'</div>');
   		  },
   		  success:function(data){
			if(typeof data.personalInfo === "undefined")
			{
			  clearCustomerInfo();
    		  $("#rightSpan").html(rBox);
			  alert(data.message);
			}
			else
			{
				var personal=data.personalInfo;
				var address=data.addressInfo;
				var connection=data.connectionInfo;
				
				$("#full_name").html(personal.full_name);
				$("#father_name").html(personal.father_name);
				$("#category_name").html(data.customer_category_name);
				$("#area_name").html(data.area_name);
				$("#isMetered").html(connection.isMetered_name);
				$("#mobile").html(personal.mobile);
				
				var ads="Division : "+address.division_name+", District :"+address.district_name+", Upazila :"+address.upazila_name;
				ads+=", Post Office :"+address.post_office+", Post Code :"+address.post_code+", Road/Street # :"+address.road_house_no;
				ads+=", "+address.address_line1+", "+address.address_line2;
				
	   		  	$("#address").html(ads);
	   		  
	   			$('.img-holder').html("<img class='img-avatar' alt='' src='"+personal.img_url+"' id='img_photo'>");
	   			$('#img_photo').safeUrl({wanted:"http://localhost/JGTDSL_PHOTO/customer/"+personal.customer_id+".jpg",rm:"http://localhost/JGTDSL_PHOTO/anon_user.png"})
	   			
	   			
	   			if(connection.isMetered_name=="Non-Metered")
	   			{
	   			  	setPanelCaption("rightSpan_caption","Infomration");
	   			  	$('#content_div').html(msgDiv.replace("MESSAGE", "Sorry, this feature is not for Metered Customer."));
					$('#msg_span').addClass('animated bounceInLeft');	   			  
	   			}
	   			else
	   			{
	   			   setPanelCaption("rightSpan_caption","METER READING");
	   			   ajaxLoad("content_div","indMeterReadingEntryForm.action");
	   			}
	   				
			}
						
   		  },
   		  error:function(){
   			//$('#ajax-panel').html('<p class="error"><strong>Oops!</strong> Try that again in a few moments.</p>');
   		  }
   	});
}
$( "#customer_id" ).focus();
</script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/pieMenu.js"></script>
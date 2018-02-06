function PieMenuInit(){		
			$('#outer_container').PieMenu({
				'starting_angel':0,
				'angel_difference' : 90,
				'radius':100,
			});			
		}
$(function() {          
	$("#submit_button").click(function() {reset(); }); 
	$( "#outer_container" ).draggable();
	PieMenuInit();
	
});
function reset(){
	if($(".menu_button").hasClass('btn-rotate'))
	$(".menu_button").trigger('click');
	
	$("#info").fadeIn("slow").fadeOut("slow");
	PieMenuInit();
}
function pieMenuCall(type)
{
 if(type=="customer_view")
 	callAction('viewCustomer.action?customer_id='+$("#customer_id").val());
 else if(type=="connection_management")
	 	callAction('connectionManagement.action?customer_id='+$("#customer_id").val());
 else if(type=="new_meter")
	 	callAction('connectionManagement.action?customer_id='+$("#customer_id").val()+"&auto_action=new_meter");
 else if(type=="disconnect")
	 	callAction('connectionManagement.action?customer_id='+$("#customer_id").val()+"&auto_action=disconnect");
 else if(type=="reconnect")
	 	callAction('connectionManagement.action?customer_id='+$("#customer_id").val()+"&auto_action=reconnect");
 else if(type=="burner_change")
	 	callAction('connectionManagement.action?customer_id='+$("#customer_id").val()+"&auto_action=burner_change");
 else if(type=="load_change")
	 	callAction('connectionManagement.action?customer_id='+$("#customer_id").val()+"&auto_action=load_change");

 $(".tip-yellowsimple").hide(); 
}


$('#pie_load').poshytip({
	className: 'tip-yellowsimple',showTimeout: 100,alignTo: 'target',
	alignX: 'center',offsetY: 5,allowTipHover: false,
	content:'Load & Pressure Change'
});
$('#pie_reconnect').poshytip({
	className: 'tip-yellowsimple',showTimeout: 100,alignTo: 'target',
	alignX: 'center',offsetY: 5,allowTipHover: false,
	content:'Reconnect'
});
$('#pie_disconnect').poshytip({
	className: 'tip-yellowsimple',showTimeout: 100,alignTo: 'target',
	alignX: 'center',offsetY: 5,allowTipHover: false,
	content:'Disconnect'
});
$('#pie_burner').poshytip({
	className: 'tip-yellowsimple',showTimeout: 100,alignTo: 'target',
	alignX: 'center',offsetY: 5,allowTipHover: false,
	content:'Burner Change'
});
$('#pie_meter').poshytip({
	className: 'tip-yellowsimple',showTimeout: 100,alignTo: 'target',
	alignX: 'center',offsetY: 5,allowTipHover: false,
	content:'New Meter'
});



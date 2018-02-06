<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/jqueryIdleTimeOut/jquery.idletimer.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/jqueryIdleTimeOut/jquery.idletimeout.js"></script>

<div id="dialog" title="Your session is about to expire!">
	<p>
		<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
		You will be logged off in <span id="dialog-countdown" style="font-weight:bold"></span> seconds.
	</p>
	
	<p>Do you want to continue your session?</p>
</div>

<script type="text/javascript">
// setup the dialog
$("#dialog").dialog({
	autoOpen: false,
	modal: true,
	width: 400,
	height: 200,
	closeOnEscape: false,
	draggable: false,
	resizable: false,
	buttons: {
		'Yes, Keep Working': function(){
			$(this).dialog('close');
		},
		'No, Logoff': function(){
			// fire whatever the configured onTimeout callback is.
			// using .call(this) keeps the default behavior of "this" being the warning
			// element (the dialog in this case) inside the callback.
			$.idleTimeout.options.onTimeout.call(this);
		}
	}
});
// cache a reference to the countdown element so we don't have to query the DOM for it on each ping.
var $countdown = $("#dialog-countdown");
// start the idle timer plugin
$.idleTimeout('#dialog', 'div.ui-dialog-buttonpane button:first', {
	idleAfter: 30*60,
	pollingInterval: 25*60,
	keepAliveURL: 'dashBoard.action',
	serverResponseEquals: 'OK',
	onTimeout: function(){
		window.location = "logout.action";
	},
	onIdle: function(){
		$(this).dialog("open");
	},
	onCountdown: function(counter){
		$countdown.html(counter); // update the counter
	}
});
	        
</script>

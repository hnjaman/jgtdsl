<div id="gbox_gridTable" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 778px;">
<div id="gview_gridTable" class="ui-jqgrid-view">
	<div class="ui-jqgrid-titlebar ui-jqgrid-caption ui-widget-header ui-corner-top ui-helper-clearfix" style="padding-left: 8px;">
		<span class="ui-jqgrid-title">Bill Month Entry</span>
	</div>
	<div class="ui-jqgrid-bdiv" style="width: 778px;height: 100%;padding: 5px;">
			<div class="row-fluid" style="margin-top: 10px;">
					<div class="span12">
										                                            					                                            
						<div class="row-fluid">
							<div class="span6">
								<label style="width: 20%;padding-left: 10px;">Amount</label>
								<input type="text" id="rate" tabindex="1" style="width: 60%"  disabled="disabled"/>							
						  	</div>
						  	
						</div>				                                            					                                            
					</div>
			</div>
	</div>		
</div>
</div>

<table id="advance_grid"></table>
<div id="advance_grid_pager"></div>

<script type="text/javascript">
  $.ajax({
    url: "getTariffForDomesticCustomer.action?customer_id="+$('#customer_id').val(),
  	dataType: 'text',		    
    type: 'POST',
    async: true,
    cache: false,
	success: function (response){
    	var rateArr=response.split("#");
    	$("#rate").val(rateArr[0]);
    	
    }    
  });

</script>
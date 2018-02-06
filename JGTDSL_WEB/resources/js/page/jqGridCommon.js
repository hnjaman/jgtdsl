function resizeGrid() {

    if (grid = $('.ui-jqgrid-btable:visible')) {
        grid.each(function(index) {
            gridId = $(this).attr('id');
            gridParentWidth = $('#gbox_' + gridId).parent().width();
            $('#' + gridId).setGridWidth(gridParentWidth-3);
        });
    };
}
/* for center align of dialog box*/
$(document).ready(function ()
		{
		    $.jgrid.jqModal = $.extend($.jgrid.jqModal || {}, {
		        beforeOpen: centerInfoDialog
		    });
		    resizeGrid();
		});


$(window).bind('resize', function() {
    setTimeout(resizeGrid, 500);
});

$(".ui-layout-center").attrchange({
    trackValues: true,
    /*
     * Default to false, if set to true the event object is updated with old and
     * new value.
     */
    callback: function(event) {
        setTimeout(resizeGrid, 500);
    }
});

//No use of it right now. We can remove that later on
function getErrorBlockHTML(msg)
{
	return "<div class='ui-state-error ui-corner-all' style='padding:3px;'>"+
		 "<strong><span><img src='/JGTDSL_WEB/resources/images/red-alert.png' width='17' height='17' />&nbsp;</span>Error Information :-</strong><br/>" +
		 msg+
		 "</div>";
}

$("#gridPager_left").css("width", "");

$("#editButton").off();
$("#editButton").click(function() {
	var gr = jQuery("#gridTable").jqGrid('getGridParam', 'selrow');
	
	if (gr != null){
		jQuery("#gridTable").jqGrid('editGridRow', gr, editParams);
	}
	else
	    alert("Please Select Row");
	});

$("#viewButton").off();
$("#viewButton").click(function() {
	var gr = jQuery("#gridTable").jqGrid('getGridParam', 'selrow');
	if (gr != null)
	    jQuery("#gridTable").jqGrid('viewGridRow', gr, viewParams);
	else
	    alert("Please Select Row");
	});

$("#newButton").off();
$("#newButton").on("click",function(){
	jQuery("#gridTable").editGridRow("new",addParams);
});

$("#deleteButton").off();
$("#deleteButton").click(function() {

	var gr = jQuery("#gridTable").jqGrid('getGridParam', 'selrow');
	if (gr != null)
		$('#gridTable').jqGrid('delGridRow',gr,deleteParams);
	else
	    alert("Please Select Row");
	});



function centerInfoDialog()
{
    var $infoDlg = $("#info_dialog");
    var $parentDiv = $infoDlg.parent();
    var dlgWidth = $infoDlg.width();
    var parentWidth = $parentDiv.width();

    $infoDlg[0].style.left = Math.round((parentWidth - dlgWidth) / 2) + "px";
}

var jqDialogParam={
        zIndex: 1500,
        width:450,
         beforeOpen: centerInfoDialog,
         afterOpen:disableOnClick,
         onClose: function () {
        	$('.ui-widget-overlay').unbind( "click" );
            return true; // allow closing
        }
        
};
var jqDialogClose=$.jgrid.edit.bClose;
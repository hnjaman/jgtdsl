function showDuesBill(){
	$.nsWindow.open({
		movable:true,
		title: 'Bill Dues',
		width: 800,
		height: 500,
		titleBarHeight:10 ,
		dataUrl: 'getDueBillList.action?customer_id='+$("#customer_id").val(),
		theme:jsVar.MODAL_THEME
    });
    
}
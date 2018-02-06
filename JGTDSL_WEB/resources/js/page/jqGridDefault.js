var row_num=($("#contentPanel").height()/27).toFixed();
row_num=parseInt(row_num);	// #contentPanel in common.css
var grid_default={
	    datatype: 'json',
	    mtype: 'GET',
		rownumbers:true,
		altRows:true,
		altclass:'altRowClass',
		cmTemplate: {editable: true, editrules: {edithidden: true}}, //This rule is necessary to show a hidden column in add,edit,view mode
        multiselect: false,
        pager: '#gridPager',
        rowNum: row_num,
        height: $("#contentPanel").height()-jsVar.GH_DEDUCT, // GH_DEDUCT:87,/*Grid Height Deduct */
        width: $("#contentPanel").width()-jsVar.GW_DEDUCT,	// GW_DEDUCT:5,/*Grid Width Deduct */
        rowList: jsVar.PAGER_COMBO,	// PAGER_COMBO:[5,10,15,20,25,40,50],
        viewrecords: true,
        gridview: true,
        rowattr: function (rd) {
            if (rd.status === "0") {
                return {"class": "redBackgroundRow"};
            }
        },
        ondblClickRow: function(rowid) {
          	 jQuery(this).jqGrid('viewGridRow', rowid,
                       {width:jsVar.CRUD_WINDOW_WIDTH,closeOnEscape:true});
          }							// CRUD_WINDOW_WIDTH:600,/* For JQ Grid Add,Edit,View Window */
	 };
jQuery.extend(jQuery.jgrid.defaults, grid_default);

			/*****  jQuery.jgrid.defaults  *****
			 $.jgrid = {
				defaults : {
					recordtext: "View {0} - {1} of {2}",
						emptyrecords: "No records to view",
					loadtext: "Loading...",
					pgtext : "Page {0} of {1}"
				},
			...
			} */
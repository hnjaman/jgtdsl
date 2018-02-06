<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("installmentBillingHome.action");
	setTitle("Installment Bill");
</script>
<style>
.ui-datepicker-calendar{
	display:none;
}
</style>

    <br/>
    &nbsp;&nbsp;&nbsp;Is Metered? <input type="text" value="1" id="isMetered" />
	<br/>
	&nbsp;&nbsp;&nbsp;Total Installment <input type="text" value="13" id="total_installment" />
	<br/>
	&nbsp;&nbsp;&nbsp;Pending Bill Ids, Pending Bill Months<input type="text" value="123|January, 2015#456|February, 2015" id="pending_bills"  />
	<br/>
	<button onclick="PrepareInstallments()">Next</button>
	<br/>
    
      <!--
     <button id="add_installment">Add Installment</button>
       -->
     <button id="add_segment">Add Segment</button>
    
    
    <button onclick="ExpandCollspase('expand')">Expand</button>
    <button onclick="ExpandCollspase('collapse')">Collapse</button>
    
    
    

    <script type="text/javascript">
    var billSelectBox="<select>"+
                         "<option value='1234'>January, 2015</option>"+
                         "<option value='4567'>February, 2015</option>"+
                      "</select>";
    
    $(document).ready(function () {
        function showSubGrid(subgrid_id, row_id) {
            var html = "Hello!";
            $("#" + subgrid_id).append(html);
        }
    
        var mydata = [{ id:1,
    			   serial:1,
    			   desc:"a",
    			   due_date:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
    			   total:"",
    			   installment_dtl:""}];
        
        var grid = $("#list");
         myDelOptions = {
                // because I use "local" data I don't want to send the changes to the server
                // so I use "processing:true" setting and delete the row manually in onclickSubmit
                onclickSubmit: function (rp_ge, rowid) {
                    var grid_id = $.jgrid.jqID(this.id)
                    // reset the value of processing option to true to
                    // skip the ajax request to 'clientArray'.
                    rp_ge.processing = true;

                    // we can use onclickSubmit function as "onclick" on "Delete" button
                    // delete row
                   var selRow = $('#'+rowid),   // get the row (<tr> element having id=rowid)
    nextRow = selRow.next(); // get the next row

                    var rowData = jQuery("#treegrid").getRowData(rowid);
					
					
					if (nextRow.hasClass('ui-subgrid')) {
    // if the next row is a subgrid one should remove it
    nextRow.remove();
}

					 grid.jqGrid('delRowData', rowid);
					 
					
					 
                    $("#delmod" + grid[0].id).hide();
                    
                    $.jgrid.hideModal("#delmod" + grid_id, {
                            gb: "#gbox_" + grid_id,
                            jqm: rp_ge.jqModal,
                            onClose: rp_ge.onClose
                        });
                    
                    

                    return true;
                },
                processing: true
            };
            
       
    			   
            
        grid.jqGrid({
            data: mydata,
            datatype: "local",
            colNames: ['Id','Serial', 'Desc.', 'Due Date', 'Principal', 'Surcharge', 'Meter Rent', 'Total',"Installment Dtl.","Edit","Delete"],
            colModel: [
                { name: 'id', index: 'id', key: true, width: 70, sorttype: "int",hidden:true },
                { name: 'serial', index: 'serial', key: true, width: 70, sorttype: "int" ,align:'center'},
                { name: 'desc', index: 'desc', width: 140, sorttype: "date" },
                { name: 'due_date', index: 'due_date', width: 120 },
                { name: 'principal', index: 'principal', width: 100, align: "right", sorttype: "float" },
                { name: 'surcharge', index: 'surcharge', width: 100, align: "right", sorttype: "float" },
                { name: 'meter_rent', index: 'total', width: 100, align: "right", sorttype: "float" },
                { name: 'total', index: 'total', width: 150, sortable: false },                
                { name: 'installment_dtl', index: 'due_date', width: 100 ,hidden:true },
                { 
            		name: 'edit', 
            		width: 60, 
            		align:'center',
            		formatter:function(){
                          return "<span class='ui-icon ui-icon-pencil' style='margin-left:18px;cursor:pointer'></span>"
                    },
                    cellattr: function (rowId, tv, rowObject, cm, rdata) {
                            return ' onClick="editInstallment(\''+rowId+'\')"';
                    }
                },
                { name: 'delete', index: 'delete', width: 60, align: 'center', sortable: false, formatter: 'actions',editable: false,
                        formatoptions: {
                            keys: true, // we want use [Enter] key to save the row and [Esc] to cancel editing.
                            delOptions: myDelOptions
                        }
                    }
            ],
            pager: '#pager',
            rowNum: 10,
            rowList: [5, 10, 20, 50],
            sortname: 'id',
            sortorder: 'asc',
            viewrecords: true,
            height: "100%",
            caption: "Custom Sorting Color",
            subGrid: true,
            subGridRowExpanded: function(subgrid_id, row_id) {
                
                var a=getFieldValueFromSelectedGridRow("list","installment_dtl");
                var tableId="installment_"+row_id;
                                           
                var html=  	 '   <table id="'+tableId+'" class="hor-minimalist-b" summary="Employee Pay Sheet"  style="float:left">  '  + 
							 '       <thead>  '  + 
							 '       	<tr>  '  + 
							 '           	<th scope="col" width="25%">Monty, Year</th>  '  + 
							 '               <th scope="col"  width="15%" style="text-align:center;">Principal</th>  '  + 
							 '               <th scope="col"  width="15%" style="text-align:center;">Surcharge</th>  '  + 
							 '               <th scope="col"  width="15%" style="text-align:center;">Meter Rent</th>  '  + 
							 '               <th scope="col"  width="20%" style="text-align:center;">Total</th>  '  +
							 '               <th scope="col"  width="10%" style="text-align:center;">Delete</th>  '  +
							 '           </tr>  '  + 
							 '       </thead>  '  + 
							 '       <tbody>  '  ;
                if(a!=""){                

					 html +=  		 '       	<tr>  '  + 
							 '           	<td>Stephen C. Cox</td>  '  + 
							 '               <td>$300</td>  '  + 
							 '               <td>$50</td>  '  + 
							 '               <td>Bob</td>  '  + 
							 '               <td><a href="javascript:void(0);" id="x1" onclick="deleteRow(\'x1\')" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></td> '+
							 '           </tr>  ';
							 
							 }
				else{
				html+=getNewRow();
				} 
						html+='       </tbody>  '  + 
							 '  </table>  '+
							 '   <div style="float:left;padding:10px;"><a href="javascript:void(0);" onclick="addInstallmentDetailRow(\''+tableId+'\')"><img src="/JGTDSL_WEB/resources/images/icons/plus.png" /></a></div> '; 
                
                $("#" + subgrid_id).append(html);
            },
            gridComplete: function () {
                $( ".ui-pg-div.ui-inline-edit" ).remove();
                $( ".ui-pg-div.ui-inline-save" ).remove();
                $( ".ui-pg-div.ui-inline-cancel" ).remove();
                
                }
        });
        grid.jqGrid('navGrid', '#pager', { add: false, edit: false, del: false, search: false, refresh: true });
        
         var subGridOptions = grid.jqGrid("getGridParam", "subGridOptions"),
                plusIcon = subGridOptions.plusicon,
                minusIcon = subGridOptions.minusicon,
                expandAllTitle = "Expand all subgrids",
                collapseAllTitle = "Collapse all subgrids";
            $("#jqgh_" + grid[0].id + "_subgrid")
                .html('<a style="cursor: pointer;"><span class="ui-icon ' + plusIcon + '" title="' + expandAllTitle + '"></span></a>')
                .click(function () {
                    var $spanIcon = $(this).find(">a>span"),
                        $body = $(this).closest(".ui-jqgrid-view")
                            .find(">.ui-jqgrid-bdiv>div>.ui-jqgrid-btable>tbody");
                    if ($spanIcon.hasClass(plusIcon)) {
                        $spanIcon.removeClass(plusIcon)
                            .addClass(minusIcon)
                            .attr("title", collapseAllTitle);
                        $body.find(">tr.jqgrow>td.sgcollapsed")
                            .click();
                    } else {
                        $spanIcon.removeClass(minusIcon)
                            .addClass(plusIcon)
                            .attr("title", expandAllTitle);
                        $body.find(">tr.jqgrow>td.sgexpanded")
                            .click();
                    }
                });
    });
    //]]>
   
   function editInstallment(rowId){
    //show dueTextBox
    //show serialTextBox
    //Show Description Calendar
    var rowData = jQuery("#list").getRowData(rowId);	
	var serial=rowData["serial"];
	var dueDate=rowData["due_date"];
	var desc=rowData["desc"];
	
	
	var dueDateId="due_date_"+rowId;
	var serialId="serial_"+rowId;
	var descId="desc_"+rowId;
	
	
	if(serial.indexOf("<input")==0) return; 
	
     var  textBox_dueDate = "<input type='text' id='"+dueDateId+"'  value='"+dueDate+"' style='width:100px;text-align:center;' />";
     var  textBox_serial = "<input type='text' id='"+serialId+"' value='"+serial+"' style='width:60px;text-align:center;' />";
     var  textBox_desc = "<input type='text' id='"+descId+"' value='"+desc+"' style='width:130px;text-align:left;' />";
      
        $("#list").jqGrid('setRowData', rowId, { 'due_date': textBox_dueDate });
        $("#list").jqGrid('setRowData', rowId, { 'serial': textBox_serial });
        $("#list").jqGrid('setRowData', rowId, { 'desc': textBox_desc });
        
        Calendar.setup($.extend(true, {}, calOptions,{
    inputField : dueDateId,
    trigger    : dueDateId}));
    
    $('#'+descId).datepicker(monthYearCalOptions);
    
   }
   function addInstallmentDetailRow(tableId){
   
   $("#"+tableId).append(getNewRow());
   }
   
							 
   function getNewRow(){
   	return '       	<tr>  '  + 
							 '           	<td>'+billSelectBox+'</td>  '  + 
							 '               <td style="text-align:center;"><input type="text" id="" style="width:80px;text-align:right;" /></td>  '  + 
							 '               <td style="text-align:center;"><input type="text" id="" style="width:80px;text-align:right;" /></td>  '  + 
							 '               <td style="text-align:center;"><input type="text" id="" style="width:80px;text-align:right;" /></td>  '  + 
							 '               <td style="text-align:center;"><input type="text" id="" style="width:80px;text-align:right;" /></td>  '  +
							 '               <td style="text-align:center;"><a href="javascript:void(0);" id="x1" onclick="deleteRow(\'x1\')" class="remCF"><img src="/JGTDSL_WEB/resources/images/delete_16.png" /></a></td> '+
							 '           </tr>  ';
   }
   function deleteRow(abc){
   console.log(abc);
   $("#"+abc).parent().parent().remove();
   }
       


   $("#add_installment").click(function () {
   
   var flag=false;
   var rowId =$("#list").jqGrid('getGridParam','selrow');  
var rowData = jQuery("#list").getRowData(rowId);

var gridRow=$("#list").getGridRowById(rowId);
var gridColumn=$("#"+rowId).find(">td.ui-sgcollapsed");
if(gridColumn.hasClass( "sgexpanded" )){
	$("#list").collapseSubGridRow(rowId);
	flag=true;
}
var newRowId=rowId+"_"+Math.floor((Math.random() * 9999) + 1);
  var newRow={ id: newRowId, invdate: "2007-10-01", name: "test", note: "note", amount: "200.00", tax: "10.00", total: "210.00" };  
                jQuery("#list").addRowData(newRowId, newRow, "after", rowId); 
                 $("#list").expandSubGridRow(newRowId);
                
                if(flag==true){
                $("#list").expandSubGridRow(rowId);
                }
});

function getAllRows(){

var allRowsInGrid = $('#list').getGridParam('data');
console.log(allRowsInGrid);

}

/*------*/
  function PrepareInstallments(){
    	var installmentMonthsArr=monthsBetweenTwoMonth("January 2015",$("#total_installment").val());
    	 var gridData=[];
    	for(var i=0;i<installmentMonthsArr.length;i++)
    	{
    	        
                    
    	var data={ id:i,
    			   serial:i+1,
    			   desc:installmentMonthsArr[i],
    			   due_date:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
    			   total:"",
    			   installment_dtl:""};
               
    	gridData.push(data);
    	}
    	
                console.log(gridData);
                reloadGridWithData(gridData,"list");                               
    }
 
    </script>
    <table id="list"><tr><td/></tr></table>
<div id="pager"/>
<button id="add_installment">Add Installment</button>
<button onclick="getAllRows()">Get AllRow</button>

http://www.ok-soft-gmbh.com/jqGrid/SubgridWithLocalGrid_.htm
http://www.ok-soft-gmbh.com/jqGrid/SubGrid.htm
http://stackoverflow.com/questions/27534105/labels-become-textboxes-on-click-edit-of-specific-row
    
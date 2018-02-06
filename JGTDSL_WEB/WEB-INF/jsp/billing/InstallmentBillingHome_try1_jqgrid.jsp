<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("installmentBillingHome.action");
	setTitle("Installment Bill");
</script>


<style>
.ui-pg-div.ui-inline-del{
padding-left:10px;
padding-right:10px;
}
</style>

    <script type="text/javascript">
    //<![CDATA[
        $(function(){
            var mydata = [{id:'1',
    			   name:"Jan, 2016",
    			   due_date:"",
    			   bill_id:"",
    			   bill:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
                  level:"0", parent:"null",  isLeaf:false, expanded:true},
                  {id:'2',
    			   name:"aaa, 2016",
    			   due_date:"",
    			   bill_id:"",
    			   bill:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
                  level:"1", parent:"1",  isLeaf:true, expanded:false}
                ],
                grid = $("#treegrid"),
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
                   

                    var rowData = jQuery("#treegrid").getRowData(rowid);
					var parent = rowData['parent'];
					var isLeaf=rowData['isLeaf'];
					if(isLeaf=="false"){

					var localRow = $('#treegrid').jqGrid('getLocalRow', rowid);
    var childRows =  $('#treegrid').jqGrid('getNodeChildren', localRow);
    
						
						
				        for (var j = 0; j < childRows.length; j++) {
				        	grid.jqGrid('delRowData', childRows[j].id);
				            $("#delmod" + grid[0].id).hide();     
				        }
					}
					
					 grid.jqGrid('delRowData', rowid);
                    $("#delmod" + grid[0].id).hide();
                    
                    $.jgrid.hideModal("#delmod" + grid_id, {
                            gb: "#gbox_" + grid_id,
                            jqm: rp_ge.jqModal,
                            onClose: rp_ge.onClose
                        });
                    
                    

                    if (grid[0].p.lastpage > 1) {
                        // reload grid to make the row from the next page visable.
                        // TODO: deleting the last row from the last page which number is higher as 1
                        grid.trigger("reloadGrid", [{ page: grid[0].p.page}]);
                    }

                    return true;
                },
                processing: true
            };
                
                

            grid.jqGrid({
                datatype: "local",
                data: mydata, // will not used at the loading,
                              // but during expanding/collapsing the nodes
                colNames:["id","Installment","Due Date","Bill Id","Bill","Principal","Surcharge","Meter Rent","Total","Delete"],
                colModel:[
                                 
                    {name:'id', index:'id', width:1, hidden:true, key:true},
                    {name:'name', index:'name', width:180,editable: true},
                    {name:'due_date', index:'due_date', width:80, align:"center"},
                    {name:'bill_id', index:'bill_id', hidden:true},
                    {name:'bill', index:'bill', width:100,align:"left"},
                    {name:'principal', index:'principal', width:80,align:"right"},
                    {name:'surcharge', index:'surcharge', width:80,align:"right",
                    	cellattr: function (rowId, tv, rawObject, cm, rdata) {                    	    
                        	return rawObject.isLeaf==true ? 'style="background-color:LightGreen"' : 'style="background-color:Tomato"';
                    	}
                    },
                    {name:'meter_rent', index:'meter_rent', width:80,align:"right"},
                    {name:'total', index:'total', width:80,align:"right"},

                     { name: 'delete', index: 'delete', width: 75, align: 'center', sortable: false, formatter: 'actions',editable: false,
                        formatoptions: {
                            keys: true, // we want use [Enter] key to save the row and [Esc] to cancel editing.
                            delOptions: myDelOptions
                        }
                    },
                ],
                 cmTemplate: { editable: true },
                 cellEdit: true,	
                height:'100%',
                rowNum: 10000,
                //pager : "#ptreegrid",
                sortname: 'id',
                treeGrid: true,
                treeGridModel: 'adjacency',
                treedatatype: "local",
                ExpandColumn: 'name',
                caption: "Demonstrate how to use Tree Grid for the Adjacency Set Model" ,
                gridComplete: function () {
                $( ".ui-pg-div.ui-inline-edit" ).remove();
                $( ".ui-pg-div.ui-inline-save" ).remove();
                $( ".ui-pg-div.ui-inline-cancel" ).remove();
                
                }
            });
            // we have to use addJSONData to load the data
            grid[0].addJSONData({
                total: 1,
                page: 1,
                records: mydata.length,
                rows: mydata
            });
        });
        
        
    //]]>
    
    function ExpandCollspase(operation) {
    
    var operationNode=operation+"Node";
    var operationRow=operation+"Row";    
    
   var rows = $("#treegrid").jqGrid('getRootNodes');
   console.log(rows.length);
   for (var i = 0; i < rows.length; i++){
        var childRows = $("#treegrid").jqGrid('getNodeChildren', rows[i]);
        $("#treegrid").jqGrid(operationNode, rows[i]);    
        $("#treegrid").jqGrid(operationRow, rows[i]);
        for (var j = 0; j < childRows.length; j++) {
            $("#treegrid").jqGrid(operationNode, childRows[j]);    
            $("#treegrid").jqGrid(operationRow, childRows[j]);
        }
    }
}

   $("#add_installment").click(function () {
   
   var rowId =$("#treegrid").jqGrid('getGridParam','selrow');  
var rowData = jQuery("#treegrid").getRowData(rowId);

var parent = rowData['parent'];
var isLeaf=rowData['isLeaf'];
  var ret;
if(isLeaf=="false"){
 ret = {"error":"","total":1,"page":1,"records":1,"rows":[{ id:'2222',
    			   name:"FEB, 2016",
    			   due_date:"",
    			   bill_id:"",
    			   bill:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
                  level:"0", parent:"null",  isLeaf:false, expanded:true }]};  
                jQuery("#treegrid").addRowData(1234, ret.rows[0], "after", rowId);        
                
}
});
   $("#add_segment").click(function () {
   
   var rowId =$("#treegrid").jqGrid('getGridParam','selrow');  
var rowData = jQuery("#treegrid").getRowData(rowId);

var parent = rowData['parent'];

var isLeaf=rowData['isLeaf'];

alert(rowId+"----"+parent);

if(isLeaf=="true"){

  var newRow={ id:'3333',
    			   name:"Jan, 2016",
    			   due_date:"",
    			   bill_id:"",
    			   bill:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
                  level:"1", parent:parent,  isLeaf:true, expanded:true };
                  
                $("#treegrid").jqGrid ('addChildNode', newRow.id, newRow.parent, newRow);
                
}
else{

  var newRow={ id:'3333',
    			   name:"AAA",
    			   due_date:"",
    			   bill_id:"",
    			   bill:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
                   level:"1", parent:rowId,  isLeaf:true, expanded:false};
                  
console.log(newRow.id);
console.log(rowId);
console.log(newRow);
				$("#treegrid").jqGrid ('addChildNode', newRow.id, newRow.parent, newRow);
}
            
 
            });


         

    </script>
    <br/>
    &nbsp;&nbsp;&nbsp;Is Metered? <input type="text" value="1" id="isMetered" />
	<br/>
	&nbsp;&nbsp;&nbsp;Total Installment <input type="text" value="13" id="total_installment" />
	<br/>
	<button onclick="PrepareInstallments()">Next</button>
	<br/>
    <table id="treegrid"><tr><td/></tr></table>
    <div id="pager"></div>
    <div id="ptreegrid"></div>
      <!--
     <button id="add_installment">Add Installment</button>
       -->
     <button id="add_segment">Add Segment</button>
    
    
    <button onclick="ExpandCollspase('expand')">Expand</button>
    <button onclick="ExpandCollspase('collapse')">Collapse</button>

    <script type="text/javascript">
    
    function PrepareInstallments(){
    	var installmentMonthsArr=getInstallmentMonthsArray("January 2015",$("#total_installment").val());
    	 var gridData=[];
    	for(var i=0;i<installmentMonthsArr.length;i++)
    	{
    	        
                    
    	var data={ id:i,
    			   name:installmentMonthsArr[i],
    			   due_date:"",
    			   bill_id:"",
    			   bill:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
                  level:"0", parent:"null",  isLeaf:false, expanded:true };
                  var sub={ id:i+"_1",
    			   name:installmentMonthsArr[i]+"_1",
    			   due_date:"",
    			   bill_id:"",
    			   bill:"",
    			   principal:"",
    			   surcharge:"",
    			   meter_rent:"",
                  level:"1", parent:i,  isLeaf:true, expanded:true };
    	gridData.push(data);
    	gridData.push(sub);
    	}
    	
                
                reloadGridWithData(gridData,"treegrid");                               
    }

function getInstallmentMonthsArray(from, totalNumberOfInstallment) {

    var arr = [];
    var datFrom = new Date('1 ' + from);
    var datTo =new Date(new Date(datFrom).setMonth(datFrom.getMonth()+parseInt(totalNumberOfInstallment)-1));
    var fromYear =  datFrom.getFullYear();
    var toYear =  datTo.getFullYear();
    var diffYear = (12 * (toYear - fromYear)) + datTo.getMonth();
    for (var i = datFrom.getMonth(); i <= diffYear; i++) {
        arr.push(monthNames[i%12] + " " + Math.floor(fromYear+(i/12)));
    }        

    return arr;
}

    
    </script>
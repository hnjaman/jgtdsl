<div id="gbox_gridTable" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 1200px;">
<div id="gview_gridTable" class="ui-jqgrid-view">
	<div class="ui-jqgrid-titlebar ui-jqgrid-caption ui-widget-header ui-corner-top ui-helper-clearfix" style="padding-left: 8px;">
		<span class="ui-jqgrid-title">Reconcilation Entry</span>
	</div>
	<div class="ui-jqgrid-bdiv" style="width: 778px;height: 100%;padding: 5px;">
			<div class="row-fluid" style="margin-top: 10px;">
					<div class="span12">
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 14.5%">Particulars</label>
								<textarea rows="1" style="width: 80%" name="" id="pariculars" ></textarea> 													
                                </textarea>		
						  	</div>
						  	
						</div>				                                            					                                            
						<div class="row-fluid">
						    <div class="span4">									    
								<label style="width: 30%; padding-left: 10px">ADD/Less:</label>
									<select id="add_less" style="width: 56%;"   name="add_less" ">
									<option value="" >Select Criteria</option>									
										<option value="add"  selected="selected">Add</option>
										<option value="less" >Less</option>										
								</select> 								
							</div>
							<div class="span4">
								<label style="width: 20%;padding-left: 10px;">Amount</label>
								<input type="text" id="amount" tabindex="3" style="width: 30%"  />							
						  	</div>
						  	<div class="span4">									    
								<button class="btn btn-beoro-3"  tabindex="4" type="button" id="btn_cancel" onclick="addAdvanceCollectionRow()">
								<span class="splashy-box_add"></span>
								Add</button>   							
							</div>
						</div>				                                            					                                            
					</div>
			</div>
	</div>		
</div>
</div>

<table id="advance_grid"></table>
<div id="advance_grid_pager"></div>
<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;border-top: 1px dashed #ddd;height: 36px;">
<table width="100%">
<tr>
	<td  width="60%" align="left">
		<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveExtraReconcilation()">
		<span class="splashy-document_letter_okay"></span>
		Save</button>
		
	</td>
	<td  width="40%" align="right">
		<button class="btn btn-beoro-3"  type="button" id="close_btn" onclick="closeModal()">
		<span class="splashy-application_windows_edit"></span>
		Close</button>
	
	</td>
</tr>
</table>					
</div> 
<script type="text/javascript">
focusNext("pariculars");

  
  $("#advance_grid").GridUnload();


$("#advance_grid").jqGrid({
    data: [],
    datatype: "local",
    colNames: ['Particulars',"Debit","Credit","Action"],
    colModel: [
        {name:'particulars',index:'particulars', width:320,sortable:false},
    	{name: 'debit',index: 'debit',editable: true,align:'right',width:80},
    	{name: 'credit',index: 'credit',editable: true,align:'center',width:80},
    	{name: 'action',index: 'action',editable: true,align:'center',width:30}
    	],
    pager: '#pager',
    width: 778,
    height: 276,  
    rownumbers: true,    
    footerrow: true
	});
$("#advance_grid_pager").hide();
$('#advance_grid').jqGrid('clearGridData');


var monthNames = [ "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December" ];

function diff(from, to) {
    var arr = [];
    var datFrom = new Date('1 ' + from);
    var datTo = new Date('1 ' + to);
    var fromYear =  datFrom.getFullYear();
    var toYear =  datTo.getFullYear();
    var diffYear = (12 * (toYear - fromYear)) + datTo.getMonth();

    for (var i = datFrom.getMonth(); i <= diffYear; i++) {
        arr.push(monthNames[i%12] + " " + Math.floor(fromYear+(i/12)));
    }        

    return arr;
}


var i=0;
function addAdvanceCollectionRow(){

			var particulars=$("#pariculars").val();
			var amount=$("#amount").val();
			var add_amount=0;
			var less_amount=0;
			var add_less=$("#add_less").val();
			if(add_less=="add")
			{ 
			 add_amount=amount=="" ? "0" : amount;
			}else
			{
			less_amount=amount=="" ? "0" : amount;;
			}
            /* particulars= "<input style='height:20px;width:320px;text-align:right;' id='particulars_"+i+"' class='recon' type='text' value='"+particulars+"'  />"; 
            debit= "<input style='height:20px;width:80px;text-align:right;' id='debit_"+i+"' class='recon' type='text' value='"+add_amount+"'  />"; 
            credit = "<input style='height:20px;width:80px;text-align:right;' id='credit_"+i+"' class='recon' type='text' value='"+less_amount+"'  />";  */
           
			del="<a style='cursor:pointer;' onclick=\"$('#advance_grid').jqGrid('delRowData',"+i+");\"><img src='/JGTDSL_WEB/resources/images/delete_16.png' /></a>"	        
			var customer_data = [{"particulars":particulars,"debit":add_amount,"credit":less_amount,"action":del}];
			jQuery("#advance_grid").jqGrid('addRowData',i,customer_data[0],"last");
			i++;
			
			clearField("pariculars","amount");
			focusNext("pariculars");

}

function saveExtraReconcilation()
{
var rows = jQuery("#advance_grid").getDataIDs();
var extraReconcilation="";
 for(a=0;a<rows.length;a++)
 {
    
    row=jQuery("#advance_grid").getRowData(rows[a]);
    extraReconcilation+=row.particulars+"#"+row.debit+"#"+row.credit;
    extraReconcilation+="@";
    

 }
 alert(extraReconcilation);

}
</script>
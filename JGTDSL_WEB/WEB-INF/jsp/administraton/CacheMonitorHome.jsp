<%@page import="java.text.NumberFormat"%>
<script  type="text/javascript">
	navCache("cacheMonitorHome.action");
</script>
<style>
.mypointer  { cursor:pointer }
.table th, .table td{padding: 6px !important;}
#gs_key{
height: auto !important;
}
.ui-jqgrid .ui-jqgrid-htable th div{
padding-bottom: 2px;
}

.shadow_effect
{
  	position:relative;       
    -webkit-box-shadow:0 1px 4px rgba(0, 0, 0, 0.5), 0 0 40px rgba(0, 0, 0, 0.1) inset;
       -moz-box-shadow:0 1px 4px rgba(0, 0, 0, 0.5), 0 0 40px rgba(0, 0, 0, 0.1) inset;
            box-shadow:0 1px 4px rgba(0, 0, 0, 0.5), 0 0 40px rgba(0, 0, 0, 0.1) inset;
}
.shadow_effect:before, .shadow_effect:after
{
	content:"";
    position:absolute; 
    z-index:-1;
    -webkit-box-shadow:0 0 20px rgba(0,0,0,0.8);
    -moz-box-shadow:0 0 20px rgba(0,0,0,0.8);
    box-shadow:0 0 20px rgba(0,0,0,0.8);
    top:0;
    bottom:0;
    left:10px;
    right:10px;
    -moz-border-radius:100px / 10px;
    border-radius:100px / 10px;
} 
.shadow_effect:after
{
	right:10px; 
    left:auto;
    -webkit-transform:skew(8deg) rotate(3deg); 
       -moz-transform:skew(8deg) rotate(3deg);     
        -ms-transform:skew(8deg) rotate(3deg);     
         -o-transform:skew(8deg) rotate(3deg); 
            transform:skew(8deg) rotate(3deg);
}

</style>
<div style="width: 65%;float: left;height: 60%;" id="ckey_div">
<table id="resultGrid"><tr><td/></tr></table>
    <div id="pager"></div>
</div>
<div style="width: 33%;float: left;height: 60%;margin-left: 1%;border: none;">
<div id="gbox_gridTable" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 100%;height: 100%">

	<div id="gview_gridTable" class="ui-jqgrid-view" style="width: 100%;height: 100%">
		<div class="ui-jqgrid-titlebar ui-jqgrid-caption ui-widget-header ui-corner-top ui-helper-clearfix" style="padding-left: 8px;">
			<span class="ui-jqgrid-title">Cache & Memory Status</span>
		</div>

		<div class="ui-jqgrid-bdiv" style="width: 99%;height: 100%;padding: 5px;">
				<div class="row-fluid">
				<div class="span10 offset1">
                                                <table class="table table-bordered table-hover">
                                                    <thead>
                                                        <tr>                                                            
                                                            <th>Name</th>
                                                            <th>Cache Hit</th>
                                                            <th>Miss Hit</th>
                                                            <th>Clear</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>PCACHE</td>
                                                            <td>1</td>
                                                            <td>11</td>
                                                            <td>
                                                            <button class="btn btn-danger" onclick="clearCache('PCACHE')">Clear</button>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>TOTAL</td>
                                                            <td>1</td>
                                                            <td>11</td>
                                                            <td>
                                                            
                                                            </td>
                                                        </tr>
                                                        
                                                    </tbody>
                                                </table>
                                            </div>
                                            </div>
                                            
                                            <div class="row-fluid">
                                             <div class="span10 offset1">
                                                <table class="table table-bordered">
                                                    <thead>
                                                        <tr>
                                                            <th>Type</th>
                                                            <th>Memory</th>                                                            
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr class="info">
                                                            <td>Maximum</td>
                                                            <td><%= NumberFormat.getInstance().format(Runtime.getRuntime().maxMemory() / (1024*1024)) %> MB</td>
                                                        </tr>
                                                        <tr class="warning">
                                                            <td>Total</td>
                                                            <td><%= NumberFormat.getInstance().format(Runtime.getRuntime().totalMemory() / (1024*1024)) %> MB</td>
                                                        </tr>
                                                        <tr class="error">
                                                            <td>Used</td>
                                                            <td><%= NumberFormat.getInstance().format(Runtime.getRuntime().freeMemory() / (1024*1024)) %> MB</td>
                                                        </tr>
                                                        <tr class="success">
                                                            <td>Free</td>
                                                            <td><%= NumberFormat.getInstance().format(Runtime.getRuntime().freeMemory() / (1024*1024)) %> MB</td>
                                                        </tr>
                                                        
                                                    </tbody>
                                                </table>
                                            </div>
                                            </div>
		</div>		
	</div>
</div>	
</div>

<p style="clear: both;margin-top: 5px;"></p>
<div style="clear: both;height: 38%;width: 99%;">
<div id="gbox_gridTable" class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" dir="ltr" style="width: 100%;height: 96%">

	<div id="gview_gridTable" class="ui-jqgrid-view" style="width: 100%;height: 90%">
		<div class="ui-jqgrid-titlebar ui-jqgrid-caption ui-widget-header ui-corner-top ui-helper-clearfix" style="padding-left: 8px;">
			<span class="ui-jqgrid-title">Cache Key Value</span>
			<div style="float: right;padding-right: 20px;">
			<span id="plain_span" class='splashy-image_modernist' style="cursor: pointer;" onclick="showKeyValue('plain')"></span>
				&nbsp;&nbsp;
			<span id="json_span" class='splashy-box' style="cursor: pointer;" onclick="showKeyValue('json')"></span>
			</div>
		</div>

		<div class="ui-jqgrid-bdiv" style="width: 99%;height: 96%;padding: 5px;" id="key_value_div">
				
                                            
		</div>		
	</div>
</div>
</div>

    <script type="text/javascript">
       var myData;
       var keyValString;
       var keyValJsonObj={};
       $.ajax({
		    url: "getAllKeys.action",
		    type: 'POST',
		    async: false,
		    cache: false,
		    contentType: false,
		    processData: false,
		    success: function (response) {
			                    myData=response.rows;
			                    generateGrid();
		    				}
		  });

function generateGrid()
{
 $("#resultGrid").jqGrid({       
                datatype: "local",
                data: myData,
                localReader: {
		            id: "key"
		        },
                autowidth:true,                
                colNames:['Key','Created On', 'Last Accessed', 'Total Hit','Del.'],
                colModel:[
                    {name:'key',index:'key', sorttype:"text",width:300,cellattr : function( rowId, value, rowbject) { return "class='mypointer'"; },search: true },
                    {name:'created_on',index:'created_on',  sorttype:"text",width:100,align:'center',search: false },
                    {name:'last_accessed_on',index:'last_accessed_on',sorttype:"text",width:100,align:'center',search: false  },
                    {name:'hit_count',index:'hit_count', sorttype:"int",width:51,align:'center',search: false },
                    { name: 'del', width: 20, sortable: false, search: false,
                      formatter:function(){
                          return "<span class='ui-icon ui-icon-trash'></span>"
                      },
                      cellattr : function( rowId, value, rowbject) { return "class='mypointer'"; }}
                ],
                ignoreCase: true,
                rowNum:10,
                rowList:[5,10,15,20,40],
                pager: '#pager',
                gridview:true,
                rownumbers:true,
                viewrecords: true,
                sortorder: 'desc',
                caption:'Search Results',
                height: $("#ckey_div").height()-105,
                onSelectRow: function(id){
				    fetchKeyValue(id);
				},
				 beforeSelectRow: function (rowid, e) {
                    var iCol = $.jgrid.getCellIndex(e.target);
                    if (iCol ==5) {
                        deleteCacheKey(rowid);
                        
                    }

                    // prevent row selection if one click on the button
                    return (iCol ==5)? false: true;
                }
            });
 
}

$("#resultGrid").jqGrid('filterToolbar',{stringResult: true, searchOnEnter: false, defaultSearch: 'cn'});

function fetchKeyValue(cacheKey)
{
	$.ajax({
		    url: "getCacheKeyValue.action",
		    dataType: 'text',
		    data:{cacheKey:cacheKey},		    
		    type: 'POST',
		    async: true,
		    cache: false,
		    success: function (response){			
			                    $("#key_value_div").html(response);  
			                   	keyValString=  response;           
			                    var keyVal = eval(response);	
			                    
			                    keyValJsonObj['Key']=jQuery.parseJSON(JSON.stringify(keyVal[0].id));
			                    keyValJsonObj['Item']=jQuery.parseJSON(JSON.stringify(keyVal[1]));
			                    keyValJsonObj['HitCount']=jQuery.parseJSON(JSON.stringify(keyVal[3]));		                    
								
								$("#plain_span").removeClass("shadow_effect").addClass("shadow_effect");
							 	$("#json_span").removeClass("shadow_effect");
		    				}
		  });
}

function showKeyValue(type){
 if(type=="plain"){
  	$("#key_value_div").html(keyValString); 
  	
  	$("#json_span").removeClass("shadow_effect");
  	$("#plain_span").removeClass("shadow_effect").addClass("shadow_effect");
  	} 
 else{
 	$('#key_value_div').html("");
 	$('#key_value_div').jsonView(keyValJsonObj);
 	
 	$("#plain_span").removeClass("shadow_effect");
 	$("#json_span").removeClass("shadow_effect").addClass("shadow_effect");
 	}
}
function deleteCacheKey(cacheKey)
{
	$.ajax({
		    url: "deleteCacheKey",
		    dataType: 'text',
		    data:{cacheKey:cacheKey},		    
		    type: 'POST',
		    async: true,
		    cache: false,
		    success: function (response){			
		    					//alert(response.message); //Not working need to check why...		    
			                    $("#key_value_div").html("Cache Key : <b><font color='blue'>[ "+cacheKey+" ]</font><b/> successfully been removed.");     
			                    //$("#resultGrid").trigger("reloadGrid");
			                    jQuery("#resultGrid").jqGrid('delRowData', cacheKey);
		    				}
		  });
}

function clearCache(cacheName)
{
		$.ajax({
		    url: "clearCache",
		    dataType: 'text',
		    data:{cacheName:cacheName},		    
		    type: 'POST',
		    async: true,
		    cache: false,
		    success: function (response){			
		    					//alert(response.message); //Not working need to check why...		    
			                    $("#key_value_div").html("Successfully Cleaned Cache : <b><font color='blue'>[ "+cacheName+" ]</font><b/>.");     			                    
			                    callAction('cacheMonitorHome.action');
			                    
		    				}
		  });
	
}
</script>
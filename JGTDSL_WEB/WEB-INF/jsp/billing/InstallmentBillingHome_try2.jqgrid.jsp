<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("installmentBillingHome.action");
	setTitle("Installment Bill");
</script>


    <br/>
    &nbsp;&nbsp;&nbsp;Is Metered? <input type="text" value="1" id="isMetered" />
	<br/>
	&nbsp;&nbsp;&nbsp;Total Installment <input type="text" value="13" id="total_installment" />
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
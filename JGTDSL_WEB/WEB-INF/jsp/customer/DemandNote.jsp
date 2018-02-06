<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
.row-fluid + .row-fluid {
    margin-top: 5px !important;
}
.dAmount
{
 text-align: right;
}
.historyTable{
 padding-left:45px;
}
</style>
<style type="text/css">
#newDemandNoteForm{height: 95%;}
 label{
    display: inline-block !important;
    float: left !important;
    clear: left !important;
    width: 25%;
    text-align: left !important;
}
.xl{
    width: 50% !important;
}
input[type=text] {
  display: inline-block !important;
  border: 1px solid #add9e4;
  float: none !important;
}
select {
  display: inline-block !important;
  float: left !important;
  border: 1px solid #add9e4;
  color: #333 !important;
}
fieldset { border:1px solid #38a1bb;padding-left: 3px;padding-top: 2px;padding-bottom: 8px;margin-top: 5px; }

legend {
  padding: 0.2em 0.5em;
  border:1px solid #38a1bb;
  color:#38a1bb;
  font-size:90%;
  text-align:right;
  margin-left: 3px;
  font-weight: bold;
  line-height: normal;
  margin-bottom: auto;
  width: auto;
  }
  .dottedInputS{
  border: none !important;
  border-bottom: 2px dotted #38a1bb  !important;
  background: none !important;
  background-color: none !important;
  box-shadow:none  !important;
  width: 50px;
  margin-bottom: 0px !important;
  text-align: right;
  color: blue !important;
  font-weight: bold;
  padding-right: 10px !important;
  }
  .dottedInputL{
  border: none !important;
  border-bottom: 2px dotted #38a1bb  !important;
  background: none !important;
  background-color: none !important;
  box-shadow:none  !important;
  width: 100px;
  margin-bottom: 0px !important;
  text-align: right;
  color: blue !important;
  font-weight: bold;
  padding-right: 10px !important;
  }
  label
  {
   padding-left: 10%;
  }
fieldset div
{
 margin-top: 8px;
}  
</style>
<script type="text/javascript">
	navCache("demandNoteDataEntry.action");
	setTitle("Demand Note");
</script>	



<div class="row-fluid" >


                    <div class="span12" style="width: 100%;">
                    <form id="newDemandNoteForm" name="newDemandNoteForm" action="saveDemandNote.action">
                    <input type="hidden" value="<s:property value='customer.customer_id'/>" name="dNote.customer_id" id="customer_id" />
                    <input type="hidden" value="<s:property value='dNote.demand_note_id'/>" name="dNote.demand_note_id" id="demand_note_id" />
                    <input type="hidden" value="<s:property value='status'/>" id="status_id" />
                    <input type="hidden" value="save" id="mode_id" />
<div class="w-box">

					<div class="w-box-header">
                        <h4>Demand Note Data Entry Form</h4>
                    </div>
                    <div class="w-box-content" style="height: 100%;padding: 15px;"  id="formContent">
                              	
                               	 <fieldset>
										<legend>(1)Customer Information</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>Customer ID</label>       
                                                 	<b>:</b> <s:property value="customer.customer_id"/>                                   	
                                                 </div>
                                                 <div class="span4"> 
                                                 	<label>Full Name</label>
                                                 	<b>:</b> <s:property value="customer.personalInfo.full_name"/>                                                 	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<label>Category Name</label>
                                                 	<b>:</b> <s:property value="customer.customer_category_name"/>                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>Father Name</label> 
                                                 	<b>:</b> <s:property value="customer.personalInfo.father_name"/>                                                 	
                                                 </div>
                                                 <div class="span4"> 
                                                 	<label>Mother Name</label>  
                                                 	<b>:</b> <s:property value="customer.personalInfo.mother_name"/>                                              	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<label>Area Name</label> 
                                                 	<b>:</b> <s:property value="customer.area_name"/>                                                 	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>Mobile No</label> 
                                                 	<b>:</b> <s:property value="customer.personalInfo.mobile"/>                                                	
                                                 </div>
                                                 <div class="span4"> 
                                                 	<label>Meter Status</label>
                                                 	<b>:</b> <s:property value="customer.connectionInfo.isMetered"/>                                                	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	                                                 	                                                
                                                 </div>
                               				</div>                               				
                               </fieldset>
                               
                                <fieldset>
										<legend>(2)Security Deposit</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(A)Double Burner</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.sd_ka_1" id="sd_ka_1"  class="dottedInputS" value="<s:property value='dNote.sd_ka_1' />" />
                                                 	Unit
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.sd_ka_amount" id="sd_ka_amount"  class="dottedInputL" value="<s:property value='dNote.sd_ka_amount' />"/>
                                                 	Tk                                                	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(B)<input type="text" name="dNote.sd_kha_1" id="sd_kha_1"  class="dottedInputS" value="<s:property value='dNote.sd_kha_1' />" style="text-align: cent"/>CFT Burner</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.sd_kha_2" id="sd_kha_2"  class="dottedInputS" value="<s:property value='dNote.sd_kha_2' />"/>
                                                 	Unit
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.sd_kha_amount" id="sd_kha_amount"  class="dottedInputL" value="<s:property value='dNote.sd_kha_amount'/>" />
                                                 	Tk.                                                 	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(C)Others
                                                 	<input type="text" name="dNote.sd_ga_1" id="sd_ga_1"  class="dottedInputS" value="<s:property value='dNote.sd_ga_1' />" style="text-align: center; width: 200;"/> 
                                                 	<!--
                                                 	চুলা ও সুরঞ্জামাদি
                                                 	  --></label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.sd_ga_2" id="sd_ga_2"  class="dottedInputS" value="<s:property value='dNote.sd_ga_2'/>"/>
                                                 	Unit
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.sd_ga_amount" id="sd_ga_amount"  class="dottedInputL" value="<s:property value='dNote.sd_ga_amount'/>"/>
                                                 	Tk.                                                 	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                                <fieldset>
										<legend>(3)Balance Security Deposit</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>(A)Double Burner</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.bsd_ka_amount" id="bsd_ka_amount"  class="dottedInputL" value="<s:property value='dNote.bsd_ka_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(B)Other Burner and Utilities</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.bsd_kha_amount" id="bsd_kha_amount"  class="dottedInputL" value="<s:property value='dNote.bsd_kha_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                               <fieldset>
										<legend>(4) Raizer /The cost of the service connection lines</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(A)3/4 inch diameter service / Raizer Construction and laying cost of goods (up to 3 meters)</br>
(MS pipe/Services T/Elbow/ Lock-Win-Koch/Regulator tape/Primer/Expense)
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_ka_amount" id="cl_ka_amount"  class="dottedInputL" value="<s:property value='dNote.cl_ka_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(B)Header Pipe Cost</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.cl_kha_1" id="cl_kha_1"  class="dottedInputS" value="<s:property value='dNote.cl_kha_1'/>"/>
                                                 	inch diameter
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_kha_amount" id="cl_kha_amount"  class="dottedInputL" value="<s:property value='dNote.cl_kha_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(C)Casing pipe installation costs</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                    
                                                 	<input type="text" name="dNote.cl_ga_1" id="cl_ga_1"  class="dottedInputS" value="<s:property value='dNote.cl_ga_1'/>" onblur="calculateCLGAtotal()"/>Tk. Rate 
                                                 	<input type="text" name="dNote.cl_ga_2" id="cl_ga_2"  class="dottedInputS" value="<s:property value='dNote.cl_ga_2'/>" onblur="calculateCLGAtotal()"/>inch diameter,
                                                 	<input type="text" name="dNote.cl_ga_3" id="cl_ga_3"  class="dottedInputS" value="<s:property value='dNote.cl_ga_3'/>" onblur="calculateCLGAtotal()"/>Meter                                                	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_ga_amount" id="cl_ga_amount"  class="dottedInputL" value="<s:property value='dNote.cl_ga_amount'/>" readonly="readonly"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(D)Additional Service Line Cost</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                    
                                                 	<input type="text" name="dNote.cl_gha_1" id="cl_gha_1"  class="dottedInputS" value="<s:property value='dNote.cl_gha_1'/>" onblur="calculateCLGHAtotal()"/>Tk. Rate
                                                 	<input type="text" name="dNote.cl_gha_2" id="cl_gha_2"  class="dottedInputS" value="<s:property value='dNote.cl_gha_2'/>" onblur="calculateCLGHAtotal()"/>Meter
                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_gha_amount" id="cl_gha_amount"  class="dottedInputL" value="<s:property value='dNote.cl_gha_amount'/>" readonly="readonly"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(E)Other Goods (line 1)</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                    
													<input type="text" name="dNote.cl_uma_l1" id="cl_uma_l1"  class="dottedInputL"  style="width: 90%" value="<s:property value='dNote.cl_uma_l1'/>"/>
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_uma_l1_amount" id="cl_uma_l1_amount"  class="dottedInputL" value="<s:property value='dNote.cl_uma_l1_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">Other Goods (line two)</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                    
													<input type="text" name="dNote.cl_uma_l2" id="cl_uma_l2"  class="dottedInputL"  style="width: 90%" value="<s:property value='dNote.cl_uma_l2'/>"/>
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_uma_l2_amount" id="cl_uma_l2_amount"  class="dottedInputL" value="<s:property value='dNote.cl_uma_l2_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                               <fieldset>
										<legend>(5)Other Fee</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(A)Commissioning Fee
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_ka_amount" id="of_ka_amount"  class="dottedInputL" value="<s:property value='dNote.of_ka_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(B)Connection Fee 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_kha_amount" id="of_kha_amount"  class="dottedInputL" value="<s:property value='dNote.of_kha_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(C)Load Increase/Decrease Fee
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_ga_amount" id="of_ga_amount"  class="dottedInputL" value="<s:property value='dNote.of_ga_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(D)Disconnection Fee
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_gha_amount" id="of_gha_amount"  class="dottedInputL" value="<s:property value='dNote.of_gha_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(E)Re-Connection Fee
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_uma_amount" id="of_uma_amount"  class="dottedInputL" value="<s:property value='dNote.of_uma_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(F)Service Charges
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_ch_amount" id="of_ch_amount"  class="dottedInputL" value="<s:property value='dNote.of_ch_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(G)Additional Bill <input type="text" name="dNote.of_cho_1" id="of_cho_1"  class="dottedInputL" value="<s:property value='dNote.of_cho_1'/>"/>
And fine
                                                 	 <input type="text" name="dNote.of_cho_2" id="of_cho_2"  class="dottedInputL" value="<s:property value='dNote.of_cho_2'/>"/> 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_cho_amount" id="of_cho_amount"  class="dottedInputL" value="<s:property value='dNote.of_cho_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	
(H)Other fees
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_jo_amount" id="of_jo_amount"  class="dottedInputL" value="<s:property value='dNote.of_jo_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                               <fieldset>
										<legend>(5)Other Expense</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(A)Cannel/Drain/Culvert Crossing Expense
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_ka_amount" id="others_ka_amount"  class="dottedInputL" value="<s:property value='dNote.others_ka_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(B)PAKA/CC Road Cutting Expanse
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_kha_amount" id="others_kha_amount"  class="dottedInputL" value="<s:property value='dNote.others_kha_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(C)Adha PAKA Road Cutting Expanse
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_ga_amount" id="others_ga_amount"  class="dottedInputL" value="<s:property value='dNote.others_ga_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(D)Raizer Shifting Cost
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_gha_amount" id="others_gha_amount"  class="dottedInputL" value="<s:property value='dNote.others_gha_amount'/>"/>
                                                 	Tk.                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               <div class="row-fluid">                                
                                            <div class="span4"></div>
                                            <div class="span4" style="font-weight: bold;">Total</div>
                                            <div class="span4">
                                                	<input type="text" name="dNote.total_in_amount" id="total_in_amount"  class="dottedInputL" readonly="readonly" style="color: maroon;" value="<s:property value='dNote.total_in_amount'/>"/> Tk.                                                  	                                                
                                             </div>
	                              	
	                           </div>   		
	                      
					             <div class="row-fluid" style="margin-top: 0px;">	        	  
					                  <div class="span12" style="text-align: right;">
					                  	<button class="btn btn-beoro-3" type="button" id="btn_reset" onclick="resetDemandForm()"><span class="splashy-diamonds_3"></span>&nbsp;&nbsp;Reset</button>
					                  	<button class="btn btn-beoro-3" type="button" id="btn_addNew"><span class="splashy-application_windows_okay"></span>&nbsp;&nbsp;Add New</button>
					                  	<button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="$('#newDemandNoteForm').html(jsImg.SETTING).load('getdemandNoteDataEntryForEdit.action?status=U&demand_note_id=<s:property value="demand_note_id"/>&customer_id=<s:property value="customer_id"/>');"><span class="splashy-application_windows_edit"></span>&nbsp;&nbsp;Edit</button> 
					                  	<button class="btn btn-beoro-4" id="btn_save">Create Demand Note</button>
					                  	<button class="btn btn-beoro-4" id="btn_update">Update</button>
					                  	<!-- <button class="btn btn-beoro-3" type="button" onclick="setTestData()">Test Data</button> -->                  	
					                  </div>									                               
								</div>
					                                    						
                          </div>
                        </div>
                     </form>
                  </div>
</div>        
                                        
                                               
                                               
<script type="text/javascript">
function calculateTotal()
{


 //console.log(parseFloat($('#sd_ga_amount').val())||0);
 var total=parseFloat(parseFloat($('#sd_ka_amount').val()||0)+parseFloat($('#sd_kha_amount').val()||0)+parseFloat($('#sd_ga_amount').val()||0)+parseFloat($('#bsd_ka_amount').val()||0)+parseFloat($('#bsd_kha_amount').val()||0)+
          parseFloat($('#cl_ka_amount').val()||0)+parseFloat($('#cl_kha_amount').val()||0)+parseFloat($('#cl_ga_amount').val()||0)+parseFloat($('#cl_gha_amount').val()||0)+parseFloat($('#cl_uma_l1_amount').val()||0)+
          parseFloat($('#cl_uma_l2_amount').val()||0)+parseFloat($('#of_ka_amount').val()||0)+parseFloat($('#of_kha_amount').val()||0)+parseFloat($('#of_ga_amount').val()||0)+parseFloat($('#of_gha_amount').val()||0)+
          parseFloat($('#of_uma_amount').val()||0)+parseFloat($('#of_ch_amount').val()||0)+parseFloat($('#of_cho_1').val()||0)+parseFloat($('#of_cho_2').val()||0)+parseFloat($('#of_cho_amount').val()||0)+parseFloat($('#of_jo_amount').val()||0)+
          parseFloat($('#others_ka_amount').val()||0)+parseFloat($('#others_kha_amount').val()||0)+
          parseFloat($('#others_ga_amount').val()||0)+parseFloat($('#others_gha_amount').val()||0));

$("#total_in_amount").val(total);         
}

$('.dottedInputL').change(calculateTotal);
//For Multiple ...
//$('.class1, .class2').click(some_function);



$("form#newDemandNoteForm").submit(function(event){
  event.preventDefault(); 
  
   var modeValue=$("#mode_id").val();//Here modeValue is to find out either update or delete
  
  if(modeValue=="update")
  {
  
  var url1='updateDemandNote.action';
  }else
  {
  
  var url1='saveDemandNote.action';
  
  }
  
  var formData = new FormData($(this)[0]); 
  $.ajax({
    url:url1,
    type: 'POST',
    data: formData,
    async: false,
    cache: false,
    contentType: false,
    processData: false,
    success: function (response) {      
      if(response.status=="OK"){
      $.jgrid.info_dialog(response.dialogCaption,response.message,$.jgrid.edit.bClose, {
	                    zIndex: 1500,
	                    width:450,
	                    /*
	                     beforeOpen: function () {
				            centerCustomerInfoDialog($("#customer_id").val());
        				},
        				*/
	                     afterOpen:disableOnClick,
	                     onClose: function () {
	                     
	                        var customer_id=$("#customer_id").val();
	                        var demand_note_id=$("#demand_note_id").val();
	                    	$('.ui-widget-overlay').unbind( "click" );
	                    	callAction('demandNoteDownloadHome.action?demand_note_id='+demand_note_id+'&customer_id='+customer_id);
	                        return true; // allow closing
	                    }
    });
    }
	 else
	 alert(response.message);
    
    }
    
  });
 
  return false;
});

function calculateCLGAtotal(){
	var amount=parseFloat($("#cl_ga_1").val()||0)*parseFloat($("#cl_ga_2").val()||0)*parseFloat($("#cl_ga_3").val()||0);
	$("#cl_ga_amount").val(amount.toFixed(2));
	calculateTotal()
}
function calculateCLGHAtotal(){
	var amount=parseFloat($("#cl_gha_1").val()||0)*parseFloat($("#cl_gha_2").val()||0);
	$("#cl_gha_amount").val(amount.toFixed(2));
	calculateTotal()
}


</script>
<script type="text/javascript">
var customer_id=$("#customer_id").val();
var actionUrl=sBase+"getDemandNoteList.action?customer_id="+customer_id;
$("#demandNoteListTbl").html(jsImg.SETTING).load(actionUrl);      
</script>
<script>
var statusVar=$("#status_id").val();
//alert(statusVar);
if(statusVar=="")
{
$("#btn_addNew").hide();
$("#btn_edit").hide();
$("#btn_update").hide();
}else if(statusVar=="Edit")
{
$("#btn_addNew").show();
$("#btn_edit").show();
$("#btn_update").hide();
$("#btn_save").hide();
$("#btn_reset").hide();
disableField();

}else if(statusVar=="Update")
{
$("#btn_addNew").show();
$("#btn_edit").hide();
$("#btn_update").show();
$("#btn_save").hide();
$("#mode_id").val("update");
//alert($("#mode_id").val());
}

</script>
<script>
$("#btn_addNew").click(function(){
	$("#btn_addNew").hide();
    $("#btn_save").show();
    $("#btn_reset").show();
    $("#btn_update").hide();
    $("#btn_edit").hide();
    //alert($("#mode_id").val());
    $("#mode_id").val("save");
    //alert($("#mode_id").val());
    
    resetDemandForm();
    enableField();
});
</script>
<script>

function enableField()
	{
	
	$("#sd_ka_1").removeAttr("disabled");
	$("#sd_ka_amount").removeAttr("disabled");
	$("#sd_kha_1").removeAttr("disabled");
	$("#sd_kha_2").removeAttr("disabled");
	$("#sd_kha_amount").removeAttr("disabled");
	$("#sd_ga_1").removeAttr("disabled");
	$("#sd_ga_2").removeAttr("disabled");
	$("#sd_ga_amount").removeAttr("disabled");
	
	$("#bsd_ka_amount").removeAttr("disabled");
	$("#bsd_kha_amount").removeAttr("disabled");
	
	$("#cl_ka_amount").removeAttr("disabled");
	$("#cl_kha_1").removeAttr("disabled");
	$("#cl_kha_amount").removeAttr("disabled");
	$("#cl_ga_1").removeAttr("disabled");
	$("#cl_ga_2").removeAttr("disabled");
	$("#cl_ga_3").removeAttr("disabled");
	$("#cl_ga_amount").removeAttr("disabled");
	$("#cl_gha_1").removeAttr("disabled");
	$("#cl_gha_2").removeAttr("disabled");
	$("#cl_gha_amount").removeAttr("disabled");
	$("#cl_uma_l1").removeAttr("disabled");
	$("#cl_uma_l1_amount").removeAttr("disabled");
	$("#cl_uma_l2").removeAttr("disabled");
	$("#cl_uma_l2_amount").removeAttr("disabled");
	$("#of_ka_amount").removeAttr("disabled");
	$("#of_kha_amount").removeAttr("disabled");
	$("#of_ga_amount").removeAttr("disabled");
	$("#of_gha_amount").removeAttr("disabled");	
	$("#of_uma_amount").removeAttr("disabled");
	$("#of_ch_amount").removeAttr("disabled");
	$("#of_cho_1").removeAttr("disabled");
	$("#of_cho_2").removeAttr("disabled");
	$("#of_cho_amount").removeAttr("disabled");	
	$("#of_jo_amount").removeAttr("disabled");
	$("#others_ka_amount").removeAttr("disabled");
	$("#others_kha_amount").removeAttr("disabled");
	$("#others_ga_amount").removeAttr("disabled");
	$("#others_gha_amount").removeAttr("disabled");
	$("#total_in_amount").removeAttr("disabled");
	
	}
function disableField()
	{
	
	$("#sd_ka_1").attr("disabled","disabled");
	$("#sd_ka_amount").removeAttr("disabled");
	$("#sd_kha_1").removeAttr("disabled");
	$("#sd_kha_2").removeAttr("disabled");
	$("#sd_kha_amount").removeAttr("disabled");
	$("#sd_ga_1").removeAttr("disabled");
	$("#sd_ga_2").attr("disabled","disabled");
	$("#sd_ga_amount").attr("disabled","disabled");
	
	$("#bsd_ka_amount").attr("disabled","disabled");
	$("#bsd_kha_amount").attr("disabled","disabled");
	
	$("#cl_ka_amount").attr("disabled","disabled");
	$("#cl_kha_1").attr("disabled","disabled");
	$("#cl_kha_amount").removeAttr("disabled");
	$("#cl_ga_1").attr("disabled","disabled");
	$("#cl_ga_2").attr("disabled","disabled");
	$("#cl_ga_3").attr("disabled","disabled");
	$("#cl_ga_amount").attr("disabled","disabled");
	$("#cl_gha_1").attr("disabled","disabled");
	$("#cl_gha_2").attr("disabled","disabled");
	$("#cl_gha_amount").attr("disabled","disabled");
	$("#cl_uma_l1").attr("disabled","disabled");
	$("#cl_uma_l1_amount").attr("disabled","disabled");
	$("#cl_uma_l2").attr("disabled","disabled");
	$("#cl_uma_l2_amount").attr("disabled","disabled");
	$("#of_ka_amount").attr("disabled","disabled");
	$("#of_kha_amount").attr("disabled","disabled");
	$("#of_ga_amount").attr("disabled","disabled");
	$("#of_gha_amount").attr("disabled","disabled");
	$("#of_uma_amount").attr("disabled","disabled");
	$("#of_ch_amount").attr("disabled","disabled");
	$("#of_cho_1").attr("disabled","disabled");
	$("#of_cho_2").attr("disabled","disabled");
	$("#of_cho_amount").attr("disabled","disabled");
	$("#of_jo_amount").attr("disabled","disabled");
	$("#others_ka_amount").attr("disabled","disabled");
	$("#others_kha_amount").attr("disabled","disabled");;
	$("#others_ga_amount").attr("disabled","disabled");
	$("#others_gha_amount").attr("disabled","disabled");
	$("#total_in_amount").attr("disabled","disabled");
	
	}
	function resetDemandForm()
{
	$("#demand_note_id").val("");
	$("#sd_ka_1").val("");
	$("#sd_ka_amount").val("");
	$("#sd_kha_1").val("");
	$("#sd_kha_2").val("");
	$("#sd_kha_amount").val("");
	$("#sd_ga_1").val("");
	$("#sd_ga_2").val("");
	$("#sd_ga_amount").val("");
	
	$("#bsd_ka_amount").val("");
	$("#bsd_kha_amount").val("");
	
	$("#cl_ka_amount").val("");
	$("#cl_kha_1").val("");
	$("#cl_kha_amount").val("");
	$("#cl_ga_1").val("");
	$("#cl_ga_2").val("");
	$("#cl_ga_3").val("");
	$("#cl_ga_amount").val("");
	$("#cl_gha_1").val("");
	$("#cl_gha_2").val("");
	$("#cl_gha_amount").val("");
	$("#cl_uma_l1").val("");
	$("#cl_uma_l1_amount").val("");
	$("#cl_uma_l2").val("");
	$("#cl_uma_l2_amount").val("");
	$("#of_ka_amount").val("");
	$("#of_kha_amount").val("");
	$("#of_ga_amount").val("");
	$("#of_gha_amount").val("");	
	$("#of_uma_amount").val("");
	$("#of_ch_amount").val("");
	$("#of_cho_1").val("");
	$("#of_cho_2").val("");
	$("#of_cho_amount").val("");	
	$("#of_jo_amount").val("");
	$("#others_ka_amount").val("");
	$("#others_kha_amount").val("");
	$("#others_ga_amount").val("");
	$("#others_gha_amount").val("");
	$("#total_in_amount").val("");
	}
	
</script>


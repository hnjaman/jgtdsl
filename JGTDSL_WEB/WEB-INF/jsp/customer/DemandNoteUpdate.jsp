<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
.row-fluid + .row-fluid {
    margin-top: 5px !important;
}
.dAmount
{
 text-align: right;
}
.span3{
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
                    <input type="hidden" value="Update" id="mode_id" />
<div class="w-box">

					<div class="w-box-header">
                        <h4>Demand Note Data Entry Form</h4>
                    </div>
                    <div class="w-box-content" style="height: 100%;padding: 15px;"  id="formContent">
                              	
                               	 <fieldset>
										<legend>কাস্টমার ইনফর্মেশন</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>কাস্টমার আ.ডি.</label>       
                                                 	<b>:</b> <s:property value="customer.customer_id"/>                                   	
                                                 </div>
                                                 <div class="span4"> 
                                                 	<label>কাস্টমারের  নাম</label>
                                                 	<b>:</b> <s:property value="customer.personalInfo.full_name"/>                                                 	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<label>ক্যাটাগরি</label>
                                                 	<b>:</b> <s:property value="customer.customer_category_name"/>                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>পিতা</label> 
                                                 	<b>:</b> <s:property value="customer.personalInfo.father_name"/>                                                 	
                                                 </div>
                                                 <div class="span4"> 
                                                 	<label>মাতা</label>  
                                                 	<b>:</b> <s:property value="customer.personalInfo.mother_name"/>                                              	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<label>এরিয়া</label> 
                                                 	<b>:</b> <s:property value="customer.area_name"/>                                                 	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>মোবাইল</label> 
                                                 	<b>:</b> <s:property value="customer.personalInfo.mobile"/>                                                	
                                                 </div>
                                                 <div class="span4"> 
                                                 	<label>মিটার স্ট্যাটাস</label>
                                                 	<b>:</b> <s:property value="customer.connectionInfo.isMetered"/>                                                	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	                                                 	                                                
                                                 </div>
                               				</div>                               				
                               </fieldset>
                               
                                <fieldset>
										<legend>(১)নিরাপত্তা জামানত</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(ক) দ্বৈত চুলা</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.sd_ka_1" id="sd_ka_1"  class="dottedInputS" value="<s:property value='dNote.sd_ka_1' />" />
                                                 	টি
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.sd_ka_amount" id="sd_ka_amount"  class="dottedInputL" value="<s:property value='dNote.sd_ka_amount' />"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(খ) <input type="text" name="dNote.sd_kha_1" id="sd_kha_1"  class="dottedInputS" value="<s:property value='dNote.sd_kha_1' />" style="text-align: cent"/> সিএফটি চুলা</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.sd_kha_2" id="sd_kha_2"  class="dottedInputS" value="<s:property value='dNote.sd_kha_2' />"/>
                                                 	টি
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.sd_kha_amount" id="sd_kha_amount"  class="dottedInputL" value="<s:property value='dNote.sd_kha_amount'/>" />
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(গ) অনন্যা/ 
                                                 	<input type="text" name="dNote.sd_ga_1" id="sd_ga_1"  class="dottedInputS" value="<s:property value='dNote.sd_ga_1' />" style="text-align: center; width: 200;"/> 
                                                 	<!--
                                                 	চুলা ও সুরঞ্জামাদি
                                                 	  --></label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.sd_ga_2" id="sd_ga_2"  class="dottedInputS" value="<s:property value='dNote.sd_ga_2'/>"/>
                                                 	টি
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.sd_ga_amount" id="sd_ga_amount"  class="dottedInputL" value="<s:property value='dNote.sd_ga_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                                <fieldset>
										<legend>(২)ব্যালেন্স নিরাপত্তা জামানত</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label>(ক) দ্বৈত চুলা</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.bsd_ka_amount" id="bsd_ka_amount"  class="dottedInputL" value="<s:property value='dNote.bsd_ka_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(খ) অনন্যা চুলা ও সুরঞ্জামাদি</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.bsd_kha_amount" id="bsd_kha_amount"  class="dottedInputL" value="<s:property value='dNote.bsd_kha_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                               <fieldset>
										<legend>(৩)রাইজার/ সার্ভিস লাইনের সংযোগ ব্যয়</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(ক) ৩/৪ ইঞ্চি ব্যাসের সার্ভিস/রাইজার নির্মাণে মালামাল ও স্থাপনা ব্যয় (৩ মিটার পর্যন্ত)<br/>
                                                 	(এমএস পাইপ/সার্ভিস টি/এলবো/লক-উইন-কক/রেগুলেটর টেপ/গ্রাইমার/নির্মাণ ব্যয়) 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_ka_amount" id="cl_ka_amount"  class="dottedInputL" value="<s:property value='dNote.cl_ka_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(খ) হেডার পাইপের মূল্য</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                 	<input type="text" name="dNote.cl_kha_1" id="cl_kha_1"  class="dottedInputS" value="<s:property value='dNote.cl_kha_1'/>"/>
                                                 	ইঞ্চি ব্যাস
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_kha_amount" id="cl_kha_amount"  class="dottedInputL" value="<s:property value='dNote.cl_kha_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(গ) কেসিং পাইপের মূল্য সহ স্থাপনা ব্যয়</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                    
                                                 	<input type="text" name="dNote.cl_ga_1" id="cl_ga_1"  class="dottedInputS" value="<s:property value='dNote.cl_ga_1'/>" onblur="calculateCLGAtotal()"/>টাকা হারে 
                                                 	<input type="text" name="dNote.cl_ga_2" id="cl_ga_2"  class="dottedInputS" value="<s:property value='dNote.cl_ga_2'/>" onblur="calculateCLGAtotal()"/>ইঞ্চি ব্যাস 
                                                 	<input type="text" name="dNote.cl_ga_3" id="cl_ga_3"  class="dottedInputS" value="<s:property value='dNote.cl_ga_3'/>" onblur="calculateCLGAtotal()"/>মিটার                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_ga_amount" id="cl_ga_amount"  class="dottedInputL" value="<s:property value='dNote.cl_ga_amount'/>" readonly="readonly"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(ঘ) অতিরিক্ত সার্ভিস লাইনের নির্মাণ ব্যয়</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	
                                                    
                                                 	<input type="text" name="dNote.cl_gha_1" id="cl_gha_1"  class="dottedInputS" value="<s:property value='dNote.cl_gha_1'/>" onblur="calculateCLGHAtotal()"/>টাকা হারে 
                                                 	<input type="text" name="dNote.cl_gha_2" id="cl_gha_2"  class="dottedInputS" value="<s:property value='dNote.cl_gha_2'/>" onblur="calculateCLGHAtotal()"/>মিটার
                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_gha_amount" id="cl_gha_amount"  class="dottedInputL" value="<s:property value='dNote.cl_gha_amount'/>" readonly="readonly"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">(ঙ) অন্যান্য মালামাল (লাইন ১)</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                    
													<input type="text" name="dNote.cl_uma_l1" id="cl_uma_l1"  class="dottedInputL"  style="width: 90%" value="<s:property value='dNote.cl_uma_l1'/>"/>
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_uma_l1_amount" id="cl_uma_l1_amount"  class="dottedInputL" value="<s:property value='dNote.cl_uma_l1_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span4">
                                                 	<label class="xl">অন্যান্য মালামাল (লাইন ২)</label>                                                 	
                                                 </div>
                                                 <div class="span4">                                                 	                                                    
													<input type="text" name="dNote.cl_uma_l2" id="cl_uma_l2"  class="dottedInputL"  style="width: 90%" value="<s:property value='dNote.cl_uma_l2'/>"/>
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.cl_uma_l2_amount" id="cl_uma_l2_amount"  class="dottedInputL" value="<s:property value='dNote.cl_uma_l2_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                               <fieldset>
										<legend>(৪)বিভিন্ন বিল</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(ক) কমিশনিং ফি 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_ka_amount" id="of_ka_amount"  class="dottedInputL" value="<s:property value='dNote.of_ka_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(খ) সংযোগ ফি 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_kha_amount" id="of_kha_amount"  class="dottedInputL" value="<s:property value='dNote.of_kha_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(গ) লোড হ্রাস/বৃদ্ধি ফি 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_ga_amount" id="of_ga_amount"  class="dottedInputL" value="<s:property value='dNote.of_ga_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(ঘ) বিচ্ছিন্নকরন ফি 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_gha_amount" id="of_gha_amount"  class="dottedInputL" value="<s:property value='dNote.of_gha_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(ঙ) পুনঃ সংযোগ ফি 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_uma_amount" id="of_uma_amount"  class="dottedInputL" value="<s:property value='dNote.of_uma_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(চ) সার্ভিস চার্জ 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_ch_amount" id="of_ch_amount"  class="dottedInputL" value="<s:property value='dNote.of_ch_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(ছ) অতিরিক্ত বিল  <input type="text" name="dNote.of_cho_1" id="of_cho_1"  class="dottedInputL" value="<s:property value='dNote.of_cho_1'/>"/> এবং  জরিমানা
                                                 	 <input type="text" name="dNote.of_cho_2" id="of_cho_2"  class="dottedInputL" value="<s:property value='dNote.of_cho_2'/>"/> 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_cho_amount" id="of_cho_amount"  class="dottedInputL" value="<s:property value='dNote.of_cho_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(জ) অন্যান্য ফি 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.of_jo_amount" id="of_jo_amount"  class="dottedInputL" value="<s:property value='dNote.of_jo_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               
                               <fieldset>
										<legend>(৫)অন্যান্য নির্মাণ ব্যয়</legend>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(ক) নালা/ড্রেন/কালভার্ট অতিক্রম ব্যয় 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_ka_amount" id="others_ka_amount"  class="dottedInputL" value="<s:property value='dNote.others_ka_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(খ) পাকা/সিসি রাস্তা কাটা ব্যয় 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_kha_amount" id="others_kha_amount"  class="dottedInputL" value="<s:property value='dNote.others_kha_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(গ) আধা পাকা রাস্তা কাটা ব্যয় 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_ga_amount" id="others_ga_amount"  class="dottedInputL" value="<s:property value='dNote.others_ga_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				<div class="row-fluid">                                
                                                 <div class="span8">
                                                 	<label style="padding-left: 5%;width: 100%">
                                                 	(ঘ) রাইসার স্থানান্তর ব্যয় 
                                                 	</label>                                                 	
                                                 </div>
                                                 <div class="span4">
                                                 	<input type="text" name="dNote.others_gha_amount" id="others_gha_amount"  class="dottedInputL" value="<s:property value='dNote.others_gha_amount'/>"/>
                                                 	টাকা                                                  	                                                
                                                 </div>
                               				</div>
                               				
                               </fieldset>
                               <div class="row-fluid">                                
                                            <div class="span4"></div>
                                            <div class="span4" style="font-weight: bold;">সর্বমোট </div>
                                            <div class="span4">
                                                	<input type="text" name="dNote.total_in_amount" id="total_in_amount"  class="dottedInputL" readonly="readonly" style="color: maroon;" value="<s:property value='dNote.total_in_amount'/>"/> টাকা                                                  	                                                
                                             </div>
	                              	
	                           </div>   		
	                        <!--    <div class="row-fluid">
                                        <div class="span12">
                                         	<div  style="float: right;padding: 20px;">
		                              			<button class="btn btn-beoro-4">Create Demand Note</button>
		                              			<button class="btn btn-beoro-4" type="button" onclick="setTestData()">Set Test Data</button>
		                              		</div>
                                        </div>
                               </div> -->
                <div class="row-fluid" style="margin-top: 0px;">	        	  
                  <div class="span12" style="text-align: right;">
                  	<button class="btn btn-beoro-3" type="button" id="btn_addNew"><span class="splashy-application_windows_okay"></span>&nbsp;&nbsp;Add New</button>
                  	<button class="btn btn-beoro-4" id="btn_update">Update</button>
                  	<button class="btn btn-beoro-4" id="btn_save" ">Create Demand Note</button>                  	
                                                                    
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


function setTestData()
{
	$("#sd_ka_1").val("1");
	$("#sd_ka_amount").val("2");
	$("#sd_kha_1").val("3");
	$("#sd_kha_2").val("4");
	$("#sd_kha_amount").val("5");
	$("#sd_ga_1").val("6");
	$("#sd_ga_2").val("7");
	$("#sd_ga_amount").val("8");
	
	$("#bsd_ka_amount").val("81");
	$("#bsd_kha_amount").val("32");
	
	$("#cl_ka_amount").val("9");
	$("#cl_kha_1").val("10");
	$("#cl_kha_amount").val("11");
	$("#cl_ga_1").val("12");
	$("#cl_ga_2").val("13");
	$("#cl_ga_3").val("14");
	$("#cl_ga_amount").val("15");
	$("#cl_gha_1").val("16");
	$("#cl_gha_2").val("17");
	$("#cl_gha_amount").val("18");
	$("#cl_uma_l1").val("19");
	$("#cl_uma_l1_amount").val("20");
	$("#cl_uma_l2").val("21");
	$("#cl_uma_l2_amount").val("22");
	$("#of_ka_amount").val("23");
	$("#of_kha_amount").val("24");
	$("#of_ga_amount").val("25");
	$("#of_gha_amount").val("26");	
	$("#of_uma_amount").val("27");
	$("#of_ch_amount").val("28");
	$("#of_cho_1").val("29");
	$("#of_cho_2").val("30");
	$("#of_cho_amount").val("31");	
	$("#of_jo_amount").val("32");
	$("#others_ka_amount").val("33");
	$("#others_kha_amount").val("34");
	$("#others_ga_amount").val("35");
	$("#others_gha_amount").val("36");
	}
	

	
$("form#newDemandNoteForm").submit(function(event){
  event.preventDefault(); 
 
  var modeValue=$("#mode_id").val();//Here conValue is to find out either update or delete
  
  if(modeValue=="Update")
  {
  
  var url1='updateDemandNote.action';
  }else
  {
  
  var url1='saveDemandNote.action';
  
  }
  
 
  
  var formData = new FormData($(this)[0]); 
  $.ajax({
    url: url1,
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
	                    	$('.ui-widget-overlay').unbind( "click" );
	                    	callAction('demandNoteDownloadHome.action?customer_id='+customer_id);
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

$("#btn_save").hide();
$("#btn_addNew").click(function(){
    $("#btn_save").show();
    $("#btn_update").hide();
    alert($("#mode_id").val());
    $("#mode_id").val("Save");
    alert($("#mode_id").val());
    
    resetDemandForm();
});

</script>



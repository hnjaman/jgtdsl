<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("ownershipChange.action");
	setTitle("Ownership Change");
</script>
<form id="ownershipForm" name="ownershipForm">
<div class="row-fluid">
	<div style="width: 48%;height: 98%;float: left;">	
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Old Customer Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Customer Id</label>	
								<input type="text" id="customer_id" name="customer.customer_id" style="font-weight: bold;color: #3b5894; z-index: 2; background: transparent;width: 71%;margin-top: -4px;"  />
								<input type="text" name="" id="customer_id_x" disabled="disabled" style="color: #CCC; position: absolute; background: transparent; z-index: 1;border: none;width: 12%;margin-top: -5px;"/>
									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Customer Name</label>	
								<input type="text" style="width: 71%"  id="old_full_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">F/H Name</label>	
								<input type="text" style="width: 71%"  id="old_father_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mother Name</label>	
								<input type="text" style="width: 71%"  id="old_mother_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Gender</label>	
								<select id="old_gender"  style="width: 73.5%;" disabled="disabled">
			                        <option value="" selected="selected">Select Gender</option>
			                            <option value="M" >Male</option>
			                            <option value="F" >Female</option>
			                            <option value="O" >Others</option>
			                    </select>								
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Freedom Fighter</label>	
								<select name="personal.freedom_fighter" id="freedom_fighter"  style="width: 73.5%;" disabled="disabled">
			                        	<option value="" selected="selected">Select Yes or No</option>
			                            <option value="Y" >Yes</option>
			                            <option value="N" >No</option>
			                    </select>								
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mobile</label>	
								<input type="text" style="width: 71%"  id="old_mobile" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Phone</label>	
								<input type="text" style="width: 71%"  id="old_phone" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Fax</label>	
								<input type="text" style="width: 71%"  id="old_fax" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">National Id</label>	
								<input type="text" style="width: 71%"  id="old_national_id" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Passport</label>	
								<input type="text" style="width: 71%"  id="old_passport_no" disabled="disabled"/>									
						  	</div>	
						 </div>				
<!-- added on sept 19 -->						 
						  <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">EMAIL</label>	
								<input type="text" style="width: 71%"  id="old_email" disabled="disabled"/>									
								
						  	</div>	
						 </div>		
						  
						  
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">TIN NO.</label>	
								<input type="text" style="width: 71%"  id="old_tin" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">ORGANIZATION NAME</label>	
								<input type="text" style="width: 71%"  id="old_organization_name" disabled="disabled"/>									
								
						  	</div>	
						 </div>
						 
<!-- for image -->
        <div class="row-fluid" style="display: true;">
                <div style="width: 110px;height:137px;border: 1px solid black;float: left;" class="span6" id="old_customer_photo">	
                	<img id='old_img_photo' />
                </div>
                 </div>
<!-- END : image -->
						 
						 		 
				</div>
		 </div>
		</div>
	</div>
	
	
	<div style="width: 48%;height: 98%;float: left;margin-left: 1%;">	
		<div style="width: 100%;float: left;margin-top: 0px;">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">New Customer Information</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">												
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%"></label>	
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Customer Name <span style="color:red;font-size: 20px;">*</span></label>	
								<input type="text" style="width: 71%"  id="full_name" name="personal.full_name"  />									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">F/H Name<span style="color:red;font-size: 20px;"></span></label>	
								<input type="text" style="width: 71%"  id="father_name" name="personal.father_name"  />									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mother Name<span style="color:red;font-size: 20px;"></span></label>	
								<input type="text" style="width: 71%"  id="mother_name" name="personal.mother_name"  />									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Gender<span style="color:red;font-size: 20px;">*</span></label>	
								<select id="gender"  style="width: 73.5%;" name="personal.gender"  >
			                        <option value="" selected="selected">Select Gender</option>
			                            <option value="Male" >Male</option>
			                            <option value="Female" >Female</option>
			                            <option value="Others" >Others</option>
			                    </select>									
								
						  	</div>	
						 </div>
						  <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Freedom Fighter</label>	
								<select name="personal.freedom_fighter_new" id="freedom_fighter_new"  style="width: 73.5%;">
			                        	<option value="" selected="selected">Select Yes or No</option>
			                            <option value="Y" >Yes</option>
			                            <option value="N" >No</option>
			                    </select>								
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Mobile</label>	
								<input type="text" style="width: 71%"  id="mobile" name="personal.mobile" />									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Phone</label>	
								<input type="text" style="width: 71%"  id="phone" name="personal.phone" />									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Fax</label>	
								<input type="text" style="width: 71%"  id="fax" name="personal.fax"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">National Id</label>	
								<input type="text" style="width: 71%"  id="national_id" name="personal.national_id"/>									
								
						  	</div>	
						 </div>
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">Passport</label>	
								<input type="text" style="width: 71%"  id="passport_no" name="personal.passport_no"/>									
								
						  	</div>	
						 </div>
						 
<!-- inserted on sept 19 -->


<div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">EMAIL</label>	
								<input type="text" style="width: 71%"  id="email" name="personal.email"/>									
								
						  	</div>	
						 </div>		
						  
						  
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">TIN NO.</label>	
								<input type="text" style="width: 71%"  id="tin" name="personal.tin"/>									
								
						  	</div>	
						 </div>
						 
						 <div class="row-fluid">
							<div class="span12">
								<label style="width: 25%">ORGANIZATION NAME</label>	
								<input type="text" style="width: 71%"  id="organization_name" name="personal.organization_name"/>									
								
						  	</div>	
						 </div>

<!-- End -->
				</div>
		 </div>
		</div>
	</div>

</div>
</form>
<!-- for image -->


 <div style=" position: absolute;
    top: 460px;
    right: 80px;
    width: 350px;
    height: 100px;">
            <div class="row-fluid" style="display: true;">
                <div style="width: 120px;height:150px;border: 1px solid black;" class="span6" id="customer_photo">  
                             
                </div>
                <div class="span6" style="padding-left: 5px;">
                    <input type="file" name="upload" id="upload" onchange="afu()" style="width: 98%;" />
                    <input type="button" name="reset" value="Clear" style="width:80px;" />
                </div>
            </div>
        </div>


<!-- END : image -->

<div class="row-fluid" style="text-align: center;padding-top: 10px;">
	<button id="btn_edit" class="btn btn-beoro-3" onclick="validateAndSaveChangeOwnershipInfo()" type="button">
	<span class="splashy-application_windows_edit"></span>
	Change Ownership
	</button>
</div>

<div id="customer_grid_div" style="height: 39%;width: 99%;"> 
<div id="tabbed-nav">
            <ul>
                <li><a>All Customer</a></li>
                <li><a>Ownership Change History (<font color="blue" style="font-weight: bold;">For this customer</font>)</a></li>
            </ul>
            <div>
                <div>
					<table id="customer_grid"></table>
					<div id="customer_grid_pager" ></div>
                </div>
                <div>
                    <table id="ownership_change_history_grid"></table>
					<div id="ownership_change_history_grid_pager" ></div>
                </div>
            </div>

        </div>
</div>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/tabInitialization.js"></script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/ownershipChange.js"></script>
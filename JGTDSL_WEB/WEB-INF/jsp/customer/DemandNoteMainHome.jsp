<%@ taglib prefix="s" uri="/struts-tags"%>
<style type="text/css">
.row-fluid + .row-fluid {
    margin-top: 5px !important;
}
.dAmount
{
 text-align: right;
}
</style>


<div class="row-fluid">
  <div class="span9">
	
	
  	<div class="w-box">
	
	  <div class="w-box-content cnt_a" id="depositDetailDiv">
		<%@ include file="DemandNote.jsp" %>					
	   </div>
	</div>
	
	</div>
    <div class="span3">
		<div class="w-box">
			<div class="w-box-header">
				<h4>Demand Note History</h4>
			</div>
			<div class="w-box-content cnt_a" style="padding: 3px;">
				<div class="row-fluid">
	            	<div class="span12" id="demandNoteListTbl">
						
					</div>
				</div>
			</div>
		</div>
   </div>

</div>



                                          
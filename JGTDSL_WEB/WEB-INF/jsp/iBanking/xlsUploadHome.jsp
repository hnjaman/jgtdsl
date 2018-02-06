<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("iBankingXlsUploadHome.action");
	setTitle("XLS Upload");	
</script>


<div class="row-fluid">
   <div class="span3"></div>
   <div class="span6">
       <div class="w-box">
           <div class="w-box-header"><h4>Upload Xls File</h4></div>
           <div class="w-box-content cnt_a" style="text-align: center;">
               <div class="row-fluid">
                   <div class="span12">
                       <p class="formSep"><label class="muted">File:</label> 
	                       <input type="file" name="upload" id="upload" />
                       </p>
                    </div>
               </div>
               <div class="formSep">
							<div id="aDiv" style="height: 10px;"></div>
			   </div>
			   <div class="formSep sepH_b">    
				    <span style="float: left;margin-top: 6px;font-size: 13px;color: red;padding-left: 15px;" id="wait_div"></span>							    							    			
				    <button id="btn_upload" onclick="afu()" type="button" class="btn btn-beoro-3">Upload</button>		      
			   </div>
           </div>
  	  </div>
   </div>
</div>

<script type="text/javascript">
function afu() {
	
    $
        .ajaxFileUpload({
            url: 'iBankingFileUploader.action',
            secureuri: false,
            fileElementId: 'upload',
            dataType: 'JSON',            
            success: function(data, status) {

                if (data == 'Not a Valid type') {
                    alert("Not a Valid type");
                    // return false;
                } else if (data == "largefile") {
                    alert("File size smaller than 150KB is allowed.");
                    // return false;
                } else {
                    if (navigator.appName == 'Opera') {

                        document.getElementById('userImg').innerHTML = "";
                        document.getElementById('userImg').innerHTML = data;
                    } else {
                        document.getElementById('userImg').innerHTML = "";
                        document.getElementById('userImg').value = "/JGTDSL_WEB/" + data + "";
                        document.getElementById('userImg').src = "/JGTDSL_WEB/" + data + "";
                    }
                }
                 $("#upload").change(afu);

            },
            error: function(data, status, e) {
                alert(e);
            }
        });
}
</script>

<!-- 
<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/journalVoucher.js"></script>
 -->
 
//vat rabate entry field validation. updated on september 12

 
$("#vat_rebate").keyup(function(e) {
    if($(this).val()<0)
     {
     $(this).val("");
     alert("Vat rebate cannot be negetive, please enter a positive number");
     $(this).focus();
     }
    
     });
//End of vat rabate entry field validation        


$("#btnInsert").click(function () {
                var eAppID =$("#appliance_list option:selected").val();
                var eAppName =$("#appliance_list option:selected").text();
                var eAppQnt = $("#appliance_qnt").val();
                //alert(eAppQnt);
                //var appNo= document.getElementById("appliance_qnt");
                var deptNo = "";
                var rowCnt = $("#tblBody tr").size();
                	//eAppID === "" && eAppName === "" && 
                if (eAppID===""){
                	alert("Select Appliance Type");
                	cbColor($("#appliance_list"),"e");
                }
                else if(eAppQnt === "") {
                		cbColor($("#appliance_list"),"v");
                		alert("Enter Appliance Quantity");
                		cbColor($("#appliance_qnt"),"e");
                }
                else {
                	cbColor($("#appliance_list"),"v");
                	cbColor($("#appliance_qnt"),"v");
                    if (rowCnt === 0) {
                        $("<tr><td id='Items" + rowCnt + "'>" + eAppID + "</td><td>" + eAppName + "</td><td align='center'>" + eAppQnt + "</td><td  align='justify'>" + deptNo +" <input type='button' value='Delete' class='btn btn-danger' id='btnDelete"+rowCnt+"'/></td></tr>").appendTo("#tblBody");
                        $("input[id^=txt]").val("");
                        //alert($("#appliance_qnt").val());
                        $("#appliance_qnt").val('');
                       
                        $("input[id^=btnDelete]").bind("click", function () {
                        $(this).parent().parent().remove();
                        getApplianceInfoStr();

                        });

                    }
                    else {
                    	cbColor($("#appliance_list"),"v");
                    	cbColor($("#appliance_qnt"),"v");
                        var dupCount = 0;
                        for (var i = 0; i < rowCnt; i++) {
                            var num = $("#Items" + i).html().toString();
                            if (eAppID == num) {
                                dupCount++;
                            }
                        }
                        if (dupCount === 0) {
                            $("<tr><td id='Items" + rowCnt + "'>" + eAppID + "</td><td>" + eAppName + "</td><td align='center'>" + eAppQnt + "</td><td  align='justify'>" + deptNo + " <input type='button' value='Delete' id='btnDelete" + rowCnt + "'/></td></tr>").appendTo("#tblBody");
                            $("input[id^=txt]").val("");
                            $("#appliance_qnt").val('');
                            $("input[id^=btnDelete]").bind("click", function () {
                             $(this).parent().parent().remove();
                             getApplianceInfoStr();
                            });
                        }
                        else {
                            alert("Your entered Appliance is already exists in the table !");
                            $("#appliance_qnt").val('');
                        }
                    }
                }
                getApplianceInfoStr();
               
            });


  function getApplianceInfoStr(){
	  var table = $("#tblDetails tbody");
      var applinace_info_str="";

      table.find('tr').each(function (i) {
      	
          var $tds = $(this).find('td'),
              productId = $tds.eq(0).text(),
              product = $tds.eq(1).text(),
              Quantity = $tds.eq(2).text();
              applinace_info_str=applinace_info_str+productId.trim()+"#"+product.trim()+"#"+Quantity.trim()+"@"
          // do something with productId, product, Quantity
          /*alert('Row ' + (i + 1) + ':\nId: ' + productId.trim()
                + '\nProduct: ' + product.trim()
                + '\nQuantity: ' + Quantity.trim());*/
              
      });

      $("#appliance_info_str").val(applinace_info_str);
  }          
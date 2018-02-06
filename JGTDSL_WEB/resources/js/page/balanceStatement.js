function checkType(type){
	if(type=="area_wise")
	{
	 disableField("customer_category");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 enableField("area_id");
	}
	else if(type=="by_category"){
	 disableChosenField("customer_id");
	 enableField("customer_category","area_id");
	 autoSelect("customer_category","area_id");
	}
	else if(type=="individual"){
	 enableChosenField("customer_id");
	 disableField("customer_category","area_id");
	 resetSelectBoxSelectedValue("customer_category","area_id");
	}
	
	if(type=="month_wise")
		{
		hideElement("from_to_date_div");
		showElement("month_div","year_div");
		}else if(type=="year_wise")
			{			
			hideElement("from_to_date_div","month_div");
			showElement("year_div");
			}else if(type=="date_wise")
			{
				
				hideElement("month_div","year_div");
				showElement("from_to_date_div");
				
				}
}	

$("#billing_month").val(getCurrentMonth());
$("#billing_year").val(getCurrentYear());

$("#month_div").hide();
$("#year_div").hide();


Calendar.setup({
    inputField : "to_date",
    trigger    : "to_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  Calendar.setup({
    inputField : "from_date",
    trigger    : "from_date",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
function fetchCategoryName()
{

	$("#category_name").val($( "#customer_category option:selected" ).text());
}

$("#customer_id").unbind();
$("#customer_id").autocomplete($.extend(true, {}, acMCustomerOption,{
	    serviceUrl: sBox.CUSTOMER_LIST,
    	onSelect:function (){
    		getCustomerInfo("",$('#customer_id').val());
    	},
}));
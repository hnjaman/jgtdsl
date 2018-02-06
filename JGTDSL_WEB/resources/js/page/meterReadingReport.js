function checkType(type){
	if(type=="area_wise")
	{
	 disableChosenField("customer_id");
	 disableField("customer_category");
	 resetSelectBoxSelectedValue("customer_category");
	 autoSelect("area_id");
	 enableField("area_id");
	 hideElement("account_div","individual_div");
	 showElement("area_category_div","month_year_div");
	}
	else if(type=="by_category"){
	 disableChosenField("customer_id");
	 enableField("customer_category","area_id");
	 autoSelect("customer_category","area_id");
	 hideElement("account_div","individual_div");
	 showElement("area_category_div","month_year_div");
	}
	else if(type=="by_individual"){

	 hideElement("area_category_div","month_year_div");
	 showElement("individual_div");
	 
	}
	else if(type=="by_account"){
		
		hideElement("area_category_div","individual_div");
		showElement("account_div");
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

hideElement("individual_div","account_div","month_div","year_div","from_to_date_div");
Calendar.setup({
    inputField : "to_date_ind",
    trigger    : "to_date_ind",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  Calendar.setup({
    inputField : "from_date_ind",
    trigger    : "from_date_ind",
	eventName : "focus",
    onSelect   : function() { this.hide();},        
    showTime   : 12,
    dateFormat : "%d-%m-%Y",
	showTime : true
	//onBlur: focusNext		
  });
  
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
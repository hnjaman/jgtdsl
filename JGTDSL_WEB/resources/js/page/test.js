function loadNewMeterTestData()
	{
		var sl=Math.floor((Math.random() * 999999999999) + 111111111111);
		$("#meter_sl_no").val(sl);$("#meter_mfg").val("01");$("#meter_year").val("2015");
		$("#measurement_type_str").val("0");
		$("#evc_sl_no").val(sl+1);$("#evc_model").val("01");$("#evc_year").val("2015");
		
		$("#meter_type").val("01");
		$("#g_rating").val("01");
		$("#conn_size").val("2");			
		$("#max_reading").val("99999");
		$("#ini_reading").val("00000");
		$("#ini_pressure").val("1.2");
		$("#ini_temperature").val("1.3");
		$("#meter_rent").val("500");
		$("#installed_date").val(getCurrentDate());
		$("#meter_remarks").val("Remarks"+sl);
		$("#pressure").val(1);
		$("#temperature").val(1);
	}
package org.jgtdsl.actions;
import org.jgtdsl.dto.DuesSurchargeDTO;
import org.jgtdsl.dto.OthersDto;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.ReportService;

import com.google.gson.Gson;

public class Report extends BaseAction {

	private static final long serialVersionUID = 4216989970426874378L;
	
	TariffDTO adjustmetAccountPayable=new TariffDTO();

	
	public String saveAdjustmentAccountPayable(){

		ReportService reportService=new ReportService();
		
		ResponseDTO response=reportService.saveAdjustmentAccountPayable(adjustmetAccountPayable);
		setJsonResponse(response);
        return null;
	}


	public TariffDTO getAdjustmetAccountPayable() {
		return adjustmetAccountPayable;
	}


	public void setAdjustmetAccountPayable(TariffDTO adjustmetAccountPayable) {
		this.adjustmetAccountPayable = adjustmetAccountPayable;
	}
	


}

package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.DemandNoteDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.DemandNoteService;
import org.jgtdsl.models.GasPurchaseService;

public class GasPurchase extends BaseAction {

	private GasPurchaseDTO gasPurchase;

	public String saveGasPurchaseInfo()
	{
		GasPurchaseService gasPurchaseServoce=new GasPurchaseService();
		ResponseDTO response=gasPurchaseServoce.saveGasPurchaseInfo(gasPurchase);
		setJsonResponse(response);
		return null;
	}

	public GasPurchaseDTO getGasPurchase() {
		return gasPurchase;
	}

	public void setGasPurchase(GasPurchaseDTO gasPurchase) {
		this.gasPurchase = gasPurchase;
	}
	
}

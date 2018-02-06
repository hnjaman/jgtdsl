package org.jgtdsl.actions;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.SurchargeService;

public class Surcharge extends BaseAction {

	private static final long serialVersionUID = 4216989970426874378L;
	public String surcharge_bills;
	public String pay_date;
	
	public String saveSurchargeInfo(){

		SurchargeService surchargeService=new SurchargeService();
		ResponseDTO response=surchargeService.updateSurchargeInfo(surchargeService.getSurchargeList(surcharge_bills),pay_date);
		setJsonResponse(response);
		return null;
	}


	public String getSurcharge_bills() {
		return surcharge_bills;
	}

	public void setSurcharge_bills(String surchargeBills) {
		surcharge_bills = surchargeBills;
	}


	public String getPay_date() {
		return pay_date;
	}


	public void setPay_date(String pay_date) {
		this.pay_date = pay_date;
	}


}

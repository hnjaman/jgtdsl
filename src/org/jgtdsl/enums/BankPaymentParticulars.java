package org.jgtdsl.enums;

public enum BankPaymentParticulars {
	
	COR_TAX(0,"Corporate Tax(Adv. Pay)"),
	BANK_CHARGE_COMMISSION(1,"Bank Charges & Comminssion"),
	RATES_TAXES(2,"Rates & Taxes"),
	EXCISE_DUTY(3,"Excise Duty");

    private String label;
    private int id;

    private BankPaymentParticulars(int id,String label) {
        this.id=id;
    	this.label = label;
    }

    public String getLabel() {
        return label;
    }
    public int getId() {
        return id;
    }
}

package org.jgtdsl.enums;

public enum DepositType {
	
	CASH_BANK(0,"Cash/Bank"),
    BANK_GURANTEE(1,"Bank Guarantee"),
    PSP(2,"PSP"),
    FDR(3,"FDR"),
    Others(4,"Others");

    private String label;
    private int id;

    private DepositType(int id,String label) {
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

package org.jgtdsl.enums;

public enum BillPurpose {

	PUBPOSE1(0,"General Billing"),
	PUBPOSE2(1,"Average Billing");
    
    private String label;
    private int id;

    private BillPurpose(int id,String label) {
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

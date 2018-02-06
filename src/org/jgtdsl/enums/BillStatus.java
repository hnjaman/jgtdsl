package org.jgtdsl.enums;

public enum BillStatus {

	
	INITIALIZED(0,"Initialized"),
	PROCESSED(1,"Approved"),
	COLLECTED(2,"Collected");
    
    private String label;
    private int id;

    private BillStatus(int id,String label) {
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

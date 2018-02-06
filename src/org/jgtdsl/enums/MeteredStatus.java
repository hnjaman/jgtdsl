package org.jgtdsl.enums;

public enum  MeteredStatus {

	NONMETERED(0,"Non-Metered"),
	METERED(1,"Metered");
    

    private String label;
    private int id;

    private MeteredStatus(int id,String label) {
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

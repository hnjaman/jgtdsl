package org.jgtdsl.enums;

public enum  DisconnType {

	PERMANET(0,"Permanent"),
    TEMPORARY(1,"Temporary");
	

    private String label;
    private int id;

    private DisconnType(int id,String label) {
    	this.id=id;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    public int getId(){
    	return id;
    }
    
}

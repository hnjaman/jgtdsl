package org.jgtdsl.enums;

public enum  MeterCategory {

	JANUARY(0,"Non-EBC"),
	FEBRUARY(1,"EBC");

    private String label;
    private int id;

    private MeterCategory(int id,String label) {
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

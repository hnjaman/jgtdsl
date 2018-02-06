package org.jgtdsl.enums;

public enum LoadChangeType {

	LOAD_CHANGE(0,"Load Change"),
    PRESSURE_CHANGE(1,"Pressure Change"),
    LOAD_PRESSURE_CHANGE(2,"Load & Pressure Change");

    private String label;
    private int id;

    private LoadChangeType(int id,String label) {
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

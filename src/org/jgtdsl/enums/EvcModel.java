package org.jgtdsl.enums;

public enum  EvcModel {

	EK220(0,"EK220"),
	OTHERS(1,"Others");

    private String label;
    private int id;

    private EvcModel(int id,String label) {
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

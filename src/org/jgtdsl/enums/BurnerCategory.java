package org.jgtdsl.enums;

public enum BurnerCategory {

	SINGLE_BURNER(0,"Single Burner"),
	DOUBLE_BURNER(1,"Double Burner");
    
    private String label;
    private int id;

    private BurnerCategory(int id,String label) {
        this.id=id;
    	this.label = label;
    }

    public String getLabel() {
        return label;
    }
    public int getId() {
    	return id+1;
    }
    
}


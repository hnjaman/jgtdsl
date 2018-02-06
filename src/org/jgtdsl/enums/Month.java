package org.jgtdsl.enums;

public enum  Month {

	JANUARY(0,"January"),
	FEBRUARY(1,"February"),
	MARCH(2,"March"),
	APRIL(3,"April"),
	MAY(4,"May"),
	JUNE(5,"June"),
	JULY(6,"July"),
	AUGUST(7,"August"),
	SEPTEMBER(8,"September"),
	OCTOBER(9,"October"),
	NOVEMBER(10,"November"),
	DECEMBER(11,"December");

    private String label;
    private int id;

    private Month(int id,String label) {
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

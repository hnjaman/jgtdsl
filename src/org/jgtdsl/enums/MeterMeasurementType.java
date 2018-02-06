package org.jgtdsl.enums;

public enum  MeterMeasurementType {

	NORMAL(0,"Normal"),
	EVC(1,"EVC");

    private String label;
    private int id;

    private MeterMeasurementType(int id,String label) {
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

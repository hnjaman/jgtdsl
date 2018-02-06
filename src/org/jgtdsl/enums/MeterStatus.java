package org.jgtdsl.enums;

public enum MeterStatus {

	DISCONNECT(0,"Disconnected"),
	CONNECTED(1,"Connected");
    

    private String label;
    private int id;

    private MeterStatus(int id,String label) {
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

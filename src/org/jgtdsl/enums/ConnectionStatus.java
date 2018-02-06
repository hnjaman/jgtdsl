package org.jgtdsl.enums;

public enum  ConnectionStatus {

	DISCONNECTED(0,"Disconnected"),
	CONNECTED(1,"Connected"),
	NEWLY_APPLIED(2,"Newly Applied");
    

    private String label;
    private int id;

    private ConnectionStatus(int id,String label) {
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

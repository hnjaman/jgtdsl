package org.jgtdsl.enums;

public enum ConnectionType {

	MAIN(0,"Main-Connection"),
	SUB(1,"Sub-Connection");
    
    private String label;
    private int id;

    private ConnectionType(int id,String label) {
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

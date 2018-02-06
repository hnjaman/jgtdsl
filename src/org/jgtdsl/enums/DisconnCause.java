package org.jgtdsl.enums;

public enum  DisconnCause {

	TYPE1(0,"Connected Regular Customer"),
	TYPE2(1,"Reconnected after Regularization"),
	TYPE3(2,"Connected Litigation Continue"),
	TYPE4(3,"Demand Note Deposited but Not Connected"),
	TYPE5(4,"Disconnected all Bill Cleared Security Money not Returned"),
	TYPE6(5,"Disconnected all Bill Cleared Security Money Returned"),
	TYPE7(6,"Disconnected Un-cleared Bill due to Application"),
	TYPE8(7,"Disconnected for Non-payment of Bill No Litigation"),
	TYPE9(8,"Disconnected for Illegal Activities No Litigation"),
	TYPE10(9,"Disconnected Litigation Continue"),
	TYPE11(10,"Disconnected but Riser not Permanently Removed"),
	TYPE12(11,"Connected Defaulter Customer"),
	TYPE13(12,"Temporarily Disconnected Customer"),
	TYPE14(13,"Riser Installed but not Gas Connected"),
	TYPE15(14,"Disconnected for Other Reasons"),
	TYPE16(15,"Demand Note not Deposited");

	
	
    private String label;
    private int id;

    private DisconnCause(int id,String label) {
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

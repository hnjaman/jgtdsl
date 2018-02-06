package org.jgtdsl.enums;

public enum ReadingPurpose {

	NEW_METER(0,"New Meter"),
	RECONNECT_METER(1,"Reconnect Meter"),
	GENERAL_BILLING(2,"General Billing"),
	AVERAGE_BILLING(3,"Average Billing"),
	DISCONNECT_METER(4,"Disconnect Meter"),
	PRESSURE_CHANGE(5,"Pressure Change"),
	BILL_ON_MAX_LOAD(6,"Bill on Maximum Load"),
	PROPORTIONAL_BILL(7,"Proportional Bill"),
	ADJUSTMENT_BILL(8,"Adjustment Bill"),
	BILL_ON_MIN_LOAD(9,"Bill on Minimum Load"),
	BILL_ON_ACTUAL_LOAD(10,"Bill on Actual Load");
	
    
    private String label;
    private int id;

    private ReadingPurpose(int id,String label) {
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


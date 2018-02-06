package org.jgtdsl.enums;

public enum ReconciliationCause {

	CAUSE_1(0,"Cheque Deposited by us but not collected by bank"),
	CAUSE_2(1,"Cheque Deposited by us but not collected by bank"),
	CAUSE_3(2,"Amount Received from STD -   but effect of which not given by bank"),
	CAUSE_4(3,"**** Amount wrongly deposited on STD- ----- by Bank 	"),
	CAUSE_5(4,"Amount wrongly deducted by bank against revenue stamp"),
	CAUSE_6(5,"Interest given by Bank but effect of which not shown by them");
/*	BILL_ON_MAX_LOAD(6,"Bill on Maximum Load"),
	PROPORTIONAL_BILL(7,"Proportional Bill"),
	ADJUSTMENT_BILL(8,"Adjustment Bill"),
	BILL_ON_MIN_LOAD(9,"Bill on Minimum Load"),
	BILL_ON_ACTUAL_LOAD(10,"Bill on Actual Load");*/
	
    
    private String label;
    private int id;

    private ReconciliationCause(int id,String label) {
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


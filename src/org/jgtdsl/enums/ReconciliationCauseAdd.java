package org.jgtdsl.enums;

public enum ReconciliationCauseAdd {

	 R0(0,"Cheque Deposited by us but not collected by bank"),
	 R1(1,"P/O Deposited by us but not collected by bank"),
	 R2(2,"Amount Received from STD - XXX but effect of which not given by bank"),
	 R3(3,"Amount wrongly deposited on STD- XXX by Bank"),
	 R4(4,"Amount wrongly deducted by bank against revenue stamp"),
	 R5(5,"Interest given by Bank but effect of which not shown by them"),
	 R6(6,"Excess Income Tax deducted by bank on interest earned"),
	 R7(7,"Amount / Gas bill Collected by Bank but not Shown by them"),
	 R8(8,"Collection charges deducted by bank but not shown by them"),
	 R9(9,"B/C & Commission Deducted by bank but not shown by them"),
	 R10(10,"Excise Duty Deducted by bank but not shown by them "),
	 R11(11,"Others (if Any)");
	
    
    private String label;
    private int id;

    private ReconciliationCauseAdd(int id,String label) {
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


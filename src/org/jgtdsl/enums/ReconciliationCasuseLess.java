package org.jgtdsl.enums;

public enum ReconciliationCasuseLess {

	 R0(0,"Amount transferred to STD - XXX but effect of which not given by bank"),
	 R1(1,"**** Amount wrongly deposited on this A/c in lieue of STD-XXX"),
	 R2(2,"Income Tax deducted by bank  on interest but not shown by them"),
	 R3(3,"Others (if Any)");
	
    
    private String label;
    private int id;

    private ReconciliationCasuseLess(int id,String label) {
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


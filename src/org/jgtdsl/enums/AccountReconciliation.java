package org.jgtdsl.enums;

public enum  AccountReconciliation {

	NA(0,"N/A"),
	ACCOUNT_1(1,"111"),
	ACCOUNT_2(2,"115"),
	ACCOUNT_3(3,"117"),
	ACCOUNT_4(4,"125"),
	ACCOUNT_5(5,"256"),
	ACCOUNT_6(6,"32"),
	ACCOUNT_7(7,"92"),
	ACCOUNT_8(8,"32"),
	ACCOUNT_9(9,"33");
	

    private String label;
    private int id;

    private AccountReconciliation(int id,String label) {
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

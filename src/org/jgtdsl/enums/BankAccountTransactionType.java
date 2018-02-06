package org.jgtdsl.enums;

public enum BankAccountTransactionType {

	SECURITY_OTHER_DEPOSIT(0,"Security/Other Deposit"),
	SALES_COLLECTION(1,"Sales Collection"),
	PAYMENT(2,"Bank Payment"),
	RECEIVE(3,"Bank Receive"),
	TRANSFER(4,"Bank Transfer"),
	INTEREST(5,"Bank Interest"),
	MARGIN_PAYMENT(6,"Margin Payment"),
	FEES_PAYMENT(7,"Fees Payment");

    private String label;
    private int id;

    private BankAccountTransactionType(int id,String label) {
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

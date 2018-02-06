package org.jgtdsl.enums;

public enum BankReceiveParticualrs {
	
	INTEST_INCOME(0,"Interest Income"),
	SALES_OF_APPLICATION(1,"Sales of Applications"),
	SALE_DUPLICATE_CONN_CARD(2,"Sale Duplicate Connection Card"),
	SALE_BILL_BOOK(3,"Sale of Bill Books"),
	REVENUE_STAMP(4,"Revenue Stamp Refund By Bank"),
	SALES_OF_OIL(5,"Sale of oil");

    private String label;
    private int id;

    private BankReceiveParticualrs(int id,String label) {
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

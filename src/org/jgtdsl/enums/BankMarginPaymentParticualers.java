package org.jgtdsl.enums;

public enum BankMarginPaymentParticualers {
	
	BGFCL(0,"BGFCL"),
	BGFCLIT(1,"BGFCL Income Tax"),
	SGFCL(2,"SGFL"),
	SGFCLIT(3,"SGFL Income Tax"),
	PDF(4,"PDF"),
	PDFIT(5,"PDF Income Tax"),
	BAPEX(6,"BAPEX"),
	BAPEXIT(7,"BAPEX Income Tax"),
	GTCL(8,"GTCL"),
	GTCLIT(9,"GTCL Income Tax"),
	DWELLHEAD(10,"DWELLHEAD"),
	DWELLHEADIT(11,"DWELLHEAD Income Tax"),
	GD_FUND(12,"Gas Development Fund"),
	GD_FUNDIT(13,"Gas Development Fund Income Tax"),
	AVALUE(14,"Asset Value"),
	AVALUEIT(15,"Asset Value Income Tax");
	
	

    private String label;
    private int id;

    private BankMarginPaymentParticualers(int id,String label) {
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

package org.jgtdsl.enums;

public enum DepositPurpose {
	
	SECURITY_DEPOSIT(9,"Security Deposit"),
	CONNECTION(10,"Connection Fee"),
	DISCONNECTION_RECONECTION(11,"Disconnection/Reconnection Fee"),
	LOAD_CHANGE(12,"Load Change Fee"),
	CUSTOMER_TYPE(13,"Customer Type Change Fee"),
	NAME_CHANGE(14,"Name Change Fee"),
	COMMISIONING(15,"Gas Line Commisioning Fee"),
	MATERIAL_COST(16,"Material Cost"),
	LEHAL_FEE(17,"Legal Fee"),
	OTHERS(18,"Others"),
	INSTALLATION(19,"Installation Cost"),
	BILL_BOOK(20,"New Bill Book Fee"),
	CONNECTION_APPLICATION_FEE(21,"New Connection Application Fee"),
	DUPLICATE_CONN_FEE(22,"Duplicate Connection Card Fee"),
	SERVICE_CHARGE(23,"Service Charge"),
	CUSTOMER_FINANCE(24,"Customer Finance"),
	PENALTY(25,"Penalty"),
	ILLIGAL_GAS_USE(26,"Illigal gas usage fee"),
	CONTIGENCY(27,"Contingency Fee"),
	ROAD_CUTTING(28,"Road Cutting Fee"),
	RISER_SHIF(29,"Riser Sifting Fee"),
	NDT(30,"N.D.T"),
	BANK_INTEREST(31,"Bank Interest"),
	EXCHASE_GAS_BILL(32,"Exchase gas bill at un-approved burner"),
	FINE_AT_BURNER(33,"Fine at un-approved burner"),
	EXCESS_BILL_M(34,"Excess Gas Bill (Metered)"),
	EXCESS_BILL_NM(35,"Excess Gas Bill (Non-Metered)"),
	COLLECTION_ILLIGALACT(36,"Collection for Illegal activities"),
	OTHERS_ILLIGAL(37,"Others Againest Illigale activies");
	
    private String label;
    private int id;

    private DepositPurpose(int id,String label) {
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

package org.jgtdsl.enums;



public enum UserRole {
	
	Superadmin(0,"Super Admin"),
	Assistantmanager(1,"Assistant Manager"),
	Manager(2,"Manager"),
	Salesuser(3,"SalesUser");
	
//	EK220(0,"EK220"),
//	OTHERS(1,"Others");

    private String label;
    private int id;

    private UserRole(int id,String label) {
        this.id=id;
    	this.label = label;
    	
    	//
    }
    
    

    public String getLabel() {
        return label;
    }
    public int getId() {
        return id;
    }

	
}

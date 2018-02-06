package org.jgtdsl.enums;

public enum  Area {
	SYLHET_EAST(0,"Sylhet_East"),
	SYLHET_WEST(1,"Sylhet_West"),
	SYLHET_SOUTH(2,"Sylhet_South"),
	ISHWARDI(3,"ISHWARDI"),
	BOGRA(4,"BOGRA"),
	Shahjadpur(5,"Shahjadpur"),
	BERA(6,"BERA"),
	SHANTHIA(7,"SHANTHIA"),
	ULLAHPARA(8,"ULLAHPARA"),
	//
	SIRAJGANJ(9,"Sirajganj"),
	SYLHET_WESTError(10,"Sylhet_WestError"),
	PABNA(11,"PABNA"),
	CHATTAK(12,"Chattak"),
	SUNAMGONJ(13,"Sunamgonj"),
	FENCHUGONJ(14,"Fenchugonj"),
	GOLAPGONJ(15,"Golapgonj"),
	MAULVIBAZAR(16,"Maulvibazar"),
	SRIMANGAL(17,"Srimangal"),
	KULAURA(18,"Kulaura"),
	SHAHJIBAZAR(19,"Shahjibazar"),
	HOBIGONJ(20,"Hobigonj"),
	BEANIBAZAR(21,"Beanibazar"),
	NOBIGONJ(22,"Nobigonj"),
	SYLHET_NORTH(23,"Sylhet_North");
	
	
	

    private String label;
    private int id;

    private Area(int id,String label) {
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

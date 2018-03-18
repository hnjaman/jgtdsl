package org.jgtdsl.enums;

public enum  Area {
	SYLHET_EAST(0,"Sylhet_East"),
	SYLHET_WEST(1,"Sylhet_West"),
	SYLHET_SOUTH(2,"Sylhet_South"),
	ISHWARDI(3,"I"),
	BOGRA(4,"B"),
	Shahjadpur(5,"S"),
	BERA(6,"BE"),
	SHANTHIA(7,"SH"),
	ULLAHPARA(8,"U"),
	//
	SIRAJGANJ(9,"Sir"),
	SYLHET_WESTError(10,"Sylhet_WestError"),
	PABNA(11,"P"),
	CHATTAK(12,"Chattak"),
	SUNAMGONJ(13,"Sunamgonj"),
	FENCHUGONJ(14,"Fenchugonj"),
	GOLAPGONJ(15,"Golapgonj"),
	MAULVIBAZAR(16,"Maulvibazar"),
	SRIMANGAL(17,"Srimangal"),
	BEANIBAZAR(18,"Beanibazar"),
	SHAHJIBAZAR(19,"Shahjibazar"),
	HOBIGONJ(20,"Hobigonj"),
	KULAURA(21,"Kulaura"),
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

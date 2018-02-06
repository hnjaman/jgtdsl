package org.jgtdsl.dto;

public class ChangeTypeDTO {

	public ChangeTypeDTO(String change_type_id,String change_type_name)
	{
		this.change_type_id=change_type_id;
		this.change_type_name=change_type_name;
	}
	
	private String change_type_id;
	private String change_type_name;
	
	public String getChange_type_id() {
		return change_type_id;
	}
	public void setChange_type_id(String changeTypeId) {
		change_type_id = changeTypeId;
	}
	public String getChange_type_name() {
		return change_type_name;
	}
	public void setChange_type_name(String changeTypeName) {
		change_type_name = changeTypeName;
	}
	
}

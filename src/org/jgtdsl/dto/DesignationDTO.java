package org.jgtdsl.dto;

import com.google.gson.Gson;

public class DesignationDTO {
	
	private String designation_id;
	private String designation_name;
	private String short_term;
	private String view_order;
	
	public String getDesignation_id() {
		return designation_id;
	}
	public void setDesignation_id(String designationId) {
		designation_id = designationId;
	}	
	
	public String getDesignation_name() {
		return designation_name;
	}
	public void setDesignation_name(String designationName) {
		designation_name = designationName;
	}
	public String getShort_term() {
		return short_term;
	}
	public void setShort_term(String shortTerm) {
		short_term = shortTerm;
	}
	public String getView_order() {
		return view_order;
	}
	public void setView_order(String viewOrder) {
		view_order = viewOrder;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	

}

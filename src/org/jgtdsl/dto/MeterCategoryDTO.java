package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MeterCategoryDTO {
	
	public String category_id;
	private String category_name;
	private String description;
	private int status;
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String categoryId) {
		category_id = categoryId;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String categoryName) {
		category_name = categoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }

}

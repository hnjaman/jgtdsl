package org.jgtdsl.dto;

public class CustomerCategoryDTO {

	private String category_id;
	private String category_name;
	private String category_type;
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
	
	public String getCategory_type() {
		return category_type;
	}
	public void setCategory_type(String categoryType) {
		category_type = categoryType;
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
		return "{\"category_id\":\"" + category_id + "\", \"category_name\":\"" + category_name + "\", \"category_type\":\"" + category_type + "\", \"description\":\"" + (description==null?"":description.replaceAll("[\\r\\n]+", "<br/>")) + "\", \"status\":\"" + status + "\"}";
    }
	
}

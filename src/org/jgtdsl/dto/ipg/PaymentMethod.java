package org.jgtdsl.dto.ipg;

import com.google.gson.Gson;

public class PaymentMethod {

	private String id;
	private String name;
	private String imagUrl;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImagUrl() {
		return imagUrl;
	}
	public void setImagUrl(String imagUrl) {
		this.imagUrl = imagUrl;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

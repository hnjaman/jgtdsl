package org.jgtdsl.dto;

import com.google.gson.Gson;

public class MeterGRatingDTO {
	
	private String rating_id;
	private String rating_name;
	private String description;
	private int status;
	private int view_order;
	public String getRating_id() {
		return rating_id;
	}
	public void setRating_id(String ratingId) {
		rating_id = ratingId;
	}
	public String getRating_name() {
		return rating_name;
	}
	public void setRating_name(String ratingName) {
		rating_name = ratingName;
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
	public int getView_order() {
		return view_order;
	}
	public void setView_order(int viewOrder) {
		view_order = viewOrder;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	

}

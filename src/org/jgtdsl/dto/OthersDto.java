package org.jgtdsl.dto;

import com.google.gson.Gson;

public class OthersDto {

	private String totalOthersComment;
	private double totalOthersAmount;
	private String totalOthersCommentString;
	private String totalOthersAmountString;
	public String getTotalOthersComment() {
		return totalOthersComment;
	}
	public void setTotalOthersComment(String totalOthersComment) {
		this.totalOthersComment = totalOthersComment;
	}
	public double getTotalOthersAmount() {
		return totalOthersAmount;
	}
	public void setTotalOthersAmount(double totalOthersAmount) {
		this.totalOthersAmount = totalOthersAmount;
	}
	public String getTotalOthersCommentString() {
		return totalOthersCommentString;
	}
	public void setTotalOthersCommentString(String totalOthersCommentString) {
		this.totalOthersCommentString = totalOthersCommentString;
	}
	public String getTotalOthersAmountString() {
		return totalOthersAmountString;
	}
	public void setTotalOthersAmountString(String totalOthersAmountString) {
		this.totalOthersAmountString = totalOthersAmountString;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
}

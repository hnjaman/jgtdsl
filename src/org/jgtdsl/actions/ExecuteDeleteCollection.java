package org.jgtdsl.actions;

import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.CollectionService;

public class ExecuteDeleteCollection extends BaseAction {
	private String scroll_no;
	ResponseDTO responce;
	
	public String execute(){
		CollectionService collectionservice = new CollectionService();
		responce=collectionservice.executeDeleteCollection(scroll_no);
		
		return "success";
	}

	public String getScroll_no() {
		return scroll_no;
	}

	public void setScroll_no(String scroll_no) {
		this.scroll_no = scroll_no;
	}

	public ResponseDTO getResponce() {
		return responce;
	}

	public void setResponce(ResponseDTO responce) {
		this.responce = responce;
	}
	
	
}

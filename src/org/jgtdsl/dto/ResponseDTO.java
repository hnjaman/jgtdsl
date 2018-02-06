package org.jgtdsl.dto;

import org.jgtdsl.utils.AC;

public class ResponseDTO {

	private String messasge;
	private boolean response;
	private String dialogCaption;
	
	public String getMessasge() {
		return messasge;
	}
	public void setMessasge(String messasge) {
		this.messasge = messasge;
	}
	public boolean isResponse() {
		return response;
	}
	public void setResponse(boolean response) {
		this.response = response;
		if(response)
			this.dialogCaption=AC.DIALOG_CAPTION_SUCCESS;
		else
			this.dialogCaption=AC.DIALOG_CAPTION_ERROR;
	}
	public String getDialogCaption() {
		return dialogCaption;
	}
	public void setDialogCaption(String dialogCaption) {
		this.dialogCaption = dialogCaption;
	}
	
	
}

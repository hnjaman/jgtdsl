package org.jgtdsl.actions;


public class DowloadHome extends BaseAction{
	private static final long serialVersionUID = 1L;
	private String customer_id;
	private String demand_note_id;
	
	

	public String demandNoteDownloadHome(){
		return SUCCESS;
	}

	public String getDemand_note_id() {
		return demand_note_id;
	}

	public void setDemand_note_id(String demand_note_id) {
		this.demand_note_id = demand_note_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}	
}
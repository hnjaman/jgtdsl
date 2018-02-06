package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.DemandNoteDTO;
import org.jgtdsl.dto.DepositTypeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DemandNoteService;
import org.jgtdsl.models.DepositService;

public class DemandNote extends BaseAction{
	private static final long serialVersionUID = -4026297230147889320L;
	private ArrayList<DepositTypeDTO> depositList=new ArrayList<DepositTypeDTO>();
	private ArrayList<DemandNoteDTO> demandNoteList=new ArrayList<DemandNoteDTO>();
	private DemandNoteDTO dNote;
	private CustomerDTO customer;
	private String customer_id;
	private String demand_note_id;
	private String mode;
	private String status;
	
	public String createDemandNote(){
		DepositService ds=new DepositService();
		this.depositList=ds.getDepositTypeList(0,0,"STATUS=1","TYPE_NAME_BAN","ASC",0);
		return SUCCESS;
	}

	public String demandNoteDataEntry(){
		if (mode == null)
			mode = "";

		if (customer_id == null || customer_id.equalsIgnoreCase(""))
			return "input";

		DemandNoteService ds = new DemandNoteService();
		CustomerService cs = new CustomerService();
		CustomerDTO customer = cs.getCustomerInfo(customer_id);
		this.customer = customer;
		if (!mode.equals("S")) {
			dNote = ds.getDemandNote(this.customer_id, demand_note_id);
		}
	
		return SUCCESS;
	}	
	
	public String getDemandNoteDataEntry(){
		
		
		DemandNoteService ds=new DemandNoteService();
		CustomerService cs=new CustomerService();
		CustomerDTO customer=cs.getCustomerInfo(customer_id);
		this.customer=customer;
		dNote=ds.getDemandNote(this.customer_id,demand_note_id);
		if(status==null)
		{
			status="Edit";
		}else if(status.equals("U"))
		{
			status="Update";
			
		}
		return SUCCESS;
	}
	
	public String saveDemandNote()
	{
		
		ResponseDTO response;
		if(session.get("role").equals("Sales User")){
			response=DemandNoteService.saveDemandNote(dNote);
			setJsonResponse(response);
		}
		return null;
	}
	
	
	public String updateDemandNote()
	{
		
		ResponseDTO response;
		if(session.get("role").equals("Sales User")){
			response=DemandNoteService.updateDemandNote(dNote);
			setJsonResponse(response);
		}
		return null;
	}
	
	public String getListOfDemandNote()
	{
		DemandNoteService ds=new DemandNoteService();
		demandNoteList=ds.getDemandNoteList(customer_id);
		return SUCCESS;
	}
	
	
	


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDemand_note_id() {
		return demand_note_id;
	}

	public void setDemand_note_id(String demand_note_id) {
		this.demand_note_id = demand_note_id;
	}

	public ArrayList<DemandNoteDTO> getDemandNoteList() {
		return demandNoteList;
	}

	public void setDemandNoteList(ArrayList<DemandNoteDTO> demandNoteList) {
		this.demandNoteList = demandNoteList;
	}

	public ArrayList<DepositTypeDTO> getDepositList() {
		return depositList;
	}
	public void setDepositList(ArrayList<DepositTypeDTO> depositList) {
		this.depositList = depositList;
	}
	public DemandNoteDTO getdNote() {
		return dNote;
	}
	public void setdNote(DemandNoteDTO dNote) {
		this.dNote = dNote;
	}
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	
		
	
	
}
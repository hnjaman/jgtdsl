package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.CustomerLedgerDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.DepositLedgerDTO;
import org.jgtdsl.dto.DepositTypeDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.models.LedgerService;
import org.jgtdsl.models.MeterRentService;

import com.google.gson.Gson;

public class Deposit extends BaseAction{

	private static final long serialVersionUID = 3888419044139471548L;	
	private DepositDTO deposit;
	private DepositDTO bgChange;
	private String customer_id;
	private String form_mode;//deposit form mode. If mode=edit then it will be in edit mode
	private ArrayList<DepositDTO> depositList=new ArrayList<DepositDTO>();
	private ArrayList<DepositTypeDTO> depositTypeList=new ArrayList<DepositTypeDTO>();
	private String deposit_id;
	private String pId;
	
	public String saveDeposit()
	{
		ResponseDTO response;
		deposit.setInserted_by(((UserDTO)session.get("user")).getUserId());
		DepositService ds=new DepositService();
		response=ds.saveDeposit(customer_id,deposit);
		setJsonResponse(response);
		return null;
	}

	public String getSecurityAndOtherDepositList()
	{
		DepositService ds=new DepositService();
		depositList=ds.getDepositList(customer_id);
		return SUCCESS;
	}
	
	public String getDepositDetail()
	{
		DepositService ds=new DepositService();
		deposit=ds.getDepositDetail(deposit_id);
		return SUCCESS;
	}
	public String getNewDepositForm()
	{
		DepositService ds=new DepositService();
		String whereClause= " Status=1 ";
		depositTypeList=ds.getDepositTypeList(0, 0, whereClause, "VIEW_ORDER", "ASC", 0);		
		return SUCCESS;
	}
	public String getDepositFormEditMode()
	{
		DepositService ds=new DepositService();
		String whereClause= " Status=1 ";
		depositTypeList=ds.getDepositTypeList(0, 0, whereClause, "VIEW_ORDER", "ASC", 0);	
		deposit=ds.getDepositDetail(deposit_id);
		form_mode="edit";
		return SUCCESS;
	}
	
	public String editDeposit()
	{
		ResponseDTO response=new ResponseDTO();
		DepositService ds=new DepositService();
		response=ds.editDeposit(deposit);
		setJsonResponse(response);
		return null;
	}
	public String deleteDeposit()
	{
		ResponseDTO response=new ResponseDTO();
		DepositService ds=new DepositService();
		int deposit_status=ds.getDepositStatus(deposit_id);
		if(deposit_status==1){
			response.setResponse(false);
			response.setMessasge("You can't delete an Authorized Transaction.");
		}
		else{
			response=ds.deleteDepositInfo(deposit_id);
		}
		setJsonResponse(response);
		return null;
	}

	public String getDepositLedger(){
		
		LedgerService customerLedger=new LedgerService();
		ArrayList<DepositLedgerDTO> depositList=customerLedger.getDepositLedger(customer_id);
		
		Gson gson = new Gson();
		String json = gson.toJson(depositList);
		setJsonResponse(json);
        return null;
	}	
	
	public String saveGankGarantieExpireExtentionInfo()
	{
		DepositService mDepositService=new DepositService();
		bgChange.setInserted_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=null;
        response=mDepositService.saveGankGarantieExpireExtentionInfo(bgChange);
			
		setJsonResponse(response);		

		return null;
	}
	
	public String deleteBankGarantieExpireChangeInfo(){
		DepositService mDepositService=new DepositService();
		bgChange.setInserted_by(((UserDTO)session.get("user")).getUserId());
		ResponseDTO response=mDepositService.deleteBankGarantieExpireChangeInfo(pId);
		
		setJsonResponse(response);
		return null;
	}
	
	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String bankGarantieManagementHome()
	{
		return SUCCESS;
	}

	public DepositDTO getBgChange() {
		return bgChange;
	}

	public void setBgChange(DepositDTO bgChange) {
		this.bgChange = bgChange;
	}

	public DepositDTO getDeposit() {
		return deposit;
	}

	public void setDeposit(DepositDTO deposit) {
		this.deposit = deposit;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}

	public String getForm_mode() {
		return form_mode;
	}

	public void setForm_mode(String formMode) {
		form_mode = formMode;
	}

	public ArrayList<DepositDTO> getDepositList() {
		return depositList;
	}

	public void setDepositList(ArrayList<DepositDTO> depositList) {
		this.depositList = depositList;
	}

	public ArrayList<DepositTypeDTO> getDepositTypeList() {
		return depositTypeList;
	}

	public void setDepositTypeList(ArrayList<DepositTypeDTO> depositTypeList) {
		this.depositTypeList = depositTypeList;
	}

	public String getDeposit_id() {
		return deposit_id;
	}

	public void setDeposit_id(String depositId) {
		deposit_id = depositId;
	}
	
	
	
}

package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.BillingNonMeteredDTO;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.MultiCollectionDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.AdjustmentCollectionService;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.TariffService;

import com.google.gson.Gson;

public class AdjustmentCollection extends BaseAction{
	private static final long serialVersionUID = 5045062988808175387L;
	private String customer_id;
	private String bill_month;
	private String bill_year;
	private String collection_date;
	private CollectionDTO collection;
	private boolean mobilePhoneUpdate;
	private  ArrayList<BillingNonMeteredDTO> duesList;
	private MultiCollectionDTO multiColl;		
	
	
	public String getBillInfo4Collection()
	{
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		CollectionDTO collection=collectionService.getBillingInfo(this.customer_id,bill_month,bill_year,collection_date);
		Gson gson = new Gson();
		String json = gson.toJson(collection);
		setJsonResponse(json);
        return null;
        
	}
	public String saveBillCollection(){
		UserDTO loggedInUser=(UserDTO)session.get("user");
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		collection.setInserted_by(loggedInUser.getUserId());
		ResponseDTO response=collectionService.saveBillCollection(collection,mobilePhoneUpdate);
		setJsonResponse(response);
		return null;
	}
	
	public String getCollectionInfo(){		
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		collection=collectionService.getCollectionInfo(collection.getCustomer_id(),collection.getCollection_id());
		Gson gson = new Gson();
		String json = gson.toJson(collection==null?"{}":collection);
		setJsonResponse(json);
        return null;		
	}
	public String getCollectionInfoByCustomerIdBillingMonthYear(){
		
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		collection=collectionService.getCollectionInfo(collection.getCustomer_id(),collection.getBill_month(),collection.getBill_year());
		Gson gson = new Gson();
		String json = gson.toJson(collection==null?"{}":collection);
		setJsonResponse(json);
        return null;	
	}
	public String getTotalCollectionByDateAccount(){		
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		double totalCollection=collectionService.getTotalCollectionByDateAccount(collection.getCollection_date(),collection.getAccount_no());
		String json = "{\"total_collection\":"+totalCollection+"}";
		setJsonResponse(json);
        return null;		
	}
	
	public String deleteBillingCollection()
	{
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		boolean canDelete=collectionService.canDeleteBillCollection(collection.getCustomer_id(), collection.getCollection_id());
		if(canDelete==false){
			ResponseDTO response=new ResponseDTO();
			response.setResponse(false);
			response.setMessasge("Collection Authorized. So, Operation Not Possible.");
			setJsonResponse(response);
			return null;
		}
		ResponseDTO response=collectionService.deleteBillCollection(collection);
		setJsonResponse(response);
		return null;	
	}
	
	public String getTariffForDomesticCustomer(){		
		TariffService ts=new TariffService();
		String Advance_bill_amount=ts.getTariffRateForDomestic(customer_id);
		setJsonResponse(Advance_bill_amount);
        return null;		
	}
	

	public String saveMultiMonthCollection(){		
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		ResponseDTO response=collectionService.saveMultiMonthCollection(multiColl);
		setJsonResponse(response);
        return null;		
	}
	public String saveCurrentMonthBillWithCollection(){		
		AdjustmentCollectionService collectionService=new AdjustmentCollectionService();
		ResponseDTO response=collectionService.saveCurrentMonthBillWithCollection(multiColl);
		setJsonResponse(response);
        return null;		
	}
	
	
	
	public String getCollection_date() {
		return collection_date;
	}
	public void setCollection_date(String collection_date) {
		this.collection_date = collection_date;
	}
	public MultiCollectionDTO getMultiColl() {
		return multiColl;
	}
	public void setMultiColl(MultiCollectionDTO multiColl) {
		this.multiColl = multiColl;
	}
	public ArrayList<BillingNonMeteredDTO> getDuesList() {
		return duesList;
	}
	public void setDuesList(ArrayList<BillingNonMeteredDTO> duesList) {
		this.duesList = duesList;
	}
	public String getBill_month() {
		return bill_month;
	}
	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public CollectionDTO getCollection() {
		return collection;
	}
	public void setCollection(CollectionDTO collection) {
		this.collection = collection;
	}
	public boolean isMobilePhoneUpdate() {
		return mobilePhoneUpdate;
	}
	public void setMobilePhoneUpdate(boolean mobilePhoneUpdate) {
		this.mobilePhoneUpdate = mobilePhoneUpdate;
	}
	
}

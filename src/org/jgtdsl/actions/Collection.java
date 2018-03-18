package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.BillingNonMeteredDTO;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.HolidayDTO;
import org.jgtdsl.dto.MultiCollectionDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.BankBranchService;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.CollectionService;
import org.jgtdsl.models.TariffService;

import com.google.gson.Gson;

public class Collection extends BaseAction{
	private static final long serialVersionUID = 5045062988808175387L;
	private String customer_id;
	private String bill_month;
	private String bill_year;
	private String collection_date;
	private String bank_id;
	private String branch_id;
	private String account_id;
	private CollectionDTO collection;
	private boolean mobilePhoneUpdate;
	private  ArrayList<BillingNonMeteredDTO> duesList;
	private MultiCollectionDTO multiColl;		
	
	private String r_coll_from_month_year;
	private String r_coll_to_month_year;
	private String c_coll_from_month_year;
	private String c_coll_to_month_year;
	private String a_coll_from_month_year;

	private String a_coll_to_month_year;
	private ArrayList<CollectionDTO> dueList;
	private ArrayList<CollectionDTO> custInfo;
	ArrayList<CollectionDTO> collectionList;
	
	
	/* Delete wrong Collection
	 *  @sujon
	 * */
	
	
	public String getListForDeleteCollection(){
		CollectionService collectionService=new CollectionService();
		collectionList = collectionService.getCollectionList4Delete(collection_date,bank_id,branch_id,account_id);
		Gson gson = new Gson();
		String json = gson.toJson(collectionList);
		setJsonResponse(json);
        return "success";
	}
	
	public String getBillInfo4Collection()
	{
		CollectionService collectionService=new CollectionService();
		dueList=collectionService.getBillingInfo(this.customer_id,bill_month,bill_year,collection_date);
		Gson gson = new Gson();
		String json = gson.toJson(dueList);
		setJsonResponse(json);
        return null;
        
	}
	public String getCustInfo4Collection()
	{
		CollectionService collectionService=new CollectionService();
		custInfo=collectionService.getCustomerInfo(this.customer_id);
		Gson gson = new Gson();
		String json = gson.toJson(custInfo);
		setJsonResponse(json);
        return null;
        
	}
	
	public String saveBillCollection(){
		UserDTO loggedInUser=(UserDTO)session.get("user");
		CollectionService collectionService=new CollectionService();
		collection.setInserted_by(loggedInUser.getUserId());
		ResponseDTO response=collectionService.saveBillCollection(collection,mobilePhoneUpdate);
		setJsonResponse(response);
		return null;
	}
	
	public String getCollectionInfo(){		
		CollectionService collectionService=new CollectionService();
		collection=collectionService.getCollectionInfo(collection.getCustomer_id(),collection.getCollection_id());
		Gson gson = new Gson();
		String json = gson.toJson(collection==null?"{}":collection);
		setJsonResponse(json);
        return null;		
	}
	public String getCollectionInfoByCustomerIdBillingMonthYear(){
		
		CollectionService collectionService=new CollectionService();
		collection=collectionService.getCollectionInfo(collection.getCustomer_id(),collection.getBill_month(),collection.getBill_year());
		Gson gson = new Gson();
		String json = gson.toJson(collection==null?"{}":collection);
		setJsonResponse(json);
        return null;	
	}
	public String getTotalCollectionByDateAccount(){		
		CollectionService collectionService=new CollectionService();
		double totalCollection=collectionService.getTotalCollectionByDateAccount(collection.getCollection_date(),collection.getAccount_no());
		String json = "{\"total_collection\":"+totalCollection+"}";
		setJsonResponse(json);
        return null;		
	}
	
	public String deleteBillingCollection()
	{
		CollectionService collectionService=new CollectionService();
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
	
	public String multiMonthCollection(){		
		BillingService billingService=new BillingService();
		duesList=billingService.getNonMeteredCustomerDueBillList(customer_id,collection_date,r_coll_from_month_year,r_coll_to_month_year,c_coll_from_month_year,c_coll_to_month_year,a_coll_from_month_year,a_coll_to_month_year);
		Gson gson = new Gson();
		String json = gson.toJson(duesList);
		setJsonResponse(json);
        return null;
	}
	public String saveMultiMonthCollection(){		
		CollectionService collectionService=new CollectionService();
		ResponseDTO response=collectionService.saveMultiMonthCollection(collection);
		setJsonResponse(response);
        return null;		
	}
	public String saveCurrentMonthBillWithCollection(){		
		CollectionService collectionService=new CollectionService();
		ResponseDTO response=collectionService.saveCurrentMonthBillWithCollection(multiColl);
		setJsonResponse(response);
        return null;		
	}
	
	//for check bill amount	
	public String checkDueAmount(){
		CollectionService collectionService=new CollectionService();
		ResponseDTO response=collectionService.checkAdvancedCollection(collection);
		setJsonResponse(response);
		return null;
	}
	//
	

	public String saveAdvancedCollection(){
		CollectionService collectionService=new CollectionService();
		ResponseDTO response=collectionService.saveAdvancedCollection(collection);
		setJsonResponse(response);
		return null;
	}
	
	
	
	
	public ArrayList<CollectionDTO> getCollectionList() {
		return collectionList;
	}

	public void setCollectionList(ArrayList<CollectionDTO> collectionList) {
		this.collectionList = collectionList;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
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
	public String getR_coll_from_month_year() {
		return r_coll_from_month_year;
	}
	public void setR_coll_from_month_year(String r_coll_from_month_year) {
		this.r_coll_from_month_year = r_coll_from_month_year;
	}
	public String getR_coll_to_month_year() {
		return r_coll_to_month_year;
	}
	public void setR_coll_to_month_year(String r_coll_to_month_year) {
		this.r_coll_to_month_year = r_coll_to_month_year;
	}
	public String getC_coll_from_month_year() {
		return c_coll_from_month_year;
	}
	public void setC_coll_from_month_year(String c_coll_from_month_year) {
		this.c_coll_from_month_year = c_coll_from_month_year;
	}
	public String getC_coll_to_month_year() {
		return c_coll_to_month_year;
	}
	public void setC_coll_to_month_year(String c_coll_to_month_year) {
		this.c_coll_to_month_year = c_coll_to_month_year;
	}
	public String getA_coll_from_month_year() {
		return a_coll_from_month_year;
	}
	public void setA_coll_from_month_year(String a_coll_from_month_year) {
		this.a_coll_from_month_year = a_coll_from_month_year;
	}
	public String getA_coll_to_month_year() {
		return a_coll_to_month_year;
	}
	public void setA_coll_to_month_year(String a_coll_to_month_year) {
		this.a_coll_to_month_year = a_coll_to_month_year;
	}
	public ArrayList<CollectionDTO> getCustInfo() {
		return custInfo;
	}
	public void setCustInfo(ArrayList<CollectionDTO> custInfo) {
		this.custInfo = custInfo;
	}
		
}

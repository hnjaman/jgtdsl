package org.jgtdsl.actions;

import java.util.ArrayList;

import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.models.CollectionService;

public class DeleteCollection extends BaseAction {
	private String collection_date;
	private String bank_id;
	private String branch_id;
	private String account_id;
	
	ArrayList<CollectionDTO> collectionList;

	public String execute(){
		
		CollectionService collectionService = new CollectionService();
		collectionList=collectionService.getCollectionList4Delete(collection_date, bank_id, branch_id, account_id);
		return "success";
		
	}

	public String getCollection_date() {
		return collection_date;
	}

	public void setCollection_date(String collection_date) {
		this.collection_date = collection_date;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
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

	public ArrayList<CollectionDTO> getCollectionList() {
		return collectionList;
	}

	public void setCollectionList(ArrayList<CollectionDTO> collectionList) {
		this.collectionList = collectionList;
	}
	
	
}

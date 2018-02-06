package org.jgtdsl.dto.ipg;

import java.util.List;

import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.CustomerDTO;

import com.google.gson.Gson;

public class DetailInfo {

	private CustomerDTO customerInfo;
	private List<ClearnessDTO> pendingBills;
	private List<CollectionDTO> paidBills;
	private List<PaymentMethod> paymentMethods;
	
	public CustomerDTO getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerDTO customerInfo) {
		this.customerInfo = customerInfo;
	}
	
	
	public List<ClearnessDTO> getPendingBills() {
		return pendingBills;
	}
	public void setPendingBills(List<ClearnessDTO> pendingBills) {
		this.pendingBills = pendingBills;
	}
	public List<CollectionDTO> getPaidBills() {
		return paidBills;
	}
	public void setPaidBills(List<CollectionDTO> paidBills) {
		this.paidBills = paidBills;
	}
	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}
	public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}

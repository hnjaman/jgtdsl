package org.jgtdsl.actions.ipg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.ipg.DetailInfo;
import org.jgtdsl.dto.ipg.IpgResponse;
import org.jgtdsl.dto.ipg.PaymentMethod;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.IpgService;
import org.jgtdsl.reports.DefaulterCertificate;




public class IpgController extends BaseAction {

	private static final long serialVersionUID = 6869465477784884789L;
	
	
	private String customerId;
	private String paymentMethodId;
	private String[] selectedBills;
	private String totalAmount;
	private String internalPaymentApiUrl;
	private String transId;
	
	
	List<ClearnessDTO> selectedBillList;
	private PaymentMethod selectedPaymentMethod;
	
	
	// from ipgResponse DTO
					private String txnStatus;
				    private String transID;
				    private String ipgTrxID;
				    private String error_msg;
				    private String card_no;
				    private String card_name;
				    
    
    IpgResponse ipgResponse = new IpgResponse();
    
        

	public String getiPgDetailInfo()		// jsonP response , for apps only
	{		
	
		DetailInfo detailInfo= new DetailInfo();
		
		CustomerService customerService=new CustomerService();
		DefaulterCertificate defaulterCertificate=new DefaulterCertificate();
		
		CustomerDTO customer=customerService.getCustomerInfo(customerId);
		detailInfo.setCustomerInfo(customer);
		
		ArrayList<ClearnessDTO> pendingBill= new ArrayList<ClearnessDTO>();
		pendingBill=defaulterCertificate.getDueMonthWeb(customerId,"");
		
		
		BillingService billingService=new BillingService();
		
		ArrayList<CollectionDTO> paidBill=new ArrayList<CollectionDTO>();
		paidBill=billingService.getPaidBillInfo(customerId);
		detailInfo.setPaidBills(paidBill);
		detailInfo.setPendingBills(pendingBill);
		
		
		
		detailInfo.setPaymentMethods(IpgService.getPaymentMethods());
		
		//setJsonResponse(detailInfo.toString());	
		setJsonResponse("jsonPresponse({\"response\":"+detailInfo.toString()+"})");

		return null;
		
	}
	
	
	
	public String getIpgConfirmationPage() {
		DefaulterCertificate defaulterCertificate=new DefaulterCertificate();
		
		String st ="";
		for(int i=0; i<selectedBills.length; i++ )
		{
			st+="'"+selectedBills[i]+"',";
		}
		st = st.substring(0, st.length() - 1);
		
		selectedBillList =  defaulterCertificate.getDueMonthWeb(customerId,st);
		double total=0d;
		for(ClearnessDTO bill :selectedBillList){
			total += bill.getDueAmount()+bill.getDueSurcharge();
		}
		
		totalAmount = String.valueOf(total);
		
		selectedPaymentMethod =  IpgService.getPaymentMethod(paymentMethodId);		
		return SUCCESS;
	}
	
	
	
	
	public String payIpgBill() {
		
		IpgService ipgService = new IpgService(); 
		DefaulterCertificate defaulterCertificate=new DefaulterCertificate();
		String transId = UUID.randomUUID().toString();
		transId = transId.replaceAll("-", "");
		String st =Arrays.toString(selectedBills);
		String stp="";
		String stm="";
		int f=1,l=16;
		
		st=st.replaceAll(",", "");
		st=st.replaceAll(" ", "");
		int billamount=st.length()/15;
		for(int i=0; i<billamount; i++ )
		{
			
			stp=st.substring(f, l);
			stm+="'"+stp+"',";
			f=l;
			l=l+15;
		}
		stm = stm.substring(0, stm.length() - 1);
		
		selectedBillList =  defaulterCertificate.getDueMonthWeb(customerId,stm);	// exception
		ResponseDTO responseDTO = ipgService.initiateTransaction(transId, customerId,paymentMethodId, Double.valueOf(totalAmount), selectedBillList);
		internalPaymentApiUrl = "http://103.48.16.144:8080/IPG/initPayment?amount="+totalAmount+"&gateway="+paymentMethodId+"&transId="+transId;
		return "success";			 //203.112.220.226
	}
	
	
	
	
	public String receiveIpgResponse(){
			
		ipgResponse.setTxnStatus(txnStatus);
		ipgResponse.setTransId(transID);
		ipgResponse.setIpgTrxId(ipgTrxID);
		ipgResponse.setError_msg(error_msg);
		ipgResponse.setCard_no(card_no);
		ipgResponse.setCardName(card_name);
		
		
		
		IpgService ipgService = new IpgService();
		ipgService.saveResponse(ipgResponse);
		
		return SUCCESS;
				
	}

	
	   
	

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String[] getSelectedBills() {
		return selectedBills;
	}



	public void setSelectedBills(String[] selectedBills) {
		this.selectedBills = selectedBills;
	}



	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInternalPaymentApiUrl() {
		return internalPaymentApiUrl;
	}

	public void setInternalPaymentApiUrl(String internalPaymentApiUrl) {
		this.internalPaymentApiUrl = internalPaymentApiUrl;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public PaymentMethod getSelectedPaymentMethod() {
		return selectedPaymentMethod;
	}

	public void setSelectedPaymentMethod(PaymentMethod selectedPaymentMethod) {
		this.selectedPaymentMethod = selectedPaymentMethod;
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getTransID() {
		return transID;
	}

	public void setTransID(String transID) {
		this.transID = transID;
	}

	public String getIpgTrxID() {
		return ipgTrxID;
	}

	public void setIpgTrxID(String ipgTrxID) {
		this.ipgTrxID = ipgTrxID;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public IpgResponse getIpgResponse() {
		return ipgResponse;
	}

	public void setIpgResponse(IpgResponse ipgResponse) {
		this.ipgResponse = ipgResponse;
	}

	public List<ClearnessDTO> getSelectedBillList() {
		return selectedBillList;
	}

	public void setSelectedBillList(List<ClearnessDTO> selectedBillList) {
		this.selectedBillList = selectedBillList;
	}



	public String getTransId() {
		return transId;
	}



	public void setTransId(String transId) {
		this.transId = transId;
	}

	
	
	
}

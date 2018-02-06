package org.jgtdsl.dto.ipg;

public class IpgResponse {

	private String txnStatus;
	private String transId;
	private String ipgTrxId;
	private String error_msg;
	private String card_no;
	private String cardName;
	
	public String getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getIpgTrxId() {
		return ipgTrxId;
	}
	public void setIpgTrxId(String ipgTrxId) {
		this.ipgTrxId = ipgTrxId;
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
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	
}

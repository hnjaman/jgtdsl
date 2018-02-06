package org.jgtdsl.dto;

import com.google.gson.Gson;

public class DemandNoteDTO {
	private String customer_id;
	private String bank_id;
	private String bank_name;
	private String branch_id;
	private String branch_name;
	private String account_no;
	private String account_name;
	private String sd_ka_1;
	private String sd_ka_amount;
	private String sd_kha_1;
	private String sd_kha_2;
	private String sd_kha_amount;
	private String sd_ga_1;
	private String sd_ga_2;
	private String sd_ga_amount;
	private String bsd_ka_amount;
	private String bsd_kha_amount;
	private String cl_ka_amount;
	private String cl_kha_1; 
	private String cl_kha_amount;
	private String cl_ga_1;
	private String cl_ga_2;
	private String cl_ga_3;
	private String cl_ga_amount;
	private String cl_gha_1;
	private String cl_gha_2;
	private String cl_gha_amount;
	private String cl_uma_l1;
	private String cl_uma_l1_amount;
	private String cl_uma_l2;
	private String cl_uma_l2_amount;
	private String of_ka_amount;
	private String of_kha_amount;
	private String of_ga_amount;
	private String of_gha_amount;
	private String of_uma_amount;
	private String of_ch_amount;
	private String of_cho_1;
	private String of_cho_2;
	private String of_cho_amount;
	private String of_jo_amount;
	private String others_ka_amount;
	private String others_kha_amount;
	private String others_ga_amount;
	private String others_gha_amount;
	private String others_uma_amount;
	private float total_in_amount;
	private String total_in_word;
	private String demand_note_id;
	private String issue_date;
	
	
	

	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
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
	public String getBank_id() {
		return bank_id;
	}
	public void setBank_id(String bankId) {
		bank_id = bankId;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bankName) {
		bank_name = bankName;
	}
	public String getBranch_id() {
		return branch_id;
	}
	public void setBranch_id(String branchId) {
		branch_id = branchId;
	}
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branchName) {
		branch_name = branchName;
	}
	
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String accountNo) {
		account_no = accountNo;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String accountName) {
		account_name = accountName;
	}
	public String getSd_ka_1() {
		return sd_ka_1;
	}
	public void setSd_ka_1(String sdKa_1) {
		sd_ka_1 = sdKa_1;
	}
	public String getSd_ka_amount() {
		return sd_ka_amount;
	}
	public void setSd_ka_amount(String sdKaAmount) {
		sd_ka_amount = sdKaAmount;
	}
	public String getSd_kha_1() {
		return sd_kha_1;
	}
	public void setSd_kha_1(String sdKha_1) {
		sd_kha_1 = sdKha_1;
	}
	public String getSd_kha_2() {
		return sd_kha_2;
	}
	public void setSd_kha_2(String sdKha_2) {
		sd_kha_2 = sdKha_2;
	}
	public String getSd_kha_amount() {
		return sd_kha_amount;
	}
	public void setSd_kha_amount(String sdKhaAmount) {
		sd_kha_amount = sdKhaAmount;
	}
	public String getSd_ga_1() {
		return sd_ga_1;
	}
	public void setSd_ga_1(String sdGa_1) {
		sd_ga_1 = sdGa_1;
	}
	public String getSd_ga_2() {
		return sd_ga_2;
	}
	public void setSd_ga_2(String sdGa_2) {
		sd_ga_2 = sdGa_2;
	}
	public String getSd_ga_amount() {
		return sd_ga_amount;
	}
	public void setSd_ga_amount(String sdGaAmount) {
		sd_ga_amount = sdGaAmount;
	}
	public String getBsd_ka_amount() {
		return bsd_ka_amount;
	}
	public void setBsd_ka_amount(String bsdKaAmount) {
		bsd_ka_amount = bsdKaAmount;
	}
	public String getBsd_kha_amount() {
		return bsd_kha_amount;
	}
	public void setBsd_kha_amount(String bsdKhaAmount) {
		bsd_kha_amount = bsdKhaAmount;
	}
	public String getCl_ka_amount() {
		return cl_ka_amount;
	}
	public void setCl_ka_amount(String clKaAmount) {
		cl_ka_amount = clKaAmount;
	}
	public String getCl_kha_1() {
		return cl_kha_1;
	}
	public void setCl_kha_1(String clKha_1) {
		cl_kha_1 = clKha_1;
	}
	public String getCl_kha_amount() {
		return cl_kha_amount;
	}
	public void setCl_kha_amount(String clKhaAmount) {
		cl_kha_amount = clKhaAmount;
	}
	public String getCl_ga_1() {
		return cl_ga_1;
	}
	public void setCl_ga_1(String clGa_1) {
		cl_ga_1 = clGa_1;
	}
	public String getCl_ga_2() {
		return cl_ga_2;
	}
	public void setCl_ga_2(String clGa_2) {
		cl_ga_2 = clGa_2;
	}
	public String getCl_ga_3() {
		return cl_ga_3;
	}
	public void setCl_ga_3(String clGa_3) {
		cl_ga_3 = clGa_3;
	}
	public String getCl_ga_amount() {
		return cl_ga_amount;
	}
	public void setCl_ga_amount(String clGaAmount) {
		cl_ga_amount = clGaAmount;
	}
	public String getCl_gha_1() {
		return cl_gha_1;
	}
	public void setCl_gha_1(String clGha_1) {
		cl_gha_1 = clGha_1;
	}
	public String getCl_gha_2() {
		return cl_gha_2;
	}
	public void setCl_gha_2(String clGha_2) {
		cl_gha_2 = clGha_2;
	}
	public String getCl_gha_amount() {
		return cl_gha_amount;
	}
	public void setCl_gha_amount(String clGhaAmount) {
		cl_gha_amount = clGhaAmount;
	}
	public String getCl_uma_l1() {
		return cl_uma_l1;
	}
	public void setCl_uma_l1(String clUmaL1) {
		cl_uma_l1 = clUmaL1;
	}
	public String getCl_uma_l1_amount() {
		return cl_uma_l1_amount;
	}
	public void setCl_uma_l1_amount(String clUmaL1Amount) {
		cl_uma_l1_amount = clUmaL1Amount;
	}
	public String getCl_uma_l2() {
		return cl_uma_l2;
	}
	public void setCl_uma_l2(String clUmaL2) {
		cl_uma_l2 = clUmaL2;
	}
	public String getCl_uma_l2_amount() {
		return cl_uma_l2_amount;
	}
	public void setCl_uma_l2_amount(String clUmaL2Amount) {
		cl_uma_l2_amount = clUmaL2Amount;
	}
	public String getOf_ka_amount() {
		return of_ka_amount;
	}
	public void setOf_ka_amount(String ofKaAmount) {
		of_ka_amount = ofKaAmount;
	}
	public String getOf_kha_amount() {
		return of_kha_amount;
	}
	public void setOf_kha_amount(String ofKhaAmount) {
		of_kha_amount = ofKhaAmount;
	}
	public String getOf_ga_amount() {
		return of_ga_amount;
	}
	public void setOf_ga_amount(String ofGaAmount) {
		of_ga_amount = ofGaAmount;
	}
	public String getOf_gha_amount() {
		return of_gha_amount;
	}
	public void setOf_gha_amount(String ofGhaAmount) {
		of_gha_amount = ofGhaAmount;
	}
	public String getOf_uma_amount() {
		return of_uma_amount;
	}
	public void setOf_uma_amount(String ofUmaAmount) {
		of_uma_amount = ofUmaAmount;
	}
	public String getOf_ch_amount() {
		return of_ch_amount;
	}
	public void setOf_ch_amount(String ofChAmount) {
		of_ch_amount = ofChAmount;
	}
	public String getOf_cho_1() {
		return of_cho_1;
	}
	public void setOf_cho_1(String ofCho_1) {
		of_cho_1 = ofCho_1;
	}
	public String getOf_cho_2() {
		return of_cho_2;
	}
	public void setOf_cho_2(String ofCho_2) {
		of_cho_2 = ofCho_2;
	}
	public String getOf_cho_amount() {
		return of_cho_amount;
	}
	public void setOf_cho_amount(String ofChoAmount) {
		of_cho_amount = ofChoAmount;
	}
	public String getOf_jo_amount() {
		return of_jo_amount;
	}
	public void setOf_jo_amount(String ofJoAmount) {
		of_jo_amount = ofJoAmount;
	}
	public String getOthers_ka_amount() {
		return others_ka_amount;
	}
	public void setOthers_ka_amount(String othersKaAmount) {
		others_ka_amount = othersKaAmount;
	}
	public String getOthers_kha_amount() {
		return others_kha_amount;
	}
	public void setOthers_kha_amount(String othersKhaAmount) {
		others_kha_amount = othersKhaAmount;
	}
	public String getOthers_ga_amount() {
		return others_ga_amount;
	}
	public void setOthers_ga_amount(String othersGaAmount) {
		others_ga_amount = othersGaAmount;
	}
	public String getOthers_gha_amount() {
		return others_gha_amount;
	}
	public void setOthers_gha_amount(String othersGhaAmount) {
		others_gha_amount = othersGhaAmount;
	}
	public String getOthers_uma_amount() {
		return others_uma_amount;
	}
	public void setOthers_uma_amount(String othersUmaAmount) {
		others_uma_amount = othersUmaAmount;
	}
	public float getTotal_in_amount() {
		return total_in_amount;
	}
	public void setTotal_in_amount(float totalInAmount) {
		total_in_amount = totalInAmount;
	}
	public String getTotal_in_word() {
		return total_in_word;
	}
	public void setTotal_in_word(String totalInWord) {
		total_in_word = totalInWord;
	}
	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}

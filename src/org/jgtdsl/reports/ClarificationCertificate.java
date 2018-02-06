package org.jgtdsl.reports;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.DuesSurchargeDTO;
import org.jgtdsl.dto.OthersDto;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.ClarificationCertificateService;
import org.jgtdsl.models.ReconciliationService;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.TransactionManager;

import com.google.gson.Gson;

public class ClarificationCertificate extends BaseAction {

	private static final long serialVersionUID = 4216989970426874378L;
	private String customer_id;
	private String issue_date;
	private String area;
	private String category;
	private String nondistributed;
	private String approve_type;


	
	public String approveCC(){
		
		ResponseDTO response=ClarificationCertificateService.approveCC(customer_id,issue_date,area,category,nondistributed,approve_type);
		setJsonResponse(response);
		return null;
	}


	

	public String getApprove_type() {
		return approve_type;
	}




	public void setApprove_type(String approve_type) {
		this.approve_type = approve_type;
	}




	public String getCustomer_id() {
		return customer_id;
	}



	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}



	public String getIssue_date() {
		return issue_date;
	}



	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}



	public String getArea() {
		return area;
	}



	public void setArea(String area) {
		this.area = area;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public String getNondistributed() {
		return nondistributed;
	}



	public void setNondistributed(String nondistributed) {
		this.nondistributed = nondistributed;
	}




	

	
}

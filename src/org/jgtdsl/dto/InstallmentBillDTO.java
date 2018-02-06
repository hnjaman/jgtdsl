package org.jgtdsl.dto;

import java.util.ArrayList;

public class InstallmentBillDTO extends MBillDTO{

	private String installmentId;
	private String installmentSerial;
	private String dueDate;
	
	
	private ArrayList<InstallmentSegmentDTO> segmentList;

	public ArrayList<InstallmentSegmentDTO> getSegmentList() {
		return segmentList;
	}

	public void setSegmentList(ArrayList<InstallmentSegmentDTO> segmentList) {
		this.segmentList = segmentList;
	}

	public String getInstallmentId() {
		return installmentId;
	}

	public void setInstallmentId(String installmentId) {
		this.installmentId = installmentId;
	}

	public String getInstallmentSerial() {
		return installmentSerial;
	}

	public void setInstallmentSerial(String installmentSerial) {
		this.installmentSerial = installmentSerial;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	
}

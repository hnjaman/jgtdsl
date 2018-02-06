package org.jgtdsl.actions;



public class ReportHome extends BaseAction{
	private static final long serialVersionUID = -758403638439017016L;
	private String reportType;
	
	public String execute(){
		
		return SUCCESS;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}


}

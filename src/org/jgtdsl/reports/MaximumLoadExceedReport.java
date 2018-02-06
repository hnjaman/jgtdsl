package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.LoadExceedReportDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class MaximumLoadExceedReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<LoadExceedReportDTO> loadExceedCustomerList=new ArrayList<LoadExceedReportDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String category_name;
	    private  String report_for2;
	    private  String from_date; 
	    private  String to_date; 
	    private  String customer_type;
	    private  String criteria_type;
	    private  String percentage_range;

	public String execute() throws Exception
	{
		
		
		
		
		String fileName="LoadExceedCustomerList.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(5,5,5,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			LoadExceedReportDTO loadExceedDto = new LoadExceedReportDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
			headerTable.setWidthPercentage(100);
		   
				
			headerTable.setWidths(new float[] {
				5,190,5
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			pcell.setBorderWidthBottom(1);
			headerTable.addCell(pcell);
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS TRANSMISSION AND DISTRIBUTION SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A company of PetroBangla)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("Regional Office :",ReportUtil.f8B);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(area)-1]),ReportUtil.f8B);
			Paragraph p = new Paragraph(); 
			p.add(chunk1);
			p.add(chunk2);
			pcell=new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			pcell=new PdfPCell(mTable);
			pcell.setBorder(0);
			pcell.setBorderWidthBottom(1);
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			pcell.setBorderWidthBottom(1);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			
			
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{30,50,30});
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("LIST OF CUSTOMER EXCEED MAXIMUM LOAD",ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);
			headLinetable.addCell(pcell);
			
			document.add(headLinetable);
			
			loadExceedCustomerList =getLoadExceedCustomerList();
			
			int totalRecordsPerCategory=0;
			int total_burner=0;

			int expireListSize=loadExceedCustomerList.size();
			String previousCustomerCategoryName=new String("");
			
			for(int i=0;i<expireListSize;i++)
			{
				loadExceedDto = loadExceedCustomerList.get(i);
				String currentCustomerCategoryName=loadExceedDto.getCustomer_category_name();
				
				if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
				{	
				
				if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
				{
					
					if(i>0)
					{
						pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(2);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
													
						
						
						pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(5);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						document.add(ptable);
					
						totalRecordsPerCategory=0;
						total_burner=0;
					}
					
				}
				ptable = new PdfPTable(7);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,30,50,30,30,30,30});
				ptable.setSpacingBefore(10);
				
				
				pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(5);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
								
				
				pcell=new PdfPCell(new Paragraph("Sr.No",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				
				pcell=new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				
				
				pcell=new PdfPCell(new Paragraph("Monthly Gas Consumption(SCM)",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Monthly Contructual Load(SCM)",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Difference",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Gas use more Than "+percentage_range+"% from MCL ",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
				}
				
				
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory+1),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(loadExceedDto.getCustomer_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(loadExceedDto.getFull_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
				pcell = new PdfPCell(new Paragraph(consumption_format.format(loadExceedDto.getActual_consumption()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(consumption_format.format(loadExceedDto.getPmax_laod()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(consumption_format.format(loadExceedDto.getDifference()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(consumption_format.format(loadExceedDto.getPercent_usage()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
			
				previousCustomerCategoryName=loadExceedDto.getCustomer_category_name();
				totalRecordsPerCategory++;
				
				
			}
			/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
			pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
										
			
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			document.add(ptable);
		
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	
	private ArrayList<LoadExceedReportDTO> getLoadExceedCustomerList()
	{
	ArrayList<LoadExceedReportDTO> loadExceedCustomerList=new ArrayList<LoadExceedReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";
			String wCriteriaClause="";
			if(report_for.equals("area_wise"))
			{
				wClause="Where substr(customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="Where substr(customer_id,1,2)="+area+" And substr(customer_id,3,2)="+customer_category;
			}
			
			if(report_for2.equals("date_wise"))
			{
				w2Clause=" And BILL_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				w2Clause=" And BILLING_MONTH="+bill_month+" and BILLING_YEAR="+bill_year;
			}else if(report_for2.equals("year_wise"))
			{
				w2Clause=" And BILLING_YEAR="+bill_year;
			}
			
			if(criteria_type.equals("lt"))
			{
				wCriteriaClause=" And  ((ACTUAL_CONSUMPTION - PMAX_LOAD)*100)/DECODE (PMAX_LOAD, 0, 1, PMAX_LOAD)<"+percentage_range;
			}else if(criteria_type.equals("gt"))
			{
				wCriteriaClause=" And  ((ACTUAL_CONSUMPTION - PMAX_LOAD)*100)/DECODE (PMAX_LOAD, 0, 1, PMAX_LOAD)>"+percentage_range;
			}else if(criteria_type.equals("eq"))
			{
				wCriteriaClause=" And  ((ACTUAL_CONSUMPTION - PMAX_LOAD)*100)/DECODE (PMAX_LOAD, 0, 1, PMAX_LOAD)="+percentage_range;
			}else if(criteria_type.equals("gteq"))
			{
				wCriteriaClause=" And  ((ACTUAL_CONSUMPTION - PMAX_LOAD)*100)/DECODE (PMAX_LOAD, 0, 1, PMAX_LOAD)>="+percentage_range;
			}else if(criteria_type.equals("lteq"))
			{
				wCriteriaClause=" And  ((ACTUAL_CONSUMPTION - PMAX_LOAD)*100)/DECODE (PMAX_LOAD, 0, 1, PMAX_LOAD)<="+percentage_range;
			}
			
			
			String defaulterSql="SELECT tbl.CUSTOMER_ID,mci.FULL_NAME ," +
					"       mci.CATEGORY_ID, " +
					"       mci.CATEGORY_NAME, " +
					"       mci.AREA_ID, " +
					"       BILLING_MONTH, " +
					"       BILLING_YEAR, " +
					"       BILL_DATE, " +
					"       MAX_LOAD,PMAX_LOAD, " +
					"       ACTUAL_CONSUMPTION, " +
					"       ACTUAL_CONSUMPTION - PMAX_LOAD DIFFERENCE, " +
					"       ((ACTUAL_CONSUMPTION - PMAX_LOAD)*100)/DECODE (PMAX_LOAD, 0, 1, PMAX_LOAD) PERCENT_USAGE " +
					"  FROM (  SELECT CUSTOMER_ID, " +
					"                 BILLING_MONTH, " +
					"                 BILLING_YEAR, TO_DATE (TO_CHAR ('01-' || BILLING_MONTH || '-' || BILLING_YEAR),'dd-MM-YYYY') BILL_DATE," +
					"                 SUM (PMAX_LOAD) PMAX_LOAD, " +
					"                 SUM (ACTUAL_CONSUMPTION) ACTUAL_CONSUMPTION " +
					"            FROM meter_reading " +wClause+
					"        GROUP BY CUSTOMER_ID, BILLING_MONTH, BILLING_YEAR) tbl,MVIEW_CUSTOMER_INFO mci " +
					" WHERE tbl.customer_id=mci.customer_id "+w2Clause+wCriteriaClause+
					" order by mci.CATEGORY_ID, tbl.CUSTOMER_ID " ;


			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		LoadExceedReportDTO loadExceedDto=new LoadExceedReportDTO();
        		loadExceedDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		loadExceedDto.setFull_name(resultSet.getString("FULL_NAME"));
        		loadExceedDto.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		loadExceedDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		loadExceedDto.setMax_load(resultSet.getFloat("MAX_LOAD"));
        		loadExceedDto.setPmax_laod(resultSet.getFloat("PMAX_LOAD"));
        		loadExceedDto.setActual_consumption(resultSet.getFloat("ACTUAL_CONSUMPTION"));
        		loadExceedDto.setDifference(resultSet.getFloat("DIFFERENCE"));
        		loadExceedDto.setPercent_usage(resultSet.getFloat("PERCENT_USAGE"));
        		
       
        		
        		
   
        		
        		loadExceedCustomerList.add(loadExceedDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loadExceedCustomerList;
	}
	
	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList() {
		return customerCategoryList;
	}



	
	public void setCustomerCategoryList(ArrayList<CustomerCategoryDTO> customerCategoryList) {
		this.customerCategoryList = customerCategoryList;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getCustomer_category() {
		return customer_category;
	}


	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
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


	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	public String getCategory_name() {
		return category_name;
	}


	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}


	public String getReport_for2() {
		return report_for2;
	}


	public void setReport_for2(String report_for2) {
		this.report_for2 = report_for2;
	}


	public String getFrom_date() {
		return from_date;
	}


	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}


	public String getTo_date() {
		return to_date;
	}


	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}


	public String getCustomer_type() {
		return customer_type;
	}


	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}


	public String getCriteria_type() {
		return criteria_type;
	}


	public void setCriteria_type(String criteria_type) {
		this.criteria_type = criteria_type;
	}


	public String getPercentage_range() {
		return percentage_range;
	}


	public void setPercentage_range(String percentage_range) {
		this.percentage_range = percentage_range;
	}
	
	


	
  }



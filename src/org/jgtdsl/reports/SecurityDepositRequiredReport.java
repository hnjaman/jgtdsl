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
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.SecurityRequireReportDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class SecurityDepositRequiredReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<SecurityRequireReportDTO> securityRequiredList=new ArrayList<SecurityRequireReportDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String category_name;
	    private String date;

	public String execute() throws Exception
	{
		
		DepositService depositeService = new  DepositService();
		
		
		
		
		String fileName="SecurityDepositRequiredList.pdf";
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
			
			SecurityRequireReportDTO securityRequiredDto = new SecurityRequireReportDTO();
			
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
			headLinetable.setWidths(new float[]{30,80,30});
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("List of Metered Customers to whom Security Deposit Required",ReportUtil.f11B));
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
			
			securityRequiredList =getSecurityRequiredInfoList();
			
			int totalRecordsPerCategory=0;
			int total_burner=0;

			int expireListSize=securityRequiredList.size();
			String previousCustomerCategoryName=new String("");
			
			for(int i=0;i<expireListSize;i++)
			{
				securityRequiredDto = securityRequiredList.get(i);
				String currentCustomerCategoryName=securityRequiredDto.getCategory_name();
				
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
						pcell.setColspan(7);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						document.add(ptable);
					
						totalRecordsPerCategory=0;
						total_burner=0;
					}
					
				}
				ptable = new PdfPTable(9);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,100,25,25,20,25,30,30,30});
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
				pcell.setColspan(7);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
								
				
				pcell=new PdfPCell(new Paragraph("Sr.No",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Monthly Contructual Load(SCM)",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("SD Equ. Month",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Rate",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Required Security deposit",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Security Deposit paid by Customer",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Security Deposit Recevale/Excess Paid",ReportUtil.f9B));
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
				
				
				pcell = new PdfPCell(new Paragraph(securityRequiredDto.getName_address(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(securityRequiredDto.getCustomer_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(securityRequiredDto.getMax_load()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(securityRequiredDto.getSd_month(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(securityRequiredDto.getRate()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(securityRequiredDto.getRequired_sequrity_deposit()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(securityRequiredDto.getPaid_sequrity_deposit()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(securityRequiredDto.getRecv_exceed_deposit()),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				
			
				previousCustomerCategoryName=securityRequiredDto.getCategory_name();
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
			pcell.setColspan(7);
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
	
	
	private ArrayList<SecurityRequireReportDTO> getSecurityRequiredInfoList()
	{
	ArrayList<SecurityRequireReportDTO> securityRequireList=new ArrayList<SecurityRequireReportDTO>();
		
		try {
			String wClause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And area_id="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And area_id="+area+" And category_id="+customer_category;
			}
			
			
			String defaulterSql=
					"SELECT tbl.CUSTOMER_ID, " +
					"       FULL_NAME || CHR (10) || ADDRESS_LINE1 NAME_ADDRESS, " +
					"       mci.CATEGORY_ID, " +
					"       mci.CATEGORY_NAME, " +
					"       mci.AREA_ID, " +
					"       tbl.MAX_LOAD, " +
					"       tbl.SD_MONTH, " +
					"       tbl.RATE, " +
					"       ROUND((tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE),0) Required_deposit, " +
					"       SECURITY_AMOUNT , " +
					"       ROUND((tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE),0) -SECURITY_AMOUNT recv_exceed " +
					"  FROM (SELECT cc.CUSTOMER_ID, " +
					"               MAX_LOAD, " +
					"               SD_MONTH, " +
					"               (SELECT Price " +
					"                  FROM CUSTOMER, MST_TARIFF " +
					"                 WHERE     Customer.CUSTOMER_CATEGORY = " +
					"                              Mst_Tariff.CUSTOMER_CATEGORY_ID " +
					"                       AND Customer_Id = cc.CUSTOMER_ID " +
					"                       AND Meter_Status = 1 " +
					"                       AND Effective_From <= " +
					"                              TO_DATE ('"+date+"', 'dd-MM-YYYY HH24:MI:SS') " +
					"                       AND (   Effective_To IS NULL " +
					"                            OR Effective_To >= " +
					"                                  TO_DATE ('"+date+"', " +
					"                                           'dd-MM-YYYY HH24:MI:SS'))) " +
					"                  RATE " +
					"          FROM customer_connection cc " +
					"         WHERE cc.ISMETERED = 01) tbl, " +
					"       (  SELECT CUSTOMER_ID, SUM (SECURITY_AMOUNT) SECURITY_AMOUNT " +
					"            FROM CUSTOMER_SECURITY_LEDGER " +
					"        GROUP BY CUSTOMER_ID) csl, " +
					"       MVIEW_CUSTOMER_INFO mci " +
					" WHERE     tbl.customer_id = mci.customer_id " +
					"       AND tbl.customer_id = csl.customer_id " +
					" AND (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) > SECURITY_AMOUNT " +wClause+
					"   order by CATEGORY_ID,CUSTOMER_ID " ;




			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		SecurityRequireReportDTO securityRequiredDto=new SecurityRequireReportDTO();
        		securityRequiredDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		securityRequiredDto.setCategory_id(resultSet.getString("CATEGORY_ID"));
        		securityRequiredDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
        		securityRequiredDto.setName_address(resultSet.getString("NAME_ADDRESS"));
        		securityRequiredDto.setSd_month(resultSet.getString("SD_MONTH"));
        		securityRequiredDto.setRate(resultSet.getFloat("RATE"));
        		securityRequiredDto.setMax_load(resultSet.getFloat("MAX_LOAD"));
        		securityRequiredDto.setRequired_sequrity_deposit(resultSet.getFloat("Required_deposit"));
        		securityRequiredDto.setPaid_sequrity_deposit(resultSet.getFloat("SECURITY_AMOUNT"));
        		securityRequiredDto.setRecv_exceed_deposit(resultSet.getFloat("recv_exceed"));
        	
        		
   
        		
        		securityRequireList.add(securityRequiredDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return securityRequireList;
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


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}
	
	


	
  }



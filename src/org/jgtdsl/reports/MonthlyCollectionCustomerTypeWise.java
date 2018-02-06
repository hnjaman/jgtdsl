
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
import org.jgtdsl.dto.MonthlyCollectionCustomerTypeWiseDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.CategoryWiseGasConsumptionDTO;
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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class MonthlyCollectionCustomerTypeWise extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	
	ArrayList<MonthlyCollectionCustomerTypeWiseDTO> sujonSokhi=new ArrayList<MonthlyCollectionCustomerTypeWiseDTO>();
	
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private String customer_category;
	    private String category_id;
	    
	    DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");

	public String execute() throws Exception
	{
		
		DepositService depositeService = new  DepositService();
		
		String fileName="MonthlyCollectionCustomerTypeWise.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(5,5,5,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			NonMeterReportDTO loadIncraseDTO = new NonMeterReportDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
			headerTable.setWidthPercentage(100);
		   
				
			headerTable.setWidths(new float[] {
				50,100,50
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			pcell.setBorderWidthBottom(1);
			headerTable.addCell(pcell);
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A company of PetroBangla)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			pcell=new PdfPCell(new Paragraph("Monthly Collection Customer Type Wise", ReportUtil.f8B));
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
			
	/*--------------------------------------------------------------------------------------------------------------------*/		
			PdfPTable jvTable = new PdfPTable(5);
			jvTable.setWidthPercentage(100);
			jvTable.setWidths(new float[]{20,20,20,20,20});
			jvTable.setSpacingBefore(15f);
			
			pcell = new PdfPCell(new Paragraph("Customer Type",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Monthly Gas Bill",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Fees",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("S.D",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			sujonSokhi=getMonthlyCollectionCustomerTypeWise();
			int listSize=sujonSokhi.size();
			
			for (int i = 0; i < listSize; i++) {
				
				pcell = new PdfPCell(new Paragraph(sujonSokhi.get(i).getCustomerType(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sujonSokhi.get(i).getMonthly_Gas_Bill()),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sujonSokhi.get(i).getSurcharge()),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sujonSokhi.get(i).getFee()),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sujonSokhi.get(i).getSucurity()),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				
				
			}
			
			document.add(jvTable);
			
			
			
	/*--------------------------------------------------------------------------------------------------------------------*/			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	private ArrayList<MonthlyCollectionCustomerTypeWiseDTO> getMonthlyCollectionCustomerTypeWise()
	{
	ArrayList<MonthlyCollectionCustomerTypeWiseDTO> categoryWiseGasConsumption=new ArrayList<MonthlyCollectionCustomerTypeWiseDTO>();
		
		try {

			String defaulterSql="SELECT aa.*, CATEGORY_NAME " +
					"  FROM (  SELECT SUBSTR (CUSTOMER_ID, 3, 2) Customer_Category, " +
					"                 SUM (NVL (Gas_bill, 0)) Gas_bill, " +
					"                 SUM (NVL (surcharge, 0)) surcharge, " +
					"                 SUM (NVL (fees, 0)) fees, " +
					"                 SUM (NVL (Security_Dep, 0)) Security_Dep " +
					"            FROM (SELECT BAL.CUSTOMER_ID, " +
					"                         ORGANIZATION_NAME, " +
					"                         SUBSTR (PARTICULARS, 8, 9) PARTICULARS, " +
					"                         TO_CHAR (TRANS_DATE, 'MM/DD/YYYY') TRANS_DATE, " +
					"                         bal.BANK_ID, " +
					"                         bal.BRANCH_ID, " +
					"                         NVL (DEBIT, 0) - NVL (surcharge, 0) Gas_bill, " +
					"                         surcharge, " +
					"                         NULL fees, " +
					"                         NULL Security_Dep " +
					"                    FROM BANK_ACCOUNT_LEDGER BAL, " +
					"                         MVIEW_CUSTOMER_INFO MCI, " +
					"                         MST_DEPOSIT MD " +
					"                   WHERE     BAL.CUSTOMER_ID = MCI.CUSTOMER_ID " +
					"                         AND MCI.CUSTOMER_ID = MD.CUSTOMER_ID " +
					"                         AND trans_type = 1 " +
					"                  UNION ALL " +
					"                  SELECT BAL.CUSTOMER_ID, " +
					"                         ORGANIZATION_NAME, " +
					"                         'Security Deposit' PARTICULARS, " +
					"                         TO_CHAR (TRANS_DATE, 'MM/DD/YYYY') TRANS_DATE, " +
					"                         bal.BANK_ID, " +
					"                         bal.BRANCH_ID, " +
					"                         NULL Gas_bill, " +
					"                         surcharge, " +
					"                         NULL fees, " +
					"                         debit Security_Dep " +
					"                    FROM BANK_ACCOUNT_LEDGER BAL, " +
					"                         MVIEW_CUSTOMER_INFO MCI, " +
					"                         MST_DEPOSIT MD " +
					"                   WHERE     BAL.CUSTOMER_ID = MCI.CUSTOMER_ID " +
					"                         AND MCI.CUSTOMER_ID = MD.CUSTOMER_ID " +
					"                         AND trans_type = 0) " +
					"        GROUP BY SUBSTR (CUSTOMER_ID, 3, 2)) aa, " +
					"       MST_CUSTOMER_CATEGORY MCC " +
					" WHERE aa.Customer_Category = MCC.CATEGORY_ID "  ;
					
	
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
  	
        	ResultSet resultSet=ps1.executeQuery();
	
        	while(resultSet.next())
        	{
        		MonthlyCollectionCustomerTypeWiseDTO sujonDTO=new MonthlyCollectionCustomerTypeWiseDTO();
        		sujonDTO.setCustomerType(resultSet.getString("CATEGORY_NAME"));
        		sujonDTO.setMonthly_Gas_Bill(resultSet.getDouble("GAS_BILL"));
        		sujonDTO.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		sujonDTO.setSucurity(resultSet.getDouble("SECURITY_DEP"));
        		sujonDTO.setFee(resultSet.getDouble("FEES"));
	
        		categoryWiseGasConsumption.add(sujonDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return categoryWiseGasConsumption;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}


	
  }



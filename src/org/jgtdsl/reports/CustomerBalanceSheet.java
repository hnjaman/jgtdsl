package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.BalanceSheetDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.TransactionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class CustomerBalanceSheet extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<BalanceSheetDTO> transactionList=new ArrayList<BalanceSheetDTO>();
	ArrayList<BalanceSheetDTO> securityListDetails = new ArrayList<BalanceSheetDTO>();
	ArrayList<BalanceSheetDTO> customerDetail=new ArrayList<BalanceSheetDTO>();
	ArrayList<BalanceSheetDTO> securityOpening=new ArrayList<BalanceSheetDTO>();
	ArrayList<BalanceSheetDTO> ledgerOpening=new ArrayList<BalanceSheetDTO>();
	
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String from_date;
	    private  String to_date;
	    private  String report_for="area_wise";
	    private String customer_id;
	    private  String area="01";
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	public String execute() throws Exception
	{
				
		String fileName="BalanceSheet.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(20,20,30,72);
		PdfPCell pcell=null;
		
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			//MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				5,190,5
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
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
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f8B);
			Paragraph p = new Paragraph(); 
			p.add(chunk1);
			p.add(chunk2);
			pcell=new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			pcell=new PdfPCell(mTable);
			pcell.setBorder(0);
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			if(report_for.equals("area_wise")){
				generatePdfDate_wise(document);
			}
			
			
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}

	
	
	
	private void generatePdfDate_wise(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("DATE WISE CUSTOMER LEDGER DATED FROM{"+from_date+"} TO {"+to_date+"}]",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		
		PdfPTable topTable = new PdfPTable(2);
		topTable.setWidthPercentage(100);
		topTable.setWidths(new float[]{60,40});
		topTable.setSpacingBefore(15f);
		
		
		PdfPCell nestedPcell1 = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		nestedPcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		nestedPcell1.setColspan(1);
		nestedPcell1.setRowspan(15);
		
		PdfPTable nestedTable1 = new PdfPTable(4);
		nestedTable1.setWidthPercentage(100);
		nestedTable1.setWidths(new float[]{25,25,25,25});
		
		pcell = new PdfPCell(new Paragraph("Customer Information",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(4);
		pcell.setRowspan(2);
		nestedTable1.addCell(pcell);
		
		customerDetail=getCustomerDetails();
		
		int customerList=customerDetail.size();
		for (int j = 0; j < customerList; j++) {
		
		pcell = new PdfPCell(new Paragraph("Category",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(1);
		pcell.setBorder(0);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getCustomer_category(),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(3);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getCustomerID(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(3);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getCustomerName(),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(3);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Pro. Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getPropriateName(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(3);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(3);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter No",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getMeterNo(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getMeterRent(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		
		
		pcell = new PdfPCell(new Paragraph("Con. Date",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+customerDetail.get(j).getConnectionDate(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Discon. Date",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": ",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		pcell.setColspan(1);
		nestedTable1.addCell(pcell);
		
		}
		nestedPcell1.addElement(nestedTable1);
		topTable.addCell(nestedPcell1);
		
		PdfPCell nestedPcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		nestedPcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		nestedPcell.setColspan(1);
		nestedPcell.setRowspan(15);
		
		////////////////////////////////////////////////////////
		
		PdfPTable nestedTable = new PdfPTable(3);
		nestedTable.setWidthPercentage(100);
		nestedTable.setWidths(new float[]{33,33,34});
		
		pcell = new PdfPCell(new Paragraph("Security Deposit",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		nestedTable.addCell(pcell);
		
		double totalOpening=0.0;
		securityOpening=getSecurityOpeningBalance();
		int securityOpeningList=securityOpening.size();
		
		for (int i = 0; i < securityOpeningList; i++) {			
		
			totalOpening=totalOpening+securityOpening.get(i).getSecurityOpening();
			pcell = new PdfPCell(new Paragraph("Opening Balance : "+taka_format.format(securityOpening.get(i).getSecurityOpening()),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(3);
			nestedTable.addCell(pcell);
		
		}
		
		
		
		
		pcell = new PdfPCell(new Paragraph("Date",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pcell.setRowspan(1);
		nestedTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Debit Amt.",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pcell.setRowspan(1);
		nestedTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Credit Amt.",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pcell.setRowspan(1);
		nestedTable.addCell(pcell);
	
		double totalSecurityAmt=0.0;
		
		securityListDetails=getSecurityDetails();
		int securityListSize=securityListDetails.size();
		for (int i = 0; i < securityListSize; i++) {
			
			pcell = new PdfPCell(new Paragraph(securityListDetails.get(i).getSecurityDate(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			pcell.setColspan(1);
			nestedTable.addCell(pcell);
			
			totalSecurityAmt=totalSecurityAmt+securityListDetails.get(i).getSecurityDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(securityListDetails.get(i).getSecurityDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			pcell.setColspan(1);
			nestedTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(securityListDetails.get(i).getSecurityCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			pcell.setColspan(1);
			nestedTable.addCell(pcell);
		}
		//////////////////////////////////////////////////////////////
		
		double allTotalSecurity=totalOpening+totalSecurityAmt;
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pcell.setBorder(0);
		nestedTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total: "+taka_format.format(allTotalSecurity),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pcell.setBorder(0);
		nestedTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pcell.setBorder(0);
		nestedTable.addCell(pcell);
		
		nestedPcell.addElement(nestedTable);
		topTable.addCell(nestedPcell);
		
//		pcell = new PdfPCell(new Paragraph("Date",ReportUtil.f9));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setColspan(1);
//		pcell.setRowspan(1);
//		topTable.addCell(pcell);
//		
//		pcell = new PdfPCell(new Paragraph("Previous",ReportUtil.f9));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setColspan(1);
//		pcell.setRowspan(1);
//		topTable.addCell(pcell);
//		
//		pcell = new PdfPCell(new Paragraph("Current",ReportUtil.f9));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setColspan(1);
//		pcell.setRowspan(1);
//		topTable.addCell(pcell);
//		
//		pcell = new PdfPCell(new Paragraph("Balance",ReportUtil.f9));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setColspan(1);
//		pcell.setRowspan(1);
//		topTable.addCell(pcell);
//		
//		pcell = new PdfPCell(new Paragraph("Status",ReportUtil.f9));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setColspan(1);
//		pcell.setRowspan(1);
//		topTable.addCell(pcell);
		
		document.add(topTable);
		
		PdfPTable pdfPTable = new PdfPTable(13);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{7,15,8,7,7,7,7,7,7,7,7,7,7});
		pdfPTable.setSpacingBefore(7f);
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Date of Bill Issued/Paid",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Particulars",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Amount of Gas Sold(SCM)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales (Tk.)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent (Tk.)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Coll. Surcharge (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Adjustment (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Others (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Debit (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Credit (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Due Date",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		double totalGasSold=0.0;
		double totalCollectionAmt=0.0;
		double totalSalesAmt=0.0;
		double totalMeterRentAmt=0.0;
		double totalBalanceAmt=0.0;
		double totalSurchargeAmt=0.0;
		double totalDuesAmt=0.0;
		transactionList=getDetailsTransactionList();
		int listSize=transactionList.size();

		
		double balanceDet =0.0;
		
		double balance=0.0;
		
		balance=getForwardBalance();
		
		for(int i=0;i<listSize;i++)
		{
			if(i==0){
				balanceDet =balance;
				pcell = new PdfPCell(new Paragraph("Balance Forward:",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(11);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(11);
				pdfPTable.addCell(pcell);
			}
		
			
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getIssueDate(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getParticular(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			totalGasSold=totalGasSold+transactionList.get(i).getConsumedGas();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getConsumedGas()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
		
			totalSalesAmt=totalSalesAmt+transactionList.get(i).getSales();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getSales()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			totalMeterRentAmt=totalMeterRentAmt+transactionList.get(i).getMeterRent();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getMeterRent()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			totalSurchargeAmt=totalSurchargeAmt+transactionList.get(i).getSurcharge();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getSurcharge()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCollectedSurcharge()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getAdjustmentAmt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getOthersAmt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			totalDuesAmt=transactionList.get(i).getDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			totalCollectionAmt=totalCollectionAmt+transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			balanceDet=balanceDet-transactionList.get(i).getCredit()+transactionList.get(i).getDebit();

							pcell = new PdfPCell(new Paragraph(taka_format.format(balanceDet),ReportUtil.f9));
							pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							pcell.setColspan(1);
							pdfPTable.addCell(pcell);
			
			
			
			//totalBalanceAmt=transactionList.get(i).getCredit()-transactionList.get(i).getDebit();
//			if(i==0){
//			balance=balanceDet-transactionList.get(0).getCredit()+transactionList.get(0).getDebit();
//
//				pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
//				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				pcell.setColspan(1);
//				pdfPTable.addCell(pcell);
//			}else if(i>0){
//				
//				totalBalanceAmt=balance;
//				totalBalanceAmt=totalBalanceAmt-transactionList.get(0).getCredit()+transactionList.get(0).getDebit();
//
//				pcell = new PdfPCell(new Paragraph(taka_format.format(totalBalanceAmt),ReportUtil.f9));
//				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				pcell.setColspan(1);
//				pdfPTable.addCell(pcell);
//				
//			}
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getDueDate(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
						
		}
		
		document.add(pdfPTable);
		
		PdfPTable footerTable1 = new PdfPTable(7);
		footerTable1.setWidthPercentage(100);
		footerTable1.setWidths(new float[]{15,12,15,12,15,12,19});
		footerTable1.setSpacingBefore(5f);
		
		pcell = new PdfPCell(new Paragraph("Total Gas Sold(SCM)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalGasSold),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total Sales Amount(Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSalesAmt),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Meter Rent Amount(Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalMeterRentAmt),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total Collection Amount (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmt),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Surcharge Amount(Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSurchargeAmt),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Dues Amount (Tk)",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balanceDet),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("In Words",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		footerTable1.addCell(pcell);
		
		
		document.add(footerTable1);
		
	}
	
	
	/////////////////////Month wise//////////////////////////
	

//	
//	
//	
//	
	private ArrayList<BalanceSheetDTO> getCustomerDetails(){           
		ArrayList<BalanceSheetDTO> customerDetails = new ArrayList<BalanceSheetDTO>();
		try {
		
			
			String account_info_sql="SELECT CI.CUSTOMER_ID,CI.CATEGORY_NAME,CI.FULL_NAME,CI.PROPRIETOR_NAME,CI.ADDRESS_LINE1,CI.CONNECTION_DATE,CM.METER_SL_NO,CM.METER_RENT  " +
									" FROM MVIEW_CUSTOMER_INFO CI,CUSTOMER_METER CM " +
									" WHERE CI.CUSTOMER_ID=CM.CUSTOMER_ID(+) " +
									" AND CI.CUSTOMER_ID= ? " ;



			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
				ps1.setString(1, customer_id);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CATEGORY_NAME"));
        		balanceSheetDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		balanceSheetDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		balanceSheetDTO.setPropriateName(resultSet.getString("PROPRIETOR_NAME"));
        		balanceSheetDTO.setAddress(resultSet.getString("ADDRESS_LINE1"));
        		balanceSheetDTO.setMeterNo(resultSet.getString("METER_SL_NO"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setConnectionDate(resultSet.getString("CONNECTION_DATE"));
        		
        		customerDetails.add(balanceSheetDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return customerDetails;
	}

	private Double getForwardBalance(){
		int count=0;
    	double balance=0.0;	
		try {		
			
				String account_info_sql="select DEBIT,CREDIT,BALANCE from customer_ledger" 
									    +" where customer_id=?"
										+" and trans_date<=to_date(?,'dd-MM-YYYY') and status=1"
										+" Order By TRANS_DATE,INSERTED_ON Asc" ;


			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			ps1.setString(1, customer_id);
			ps1.setString(2, from_date);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        
        	
        	while(resultSet.next())
        	{
        		if(count==0){
        			balance=resultSet.getDouble("BALANCE");
        		}else
        		{
        			balance=balance+resultSet.getDouble("DEBIT")-resultSet.getDouble("CREDIT");
        		}
        		count++;
        		   
        		
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return balance;
	}
	
	private ArrayList<BalanceSheetDTO> getSecurityOpeningBalance(){
		ArrayList<BalanceSheetDTO> openingBalanceAmt= new ArrayList<BalanceSheetDTO>();
		try {
			
			String transaction_sql="SELECT sum(SECURITY_AMOUNT) SECURITY_AMOUNT, sum(CREDIT) CREDIT " +
					"FROM(SELECT SECURITY_AMOUNT,CREDIT " +
					"FROM CUSTOMER_SECURITY_LEDGER " +
					"WHERE CUSTOMER_ID = '"+customer_id+"' " +
					"AND SECURITY_AMOUNT > 0 " +
					"AND TRANS_DATE < TO_DATE ('"+from_date+"', 'dd/mm/yyyy')) " ;
			
			
				PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
				ResultSet resultSet=ps1.executeQuery();
        	
        	
				while(resultSet.next())
				{
					BalanceSheetDTO openDto=new BalanceSheetDTO();
					//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
					
					openDto.setSecurityOpening(resultSet.getDouble("SECURITY_AMOUNT"));
					openDto.setSecurityCredit(resultSet.getDouble("CREDIT"));
					
					openingBalanceAmt.add(openDto);
   
        		
        		
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return openingBalanceAmt;
	}
	
	
	
	
	private ArrayList<BalanceSheetDTO> getSecurityDetails(){
		ArrayList<BalanceSheetDTO> securityTransactionList=new ArrayList<BalanceSheetDTO>();
		
		try {
			
			
			
		
			
			
			String transaction_sql="select TO_CHAR(TRANS_DATE) TRANS_DATE, SECURITY_AMOUNT, CREDIT from( " +
									"select TRANS_DATE, SECURITY_AMOUNT, CREDIT,rownum sl " +
									"from( " +
									"SELECT TRANS_DATE, SECURITY_AMOUNT, CREDIT " +
									"    FROM CUSTOMER_SECURITY_LEDGER " +
									"   WHERE CUSTOMER_ID = '"+customer_id+"' AND SECURITY_AMOUNT > 0 " +
									"ORDER BY TRANS_DATE desc) " +
									") " +
									"where sl<7 " +
									"AND TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd/mm/yyyy') " +
									"                          AND TO_DATE ('"+to_date+"', 'dd/mm/yyyy') " ;



			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO securityDto=new BalanceSheetDTO();
        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		securityDto.setSecurityDate(resultSet.getString("TRANS_DATE"));
        		securityDto.setSecurityDebit(resultSet.getDouble("SECURITY_AMOUNT"));
        		securityDto.setSecurityCredit(resultSet.getDouble("CREDIT"));     		
   
        		
        		securityTransactionList.add(securityDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return securityTransactionList;
	}
	

	private ArrayList<BalanceSheetDTO> getDetailsTransactionList()
	{
	ArrayList<BalanceSheetDTO> transactionListDetails=new ArrayList<BalanceSheetDTO>();
		
		try {
			
			
			
		
			
			
			String transaction_sql="SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
					"       CL.PARTICULARS, " +
					"       tm.BILLED_CONSUMPTION scm, " +
					"         CL.DEBIT " +
					"       - (  NVL (METER_RENT, 0) " +
					"          + NVL (ADJUSTMENT_AMOUNT, 0) " +
					"          + NVL (SURCHARGE_AMOUNT, 0) " +
					"          + NVL (OTHERS_AMOUNT, 0)) " +
					"          sales, " +
					"       METER_RENT, " +
					"       SURCHARGE_AMOUNT, " +
					"       SURCHARGE_AMOUNT COLL_SURCHARGE, " +
					"       ADJUSTMENT_AMOUNT, " +
					"       OTHERS_AMOUNT, " +
					"       decode(CL.PARTICULARS,'Balance Transfer',cl.BALANCE,cl.DEBIT) DEBIT, " +
					"       CL.CREDIT, " +
					"       TO_CHAR (tm.LAST_PAY_DATE_W_SC) DUEDATE, " +
					"       TRANS_DATE TRANS_DATE1,CL.STATUS " +
					"  FROM CUSTOMER_LEDGER cl, " +
					"       (SELECT BM.BILL_ID, " +
					"               BILLED_CONSUMPTION, " +
					"               METER_RENT, " +
					"               ADJUSTMENT_AMOUNT, " +
					"               SURCHARGE_AMOUNT, " +
					"               OTHERS_AMOUNT, " +
					"               LAST_PAY_DATE_W_SC " +
					"          FROM SUMMARY_MARGIN_PB sm, BILL_METERED bm " +
					"         WHERE BM.BILL_ID = SM.BILL_ID AND CUSTOMER_ID =  '"+customer_id+"') tm " +
					" WHERE     CL.BILL_ID = tm.BILL_ID(+) " +
					"       AND cl.CUSTOMER_ID =  '"+customer_id+"' " +
					"       AND NVL (CL.CREDIT, 0) = 0 " +
					"       AND TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd/mm/yyyy') " +
					"                          AND TO_DATE ('"+to_date+"', 'dd/mm/yyyy') " +
					"                          AND cl.status=1 " +
					"UNION ALL " +
					"SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
					"       CL.PARTICULARS, " +
					"       NULL scm, " +
					"       NULL sales, " +
					"       NULL METER_RENT, " +
					"       NULL SURCHARGE_AMOUNT, " +
					"       NULL coll_surcharge, " +
					"       NULL ADJUSTMENT_AMOUNT, " +
					"       NULL OTHERS_AMOUNT, " +
					"       cl.DEBIT, " +
					"       CL.CREDIT, " +
					"       NULL duedate, " +
					"       TRANS_DATE TRANS_DATE1,CL.STATUS " +
					"  FROM CUSTOMER_LEDGER cl, BILL_COLLECTION_METERED bc " +
					" WHERE     CL.COLLECTION_ID = bc.COLLECTION_ID(+) " +
					"       AND cl.CUSTOMER_ID = '"+customer_id+"' " +
					"       AND NVL (CL.DEBIT, 0) = 0 " +
					"       AND TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd/mm/yyyy') " +
					"                          AND TO_DATE ('"+to_date+"', 'dd/mm/yyyy') " +
					"                          AND status=1 " +
					"            and CL.PARTICULARS<>'Balance Transfer' " +
					"ORDER BY TRANS_DATE1 " ;

					
					
					
					
					
					
					
					
					
					
//					"SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
//					"       CL.PARTICULARS, " +
//					"       tm.BILLED_CONSUMPTION scm, " +
//					"         CL.DEBIT " +
//					"       - (  NVL (METER_RENT, 0) " +
//					"          + NVL (ADJUSTMENT_AMOUNT, 0) " +
//					"          + NVL (SURCHARGE_AMOUNT, 0) " +
//					"          + NVL (OTHERS_AMOUNT, 0)) " +
//					"          sales, " +
//					"       METER_RENT, " +
//					"       SURCHARGE_AMOUNT, " +
//					"       SURCHARGE_AMOUNT COLL_SURCHARGE, " +
//					"       ADJUSTMENT_AMOUNT, " +
//					"       OTHERS_AMOUNT, " +
//					"       case " +
//					"   when particulars like '%Balance Transfer%' then balance " +
//					"   else cl.DEBIT " +
//					"end DEBIT,CL.CREDIT, " +
//					"       TO_CHAR (tm.LAST_PAY_DATE_W_SC) DUEDATE, " +
//					"       TRANS_DATE TRANS_DATE1 " +
//					"  FROM CUSTOMER_LEDGER cl, " +
//					"       (SELECT BM.BILL_ID, " +
//					"               BILLED_CONSUMPTION, " +
//					"               METER_RENT, " +
//					"               ADJUSTMENT_AMOUNT, " +
//					"               SURCHARGE_AMOUNT, " +
//					"               OTHERS_AMOUNT, " +
//					"               LAST_PAY_DATE_W_SC " +
//					"          FROM SUMMARY_MARGIN_PB sm, BILL_METERED bm " +
//					"         WHERE BM.BILL_ID = SM.BILL_ID AND CUSTOMER_ID = '"+customer_id+"') tm " +
//					" WHERE     CL.BILL_ID = tm.BILL_ID(+) " +
//					"       AND cl.CUSTOMER_ID = '"+customer_id+"' " +
//					"       AND NVL (CL.CREDIT, 0) = 0 " +
//					"       AND TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd/mm/yyyy') " +
//					"                          AND TO_DATE ('"+to_date+"', 'dd/mm/yyyy') " +
//					"UNION ALL " +
//					"SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
//					"       CL.PARTICULARS, " +
//					"       NULL scm, " +
//					"       NULL sales, " +
//					"       NULL METER_RENT, " +
//					"       NULL SURCHARGE_AMOUNT, " +
//					"       NULL coll_surcharge, " +
//					"       NULL ADJUSTMENT_AMOUNT, " +
//					"       NULL OTHERS_AMOUNT, " +
//					"       cl.DEBIT, " +
//					"       CL.CREDIT, " +
//					"       NULL duedate, " +
//					"       TRANS_DATE TRANS_DATE1 " +
//					"  FROM CUSTOMER_LEDGER cl, BILL_COLLECTION_METERED bc " +
//					" WHERE     CL.COLLECTION_ID = bc.COLLECTION_ID(+) " +
//					"       AND cl.CUSTOMER_ID = '"+customer_id+"' " +
//					"       AND NVL (CL.DEBIT, 0) = 0 " +
//					"       AND TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd/mm/yyyy') " +
//					"                          AND TO_DATE ('"+to_date+"', 'dd/mm/yyyy') " +
//					"      AND  particulars NOT LIKE '%Balance Transfer%'           " +
//					"      ORDER BY TRANS_DATE1,TRANS_ID " ;


			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO transactionDto1=new BalanceSheetDTO();
        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto1.setIssueDate(resultSet.getString("TRANS_DATE"));
        		transactionDto1.setParticular(resultSet.getString("PARTICULARS"));
        		transactionDto1.setConsumedGas(resultSet.getDouble("SCM"));
        		transactionDto1.setSales(resultSet.getDouble("SALES"));
        		transactionDto1.setMeterRent(resultSet.getDouble("METER_RENT"));
        		transactionDto1.setSurcharge(resultSet.getDouble("SURCHARGE_AMOUNT"));
        		transactionDto1.setCollectedSurcharge(resultSet.getDouble("COLL_SURCHARGE"));
        		transactionDto1.setAdjustmentAmt(resultSet.getDouble("ADJUSTMENT_AMOUNT"));
        		transactionDto1.setOthersAmt(resultSet.getDouble("OTHERS_AMOUNT"));
        		transactionDto1.setDebit(resultSet.getDouble("DEBIT"));
        		transactionDto1.setCredit(resultSet.getDouble("CREDIT"));
        		transactionDto1.setDueDate(resultSet.getString("DUEDATE"));       		
   
        		
        		transactionListDetails.add(transactionDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionListDetails;
	}
	
	
	
	
	

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}

	
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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




	public String getCustomer_id() {
		return customer_id;
	}




	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	
  }



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

import oracle.jdbc.driver.OracleCallableStatement;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.NonMeterBillSummeryDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.TariffDTO;
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




public class NonMeterBillSummeryReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String billing_month;
	    private  String billing_year;
	    private  String report_for; 
	    private  String area="01";
	    NonMeterBillSummeryDTO billSummeryInfo=new NonMeterBillSummeryDTO();
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static String pattern = "###";
        static DecimalFormat  qnt_format = new DecimalFormat(pattern);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	public String execute() throws Exception
	{
				
		String fileName=String.valueOf(Month.values()[Integer.valueOf(billing_month)-1])+"/"+billing_year+"_Non_Meter_Bill_Summery_"+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1])+".pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
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
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			PdfPTable headline = new PdfPTable(1);
			headline.setWidths(new float[]{100});
			
			pcell = new PdfPCell(new Paragraph("Calculation Of Customers,Burners & Billed Amount of Domestic(NM) for the month of "+Month.values()[Integer.valueOf(billing_month)-1]+" "+billing_year,ReportUtil.f11B));
			pcell.setBorder(0);
			headline.addCell(pcell);
			document.add(headline);
			
			double tarifRate=gettarifRate();    /*-------------unit price of gas--------------*/
			double price=gettarifPrice();       /*-------------Burner bill of Gas-------------*/
						
			
	    	billSummeryInfo=getNonMeterBillSummery();
			PdfPTable topTable = new PdfPTable(8);
			topTable.setWidthPercentage(100);
			topTable.setWidths(new float[]{5,25,10,10,10,10,15,15});
			topTable.setSpacingBefore(15f);
			
			pcell = new PdfPCell(new Paragraph("Private:",ReportUtil.f11B));
			pcell.setColspan(8);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SL. No",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Particulars",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Number of Customers",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Quantity of Double Burnres",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Rate per DB(Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Billed Amount (Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Gas Consumption (SCM)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Actual Bill",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("2",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("3",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("4",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("5",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("7=4x5",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("8=7/(Rate)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("A)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Opening Balance",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalCustomerPvtFull()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getBurnerPvtFull()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(price),ReportUtil.f9B));  /*First Square*/
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(6);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double openingAmountPvtFull=(billSummeryInfo.getBurnerPvtFull()*price);
			pcell = new PdfPCell(new Paragraph(taka_format.format(openingAmountPvtFull),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			
			
			double partialPvtTotaFull=billSummeryInfo.getTotalPvtConAmount()+billSummeryInfo.getTotalPvtIncAmount()+billSummeryInfo.getFTotalPvtReconPartialAmount()-billSummeryInfo.getFTotalPvtDisPartialAmount();
			double totalPvtAmountFull=openingAmountPvtFull+partialPvtTotaFull;
			double totalPvtConsumption=totalPvtAmountFull/tarifRate;
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtConsumption),ReportUtil.f9));  /*First Line Square*/
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(7);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("01",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Add: New Connection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalPvtConCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalPvtConBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(billSummeryInfo.getTotalPvtConAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////// This Section(B) is for total Partial Amount Private(Full)//////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
			pcell = new PdfPCell(new Paragraph(taka_format.format(partialPvtTotaFull),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(5);
			topTable.addCell(pcell);
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("02",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Add: Burner Extension",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9));   ////qnt_format.format(billSummeryInfo.getTotalPvtIncCus()
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalPvtIncBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(billSummeryInfo.getTotalPvtIncAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("03",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Less: Burner Disconnection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getFTotalPvtDisCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getFTotalPvtDisBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+taka_format.format(billSummeryInfo.getFTotalPvtDisPartialAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("04",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Add: Burner Reconnection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getFTotalPvtReconCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getFTotalPvtReconBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(billSummeryInfo.getFTotalPvtReconPartialAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("05",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Adjustment(if Any)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total(A)= ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			topTable.addCell(pcell);
			
			double totalCustomerPvtFinal=billSummeryInfo.getTotalCustomerPvtFull()+billSummeryInfo.getTotalPvtConCus()+billSummeryInfo.getFTotalPvtReconCus()-billSummeryInfo.getFTotalPvtDisCus()/*+billSummeryInfo.getTotalPvtIncCus()*/;
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalCustomerPvtFinal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double totalBurnerPvtFinal=billSummeryInfo.getBurnerPvtFull()+billSummeryInfo.getTotalPvtConBurQnt()+billSummeryInfo.getTotalPvtIncBurQnt()-billSummeryInfo.getFTotalPvtDisBurQnt()+billSummeryInfo.getFTotalPvtReconBurQnt();
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalBurnerPvtFinal),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
		
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPvtAmountFull),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			/////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////Section B Start/////////////////////////////////////////
			//////////////////////////////////////////////////////////////////////////////////////////
			pcell = new PdfPCell(new Paragraph("B)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Bill for Temporary Disconnection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalCustomerPvtHalf()),ReportUtil.f9B)); ///// Bill for Temporary Disconnection >>>>column 3
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getBurnerPvtHalf()),ReportUtil.f9B));///// Bill for Temporary Disconnection >>>>column 4
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(price/2),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(3);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double openingAmountPvtHalf=billSummeryInfo.getBurnerPvtHalf()*(price/2);  //////////// this need to be change
			pcell = new PdfPCell(new Paragraph(qnt_format.format(openingAmountPvtHalf),ReportUtil.f9B));           /////////////// ///// Bill for Temporary Disconnection >>>>column 7
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			
			
			double totalPartialPvtHalf=billSummeryInfo.getHTotalPvtDisPartialAmount()-billSummeryInfo.getHTotalPvtReconPartialAmount()-billSummeryInfo.getTempToPerDissPartialAmount();
			
			double totalPvtAmountHalf=openingAmountPvtHalf+totalPartialPvtHalf;//(Partial Amount Pvt half section)
			
			double totalPartialGovtConsumption=totalPvtAmountHalf/tarifRate;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPartialGovtConsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(5);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Add: New tem. Disconnection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getHTotalPvtDisCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getHTotalPvtDisBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(billSummeryInfo.getHTotalPvtDisPartialAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////// This Section(B) is for total Partial Amount Private(Applied by Customer)//////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPartialPvtHalf),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(3);
			topTable.addCell(pcell);
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Less: Reconnection From TD",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getHTotalPvtReconCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getHTotalPvtReconBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+taka_format.format(billSummeryInfo.getHTotalPvtReconPartialAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Less:Permanent Disconn from TD",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getTempToPerDissCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getTempToPerDissBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+taka_format.format(billSummeryInfo.getTempToPerDissPartialAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total(B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalCustomerPvtHalf()+billSummeryInfo.getHTotalPvtDisCus()-billSummeryInfo.getHTotalPvtReconCus()- billSummeryInfo.getTempToPerDissCus()),ReportUtil.f8B));  //// opening customer pvt half+ reconnect pvt half-disconnection pvt half- disconnection from temp to Permant
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			//////////////////////Sub Total(B)=   (row)           4(column) //////////////////
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getBurnerPvtHalf()+billSummeryInfo.getHTotalPvtDisBurQnt()-billSummeryInfo.getHTotalPvtReconBurQnt()- billSummeryInfo.getTempToPerDissBurQnt()),ReportUtil.f8B));  //// opening Burner pvt half+ reconnect pvt half-disconnection pvt half- disconnection from temp to Permant
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalPvtAmountHalf),ReportUtil.f8B)); //// Sub Total(B)= (column 7)
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Dom. Private(A+B)=",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalCustomerPvtFinal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalBurnerPvtFinal),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double totalPvtAmount=totalPvtAmountFull+totalPvtAmountHalf;
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPvtAmount),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtConsumption),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			
			
			
			/////////////////////////////////////////////////////////////////////
			///////////////////////End of Domestic Gvt///////////////////////////
			/////////////////////////////////////////////////////////////////////
			
			
			pcell = new PdfPCell(new Paragraph("GOVERNMENT :",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			pcell.setColspan(8);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("C)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Opening Balance",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalCustomerGvtFull()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getBurnerGvtFull()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(price),ReportUtil.f9));  /*First Square*/
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(6);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double openingBalanceGovt=billSummeryInfo.getBurnerGvtFull()*price;
			pcell = new PdfPCell(new Paragraph(qnt_format.format(openingBalanceGovt),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			
			double totalPartialGvt=billSummeryInfo.getTotalGovtConAmount()+billSummeryInfo.getTotalGovtIncAmount()+billSummeryInfo.getTotalGovtReconPartialAmount()-billSummeryInfo.getTotalGovtDisPartialAmount();
			double totalAmountGovt=openingBalanceGovt+totalPartialGvt;
			double totalConsumptionGvt=totalAmountGovt/tarifRate;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalConsumptionGvt),ReportUtil.f9B));  /*First Line Square*/
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(7);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("01",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Add: New Connection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtConCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtConBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtConAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
		
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalPartialGvt),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setRowspan(5);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("02",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Add: Burner Extension",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtIncCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtIncBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtIncAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("03",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Less: Burner Disconnection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getTotalGovtDisCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getTotalGovtDisBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(-)"+qnt_format.format(billSummeryInfo.getTotalGovtDisPartialAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("04",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Add: Burner Reconnection",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtReconCus()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtReconBurQnt()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(billSummeryInfo.getTotalGovtReconPartialAmount()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("05",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Adjustment(if Any)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("0.00",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total(C)= ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			topTable.addCell(pcell);
			
			double totalCustomerGvt=billSummeryInfo.getTotalCustomerGvtFull()+billSummeryInfo.getTotalGovtConCus()+billSummeryInfo.getTotalGovtIncCus()-billSummeryInfo.getTotalGovtDisCus()+billSummeryInfo.getTotalGovtReconCus();
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalCustomerGvt),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double totalBurnerGvt=billSummeryInfo.getBurnerGvtFull()+billSummeryInfo.getTotalGovtConBurQnt()+billSummeryInfo.getTotalGovtIncBurQnt()+billSummeryInfo.getTotalGovtIncAmount()-billSummeryInfo.getTotalGovtDisBurQnt()+billSummeryInfo.getTotalGovtReconBurQnt();
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalBurnerGvt),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalAmountGovt),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			
			
			
			/*----------------------------------------------Sub Total A+B+C-------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("Grand Total(A+B+C)=",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			topTable.addCell(pcell);
			
			double totalCustomer=totalCustomerPvtFinal+totalCustomerGvt;
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalCustomer),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double totalBurner=totalBurnerPvtFinal+totalBurnerGvt;
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalBurner),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double totalAmount=totalAmountGovt+totalPvtAmount;
			pcell = new PdfPCell(new Paragraph(qnt_format.format(totalAmount),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			double totalConsumption=totalConsumptionGvt+totalPvtConsumption; 
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalConsumption),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			topTable.addCell(pcell);
			
			/*------------------------------------------------------------------------------*/
			
			
		
			document.add(topTable);			
		
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
				
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	private NonMeterBillSummeryDTO getNonMeterBillSummery()
	{
		NonMeterBillSummeryDTO billSummery=new NonMeterBillSummeryDTO();
		ResponseDTO response=new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code=0;
		
		
		try {
			
			
		
			
			
			stmt = (OracleCallableStatement) conn.prepareCall("{ call get_Non_Meter_Burner_Info(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  }");
			stmt.setString(1, area);           
            stmt.setString(2,billing_month);
            stmt.setString(3, billing_year);
            stmt.registerOutParameter(4, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(5, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(6, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(7, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(8, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(9, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(10, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(11, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(12, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(13, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(14, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(15, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(16, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(17, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(18, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(19, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(20, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(21, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(22, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(23, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(24, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(25, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(26, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(27, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(28, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(29, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(30, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(31, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(32, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(33, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(34, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(35, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(36, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(37, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(38, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(39, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(40, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(41, java.sql.Types.DOUBLE);
            stmt.registerOutParameter(42, java.sql.Types.DOUBLE);
          
            
            stmt.executeUpdate();
            billSummery.setTotalCustomerPvtFull(stmt.getDouble(4));
            billSummery.setBurnerPvtFull(stmt.getDouble(5));
            billSummery.setTotalCustomerPvtHalf(stmt.getDouble(6));
            billSummery.setBurnerPvtHalf(stmt.getDouble(7));
            billSummery.setTotalCustomerGvtFull(stmt.getDouble(8));
            billSummery.setBurnerGvtFull(stmt.getDouble(9));
            billSummery.setFTotalPvtDisCus(stmt.getDouble(10));
            billSummery.setFTotalPvtDisBurQnt(stmt.getDouble(11));
            billSummery.setFTotalPvtDisPartialAmount(stmt.getDouble(12));
            billSummery.setTotalGovtDisCus(stmt.getDouble(13));
            billSummery.setTotalGovtDisBurQnt(stmt.getDouble(14));
            billSummery.setTotalGovtDisPartialAmount(stmt.getDouble(15));
            billSummery.setFTotalPvtReconCus(stmt.getDouble(16));
            billSummery.setFTotalPvtReconBurQnt(stmt.getDouble(17));
            billSummery.setFTotalPvtReconPartialAmount(stmt.getDouble(18));
            billSummery.setTotalGovtReconCus(stmt.getDouble(19));
            billSummery.setTotalGovtReconBurQnt(stmt.getDouble(20));
            billSummery.setTotalGovtReconPartialAmount(stmt.getDouble(21));
            billSummery.setTotalPvtIncCus(stmt.getDouble(22));
            billSummery.setTotalPvtIncBurQnt(stmt.getDouble(23));
            billSummery.setTotalPvtIncAmount(stmt.getDouble(24));
            billSummery.setTotalGovtIncCus(stmt.getDouble(25));
            billSummery.setTotalGovtIncBurQnt(stmt.getDouble(26));
            billSummery.setTotalGovtIncAmount(stmt.getDouble(27));
            billSummery.setTotalPvtConCus(stmt.getDouble(28));
            billSummery.setTotalPvtConBurQnt(stmt.getDouble(29));
            billSummery.setTotalPvtConAmount(stmt.getDouble(30));
            billSummery.setTotalGovtConCus(stmt.getDouble(31));
            billSummery.setTotalGovtConBurQnt(stmt.getDouble(32));
            billSummery.setTotalGovtConAmount(stmt.getDouble(33));
            billSummery.setHTotalPvtDisCus(stmt.getDouble(34));
            billSummery.setHTotalPvtDisBurQnt(stmt.getDouble(35));
            billSummery.setHTotalPvtDisPartialAmount(stmt.getDouble(36));
            billSummery.setHTotalPvtReconCus(stmt.getDouble(37));
            billSummery.setHTotalPvtReconBurQnt(stmt.getDouble(38));
            billSummery.setHTotalPvtReconPartialAmount(stmt.getDouble(39));
            billSummery.setTempToPerDissCus(stmt.getDouble(40));
            billSummery.setTempToPerDissBurQnt(stmt.getDouble(41));
            billSummery.setTempToPerDissPartialAmount(stmt.getDouble(42));
            

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return billSummery;
	}
		
	private Double gettarifPrice(){
		
		
		double tarifRate=0.0; 
		
		
		try {
			
			
			String account_info_sql="select PRICE  from MST_TARIFF  " +
					"where CUSTOMER_CATEGORY_ID='01' and BURNER_CATEGORY=2 " +
					"and EFFECTIVE_FROM<=to_date(lpad("+billing_month+",2,0)||"+billing_year+",'mmyyyy') " +
					"and nvl(EFFECTIVE_TO,sysdate)>=to_date(lpad("+billing_month+",2,0)||"+billing_year+",'mmyyyy') ";
			
			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		tarifRate=resultSet.getDouble("PRICE");
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tarifRate;
		
		
	}
	
private Double gettarifRate(){
		
		
		double tarifRate=0.0; 
		
		
		try {
			
			
			String account_info_sql="select PRICE  from MST_TARIFF  " +
					"where CUSTOMER_CATEGORY_ID='01'  " +
					"AND METER_STATUS=1 " +
					"AND BURNER_CATEGORY is null " +
					"and EFFECTIVE_FROM<=to_date(lpad("+billing_month+",2,0)||"+billing_year+",'mmyyyy') " +
					"and nvl(EFFECTIVE_TO,sysdate)>=to_date(lpad("+billing_month+",2,0)||"+billing_year+",'mmyyyy') ";
			
			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		tarifRate=resultSet.getDouble("PRICE");
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tarifRate;
		
		
	}

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}







	public String getBilling_month() {
		return billing_month;
	}

	public void setBilling_month(String billing_month) {
		this.billing_month = billing_month;
	}

	public String getBilling_year() {
		return billing_year;
	}

	public void setBilling_year(String billing_year) {
		this.billing_year = billing_year;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	
  }



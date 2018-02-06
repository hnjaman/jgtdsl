package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class AccountsPayableReport extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	AccountDTO accountInfo=new AccountDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	

	    private  String bill_month;
	    private  String bill_year;
	    private  String collection_date;
	    private  String report_for; 
	    private  String area="01";
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	

	public String execute() throws Exception
	{
				
		String fileName="AccountsPayableReport.pdf";
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

			pcell=new PdfPCell(new Paragraph("Head Office,Nalka, Sirajganj", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			pcell=new PdfPCell(mTable);
			pcell.setBorder(0);
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			pcell.setMinimumHeight(10f);
			headerTable.addCell(pcell);
			
			document.add(headerTable);
			
			PdfPTable headLine=new PdfPTable(1);
			headLine.setWidths(new float[]{100});
			
			pcell = new PdfPCell(new Paragraph("Accounts Payable for the Month of ",ReportUtil.f11B));
			pcell.setBorder(0);
			pcell.setMinimumHeight(10f);
			headLine.addCell(pcell);
			
			document.add(headLine);
			
			PdfPTable headLinetable = null;
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{10,80,10});
			
//			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
//			pcell.setBorder(0);
//			headLinetable.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph("STATEMENT OF ACCOUNTS PAYABLE FOR THE MONTH OF "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year+"(Provisional)",ReportUtil.f11B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			pcell.setBorder(0);
//			headLinetable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph(""));
//			pcell.setBorder(0);
//			headLinetable.addCell(pcell);
			
			
			document.add(headLinetable);
			
			GasPurchaseDTO purchaseRatio=new GasPurchaseDTO();
			TariffDTO bgfclMarginInfo=new TariffDTO();
			TariffDTO sgflMarginInfo=new TariffDTO();
			
			purchaseRatio=getGasPurchaseRatio();
			
			bgfclMarginInfo=getMarginInfo("MARGIN_BGFCL");
			sgflMarginInfo=getMarginInfo("MARGIN_SGFL");
			double vat_rebate=getTotalVatRebate();
			TariffDTO marginInfo=getPaidInfo();
			TariffDTO adjustmentInfo=getAdjustmentInfo();
			TariffDTO PayableInfoUptoLastMonth= getPayableInfoUpToLastMonth();
			

			
			PdfPTable pTable = new PdfPTable(10);
			pTable.setWidthPercentage(100);
			pTable.setWidths(new float[]{5,10,11,10,10,10,10,11,10,13});
			pTable.setHeaderRows(1);
			
			pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Name of the Organization",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Amount Payable up to last month",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Adjustment",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Amount Payable up to last month after adjustment",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Amount Payable during "  ,ReportUtil.f9)); /*Value of Month will obtain from DB */
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Amount Paid during "   ,ReportUtil.f9)); /*Value of Month will obtain from DB */
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Outstanding Amount Payable up to " ,ReportUtil.f9));/*Value of Date will obtain from DB */
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Equivalent Arrear months",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Name of the arrear months",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Column Number */
			
			pcell = new PdfPCell(new Paragraph("01",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("02",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("03",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("04",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("05",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("06"  ,ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("07",ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("08=(05+06)-07" ,ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Value of BGFCL */
			
			
			pcell = new PdfPCell(new Paragraph("01",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("BGFCL",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getBgfcl()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getBgfcl()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double payable_after_adjust_bgfcl=PayableInfoUptoLastMonth.getBgfcl()+adjustmentInfo.getBgfcl();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_bgfcl),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			
			double bgfcl=bgfclMarginInfo.getVat()+bgfclMarginInfo.getSd()+bgfclMarginInfo.getWellhead()+(vat_rebate*purchaseRatio.getBgfcl_ratio()); //// minus vat rebate
			pcell = new PdfPCell(new Paragraph(taka_format.format(bgfcl)  ,ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getBgfcl()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double outstanding_bgfcl=payable_after_adjust_bgfcl+bgfcl-marginInfo.getBgfcl();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_bgfcl),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Value of SGFL */
			
			pcell = new PdfPCell(new Paragraph("02",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SGFL",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getSgfl()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getSgfl()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double payable_after_adjust_sgfl=PayableInfoUptoLastMonth.getSgfl()+adjustmentInfo.getSgfl();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_sgfl),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
            double sgfl=sgflMarginInfo.getVat()+sgflMarginInfo.getSd()+sgflMarginInfo.getWellhead()+(vat_rebate*purchaseRatio.getSgfl_ratio()); ;/// vat rebate minus
			pcell = new PdfPCell(new Paragraph(taka_format.format(sgfl),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getSgfl()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double outstanding_sgfl=payable_after_adjust_sgfl+sgfl-marginInfo.getSgfl();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_sgfl),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Value of PDF */
			
			pcell = new PdfPCell(new Paragraph("03",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("PDF",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getPdf()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getPdf()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double payable_after_adjust_pdf=PayableInfoUptoLastMonth.getPdf()+adjustmentInfo.getPdf();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_pdf),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double pdf=bgfclMarginInfo.getPdf()+sgflMarginInfo.getPdf();
			pcell = new PdfPCell(new Paragraph(taka_format.format(pdf),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getPdf()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double outstanding_pdf=payable_after_adjust_pdf+pdf-marginInfo.getPdf();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_pdf) ,ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/*Value of BAPEX */
			
			pcell = new PdfPCell(new Paragraph("04",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("BAPEX",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getBapex()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getBapex()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			
			double payable_after_adjust_bapex=PayableInfoUptoLastMonth.getBapex()+adjustmentInfo.getBapex();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_bapex),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double bapex=bgfclMarginInfo.getBapex()+sgflMarginInfo.getBapex();
			pcell = new PdfPCell(new Paragraph(taka_format.format(bapex) ,ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getBapex()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double outstanding_Bapex=payable_after_adjust_bapex+bapex-marginInfo.getBapex();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_Bapex) ,ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Value of GTCL */
			
			pcell = new PdfPCell(new Paragraph("05",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("GTCL",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getTrasmission()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getTrasmission()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double payable_after_adjust_gtcl=PayableInfoUptoLastMonth.getTrasmission()+adjustmentInfo.getTrasmission();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_gtcl),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double gtcl=bgfclMarginInfo.getTrasmission()+sgflMarginInfo.getTrasmission();
			pcell = new PdfPCell(new Paragraph(taka_format.format(gtcl),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getTrasmission()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double outstanding_gtcl=payable_after_adjust_gtcl+gtcl-marginInfo.getTrasmission();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_gtcl) ,ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Deficit Wellhead Margin for BAPEX */
			
			pcell = new PdfPCell(new Paragraph("06",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Deficit Wellhead Margin for BAPEX",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getDwellhead()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getDwellhead()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double payable_after_adjust_dwellhead=PayableInfoUptoLastMonth.getDwellhead()+adjustmentInfo.getDwellhead();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_dwellhead),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double dwellHead=bgfclMarginInfo.getDwellhead()+sgflMarginInfo.getDwellhead();
			pcell = new PdfPCell(new Paragraph(taka_format.format(dwellHead) ,ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getDwellhead()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double outstanding_dwellhead=payable_after_adjust_dwellhead+dwellHead-marginInfo.getDwellhead();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_dwellhead),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Gas Development Fund */
			
			pcell = new PdfPCell(new Paragraph("07",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Gas Development Fund",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getGdfund()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getGdfund()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double payable_after_adjust_gd_fund=PayableInfoUptoLastMonth.getGdfund()+adjustmentInfo.getGdfund();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_gd_fund),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double gd_fund=bgfclMarginInfo.getGdfund()+sgflMarginInfo.getGdfund();
			pcell = new PdfPCell(new Paragraph(taka_format.format(gd_fund),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getGdfund()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double outstanding_gdfund=payable_after_adjust_gd_fund+gd_fund-marginInfo.getGdfund();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_gdfund),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Asset value of Gas*/
			
			pcell = new PdfPCell(new Paragraph("08",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Asset value of Gas",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(PayableInfoUptoLastMonth.getAvalue()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustmentInfo.getAvalue()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double payable_after_adjust_avalue=PayableInfoUptoLastMonth.getAvalue()+adjustmentInfo.getAvalue();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjust_avalue),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double asset_value=bgfclMarginInfo.getAvalue()+sgflMarginInfo.getAvalue();
			pcell = new PdfPCell(new Paragraph(taka_format.format(asset_value) ,ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(marginInfo.getAvalue()),ReportUtil.f9)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double outstanding_avalue=payable_after_adjust_avalue+asset_value-marginInfo.getAvalue();
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding_avalue),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("09",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			/* Total Payable Amount Calculation */
			
			pcell = new PdfPCell(new Paragraph("Total Payable =",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			pTable.addCell(pcell);
			
			double payableUptoLastMonth=PayableInfoUptoLastMonth.getBgfcl()+PayableInfoUptoLastMonth.getSgfl()+PayableInfoUptoLastMonth.getPdf()+PayableInfoUptoLastMonth.getBapex()+PayableInfoUptoLastMonth.getTrasmission()+PayableInfoUptoLastMonth.getDwellhead()+PayableInfoUptoLastMonth.getGdfund()
					+PayableInfoUptoLastMonth.getAvalue();
			pcell = new PdfPCell(new Paragraph(taka_format.format(payableUptoLastMonth),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double adjustment=adjustmentInfo.getBgfcl()+adjustmentInfo.getSgfl()+adjustmentInfo.getPdf()+adjustmentInfo.getBapex()+adjustmentInfo.getTrasmission()+adjustmentInfo.getDwellhead()+adjustmentInfo.getGdfund()+adjustmentInfo.getAvalue();
			pcell = new PdfPCell(new Paragraph(taka_format.format(adjustment),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			double payable_after_adjustment=payable_after_adjust_bgfcl+payable_after_adjust_sgfl+payable_after_adjust_pdf+payable_after_adjust_bapex+payable_after_adjust_gtcl+payable_after_adjust_dwellhead+payable_after_adjust_gd_fund+payable_after_adjust_avalue;
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_after_adjustment),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double payable_current_month=bgfcl+sgfl+pdf+bapex+gtcl+dwellHead+gd_fund+asset_value;
			pcell = new PdfPCell(new Paragraph(taka_format.format(payable_current_month),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double total_margin_payment=marginInfo.getBgfcl()+marginInfo.getSgfl()+marginInfo.getPdf()+marginInfo.getBapex()+marginInfo.getTrasmission()+marginInfo.getDwellhead()+marginInfo.getGdfund()+marginInfo.getAvalue();
			pcell = new PdfPCell(new Paragraph(taka_format.format(total_margin_payment),ReportUtil.f11B)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			double outstanding=outstanding_gtcl+outstanding_sgfl+outstanding_pdf+outstanding_Bapex+outstanding_bgfcl+outstanding_avalue+outstanding_dwellhead+outstanding_gdfund;
			pcell = new PdfPCell(new Paragraph(taka_format.format(outstanding),ReportUtil.f11B)); 
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("" ,ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			document.add(pTable);
			/* End of the table */
			
			deleteCurrentHistoty();
			insertCurrentHistory(outstanding_bgfcl,outstanding_sgfl,outstanding_pdf,outstanding_Bapex,outstanding_dwellhead,outstanding_gtcl,outstanding_gdfund,outstanding_avalue);
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	public void deleteCurrentHistoty(){
		
			try {
			//// area,month,year change kora hoiche initialization e
			String delete_sql="Delete from ACCOUNT_PAYABLE_HISTORY where month=3 and year=2016";
			PreparedStatement ps1=conn.prepareStatement(delete_sql);
			ps1.executeUpdate();
       	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ;
	}
	public void insertCurrentHistory(double outstanding_bgfcl,double outstanding_sgfl,double outstanding_pdf,double outstanding_Bapex,double outstanding_dwellhead,double outstanding_gtcl,double outstanding_gdfund,double outstanding_avalue){
		
		try {
		//// area,month,year change kora hoiche initialization e
	    String sql=" INSERT INTO ACCOUNT_PAYABLE_HISTORY(MONTH,YEAR,BGFCL,SGFL,PDF,BAPEX,GTCL,DWELLHEAD,GD_FUND,AVALUE)VALUES (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt=conn.prepareStatement(sql);
		stmt.setString(1,bill_month);
		stmt.setString(2,bill_year);
		stmt.setDouble(3,outstanding_bgfcl);
		stmt.setDouble(4,outstanding_sgfl);
		stmt.setDouble(5,outstanding_pdf);
		stmt.setDouble(6,outstanding_Bapex);
		stmt.setDouble(7,outstanding_gtcl);
		stmt.setDouble(8,outstanding_dwellhead);
		stmt.setDouble(9,outstanding_gdfund);
		stmt.setDouble(10,outstanding_avalue);
		stmt.executeUpdate();
   	
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return ;
}
	public GasPurchaseDTO getGasPurchaseRatio(){
		GasPurchaseDTO purchaseRatio=new GasPurchaseDTO();
			try {
			
			
			//// area,month,year change kora hoiche initialization e
			String gas_purchase_ratio_sql="select ROUND(((TOTAL_BGFCL/TOTAL_GTCL)),3) BGFCL_RATIO,ROUND(((TOTAL_SGFL/TOTAL_GTCL)),3) SGFL_RATIO,ROUND(((TOTAL_IOC/TOTAL_GTCL)*100),3) IOC_RATIO from GAS_PURCHASE_SUMMARY"+
							              " Where month=3"+
							              " and year=2016";
			
			PreparedStatement ps1=conn.prepareStatement(gas_purchase_ratio_sql);
        	ResultSet resultSet=ps1.executeQuery();
        	while(resultSet.next())
        	{
        		purchaseRatio.setBgfcl_ratio(resultSet.getDouble("BGFCL_RATIO"));
        		purchaseRatio.setSgfl_ratio(resultSet.getDouble("SGFL_RATIO"));
        		purchaseRatio.setIoc_ratio(resultSet.getDouble("SGFL_RATIO"));	
        	}  	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return purchaseRatio;
	}
	
	
	
	public double getTotalVatRebate(){
		double vat_rebate=0.0;
		
			try {
			
				
			//// area,month,year change kora hoiche initialization e
			String margin_info_sql="select SUM(VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT from summary_margin_pb pb,BILL_METERED bm"+
									" WHERE PB.BILL_ID=bm.BILL_ID"+
									" AND VAT_REBATE_AMOUNT<>0"+
									" AND VAT_REBATE_AMOUNT is not null"+
									" AND BILL_MONTH=4"+
									" AND BILL_YEAR=2016";
			
			PreparedStatement ps1=conn.prepareStatement(margin_info_sql);
        	ResultSet resultSet=ps1.executeQuery();
        	while(resultSet.next())
        	{
        		
        		vat_rebate=resultSet.getDouble("VAT_REBATE_AMOUNT");
        	
        	}  	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vat_rebate;
	}
	public TariffDTO getMarginInfo(String tableName){
		TariffDTO marginInfo=new TariffDTO();
			try {
			
			
			//// area,month,year change kora hoiche initialization e
			String margin_info_sql="select * from "+tableName+""+
												      " Where Month=3"+
													  " And Year=2016";
			
			PreparedStatement ps1=conn.prepareStatement(margin_info_sql);
        	ResultSet resultSet=ps1.executeQuery();
        	while(resultSet.next())
        	{
        		
        		marginInfo.setVat(resultSet.getFloat("VAT"));
        		marginInfo.setSd(resultSet.getFloat("SD"));
        		marginInfo.setPdf(resultSet.getFloat("PDF"));
        		marginInfo.setBapex(resultSet.getFloat("BAPEX"));
        		marginInfo.setWellhead(resultSet.getFloat("WELLHEAD"));
        		marginInfo.setDwellhead(resultSet.getFloat("DWELLHED"));
        		marginInfo.setTrasmission(resultSet.getFloat("TRANSMISSION"));
        		marginInfo.setGdfund(resultSet.getFloat("GD_FUND"));
        		marginInfo.setAvalue(resultSet.getFloat("ASSET_VALUE"));	
        		marginInfo.setDistribution(resultSet.getFloat("DISTRIBUTION_MARGIN"));	
        	}  	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return marginInfo;
	}
	
	public TariffDTO getPaidInfo(){
		TariffDTO paidInfo=new TariffDTO();
			try {
			
			
			//// area,month,year change kora hoiche initialization e
			String paid_info_sql="select SUM(BGFCL) BGFCL,SUM(SGFCL) SGFCL,SUM(PDF) PDF,SUM(GTCL) GTCL,SUM(DWELLHEAD) DWELLHEAD,SUM(GD_FUND) GD_FUND,SUM(BAPEX) BAPEX,SUM(AVALUE) AVALUE from " +
					"( " +
					"select  " +
					"case when PARTICULARS like '%BGFCL%' then SUM(DEBIT) else 0 end BGFCL, " +
					"case when PARTICULARS like '%SGFCL%' then SUM(DEBIT) else 0 end SGFCL, " +
					"case when PARTICULARS like '%PDF%' then SUM(DEBIT) else 0 end PDF, " +
					"case when PARTICULARS like '%GTCL%' then SUM(DEBIT) else 0 end GTCL, " +
					"case when PARTICULARS like '%DWELLHEAD%' then SUM(DEBIT) else 0 end DWELLHEAD, " +
					"case when PARTICULARS like '%Gas Development Fund%' then SUM(DEBIT) else 0 end GD_FUND, " +
					"case when PARTICULARS like '%BAPEX%' then SUM(DEBIT) else 0  end BAPEX, " +
					"case when PARTICULARS like '%Asset Value%' then SUM(DEBIT)  else 0 end AVALUE " +
					"from bank_account_ledger " +
					"Where Trans_type=6 " +
					"And to_char(trans_date,'MM')=09 " +
					"And to_char(trans_date,'YYYY')=2016 " +
					"group by PARTICULARS " +
					") " ;

			
			PreparedStatement ps1=conn.prepareStatement(paid_info_sql);
        	ResultSet resultSet=ps1.executeQuery();
        	while(resultSet.next())
        	{
        		
        		paidInfo.setBgfcl(resultSet.getFloat("BGFCL"));
        		paidInfo.setSgfl(resultSet.getFloat("SGFCL"));
        		paidInfo.setPdf(resultSet.getFloat("PDF"));
        		paidInfo.setBapex(resultSet.getFloat("BAPEX"));
        		paidInfo.setDwellhead(resultSet.getFloat("DWELLHEAD"));
        		paidInfo.setTrasmission(resultSet.getFloat("GTCL"));
        		paidInfo.setGdfund(resultSet.getFloat("GD_FUND"));
        		paidInfo.setAvalue(resultSet.getFloat("AVALUE"));
        		
        	}  	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return paidInfo;
	}
	
	public TariffDTO getAdjustmentInfo(){
		TariffDTO adjustmentInfo=new TariffDTO();
			try {
			
			
			//// area,month,year change kora hoiche initialization e
			String adjustment_info_sql="select round(BGFCL,3) BGFCL,round(SGFL,3) SGFL,round(PDF,3) PDF,round(BAPEX,3) BAPEX,round(GTCL,3) GTCL,round(DWELLHEAD,3) DWELLHEAD,round(GD_FUND,3) GD_FUND,round(AVALUE,3) AVALUE " +
					" from ADJUSTMENT_ACCOUNT_PAYABLE"+
					" where month=3"+
					" and year=2016";
					
			
			PreparedStatement ps1=conn.prepareStatement(adjustment_info_sql);
        	ResultSet resultSet=ps1.executeQuery();
        	while(resultSet.next())
        	{
        		
        		adjustmentInfo.setBgfcl(resultSet.getFloat("BGFCL"));
        		adjustmentInfo.setSgfl(resultSet.getFloat("SGFL"));
        		adjustmentInfo.setPdf(resultSet.getFloat("PDF"));
        		adjustmentInfo.setBapex(resultSet.getFloat("BAPEX"));
        		adjustmentInfo.setDwellhead(resultSet.getFloat("DWELLHEAD"));
        		adjustmentInfo.setTrasmission(resultSet.getFloat("GTCL"));
        		adjustmentInfo.setGdfund(resultSet.getFloat("GD_FUND"));
        		adjustmentInfo.setAvalue(resultSet.getFloat("AVALUE"));
        		
        	}  	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return adjustmentInfo;
	}

	public TariffDTO getPayableInfoUpToLastMonth(){
		TariffDTO adjustmentInfo=new TariffDTO();
			try {
			
			
				//// area,month,year change kora hoiche initialization e
				String adjustment_info_sql="select round(BGFCL,3) BGFCL,round(SGFL,3) SGFL,round(PDF,3) PDF,round(BAPEX,3) BAPEX,round(GTCL,3) GTCL,round(DWELLHEAD,3) DWELLHEAD,round(GD_FUND,3) GD_FUND,round(AVALUE,3) AVALUE " +
						" from ACCOUNT_PAYABLE_HISTORY"+
						" where month="+String.valueOf(Integer.valueOf(bill_month)-1)+
						" and year="+bill_year;
								
			
			PreparedStatement ps1=conn.prepareStatement(adjustment_info_sql);
        	ResultSet resultSet=ps1.executeQuery();
        	while(resultSet.next())
        	{
        		adjustmentInfo.setBgfcl(resultSet.getFloat("BGFCL"));
        		adjustmentInfo.setSgfl(resultSet.getFloat("SGFL"));
        		adjustmentInfo.setPdf(resultSet.getFloat("PDF"));
        		adjustmentInfo.setBapex(resultSet.getFloat("BAPEX"));
        		adjustmentInfo.setDwellhead(resultSet.getFloat("DWELLHEAD"));
        		adjustmentInfo.setTrasmission(resultSet.getFloat("GTCL"));
        		adjustmentInfo.setGdfund(resultSet.getFloat("GD_FUND"));
        		adjustmentInfo.setAvalue(resultSet.getFloat("AVALUE"));
        		
        	}  	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return adjustmentInfo;
	}

	
	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
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



	public String getCollection_date() {
		return collection_date;
	}

	public void setCollection_date(String collection_date) {
		this.collection_date = collection_date;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}


}

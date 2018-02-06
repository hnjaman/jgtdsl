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
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.JournalVoucherDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
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




public class JournalVoucher extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>journalList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>bgfclList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>sgflList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>journaPayablelList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>journaBG_SG = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>journaVAT = new ArrayList<JournalVoucherDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String month;
	    private  String collection_year;
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
				
		String fileName="Journal_Voucher.pdf";
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

			pcell=new PdfPCell(new Paragraph("Revenue Department : Nalka, Sirajganj", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			
			document.add(mTable);
			
			PdfPTable headline = new PdfPTable(3);
			headline.setWidths(new float[]{20,60,20});
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			headline.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Journal Voucher For Sales",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			headline.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("J.V(Rev.)No.",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			headline.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			headline.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("For the FY : "+collection_year,ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			headline.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Date-",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			headline.addCell(pcell);
			
			document.add(headline);
			
			double totalPowerBill=0.0;
			double totalCNGBill=0.0;
			double totalIndustrialBill=0.0;
			double totalCommercialBill=0.0;
			double totalDomesticBill=0.0;
			double totalCaptBill=0.0;
			double totalCaptMeterRent=0.0;
			double totalCNGMeterRent=0.0;
			double totalIndustralMeterRent=0.0;
			double totalCommercialMeterRent=0.0;
			double totalDomesticMeterRent=0.0;
			PdfPTable jvTable = new PdfPTable(7);
			jvTable.setWidthPercentage(100);
			jvTable.setWidths(new float[]{10,5,33,6,10,18,18});
			jvTable.setSpacingBefore(15f);
			
			pcell = new PdfPCell(new Paragraph("Date",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Particulars",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("L.F.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Control Code",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Debit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Credit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			journalList=getJournalVoucherDetails();
			
			pcell = new PdfPCell(new Paragraph(journalList.get(0).getDate(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("A.",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To,    Accounts Receivable(Power)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("22306",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalPowerBill=journalList.get(0).getGasBill()+journalList.get(1).getGasBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPowerBill),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To,    Accounts Receivable(CNG)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("22307",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalCNGBill=journalList.get(2).getGasBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalCNGBill),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To,    Accounts Receivable(Captive)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			totalCaptBill=journalList.get(3).getGasBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalCaptBill),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To,    Accounts Receivable(Industrial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("22303",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalIndustrialBill=journalList.get(4).getGasBill()+journalList.get(5).getGasBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalIndustrialBill),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To,    Accounts Receivable(Comercial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("22302",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalCommercialBill=journalList.get(6).getGasBill()+journalList.get(7).getGasBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalCommercialBill),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To,    Accounts Receivable(Domestic)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("22301",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalDomesticBill=journalList.get(8).getGasBill()+journalList.get(9).getGasBill()+
					journalList.get(10).getGasBill()+journalList.get(11).getGasBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalDomesticBill),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/////////////////////////////////Meter rent/////////////////////////////////////////////////////
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Meter Rent(Cap.Power)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91228",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalCaptMeterRent=journalList.get(3).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalCaptMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Meter Rent(CNG)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91229",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalCNGMeterRent=journalList.get(2).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalCNGMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Meter Rent(Industrial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91210",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalIndustralMeterRent=journalList.get(4).getMeterRent()+journalList.get(5).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalIndustralMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Meter Rent(Comercial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91220",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalCommercialMeterRent=journalList.get(6).getMeterRent()+journalList.get(7).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalCommercialMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Meter Rent(Domestic)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91250",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			totalDomesticMeterRent=journalList.get(8).getMeterRent()+journalList.get(9).getMeterRent()+
					journalList.get(10).getMeterRent()+journalList.get(11).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalDomesticMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/*----------------------------------------End of Debit Part------------------------------*/
			
			journaBG_SG=getBgfclSgfcl();
			journaVAT=getVATRebate();
			
			double bgfclAmount=journaBG_SG.get(0).getBgfcl()*journaVAT.get(0).getVat_revate();
			double sgflAmount=journaBG_SG.get(0).getSgfl()*journaVAT.get(0).getVat_revate();
			
			/*----------------------------------------BGFCL------------------------------------------*/
			bgfclList=getbgfcl();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("B.",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To BGFCL for Welhead",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34012",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(bgfclList.get(0).getBgWhelHead()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To BGFCL for SD & VAT",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34012",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(bgfclList.get(0).getBgSDVAT()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To BGFCL for VAT(REBATE)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34012",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(bgfclAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/*---------------------------------------------SGFL--------------------------------------------------*/
			
			sgflList=getSGFL();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To SGFL for SD & VAT",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34012",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(sgflList.get(0).getSgWellHead()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To SGFL for SD & VAT",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34012",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(sgflList.get(0).getSgSDVAT()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To SGFL for VAT(REBATE)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34012",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(sgflAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/*--------------------------------------All Payable------------------------------------------*/
			
			journaPayablelList=getTransPayable();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To GTCL for Transmission Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34013",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(journaPayablelList.get(0).getTransmissionMargin()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To Petrobangla for PDF Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34016",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(journaPayablelList.get(0).getPDFMargin()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To Petrobangla for BAPEX Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34014",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(journaPayablelList.get(0).getBapexMargin()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To Petrobangla for Deficit Wellhead Margin for Bapex",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34020",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(journaPayablelList.get(0).getdWhelheadMargin()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("To, Payable To Petrobangla for Gas Development Fund",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34021",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(journaPayablelList.get(0).getGdFund()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			
			
			double totalPartA=totalPowerBill+ totalCNGBill+totalIndustrialBill+ totalCommercialBill+totalDomesticBill+
							  totalCaptBill+ totalCaptMeterRent+	totalCNGMeterRent+totalIndustralMeterRent+totalCommercialMeterRent+
							  totalDomesticMeterRent;
			
			double totalPartB=bgfclList.get(0).getBgWhelHead()+bgfclList.get(0).getBgSDVAT()+bgfclAmount+
							  sgflList.get(0).getSgWellHead()+sgflList.get(0).getSgSDVAT()+	sgflAmount+
							  journaPayablelList.get(0).getTransmissionMargin()+journaPayablelList.get(0).getPDFMargin()+
							  journaPayablelList.get(0).getBapexMargin()+journaPayablelList.get(0).getdWhelheadMargin()+
							  journaPayablelList.get(0).getGdFund();
			
			
			double totalMargin=totalPartA-totalPartB;
			
			
			
			
			

			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("TO Margin of JGTDSL (Balance in Figure)(A-B)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalMargin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPartA),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPartA),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
				
			
		
			document.add(jvTable);			
		
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
				
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
		
	private ArrayList<JournalVoucherDTO>getJournalVoucherDetails(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		
		try {
			
			String[] fiscalYear=collection_year.split("-");
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			
			
			String defaulterSql="select CUSTOMER_CATEGORY, sum(GAS_BILL) GAS_BILL ,sum(METER_RENT) METER_RENT,CUSTOMER_CATEGORY_NAME " +
					"from( select BM.CUSTOMER_CATEGORY, GAS_BILL ,SM.METER_RENT,bm.CUSTOMER_CATEGORY_NAME " +
					"from SUMMARY_MARGIN_PB sm, BILL_METERED bm " +
					"where sm.bill_id=bm.bill_id " +
					"and BILL_YEAR||lpad(BILL_MONTH,2,0) between '"+firstPart+"' and '"+secondPart+"'  " +
					"union all " +
					"select CUSTOMER_CATEGORY, BILLED_AMOUNT as GAS_BILL, 0 as  METER_RENT,CUSTOMER_CATEGORY_NAME " +
					"from BILL_NON_METERED " +
					"where BILL_YEAR||lpad(BILL_MONTH,2,0) between '"+firstPart+"' and '"+secondPart+"'  " +
					") group by CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME " +
					"order by CUSTOMER_CATEGORY desc " ;





			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		
        		jDto.setCustomerCategory(resultSet.getString("CUSTOMER_CATEGORY"));
        		jDto.setGasBill(resultSet.getDouble("GAS_BILL"));
        		jDto.setMeterRent(resultSet.getDouble("METER_RENT"));
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return journalVoucherList;
	}
	private ArrayList<JournalVoucherDTO>getbgfcl(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		
		try {
			String[] fiscalYear=collection_year.split("-");
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			
			String defaulterSql="select sum(WELLHEAD) BWELLHEAD, sum(sd+vat) BSDVAT " +
					"from MARGIN_BGFCL where year||lpad(month,2,0) between '"+firstPart+"' and '"+secondPart+"' " ;





			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		
        		jDto.setBgWhelHead(resultSet.getDouble("BWELLHEAD"));
        		jDto.setBgSDVAT(resultSet.getDouble("BSDVAT"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return journalVoucherList;
	}
	private ArrayList<JournalVoucherDTO>getSGFL(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			String[] fiscalYear=collection_year.split("-");
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			
			
			String defaulterSql="select sum(WELLHEAD) SWELLHEAD, sum(sd+vat) SSDVAT " +
					"from MARGIN_SGFL where year||lpad(month,2,0) between '"+firstPart+"' and '"+secondPart+"' " ;





			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		
        		jDto.setSgWellHead(resultSet.getDouble("SWELLHEAD"));
        		jDto.setSgSDVAT(resultSet.getDouble("SSDVAT"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	private ArrayList<JournalVoucherDTO>getTransPayable(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			String[] fiscalYear=collection_year.split("-");
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			
			
			String defaulterSql="select sum(TRANSMISSION) TRANSMISSION,sum(PDF) PDF,sum(BAPEX) BAPEX,sum(DWELLHED) DWELLHED,sum(GD_FUND) GD_FUND " +
					"from( " +
					"select TRANSMISSION,PDF,BAPEX,DWELLHED,GD_FUND " +
					"from MARGIN_BGFCL where year||lpad(month,2,0) between '"+firstPart+"' and '"+secondPart+"' " +
					"union all " +
					"select TRANSMISSION,PDF,BAPEX,DWELLHED,GD_FUND " +
					"from MARGIN_SGFL where year||lpad(month,2,0) between '"+firstPart+"' and '"+secondPart+"' " +
					") " ;






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		
        		jDto.setTransmissionMargin(resultSet.getDouble("TRANSMISSION"));
        		jDto.setPDFMargin(resultSet.getDouble("PDF"));
        		jDto.setBapexMargin(resultSet.getDouble("BAPEX"));
        		jDto.setdWhelheadMargin(resultSet.getDouble("DWELLHED"));
        		jDto.setGdFund(resultSet.getDouble("GD_FUND"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return journalVoucherList;
	}
	
	private ArrayList<JournalVoucherDTO>getBgfclSgfcl(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			
			String[] fiscalYear=collection_year.split("-");
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			
			String defaulterSql="select sum(TOTAL_BGFCL)/sum(TOTAL_GTCL) BGFCL , sum(TOTAL_SGFL)/sum(TOTAL_GTCL) SGFL " +
					"from GAS_PURCHASE_SUMMARY where year||lpad(month,2,0) between '"+firstPart+"' and '"+secondPart+"' " ;






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		
        		jDto.setBgfcl(resultSet.getDouble("BGFCL"));
        		jDto.setSgfl(resultSet.getDouble("SGFL"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	private ArrayList<JournalVoucherDTO>getVATRebate(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			String[] fiscalYear=collection_year.split("-");
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			
			
			String defaulterSql="select sum(VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT " +
					"from SUMMARY_MARGIN_PB where bill_id in( " +
					"select bill_id from BILL_METERED  " +
					"where BILL_YEAR||lpad(BILL_MONTH,2,0) between '"+firstPart+"' and '"+secondPart+"' ) " ;






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		
        		jDto.setVat_revate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	
	

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	public String getMonth() {
		return month;
	}



	public void setMonth(String month) {
		this.month = month;
	}



	public String getCollection_year() {
		return collection_year;
	}

	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	
  }



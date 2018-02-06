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

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class MarginCalculationReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<TariffDTO> tarrifMargin=new ArrayList<TariffDTO>();
	GasPurchaseDTO monthlyGasPurchase=new GasPurchaseDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area="01";
	    private  String customer_category;
	    private  String bill_month="01";
	    private  String bill_year="2016";
	    private  String report_for; 
	    private  String category_name;
		private PdfPCell pcell;
		private PdfPTable ptable;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");

	public String execute() throws Exception
	{
				
		String fileName="Various_Margin_Report.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		//DecimalFormat consumption_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		//DecimalFormat factor_format=new DecimalFormat("##########0.000");
		
		
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
			
			
			
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{30,50,30});
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("SATEMENT SHOWING THE CALCULATION OF VARIOUS MARGIN FOR THE MONTH "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,ReportUtil.f11B));
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
			
			monthlyGasPurchase=getMonthlyGasPurchaseInformation();
			tarrifMargin=getTarrifMargin();
			
			
			
			
			PdfPTable variousMargintable = new PdfPTable(15);
			variousMargintable.setWidthPercentage(100);
			variousMargintable.setWidths(new float[]{2,6,7,4,7,7,7,7,7,7,7,8,8,8,8});		
			
			pcell=new PdfPCell(new Paragraph("SL No",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Consumption SCM",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("End User Price SCM",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("VAT",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("SD",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("PB's Margin @ 45% On End User Price",font3));
			pcell.setColspan(8);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Taka",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("GOB's Margin @ 55% on end user price",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("PDF Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("BAPEX Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Wellhead Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Dif. Wellhead Margin for BAPEX",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Transmission Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Development Fund",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Asset Value of Gas",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Distribution Margin",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			//--------Power----------//////////////////////////
			
			pcell = new PdfPCell(new Paragraph("01",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Power",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getVat()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getSd()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getPdf()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getBapex()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getWellhead()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getDwellhead()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getTrasmission()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getGdfund()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getAvalue()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getDistribution()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(0).getPrice()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_power()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getPrice()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_power()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getPrice()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_power()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getPrice()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			/////////-----Captive Power-------////////////////
			
			pcell = new PdfPCell(new Paragraph("02",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cap. Power",font3));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getVat()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getSd()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getPdf()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getBapex()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getWellhead()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getDwellhead()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getTrasmission()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getGdfund()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getAvalue()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getDistribution()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getPrice()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cap_bgfcl",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_VAT_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_SD_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_PDF_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_BAPEX_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Wellhead_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Dif.Wellhead_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Transmission_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_GDFUND_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_AssetVGas_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Distribution_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Total_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SGFL_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_VAT_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_SD_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_PDF_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_BAPEX_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Wellhead_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_DWelhead_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Transmission_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_GDFUND_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_AValue_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Distribution_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Total_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IOC",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_VAT_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_SD_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_PDF_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_BAPEX_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Wellhead_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_DWelhead_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Transmission_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_GDFUND_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_AValue_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Distribution_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Total_Cap",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----CNG-----------------////////////////
			
			pcell = new PdfPCell(new Paragraph("03",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("CNG",font3));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getVat()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getSd()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getPdf()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getBapex()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getWellhead()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getDwellhead()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getTrasmission()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getGdfund()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getAvalue()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getDistribution()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getPrice()),font2));
			pcell.setFixedHeight(2);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgfcl_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_VAT_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_SD_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_PDF_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_BAPEX_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Wellhead_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Dif.Wellhead_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Transmission_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_GDFUND_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_AssetVGas_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Distribution_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Total_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SGFL_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_VAT_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgflCNGD_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_PDF_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_BAPEX_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Wellhead_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_DWelhead_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Transmission_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_GDFUND_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_AValue_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Distribution_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Total_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IOC_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_VAT_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_SD_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_PDF_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_BAPEX_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Wellhead_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_DWelhead_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Transmission_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_GDFUND_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_AValue_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Distribution_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Total_CNG",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----Industrial----------////////////////
			
			pcell = new PdfPCell(new Paragraph("04",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Industrial",font3));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_VAT_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_SD_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_PDF_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_BAPEX_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_Wellhead_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_Dif.Wellhead_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_Transmission_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_GDFUND_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_AssetVGas_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_Distribution_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IND_Total_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgfcl_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_VAT_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_SD_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_PDF_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_BAPEX_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Wellhead_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Dif.Wellhead_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Transmission_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_GDFUND_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_AssetVGas_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Distribution_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Total_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SGFL",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_VAT_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_SD_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_PDF_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_BAPEX_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Wellhead_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_DWelhead_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Transmission_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_GDFUND_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_AValue_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Distribution_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Total_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IOC",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_VAT_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_SD_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_PDF_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_BAPEX_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Wellhead_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_DWelhead_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Transmission_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_GDFUND_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_AValue_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Distribution_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Total_IND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----Commercial----------////////////////
			
			pcell = new PdfPCell(new Paragraph("05",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commercial",font3));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_VAT_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COMSD_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_PDF_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_BAPEX_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_Wellhead_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_Dif.Wellhead_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_Transmission_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_GDFUND_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_AssetVGas_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_Distribution_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("COM_Total_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgfcl_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_VAT_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_SD_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_PDF_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_BAPEX_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Wellhead_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Dif.Wellhead_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Transmission_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_GDFUND_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_AssetVGas_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Distribution_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Total_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SGFL",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_VAT_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_SD_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_PDF_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_BAPEX_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Wellhead_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_DWelhead_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Transmission_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_GDFUND_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_AValue_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Distribution_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Total_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IOC",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_VAT_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_SD_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_PDF_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_BAPEX_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Wellhead_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_DWelhead_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Transmission_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_GDFUND_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_AValue_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Distribution_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Total_COM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			/////////-----Domestic------------////////////////
			
			pcell = new PdfPCell(new Paragraph("06",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Domestic",font3));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_VAT_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_SD_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_PDF_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_BAPEX_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_Wellhead_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_Dif.Wellhead_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_Transmission_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_GDFUND_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_AssetVGas_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_Distribution_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("DOM_Total_VAT",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgfcl_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_VAT_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_SD_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_PDF_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_BAPEX_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Wellhead_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Dif.Wellhead_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Transmission_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_GDFUND_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_AssetVGas_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Distribution_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("bgcl_Total_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SGFL",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_VAT_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_SD_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_PDF_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_BAPEX_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Wellhead_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_DWelhead_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Transmission_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_GDFUND_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_AValue_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Distribution_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("sgfl_Total_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("IOC",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_VAT_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_SD_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_PDF_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_BAPEX_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Wellhead_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_DWelhead_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Transmission_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_GDFUND_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_AValue_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Distribution_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ioc_Total_DOM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			//////////////////------Total BGFCL---------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_SCM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_VAT",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_SD",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_PDF",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_BAPEX",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_WellHead",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_DWhellhead",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_Transmission",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_GDFUND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_AssetVGAS",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_Distribution",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL_Total",font2));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			
			//////////////////------Total SGFL----------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total SGFL=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_SCM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_VAT",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_SD",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_PDF",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_BAPEX",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_WellHead",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_DWhellhead",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_Transmission",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_GDFUND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_AssetVGAS",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total SGFL_Distribution",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			
			//////////////////------Grand Total---------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Grand Total=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_SCM",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_VAT",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_SD",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_PDF",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_BAPEX",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_WellHead",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_DWhellhead",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_Transmission",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_GDFUND",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_AssetVGAS",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Grand_Total_Distribution",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			//////////////////------Total Bill of BGFCL/SGFL-//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total Bill of BGFCL/SGFL=",font2));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Bill_VAT",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Bill_SD",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(9);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			document.add(variousMargintable);
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	private ArrayList<TariffDTO> getTarrifMargin()
	{
		
		ArrayList<TariffDTO> tariffMarginList=new ArrayList<TariffDTO>();
	
		
		try {
			
			
			//// area,month,year change kora hoiche initialization e
			String margin_calculation_sql="select MT.*,SUBSTR (CATEGORY_NAME, 0, INSTR (CATEGORY_NAME, '(') - 1) CATEGORY_NAME from MST_TARIFF MT,MST_CUSTOMER_CATEGORY mcc " +
					"where " +
					"MT.CUSTOMER_CATEGORY_ID=MCC.CATEGORY_ID " +
					"and  " +
					"Effective_From<=to_date('01-"+bill_month+"-"+bill_year+"','dd-MM-YYYY HH24:MI:SS') " +
					"And (Effective_To is Null or Effective_To>=to_date('01-"+bill_month+"-"+bill_year+"','dd-MM-YYYY HH24:MI:SS')) " +
					"and CUSTOMER_CATEGORY_ID in(01,03,05,07,09,11) " +
					"and meter_status=1 " +
					"order by CUSTOMER_CATEGORY_ID desc " ;





			
			PreparedStatement ps1=conn.prepareStatement(margin_calculation_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TariffDTO tariffMargin=new TariffDTO();
        		
        		tariffMargin.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		tariffMargin.setPrice(resultSet.getFloat("PRICE"));
        		tariffMargin.setPb(resultSet.getFloat("PB"));
        		tariffMargin.setVat(resultSet.getFloat("VAT"));
        		tariffMargin.setSd(resultSet.getFloat("SD"));
        		tariffMargin.setPdf(resultSet.getFloat("PDF"));
        		tariffMargin.setBapex(resultSet.getFloat("BAPEX"));
        		tariffMargin.setWellhead(resultSet.getFloat("WELLHEAD"));
        		tariffMargin.setDwellhead(resultSet.getFloat("DWELLHEAD"));
        		tariffMargin.setTrasmission(resultSet.getFloat("TRNSMISSION"));
        		tariffMargin.setGdfund(resultSet.getFloat("GDFUND"));
        		tariffMargin.setDistribution(resultSet.getFloat("DISTRIBUTION"));
        		tariffMargin.setAvalue(resultSet.getFloat("AVALUE"));    		
        		tariffMarginList.add(tariffMargin);
        		
        	}
            
        	
        	
        	
        	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tariffMarginList;
	}
	@SuppressWarnings("unused")
	private GasPurchaseDTO getMonthlyGasPurchaseInformation()
	{
		GasPurchaseDTO gasPurchase = new GasPurchaseDTO();
	
		
	
		
		try {
			
			
			
 			String monthly_gas_purchase="select bgfcl.MONTH, bgfcl.YEAR,TOTAL_BGFCL, TOTAL_SGFL, TOTAL_IOC, TOTAL_GTCL, " +
					"bgfcl.PW_GVT, bgfcl.PW_PVT, bgfcl.CAP_GVT,  " +
					"bgfcl.CAP_PVT, bgfcl.CNG_GVT, bgfcl.CNG_PVT,  " +
					"bgfcl.IND_GVT, bgfcl.IND_PVT, bgfcl.COMM_GVT,  " +
					"bgfcl.COMM_PVT, bgfcl.DOM_M_GVT, bgfcl.DOM_M_PVT,  " +
					"bgfcl.DOM_NM_GVT, bgfcl.DOM_NM_PVT, bgfcl.FERTILIZER_GVT,  " +
					"bgfcl.FERTILIZER_PVT, bgfcl.TEA_GVT, bgfcl.TEA_PVT, " +
					"ioc.PW_GVT PW_GVT_IOC, ioc.PW_PVT PW_PVT_IOC, ioc.CAP_GVT CAP_GVT_IOC,  " +
					"ioc.CAP_PVT CAP_PVT_IOC, ioc.CNG_GVT CNG_GVT, ioc.CNG_PVT CNG_PVT_IOC,  " +
					"ioc.IND_GVT IND_GVT_IOC, ioc.IND_PVT IND_PVT_IOC, ioc.COMM_GVT COMM_GVT_IOC,   " +
					"ioc.COMM_PVT COMM_PVT_IOC, ioc.DOM_M_GVT DOM_M_GVT_IOC, ioc.DOM_M_PVT DOM_M_PVT_IOC,  " +
					"ioc.DOM_NM_GVT DOM_NM_GVT_IOC, ioc.DOM_NM_PVT DOM_NM_PVT_IOC, ioc.FERTILIZER_GVT FERTILIZER_GVT_IOC,  " +
					"ioc.FERTILIZER_PVT FERTILIZER_PVT_IOC, ioc.TEA_GVT TEA_GVT_IOC, ioc.TEA_PVT TEA_PVT_IOC, " +
					"sgfl.PW_GVT PW_GVT_sgfl, sgfl.PW_GVT PW_PVT_sgfl, sgfl.CAP_GVT CAP_GVT_sgfl,  " +
					"sgfl.CAP_PVT CAP_PVT_sgfl, sgfl.CNG_GVT CNG_GVT, sgfl.CNG_PVT CNG_PVT_sgfl,  " +
					"sgfl.IND_GVT IND_GVT_sgfl, sgfl.IND_PVT IND_PVT_sgfl, sgfl.COMM_GVT COMM_GVT_sgfl,   " +
					"sgfl.COMM_PVT COMM_PVT_sgfl, sgfl.DOM_M_GVT DOM_M_GVT_sgfl, sgfl.DOM_M_PVT DOM_M_PVT_sgfl,  " +
					"sgfl.DOM_NM_GVT DOM_NM_GVT_sgfl, sgfl.DOM_NM_PVT DOM_NM_PVT_sgfl, sgfl.FERTILIZER_GVT FERTILIZER_GVT_sgfl,  " +
					"sgfl.FERTILIZER_PVT FERTILIZER_PVT_sgfl, sgfl.TEA_GVT TEA_GVT_sgfl, sgfl.TEA_PVT TEA_PVT_sgfl " +
					"from GAS_PURCHASE_SUMMARY gps,GAS_PURCHASE_BGFCL bgfcl,GAS_PURCHASE_IOC ioc,GAS_PURCHASE_SGFL sgfl " +
					"where gps.pid=bgfcl.pid " +
					"and gps.pid=ioc.pid " +
					"and gps.pid=sgfl.pid " ;






			
			PreparedStatement ps1=conn.prepareStatement(monthly_gas_purchase);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		gasPurchase.setMonth(resultSet.getString("MONTH"));
        		gasPurchase.setYear(resultSet.getString("YEAR"));
        		gasPurchase.setTotal_bgfcl(resultSet.getDouble("TOTAL_BGFCL"));
        		gasPurchase.setTotal_sgfl(resultSet.getDouble("TOTAL_SGFL"));
        		gasPurchase.setTotal_ioc(resultSet.getDouble("TOTAL_IOC"));
        		gasPurchase.setTotal_gtcl(resultSet.getDouble("TOTAL_GTCL"));
        		gasPurchase.setBgfcl_power_gvt(resultSet.getDouble("PW_GVT"));
        		gasPurchase.setBgfcl_power_pvt(resultSet.getDouble("PW_PVT"));
        		gasPurchase.setBgfcl_captive_gvt(resultSet.getDouble("CAP_GVT"));
        		gasPurchase.setBgfcl_captive_pvt(resultSet.getDouble("CAP_PVT"));
        		gasPurchase.setBgfcl_cng_gvt(resultSet.getDouble("CAP_GVT"));
        		gasPurchase.setBgfcl_cng_pvt(resultSet.getDouble("CAP_PVT"));
        		gasPurchase.setBgfcl_industry_gvt(resultSet.getDouble("IND_GVT"));
        		gasPurchase.setBgfcl_industry_pvt(resultSet.getDouble("IND_PVT"));
        		gasPurchase.setBgfcl_comm_gvt(resultSet.getDouble("COMM_GVT"));
        		gasPurchase.setBgfcl_comm_pvt(resultSet.getDouble("COMM_PVT"));
        		gasPurchase.setBgfcl_dom_meter_gvt(resultSet.getDouble("DOM_M_GVT"));
        		gasPurchase.setBgfcl_dom_meter_pvt(resultSet.getDouble("DOM_M_PVT"));
        		gasPurchase.setBgfcl_dom_nmeter_gvt(resultSet.getDouble("DOM_NM_GVT"));
        		gasPurchase.setBgfcl_dom_nmeter_pvt(resultSet.getDouble("DOM_NM_PVT"));
        		gasPurchase.setBgfcl_fertilizer_gvt(resultSet.getDouble("FERTILIZER_GVT"));
        		gasPurchase.setBgfcl_fertilizer_pvt(resultSet.getDouble("FERTILIZER_PVT"));
        		gasPurchase.setBgfcl_tea_gvt(resultSet.getDouble("TEA_GVT"));
        		gasPurchase.setBgfcl_tea_pvt(resultSet.getDouble("TEA_PVT"));
        		gasPurchase.setBgfcl_power(resultSet.getDouble("PW_GVT")+resultSet.getDouble("PW_PVT"));
        		gasPurchase.setBgfcl_captive(resultSet.getDouble("CAP_GVT")+resultSet.getDouble("CAP_PVT"));
        		gasPurchase.setBgfcl_cng(resultSet.getDouble("CAP_GVT")+resultSet.getDouble("CAP_PVT"));
        		gasPurchase.setBgfcl_industrial(resultSet.getDouble("IND_GVT")+resultSet.getDouble("IND_GVT"));
        		gasPurchase.setBgfcl_commercial(resultSet.getDouble("COMM_GVT")+resultSet.getDouble("COMM_PVT"));
        		gasPurchase.setBgfcl_Domestic(resultSet.getDouble("DOM_M_GVT")+resultSet.getDouble("DOM_M_PVT")+resultSet.getDouble("DOM_NM_GVT")+resultSet.getDouble("DOM_NM_PVT"));
        		gasPurchase.setBgfcl_fertilizer(resultSet.getDouble("FERTILIZER_GVT")+resultSet.getDouble("FERTILIZER_PVT"));
        		gasPurchase.setBgfcl_tea(resultSet.getDouble("TEA_GVT")+resultSet.getDouble("TEA_GVT"));
        		
        		
           		gasPurchase.setIoc_power_gvt(resultSet.getDouble("PW_GVT_IOC"));
        		gasPurchase.setIoc_power_pvt(resultSet.getDouble("PW_PVT_IOC"));
        		gasPurchase.setIoc_captive_gvt(resultSet.getDouble("CAP_GVT_IOC"));
        		gasPurchase.setIoc_captive_pvt(resultSet.getDouble("CAP_PVT_IOC"));
        		gasPurchase.setIoc_cng_gvt(resultSet.getDouble("CAP_GVT_IOC"));
        		gasPurchase.setIoc_cng_pvt(resultSet.getDouble("CAP_PVT_IOC"));
        		gasPurchase.setIoc_industry_gvt(resultSet.getDouble("IND_GVT_IOC"));
        		gasPurchase.setIoc_industry_pvt(resultSet.getDouble("IND_PVT_IOC"));
        		gasPurchase.setIoc_comm_gvt(resultSet.getDouble("COMM_GVT_IOC"));
        		gasPurchase.setIoc_comm_pvt(resultSet.getDouble("COMM_PVT_IOC"));
        		gasPurchase.setIoc_dom_meter_gvt(resultSet.getDouble("DOM_M_GVT_IOC"));
        		gasPurchase.setIoc_dom_meter_pvt(resultSet.getDouble("DOM_M_PVT_IOC"));
        		gasPurchase.setIoc_dom_nmeter_gvt(resultSet.getDouble("DOM_NM_GVT_IOC"));
        		gasPurchase.setIoc_dom_nmeter_pvt(resultSet.getDouble("DOM_NM_PVT_IOC"));
        		gasPurchase.setIoc_fertilizer_gvt(resultSet.getDouble("FERTILIZER_GVT_IOC"));
        		gasPurchase.setIoc_fertilizer_pvt(resultSet.getDouble("FERTILIZER_PVT_IOC"));
        		gasPurchase.setIoc_tea_gvt(resultSet.getDouble("TEA_GVT_IOC"));
        		gasPurchase.setIoc_tea_pvt(resultSet.getDouble("TEA_PVT_IOC"));
        		gasPurchase.setIoc_tea_gvt(resultSet.getDouble("TEA_GVT"));
        		gasPurchase.setIoc_tea_pvt(resultSet.getDouble("TEA_PVT"));
        		gasPurchase.setIoc_power(resultSet.getDouble("PW_GVT_IOC")+resultSet.getDouble("PW_PVT_IOC"));
        		gasPurchase.setIoc_captive(resultSet.getDouble("CAP_GVT_IOC")+resultSet.getDouble("CAP_PVT_IOC"));
        		gasPurchase.setIoc_cng(resultSet.getDouble("CAP_GVT_IOC")+resultSet.getDouble("CAP_PVT_IOC"));
        		gasPurchase.setIoc_industrial(resultSet.getDouble("IND_GVT_IOC")+resultSet.getDouble("IND_GVT_IOC"));
        		gasPurchase.setIoc_commercial(resultSet.getDouble("COMM_GVT_IOC")+resultSet.getDouble("COMM_PVT_IOC"));
        		gasPurchase.setIoc_Domestic(resultSet.getDouble("DOM_M_GVT_IOC")+resultSet.getDouble("DOM_M_PVT_IOC")+resultSet.getDouble("DOM_NM_GVT_IOC")+resultSet.getDouble("DOM_NM_PVT_IOC"));
        		gasPurchase.setIoc_fertilizer(resultSet.getDouble("FERTILIZER_GVT_IOC")+resultSet.getDouble("FERTILIZER_PVT_IOC"));
        		gasPurchase.setIoc_tea(resultSet.getDouble("TEA_GVT_IOC")+resultSet.getDouble("TEA_GVT_IOC"));
        		
        		gasPurchase.setSgfl_power_gvt(resultSet.getDouble("PW_GVT_SGFL"));
        		gasPurchase.setSgfl_power_pvt(resultSet.getDouble("PW_PVT_SGFL"));
        		gasPurchase.setSgfl_captive_gvt(resultSet.getDouble("CAP_GVT_SGFL"));
        		gasPurchase.setSgfl_captive_pvt(resultSet.getDouble("CAP_PVT_SGFL"));
        		gasPurchase.setSgfl_cng_gvt(resultSet.getDouble("CAP_GVT_SGFL"));
        		gasPurchase.setSgfl_cng_pvt(resultSet.getDouble("CAP_PVT_SGFL"));
        		gasPurchase.setSgfl_industry_gvt(resultSet.getDouble("IND_GVT_SGFL"));
        		gasPurchase.setSgfl_industry_pvt(resultSet.getDouble("IND_PVT_SGFL"));
        		gasPurchase.setSgfl_comm_gvt(resultSet.getDouble("COMM_GVT_SGFL"));
        		gasPurchase.setSgfl_comm_pvt(resultSet.getDouble("COMM_PVT_SGFL"));
        		gasPurchase.setSgfl_dom_meter_gvt(resultSet.getDouble("DOM_M_GVT_SGFL"));
        		gasPurchase.setSgfl_dom_meter_pvt(resultSet.getDouble("DOM_M_PVT_SGFL"));
        		gasPurchase.setSgfl_dom_nmeter_gvt(resultSet.getDouble("DOM_NM_GVT_SGFL"));
        		gasPurchase.setSgfl_dom_nmeter_pvt(resultSet.getDouble("DOM_NM_PVT_SGFL"));
        		gasPurchase.setSgfl_fertilizer_gvt(resultSet.getDouble("FERTILIZER_GVT_SGFL"));
        		gasPurchase.setSgfl_fertilizer_pvt(resultSet.getDouble("FERTILIZER_PVT_SGFL"));
        		gasPurchase.setSgfl_tea_gvt(resultSet.getDouble("TEA_GVT_SGFL"));
        		gasPurchase.setSgfl_tea_pvt(resultSet.getDouble("TEA_PVT_SGFL"));
        		gasPurchase.setSgfl_power(resultSet.getDouble("PW_GVT_SGFL")+resultSet.getDouble("PW_PVT_SGFL"));
        		gasPurchase.setSgfl_captive(resultSet.getDouble("CAP_GVT_SGFL")+resultSet.getDouble("CAP_PVT_SGFL"));
        		gasPurchase.setSgfl_cng(resultSet.getDouble("CAP_GVT_SGFL")+resultSet.getDouble("CAP_PVT_SGFL"));
        		gasPurchase.setSgfl_industrial(resultSet.getDouble("IND_GVT_SGFL")+resultSet.getDouble("IND_GVT_SGFL"));
        		gasPurchase.setSgfl_commercial(resultSet.getDouble("COMM_GVT_SGFL")+resultSet.getDouble("COMM_PVT_SGFL"));
        		gasPurchase.setSgfl_Domestic(resultSet.getDouble("DOM_M_GVT_SGFL")+resultSet.getDouble("DOM_M_PVT_SGFL")+resultSet.getDouble("DOM_NM_GVT_SGFL")+resultSet.getDouble("DOM_NM_PVT_SGFL"));
        		gasPurchase.setSgfl_fertilizer(resultSet.getDouble("FERTILIZER_GVT_SGFL")+resultSet.getDouble("FERTILIZER_PVT_SGFL"));
        		gasPurchase.setSgfl_tea(resultSet.getDouble("TEA_GVT_SGFL")+resultSet.getDouble("TEA_GVT_SGFL"));
        	
        	}
      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gasPurchase;
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
	
	


	
  }



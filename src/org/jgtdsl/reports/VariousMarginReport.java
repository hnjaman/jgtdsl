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

import org.apache.poi.ss.formula.ptg.PowerPtg;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.cache.CacheUtil;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

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




public class VariousMarginReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<TariffDTO> tarrifMargin=new ArrayList<TariffDTO>();
	GasPurchaseDTO monthlyGasPurchase=new GasPurchaseDTO();
	GasPurchaseDTO monthlyGasPurchaseNew= new GasPurchaseDTO();
	public  ServletContext servlet;
	HashMap<String, Double> monthlySalesInfo=new HashMap<String, Double>();
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area="01";
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String category_name;
		private PdfPCell pcell;
		private PdfPTable ptable;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		static DecimalFormat margin_format = new DecimalFormat("#############0.0000000000000000000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		

	public String execute() throws Exception
	{
				
		String fileName="Various_Margin_Report.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		//DecimalFormat consumption_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.00000000");
		//DecimalFormat factor_format=new DecimalFormat("##########0.000");
		
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			//MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				40,120,40
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE :",ReportUtil.f8B);
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
			
			monthlySalesInfo=getMonthlySalesInformation();
			monthlyGasPurchase=getMonthlyGasPurchaseInformation();
			tarrifMargin=getTarrifMargin();
			
			
			
			double total_bgfcl_scm=0.0;
			double total_bgfcl_vat=0.0;
			double total_bgfcl_sd=0.0;
			double total_bgfcl_pdf=0.0;
			double total_bgfcl_bapex=0.0;
			double total_bgfcl_wellhead=0.0;
			double total_bgfcl_dwelhead=0.0;
			double total_bgfcl_transmission=0.0;
			double total_bgfcl_gdfund=0.0;
			double total_bgfcl_Avalue=0.0;
			double total_bgfcl_distribution=0.0;
			
			double total_sgfl_scm=0.0; 
			double total_sgfl_vat=0.0;
			double total_sgfl_sd=0.0;
			double total_sgfl_pdf=0.0;
			double total_sgfl_bapex=0.0;
			double total_sgfl_wellhead=0.0;
			double total_sgfl_dwelhead=0.0;
			double total_sgfl_transmission=0.0;
			double total_sgfl_gdfund=0.0;
			double total_sgfl_Avalue=0.0;
			double total_sgfl_distribution=0.0;
			
			double grand_total_scm=0.0; 
			double grand_total_vat=0.0;
			double grand_total_sd=0.0;
			double grand_total_pdf=0.0;
			double grand_total_bapex=0.0;
			double grand_total_wellhead=0.0;
			double grand_total_dwelhead=0.0;
			double grand_total_transmission=0.0;
			double grand_total_gdfund=0.0;
			double grand_total_Avalue=0.0;
			double grand_total_distribution=0.0;
			
			double total_bill_vat=0.0;
			double total_bill_sd=0.0;
			double total_taka=0.0;
			
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
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
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
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
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
			
			
			double sys_gain_pow=(monthlySalesInfo.get("pow_gvt")+monthlySalesInfo.get("pow_pvt"))-monthlyGasPurchase.getTotal_power();
			double distribution_pow=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getDistribution())+sys_gain_pow*tarrifMargin.get(0).getPrice();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_pow),font2));
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
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			/////////-----Captive Power-------////////////////
			
			pcell = new PdfPCell(new Paragraph("02",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cap. Power",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(2).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
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
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_captive()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			double sys_gain_cap=(monthlySalesInfo.get("cap_gvt")+monthlySalesInfo.get("cap_pvt"))-monthlyGasPurchase.getTotal_captive();
			double distribution_cap=(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getDistribution())+sys_gain_cap*tarrifMargin.get(2).getPrice();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_cap),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_captive()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_captive()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----CNG-----------------////////////////
			
			pcell = new PdfPCell(new Paragraph("03",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("CNG",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(1).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
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
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_cng()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			
			double sys_gain_cng=(monthlySalesInfo.get("cng_gvt")+monthlySalesInfo.get("cng_pvt"))-monthlyGasPurchase.getTotal_cng();
			double distribution_cng=(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getDistribution())+sys_gain_cng*tarrifMargin.get(1).getPrice();
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_cng),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_cng()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
		
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_cng()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----Industrial----------////////////////
			
			pcell = new PdfPCell(new Paragraph("04",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Industrial",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(3).getPrice()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_industrial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			double sys_gain_ind=(monthlySalesInfo.get("ind_gvt")+monthlySalesInfo.get("ind_pvt"))-monthlyGasPurchase.getTotal_industrial();
			double distribution_ind=(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getDistribution())+sys_gain_ind*tarrifMargin.get(3).getPrice();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_ind),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_industrial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_industrial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			
			/////////-----Commercial----------////////////////
			
			pcell = new PdfPCell(new Paragraph("05",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commercial",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(4).getPrice()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_commercial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			
			double sys_gain_comm=(monthlySalesInfo.get("comm_gvt")+monthlySalesInfo.get("comm_pvt"))-monthlyGasPurchase.getTotal_commercial();
			double distribution_comm=(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getDistribution())+sys_gain_comm*tarrifMargin.get(4).getPrice();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_comm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_commercial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_commercial()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			/////////-----Domestic------------////////////////
			
			pcell = new PdfPCell(new Paragraph("06",font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Domestic",font3));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getPrice()),font2));
			pcell.setRowspan(4);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(tarrifMargin.get(5).getPrice()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_Domestic()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			double sys_gain_dommestic=(monthlySalesInfo.get("dom_m_gvt")+monthlySalesInfo.get("dom_m_pvt")+monthlySalesInfo.get("dom_nm_gvt")+monthlySalesInfo.get("dom_nm_pvt"))-monthlyGasPurchase.getTotal_domestic();
			double distribution_dommestic=(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getDistribution())+sys_gain_dommestic*tarrifMargin.get(5).getPrice();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distribution_dommestic),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_Domestic()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			pcell.setBorderWidthBottom(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_Domestic()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getVat()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getSd()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getPdf()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getBapex()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getWellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getDwellhead()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getTrasmission()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getGdfund()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getAvalue()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getDistribution()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getPrice()),ReportUtil.f8B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidthTop(0);
			variousMargintable.addCell(pcell);
			
			//////////////////------Total BGFCL---------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total BGFCL=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_scm= monthlyGasPurchase.getBgfcl_power()+monthlyGasPurchase.getBgfcl_captive()+monthlyGasPurchase.getBgfcl_cng()+monthlyGasPurchase.getBgfcl_industrial()+monthlyGasPurchase.getBgfcl_commercial()+monthlyGasPurchase.getBgfcl_Domestic();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_scm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_vat=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getVat())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getVat())	+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getVat())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getVat())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getVat())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getVat());			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_vat),font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_bgfcl_sd=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getSd())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getSd())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getSd())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getSd())	+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getSd())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getSd());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_sd),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_bgfcl_pdf=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getPdf())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getPdf())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getPdf())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getPdf())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getPdf())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getPdf());
			
			
			System.out.println("BGFCL PDF : "+consumption_format.format(total_bgfcl_pdf));
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_pdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_bapex=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getBapex())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getBapex())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getBapex())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getBapex())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getBapex())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getBapex());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_bapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_wellhead=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getWellhead())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getWellhead())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getWellhead())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getWellhead())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getWellhead())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getWellhead());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_wellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_dwelhead=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getDwellhead())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getDwellhead())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getDwellhead())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getDwellhead())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getDwellhead())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getDwellhead());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_dwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_transmission=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getTrasmission())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getTrasmission())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getTrasmission())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getTrasmission())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getTrasmission())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getTrasmission());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_transmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_gdfund=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getGdfund())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getGdfund())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getGdfund())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getGdfund())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getGdfund())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getGdfund());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_gdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_Avalue=(monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getAvalue())+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getAvalue())+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getAvalue())+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getAvalue())+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getAvalue())+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getAvalue());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_Avalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bgfcl_distribution=distribution_pow+distribution_cap+distribution_cng+distribution_comm+distribution_ind+distribution_dommestic;
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bgfcl_distribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_taka = (monthlyGasPurchase.getBgfcl_power()*tarrifMargin.get(0).getPrice())+(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getPrice())+(monthlyGasPurchase.getIoc_power()*tarrifMargin.get(0).getPrice())
					+(monthlyGasPurchase.getBgfcl_captive()*tarrifMargin.get(2).getPrice())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getPrice())+(monthlyGasPurchase.getIoc_captive()*tarrifMargin.get(2).getPrice())
					+(monthlyGasPurchase.getBgfcl_cng()*tarrifMargin.get(1).getPrice())+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getPrice())+(monthlyGasPurchase.getIoc_cng()*tarrifMargin.get(1).getPrice())
					+(monthlyGasPurchase.getBgfcl_industrial()*tarrifMargin.get(3).getPrice())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getPrice())+(monthlyGasPurchase.getIoc_industrial()*tarrifMargin.get(3).getPrice())
					+(monthlyGasPurchase.getBgfcl_commercial()*tarrifMargin.get(4).getPrice())+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getPrice())+(monthlyGasPurchase.getIoc_commercial()*tarrifMargin.get(4).getPrice())
					+(monthlyGasPurchase.getBgfcl_Domestic()*tarrifMargin.get(5).getPrice())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getPrice())+(monthlyGasPurchase.getIoc_Domestic()*tarrifMargin.get(5).getPrice());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_taka),ReportUtil.f9B));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			insertMarginDate("MARGIN_BGFCL",total_bgfcl_vat,total_bgfcl_sd,total_bgfcl_pdf,total_bgfcl_bapex,total_bgfcl_wellhead,total_bgfcl_dwelhead,total_bgfcl_transmission,total_bgfcl_gdfund,total_bgfcl_Avalue,total_bgfcl_distribution);
			
			
			/*---------------------------------------------------------------------------For Testing Purpose Code of PDF Margin-------------------------------------------------------------------------*/
			
			String powerConsump=margin_format.format(monthlyGasPurchase.getBgfcl_power_pvt()+monthlyGasPurchase.getBgfcl_power_gvt());
			String cngConsump=margin_format.format(monthlyGasPurchase.getBgfcl_cng_pvt()+monthlyGasPurchase.getBgfcl_cng_gvt());
			String captiveConsum=margin_format.format(monthlyGasPurchase.getBgfcl_captive_pvt()+monthlyGasPurchase.getBgfcl_captive_gvt());
			String indusConsump=margin_format.format(monthlyGasPurchase.getBgfcl_industry_pvt()+monthlyGasPurchase.getBgfcl_industry_gvt());
			String commConsump=margin_format.format(monthlyGasPurchase.getBgfcl_comm_pvt()+monthlyGasPurchase.getBgfcl_comm_gvt());
			String domesConsump=margin_format.format(monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getBgfcl_dom_meter_pvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_pvt());
		
			double powerTarif=tarrifMargin.get(0).getPdf();
			double cngTarif=tarrifMargin.get(1).getPdf();
			double captiveTarif=tarrifMargin.get(2).getPdf();
			double indusTarif=tarrifMargin.get(3).getPdf();
			double commTarif=tarrifMargin.get(4).getPdf();
			double domesTarif=tarrifMargin.get(5).getPdf();
			
			
			 
			double abs = Double.valueOf(consumption_format.format(tarrifMargin.get(0).getPdf()));
			
			String tpowerConsump=margin_format.format((monthlyGasPurchase.getBgfcl_power_pvt()+monthlyGasPurchase.getBgfcl_power_gvt())*tarrifMargin.get(0).getPdf());
			String tcngConsump=margin_format.format((monthlyGasPurchase.getBgfcl_cng_pvt()+monthlyGasPurchase.getBgfcl_cng_gvt())*tarrifMargin.get(1).getPdf());
			String tcaptiveConsum=margin_format.format((monthlyGasPurchase.getBgfcl_captive_pvt()+monthlyGasPurchase.getBgfcl_captive_gvt())*tarrifMargin.get(2).getPdf());
			String tindusConsump=margin_format.format((monthlyGasPurchase.getBgfcl_industry_pvt()+monthlyGasPurchase.getBgfcl_industry_gvt())*tarrifMargin.get(3).getPdf());
			String tcommConsump=margin_format.format((monthlyGasPurchase.getBgfcl_comm_pvt()+monthlyGasPurchase.getBgfcl_comm_gvt())*tarrifMargin.get(4).getPdf());
			String tdomesConsump=margin_format.format((monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getBgfcl_dom_meter_pvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_pvt())*tarrifMargin.get(5).getPdf());
			
			
			double power=Double.parseDouble(powerConsump);
			double cng=Double.parseDouble(cngConsump);
			
			
			/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
			
			double total_bgfcl_scm1=0.0;
			double total_bgfcl_vat1=0.0;
			double total_bgfcl_sd1=0.0;
			double total_bgfcl_pdf1=0.0;
			double total_bgfcl_bapex1=0.0;
			double total_bgfcl_wellhead1=0.0;
			double total_bgfcl_dwelhead1=0.0;
			double total_bgfcl_transmission1=0.0;
			double total_bgfcl_gdfund1=0.0;
			double total_bgfcl_Avalue1=0.0;
			double total_bgfcl_distribution1=0.0;
			
			double total_sgfl_scm1=0.0; 
			double total_sgfl_vat1=0.0;
			double total_sgfl_sd1=0.0;
			double total_sgfl_pdf1=0.0;
			double total_sgfl_bapex1=0.0;
			double total_sgfl_wellhead1=0.0;
			double total_sgfl_dwelhead1=0.0;
			double total_sgfl_transmission1=0.0;
			double total_sgfl_gdfund1=0.0;
			double total_sgfl_Avalue1=0.0;
			double total_sgfl_distribution1=0.0;
			
			
			
			monthlyGasPurchaseNew=getMonthlyGasPurchaseInformationNew();
			
			total_bgfcl_scm1=monthlyGasPurchaseNew.getBgfcl_power_gvt()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()+monthlyGasPurchaseNew.getBgfcl_industry_gvt()
					+monthlyGasPurchaseNew.getBgfcl_comm_gvt()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt();
			
			total_bgfcl_vat1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getVat()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getVat()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getVat()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getVat()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getVat()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getVat();
			
			total_bgfcl_sd1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getSd()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getSd()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getSd()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getSd()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getSd()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getSd();
			
			total_bgfcl_pdf1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getPdf()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getPdf()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getPdf()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getPdf()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getPdf()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getPdf();
					
					
			total_bgfcl_bapex1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getBapex()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getBapex()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getBapex()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getBapex()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getBapex()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getBapex();
			
			
			total_bgfcl_wellhead1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getWellhead()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getWellhead()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getWellhead()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getWellhead()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getWellhead()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getWellhead();
			
			
			total_bgfcl_dwelhead1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getDwellhead()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getDwellhead()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getDwellhead()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getDwellhead()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getDwellhead()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getDwellhead();
			
			
			total_bgfcl_transmission1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getTrasmission()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getTrasmission()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getTrasmission()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getTrasmission()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getTrasmission()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getTrasmission();
			
			total_bgfcl_gdfund1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getGdfund()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getGdfund()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getGdfund()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getGdfund()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getGdfund()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getGdfund();
			
			total_bgfcl_Avalue1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getAvalue()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getAvalue()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getAvalue()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getAvalue()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getAvalue()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getAvalue();
					
			
			total_bgfcl_distribution1=monthlyGasPurchaseNew.getBgfcl_power_gvt()*tarrifMargin.get(0).getDistribution()+monthlyGasPurchaseNew.getBgfcl_cng_gvt()*tarrifMargin.get(1).getDistribution()+monthlyGasPurchaseNew.getBgfcl_captive_gvt()*tarrifMargin.get(2).getDistribution()+
					monthlyGasPurchaseNew.getBgfcl_industry_gvt()*tarrifMargin.get(3).getDistribution()+monthlyGasPurchaseNew.getBgfcl_comm_gvt()*tarrifMargin.get(4).getDistribution()+monthlyGasPurchaseNew.getBgfcl_dom_meter_gvt()*tarrifMargin.get(5).getDistribution();
			
			
			
			total_sgfl_scm1=monthlyGasPurchaseNew.getSgfl_power_gvt()+monthlyGasPurchaseNew.getSgfl_captive_gvt()+monthlyGasPurchaseNew.getSgfl_cng_gvt()+monthlyGasPurchaseNew.getSgfl_industry_gvt()
					+monthlyGasPurchaseNew.getSgfl_comm_gvt()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt();
			
			total_sgfl_vat1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getVat()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getVat()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getVat()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getVat()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getVat()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getVat();
			
			total_sgfl_sd1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getSd()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getSd()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getSd()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getSd()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getSd()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getSd();
			
			total_sgfl_pdf1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getPdf()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getPdf()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getPdf()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getPdf()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getPdf()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getPdf();
					
					
			total_sgfl_bapex1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getBapex()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getBapex()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getBapex()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getBapex()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getBapex()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getBapex();
			
			
			total_sgfl_wellhead1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getWellhead()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getWellhead()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getWellhead()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getWellhead()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getWellhead()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getWellhead();
			
			
			total_sgfl_dwelhead1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getDwellhead()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getDwellhead()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getDwellhead()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getDwellhead()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getDwellhead()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getDwellhead();
			
			
			total_sgfl_transmission1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getTrasmission()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getTrasmission()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getTrasmission()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getTrasmission()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getTrasmission()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getTrasmission();
			
			total_sgfl_gdfund1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getGdfund()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getGdfund()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getGdfund()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getGdfund()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getGdfund()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getGdfund();
			
			total_sgfl_Avalue1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getAvalue()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getAvalue()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getAvalue()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getAvalue()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getAvalue()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getAvalue();
					
			
			total_sgfl_distribution1=monthlyGasPurchaseNew.getSgfl_power_gvt()*tarrifMargin.get(0).getDistribution()+monthlyGasPurchaseNew.getSgfl_cng_gvt()*tarrifMargin.get(1).getDistribution()+monthlyGasPurchaseNew.getSgfl_captive_gvt()*tarrifMargin.get(2).getDistribution()+
					monthlyGasPurchaseNew.getSgfl_industry_gvt()*tarrifMargin.get(3).getDistribution()+monthlyGasPurchaseNew.getSgfl_comm_gvt()*tarrifMargin.get(4).getDistribution()+monthlyGasPurchaseNew.getSgfl_dom_meter_gvt()*tarrifMargin.get(5).getDistribution();
			
			
			insertMarginDateNew("MARGIN_BGFCL_NEW", total_bgfcl_vat1, total_bgfcl_sd1, total_bgfcl_pdf1, total_bgfcl_bapex1, total_bgfcl_wellhead1, total_bgfcl_dwelhead1, total_bgfcl_transmission1, total_bgfcl_gdfund1, total_bgfcl_Avalue1, total_bgfcl_distribution1);
			
			
			
			insertMarginDateNew("MARGIN_SGFL_NEW",total_sgfl_vat1,total_sgfl_sd1,total_sgfl_pdf1,total_sgfl_bapex1,total_sgfl_wellhead1,total_sgfl_dwelhead1,total_sgfl_transmission1,total_sgfl_gdfund1,total_sgfl_Avalue1,total_sgfl_distribution1);
			
			
			
			
			
			/*---------------------------------------------------------------------------End of Test------------------------------------------------------------------------------------------------------------------*/
		
			//////////////////------Total SGFL----------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total SGFL=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_scm= monthlyGasPurchase.getSgfl_power()+monthlyGasPurchase.getSgfl_captive()+monthlyGasPurchase.getSgfl_cng()
			+monthlyGasPurchase.getSgfl_industrial()+monthlyGasPurchase.getSgfl_commercial()+monthlyGasPurchase.getSgfl_Domestic();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_scm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_vat=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getVat())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getVat())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getVat())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getVat())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getVat())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getVat());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_vat),font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_sgfl_sd=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getSd())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getSd())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getSd())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getSd())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getSd())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getSd());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_sd),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			total_sgfl_pdf=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getPdf())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getPdf())+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getPdf())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getPdf())+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getPdf())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getPdf());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_pdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			System.out.println("Total SGFL : "+consumption_format.format(total_sgfl_pdf));
			
			total_sgfl_bapex=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getBapex())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getBapex())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getBapex())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getBapex())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getBapex())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getBapex());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_bapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_wellhead=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getWellhead())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getWellhead())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getWellhead())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getWellhead())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getWellhead())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getWellhead());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_wellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_dwelhead=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getDwellhead())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getDwellhead())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getDwellhead())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getDwellhead())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getDwellhead())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getDwellhead());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_dwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_transmission=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getTrasmission())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getTrasmission())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getTrasmission())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getTrasmission())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getTrasmission())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getTrasmission());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_transmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_gdfund=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getGdfund())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getGdfund())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getGdfund())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getGdfund())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getGdfund())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getGdfund());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_gdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_Avalue=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getAvalue())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getAvalue())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getAvalue())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getAvalue())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getAvalue())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getAvalue());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_Avalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_sgfl_distribution=(monthlyGasPurchase.getSgfl_power()*tarrifMargin.get(0).getDistribution())+(monthlyGasPurchase.getSgfl_captive()*tarrifMargin.get(2).getDistribution())
					+(monthlyGasPurchase.getSgfl_cng()*tarrifMargin.get(1).getDistribution())+(monthlyGasPurchase.getSgfl_industrial()*tarrifMargin.get(3).getDistribution())
					+(monthlyGasPurchase.getSgfl_commercial()*tarrifMargin.get(4).getDistribution())+(monthlyGasPurchase.getSgfl_Domestic()*tarrifMargin.get(5).getDistribution());
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_sgfl_distribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			insertMarginDate("MARGIN_SGFL",total_sgfl_vat,total_sgfl_sd,total_sgfl_pdf,total_sgfl_bapex,total_sgfl_wellhead,total_sgfl_dwelhead,total_sgfl_transmission,total_sgfl_gdfund,total_sgfl_Avalue,total_sgfl_distribution);
			//////////////////------Grand Total---------//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Grand Total=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_scm=total_bgfcl_scm+total_sgfl_scm;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_scm),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_vat=total_bgfcl_vat+total_sgfl_vat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_vat),font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			grand_total_sd=total_bgfcl_sd+total_sgfl_sd;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_sd),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			variousMargintable.addCell(pcell);
			
			grand_total_pdf=total_bgfcl_pdf+total_sgfl_pdf;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_pdf),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_bapex=total_bgfcl_bapex+total_sgfl_bapex;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_bapex),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_wellhead=total_bgfcl_wellhead+total_sgfl_wellhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_wellhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_dwelhead=total_bgfcl_dwelhead+total_sgfl_dwelhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_dwelhead),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_transmission=total_bgfcl_transmission+total_sgfl_transmission;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_transmission),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_gdfund=total_bgfcl_gdfund+total_sgfl_gdfund;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_gdfund),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_Avalue=total_bgfcl_Avalue+total_sgfl_Avalue;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_Avalue),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			grand_total_distribution=total_bgfcl_distribution+total_sgfl_distribution;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(grand_total_distribution),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			//////////////////------Total Bill of BGFCL/SGFL-//////////////////////
			
			pcell = new PdfPCell(new Paragraph("Total Bill of BGFCL/SGFL=",font2));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bill_vat = total_bgfcl_vat+total_bgfcl_sd+total_bgfcl_wellhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bill_vat),ReportUtil.f9B));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			variousMargintable.addCell(pcell);
			
			total_bill_sd=total_sgfl_vat+total_sgfl_sd+total_sgfl_wellhead;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(total_bill_sd),ReportUtil.f9B));
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
	
	public void insertMarginDate(String tableName,double total_bgfcl_vat,double total_bgfcl_sd,double total_bgfcl_pdf,double total_bgfcl_bapex,double total_bgfcl_wellhead,double total_bgfcl_dwelhead,double total_bgfcl_transmission,double total_bgfcl_gdfund,double total_bgfcl_Avalue,double total_bgfcl_distribution){
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		
		String deleteString="Delete  "+tableName+" where MONTH=? AND YEAR=?";
		String sqlInsert=" Insert into "+tableName+"(MONTH,YEAR,VAT,SD,PDF,BAPEX,WELLHEAD,DWELLHED,TRANSMISSION,GD_FUND,ASSET_VALUE,DISTRIBUTION_MARGIN) " +
				 		  " Values(?,?,?,?,?,?,?,?,?,?,?,?)";
		

		
		

		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(deleteString);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.execute();

				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.setDouble(3,total_bgfcl_vat);
				stmt.setDouble(4,total_bgfcl_sd);
				stmt.setDouble(5,total_bgfcl_pdf);
				stmt.setDouble(6,total_bgfcl_bapex);
				stmt.setDouble(7,total_bgfcl_wellhead);	
				stmt.setDouble(8,total_bgfcl_dwelhead);		
				stmt.setDouble(9,total_bgfcl_transmission);
				stmt.setDouble(10,total_bgfcl_gdfund);
				stmt.setDouble(11,total_bgfcl_Avalue);
				stmt.setDouble(12,total_bgfcl_distribution);
				stmt.execute();
				
			
				
				transactionManager.commit();
				
			
				
			

			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return;
	}
	
	/*----------------------------------------------------------------------------------------------Should be comment out---------------------------------------------------------------------------------*/
	
	public void insertMarginDateNew(String tableName,double total_bgfcl_vat,double total_bgfcl_sd,double total_bgfcl_pdf,double total_bgfcl_bapex,double total_bgfcl_wellhead,double total_bgfcl_dwelhead,double total_bgfcl_transmission,double total_bgfcl_gdfund,double total_bgfcl_Avalue,double total_bgfcl_distribution){
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		
		String deleteString="Delete  "+tableName+" where MONTH=? AND YEAR=?";
		String sqlInsert=" Insert into "+tableName+"(MONTH,YEAR,VAT,SD,PDF,BAPEX,WELLHEAD,DWELLHED,TRANSMISSION,GD_FUND,ASSET_VALUE,DISTRIBUTION_MARGIN) " +
				 		  " Values(?,?,?,?,?,?,?,?,?,?,?,?)";
		

		
		

		PreparedStatement stmt = null;
			try
			{
				stmt = conn.prepareStatement(deleteString);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.execute();

				stmt = conn.prepareStatement(sqlInsert);
				stmt.setString(1,bill_month);
				stmt.setString(2,bill_year);
				stmt.setDouble(3,total_bgfcl_vat);
				stmt.setDouble(4,total_bgfcl_sd);
				stmt.setDouble(5,total_bgfcl_pdf);
				stmt.setDouble(6,total_bgfcl_bapex);
				stmt.setDouble(7,total_bgfcl_wellhead);	
				stmt.setDouble(8,total_bgfcl_dwelhead);		
				stmt.setDouble(9,total_bgfcl_transmission);
				stmt.setDouble(10,total_bgfcl_gdfund);
				stmt.setDouble(11,total_bgfcl_Avalue);
				stmt.setDouble(12,total_bgfcl_distribution);
				stmt.execute();
				
			
				
				transactionManager.commit();
				
			
				
			

			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return;
	}
	
	
	private GasPurchaseDTO getMonthlyGasPurchaseInformationNew()
	{
		GasPurchaseDTO gasPurchase = new GasPurchaseDTO();
	
		
	
		
		try {
			
			
			
 			String monthly_gas_purchase="select bgfcl.MONTH, bgfcl.YEAR,TOTAL_BGFCL, TOTAL_SGFL, TOTAL_IOC, TOTAL_GTCL,  " +
 					"                    bgfcl.POWER, bgfcl.CAPTIVE, bgfcl.CNG, bgfcl.INDUSTRY, bgfcl.COMMERCIAL,bgfcl.DOMESTIC,bgfcl.FERTILIZER, bgfcl.TEA, " +
 					"                    sgfl.POWER POWER_SGFL, sgfl.CAPTIVE CAPTIVE_SGFL, sgfl.CNG CNG_SGFL, sgfl.INDUSTRY INDUSTRY_SGFL, sgfl.COMMERCIAL COMMERCIAL_SGFL, " +
 					"                    sgfl.DOMESTIC DOMESTIC_SGFL,sgfl.FERTILIZER FERTILIZER_SGFL, sgfl.TEA TEA_SGFL                     " +
 					"                    from GAS_PURCHASE_SUMMARY gps,TOTAL_BGFCL_RATIO bgfcl,TOTAL_SGFL_RATIO sgfl  " +
 					"                    where gps.pid=bgfcl.pid  " +
 					"                    and gps.pid=sgfl.pid  " +
 					"                     AND bgfcl.MONTH="+bill_month+
 					"                     AND bgfcl.YEAR="+bill_year+" " ;






			
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
        		
        		gasPurchase.setBgfcl_power_gvt(resultSet.getDouble("POWER"));
        		gasPurchase.setBgfcl_captive_gvt(resultSet.getDouble("CAPTIVE"));
        		gasPurchase.setBgfcl_cng_gvt(resultSet.getDouble("CNG"));
        		gasPurchase.setBgfcl_industry_gvt(resultSet.getDouble("INDUSTRY"));
        		gasPurchase.setBgfcl_comm_gvt(resultSet.getDouble("COMMERCIAL"));
        		gasPurchase.setBgfcl_dom_meter_gvt(resultSet.getDouble("DOMESTIC"));
        		gasPurchase.setBgfcl_fertilizer_gvt(resultSet.getDouble("FERTILIZER"));
        		gasPurchase.setBgfcl_tea_gvt(resultSet.getDouble("TEA"));
        		
        		gasPurchase.setSgfl_power_gvt(resultSet.getDouble("POWER_SGFL"));
        		gasPurchase.setSgfl_captive_gvt(resultSet.getDouble("CAPTIVE_SGFL"));
        		gasPurchase.setSgfl_cng_gvt(resultSet.getDouble("CNG_SGFL"));
        		gasPurchase.setSgfl_industry_gvt(resultSet.getDouble("INDUSTRY_SGFL"));
        		gasPurchase.setSgfl_comm_gvt(resultSet.getDouble("COMMERCIAL_SGFL"));
        		gasPurchase.setSgfl_dom_meter_gvt(resultSet.getDouble("DOMESTIC_SGFL"));
        		gasPurchase.setSgfl_fertilizer_gvt(resultSet.getDouble("FERTILIZER_SGFL"));
        		gasPurchase.setSgfl_tea_gvt(resultSet.getDouble("TEA_SGFL"));
        		
        		
           		
        	}
      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gasPurchase;
	}
	
	
	
	/*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	
	
	
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
					"ioc.CAP_PVT CAP_PVT_IOC, ioc.CNG_GVT CNG_GVT_IOC, ioc.CNG_PVT CNG_PVT_IOC,  " +
					"ioc.IND_GVT IND_GVT_IOC, ioc.IND_PVT IND_PVT_IOC, ioc.COMM_GVT COMM_GVT_IOC,   " +
					"ioc.COMM_PVT COMM_PVT_IOC, ioc.DOM_M_GVT DOM_M_GVT_IOC, ioc.DOM_M_PVT DOM_M_PVT_IOC,  " +
					"ioc.DOM_NM_GVT DOM_NM_GVT_IOC, ioc.DOM_NM_PVT DOM_NM_PVT_IOC, ioc.FERTILIZER_GVT FERTILIZER_GVT_IOC,  " +
					"ioc.FERTILIZER_PVT FERTILIZER_PVT_IOC, ioc.TEA_GVT TEA_GVT_IOC, ioc.TEA_PVT TEA_PVT_IOC, " +
					"sgfl.PW_GVT PW_GVT_sgfl, sgfl.PW_PVT PW_PVT_sgfl, sgfl.CAP_GVT CAP_GVT_sgfl,  " +
					"sgfl.CAP_PVT CAP_PVT_sgfl, sgfl.CNG_GVT CNG_GVT_sgfl, sgfl.CNG_PVT CNG_PVT_sgfl,  " +
					"sgfl.IND_GVT IND_GVT_sgfl, sgfl.IND_PVT IND_PVT_sgfl, sgfl.COMM_GVT COMM_GVT_sgfl,   " +
					"sgfl.COMM_PVT COMM_PVT_sgfl, sgfl.DOM_M_GVT DOM_M_GVT_sgfl, sgfl.DOM_M_PVT DOM_M_PVT_sgfl,  " +
					"sgfl.DOM_NM_GVT DOM_NM_GVT_sgfl, sgfl.DOM_NM_PVT DOM_NM_PVT_sgfl, sgfl.FERTILIZER_GVT FERTILIZER_GVT_sgfl,  " +
					"sgfl.FERTILIZER_PVT FERTILIZER_PVT_sgfl, sgfl.TEA_GVT TEA_GVT_sgfl, sgfl.TEA_PVT TEA_PVT_sgfl " +
					"from GAS_PURCHASE_SUMMARY gps,GAS_PURCHASE_BGFCL bgfcl,GAS_PURCHASE_IOC ioc,GAS_PURCHASE_SGFL sgfl " +
					"where gps.pid=bgfcl.pid " +
					"and gps.pid=ioc.pid " +
					"and gps.pid=sgfl.pid "+
					" AND bgfcl.MONTH="+bill_month+
					" AND bgfcl.YEAR="+bill_year;






			
			PreparedStatement ps1=conn.prepareStatement(monthly_gas_purchase);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		Double powper_bgfcl=0.0;
        		Double cap_bgfcl=0.0;
        		Double cng_bgfcl=0.0;
        		Double ind_bgfcl=0.0;
        		Double comm_bgfcl=0.0;
        		Double domestic_bgfcl=0.0;
        		
        		Double powper_ioc=0.0;
        		Double cap_ioc=0.0;
        		Double cng_ioc=0.0;
        		Double ind_ioc=0.0;
        		Double comm_ioc=0.0;
        		Double domestic_ioc=0.0;
        		
        		Double powper_sgfl=0.0;
        		Double cap_sgfl=0.0;
        		Double cng_sgfl=0.0;
        		Double ind_sgfl=0.0;
        		Double comm_sgfl=0.0;
        		Double domestic_sgfl=0.0;
        		
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
        		gasPurchase.setBgfcl_cng_gvt(resultSet.getDouble("CNG_GVT"));
        		gasPurchase.setBgfcl_cng_pvt(resultSet.getDouble("CNG_PVT"));
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
        		powper_bgfcl=resultSet.getDouble("PW_GVT")+resultSet.getDouble("PW_PVT");
        		gasPurchase.setBgfcl_power(powper_bgfcl);
        		cap_bgfcl=resultSet.getDouble("CAP_GVT")+resultSet.getDouble("CAP_PVT");
        		gasPurchase.setBgfcl_captive(cap_bgfcl);
        		cng_bgfcl=resultSet.getDouble("CNG_GVT")+resultSet.getDouble("CNG_PVT");
        		gasPurchase.setBgfcl_cng(cng_bgfcl);
        		ind_bgfcl=resultSet.getDouble("IND_GVT")+resultSet.getDouble("IND_PVT");
        		gasPurchase.setBgfcl_industrial(ind_bgfcl);
        		comm_bgfcl=resultSet.getDouble("COMM_GVT")+resultSet.getDouble("COMM_PVT");
        		gasPurchase.setBgfcl_commercial(comm_bgfcl);
        		domestic_bgfcl=resultSet.getDouble("DOM_M_GVT")+resultSet.getDouble("DOM_M_PVT")+resultSet.getDouble("DOM_NM_GVT")+resultSet.getDouble("DOM_NM_PVT");
        		gasPurchase.setBgfcl_Domestic(domestic_bgfcl);
        		gasPurchase.setBgfcl_fertilizer(resultSet.getDouble("FERTILIZER_GVT")+resultSet.getDouble("FERTILIZER_PVT"));
        		gasPurchase.setBgfcl_tea(resultSet.getDouble("TEA_GVT")+resultSet.getDouble("TEA_GVT"));
        		
        		
           		gasPurchase.setIoc_power_gvt(resultSet.getDouble("PW_GVT_IOC"));
        		gasPurchase.setIoc_power_pvt(resultSet.getDouble("PW_PVT_IOC"));
        		gasPurchase.setIoc_captive_gvt(resultSet.getDouble("CAP_GVT_IOC"));
        		gasPurchase.setIoc_captive_pvt(resultSet.getDouble("CAP_PVT_IOC"));
        		gasPurchase.setIoc_cng_gvt(resultSet.getDouble("CNG_GVT_IOC"));
        		gasPurchase.setIoc_cng_pvt(resultSet.getDouble("CNG_PVT_IOC"));
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
        		powper_ioc=resultSet.getDouble("PW_GVT_IOC")+resultSet.getDouble("PW_PVT_IOC");
        		gasPurchase.setIoc_power(powper_ioc);
        		cap_ioc=resultSet.getDouble("CAP_GVT_IOC")+resultSet.getDouble("CAP_PVT_IOC");
        		gasPurchase.setIoc_captive(cap_ioc);
        		cng_ioc=resultSet.getDouble("CAP_GVT_IOC")+resultSet.getDouble("CAP_PVT_IOC");
        		gasPurchase.setIoc_cng(cng_bgfcl);
        		ind_ioc=resultSet.getDouble("IND_GVT_IOC")+resultSet.getDouble("IND_GVT_IOC");
        		gasPurchase.setIoc_industrial(ind_ioc);
        		comm_ioc=resultSet.getDouble("COMM_GVT_IOC")+resultSet.getDouble("COMM_PVT_IOC");
        		gasPurchase.setIoc_commercial(comm_ioc);
        		domestic_ioc=resultSet.getDouble("DOM_M_GVT_IOC")+resultSet.getDouble("DOM_M_PVT_IOC")+resultSet.getDouble("DOM_NM_GVT_IOC")+resultSet.getDouble("DOM_NM_PVT_IOC");
        		gasPurchase.setIoc_Domestic(domestic_ioc);
        		gasPurchase.setIoc_fertilizer(resultSet.getDouble("FERTILIZER_GVT_IOC")+resultSet.getDouble("FERTILIZER_PVT_IOC"));
        		gasPurchase.setIoc_tea(resultSet.getDouble("TEA_GVT_IOC")+resultSet.getDouble("TEA_GVT_IOC"));
        		
        		gasPurchase.setSgfl_power_gvt(resultSet.getDouble("PW_GVT_SGFL"));
        		gasPurchase.setSgfl_power_pvt(resultSet.getDouble("PW_PVT_SGFL"));
        		System.out.println("Pow_Sgcl_gvt="+resultSet.getDouble("PW_GVT_SGFL"));
        		System.out.println("Pow_Sgcl_pvt="+resultSet.getDouble("PW_PVT_SGFL"));
        		gasPurchase.setSgfl_captive_gvt(resultSet.getDouble("CAP_GVT_SGFL"));
        		gasPurchase.setSgfl_captive_pvt(resultSet.getDouble("CAP_PVT_SGFL"));
        		gasPurchase.setSgfl_cng_gvt(resultSet.getDouble("CNG_GVT_SGFL"));
        		gasPurchase.setSgfl_cng_pvt(resultSet.getDouble("CNG_PVT_SGFL"));
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
        		
        		powper_sgfl=resultSet.getDouble("PW_GVT_SGFL")+resultSet.getDouble("PW_PVT_SGFL");
        		gasPurchase.setSgfl_power(powper_sgfl);   
        		cap_sgfl=resultSet.getDouble("CAP_GVT_SGFL")+resultSet.getDouble("CAP_PVT_SGFL");
        		gasPurchase.setSgfl_captive(cap_sgfl);
        		cng_sgfl=resultSet.getDouble("CNG_GVT_SGFL")+resultSet.getDouble("CNG_PVT_SGFL");
        		gasPurchase.setSgfl_cng(cng_sgfl);
        		ind_sgfl=resultSet.getDouble("IND_GVT_SGFL")+resultSet.getDouble("IND_PVT_SGFL");
        		gasPurchase.setSgfl_industrial(ind_sgfl);
        		comm_sgfl=resultSet.getDouble("COMM_GVT_SGFL")+resultSet.getDouble("COMM_PVT_SGFL");
        		gasPurchase.setSgfl_commercial(comm_sgfl);
        		domestic_sgfl=resultSet.getDouble("DOM_M_GVT_SGFL")+resultSet.getDouble("DOM_M_PVT_SGFL")+resultSet.getDouble("DOM_NM_GVT_SGFL")+resultSet.getDouble("DOM_NM_PVT_SGFL");
        		gasPurchase.setSgfl_Domestic(domestic_sgfl);
        		gasPurchase.setSgfl_fertilizer(resultSet.getDouble("FERTILIZER_GVT_SGFL")+resultSet.getDouble("FERTILIZER_PVT_SGFL"));
        		gasPurchase.setSgfl_tea(resultSet.getDouble("TEA_GVT_SGFL")+resultSet.getDouble("TEA_GVT_SGFL"));
        	
        		gasPurchase.setTotal_power(powper_bgfcl+powper_ioc+powper_sgfl);
        		gasPurchase.setTotal_captive(cap_bgfcl+cap_ioc+cap_sgfl);
        		gasPurchase.setTotal_cng(cng_bgfcl+cng_ioc+cng_sgfl);
        		gasPurchase.setTotal_industrial(ind_bgfcl+ind_ioc+ind_sgfl);
        		gasPurchase.setTotal_commercial(comm_bgfcl+comm_ioc+comm_sgfl);
        		gasPurchase.setTotal_domestic(domestic_bgfcl+domestic_ioc+domestic_sgfl);
        		
        		
        		System.out.println("Power BGFCL Total : "+consumption_format.format(powper_bgfcl));
        		System.out.println("Captive BGFCL Total : "+consumption_format.format(cap_bgfcl));
        		System.out.println("CNG BGFCL Total : "+consumption_format.format(cng_bgfcl));
        		System.out.println("Indus BGFCL Total : "+consumption_format.format(ind_bgfcl));
        		System.out.println("Commercial BGFCL Total : "+consumption_format.format(comm_bgfcl));
        		System.out.println("Domestic BGFCL Total : "+consumption_format.format(domestic_bgfcl));
        		System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
        		System.out.println("Power SGFL Total : "+consumption_format.format(powper_sgfl));
        		System.out.println("Captive SGFL Total : "+consumption_format.format(cap_sgfl));
        		System.out.println("CNG SGFL Total : "+consumption_format.format(cng_sgfl));
        		System.out.println("Industial SGFL Total : "+consumption_format.format(ind_sgfl));
        		System.out.println("Commercial SGFL Total : "+consumption_format.format(comm_sgfl));
        		System.out.println("Domestic SGFL Total : "+consumption_format.format(domestic_sgfl));
        		
        		System.out.println("Total Power : "+consumption_format.format(powper_bgfcl+powper_ioc+powper_sgfl));
        		System.out.println("Total Captive : "+consumption_format.format(cap_bgfcl+cap_ioc+cap_sgfl));
        		System.out.println("Total CNG : "+consumption_format.format(cng_bgfcl+cng_ioc+cng_sgfl));
        		System.out.println("Total Indus : "+consumption_format.format(ind_bgfcl+ind_ioc+ind_sgfl));
        		System.out.println("Total Comm : "+consumption_format.format(comm_bgfcl+comm_ioc+comm_sgfl));
        		System.out.println("Total Domestic : "+consumption_format.format(domestic_bgfcl+domestic_ioc+domestic_sgfl));
        	}
      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gasPurchase;
	}
	
	private HashMap<String, Double> getMonthlySalesInformation()
	{
		HashMap<String, Double> monthlySalesInfo = new HashMap<String, Double>();
		monthlySalesInfo.put("dom_m_pvt",0.0);
		monthlySalesInfo.put("dom_m_gvt",0.0);
		monthlySalesInfo.put("dom_nm_gvt", 0.0);
		monthlySalesInfo.put("dom_nm_pvt", 0.0);
		monthlySalesInfo.put("comm_pvt",0.0);
		monthlySalesInfo.put("comm_gvt",0.0);
		monthlySalesInfo.put("ind_pvt",0.0);
		monthlySalesInfo.put("ind_gvt",0.0);
		monthlySalesInfo.put("cap_pvt",0.0);
		monthlySalesInfo.put("cap_gvt",0.0);
		monthlySalesInfo.put("cng_pvt",0.0);
		monthlySalesInfo.put("cng_gvt",0.0);
		monthlySalesInfo.put("pow_pvt",0.0);
		monthlySalesInfo.put("pow_gvt",0.0);
		monthlySalesInfo.put("total_sales_JGTDSL",0.0);
		Double total_sales_JGTDSL=0.0;
		
	
		
		try {
			
			
			
			String monthly_sales_metered="select SUM(BILLED_CONSUMPTION) BILLED_CONSUMPTION,CUSTOMER_CATEGORY from bill_metered " +
					" where BILL_MONTH="+bill_month+
					" AND BILL_YEAR="+bill_year+
					" group by CUSTOMER_CATEGORY " +
					" order by CUSTOMER_CATEGORY";
			String monthly_sales_non_metered="select SUM(TOTAL_CONSUMPTION) BILLED_CONSUMPTION,CUSTOMER_CATEGORY from bill_non_metered " +
					" where BILL_MONTH="+bill_month+
					" AND BILL_YEAR="+bill_year+
					" group by CUSTOMER_CATEGORY " +
					" order by CUSTOMER_CATEGORY";




			
			PreparedStatement ps1=conn.prepareStatement(monthly_sales_metered);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		if(resultSet.getString("CUSTOMER_CATEGORY").equals("01"))
        		{
        			monthlySalesInfo.put("dom_m_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        			
        		}else if(resultSet.getString("CUSTOMER_CATEGORY").equals("02"))
        		{
        			monthlySalesInfo.put("dom_m_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("03"))
        		{
        			monthlySalesInfo.put("comm_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("04"))
        		{
        			monthlySalesInfo.put("comm_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("05"))
        		{
        			monthlySalesInfo.put("ind_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("06"))
        		{
        			monthlySalesInfo.put("ind_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("07"))
        		{
        			monthlySalesInfo.put("cap_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("08"))
        		{
        			monthlySalesInfo.put("cap_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("09"))
        		{
        			monthlySalesInfo.put("cng_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("10"))
        		{
        			monthlySalesInfo.put("cng_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("11"))
        		{
        			monthlySalesInfo.put("pow_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        		else if(resultSet.getString("CUSTOMER_CATEGORY").equals("12"))
        		{
        			monthlySalesInfo.put("pow_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
        	}
             ps1=conn.prepareStatement(monthly_sales_non_metered);
        	 resultSet=ps1.executeQuery();
     	
        	while(resultSet.next())
        	{
        		if(resultSet.getString("CUSTOMER_CATEGORY").equals("01"))
        		{
        			monthlySalesInfo.put("dom_nm_pvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}else if(resultSet.getString("CUSTOMER_CATEGORY").equals("02"))
        		{
        			monthlySalesInfo.put("dom_nm_gvt", resultSet.getDouble("BILLED_CONSUMPTION"));
        			total_sales_JGTDSL=total_sales_JGTDSL+resultSet.getDouble("BILLED_CONSUMPTION");
        		}
         			
        	}
        	
        	
        	monthlySalesInfo.put("total_sales_JGTDSL",total_sales_JGTDSL);
        	
        	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return monthlySalesInfo;
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



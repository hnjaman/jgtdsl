package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.MeterReadingDTO;
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




public class GasConsumptionReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<MeterReadingDTO> readingReport=new ArrayList<MeterReadingDTO>();
	HashMap<String, Double> monthlySalesInfo=new HashMap<String, Double>();
	GasPurchaseDTO monthlyGasPurchase=new GasPurchaseDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
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
		static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");

	public String execute() throws Exception
	{
				
		String fileName="Gas_Consumption_Report.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		//DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
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
			
			pcell=new PdfPCell(new Paragraph("CATEGORY WISE GAS CONSUMPTION REPORT FOR THE MONTH "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,ReportUtil.f11B));
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
			
			
			
			PdfPTable summeryInfotable = new PdfPTable(6);
			summeryInfotable.setWidthPercentage(100);
			summeryInfotable.setWidths(new float[]{10,10,10,50,10,10});		
			
			pcell=new PdfPCell(new Paragraph("IOC = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_ioc()),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(" ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(" ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("GTCL = ",font3));
			pcell.setRowspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_gtcl()),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("BGFCL = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_bgfcl()),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_bgfcl()/monthlyGasPurchase.getTotal_gtcl()),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(" ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("JGTDSL = ",font3));
			pcell.setRowspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("total_sales_JGTDSL")),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("SGFL = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_sgfl()),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_sgfl()/monthlyGasPurchase.getTotal_gtcl()),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(" ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("C=GTCL/JGTDSL = ",font3));
			pcell.setRowspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_gtcl()/monthlySalesInfo.get("total_sales_JGTDSL")),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getTotal_gtcl()),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(" ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("System Gain(SCM) = ",font2));
			pcell.setRowspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("need to be calculated",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			pcell.setBorder(0);
			summeryInfotable.addCell(pcell);
			
			document.add(summeryInfotable);
			
			PdfPTable breakline = new PdfPTable(1);
			breakline.setWidthPercentage(100);
			breakline.setWidths(new float[]{100});
			
			pcell= new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setBorder(0);
			breakline.addCell(pcell);
			document.add(breakline);
			
			//Start Mid Table//
			
			PdfPTable dataTable = new PdfPTable(9);
			dataTable.setWidthPercentage(100);
			dataTable.setWidths(new float[]{5,20,20,20,20,20,20,20,20});
			double totalGtclPerCat=0.0;
			double totalSysGain=0.0;
			
			double subSales=0.0;
			double subBgfcl=0.0;
			double subIoc=0.0;
			double subSgfl=0.0;
			double subTotalGtcl=0.0;
			double subSysgain=0.0;
			
			double subSale67=0.0;
			double subBgfcl67=0.0;
			double subIoc67=0.0;
			double subSgfl67=0.0;
			double subTotalGtcl67=0.0;
			double subSysgain67=0.0;
			
			double totalGovtSale=0.0;
			double totalPvtSale=0.0;
			double totalGovtBgfcl=0.0;
			double totalPvtBgfcl=0.0;
			double totalGovtIoc=0.0;
			double totalPvtIoc=0.0;
			double totalGovtSgfl=0.0;
			double totalPvtSgfl=0.0;
			double totalGovtGtcl=0.0;
			double totalPvtGtcl=0.0;
			double totalGovSysGain=0.0;
			double totalPvtSysGain=0.0;
			
			double gTotalSale=0.0;
			double gTotalBgfcl=0.0;
			double gTotalIoc=0.0;
			double gTotalSgfl=0.0;
			double gTotalGtcl=0.0;
			double gTotalSysGain=0.0;
			
			
			pcell = new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Gas Sales to different customers as MIS Statement of Mkt. Dpt.(SCM)",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Amount of Gas purchase/Gas supplied by GTCL (SCM)",font3));
			pcell.setColspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("System Gain(SCM)",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Actual Gas Sales(SCM)",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("BGFCL",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("IOC",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("SGFL",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph("01",font2));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("a)Power(Govt)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("pow_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_power_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_power_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_power_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_power_gvt()+monthlyGasPurchase.getIoc_power_gvt()+monthlyGasPurchase.getSgfl_power_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("pow_gvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("pow_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("b)Power(PVT)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("pow_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_power_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_power_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_power_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_power_pvt()+monthlyGasPurchase.getIoc_power_pvt()+monthlyGasPurchase.getSgfl_power_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("pow_pvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("pow_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSales=monthlySalesInfo.get("pow_gvt")+monthlySalesInfo.get("pow_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subBgfcl=monthlyGasPurchase.getBgfcl_power_gvt()+monthlyGasPurchase.getBgfcl_power_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subIoc=monthlyGasPurchase.getIoc_power_gvt()+monthlyGasPurchase.getIoc_power_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSgfl=monthlyGasPurchase.getSgfl_power_gvt()+monthlyGasPurchase.getSgfl_power_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subTotalGtcl=subBgfcl+subIoc+subSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSysgain=subSales-subTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
					
			//Captive////////////////////////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("02",font2));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("a)Captive Power(Govt)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cap_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_captive_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_captive_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_captive_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_captive_gvt()+monthlyGasPurchase.getIoc_captive_gvt()+monthlyGasPurchase.getSgfl_captive_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("cap_gvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cap_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("b)Captive Power(PVT)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cap_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_captive_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_captive_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_captive_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_captive_pvt()+monthlyGasPurchase.getIoc_captive_pvt()+monthlyGasPurchase.getSgfl_captive_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("cap_pvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cap_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSales=monthlySalesInfo.get("cap_gvt")+monthlySalesInfo.get("cap_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subBgfcl=monthlyGasPurchase.getBgfcl_captive_gvt()+monthlyGasPurchase.getBgfcl_captive_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subIoc=monthlyGasPurchase.getIoc_captive_gvt()+monthlyGasPurchase.getIoc_captive_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSgfl=monthlyGasPurchase.getSgfl_captive_gvt()+monthlyGasPurchase.getSgfl_captive_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subTotalGtcl=subBgfcl+subIoc+subSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSysgain=subSales-subTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			//CNG/////////////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("03",font2));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("a)CNG(Govt)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cng_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_cng_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_cng_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_cng_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_cng_gvt()+monthlyGasPurchase.getIoc_cng_gvt()+monthlyGasPurchase.getSgfl_cng_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("cng_gvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cng_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("b)CNG(PVT)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cng_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_cng_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_cng_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_cng_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_cng_pvt()+monthlyGasPurchase.getIoc_cng_pvt()+monthlyGasPurchase.getSgfl_cng_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("cng_pvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("cng_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSales=monthlySalesInfo.get("cng_gvt")+monthlySalesInfo.get("cng_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subBgfcl=monthlyGasPurchase.getBgfcl_cng_gvt()+monthlyGasPurchase.getBgfcl_cng_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subIoc=monthlyGasPurchase.getIoc_cng_gvt()+monthlyGasPurchase.getIoc_cng_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSgfl=monthlyGasPurchase.getSgfl_cng_gvt()+monthlyGasPurchase.getSgfl_cng_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subTotalGtcl=subBgfcl+subIoc+subSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSysgain=subSales-subTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);

			//Industrial//////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("04",font2));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("a)Industrial(Govt)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("ind_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_industry_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_industry_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_industry_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_industry_gvt()+monthlyGasPurchase.getIoc_industry_gvt()+monthlyGasPurchase.getSgfl_industry_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("ind_gvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("ind_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("b)Industrial(PVT)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("ind_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_industry_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_industry_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_industry_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_industry_pvt()+monthlyGasPurchase.getIoc_industry_pvt()+monthlyGasPurchase.getSgfl_industry_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("ind_pvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("ind_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSales=monthlySalesInfo.get("ind_gvt")+monthlySalesInfo.get("ind_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subBgfcl=monthlyGasPurchase.getBgfcl_industry_gvt()+monthlyGasPurchase.getBgfcl_industry_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subIoc=monthlyGasPurchase.getIoc_industry_gvt()+monthlyGasPurchase.getIoc_industry_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSgfl=monthlyGasPurchase.getSgfl_industry_gvt()+monthlyGasPurchase.getSgfl_industry_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subTotalGtcl=subBgfcl+subIoc+subSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSysgain=subSales-subTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			//Commercial///////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("05",font2));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("a)Commercial(Govt)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("comm_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_comm_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_comm_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_comm_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_comm_gvt()+monthlyGasPurchase.getIoc_comm_gvt()+monthlyGasPurchase.getSgfl_comm_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("comm_gvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("comm_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("b)Commercial(PVT)",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("comm_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_comm_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_comm_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_comm_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_comm_pvt()+monthlyGasPurchase.getIoc_comm_pvt()+monthlyGasPurchase.getSgfl_comm_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("comm_pvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("comm_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSales=monthlySalesInfo.get("comm_gvt")+monthlySalesInfo.get("comm_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subBgfcl=monthlyGasPurchase.getBgfcl_comm_gvt()+monthlyGasPurchase.getBgfcl_comm_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subIoc=monthlyGasPurchase.getIoc_comm_gvt()+monthlyGasPurchase.getIoc_comm_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSgfl=monthlyGasPurchase.getSgfl_comm_gvt()+monthlyGasPurchase.getSgfl_comm_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subTotalGtcl=subBgfcl+subIoc+subSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSysgain=subSales-subTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			//Domestic Metered////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("06",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			//head////////////
			pcell = new PdfPCell(new Paragraph("a)Domestic(Met'r)",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			/////////////////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("a)Govt",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_m_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_dom_meter_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_dom_meter_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_dom_meter_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getIoc_dom_meter_gvt()+monthlyGasPurchase.getSgfl_dom_meter_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("dom_m_gvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_m_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("b)Private",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_m_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_dom_meter_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_dom_meter_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_dom_meter_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_dom_meter_pvt()+monthlyGasPurchase.getIoc_dom_meter_pvt()+monthlyGasPurchase.getSgfl_dom_meter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("dom_m_pvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_m_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sub Total = ",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSales=monthlySalesInfo.get("dom_m_gvt")+monthlySalesInfo.get("dom_m_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subBgfcl=monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getBgfcl_dom_meter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subIoc=monthlyGasPurchase.getIoc_dom_meter_gvt()+monthlyGasPurchase.getIoc_dom_meter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSgfl=monthlyGasPurchase.getSgfl_dom_meter_gvt()+monthlyGasPurchase.getSgfl_dom_meter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subTotalGtcl=subBgfcl+subIoc+subSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSysgain=subSales-subTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			//Domestic Non-Metered////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("07",font2));
			pcell.setRowspan(4);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			//head////////////
			pcell = new PdfPCell(new Paragraph("a)Domestic(Non Met'r)",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
				
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
				
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph("",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
					/////////////////////////////////////////////////////////////
					
			pcell = new PdfPCell(new Paragraph("a)Govt",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_nm_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_dom_nmeter_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_dom_nmeter_gvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()+monthlyGasPurchase.getIoc_dom_nmeter_gvt()+monthlyGasPurchase.getSgfl_dom_nmeter_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("dom_nm_gvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_nm_gvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph("b)Private",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			dataTable.addCell(pcell);
				
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_nm_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getBgfcl_dom_nmeter_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getIoc_dom_nmeter_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlyGasPurchase.getSgfl_dom_nmeter_pvt()),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGtclPerCat=monthlyGasPurchase.getBgfcl_dom_nmeter_pvt()+monthlyGasPurchase.getIoc_dom_nmeter_pvt()+monthlyGasPurchase.getSgfl_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGtclPerCat),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalSysGain= monthlySalesInfo.get("dom_nm_pvt")-totalGtclPerCat;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(monthlySalesInfo.get("dom_nm_pvt")),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph("Sub Total = ",font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSales=monthlySalesInfo.get("dom_nm_gvt")+monthlySalesInfo.get("dom_nm_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subBgfcl=monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subIoc=monthlyGasPurchase.getIoc_dom_nmeter_gvt()+monthlyGasPurchase.getIoc_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSgfl=monthlyGasPurchase.getSgfl_dom_nmeter_gvt()+monthlyGasPurchase.getSgfl_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subTotalGtcl=subBgfcl+subIoc+subSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			subSysgain=subSales-subTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBackgroundColor(new BaseColor(240,240,240));
			dataTable.addCell(pcell);
			
			////For Sub Total(06+07)//////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("Sub Total(06+07)=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(pcell);
			
			subSale67=monthlySalesInfo.get("dom_m_gvt")+monthlySalesInfo.get("dom_m_pvt")+monthlySalesInfo.get("dom_nm_gvt")+monthlySalesInfo.get("dom_nm_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSale67),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			subBgfcl67=monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getBgfcl_dom_meter_pvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_gvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subBgfcl67),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			subIoc67=monthlyGasPurchase.getIoc_dom_meter_gvt()+monthlyGasPurchase.getIoc_dom_meter_pvt()+monthlyGasPurchase.getIoc_dom_nmeter_gvt()+monthlyGasPurchase.getIoc_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subIoc67),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			subSgfl67=monthlyGasPurchase.getSgfl_dom_meter_gvt()+monthlyGasPurchase.getSgfl_dom_meter_pvt()+monthlyGasPurchase.getSgfl_dom_nmeter_gvt()+monthlyGasPurchase.getSgfl_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSgfl67),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			subTotalGtcl67=subBgfcl67+subIoc67+subSgfl67;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subTotalGtcl67),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			subSysgain67=subSale67-subTotalGtcl67;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSysgain67),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(subSale67),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			////////////////Total Government margin///////////////
			
			pcell = new PdfPCell(new Paragraph("Total(Govt.)=",font2));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(pcell);
			
			totalGovtSale=monthlySalesInfo.get("dom_m_gvt")+monthlySalesInfo.get("comm_gvt")+monthlySalesInfo.get("ind_gvt")+
						  monthlySalesInfo.get("cap_gvt")+ monthlySalesInfo.get("cng_gvt")+ monthlySalesInfo.get("pow_gvt")+monthlySalesInfo.get("dom_nm_gvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGovtSale),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGovtBgfcl=monthlyGasPurchase.getBgfcl_captive_gvt()+monthlyGasPurchase.getBgfcl_cng_gvt()+monthlyGasPurchase.getBgfcl_comm_gvt()
					       +monthlyGasPurchase.getBgfcl_dom_meter_gvt()+monthlyGasPurchase.getBgfcl_industry_gvt()+monthlyGasPurchase.getBgfcl_power_gvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGovtBgfcl),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGovtIoc = monthlyGasPurchase.getIoc_captive_gvt()+monthlyGasPurchase.getIoc_cng_gvt()+monthlyGasPurchase.getIoc_comm_gvt()
						   +monthlyGasPurchase.getIoc_dom_meter_gvt()+monthlyGasPurchase.getIoc_industry_gvt()+monthlyGasPurchase.getIoc_power_gvt()+monthlyGasPurchase.getIoc_dom_nmeter_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGovtIoc),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGovtSgfl = monthlyGasPurchase.getSgfl_captive_gvt()+monthlyGasPurchase.getSgfl_cng_gvt()+monthlyGasPurchase.getSgfl_comm_gvt()
						    +monthlyGasPurchase.getSgfl_dom_meter_gvt()+monthlyGasPurchase.getSgfl_industry_gvt()+monthlyGasPurchase.getSgfl_power_gvt()+monthlyGasPurchase.getSgfl_dom_nmeter_gvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGovtSgfl),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGovtGtcl=totalGovtBgfcl+totalGovtIoc+totalGovtSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGovtGtcl),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalGovSysGain=totalGovtSale-totalGovtGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGovSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalGovtSale),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total(Pvt.)=",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(pcell);
			
			totalPvtSale=monthlySalesInfo.get("dom_m_pvt")+monthlySalesInfo.get("dom_nm_pvt")+monthlySalesInfo.get("comm_pvt")+monthlySalesInfo.get("ind_pvt")+
				     monthlySalesInfo.get("cap_pvt")+ monthlySalesInfo.get("cng_pvt")+ monthlySalesInfo.get("pow_pvt");
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtSale),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalPvtBgfcl=monthlyGasPurchase.getBgfcl_captive_pvt()+monthlyGasPurchase.getBgfcl_cng_pvt()+monthlyGasPurchase.getBgfcl_comm_pvt()
				          +monthlyGasPurchase.getBgfcl_dom_meter_pvt()+monthlyGasPurchase.getBgfcl_industry_pvt()+monthlyGasPurchase.getBgfcl_power_pvt()+monthlyGasPurchase.getBgfcl_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtBgfcl),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalPvtIoc = monthlyGasPurchase.getIoc_captive_pvt()+monthlyGasPurchase.getIoc_cng_pvt()+monthlyGasPurchase.getIoc_comm_pvt()
						  +monthlyGasPurchase.getIoc_dom_meter_pvt()+monthlyGasPurchase.getIoc_industry_pvt()+monthlyGasPurchase.getIoc_power_pvt()+monthlyGasPurchase.getIoc_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtIoc),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalPvtSgfl = monthlyGasPurchase.getSgfl_captive_pvt()+monthlyGasPurchase.getSgfl_cng_pvt()+monthlyGasPurchase.getSgfl_comm_pvt()
					 	   +monthlyGasPurchase.getSgfl_dom_meter_pvt()+monthlyGasPurchase.getSgfl_industry_pvt()+monthlyGasPurchase.getSgfl_power_pvt()+monthlyGasPurchase.getSgfl_dom_nmeter_pvt();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtSgfl),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalPvtGtcl=totalPvtBgfcl+totalPvtIoc+totalPvtSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtGtcl),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			totalPvtSysGain = totalPvtSale-totalPvtGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtSysGain),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPvtSale),font2));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			////////////////Grand Total Calculation////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("Grand Total(Govt.+Pvt.)=",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			dataTable.addCell(pcell);
			
			gTotalSale=totalGovtSale+totalPvtSale;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(gTotalSale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			gTotalBgfcl=totalGovtBgfcl+totalPvtBgfcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(gTotalBgfcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			gTotalIoc = totalGovtIoc+totalPvtIoc;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(gTotalIoc),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			gTotalSgfl = totalGovtSgfl+totalPvtSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(gTotalSgfl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			gTotalGtcl = gTotalBgfcl+ gTotalIoc+ gTotalSgfl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(gTotalGtcl),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			gTotalSysGain = gTotalSale-gTotalGtcl;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(gTotalSysGain),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(gTotalSale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dataTable.addCell(pcell);
			
			document.add(dataTable);
				
				
				
				
				
		
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	private ArrayList<MeterReadingDTO> getLoadIncraseInformation()
	{
	ArrayList<MeterReadingDTO> meterReadingInfo=new ArrayList<MeterReadingDTO>();
		
		try {
			String wClause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(mr.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(mr.customer_id,1,2)="+area+" And substr(mr.customer_id,3,2)="+customer_category;
			}
			
			
			String defaulterSql="select mr.customer_id,mcc.CATEGORY_ID,MCC.CATEGORY_NAME,mcc.CATEGORY_TYPE,cpi.full_name||chr(10)||ca.ADDRESS_LINE1 NAME_ADDRESS,to_char(CONNECTION_DATE,'dd-MM-YYYY') CONNECTION_DATE,mmg.RATING_NAME||chr(10)||METER_SL_NO Meter_info,CURR_READING,PREV_READING,DIFFERENCE,mr.PRESSURE,mr.PRESSURE_FACTOR, " +
					"mr.ACTUAL_CONSUMPTION,mr.METER_RENT,mr.pmin_load,mr.pmax_load, decode(mr.pmax_load,0,mr.pmax_load,((mr.ACTUAL_CONSUMPTION*100)/ mr.pmax_load)) Percent  " +
					"from meter_reading mr,customer_connection cc,customer_meter cm,CUSTOMER_ADDRESS ca,customer cus,MST_CUSTOMER_CATEGORY mcc,CUSTOMER_PERSONAL_INFO cpi,MST_METER_GRATING mmg " +
					"where mr.customer_id=cc.customer_id " +
					"and mr.meter_id=cm.meter_id " +
					"and mr.customer_id=ca.customer_id " +
					"and mr.customer_id=CUS.CUSTOMER_ID " +
					"and substr(mr.customer_id,3,2)=MCC.CATEGORY_ID " +
					"and mr.customer_id=cpi.customer_id " +
					"and CM.G_RATING=mmg.RATING_ID " +
					"and mr.billing_month=03 " +
					"and mr.billing_year=2016 " +wClause+
					"order by mcc.CATEGORY_ID desc ,mr.customer_id asc" ;




			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		MeterReadingDTO meterReadingDto=new MeterReadingDTO();
        		CustomerDTO customerDto=new CustomerDTO();
        		CustomerPersonalDTO personalInfoDto=new CustomerPersonalDTO();
        		AddressDTO addressInfoDto=new AddressDTO();
        		CustomerConnectionDTO connectionInfoDto=new CustomerConnectionDTO();
        		
        		meterReadingDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		connectionInfoDto.setConnection_date(resultSet.getString("CONNECTION_DATE"));
        		customerDto.setConnectionInfo(connectionInfoDto);
        		customerDto.setCustomer_category(resultSet.getString("CATEGORY_ID"));
        		customerDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		customerDto.setAddress(resultSet.getString("NAME_ADDRESS"));
        		meterReadingDto.setCustomer(customerDto);
        		
        		meterReadingDto.setMeter_sl_no(resultSet.getString("Meter_info"));
        		meterReadingDto.setActual_consumption(resultSet.getFloat("ACTUAL_CONSUMPTION"));
        		meterReadingDto.setPressure(resultSet.getFloat("PRESSURE"));
        		meterReadingDto.setPressure_factor(resultSet.getFloat("PRESSURE_FACTOR"));
        		meterReadingDto.setPrev_reading(resultSet.getLong("PREV_READING"));
        		meterReadingDto.setCurr_reading(resultSet.getLong("CURR_READING"));
        		meterReadingDto.setDifference(resultSet.getLong("DIFFERENCE"));
        		meterReadingDto.setMax_load(resultSet.getFloat("PMAX_LOAD"));
        		meterReadingDto.setMin_load(resultSet.getFloat("PMIN_LOAD"));
        		meterReadingDto.setMeter_rent(resultSet.getFloat("METER_RENT"));
        		meterReadingDto.setPercent(resultSet.getFloat("PERCENT"));
   		
        		meterReadingInfo.add(meterReadingDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return meterReadingInfo;
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
					"and bgfcl.MONTH="+bill_month+
					"and bgfcl.YEAR="+bill_year;






			
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
        		
        		gasPurchase.setSgfl_power_gvt(resultSet.getDouble("PW_GVT_SGFL"));
        		gasPurchase.setSgfl_power_pvt(resultSet.getDouble("PW_PVT_SGFL"));
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



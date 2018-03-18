package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;

import oracle.jdbc.driver.OracleCallableStatement;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CollectionReportDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.masterData.CustomerCategory;
import org.jgtdsl.reports.*;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;



public class MonthlyCollectionReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	
    private static String area;
    private static String customer_category;
    private static String bill_month;
    private static String bill_year;
    private static String report_for; 
    private static String category_name;
	static ArrayList<CollectionReportDTO> collectionInfoList=new ArrayList<CollectionReportDTO>();
	
	static UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	

	Connection conn = ConnectionManager.getConnection();
	String sql = "";
	ArrayList<String>customerType=new ArrayList<String>();
	
	PreparedStatement ps=null;
	ResultSet rs=null;
	//String[] areaName=new String[10];
	int a=0;
	


	public ServletContext servlet;
	ServletContext servletContext = null;

	static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat("##########0.000");


	public String execute() throws Exception {




		String fileName = "CollectionStatement.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,60,72);
		
		try {

			
			
			
			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(Event);
			PdfPCell pcell = null;
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		    Rectangle page = document.getPageSize();
		    headerTable.setTotalWidth(page.getWidth());
			float a=((page.getWidth()*15)/100)/2;
			float b=((page.getWidth()*30)/100)/2;
				
			headerTable.setWidths(new float[] {
				a,b,a
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png"); 	// image path
			Image img = Image.getInstance(realPath);
						
            	//img.scaleToFit(10f, 200f);
            	//img.scalePercent(200f);
				img.scaleAbsolute(28f, 31f);
				//img.setAbsolutePosition(145f, 780f);		
            	img.setAbsolutePosition(350f, 520f);		// rotate
            
	        document.add(img);
		
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{b});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			
			
			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ",font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(area)-1]),font3);
			Paragraph p = new Paragraph(); 
			p.add(chunk1);
			p.add(chunk2);
			pcell=new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			
						
			pcell=new PdfPCell(mTable);
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			
			
			String bmont = "00".substring(bill_month.length()) + bill_month;

		
        	int month_year=Integer.valueOf(bill_year+bmont);
			if(report_for.equals("area_wise")&& month_year>=201605)
			{
				
				area_wise(document);
			}
			
			
		 	
			if(report_for.equals("category_wise")&& month_year>=201605)							// category_wise 
			{
				
				category_wise(document);
			}
			
			if(report_for.equals("category_type_wise"))
			{
				category_type_wise(document);
			}
			
		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;



		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;




	}


	


	private static PdfPCell createTableNotHeaderCell(final String text) {
		final PdfPCell cell = new PdfPCell(new Paragraph(text, font2));

		cell.setMinimumHeight(16f);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		//cell.setBackgroundColor(new BaseColor(242,242,242));
		cell.setBorderColor(BaseColor.BLACK);


		return cell;
	}

	private static PdfPCell createTableHeaderCell(final String text) {
		final PdfPCell cell = new PdfPCell(new Phrase(text, font1));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		//cell.setBackgroundColor(new BaseColor(210,211,212));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderColor(BaseColor.BLACK);
		cell.setFixedHeight(20f);

		return cell;
	}
	
	
	private static void area_wise(Document document) throws DocumentException
	{
		
		PdfPTable headlineTable = new PdfPTable(3);
		headlineTable.setSpacingBefore(5);
		headlineTable.setSpacingAfter(10);
		headlineTable.setWidths(new float[] {
				40,70,40
			});
		PdfPCell pcell = null;
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Monthly Collection Statement For the month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year, ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		document.add(headlineTable);
		
	
	
		
		Double totalOpeningBalance=0.0;
		Double totalAdjustment=0.0;
		Double totalCurrSales=0.0;
		Double totalCurrSurcharge=0.0;
		Double totalAccountReceivale=0.0;
		Double totalGasBill=0.0;
		Double totalMeterRent=0.0;
		Double totalSurcharge=0.0;
		Double totalIncomeTax=0.0;
		Double totalVatRebate=0.0;
		Double totalHhvNhv =0.0;
		Double totalTotalCollection =0.0;
		Double totalPreviousDue =0.0;
		Double totalAvgMonthlySales =0.0;
		Double totalAvgDue =0.0;
		
		
		
		Double subSumOpeningBalance=0.0;
		Double subSumAdjustment=0.0;
		Double subSumCurrSales=0.0;
		Double subSumCurrSurcharge=0.0;
		Double subSumAccountReceivale=0.0;
		Double subSumGasBill=0.0;
		Double subSumMeterRent=0.0;
		Double subSumSurcharge=0.0;
		Double subSumIncomeTax=0.0;
		Double subSumVatRebate=0.0;
		Double subSumHhvNhv =0.0;
		Double subSumTotalCollection =0.0;
		Double subSumPreviousDue =0.0;
		Double subSumAvgMonthlySales =0.0;
		Double subSumAvgDue =0.0;
		
		
	
		
		PdfPTable datatable1 = new PdfPTable(17);
		
		datatable1.setWidthPercentage(100);
		datatable1.setWidths(new float[] {15,50,40,30,40,30,40,40,40,40,40,40,40,40,40,40,40});
		
		
		pcell=new PdfPCell(new Paragraph("Sl.No",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		String dateChunk="";
	    String dateChunk2="";
	  
	    int month_prev=Integer.valueOf(bill_month)-1;
	    
	    int numDays1 = Utils.getLastDayOfMonth(month_prev,Integer.valueOf(bill_year));
	    int numDays2= Utils.getLastDayOfMonth(Integer.valueOf(bill_month),Integer.valueOf(bill_year));
	    
	    if(bill_month.equals("01"))
	    {
	     dateChunk="Balance as on 31-12-"+(Integer.valueOf(bill_year)-1);
	     dateChunk2="Total Due as on 31-12-"+(Integer.valueOf(bill_year)-1);
	    }else
	    {
	     dateChunk="Balance as on "+numDays1+"-"+(Integer.valueOf(bill_month)-1)+"-"+bill_year;
	     dateChunk2="Total Due as on "+numDays2+"-"+(Integer.valueOf(bill_month))+"-"+bill_year;
	    }
		pcell=new PdfPCell(new Paragraph(dateChunk,font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Adjustment",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Sales For "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Surcharge",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("A/R as on DATE",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Collection For Month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,font3));
		pcell.setColspan(7);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
		pcell=new PdfPCell(new Paragraph(dateChunk2,font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Average monthly Sales",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Average due month",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
	
		pcell=new PdfPCell(new Paragraph("Gas bill",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Surcharge",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Income Tax",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Vat rebate",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Total Collection",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
	    
		pcell=new PdfPCell(new Paragraph("01",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("02",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("03",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("04",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("05",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("06=10",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("07=(3+4+5+6)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("08",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("09",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("10",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("11",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("12",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("13",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("14=(8+9+10+11+12+13)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("15=(7-14)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("16",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("17=(15/16)",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		
		
		generateCollectionDataForReport();
		collectionInfoList=getCollectionInfoList();
		int expireListSize=collectionInfoList.size();
		

		pcell=new PdfPCell(new Paragraph("1",font3));
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph(Area.values()[Integer.valueOf(area)-1]+" AREA",font3));
		pcell.setColspan(16);
		datatable1.addCell(pcell);
		
		int cusCatCount=97;
		String previousType="GOVT";
		
		String currnertType="";
		try {
		
			   			
			for(int i=0;i<expireListSize;i++)
			
        	{
				CollectionReportDTO collectionDto=collectionInfoList.get(i);
				
    			if(cusCatCount==97)
    			{
    				pcell=new PdfPCell(new Paragraph(previousType.equals("PVT")?"A) PRIVATE":"A) GOVERNMENT",font3));
    				pcell.setColspan(2);
    				datatable1.addCell(pcell);
    				pcell=new PdfPCell(new Paragraph("",font3));
    				pcell.setColspan(15);
    				datatable1.addCell(pcell);
    			}
    			
    			currnertType=collectionDto.getCategory_type();
    			if(!currnertType.equals(previousType))
    			{
    			
	    			if(cusCatCount!=97){
	    				
	    				pcell=new PdfPCell(new Paragraph("Sub Total (A)",font3));
	    				pcell.setColspan(2);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumOpeningBalance),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAdjustment),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSurcharge),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAccountReceivale),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumGasBill),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumSurcharge),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumIncomeTax),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumVatRebate),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    			
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHhvNhv),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalCollection),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAvgMonthlySales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue/subSumAvgMonthlySales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				 totalOpeningBalance=totalOpeningBalance+subSumOpeningBalance;
	    				 totalAdjustment=totalAdjustment+subSumAdjustment;
	    				 totalCurrSales=totalCurrSales+subSumCurrSales;
	    				 totalCurrSurcharge=totalCurrSurcharge+subSumCurrSurcharge;
	    				 totalAccountReceivale=totalAccountReceivale+subSumAccountReceivale;
	    				 totalGasBill=totalGasBill+subSumGasBill;
	    				 totalMeterRent=totalMeterRent+subSumMeterRent;
	    				 totalSurcharge=totalSurcharge+subSumSurcharge;
	    				 totalIncomeTax=totalIncomeTax+subSumIncomeTax;
	    				 totalVatRebate=totalVatRebate+subSumVatRebate;
	    				 totalHhvNhv =totalHhvNhv+subSumHhvNhv;
	    				 totalTotalCollection =totalTotalCollection+subSumTotalCollection;
	    				 totalPreviousDue =totalPreviousDue+subSumPreviousDue;
	    				 totalAvgMonthlySales =totalAvgMonthlySales+subSumAvgMonthlySales;
	    				 totalAvgDue =totalAvgDue+subSumAvgDue;
	    				
	    				
	    				 
	    				 subSumOpeningBalance=0.0;
	    				 subSumAdjustment=0.0;
	    				 subSumCurrSales=0.0;
	    				 subSumCurrSurcharge=0.0;
	    				 subSumAccountReceivale=0.0;
	    				 subSumGasBill=0.0;
	    				 subSumMeterRent=0.0;
	    				 subSumSurcharge=0.0;
	    				 subSumIncomeTax=0.0;
	    				 subSumVatRebate=0.0;
	    				 subSumHhvNhv =0.0;
	    				 subSumTotalCollection =0.0;
	    				 subSumPreviousDue =0.0;
	    				 subSumAvgMonthlySales =0.0;
	    				 subSumAvgDue =0.0;

	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(currnertType.equals("PVT")?"B) PRIVATE":"B) GOVERNMENT",font3));
	    				pcell.setColspan(2);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph("",font3));
	    				pcell.setColspan(15);
	    				datatable1.addCell(pcell);
	    				cusCatCount=97;
	    				previousType=currnertType;
	    			}
					
    			}
    			
    			pcell=new PdfPCell(new Paragraph(Character.toString ((char) cusCatCount)+")",font3));
    			datatable1.addCell(pcell);
    			pcell=new PdfPCell(new Paragraph(collectionDto.getCategory_name(),font3));
    			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
    			datatable1.addCell(pcell);
    				    		
    			
    			
    	        
    			
    			subSumOpeningBalance=subSumOpeningBalance+collectionDto.getOpening_balance();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getOpening_balance()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAdjustment=subSumAdjustment+collectionDto.getAdjustment();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAdjustment()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumCurrSales=subSumCurrSales+collectionDto.getCurr_sales();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getCurr_sales()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumCurrSurcharge=subSumCurrSurcharge+collectionDto.getCurr_surcharge();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getCurr_surcharge()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAccountReceivale=subSumAccountReceivale+collectionDto.getAccount_receivable();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAccount_receivable()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumGasBill=subSumGasBill+collectionDto.getGas_bill();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getGas_bill()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumMeterRent=subSumMeterRent+collectionDto.getMeter_rent();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getMeter_rent()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumSurcharge=subSumSurcharge+collectionDto.getColl_surcharge();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getColl_surcharge()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumIncomeTax=subSumIncomeTax+collectionDto.getIncome_tax();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getIncome_tax()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumVatRebate=subSumVatRebate+collectionDto.getVat_rebate();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getVat_rebate()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumHhvNhv=subSumHhvNhv+collectionDto.getHhv_nhv();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getHhv_nhv()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumTotalCollection=subSumTotalCollection+collectionDto.getTotal_collection();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getTotal_collection()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumPreviousDue=subSumPreviousDue+collectionDto.getPrevious_due();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getPrevious_due()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAvgMonthlySales=subSumAvgMonthlySales+collectionDto.getAvg_monthly_sales();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAvg_monthly_sales()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAvgDue=subSumAvgDue+collectionDto.getAvg_due();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAvg_due()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    		
    			cusCatCount++;
        	}	
    		
		
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumOpeningBalance),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell); 
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAdjustment),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAccountReceivale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumGasBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumIncomeTax),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumVatRebate),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHhvNhv),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalCollection),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue/subSumAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			 totalOpeningBalance=totalOpeningBalance+subSumOpeningBalance;
			 totalAdjustment=totalAdjustment+subSumAdjustment;
			 totalCurrSales=totalCurrSales+subSumCurrSales;
			 totalCurrSurcharge=totalCurrSurcharge+subSumCurrSurcharge;
			 totalAccountReceivale=totalAccountReceivale+subSumAccountReceivale;
			 totalGasBill=totalGasBill+subSumGasBill;
			 totalMeterRent=totalMeterRent+subSumMeterRent;
			 totalSurcharge=totalSurcharge+subSumSurcharge;
			 totalIncomeTax=totalIncomeTax+subSumIncomeTax;
			 totalVatRebate=totalVatRebate+subSumVatRebate;
			 totalHhvNhv =totalHhvNhv+subSumHhvNhv;
			 totalTotalCollection =totalTotalCollection+subSumTotalCollection;
			 totalPreviousDue =totalPreviousDue+subSumPreviousDue;
			 totalAvgMonthlySales =totalAvgMonthlySales+subSumAvgMonthlySales;
			 totalAvgDue =totalAvgDue+subSumAvgDue;
			
			pcell=new PdfPCell(new Paragraph("Total Sales Of "+Area.values()[Integer.valueOf(area)-1]+"(A+B)=",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalOpeningBalance),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAdjustment),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalCurrSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalCurrSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAccountReceivale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalGasBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalIncomeTax),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalVatRebate),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalHhvNhv),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalTotalCollection),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalPreviousDue),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalPreviousDue/totalAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
		
			
			        
			        
			    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
		
		document.add(datatable1);

		
	}
	
	
	
	// ****************************************** category wise **************************
	
	
	private static void category_wise(Document document) throws DocumentException
	{
		
		PdfPTable headlineTable = new PdfPTable(3);
		headlineTable.setSpacingBefore(5);
		headlineTable.setSpacingAfter(10);
		headlineTable.setWidths(new float[] {
				40,70,40
			});
		PdfPCell pcell = null;
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Monthly Collection Statement For the month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year, ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		document.add(headlineTable);
		
	
	
		
		Double totalOpeningBalance=0.0;
		Double totalAdjustment=0.0;
		Double totalCurrSales=0.0;
		Double totalCurrSurcharge=0.0;
		Double totalAccountReceivale=0.0;
		Double totalGasBill=0.0;
		Double totalMeterRent=0.0;
		Double totalSurcharge=0.0;
		Double totalIncomeTax=0.0;
		Double totalVatRebate=0.0;
		Double totalHhvNhv =0.0;
		Double totalTotalCollection =0.0;
		Double totalPreviousDue =0.0;
		Double totalAvgMonthlySales =0.0;
		Double totalAvgDue =0.0;
		
		
		
		Double subSumOpeningBalance=0.0;
		Double subSumAdjustment=0.0;
		Double subSumCurrSales=0.0;
		Double subSumCurrSurcharge=0.0;
		Double subSumAccountReceivale=0.0;
		Double subSumGasBill=0.0;
		Double subSumMeterRent=0.0;
		Double subSumSurcharge=0.0;
		Double subSumIncomeTax=0.0;
		Double subSumVatRebate=0.0;
		Double subSumHhvNhv =0.0;
		Double subSumTotalCollection =0.0;
		Double subSumPreviousDue =0.0;
		Double subSumAvgMonthlySales =0.0;
		Double subSumAvgDue =0.0;
		
		
		
		PdfPTable collectionMonthCustomerType = new PdfPTable(3);
        collectionMonthCustomerType.setWidths(new float[] {60,90,30});
        collectionMonthCustomerType.setSpacingBefore(5);
        collectionMonthCustomerType.setSpacingAfter(5);
        
        
        pcell = new PdfPCell(new Paragraph("Collection Month: "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,font3));
        pcell.setBorder(0);
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        collectionMonthCustomerType.addCell(pcell);
        
        pcell = new PdfPCell(new Paragraph("Type Of Customer",font3));
        pcell.setBorder(0);
        pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        collectionMonthCustomerType.addCell(pcell);
        
        
        pcell = new PdfPCell(new Paragraph("Captive Power",font3));
        //customerType.setBorder(0);
        pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        collectionMonthCustomerType.addCell(pcell);
        

        
        document.add(collectionMonthCustomerType);
	
        
		
		PdfPTable datatable1 = new PdfPTable(10);
		//datatable1.setWidthPercentage(100);
		datatable1.setWidths(new float[] {15,25,15,20,25,20,20,15,10,15});
		datatable1.setSpacingBefore(5);
		

		
		
		pcell=new PdfPCell(new Paragraph("Code",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer Details",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
	  
	    
		
		pcell=new PdfPCell(new Paragraph("Billing Month",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Date Of Payment",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Bank & Branch Name",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Gas Bill",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Surcharge",font3));
		pcell.setColspan(7);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
		pcell=new PdfPCell(new Paragraph("Fees",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("S.D",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Total Paid",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
	
		
	    
		
		
		
		
		generateCollectionDataForReport();
		collectionInfoList=getCollectionInfoList();
		int expireListSize=collectionInfoList.size();
		

		pcell=new PdfPCell(new Paragraph("1",font3));
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph(Area.values()[Integer.valueOf(area)-1]+" AREA",font3));
		pcell.setColspan(16);
		datatable1.addCell(pcell);
		
		int cusCatCount=97;
		String previousType="GOVT";
		
		String currnertType="";
		try {
		
			   			
			for(int i=0;i<expireListSize;i++)
			
        	{
				CollectionReportDTO collectionDto=collectionInfoList.get(i);
				
    			if(cusCatCount==97)
    			{
    				pcell=new PdfPCell(new Paragraph(previousType.equals("PVT")?"A) PRIVATE":"A) GOVERNMENT",font3));
    				pcell.setColspan(2);
    				datatable1.addCell(pcell);
    				pcell=new PdfPCell(new Paragraph("",font3));
    				pcell.setColspan(15);
    				datatable1.addCell(pcell);
    			}
    			
    			currnertType=collectionDto.getCategory_type();
    			if(!currnertType.equals(previousType))
    			{
    			
	    			if(cusCatCount!=97){
	    				
	    				pcell=new PdfPCell(new Paragraph("Sub Total (A)",font3));
	    				pcell.setColspan(2);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumOpeningBalance),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAdjustment),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSurcharge),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAccountReceivale),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumGasBill),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumSurcharge),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumIncomeTax),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumVatRebate),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    			
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHhvNhv),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalCollection),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAvgMonthlySales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue/subSumAvgMonthlySales),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				 totalOpeningBalance=totalOpeningBalance+subSumOpeningBalance;
	    				 totalAdjustment=totalAdjustment+subSumAdjustment;
	    				 totalCurrSales=totalCurrSales+subSumCurrSales;
	    				 totalCurrSurcharge=totalCurrSurcharge+subSumCurrSurcharge;
	    				 totalAccountReceivale=totalAccountReceivale+subSumAccountReceivale;
	    				 totalGasBill=totalGasBill+subSumGasBill;
	    				 totalMeterRent=totalMeterRent+subSumMeterRent;
	    				 totalSurcharge=totalSurcharge+subSumSurcharge;
	    				 totalIncomeTax=totalIncomeTax+subSumIncomeTax;
	    				 totalVatRebate=totalVatRebate+subSumVatRebate;
	    				 totalHhvNhv =totalHhvNhv+subSumHhvNhv;
	    				 totalTotalCollection =totalTotalCollection+subSumTotalCollection;
	    				 totalPreviousDue =totalPreviousDue+subSumPreviousDue;
	    				 totalAvgMonthlySales =totalAvgMonthlySales+subSumAvgMonthlySales;
	    				 totalAvgDue =totalAvgDue+subSumAvgDue;
	    				
	    				
	    				 
	    				 subSumOpeningBalance=0.0;
	    				 subSumAdjustment=0.0;
	    				 subSumCurrSales=0.0;
	    				 subSumCurrSurcharge=0.0;
	    				 subSumAccountReceivale=0.0;
	    				 subSumGasBill=0.0;
	    				 subSumMeterRent=0.0;
	    				 subSumSurcharge=0.0;
	    				 subSumIncomeTax=0.0;
	    				 subSumVatRebate=0.0;
	    				 subSumHhvNhv =0.0;
	    				 subSumTotalCollection =0.0;
	    				 subSumPreviousDue =0.0;
	    				 subSumAvgMonthlySales =0.0;
	    				 subSumAvgDue =0.0;

	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(currnertType.equals("PVT")?"B) PRIVATE":"B) GOVERNMENT",font3));
	    				pcell.setColspan(2);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph("",font3));
	    				pcell.setColspan(15);
	    				datatable1.addCell(pcell);
	    				cusCatCount=97;
	    				previousType=currnertType;
	    			}
					
    			}
    			
    			pcell=new PdfPCell(new Paragraph(Character.toString ((char) cusCatCount)+")",font3));
    			datatable1.addCell(pcell);
    			pcell=new PdfPCell(new Paragraph(collectionDto.getCategory_name(),font3));
    			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
    			datatable1.addCell(pcell);
    				    		
    			
    			
    	        
    			
    			subSumOpeningBalance=subSumOpeningBalance+collectionDto.getOpening_balance();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getOpening_balance()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAdjustment=subSumAdjustment+collectionDto.getAdjustment();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAdjustment()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumCurrSales=subSumCurrSales+collectionDto.getCurr_sales();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getCurr_sales()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumCurrSurcharge=subSumCurrSurcharge+collectionDto.getCurr_surcharge();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getCurr_surcharge()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAccountReceivale=subSumAccountReceivale+collectionDto.getAccount_receivable();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAccount_receivable()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumGasBill=subSumGasBill+collectionDto.getGas_bill();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getGas_bill()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumMeterRent=subSumMeterRent+collectionDto.getMeter_rent();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getMeter_rent()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumSurcharge=subSumSurcharge+collectionDto.getColl_surcharge();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getColl_surcharge()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumIncomeTax=subSumIncomeTax+collectionDto.getIncome_tax();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getIncome_tax()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumVatRebate=subSumVatRebate+collectionDto.getVat_rebate();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getVat_rebate()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumHhvNhv=subSumHhvNhv+collectionDto.getHhv_nhv();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getHhv_nhv()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumTotalCollection=subSumTotalCollection+collectionDto.getTotal_collection();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getTotal_collection()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumPreviousDue=subSumPreviousDue+collectionDto.getPrevious_due();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getPrevious_due()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAvgMonthlySales=subSumAvgMonthlySales+collectionDto.getAvg_monthly_sales();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAvg_monthly_sales()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			subSumAvgDue=subSumAvgDue+collectionDto.getAvg_due();
    			pcell=new PdfPCell(new Paragraph(taka_format.format(collectionDto.getAvg_due()),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    		
    			cusCatCount++;
        	}	
    		
		
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumOpeningBalance),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell); 
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAdjustment),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumCurrSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAccountReceivale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumGasBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumIncomeTax),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumVatRebate),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHhvNhv),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalCollection),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumPreviousDue/subSumAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			 totalOpeningBalance=totalOpeningBalance+subSumOpeningBalance;
			 totalAdjustment=totalAdjustment+subSumAdjustment;
			 totalCurrSales=totalCurrSales+subSumCurrSales;
			 totalCurrSurcharge=totalCurrSurcharge+subSumCurrSurcharge;
			 totalAccountReceivale=totalAccountReceivale+subSumAccountReceivale;
			 totalGasBill=totalGasBill+subSumGasBill;
			 totalMeterRent=totalMeterRent+subSumMeterRent;
			 totalSurcharge=totalSurcharge+subSumSurcharge;
			 totalIncomeTax=totalIncomeTax+subSumIncomeTax;
			 totalVatRebate=totalVatRebate+subSumVatRebate;
			 totalHhvNhv =totalHhvNhv+subSumHhvNhv;
			 totalTotalCollection =totalTotalCollection+subSumTotalCollection;
			 totalPreviousDue =totalPreviousDue+subSumPreviousDue;
			 totalAvgMonthlySales =totalAvgMonthlySales+subSumAvgMonthlySales;
			 totalAvgDue =totalAvgDue+subSumAvgDue;
			
			pcell=new PdfPCell(new Paragraph("Total Sales Of "+Area.values()[Integer.valueOf(area)-1]+"(A+B)=",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalOpeningBalance),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAdjustment),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalCurrSales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalCurrSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAccountReceivale),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalGasBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalSurcharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalIncomeTax),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalVatRebate),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalHhvNhv),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalTotalCollection),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalPreviousDue),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalPreviousDue/totalAvgMonthlySales),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
		
		
			
			        
			        
			    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
		
		document.add(datatable1);

		
	}
	
	
	/*	All Category type wise collection report
	 * 
	 * 	sujon
	 * */
	
	private static void category_type_wise(Document document) throws DocumentException
	{
		
		PdfPTable headlineTable = new PdfPTable(3);
		headlineTable.setSpacingBefore(5);
		headlineTable.setSpacingAfter(10);
		headlineTable.setWidths(new float[] {
				40,70,40
			});
		PdfPCell pcell = null;
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Monthly Collection Customer Type Wise", ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		document.add(headlineTable);
		
	

		
		
		PdfPTable collectionMonthCustomerType = new PdfPTable(1);
        collectionMonthCustomerType.setWidths(new float[] {180});
        collectionMonthCustomerType.setSpacingBefore(5);
        collectionMonthCustomerType.setSpacingAfter(5);
        
        
        pcell = new PdfPCell(new Paragraph("Collection Month: "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,ReportUtil.f11B));
        pcell.setBorder(0);
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        collectionMonthCustomerType.addCell(pcell);

        document.add(collectionMonthCustomerType);
	
        
		
		PdfPTable pdfPTable = new PdfPTable(9);
		//datatable1.setWidthPercentage(100);
		pdfPTable.setWidths(new float[] {10,30,25,20,20,20,25,15,15});
		pdfPTable.setSpacingBefore(5);
		

		
		
		pcell=new PdfPCell(new Paragraph("Sl No",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer Category",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Monthly Gas Bill",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Surcharge",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Security",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Fees",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Total Amount",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
	
		pcell=new PdfPCell(new Paragraph("Description",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Comment",ReportUtil.f11B));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		

		collectionInfoList=getCollectionInfoList();
		int listSize=collectionInfoList.size();
		

		double totalGasBill=0.0;
		double totalSurcharge=0.0;
		double totalFees=0.0;
		double totalSecurityDeposit=0.0;
		double total=0.0;
		
		for(int i=0;i<listSize;i++)
		{
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(collectionInfoList.get(i).getCategory_name(),ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(taka_format.format(collectionInfoList.get(i).getGas_bill()),ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(collectionInfoList.get(i).getColl_surcharge()),ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(collectionInfoList.get(i).getSecurity()),ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(collectionInfoList.get(i).getFees()),ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(collectionInfoList.get(i).getGas_bill()+collectionInfoList.get(i).getColl_surcharge()+collectionInfoList.get(i).getFees()+collectionInfoList.get(i).getSecurity()),ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f11));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			 totalGasBill+=collectionInfoList.get(i).getGas_bill();
			 totalSurcharge+=collectionInfoList.get(i).getColl_surcharge();
			 totalFees+=collectionInfoList.get(i).getFees();
			 totalSecurityDeposit+=collectionInfoList.get(i).getSecurity();
			 total+=collectionInfoList.get(i).getGas_bill()+collectionInfoList.get(i).getColl_surcharge()+collectionInfoList.get(i).getFees()+collectionInfoList.get(i).getSecurity();
					
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(2);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalGasBill),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSurcharge),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSecurityDeposit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalFees),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(total),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);

		
	}
	
	/*	End All Category type wise collection report
	 * 
	 * 	sujon
	 * */
	
	
	private static void generateCollectionDataForReport()
	{
		OracleCallableStatement stmt=null;
		Connection conn = ConnectionManager.getConnection();
		try {
			int month=Integer.parseInt(bill_month);
			String billMonth="";
			if(month<10){
				billMonth="0"+Integer.toString(month);
			}else{
				billMonth=Integer.toString(month);
			}
			
			String monthyear=bill_year+billMonth;
			int yearmon=Integer.parseInt(monthyear);
			
			if(yearmon>201604){
			
			//System.out.println("Procedure Save_Multi_Month_Collection Begins");
			stmt = (OracleCallableStatement) conn.prepareCall("{ call Collection_Report_Helper(?,?,?) }");
			 
			
			stmt.setString(1, area);
			stmt.setInt(2,Integer.valueOf(bill_month));
			stmt.setInt(3, Integer.valueOf(bill_year));
			stmt.executeUpdate();
		
	
			}
        	
   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	 
	
	private static ArrayList<CollectionReportDTO> getCollectionInfoList()
	{
	ArrayList<CollectionReportDTO> collectionInfoList=new ArrayList<CollectionReportDTO>();
	Connection conn = ConnectionManager.getConnection();	
	//String area=loggedInUser.getArea_id();
	String westAreaCode = new String("02");		// for power collection
		try {
			String wClause="";
			String w2Clause="";
		
			
			if(report_for.equals("category_type_wise"))
			{
				String defaulterSql="  SELECT CATEGORY, " +
						"         CATEGORY_NAME, " +
						"         SUM (ACTUAL_REVENUE) ACTUAL_REVENUE, " +
						"         SUM (SURCHARGE) SURCHARGE, " +
						"         SUM (FEES) FEESS, " +
						"         SUM (SECURITY) SECURITY " +
						"    FROM (  SELECT SUBSTR (BAL.CUSTOMER_ID, 3, 2) CATEGORY, " +
						"                   CATEGORY_NAME, " +
						"                   SUM (DEBIT) - SUM (SURCHARGE) ACTUAL_REVENUE, " +
						"                   SUM (SURCHARGE) SURCHARGE, " +
						"                   0 FEES, " +
						"                   0 SECURITY " +
						"              FROM bank_account_ledger BAL, " +
						"                   MST_BANK_INFO MBI, " +
						"                   MST_BRANCH_INFO MBRI, " +
						"                   MST_CUSTOMER_CATEGORY MCC " +
						"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
						"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
						"					AND MBRI.area_id = MBI.area_id" +
						"                   AND SUBSTR (BAL.CUSTOMER_ID, 3, 2) = MCC.CATEGORY_ID " +
						"                   AND TO_CHAR (TRANS_DATE, 'MM') = LPAD ("+bill_month+", 2, 0) " +
						"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+bill_year+" " +
						"                   AND TRANS_TYPE = 1 " +
						"                   AND MBRI.AREA_ID = '"+area+"' " +
						" 					AND BAL.CUSTOMER_ID not LIKE '0213%'" +
						"          GROUP BY TRANS_TYPE, SUBSTR (BAL.CUSTOMER_ID, 3, 2), CATEGORY_NAME " +
						"          UNION ALL " +
						"            SELECT SUBSTR (BAL.CUSTOMER_ID, 3, 2) CATEGORY, " +
						"                   CATEGORY_NAME, " +
						"                   0 ACTUAL_REVENUE, " +
						"                   0 SURCHARGE, " +
						"                   SUM (debit) AS FEES, " +
						"                   0 SECURITY " +
						"              FROM bank_account_ledger BAL, " +
						"                   MST_BANK_INFO MBI, " +
						"                   MST_BRANCH_INFO MBRI, " +
						"                   MST_CUSTOMER_CATEGORY MCC " +
						"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
						"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
						"					AND MBRI.area_id = MBI.area_id" +
						"                   AND SUBSTR (BAL.CUSTOMER_ID, 3, 2) = MCC.CATEGORY_ID " +
						"                   AND TO_CHAR (TRANS_DATE, 'MM') = LPAD ("+bill_month+", 2, 0) " +
						"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+bill_year+" " +
						"                   AND TRANS_TYPE = 7 " +
						"                   AND MBRI.AREA_ID = '"+area+"'" +
						" 					AND BAL.CUSTOMER_ID not LIKE '0213%'" +
						"          GROUP BY TRANS_TYPE, SUBSTR (BAL.CUSTOMER_ID, 3, 2), CATEGORY_NAME " +
						"          UNION ALL " +
						"            SELECT SUBSTR (BAL.CUSTOMER_ID, 3, 2) CATEGORY, " +
						"                   CATEGORY_NAME, " +
						"                   0 ACTUAL_REVENUE, " +
						"                   0 SURCHARGE, " +
						"                   0 FEES, " +
						"                   SUM (debit) - SUM (credit) AS SECURITY " +
						"              FROM bank_account_ledger BAL, " +
						"                   MST_BANK_INFO MBI, " +
						"                   MST_BRANCH_INFO MBRI, " +
						"                   MST_CUSTOMER_CATEGORY MCC " +
						"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
						"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
						"					AND MBRI.area_id = MBI.area_id" +
						"                   AND SUBSTR (BAL.CUSTOMER_ID, 3, 2) = MCC.CATEGORY_ID " +
						"                   AND TO_CHAR (TRANS_DATE, 'MM') = LPAD ("+bill_month+", 2, 0) " +
						"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+bill_year+" " +
						"                   AND TRANS_TYPE = 0 " +
						"					AND REF_ID not in(select DEPOSIT_ID from MST_DEPOSIT where DEPOSIT_TYPE=1)" +
						"                   AND MBRI.AREA_ID = '"+area+"'" +
						" 					AND BAL.CUSTOMER_ID not LIKE '0213%'" +
						"          GROUP BY TRANS_TYPE, SUBSTR (BAL.CUSTOMER_ID, 3, 2), CATEGORY_NAME) " +
						"GROUP BY CATEGORY, CATEGORY_NAME " ;
				
				
				PreparedStatement ps1=conn.prepareStatement(defaulterSql);
//				 ps1.setString(1, bill_month);
//				 ps1.setString(2, bill_year);
//				 ps1.setString(3, area);
//				 ps1.setString(4, bill_month);
//				 ps1.setString(5, bill_year);
//				 ps1.setString(6, area);
//				 ps1.setString(7, bill_month);
//				 ps1.setString(8, bill_year);
//				 ps1.setString(9, area);
				
	        	ResultSet resultSet=ps1.executeQuery();
	        	
	        	while(resultSet.next())
	        	{
	        		CollectionReportDTO collectionDto=new CollectionReportDTO();
	        		collectionDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
	        		collectionDto.setGas_bill(resultSet.getDouble("ACTUAL_REVENUE"));
	        		collectionDto.setColl_surcharge(resultSet.getDouble("SURCHARGE"));
	        		collectionDto.setFees(resultSet.getDouble("FEESS"));
	        		collectionDto.setSecurity(resultSet.getDouble("SECURITY"));
	        		
	        		collectionInfoList.add(collectionDto);
	        		
	        	}
			}
			else
			{
				String defaulterSql="select AREA_NAME,cr.* ,CATEGORY_TYPE from COLLECTION_RRPORT cr,MST_CUSTOMER_CATEGORY mcc,MST_AREA ma " +
						"where CR.CATEGORY_ID=mcc.CATEGORY_ID " +
						"and  cr.area_id=MA.AREA_ID " +
						"order by CR.AREA_ID,CATEGORY_TYPE,CR.CATEGORY_ID " ;
				//	     bill_month and Bill_year conside korte hobe
				
				PreparedStatement ps1=conn.prepareStatement(defaulterSql);
			
	        	
	        	ResultSet resultSet=ps1.executeQuery();
	        	
	        	
	        	while(resultSet.next())
	        	{
	        		CollectionReportDTO collectionDto=new CollectionReportDTO();
	        		collectionDto.setArea_id(resultSet.getString("AREA_ID"));
	        		collectionDto.setArea_name(resultSet.getString("AREA_NAME"));
	        		collectionDto.setCategory_id(resultSet.getString("CATEGORY_ID"));
	        		//loadIncraseDTO.setName_address(resultSet.getString("NAME_ADDRESS"));
	        		collectionDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
	        		collectionDto.setCategory_type(resultSet.getString("CATEGORY_TYPE"));
	        		collectionDto.setOpening_balance(resultSet.getDouble("BALANCE_PREV_MONTH"));
	        		collectionDto.setAdjustment(resultSet.getDouble("ADJUSTMENT"));
	        		collectionDto.setCurr_sales(resultSet.getDouble("SALES_CURR_MONTH"));
	        		collectionDto.setCurr_surcharge(resultSet.getDouble("SURCHARGE"));
	        		collectionDto.setAccount_receivable(resultSet.getDouble("AR_CURR_MONTH"));
	        		collectionDto.setGas_bill(resultSet.getDouble("GAS_BILL_COLL"));
	        		collectionDto.setMeter_rent(resultSet.getDouble("METER_RENT_COLL"));
	        		collectionDto.setColl_surcharge(resultSet.getDouble("SURCHARGE_COLL"));
	        		collectionDto.setIncome_tax(resultSet.getDouble("TAX_COLL"));
	        		collectionDto.setVat_rebate(resultSet.getDouble("VAT_REBATE"));
	        		collectionDto.setHhv_nhv(resultSet.getDouble("NHV_HHV_COLL"));
	        		collectionDto.setTotal_collection(resultSet.getDouble("TOTAL_COLL"));
	        		collectionDto.setPrevious_due(resultSet.getDouble("TOTAL_DUE"));
	        		collectionDto.setAvg_monthly_sales(resultSet.getDouble("AVG_MONTHLY_SALES"));
	        		collectionDto.setAvg_due(resultSet.getDouble("AVERAGE_DUE"));
	        		
	        		
	        		collectionInfoList.add(collectionDto);
	        		
	        	}
			}
			
			
			// power customer collection
			
			if(area.equals(westAreaCode)){
				
				String powerCollectionSql="SELECT CATEGORY, " +
											"         CATEGORY_NAME, " +
											"         SUM (ACTUAL_REVENUE) ACTUAL_REVENUE, " +
											"         SUM (SURCHARGE) SURCHARGE, " +
											"         SUM (FEES) FEESS, " +
											"         SUM (SECURITY) SECURITY " +
											"    FROM (  SELECT SUBSTR (BAL.CUSTOMER_ID, 3, 2) CATEGORY, " +
											"                   CATEGORY_NAME, " +
											"                   SUM (DEBIT) - SUM (SURCHARGE) ACTUAL_REVENUE, " +
											"                   SUM (SURCHARGE) SURCHARGE, " +
											"                   0 FEES, " +
											"                   0 SECURITY " +
											"              FROM bank_account_ledger BAL, " +
											"                   MST_BANK_INFO MBI, " +
											"                   MST_BRANCH_INFO MBRI, " +
											"                   MST_CUSTOMER_CATEGORY MCC " +
											"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
											"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
											"                   AND MBRI.area_id = MBI.area_id " +
											"                   AND SUBSTR (BAL.CUSTOMER_ID, 3, 2) = MCC.CATEGORY_ID " +
											"                   AND TO_CHAR (TRANS_DATE, 'MM') = LPAD ("+bill_month+", 2, 0) " +
											"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+bill_year+" " +
											"                   AND TRANS_TYPE = 1 " +
											"                   and BAL.CUSTOMER_ID like '0213%' " +
											"          GROUP BY TRANS_TYPE, SUBSTR (BAL.CUSTOMER_ID, 3, 2), CATEGORY_NAME " +
											"          UNION ALL " +
											"            SELECT SUBSTR (BAL.CUSTOMER_ID, 3, 2) CATEGORY, " +
											"                   CATEGORY_NAME, " +
											"                   0 ACTUAL_REVENUE, " +
											"                   0 SURCHARGE, " +
											"                   SUM (debit) AS FEES, " +
											"                   0 SECURITY " +
											"              FROM bank_account_ledger BAL, " +
											"                   MST_BANK_INFO MBI, " +
											"                   MST_BRANCH_INFO MBRI, " +
											"                   MST_CUSTOMER_CATEGORY MCC " +
											"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
											"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
											"                   AND MBRI.area_id = MBI.area_id " +
											"                   AND SUBSTR (BAL.CUSTOMER_ID, 3, 2) = MCC.CATEGORY_ID " +
											"                   AND TO_CHAR (TRANS_DATE, 'MM') = LPAD ("+bill_month+", 2, 0) " +
											"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+bill_year+" " +
											"                   AND TRANS_TYPE = 7 " +
											"                   and BAL.CUSTOMER_ID like '0213%' " +
											"          GROUP BY TRANS_TYPE, SUBSTR (BAL.CUSTOMER_ID, 3, 2), CATEGORY_NAME " +
											"          UNION ALL " +
											"            SELECT SUBSTR (BAL.CUSTOMER_ID, 3, 2) CATEGORY, " +
											"                   CATEGORY_NAME, " +
											"                   0 ACTUAL_REVENUE, " +
											"                   0 SURCHARGE, " +
											"                   0 FEES, " +
											"                   SUM (debit) - SUM (credit) AS SECURITY " +
											"              FROM bank_account_ledger BAL, " +
											"                   MST_BANK_INFO MBI, " +
											"                   MST_BRANCH_INFO MBRI, " +
											"                   MST_CUSTOMER_CATEGORY MCC " +
											"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
											"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
											"                   AND MBRI.area_id = MBI.area_id " +
											"                   AND SUBSTR (BAL.CUSTOMER_ID, 3, 2) = MCC.CATEGORY_ID " +
											"                   AND TO_CHAR (TRANS_DATE, 'MM') = LPAD ("+bill_month+", 2, 0) " +
											"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+bill_year+" " +
											"                   AND TRANS_TYPE = 0 " +
											"                   AND REF_ID NOT IN (SELECT DEPOSIT_ID " +
											"                                        FROM MST_DEPOSIT " +
											"                                       WHERE DEPOSIT_TYPE = 1) " +
											"                   and BAL.CUSTOMER_ID like '0213%' " +
											"          GROUP BY TRANS_TYPE, SUBSTR (BAL.CUSTOMER_ID, 3, 2), CATEGORY_NAME) " +
											"GROUP BY CATEGORY, CATEGORY_NAME ";
				
				
				PreparedStatement ps1=conn.prepareStatement(powerCollectionSql);
				
	        	ResultSet resultSet=ps1.executeQuery();
	        	
	        	while(resultSet.next())
	        	{
	        		CollectionReportDTO collectionDto=new CollectionReportDTO();
	        		collectionDto.setCategory_name(resultSet.getString("CATEGORY_NAME"));
	        		collectionDto.setGas_bill(resultSet.getDouble("ACTUAL_REVENUE"));
	        		collectionDto.setColl_surcharge(resultSet.getDouble("SURCHARGE"));
	        		collectionDto.setFees(resultSet.getDouble("FEESS"));
	        		collectionDto.setSecurity(resultSet.getDouble("SECURITY"));
	        		
	        		collectionInfoList.add(collectionDto);
	        		
	        	}
			}
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return collectionInfoList;
	}


				public String getArea() {
					return area;
				}


				public void setArea(String area) {
					this.area = area;
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


				public String getCustomer_category() {
					return customer_category;
				}


				public void setCustomer_category(String customer_category) {
					this.customer_category = customer_category;
				}


				public String getCategory_name() {
					return category_name;
				}


				public void setCategory_name(String category_name) {
					this.category_name = category_name;
				}


		
				
				public ServletContext getServlet() {
					return servlet;
				}

				public void setServletContext(ServletContext servlet) {
					this.servlet = servlet;
				}
				


	}
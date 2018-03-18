package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.DisconnType;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.masterData.CustomerCategory;
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



public class MonthlySalesStatement extends BaseAction {
	private static final long serialVersionUID = 1L;
	
    private static String area;
    private static String customer_category;
    private static String bill_month;
    private static String bill_year;
    private String report_for; 
    private static String category_name;

	
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
	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");


	public String execute() throws Exception {




		
		String fileName=String.valueOf(Month.values()[Integer.valueOf(bill_month)-1].getLabel());
		       fileName +="/"+bill_year+"SalesStatement";
		       fileName +=String.valueOf(String.valueOf(Area.values()[Integer.valueOf(area)-1]));
		       fileName +=".pdf";
		 
		 
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
			float b=((page.getWidth()*40)/100)/2;
				
			headerTable.setWidths(new float[] {
				a,b,a
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			
// for logo			
	
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png");  // image path
			   Image img = Image.getInstance(realPath);
			      
			             //img.scaleToFit(10f, 200f);
			             //img.scalePercent(200f);
			            img.scaleAbsolute(32f, 35f);
			            //img.setAbsolutePosition(145f, 780f);  
			             img.setAbsolutePosition(348f, 515f);  // rotate
			            
			         document.add(img);
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{b});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A company of PetroBangla)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
//error in chunk2			
			
			Chunk chunk1 = new Chunk("Regional Office :",font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(area)-1]),font3);
			//Chunk chunk2 = new Chunk(String.valueOf(Area.values()[1]),font3);
			
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
			
			if(report_for.equals("area_wise"))
			{
				area_wise(document);
			}else
			{
				category_wise(document);
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
		Connection conn = ConnectionManager.getConnection();
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
		
		pcell=new PdfPCell(new Paragraph("Statement of Gas Sales For The Month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year, ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		document.add(headlineTable);
		
		BigDecimal domGovActExpMin=BigDecimal.ZERO;
		BigDecimal domGovActWithMin=BigDecimal.ZERO;
		BigDecimal domGovBillunit=BigDecimal.ZERO;
		BigDecimal domGovDiff=BigDecimal.ZERO;
		BigDecimal domGovTotalCon=BigDecimal.ZERO;
		BigDecimal domGovValueOfActCon=BigDecimal.ZERO;
		BigDecimal domGovMinCharge=BigDecimal.ZERO;
		BigDecimal domGovHHV=BigDecimal.ZERO;
		BigDecimal domGovMeterRent=BigDecimal.ZERO;
		BigDecimal domGovTotalBill=BigDecimal.ZERO;
	
		
		BigDecimal totalActExpMin=BigDecimal.ZERO;
		BigDecimal totalActWithMin=BigDecimal.ZERO;
		BigDecimal totalBillunit=BigDecimal.ZERO;
		BigDecimal totalDiff=BigDecimal.ZERO;
		BigDecimal totalTotalCon=BigDecimal.ZERO;
		BigDecimal totalValueOfActCon=BigDecimal.ZERO;
		BigDecimal totalMinCharge=BigDecimal.ZERO;
		BigDecimal totalHHV=BigDecimal.ZERO;
		BigDecimal totalMeterRent=BigDecimal.ZERO;
		BigDecimal totalTotalBill=BigDecimal.ZERO;
		
		
		
		BigDecimal subSumActExpMin=BigDecimal.ZERO;
		BigDecimal subSumActWithMin=BigDecimal.ZERO;
		BigDecimal subSumBillunit=BigDecimal.ZERO;
		BigDecimal subSumDiff=BigDecimal.ZERO;
		BigDecimal subSumTotalCon=BigDecimal.ZERO;
		BigDecimal subSumValueOfActCon=BigDecimal.ZERO;
		BigDecimal subSumMinCharge=BigDecimal.ZERO;
		BigDecimal subSumHHV=BigDecimal.ZERO;
		BigDecimal subSumMeterRent=BigDecimal.ZERO;
		BigDecimal subSumTotalBill=BigDecimal.ZERO;
		
	
		
		PdfPTable datatable1 = new PdfPTable(13);
		
		datatable1.setWidthPercentage(100);
		datatable1.setWidths(new float[] {15,50,40,40,30,30,40,30,30,30,30,30,40
		});
		
		
		pcell=new PdfPCell(new Paragraph("Sl.No",font3));
		pcell.setRowspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
		pcell.setRowspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
		pcell.setColspan(5);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
		pcell.setColspan(7);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
		pcell.setColspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
		pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		
		
		pcell=new PdfPCell(new Paragraph("Ramarks",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		
		
		pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Difference",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
//		pcell=new PdfPCell(new Paragraph("01",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("02",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("03",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("04",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("05",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
		
//		pcell=new PdfPCell(new Paragraph("rate",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph("08",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("09",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("10",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("11",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);		
//		pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("1",font3));
//		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph(Area.values()[Integer.valueOf(area)-1]+" AREA",font3));
		pcell.setColspan(13);
		datatable1.addCell(pcell);
		
		int cusCatCount=97;
		String previousType="GOVT";
		
		String currnertType="";
		try {
		
			String sql1="select MCC.CATEGORY_ID, CATEGORY_NAME" +
						",MCC.CATEGORY_TYPE,"+
						"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
						"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
						"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
						"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
						"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
						"where SR.customer_id=conn.customer_id and BILLING_MONTH=? and BILLING_YEAR=?"+
						"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)=?"+ 
						"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,MCC.CATEGORY_TYPE,RATE "+
						"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID ASC";
    		PreparedStatement ps1=conn.prepareStatement(sql1);
    		ps1.setString(1, bill_month);
    		ps1.setString(2, bill_year);
    		ps1.setString(3, area);
    		ResultSet rs1=ps1.executeQuery();    			
    		while(rs1.next())
        	{
    			currnertType=rs1.getString("CATEGORY_TYPE");
    			
    			if(cusCatCount==97)
    			{
    				
    				pcell=new PdfPCell(new Paragraph(previousType.equals(currnertType)?"A) GOVERNMENT":"B) PRIVATE",font3));
    				pcell.setColspan(2);
    				datatable1.addCell(pcell);
    				pcell=new PdfPCell(new Paragraph("",font3));
    				pcell.setColspan(11);
    				datatable1.addCell(pcell);
    			}
    			
    			
    			if(!currnertType.equals(previousType))
    			{
    			
	    			if(cusCatCount!=97){
//	    				pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",font3));
//	    				pcell.setColspan(2);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovActExpMin),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovActWithMin),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovBillunit),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovDiff),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovTotalCon),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				
//	    				
//	    				pcell=new PdfPCell(new Paragraph(taka_format.format(domGovValueOfActCon),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				pcell=new PdfPCell(new Paragraph(taka_format.format(domGovMinCharge),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    						    				    				
//	    				pcell=new PdfPCell(new Paragraph(taka_format.format(domGovMeterRent),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				pcell=new PdfPCell(new Paragraph(taka_format.format(domGovHHV),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				
//	    				
//	    				pcell=new PdfPCell(new Paragraph(taka_format.format(domGovTotalBill),font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
//	    				
//	    				
//	    				
//	    				pcell=new PdfPCell(new Paragraph("",font3));
//	    				pcell.setColspan(1);
//	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//	    				datatable1.addCell(pcell);
	    				
	    				
	    				 domGovActExpMin=BigDecimal.ZERO;
	    				 domGovActWithMin=BigDecimal.ZERO;
	    				 domGovBillunit=BigDecimal.ZERO;
	    				 domGovDiff=BigDecimal.ZERO;
	    				 domGovTotalCon=BigDecimal.ZERO;
	    				 domGovValueOfActCon=BigDecimal.ZERO;
	    				 domGovMinCharge=BigDecimal.ZERO;
	    				 domGovHHV=BigDecimal.ZERO;
	    				 domGovMeterRent=BigDecimal.ZERO;
	    				 domGovTotalBill=BigDecimal.ZERO;
		    			
	    				pcell=new PdfPCell(new Paragraph("Sub Total (A)",font3));
	    				pcell.setColspan(2);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumActExpMin),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumActWithMin),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumBillunit),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumDiff),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumTotalCon),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumValueOfActCon),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMinCharge),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHHV),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    			
	    				
	    				pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalBill),font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph("",font3));
	    				pcell.setColspan(1);
	    				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    				datatable1.addCell(pcell);
	    				
	    				
	    				totalActExpMin=totalActExpMin.add(subSumActExpMin);
	    				totalActWithMin=totalActWithMin.add(subSumActWithMin);
	    				totalBillunit=totalBillunit.add(subSumBillunit);
	    				totalDiff=totalDiff.add(subSumDiff);
	    				totalTotalCon=totalTotalCon.add(subSumTotalCon);
	    				totalValueOfActCon=totalValueOfActCon.add(subSumValueOfActCon);
	    				totalMinCharge=totalMinCharge.add(subSumMinCharge);
	    				totalHHV=totalHHV.add(subSumHHV);
	    				totalMeterRent=totalMeterRent.add(subSumMeterRent);
	    				totalTotalBill=totalTotalBill.add(subSumTotalBill);
	    				
	    				
	    				
	    				 
	    				     subSumActExpMin=BigDecimal.ZERO;
	    					 subSumActWithMin=BigDecimal.ZERO;
	    					 subSumBillunit=BigDecimal.ZERO;
	    					 subSumDiff=BigDecimal.ZERO;
	    					 subSumTotalCon=BigDecimal.ZERO;
	    					 subSumValueOfActCon=BigDecimal.ZERO;
	    					 subSumMinCharge=BigDecimal.ZERO;
	    					 subSumHHV=BigDecimal.ZERO;
	    					 subSumMeterRent=BigDecimal.ZERO;
	    					 subSumTotalBill=BigDecimal.ZERO;

	    				
	    				
	    				
	    				
	    				pcell=new PdfPCell(new Paragraph(currnertType.equals("PVT")?"B) PRIVATE":"A) GOVERNMENT",font3));
	    				pcell.setColspan(2);
	    				datatable1.addCell(pcell);
	    				pcell=new PdfPCell(new Paragraph("",font3));
	    				pcell.setColspan(11);
	    				datatable1.addCell(pcell);
	    				cusCatCount=97;
	    				previousType=currnertType;
	    			}
	    			
	    		
					
    			}
    			
    			pcell=new PdfPCell(new Paragraph(Character.toString ((char) cusCatCount)+")",font3));
    			datatable1.addCell(pcell);
    			pcell=new PdfPCell(new Paragraph(rs1.getString("CATEGORY_NAME"),font3));
    			pcell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
    			datatable1.addCell(pcell);
    				    		
    			
    			
    	        
    			
    			subSumActExpMin=subSumActExpMin.add(rs1.getBigDecimal("ACTUAL_EXCEPT_MINIMUM")); // new solution	    			
    			
    			String category=rs1.getString("CATEGORY_ID");
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovActExpMin=domGovActExpMin.add(rs1.getFloat("ACTUAL_EXCEPT_MINIMUM")==0?BigDecimal.ZERO:rs1.getBigDecimal("ACTUAL_EXCEPT_MINIMUM"));
    			}
    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getBigDecimal("ACTUAL_EXCEPT_MINIMUM")),font2));//new solution
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			
    			subSumActWithMin=subSumActWithMin.add(rs1.getFloat("ACTUAL_WITH_MINIMUM")==0?BigDecimal.ZERO:rs1.getBigDecimal("ACTUAL_WITH_MINIMUM"));
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovActWithMin=domGovActWithMin.add(rs1.getFloat("ACTUAL_WITH_MINIMUM")==0?BigDecimal.ZERO:rs1.getBigDecimal("ACTUAL_WITH_MINIMUM"));
    			}
    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("ACTUAL_WITH_MINIMUM")==0?BigDecimal.ZERO:rs1.getBigDecimal("ACTUAL_WITH_MINIMUM")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			subSumBillunit=subSumBillunit.add(rs1.getFloat("BILLING_UNIT")==0?BigDecimal.ZERO:rs1.getBigDecimal("BILLING_UNIT"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovBillunit=domGovBillunit.add(rs1.getFloat("BILLING_UNIT")==0?BigDecimal.ZERO:rs1.getBigDecimal("BILLING_UNIT"));
    			}
    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("BILLING_UNIT")==0?BigDecimal.ZERO:rs1.getBigDecimal("BILLING_UNIT")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			subSumDiff=subSumDiff.add(rs1.getFloat("DIFFERENCE")==0?BigDecimal.ZERO:rs1.getBigDecimal("DIFFERENCE"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovDiff=domGovDiff.add(rs1.getFloat("DIFFERENCE")==0?BigDecimal.ZERO:rs1.getBigDecimal("DIFFERENCE"));
    			}
    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("DIFFERENCE")==0?BigDecimal.ZERO:rs1.getBigDecimal("DIFFERENCE")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			subSumTotalCon=subSumTotalCon.add(rs1.getFloat("TOTAL_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_ACTUAL_CONSUMPTION"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovTotalCon=domGovTotalCon.add(rs1.getFloat("TOTAL_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_ACTUAL_CONSUMPTION"));
    			}
    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("TOTAL_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_ACTUAL_CONSUMPTION")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
//    			pcell=new PdfPCell(new Paragraph(rs1.getString("RATE"),font2));
//    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//    			datatable1.addCell(pcell);
    			
    			
    			
    			
    			
    			subSumValueOfActCon=subSumValueOfActCon.add(rs1.getFloat("VALUE_OF_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("VALUE_OF_ACTUAL_CONSUMPTION"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovValueOfActCon=domGovValueOfActCon.add(rs1.getFloat("VALUE_OF_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("VALUE_OF_ACTUAL_CONSUMPTION"));
    			}
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("VALUE_OF_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("VALUE_OF_ACTUAL_CONSUMPTION")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			subSumMinCharge=subSumMinCharge.add(rs1.getFloat("MINIMUM_CHARGE")==0?BigDecimal.ZERO:rs1.getBigDecimal("MINIMUM_CHARGE"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovMinCharge=domGovMinCharge.add(rs1.getFloat("MINIMUM_CHARGE")==0?BigDecimal.ZERO:rs1.getBigDecimal("MINIMUM_CHARGE"));
    			}
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("MINIMUM_CHARGE")==0?BigDecimal.ZERO:rs1.getBigDecimal("MINIMUM_CHARGE")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			
    			subSumMeterRent=subSumMeterRent.add(rs1.getFloat("METER_RENT")==0?BigDecimal.ZERO:rs1.getBigDecimal("METER_RENT"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovMeterRent=domGovMeterRent.add(rs1.getFloat("METER_RENT")==0?BigDecimal.ZERO:rs1.getBigDecimal("METER_RENT"));
    			}
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("METER_RENT")==0?BigDecimal.ZERO:rs1.getBigDecimal("METER_RENT")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			

    			
    			subSumHHV=subSumHHV.add(rs1.getFloat("HHV_NHV_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("HHV_NHV_AMOUNT"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovHHV=domGovHHV.add(rs1.getFloat("HHV_NHV_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("HHV_NHV_AMOUNT"));
    			}
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("HHV_NHV_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("HHV_NHV_AMOUNT")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			
    			
    			subSumTotalBill=subSumTotalBill.add(rs1.getFloat("TOTAL_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_AMOUNT"));
    			
    			if(category.equals("01")||category.equals("02"))
    			{
    				domGovTotalBill=domGovTotalBill.add(rs1.getFloat("TOTAL_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_AMOUNT"));
    			}
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("TOTAL_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_AMOUNT")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			pcell=new PdfPCell(new Paragraph(" ",font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			
    			
    			
    			
    			
    			cusCatCount++;
        	}	
    		totalActExpMin=totalActExpMin.add(subSumActExpMin);
			totalActWithMin=totalActWithMin.add(subSumActWithMin);
			totalBillunit=totalBillunit.add(subSumBillunit);
			totalDiff=totalDiff.add(subSumDiff);
			totalTotalCon=totalTotalCon.add(subSumTotalCon);
			totalValueOfActCon=totalValueOfActCon.add(subSumValueOfActCon);
			totalMinCharge=totalMinCharge.add(subSumMinCharge);
			totalHHV=totalHHV.add(subSumHHV);
			totalMeterRent=totalMeterRent.add(subSumMeterRent);
			totalTotalBill=totalTotalBill.add(subSumTotalBill);
			
//    		pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",font3));
//			pcell.setColspan(2);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovActExpMin),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovActWithMin),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovBillunit),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovDiff),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(domGovTotalCon),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			
//			
//			
//			pcell=new PdfPCell(new Paragraph(taka_format.format(domGovValueOfActCon),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(taka_format.format(domGovMinCharge),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			
//			
//			pcell=new PdfPCell(new Paragraph(taka_format.format(domGovMeterRent),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(taka_format.format(domGovHHV),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			
//			
//			pcell=new PdfPCell(new Paragraph(taka_format.format(domGovTotalBill),font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			
//			
//			pcell=new PdfPCell(new Paragraph("",font3));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumActExpMin),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumActWithMin),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumBillunit),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumDiff),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(subSumTotalCon),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
//			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumValueOfActCon),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMinCharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumHHV),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(subSumTotalBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
//			
			pcell=new PdfPCell(new Paragraph("",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Total Sales Of "+Area.values()[Integer.valueOf(area)-1]+"(A+B)=",font3));
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActExpMin),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActWithMin),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillunit),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalDiff),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalTotalCon),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalValueOfActCon),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalMinCharge),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalMeterRent),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalHHV),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalTotalBill),font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("",font3));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			        
			        
			    
		} catch (Exception e){e.printStackTrace();}
 		finally{try{ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}conn = null;}
	        
		
	        
		
		document.add(datatable1);

		
	}
	
	private static void category_wise(Document document) throws DocumentException
	{
		Connection conn = ConnectionManager.getConnection();
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
		
		pcell=new PdfPCell(new Paragraph("Monthly Sales "+category_name+" For the month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year, ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		document.add(headlineTable);
		
	
		
		BigDecimal totalActExpMin=BigDecimal.ZERO;
		BigDecimal totalActWithMin=BigDecimal.ZERO;
		BigDecimal totalBillunit=BigDecimal.ZERO;
		BigDecimal totalDiff=BigDecimal.ZERO;
		BigDecimal totalTotalCon=BigDecimal.ZERO;
		BigDecimal totalValueOfActCon=BigDecimal.ZERO;
		BigDecimal totalMinCharge=BigDecimal.ZERO;
		BigDecimal totalHHV=BigDecimal.ZERO;
		BigDecimal totalMeterRent=BigDecimal.ZERO;
		BigDecimal totalTotalBill=BigDecimal.ZERO;
		
		
		
	
		
	
		
		PdfPTable datatable1 = new PdfPTable(8);
		
		datatable1.setWidthPercentage(100);
		datatable1.setWidths(new float[] {15,25,70,30,40,30,30,40});
		datatable1.setHeaderRows(3);
		
		
		pcell=new PdfPCell(new Paragraph("Sl.No",font3));
		pcell.setRowspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer Code",font3));
		pcell.setRowspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer Name",font3));
		pcell.setRowspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		
		
		pcell=new PdfPCell(new Paragraph("Monthly Gas Consumption m3",font3));
		//pcell.setColspan(5);
		pcell.setRowspan(3);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Amount in Taka",font3));
		pcell.setColspan(4);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
//
//		pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
//		pcell.setRowspan(2);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
//		pcell.setColspan(3);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
//		pcell.setRowspan(2);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("Rate",font3));
//		pcell.setRowspan(2);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Actual Gas Bill",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Minimum Gas Bill",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
//		pcell.setRowspan(2);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
	
		pcell=new PdfPCell(new Paragraph("Total",font3));
		pcell.setRowspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("Difference",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
		
//		pcell=new PdfPCell(new Paragraph("01",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("02",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph("03",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph("04",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("05",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("06",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("07=(06-05)",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("08=(04+05)",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph("rate",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph("09",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("10)",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("11",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
//		pcell=new PdfPCell(new Paragraph("12",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);		
//		pcell=new PdfPCell(new Paragraph("13=(08+09+10+11+12)",font3));
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph("1",font3));
		datatable1.addCell(pcell);
		pcell=new PdfPCell(new Paragraph(Area.values()[Integer.valueOf(area)-1]+" AREA",font3));
		pcell.setColspan(13);
		datatable1.addCell(pcell);
		
		int cusCatCount=97;
		String previousType="GOVT";
		
		String currnertType="";
		try {
		
			String sql1="select MCC.CATEGORY_ID,decode(SR.IS_METER,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),0,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
						",MCC.CATEGORY_TYPE,SR.IS_METER,FULL_NAME, SR.customer_id CODE,"+
						" ACTUAL_EXCEPT_MINIMUM, ACTUAL_WITH_MINIMUM, "+
						" BILLING_UNIT,  DIFFERENCE,TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
						" VALUE_OF_ACTUAL_CONSUMPTION,MINIMUM_CHARGE, METER_RENT, "+
						" SURCHARGE_AMOUNT, HHV_NHV_AMOUNT, TOTAL_AMOUNT "+
						"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc,CUSTOMER_PERSONAL_INFO cpi "+
						"where SR.customer_id=conn.customer_id and  SR.customer_id=cpi.customer_id and BILLING_MONTH=? and BILLING_YEAR=?"+
						"AND substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)=?"+ 
						"AND SUBSTR(SR.customer_id, 3, 2)=?"+
						"ORDER BY SR.customer_id";
    		PreparedStatement ps1=conn.prepareStatement(sql1);
    		ps1.setString(1, bill_month);
    		ps1.setString(2, bill_year);
    		ps1.setString(3, area);
    		ps1.setString(4, customer_category);
    		ResultSet rs1=ps1.executeQuery();    	
    		int count=1;
    		while(rs1.next())
        	{   
    			pcell=new PdfPCell(new Paragraph(String.valueOf(count++),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			pcell=new PdfPCell(new Paragraph(rs1.getString("CODE"),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			datatable1.addCell(pcell);
    			
    			pcell=new PdfPCell(new Paragraph(rs1.getString("FULL_NAME"),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
    			datatable1.addCell(pcell);
    			
    			
    			
//    			totalActExpMin=totalActExpMin.add(rs1.getBigDecimal("ACTUAL_EXCEPT_MINIMUM")); // new solution	    			  		
//    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getBigDecimal("ACTUAL_EXCEPT_MINIMUM")),font2));//new solution
//    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//    			datatable1.addCell(pcell);
//    			
//   			
//   			
//    			totalActWithMin=totalActWithMin.add(rs1.getFloat("ACTUAL_WITH_MINIMUM")==0?BigDecimal.ZERO:rs1.getBigDecimal("ACTUAL_WITH_MINIMUM"));
//    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("ACTUAL_WITH_MINIMUM")==0?BigDecimal.ZERO:rs1.getBigDecimal("ACTUAL_WITH_MINIMUM")),font2));
//    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//    			datatable1.addCell(pcell);
    			
//    			
//    			totalBillunit=totalBillunit.add(rs1.getFloat("BILLING_UNIT")==0?BigDecimal.ZERO:rs1.getBigDecimal("BILLING_UNIT"));
//    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("BILLING_UNIT")==0?BigDecimal.ZERO:rs1.getBigDecimal("BILLING_UNIT")),font2));
//    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//    			datatable1.addCell(pcell);
//    			
//    			
//    			totalDiff=totalDiff.add(rs1.getFloat("DIFFERENCE")==0?BigDecimal.ZERO:rs1.getBigDecimal("DIFFERENCE"));
//    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("DIFFERENCE")==0?BigDecimal.ZERO:rs1.getBigDecimal("DIFFERENCE")),font2));
//    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//    			datatable1.addCell(pcell);
//    			
    			
    			totalTotalCon=totalTotalCon.add(rs1.getFloat("TOTAL_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_ACTUAL_CONSUMPTION"));
    			pcell=new PdfPCell(new Paragraph(consumption_format.format(rs1.getFloat("TOTAL_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_ACTUAL_CONSUMPTION")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
//    			pcell=new PdfPCell(new Paragraph(rs1.getString("RATE"),font2));
//    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//    			datatable1.addCell(pcell);
    			
    			
    			
    			totalValueOfActCon=totalValueOfActCon.add(rs1.getFloat("VALUE_OF_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("VALUE_OF_ACTUAL_CONSUMPTION"));
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("VALUE_OF_ACTUAL_CONSUMPTION")==0?BigDecimal.ZERO:rs1.getBigDecimal("VALUE_OF_ACTUAL_CONSUMPTION")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			totalMinCharge=totalMinCharge.add(rs1.getFloat("MINIMUM_CHARGE")==0?BigDecimal.ZERO:rs1.getBigDecimal("MINIMUM_CHARGE"));
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("MINIMUM_CHARGE")==0?BigDecimal.ZERO:rs1.getBigDecimal("MINIMUM_CHARGE")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			
    			
    			
    			totalMeterRent=totalMeterRent.add(rs1.getFloat("METER_RENT")==0?BigDecimal.ZERO:rs1.getBigDecimal("METER_RENT"));
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("METER_RENT")==0?BigDecimal.ZERO:rs1.getBigDecimal("METER_RENT")),font2));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    			

    			
//    			totalHHV=totalHHV.add(rs1.getFloat("HHV_NHV_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("HHV_NHV_AMOUNT"));
//    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("HHV_NHV_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("HHV_NHV_AMOUNT")),font2));
//    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//    			datatable1.addCell(pcell);
//    			
    			
    			
    			
    			totalTotalBill=totalTotalBill.add(rs1.getFloat("TOTAL_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_AMOUNT"));
    			pcell=new PdfPCell(new Paragraph(taka_format.format(rs1.getFloat("TOTAL_AMOUNT")==0?BigDecimal.ZERO:rs1.getBigDecimal("TOTAL_AMOUNT")),ReportUtil.f8B));
    			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			datatable1.addCell(pcell);
    		
    		
    			
    			
        	}	
    		
		} catch (Exception e){e.printStackTrace();}
 		finally{try{ConnectionManager.closeConnection(conn);} catch (Exception e)
			{e.printStackTrace();}conn = null;}
	        
		
    		
    		pcell=new PdfPCell(new Paragraph("Total:",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActExpMin),ReportUtil.f9B));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActWithMin),ReportUtil.f9B));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillunit),ReportUtil.f9B));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalDiff),ReportUtil.f9B));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalTotalCon),ReportUtil.f9B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalValueOfActCon),ReportUtil.f9B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalMinCharge),ReportUtil.f9B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalMeterRent),ReportUtil.f9B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph(taka_format.format(totalHHV),ReportUtil.f9B));
//			pcell.setColspan(1);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(totalTotalBill),ReportUtil.f9B));
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
		
		
			document.add(datatable1);
			        
				
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
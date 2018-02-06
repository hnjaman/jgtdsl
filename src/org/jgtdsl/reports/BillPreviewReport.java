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
import org.jgtdsl.dto.BankBookDTO;
import org.jgtdsl.dto.MeterReadingReportDTO;
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



public class BillPreviewReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	
    private static String area;
    private static String customer_category;
    private static String bill_month;
    private static String bill_year;
    private String report_for; 
    private static String category_name;

	ArrayList<MeterReadingReportDTO> billInfo = new ArrayList<MeterReadingReportDTO>();
	String sql = "";
	ArrayList<String>customerType=new ArrayList<String>();
	
	PreparedStatement ps=null;
	ResultSet rs=null;
	Connection conn =ConnectionManager.getConnection();



	public ServletContext servlet;
	ServletContext servletContext = null;

	static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	//static Font font4 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
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
			
			pcell=new PdfPCell(new Paragraph("(A Company of PetroBangla)", ReportUtil.f8B));
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
				//category_wise(document);
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
	
	
	private  void area_wise(Document document) throws DocumentException
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
		
		pcell=new PdfPCell(new Paragraph("Statement of Gas Sales For The Month Of "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year, ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		headlineTable.addCell(pcell);
		document.add(headlineTable);
		
		
		PdfPTable datatable1 = new PdfPTable(15);
		
		datatable1.setWidthPercentage(100);
		datatable1.setWidths(new float[] {30,60,30,30,20,30,30,30,30,20,30,30,20,20,30
		});
		
		
		pcell=new PdfPCell(new Paragraph("Customer Code",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer Name",font3));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Previous Reading",font3));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Present Reading",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Pressure Factor",font3));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Consumption",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Min Load",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Gas Bill",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Min Bill",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Avg. Bill against D/O",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
	
		pcell=new PdfPCell(new Paragraph("Total Bill",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);		
		
		pcell=new PdfPCell(new Paragraph("Meter + CMS Rent",font3));
		pcell.setRowspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);		
		
		pcell=new PdfPCell(new Paragraph("Surcharge",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Adjust",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Total Bill To Pay",font3));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		datatable1.addCell(pcell);
		
		
		
				
		}
	
	private ArrayList<MeterReadingReportDTO> getBillInfo()
	{
	ArrayList<MeterReadingReportDTO> billList=new ArrayList<MeterReadingReportDTO>();
	
	
	try {
		
		
		String wClause="";
		
		
		if(report_for.equals("area_wise"))
		{
			wClause="AND substr(mr.CUSTOMER_ID,1,2)='"+area+" '";
		}else if(report_for.equals("category_wise"))
		{
			wClause="AND substr(mr.CUSTOMER_ID,3,2)='"+customer_category+" '";
		}
	
		
		
		String transaction_sql="select mr.CUSTOMER_ID,bm.CUSTOMER_NAME,mr.PREV_READING,mr.CURR_READING,mr.PRESSURE_FACTOR, "+
				"bm.BILLED_CONSUMPTION,mr.MIN_LOAD,VALUE_OF_ACTUAL_CONSUMPTION,MINIMUM_CHARGE,BILLED_AMOUNT,null avg_bill, " +
				"bm.METER_RENT,bm.SURCHARGE_AMOUNT,ADJUSTMENT_AMOUNT,sr.TOTAL_AMOUNT,ZONE_NAME,ZONE,CUSTOMER.area, " +
				"MR.RATE,bm.ISSUE_DATE,bm.LAST_PAY_DATE_WO_SC,mcc.CATEGORY_ID,MCC.CATEGORY_NAME " +
				"from meter_reading mr,bill_metered bm,sales_report sr,summary_margin_pb smb,CUSTOMER,MST_ZONE MZ,MST_CUSTOMER_CATEGORY MCC " +
				"where MR.CUSTOMER_ID=BM.CUSTOMER_ID " +
				"AND MR.BILLING_MONTH=BM.BILL_MONTH " +
				"AND MR.BILLING_YEAR=BM.BILL_YEAR " +
				"AND BM.BILL_ID=SR.BILL_ID " +
				"AND BM.BILL_ID=SMB.BILL_ID " +
				"and CUSTOMER.ZONE=MZ.ZONE_ID " +
				"and CUSTOMER.AREA=MZ.AREA_ID " +
				"and BM.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID " +
				"AND substr(mr.customer_id,3,2)=mcc.CATEGORY_ID " +
				"AND BM.BILL_YEAR=2017 " +
				"AND BM.BILL_MONTH=10 " +wClause+
				"ORDER BY ZONE,MR.CUSTOMER_ID ASC "; 
 ;
				

		
		PreparedStatement ps1=conn.prepareStatement(transaction_sql);
	
    	
    	ResultSet resultSet=ps1.executeQuery();
    	
    	
    	while(resultSet.next())
    	{
    		MeterReadingReportDTO mDto=new MeterReadingReportDTO();

    		mDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
    		mDto.setCustomer_name(resultSet.getString("CUSTOMER_NAME"));
    		mDto.setPrevious_reading(resultSet.getDouble("PREV_READING"));
    		mDto.setPresent_reading(resultSet.getDouble("CURR_READING"));
    		mDto.setPressure_factor(resultSet.getDouble("PRESSURE_FACTOR"));
    		mDto.setConsumption(resultSet.getDouble("BILLED_CONSUMPTION"));
    		mDto.setMin_load(resultSet.getDouble("MIN_LOAD"));
    		mDto.setGas_bill(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
    		mDto.setMin_bill(resultSet.getDouble("MINIMUM_CHARGE"));
    		mDto.setAvg_bill(resultSet.getDouble("avg_bill"));
    		mDto.setTotal_bill(resultSet.getDouble("BILLED_AMOUNT"));
    		mDto.setMeter_CMS_rent(resultSet.getDouble("METER_RENT"));
    		mDto.setSurcharge(resultSet.getDouble("SURCHARGE_AMOUNT"));
    		mDto.setAdjust(resultSet.getDouble("ADJUSTMENT_AMOUNT"));
    		mDto.setTotal_bill_to_pay(resultSet.getDouble("TOTAL_AMOUNT"));
    		mDto.setIssue_date(resultSet.getString("ISSUE_DATE"));
    		mDto.setDue_date(resultSet.getString("LAST_PAY_DATE_WO_SC"));
    		mDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
    		mDto.setRate(resultSet.getDouble("RATE"));
    		mDto.setMohollah(resultSet.getString("ZONE_NAME"));
    		mDto.setZone_id(resultSet.getString("ZONE"));
    		mDto.setZone_name(resultSet.getString("ZONE_NAME"));
    		mDto.setCategory_id(resultSet.getString("CATEGORY_ID"));
    		

    		
    		billList.add(mDto);
    		
    	}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return billList;
	}	
	
	


	}
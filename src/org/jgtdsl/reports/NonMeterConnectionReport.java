package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class NonMeterConnectionReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<NonMeterReportDTO> connectionList=new ArrayList<NonMeterReportDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String report_for2;
	    private  String from_date; 
	    private  String to_date; 
	    private  String customer_type;
	    private  String category_name;
	    DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");

	public String execute() throws Exception
	{
		
		DepositService depositeService = new  DepositService();
		
		
		
		
		String fileName="ConnectionInformation.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(5,5,5,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			NonMeterReportDTO loadIncraseDTO = new NonMeterReportDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
			headerTable.setWidthPercentage(100);
		   
				
			headerTable.setWidths(new float[] {
				5,190,5
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			pcell.setBorderWidthBottom(1);
			headerTable.addCell(pcell);
			
			//for logo
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png");  // image path
			   Image img = Image.getInstance(realPath);
			      
			             //img.scaleToFit(10f, 200f);
			             //img.scalePercent(200f);
			            img.scaleAbsolute(28f, 31f);
			            img.setAbsolutePosition(270f, 557f);  
			             //img.setAbsolutePosition(290f, 540f);  // rotate
			            
			         document.add(img);
			
			
			
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
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
			pcell.setBorderWidthBottom(1);
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			pcell.setBorderWidthBottom(1);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			if (report_for.equals("area_wise")) {
				
				if (customer_type.equals("01")) {
					getMeterCustomerReconnectionInfo(document);
				} else if(customer_type.equals("02")){
					getNonMeterCustomerReconnectionInfo(document);
				}
				
			} else if(report_for.equals("category_wise")){
				
				if (customer_category.equals("01") || customer_category.equals("09")) {
					getNonMeterCustomerReconnectionInfo(document);
				} else {
					getMeterCustomerReconnectionInfo(document);
				}
				
			}
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	private void getMeterCustomerReconnectionInfo(Document document) throws DocumentException
	{
		/////////////////////////////////////////////////////
		
		//////////////////Meter Customer//////////////////////
		
		////////////////////////////////////////////////////
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		String headLine="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,80,30});
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		
		
		if(report_for2.equals("date_wise"))
		{
			headLine="CONNECTION INFORMATION (METER) FROM DATE "+from_date+" TO DATE "+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="CONNECTION INFORMATION (METER) FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="CONNECTION INFORMATION (METER) FOR YEAR OF "+bill_year;
		}
		
		
		pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f11B));
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
	
		connectionList =getMeterCustomerReconnectionList();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_amount=0;
      
		int expireListSize=connectionList.size();
		String previousCustomerCategoryName=new String("");
		if(expireListSize>0){
		for(int i=0;i<expireListSize;i++)
		{
			NonMeterReportDTO loadIncraseDTO = connectionList.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
					pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
												
					
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(10);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					document.add(ptable);
				
				
					totalRecordsPerCategory=0;
					total_burner=0;
					total_amount=0;
				}
				
			}
			ptable = new PdfPTable(11);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{15,20,35,45,25,30,30,30,30,30,30});
			ptable.setSpacingBefore(10);
			
			if(i==0)// only for very beginng of the table for printing Area name
			{
				pcell=new PdfPCell(new Paragraph("Area/Region Name:"+String.valueOf(Area.values()[Integer.valueOf(area)-1]),ReportUtil.f11B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(10);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
			}
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(10);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Sr.No",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Code",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Address",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			
			pcell=new PdfPCell(new Paragraph("Monthly Load",ReportUtil.f9B));
			pcell.setColspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Meter Type",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Pressure Factor",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter No.",ReportUtil.f9B));
			//pcell.setColspan(3);
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
		
			
		
			
			pcell=new PdfPCell(new Paragraph("Date of Connection",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Remarks",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum",ReportUtil.f9B));		
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Maximum",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("Meter No.",ReportUtil.f9B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph("Initial Reading",ReportUtil.f9B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph("Present",ReportUtil.f9B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
												
			}
			
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory+1),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getCustomer_id(),ReportUtil.f8B));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getName_address(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getAddress(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);					
			

			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getMin_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getMax_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getMeter_type(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getPressure_factor()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getMeter_sl_no(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getPrevious_reading()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getCurrent_reading()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
			
			
			
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getEffective_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
		
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			totalRecordsPerCategory++;
			
			
		}
	}
		else{
			ptable = new PdfPTable(11);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{15,20,35,45,25,30,30,30,30,30,30});
			ptable.setSpacingBefore(10);
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
									

		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(10);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		document.add(ptable);
	
		
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
	}
	
	private void getNonMeterCustomerReconnectionInfo(Document document) throws DocumentException
	{
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		String headLine="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,80,30});
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		if(report_for2.equals("date_wise"))
		{
			headLine="CONNECTION INFORMATION FROM DATE"+from_date+" TO DATE"+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="CONNECTION INFORMATION FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="CONNECTION INFORMATION FOR YEAR OF "+bill_year;
		}
		
		pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f11B));
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
		
		connectionList =getNonMeterCustomerConnectionList();

		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_amount=0;

		int expireListSize=connectionList.size();
		String previousCustomerCategoryName=new String("");
		
		for(int i=0;i<expireListSize;i++)
		{
			NonMeterReportDTO loadIncraseDTO = connectionList.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
					pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(3);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
												
					pcell=new PdfPCell(new Paragraph("Total Burner:",ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(1);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(String.valueOf(total_burner),ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(1);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					
//					pcell=new PdfPCell(new Paragraph("Total Amount:",ReportUtil.f11B));
//					pcell.setMinimumHeight(18f);
//					pcell.setColspan(2);
//					pcell.setBorder(0);
//					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//					ptable.addCell(pcell);
//					
//					pcell=new PdfPCell(new Paragraph(taka_format.format(total_amount),ReportUtil.f11B));
//					pcell.setMinimumHeight(18f);
//					pcell.setColspan(1);
//					pcell.setBorder(0);
//					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//					ptable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					document.add(ptable);
				
					totalRecordsPerCategory=0;
					total_burner=0;
					total_amount=0;
				}
				
			}
			ptable = new PdfPTable(7);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{15,30,45,55,25,30,40});
			ptable.setSpacingBefore(10);
			
			if(i==0)// only for very beginng of the table for printing Area name/// 
			{
				pcell=new PdfPCell(new Paragraph("Area/Region Name:"+String.valueOf(Area.values()[Integer.valueOf(area)-1]),ReportUtil.f11B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(6);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
			}
			
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(4);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Sr.No",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Address",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Burner Qnt ",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Effective Date",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Comments",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			}
			
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory+1),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getCustomer_id(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getFull_name(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getAddress(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getCustomer_id(),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
			
			total_burner=total_burner+loadIncraseDTO.getNew_burner_qnt();
			pcell = new PdfPCell(new Paragraph(String.valueOf(loadIncraseDTO.getNew_burner_qnt()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getEffective_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
//			total_amount=total_amount+loadIncraseDTO.getPertial_bill();
//			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getPertial_bill()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
//			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getComments(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			totalRecordsPerCategory++;
			
			
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(3);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
									
		pcell=new PdfPCell(new Paragraph("Total Burner:",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph(String.valueOf(total_burner),ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
//		pcell=new PdfPCell(new Paragraph("Total Amount:",ReportUtil.f11B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(2);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph(taka_format.format(total_amount),ReportUtil.f11B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(1);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		document.add(ptable);
	
		
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
		
	}
	
	private ArrayList<NonMeterReportDTO> getMeterCustomerReconnectionList()
	{
	ArrayList<NonMeterReportDTO> disconnCustomerList=new ArrayList<NonMeterReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";
												
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(MR.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				//wClause="And substr(MR.customer_id,1,2)="+area+" And substr(MR.customer_id,3,2)="+customer_category;
				wClause=" And substr(MR.customer_id,3,2)="+customer_category;
			}
			
			if(report_for2.equals("date_wise"))
			{
				w2Clause=" And CONNECTION_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				w2Clause=" And to_char(to_date(CONNECTION_DATE,'dd-mm-YYYY'),'MM')="+bill_month+" and to_char(to_date(CONNECTION_DATE,'dd-mm-YYYY'),'YYYY')="+bill_year;
			}else if(report_for2.equals("year_wise"))
			{
				w2Clause=" And to_char(to_date(CONNECTION_DATE,'dd-mm-YYYY'),'YYYY')="+bill_year;
			}
			
			
		
			
			
			String defaulterSql="select MR.CUSTOMER_ID,mci.FULL_NAME,mci.CATEGORY_ID,mci.CATEGORY_NAME,mci.ADDRESS,METER_SL_NO,CONNECTION_DATE INSTALLED_DATE,INSTALLED_DATE INSTALLED_DATE_ORDER,PMIN_LOAD,PMAX_LOAD,MR.METER_ID " +
					",PREV_READING,CURR_READING,ACTUAL_CONSUMPTION,MR.METER_RENT,0.0 PERCIAL_BILL, MR.PRESSURE_FACTOR, MT.TYPE_NAME  " +
					"from METER_READING MR ,CUSTOMER_METER CM,MVIEW_CUSTOMER_INFO mci ,MST_METER_TYPE MT " +
					"where  MR.METER_ID=CM.METER_ID  " +
					" AND MR.CUSTOMER_ID=mci.CUSTOMER_ID " +
					" AND (MR.CUSTOMER_ID,MR.PREV_READING_DATE) in(  " +
					"select CUSTOMER_ID,CURR_READING_DATE from METER_READING where CUSTOMER_ID=MR.CUSTOMER_ID AND METER_ID=MR.METER_ID and READING_PURPOSE=0)  " +wClause+w2Clause+
					"AND CM.METER_TYPE = MT.TYPE_ID order by mci.CATEGORY_ID,INSTALLED_DATE_ORDER,MR.CUSTOMER_ID " ;

					
					
			//System.out.println(defaulterSql);
					
					
//					
//					"select MR.CUSTOMER_ID,mci.FULL_NAME,mci.CATEGORY_ID,mci.CATEGORY_NAME,METER_SL_NO,to_char(INSTALLED_DATE,'dd-MM-YYYY') INSTALLED_DATE,INSTALLED_DATE INSTALLED_DATE_ORDER,PMIN_LOAD,PMAX_LOAD,MR.METER_ID"+
//						",PREV_READING,CURR_READING,ACTUAL_CONSUMPTION,MR.METER_RENT,0.0 PERCIAL_BILL " +
//						"from METER_READING MR ,CUSTOMER_METER CM,MVIEW_CUSTOMER_INFO mci " +
//						"where  MR.METER_ID=CM.METER_ID " +
//						" AND MR.CUSTOMER_ID=mci.CUSTOMER_ID" +
//						" AND (MR.CUSTOMER_ID,MR.PREV_READING_DATE) in( " +
//						"select CUSTOMER_ID,CURR_READING_DATE from METER_READING where CUSTOMER_ID=MR.CUSTOMER_ID AND METER_ID=MR.METER_ID and READING_PURPOSE=0) " +wClause+w2Clause+
//						"order by mci.CATEGORY_ID,INSTALLED_DATE_ORDER,MR.CUSTOMER_ID" ;






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		NonMeterReportDTO disconnDto=new NonMeterReportDTO();
        		disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		disconnDto.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		disconnDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		disconnDto.setName_address(resultSet.getString("FULL_NAME"));
        		disconnDto.setMin_load(resultSet.getFloat("PMIN_LOAD"));
        		disconnDto.setMax_load(resultSet.getFloat("PMAX_LOAD"));
        		disconnDto.setMeter_sl_no(resultSet.getString("METER_SL_NO"));
        		disconnDto.setPrevious_reading(resultSet.getFloat("PREV_READING"));
        		disconnDto.setCurrent_reading(resultSet.getFloat("CURR_READING"));
        		disconnDto.setActual_consumption(resultSet.getFloat("ACTUAL_CONSUMPTION"));
        		disconnDto.setEffective_date(resultSet.getString("INSTALLED_DATE"));
        		disconnDto.setMeter_rent(resultSet.getFloat("METER_RENT"));
        		disconnDto.setPertial_bill(resultSet.getFloat("PERCIAL_BILL"));
        		disconnDto.setPressure_factor(resultSet.getFloat("PRESSURE_FACTOR"));
        		disconnDto.setAddress(resultSet.getString("ADDRESS"));
        		disconnDto.setMeter_type(resultSet.getString("TYPE_NAME"));
        		
        		
        		
   
        		
        		disconnCustomerList.add(disconnDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return disconnCustomerList;
	}


	private ArrayList<NonMeterReportDTO> getNonMeterCustomerConnectionList()
	{
	ArrayList<NonMeterReportDTO> loadIncraseInfo=new ArrayList<NonMeterReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(bqcM.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(bqcM.customer_id,1,2)="+area+" And substr(bqcM.customer_id,3,2)="+customer_category;
			}
			
			if(report_for2.equals("date_wise"))
			{
				w2Clause=" And EFFECTIVE_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				w2Clause=" And to_char(EFFECTIVE_DATE,'mm')="+bill_month+" and to_char(EFFECTIVE_DATE,'YYYY')="+bill_year;
			}else if(report_for2.equals("year_wise"))
			{
				w2Clause=" And to_char(EFFECTIVE_DATE,'YYYY')="+bill_year;
			}
			
			String defaulterSql=" " +
					"  SELECT bqcM.CUSTOMER_ID, " +
					"         MCC.CATEGORY_TYPE, " +
					"         MCC.CATEGORY_ID, " +
					"         MCC.CATEGORY_NAME, " +
					"         cpi.Full_name,ca.ADDRESS_LINE1 ADDRESS, " +
					"         NEW_APPLIANCE_QNT BURNER_QNT, " +
					"         to_char(EFFECTIVE_DATE,'dd-MM-YYYY') EFFECTIVE_DATE,EFFECTIVE_DATE  EFFECTIVE_DATE_ORDER ," +
					"         calculate_partial_bill (bqcM.CUSTOMER_ID,NEW_APPLIANCE_QNT,'C', " +
					"                                 TO_CHAR (EFFECTIVE_DATE, 'dd-MM-YYYY'),BQCM.APPLIANCE_TYPE_CODE) " +
					"            Partial_bill " +
					"    FROM (SELECT * " +
					"            FROM burner_qnt_change bqc " +
					"           WHERE     pid = (SELECT MIN (pid) " +
					"                              FROM burner_qnt_change " +
					"                             WHERE CUSTOMER_ID = bqc.CUSTOMER_ID) " +
					"                 AND TO_CHAR (EFFECTIVE_DATE, 'YYYY') = "+bill_year+") bqcM, " +
					"         customer_personal_info cpi, " +
					"         CUSTOMER_ADDRESS ca, " +
					"         MST_CUSTOMER_CATEGORY mcc " +
					"   WHERE     bqcM.customer_id = cpi.customer_id " +
					"         AND bqcM.customer_id = ca.customer_id " +
					"         AND SUBSTR (bqcM.customer_id, 3, 2) = MCC.CATEGORY_ID " +wClause+w2Clause+
					"ORDER BY MCC.CATEGORY_ID, EFFECTIVE_DATE_ORDER " ;


			//System.out.println(defaulterSql);

			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		NonMeterReportDTO loadIncraseDTO=new NonMeterReportDTO();
        		loadIncraseDTO.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		loadIncraseDTO.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		loadIncraseDTO.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		//loadIncraseDTO.setName_address(resultSet.getString("NAME_ADDRESS"));
        		loadIncraseDTO.setFull_name(resultSet.getString("Full_name"));
        		loadIncraseDTO.setAddress(resultSet.getString("ADDRESS"));
        		loadIncraseDTO.setNew_burner_qnt(resultSet.getInt("BURNER_QNT"));
        		loadIncraseDTO.setPertial_bill(resultSet.getFloat("Partial_bill"));
        		loadIncraseDTO.setEffective_date(resultSet.getString("EFFECTIVE_DATE"));
        		loadIncraseDTO.setComments("");
        		
        		
   
        		
        		loadIncraseInfo.add(loadIncraseDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loadIncraseInfo;
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


	public String getReport_for2() {
		return report_for2;
	}


	public void setReport_for2(String report_for2) {
		this.report_for2 = report_for2;
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


	public String getCustomer_type() {
		return customer_type;
	}


	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}
	
	
	
	public ServletContext getServlet() {
		  return servlet;
		 }

		 public void setServletContext(ServletContext servlet) {
		  this.servlet = servlet;
		 }


	
  }



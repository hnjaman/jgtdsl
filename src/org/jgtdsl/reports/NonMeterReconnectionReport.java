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
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.ReconnectDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;



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




public class NonMeterReconnectionReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<NonMeterReportDTO> reconnectionList=new ArrayList<NonMeterReportDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String area;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String category_name;
	    private  String report_for2;
	    private  String from_date; 
	    private  String to_date; 
	    private  String customer_type;
		private NonMeterReportDTO reconnectionDto;

		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");
	public String execute() throws Exception
	{
		
		
		
		
		String fileName="Reconnection_info.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(5,5,60,72);
		PdfPCell pcell=null;
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				5,190,5
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
//for logo
			
			
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png");  // image path
			   Image img = Image.getInstance(realPath);
			      
			             //img.scaleToFit(10f, 200f);
			             //img.scalePercent(200f);
			            img.scaleAbsolute(28f, 31f);
			            img.setAbsolutePosition(270f, 502f);  
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
			headerTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			document.add(headerTable);
			
			if (report_for.equals("area_wise")) {
				
				if (customer_type.equals("01")) {
					getMeterCustomerReconnectionInfo(document);
				} else if(customer_type.equals("02")){
					getNonMeterCustomerReconnectionInfo(document);
				}
				
			} else if(report_for.equals("category_wise")){
					getMeterCustomerReconnectionInfo(document);
				if (customer_category.equals("01") || customer_category.equals("09")) {
					getNonMeterCustomerReconnectionInfo(document);
				} else {
					
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
			headLine="RECONNECTION INFORMATION (METER) FROM DATE "+from_date+" TO DATE "+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="RECONNECTION INFORMATION (METER) FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="RECONNECTION INFORMATION (METER) FOR YEAR OF "+bill_year;
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
	
		reconnectionList =getMeterCustomerReconnectionList();
		
		String reconn_Date="";
		double minLoad=0.0;
		String lastDay="";
		
		double last_day=0.0;
		double day_diff=0.0;
		double pminLoad=0.0;
		double partialBill=0.0;
		double first_day=0.0;
		double actualConsumtion=0.0;
		double rate=0.0;
		
		
		int totalRecordsPerCategory=0;

		int expireListSize=reconnectionList.size();
		String previousCustomerCategoryName=new String("");
		if(expireListSize>0){
		for(int i=0;i<expireListSize;i++)
		{
			NonMeterReportDTO loadIncraseDTO = reconnectionList.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			
			reconn_Date=loadIncraseDTO.getEffective_date();
			minLoad=loadIncraseDTO.getMin_load();
			lastDay=loadIncraseDTO.getLastDay();
			
			String firstDay [] =reconn_Date.split("-");
			String dayDiff=firstDay[0].toString();
			
			last_day=Double.parseDouble(lastDay);
			first_day=Double.parseDouble(dayDiff);
			day_diff=last_day-first_day+1;
			
			pminLoad=(day_diff*minLoad)/last_day;
			actualConsumtion=loadIncraseDTO.getActual_consumption();
			rate = loadIncraseDTO.getRate();
			
			if(pminLoad>actualConsumtion){
				partialBill=pminLoad*rate;
			}else{
				partialBill=actualConsumtion*rate;
			}
			
			
			
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
					pcell.setColspan(8);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					document.add(ptable);
				
				
					totalRecordsPerCategory=0;
				
				}
				
			}
			ptable = new PdfPTable(7);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{10,25,40,25,50,25,30});
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
				pcell.setColspan(8);
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
			pcell.setColspan(8);
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
			
		
			
			pcell=new PdfPCell(new Paragraph("Paid Due Amount",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("Meter Reading",ReportUtil.f9B));
//			pcell.setColspan(2);
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Connection Status",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f9B));
//			pcell.setRowspan(2);
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Date of Reconnection",ReportUtil.f9B));
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
			
//			pcell=new PdfPCell(new Paragraph("Previous",ReportUtil.f9B));		
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
//			
//			pcell=new PdfPCell(new Paragraph("On Reconn. Date ",ReportUtil.f9B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
//			
												
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
			

		
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(0),ReportUtil.f8));
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
//			
			
			pcell = new PdfPCell(new Paragraph(DisconnCause.values()[loadIncraseDTO.getConn_status()].getLabel(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
//			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getMeter_rent()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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
			//fixing null pointer exception for meter ~ Prince
			ptable = new PdfPTable(7);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{10,25,40,25,50,25,30});
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
		pcell.setColspan(8);
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
		headLinetable.setWidths(new float[]{30,50,30});
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		if(report_for2.equals("date_wise"))
		{
			headLine="RECONNECTION INFORMATION FROM DATE"+from_date+" TO DATE"+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="RECONNECTION INFORMATION FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="RECONNECTION INFORMATION FOR YEAR OF "+bill_year;
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
		
		reconnectionList =getNonMeterCustomerReconnectionList();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_amount=0;

		int expireListSize=reconnectionList.size();
		String previousCustomerCategoryName=new String("");
		if(expireListSize>0){
		for(int i=0;i<expireListSize;i++)
		{
			reconnectionDto = reconnectionList.get(i);
			String currentCustomerCategoryName=reconnectionDto.getCustomer_category_name();
			
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
												
//					pcell=new PdfPCell(new Paragraph("Total Burner:",ReportUtil.f9B));
//					pcell.setMinimumHeight(18f);
//					pcell.setColspan(1);
//					pcell.setBorder(0);
//					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//					ptable.addCell(pcell);
//					
//					pcell=new PdfPCell(new Paragraph(String.valueOf(total_burner),ReportUtil.f9B));
//					pcell.setMinimumHeight(18f);
//					pcell.setColspan(1);
//					pcell.setBorder(0);
//					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//					ptable.addCell(pcell);
					
//					pcell=new PdfPCell(new Paragraph("Total Amount:",ReportUtil.f9B));
//					pcell.setMinimumHeight(18f);
//					pcell.setColspan(2);
//					pcell.setBorder(0);
//					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//					ptable.addCell(pcell);
//					
//					pcell=new PdfPCell(new Paragraph(taka_format.format(total_amount),ReportUtil.f9B));
//					pcell.setMinimumHeight(18f);
//					pcell.setColspan(1);
//					pcell.setBorder(0);
//					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//					ptable.addCell(pcell);
					
					total_amount=0;
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(1);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					document.add(ptable);
				}
				
			}
			ptable = new PdfPTable(7);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{10,30,75,25,60,30,30});
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
				pcell.setColspan(6);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
			}
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f9B));
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
			
//			pcell=new PdfPCell(new Paragraph("Address",ReportUtil.f9B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//			ptable.addCell(pcell);
//			
			
			
			pcell=new PdfPCell(new Paragraph("Paid Due Amount",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Connection Status",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Date of Reconnection",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Remarks",ReportUtil.f9B));
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
			
			
			
			pcell = new PdfPCell(new Paragraph(reconnectionDto.getCustomer_id(),ReportUtil.f8B));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(reconnectionDto.getFull_name(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell = new PdfPCell(new Paragraph(reconnectionDto.getAddress(),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
			
//paid due amount is added according to old_jalalabad_software .
//current system doesn't have data for paid_due_amount
			//if paid_due_amount is added later then we will put it in the report
			// ~oct 18 ~ Prince
			
			//total_burner=total_burner+reconnectionDto.getBurner_qnt();
			pcell = new PdfPCell(new Paragraph(taka_format.format(0),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			total_amount=total_amount+reconnectionDto.getPertial_bill();
			pcell = new PdfPCell(new Paragraph(DisconnCause.values()[ Integer.valueOf(reconnectionDto.getReconn_cause())].getLabel(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph(reconnectionDto.getEffective_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			
			pcell = new PdfPCell(new Paragraph(reconnectionDto.getComments(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
		
			previousCustomerCategoryName=reconnectionDto.getCustomer_category_name();
			totalRecordsPerCategory++;
			
			
		}
	}
		else{
			//fixing null pointer exception ~dec 13 ~ Prince
			ptable = new PdfPTable(7);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{10,30,75,25,60,30,30});
			ptable.setSpacingBefore(10);
		}
	/*	[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(6);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
									
//		pcell=new PdfPCell(new Paragraph("Total Burner:",ReportUtil.f9B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(1);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph(String.valueOf(total_burner),ReportUtil.f9B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(1);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
		
//		pcell=new PdfPCell(new Paragraph("Total Amount:",ReportUtil.f9B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(2);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
		
//		pcell=new PdfPCell(new Paragraph(taka_format.format(total_amount),ReportUtil.f9B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(1);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		document.add(ptable);
	
		
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
		
		
		
		
		
		
	}
//bqc.appliance_type_code in calculate partial bill is added later ~sept 26 ~Prince	
	private ArrayList<NonMeterReportDTO> getNonMeterCustomerReconnectionList()
	{
	ArrayList<NonMeterReportDTO> reconnectionList=new ArrayList<NonMeterReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(bqc.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(bqc.customer_id,1,2)="+area+" And substr(bqc.customer_id,3,2)="+customer_category;
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
			
			String defaulterSql="SELECT bqc.CUSTOMER_ID, " +
					"       MCC.CATEGORY_TYPE, " +
					"       MCC.CATEGORY_ID, " +
					"       MCC.CATEGORY_NAME, " +
					"       cpi.Full_name ,ca.ADDRESS_LINE1 ADDRESS, " +
					"       bqc.NEW_RECONN_QNT_4M_PERMANENT BURNER_QNT,bqc.RECONNECTION_CAUSE, " +
					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, " +
					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_RECONN_QNT_4M_PERMANENT,'R', " +
					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY'),bqc.APPLIANCE_TYPE_CODE) " +
					"          Partial_bill ,REMARKS" +
					"  FROM burner_qnt_change bqc, " +
					"       customer_personal_info cpi, " +
					"       CUSTOMER_ADDRESS ca, " +
					"       MST_CUSTOMER_CATEGORY mcc " +
					" WHERE     bqc.customer_id = cpi.customer_id " +
					"       AND bqc.customer_id = ca.customer_id " +
					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +w2Clause+
					" and NEW_RECONN_QNT_4M_PERMANENT>0   " +wClause+
					"union all " +
					"SELECT bqc.CUSTOMER_ID, " +
					"       MCC.CATEGORY_TYPE, " +
					"       MCC.CATEGORY_ID, " +
					"       MCC.CATEGORY_NAME, " +
					"       cpi.Full_name, ca.ADDRESS_LINE1 ADDRESS, " +
					"       bqc.NEW_RECONN_QNT_4M_TEMPORARY BURNER_QNT, bqc.RECONNECTION_CAUSE, " +
					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, " +
					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_RECONN_QNT_4M_TEMPORARY,'R', " +
					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY'),bqc.APPLIANCE_TYPE_CODE) " +
					"          Partial_bill,REMARKS " +
					"  FROM burner_qnt_change bqc, " +
					"       customer_personal_info cpi, " +
					"       CUSTOMER_ADDRESS ca, " +
					"       MST_CUSTOMER_CATEGORY mcc " +
					" WHERE     bqc.customer_id = cpi.customer_id " +
					"       AND bqc.customer_id = ca.customer_id " +
					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +w2Clause+
					"       and NEW_RECONN_QNT_4M_TEMPORARY>0   " +wClause+
					
					" union all " +
					"SELECT bqc.CUSTOMER_ID, " +
					"       MCC.CATEGORY_TYPE, " +
					"       MCC.CATEGORY_ID, " +
					"       MCC.CATEGORY_NAME, " +
					"       cpi.Full_name, ca.ADDRESS_LINE1 ADDRESS, " +
					"       bqc.NEW_RECONN_QNT_4M_TEMP_HALF BURNER_QNT, bqc.RECONNECTION_CAUSE," +
					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, " +
					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_RECONN_QNT_4M_TEMP_HALF,'R', " +
					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY'),bqc.APPLIANCE_TYPE_CODE) " +
					"          Partial_bill,REMARKS " +
					"  FROM burner_qnt_change bqc, " +
					"       customer_personal_info cpi, " +
					"       CUSTOMER_ADDRESS ca, " +
					"       MST_CUSTOMER_CATEGORY mcc " +
					" WHERE     bqc.customer_id = cpi.customer_id " +
					"       AND bqc.customer_id = ca.customer_id " +
					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +w2Clause+
					"       and NEW_RECONN_QNT_4M_TEMP_HALF>0   " +wClause+
					"order by CATEGORY_ID,EFFECTIVE_DATE,CUSTOMER_ID "
					;



			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		NonMeterReportDTO reconnectionDto=new NonMeterReportDTO();
        		reconnectionDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		reconnectionDto.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		reconnectionDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		//reconnectionDto.setName_address(resultSet.getString("NAME_ADDRESS"));
        		reconnectionDto.setFull_name(resultSet.getString("Full_name"));
        		reconnectionDto.setAddress(resultSet.getString("ADDRESS"));
        		//reconnectionDto.setBurner_qnt(resultSet.getInt("BURNER_QNT"));
        		reconnectionDto.setPertial_bill(resultSet.getFloat("Partial_bill"));
        		reconnectionDto.setEffective_date(resultSet.getString("EFFECTIVE_DATE"));
        		reconnectionDto.setComments(resultSet.getString("REMARKS"));
        		reconnectionDto.setReconn_cause(resultSet.getString("RECONNECTION_CAUSE"));
        		
        		
   
        		
        		reconnectionList.add(reconnectionDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reconnectionList;
	}
	
	private ArrayList<NonMeterReportDTO> getMeterCustomerReconnectionList()
	{
	ArrayList<NonMeterReportDTO> disconnCustomerList=new ArrayList<NonMeterReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(rm.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(rm.customer_id,1,2)="+area+" And substr(rm.customer_id,3,2)="+customer_category;
			}
			
			if(report_for2.equals("date_wise"))
			{
				w2Clause=" And RECONNECT_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				w2Clause=" And to_char(RECONNECT_DATE,'mm')="+bill_month+" and to_char(RECONNECT_DATE,'YYYY')="+bill_year;
			}else if(report_for2.equals("year_wise"))
			{
				w2Clause=" And to_char(RECONNECT_DATE,'YYYY')="+bill_year;
			}
			
			
		
			
			
			String defaulterSql="SELECT CAST(to_char(LAST_DAY(to_date('01-'||to_char(to_date(RECONNECT_DATE,'dd-MM-YYYY'), 'mm')||'-'||to_char(to_date(RECONNECT_DATE,'dd-MM-YYYY'), 'YYYY'),'dd-MM-YYYY')),'dd') AS INT) LASTDAY, " +
					"         rm.CUSTOMER_ID, " +
					"         mci.FULL_NAME, " +
					"         mci.CATEGORY_ID, " +
					"         mci.CATEGORY_NAME, " +
					"         TO_CHAR (RECONNECT_DATE, 'dd-MM-YYYY') RECONNECT_DATE, " +
					"         RECONNECT_DATE RECONNECT_DATE_ORDER, " +
					"         rm.READING_ID, " +
					"         NVL (RATE, 0) RATE, " +
					"         MR.MIN_LOAD, " +
					"         NVL (PREV_READING, 0) PREV_READING, " +
					"         CURR_READING, " +
					"         DIFFERENCE, mci.CONNECTION_STATUS," +
					"         ACTUAL_CONSUMPTION, " +
					"         DECODE (TO_CHAR (RECONNECT_DATE, 'MM'), " +
					"                 TO_CHAR (DISCONNECT_DATE, 'MM'), " +
					"                 METER_RENT) " +
					"            METER_RENT " +
					"    FROM RECONN_METERED rm, " +
					"         METER_READING MR, " +
					"         MVIEW_CUSTOMER_INFO mci, " +
					"         DISCONN_METERED DM " +
					"   WHERE     rm.reading_id = mr.reading_id " +
					"         AND rm.customer_id = mci.customer_id " +
					"         AND RM.DISCONNECT_ID = DM.PID " +wClause+w2Clause+
					"ORDER BY RECONNECT_DATE_ORDER ASC, rm.CUSTOMER_ID ASC " ;

			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		NonMeterReportDTO disconnDto=new NonMeterReportDTO();
        		disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		disconnDto.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		disconnDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		disconnDto.setName_address(resultSet.getString("FULL_NAME"));
        		disconnDto.setMin_load(resultSet.getFloat("MIN_LOAD"));
        		disconnDto.setPrevious_reading(resultSet.getFloat("PREV_READING"));
        		disconnDto.setCurrent_reading(resultSet.getFloat("CURR_READING"));
        		disconnDto.setActual_consumption(resultSet.getFloat("ACTUAL_CONSUMPTION"));
        		disconnDto.setEffective_date(resultSet.getString("RECONNECT_DATE"));
        		disconnDto.setMeter_rent(resultSet.getFloat("METER_RENT"));
        		disconnDto.setLastDay(resultSet.getString("LASTDAY"));
        		disconnDto.setRate(resultSet.getDouble("RATE"));
        		disconnDto.setConn_status(resultSet.getInt("CONNECTION_STATUS"));
        		
        		
   
        		
        		disconnCustomerList.add(disconnDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return disconnCustomerList;
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



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
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.DisconnCause;
import org.jgtdsl.enums.DisconnCauseNonMeter;
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




public class NonMeterDisconnectionReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<NonMeterReportDTO> disconnectionCustomerlist=new ArrayList<NonMeterReportDTO>();
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
	   
	    
	    DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		DecimalFormat factor_format=new DecimalFormat("##########0.000");

	public String execute() throws Exception
	{
		
		
		
		
		String fileName="DisconnectionCustomerList.pdf";
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
			            img.setAbsolutePosition(268f, 500f);  
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
					getMeterCustomerDisconnectionInfo(document);
				} else if(customer_type.equals("02")){
					getNonMeterCustomerDisconnectionInfo(document);
				}
				
			} else if(report_for.equals("category_wise")){
				
				if (customer_category.equals("01") || customer_category.equals("09")) {
					getNonMeterCustomerDisconnectionInfo(document);
				} else {
					getMeterCustomerDisconnectionInfo(document);
				}
				
			}
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	private void getMeterCustomerDisconnectionInfo(Document document) throws DocumentException
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
			headLine="DISCONNECTION INFORMATION (METER) FROM DATE "+from_date+" TO DATE "+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="DISCONNECTION INFORMATION (METER) FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
			
		}else if(report_for2.equals("year_wise"))
		{
			headLine="DISCONNECTION INFORMATION (METER) FOR YEAR OF "+bill_year;
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
	
        disconnectionCustomerlist =getMeterCustomerDisconnectionList();
		
		int totalRecordsPerCategory=0;
		
		String disconn_Date="";
		double minLoad=0.0;
		String lastDay="";
		
		double last_day=0.0;
		double day_diff=0.0;
		double pminLoad=0.0;
		double partialBill=0.0;
		double actualConsumtion=0.0;
		double rate=0.0;
//editing
        double dueAmount;
		int expireListSize=disconnectionCustomerlist.size();
		String previousCustomerCategoryName=new String("");
		if(expireListSize>0){
		for(int i=0;i<expireListSize;i++)
		{
			NonMeterReportDTO loadIncraseDTO = disconnectionCustomerlist.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			
			
			disconn_Date=loadIncraseDTO.getEffective_date();
			minLoad=loadIncraseDTO.getMin_load();
			lastDay=loadIncraseDTO.getLastDay();
			
			String firstDay [] =disconn_Date.split("-");
			String dayDiff=firstDay[0].toString();
			
			last_day=Double.parseDouble(lastDay);
			day_diff=Double.parseDouble(dayDiff);
			
			pminLoad=(day_diff*minLoad)/last_day;
			actualConsumtion=loadIncraseDTO.getActual_consumption();
			rate = loadIncraseDTO.getRate();
			
//for due amount 
			String da []=loadIncraseDTO.getDue_amount().split("#");
			dueAmount=Double.parseDouble(da[0]);
//making string for disconnCause			
			//String dc =" "+ DisconnCause.values()[Integer.parseInt(loadIncraseDTO.getDisconn_cause())].getLabel();
			int s=Integer.parseInt(loadIncraseDTO.getDisconn_cause());
			String dc =DisconnCause.values()[s].getLabel();
			//String dc = DisconnCause.values()[1];
			//String dd = Area.values()[24];
			
			
			
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
			
			ptable = new PdfPTable(8);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{10,25,35,20,55,30,30,30});
			ptable.setSpacingBefore(10);
			
			if(i==0)// only for very beginng of the table for printing Area name
			{
				pcell=new PdfPCell(new Paragraph("Area/Region Name:"+String.valueOf(Area.values()[Integer.valueOf(area)-1]),ReportUtil.f11B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(3);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
//EDITING				
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
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
//EDITING
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
			
			
			//EDITING 
			pcell=new PdfPCell(new Paragraph("Due Amount",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Disconnection Type",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Date of Disconnection",ReportUtil.f9B));
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("Minimum Load up to Disconn. Date",ReportUtil.f9B));
//			pcell.setRowspan(2);
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Reading at Disconnection",ReportUtil.f9B));
			//pcell.setColspan(2);
			pcell.setRowspan(2);
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("Actual Gas Consumption",ReportUtil.f9B));
//			pcell.setRowspan(2);
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
			
		

			
//			pcell=new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
//			pcell.setRowspan(2);
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
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
			
//			pcell=new PdfPCell(new Paragraph("On Disconn. Date ",ReportUtil.f9B));
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
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(dueAmount),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			//pcell = new PdfPCell(new Paragraph( toString(  DisconnCause.values()[ Integer.parseInt(loadIncraseDTO.getDisconn_cause())]) ,ReportUtil.f8));
			pcell = new PdfPCell(new Paragraph( dc ,ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getEffective_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getMin_load()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
//			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getPrevious_reading()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
//			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getCurrent_reading()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
//			pcell = new PdfPCell(new Paragraph(consumption_format.format(loadIncraseDTO.getActual_consumption()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
//			pcell = new PdfPCell(new Paragraph(taka_format.format(loadIncraseDTO.getMeter_rent()),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
			
//editing			
//			pcell = new PdfPCell(new Paragraph(taka_format.format(partialBill),ReportUtil.f8));
//			//pcell = new PdfPCell(new Paragraph(taka_format.format(dueAmount),ReportUtil.f8));
//			pcell.setMinimumHeight(16f);
//			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			
			
			
			
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f8));
			//pcell = new PdfPCell(new Paragraph(taka_format.format(dueAmount),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
		
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			totalRecordsPerCategory++;
			
			
		}
	}else{
		ptable = new PdfPTable(8);
		ptable.setWidthPercentage(100);
		ptable.setWidths(new float[]{10,25,35,20,55,30,30,30});
		ptable.setSpacingBefore(10);
	}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(3);
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
	private String toString(DisconnCause disconnCause) {
		// TODO Auto-generated method stub
		return null;
	}
	private void getNonMeterCustomerDisconnectionInfo(Document document) throws DocumentException
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
			headLine="DISCONNECTION INFORMATION FROM DATE "+from_date+" TO DATE "+to_date;
		}else if(report_for2.equals("month_wise"))
		{
			headLine="DISCONNECTION INFORMATION FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise"))
		{
			headLine="DISCONNECTION INFORMATION FOR YEAR OF "+bill_year;
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
		
		disconnectionCustomerlist =getNonMeterDisconnectedCustomerList();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		float total_amount=0;

		int expireListSize=disconnectionCustomerlist.size();
		String previousCustomerCategoryName=new String("");
		String previousDisconnType=new String("");
	    String previousDisconnCause=new String("");
		
	    
	    if(expireListSize>0){
	    	
		for(int i=0;i<expireListSize;i++)
		{
			NonMeterReportDTO loadIncraseDTO = disconnectionCustomerlist.get(i);
			String currentCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			String currentDissConnType=loadIncraseDTO.getDisconn_type();
			String currentDissConnCause=loadIncraseDTO.getDisconn_cause();
			
		
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
//					pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
//					pcell.setMinimumHeight(18f);
//					pcell.setColspan(2);
//					pcell.setBorder(0);
//					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
//					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//					ptable.addCell(pcell);
//												
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
//					
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
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(7);
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
			ptable.setWidths(new float[]{15,24,45,55,30,30,40});
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
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
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
			
			pcell=new PdfPCell(new Paragraph("Address",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Disconnected Burner Qty. ",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Disconnection Date",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
//			pcell=new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
//			pcell.setMinimumHeight(18f);
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Remarks",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
		
			}
			
			if (!currentDissConnType.equals(previousDisconnType))
			{
				String disconn_heading ;
				if(currentDissConnType.replaceFirst("^0+(?!$)", "").equalsIgnoreCase("1"))
				{
					disconn_heading="Permanent Disconnection :";
				}
				else if(currentDissConnType.replaceFirst("^0+(?!$)", "").equalsIgnoreCase("3"))
				{
					disconn_heading="Temporary to Permanent Disconnection :";
				}
				else
				{
					disconn_heading="Temporary Disconnection :";
				}
			//	pcell=new PdfPCell(new Paragraph(currentDissConnType.replaceFirst("^0+(?!$)", "").equalsIgnoreCase("1")||currentDissConnType.replaceFirst("^0+(?!$)", "").equalsIgnoreCase("3")?"Permanent Disconnection :":"Temporary Disconnection:",ReportUtil.f11B));
				pcell=new PdfPCell(new Paragraph(disconn_heading,ReportUtil.f11B));
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
			}
			

			if (!currentDissConnCause.equals(previousDisconnCause))
			{
				pcell=new PdfPCell(new Paragraph(String.valueOf(DisconnCauseNonMeter.values()[Integer.valueOf(currentDissConnCause)]),ReportUtil.f11B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(3);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setColspan(4);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
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
			//pcell = new PdfPCell(new Paragraph(loadIncraseDTO.getComments(),ReportUtil.f8));
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
		
			previousCustomerCategoryName=loadIncraseDTO.getCustomer_category_name();
			previousDisconnType=loadIncraseDTO.getDisconn_type();
			previousDisconnCause=loadIncraseDTO.getDisconn_cause();
			totalRecordsPerCategory++;
			
			
		}
		}else{
			//fixing null pointer exception
			ptable = new PdfPTable(10);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[] { 10, 25, 35, 65, 25, 30, 30, 30,
					30, 30 });
			ptable.setSpacingBefore(10);
			//end of fix
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
//		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f9B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(2);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
//									
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
//		
//		pcell=new PdfPCell(new Paragraph("Total Amount:",ReportUtil.f9B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(2);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
//		
//		pcell=new PdfPCell(new Paragraph(taka_format.format(total_amount),ReportUtil.f9B));
//		pcell.setMinimumHeight(18f);
//		pcell.setColspan(1);
//		pcell.setBorder(0);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
//		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(7);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		document.add(ptable);
	
		
		/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
		
		
		
		
		
	}
	
	private ArrayList<NonMeterReportDTO> getMeterCustomerDisconnectionList()
	{
	ArrayList<NonMeterReportDTO> disconnCustomerList=new ArrayList<NonMeterReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(dm.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(dm.customer_id,1,2)="+area+" And substr(dm.customer_id,3,2)="+customer_category;
			}
			
			if(report_for2.equals("date_wise"))
			{
				w2Clause=" And DISCONNECT_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}else if(report_for2.equals("month_wise"))
			{
				w2Clause=" And to_char(DISCONNECT_DATE,'mm')="+bill_month+" and to_char(DISCONNECT_DATE,'YYYY')="+bill_year;
			}else if(report_for2.equals("year_wise"))
			{
				w2Clause=" And to_char(DISCONNECT_DATE,'YYYY')="+bill_year;
			}
			
			
		
			
			
			String defaulterSql=  "SELECT CAST ( " +
					"            TO_CHAR ( " +
					"               LAST_DAY ( " +
					"                  TO_DATE ( " +
					"                        '01-' " +
					"                     || TO_CHAR (TO_DATE (DISCONNECT_DATE, 'dd-MM-YYYY'), 'mm') " +
					"                     || '-' " +
					"                     || TO_CHAR (TO_DATE (DISCONNECT_DATE, 'dd-MM-YYYY'), " +
					"                                 'YYYY'), " +
					"                     'dd-MM-YYYY')), " +
					"               'dd') AS INT) " +
					"            LASTDAY, " +
					"         dm.CUSTOMER_ID, " +
					"         mci.FULL_NAME, " +
					"         mci.CATEGORY_ID, " +
					"         mci.CATEGORY_NAME, " +
					"         DISCONNECT_CAUSE, " +
					"         TO_CHAR (DISCONNECT_DATE, 'dd-MM-YYYY') DISCONNECT_DATE, " +
					"         DISCONNECT_DATE DISCONNECT_DATE_ORDER, " +
					"         dm.READING_ID, " +
					"         NVL (RATE, 0) RATE, " +
					"         MR.MIN_LOAD, " +
					"         NVL (PREV_READING, 0) PREV_READING, " +
					"         CURR_READING, " +
					"         DIFFERENCE, " +
					"         ACTUAL_CONSUMPTION, " +
					"         METER_RENT, " +
					"         ROUND (DIFFERENCE * RATE, 0) PERCIAL_BILL, " +
					"          getDueMonthMeter ( " +
					"            dm.customer_id, " +
					"               TO_CHAR (DISCONNECT_DATE, 'YYYY') " +
					"            || TO_CHAR (DISCONNECT_DATE, 'mm')) " +
					"            DUEMONTH " +
					"    FROM DISCONN_METERED dm, METER_READING MR, MVIEW_CUSTOMER_INFO mci " +
					"   WHERE     dm.reading_id = mr.reading_id " +
					"         AND dm.customer_id = mci.customer_id " +wClause+w2Clause+
					"ORDER BY CATEGORY_ID,DISCONNECT_DATE_ORDER ASC " ;

					
			
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
        		disconnDto.setEffective_date(resultSet.getString("DISCONNECT_DATE"));
        		disconnDto.setMeter_rent(resultSet.getFloat("METER_RENT"));
        		disconnDto.setPertial_bill(resultSet.getFloat("PERCIAL_BILL"));
        		disconnDto.setLastDay(resultSet.getString("LASTDAY"));
        		disconnDto.setRate(resultSet.getDouble("RATE"));
//for matching with jalalabad old report ~Oct 11 ~ Prince
        		disconnDto.setDue_amount(resultSet.getString("DUEMONTH"));
        		disconnDto.setDue_months(resultSet.getString("DUEMONTH"));
        		disconnDto.setDisconn_cause(resultSet.getString("DISCONNECT_CAUSE"));
        		
        		
        		
   
        		
        		disconnCustomerList.add(disconnDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return disconnCustomerList;
	}
	private ArrayList<NonMeterReportDTO> getNonMeterDisconnectedCustomerList()
	{
	ArrayList<NonMeterReportDTO> loadIncraseInfo=new ArrayList<NonMeterReportDTO>();
		
		try {
			String wClause="";
			String w2Clause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(bqc.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause=" And substr(bqc.customer_id,1,2)="+area+" And substr(bqc.customer_id,3,2)="+customer_category;
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
			
//,BQC.APPLIANCE_TYPE_CODE IS ADDED TO PARTIAL BILL CALC.			
			String defaulterSql="SELECT bqc.CUSTOMER_ID, " +
					"       MCC.CATEGORY_TYPE, " +
					"       MCC.CATEGORY_ID, " +
					"       MCC.CATEGORY_NAME, " +
					"       cpi.Full_name ,ca.ADDRESS_LINE1 ADDRESS, " +
					"       bqc.NEW_PERMANENT_DISCON_QNT BURNER_QNT, " +
					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, bqc.EFFECTIVE_DATE EFFECTIVE_DATE_ORDER ," +
					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_PERMANENT_DISCON_QNT,'D', " +
					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY'),BQC.APPLIANCE_TYPE_CODE) " +
					"          Partial_bill ,REMARKS,BQC.DISCONN_TYPE,DISCONN_CAUSE" +
					"  FROM burner_qnt_change bqc, " +
					"       customer_personal_info cpi, " +
					"       CUSTOMER_ADDRESS ca, " +
					"       MST_CUSTOMER_CATEGORY mcc " +
					"  WHERE     bqc.customer_id = cpi.customer_id " +
					"       AND bqc.customer_id = ca.customer_id " +
					"       AND DISCONN_TYPE<>3"+
					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +
					w2Clause+
					"  and NEW_PERMANENT_DISCON_QNT>0   " +wClause+
					"  union all " +
					"SELECT bqc.CUSTOMER_ID, " +
					"       MCC.CATEGORY_TYPE, " +
					"       MCC.CATEGORY_ID, " +
					"       MCC.CATEGORY_NAME, " +
					"       cpi.Full_name ,ca.ADDRESS_LINE1 ADDRESS, " +
					"       bqc.NEW_PERMANENT_DISCON_QNT BURNER_QNT, " +
					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, bqc.EFFECTIVE_DATE EFFECTIVE_DATE_ORDER ," +
					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_PERMANENT_DISCON_QNT,'T_P_D_N', " +
					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY'),BQC.APPLIANCE_TYPE_CODE) " +
					"          Partial_bill ,REMARKS,BQC.DISCONN_TYPE,DISCONN_CAUSE" +
					"  FROM burner_qnt_change bqc, " +
					"       customer_personal_info cpi, " +
					"       CUSTOMER_ADDRESS ca, " +
					"       MST_CUSTOMER_CATEGORY mcc " +
					"  WHERE     bqc.customer_id = cpi.customer_id " +
					"       AND bqc.customer_id = ca.customer_id " +
					"    AND DISCONN_TYPE=3"+
					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +
					w2Clause+
					"  and NEW_PERMANENT_DISCON_QNT>0   " +wClause+
					
					"  union all " +
					"  SELECT bqc.CUSTOMER_ID, " +
					"       MCC.CATEGORY_TYPE, " +
					"       MCC.CATEGORY_ID, " +
					"       MCC.CATEGORY_NAME, " +
					"       cpi.Full_name,ca.ADDRESS_LINE1 ADDRESS, " +
					"       bqc.NEW_TEMPORARY_DISCONN_QNT BURNER_QNT, " +
					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, bqc.EFFECTIVE_DATE EFFECTIVE_DATE_ORDER, " +
					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_TEMPORARY_DISCONN_QNT,'D', " +
					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY'),BQC.APPLIANCE_TYPE_CODE) " +
					"          Partial_bill ,REMARKS,BQC.DISCONN_TYPE,DISCONN_CAUSE" +
					"  FROM burner_qnt_change bqc, " +
					"       customer_personal_info cpi, " +
					"       CUSTOMER_ADDRESS ca, " +
					"       MST_CUSTOMER_CATEGORY mcc " +
					"  WHERE     bqc.customer_id = cpi.customer_id " +
					"       AND bqc.customer_id = ca.customer_id " +
					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +
					w2Clause+
					"       and NEW_TEMPORARY_DISCONN_QNT>0   " +wClause+
					" order by CATEGORY_ID,DISCONN_TYPE,DISCONN_CAUSE,EFFECTIVE_DATE_ORDER ,CUSTOMER_ID" ;
					
					
					
					
					
					
					
					
					
					
					
					
					
					
//					"SELECT bqc.CUSTOMER_ID, " +
//					"       MCC.CATEGORY_TYPE, " +
//					"       MCC.CATEGORY_ID, " +
//					"       MCC.CATEGORY_NAME, " +
//					"       cpi.Full_name ,ca.ADDRESS_LINE1 ADDRESS, " +
//					"       bqc.NEW_PERMANENT_DISCON_QNT BURNER_QNT, " +
//					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, bqc.EFFECTIVE_DATE EFFECTIVE_DATE_ORDER ," +
//					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_PERMANENT_DISCON_QNT,'D', " +
//					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY')) " +
//					"          Partial_bill ,REMARKS,BQC.DISCONN_TYPE,DISCONN_CAUSE" +
//					"  FROM burner_qnt_change bqc, " +
//					"       customer_personal_info cpi, " +
//					"       CUSTOMER_ADDRESS ca, " +
//					"       MST_CUSTOMER_CATEGORY mcc " +
//					"  WHERE     bqc.customer_id = cpi.customer_id " +
//					"       AND bqc.customer_id = ca.customer_id " +
//					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +
//					w2Clause+
//					"  and NEW_PERMANENT_DISCON_QNT>0   " +wClause+
//					"  union all " +
//					"  SELECT bqc.CUSTOMER_ID, " +
//					"       MCC.CATEGORY_TYPE, " +
//					"       MCC.CATEGORY_ID, " +
//					"       MCC.CATEGORY_NAME, " +
//					"       cpi.Full_name,ca.ADDRESS_LINE1 ADDRESS, " +
//					"       bqc.NEW_TEMPORARY_DISCONN_QNT BURNER_QNT, " +
//					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, bqc.EFFECTIVE_DATE EFFECTIVE_DATE_ORDER, " +
//					"       calculate_partial_bill (bqc.CUSTOMER_ID,bqc.NEW_TEMPORARY_DISCONN_QNT,'D', " +
//					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY')) " +
//					"          Partial_bill ,REMARKS,BQC.DISCONN_TYPE,DISCONN_CAUSE" +
//					"  FROM burner_qnt_change bqc, " +
//					"       customer_personal_info cpi, " +
//					"       CUSTOMER_ADDRESS ca, " +
//					"       MST_CUSTOMER_CATEGORY mcc " +
//					"  WHERE     bqc.customer_id = cpi.customer_id " +
//					"       AND bqc.customer_id = ca.customer_id " +
//					"       AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID " +
//					w2Clause+
//					"       and NEW_TEMPORARY_DISCONN_QNT>0   " +wClause+
//					" order by CATEGORY_ID,DISCONN_TYPE,DISCONN_CAUSE,EFFECTIVE_DATE_ORDER ,CUSTOMER_ID" ;



			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		NonMeterReportDTO loadIncraseDTO=new NonMeterReportDTO();
        		loadIncraseDTO.setDisconn_type(resultSet.getString("DISCONN_TYPE"));
        		loadIncraseDTO.setDisconn_cause(resultSet.getString("DISCONN_CAUSE"));
        		loadIncraseDTO.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		loadIncraseDTO.setCustomer_category_id(resultSet.getString("CATEGORY_ID"));
        		loadIncraseDTO.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		//loadIncraseDTO.setName_address(resultSet.getString("NAME_ADDRESS"));
        		loadIncraseDTO.setFull_name(resultSet.getString("Full_name"));
        		loadIncraseDTO.setAddress(resultSet.getString("ADDRESS"));
        		loadIncraseDTO.setNew_burner_qnt(resultSet.getInt("BURNER_QNT"));
        		loadIncraseDTO.setPertial_bill(resultSet.getFloat("Partial_bill"));
        		loadIncraseDTO.setEffective_date(resultSet.getString("EFFECTIVE_DATE"));
        		loadIncraseDTO.setComments(resultSet.getString("REMARKS"));
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



package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.DisconnectDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;

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

public class DisconnectionDetails extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	DisconnectDTO disconnectDTO=null;
	ArrayList<DisconnectDTO>disconnectList=new ArrayList<DisconnectDTO>();
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
    private  String disconnect_type;
    
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	

	public String execute() throws Exception
	{
				
		String fileName="DisconnectionReport.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
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

			Chunk chunk1 = new Chunk("Regional Office :",ReportUtil.f8B);
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
			
			if(customer_type.equals("01")){
				generatePdfMeteredCustomer(document);
			}else {
				generatePdfNonmeteredCustomer(document);
			}
			
			
			
			
			
			
			
			/* End of the table */
			
			
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	private void generatePdfMeteredCustomer(Document document) throws DocumentException{
		
		String headLine="";
		PdfPTable headLinetable = null;
		PdfPCell pcell = null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{5,90,5});
		
		if(report_for2.equals("date_wise")){
			headLine="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ARREAR GAS BILL & RECEIVABLE AMOUNT FOR THE DAY OF "+from_date+" TO "+to_date;
		}else if(report_for2.equals("month_wise")){
			headLine="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ARREAR GAS BILL & RECEIVABLE AMOUNT FOR THE MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
		}else if(report_for2.equals("year_wise")){
			headLine="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ARREAR GAS BILL & RECEIVABLE AMOUNT FOR THE MONTH OF "+bill_year;
		}
		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f9B));
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headLinetable.addCell(pcell);
		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		PdfPTable pTable = new PdfPTable(16);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{3,8,6,6,6,6,6,6,4,5,6,6,6,8,8,10});
		
		String previousCategory= new String("");
		double readingDifference=0.0;
		double actualGasConsumption=0.0;
		double totalReceivable=0.0;
		
		double totalActualConsumption=0.0;
		double totalMeterRent=0.0;
		double totalPartialBill=0.0;
		double totalReceivableBill=0.0;
		
		String dueBillMonth="";
		double billAmount=0.0;
		String psrt2="";
		String part1="";
		String firstPart="";
		
		disconnectList=getdisconnectionListMetered();
		int listSize=disconnectList.size();
		for (int i = 0; i < listSize; i++) {
			
			String currentCategory=disconnectList.get(i).getCustomerCategory();
			
			dueBillMonth=disconnectList.get(i).getDueMonth();
			String[] parts=dueBillMonth.split("#");
			int size=parts.length;
			if(size==0)
			{
				firstPart="0";
				psrt2="";
			}else
			{
				part1=parts[0];
				int part1Size=part1.length();
				if(part1Size==0){
					firstPart="0";
					psrt2=parts[1].toString();
				}else{
					firstPart=parts[0];
					psrt2=parts[1].toString();
				}
				
			}
			
			billAmount=Double.parseDouble(firstPart);
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Area/Region",ReportUtil.f11B));
				pcell.setColspan(3);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(":"+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(13);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Category",ReportUtil.f11B));
				pcell.setColspan(3);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(":"+disconnectList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(13);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
				pcell.setRowspan(3);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Date of Disconn.",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Min. Load Upto Disconn. (SCM)",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Reading",ReportUtil.f9B));
				pcell.setColspan(3);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Psig",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Pressure Factor",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Actual Gas Consum. (SCM)",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setRowspan(2);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Dues Gas Bill",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Total Receivable Bill",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Name of Due/Pending Gas Bill",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("On Disconn. Date",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Previous"  ,ReportUtil.f9B)); 
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Difference",ReportUtil.f9B)); 
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("1" ,ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("2",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("3",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("4",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("5",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("6",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("7=(5-6)",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("8",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("9",ReportUtil.f9)); 
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("10=(7*9)",ReportUtil.f9)); 
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("11" ,ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("12=(10*Rate)+11",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("13",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("14=(12+13)",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("15",ReportUtil.f9));
				pcell.setColspan(1);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
				
					pcell = new PdfPCell(new Paragraph("Area/Region",ReportUtil.f11B));
					pcell.setColspan(3);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(":"+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
					pcell.setBorder(0);
					pcell.setColspan(13);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category",ReportUtil.f11B));
					pcell.setColspan(3);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(":"+disconnectList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
					pcell.setBorder(0);
					pcell.setColspan(13);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
					pcell.setRowspan(3);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Date of Disconn.",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Min. Load Upto Disconn. (SCM)",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Meter Reading",ReportUtil.f9B));
					pcell.setColspan(3);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Psig",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Pressure Factor",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Actual Gas Consum. (SCM)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setRowspan(2);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Dues Gas Bill",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Total Receivable Bill",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("Name of Due/Pending Gas Bill",ReportUtil.f9B));
					pcell.setRowspan(2);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("On Disconn. Date",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Previous"  ,ReportUtil.f9B)); 
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Difference",ReportUtil.f9B)); 
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("1" ,ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("2",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("3",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("4",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("5",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("6",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("7=(5-6)",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("8",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("9",ReportUtil.f9)); 
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("10=(7*9)",ReportUtil.f9)); 
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("11" ,ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("12=(10*Rate)+11",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("13",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("14=(12+13)",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("15",ReportUtil.f9));
					pcell.setColspan(1);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pTable.addCell(pcell);
				
				
				}
				previousCategory=disconnectList.get(i).getCustomerCategory();
				
			}
			
			/////////////////////////////////////////////////////////////////////////////////////////////////
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(disconnectList.get(i).getCustomer_name(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(disconnectList.get(i).getCustomer_id(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(disconnectList.get(i).getDisconnect_date(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getMinimumLoadSCM()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getDisconnDateReading()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getPreviousReading()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			readingDifference=disconnectList.get(i).getDisconnDateReading()-disconnectList.get(i).getPreviousReading();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(readingDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getPsig()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getPressureFactor()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			actualGasConsumption=readingDifference*disconnectList.get(i).getPressureFactor();
			pcell = new PdfPCell(new Paragraph(consumption_format.format(actualGasConsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalActualConsumption=totalActualConsumption+actualGasConsumption;
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getMeterRent()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalMeterRent=totalMeterRent+disconnectList.get(i).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getPartialBill()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalPartialBill=totalPartialBill+disconnectList.get(i).getPartialBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(billAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			totalReceivable=disconnectList.get(i).getPartialBill()+billAmount;
			pcell = new PdfPCell(new Paragraph(consumption_format.format(totalReceivable),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalReceivableBill=totalReceivableBill+billAmount;
			
			pcell = new PdfPCell(new Paragraph(psrt2,ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
		}
		
		pcell = new PdfPCell(new Paragraph("Total=",ReportUtil.f9B));
		pcell.setColspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setColspan(8);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalActualConsumption),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalMeterRent),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalPartialBill),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalReceivableBill),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalReceivableBill),ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);

		
		document.add(pTable);
	}
	
private void generatePdfNonmeteredCustomer(Document document) throws DocumentException{
		
		PdfPTable headLinetable = null;
		PdfPCell pcell = null;
		String headline="";
		
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{5,90,5});
		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		pcell.setMinimumHeight(18f);
		headLinetable.addCell(pcell);
		
		if(disconnect_type.equals("01")){
		
			if(report_for2.equals("date_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ARREAR GAS BILL & RECEIVABLE AMOUNT FOR THE DAY OF "+from_date+" TO "+to_date;
			}else if(report_for2.equals("month_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ARREAR GAS BILL & RECEIVABLE AMOUNT FOR THE MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
			}else if(report_for2.equals("year_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ARREAR GAS BILL & RECEIVABLE AMOUNT FOR THE MONTH OF "+bill_year;
			}
		}else if(disconnect_type.equals("02")){
			
			if(report_for2.equals("date_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ILLEGAL OPERATION GAS  & RECEIVABLE AMOUNT FOR THE DAY OF "+from_date+" TO "+to_date;
			}else if(report_for2.equals("month_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ILLEGAL OPERATION GAS & RECEIVABLE AMOUNT FOR THE MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
			}else if(report_for2.equals("year_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR ILLEGAL OPERATION GAS & RECEIVABLE AMOUNT FOR THE MONTH OF "+bill_year;
			}
			
		}else if(disconnect_type.equals("03")){
			
			if(report_for2.equals("date_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR APPLIED BY CUSTOMER  & RECEIVABLE AMOUNT FOR THE DAY OF "+from_date+" TO "+to_date;
			}else if(report_for2.equals("month_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR APPLIED BY CUSTOMER & RECEIVABLE AMOUNT FOR THE MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
			}else if(report_for2.equals("year_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR APPLIED BY CUSTOMER & RECEIVABLE AMOUNT FOR THE MONTH OF "+bill_year;
			}
			
		}else if(disconnect_type.equals("04")){
			
			if(report_for2.equals("date_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR GOVT. TRANSFER & RECEIVABLE AMOUNT FOR THE DAY OF "+from_date+" TO "+to_date;
			}else if(report_for2.equals("month_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR GOVT. TRANSFER & RECEIVABLE AMOUNT FOR THE MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year;
			}else if(report_for2.equals("year_wise")){
				headline="THE STATEMENT SHOWING THE DISCONNECTED CUSTOMER INFORMATION FOR GOVT. TRANSFER & RECEIVABLE AMOUNT FOR THE MONTH OF "+bill_year;
			}
			
		}
		
		
		
		pcell=new PdfPCell(new Paragraph(headline,ReportUtil.f11B));
		pcell.setBorder(0);
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headLinetable.addCell(pcell);
		
		
		pcell=new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		pcell.setMinimumHeight(18f);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		PdfPTable pTable=new PdfPTable(9);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{3,15,8,9,6,9,9,9,32});
		pTable.setHeaderRows(2);
		
		
		
		
		/////////////////////////////////////////////////
		
		double partialBill=0.0;
		double duesBill=0.0;
		double receivableBill=0.0;
		
		String previousCategory=new String("");
		String dueBillMonth="";
		double billAmount=0.0;
		double totalReceivableAmt=0.0;
		String psrt2="";
		String part1="";
		String firstPart="";
		
		disconnectList=getdisconnectionListArrear();
		int listSize=disconnectList.size();		
		
		
		for (int i = 0; i < listSize; i++) {
			
			String currentCategory=disconnectList.get(i).getCustomerCategory();
			
			dueBillMonth=disconnectList.get(i).getDueMonth();
			String[] parts=dueBillMonth.split("#");
			int size=parts.length;
			if(size==0)
			{
				firstPart="0";
				psrt2="";
			}else
			{
				part1=parts[0];
				int part1Size=part1.length();
				if(part1Size==0){
					firstPart="0";
					psrt2=parts[1].toString();
				}else{
					firstPart=parts[0];
					psrt2=parts[1].toString();
				}
				
			}
			
			
			
			billAmount=Double.parseDouble(firstPart);
			
			totalReceivableAmt=billAmount+disconnectList.get(i).getPartialBill();
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Area/Region",ReportUtil.f11B));
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(":"+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(7);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Category",ReportUtil.f11B));
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(":"+disconnectList.get(i).getCustomerCategoryType(),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(7);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Date of Disconnection",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Burner Qnty",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Dues Gas Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Total Receivable Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Name of Due/Pending Gas Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("1",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("2",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("3",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("4",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("5={(4*650)/ month}*days",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("6",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("7=(5+6)"  ,ReportUtil.f9B)); 
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("8",ReportUtil.f9B)); 
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
				
				pcell = new PdfPCell(new Paragraph("Area/Region",ReportUtil.f11B));
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(":"+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(7);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Category",ReportUtil.f11B));
				pcell.setColspan(2);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(":"+disconnectList.get(i).getCustomerCategoryType(),ReportUtil.f11B));
				pcell.setBorder(0);
				pcell.setColspan(7);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f9B));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Date of Disconnection",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Burner Qnty)",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Partial Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Dues Gas Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Total Receivable Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("Name of Due/Pending Gas Bill",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("1",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("2",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("3",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("4",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph("5={(4*650)/ month}*days",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("6",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("7=(5+6)"  ,ReportUtil.f9B)); 
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("8",ReportUtil.f9B)); 
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pTable.addCell(pcell);
				
				
				}
				previousCategory=disconnectList.get(i).getCustomerCategory();
				
			}
			
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(disconnectList.get(i).getCustomer_name(),ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(disconnectList.get(i).getCustomer_id(),ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(disconnectList.get(i).getDisconnect_date(),ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getBurnerQnty()),ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			partialBill=partialBill+disconnectList.get(i).getPartialBill();
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(disconnectList.get(i).getPartialBill()),ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			duesBill=duesBill+billAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(billAmount),ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			receivableBill=receivableBill+totalReceivableAmt;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalReceivableAmt),ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(psrt2,ReportUtil.f9));
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);
			
			
			
		}
		pcell = new PdfPCell(new Paragraph("Total =",ReportUtil.f9B));
		pcell.setColspan(2);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(partialBill),ReportUtil.f9B));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(duesBill),ReportUtil.f9B));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(receivableBill),ReportUtil.f9B));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pTable.addCell(pcell);
		
		

		
		document.add(pTable);
	}



	
	private ArrayList<DisconnectDTO> getdisconnectionListArrear(){
		ArrayList<DisconnectDTO> disconnectionList= new ArrayList<DisconnectDTO>();
		try {
			
			String wClause="";
			String w2Clause="";
			String w3Clause="";
			
			if(report_for.equals("area_wise")){
				wClause="AND SUBSTR(bqc.CUSTOMER_ID,1,2)="+area+"AND SUBSTR (bqc.customer_id, 3, 2) = MCC.CATEGORY_ID";
			}else if(report_for.equals("category_wise")){
				wClause="AND SUBSTR(bqc.CUSTOMER_ID,1,2)="+area+"AND SUBSTR(bqc.CUSTOMER_ID,3,2)="+customer_type;
			}
			if(disconnect_type.equals("01")){
				
				w3Clause=" AND DISCONN_CAUSE=1 ";
				
			}else if(disconnect_type.equals("02")){
				
				w3Clause=" AND DISCONN_CAUSE=0 ";
				
			}else if(disconnect_type.equals("03")){
				w3Clause=" AND DISCONN_CAUSE=2 ";
			}else if(disconnect_type.equals("04")){
				w3Clause=" AND DISCONN_CAUSE=3 ";
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
			
			
			
			String disconnection_info_sql="SELECT bqc.CUSTOMER_ID, " +
					"       MCC.CATEGORY_TYPE, " +
					"       MCC.CATEGORY_ID, " +
					"       MCC.CATEGORY_NAME, " +
					"       cpi.Full_name, " +
					"       bqc.NEW_PERMANENT_DISCON_QNT+bqc.NEW_TEMPORARY_DISCONN_QNT BURNER_QNT, " +
					"       TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY') EFFECTIVE_DATE, " +
					"       bqc.EFFECTIVE_DATE EFFECTIVE_DATE_ORDER, " +
					"       calculate_partial_bill (bqc.CUSTOMER_ID, " +
					"                               bqc.NEW_PERMANENT_DISCON_QNT+bqc.NEW_TEMPORARY_DISCONN_QNT, " +
					"                               'D', " +
					"                               TO_CHAR (bqc.EFFECTIVE_DATE, 'dd-MM-YYYY')) " +
					"          Partial_bill, " +
					"       REMARKS,getDueMonth(bqc.customer_id,TO_CHAR (EFFECTIVE_DATE, 'YYYY')||TO_CHAR (EFFECTIVE_DATE, 'mm')) DUEMONTH " +
					"  		FROM burner_qnt_change bqc, " +
					"       customer_personal_info cpi, " +
					"       MST_CUSTOMER_CATEGORY mcc " +
					" 		WHERE     bqc.customer_id = cpi.customer_id " +wClause+w2Clause+w3Clause+
					"		order by CATEGORY_ID,bqc.customer_id ";




			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		disconnectDTO = new DisconnectDTO();
        		
        		disconnectDTO.setCustomer_name(resultSet.getString("FULL_NAME"));
        		disconnectDTO.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		disconnectDTO.setDisconnect_date(resultSet.getString("EFFECTIVE_DATE"));
        		disconnectDTO.setBurnerQnty(resultSet.getDouble("BURNER_QNT"));
        		disconnectDTO.setPartialBill(resultSet.getDouble("PARTIAL_BILL"));
        		disconnectDTO.setDueMonth(resultSet.getString("DUEMONTH"));
        		disconnectDTO.setRemarks(resultSet.getString("REMARKS"));
        		disconnectDTO.setCustomerCategory(resultSet.getString("CATEGORY_ID"));
        		disconnectDTO.setCustomerCategoryType(resultSet.getString("CATEGORY_NAME"));
        		disconnectionList.add(disconnectDTO);
   
        		
        		
        		
        	}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return disconnectionList;
	}
	
	
	private ArrayList<DisconnectDTO> getdisconnectionListMetered(){
		ArrayList<DisconnectDTO> disconnectionList= new ArrayList<DisconnectDTO>();
		try {
			String wClause="";
			String w2Clause="";

			if(report_for.equals("area_wise")){
				wClause="AND SUBSTR(CPI.CUSTOMER_ID,1,2)="+area+"AND SUBSTR (CPI.CUSTOMER_ID, 3, 2) = bm.CUSTOMER_CATEGORY";
			}else if(report_for.equals("category_wise")){
				wClause="AND SUBSTR(CPI.CUSTOMER_ID,1,2)="+area+"AND SUBSTR(CPI.CUSTOMER_ID,3,2)="+customer_type;
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
			
			
			String disconnection_info_sql="select bm.CATEGORY_TYPE,bm.CUSTOMER_CATEGORY,bm.CUSTOMER_CATEGORY_NAME, FULL_NAME,CPI.CUSTOMER_ID,to_char(DM.DISCONNECT_DATE) DISCONNECT_DATE,BM.MINIMUM_LOAD,MR.CURR_READING,MR.PREV_READING, " +
					"	MR.PRESSURE, MR.PRESSURE_FACTOR,MR.ACTUAL_CONSUMPTION,MR.METER_RENT,round((MR.RATE*MR.ACTUAL_CONSUMPTION)+MR.METER_RENT,2) partial_bill, " +
					"	getDueMonthMeter(cpi.customer_id,TO_CHAR (DISCONNECT_DATE, 'YYYY')||TO_CHAR (DISCONNECT_DATE, 'mm')) DUEMONTH " +
					"	from DISCONN_METERED dm,customer_personal_info cpi,BILLING_READING_MAP brm, BILL_METERED bm,METER_READING mr " +
					"	where DISCONNECT_CAUSE in (0,1,2,3) " +wClause+w2Clause+
					"	and DM.CUSTOMER_ID=CPI.CUSTOMER_ID " +
					"	and DM.READING_ID=BRM.READING_ID " +
					"	and BRM.BILL_ID=BM.BILL_ID " +
					"	and DM.READING_ID=MR.READING_ID " +
					"	ORDER BY CUSTOMER_CATEGORY  " ;




			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		disconnectDTO = new DisconnectDTO();
        		
        		disconnectDTO.setCustomer_name(resultSet.getString("FULL_NAME"));
        		disconnectDTO.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		disconnectDTO.setDisconnect_date(resultSet.getString("DISCONNECT_DATE"));
        		disconnectDTO.setMinimumLoadSCM(resultSet.getDouble("MINIMUM_LOAD"));
        		disconnectDTO.setDisconnDateReading(resultSet.getDouble("CURR_READING"));
        		disconnectDTO.setPreviousReading(resultSet.getDouble("PREV_READING"));
        		disconnectDTO.setPsig(resultSet.getDouble("PRESSURE"));
        		disconnectDTO.setPressureFactor(resultSet.getDouble("PRESSURE_FACTOR"));
        		disconnectDTO.setActualGasConsumption(resultSet.getDouble("ACTUAL_CONSUMPTION"));
        		disconnectDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		disconnectDTO.setPartialBill(resultSet.getDouble("PARTIAL_BILL"));
        		disconnectDTO.setDueMonth(resultSet.getString("DUEMONTH"));
        		disconnectDTO.setCustomerCategory(resultSet.getString("CUSTOMER_CATEGORY"));
        		disconnectDTO.setCustomerCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		disconnectDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		disconnectionList.add(disconnectDTO);

   
        		
        		
        		
        	}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return disconnectionList;
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

	public String getDisconnect_type() {
		return disconnect_type;
	}

	public void setDisconnect_type(String disconnect_type) {
		this.disconnect_type = disconnect_type;
	}
	

	
	

}


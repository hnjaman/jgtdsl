package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerListDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
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




public class CustomerListReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<CustomerListDTO> customerListInfo=new ArrayList<CustomerListDTO>();
	CustomerListDTO customerListDTO = new CustomerListDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String from_date;
	    private  String report_for; 
	    private  String area;
	    private  String customer_type;
	    private  String customer_category;
	    private  String customer_status;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	public String execute() throws Exception
	{
				
		String fileName="Customer_List.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
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
			
			
			
			
			if(report_for.equals("area_wise")){
				if(customer_type.equals("01")){
				generatePdfMeterArea_wise(document);
				}else {
					generatePdfnonMeterArea_wise(document);
				}
			}else if(report_for.equals("category_wise")){
				generatePdfCategory_wise(document);
			}else if(report_for.equals("summary")){
				generatePdfSummary(document);
			}
			
			
			

			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}

	
	
	
	private void generatePdfMeterArea_wise(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer List As on {"+from_date+"}",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(8);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{4,8,18,20,20,7,15,8});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Cus. ID",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("F. Name/Pro. Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Address",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Load",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Ledger balance(Tk.)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Status",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		///////Balance Forward///////////////////
		customerListInfo = getMeterCustomerList();
		double subBalance=0.0;
		double totalBalance=0.0;
		int listSize=customerListInfo.size();
		String previousCategory="";
		String currentCategory="";
		
		for (int i = 0; i < listSize; i++) {
			
			
			currentCategory=customerListInfo.get(i).getCategory();
			if(i==0){
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCategoryName(),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(8);
				pdfPTable.addCell(pcell);
			}
			
			if(!currentCategory.equals(previousCategory)){
				if(i>0){
					pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(6);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pdfPTable.addCell(pcell);
					
					subBalance=0.0;
					
					pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCategoryName(),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(8);
					pdfPTable.addCell(pcell);
				}
				
				previousCategory=customerListInfo.get(i).getCategory();
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerId(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getFatherName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerAddress(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(customerListInfo.get(i).getMaxLoad()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			subBalance=subBalance+customerListInfo.get(i).getLedgerBalance();
			totalBalance=totalBalance+customerListInfo.get(i).getLedgerBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(customerListInfo.get(i).getLedgerBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getStatus(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			if(i==listSize-1){
				pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(6);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(6);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		
		
		
		document.add(pdfPTable);
		
	}
	
	private void generatePdfnonMeterArea_wise(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer List As on {"+from_date+"}",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(9);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{4,8,18,20,20,7,7,10,6});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Cus. ID",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("F. Name/Pro. Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Address",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Full Burner",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Half Burner",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Ledger balance(Tk.)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Status",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		///////Balance Forward///////////////////
		customerListInfo = getnonMeterCustomerList();
		double subBalance=0.0;
		double totalBalance=0.0;
		double subFullBurner=0.0;
		double subHalfBurner=0.0;
		double totalFullBurner=0.0;
		double totalHalfBurner=0.0;
		double halfBurner=0.0;
		
		int subFullBurnerInt=0;
		int subHalfBurnerInt=0;
		int totalFullBurnerInt=0;
		int totalHalfBurnerInt=0;
		
		int listSize=customerListInfo.size();
		String previousCategory="";
		String currentCategory="";
		
		for (int i = 0; i < listSize; i++) {
			
			
			currentCategory=customerListInfo.get(i).getCategory();
			if(i==0){
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCategoryName(),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(9);
				pdfPTable.addCell(pcell);
			}
			
			if(!currentCategory.equals(previousCategory)){
				if(i>0){
					pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(5);
					pdfPTable.addCell(pcell);
					
					subFullBurnerInt=(int)subFullBurner;
					String subFullBurnerString=String.valueOf(subFullBurnerInt);
					
					pcell = new PdfPCell(new Paragraph(subFullBurnerString,ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pdfPTable.addCell(pcell);
					
					subHalfBurnerInt=(int)subHalfBurner;
					String subHalfBurnerString=String.valueOf(subHalfBurnerInt);
					
					pcell = new PdfPCell(new Paragraph(subHalfBurnerString,ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pdfPTable.addCell(pcell);
					
					subFullBurner=0.0;
					subHalfBurner=0.0;
					subBalance=0.0;
					
					pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCategoryName(),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(9);
					pdfPTable.addCell(pcell);
				}
				
				previousCategory=customerListInfo.get(i).getCategory();
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerId(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getFatherName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerAddress(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			subFullBurner=subFullBurner+customerListInfo.get(i).getBurnerQty();
			totalFullBurner=totalFullBurner+customerListInfo.get(i).getBurnerQty();
			int burnerQty=(int)(customerListInfo.get(i).getBurnerQty());
			String bQy=String.valueOf(burnerQty);
			
			pcell = new PdfPCell(new Paragraph(bQy,ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfPTable.addCell(pcell);
			
			halfBurner=(customerListInfo.get(i).getBillBurner()-customerListInfo.get(i).getBurnerQty())*2;
			
			subHalfBurner=(subHalfBurner+halfBurner);
			totalHalfBurner=totalHalfBurner+halfBurner;
			int halfBurQty=(int)halfBurner;
			String halfBQy=String.valueOf(halfBurQty);
			
			pcell = new PdfPCell(new Paragraph(halfBQy,ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfPTable.addCell(pcell);
			
			subBalance=subBalance+customerListInfo.get(i).getLedgerBalance();
			totalBalance=totalBalance+customerListInfo.get(i).getLedgerBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(customerListInfo.get(i).getLedgerBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getStatus(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfPTable.addCell(pcell);
			
			if(i==listSize-1){
				pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(5);
				pdfPTable.addCell(pcell);
				
				subFullBurnerInt=(int)subFullBurner;
				String subFullBurnerString=String.valueOf(subFullBurnerInt);
				
				pcell = new PdfPCell(new Paragraph(subFullBurnerString,ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				subHalfBurnerInt=(int)subHalfBurner;
				String subHalfBurnerString=String.valueOf(subHalfBurnerInt);
				
				pcell = new PdfPCell(new Paragraph(subHalfBurnerString,ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(5);
		pdfPTable.addCell(pcell);
		
		totalFullBurnerInt=(int)totalFullBurner;
		String totalFull=String.valueOf(totalFullBurnerInt);
		
		pcell = new PdfPCell(new Paragraph(totalFull,ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		totalHalfBurnerInt=(int)totalHalfBurner;
		String totalHalf=String.valueOf(totalHalfBurnerInt);
		
		pcell = new PdfPCell(new Paragraph(totalHalf,ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		
		
		
		document.add(pdfPTable);
		
	}
	
	
	/////////////////////Month wise//////////////////////////
	
	private void generatePdfCategory_wise(Document document) throws DocumentException
	{
		
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Customer List As on {"+from_date+"}",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(8);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{5,8,19,21,21,8,11,7});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Cus. ID",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("F. Name/Pro. Name",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Address",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Load/Burner",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
				
		pcell = new PdfPCell(new Paragraph("Ledger balance(Tk.)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Status",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		///////Balance Forward///////////////////
		customerListInfo = getCategoryWiseCustomerList();
		
		double totalBalance=0.0;
		double totalFullBurner=0.0;
		
		
		int listSize=customerListInfo.size();
		
		for (int i = 0; i < listSize; i++) {
			
			totalBalance=totalBalance+customerListInfo.get(i).getLedgerBalance();
			totalFullBurner=totalFullBurner+customerListInfo.get(i).getMaxLoad();
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerId(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getFatherName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerAddress(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(customerListInfo.get(i).getMaxLoad()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfPTable.addCell(pcell);
									
			pcell = new PdfPCell(new Paragraph(taka_format.format(customerListInfo.get(i).getLedgerBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pdfPTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getStatus(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfPTable.addCell(pcell);
						
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(5);
		pdfPTable.addCell(pcell);
				
		pcell = new PdfPCell(new Paragraph(consumption_format.format(totalFullBurner),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		
		
		
		document.add(pdfPTable);
		
	}
	
	
	//////////////////////////Details Collection///////////////////////////////
	
	private void generatePdfSummary(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Summary of Customer List As on {"+from_date+"}",ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(7);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{5,20,15,15,15,20,10});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Category",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Type",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Number of Customer",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Load/Burner",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Ledger Balance(Tk.)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Status",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		///////Balance Forward///////////////////
		customerListInfo = getCustomerListSummary();
		
		double totalBalance=0.0;
		double totalCustomer=0.0;
		double totalBurner=0.0;
		
		double numberOfCustomer=0.0;
		String categoryName="";
		double balance=0.0;
		double burnerQuantity=0.0;
		String customerType="";
		
		int listSize=customerListInfo.size();
		
		for (int i = 0; i < listSize; i++) {	
			
			categoryName=customerListInfo.get(i).getCategoryName();
			customerType=customerListInfo.get(i).getCustomerType();
			numberOfCustomer=customerListInfo.get(i).getNumberOfCustomer();
			burnerQuantity=customerListInfo.get(i).getBurnerQty();
			balance=customerListInfo.get(i).getLedgerBalance();
			
			totalCustomer=totalCustomer+numberOfCustomer;
			totalBurner=totalBurner+burnerQuantity;
			totalBalance=totalBalance+balance;
			
			int numberOfCustomerInt=(int)numberOfCustomer;
			String totalCust=String.valueOf(numberOfCustomerInt);
			int numberOfBurnerInt=(int)burnerQuantity;
			String totalBur=String.valueOf(numberOfBurnerInt);
			
			/*-----------------------Pdf Start from here-------------------------*/
			
			if(burnerQuantity>0.0){
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(categoryName+" (Non-Meter)",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(customerType,ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(totalCust,ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(totalBur,ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
							
				pcell = new PdfPCell(new Paragraph("ACTIVE",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
			}else{
				
				pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(categoryName,ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(customerType,ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(totalCust,ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pdfPTable.addCell(pcell);
							
				pcell = new PdfPCell(new Paragraph("ACTIVE",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
			}
			
			
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(3);
		pdfPTable.addCell(pcell);
		
		int totalCustomerInt=(int)totalCustomer;
		String grandTotalCust=String.valueOf(totalCustomerInt);
			
		pcell = new PdfPCell(new Paragraph(grandTotalCust,ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		int totalBurnerInt=(int)totalBurner;
		String grandTotalBuener=String.valueOf(totalBurnerInt);
		
		pcell = new PdfPCell(new Paragraph(grandTotalBuener,ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
		
	}
	
	////////////////////////End of Details Collection/////////////////////////////
	
	private ArrayList<CustomerListDTO> getMeterCustomerList()
	{
		ArrayList<CustomerListDTO> customerList= new ArrayList<CustomerListDTO>();
		

		String area=loggedInUser.getArea_id();	
		try {
		
			String customer_info_sql="select MCI.CUSTOMER_ID,MCI.CATEGORY_ID,MCI.CATEGORY_NAME,MCI.FULL_NAME,FATHER_NAME,ADDRESS_LINE1,GET_CUSTOMER_LEDGER_BALANCE(mci.CUSTOMER_ID,'"+from_date+"') balance,tab1.MAX_LOAD MAX_LOAD, " +
					"decode(status,'1', 'Active', 'Inactive') status " +
					"from MVIEW_CUSTOMER_INFO MCI, " +
					"(select CUSTOMER_ID,MAX_LOAD from meter_reading where READING_ID in( " +
					"select max(READING_ID) from meter_reading where CURR_READING_DATE<=to_date('"+from_date+"','dd-MM-YYYY') " +
					"group by customer_id)) tab1 " +
					"WHERE TAB1.CUSTOMER_ID = MCI.CUSTOMER_ID " +
					"AND SUBSTR(MCI.CUSTOMER_ID,1,2)='"+area+"' " +
					"AND MCI.ISMETERED=1 " +
					"ORDER BY MCI.CUSTOMER_ID,MCI.CATEGORY_ID " ;




			
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		customerListDTO=new CustomerListDTO();
        		
        		customerListDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		customerListDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		customerListDTO.setFatherName(resultSet.getString("FATHER_NAME"));
        		customerListDTO.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
        		customerListDTO.setStatus(resultSet.getString("status"));
        		customerListDTO.setLedgerBalance(resultSet.getDouble("balance"));
        		customerListDTO.setCategory(resultSet.getString("CATEGORY_ID"));
        		customerListDTO.setCategoryName(resultSet.getString("CATEGORY_NAME"));
        		customerListDTO.setMaxLoad(resultSet.getDouble("MAX_LOAD"));
        		
        		customerList.add(customerListDTO);
        		
        		
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerList;
	}
	
	private ArrayList<CustomerListDTO> getnonMeterCustomerList()
	{
		ArrayList<CustomerListDTO> customerList= new ArrayList<CustomerListDTO>();
		

		String area=loggedInUser.getArea_id();	
		try {
		
			String customer_info_sql="select MCI.CUSTOMER_ID,MCI.CATEGORY_ID,MCI.CATEGORY_NAME,MCI.FULL_NAME,FATHER_NAME,ADDRESS_LINE1,GET_CUSTOMER_LEDGER_BALANCE(mci.CUSTOMER_ID,'"+from_date+"') Balance,NEW_DOUBLE_BURNER_QNT_BILLCAL,NEW_DOUBLE_BURNER_QNT, " +
					"CASE NEW_DOUBLE_BURNER_QNT_BILLCAL when 0 then 'Inactive' ELSE 'Active' END status " +
					"from MVIEW_CUSTOMER_INFO MCI, " +
					"(select CUSTOMER_ID, NEW_DOUBLE_BURNER_QNT,NEW_DOUBLE_BURNER_QNT_BILLCAL  from BURNER_QNT_CHANGE where PID IN( " +
					"select max(PID) from BURNER_QNT_CHANGE where SUBSTR(customer_id,1,2)='"+area+"' and  effective_date<=to_date('"+from_date+"','dd-MM-YYYY')  " +
					"GROUP BY CUSTOMER_ID)) tab1 " +
					"WHERE TAB1.CUSTOMER_ID = MCI.CUSTOMER_ID " +
					"AND SUBSTR(MCI.CUSTOMER_ID,1,2)='"+area+"' " +
					"AND MCI.ISMETERED=0 " +
					"ORDER BY MCI.CUSTOMER_ID " ;





			
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		customerListDTO=new CustomerListDTO();
        		
        		customerListDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		customerListDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		customerListDTO.setFatherName(resultSet.getString("FATHER_NAME"));
        		customerListDTO.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
        		customerListDTO.setStatus(resultSet.getString("status"));
        		customerListDTO.setLedgerBalance(resultSet.getDouble("balance"));
        		customerListDTO.setCategory(resultSet.getString("CATEGORY_ID"));
        		customerListDTO.setCategoryName(resultSet.getString("CATEGORY_NAME"));
        		customerListDTO.setBurnerQty(resultSet.getFloat("NEW_DOUBLE_BURNER_QNT"));
        		customerListDTO.setBillBurner(resultSet.getFloat("NEW_DOUBLE_BURNER_QNT_BILLCAL"));
        		
        		customerList.add(customerListDTO);
        		
        		
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerList;
	}
	
	private ArrayList<CustomerListDTO> getCustomerListSummary()
	{
		ArrayList<CustomerListDTO> customerList= new ArrayList<CustomerListDTO>();
		

		String area=loggedInUser.getArea_id();	
		try {
		
			String customer_info_sql="select CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE,CUSTOMER_ID, BuenerQty,balance,Metered from( " +
					"select tab1.CATEGORY_ID,tab1.CATEGORY_NAME,tab1.CATEGORY_TYPE, tab1.CUSTOMER_ID, BuenerQty,balance,Metered from( " +
					"select CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE, count(CUSTOMER_ID) CUSTOMER_ID,sum(BURNER) BuenerQty from VIEW_CUSTOMER_ACTIVE " +
					"where substr(CUSTOMER_ID,1,2)='"+area+"' " +
					"and BURNER<>0 " +
					"group by CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE) tab1, " +
					"(select substr(customer_id,3,2) CATEGORY_ID,sum(BAL) balance,'Non-Metered' as Metered from( " +
					"select cl.customer_id,(SUM (DEBIT) - SUM (CREDIT)) " +
					"                   + SUM ( " +
					"                        CASE " +
					"                           WHEN PARTICULARS LIKE 'Balance Transfer' THEN BALANCE " +
					"                        END) " +
					"                      BAL from customer_ledger CL,customer_connection CN " +
					"WHERE CL.customer_id=cn.customer_id " +
					"AND substr(CL.CUSTOMER_ID,1,2)='"+area+"' " +
					"and ISMETERED=0 " +
					"AND cl.status=1 " +
					"group by cl.customer_id) " +
					"group by substr(customer_id,3,2)) tab2 " +
					"where tab1.CATEGORY_ID=tab2.CATEGORY_ID " +
					"union all " +
					"select t1.CATEGORY_ID,t1.CATEGORY_NAME,t1.CATEGORY_TYPE,t1.CUSTOMER_ID,BuenerQty,balance,Metered from( " +
					"SELECT CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE,COUNT (customer_id) CUSTOMER_ID,NULL BuenerQty " +
					"FROM MVIEW_CUSTOMER_INFO " +
					"WHERE ismetered = 1 " +
					"AND SUBSTR (customer_id, 1, 2) ='"+area+"' " +
					"AND status = 1 " +
					"GROUP BY CATEGORY_ID, CATEGORY_NAME, CATEGORY_TYPE) t1, " +
					"(select substr(customer_id,3,2) CATEGORY_ID,sum(BAL) balance,'Metered' as Metered from( " +
					"select cl.customer_id,(SUM (DEBIT) - SUM (CREDIT)) " +
					"                   + SUM ( " +
					"                        CASE " +
					"                           WHEN PARTICULARS LIKE 'Balance Transfer' THEN BALANCE " +
					"                        END) " +
					"                      BAL from customer_ledger CL,customer_connection CN " +
					"WHERE CL.customer_id=cn.customer_id " +
					"AND substr(CL.CUSTOMER_ID,1,2)='"+area+"' " +
					"and ISMETERED=1 " +
					"AND cl.status=1 " +
					"group by cl.customer_id) " +
					"group by substr(customer_id,3,2)) t2 " +
					"where t1.CATEGORY_ID=t2.CATEGORY_ID) " +
					"order by CATEGORY_ID desc " ;	
					
					
					
//					
//					"select CUSTOMER_ID, aa.CATEGORY_ID,CATEGORY_NAME,BAL,BuenerQty,CATEGORY_TYPE " +
//					"from ( " +
//					"SELECT count(customer_id) CUSTOMER_ID,CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE,null BuenerQty " +
//					"FROM MVIEW_CUSTOMER_INFO " +
//					"WHERE ismetered=1 " +
//					"AND substr(customer_id,1,2)='"+area+"' " +
//					"AND status=1 " +
//					"group by CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE " +
//					"union all " +
//					"select CUSTOMER_ID,ab.CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE,BuenerQty from( " +
//					"SELECT count(customer_id) CUSTOMER_ID,CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE " +
//					"FROM MVIEW_CUSTOMER_INFO " +
//					"WHERE ismetered=0 " +
//					"AND substr(customer_id,1,2)='"+area+"' " +
//					"AND status=1 " +
//					"group by CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE)ab, " +
//					"(SELECT substr(BQC.CUSTOMER_ID,3,2) category_id, " +
//					"sum(BQC.NEW_DOUBLE_BURNER_QNT) BuenerQty " +
//					"FROM BURNER_QNT_CHANGE BQC " +
//					"WHERE PID IN " +
//					"(  SELECT MAX (PID) " +
//					"FROM BURNER_QNT_CHANGE " +
//					"WHERE     SUBSTR (customer_id, 1, 2) = '"+area+"' " +
//					"AND effective_date <= " +
//					"TO_DATE ('"+from_date+"', 'dd-MM-YYYY') " +
//					"GROUP BY CUSTOMER_ID) " +
//					"group by substr(CUSTOMER_ID,3,2)) ba " +
//					"where ab.CATEGORY_ID=ba.CATEGORY_ID) aa,( " +
//					"SELECT substr(CUSTOMER_ID,3,2) CATEGORY_ID,(SUM(DEBIT)-SUM(CREDIT))+SUM(CASE WHEN PARTICULARS LIKE 'Balance Transfer' THEN BALANCE  END ) BAL " +
//					"FROM CUSTOMER_LEDGER " +
//					"WHERE " +
//					"substr(CUSTOMER_ID,1,2)='"+area+"' " +
//					"AND STATUS=1 " +
//					"group by substr(CUSTOMER_ID,3,2) " +
//					")bb where aa.CATEGORY_ID=bb.CATEGORY_ID " +
//					"order by bb.CATEGORY_ID desc " ;

						
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		customerListDTO=new CustomerListDTO();
        		
        		customerListDTO.setNumberOfCustomer(resultSet.getDouble("CUSTOMER_ID"));
        		customerListDTO.setCategory(resultSet.getString("CATEGORY_ID"));
        		customerListDTO.setCategoryName(resultSet.getString("CATEGORY_NAME"));
        		customerListDTO.setBurnerQty(resultSet.getFloat("BuenerQty"));
        		customerListDTO.setCustomerType(resultSet.getString("CATEGORY_TYPE"));
        		customerListDTO.setLedgerBalance(resultSet.getDouble("balance"));
        		
        		customerList.add(customerListDTO);
        		
        		
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerList;
	}

	
	private ArrayList<CustomerListDTO> getCategoryWiseCustomerList()
	{
		ArrayList<CustomerListDTO> customerList= new ArrayList<CustomerListDTO>();
		

		String area=loggedInUser.getArea_id();	
		try {
			
			String customer_info_sql="";
			
			if(customer_type.equals("01")){
				
				customer_info_sql="select MCI.CUSTOMER_ID,MCI.CATEGORY_ID,MCI.CATEGORY_NAME,MCI.FULL_NAME,FATHER_NAME,ADDRESS_LINE1,GET_CUSTOMER_LEDGER_BALANCE(mci.CUSTOMER_ID,'"+from_date+"') Balance,tab1.MAX_LOAD MAX_LOAD, " +
						"decode(status,'1', 'Active', 'Inactive') status " +
						"from MVIEW_CUSTOMER_INFO MCI, " +
						"(select CUSTOMER_ID,MAX_LOAD from meter_reading where READING_ID in( " +
						"select max(READING_ID) from meter_reading where CURR_READING_DATE<=to_date('"+from_date+"','dd-MM-YYYY') " +
						"group by customer_id)) tab1 " +
						"WHERE TAB1.CUSTOMER_ID = MCI.CUSTOMER_ID " +
						"AND SUBSTR(MCI.CUSTOMER_ID,1,2)='"+area+"' " +
						"AND SUBSTR(MCI.CUSTOMER_ID,3,2)='"+customer_category+"' " +
						"AND MCI.ISMETERED=1 " +
						"ORDER BY MCI.CUSTOMER_ID,MCI.CATEGORY_ID " ;
				
			}else{
				
				customer_info_sql="select MCI.CUSTOMER_ID,MCI.CATEGORY_ID,MCI.CATEGORY_NAME,MCI.FULL_NAME,FATHER_NAME,ADDRESS_LINE1,GET_CUSTOMER_LEDGER_BALANCE(mci.CUSTOMER_ID,'"+from_date+"') Balance,NEW_DOUBLE_BURNER_QNT MAX_LOAD, " +
						"CASE NEW_DOUBLE_BURNER_QNT_BILLCAL when 0 then 'Inactive' ELSE 'Active' END status " +
						"from MVIEW_CUSTOMER_INFO MCI, " +
						"(select CUSTOMER_ID, NEW_DOUBLE_BURNER_QNT,NEW_DOUBLE_BURNER_QNT_BILLCAL  from BURNER_QNT_CHANGE where PID IN( " +
						"select max(PID) from BURNER_QNT_CHANGE where SUBSTR(customer_id,1,2)='"+area+"' and  effective_date<=to_date('"+from_date+"','dd-MM-YYYY')  " +
						"GROUP BY CUSTOMER_ID)) tab1 " +
						"WHERE TAB1.CUSTOMER_ID = MCI.CUSTOMER_ID " +
						"AND SUBSTR(MCI.CUSTOMER_ID,1,2)='"+area+"' " +
						"AND SUBSTR(MCI.CUSTOMER_ID,3,2)='"+customer_category+"' " +
						"AND MCI.ISMETERED=0 " +
						"ORDER BY MCI.CUSTOMER_ID " ;
				
			}
		
			




			
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		customerListDTO=new CustomerListDTO();
        		
        		customerListDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		customerListDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		customerListDTO.setFatherName(resultSet.getString("FATHER_NAME"));
        		customerListDTO.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
        		customerListDTO.setStatus(resultSet.getString("status"));
        		customerListDTO.setLedgerBalance(resultSet.getDouble("balance"));
        		customerListDTO.setCategory(resultSet.getString("CATEGORY_ID"));
        		customerListDTO.setCategoryName(resultSet.getString("CATEGORY_NAME"));
        		customerListDTO.setMaxLoad(resultSet.getDouble("MAX_LOAD"));
        		
        		customerList.add(customerListDTO);
        		
        		
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerList;
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




	public String getFrom_date() {
		return from_date;
	}




	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}




	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}




	public String getCustomer_type() {
		return customer_type;
	}




	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}




	public String getCustomer_status() {
		return customer_status;
	}




	public void setCustomer_status(String customer_status) {
		this.customer_status = customer_status;
	}

	
  }



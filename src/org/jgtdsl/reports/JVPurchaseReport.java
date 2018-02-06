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
import org.jgtdsl.dto.JournalVoucherDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;




import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class JVPurchaseReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>purchaseJVList = new ArrayList<JournalVoucherDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String month;
	    private  String collection_year;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String area="01";
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		
		
		
		
	public String execute() throws Exception
	{
				
		String fileName="Purchase_JV.pdf";
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
			
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png"); 	// image path
			Image img = Image.getInstance(realPath);
						
            	//img.scaleToFit(10f, 200f);
            	//img.scalePercent(200f);
            img.scaleAbsolute(28f, 31f);
            img.setAbsolutePosition(145f, 780f);		
            	//img.setAbsolutePosition(290f, 540f);		// rotate
            
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

			Chunk chunk1 = new Chunk("Head Office : ",ReportUtil.f8B);
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
			
			
			PdfPTable headLinetable = null;
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{10,80,10});
			
			String headLine="";
			
			if(report_for.equals("month_wise")){
				headLine="Journal Voucher of Gas Purchase "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year;
			}else if(report_for.equals("fiscal_wise")){
				headLine="Journal Voucher of Gas Purchase FY :"+collection_year;
			}
			
			
						
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			document.add(headLinetable);	
			
			
			
			PdfPTable jvTable = new PdfPTable(7);
			jvTable.setWidthPercentage(100);
			jvTable.setWidths(new float[]{35,5,10,20,5,20,5});
			jvTable.setSpacingBefore(15f);
			
			pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			pcell.setColspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Control Code",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Debit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Credit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ps.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ps.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*-------------------------------End of Head Line-----------------------------------------------*/
		
			double bgfclDebit=0.0;
			double sgflDebit=0.0;
			double gtclDebit=0.0;
			double pdfDebit=0.0;
			double bapexDebit=0.0;
			double dwmbDebit=0.0;
			double gdfDebit=0.0;
			double avalueDebit=0.0;
				
			purchaseJVList=getSalesJV();
			int listSize=purchaseJVList.size();			
			
			for (int i = 0; i < listSize; i++) {
								
				bgfclDebit=purchaseJVList.get(i).getBgfcl();
				sgflDebit=purchaseJVList.get(i).getSgfl();
				gtclDebit=purchaseJVList.get(i).getTransmissionMargin();
				pdfDebit=purchaseJVList.get(i).getPDFMargin();
				bapexDebit=purchaseJVList.get(i).getBapexMargin();
				dwmbDebit=purchaseJVList.get(i).getdWhelheadMargin();
				gdfDebit=purchaseJVList.get(i).getGdFund();
				avalueDebit=purchaseJVList.get(i).getAvalue();
			}
			
			
			pcell = new PdfPCell(new Paragraph("Gas Purchase Inter Company (BGFCL)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bgfclDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Gas Purchase Inter Company (SGFL)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(sgflDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Transmission Margin (GTCL)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(gtclDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("PDF Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(pdfDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("BAPEX Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bapexDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("DWMB Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(dwmbDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Gas Development Fund(GDF)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(gdfDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Asset Value Of Gas",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(avalueDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/////////////////////////////////////////////////////////////////////////////////////////
			
			pcell = new PdfPCell(new Paragraph("Liabilities for Gas Purchase (BGFCL)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bgfclDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Liabilities for Gas Purchase (SGFL)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(sgflDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Liabilities for Transmission Charge (GTCL)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(gtclDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Liabilities for price Deficit Fund(PDF)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(pdfDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Liabilities for BAPEX Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bapexDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Liabilities for DWMB Margin",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(dwmbDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Liabilities for Gas Development Fund(GDF)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(gdfDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Liabilities for Asset Value of Gas",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(avalueDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/*---------------------------------------------------------------------------*/
			
			
			/*------------------------------------------------------------------------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bgfclDebit+sgflDebit+gtclDebit+pdfDebit+bapexDebit+dwmbDebit+gdfDebit+avalueDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bgfclDebit+sgflDebit+gtclDebit+pdfDebit+bapexDebit+dwmbDebit+gdfDebit+avalueDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("----",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			
			
			/*----------------------------------------End of Debit Part------------------------------*/
			
			
			
				
			
		
			document.add(jvTable);			
		
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
				
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
		

	private ArrayList<JournalVoucherDTO>getSalesJV(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			
			
			
			String defaulterSql="";
			
			if(report_for.equals("month_wise")){
				defaulterSql="select MB.MONTH,MB.YEAR, (MB.SD+MB.VAT+MB.WELLHEAD) BGFCL,(MS.SD+MS.VAT+MS.WELLHEAD) SGFL, " +
						"MB.PDF+MS.PDF PDF,MB.BAPEX+MS.BAPEX BAPEX,MB.DWELLHED+MS.DWELLHED DWELLHED,MB.GD_FUND+MS.GD_FUND GD_FUND, " +
						"MB.ASSET_VALUE+MS.ASSET_VALUE AVALUE,MB.TRANSMISSION+MS.TRANSMISSION TRANSMISSION " +
						"from MARGIN_BGFCL MB,MARGIN_SGFL MS " +
						"where MB.MONTH=MS.MONTH " +
						"AND MB.YEAR=MS.YEAR " +
						"AND MB.MONTH="+bill_month+" and MB.YEAR="+bill_year+" " ;

			}else if(report_for.equals("fiscal_wise")){
				
				String[] fiscalYear=collection_year.split("-");
				String firstPart=fiscalYear[0].toString()+"07";
				String secondPart=fiscalYear[1].toString()+"06";
				
				defaulterSql="select sum(MB.SD+MB.VAT+MB.WELLHEAD) BGFCL,sum(MS.SD+MS.VAT+MS.WELLHEAD) SGFL, " +
						"sum(MB.PDF+MS.PDF) PDF,sum(MB.BAPEX+MS.BAPEX) BAPEX,sum(MB.DWELLHED+MS.DWELLHED) DWELLHED,sum(MB.GD_FUND+MS.GD_FUND) GD_FUND, " +
						"sum(MB.ASSET_VALUE+MS.ASSET_VALUE) AVALUE,sum(MB.TRANSMISSION+MS.TRANSMISSION) TRANSMISSION " +
						"from MARGIN_BGFCL MB,MARGIN_SGFL MS " +
						"where MB.MONTH=MS.MONTH " +
						"AND MB.YEAR=MS.YEAR " +
						"AND MB.YEAR||lpad(MB.MONTH,2,0) between "+firstPart+" and "+secondPart+" " ;
			}
														
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setBgfcl(resultSet.getDouble("BGFCL"));
        		jDto.setSgfl(resultSet.getDouble("SGFL"));
        		jDto.setPDFMargin(resultSet.getDouble("PDF"));
        		jDto.setBapexMargin(resultSet.getDouble("BAPEX"));
        		jDto.setdWhelheadMargin(resultSet.getDouble("DWELLHED"));
        		jDto.setGdFund(resultSet.getDouble("GD_FUND"));
        		jDto.setAvalue(resultSet.getDouble("AVALUE"));
        		jDto.setTransmissionMargin(resultSet.getDouble("TRANSMISSION"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	
	
	/*Query for Area Wise JV .It may be use in future..........
	 * select * from (
					select substr(CUSTOMER_ID,3,2) cat,sum(TOTAL_AMOUNT) DEBIT,null CREDIT, 1 as nm  from SALES_REPORT 
					where BILLING_YEAR||lpad(BILLING_MONTH,2,0) between 201507 and 201606
					AND substr(customer_id,1,2)='01'
					group by substr(CUSTOMER_ID,3,2)
					union all 
					select substr(CUSTOMER_ID,3,2) cat,null DEBIT,sum(TOTAL_AMOUNT) CREDIT, 2 as nm  from SALES_REPORT 
					where BILLING_YEAR||lpad(BILLING_MONTH,2,0) between 201507 and 201606
					AND substr(customer_id,1,2)='01'
					group by substr(CUSTOMER_ID,3,2)
					) order by nm,cat desc
					
					*
					*
					*/
	
	
	
	

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	public String getMonth() {
		return month;
	}



	public void setMonth(String month) {
		this.month = month;
	}



	public String getCollection_year() {
		return collection_year;
	}

	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
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

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}
	
	
  }




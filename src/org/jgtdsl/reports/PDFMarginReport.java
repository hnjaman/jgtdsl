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
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;



import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class PDFMarginReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>pdfList = new ArrayList<JournalVoucherDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String month;
	    private  String collection_year;
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
				
		String fileName="PDF_Report.pdf";
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

			pcell=new PdfPCell(new Paragraph("Revenue Section : Nalka, Sirajganj", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Liabilities for PDF Margin", ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("For the FY : "+collection_year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			document.add(mTable);
			
			
			
			
			
			
			PdfPTable jvTable = new PdfPTable(7);
			jvTable.setWidthPercentage(100);
			jvTable.setWidths(new float[]{7,30,15,11,11,11,15});
			jvTable.setSpacingBefore(15f);
			
			pcell = new PdfPCell(new Paragraph("Date",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Particulars",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Debit(Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Credit(Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(3);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Balance/Payable(Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("BGFCL",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SGFL",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Toatl",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*-------------------------------End of Head Line-----------------------------------------------*/
			double totalDebit=0.0;
			double totalBgfcl=0.0;
			double openingBalance=0.0;
			double balance=0.0;
			double totalSgfl=0.0;
			double totalCredit=0.0;
			
			double debit=0.0;
			double bgfcl=0.0;
			double sgfl=0.0;
			
			pdfList=getPDFDetails();
			int listSize=pdfList.size();
			
			for (int i = 0; i < listSize; i++) {
				
				debit=pdfList.get(i).getDebit();
				bgfcl=pdfList.get(i).getBgfcl();
				sgfl=pdfList.get(i).getSgfl();
				
				
				if(i==0){
					balance=openingBalance;
				}
				
				if(debit>0.0){
					balance=balance-debit;
				}else{
					balance=balance+bgfcl+sgfl;
				}
				if(i==0){
					
					pcell = new PdfPCell(new Paragraph("01-07-"+pdfList.get(i).getYear(),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Opening Balance=",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(4);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(openingBalance),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
				}
				
				if(debit>0.0){
					
					pcell = new PdfPCell(new Paragraph(pdfList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("By Bank,"+Month.values()[Integer.valueOf(pdfList.get(i).getMonth())-1]+"-"+pdfList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalDebit=totalDebit+debit;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(debit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
										
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
				}else{
					
					pcell = new PdfPCell(new Paragraph(pdfList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("To, Gas Purchase, "+Month.values()[Integer.valueOf(pdfList.get(i).getMonth())-1]+"-"+pdfList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					totalBgfcl=totalBgfcl+bgfcl;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(bgfcl),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalSgfl=totalSgfl+sgfl;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(sgfl),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalCredit=totalCredit+bgfcl+sgfl;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(bgfcl+sgfl),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
				}
								
			}
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBgfcl),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalSgfl),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCredit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9B));
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
		

	private ArrayList<JournalVoucherDTO>getPDFDetails(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			String[] fiscalYear=collection_year.split("-");
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			String firstDate="01-07-"+fiscalYear[0].toString();
			String sndDate="30-06-"+fiscalYear[1].toString();
			
			
			String defaulterSql="SELECT TRANS_DATE,T_DATE,MONTH,YEAR, CREDIT,BGFCL,SGFL  FROM( " +
					"SELECT TRANS_DATE,T_DATE,MONTH,YEAR,NULL CREDIT,BGFCL,SGFL FROM( " +
					"SELECT to_char(LAST_DAY (TO_DATE (MB.MONTH || '-' || MB.YEAR, 'MM-YYYY'))) TRANS_DATE,LAST_DAY (TO_DATE (MB.MONTH || '-' || MB.YEAR, 'MM-YYYY')) T_DATE, MS.MONTH MONTH,MS.YEAR YEAR,MB.PDF BGFCL,MS.PDF SGFL  FROM MARGIN_BGFCL MB,MARGIN_SGFL MS " +
					"WHERE MB.MONTH=MS.MONTH " +
					"AND MB.YEAR=MS.YEAR " +
					"and to_number(MB.YEAR||lpad(MB.MONTH,2,0)) between "+firstPart+" and "+secondPart+") " +
					"UNION ALL " +
					"SELECT TRANS_DATE,T_DATE,MONTH,YEAR,CREDIT,NULL BGFCL,NULL SGFL FROM( " +
					"select to_char(TRANS_DATE,'dd-mm-yyyy') TRANS_DATE ,TRANS_DATE T_DATE,COLLECTION_MONTH MONTH,COLLECTION_YEAR YEAR,CREDIT from MARGIN_ACCOUNT_PAYABLE_DTL ma, BANK_ACCOUNT_LEDGER ba  " +
					"where MA.TRANS_ID=ba.TRANS_ID " +
					"AND TRANS_TYPE=6 " +
					"AND TRANS_DATE between to_date('"+firstDate+"','dd/mm/yyyy') and to_date('"+sndDate+"','dd/mm/yyyy') " +
					"AND PARTICULARS LIKE '%PDF%')) " +
					"ORDER BY T_DATE " ;
													
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setTransactionDate(resultSet.getString("TRANS_DATE"));
        		jDto.setMonth(resultSet.getString("month"));
        		jDto.setYear(resultSet.getString("year"));
        		jDto.setDebit(resultSet.getDouble("CREDIT"));
        		jDto.setBgfcl(resultSet.getDouble("BGFCL"));
        		jDto.setSgfl(resultSet.getDouble("SGFL"));
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	
	
	

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

	
  }





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




public class GTCLReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>gtclList = new ArrayList<JournalVoucherDTO>();
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
				
		String fileName="GTCL_Report.pdf";
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
			
			pcell=new PdfPCell(new Paragraph("Liabilities for Transmission Margin", ReportUtil.f11B));
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
			jvTable.setWidths(new float[]{10,30,12,12,12,12,12});
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
			pcell.setColspan(3);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Credit(Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Balance/Payable(Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Bill",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Income Tax",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*-------------------------------End of Head Line-----------------------------------------------*/
			double bill=0.0;
			double incomeTax=0.0;
			double totalDebit=0.0;
			double credit=0.0;
			double openingBalance=0.0;
			double balance=0.0;
			
			double totalBill=0.0;
			double totalIncomeTax=0.0;
			double grandTotalDebit=0.0;
			double totalCredit=0.0;
			
			gtclList=getBGFCLCredit();
			int listSize=gtclList.size();
			
			for (int i = 0; i < listSize; i++) {
				bill = gtclList.get(i).getBill();
				incomeTax=gtclList.get(i).getIncomeTax();
				credit=gtclList.get(i).getGtcl();
				totalDebit=bill+incomeTax;
				
				if(i==0){
					balance=openingBalance;
				}
				
				if(bill>0.0){
					balance=balance-bill-incomeTax;
				}else{
					balance=balance+credit;
				}
				if(i==0){
					
					pcell = new PdfPCell(new Paragraph("01-07-"+gtclList.get(i).getYear(),ReportUtil.f9B));
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
				
				if(bill>0.0){
					
					pcell = new PdfPCell(new Paragraph(gtclList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("By Bank,"+Month.values()[Integer.valueOf(gtclList.get(i).getMonth())-1]+"-"+gtclList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalBill=totalBill+bill;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(bill),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalIncomeTax=totalIncomeTax+incomeTax;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(incomeTax),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					grandTotalDebit=grandTotalDebit+totalDebit;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(totalDebit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);

					pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
				}else{
					
					pcell = new PdfPCell(new Paragraph(gtclList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("To, Transmission Margin, "+Month.values()[Integer.valueOf(gtclList.get(i).getMonth())-1]+"-"+gtclList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalCredit=totalCredit+credit;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(credit*0.1565),ReportUtil.f9));
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
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBill),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalIncomeTax),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCredit),ReportUtil.f9B));
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
		

	private ArrayList<JournalVoucherDTO>getBGFCLCredit(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			String[] fiscalYear=collection_year.split("-");
			String firstPart="01/07/"+fiscalYear[0].toString();
			String secondPart="30/06/"+fiscalYear[1].toString();
			
			
			String defaulterSql="select to_char(TRANS_DATE) T_DATE,TRANS_DATE,MONTH,YEAR,BILL,incomeTax,GTCL CREDIT from( " +
					"select TRANS_DATE,MONTH,YEAR,BILL,incomeTax,null GTCL  from( " +
					"select TRANS_DATE,COLLECTION_MONTH MONTH,COLLECTION_YEAR YEAR,CREDIT BILL,round((CREDIT*3)/97,0) as incomeTax from MARGIN_ACCOUNT_PAYABLE_DTL MA, bank_account_ledger BA " +
					"where BA.TRANS_ID = MA.TRANS_ID  " +
					"AND trans_type=6 " +
					"AND PARTICULARS = 'GTCL' " +
					"AND TRANS_DATE between to_date('"+firstPart+"','dd/mm/yyyy') and to_date('"+secondPart+"','dd/mm/yyyy')) " +
					"union all " +
					"select TRANS_DATE,MONTH,YEAR,null BILL,null incomeTax,GTCL from( " +
					"select  LAST_DAY (TO_DATE (MONTH || '-' || YEAR, 'MM-YYYY')) TRANS_DATE, " +
					"MONTH ,YEAR ,TOTAL_GTCL GTCL from GAS_PURCHASE_SUMMARY " +
					") WHERE TRANS_DATE between to_date('"+firstPart+"','dd/mm/yyyy') and to_date('"+secondPart+"','dd/mm/yyyy')) " +
					"order by TRANS_DATE " ;



									
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setTransactionDate(resultSet.getString("T_DATE"));
        		jDto.setBill(resultSet.getDouble("BILL"));
        		jDto.setIncomeTax(resultSet.getDouble("incomeTax"));
        		jDto.setGtcl(resultSet.getDouble("CREDIT"));
        		jDto.setMonth(resultSet.getString("MONTH"));
        		jDto.setYear(resultSet.getString("YEAR"));
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




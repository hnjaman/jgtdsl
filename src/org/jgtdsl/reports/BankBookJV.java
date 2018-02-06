
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
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.BranchDTO;
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




public class BankBookJV extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>bankBookJVList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>bankBookDebitList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>transferJVList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>receiveJVList = new ArrayList<JournalVoucherDTO>();
	AccountDTO accountInfo=new AccountDTO();
	
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String month;
	    private  String collection_year;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for; 
	    private  String area="01";
	    private  String bank_id;
	    private  String branch_id;
	    private  String account_no;
	    private  String from_date;
	    private  String to_date;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		
		
		
		
	public String execute() throws Exception
	{
				
		String fileName="BankBook_JV.pdf";
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

			Chunk chunk1 = new Chunk("Revenue Section :",ReportUtil.f8B);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f9B);
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
				headLine="For the month "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year;
			}else if(report_for.equals("fiscal_wise")){
				headLine="For the FY :"+collection_year;
			}else if(report_for.equals("date_wise")){
				headLine="From "+from_date+" to "+to_date;
			}
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Journal Voucher",ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
						
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f9B));
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
			jvTable.setWidths(new float[]{30,5,15,20,5,20,5});
			jvTable.setSpacingBefore(15f);
						
			pcell = new PdfPCell(new Paragraph("J.V. NO: 01",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(7);
			pcell.setBorder(0);
			jvTable.addCell(pcell);
			
//			if(report_for.equals("date_wise")){
//				pcell = new PdfPCell(new Paragraph("Date : "+to_date,ReportUtil.f9));
//				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				pcell.setColspan(7);
//				pcell.setBorder(0);
//				jvTable.addCell(pcell);
//			}else if(report_for.equals("fiscal_wise")){
//				
//				String[] fiscalYear=collection_year.split("-");
//				String secondPart="30-06-"+fiscalYear[1].toString();
//				
//				pcell = new PdfPCell(new Paragraph("Date : "+secondPart,ReportUtil.f9));
//				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				pcell.setColspan(7);
//				pcell.setBorder(0);
//				jvTable.addCell(pcell);
//			}
			
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
						
			double powerCredit=0.0;
			double capCredit=0.0;
			double cngCredit=0.0;
			double indCredit=0.0;
			double comCredit=0.0;
			double domCredit=0.0;
			
			double mPowerCredit=0.0;
			double mCapCredit=0.0;
			double mCngCredit=0.0;
			double mIndCredit=0.0;
			double mComCredit=0.0;
			double mDomCredit=0.0;
			
			double sPowerCredit=0.0;
			double sCapCredit=0.0;
			double sCngCredit=0.0;
			double sIndCredit=0.0;
			double sComCredit=0.0;
			double sDomCredit=0.0;
			
			double interestIncome=0.0; 
			double hhvNHV=0.0;
			
			double corporateTax=0.0;
			double bankCharge=0.0;
			double revenue=0.0;
			
			accountInfo = getAccountInfo();
			bankBookJVList=getBankBook_AcJV();
			
			
			powerCredit=bankBookJVList.get(0).getCredit()+bankBookJVList.get(1).getCredit();
			cngCredit=bankBookJVList.get(2).getCredit()+bankBookJVList.get(3).getCredit();
			capCredit=bankBookJVList.get(4).getCredit()+bankBookJVList.get(5).getCredit();
			indCredit=bankBookJVList.get(6).getCredit()+bankBookJVList.get(7).getCredit();
			comCredit=bankBookJVList.get(8).getCredit()+bankBookJVList.get(9).getCredit();
			domCredit=bankBookJVList.get(10).getCredit()+bankBookJVList.get(11).getCredit();
			
			sPowerCredit=bankBookJVList.get(12).getCredit()+bankBookJVList.get(13).getCredit();
			sCngCredit=bankBookJVList.get(14).getCredit()+bankBookJVList.get(15).getCredit();
			sCapCredit=bankBookJVList.get(16).getCredit()+bankBookJVList.get(17).getCredit();
			sIndCredit=bankBookJVList.get(18).getCredit()+bankBookJVList.get(19).getCredit();
			sComCredit=bankBookJVList.get(20).getCredit()+bankBookJVList.get(21).getCredit();
			sDomCredit=bankBookJVList.get(22).getCredit()+bankBookJVList.get(23).getCredit();
			
			mPowerCredit=bankBookJVList.get(24).getCredit()+bankBookJVList.get(25).getCredit();
			mCngCredit=bankBookJVList.get(26).getCredit()+bankBookJVList.get(27).getCredit();
			mCapCredit=bankBookJVList.get(28).getCredit()+bankBookJVList.get(29).getCredit();
			mIndCredit=bankBookJVList.get(30).getCredit()+bankBookJVList.get(31).getCredit();
			mComCredit=bankBookJVList.get(32).getCredit()+bankBookJVList.get(33).getCredit();
			mDomCredit=bankBookJVList.get(34).getCredit()+bankBookJVList.get(35).getCredit();
			
			interestIncome=bankBookJVList.get(36).getCredit();
			hhvNHV=bankBookJVList.get(37).getCredit();
			
			double totalCredit=powerCredit+cngCredit+capCredit+indCredit+comCredit+domCredit+sPowerCredit+sCapCredit+sCngCredit+sIndCredit+
							   sComCredit+sDomCredit+mPowerCredit+mCapCredit+mCngCredit+mIndCredit+mComCredit+mDomCredit+interestIncome+hhvNHV;
			
			
			bankBookDebitList=getBankBook_AcJVDebit();
			
			corporateTax=bankBookDebitList.get(0).getDebit();
			bankCharge=bankBookDebitList.get(1).getDebit();
			revenue=bankBookDebitList.get(2).getDebit();
			
			double totalDebit=totalCredit-(corporateTax+bankCharge+revenue);
			
			
					
			
			pcell = new PdfPCell(new Paragraph("Bank A/c STD "+accountInfo.getAccount_no()+", "+accountInfo.getBranch_name()+", "+accountInfo.getBank_name(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalDebit),ReportUtil.f9));
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
						
			pcell = new PdfPCell(new Paragraph("Corporate Tax(Adv, Payment)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("33150",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(corporateTax),ReportUtil.f9));
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
			
			pcell = new PdfPCell(new Paragraph("Bank Charges & Commission",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("87901",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bankCharge),ReportUtil.f9));
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
			
			pcell = new PdfPCell(new Paragraph("Rates & Taxes(Revenue Stamp)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("86901",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(revenue),ReportUtil.f9));
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
			
			/*------------------------------------------End of Debit Part--------------------------------------------------------------*/
			
			
			String str=accountInfo.getAccount_name();
			if(str.contains("INDUSTRIAL")){
				
				pcell = new PdfPCell(new Paragraph("Account Receivable (Power)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("22304",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(powerCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Account Receivable (CNG)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("22309",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(cngCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Account Receivable (Cap. Power)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("22306",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(capCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Account Receivable (Industrial)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("22303",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(indCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Account Receivable (HHV/NHV) Power",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("91107",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(hhvNHV),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				
				
				pcell = new PdfPCell(new Paragraph("Mt'r Rent Receivable (Power)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("91201",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(mPowerCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Mt'r Rent Receivable (CNG)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("91229",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(mCngCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Mt'r Rent Receivable (Cap. Power)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("91228",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(mCapCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Mt'r Rent Receivable (Industrial)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("91210",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(mIndCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				
				
				
				
				pcell = new PdfPCell(new Paragraph("Late Payment Penalties (Power)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("97007",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sPowerCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Late Payment Penalties (CNG)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("97006",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sCngCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Late Payment Penalties (Cap. Power)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("97005",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sCapCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Late Payment Penalties (Industrial)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("97001",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sIndCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Interest Income on STD A/c",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("95002",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(interestIncome),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
			}	
			
			else if(str.contains("COMMERCIAL")){
				
				pcell = new PdfPCell(new Paragraph("Account Receivable (Commercial)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("22302",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(comCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Mt'r Rent Receivable (Commercial)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("91220",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(mComCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Late Payment Penalties (Commercial)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("97002",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sComCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Interest Income on STD A/c",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("95002",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(interestIncome),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
			}else if(str.contains("DOMESTIC")){
				
				pcell = new PdfPCell(new Paragraph("Account Receivable (Domestic)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("22301",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(domCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Mt'r Rent Receivable (Domestic)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("91250",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(mDomCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Late Payment Penalties (Domestic)",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("97004",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(sDomCredit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Interest Income on STD A/c",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("95002",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(interestIncome),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				jvTable.addCell(pcell);
			}
			
			
			
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCredit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCredit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			if(report_for.equals("date_wise")){
				
				pcell = new PdfPCell(new Paragraph("Being the amount received against the above mentioned heads for the Period: "+from_date+" to "+to_date+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}else if(report_for.equals("month_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount received against the above mentioned heads for the month : "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}else if(report_for.equals("fiscal_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount received against the above mentioned heads for the FY : "+collection_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}
						
			
			/*----------------------------------------End of Debit Part------------------------------*/
		
			document.add(jvTable);
			
			document.newPage();
			
			//document.setMargins(20, 20, 30, 72);
			
			PdfPTable headerTable1 = new PdfPTable(3);
			   
			
			headerTable.setWidths(new float[] {
				5,90,5
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable1.addCell(pcell);
			
			PdfPTable mTable1=new PdfPTable(1);
			mTable1.setWidths(new float[]{100});
//			pcell=new PdfPCell(new Paragraph("JALALABAD GAS TRANSMISSION AND DISTRIBUTION SYSTEM LIMITED", ReportUtil.f9B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setBorder(0);	
//			mTable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable1.addCell(pcell);

			Chunk chunk3 = new Chunk("Revenue Section :",ReportUtil.f8B);
			Chunk chunk4 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f9B);
			Paragraph pp = new Paragraph(); 
			pp.add(chunk3);
			pp.add(chunk4);
			pcell=new PdfPCell(pp);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable1.addCell(pcell);
					
			pcell=new PdfPCell(mTable1);
			pcell.setBorder(0);
			headerTable1.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable1.addCell(pcell);
							
			document.add(headerTable1);
			
			
			PdfPTable headLinetable1 = null;
			headLinetable1 = new PdfPTable(3);
			headLinetable1.setWidthPercentage(100);
			headLinetable1.setWidths(new float[]{10,80,10});
			
			;
			
			if(report_for.equals("month_wise")){
				headLine="For the month "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year;
			}else if(report_for.equals("fiscal_wise")){
				headLine="For the FY :"+collection_year;
			}else if(report_for.equals("date_wise")){
				headLine="From "+from_date+" to "+to_date;
			}
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Journal Voucher",ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable1.addCell(pcell);
						
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(headLine,ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable1.addCell(pcell);
			
			document.add(headLinetable1);
			
			PdfPTable transferTable = new PdfPTable(7);			
			transferTable.setWidthPercentage(100);
			transferTable.setWidths(new float[] {30,5,15,20,5,20,5});
			
			
			pcell = new PdfPCell(new Paragraph("J.V. NO : 02",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(7);
			pcell.setBorder(0);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(7);
			pcell.setBorder(0);
			pcell.setPaddingTop(10f);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			pcell.setColspan(2);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Control Code",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Debit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Credit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ps.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ps.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			transferTable.addCell(pcell);
			
			transferJVList=getTransfer_AcJV();
			int listSize = transferJVList.size();
			
			double transferDebit = 0.0;
			
			for (int i = 0; i < listSize; i++) {
				
				pcell = new PdfPCell(new Paragraph(transferJVList.get(i).getParticulars(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				transferTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				transferTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				transferTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(transferJVList.get(i).getDebit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				transferTable.addCell(pcell);
				
				transferDebit=transferDebit+transferJVList.get(i).getDebit();
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				transferTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				transferTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				transferTable.addCell(pcell);
				
			}
			
			pcell = new PdfPCell(new Paragraph("Bank A/c"+accountInfo.getBank_name()+","+accountInfo.getBranch_name()+","+" STD-"+accountInfo.getAccount_no(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transferDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transferDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transferDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			if(report_for.equals("date_wise")){
				
				pcell = new PdfPCell(new Paragraph("Being the amount transferred to the above Bank A/c during the Period: "+from_date+" to "+to_date+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				transferTable.addCell(pcell);
			}else if(report_for.equals("month_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount transferred to the above Bank A/c during  the month : "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				transferTable.addCell(pcell);
			}else if(report_for.equals("fiscal_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount transferred to the above Bank A/c during the FY : "+collection_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				transferTable.addCell(pcell);
			}
			
			document.add(transferTable);
			
			
			/*--------------------------------------END of Transfer Part--------------------------------------------------*/
			
			document.newPage();
			
			document.setMargins(20, 20, 30, 72);
			PdfPTable receiveTable = new PdfPTable(7);			
			receiveTable.setWidthPercentage(100);
			receiveTable.setWidths(new float[] {30,5,15,20,5,20,5});
			
			
			pcell = new PdfPCell(new Paragraph("J.V. NO : 03",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(7);
			pcell.setBorder(0);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(7);
			pcell.setBorder(0);
			pcell.setPaddingTop(10f);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			pcell.setColspan(2);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Control Code",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Debit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Credit",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(2);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ps.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Tk.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Ps.",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			receiveTable.addCell(pcell);
			
			receiveJVList=getReceive_AcJV();
			
			int listSiz=receiveJVList.size();
			
			double receiveDebit = 0.0;
			
			for (int i = 0; i < listSiz; i++) {
				
				receiveDebit=receiveDebit+receiveJVList.get(i).getCredit();
				
			}
			pcell = new PdfPCell(new Paragraph("Bank A/c"+accountInfo.getBank_name()+","+accountInfo.getBranch_name()+","+" STD-"+accountInfo.getAccount_no(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(receiveDebit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			for (int i = 0; i < listSiz; i++) {
				
				pcell = new PdfPCell(new Paragraph(receiveJVList.get(i).getParticulars(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				receiveTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				receiveTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				receiveTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				receiveTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				receiveTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(receiveJVList.get(i).getCredit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				receiveTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				receiveTable.addCell(pcell);
				
			}
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(receiveDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(receiveDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			receiveTable.addCell(pcell);
			
			if(report_for.equals("date_wise")){
				
				pcell = new PdfPCell(new Paragraph("Being the amount received from the above mentioned Bank A/c during the Period: "+from_date+" to "+to_date+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				receiveTable.addCell(pcell);
			}else if(report_for.equals("month_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount received from the above mentioned Bank A/c during  the month : "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				receiveTable.addCell(pcell);
			}else if(report_for.equals("fiscal_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount received from the above mentioned Bank A/c during the FY : "+collection_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				receiveTable.addCell(pcell);
			}
			
			document.add(receiveTable);
		
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
				
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
		
	
	private ArrayList<JournalVoucherDTO>getBankBook_AcJVDebit(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
					
			String wClause="";
			
			if(report_for.equals("date_wise")){
				wClause=" AND trans_date between to_date('"+from_date+"','dd-mm-yyyy') and to_date('"+to_date+"','dd-mm-yyyy') ";
			}if(report_for.equals("month_wise")){
				wClause=" AND to_char(trans_date,'mm') ="+bill_month+" and to_char(trans_date,'yyyy') ="+bill_year+" ";
			}if(report_for.equals("fiscal_wise")){
				String[] fiscalYear=collection_year.split("-");
				String firstPart="01-07-"+fiscalYear[0].toString();
				String secondPart="30-06-"+fiscalYear[1].toString();
				
				wClause=" AND trans_date between to_date('"+firstPart+"','dd-mm-yyyy') and to_date('"+secondPart+"','dd-mm-yyyy') ";
			}
			
			String defaulterSql="select 'Corporate Tax' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger  " +
					"WHERE BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' "+
					"AND trans_type=2  " +
					"AND PARTICULARS LIKE '%Corporate Tax%'  " +wClause+
					"UNION ALL  " +
					"select PARTICULARS,sum(DEBIT) DEBIT from( " +
					"select 'Bank Charges' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger  " +
					"WHERE BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' "+
					"AND trans_type=2 " +
					"AND PARTICULARS LIKE '%Bank Charges%'   " +wClause+
					"union all  " +
					"select 'Bank Charges' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger  " +
					"WHERE BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' "+
					"AND trans_type=2 " +
					"AND  PARTICULARS LIKE '%Excise Duty%' " +wClause+
					" ) group by PARTICULARS " +
					"UNION ALL  " +
					"select 'Rates and Tax' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger  " +
					"WHERE BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' "+
					"AND trans_type=2  " +
					"AND PARTICULARS LIKE '%Rates%' " +wClause ;

					
					
					
					
					
					
					
					
//					"select 'Corporate Tax' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger " +
//					"WHERE BANK_ID='"+bank_id+"' " +
//					"AND BRANCH_ID='"+branch_id+"' " +
//					"AND ACCOUNT_NO='"+account_no+"' "+
//					"AND trans_type=2 " +
//					"AND PARTICULARS LIKE '%Corporate Tax%' " +wClause+
//					"UNION ALL " +
//					"select 'Bank Charges' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger " +
//					"WHERE BANK_ID='"+bank_id+"' " +
//					"AND BRANCH_ID='"+branch_id+"' " +
//					"AND ACCOUNT_NO='"+account_no+"' " +
//					"AND trans_type=2 " +
//					"AND PARTICULARS LIKE '%Bank Charges%' " +wClause+
//					"UNION ALL " +
//					"select 'Rates and Tax' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger " +
//					"WHERE BANK_ID='"+bank_id+"' " +
//					"AND BRANCH_ID='"+branch_id+"' " +
//					"AND ACCOUNT_NO='"+account_no+"' " +
//					"AND trans_type=2 " +
//					"AND PARTICULARS LIKE '%Rates%' " +wClause;


			
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setDebit(resultSet.getDouble("DEBIT"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	

	private ArrayList<JournalVoucherDTO>getBankBook_AcJV(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			
			
			
			String wClause="";
			String wClause2="";
					
			if(report_for.equals("date_wise")){
				wClause=" AND trans_date between to_date('"+from_date+"','dd-mm-yyyy') and to_date('"+to_date+"','dd-mm-yyyy') ";
				wClause2=" And COLLECTION_DATE BETWEEN to_DATE ('"+from_date+"', 'dd-MM-YYYY') AND to_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			}if(report_for.equals("month_wise")){
				wClause=" AND to_char(trans_date,'mm') ="+bill_month+" and to_char(trans_date,'yyyy') ="+bill_year+" ";
				wClause2=" And to_char(COLLECTION_DATE,'mm')="+bill_month+" and to_char(COLLECTION_DATE,'YYYY')="+bill_year;
			}if(report_for.equals("fiscal_wise")){
				String[] fiscalYear=collection_year.split("-");
				String firstPart="01-07-"+fiscalYear[0].toString();
				String secondPart="30-06-"+fiscalYear[1].toString();
				
				wClause=" AND trans_date between to_date('"+firstPart+"','dd-mm-yyyy') and to_date('"+secondPart+"','dd-mm-yyyy') ";
				wClause2=" And COLLECTION_DATE BETWEEN to_DATE ('"+firstPart+"', 'dd-MM-YYYY') AND to_DATE ('"+secondPart+"', 'dd-MM-YYYY')";
			}
			
			String defaulterSql="SELECT PARTICULARS, " +
					"       CATEGORY_ID, " +
					"       CREDIT, " +
					"       CATEGORY_NAME, " +
					"       CATEGORY_TYPE " +
					"  FROM (  SELECT PARTICULARS, " +
					"                 CATEGORY_ID, " +
					"                 SUM (NVL (CREDIT, 0))-sum(nvl( HHV_NHV_BILL, 0)) CREDIT, " +
					"                 CATEGORY_NAME, " +
					"                 CATEGORY_TYPE " +
					"            FROM (  SELECT 'Account Receivable' AS PARTICULARS, " +
					"                           CATEGORY_ID, " +
					"                           SUM (credit) CREDIT,sum(HHV_NHV_BILL) HHV_NHV_BILL, " +
					"                           CATEGORY_NAME, " +
					"                           CATEGORY_TYPE " +
					"                      FROM (SELECT MCC.CATEGORY_ID CATEGORY_ID, " +
					"                                   CREDIT,HHV_NHV_BILL, " +
					"                                   CATEGORY_NAME, " +
					"                                   CATEGORY_TYPE " +
					"                              FROM (  SELECT SUBSTR (bal.CUSTOMER_ID, 3, 2) " +
					"                                                CATEGORY_ID, " +
					"                                               SUM (NVL (DEBIT, 0)) " +
					"                                             - SUM (NVL (METER_RENT, 0)) " +
					"                                             - SUM (NVL (SURCHARGE, 0)) " +
					"                                                CREDIT " +
					"                                        FROM bank_account_ledger BAL " +
					"                                       WHERE     trans_type = 1 " +
					"                                             AND BANK_ID = '"+bank_id+"' " +
					"                           AND BRANCH_ID = '"+branch_id+"' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                                    GROUP BY SUBSTR (bal.CUSTOMER_ID, 3, 2)) tab, " +
					"                                    (SELECT NVL (SUM (HHV_NHV_BILL), 0) HHV_NHV_BILL, SUBSTR (CUSTOMER_ID, 3, 2) CATEGORY_ID,ACCOUNT_NO  " +
					"                                        FROM SUMMARY_MARGIN_PB pb,BILL_COLLECTION_METERED bcm  " +
					"                                        WHERE pb.bill_id = BCM.BILL_ID  " +
					"                                        AND BANK_ID = '"+bank_id+"' " +
					"                           AND BRANCH_ID = '"+branch_id+"' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause2+
					"                                        GROUP BY SUBSTR (CUSTOMER_ID, 3, 2),ACCOUNT_NO) tab2, " +
					"                                   mst_customer_category MCC " +
					"                             WHERE     MCC.CATEGORY_ID = tab.CATEGORY_ID(+) " +
					"                                   AND MCC.CATEGORY_ID=tab2.CATEGORY_ID(+) " +
					"                                   AND MCC.CATEGORY_ID <> '14' " +
					"                                   AND MCC.CATEGORY_ID <> '13') " +
					"                  GROUP BY CATEGORY_NAME, CATEGORY_TYPE, CATEGORY_ID " +
					"                  ORDER BY CATEGORY_ID DESC) " +
					"        GROUP BY PARTICULARS, " +
					"                 CATEGORY_ID, " +
					"                 CATEGORY_NAME, " +
					"                 CATEGORY_TYPE " +
					"        ORDER BY CATEGORY_ID DESC) " +
					"UNION ALL " +
					"SELECT PARTICULARS, " +
					"       CATEGORY_ID, " +
					"       CREDIT, " +
					"       CATEGORY_NAME, " +
					"       CATEGORY_TYPE " +
					"  FROM (  SELECT PARTICULARS, " +
					"                 MCC.CATEGORY_ID CATEGORY_ID, " +
					"                 CREDIT, " +
					"                 CATEGORY_NAME, " +
					"                 CATEGORY_TYPE " +
					"            FROM (  SELECT 'Late Payment Penalties' AS PARTICULARS, " +
					"                           SUBSTR (CUSTOMER_ID, 3, 2) CATEGORY_ID, " +
					"                           SUM (Surcharge) CREDIT " +
					"                      FROM bank_account_ledger " +
					"                     WHERE     trans_type = 1 " +
					"                           AND CUSTOMER_ID IS NOT NULL " +
					"                           AND BANK_ID = '"+bank_id+"' " +
					"                           AND BRANCH_ID = '"+branch_id+"' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                  GROUP BY SUBSTR (CUSTOMER_ID, 3, 2)) tab1, " +
					"                 mst_customer_category MCC " +
					"           WHERE     MCC.CATEGORY_ID = tab1.CATEGORY_ID(+) " +
					"                 AND MCC.CATEGORY_ID <> '14' " +
					"                 AND MCC.CATEGORY_ID <> '13' " +
					"        ORDER BY CATEGORY_ID DESC) " +
					"UNION ALL " +
					"SELECT PARTICULARS, " +
					"       CATEGORY_ID, " +
					"       CREDIT, " +
					"       CATEGORY_NAME, " +
					"       CATEGORY_TYPE " +
					"  FROM (  SELECT PARTICULARS, " +
					"                 MCC.CATEGORY_ID CATEGORY_ID, " +
					"                 CREDIT, " +
					"                 CATEGORY_NAME, " +
					"                 CATEGORY_TYPE " +
					"            FROM (  SELECT 'Mtr Rent Receivable' AS PARTICULARS, " +
					"                           SUBSTR (CUSTOMER_ID, 3, 2) CATEGORY_ID, " +
					"                           SUM (METER_RENT) CREDIT " +
					"                      FROM bank_account_ledger " +
					"                     WHERE     trans_type = 1 " +
					"                           AND CUSTOMER_ID IS NOT NULL " +
					"                           AND BANK_ID = '"+bank_id+"' " +
					"                           AND BRANCH_ID = '"+branch_id+"' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"                  GROUP BY SUBSTR (CUSTOMER_ID, 3, 2)) tab1, " +
					"                 mst_customer_category MCC " +
					"           WHERE     MCC.CATEGORY_ID = tab1.CATEGORY_ID(+) " +
					"                 AND MCC.CATEGORY_ID <> '14' " +
					"                 AND MCC.CATEGORY_ID <> '13' " +
					"        ORDER BY CATEGORY_ID DESC) " +
					"UNION ALL " +
					"SELECT PARTICULARS, " +
					"       NULL CATEGORY_ID, " +
					"       CREDIT, " +
					"       NULL CATEGORY_NAME, " +
					"       NULL CATEGORY_TYPE " +
					"  FROM (SELECT 'Interest Income on STD Ac' AS PARTICULARS, " +
					"               NULL CATEGORY_ID, " +
					"               SUM (DEBIT) CREDIT " +
					"          FROM bank_account_ledger " +
					"         WHERE     BANK_ID = '"+bank_id+"' " +
					"                           AND BRANCH_ID = '"+branch_id+"' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
					"               AND trans_type in(5,3)  " +
					"               AND PARTICULARS not like '%Refund By%' ) " +
					"union all " +
					"select 'HHV/NHV Receivable' AS PARTICULARS,null CATEGORY_ID,sum(CREDIT) CREDIT,NULL CATEGORY_NAME, NULL CATEGORY_TYPE from( " +
					"SELECT SUBSTR (CUSTOMER_ID, 3, 2) CATEGORY_ID,NVL (SUM (HHV_NHV_BILL), 0) Credit,ACCOUNT_NO,COLLECTION_DATE  " +
					"                                        FROM SUMMARY_MARGIN_PB pb,BILL_COLLECTION_METERED bcm  " +
					"                                        WHERE pb.bill_id = BCM.BILL_ID  " +
					"                                        AND BANK_ID = '"+bank_id+"' " +
					"                           AND BRANCH_ID = '"+branch_id+"' " +
					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause2+
					"                                        GROUP BY SUBSTR (CUSTOMER_ID, 3, 2),ACCOUNT_NO,COLLECTION_DATE) " ;

					
					
					
					
					
					
					
					
					
//					"SELECT PARTICULARS, " +
//					"       CATEGORY_ID, " +
//					"       CREDIT, " +
//					"       CATEGORY_NAME, " +
//					"       CATEGORY_TYPE " +
//					"  FROM (  SELECT PARTICULARS, " +
//					"                 CATEGORY_ID, " +
//					"                 SUM (nvl(CREDIT,0)) CREDIT, " +
//					"                 CATEGORY_NAME, " +
//					"                 CATEGORY_TYPE " +
//					"            FROM (SELECT  'Account Receivable' AS PARTICULARS,CATEGORY_ID,sum(credit) CREDIT,CATEGORY_NAME,CATEGORY_TYPE from( " +
//					"SELECT   MCC.CATEGORY_ID CATEGORY_ID, " +
//					"                         CREDIT, " +
//					"                         CATEGORY_NAME, " +
//					"                         CATEGORY_TYPE " +
//					"                    FROM (SELECT  SUBSTR (bal.CUSTOMER_ID, 3, 2) CATEGORY_ID,SUM (nvl(DEBIT,0))- SUM (nvl(METER_RENT,0))- SUM (nvl(SURCHARGE,0))  CREDIT "+
//                    "         FROM bank_account_ledger BAL "+
//                    "        WHERE  trans_type=1 "+
//                    "             AND BANK_ID = '"+bank_id+"' " +
//                    "             AND BRANCH_ID = '"+branch_id+"' " +
//                    "            AND ACCOUNT_NO = '"+account_no+"' " +wClause+
//                    "  GROUP BY SUBSTR (bal.CUSTOMER_ID, 3, 2)) tab, " +
//					"                         mst_customer_category MCC " +
//					"                   WHERE     MCC.CATEGORY_ID = tab.CATEGORY_ID(+) " +
//					"                         AND MCC.CATEGORY_ID <> '14' " +
//					"                         AND MCC.CATEGORY_ID <> '13') " +
//					"                         group by CATEGORY_NAME,CATEGORY_TYPE,CATEGORY_ID " +
//					"                         order by CATEGORY_ID desc) " +
//					"        GROUP BY PARTICULARS, " +
//					"                 CATEGORY_ID, " +
//					"                 CATEGORY_NAME, " +
//					"                 CATEGORY_TYPE " +
//					"        ORDER BY CATEGORY_ID DESC) " +
//					"UNION ALL " +
//					"SELECT PARTICULARS, " +
//					"       CATEGORY_ID, " +
//					"       CREDIT, " +
//					"       CATEGORY_NAME, " +
//					"       CATEGORY_TYPE " +
//					"  FROM (  SELECT PARTICULARS, " +
//					"                 MCC.CATEGORY_ID CATEGORY_ID, " +
//					"                 CREDIT, " +
//					"                 CATEGORY_NAME, " +
//					"                 CATEGORY_TYPE " +
//					"            FROM (  SELECT 'Late Payment Penalties' AS PARTICULARS, " +
//					"                           SUBSTR (CUSTOMER_ID, 3, 2) CATEGORY_ID, " +
//					"                           SUM (Surcharge) CREDIT " +
//					"                      FROM bank_account_ledger " +
//					"                     WHERE     trans_type = 1 " +
//					"                           AND CUSTOMER_ID IS NOT NULL " +
//					"                           AND BANK_ID = '"+bank_id+"' " +
//					"                           AND BRANCH_ID = '"+branch_id+"' " +
//					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
//					"                  GROUP BY SUBSTR (CUSTOMER_ID, 3, 2)) tab1, " +
//					"                 mst_customer_category MCC " +
//					"           WHERE     MCC.CATEGORY_ID = tab1.CATEGORY_ID(+) " +
//					"                 AND MCC.CATEGORY_ID <> '14' " +
//					"                 AND MCC.CATEGORY_ID <> '13' " +
//					"        ORDER BY CATEGORY_ID DESC) " +
//					"UNION ALL " +
//					"SELECT PARTICULARS, " +
//					"       CATEGORY_ID, " +
//					"       CREDIT, " +
//					"       CATEGORY_NAME, " +
//					"       CATEGORY_TYPE " +
//					"  FROM (  SELECT PARTICULARS, " +
//					"                 MCC.CATEGORY_ID CATEGORY_ID, " +
//					"                 CREDIT, " +
//					"                 CATEGORY_NAME, " +
//					"                 CATEGORY_TYPE " +
//					"            FROM (  SELECT 'Mtr Rent Receivable' AS PARTICULARS, " +
//					"                           SUBSTR (CUSTOMER_ID, 3, 2) CATEGORY_ID, " +
//					"                           SUM (METER_RENT) CREDIT " +
//					"                      FROM bank_account_ledger " +
//					"                     WHERE     trans_type = 1 " +
//					"                           AND CUSTOMER_ID IS NOT NULL " +
//					"                           AND BANK_ID = '"+bank_id+"' " +
//					"                           AND BRANCH_ID = '"+branch_id+"' " +
//					"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
//					"                  GROUP BY SUBSTR (CUSTOMER_ID, 3, 2)) tab1, " +
//					"                 mst_customer_category MCC " +
//					"           WHERE     MCC.CATEGORY_ID = tab1.CATEGORY_ID(+) " +
//					"                 AND MCC.CATEGORY_ID <> '14' " +
//					"                 AND MCC.CATEGORY_ID <> '13' " +
//					"        ORDER BY CATEGORY_ID DESC) " +
//					"UNION ALL " +
//					"SELECT PARTICULARS, " +
//					"       NULL CATEGORY_ID, " +
//					"       CREDIT, " +
//					"       NULL CATEGORY_NAME, " +
//					"       NULL CATEGORY_TYPE " +
//					"  FROM (SELECT 'Interest Income on STD Ac' AS PARTICULARS, " +
//					"               NULL CATEGORY_ID, " +
//					"               SUM (DEBIT) CREDIT " +
//					"          FROM bank_account_ledger " +
//					"         WHERE     BANK_ID = '"+bank_id+"' " +
//					"               AND BRANCH_ID = '"+branch_id+"' " +
//					"               AND ACCOUNT_NO = '"+account_no+"' " +wClause+
//					"               AND trans_type = 5) " ;

			
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setCredit(resultSet.getDouble("CREDIT"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	
	private ArrayList<JournalVoucherDTO>getTransfer_AcJV(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			
			
			
			String wClause="";
			
			if(report_for.equals("date_wise")){
				wClause=" AND trans_date between to_date('"+from_date+"','dd-mm-yyyy') and to_date('"+to_date+"','dd-mm-yyyy') ";
			}if(report_for.equals("month_wise")){
				wClause=" AND to_char(trans_date,'mm') ="+bill_month+" and to_char(trans_date,'yyyy') ="+bill_year+" ";
			}if(report_for.equals("fiscal_wise")){
				String[] fiscalYear=collection_year.split("-");
				String firstPart="01-07-"+fiscalYear[0].toString();
				String secondPart="30-06-"+fiscalYear[1].toString();
				
				wClause=" AND trans_date between to_date('"+firstPart+"','dd-mm-yyyy') and to_date('"+secondPart+"','dd-mm-yyyy') ";
			}
			
			String defaulterSql="select * from bank_account_ledger " +
					"where trans_type=4 " +
					"AND BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' " +wClause+
					"AND PARTICULARS LIKE '%AMOUNT TRANSFER%' " ;
		
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setDebit(resultSet.getDouble("CREDIT"));
        		jDto.setParticulars(resultSet.getString("PARTICULARS"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	
	private ArrayList<JournalVoucherDTO>getReceive_AcJV(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			
			
			
			String wClause="";
			
			if(report_for.equals("date_wise")){
				wClause=" AND trans_date between to_date('"+from_date+"','dd-mm-yyyy') and to_date('"+to_date+"','dd-mm-yyyy') ";
			}if(report_for.equals("month_wise")){
				wClause=" AND to_char(trans_date,'mm') ="+bill_month+" and to_char(trans_date,'yyyy') ="+bill_year+" ";
			}if(report_for.equals("fiscal_wise")){
				String[] fiscalYear=collection_year.split("-");
				String firstPart="01-07-"+fiscalYear[0].toString();
				String secondPart="30-06-"+fiscalYear[1].toString();
				
				wClause=" AND trans_date between to_date('"+firstPart+"','dd-mm-yyyy') and to_date('"+secondPart+"','dd-mm-yyyy') ";
			}
			
			String defaulterSql="select * from bank_account_ledger " +
					"where trans_type=4 " +
					"AND BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' " +wClause+
					"AND PARTICULARS LIKE '%AMOUNT RECEIVED%' " ;
							
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setCredit(resultSet.getDouble("DEBIT"));
        		jDto.setParticulars(resultSet.getString("PARTICULARS"));
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
	
	
	private AccountDTO getAccountInfo()
	{
           AccountDTO accountInfo=null;
           BranchDTO branchInfo=new BranchDTO();
		
		try {
			accountInfo = new AccountDTO();
			
			String account_info_sql="select * from MST_ACCOUNT_INFO mci,MST_BANK_INFO mbi,MST_BRANCH_INFO mbri " +
					"where mci.bank_id=mbi.bank_id " +
					"and mci.branch_id=mbri.branch_id " +
					"and MCI.BANK_ID=? " +
					"and MCI.branch_id=? " +
					"and MCI.account_no=? " ;



			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			ps1.setString(1, bank_id);
			ps1.setString(2, branch_id);
			ps1.setString(3, account_no);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		
        		accountInfo.setBank_name(resultSet.getString("BANK_NAME"));
        		accountInfo.setBranch_name(resultSet.getString("BRANCH_NAME"));
        		accountInfo.setBranch(branchInfo);
        		accountInfo.setAccount_name(resultSet.getString("ACCOUNT_NAME"));
        		accountInfo.setAccount_no(resultSet.getString("ACCOUNT_NO")) ; 
        		accountInfo.setOpening_balance(resultSet.getString("OPENING_BALANCE"));
        		accountInfo.setAc_opening_date(resultSet.getString("OPENING_DATE"));
   
        		
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accountInfo;
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


	public String getBank_id() {
		return bank_id;
	}


	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}


	public String getBranch_id() {
		return branch_id;
	}


	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}


	public String getAccount_no() {
		return account_no;
	}


	public void setAccount_no(String account_no) {
		this.account_no = account_no;
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

	
	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

	
	
  }




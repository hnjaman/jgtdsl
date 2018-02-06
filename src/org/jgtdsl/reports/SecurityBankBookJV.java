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
import org.jgtdsl.dto.BankBookDTO;
import org.jgtdsl.dto.BranchDTO;
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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class SecurityBankBookJV extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<BankBookDTO>bankBookJVList = new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO>bankBooCreditList = new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO>bankBookDebitList = new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO>transferJVList = new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO>receiveJVList = new ArrayList<BankBookDTO>();
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
				
		String fileName="Security_BankBook_JV.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(20,20,30,72);
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

			Chunk chunk1 = new Chunk("Head Office :",ReportUtil.f8B);
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
				headLine="Journal Voucher of Security Bank Book for the month "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year;
			}else if(report_for.equals("fiscal_wise")){
				headLine="Journal Voucher of Security Bank Book FY :"+collection_year;
			}else if(report_for.equals("date_wise")){
				headLine="Journal Voucher of Security BankBook From "+from_date+" to "+to_date;
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
			jvTable.setWidths(new float[]{30,5,15,20,5,20,5});
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
						
			double conpowerCredit=0.0;
			double concapCredit=0.0;
			double concngCredit=0.0;
			double conindCredit=0.0;
			double concomCredit=0.0;
			double condomCredit=0.0;
			
			double commPowerCredit=0.0;
			double commCapCredit=0.0;
			double commCngCredit=0.0;
			double commIndCredit=0.0;
			double commComCredit=0.0;
			double commDomCredit=0.0;
			
			double security=0.0;
			double saleOfStore=0.0;
			double distributionLine=0.0;
			double serviceCharge=0.0;
			double penalty=0.0;
			double saleOfApplication=0.0;
			double othersIncome=0.0;
			double interestIncome=0.0; 
			
			double corporateTax=0.0;
			double bankCharge=0.0;
			
			accountInfo = getAccountInfo();
			bankBookJVList=getBankBook_AcJV();
			bankBooCreditList=getCredit_AcJV();
			
			
			condomCredit=bankBookJVList.get(0).getConnectionFee()+bankBookJVList.get(1).getConnectionFee();
			concomCredit=bankBookJVList.get(2).getConnectionFee()+bankBookJVList.get(3).getConnectionFee();
			conindCredit=bankBookJVList.get(4).getConnectionFee()+bankBookJVList.get(5).getConnectionFee();
			concapCredit=bankBookJVList.get(6).getConnectionFee()+bankBookJVList.get(7).getConnectionFee();
			concngCredit=bankBookJVList.get(8).getConnectionFee()+bankBookJVList.get(9).getConnectionFee();
			conpowerCredit=bankBookJVList.get(10).getConnectionFee()+bankBookJVList.get(11).getConnectionFee();
			
			commDomCredit=bankBookJVList.get(0).getCommissionFee()+bankBookJVList.get(1).getCommissionFee();
			commComCredit=bankBookJVList.get(2).getCommissionFee()+bankBookJVList.get(3).getCommissionFee();
			commIndCredit=bankBookJVList.get(4).getCommissionFee()+bankBookJVList.get(5).getCommissionFee();
			commCapCredit=bankBookJVList.get(6).getCommissionFee()+bankBookJVList.get(7).getCommissionFee();
			commCngCredit=bankBookJVList.get(8).getCommissionFee()+bankBookJVList.get(9).getCommissionFee();
			commPowerCredit=bankBookJVList.get(10).getCommissionFee()+bankBookJVList.get(11).getCommissionFee();
			
			security=bankBookJVList.get(0).getSecurityDeposit()+bankBookJVList.get(1).getSecurityDeposit()+
					 bankBookJVList.get(2).getSecurityDeposit()+bankBookJVList.get(3).getSecurityDeposit()+
					 bankBookJVList.get(4).getSecurityDeposit()+bankBookJVList.get(5).getSecurityDeposit()+
					 bankBookJVList.get(6).getSecurityDeposit()+bankBookJVList.get(7).getSecurityDeposit()+
					 bankBookJVList.get(8).getSecurityDeposit()+bankBookJVList.get(9).getSecurityDeposit()+
					 bankBookJVList.get(10).getSecurityDeposit()+bankBookJVList.get(11).getSecurityDeposit();
			
			saleOfStore=bankBookJVList.get(0).getSaleOfStore()+bankBookJVList.get(1).getSaleOfStore()+
					 bankBookJVList.get(2).getSaleOfStore()+bankBookJVList.get(3).getSaleOfStore()+
					 bankBookJVList.get(4).getSaleOfStore()+bankBookJVList.get(5).getSaleOfStore()+
					 bankBookJVList.get(6).getSaleOfStore()+bankBookJVList.get(7).getSaleOfStore()+
					 bankBookJVList.get(8).getSaleOfStore()+bankBookJVList.get(9).getSaleOfStore()+
					 bankBookJVList.get(10).getSaleOfStore()+bankBookJVList.get(11).getSaleOfStore();
			
			distributionLine=bankBookJVList.get(0).getPipeLineConstruction()+bankBookJVList.get(1).getPipeLineConstruction()+
					 bankBookJVList.get(2).getPipeLineConstruction()+bankBookJVList.get(3).getPipeLineConstruction()+
					 bankBookJVList.get(4).getPipeLineConstruction()+bankBookJVList.get(5).getPipeLineConstruction()+
					 bankBookJVList.get(6).getPipeLineConstruction()+bankBookJVList.get(7).getPipeLineConstruction()+
					 bankBookJVList.get(8).getPipeLineConstruction()+bankBookJVList.get(9).getPipeLineConstruction()+
					 bankBookJVList.get(10).getPipeLineConstruction()+bankBookJVList.get(11).getPipeLineConstruction();
			
			serviceCharge=bankBookJVList.get(0).getServiceCharge()+bankBookJVList.get(1).getServiceCharge()+
					 bankBookJVList.get(2).getServiceCharge()+bankBookJVList.get(3).getServiceCharge()+
					 bankBookJVList.get(4).getServiceCharge()+bankBookJVList.get(5).getServiceCharge()+
					 bankBookJVList.get(6).getServiceCharge()+bankBookJVList.get(7).getServiceCharge()+
					 bankBookJVList.get(8).getServiceCharge()+bankBookJVList.get(9).getServiceCharge()+
					 bankBookJVList.get(10).getServiceCharge()+bankBookJVList.get(11).getServiceCharge();
			
			penalty= bankBookJVList.get(0).getPenalties()+bankBookJVList.get(1).getPenalties()+
					 bankBookJVList.get(2).getPenalties()+bankBookJVList.get(3).getPenalties()+
					 bankBookJVList.get(4).getPenalties()+bankBookJVList.get(5).getPenalties()+
					 bankBookJVList.get(6).getPenalties()+bankBookJVList.get(7).getPenalties()+
					 bankBookJVList.get(8).getPenalties()+bankBookJVList.get(9).getPenalties()+
					 bankBookJVList.get(10).getPenalties()+bankBookJVList.get(11).getPenalties();
			
			othersIncome=bankBookJVList.get(0).getOthersIncome()+bankBookJVList.get(1).getOthersIncome()+
					 bankBookJVList.get(2).getOthersIncome()+bankBookJVList.get(3).getOthersIncome()+
					 bankBookJVList.get(4).getOthersIncome()+bankBookJVList.get(5).getOthersIncome()+
					 bankBookJVList.get(6).getOthersIncome()+bankBookJVList.get(7).getOthersIncome()+
					 bankBookJVList.get(8).getOthersIncome()+bankBookJVList.get(9).getOthersIncome()+
					 bankBookJVList.get(10).getOthersIncome()+bankBookJVList.get(11).getOthersIncome();
			
			saleOfApplication=bankBooCreditList.get(0).getCredit();
			interestIncome=bankBooCreditList.get(1).getCredit();
			
			double totalCredit=condomCredit+concomCredit+conindCredit+concapCredit+concngCredit+conpowerCredit+
							   commDomCredit+commComCredit+commIndCredit+commCapCredit+commCngCredit+commPowerCredit+
							   security+saleOfStore+distributionLine+serviceCharge+penalty+othersIncome+saleOfApplication+
							   interestIncome;
			
			
			bankBookDebitList=getBankBook_AcJVDebit();
			
			corporateTax=bankBookDebitList.get(0).getDebit();
			bankCharge=bankBookDebitList.get(1).getDebit();
			
			double totalDebit=totalCredit-(corporateTax+bankCharge);
			
			
			
			pcell = new PdfPCell(new Paragraph("Bank A/c"+accountInfo.getBank_name()+","+accountInfo.getBranch_name()+","+" STD-"+accountInfo.getAccount_no(),ReportUtil.f9));
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
			
			
			/*------------------------------------------End of Debit Part--------------------------------------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("Security Deposit (Customer)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("33906",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(security),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Connection Fees (Domestic)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91350",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(condomCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Connection Fees (Commercial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91320",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(concomCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Connection Fees (Industrial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91310",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(conindCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Connection Fees (Captive Power)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91302",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(concapCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Connection Fees (CNG)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(concngCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Connection Fees (Power)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("91301",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(conpowerCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/*----------------------------------------End of Connection Fees--------------------------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("Commissioning Fees (Domestic)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("94101",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(commDomCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commissioning Fees (Commercial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("94101",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(commComCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commissioning Fees (Industrial)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("94101",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(commIndCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commissioning Fees (Captive Power)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("94101",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(commCapCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commissioning Fees (CNG)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("94101",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(commCngCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commissioning Fees (Power)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("94101",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(commPowerCredit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			/*---------------------------------------End of Commissioning Fees-------------------------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("Profit & Loss on Sale of Stores",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("97510",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(saleOfStore),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Distribution Line",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("01073",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(distributionLine),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Service Charges",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("94001",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(serviceCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Penalty & Fine Received",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("97009",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(penalty),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sale of Application & Bill Books",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("97010",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(saleOfApplication),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Others Income",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Cr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("97990",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(othersIncome),ReportUtil.f9));
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
			
			/*-----------------------------------End of Credit Part-----------------------------------------------------*/
			
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
				
				pcell = new PdfPCell(new Paragraph("(Being the amount received against the above heads taken into A/c through this Journal Voucher)",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}else if(report_for.equals("month_wise")){
				pcell = new PdfPCell(new Paragraph("(Being the amount received against the above heads taken into A/c through this Journal Voucher)",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}else if(report_for.equals("fiscal_wise")){
				pcell = new PdfPCell(new Paragraph("(Being the amount received against the above heads taken into A/c through this Journal Voucher)",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}
			
			/*----------------------------------------End of Journal 01 Part------------------------------*/
		
			document.add(jvTable);
			
			document.newPage();
			
			document.setMargins(20, 20, 30, 72);
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
				
				pcell = new PdfPCell(new Paragraph(transferJVList.get(i).getParticular(),ReportUtil.f9));
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
				
				pcell = new PdfPCell(new Paragraph(receiveJVList.get(i).getParticular(),ReportUtil.f9));
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
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(receiveDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(receiveDebit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			transferTable.addCell(pcell);
			
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
			
		    
		}catch(Exception e){e.printStackTrace();
		
		}
		
		return null;
		
	}
		
	
	private ArrayList<BankBookDTO>getBankBook_AcJVDebit(){
		ArrayList<BankBookDTO>journalVoucherList = new ArrayList<BankBookDTO>();
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
			
			String defaulterSql="select 'Corporate Tax' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger " +
					"WHERE BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' "+
					"AND trans_type=2 " +
					"AND PARTICULARS LIKE '%Corporate Tax%' " +wClause+
					"UNION ALL " +
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
					"union all  " +
					"select 'Bank Charges' as PARTICULARS,sum(CREDIT) DEBIT from bank_account_ledger  " +
					"WHERE BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' "+
					"AND trans_type=2 " +
					"AND  PARTICULARS LIKE '%Rates%' " +wClause+
					" ) group by PARTICULARS " ;
							
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BankBookDTO jDto = new BankBookDTO();
        		jDto.setDebit(resultSet.getDouble("DEBIT"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	

	private ArrayList<BankBookDTO>getBankBook_AcJV(){
		ArrayList<BankBookDTO>journalVoucherList = new ArrayList<BankBookDTO>();
		try {
			
			
			
			String wClause="";
			
			if(report_for.equals("date_wise")){
				wClause=" AND DEPOSIT_DATE between to_date('"+from_date+"','dd-mm-yyyy') and to_date('"+to_date+"','dd-mm-yyyy') ";
			}if(report_for.equals("month_wise")){
				wClause=" AND to_char(DEPOSIT_DATE,'mm') ="+bill_month+" and to_char(DEPOSIT_DATE,'yyyy') ="+bill_year+" ";
			}if(report_for.equals("fiscal_wise")){
				String[] fiscalYear=collection_year.split("-");
				String firstPart="01-07-"+fiscalYear[0].toString();
				String secondPart="30-06-"+fiscalYear[1].toString();
				
				wClause=" AND DEPOSIT_DATE between to_date('"+firstPart+"','dd-mm-yyyy') and to_date('"+secondPart+"','dd-mm-yyyy') ";
			}
			
			String defaulterSql="select CATEGORY_ID,CATEGORY_NAME,TOTAL_DEPOSIT,SECURITYDEPOSIT,SALESOFSTORE,(CONNECTIONFEE+DISCONNECTIONFEE+RECONNECTIONFEE+LOADINCDESCFEE) CONNECTIONFEE,COMISSIONINGFEE,SERVICECHARGE,DISTRIBUTIONLINE, " +
					"PENALTY+ADDITIONALBILL PENALTY,(NAMECHANGEFEE+BURNERSHIFTINGFEE+RAIZERSHIFTINGFEE+CONSULTINGFEE+OTHERFEES) OTHERSINCOME ,cat from( " +
					"select  SUBSTR (CUSTOMER_ID, 3, 2) cat,  " +
					"         SUM (TOTAL_DEPOSIT) TOTAL_DEPOSIT, " +
					"         SUM (SECURITYDEPOSIT) SECURITYDEPOSIT, " +
					"         SUM (SALESOFSTORE) SALESOFSTORE, " +
					"         SUM (CONNECTIONFEE) CONNECTIONFEE, " +
					"         SUM (COMISSIONINGFEE) COMISSIONINGFEE, " +
					"         SUM (SERVICECHARGE) SERVICECHARGE, " +
					"         SUM (PIPELINECONSTRUCTION) DISTRIBUTIONLINE, " +
					"         SUM (LOADINCDESCFEE) LOADINCDESCFEE, " +
					"         SUM (DISCONNECTIONFEE) DISCONNECTIONFEE, " +
					"         SUM (RECONNECTIONFEE) RECONNECTIONFEE, " +
					"         SUM (ADDITIONALBILL) ADDITIONALBILL, " +
					"         SUM (PENALTY) PENALTY, " +
					"         SUM (NAMECHANGEFEE) NAMECHANGEFEE, " +
					"         SUM (BURNERSHIFTINGFEE) BURNERSHIFTINGFEE, " +
					"         SUM (RAIZERSHIFTINGFEE) RAIZERSHIFTINGFEE, " +
					"         SUM (CONSULTINGFEE) CONSULTINGFEE, " +
					"         SUM (OTHERFEES) OTHERFEES " +
					"    FROM MST_DEPOSIT md, VIEW_DEPOSIT vd " +
					"    where MD.DEPOSIT_ID = VD.DEPOSIT_ID " +
					"    AND BANK_ID='"+bank_id+"' " +
					"    AND BRANCH_ID='"+branch_id+"' " +
					"    AND ACCOUNT_NO ='"+account_no+"' " +wClause+
					"    group by SUBSTR (CUSTOMER_ID, 3, 2)) tab,MST_CUSTOMER_CATEGORY cc " +
					"    where tab.cat(+)=CC.CATEGORY_ID " +
					"    AND CATEGORY_ID<>'13' " +
					"    AND CATEGORY_ID<>'14' " +
					"    order by CATEGORY_ID " ;
		
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BankBookDTO bankDto = new BankBookDTO();
        		   
        		bankDto.setParticular(resultSet.getString("CATEGORY_NAME"));
        		bankDto.setTotalCollection(resultSet.getDouble("TOTAL_DEPOSIT"));
        		bankDto.setSecurityDeposit(resultSet.getDouble("SECURITYDEPOSIT"));
        		bankDto.setSaleOfStore(resultSet.getDouble("SALESOFSTORE"));
        		bankDto.setConnectionFee(resultSet.getDouble("CONNECTIONFEE"));
        		bankDto.setCommissionFee(resultSet.getDouble("COMISSIONINGFEE"));
        		bankDto.setServiceCharge(resultSet.getDouble("SERVICECHARGE"));
        		bankDto.setPipeLineConstruction(resultSet.getDouble("DISTRIBUTIONLINE"));
        		bankDto.setPenalties(resultSet.getDouble("PENALTY"));
        		bankDto.setOthersIncome(resultSet.getDouble("OTHERSINCOME"));
        		
        		journalVoucherList.add(bankDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return journalVoucherList;
	}
	
	

	private ArrayList<BankBookDTO>getCredit_AcJV(){
		ArrayList<BankBookDTO>journalVoucherList = new ArrayList<BankBookDTO>();
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
			
			String defaulterSql="select PARTICULARS,sum(CREDIT) CREDIT from( " +
					"select 'Sale of Application/Bill Book' PARTICULARS,  sum(nvl(DEBIT,0)) CREDIT from bank_account_ledger " +
					"where  BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' " +wClause+
					"AND trans_type=3 " +
					"AND PARTICULARS LIKE '%Sale of Bill%' " +
					"union all " +
					"select 'Sale of Application/Bill Book' PARTICULARS,  sum(nvl(DEBIT,0)) CREDIT from bank_account_ledger " +
					"where BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' " +wClause+
					"AND trans_type=3 " +
					"AND PARTICULARS LIKE '%Sales of Application%') " +
					"group by PARTICULARS  " +
					"UNION ALL  " +
					"select 'Interest Income' as Particulars, sum(DEBIT) CREDIT from bank_account_ledger  " +
					"WHERE BANK_ID='"+bank_id+"' " +
					"AND BRANCH_ID='"+branch_id+"' " +
					"AND ACCOUNT_NO='"+account_no+"' " +wClause+
					"AND trans_type in (5,3)  " ;

					
					
					
					
					
					
					
					
					
					
					
//					"select'Sale of Application/Bill Book' PARTICULARS,  sum(DEBIT) CREDIT from bank_account_ledger " +
//				"where trans_type=3 " +				
//					"AND BANK_ID='"+bank_id+"' " +
//					"AND BRANCH_ID='"+branch_id+"' " +
//					"AND ACCOUNT_NO='"+account_no+"' " +wClause+
//					"and PARTICULARS LIKE '%Sale of Bill%' OR PARTICULARS LIKE '%Sales of Application%' " +
//					"UNION ALL " +
//					"select 'Interest Income' as Particulars, sum(DEBIT) CREDIT from bank_account_ledger " +
//					"WHERE BANK_ID='"+bank_id+"' " +
//					"AND BRANCH_ID='"+branch_id+"' " +
//					"AND ACCOUNT_NO='"+account_no+"' " +wClause+
//					"AND trans_type=5 " ;
//					
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BankBookDTO jDto = new BankBookDTO();
        		jDto.setCredit(resultSet.getDouble("CREDIT"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	
	
	
	private ArrayList<BankBookDTO>getTransfer_AcJV(){
		ArrayList<BankBookDTO>journalVoucherList = new ArrayList<BankBookDTO>();
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
        		BankBookDTO jDto = new BankBookDTO();
        		jDto.setDebit(resultSet.getDouble("CREDIT"));
        		jDto.setParticular(resultSet.getString("PARTICULARS"));
        		
        		journalVoucherList.add(jDto);
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

		
		return journalVoucherList;
	}
	
	
	private ArrayList<BankBookDTO>getReceive_AcJV(){
		ArrayList<BankBookDTO>journalVoucherList = new ArrayList<BankBookDTO>();
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
        		BankBookDTO jDto = new BankBookDTO();
        		jDto.setCredit(resultSet.getDouble("DEBIT"));
        		jDto.setParticular(resultSet.getString("PARTICULARS"));
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


	
	
  }




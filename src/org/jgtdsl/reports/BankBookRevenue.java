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

public class BankBookRevenue extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	ArrayList<BankBookDTO> bankBookListDebit=new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO> bankBookListCredit=new ArrayList<BankBookDTO>();
	AccountDTO accountInfo=new AccountDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String bank_id;
	    private  String branch_id;
	    private  String account_no;
	    private  String collection_month;
	    private  String collection_year;
	    private  String collection_date;
	    private  String report_for; 
	    private  String area="01";
	    private  String from_date;
	    private  String to_date;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		BankBookDTO bankBookDTO = null;

	public String execute() throws Exception
	{
				
		String fileName="Bank_Book_Statement.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(20,20,30,72);
		PdfPCell pcell=null;
		bankBookDTO = new BankBookDTO();
		
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
            //img.setAbsolutePosition(145f, 780f);		
            	img.setAbsolutePosition(270f, 530f);		// rotate
            
	        document.add(img);
			
			
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A Company of PetroBangla)", ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("Regional Office :",ReportUtil.f9B);
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
			
			accountInfo = getAccountInfo();
			
			String acName=accountInfo.getAccount_name();
			String part=acName.substring(0, acName.length() -3);
			
			
			PdfPTable headLinetable = null;
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{10,80,10});
			
			String headLine="";
			if(report_for.equals("month_wise")){
				headLine="BANK BOOK OF "+part;
			}else if(report_for.equals("date_wise")){
				headLine="BANK BOOK REVENUE COLLECTION FOR THE DATE OF ["+from_date+"] TO ["+to_date+"]";
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
			if(report_for.equals("month_wise")){
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("FOR THE MONTH OF "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			}else if(report_for.equals("date_wise")){
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
				pcell.setBorder(0);
				headLinetable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("FOR THE PERIOD OF ["+from_date+"] TO ["+to_date+"]",ReportUtil.f11B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setBorder(0);
				headLinetable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(""));
				pcell.setBorder(0);
				headLinetable.addCell(pcell);
				
			}
			
			
			
			pcell = new PdfPCell(new Paragraph("STD A/C NO. : "+accountInfo.getAccount_no()+", "+accountInfo.getBank_name()+", "+accountInfo.getBranch_name(),ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(3);
			pcell.setPaddingTop(5f);
			pcell.setPaddingBottom(15f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			document.add(headLinetable);			
			
			PdfPTable debitTable = new PdfPTable(9);
			debitTable.setWidthPercentage(100);
			debitTable.setWidths(new float[]{6,30,12,11,7,7,8,8,11});
			debitTable.setHeaderRows(1);
			
			pcell = new PdfPCell(new Paragraph("(Amount in Taka)",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(9);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dated",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Particular's",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Total Revenue",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Actual Revenue",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Interest",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Misce/HHV/NHV",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Rec./Trans. Amount",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			bankBookListDebit=getDebitList();
			
			double bankBookOpeningBalance=0.0;
			
			if(report_for.equals("month_wise")){
				 bankBookOpeningBalance=getBankBookOpeningBalanceMonthWise();
			}else if(report_for.equals("date_wise")){
				 bankBookOpeningBalance=getBankBookOpeningBalanceDatewise();
			}
			
			
			
			int debitListSize=bankBookListDebit.size();
			
			double totalRevenue=0.0;
			double actualRevenue=0.0;
			double meterRent=0.0;
			double surcharge=0.0;
			double interest=0.0;
			double miscella=0.0;
			double receiveAmt=0.0;
			
			

			for(int i=0;i<debitListSize;i++)
			{
				double actualRevenueDebit=bankBookListDebit.get(i).getTotal_revenue()-(bankBookListDebit.get(i).getMeter_rent()+bankBookListDebit.get(i).getSuecharge()+bankBookListDebit.get(i).getInterest()+bankBookListDebit.get(i).getMiscellaneous()+bankBookListDebit.get(i).getHhv())-bankBookListDebit.get(i).getCredit();
				
				if(i==0){
				pcell = new PdfPCell(new Paragraph("REVENUE COLLECTION (DR)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(9);
				pcell.setMinimumHeight(20f);
				pcell.setBorder(0);
				debitTable.addCell(pcell);
				}
			
				
				pcell = new PdfPCell(new Paragraph(bankBookListDebit.get(i).getTrans_date(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(bankBookListDebit.get(i).getParticular(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getTotal_revenue()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(actualRevenueDebit),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getMeter_rent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getSuecharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getInterest()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getMiscellaneous()+bankBookListDebit.get(i).getHhv()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getCredit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				///////////Total//////////
				totalRevenue=totalRevenue+bankBookListDebit.get(i).getTotal_revenue();
				actualRevenue = actualRevenue+actualRevenueDebit;
				meterRent = meterRent+bankBookListDebit.get(i).getMeter_rent();
				surcharge=surcharge+bankBookListDebit.get(i).getSuecharge();
				interest=interest+bankBookListDebit.get(i).getInterest();
				miscella=miscella+bankBookListDebit.get(i).getMiscellaneous()+bankBookListDebit.get(i).getHhv();
				receiveAmt=receiveAmt+bankBookListDebit.get(i).getCredit();
				
					
			}
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalRevenue),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(actualRevenue),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(meterRent),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(surcharge),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(interest),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(miscella),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(receiveAmt),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pcell.setBorder(0);
			debitTable.addCell(pcell);
			
			document.add(debitTable);
			
			
			PdfPTable creditTable = new PdfPTable(9);
			creditTable.setWidthPercentage(100);
			creditTable.setWidths(new float[]{6,30,12,9,8,8,8,8,11});
			
			bankBookListCredit=getCreditList();
			
			int creditListSize=bankBookListCredit.size();
			
			double totalTransferAmount=0.0;
			
			for(int i=0;i<creditListSize;i++)
			{
				if(i==0){
				pcell = new PdfPCell(new Paragraph("FUND TRANSFER(CR)",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(9);
				pcell.setMinimumHeight(20f);
				pcell.setBorder(0);
				creditTable.addCell(pcell);
				}
			
				
				pcell = new PdfPCell(new Paragraph(bankBookListCredit.get(i).getTrans_date(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(bankBookListCredit.get(i).getParticular(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getTotal_revenue()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getActual_revenue()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getMeter_rent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getSuecharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getInterest()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getMiscellaneous()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getCredit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				totalTransferAmount=totalTransferAmount+bankBookListCredit.get(i).getCredit();
								
				
			}
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalTransferAmount),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(9);
			pcell.setBorder(0);
			creditTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(9);
			pcell.setMinimumHeight(2f);
			creditTable.addCell(pcell);
			
			document.add(creditTable);
			
			PdfPTable footerTable = new PdfPTable(9);
			footerTable.setWidthPercentage(100);
			footerTable.setWidths(new float[]{6,30,12,9,8,8,8,8,11});
			
			
			double totalPaidAmount=totalTransferAmount;
			
			pcell = new PdfPCell(new Paragraph("Total Collection Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalRevenue),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Amount Paid Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(5);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPaidAmount),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			double openingBalance=bankBookOpeningBalance;
			double totalTakaDebit=totalRevenue+openingBalance;
			double closingBalance=totalTakaDebit-totalPaidAmount;
			double totalTakaCredit=totalPaidAmount+closingBalance;
			
			pcell = new PdfPCell(new Paragraph("(+) Opening Balance Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(openingBalance),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Closing Balance Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(5);
			footerTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(closingBalance),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Total Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalTakaDebit),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(5);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalTakaCredit),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(9);
			pcell.setMinimumHeight(2f);
			footerTable.addCell(pcell);
			
			document.add(footerTable);
			
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	
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
	
	

	private ArrayList<BankBookDTO> getDebitList()
	{
	ArrayList<BankBookDTO> bankBookDebitList=new ArrayList<BankBookDTO>();
	
	
	try {
		String wClause="";
		String wClause2="";
		
		
		if(report_for.equals("date_wise"))
		{
			wClause=" And TRANS_DATE BETWEEN to_DATE ('"+from_date+"', 'dd-MM-YYYY') AND to_DATE ('"+to_date+"', 'dd-MM-YYYY')";
			wClause2=" And COLLECTION_DATE BETWEEN to_DATE ('"+from_date+"', 'dd-MM-YYYY') AND to_DATE ('"+to_date+"', 'dd-MM-YYYY')";
		}else if(report_for.equals("month_wise"))
		{
			wClause=" And to_char(TRANS_DATE,'mm')="+collection_month+" and to_char(TRANS_DATE,'YYYY')="+collection_year;
			wClause2=" And to_char(COLLECTION_DATE,'mm')="+collection_month+" and to_char(COLLECTION_DATE,'YYYY')="+collection_year;
		}
		
		
	
		
		
		String transaction_sql="  SELECT TO_CHAR (TRANS_DATE, 'dd-mm-yyyy') TRANS_DATE, " +
				"         PARTICULARS, " +
				"         DEBIT, " +
				"         METER_RENT, " +
				"         SURCHARGE, " +
				"         interest, " +
				"         Miscellaneous, " +
				"         Transfer_Amount, " +
				"         HHV_NHV_BILL " +
				"    FROM (SELECT TRANS_DATE, " +
				"                 PARTICULARS, " +
				"                 DEBIT, " +
				"                 METER_RENT, " +
				"                 SURCHARGE, " +
				"                 interest, " +
				"                 Miscellaneous, " +
				"                 Transfer_Amount, " +
				"                 HHV_NHV_BILL " +
				"            FROM (  SELECT PARTICULARS, " +
				"                           MAX (TRANS_DATE) TRANS_DATE, " +
				"                           SUM (DEBIT) DEBIT, " +
				"                           SUM (METER_RENT) METER_RENT, " +
				"                           SUM (SURCHARGE) SURCHARGE, " +
				"                           SUM (INTEREST) INTEREST, " +
				"                           SUM (MISCELLANEOUS) MISCELLANEOUS, " +
				"                           SUM (TRANSFER_AMOUNT) TRANSFER_AMOUNT, " +
				"                           SUM (HHV_NHV_BILL) HHV_NHV_BILL " +
				"                      FROM (SELECT TRANS_DATE, " +
				"                                   DECODE ( " +
				"                                      cat, " +
				"                                      '01', 'Accounts Receivable Domestic Pvt.', " +
				"                                      '02', 'Accounts Receivable Domestic Govt.', " +
				"                                      '03', 'Accounts Receivable Commercial Pvt.', " +
				"                                      '04', 'Accounts Receivable Commercial Govt.', " +
				"                                      '05', 'Accounts Receivable Industrial Pvt.', " +
				"                                      '06', 'Accounts Receivable Industrial Govt.', " +
				"                                      '07', 'Accounts Receivable Captive Pvt.', " +
				"                                      '08', 'Accounts Receivable Captive Govt.', " +
				"                                      '09', 'Accounts Receivable C.N.G Pvt.', " +
				"                                      '10', 'Accounts Receivable C.N.G Govt.', " +
				"                                      '11', 'Accounts Receivable Power Pvt.', " +
				"                                      '12', 'Accounts Receivable Power Govt.') " +
				"                                      PARTICULARS, " +
				"                                   DEBIT, " +
				"                                   METER_RENT, " +
				"                                   SURCHARGE, " +
				"                                   NULL interest, " +
				"                                   NULL Miscellaneous, " +
				"                                   NULL Transfer_Amount, " +
				"                                   HHV_NHV_BILL " +
				"                              FROM (SELECT TRANS_DATE,cat,DEBIT,METER_RENT,SURCHARGE,CATEGORY,HHV_NHV_BILL FROM ( " +
				"                                SELECT TRANS_DATE,SUBSTR (CUSTOMER_ID, 3, 2) cat,ACCOUNT_NO,SUM (DEBIT) DEBIT,SUM (METER_RENT) METER_RENT,SUM (SURCHARGE) SURCHARGE " +
				"                                                FROM BANK_ACCOUNT_LEDGER " +
				"                                               WHERE     TRANS_TYPE = 1 " +
				"                                                     AND CUSTOMER_ID IS NOT NULL " +
				"                                                     AND ACCOUNT_NO = '"+account_no+"' " +wClause+
				"                                            GROUP BY TRANS_DATE, " +
				"                                                     SUBSTR (CUSTOMER_ID, 3, 2),ACCOUNT_NO) " +
				"                                           tab1, " +
				"                                           (SELECT NVL (SUM (HHV_NHV_BILL), 0) " +
				"                                                        HHV_NHV_BILL, " +
				"                                                     SUBSTR (CUSTOMER_ID, 3, 2) " +
				"                                                        CATEGORY,ACCOUNT_NO,COLLECTION_DATE " +
				"                                                FROM SUMMARY_MARGIN_PB pb, " +
				"                                                     BILL_COLLECTION_METERED bcm " +
				"                                               WHERE     pb.bill_id = BCM.BILL_ID " + wClause2+
				"                                                     AND ACCOUNT_NO='"+account_no+"' " +
				"                                            GROUP BY SUBSTR (CUSTOMER_ID, 3, 2),ACCOUNT_NO,COLLECTION_DATE) " +
				"                                           tab2 " +
				"                                     WHERE tab1.cat = tab2.CATEGORY(+) " +
				"                                     and tab1.ACCOUNT_NO=tab2.ACCOUNT_NO(+) " +
				"                                     and tab1.TRANS_DATE=tab2.COLLECTION_DATE(+))) " +
				"                  GROUP BY PARTICULARS) " +
				"          UNION ALL " +
				"          SELECT TRANS_DATE, " +
				"                 PARTICULARS, " +
				"                 DEBIT, " +
				"                 METER_RENT, " +
				"                 SURCHARGE, " +
				"                 NULL interest, " +
				"                 NULL Miscellaneous, " +
				"                 NULL Transfer_Amount, " +
				"                 NULL HHV_NHV_BILL " +
				"            FROM (  SELECT TRANS_DATE, " +
				"                           PARTICULARS, " +
				"                           SUM (DEBIT) DEBIT, " +
				"                           SUM (METER_RENT) METER_RENT, " +
				"                           SUM (SURCHARGE) SURCHARGE " +
				"                      FROM BANK_ACCOUNT_LEDGER " +
				"                     WHERE     TRANS_TYPE = 4 " +
				"                           AND DEBIT > 0 " +
				"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
				"                  GROUP BY TRANS_DATE, PARTICULARS) " +
				"          UNION ALL " +
				"          SELECT TRANS_DATE, " +
				"                 PARTICULARS, " +
				"                 DEBIT, " +
				"                 METER_RENT, " +
				"                 SURCHARGE, " +
				"                 DEBIT interest, " +
				"                 NULL Miscellaneous, " +
				"                 NULL Transfer_Amount, " +
				"                 NULL HHV_NHV_BILL " +
				"            FROM (  SELECT TRANS_DATE, " +
				"                           PARTICULARS, " +
				"                           SUM (DEBIT) DEBIT, " +
				"                           SUM (METER_RENT) METER_RENT, " +
				"                           SUM (SURCHARGE) SURCHARGE " +
				"                      FROM BANK_ACCOUNT_LEDGER " +
				"                     WHERE     TRANS_TYPE = 5 " +
				"                           AND DEBIT > 0 " +
				"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
				"                  GROUP BY TRANS_DATE, PARTICULARS) " +
				"          UNION ALL " +
				"          SELECT TRANS_DATE, " +
				"                 PARTICULARS, " +
				"                 DEBIT, " +
				"                 METER_RENT, " +
				"                 SURCHARGE, " +
				"                 DEBIT interest, " +
				"                 NULL Miscellaneous, " +
				"                 NULL Transfer_Amount, " +
				"                 NULL HHV_NHV_BILL " +
				"            FROM (  SELECT TRANS_DATE, " +
				"                           PARTICULARS, " +
				"                           SUM (DEBIT) DEBIT, " +
				"                           SUM (METER_RENT) METER_RENT, " +
				"                           SUM (SURCHARGE) SURCHARGE " +
				"                      FROM BANK_ACCOUNT_LEDGER " +
				"                     WHERE     TRANS_TYPE = 3 " +
				"                           AND DEBIT > 0 " +
				"                           AND PARTICULARS NOT LIKE '%Refund%' " +
				"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
				"                  GROUP BY TRANS_DATE, PARTICULARS) " +
				"          UNION ALL " +
				"          SELECT TRANS_DATE, " +
				"                 PARTICULARS, " +
				"                 DEBIT, " +
				"                 METER_RENT, " +
				"                 SURCHARGE, " +
				"                 NULL interest, " +
				"                 DEBIT Miscellaneous, " +
				"                 NULL Transfer_Amount, " +
				"                 NULL HHV_NHV_BILL " +
				"            FROM (  SELECT TRANS_DATE, " +
				"                           PARTICULARS, " +
				"                           SUM (DEBIT) DEBIT, " +
				"                           SUM (METER_RENT) METER_RENT, " +
				"                           SUM (SURCHARGE) SURCHARGE " +
				"                      FROM BANK_ACCOUNT_LEDGER " +
				"                     WHERE     TRANS_TYPE = 3 " +
				"                           AND DEBIT > 0 " +
				"                           AND PARTICULARS LIKE '%Refund%' " +
				"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
				"                  GROUP BY TRANS_DATE, PARTICULARS) " +
				"          UNION ALL " +
				"          SELECT TRANS_DATE, " +
				"                 PARTICULARS, " +
				"                 DEBIT DEBIT, " +
				"                 METER_RENT, " +
				"                 SURCHARGE, " +
				"                 NULL interest, " +
				"                 NULL Miscellaneous, " +
				"                 NULL Transfer_Amount, " +
				"                 NULL HHV_NHV_BILL " +
				"            FROM (  SELECT TRANS_DATE, " +
				"                           PARTICULARS, " +
				"                           SUM (DEBIT) DEBIT, " +
				"                           SUM (METER_RENT) METER_RENT, " +
				"                           SUM (SURCHARGE) SURCHARGE " +
				"                      FROM BANK_ACCOUNT_LEDGER " +
				"                     WHERE     TRANS_TYPE = 7 " +
				"                           AND DEBIT > 0 " +
				"                           AND ACCOUNT_NO = '"+account_no+"' " +wClause+
				"                  GROUP BY TRANS_DATE, PARTICULARS)) " +
				"ORDER BY Transfer_Amount ASC, TRANS_DATE, PARTICULARS " ;

				

		
		PreparedStatement ps1=conn.prepareStatement(transaction_sql);
	
    	
    	ResultSet resultSet=ps1.executeQuery();
    	
    	
    	while(resultSet.next())
    	{
    		BankBookDTO bankDto = new BankBookDTO();

    		bankDto.setTrans_date(resultSet.getString("TRANS_DATE"));
    		bankDto.setParticular(resultSet.getString("PARTICULARS"));
    		bankDto.setTotal_revenue(resultSet.getDouble("DEBIT"));
    		bankDto.setMeter_rent(resultSet.getDouble("METER_RENT"));
    		bankDto.setSuecharge(resultSet.getDouble("SURCHARGE"));
    		bankDto.setInterest(resultSet.getDouble("interest"));
    		bankDto.setMiscellaneous(resultSet.getDouble("Miscellaneous"));
    		bankDto.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
    		bankDto.setCredit(resultSet.getDouble("Transfer_Amount"));
    		

    		
    		bankBookDebitList.add(bankDto);
    		
    	}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return bankBookDebitList;
	
	}
	
	private ArrayList<BankBookDTO> getCreditList()
	{
	ArrayList<BankBookDTO> bankBookCreditList=new ArrayList<BankBookDTO>();
	
	
	try {
		
		
		String wClause="";
		
		
		if(report_for.equals("date_wise"))
		{
			wClause=" And TRANS_DATE BETWEEN TO_DATE ('"+from_date+"', 'dd-MM-YYYY') AND TO_DATE ('"+to_date+"', 'dd-MM-YYYY')";
		}else if(report_for.equals("month_wise"))
		{
			wClause=" And to_char(TRANS_DATE,'mm')="+collection_month+" and to_char(TRANS_DATE,'YYYY')="+collection_year;
		}
	
		
		
		String transaction_sql="select TRANS_DATE,PARTICULARS,DEBIT,ACTUAL_REVENUE,METER_RENT,SURCHARGE,NULL Miscellaneous,CREDIT,null interest from( " +
				"select to_char(TRANS_DATE) TRANS_DATE,PARTICULARS,SUM(DEBIT) DEBIT,SUM(CREDIT)CREDIT,SUM (ACTUAL_REVENUE)ACTUAL_REVENUE,SUM (METER_RENT) METER_RENT,SUM (SURCHARGE) SURCHARGE from( " +
				"SELECT LAST_DAY(to_date(TRANS_DATE,'dd-mm-yyyy')) TRANS_DATE,PARTICULARS,SUM(DEBIT) DEBIT,SUM(CREDIT)CREDIT,SUM (ACTUAL_REVENUE)ACTUAL_REVENUE,SUM (METER_RENT) METER_RENT,SUM (SURCHARGE) SURCHARGE  " +
				"FROM BANK_ACCOUNT_LEDGER  " +
				"WHERE TRANS_TYPE IN (2,4)  " +
				"AND CREDIT > 0  " +
				"AND ACCOUNT_NO = '"+account_no+"' " +wClause+
				" GROUP BY TRANS_DATE, PARTICULARS) " +
				" group by TRANS_DATE, PARTICULARS) " ;
				

		
		PreparedStatement ps1=conn.prepareStatement(transaction_sql);
	
    	
    	ResultSet resultSet=ps1.executeQuery();
    	
    	
    	while(resultSet.next())
    	{
    		BankBookDTO bankDto1 = new BankBookDTO();

    		bankDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
    		bankDto1.setParticular(resultSet.getString("PARTICULARS"));
    		bankDto1.setTotal_revenue(resultSet.getDouble("DEBIT"));
    		bankDto1.setActual_revenue(resultSet.getDouble("ACTUAL_REVENUE"));
    		bankDto1.setMeter_rent(resultSet.getDouble("METER_RENT"));
    		bankDto1.setSuecharge(resultSet.getDouble("SURCHARGE"));
    		bankDto1.setInterest(resultSet.getDouble("interest"));
    		bankDto1.setMiscellaneous(resultSet.getDouble("Miscellaneous"));
    		bankDto1.setCredit(resultSet.getDouble("CREDIT"));
    		

    		
    		bankBookCreditList.add(bankDto1);
    		
    	}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return bankBookCreditList;
	}
	
	
	private Double getBankBookOpeningBalanceMonthWise(){
		
		int closingMonth=Integer.valueOf(collection_month);
		int closingYear=Integer.valueOf(collection_year);
		
		int tempMonth=0;
		int tempYear=0;
		double openingBalance=0.0; 
		
		
		try {
			if(closingMonth==1){
				tempMonth=12;
				tempYear=closingYear-1;
			}else if(closingMonth>1){
				tempMonth=closingMonth-1;
				tempYear=closingYear;
			}
			
			String account_info_sql= "select get_opening_balance ('"+bank_id+"','"+branch_id+"','"+account_no+"','"+tempMonth+"','"+tempYear+"') BALANCE from dual " ;
			
			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		openingBalance=resultSet.getDouble("BALANCE");
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return openingBalance;
		
	}

	
	
	private Double getBankBookOpeningBalanceDatewise()
	 {
	  
	  
	  double openingBalance=0.0; 
	  
	  
	  try {
	  
	   
	   
	   String account_info_sql="SELECT BALANCE " +
	      "  FROM BANK_ACCOUNT_LEDGER " +
	      " WHERE TRANS_ID IN " +
	      "          (SELECT MAX (TRANS_ID) TRANS_ID " +
	      "             FROM BANK_ACCOUNT_LEDGER " +
	      "            WHERE TRANS_DATE IN " +
	      "                         (SELECT MAX(TRANS_DATE) " +
	      "FROM BANK_ACCOUNT_LEDGER " +
	      "WHERE     BANK_ID= '"+bank_id+"'" +
	      "AND branch_id ='"+branch_id+"' " +
	      "AND ACCOUNT_NO = '"+account_no+"'" +
	      "AND TRANS_DATE < TO_DATE ('"+from_date+"', 'dd/mm/yyyy') "+
	      "AND Status = 1) " +
	      "                  AND branch_id ='"+branch_id+"' " +
	      "                  AND ACCOUNT_NO = '"+account_no+"'" +
	      "                  AND Status = 1) " ;




	   
	   PreparedStatement ps1=conn.prepareStatement(account_info_sql);
	   
	         ResultSet resultSet=ps1.executeQuery();
	         
	         
	         while(resultSet.next())
	         {
	           
	          openingBalance=resultSet.getDouble("BALANCE");
	          
	          
	         }
	  } catch (SQLException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  
	  return openingBalance;
	 }
	
	
	
	
	
//	private Double getBankBookOpeningBalance1()
//	{
//		
//		
//		int tempMonth=0;
//		int tempYear=0;
//		double openingBalance=0.0; 
//		String wClause="";
//		
//		
//		try {
//		
//			if(report_for.equals("month_wise")){
//				
//				int closingMonth=Integer.valueOf(collection_month);
//				int closingYear=Integer.valueOf(collection_year);
//				
//				if(closingMonth==1){
//					tempMonth=12;
//					tempYear=closingYear-1;
//				}else if(closingMonth>1){
//					tempMonth=closingMonth;
//					tempYear=closingYear;
//				}
//				String tempDate="01-"+tempMonth+"-"+tempYear;
//				
//				wClause=" AND TRANS_DATE < to_date('"+tempDate+"','dd/mm/yyyy') ";
//			}else if(report_for.equals("date_wise")){
//				wClause="AND TRANS_DATE < TO_DATE ('"+from_date+"', 'dd/mm/yyyy') ";
//			}
//			
//			String account_info_sql="SELECT BALANCE " +
//					 "  FROM BANK_ACCOUNT_LEDGER " +
//					 " WHERE TRANS_ID IN " +
//					 "          (SELECT MAX (TRANS_ID) TRANS_ID " +
//					 "             FROM BANK_ACCOUNT_LEDGER " +
//					 "            WHERE TRANS_DATE IN " +
//					 "                         (SELECT MAX(TRANS_DATE) " +
//					 "FROM BANK_ACCOUNT_LEDGER " +
//					 "WHERE     BANK_ID= '"+bank_id+"'" +
//					 "AND branch_id ='"+branch_id+"' " +
//					 "AND ACCOUNT_NO = '"+account_no+"'" +wClause+
//					 "AND Status = 1) " +
//					 "                  AND branch_id ='"+branch_id+"' " +
//					 "                  AND ACCOUNT_NO = '"+account_no+"'" +
//					 "                  AND Status = 1) " ;
//
//
//
//
//			
//			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
//			
//        	ResultSet resultSet=ps1.executeQuery();
//        	
//        	
//        	while(resultSet.next())
//        	{
//        		 
//        		openingBalance=resultSet.getDouble("BALANCE");
//        		
//        		
//        	}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return openingBalance;
//		
//		
//		
//		
//		
//		
//		
////		int closingMonth=Integer.valueOf(collection_month);
////		int closingYear=Integer.valueOf(collection_year);
////		
////		int tempMonth=0;
////		int tempYear=0;
////		double openingBalance=0.0; 
////		
////		
////		try {
////			if(closingMonth==1){
////				tempMonth=12;
////				tempYear=closingYear-1;
////			}else if(closingMonth>1){
////				tempMonth=closingMonth-1;
////				tempYear=closingYear;
////			}
////		
////			String account_info_sql="select BALANCE from BANK_ACCOUNT_LEDGER WHERE TRANS_ID IN "+
////					"( " +
////					"select MAX(TRANS_ID) TRANS_ID from BANK_ACCOUNT_LEDGER where TRANS_DATE in " +
////					"(select  MAX(TRANS_DATE) FROM BANK_ACCOUNT_LEDGER " +
////					"WHERE  TO_CHAR (TRANS_DATE, 'MM') = "+tempMonth+" "+
////					"AND TO_CHAR (TRANS_DATE, 'YYYY') ="+tempYear+" " +
////					"and branch_id='"+branch_id+"' " +
////					"AND ACCOUNT_NO='"+account_no+"' " +
////					" And Status=1 ) " +
////					" and branch_id='"+branch_id+"' " +
////					"AND ACCOUNT_NO='"+account_no+"'" +
////					" And Status=1  " +
////					") ";
////
////
////
////
////			
////			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
////			
////        	ResultSet resultSet=ps1.executeQuery();
////        	
////        	
////        	while(resultSet.next())
////        	{
////        		 
////        		openingBalance=resultSet.getDouble("BALANCE");
////        		
////        		
////        	}
////		} catch (SQLException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		
////		return openingBalance;
//	}
//	
	

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
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

	public String getCollection_month() {
		return collection_month;
	}

	public void setCollection_month(String collection_month) {
		this.collection_month = collection_month;
	}

	public String getCollection_year() {
		return collection_year;
	}

	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}

	public String getCollection_date() {
		return collection_date;
	}

	public void setCollection_date(String collection_date) {
		this.collection_date = collection_date;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

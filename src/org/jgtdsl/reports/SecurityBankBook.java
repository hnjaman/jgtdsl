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

import com.itextpdf.text.BaseColor;
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

public class SecurityBankBook extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	ArrayList<BankBookDTO> bankBookListDebit=new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO> bankBookListCredit=new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO> bankBookDebit1= new ArrayList<BankBookDTO>();
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
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		BankBookDTO bankBookDTO = null;

	public String execute() throws Exception
	{
				
		String fileName="Security_Bank_Book.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
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
				40,120,40
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
            img.setAbsolutePosition(350f, 550f);		// rotate
            
	        document.add(img);
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ",ReportUtil.f8B);
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
			
			accountInfo = getAccountInfo();
			
			
			PdfPTable headLinetable = null;
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{10,80,10});
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			pcell=new PdfPCell(new Paragraph("BANK BOOK OF SECURITY & OTHERS COLLECTION FOR THE MONTH OF "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("STD A/C NO. : "+accountInfo.getAccount_no()+", "+accountInfo.getBank_name()+", "+accountInfo.getBranch_name(),ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(3);
			pcell.setPaddingTop(5f);
			pcell.setPaddingBottom(15f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			document.add(headLinetable);
			
			
			PdfPTable debitTable = new PdfPTable(13);
			debitTable.setWidthPercentage(100);
			debitTable.setWidths(new float[]{5,18,7,7,7,7,7,7,7,7,7,7,7});
			debitTable.setHeaderRows(1);
			
			pcell = new PdfPCell(new Paragraph("(Amount in Taka)",ReportUtil.f8B));
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(13);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Date",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Security Deposit",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Sale of Store",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Connection Fee",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commissioning Fee",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Distribution Line",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Service Charge",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Penalties & Fine",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Others Income",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Interest Income",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Fund Receive/ Fund Transfer",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			debitTable.addCell(pcell);
			
			bankBookListDebit=getDebitList();
			
			int debitListSize=bankBookListDebit.size();
			
			double totalCollection=0.0;
			double totalSecurityDeposit=0.0;
			double totalSaleOfStore=0.0;
			double totalConnectionFee=0.0;
			double totalCommissionFee=0.0;
			double totalDistributionLine=0.0;
			double totalServiceCharge=0.0;
			double totalPenalties=0.0;
			double totalOthersIncome=0.0;
			double totalInterestIncome=0.0;
			double totalFundReceive=0.0;
			double connectionFee=0.0;
			double othersIncome=0.0;
			
			

			for(int i=0;i<debitListSize;i++)
			{
				
				
				if(i==0){
				pcell = new PdfPCell(new Paragraph("A) SECURITY & OTHERS COLLECTION",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(5);
				pcell.setMinimumHeight(20f);
				pcell.setBorder(0);
				debitTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("DEBIT",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(8);
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
				
				totalCollection=totalCollection+bankBookListDebit.get(i).getTotalCollection();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getTotalCollection()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalSecurityDeposit=totalSecurityDeposit+bankBookListDebit.get(i).getSecurityDeposit();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getSecurityDeposit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalSaleOfStore=totalSaleOfStore+bankBookListDebit.get(i).getSaleOfStore();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getSaleOfStore()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				connectionFee=bankBookListDebit.get(i).getConnectionFee()+bankBookListDebit.get(i).getDisconnectionFee()+
							  bankBookListDebit.get(i).getReconnectionFee()+bankBookListDebit.get(i).getLoadIncrease()+
							  bankBookListDebit.get(i).getLoadDecreaseFee();
				totalConnectionFee=totalConnectionFee+connectionFee;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(connectionFee),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalCommissionFee=totalCommissionFee+bankBookListDebit.get(i).getCommissionFee();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getCommissionFee()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalDistributionLine=totalDistributionLine+bankBookListDebit.get(i).getPipeLineConstruction();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getPipeLineConstruction()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalServiceCharge=totalServiceCharge+bankBookListDebit.get(i).getServiceCharge();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getServiceCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalPenalties= totalPenalties+bankBookListDebit.get(i).getPenalties()+bankBookListDebit.get(i).getAdditionalBill();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getPenalties()+bankBookListDebit.get(i).getAdditionalBill()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				othersIncome=bankBookListDebit.get(i).getOthersIncome()+bankBookListDebit.get(i).getRaizerShiftingFee()+
							 bankBookListDebit.get(i).getNameChange()+bankBookListDebit.get(i).getBurnerShifting()+
							 bankBookListDebit.get(i).getConsultingFee()+bankBookListDebit.get(i).getMeterMaintaince()+
							 bankBookListDebit.get(i).getLegalFee()+bankBookListDebit.get(i).getSalesOfApplication();
				
				totalOthersIncome=totalOthersIncome+othersIncome;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(othersIncome),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalInterestIncome= totalInterestIncome+bankBookListDebit.get(i).getInterestIncome();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getInterestIncome()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				totalFundReceive=totalFundReceive+bankBookListDebit.get(i).getFundReceive();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListDebit.get(i).getFundReceive()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable.addCell(pcell);
				
				
					
			}
			
			document.add(debitTable);
			
			PdfPTable debitTable1 = new PdfPTable(13);
			debitTable1.setWidthPercentage(100);
			debitTable1.setWidths(new float[]{5,18,7,7,7,7,7,7,7,7,7,7,7});
			
			bankBookDebit1=getDebitListNext();
			int listSize= bankBookDebit1.size();
			
			double totalCollection1=0.0;
			double intersetIncome2=0.0;
			double othersIncome2=0.0;
			double foundReceive=0.0;
			
			for (int j = 0; j < listSize; j++) {
				
				pcell = new PdfPCell(new Paragraph(bankBookDebit1.get(j).getTrans_date(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(bankBookDebit1.get(j).getParticular(),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getTotalCollection()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				totalCollection1=totalCollection1+bankBookDebit1.get(j).getTotalCollection();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getSecurityDeposit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getSaleOfStore()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getConnectionFee()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getCommissionFee()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getPipeLineConstruction()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getServiceCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getPenalties()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				othersIncome2=othersIncome2+bankBookDebit1.get(j).getOthersIncome();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getOthersIncome()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				intersetIncome2=intersetIncome2+bankBookDebit1.get(j).getInterest();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getInterest()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				foundReceive=foundReceive+bankBookDebit1.get(j).getFundReceive();
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookDebit1.get(j).getFundReceive()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				debitTable1.addCell(pcell);
				
				
			}
			
			pcell = new PdfPCell(new Paragraph("Sub Total = ",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollection+totalCollection1),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalSecurityDeposit),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalSaleOfStore),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalConnectionFee),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCommissionFee),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalDistributionLine),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalServiceCharge),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPenalties),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			double subtotalOthers=othersIncome2+totalOthersIncome;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalOthers),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			double subTotalInterestIncome=totalInterestIncome+intersetIncome2;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalInterestIncome),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(foundReceive),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			debitTable1.addCell(pcell);
			
			document.add(debitTable1);
			
			
			PdfPTable creditTable = new PdfPTable(13);
			creditTable.setWidthPercentage(100);
			creditTable.setWidths(new float[]{5,18,7,7,7,7,7,7,7,7,7,7,7});
			creditTable.setHeaderRows(1);
			
//			pcell = new PdfPCell(new Paragraph("Date",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			
//			pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			
//			pcell = new PdfPCell(new Paragraph("Security Deposit",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Sale of Store",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Connection Fee",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Commissioning Fee",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			
//			pcell = new PdfPCell(new Paragraph("Distribution Line",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			
//			pcell = new PdfPCell(new Paragraph("Service Charge",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Penalties & Fine",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Others Income",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Interest Income",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
//			
//			pcell = new PdfPCell(new Paragraph("Fund Receive/ Fund Transfer",ReportUtil.f11B));
//			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//			pcell.setColspan(1);
//			creditTable.addCell(pcell);
			
			bankBookListCredit=getCreditList();
			
			int creditListSize=bankBookListCredit.size();
			
			double totalTransferAmount=0.0;
			
			for(int i=0;i<creditListSize;i++)
			{
				if(i==0){
					pcell = new PdfPCell(new Paragraph("Fund Transfer & Others Payments",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(5);
					pcell.setMinimumHeight(20f);
					pcell.setBorder(0);
					creditTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("CREDIT",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(8);
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
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(bankBookListCredit.get(i).getFundTransfer()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				creditTable.addCell(pcell);
				
				totalTransferAmount=totalTransferAmount+bankBookListCredit.get(i).getFundTransfer();
								
				
			}
			
			pcell = new PdfPCell(new Paragraph("Sub Total=",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			pcell.setBorderColorRight(BaseColor.WHITE);
			creditTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalTransferAmount),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(11);
			creditTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(13);
			creditTable.addCell(pcell);
			
			document.add(creditTable);
			
			PdfPTable footerTable = new PdfPTable(13);
			footerTable.setWidthPercentage(100);
			footerTable.setWidths(new float[]{5,18,7,7,7,7,7,7,7,7,7,7,7});
			
			pcell = new PdfPCell(new Paragraph("Total Collection Taka =",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollection+totalCollection1),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Paid Amount Taka =",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(9);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalTransferAmount),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			double openingBalance=getOpeningBalance(); 
			double totalTaka=openingBalance+totalCollection+totalCollection1;
			double closingBalance=totalTaka-totalTransferAmount;
			double totalTakaCredit=closingBalance+totalTransferAmount;
			
			pcell = new PdfPCell(new Paragraph("(+) Opening Balance Taka =",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(openingBalance),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Closing Balance Taka =",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(9);
			footerTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(closingBalance),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Total Taka =",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalTaka),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Taka",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(9);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalTakaCredit),ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(13);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("NB: Connection Fees includes: (Connection+Disconnection Fees+Load Increase Fees+Load Decrease Fees)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(13);
			pcell.setBorder(0);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Other's Income include :(Burner/Raizer/CMS/RMS Shifting+Ownership/Name Change+Consultancy Fees+Meter/RMS Maintainance+Legal Fees+Sales of Application/Bill Book/Connection Card/Others)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(13);
			pcell.setBorder(0);
			footerTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Penalties & Fine Include : Additional Bill + Penalties",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(13);
			pcell.setBorder(0);
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
			
			String area=loggedInUser.getArea_id();
			
		
			
			
			String transaction_sql="select CATEGORY_ID,CATEGORY_NAME, sum(TOTAL_DEPOSIT) TOTAL_DEPOSIT, " +
					"sum(SECURITYDEPOSIT)SECURITYDEPOSIT, " +
					"sum(SALESOFSTORE)SALESOFSTORE, " +
					"sum(CONNECTIONFEE)CONNECTIONFEE, " +
					"sum(COMISSIONINGFEE)COMISSIONINGFEE, " +
					"sum(SERVICECHARGE)SERVICECHARGE, " +
					"sum(PIPELINECONSTRUCTION)PIPELINECONSTRUCTION, " +
					"sum(LOADINCDESCFEE)LOADINCDESCFEE, " +
					"sum(DISCONNECTIONFEE)DISCONNECTIONFEE, " +
					"sum(RECONNECTIONFEE)RECONNECTIONFEE, " +
					"sum(ADDITIONALBILL)ADDITIONALBILL, " +
					"sum(PENALTY)PENALTY, " +
					"sum(NAMECHANGEFEE)NAMECHANGEFEE, " +
					"sum(BURNERSHIFTINGFEE)BURNERSHIFTINGFEE, " +
					"sum(RAIZERSHIFTINGFEE)RAIZERSHIFTINGFEE, " +
					"sum(CONSULTINGFEE)CONSULTINGFEE, " +
					"sum(OTHERFEES)OTHERFEES " +
					"from MST_DEPOSIT md, VIEW_DEPOSIT vd,MST_CUSTOMER_CATEGORY cc " +
					"where MD.DEPOSIT_ID=VD.DEPOSIT_ID " +
					"and substr(CUSTOMER_ID,3,2)=CC.CATEGORY_ID " +
					"and substr(CUSTOMER_ID,1,2)='"+area+"' " +
					"and ACCOUNT_NO='"+account_no+"' " +
					"AND TO_CHAR(DEPOSIT_DATE,'MM')="+collection_month+" " +
					"AND TO_CHAR(DEPOSIT_DATE,'YYYY')="+collection_year+" " +
					"group by CATEGORY_ID,CATEGORY_NAME " +
					"order by CATEGORY_ID " ;


			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
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
        		bankDto.setPipeLineConstruction(resultSet.getDouble("PIPELINECONSTRUCTION"));
        		bankDto.setLoadIncrease(resultSet.getDouble("LOADINCDESCFEE"));
        		bankDto.setDisconnectionFee(resultSet.getDouble("DISCONNECTIONFEE"));
        		bankDto.setReconnectionFee(resultSet.getDouble("RECONNECTIONFEE"));
        		bankDto.setAdditionalBill(resultSet.getDouble("ADDITIONALBILL"));
        		bankDto.setPenalties(resultSet.getDouble("PENALTY"));
        		bankDto.setNameChange(resultSet.getDouble("NAMECHANGEFEE"));
        		bankDto.setBurnerShifting(resultSet.getDouble("BURNERSHIFTINGFEE"));
        		bankDto.setRaizerShiftingFee(resultSet.getDouble("RAIZERSHIFTINGFEE"));
        		bankDto.setConsultingFee(resultSet.getDouble("CONSULTINGFEE"));
        		bankDto.setOthersIncome(resultSet.getDouble("OTHERFEES"));
        		
        		bankBookDebitList.add(bankDto);
        		
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bankBookDebitList;
	}
	
	private ArrayList<BankBookDTO> getDebitListNext()
	{
	ArrayList<BankBookDTO> bankBookDebittListNext=new ArrayList<BankBookDTO>();
		
		try {
			
		
			
			
			String transaction_sql="SELECT TO_CHAR(TRANS_DATE,'dd-MON-yyyy') TRANS_DATE, " +
					"       PARTICULARS,DEBIT totalcollection, " +
					"       NULL AS otherincome, " +
					"       NULL interest, " +
					"       DEBIT fundreceive " +
					"  FROM BANK_ACCOUNT_LEDGER " +
					" WHERE     BANK_ID = '"+bank_id+"' " +
					"       AND BRANCH_ID = '"+branch_id+"' " +
					"       AND ACCOUNT_NO = '"+account_no+"' " +
					"       AND TRANS_TYPE = 4 " +
					"       AND DEBIT <> 0 " +
					"       AND TO_CHAR(TRANS_DATE,'MM')="+collection_month+" " +
					"AND TO_CHAR(TRANS_DATE,'YYYY')="+collection_year+" " +
					"UNION ALL " +
					"SELECT TO_CHAR(TRANS_DATE,'dd-MON-yyyy') TRANS_DATE, " +
					"       PARTICULARS,DEBIT totalcollection, " +
					"       NULL AS otherincome, " +
					"       DEBIT interest, " +
					"       NULL fundreceive " +
					"  FROM BANK_ACCOUNT_LEDGER " +
					" WHERE     BANK_ID = '"+bank_id+"' " +
					"       AND BRANCH_ID = '"+branch_id+"' " +
					"       AND ACCOUNT_NO = '"+account_no+"' " +
					"       AND TRANS_TYPE = 5 " +
					"       AND DEBIT <> 0 " +
					"       AND TO_CHAR(TRANS_DATE,'MM')="+collection_month+" " +
					"AND TO_CHAR(TRANS_DATE,'YYYY')="+collection_year+" " +
					"UNION ALL " +
					"SELECT TO_CHAR(TRANS_DATE,'dd-MON-yyyy') TRANS_DATE, " +
					"       PARTICULARS,DEBIT totalcollection, " +
					"       DEBIT AS otherincome, " +
					"       NULL interest, " +
					"       NULL fundreceive " +
					"  FROM BANK_ACCOUNT_LEDGER " +
					" WHERE     BANK_ID = '"+bank_id+"' " +
					"       AND BRANCH_ID = '"+branch_id+"' " +
					"       AND ACCOUNT_NO = '"+account_no+"' " +
					"       AND TRANS_TYPE = 3 " +
					"       AND DEBIT <> 0 " +
					"AND TO_CHAR(TRANS_DATE,'MM')="+collection_month+" " +
					"AND TO_CHAR(TRANS_DATE,'YYYY')="+collection_year+" " ;








			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BankBookDTO bankDto1 = new BankBookDTO();
   
        		bankDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		bankDto1.setParticular(resultSet.getString("PARTICULARS"));
        		bankDto1.setOthersIncome(resultSet.getDouble("OTHERINCOME"));
        		bankDto1.setInterest(resultSet.getDouble("INTEREST"));
        		bankDto1.setFundReceive(resultSet.getDouble("FUNDRECEIVE"));
        		bankDto1.setTotalCollection(resultSet.getDouble("totalcollection"));
   
        		
        		bankBookDebittListNext.add(bankDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bankBookDebittListNext;
	}
	
	private ArrayList<BankBookDTO> getCreditList()
	{
	ArrayList<BankBookDTO> bankBookCredittList=new ArrayList<BankBookDTO>();
		
		try {
			
			
			
			String area=loggedInUser.getArea_id();
			
			
			String transaction_sql= "SELECT TO_CHAR (TRANS_DATE,'dd-mm-yyyy') TRANS_DATE, " +
									"       PARTICULARS, " +
									"       DEBIT, " +
									"       credit " +
									"  FROM BANK_ACCOUNT_LEDGER " +
									" WHERE     BANK_ID = '"+bank_id+"' " +
									"       AND BRANCH_ID = '"+branch_id+"' " +
									"       AND ACCOUNT_NO = '"+account_no+"' " +
									"       AND TRANS_TYPE = 5 " +
									"       AND CREDIT <> 0 " +
									"       AND TO_CHAR(TRANS_DATE,'MM')="+collection_month+" " +
									"AND TO_CHAR(TRANS_DATE,'YYYY')="+collection_year+" " +
									"UNION ALL " +
									"SELECT TO_CHAR (TRANS_DATE,'dd-mm-yyyy') TRANS_DATE, " +
									"       PARTICULARS, " +
									"       DEBIT, " +
									"       credit " +
									"  FROM BANK_ACCOUNT_LEDGER " + 
									" WHERE     BANK_ID = '"+bank_id+"' " +
									"       AND BRANCH_ID = '"+branch_id+"' " +
									"       AND ACCOUNT_NO = '"+account_no+"' " +
									"       AND TRANS_TYPE = 4 " +
									"       AND CREDIT <> 0 " +
									"       AND TO_CHAR(TRANS_DATE,'MM')="+collection_month+" " +
									"AND TO_CHAR(TRANS_DATE,'YYYY')="+collection_year+" " +
									"UNION ALL " +
									"SELECT TO_CHAR (TRANS_DATE,'dd-mm-yyyy') TRANS_DATE, " +
									"       PARTICULARS, " +
									"       DEBIT, " +
									"       credit " +
									"  FROM BANK_ACCOUNT_LEDGER " +
									" WHERE     BANK_ID = '"+bank_id+"' " +
									"       AND BRANCH_ID = '"+branch_id+"' " +
									"       AND ACCOUNT_NO = '"+account_no+"' " +
									"       AND TRANS_TYPE = 2 " +
									"       AND CREDIT <> 0 " +
									"       AND TO_CHAR(TRANS_DATE,'MM')="+collection_month+" " +
									"AND TO_CHAR(TRANS_DATE,'YYYY')="+collection_year+" " ;







			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BankBookDTO bankDto1 = new BankBookDTO();
   
        		bankDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		bankDto1.setParticular(resultSet.getString("PARTICULARS"));
        		bankDto1.setFundTransfer(resultSet.getDouble("CREDIT"));
        		
   
        		
        		bankBookCredittList.add(bankDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bankBookCredittList;
	}
	
	private double getOpeningBalance(){
		int closingMonth=Integer.valueOf(collection_month)-1;
		int closingYear=Integer.valueOf(collection_year);
		
		int tempMonth=0;
		int tempYear=0;
		double openingBalanceAmt=0.0; 
		
		try {
			if(closingMonth==0){
				tempMonth=12;
				tempYear=closingYear-1;
			}else if(closingMonth>0){
				tempMonth=closingMonth;
				tempYear=closingYear;
			}
			//String tempDate="01-"+tempMonth+"-"+tempYear;
		
			String account_info_sql= "select get_opening_balance ('"+bank_id+"','"+branch_id+"','"+account_no+"','"+tempMonth+"','"+tempYear+"') BALANCE from dual " ;
					
					
					
					
					
//					
//					"SELECT BALANCE " +
//									 "  FROM BANK_ACCOUNT_LEDGER " +
//									 " WHERE TRANS_ID IN " +
//									 "          (SELECT MAX (TRANS_ID) TRANS_ID " +
//									 "             FROM BANK_ACCOUNT_LEDGER " +
//									 "            WHERE TRANS_DATE IN " +
//									 "                         (SELECT MAX(TRANS_DATE) " +
//									 "FROM BANK_ACCOUNT_LEDGER " +
//									 "WHERE     BANK_ID= '"+bank_id+"'" +
//									 "AND branch_id ='"+branch_id+"' " +
//									 "AND ACCOUNT_NO = '"+account_no+"'" +
//									 "AND TRANS_DATE <=to_date('"+tempDate+"','dd/mm/yyyy') "+
//									 "AND Status = 1) " +
//									 "                  AND branch_id ='"+branch_id+"' " +
//									 "                  AND ACCOUNT_NO = '"+account_no+"'" +
//									 "                  AND Status = 1) " ;





			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		openingBalanceAmt=resultSet.getDouble("BALANCE");
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return openingBalanceAmt;
	}	

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
	
	
	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}
	



}

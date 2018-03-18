package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AccountDTO;
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.dto.BranchDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.GasPurchaseDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.TariffDTO;
import org.jgtdsl.dto.TransactionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ValidationAware;




public class CollectionBankStatement extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
	AccountDTO accountInfo=new AccountDTO();
	ArrayList<TransactionDTO> allBnakBranchNameID=new ArrayList<TransactionDTO>();
	ArrayList<TransactionDTO> allCategoryNameID = new ArrayList<TransactionDTO>();
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
		
		
	public String execute() throws Exception
	{
				
		String fileName="Collection_Statement.pdf";
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
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ",ReportUtil.f9B);
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
			
			
			if(report_for.equals("security")){
				generatePdf_for_SecurityCollection(document);
			}else if(report_for.equals("date_wise")){
				generatePdfDate_wise(document);
			}else if(report_for.equals("month_wise")){
				//generatePdfMonth_wise(document);				// per day total collection month wise for a specific bank
				generatePdf_PerDay_AllBank_MonthWise(document); // per day total collection month wise for ###All Bank###
			}else if(report_for.equals("month_wiseDetails")){
				generatePdfMonth_wiseDetails(document);
			}else if(report_for.equals("bank_wise")){
				generatePdfBank_wise(document);
			}else if(report_for.equals("all_bank_wise_monthly")){
				generatePdfAll_Bank_wiseMonthly(document);
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
	
	/* Security collection month wise
	 * */

	private void generatePdf_for_SecurityCollection(Document document) throws DocumentException
	{

		allCategoryNameID = getAllCategoryNameID();
		int allcategory = allCategoryNameID.size();
		
		allBnakBranchNameID=getAllBnakBranchNameID();
		//double forwardBalance=getForwardBalanceMonthwise();
		int allbranch=allBnakBranchNameID.size();
		
		document.setMargins(20,20,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(1);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{100});
		
		pcell = new PdfPCell(new Paragraph("Monthly Security Collection",ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);		
				
		pcell=new PdfPCell(new Paragraph("COLLECTION MONTH : "+Month.values()[Integer.valueOf(collection_month)-1]+"-"+collection_year,ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		pcell.setPaddingBottom(5);
		headLinetable.addCell(pcell);
		
//		pcell = new PdfPCell(new Paragraph("gfhfghgf"));
//		pcell.setBorder(0);
//		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		double grandtotalSecurity=0.0;
		
		for(int category=0;category<allcategory;category++){
			
			String category_id = allCategoryNameID.get(category).getCategory_id();
			int count=0;
			
			for(int i=0;i<allbranch;i++){	
				
				String branch_id=allBnakBranchNameID.get(i).getBranch_id();
				String category_name = allCategoryNameID.get(category).getCategory_name();
				String bank_name=allBnakBranchNameID.get(i).getBank_name();
				String branch_name=allBnakBranchNameID.get(i).getBranch_name();
				
				transactionList=getAllBankWiseSecurityCollection(branch_id,category_id);
				
				int listSize=transactionList.size();
				if(listSize==0){
					continue;
				}
				
				if(count==0){
					PdfPTable categoryname = null;
					//PdfPCell pcell=null;
					categoryname = new PdfPTable(1);
					categoryname.setWidthPercentage(100);
					categoryname.setWidths(new float[]{100});
					
					pcell = new PdfPCell(new Paragraph(""+category_name,ReportUtil.f9B));
					pcell.setMinimumHeight(18f);
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					pcell.setBorder(0);
					categoryname.addCell(pcell);
					
					document.add(categoryname);
					document.add(new Paragraph("\n"));
					count=1;
				}
				
				PdfPTable bankbranch = null;
				//PdfPCell pcell=null;
				bankbranch = new PdfPTable(1);
				bankbranch.setWidthPercentage(100);
				bankbranch.setWidths(new float[]{100});
				
				pcell = new PdfPCell(new Paragraph(""+bank_name+",    "+branch_name+"    "+branch_id,ReportUtil.f8B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setBorder(0);
				bankbranch.addCell(pcell);
				
				document.add(bankbranch);
				
				//document.add(new Paragraph("\n"));
				
				PdfPTable pdfPTable = new PdfPTable(5);
				pdfPTable.setWidthPercentage(100);
				pdfPTable.setWidths(new float[]{10,20,35,20,15});
				pdfPTable.setHeaderRows(1);
				
				pcell = new PdfPCell(new Paragraph("SL No",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Security",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Comment",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				//document.add(pdfPTable);
				
				
			
				
				double totalSecurity=0.0;
//				double totalSurcharge=0.0;
//				double totalFees=0.0;
//				double totalSecurityDeposit=0.0;
//				double total=0.0;

					for(int j=0;j<listSize;j++)
					{
						pcell = new PdfPCell(new Paragraph(String.valueOf(j+1),ReportUtil.f9));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setColspan(1);
						pdfPTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(transactionList.get(j).getCustomer_id(),ReportUtil.f9));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(1);
						pdfPTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(transactionList.get(j).getFull_name(),ReportUtil.f9));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(1);
						pdfPTable.addCell(pcell);
								
						pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(j).getDebit()),ReportUtil.f9));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setColspan(1);
						pdfPTable.addCell(pcell);
						
						pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f9));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setColspan(1);
						pdfPTable.addCell(pcell);
								
						grandtotalSecurity+=transactionList.get(j).getDebit();
						totalSecurity+=transactionList.get(j).getDebit();
						
//						 totalSurcharge+=transactionList.get(j).getSurcharge();
//						 totalFees+=transactionList.get(j).getFees();
//						 totalSecurityDeposit+=transactionList.get(j).getSecurity();
//						 total+=transactionList.get(j).getGas_bill()+transactionList.get(j).getSurcharge()+transactionList.get(j).getFees()+transactionList.get(j).getSecurity();
				
					}
				
				pcell = new PdfPCell(new Paragraph("Total = ",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(3);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(totalSecurity),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
					
				pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				document.add(pdfPTable);
				
				document.add(new Paragraph("\n"));
				
//				if(i<allbranch-1){
//					document.newPage();
//				}
								
			}
			
		}
		
		PdfPTable grandTotal = new PdfPTable(5);
		grandTotal.setWidthPercentage(100);
		grandTotal.setWidths(new float[]{10,20,35,20,15});
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		grandTotal.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandtotalSecurity),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		grandTotal.addCell(pcell);
			
		pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		grandTotal.addCell(pcell);
		
		document.add(grandTotal);
		
	}
	
	/* Security Collection month wise end
	 * */
	
	private void generatePdfDate_wise(Document document) throws DocumentException
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
		
		pcell=new PdfPCell(new Paragraph("DATE WISE BANK STATEMENT [ACCOUNT NO. {"+account_no+"} COLLECTION DATED FOR{"+collection_date+"}]",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		double forwardBalanceDate=getForwardBalanceDatewise();
		
		accountInfo=getAccountInfo();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBank_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Phone",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getPhone(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account No :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAccount_no(),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fax",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getFax(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("A/C Opening Date :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAc_opening_date(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Email",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getEmail(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		
		
		
		
		pcell = new PdfPCell(new Paragraph("Opening Balance :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalanceDate),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getAccount_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
		
		PdfPTable pdfPTable = new PdfPTable(6);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{10,15,35,15,15,15});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(2);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Deposit (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Withdrawal (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		//document.add(pdfPTable);
		
		///////Balance Forward///////////////////

		
		pcell = new PdfPCell(new Paragraph("Balance Forward :",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(5);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalanceDate),ReportUtil.f8));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pcell.setBorderColorLeft(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		//document.add(balanceForward);
		
		/////////Date/////////
		
		
		pcell = new PdfPCell(new Paragraph("Date : ",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(collection_date,ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setColspan(5);
		pdfPTable.addCell(pcell);
		
		transactionList=getTransactionListDateWise();
		int listSize=transactionList.size();
		double balance=forwardBalanceDate;
		double subTotalDebit=0.0;
		double subTotalCredit=0.0;
		double grandTotalDebit=0.0;
		double grandTotalCredit=0.0;

		for(int i=0;i<listSize;i++)
		{
		
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getCustomer_id(),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
		
			subTotalDebit=subTotalDebit+transactionList.get(i).getDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			subTotalCredit=subTotalCredit+transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			balance=balance+transactionList.get(i).getDebit()-transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Grand Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
		
	}
	
	
	/////////////////////Month wise//////////////////////////
	
	private void generatePdfMonth_wise(Document document) throws DocumentException
	{
		
		document.setMargins(20,20,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);		
				
		pcell=new PdfPCell(new Paragraph("COLLECTION STATEMENT ACCOUNT NO. {"+account_no+"} FOR THE MONTH "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		accountInfo=getAccountInfo();
		double forwardBalance=getForwardBalanceMonthwise();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBank_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Phone",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getPhone(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account No :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAccount_no(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fax",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getFax(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("A/C Opening Date :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAc_opening_date(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Email",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getEmail(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Opening Balance :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getAccount_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
		
		PdfPTable pdfPTable = new PdfPTable(5);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{15,40,15,15,15});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Date",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Deposit (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Withdrawal (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		//document.add(pdfPTable);
		
		///////Balance Forward///////////////////
		
		
		pcell = new PdfPCell(new Paragraph("Balance Forward :",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pcell.setBorderColorLeft(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		//document.add(balanceForward);
		
		transactionList=getCollectlistByMonth();
		int listSize=transactionList.size();
		double balance=forwardBalance;
		double subTotalDebit=0.0;
		double subTotalCredit=0.0;

		for(int i=0;i<listSize;i++)
		{
		
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getTrans_date().toString(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);			
		
			subTotalDebit=subTotalDebit+transactionList.get(i).getDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			subTotalCredit=subTotalCredit+transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			balance=balance+transactionList.get(i).getDebit()-transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
		
	}
	
	
	/* All bank per day total collection Monthwise
	 * 
	 * sujon 17 jan 18
	 * */
	
	private void generatePdf_PerDay_AllBank_MonthWise(Document document) throws DocumentException
	{

		double grandTotalGasBill=0.0;
		double grandTotalSurcharge=0.0;
		double grandTotalFees=0.0;
		double grandTotalSecurityDeposit=0.0;
		double grandTotal=0.0;
		
		allBnakBranchNameID=getAllBnakBranchNameID();
		//double forwardBalance=getForwardBalanceMonthwise();
		int allbranch=allBnakBranchNameID.size();
		
		//PdfPTable pdfPTable = null;
		//PdfPCell pcell=null;
		PdfPTable grand = null;
				
		PdfPCell pgrand=null;
		
		for(int i=0;i<allbranch;i++){
			
			String branch_id=allBnakBranchNameID.get(i).getBranch_id();
			
			transactionList=getAllBankWiseTotalsTransaction(branch_id);
			int listSize=transactionList.size();
			if(listSize==0){
				continue;
			}
			
			document.setMargins(20,20,48,72);
			PdfPTable headLinetable = null;
			PdfPCell pcell=null;
			headLinetable = new PdfPTable(1);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{100});
			
			pcell = new PdfPCell(new Paragraph("Monthly Bank Collection",ReportUtil.f8B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);		
					
			pcell=new PdfPCell(new Paragraph("COLLECTION MONTH : "+Month.values()[Integer.valueOf(collection_month)-1]+"-"+collection_year,ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			pcell.setPaddingBottom(5);
			headLinetable.addCell(pcell);
			
//			pcell = new PdfPCell(new Paragraph("gfhfghgf"));
//			pcell.setBorder(0);
//			headLinetable.addCell(pcell);
			
			document.add(headLinetable);
			
			
			String bank_name=allBnakBranchNameID.get(i).getBank_name();
			String branch_name=allBnakBranchNameID.get(i).getBranch_name();
			
			PdfPTable bankbranch = null;
			//PdfPCell pcell=null;
			bankbranch = new PdfPTable(1);
			bankbranch.setWidthPercentage(100);
			bankbranch.setWidths(new float[]{100});
			
			pcell = new PdfPCell(new Paragraph(""+bank_name+", "+branch_name+" "+branch_id,ReportUtil.f8B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			bankbranch.addCell(pcell);
			
			document.add(bankbranch);
			
			PdfPTable pdfPTable = new PdfPTable(8);
			//pdfPTable = new PdfPTable(8);
			pdfPTable.setWidthPercentage(100);
			pdfPTable.setWidths(new float[]{5,19,15,12,12,10,15,12});
			pdfPTable.setHeaderRows(1);
			
			pcell = new PdfPCell(new Paragraph("SL",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Payment Date",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Monthly Gas Bill",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Fess",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Security",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Amount",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Comment",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			//document.add(pdfPTable);
			
			
		
			
			double totalGasBill=0.0;
			double totalSurcharge=0.0;
			double totalFees=0.0;
			double totalSecurityDeposit=0.0;
			double total=0.0;
			
			

				for(int j=0;j<listSize;j++)
				{
					pcell = new PdfPCell(new Paragraph(String.valueOf(j+1),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(transactionList.get(j).getTrans_date(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
							
					
							
					pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(j).getGas_bill()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(j).getSurcharge()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(j).getFees()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(j).getSecurity()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(j).getGas_bill()+transactionList.get(j).getSurcharge()+transactionList.get(j).getFees()+transactionList.get(j).getSecurity()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
							
					
					 totalGasBill+=transactionList.get(j).getGas_bill();
					 totalSurcharge+=transactionList.get(j).getSurcharge();
					 totalFees+=transactionList.get(j).getFees();
					 totalSecurityDeposit+=transactionList.get(j).getSecurity();
					 total+=transactionList.get(j).getGas_bill()+transactionList.get(j).getSurcharge()+transactionList.get(j).getFees()+transactionList.get(j).getSecurity();
			
					
				}
				
				
				grandTotalGasBill+=totalGasBill;
				grandTotalSurcharge+=totalSurcharge;
				grandTotalFees+=totalFees;
				grandTotalSecurityDeposit+=totalSecurityDeposit;
				grandTotal+=total;
			
			pcell = new PdfPCell(new Paragraph("Total = ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalGasBill),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalSurcharge),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalFees),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalSecurityDeposit),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(total),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			document.add(pdfPTable);
			
			if(i<allbranch-2){
				document.newPage();
			}
		}
		
		grand = new PdfPTable(8);
		grand.setWidthPercentage(100);
		grand.setWidths(new float[]{5,19,15,12,12,10,15,12});
		grand.setHeaderRows(1);
		
		pgrand = new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_CENTER);
		pgrand.setColspan(8);
		pgrand.setBorder(1);
		grand.addCell(pgrand);
		
		pgrand = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_CENTER);
		pgrand.setColspan(2);
		grand.addCell(pgrand);
		
		pgrand = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill),ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pgrand.setColspan(1);
		grand.addCell(pgrand);
		
		pgrand = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge),ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pgrand.setColspan(1);
		grand.addCell(pgrand);
		
		pgrand = new PdfPCell(new Paragraph(taka_format.format(grandTotalFees),ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pgrand.setColspan(1);
		grand.addCell(pgrand);
		
		pgrand = new PdfPCell(new Paragraph(taka_format.format(grandTotalSecurityDeposit),ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pgrand.setColspan(1);
		grand.addCell(pgrand);
		
		pgrand = new PdfPCell(new Paragraph(taka_format.format(grandTotal),ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pgrand.setColspan(1);
		grand.addCell(pgrand);
		
		pgrand = new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
		pgrand.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pgrand.setColspan(1);
		grand.addCell(pgrand);

		document.add(grand);
	
	}
	
	/* End All bank per day total collection Monthwise
	 * 
	 * sujon
	 * */
	
	//////////////////////////Details Collection///////////////////////////////
	
	private void generatePdfMonth_wiseDetails(Document document) throws DocumentException
	{
		
		document.setMargins(20,20,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);		
				
		pcell=new PdfPCell(new Paragraph("COLLECTION STATEMENT ACCOUNT NO. {"+account_no+"} FOR THE MONTH "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		accountInfo=getAccountInfo();
		double forwardBalance=getForwardBalanceMonthwise();
		
		PdfPTable middleTable = new PdfPTable(4);
		middleTable.setWidthPercentage(100);
		middleTable.setWidths(new float[]{14,50,18,18});
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBank_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Address",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getAddress(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Phone",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getPhone(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account No :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getAccount_no(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fax",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getFax(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("A/C Opening Date :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(accountInfo.getOpening_date(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Email",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getBranch().getEmail(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Opening Balance :",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Account Name",ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(": "+accountInfo.getAccount_name(),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingBottom(10f);
		pcell.setBorder(0);
		middleTable.addCell(pcell);
		
		document.add(middleTable);
		
		PdfPTable pdfPTable = new PdfPTable(7);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{5,10,30,10,15,15,15});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Particular",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(2);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Month of Bill",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Deposit (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Withdrawal (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance (Tk.)",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		//document.add(pdfPTable);
		
		///////Balance Forward///////////////////
		
		
		pcell = new PdfPCell(new Paragraph("Balance Forward :",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(6);
		pcell.setBorderColorRight(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(forwardBalance),ReportUtil.f9));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pcell.setBorderColorLeft(BaseColor.WHITE);
		pdfPTable.addCell(pcell);
		
		//document.add(balanceForward);
		
		transactionList=getMonthlyDetailsTransactionList();
		int listSize=transactionList.size();
		double balance=forwardBalance;
		double subTotalDebit=0.0;
		double subTotalCredit=0.0;
		double daywiseSubTotalDebit=0.0;
		double daywiseSubTotalCredit=0.0;
		double daywiseSubTotalBalance=0.0;
		String previousDate=new String("");

		for(int i=0;i<listSize;i++)
		{
			String currentDate=transactionList.get(i).getTrans_date();
			
			if(!currentDate.equals(previousDate)){
				
				if(i>0){
					pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(4);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalDebit),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalCredit),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalBalance),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pdfPTable.addCell(pcell);
					
					daywiseSubTotalDebit=0.0;
					daywiseSubTotalCredit=0.0;
					daywiseSubTotalBalance=0.0;
				}
				
				pcell = new PdfPCell(new Paragraph(transactionList.get(i).getTrans_date(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				pdfPTable.addCell(pcell);
				
				
			}
		
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getCustomer_id(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBillMonth(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
		
			subTotalDebit=subTotalDebit+transactionList.get(i).getDebit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getDebit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			subTotalCredit=subTotalCredit+transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getCredit()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			balance=balance+transactionList.get(i).getDebit()-transactionList.get(i).getCredit();
			pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			if(currentDate.equals(previousDate)){
				daywiseSubTotalDebit=daywiseSubTotalDebit+transactionList.get(i).getDebit();
				daywiseSubTotalCredit=daywiseSubTotalCredit+transactionList.get(i).getCredit();
				daywiseSubTotalBalance=balance;
			}else if(!currentDate.equals(previousDate)){
				daywiseSubTotalDebit=daywiseSubTotalDebit+transactionList.get(i).getDebit();
				daywiseSubTotalCredit=daywiseSubTotalCredit+transactionList.get(i).getCredit();
				//
				daywiseSubTotalBalance=balance;
			}
			
			
			
			previousDate=transactionList.get(i).getTrans_date();
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph("Sub Total",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(4);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalDebit),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalCredit),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(daywiseSubTotalBalance),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pdfPTable.addCell(pcell);
			}
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalDebit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCredit),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
		
	}
	
	////////////////////////End of Details Collection/////////////////////////////
	
	
	
	/*	Bank Wise Colection Report
	 * 
	 *  Sujon
	 * */
	
	private void generatePdfBank_wise(Document document) throws DocumentException
	{
		
		document.setMargins(20,20,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);		
				
		pcell=new PdfPCell(new Paragraph("COLLECTION STATEMENT FOR THE MONTH "+Month.values()[Integer.valueOf(collection_month)-1]+", "+collection_year,ReportUtil.f8B));
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
		pdfPTable.setWidths(new float[]{5,19,20,12,12,10,10,12});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("SL",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fess",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Security",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		//document.add(pdfPTable);
		
		
		transactionList=getBankWiseDetailsTransaction();
		int listSize=transactionList.size();
		
		double totalGasBill=0.0;
		double totalSurcharge=0.0;
		double totalFees=0.0;
		double totalSecurityDeposit=0.0;
		double total=0.0;

		for(int i=0;i<listSize;i++)
		{
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBank_name(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBranch_name(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getGas_bill()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getFees()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getSecurity()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getGas_bill()+transactionList.get(i).getSurcharge()+transactionList.get(i).getFees()+transactionList.get(i).getSecurity()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			
			 totalGasBill+=transactionList.get(i).getGas_bill();
			 totalSurcharge+=transactionList.get(i).getSurcharge();
			 totalFees+=transactionList.get(i).getFees();
			 totalSecurityDeposit+=transactionList.get(i).getSecurity();
			 total+=transactionList.get(i).getGas_bill()+transactionList.get(i).getSurcharge()+transactionList.get(i).getFees()+transactionList.get(i).getSecurity();
					
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalGasBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalFees),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSecurityDeposit),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(total),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
	}
	
	
	
	/* End Bank Wise Collection Report
	 * Sujon
	 * */
	
	
	
	
	/* Start All Bank monthly collection
	 * 
	 * sujon
	 * */
	
	private void generatePdfAll_Bank_wiseMonthly(Document document) throws DocumentException
	{
		
		document.setMargins(20,20,48,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(1);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{100});
		
		pcell = new PdfPCell(new Paragraph("Monthly Bank Collection",ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);		
				
		pcell=new PdfPCell(new Paragraph("COLLECTION MONTH : "+Month.values()[Integer.valueOf(collection_month)-1]+"-"+collection_year,ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		pcell.setPaddingBottom(5);
		headLinetable.addCell(pcell);
		
//		pcell = new PdfPCell(new Paragraph("gfhfghgf"));
//		pcell.setBorder(0);
//		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		PdfPTable pdfPTable = new PdfPTable(8);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{5,19,20,12,12,10,10,12});
		pdfPTable.setHeaderRows(1);
		
		pcell = new PdfPCell(new Paragraph("SL",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank Name",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Branch Name",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Fess",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Security",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f11B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		//document.add(pdfPTable);
		
		
		transactionList=getAllBankWiseTotalsTransaction();
		int listSize=transactionList.size();
		
		
		double totalGasBill=0.0;
		double totalSurcharge=0.0;
		double totalFees=0.0;
		double totalSecurityDeposit=0.0;
		double total=0.0;

		for(int i=0;i<listSize;i++)
		{
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBank_name(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(transactionList.get(i).getBranch_name(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
					
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getGas_bill()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getSurcharge()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getFees()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getSecurity()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(transactionList.get(i).getGas_bill()+transactionList.get(i).getSurcharge()+transactionList.get(i).getFees()+transactionList.get(i).getSecurity()),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pdfPTable.addCell(pcell);
					
			
			 totalGasBill+=transactionList.get(i).getGas_bill();
			 totalSurcharge+=transactionList.get(i).getSurcharge();
			 totalFees+=transactionList.get(i).getFees();
			 totalSecurityDeposit+=transactionList.get(i).getSecurity();
			 total+=transactionList.get(i).getGas_bill()+transactionList.get(i).getSurcharge()+transactionList.get(i).getFees()+transactionList.get(i).getSecurity();
	
					
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalGasBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalFees),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(totalSecurityDeposit),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(total),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(1);
		pdfPTable.addCell(pcell);
		
		document.add(pdfPTable);
	}
	
	
	/* End All Bank monthly collection
	 * 
	 * sujon
	 * 6 jan 18
	 * */
	
	
	private Double getForwardBalanceMonthwise()
	{
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
	
	private Double getForwardBalanceDatewise()
	{
		double openingBalanceDatewise=0.0; 
		

		
		try {
			

			String account_info_sql="SELECT BALANCE "+
					"  FROM BANK_ACCOUNT_LEDGER "+
					" WHERE TRANS_ID IN "+
					"          (SELECT MAX (TRANS_ID) TRANS_ID "+
					"             FROM BANK_ACCOUNT_LEDGER "+
					"            WHERE     TRANS_DATE IN "+
					"                         (SELECT MAX (TRANS_DATE) "+
					"                            FROM BANK_ACCOUNT_LEDGER "+
					"                           WHERE TRANS_DATE <to_date('"+collection_date+"','dd/mm/yyyy') "+
					"                                 AND branch_id = '"+branch_id+"' "+
					"                                 AND ACCOUNT_NO = '"+account_no+"' "+
					"                                 AND Status = 1) "+
					"                  AND branch_id = '"+branch_id+"' "+
					"                  AND ACCOUNT_NO = '"+account_no+"' "+
					"                  AND Status = 1) ";




			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		openingBalanceDatewise=resultSet.getDouble("BALANCE");
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return openingBalanceDatewise;
	}
	
	
	private AccountDTO getAccountInfo()
	 {
	           AccountDTO accountInfo=new AccountDTO();
	           BankDTO bankInfo=new BankDTO();
	           BranchDTO branchInfo=new BranchDTO();
	  
	  try {
	  
	   
	   String account_info_sql="SELECT MCI.ACCOUNT_NO,mci.ACCOUNT_NAME,to_char(mci.AC_OPENING_DATE) AC_OPENING_DATE,mbi.BANK_NAME,BRANCH_NAME,MBRI.ADDRESS,MBRI.PHONE,MBRI.FAX,MBRI.EMAIL " +
	     "  FROM MST_ACCOUNT_INFO mci, MST_BANK_INFO mbi, MST_BRANCH_INFO mbri " +
	     " WHERE     mci.bank_id = mbi.bank_id " +
	     "       AND mci.branch_id = mbri.branch_id " +
	     "		and mbi.area_id=MBRI.AREA_ID" +
	     "       AND MCI.BANK_ID =? " +
	     "       AND MCI.branch_id =? " +
	     "       AND MCI.account_no =? " ;

	     
	   
	   PreparedStatement ps1=conn.prepareStatement(account_info_sql);
	   ps1.setString(1, bank_id);
	   ps1.setString(2, branch_id);
	   ps1.setString(3, account_no);
	  
	         
	         ResultSet resultSet=ps1.executeQuery();
	         
	         
	         while(resultSet.next())
	         {
	          
	          
	          accountInfo.setBank_name(resultSet.getString("BANK_NAME"));
	          branchInfo.setAddress(resultSet.getString("ADDRESS"));
	          branchInfo.setPhone(resultSet.getString("PHONE"));
	          branchInfo.setFax(resultSet.getString("FAX"));
	          branchInfo.setEmail(resultSet.getString("EMAIL"));
	          accountInfo.setBranch_name(resultSet.getString("BRANCH_NAME"));
	          accountInfo.setBranch(branchInfo);
	          accountInfo.setAccount_name(resultSet.getString("ACCOUNT_NAME"));
	          accountInfo.setAccount_no(resultSet.getString("ACCOUNT_NO")) ; 
	          accountInfo.setAc_opening_date(resultSet.getString("AC_OPENING_DATE"));
	   
	          
	          
	          
	         }
	  } catch (SQLException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  
	  return accountInfo;
	 }
	
	
	/* Get all category name id
	 * */
	
	private ArrayList<TransactionDTO> getAllCategoryNameID()
	 {
		ArrayList<TransactionDTO> allCategoryNameID=new ArrayList<TransactionDTO>();
		String area=loggedInUser.getArea_id();
		
	  
	  try {
		  	
		  	String account_info_sql="select CATEGORY_ID,CATEGORY_NAME from MST_CUSTOMER_CATEGORY order by CATEGORY_NAME";

	     
	   
					   PreparedStatement ps1=conn.prepareStatement(account_info_sql);
//						   ps1.setString(1, area);
					   
	         
	         ResultSet resultSet=ps1.executeQuery();
	         
	         
	         while(resultSet.next())
	         {
	        	
	        	  TransactionDTO transactionDTO = new TransactionDTO();
	        	 
	        	  transactionDTO.setCategory_id(resultSet.getString("CATEGORY_ID"));
	        	  transactionDTO.setCategory_name(resultSet.getString("CATEGORY_NAME"));
	        	
	        	  
	        	  allCategoryNameID.add(transactionDTO);
	         }
	         
	  } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	  }
	  
	  return allCategoryNameID;
	 }
	
	
	/* get all category name id end
	 * */
	
	
	/* Get all bank name, branch name, branch id
	 * 
	 * sujon
	 * */
	
	private ArrayList<TransactionDTO> getAllBnakBranchNameID()
	 {
		ArrayList<TransactionDTO> allBankBranchNameID=new ArrayList<TransactionDTO>();
		String area=loggedInUser.getArea_id();
		
	  
	  try {
		  	
		  	String account_info_sql="SELECT MBI.BANK_NAME, MBRI.BRANCH_NAME, MBRI.BRANCH_ID " +
								   "  FROM MST_BRANCH_INFO MBRI, mst_bank_info mbi " +
								   " WHERE MBRI.BANK_ID = MBI.BANK_ID " +
								   "AND MBRI.area_id = MBI.area_id " +
								   "AND mbri.AREA_ID = ? " +
								   "order by MBI.BANK_NAME, MBRI.BRANCH_NAME";

	     
	   
						   PreparedStatement ps1=conn.prepareStatement(account_info_sql);
						   ps1.setString(1, area);
					   
	         
	         ResultSet resultSet=ps1.executeQuery();
	         
	         
	         while(resultSet.next())
	         {
	        	
	        	  TransactionDTO transactionDTO = new TransactionDTO();
	        	 
	        	  transactionDTO.setBank_name(resultSet.getString("BANK_NAME"));
	        	  transactionDTO.setBranch_name(resultSet.getString("BRANCH_NAME"));
	        	  transactionDTO.setBranch_id(resultSet.getString("BRANCH_ID"));
	        	  
	        	  allBankBranchNameID.add(transactionDTO);
	         }
	         
	  } catch (SQLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	  }
	  
	  return allBankBranchNameID;
	 }
	
	/* End get all bank name, branch name, branch id
	 * */
	
	
/*	private AccountDTO getAccountInfo()
	{
           AccountDTO accountInfo=new AccountDTO();
           BankDTO bankInfo=new BankDTO();
           BranchDTO branchInfo=new BranchDTO();
		
		try {
		
			
			String account_info_sql="SELECT MCI.ACCOUNT_NO,mci.ACCOUNT_NAME,mci.AC_OPENING_DATE,mbi.BANK_NAME,BRANCH_NAME,MBRI.ADDRESS,MBRI.PHONE,MBRI.FAX,MBRI.EMAIL " +
				     "  FROM MST_ACCOUNT_INFO mci, MST_BANK_INFO mbi, MST_BRANCH_INFO mbri " +
				     " WHERE     mci.bank_id = mbi.bank_id " +
				     "       AND mci.branch_id = mbri.branch_id " +
				     "       AND MCI.BANK_ID =? " +
				     "       AND MCI.branch_id =? " +
				     "       AND MCI.account_no =? " ;



			
			PreparedStatement ps1=conn.prepareStatement(account_info_sql);
			ps1.setString(1, bank_id);
			ps1.setString(2, branch_id);
			ps1.setString(3, account_no);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		
        		accountInfo.setBank_name(resultSet.getString("BANK_NAME"));
        		branchInfo.setAddress(resultSet.getString("ADDRESS"));
        		branchInfo.setPhone(resultSet.getString("PHONE"));
        		branchInfo.setFax(resultSet.getString("FAX"));
        		branchInfo.setEmail(resultSet.getString("EMAIL"));
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
	
	*/

	private ArrayList<TransactionDTO> getTransactionListDateWise()
	{
	ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
	CustomerDTO customer=new CustomerDTO();
	CustomerPersonalDTO cusPersonalInfo=new CustomerPersonalDTO();
		
		try {
			
			String transaction_sql="SELECT TRANS_DATE, " +
				     "         tbl.CUSTOMER_ID, " +
				     "         DECODE (tbl.CUSTOMER_ID, NULL, PARTICULARS, FULL_NAME) PARTICULARS, " +
				     "         DECODE (tbl.CUSTOMER_ID, " +
				     "                 NULL, '', " +
				     "                 REPLACE (PARTICULARS, 'Collection, ')) " +
				     "            BILL_MONTH, " +
				     "         DEBIT, " +
				     "         CREDIT, " +
				     "         INSERTED_ON " +
				     "    FROM (SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
				     "                 TRANS_ID, " +
				     "                 CUSTOMER_ID, " +
				     "                 PARTICULARS, " +
				     "                 DEBIT, " +
				     "                 CREDIT, " +
				     "                 INSERTED_ON " +
				     "            FROM BANK_ACCOUNT_LEDGER " +
				     "           WHERE     TO_CHAR (TRANS_DATE, 'dd-MM-yyyy') = '"+collection_date+"' " +
				     "                 AND ACCOUNT_NO = '"+account_no+"' " +
				     "                 AND TRANS_TYPE <> 0 " +
				     "          UNION ALL " +
				     "          SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
				     "                 TRANS_ID, " +
				     "                 CUSTOMER_ID, " +
				     "                 PARTICULARS, " +
				     "                 DEBIT, " +
				     "                 CREDIT, " +
				     "                 INSERTED_ON " +
				     "            FROM BANK_ACCOUNT_LEDGER " +
				     "           WHERE     TO_CHAR (TRANS_DATE, 'dd-MM-yyyy') = '"+collection_date+"' " +
				     "                 AND ACCOUNT_NO = '"+account_no+"' " +
				     "                 AND TRANS_TYPE = 0" +
				     "				   AND REF_ID not in(select DEPOSIT_ID from MST_DEPOSIT where DEPOSIT_TYPE=1)) tbl " +
				     "         LEFT OUTER JOIN MVIEW_CUSTOMER_INFO mcf " +
				     "            ON tbl.CUSTOMER_ID = mcf.CUSTOMER_ID " +
				     " ORDER BY INSERTED_ON,TRANS_DATE asc,TRANS_ID asc " ;
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		transactionDto1.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto1.setBillMonth(resultSet.getString("BILL_MONTH"));
        		transactionDto1.setParticulars(resultSet.getString("PARTICULARS"));
        		transactionDto1.setDebit(resultSet.getDouble("DEBIT"));
        		transactionDto1.setCredit(resultSet.getDouble("CREDIT"));
	
        		transactionList.add(transactionDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionList;
	}
	
	private ArrayList<TransactionDTO> getCollectlistByMonth(){
		ArrayList<TransactionDTO> transactionList=new ArrayList<TransactionDTO>();
		
		try {
			
			
			
		
			
			
			String transaction_sql="SELECT TRANS_DATE, " +
					"         PERTICULARS, " +
					"         DEBIT, " +
					"         CREDIT " +
					"    FROM (  " +
					"    SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"    SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Received Amount From Customer As Security Deposit', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                 CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 0" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'By  Bank Gas Bill Collection', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 1" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                      PARTICULARS PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 2" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"           SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Receive Amount from Bank (Refund,Interest etc)', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 3" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"           SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'AMOUNT RECEIVE FROM BANK FOR STD ACCOUNT', " +
					"                           'AMOUNT TRANSFER TO STD ACCOUNT') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 4" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE, PERTICULARS " +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Interest Income', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 5" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE,PERTICULARS" +
					"          UNION ALL " +
					"          SELECT TRANS_DATE,PERTICULARS,SUM (DEBIT) DEBIT, " +
					"                   SUM (CREDIT) CREDIT FROM( " +
					"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE,TRANS_ID, " +
					"                   DECODE (CREDIT, " +
					"                           0, 'Installment Collection', " +
					"                           'Withdrawl') " +
					"                      PERTICULARS, " +
					"                   DEBIT, " +
					"                   CREDIT " +
					"              FROM BANK_ACCOUNT_LEDGER " +
					"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
					"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
					"                   AND ACCOUNT_NO = '"+account_no+"' " +
					"                   AND TRANS_TYPE = 7" +
					"                   ORDER BY TRANS_DATE asc,TRANS_ID asc) " +
					"          GROUP BY TRANS_DATE,PERTICULARS) " +
					"		   ORDER BY TRANS_DATE " ;




			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto=new TransactionDTO();
        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto.setTrans_date(resultSet.getString("TRANS_DATE"));
        		transactionDto.setParticulars(resultSet.getString("PERTICULARS"));
        		transactionDto.setDebit(resultSet.getDouble("DEBIT"));
        		transactionDto.setCredit(resultSet.getDouble("CREDIT"));       		
   
        		
        		transactionList.add(transactionDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return transactionList;
	}
	

	private ArrayList<TransactionDTO> getMonthlyDetailsTransactionList()
	{
	ArrayList<TransactionDTO> transactionListDetails=new ArrayList<TransactionDTO>();
	String area=loggedInUser.getArea_id();
	String wClause="";
//	if(account_no.equals("01787685899-0501")){
//		wClause=" AND TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
//				"                 AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
//				"                 and substr(CUSTOMER_ID,1,2)='"+area+"' " ;
//	}else{
//		wClause=" AND TO_CHAR (TRANS_DATE, 'MM') = "+collection_month+" " +
//				"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " ;
//	}
		
		try {
			
			String transaction_sql="SELECT TRANS_DATE, " +
									"         tbl.CUSTOMER_ID, " +
									"         DECODE (tbl.CUSTOMER_ID, NULL, PARTICULARS, FULL_NAME) PARTICULARS, " +
									"         DECODE (tbl.CUSTOMER_ID, " +
									"                 NULL, '', " +
									"                 REPLACE (PARTICULARS, 'Collection, ')) " +
									"            BILL_MONTH, " +
									"         DEBIT, " +
									"         CREDIT " +
									"    FROM (SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
									"                 TRANS_ID, " +
									"                 CUSTOMER_ID, " +
									"                 PARTICULARS, " +
									"                 DEBIT, " +
									"                 CREDIT " +
									"            FROM BANK_ACCOUNT_LEDGER " +
									"           WHERE     ACCOUNT_NO = '"+account_no+"' " +
									"                 AND TRANS_TYPE <> 0 " +
									"                 AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collection_month+",2,0) " +
									"                 AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+" " +
									"          UNION ALL " +
									"          SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
									"                 TRANS_ID, " +
									"                 CUSTOMER_ID, " +
									"                 PARTICULARS, " +
									"                 DEBIT, " +
									"                 CREDIT " +
									"            FROM BANK_ACCOUNT_LEDGER " +
									"           WHERE     ACCOUNT_NO = '"+account_no+"' " +
									"                 AND TRANS_TYPE = 0 " +
									"                 AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collection_month+",2,0) " +
									"                 AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collection_year+"" +
									"				  AND REF_ID not in(select DEPOSIT_ID from MST_DEPOSIT where DEPOSIT_TYPE=1)) tbl " +
									"         LEFT OUTER JOIN MVIEW_CUSTOMER_INFO mcf " +
									"            ON tbl.CUSTOMER_ID = mcf.CUSTOMER_ID " +
									"ORDER BY TRANS_DATE ASC, TRANS_ID ASC " ;
					
		


			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		transactionDto1.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto1.setBillMonth(resultSet.getString("BILL_MONTH"));
        		transactionDto1.setParticulars(resultSet.getString("PARTICULARS"));
        		transactionDto1.setDebit(resultSet.getDouble("DEBIT"));
        		transactionDto1.setCredit(resultSet.getDouble("CREDIT"));
        		
        		transactionListDetails.add(transactionDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionListDetails;
	}
	
	
	/*
	 * */
	
	private ArrayList<TransactionDTO> getBankWiseDetailsTransaction()
	{
	ArrayList<TransactionDTO> transactionListDetails=new ArrayList<TransactionDTO>();
	String area=loggedInUser.getArea_id();
	
	int collectionMonth=Integer.valueOf(collection_month);
	int collectionYear=Integer.valueOf(collection_year);
		
		try {
			
			String transaction_sql="SELECT BANK_NAME, " +
									"         BRANCH_NAME, " +
									"         SUM (ACTUAL_REVENUE) ACTUAL_REVENUE, " +
									"         SUM (SURCHARGE) SURCHARGE, " +
									"         SUM (FEES) FEESS, " +
									"         SUM (SECURITY) SECURITY " +
									"    FROM (  SELECT BANK_NAME, " +
									"                   BRANCH_NAME, " +
									"                   SUM (DEBIT) - SUM (SURCHARGE) ACTUAL_REVENUE, " +
									"                   SUM (SURCHARGE) SURCHARGE, " +
									"                   0 FEES, " +
									"                   0 SECURITY " +
									"              FROM bank_account_ledger BAL, " +
									"                   MST_BANK_INFO MBI, " +
									"                   MST_BRANCH_INFO MBRI " +
									"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
									"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
									"					 and MBRI.area_id=MBI.area_id" +
									"                   AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 1 " +
									"                   AND MBRI.AREA_ID ='"+area+"' " +
									"          GROUP BY BANK_NAME, BRANCH_NAME " +
									"          UNION ALL " +
									"            SELECT BANK_NAME, " +
									"                   BRANCH_NAME, " +
									"                   0 ACTUAL_REVENUE, " +
									"                   0 SURCHARGE, " +
									"                   SUM (debit) AS FEES, " +
									"                   0 SECURITY " +
									"              FROM bank_account_ledger BAL, " +
									"                   MST_BANK_INFO MBI, " +
									"                   MST_BRANCH_INFO MBRI " +
									"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
									"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
									"					 and MBRI.area_id=MBI.area_id" +
									"                   AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 7 " +
									"                   AND MBRI.AREA_ID ='"+area+"' " +
									"          GROUP BY BANK_NAME, BRANCH_NAME " +
									"          UNION ALL " +
									"            SELECT BANK_NAME, " +
									"                   BRANCH_NAME, " +
									"                   0 ACTUAL_REVENUE, " +
									"                   0 SURCHARGE, " +
									"                   0 FEES, " +
									"                   SUM (debit) - SUM (credit) AS SECURITY " +
									"              FROM bank_account_ledger BAL, " +
									"                   MST_BANK_INFO MBI, " +
									"                   MST_BRANCH_INFO MBRI " +
									"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
									"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
									"					 and MBRI.area_id=MBI.area_id" +
									"                   AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 0 " +
									"					AND REF_ID not in(select DEPOSIT_ID from MST_DEPOSIT where DEPOSIT_TYPE=1)" +
									"                   AND MBRI.AREA_ID ='"+area+"'" +
									"          GROUP BY BANK_NAME, BRANCH_NAME) " +
									"GROUP BY BANK_NAME, BRANCH_NAME " +
									"ORDER BY BANK_NAME ";
			
			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setBank_name(resultSet.getString("BANK_NAME"));
        		transactionDto1.setBranch_name(resultSet.getString("BRANCH_NAME"));
        		transactionDto1.setGas_bill(resultSet.getDouble("ACTUAL_REVENUE"));
        		transactionDto1.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		transactionDto1.setFees(resultSet.getDouble("FEESS"));
        		transactionDto1.setSecurity(resultSet.getDouble("SECURITY"));
        		
        		
        		transactionListDetails.add(transactionDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionListDetails;
	}
	
	
	/* End Bank Wise Transaction 
	 * sujon
	 * */
	
	
	/* All bank wise total collection
	 * 
	 * sujon 
	 * */
	
	private ArrayList<TransactionDTO> getAllBankWiseTotalsTransaction()
	{
	ArrayList<TransactionDTO> transactionListDetails=new ArrayList<TransactionDTO>();
	String area=loggedInUser.getArea_id();
	int collectionMonth=Integer.valueOf(collection_month);
	int collectionYear=Integer.valueOf(collection_year);
		
		try {
			
			String transaction_sql="SELECT BANK_NAME, " +
									"         BRANCH_NAME, " +
									"         SUM (ACTUAL_REVENUE) ACTUAL_REVENUE, " +
									"         SUM (SURCHARGE) SURCHARGE, " +
									"         SUM (FEES) FEESS, " +
									"         SUM (SECURITY) SECURITY " +
									"    FROM (  SELECT MBI.BANK_NAME, " +
									"                   MBRI.BRANCH_NAME, " +
									"                   SUM (ACTUAL_REVENUE) ACTUAL_REVENUE, " +
									"                   SUM (SURCHARGE) SURCHARGE, " +
									"                   0 FEES, " +
									"                   0 SECURITY " +
									"              FROM bank_account_ledger BAL, " +
									"                   MST_BANK_INFO MBI, " +
									"                   MST_BRANCH_INFO MBRI " +
									"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
									"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
									"                   AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 1 " +
									"                   AND MBRI.BRANCH_ID IN (SELECT MBRI.BRANCH_ID " +
									"                                            FROM MST_BRANCH_INFO MBRI " +
									"                                           WHERE AREA_ID = '"+area+"') " +
									"          GROUP BY TRANS_TYPE, MBI.BANK_NAME, MBRI.BRANCH_NAME " +
									"          UNION ALL " +
									"            SELECT MBI.BANK_NAME, " +
									"                   MBRI.BRANCH_NAME, " +
									"                   0 ACTUAL_REVENUE, " +
									"                   0 SURCHARGE, " +
									"                   SUM (debit) AS FEES, " +
									"                   0 SECURITY " +
									"              FROM bank_account_ledger BAL, " +
									"                   MST_BANK_INFO MBI, " +
									"                   MST_BRANCH_INFO MBRI " +
									"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
									"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
									"                   AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 7 " +
									"                   AND MBRI.BRANCH_ID IN (SELECT MBRI.BRANCH_ID " +
									"                                            FROM MST_BRANCH_INFO MBRI " +
									"                                           WHERE AREA_ID = '"+area+"') " +
									"          GROUP BY MBI.BANK_NAME, MBRI.BRANCH_NAME " +
									"          UNION ALL " +
									"            SELECT MBI.BANK_NAME, " +
									"                   MBRI.BRANCH_NAME, " +
									"                   0 ACTUAL_REVENUE, " +
									"                   0 SURCHARGE, " +
									"                   0 FEES, " +
									"                   SUM (debit) - SUM (credit) AS SECURITY " +
									"              FROM bank_account_ledger BAL, " +
									"                   MST_BANK_INFO MBI, " +
									"                   MST_BRANCH_INFO MBRI " +
									"             WHERE     BAL.BRANCH_ID = MBRI.BRANCH_ID " +
									"                   AND MBI.BANK_ID = MBRI.BANK_ID " +
									"                   AND TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 0 " +
									"					AND REF_ID not in(select DEPOSIT_ID from MST_DEPOSIT where DEPOSIT_TYPE=1)" +
									"                   AND MBRI.BRANCH_ID IN (SELECT MBRI.BRANCH_ID " +
									"                                            FROM MST_BRANCH_INFO MBRI " +
									"                                           WHERE AREA_ID = '"+area+"') " +
									"          GROUP BY MBI.BANK_NAME, MBRI.BRANCH_NAME) " +
									"GROUP BY BANK_NAME, BRANCH_NAME " +
									"ORDER BY BANK_NAME ";

			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setBank_name(resultSet.getString("BANK_NAME"));
        		transactionDto1.setBranch_name(resultSet.getString("BRANCH_NAME"));
        		transactionDto1.setGas_bill(resultSet.getDouble("ACTUAL_REVENUE"));
        		transactionDto1.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		transactionDto1.setFees(resultSet.getDouble("FEESS"));
        		transactionDto1.setSecurity(resultSet.getDouble("SECURITY"));
        		
        		
        		transactionListDetails.add(transactionDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return transactionListDetails;
	}
	
	/* end all bank wise total collection
	 * 
	 * */
	
	
	
	/* security collection month wise for all category 
	 * 
	 * */
	
	private ArrayList<TransactionDTO> getAllBankWiseSecurityCollection( String branch_id, String category_id )
	{
	ArrayList<TransactionDTO> transactionListDetails=new ArrayList<TransactionDTO>();
	String area=loggedInUser.getArea_id();
	int collectionMonth=Integer.valueOf(collection_month);

	int collectionYear=Integer.valueOf(collection_year);
	
	PreparedStatement ps1=null;
	ResultSet resultSet=null;
	
		
		try {
			
			String transaction_sql="SELECT bal.CUSTOMER_ID,CPI.FULL_NAME, DEBIT " +
									"    FROM bank_account_ledger bal, CUSTOMER_PERSONAL_INFO cpi " +
									"   WHERE     BAL.CUSTOMER_ID = CPI.CUSTOMER_ID " +
									"         AND TO_CHAR (TRANS_DATE, 'MM') = LPAD ("+collectionMonth+", 2, 0) " +
									"         AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"         AND TRANS_TYPE = 0 " +
									"         AND ACCOUNT_NO = '"+branch_id+"' " +
									"         and Substr(bal.CUSTOMER_ID,3,2)='"+category_id+"' " +
									"ORDER BY CUSTOMER_ID ";

			
			 ps1=conn.prepareStatement(transaction_sql);
			//ps1.setString(1, collection_month);
			//ps1.setString(2, collection_year);
			//ps1.setString(3, branch_id);
			//ps1.setString(3, collection_month);
			//ps1.setString(4, collection_year);
			//ps1.setString(6, branch_id);
			//ps1.setString(5, collection_month);
			//ps1.setString(6, collection_year);
			//ps1.setString(9, branch_id);
        	
        	resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		transactionDto1.setFull_name(resultSet.getString("FULL_NAME"));
        		transactionDto1.setDebit(resultSet.getDouble("DEBIT"));
        		  
        		transactionListDetails.add(transactionDto1);
        		
        	}
        	
        	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally{
			try {
				resultSet.close();
				ps1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
//		finally { 
//		    try { 
//		        if (resultSet != null) 
//		        	resultSet.close(); 
//		    } catch (SQLException sqle) {// log this} 
//		    try { 
//		        if (stmt != null) 
//		            stmt.close(); 
//		    } catch (SQLException sqle) {// log this} 
//		    try { 
//		        if (conn != null) 
//		            conn.close(); 
//		    } catch (SQLException sqle)  {// log this} }
//		}
		
		
		
		return transactionListDetails;
	}
	
	
	/* security collection month wise for all category end
	 * 
	 * */
	
	
	
	/* All Bank wise transaction method with branch_id argument 
	 * 
	 *  sujon
	 * */
	
	private ArrayList<TransactionDTO> getAllBankWiseTotalsTransaction( String branch_id )
	{
	ArrayList<TransactionDTO> transactionListDetails=new ArrayList<TransactionDTO>();
	String area=loggedInUser.getArea_id();
	int collectionMonth=Integer.valueOf(collection_month);

	int collectionYear=Integer.valueOf(collection_year);
		
		try {
			
			String transaction_sql="SELECT TRANS_DATE, " +
									"         SUM (ACTUAL_REVENUE) ACTUAL_REVENUE, " +
									"         SUM (SURCHARGE) SURCHARGE, " +
									"         SUM (FEES) FEESS, " +
									"         SUM (SECURITY) SECURITY " +
									"    FROM (  SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
									"                   SUM (DEBIT) - SUM (SURCHARGE) as ACTUAL_REVENUE, " +
									"                   SUM (SURCHARGE) SURCHARGE, " +
									"                   0 FEES, " +
									"                   0 SECURITY " +
									"              FROM bank_account_ledger " +
									"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 1 " +
									"                   AND BRANCH_ID = '"+branch_id+"'" +
									"          GROUP BY TRANS_DATE " +
									"          UNION ALL " +
									"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
									"                   0 ACTUAL_REVENUE, " +
									"                   0 SURCHARGE, " +
									"                   SUM (debit) AS FEES, " +
									"                   0 SECURITY " +
									"              FROM bank_account_ledger " +
									"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+" " +
									"                   AND TRANS_TYPE = 7 " +
									"                   AND BRANCH_ID = '"+branch_id+"'" +
									"          GROUP BY TRANS_DATE " +
									"          UNION ALL " +
									"            SELECT TO_CHAR (TRANS_DATE) TRANS_DATE, " +
									"                   0 ACTUAL_REVENUE, " +
									"                   0 SURCHARGE, " +
									"                   0 FEES, " +
									"                   SUM (debit) - SUM (credit) AS SECURITY " +
									"              FROM bank_account_ledger " +
									"             WHERE     TO_CHAR (TRANS_DATE, 'MM') = lpad("+collectionMonth+",2,0) " +
									"                   AND TO_CHAR (TRANS_DATE, 'YYYY') = "+collectionYear+"" +
									"                   AND TRANS_TYPE = 0 " +
									"                   AND BRANCH_ID = '"+branch_id+"'" +
									"          			AND REF_ID not in(select DEPOSIT_ID from MST_DEPOSIT where DEPOSIT_TYPE=1)" +
									"				GROUP BY TRANS_DATE) " +
									"GROUP BY TRANS_DATE " +
									"ORDER BY TRANS_DATE ASC ";

			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
			//ps1.setString(1, collection_month);
			//ps1.setString(2, collection_year);
			//ps1.setString(3, branch_id);
			//ps1.setString(3, collection_month);
			//ps1.setString(4, collection_year);
			//ps1.setString(6, branch_id);
			//ps1.setString(5, collection_month);
			//ps1.setString(6, collection_year);
			//ps1.setString(9, branch_id);
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		TransactionDTO transactionDto1=new TransactionDTO();
        		transactionDto1.setTrans_date(resultSet.getString("TRANS_DATE"));
        		transactionDto1.setGas_bill(resultSet.getDouble("ACTUAL_REVENUE"));
        		transactionDto1.setSurcharge(resultSet.getDouble("SURCHARGE"));
        		transactionDto1.setFees(resultSet.getDouble("FEESS"));
        		transactionDto1.setSecurity(resultSet.getDouble("SECURITY"));
        		
        		transactionListDetails.add(transactionDto1);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return transactionListDetails;
	}
	
	/* End All Bank wise transaction method with branch_id argument 
	 * 
	 *  sujon
	 * */
	

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



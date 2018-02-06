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
import org.jgtdsl.dto.BalanceSheetDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
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

public class YearlyBalanceSheet extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	ArrayList<BalanceSheetDTO> balanceSheetInfoList = new ArrayList<BalanceSheetDTO>();
	
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	

	    private  String fiscal_year;
	    private  String report_for; 
	    private  String area="01";
	    private  String from_date;
	    private  String to_date;
	    private  String customer_type;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	

	public String execute() throws Exception
	{
				
		String fileName="YearlyBalance_Sheet.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate() );
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
			
			PdfPTable headLinetable = null;
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{5,90,5});
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Area Wise Accounts Receivable Statement of Different Categories as on "+from_date+" To "+to_date,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headLinetable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setMinimumHeight(8f);
			pcell.setBorder(0);
			pcell.setColspan(3);
			headLinetable.addCell(pcell);
			
			
			document.add(headLinetable);
			
			///////////////////////////////////////////////////////////////////////
			
			
			/* End of the table */
			
			
			if(report_for.equals("details")){
				if(customer_type.equals("01")){
					generatePdfDetails(document);
				}else{
					generatePdfDetailsNonmetered(document);
				}
				
			}else if(report_for.equals("area_wise")){
				generatePdfArea_wise(document);
			}else if(report_for.equals("category_wise")){
				generatePdfCategory_wise(document);
			}else if(report_for.equals("category_wisef")){
				generatePdfJGTDSLReceivable(document);
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
	
	private void generatePdfDetailsNonmetered(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(16);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{3,12,7,7,7,6,6,6,6,6,6,6,6,6,5,5});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Burner Qty",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(6);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Freedom Fighter/ Waiver",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		String previousCategory= new String("");
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalCollectedSurcharge=0.0;
		double subtotalchalanIT=0.0;
		double subTotalWaiver=0.0;
		double subTotalCollectionAmt=0.0;
		
		double grandTotalPreviousBalance=0.0;
		double grandTotalGasBill=0.0;
		double grandTotalSurcharge=0.0;
		double grandTotalSales=0.0;
		double grandTotalGasBillCollection=0.0;
		double grandTotalMeterRent=0.0;
		double grandTotalHHV=0.0;
		double grandTotalCollection1=0.0;
		double grandTotalChalanIT=0.0;
		double grandTotalVatRebate=0.0;
		double grandTotalCollection2=0.0;
		double grandTotalEndingBalance=0.0;
		double grandTotalCollectedSurcharge=0.0;
		
		
		balanceSheetInfoList=getYearlyBalanceSheetInfoNonMetered();
		
		
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String currentCategory=balanceSheetInfoList.get(i).getCustomer_category();
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setBorder(0);
				pcell.setColspan(17);
				pTable.addCell(pcell);
				
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(4);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectedSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalCollectionTotal=subtotalCollectionAmt+subtotalCollectedSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalWaiver),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalWaiver;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalPreviousBalance=0.0;
					subtotalSalesBill=0.0;
					subtotalSaleSurcharge=0.0;
					subtotalSalesTotal=0.0;
					subtotalCollectionAmt=0.0;
					subtotalCollectionTotal=0.0;
					subtotalEndingBalance=0.0;
					totalCollectionEnd=0.0;
					subtotalMeterRent=0.0;
					subtotalHHV=0.0;
					subtotalchalanIT=0.0;
					subtotalCollectedSurcharge=0.0;
					subTotalWaiver=0.0;
					subTotalCollectionAmt=0.0;
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setMinimumHeight(10f);
					pcell.setBorder(0);
					pcell.setColspan(16);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setBorder(0);
					pcell.setColspan(16);
					pTable.addCell(pcell);					
				
				}
				previousCategory=balanceSheetInfoList.get(i).getCustomer_category();
				
				
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerName()+" "+balanceSheetInfoList.get(i).getCustomerAddress(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerID(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			

			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBurner()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getCollectedSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getWaiver()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getWaiver()+balanceSheetInfoList.get(i).getChalanIT());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			grandTotalPreviousBalance=grandTotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			grandTotalGasBill=grandTotalGasBill+balanceSheetInfoList.get(i).getBillAmount();
			grandTotalSurcharge=grandTotalSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			grandTotalSales=grandTotalSales+grandTotalGasBill+grandTotalSurcharge;
			grandTotalGasBillCollection=grandTotalGasBillCollection+balanceSheetInfoList.get(i).getCollectedAmount();
			grandTotalCollectedSurcharge=grandTotalCollectedSurcharge+balanceSheetInfoList.get(i).getCollectedSurcharge();
			grandTotalCollection1=grandTotalCollection1+grandTotalGasBillCollection+grandTotalCollectedSurcharge;
			grandTotalChalanIT=grandTotalChalanIT+balanceSheetInfoList.get(i).getChalanIT();
			grandTotalVatRebate=grandTotalVatRebate+balanceSheetInfoList.get(i).getWaiver();
			grandTotalCollection2=grandTotalCollection2+grandTotalChalanIT+grandTotalVatRebate+grandTotalCollection1;
			grandTotalEndingBalance=grandTotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			///////////////////////////////////////////////
			
			if(currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				subtotalCollectedSurcharge=subtotalCollectedSurcharge+balanceSheetInfoList.get(i).getCollectedSurcharge();
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalWaiver=subTotalWaiver+balanceSheetInfoList.get(i).getWaiver();
				
			}else if(!currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				subtotalCollectedSurcharge=subtotalCollectedSurcharge+balanceSheetInfoList.get(i).getCollectedSurcharge();
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalWaiver=subTotalWaiver+balanceSheetInfoList.get(i).getVateRebate();
			}
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(4);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectedSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				
				subtotalCollectionTotal=subtotalCollectionAmt+subtotalCollectedSurcharge+subtotalMeterRent+subtotalHHV;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalWaiver),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalWaiver;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(4);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSales),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollectedSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection1),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);		
		
		document.add(pTable);
		
	}
	
	private void generatePdfDetails(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(17);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,10,6,6,6,6,6,6,6,6,6,6,6,6,7,6,5});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Customer Code",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(8);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		String previousCategory= new String("");
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subTotalCollectionAmt=0.0;
		
		double grandTotalPreviousBalance=0.0;
		double grandTotalGasBill=0.0;
		double grandTotalSurcharge=0.0;
		double grandTotalSales=0.0;
		double grandTotalGasBillCollection=0.0;
		double grandTotalMeterRent=0.0;
		double grandTotalHHV=0.0;
		double grandTotalCollection1=0.0;
		double grandTotalChalanIT=0.0;
		double grandTotalVatRebate=0.0;
		double grandTotalCollection2=0.0;
		double grandTotalEndingBalance=0.0;
		
		
		balanceSheetInfoList=getYearlyBalanceSheetInfo();
		
		
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String currentCategory=balanceSheetInfoList.get(i).getCustomer_category();
			
			if(i==0){
				
				pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setBorder(0);
				pcell.setColspan(17);
				pTable.addCell(pcell);
				
				
			}
			
			if(!currentCategory.equals(previousCategory)){
				
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(3);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalCollectionTotal=subtotalCollectionAmt+subtotalSaleSurcharge+subtotalMeterRent+subtotalHHV;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalVatRebate;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(1);
					pTable.addCell(pcell);
					
					subtotalPreviousBalance=0.0;
					subtotalSalesBill=0.0;
					subtotalSaleSurcharge=0.0;
					subtotalSalesTotal=0.0;
					subtotalCollectionAmt=0.0;
					subtotalCollectionTotal=0.0;
					subtotalEndingBalance=0.0;
					totalCollectionEnd=0.0;
					subtotalMeterRent=0.0;
					subtotalHHV=0.0;
					subtotalchalanIT=0.0;
					subTotalVatRebate=0.0;
					subTotalCollectionAmt=0.0;
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setMinimumHeight(10f);
					pcell.setBorder(0);
					pcell.setColspan(17);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Customer Category        :"+balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setBorder(0);
					pcell.setColspan(17);
					pTable.addCell(pcell);					
				
				}
				previousCategory=balanceSheetInfoList.get(i).getCustomer_category();
				
				
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerName()+" "+balanceSheetInfoList.get(i).getCustomerAddress(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerID(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getVateRebate()+balanceSheetInfoList.get(i).getChalanIT());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			
			grandTotalPreviousBalance=grandTotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			grandTotalGasBill=grandTotalGasBill+balanceSheetInfoList.get(i).getBillAmount();
			grandTotalSurcharge=grandTotalSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			grandTotalSales=grandTotalSales+grandTotalGasBill+grandTotalSurcharge;
			grandTotalGasBillCollection=grandTotalGasBillCollection+balanceSheetInfoList.get(i).getCollectedAmount();
			grandTotalMeterRent=grandTotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			grandTotalHHV=grandTotalHHV+balanceSheetInfoList.get(i).getHhv();
			grandTotalCollection1=grandTotalCollection1+grandTotalGasBillCollection+grandTotalMeterRent+grandTotalHHV+grandTotalSurcharge;
			grandTotalChalanIT=grandTotalChalanIT+balanceSheetInfoList.get(i).getChalanIT();
			grandTotalVatRebate=grandTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			grandTotalCollection2=grandTotalCollection2+grandTotalChalanIT+grandTotalVatRebate+grandTotalCollection1;
			grandTotalEndingBalance=grandTotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			///////////////////////////////////////////////
			
			if(currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
			}else if(!currentCategory.equals(previousCategory)){
				
				subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
				subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			}
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(3);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalSalesTotal=subtotalSalesBill+subtotalSaleSurcharge;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subtotalCollectionTotal=subtotalCollectionAmt+subtotalSaleSurcharge+subtotalMeterRent+subtotalHHV;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				subTotalCollectionAmt=subtotalCollectionTotal+subtotalchalanIT+subTotalVatRebate;
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(1);
				pTable.addCell(pcell);
			}
			
		}
		
		pcell = new PdfPCell(new Paragraph("Grand Total = ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalPreviousBalance),ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSales),ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalGasBillCollection),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalMeterRent),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalHHV),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection1),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalChalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCollection2),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pTable.addCell(pcell);		
		
		document.add(pTable);
		
	}
	
	private void generatePdfArea_wise(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(16);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,10,6,6,6,6,6,6,6,6,6,6,6,8,8,6});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Area",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(8);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subtotalCollectionEnd=0.0;
		
		balanceSheetInfoList=getAreawiseBalanceSheetInfo();
		
		String areaName="";
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			areaName=String.valueOf(Area.values()[Integer.valueOf(balanceSheetInfoList.get(i).getArea_id())-1]);
			
			pcell = new PdfPCell(new Paragraph(areaName,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesTotal=subtotalSalesTotal+totalBillAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalCollectionTotal=subtotalCollectionTotal+totalCollectionAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionEnd=subtotalCollectionEnd+totalCollectionEnd;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionEnd),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
		
	}
	private void generatePdfCategory_wise(Document document)throws DocumentException{

		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(16);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,10,6,6,6,6,6,6,6,6,6,6,6,8,8,6});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Category",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(8);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		double totalGovtAmount=0.0;
		double totalPvtAmount=0.0;
		
		
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subtotalCollectionEnd=0.0;
		String previousSubCategory=new String("");
		String CategoryId=new String("");
		double subCategorytotalPreviousBalance=0.0;
		double subCategorytotalSalesBill=0.0;
		double subCategorytotalSaleSurcharge=0.0;
		double subCategorytotalSalesTotal=0.0;
		double subCategorytotalCollectionAmt=0.0;
		double subCategorytotalCollectionTotal=0.0;
		double subCategorytotalEndingBalance=0.0;
		double totalCategoryCollectionEnd=0.0;
		double subCategorytotalMeterRent=0.0;
		double subCategorytotalHHV=0.0;
		double subCategorytotalchalanIT=0.0;
		double subCategoryTotalVatRebate=0.0;
		double subCategorytotalCollectionEnd=0.0;
		double totalCategoryBillAmount=0.0;
		double totalCategoryCollectionAmount=0.0;
		
		balanceSheetInfoList=getCategorywiseBalanceSheetInfo();
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String CurrentSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			CategoryId=balanceSheetInfoList.get(i).getCustomer_category();
			
			
			if(!CurrentSubCategory.equals(previousSubCategory)){
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					subCategorytotalPreviousBalance=0.0;
					subCategorytotalSalesBill=0.0;
					subCategorytotalSaleSurcharge=0.0;
					subCategorytotalSalesTotal=0.0;
					subCategorytotalCollectionAmt=0.0;
					subCategorytotalCollectionTotal=0.0;
					subCategorytotalEndingBalance=0.0;
					totalCategoryCollectionEnd=0.0;
					subCategorytotalMeterRent=0.0;
					subCategorytotalHHV=0.0;
					subCategorytotalchalanIT=0.0;
					subCategoryTotalVatRebate=0.0;
					subCategorytotalCollectionEnd=0.0;
					totalCategoryBillAmount=0.0;
					totalCategoryCollectionAmount=0.0;
					
				}
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesTotal=subtotalSalesTotal+totalBillAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			
			subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalCollectionTotal=subtotalCollectionTotal+totalCollectionAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionEnd=subtotalCollectionEnd+totalCollectionEnd;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			if(CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				
			}else if(!CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
	
			}
			previousSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(2);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
			}
			
			
			if(CategoryId.equals("02") || CategoryId.equals("04") || CategoryId.equals("06") || CategoryId.equals("08") ||
					CategoryId.equals("10") || CategoryId.equals("12")){
				
			}
			
			
		}
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionEnd),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
	
	}
	
	private void generatePdfJGTDSLReceivable(Document document)throws DocumentException{
		
		document.setMargins(20,20,30,72);
		PdfPCell pcell=new PdfPCell();
		PdfPTable pTable = new PdfPTable(16);
		pTable.setWidthPercentage(100);
		pTable.setWidths(new float[]{2,10,6,6,6,6,6,6,6,6,6,6,6,8,8,6});
		pTable.setHeaderRows(3);
		
		pcell = new PdfPCell(new Paragraph("Sl. No.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Category",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);		
		
		pcell = new PdfPCell(new Paragraph("Balance on "+from_date,ReportUtil.f8B));/* Value of Previous Economic Year*/
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Sales",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pcell.setColspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(8);
		pcell.setRowspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Balance on "+to_date,ReportUtil.f8B)); /*Value of current Economic year */
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Average Due Equivalent Month",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(3);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Bank",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(5);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Chalan/IT",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total Collection",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Gas Bill (with meter rent)",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Gas Bill",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Surcharge",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("HHV/NHV",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(1);
		pTable.addCell(pcell);
		
		double totalGovtAmount=0.0;
		double totalPvtAmount=0.0;
		
		
		double totalBillAmount=0.0;
		double totalCollectionAmount=0.0;
		
		double subtotalPreviousBalance=0.0;
		double subtotalSalesBill=0.0;
		double subtotalSaleSurcharge=0.0;
		double subtotalSalesTotal=0.0;
		double subtotalCollectionAmt=0.0;
		double subtotalCollectionTotal=0.0;
		double subtotalEndingBalance=0.0;
		double totalCollectionEnd=0.0;
		double subtotalMeterRent=0.0;
		double subtotalHHV=0.0;
		double subtotalchalanIT=0.0;
		double subTotalVatRebate=0.0;
		double subtotalCollectionEnd=0.0;
		String previousSubCategory=new String("");
		String CategoryId=new String("");
		double subCategorytotalPreviousBalance=0.0;
		double subCategorytotalSalesBill=0.0;
		double subCategorytotalSaleSurcharge=0.0;
		double subCategorytotalSalesTotal=0.0;
		double subCategorytotalCollectionAmt=0.0;
		double subCategorytotalCollectionTotal=0.0;
		double subCategorytotalEndingBalance=0.0;
		double totalCategoryCollectionEnd=0.0;
		double subCategorytotalMeterRent=0.0;
		double subCategorytotalHHV=0.0;
		double subCategorytotalchalanIT=0.0;
		double subCategoryTotalVatRebate=0.0;
		double subCategorytotalCollectionEnd=0.0;
		double totalCategoryBillAmount=0.0;
		double totalCategoryCollectionAmount=0.0;
		
		double gsubCategorytotalPreviousBalance=0.0;
		double gsubCategorytotalSalesBill=0.0;
		double gsubCategorytotalSaleSurcharge=0.0;
		double gsubCategorytotalSalesTotal=0.0;
		double gsubCategorytotalCollectionAmt=0.0;
		double gsubCategorytotalCollectionTotal=0.0;
		double gsubCategorytotalEndingBalance=0.0;
		double gtotalCategoryCollectionEnd=0.0;
		double gsubCategorytotalMeterRent=0.0;
		double gsubCategorytotalHHV=0.0;
		double gsubCategorytotalchalanIT=0.0;
		double gsubCategoryTotalVatRebate=0.0;
		double gsubCategorytotalCollectionEnd=0.0;
		double gtotalCategoryBillAmount=0.0;
		double gtotalCategoryCollectionAmount=0.0;
		
		double psubCategorytotalPreviousBalance=0.0;
		double psubCategorytotalSalesBill=0.0;
		double psubCategorytotalSaleSurcharge=0.0;
		double psubCategorytotalSalesTotal=0.0;
		double psubCategorytotalCollectionAmt=0.0;
		double psubCategorytotalCollectionTotal=0.0;
		double psubCategorytotalEndingBalance=0.0;
		double ptotalCategoryCollectionEnd=0.0;
		double psubCategorytotalMeterRent=0.0;
		double psubCategorytotalHHV=0.0;
		double psubCategorytotalchalanIT=0.0;
		double psubCategoryTotalVatRebate=0.0;
		double psubCategorytotalCollectionEnd=0.0;
		double ptotalCategoryBillAmount=0.0;
		double ptotalCategoryCollectionAmount=0.0;
		
		balanceSheetInfoList=getCategorywiseFullBalanceSheetInfo();
		
		int listSize=balanceSheetInfoList.size();
		
		for (int i = 0; i < listSize; i++) {
			
			String CurrentSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			CategoryId=balanceSheetInfoList.get(i).getCustomer_category();
			
			
			if(!CurrentSubCategory.equals(previousSubCategory)){
				if(i>0){
					
					pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pTable.addCell(pcell);
					
					subCategorytotalPreviousBalance=0.0;
					subCategorytotalSalesBill=0.0;
					subCategorytotalSaleSurcharge=0.0;
					subCategorytotalSalesTotal=0.0;
					subCategorytotalCollectionAmt=0.0;
					subCategorytotalCollectionTotal=0.0;
					subCategorytotalEndingBalance=0.0;
					totalCategoryCollectionEnd=0.0;
					subCategorytotalMeterRent=0.0;
					subCategorytotalHHV=0.0;
					subCategorytotalchalanIT=0.0;
					subCategoryTotalVatRebate=0.0;
					subCategorytotalCollectionEnd=0.0;
					totalCategoryBillAmount=0.0;
					totalCategoryCollectionAmount=0.0;
					
				}
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(balanceSheetInfoList.get(i).getCustomerCategoryName(),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pTable.addCell(pcell);			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getPreviousBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalPreviousBalance=subtotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getBillAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesBill=subtotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSaleSurcharge=subtotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
			
			totalBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBillAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalSalesTotal=subtotalSalesTotal+totalBillAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getCollectedAmount()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionAmt=subtotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getMeterRent()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalMeterRent=subtotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getSurcharge()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getHhv()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			
			subtotalHHV=subtotalHHV+balanceSheetInfoList.get(i).getHhv();
			
			totalCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
					+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionAmount),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalCollectionTotal=subtotalCollectionTotal+totalCollectionAmount;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getChalanIT()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalchalanIT=subtotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getVateRebate()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subTotalVatRebate=subTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
			
			totalCollectionEnd=totalCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalCollectionEnd),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			subtotalCollectionEnd=subtotalCollectionEnd+totalCollectionEnd;
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(balanceSheetInfoList.get(i).getEndingBalance()),ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(1);
			pTable.addCell(pcell);
			
			subtotalEndingBalance=subtotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pTable.addCell(pcell);
			
			
			if(CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
				
			}else if(!CurrentSubCategory.equals(previousSubCategory)){
				
				subCategorytotalPreviousBalance=subCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				subCategorytotalSalesBill=subCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				subCategorytotalSaleSurcharge=subCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				totalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				subCategorytotalSalesTotal=subCategorytotalSalesTotal+totalCategoryBillAmount;
				subCategorytotalCollectionAmt=subCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				subCategorytotalMeterRent=subCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				subCategorytotalHHV=subCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				totalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				subCategorytotalCollectionTotal=subCategorytotalCollectionTotal+totalCategoryCollectionAmount;
				subCategorytotalchalanIT=subCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				subCategoryTotalVatRebate=subCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				totalCategoryCollectionEnd=totalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				subCategorytotalCollectionEnd=subCategorytotalCollectionEnd+totalCategoryCollectionEnd;
				subCategorytotalEndingBalance=subCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
	
			}
			previousSubCategory=balanceSheetInfoList.get(i).getSubCategory();
			
			if(i==listSize-1){
				
				pcell = new PdfPCell(new Paragraph(" Sub Total",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(2);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalPreviousBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesBill),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSalesTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionAmt),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalMeterRent),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalSaleSurcharge),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalHHV),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionTotal),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalchalanIT),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategoryTotalVatRebate),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalCollectionEnd),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(taka_format.format(subCategorytotalEndingBalance),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pTable.addCell(pcell);
				
			}
			
			
			if(CategoryId.equals("02") || CategoryId.equals("04") || CategoryId.equals("06") || CategoryId.equals("08") ||
					CategoryId.equals("10") || CategoryId.equals("12")){
				
				gsubCategorytotalPreviousBalance=gsubCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				gsubCategorytotalSalesBill=gsubCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				gsubCategorytotalSaleSurcharge=gsubCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				gtotalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				gsubCategorytotalSalesTotal=gsubCategorytotalSalesTotal+gtotalCategoryBillAmount;
				gsubCategorytotalCollectionAmt=gsubCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				gsubCategorytotalMeterRent=gsubCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				gsubCategorytotalHHV=gsubCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				gtotalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				gsubCategorytotalCollectionTotal=gsubCategorytotalCollectionTotal+gtotalCategoryCollectionAmount;
				gsubCategorytotalchalanIT=gsubCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				gsubCategoryTotalVatRebate=gsubCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				gtotalCategoryCollectionEnd=gtotalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				gsubCategorytotalCollectionEnd=gsubCategorytotalCollectionEnd+gtotalCategoryCollectionEnd;
				gsubCategorytotalEndingBalance=gsubCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
				
			}else{
				
				psubCategorytotalPreviousBalance=psubCategorytotalPreviousBalance+balanceSheetInfoList.get(i).getPreviousBalance();
				psubCategorytotalSalesBill=psubCategorytotalSalesBill+balanceSheetInfoList.get(i).getBillAmount();
				psubCategorytotalSaleSurcharge=psubCategorytotalSaleSurcharge+balanceSheetInfoList.get(i).getSurcharge();
				ptotalCategoryBillAmount=balanceSheetInfoList.get(i).getBillAmount()+balanceSheetInfoList.get(i).getSurcharge();
				psubCategorytotalSalesTotal=psubCategorytotalSalesTotal+ptotalCategoryBillAmount;
				psubCategorytotalCollectionAmt=psubCategorytotalCollectionAmt+balanceSheetInfoList.get(i).getCollectedAmount();
				psubCategorytotalMeterRent=psubCategorytotalMeterRent+balanceSheetInfoList.get(i).getMeterRent();
				
				psubCategorytotalHHV=psubCategorytotalHHV+balanceSheetInfoList.get(i).getHhv();
				
				ptotalCategoryCollectionAmount=balanceSheetInfoList.get(i).getCollectedAmount()+balanceSheetInfoList.get(i).getSurcharge()
						+balanceSheetInfoList.get(i).getMeterRent()+balanceSheetInfoList.get(i).getHhv();
				
				psubCategorytotalCollectionTotal=psubCategorytotalCollectionTotal+ptotalCategoryCollectionAmount;
				psubCategorytotalchalanIT=psubCategorytotalchalanIT+balanceSheetInfoList.get(i).getChalanIT();
				
				psubCategoryTotalVatRebate=psubCategoryTotalVatRebate+balanceSheetInfoList.get(i).getVateRebate();
				
				ptotalCategoryCollectionEnd=ptotalCategoryCollectionAmount+(balanceSheetInfoList.get(i).getChalanIT()+balanceSheetInfoList.get(i).getVateRebate());
				psubCategorytotalCollectionEnd=psubCategorytotalCollectionEnd+ptotalCategoryCollectionEnd;
				psubCategorytotalEndingBalance=psubCategorytotalEndingBalance+balanceSheetInfoList.get(i).getEndingBalance();
	
				
			}
			
			
		}
		
		/*------------------------Sub Total Govt---------------------------*/
		
		pcell = new PdfPCell(new Paragraph(" Sub Total Govt.",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalPreviousBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSalesBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSalesTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalCollectionAmt),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalMeterRent),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalHHV),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalCollectionTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalchalanIT),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategoryTotalVatRebate),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalCollectionEnd),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(gsubCategorytotalEndingBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		/*------------------------Sub Total PVT.----------------------------*/
		
		pcell = new PdfPCell(new Paragraph(" Sub Total PVT.",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalPreviousBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSalesBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSalesTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalCollectionAmt),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalMeterRent),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalHHV),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalCollectionTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalchalanIT),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategoryTotalVatRebate),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalCollectionEnd),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(psubCategorytotalEndingBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		/*------------------------Grand Total------------------------------*/
		
		pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setColspan(2);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalPreviousBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesBill),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSalesTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionAmt),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalMeterRent),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalSaleSurcharge),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalHHV),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionTotal),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalchalanIT),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subTotalVatRebate),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalCollectionEnd),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(taka_format.format(subtotalEndingBalance),ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pTable.addCell(pcell);
		
		document.add(pTable);
		
		
	} 
	
	
	private ArrayList<BalanceSheetDTO> getYearlyBalanceSheetInfo(){
				
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		
		String area=loggedInUser.getArea_id();
		
		try {
			String[] fromDate=from_date.split("-");
			String[] toDate=to_date.split("-");
			
				String fromDateMonth=fromDate[1].toString();
				String fromDateYear=fromDate[2].toString();
				
				String toDateMonth=toDate[1].toString();
				String toDateYear=toDate[2].toString();
				
				String fromYearMonth1=fromDateYear+""+fromDateMonth;
				String toYearMonth1=toDateYear+""+toDateMonth;
				
				int fromYearMonth=Integer.parseInt(fromYearMonth1);
				int toYearMonth=Integer.parseInt(toYearMonth1);
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="SELECT aa.CUSTOMER_ID, " +
					"         aa.CUSTOMER_CATEGORY, " +
					"         aa.CUSTOMER_CATEGORY_NAME, " +
					"         MCI.FULL_NAME, " +
					"         MCI.ADDRESS, " +
					"         getLedgerBal (aa.CUSTOMER_ID, '"+from_date+"') bal_f, " +
					"         aa.BILLED_AMOUNT, " +
					"         aa.SURCHARGE_AMOUNT, " +
					"         bb.COLLECTION_AMOUNT, " +
					"         aa.METER_RENT, " +
					"         aa.HHV_NHV_BILL, " +
					"         aa.VAT_REBATE_AMOUNT, " +
					"         bb.TAX_AMOUNT, " +
					"         getLedgerBal (aa.CUSTOMER_ID, '"+to_date+"') bal_n " +
					"    FROM (  SELECT bm.CUSTOMER_ID, " +
					"                   BM.CUSTOMER_CATEGORY, " +
					"                   BM.CUSTOMER_CATEGORY_NAME, " +
					"                   SUM (bm.BILLED_AMOUNT) BILLED_AMOUNT, " +
					"                   SUM (pb.SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
					"                   sum(pb.METER_RENT) METER_RENT, " +
					"                   sum(pb.HHV_NHV_BILL) HHV_NHV_BILL, " +
					"                   sum(pb.VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT " +
					"              FROM summary_margin_pb pb, bill_metered bm " +
					"             WHERE     PB.BILL_ID = bm.bill_id " +
					"                   AND TO_NUMBER (bm.bill_year || LPAD (bm.bill_month, 2, 0)) BETWEEN "+fromYearMonth+" " +
					"                                                                                  AND "+toYearMonth+" " +
					"          GROUP BY bm.CUSTOMER_ID, " +
					"                   BM.CUSTOMER_CATEGORY, " +
					"                   BM.CUSTOMER_CATEGORY_NAME) aa, " +
					"         MVIEW_CUSTOMER_INFO mci, " +
					"         (  SELECT CUSTOMER_ID, SUM (COLLECTION_AMOUNT) COLLECTION_AMOUNT, sum(bc.TAX_AMOUNT) TAX_AMOUNT " +
					"              FROM BILL_COLLECTION_METERED bc " +
					"             WHERE BILL_ID IN " +
					"                      (SELECT bill_id " +
					"                         FROM bill_metered " +
					"                        WHERE TO_NUMBER (bill_year || LPAD (bill_month, 2, 0)) BETWEEN "+fromYearMonth+" " +
					"                                                                                   AND "+toYearMonth+") " +
					"          GROUP BY CUSTOMER_ID) bb " +
					"   WHERE     aa.CUSTOMER_ID = mci.CUSTOMER_ID " +
					"         AND aa.CUSTOMER_ID = bb.CUSTOMER_ID " +
					"         AND bb.CUSTOMER_ID = mci.CUSTOMER_ID " +
					"         AND SUBSTR (aa.CUSTOMER_ID, 1, 2) = '"+area+"' " +
					"ORDER BY CUSTOMER_CATEGORY, aa.CUSTOMER_ID " ;





			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		balanceSheetDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		balanceSheetDTO.setCustomerAddress(resultSet.getString("ADDRESS"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("BAL_F"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE_AMOUNT"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTION_AMOUNT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("BAL_N"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
        		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return balanceSheetList;
	}
	
	private ArrayList<BalanceSheetDTO> getYearlyBalanceSheetInfoNonMetered(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		
		String area=loggedInUser.getArea_id();
		
		try {
			String[] fromDate=from_date.split("-");
			String[] toDate=to_date.split("-");
			
				String fromDateMonth=fromDate[1].toString();
				String fromDateYear=fromDate[2].toString();
				
				String toDateMonth=toDate[1].toString();
				String toDateYear=toDate[2].toString();
				
				String fromYearMonth1=fromDateYear+""+fromDateMonth;
				String toYearMonth1=toDateYear+""+toDateMonth;
				
				int fromYearMonth=Integer.parseInt(fromYearMonth1);
				int toYearMonth=Integer.parseInt(toYearMonth1);
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="select TAB1.CUSTOMER_ID,FULL_NAME,ADDRESS,getLedgerBal (TAB1.CUSTOMER_ID, '"+from_date+"') bal_f,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,BILLED_AMOUNT,COLLECTED_BILLED_AMOUNT, PAYABLE_SURCHARGE,COLLECTED_SURCHARGE,WAIVER, " +
					" NEW_DOUBLE_BURNER_QNT,getLedgerBal (TAB1.CUSTOMER_ID, '"+to_date+"') bal_next,NEW_DOUBLE_BURNER_QNT_BILLCAL from( " +
					"select aa.CUSTOMER_ID CUSTOMER_ID,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,BILLED_AMOUNT,COLLECTED_BILLED_AMOUNT, PAYABLE_SURCHARGE,COLLECTED_SURCHARGE,WAIVER from( " +
					"SELECT CUSTOMER_ID,CUSTOMER_CATEGORY,CUSTOMER_CATEGORY_NAME,SUM (BILLED_AMOUNT) BILLED_AMOUNT,sum(nvl(COLLECTED_BILLED_AMOUNT,0)) COLLECTED_BILLED_AMOUNT, " +
					"                   SUM (nvl(ACTUAL_SURCHARGE,0)) PAYABLE_SURCHARGE,sum(nvl(COLLECTED_SURCHARGE,0)) COLLECTED_SURCHARGE " +
					"              FROM  bill_non_metered  " +
					"             WHERE   TO_NUMBER (bill_year || LPAD (bill_month, 2, 0)) BETWEEN "+fromYearMonth+" " +
					"																		   AND "+toYearMonth+" " +
					"             AND substr(customer_id,1,2)= '"+area+"' " +
					"          GROUP BY CUSTOMER_ID, " +
					"                   CUSTOMER_CATEGORY, " +
					"                   CUSTOMER_CATEGORY_NAME) aa, " +
					" (select customer_id, sum(nvl(TOTAL_COLLECTED_AMOUNT,0)) WAIVER from bill_collection_non_metered " +
					"where bank_id='1' " +
					"AND COLLECTION_DATE between to_date( '"+from_date+"','dd-mm-rrrr') AND to_date( '"+to_date+"','dd-mm-rrrr') " +
					"group by customer_id) bb " +
					"where aa.CUSTOMER_ID=bb.CUSTOMER_ID(+)) tab1, " +
					"(select bqc.CUSTOMER_ID CUSTOMER_ID,MCI.FULL_NAME FULL_NAME,MCI.ADDRESS_LINE1 ADDRESS, bqc.NEW_DOUBLE_BURNER_QNT NEW_DOUBLE_BURNER_QNT,bqc.NEW_DOUBLE_BURNER_QNT_BILLCAL NEW_DOUBLE_BURNER_QNT_BILLCAL " +
					" from BURNER_QNT_CHANGE BQC,MVIEW_CUSTOMER_INFO MCI where PID IN(  " +
					"						select max(PID) from BURNER_QNT_CHANGE where SUBSTR(customer_id,1,2)='"+area+"' and  effective_date<=to_date( '"+from_date+"','dd-MM-YYYY') " +
					"						GROUP BY CUSTOMER_ID) " +
					"						AND MCI.CUSTOMER_ID = BQC.CUSTOMER_ID " +
					"						order by CUSTOMER_ID desc) tab2 " +
					"where TAB1.CUSTOMER_ID=TAB2.CUSTOMER_ID " +
					"order by TAB1.CUSTOMER_ID " ;

					
			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		balanceSheetDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		balanceSheetDTO.setCustomerAddress(resultSet.getString("ADDRESS"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("BAL_F"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("PAYABLE_SURCHARGE"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTED_BILLED_AMOUNT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("bal_next"));
        		balanceSheetDTO.setBurner(resultSet.getDouble("NEW_DOUBLE_BURNER_QNT_BILLCAL"));
        		balanceSheetDTO.setCollectedSurcharge(resultSet.getDouble("COLLECTED_SURCHARGE"));
        		balanceSheetDTO.setWaiver(resultSet.getDouble("WAIVER"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return balanceSheetList;
	}
	
	private ArrayList<BalanceSheetDTO> getAreawiseBalanceSheetInfo(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		
		
		try {
			String[] fromDate=from_date.split("-");
			String[] toDate=to_date.split("-");
			
				String fromDateMonth=fromDate[1].toString();
				String fromDateYear=fromDate[2].toString();
				
				String toDateMonth=toDate[1].toString();
				String toDateYear=toDate[2].toString();
				
				String fromYearMonth1=fromDateYear+""+fromDateMonth;
				String toYearMonth1=toDateYear+""+toDateMonth;
				
				int fromYearMonth=Integer.parseInt(fromYearMonth1);
				int toYearMonth=Integer.parseInt(toYearMonth1);
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="select area,  " +
					"        sum(bal_f) bal_f , " +
					"        sum(BILLED_AMOUNT) BILLED_AMOUNT, " +
					"        sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
					"        sum(COLLECTION_AMOUNT) COLLECTION_AMOUNT, " +
					"        sum(METER_RENT) METER_RENT, " +
					"        sum(HHV_NHV_BILL) HHV_NHV_BILL, " +
					"        sum(VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT, " +
					"        sum(TAX_AMOUNT) TAX_AMOUNT, " +
					"        sum(bal_n) bal_n " +
					"from ( " +
					"  SELECT SUBSTR (aa.CUSTOMER_ID, 1, 2) area,        " +
					"         getLedgerBal (aa.CUSTOMER_ID, '"+from_date+"') bal_f, " +
					"         aa.BILLED_AMOUNT, " +
					"         aa.SURCHARGE_AMOUNT, " +
					"         bb.COLLECTION_AMOUNT, " +
					"         aa.METER_RENT, " +
					"         aa.HHV_NHV_BILL, " +
					"         aa.VAT_REBATE_AMOUNT, " +
					"         bb.TAX_AMOUNT, " +
					"         getLedgerBal (aa.CUSTOMER_ID, '"+to_date+"') bal_n " +
					"    FROM (  SELECT bm.CUSTOMER_ID, " +
					"                   BM.CUSTOMER_CATEGORY, " +
					"                   BM.CUSTOMER_CATEGORY_NAME, " +
					"                   SUM (bm.BILLED_AMOUNT) BILLED_AMOUNT, " +
					"                   SUM (pb.SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
					"                   sum(pb.METER_RENT) METER_RENT, " +
					"                   sum(pb.HHV_NHV_BILL) HHV_NHV_BILL, " +
					"                   sum(pb.VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT " +
					"              FROM summary_margin_pb pb, bill_metered bm " +
					"             WHERE     PB.BILL_ID = bm.bill_id " +
					"                   AND TO_NUMBER (bm.bill_year || LPAD (bm.bill_month, 2, 0)) BETWEEN  "+fromYearMonth+" " +
					"                                                                                  AND  "+toYearMonth+" " +
					"          GROUP BY bm.CUSTOMER_ID, " +
					"                   BM.CUSTOMER_CATEGORY, " +
					"                   BM.CUSTOMER_CATEGORY_NAME) aa, " +
					"         (  SELECT CUSTOMER_ID, SUM (COLLECTION_AMOUNT) COLLECTION_AMOUNT, sum(bc.TAX_AMOUNT) TAX_AMOUNT " +
					"              FROM BILL_COLLECTION_METERED bc " +
					"             WHERE BILL_ID IN " +
					"                      (SELECT bill_id " +
					"                         FROM bill_metered " +
					"                        WHERE TO_NUMBER (bill_year || LPAD (bill_month, 2, 0)) BETWEEN  "+fromYearMonth+" " +
					"                                                                                   AND "+toYearMonth+") " +
					"          GROUP BY CUSTOMER_ID) bb " +
					"   WHERE  aa.CUSTOMER_ID = bb.CUSTOMER_ID " +
					") " +
					"group by  area " +
					"order by area " ;






			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		
        		
        		balanceSheetDTO.setArea_id(resultSet.getString("AREA"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("bal_f"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE_AMOUNT"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTION_AMOUNT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("BAL_N"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
        		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return balanceSheetList;
	}
	
private ArrayList<BalanceSheetDTO> getCategorywiseBalanceSheetInfo(){
		
		ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
		
		String area=loggedInUser.getArea_id();
		
		try {
			String[] fromDate=from_date.split("-");
			String[] toDate=to_date.split("-");
			
				String fromDateMonth=fromDate[1].toString();
				String fromDateYear=fromDate[2].toString();
				
				String toDateMonth=toDate[1].toString();
				String toDateYear=toDate[2].toString();
				
				String fromYearMonth1=fromDateYear+""+fromDateMonth;
				String toYearMonth1=toDateYear+""+toDateMonth;
				
				int fromYearMonth=Integer.parseInt(fromYearMonth1);
				int toYearMonth=Integer.parseInt(toYearMonth1);
			
			
			 	
	        	ResultSet resultSet;
			
			
			
			String disconnection_info_sql="select CUSTOMER_CATEGORY, " +
					"        CUSTOMER_CATEGORY_NAME,  " +
					"		 substr(CUSTOMER_CATEGORY_NAME,1,3) SUBCATE, "+
					"        sum(bal_f) bal_f, " +
					"        sum(BILLED_AMOUNT) BILLED_AMOUNT, " +
					"        sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
					"        sum(COLLECTION_AMOUNT) COLLECTION_AMOUNT, " +
					"        sum(METER_RENT) METER_RENT, " +
					"        sum(HHV_NHV_BILL) HHV_NHV_BILL, " +
					"        sum(VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT, " +
					"        sum(TAX_AMOUNT) TAX_AMOUNT, " +
					"        sum(bal_n) bal_n " +
					"from ( " +
					"  SELECT aa.CUSTOMER_ID, " +
					"         aa.CUSTOMER_CATEGORY, " +
					"         aa.CUSTOMER_CATEGORY_NAME,         " +
					"         getLedgerBal (aa.CUSTOMER_ID, '"+from_date+"') bal_f, " +
					"         aa.BILLED_AMOUNT, " +
					"         aa.SURCHARGE_AMOUNT, " +
					"         bb.COLLECTION_AMOUNT, " +
					"         aa.METER_RENT, " +
					"         aa.HHV_NHV_BILL, " +
					"         aa.VAT_REBATE_AMOUNT, " +
					"         bb.TAX_AMOUNT, " +
					"         getLedgerBal (aa.CUSTOMER_ID, '"+to_date+"') bal_n " +
					"    FROM (  SELECT bm.CUSTOMER_ID, " +
					"                   BM.CUSTOMER_CATEGORY, " +
					"                   BM.CUSTOMER_CATEGORY_NAME, " +
					"                   SUM (bm.BILLED_AMOUNT) BILLED_AMOUNT, " +
					"                   SUM (pb.SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
					"                   sum(pb.METER_RENT) METER_RENT, " +
					"                   sum(pb.HHV_NHV_BILL) HHV_NHV_BILL, " +
					"                   sum(pb.VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT "+
					"              FROM summary_margin_pb pb, bill_metered bm " +
					"             WHERE     PB.BILL_ID = bm.bill_id " +
					"                   AND TO_NUMBER (bm.bill_year || LPAD (bm.bill_month, 2, 0)) BETWEEN "+fromYearMonth+" " +
					"                                                                                  AND "+toYearMonth+" " +
					"          GROUP BY bm.CUSTOMER_ID, " +
					"                   BM.CUSTOMER_CATEGORY, " +
					"                   BM.CUSTOMER_CATEGORY_NAME) aa, " +
					"         (  SELECT CUSTOMER_ID, SUM (COLLECTION_AMOUNT) COLLECTION_AMOUNT, sum(bc.TAX_AMOUNT) TAX_AMOUNT " +
					"              FROM BILL_COLLECTION_METERED bc " +
					"             WHERE BILL_ID IN " +
					"                      (SELECT bill_id " +
					"                         FROM bill_metered " +
					"                        WHERE TO_NUMBER (bill_year || LPAD (bill_month, 2, 0)) BETWEEN "+fromYearMonth+" " +
					"                                                                                   AND "+toYearMonth+") " +
					"          GROUP BY CUSTOMER_ID) bb " +
					"   WHERE  aa.CUSTOMER_ID = bb.CUSTOMER_ID " +
					"         AND SUBSTR (aa.CUSTOMER_ID, 1, 2) = '"+area+"' " +
					") " +
					"group by  CUSTOMER_CATEGORY, " +
					"        CUSTOMER_CATEGORY_NAME " +
					"        order by CUSTOMER_CATEGORY " ;







			
			PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
		
        	
        	 resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
        		
        		
        		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
        		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
        		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("bal_f"));
        		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
        		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE_AMOUNT"));
        		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTION_AMOUNT"));
        		balanceSheetDTO.setEndingBalance(resultSet.getDouble("BAL_N"));
        		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
        		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
        		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
        		balanceSheetDTO.setSubCategory(resultSet.getString("SUBCATE"));
        		
        		balanceSheetList.add(balanceSheetDTO);
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return balanceSheetList;
	}

private ArrayList<BalanceSheetDTO> getCategorywiseFullBalanceSheetInfo(){
	
	ArrayList<BalanceSheetDTO> balanceSheetList = new ArrayList<BalanceSheetDTO>();
	
	String area=loggedInUser.getArea_id();
	
	try {
		String[] fromDate=from_date.split("-");
		String[] toDate=to_date.split("-");
		
			String fromDateMonth=fromDate[1].toString();
			String fromDateYear=fromDate[2].toString();
			
			String toDateMonth=toDate[1].toString();
			String toDateYear=toDate[2].toString();
			
			String fromYearMonth1=fromDateYear+""+fromDateMonth;
			String toYearMonth1=toDateYear+""+toDateMonth;
			
			int fromYearMonth=Integer.parseInt(fromYearMonth1);
			int toYearMonth=Integer.parseInt(toYearMonth1);
		
		
		 	
        	ResultSet resultSet;
		
		
		
		String disconnection_info_sql="select CUSTOMER_CATEGORY, " +
				"        CUSTOMER_CATEGORY_NAME,  " +
				"		 substr(CUSTOMER_CATEGORY_NAME,1,3) SUBCATE, "+
				"        sum(bal_f) bal_f, " +
				"        sum(BILLED_AMOUNT) BILLED_AMOUNT, " +
				"        sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
				"        sum(COLLECTION_AMOUNT) COLLECTION_AMOUNT, " +
				"        sum(METER_RENT) METER_RENT, " +
				"        sum(HHV_NHV_BILL) HHV_NHV_BILL, " +
				"        sum(VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT, " +
				"        sum(TAX_AMOUNT) TAX_AMOUNT, " +
				"        sum(bal_n) bal_n " +
				"from ( " +
				"  SELECT aa.CUSTOMER_ID, " +
				"         aa.CUSTOMER_CATEGORY, " +
				"         aa.CUSTOMER_CATEGORY_NAME,         " +
				"         getLedgerBal (aa.CUSTOMER_ID, '"+from_date+"') bal_f, " +
				"         aa.BILLED_AMOUNT, " +
				"         aa.SURCHARGE_AMOUNT, " +
				"         bb.COLLECTION_AMOUNT, " +
				"         aa.METER_RENT, " +
				"         aa.HHV_NHV_BILL, " +
				"         aa.VAT_REBATE_AMOUNT, " +
				"         bb.TAX_AMOUNT, " +
				"         getLedgerBal (aa.CUSTOMER_ID, '"+to_date+"') bal_n " +
				"    FROM (  SELECT bm.CUSTOMER_ID, " +
				"                   BM.CUSTOMER_CATEGORY, " +
				"                   BM.CUSTOMER_CATEGORY_NAME, " +
				"                   SUM (bm.BILLED_AMOUNT) BILLED_AMOUNT, " +
				"                   SUM (pb.SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
				"                   sum(pb.METER_RENT) METER_RENT, " +
				"                   sum(pb.HHV_NHV_BILL) HHV_NHV_BILL, " +
				"                   sum(pb.VAT_REBATE_AMOUNT) VAT_REBATE_AMOUNT "+
				"              FROM summary_margin_pb pb, bill_metered bm " +
				"             WHERE     PB.BILL_ID = bm.bill_id " +
				"                   AND TO_NUMBER (bm.bill_year || LPAD (bm.bill_month, 2, 0)) BETWEEN "+fromYearMonth+" " +
				"                                                                                  AND "+toYearMonth+" " +
				"          GROUP BY bm.CUSTOMER_ID, " +
				"                   BM.CUSTOMER_CATEGORY, " +
				"                   BM.CUSTOMER_CATEGORY_NAME) aa, " +
				"         (  SELECT CUSTOMER_ID, SUM (COLLECTION_AMOUNT) COLLECTION_AMOUNT, sum(bc.TAX_AMOUNT) TAX_AMOUNT " +
				"              FROM BILL_COLLECTION_METERED bc " +
				"             WHERE BILL_ID IN " +
				"                      (SELECT bill_id " +
				"                         FROM bill_metered " +
				"                        WHERE TO_NUMBER (bill_year || LPAD (bill_month, 2, 0)) BETWEEN "+fromYearMonth+" " +
				"                                                                                   AND "+toYearMonth+") " +
				"          GROUP BY CUSTOMER_ID) bb " +
				"   WHERE  aa.CUSTOMER_ID = bb.CUSTOMER_ID " +
				") " +
				"group by  CUSTOMER_CATEGORY, " +
				"        CUSTOMER_CATEGORY_NAME " +
				"        order by CUSTOMER_CATEGORY " ;







		
		PreparedStatement ps1=conn.prepareStatement(disconnection_info_sql);
	
    	
    	 resultSet=ps1.executeQuery();
    	
    	
    	while(resultSet.next())
    	{
    		BalanceSheetDTO balanceSheetDTO = new BalanceSheetDTO();
    		
    		
    		balanceSheetDTO.setCustomerCategoryName(resultSet.getString("CUSTOMER_CATEGORY_NAME"));
    		balanceSheetDTO.setCustomer_category(resultSet.getString("CUSTOMER_CATEGORY"));
    		balanceSheetDTO.setPreviousBalance(resultSet.getDouble("bal_f"));
    		balanceSheetDTO.setBillAmount(resultSet.getDouble("BILLED_AMOUNT"));
    		balanceSheetDTO.setSurcharge(resultSet.getDouble("SURCHARGE_AMOUNT"));
    		balanceSheetDTO.setCollectedAmount(resultSet.getDouble("COLLECTION_AMOUNT"));
    		balanceSheetDTO.setEndingBalance(resultSet.getDouble("BAL_N"));
    		balanceSheetDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
    		balanceSheetDTO.setHhv(resultSet.getDouble("HHV_NHV_BILL"));
    		balanceSheetDTO.setChalanIT(resultSet.getDouble("TAX_AMOUNT"));
    		balanceSheetDTO.setVateRebate(resultSet.getDouble("VAT_REBATE_AMOUNT"));
    		balanceSheetDTO.setSubCategory(resultSet.getString("SUBCATE"));
    		
    		balanceSheetList.add(balanceSheetDTO);
    	}
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	return balanceSheetList;
}


	
	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	
	public String getFiscal_year() {
		return fiscal_year;
	}

	public void setFiscal_year(String fiscal_year) {
		this.fiscal_year = fiscal_year;
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

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}


}

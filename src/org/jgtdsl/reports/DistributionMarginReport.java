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




public class DistributionMarginReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>distributionList = new ArrayList<JournalVoucherDTO>();
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
				
		String fileName="Distribution_Margin_Report.pdf";
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
			
			pcell=new PdfPCell(new Paragraph("Ledger of Distribution Margin", ReportUtil.f11B));
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
			
			pcell = new PdfPCell(new Paragraph("Bill Month",ReportUtil.f9B));
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
			
			pcell = new PdfPCell(new Paragraph("Bill",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("System Gain",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*-------------------------------End of Head Line-----------------------------------------------*/
			double debit=0.0;
			double bill=0.0;
			double sysGain=0.0;
			double totalCredit=0.0;
	
			double openingBalance=0.0;
			double balance=0.0;
			
			double totalDebit=0.0;
			double totalBill=0.0;
			double totalSysGain=0.0;
			double grandTotalCredit=0.0;
			
			
			distributionList=getBGFCLCredit();
			int listSize=distributionList.size();
			
			for (int i = 0; i < listSize; i++) {
				debit=distributionList.get(i).getDebit();
				bill = distributionList.get(i).getSales();
				sysGain=distributionList.get(i).getSystemGain();
				totalCredit=bill+sysGain;
				
				if(i==0){
					balance=openingBalance;
				}
				
				if(debit>0.0){
					balance=balance+debit;
				}else{
					balance=balance+totalCredit;
				}
				if(i==0){
					
					pcell = new PdfPCell(new Paragraph("01-07-"+distributionList.get(i).getYear(),ReportUtil.f9B));
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
					
					pcell = new PdfPCell(new Paragraph(distributionList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("By Bank,"+Month.values()[Integer.valueOf(distributionList.get(i).getMonth())-1]+"-"+distributionList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalDebit=totalDebit+debit;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(debit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalSysGain=totalSysGain+sysGain;
					
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
					
					pcell = new PdfPCell(new Paragraph(distributionList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("To, Distribution Margin, "+Month.values()[Integer.valueOf(distributionList.get(i).getMonth())-1]+"-"+distributionList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					totalBill=totalBill+bill;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(bill),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalSysGain=totalSysGain+sysGain;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(sysGain),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					grandTotalCredit=grandTotalCredit+totalCredit;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(totalCredit),ReportUtil.f9));
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
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalBill),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalSysGain),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(grandTotalCredit),ReportUtil.f9B));
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
			String firstPart=fiscalYear[0].toString()+"07";
			String secondPart=fiscalYear[1].toString()+"06";
			
			
			String defaulterSql="SELECT LAST_DAY (TO_DATE (tab1.BILLING_MONTH || '-' || tab1.BILLING_YEAR, 'MM-YYYY')) TRANS_DATE," +
					"to_char(LAST_DAY (TO_DATE (tab1.BILLING_MONTH || '-' || tab1.BILLING_YEAR, 'MM-YYYY'))) T_DATE,TAB1.BILLING_MONTH,TAB2.BILLING_YEAR, " +
					"SALES,PURCHASE,(SALES-PURCHASE) SYSTEM_GAIN FROM( " +
					"SELECT BILLING_MONTH ,BILLING_YEAR, SUM (MINIMUM_CHARGE) + SUM (VALUE_OF_ACTUAL_CONSUMPTION)  SALES " +
					"FROM SALES_REPORT SR, CUSTOMER_CONNECTION conn, MST_CUSTOMER_CATEGORY mcc " +
					"WHERE SR.customer_id = conn.customer_id " +
					"AND BILLING_YEAR||lpad(BILLING_MONTH,2,0) between "+firstPart+" and "+secondPart+" " +
					"         AND SUBSTR (SR.customer_id, 3, 2) = MCC.CATEGORY_ID " +
					"         group by BILLING_MONTH,BILLING_YEAR) TAB1, " +
					"(SELECT BILLING_MONTH,BILLING_YEAR, PURCHASE FROM( " +
					"SELECT BILLING_MONTH , BILLING_YEAR, " +
					"(DISTRIBUTION_MARGIN+ASSET_VALUE+BAPEX+DWELLHED+DWELLHED+GD_FUND+PDF+SD+TRANSMISSION+VAT+WELLHEAD) PURCHASE FROM( " +
					"select MB.MONTH BILLING_MONTH ,MB.YEAR BILLING_YEAR, " +
					"(MB.DISTRIBUTION_MARGIN+MS.DISTRIBUTION_MARGIN) DISTRIBUTION_MARGIN, " +
					"(MB.ASSET_VALUE+MS.ASSET_VALUE) ASSET_VALUE, " +
					"(MB.BAPEX+MS.BAPEX) BAPEX, " +
					"(MB.DWELLHED+MS.DWELLHED) DWELLHED, " +
					"(MB.GD_FUND+MS.GD_FUND) GD_FUND, " +
					"(MB.PDF+MS.PDF) PDF, " +
					"(MB.SD+MS.SD) SD, " +
					"(MB.TRANSMISSION+MS.TRANSMISSION) TRANSMISSION, " +
					"(MB.VAT+MS.VAT) VAT, " +
					"(MB.WELLHEAD+MS.WELLHEAD) WELLHEAD " +
					"from MARGIN_BGFCL MB,MARGIN_SGFL MS " +
					"where MB.MONTH=MS.MONTH " +
					"and MB.YEAR=MS.YEAR)" +
					"where BILLING_YEAR||lpad(BILLING_MONTH,2,0) between "+firstPart+" and "+secondPart+" )) TAB2 " +
					"WHERE TAB1.BILLING_MONTH=TAB2.BILLING_MONTH(+) " +
					"AND TAB1.BILLING_YEAR=TAB2.BILLING_YEAR(+) " +
					"ORDER BY TAB2.BILLING_YEAR,TAB1.BILLING_MONTH " ;



									
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setTransactionDate(resultSet.getString("T_DATE"));
        		jDto.setSales(resultSet.getDouble("SALES"));
        		jDto.setPurchase(resultSet.getDouble("PURCHASE"));
        		jDto.setSystemGain(resultSet.getDouble("SYSTEM_GAIN"));
        		jDto.setMonth(resultSet.getString("BILLING_MONTH"));
        		jDto.setYear(resultSet.getString("BILLING_YEAR"));
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




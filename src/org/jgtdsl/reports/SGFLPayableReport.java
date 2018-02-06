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




public class SGFLPayableReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>bgfclList = new ArrayList<JournalVoucherDTO>();
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
				
		String fileName="BGFCL_Report.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
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
			
			pcell=new PdfPCell(new Paragraph("Liabilities for Wellhead, SD & VAT (SGFL)", ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("For the FY : "+collection_year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			document.add(mTable);
			
			
			
			
			
			
			PdfPTable jvTable = new PdfPTable(11);
			jvTable.setWidthPercentage(100);
			jvTable.setWidths(new float[]{6,20,8,8,8,8,8,8,8,8,10});
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
			pcell.setColspan(4);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Credit(Tk.)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setColspan(4);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Balance/Payable",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setRowspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Net Paid",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Income Tax",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("VAT Rebate",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("W/Head",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("SD",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("VAT",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Purchase",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*-------------------------------End of Head Line-----------------------------------------------*/
			double totalNetPabable=0.0;
			double totalIncomeTax=0.0;
			double openingBalance=0.0;
			double balance=0.0;
			double totalDebit=0.0;
			double totalVatRevate=0.0;
			double totalWellHead=0.0;
			double totalSD=0.0;
			double totalVAT=0.0;
			double totalPurchaseAmt=0.0;
			
			double netPaid=0.0;
			double incomeTax=0.0;
			double vatRebate=0.0;
			double totalDabitAmt=0.0;
			double wellHead=0.0;
			double sd=0.0;
			double vat=0.0;
			double totalPurchase=0.0;
			
			bgfclList=getBGFCLCredit();
			int listSize=bgfclList.size();
			
			for (int i = 0; i < listSize; i++) {
				netPaid = bgfclList.get(i).getNetPayable();
				incomeTax=bgfclList.get(i).getIncomeTax();
				vatRebate=bgfclList.get(i).getVat_revate()*bgfclList.get(i).getRatio();
				wellHead=bgfclList.get(i).getWellHead();
				sd=bgfclList.get(i).getSd();
				vat=bgfclList.get(i).getVat();
				totalDebit=netPaid+incomeTax;
				
				
				
				if(i==0){
					balance=openingBalance;
				}
				
				if(netPaid>0.0){
					balance=balance-totalDebit;
				}else{
					balance=balance+wellHead+sd+vat;
				}
				if(i==0){
					
					pcell = new PdfPCell(new Paragraph("01-07-"+bgfclList.get(i).getPurchaseYear(),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Opening Balance=",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(8);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(openingBalance),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
				}
				
				if(netPaid>0.0){
					
					pcell = new PdfPCell(new Paragraph(bgfclList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("By Bank,"+Month.values()[Integer.valueOf(bgfclList.get(i).getByBankMonth())-1]+"-"+bgfclList.get(i).getByBankyear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalNetPabable=totalNetPabable+netPaid;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(netPaid),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalIncomeTax=totalIncomeTax+incomeTax;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(incomeTax),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					totalDabitAmt=totalDabitAmt+totalDebit;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(totalDebit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
				}else{
					
					pcell = new PdfPCell(new Paragraph(bgfclList.get(i).getTransactionDate(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("To, Gas Purchase, "+Month.values()[Integer.valueOf(bgfclList.get(i).getPurchaseMonth())-1]+"-"+bgfclList.get(i).getPurchaseYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					totalVatRevate=totalVatRevate+vatRebate;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(vatRebate),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(vatRebate),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					totalWellHead=totalWellHead+wellHead;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(wellHead),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalSD=totalSD+sd;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(sd),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalVAT=totalVAT+vat;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(vat),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					
					
					totalPurchase=wellHead+sd+vat;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(totalPurchase),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalPurchaseAmt=totalPurchaseAmt+totalPurchase;
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(balance),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
				}
								
			}
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalNetPabable),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalIncomeTax),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalVatRevate),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalVatRevate+totalDabitAmt),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalWellHead),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalSD),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalVAT),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPurchaseAmt),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("--------",ReportUtil.f9B));
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
			String firstDate="01-07-"+fiscalYear[0].toString();
			String sndDate="30-06-"+fiscalYear[1].toString();
			
			
			String defaulterSql="SELECT to_char(TRANS_DATE,'dd-mm-yyyy') TRANS_DATE,TRANS_DATE T_DATE, COLLECTION_MONTH, COLLECTION_YEAR,NETPAID,INCOMETEX,MONTH,YEAR,WELLHEAD,SD,VAT,VATREBATE,RATIO From( " +
					"select TRANS_DATE,NULL COLLECTION_MONTH, NULL COLLECTION_YEAR,NULL NETPAID,NULL INCOMETEX,MONTH,YEAR,WELLHEAD,SD,VAT,VATREBATE,RATIO from( " +
					"select TRANS_DATE,TAB1.MONTH,TAB1.YEAR,WELLHEAD,SD,VAT,VATREBATE,RATIO from( " +
					"SELECT LAST_DAY (TO_DATE (month || '-' || MB.year, 'MM-YYYY')) TRANS_DATE, MB.month,MB.year,WELLHEAD,sd,vat                " +
					"FROM MARGIN_SGFL MB " +
					"WHERE MB.year || LPAD (month, 2, 0) BETWEEN "+firstPart+" AND "+secondPart+" " +
					"ORDER BY MB.YEAR ASC, MONTH) tab1, " +
					"(select * from " +
					"(select LAST_DAY (TO_DATE (BILL_MONTH || '-' || BILL_YEAR, 'MM-YYYY')) TRANS_DATE1, BILL_MONTH,BILL_YEAR, sum(VAT_REBATE_AMOUNT) VATREBATE " +
					"from SUMMARY_MARGIN_PB sm, BILL_METERED bm " +
					"where SM.BILL_ID=bm.bill_id " +
					"and nvl(VAT_REBATE_AMOUNT,0)>0 " +
					"and to_number(bm.BILL_YEAR||lpad(bm.BILL_MONTH,2,0)) between "+firstPart+" and "+secondPart+" " +
					"group by BILL_MONTH,BILL_YEAR) tm1, " +
					"(select  sm.month,sm.year, sm.vat/(bm.vat+sm.vat) ratio " +
					"from MARGIN_BGFCL bm, MARGIN_SGFL sm " +
					"where bm.month=sm.month and bm.year=sm.year " +
					"and to_number(sm.YEAR||lpad(sm.MONTH,2,0)) between "+firstPart+" and "+secondPart+") tm2 " +
					"where tm1.BILL_MONTH(+)=tm2.month and tm1.BILL_YEAR(+)=tm2.year) tab2 " +
					"where TAB1.MONTH=TAB2.MONTH(+) " +
					"and TAB1.YEAR=TAB2.YEAR(+)) " +
					"UNION ALL " +
					"SELECT TRANS_DATE, COLLECTION_MONTH, COLLECTION_YEAR,NETPAID,INCOMETEX,NULL MONTH,NULL YEAR,NULL WELLHEAD,NULL SD,NULL VAT,NULL VATREBATE,NULL RATIO From( " +
					"select TRANS_DATE, COLLECTION_MONTH , COLLECTION_YEAR, sum(netpaid) NETPAID,sum(incometax) INCOMETEX from( " +
					"select ba.TRANS_DATE, COLLECTION_MONTH, COLLECTION_YEAR,  " +
					"decode(PARTICULARS,'SGFL',CREDIT,0) netpaid, " +
					"decode(PARTICULARS,'SGFL Income Tax',CREDIT,0) incometax " +
					"from MARGIN_ACCOUNT_PAYABLE_DTL ma, BANK_ACCOUNT_LEDGER ba  " +
					"where MA.TRANS_ID=ba.TRANS_ID " +
					"and ba.TRANS_TYPE=6  " +
					"and TRANS_DATE between to_date('"+firstDate+"','dd/mm/yyyy') and to_date('"+sndDate+"','dd/mm/yyyy') " +
					"and PARTICULARS in( 'SGFL','SGFL Income Tax') " +
					") group by TRANS_DATE, COLLECTION_MONTH, COLLECTION_YEAR)) " +
					"ORDER BY T_DATE " ;
									
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setTransactionDate(resultSet.getString("TRANS_DATE"));
        		jDto.setByBankMonth(resultSet.getString("COLLECTION_MONTH"));
        		jDto.setByBankyear(resultSet.getString("COLLECTION_YEAR"));
        		jDto.setPurchaseMonth(resultSet.getString("month"));
        		jDto.setPurchaseYear(resultSet.getString("year"));
        		jDto.setNetPayable(resultSet.getDouble("NETPAID"));
        		jDto.setIncomeTax(resultSet.getDouble("INCOMETEX"));
        		jDto.setWellHead(resultSet.getDouble("WELLHEAD"));
        		jDto.setSd(resultSet.getDouble("sd"));
        		jDto.setVat(resultSet.getDouble("vat"));
        		jDto.setVat_revate(resultSet.getDouble("VATREBATE"));
        		jDto.setRatio(resultSet.getDouble("RATIO"));
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





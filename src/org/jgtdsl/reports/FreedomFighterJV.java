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




import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class FreedomFighterJV extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>freedomFighterJVList = new ArrayList<JournalVoucherDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String month;
	    private  String collection_year;
	    private  String bill_month;
	    private  String bill_year;
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
		
		
		
		
	public String execute() throws Exception
	{
				
		String fileName="Freedom_Fighter_JV.pdf";
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

			Chunk chunk1 = new Chunk("Revenue Department,",ReportUtil.f8B);
			Chunk chunk2 = new Chunk("Nalka, Sirajganj",ReportUtil.f8B);
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
				headLine="Journal Voucher of Freedom Fighter for the month "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year;
			}else if(report_for.equals("fiscal_wise")){
				headLine="Journal Voucher of Freedom Fighter FY :"+collection_year;
			}else if(report_for.equals("date_wise")){
				headLine="Journal Voucher of Freedom Fighter From "+from_date+" to "+to_date;
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
						
			freedomFighterJVList=getVATRebateJV();
			
			double bgfclDebit=freedomFighterJVList.get(0).getDebit();
			double sgflDebit=freedomFighterJVList.get(1).getDebit();
			
			double credit=freedomFighterJVList.get(2).getCredit();
			
			
			pcell = new PdfPCell(new Paragraph(freedomFighterJVList.get(0).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34012",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(bgfclDebit),ReportUtil.f9));
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
			
			pcell = new PdfPCell(new Paragraph(freedomFighterJVList.get(1).getParticulars(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Dr",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("34015",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(sgflDebit),ReportUtil.f9));
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
			
			pcell = new PdfPCell(new Paragraph(freedomFighterJVList.get(2).getParticulars(),ReportUtil.f8));
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
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(credit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
						
			/*------------------------------------------------------------------------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("Total",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(3);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(freedomFighterJVList.get(0).getDebit()+freedomFighterJVList.get(1).getDebit()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(freedomFighterJVList.get(2).getCredit()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("-",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			if(report_for.equals("date_wise")){
				
				pcell = new PdfPCell(new Paragraph("Being the amount deducted from Liabilities for gas purchase against Gas bill of honorable Freedom fighter customers for the Period: "+from_date+" to "+to_date+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}else if(report_for.equals("month_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount deducted from Liabilities for gas purchase against Gas bill of honorable Freedom fighter customers for the month : "+Month.values()[Integer.valueOf(bill_month)-1]+" "+bill_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}else if(report_for.equals("fiscal_wise")){
				pcell = new PdfPCell(new Paragraph("Being the amount deducted from Liabilities for gas purchase against Gas bill of honorable Freedom fighter customers for the FY : "+collection_year+" taken into accounts through this Journal Voucher.",ReportUtil.f8));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				jvTable.addCell(pcell);
			}
			
			
			
			document.add(jvTable);			
		
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
		

	private ArrayList<JournalVoucherDTO>getVATRebateJV(){
		ArrayList<JournalVoucherDTO>journalVoucherList = new ArrayList<JournalVoucherDTO>();
		try {
			String wClause="";
			String wClause1="";
			
			if(report_for.equals("date_wise")){
				String[] fromDate= from_date.split("-");
				String[] toDate=to_date.split("-");
				
				String fromDate1=fromDate[2].toString()+fromDate[1].toString();
				String toDate1=toDate[2].toString()+toDate[1].toString();				
				
				wClause=" AND BILL_YEAR||lpad(BILL_MONTH,2,0) between "+fromDate1+" and "+toDate1+" " ;
				wClause1=" AND bm.YEAR||lpad(bm.MONTH,2,0) between "+fromDate1+" and "+toDate1+" " ;
			}if(report_for.equals("month_wise")){
				wClause=" AND BILL_YEAR="+bill_year+" AND BILL_MONTH="+bill_month+" ";
				wClause1=" AND bm.YEAR="+bill_year+" AND bm.MONTH="+bill_month+" ";
			}if(report_for.equals("fiscal_wise")){
				String[] fiscalYear=collection_year.split("-");
				String firstPart=fiscalYear[0].toString()+"07";
				String secondPart=fiscalYear[1].toString()+"06";
				
				wClause=" AND BILL_YEAR||lpad(BILL_MONTH,2,0) between "+firstPart+" and "+secondPart+" " ;
				wClause1=" AND bm.YEAR||lpad(bm.MONTH,2,0) between "+firstPart+" and "+secondPart+" " ;
			}
			
			
			String defaulterSql="SELECT 'Liabilities for GAS purchase (BGFCL)' as PARTICULAR,sum(DEBIT) DEBIT,null CREDIT FROM( " +
					"SELECT tab1.BILL_MONTH,tab1.BILL_YEAR,DEBIT*RATIO DEBIT from ( " +
					"select BILL_MONTH,BILL_YEAR,sum(DOUBLE_BURNER_RATE) DEBIT from bill_non_metered " +
					"where FF_QUOTA='Y' " +wClause+
					"group by BILL_MONTH,BILL_YEAR) tab1, " +
					"(SELECT BILL_MONTH,BILL_YEAR,MB_VAT / NULLIF (GT_VAT, 0) AS RATIO FROM  " +
					"(SELECT bm.month BILL_MONTH,bm.year BILL_YEAR,bm.vat MB_VAT,sm.vat MS_VAT,(bm.vat + sm.vat) GT_VAT " +
					"FROM MARGIN_BGFCL bm, MARGIN_SGFL sm " +
					"WHERE bm.month = sm.month " +
					"AND bm.year = sm.year " +wClause1+
					" )) tab2 " +
					"WHERE tab1.BILL_MONTH=TAB2.BILL_MONTH " +
					"AND tab1.BILL_YEAR=TAB2.BILL_YEAR) " +
					"UNION ALL " +
					"SELECT 'Liabilities for GAS purchase (SGFL)' as PARTICULAR,sum(DEBIT) DEBIT,null CREDIT FROM( " +
					"SELECT tab1.BILL_MONTH,tab1.BILL_YEAR,DEBIT*RATIO DEBIT from ( " +
					"select BILL_MONTH,BILL_YEAR,sum(DOUBLE_BURNER_RATE) DEBIT from bill_non_metered " +
					"where FF_QUOTA='Y' " +wClause+
					"group by BILL_MONTH,BILL_YEAR) tab1, " +
					"(SELECT BILL_MONTH,BILL_YEAR,MS_VAT / NULLIF (GT_VAT, 0) AS RATIO FROM  " +
					"(SELECT bm.month BILL_MONTH,bm.year BILL_YEAR,bm.vat MB_VAT,sm.vat MS_VAT,(bm.vat + sm.vat) GT_VAT " +
					"FROM MARGIN_BGFCL bm, MARGIN_SGFL sm " +
					"WHERE bm.month = sm.month " +
					"AND bm.year = sm.year " +wClause1+
					" )) tab2 " +
					"WHERE tab1.BILL_MONTH=TAB2.BILL_MONTH " +
					"AND tab1.BILL_YEAR=TAB2.BILL_YEAR) " +
					"UNION ALL " +
					"SELECT 'Accounts Receivable (Domestic)' as PARTICULAR,null DEBIT,sum(CREDIT) CREDIT from( " +
					"select BILL_MONTH,BILL_YEAR,sum(DOUBLE_BURNER_RATE) CREDIT from bill_non_metered " +
					"where FF_QUOTA='Y' " +wClause+
					"group by BILL_MONTH,BILL_YEAR) " ;
		
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setParticulars(resultSet.getString("PARTICULAR"));
        		jDto.setDebit(resultSet.getDouble("DEBIT"));
        		jDto.setCredit(resultSet.getDouble("CREDIT"));
        		
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




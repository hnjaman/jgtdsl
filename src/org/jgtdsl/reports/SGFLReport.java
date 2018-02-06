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




public class SGFLReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>sgfllList = new ArrayList<JournalVoucherDTO>();
	ArrayList<JournalVoucherDTO>sgfllDebitList = new ArrayList<JournalVoucherDTO>();
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
			
			pcell=new PdfPCell(new Paragraph("Liabilities for Wellhead, SD & VAT (SGFL)", ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("For the FY : "+collection_year,ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
					
			document.add(mTable);
			
			
			
			
			
			
			PdfPTable jvTable = new PdfPTable(8);
			jvTable.setWidthPercentage(100);
			jvTable.setWidths(new float[]{7,20,12,12,12,12,12,13});
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
			pcell.setRowspan(2);
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
			double sgfllAmt=0.0;
			double totalPurchase=0.0;
			double openingBalance=0.0;
			double balance=0.0;
			double welHeadAmt=0.0;
			double sdAmt=0.0;
			double vatAmt=0.0;
			double totalDebit=0.0;
			double totalWellHead=0.0;
			double totalSD=0.0;
			double totalVAT=0.0;
			double totalPurchaseAmt=0.0;
			
			sgfllList=getBGFCLCredit();
			int listSize=sgfllList.size();
			
			for (int i = 0; i < listSize; i++) {
				sgfllAmt = sgfllList.get(i).getBgfcl();
				welHeadAmt=sgfllList.get(i).getWellHead();
				sdAmt=sgfllList.get(i).getSd();
				vatAmt=sgfllList.get(i).getVat();
				
				
				if(i==0){
					balance=openingBalance;
				}
				
				if(sgfllAmt>0.0){
					balance=balance-sgfllList.get(i).getBgfcl();
				}else{
					balance=balance+welHeadAmt+sdAmt+vatAmt;
				}
				if(i==0){
					
					pcell = new PdfPCell(new Paragraph("01-07-"+sgfllList.get(i).getYear(),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("Opening Balance=",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(5);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(openingBalance),ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
				}
				
				if(sgfllAmt>0.0){
					
					pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("By Bank,"+Month.values()[Integer.valueOf(sgfllList.get(i).getMonth())-1]+"-"+sgfllList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(sgfllList.get(i).getBgfcl()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalDebit=totalDebit+sgfllList.get(i).getBgfcl();
					
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
					
					pcell = new PdfPCell(new Paragraph(sgfllList.get(i).getLastDay(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("To, Gas Purchase, "+Month.values()[Integer.valueOf(sgfllList.get(i).getMonth())-1]+"-"+sgfllList.get(i).getYear(),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph("--",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					jvTable.addCell(pcell);
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(sgfllList.get(i).getWellHead()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalWellHead=totalWellHead+sgfllList.get(i).getWellHead();
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(sgfllList.get(i).getSd()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalSD=totalSD+sgfllList.get(i).getSd();
					
					pcell = new PdfPCell(new Paragraph(taka_format.format(sgfllList.get(i).getVat()),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					jvTable.addCell(pcell);
					
					totalVAT=totalVAT+sgfllList.get(i).getVat();
					
					totalPurchase=sgfllList.get(i).getWellHead()+sgfllList.get(i).getSd()+sgfllList.get(i).getVat();
					
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
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalDebit),ReportUtil.f9B));
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
			
			
			String defaulterSql="select month,year, WELLHEAD, sd, vat,TOTAL_SGFL, TO_CHAR(LASTDAY) LASTDAY from( " +
					"select month,year,null WELLHEAD, null sd,null vat,TOTAL_SGFL,LAST_DAY(TO_Date(month||'-'||year, 'MM-YYYY')) LASTDAY " +
					"from GAS_PURCHASE_SUMMARY where year||lpad(month,2,0) between "+firstPart+" and "+secondPart+" " +
					"union all " +
					"select month,year,WELLHEAD, sd,vat, null TOTAL_SGFL,LAST_DAY(TO_Date(month||'-'||year, 'MM-YYYY')) LASTDAY " +
					"from MARGIN_SGFL where year||lpad(month,2,0) between "+firstPart+" and "+secondPart+" " +
					"order by YEAR ASC,MONTH) " ;
									
						
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		JournalVoucherDTO jDto = new JournalVoucherDTO();
        		jDto.setBgfcl(resultSet.getDouble("TOTAL_SGFL"));
        		jDto.setWellHead(resultSet.getDouble("WELLHEAD"));
        		jDto.setSd(resultSet.getDouble("sd"));
        		jDto.setVat(resultSet.getDouble("vat"));
        		jDto.setMonth(resultSet.getString("month"));
        		jDto.setYear(resultSet.getString("year"));
        		jDto.setLastDay(resultSet.getString("LASTDAY"));
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




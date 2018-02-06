package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


import javax.servlet.ServletContext;


import org.apache.commons.collections.map.HashedMap;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.ClarificationDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerListDTO;
import org.jgtdsl.dto.JournalVoucherDTO;
import org.jgtdsl.dto.SecurityRequireReportDTO;
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




public class CertificationSummaryReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<JournalVoucherDTO>tdsJVList = new ArrayList<JournalVoucherDTO>();
	CustomerListDTO customerListDTO = new CustomerListDTO();
	ArrayList<CustomerListDTO> custInfo = new ArrayList<CustomerListDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String month;
	    private  String collection_year;
	    private  String calender_year;
	    private  String issue_month;
	    private  String issue_year; 
	    private  String area;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		HashMap<String, Integer> customerList = new HashMap<String, Integer>();
		ClarificationDTO cusDto=new ClarificationDTO();
		HashMap<String, Integer> ppIssuedCurrMonth = new HashMap<String, Integer>();
		HashMap<String, Integer> ppIssuedLastMonth = new HashMap<String, Integer>();
		HashMap<String, Integer> withDues= new HashMap<String, Integer>();
		HashMap<String, Integer> withoutDues = new HashMap<String, Integer>();
		int number_of_burner=0;
		
		
		
	public String execute() throws Exception
	{
				
		String fileName="Prottayan.pdf";
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
			
			String headLine="Statement Showing Numbers of Prattayan Pattra Issued to Customers in "+Month.values()[Integer.valueOf(issue_month)-1]+" "+issue_year+" Against The Calendar Year-"+calender_year;
									
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
			
			
			customerList=getCustomerList();
			ppIssuedCurrMonth=getPpIssuedCurrMonth();
			ppIssuedLastMonth=getPpIssuedLastMonth();
			withDues=getDuesStatus("WDUES"); //with dues
			withoutDues=getDuesStatus("WODUES"); //with dues
			custInfo=getBurnerQuantity();
			
			
			
			
			
			
			
			
			PdfPTable jvTable = new PdfPTable(10);
			jvTable.setWidthPercentage(100);
			jvTable.setWidths(new float[]{4,16,10,10,10,10,10,10,10,10});
			jvTable.setSpacingBefore(15f);
			
			pcell = new PdfPCell(new Paragraph("SI. No",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Category of Customer",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Number of Connection's",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Number of Total Customer",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Number of Prattayan Pattra Issued upto last Month",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Number of Prattayan Pattra Issued  During The Month",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Number of Prattayan Pattra Issued  without Dues",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Number of Prattayan Pattra Issued  with Dues",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Number of Prattayan Pattra Issued ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Total Number of Prattayan Pattra to be Issued",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("2",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("3",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("4",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("5",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("6",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("7",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("8",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("9=(7+8)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("10=(4-9)",ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*----------------------------Power Customer Start---------------------------*/
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Power",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			
			int totalPowerCustomer=(customerList.containsKey("121")?customerList.get("121"):0)+(customerList.containsKey("111")?customerList.get("111"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalPowerCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalPowerCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int powerLastMonPp=(ppIssuedLastMonth.containsKey("121")?ppIssuedLastMonth.get("121"):0)+(ppIssuedLastMonth.containsKey("111")?ppIssuedLastMonth.get("111"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(powerLastMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int powerCurrMonPp=(ppIssuedCurrMonth.containsKey("121")?ppIssuedCurrMonth.get("121"):0)+(ppIssuedCurrMonth.containsKey("111")?ppIssuedCurrMonth.get("111"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(powerCurrMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int powerWithoutDues=(withoutDues.containsKey("121")?withoutDues.get("121"):0)+(withoutDues.containsKey("111")?withoutDues.get("111"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(powerWithoutDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int powerWithDues=(withDues.containsKey("121")?withDues.get("121"):0)+(withDues.containsKey("111")?withDues.get("111"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(powerWithDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int powertotalPpIssued=powerWithoutDues+powerWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(powertotalPpIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int powerTotalPp2bIssued=totalPowerCustomer-powertotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(powerTotalPp2bIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*--------------------------CNG Power Customer-------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("2",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("CNG",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			int totalCngCustomer=(customerList.containsKey("101")?customerList.get("101"):0)+(customerList.containsKey("091")?customerList.get("091"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCngCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCngCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int cngLastMonPp=(ppIssuedLastMonth.containsKey("101")?ppIssuedLastMonth.get("101"):0)+(ppIssuedLastMonth.containsKey("091")?ppIssuedLastMonth.get("091"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(cngLastMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int cngCurrMonPp=(ppIssuedCurrMonth.containsKey("101")?ppIssuedCurrMonth.get("101"):0)+(ppIssuedCurrMonth.containsKey("091")?ppIssuedCurrMonth.get("091"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(cngCurrMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int cngWithoutDues=(withoutDues.containsKey("101")?withoutDues.get("101"):0)+(withoutDues.containsKey("091")?withoutDues.get("091"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(cngWithoutDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int cngWithDues=(withDues.containsKey("101")?withDues.get("101"):0)+(withDues.containsKey("091")?withDues.get("091"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(cngWithDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int cngtotalPpIssued=cngWithoutDues+cngWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(cngtotalPpIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int cngTotalPp2bIssued=totalCngCustomer-cngtotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(cngTotalPp2bIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*--------------------------Captive Customer-------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("3",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Captive",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			
			int totalCaptiveCustomer=(customerList.containsKey("081")?customerList.get("081"):0)+(customerList.containsKey("071")?customerList.get("071"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCaptiveCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCaptiveCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int captiveLastMonPp=(ppIssuedLastMonth.containsKey("081")?ppIssuedLastMonth.get("081"):0)+(ppIssuedLastMonth.containsKey("071")?ppIssuedLastMonth.get("071"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(captiveLastMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int captiveCurrMonPp=(ppIssuedCurrMonth.containsKey("081")?ppIssuedCurrMonth.get("081"):0)+(ppIssuedCurrMonth.containsKey("071")?ppIssuedCurrMonth.get("071"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(captiveCurrMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int captiveWithoutDues=(withoutDues.containsKey("081")?withoutDues.get("081"):0)+(withoutDues.containsKey("071")?withoutDues.get("071"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(captiveWithoutDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int captiveWithDues=(withDues.containsKey("081")?withDues.get("081"):0)+(withDues.containsKey("071")?withDues.get("071"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(captiveWithDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int captivetotalPpIssued=captiveWithoutDues+captiveWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(captivetotalPpIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int captiveTotalPp2bIssued=totalCaptiveCustomer-captivetotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(captiveTotalPp2bIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*--------------------------Industrial Customer-------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("4",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("Industrial",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			
			int totalIndustrialCustomer=(customerList.containsKey("061")?customerList.get("061"):0)+(customerList.containsKey("051")?customerList.get("051"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalIndustrialCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalIndustrialCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int IndustrialLastMonPp=(ppIssuedLastMonth.containsKey("061")?ppIssuedLastMonth.get("061"):0)+(ppIssuedLastMonth.containsKey("051")?ppIssuedLastMonth.get("051"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(IndustrialLastMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int IndustrialCurrMonPp=(ppIssuedCurrMonth.containsKey("061")?ppIssuedCurrMonth.get("061"):0)+(ppIssuedCurrMonth.containsKey("051")?ppIssuedCurrMonth.get("051"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(IndustrialCurrMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int IndustrialrWithoutDues=(withoutDues.containsKey("061")?withoutDues.get("061"):0)+(withoutDues.containsKey("051")?withoutDues.get("051"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(IndustrialrWithoutDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int IndustrialWithDues=(withDues.containsKey("061")?withDues.get("061"):0)+(withDues.containsKey("051")?withDues.get("051"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(IndustrialWithDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int industrialtotalPpIssued=IndustrialrWithoutDues+IndustrialWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(industrialtotalPpIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int industrialTotalPp2bIssued=totalIndustrialCustomer-industrialtotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(industrialTotalPp2bIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*--------------------------Commercial Customer-------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("5",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Commercial",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			int totalCommercialCustomer=(customerList.containsKey("041")?customerList.get("041"):0)+(customerList.containsKey("031")?customerList.get("031"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCommercialCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCommercialCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int CommercialLastMonPp=(ppIssuedLastMonth.containsKey("041")?ppIssuedLastMonth.get("041"):0)+(ppIssuedLastMonth.containsKey("031")?ppIssuedLastMonth.get("031"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(CommercialLastMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int CommercialCurrMonPp=(ppIssuedCurrMonth.containsKey("041")?ppIssuedCurrMonth.get("041"):0)+(ppIssuedCurrMonth.containsKey("031")?ppIssuedCurrMonth.get("031"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(CommercialCurrMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			
			int CommercialWithoutDues=(withoutDues.containsKey("041")?withoutDues.get("041"):0)+(withoutDues.containsKey("031")?withoutDues.get("031"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(CommercialWithoutDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int CommercialWithDues=(withDues.containsKey("041")?withDues.get("041"):0)+(withDues.containsKey("031")?withDues.get("031"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(CommercialWithDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int commercialtotalPpIssued=CommercialWithoutDues+CommercialWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(commercialtotalPpIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int commercialTotalPp2bIssued=totalCommercialCustomer-commercialtotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(commercialTotalPp2bIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			

			
			/*--------------------------Domestic Customer----------will be blank---------------------*/
			
			pcell = new PdfPCell(new Paragraph("7",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("Domestic",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*--------------------------Metered Customer-------------------------------*/
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(a)Metered(Metered)",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			
			int totalDomMeterCustomer=(customerList.containsKey("021")?customerList.get("021"):0)+(customerList.containsKey("011")?customerList.get("011"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalDomMeterCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalDomMeterCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomMeterLastMonPp=(ppIssuedLastMonth.containsKey("021")?ppIssuedLastMonth.get("021"):0)+(ppIssuedLastMonth.containsKey("011")?ppIssuedLastMonth.get("011"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomMeterLastMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			
			int DomMeterCurrMonPp=(ppIssuedCurrMonth.containsKey("021")?ppIssuedCurrMonth.get("021"):0)+(ppIssuedCurrMonth.containsKey("011")?ppIssuedCurrMonth.get("011"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomMeterCurrMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomMeterWithoutDues=(withoutDues.containsKey("021")?withoutDues.get("021"):0)+(withoutDues.containsKey("011")?withoutDues.get("011"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomMeterWithoutDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomMeterWithDues=(withDues.containsKey("021")?withDues.get("021"):0)+(withDues.containsKey("011")?withDues.get("011"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomMeterWithDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomMetertotalPpIssued=DomMeterWithoutDues+DomMeterWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomMetertotalPpIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomMeterTotalPp2bIssued=totalDomMeterCustomer-DomMetertotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomMeterTotalPp2bIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			/*--------------------------Double Burner Customer-------------------------------*/
			
			int totalDomNonMeterCustomer=(customerList.containsKey("020")?customerList.get("020"):0)+(customerList.containsKey("010")?customerList.get("010"):0);
			
			double totalburner=custInfo.get(0).getNumberOfCustomer();
			String burner=Double.toString(totalburner);
			int totalBurnerQty=(int)(totalburner);
			
			
			pcell = new PdfPCell(new Paragraph("1",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("(b)Double Burner",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			jvTable.addCell(pcell);
			
			//String totalDomNonMeterCustomer=custInfo.get(0).getCustomerId();
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalBurnerQty),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalDomNonMeterCustomer),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomNonMeterLastMonPp=(ppIssuedLastMonth.containsKey("020")?ppIssuedLastMonth.get("020"):0)+(ppIssuedLastMonth.containsKey("010")?ppIssuedLastMonth.get("010"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomNonMeterLastMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomNonMeterCurrMonPp=(ppIssuedCurrMonth.containsKey("020")?ppIssuedCurrMonth.get("020"):0)+(ppIssuedCurrMonth.containsKey("010")?ppIssuedCurrMonth.get("010"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomNonMeterCurrMonPp),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomNonMeterWithoutDues=(withoutDues.containsKey("020")?withoutDues.get("020"):0)+(withoutDues.containsKey("010")?withoutDues.get("010"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomNonMeterWithoutDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomNonMeterWithDues=(withDues.containsKey("020")?withDues.get("020"):0)+(withDues.containsKey("010")?withDues.get("010"):0);
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomNonMeterWithDues),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomNonMetertotalPpIssued=DomNonMeterWithoutDues+DomNonMeterWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomNonMetertotalPpIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int DomNonMeterTotalPp2bIssued=totalDomNonMeterCustomer-DomNonMetertotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(DomNonMeterTotalPp2bIssued),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			

			
			/*--------------------------Total-----------------------------------*/
					
			pcell = new PdfPCell(new Paragraph("Total Customer(1+...+8)= ",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			jvTable.addCell(pcell);
			
			int totalConnection=totalPowerCustomer+totalCngCustomer+totalCaptiveCustomer+totalIndustrialCustomer+totalCommercialCustomer+totalDomMeterCustomer+totalBurnerQty;
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalConnection),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int totalCustomer=totalPowerCustomer+totalCngCustomer+totalCaptiveCustomer+totalIndustrialCustomer+totalCommercialCustomer+totalDomMeterCustomer+totalDomNonMeterCustomer;
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCustomer),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int totalLastMonPp=powerLastMonPp+cngLastMonPp+captiveLastMonPp+IndustrialLastMonPp+CommercialLastMonPp+DomMeterLastMonPp+DomNonMeterLastMonPp;
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalLastMonPp),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int totalCurrMonPp=powerCurrMonPp+cngCurrMonPp+captiveCurrMonPp+IndustrialCurrMonPp+CommercialCurrMonPp+DomMeterCurrMonPp+DomNonMeterCurrMonPp;
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalCurrMonPp),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int totalWithoutDues=powerWithoutDues+cngWithoutDues+captiveWithoutDues+IndustrialrWithoutDues+CommercialWithoutDues+DomMeterWithoutDues+DomNonMeterWithoutDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalWithoutDues),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int totalWithDues=powerWithDues+cngWithDues+captiveWithDues+IndustrialWithDues+CommercialWithDues+DomMeterWithDues+DomNonMeterWithDues;
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalWithDues),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			int totalPpIssued=powertotalPpIssued+cngtotalPpIssued+captivetotalPpIssued+industrialtotalPpIssued+commercialtotalPpIssued+DomMetertotalPpIssued+DomNonMetertotalPpIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalPpIssued),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			int totalPp2bIssued=powerTotalPp2bIssued+cngTotalPp2bIssued+captiveTotalPp2bIssued+industrialTotalPp2bIssued+commercialTotalPp2bIssued+DomMeterTotalPp2bIssued+DomNonMeterTotalPp2bIssued;
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalPp2bIssued),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
			/*--------------------------Last Total----------------------*/
					
			pcell = new PdfPCell(new Paragraph("Percentage (%) of Prattayan Pattra Issued = ",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(4);
			jvTable.addCell(pcell);
			
			
			float ppIssuedLastMonthPercentage=(float)totalLastMonPp/totalCustomer;
			pcell = new PdfPCell(new Paragraph(taka_format.format(ppIssuedLastMonthPercentage*100),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			float ppIssuedCurrMonthPercentage=(float)totalCurrMonPp/totalCustomer;
			pcell = new PdfPCell(new Paragraph(taka_format.format(ppIssuedCurrMonthPercentage*100),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			float withOutDuesPercentage=(float)totalWithoutDues/totalCustomer;
			pcell = new PdfPCell(new Paragraph(taka_format.format((withOutDuesPercentage)*100),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			float withDuesPercentage=(float)totalWithDues/totalCustomer;
			pcell = new PdfPCell(new Paragraph(taka_format.format(withDuesPercentage*100),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			float totalPpIssuedpercentage=(float)totalPpIssued/totalCustomer;
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPpIssuedpercentage*100),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			float totalPp2bIssuedPercentage=(float)totalPp2bIssued/totalCustomer;
			pcell = new PdfPCell(new Paragraph(taka_format.format(totalPp2bIssuedPercentage*100),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			jvTable.addCell(pcell);
			
			
		
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
			

	private HashMap<String, Integer> getCustomerList(){
		
		 HashMap<String, Integer> cusList = new HashMap<String, Integer>();
	
		
		try {
			String transaction_sql=	"select aa.CATEGORY_ID,NVL(bb.COUNT,0) COUNT,NVL(BB.ISMETERED,1) ISMETERED from mst_customer_category aa,("+
									"select CATEGORY_ID,ISMETERED,count(CUSTOMER_ID) COUNT from helper_clarification where substr(customer_id,1,2)=?"+
									" group by CATEGORY_ID,ismetered) bb"+
									" where aa.CATEGORY_ID=bb.CATEGORY_ID(+)"+
									" order by  aa.CATEGORY_ID desc,ISMETERED desc";


					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
			ps1.setString(1, area);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{

        		String category=resultSet.getString("CATEGORY_ID");
        		String isMeter=resultSet.getString("ISMETERED");
        		String sKey=category+isMeter;
        		cusList.put(sKey,Integer.valueOf(resultSet.getString("COUNT")));
      
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return cusList;
	}
	
	
	private  HashMap<String, Integer> getPpIssuedCurrMonth(){
		
	    
	    HashMap<String, Integer> ppIssuedLatMonth = new HashMap<String, Integer>();
		
		try {
			String transaction_sql=	"SELECT aa.CATEGORY_ID, NVL (bb.COUNT, 0) COUNT, NVL (ISMETERED, 1) ISMETERED "+
									 " FROM mst_customer_category aa, "+
									   "   (  SELECT CATEGORY_ID,ISMETERED, COUNT (CUSTOMER_ID) COUNT "+
									          "  FROM (SELECT CH.CUSTOMER_ID, SUBSTR (CH.CUSTOMER_ID, 3, 2) CATEGORY_ID,CC.ISMETERED "+
									            "        FROM CLARIFICATION_HISTORY ch,customer_connection cc "+
									           "        WHERE CH.CUSTOMER_ID=cc.customer_id "+
									           "        AND      "+
									                 "  CALENDER_YEAR="+calender_year+"  AND TO_CHAR (ISSUE_DATE, 'MM') ="+issue_month+""+
									                  "  AND TO_CHAR (ISSUE_DATE, 'yyyy') ="+issue_year+" AND SUBSTR (CH.CUSTOMER_ID, 1, 2)="+area+")"+
									       " GROUP BY CATEGORY_ID,ISMETERED) bb "+
									"  WHERE aa.CATEGORY_ID = bb.CATEGORY_ID(+) "+
									"  order by  aa.CATEGORY_ID desc,ISMETERED desc ";
 


					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		/*	ps1.setString(1, calender_year);
			ps1.setString(2, issue_month);
			ps1.setString(3, issue_year);
			ps1.setString(4, area);
		*/
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		String category=resultSet.getString("CATEGORY_ID");
        		String isMeter=resultSet.getString("ISMETERED");
        		String sKey=category+isMeter;
        		ppIssuedLatMonth.put(sKey,Integer.valueOf(resultSet.getString("COUNT")));
      
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return ppIssuedLatMonth;
	}
	
	
	
	private  HashMap<String, Integer> getPpIssuedLastMonth(){
		
	    
	    HashMap<String, Integer> ppIssuedLatMonth = new HashMap<String, Integer>();
		
		try {
			
			
			if(issue_month.equals("1")){
				ppIssuedLatMonth.put("121",0);
				ppIssuedLatMonth.put("111",0);
				ppIssuedLatMonth.put("101",0);
				ppIssuedLatMonth.put("091",0);
				ppIssuedLatMonth.put("081",0);
				ppIssuedLatMonth.put("071",0);
				ppIssuedLatMonth.put("061",0);
				ppIssuedLatMonth.put("051",0);
				ppIssuedLatMonth.put("041",0);
				ppIssuedLatMonth.put("031",0);
				ppIssuedLatMonth.put("021",0);
				ppIssuedLatMonth.put("011",0);
				ppIssuedLatMonth.put("020",0);
				ppIssuedLatMonth.put("010",0);
				return ppIssuedLatMonth;
			}
			
			String from_con=issue_year+"01";	
			String to_con=issue_month.equals("01")?issue_year+"01":issue_year+"00".substring(String.valueOf(Integer.valueOf(issue_month)-1).length())+String.valueOf(Integer.valueOf(issue_month)-1);
			
			String transaction_sql=	"SELECT aa.CATEGORY_ID, NVL (bb.COUNT, 0) COUNT, NVL (ISMETERED, 1) ISMETERED "+
									 " FROM mst_customer_category aa, "+
									   "   (  SELECT CATEGORY_ID,ISMETERED, COUNT (CUSTOMER_ID) COUNT "+
									          "  FROM (SELECT CH.CUSTOMER_ID, SUBSTR (CH.CUSTOMER_ID, 3, 2) CATEGORY_ID,CC.ISMETERED "+
									            "        FROM CLARIFICATION_HISTORY ch,customer_connection cc "+
									           "        WHERE CH.CUSTOMER_ID=cc.customer_id "+
									                 "   AND to_char(ISSUE_DATE,'yyyy') || LPAD (to_char(ISSUE_DATE,'MM'), 2, 0) BETWEEN ? AND ?"+
									                  " AND SUBSTR (CH.CUSTOMER_ID, 1, 2)=?)"+
									       " GROUP BY CATEGORY_ID,ISMETERED) bb "+
									"  WHERE aa.CATEGORY_ID = bb.CATEGORY_ID(+) "+
									"  order by  aa.CATEGORY_ID desc,ISMETERED desc ";
 


					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
			ps1.setString(1, from_con);
			ps1.setString(2, to_con);
			ps1.setString(3, area);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		String category=resultSet.getString("CATEGORY_ID");
        		String isMeter=resultSet.getString("ISMETERED");
        		String sKey=category+isMeter;
        		ppIssuedLatMonth.put(sKey,Integer.valueOf(resultSet.getString("COUNT")));
      
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return ppIssuedLatMonth;
	}
	
	
	
	private  HashMap<String, Integer> getDuesStatus(String status){
		
	    
	    HashMap<String, Integer> ppIssuedLatMonth = new HashMap<String, Integer>();
	    String wClause="";
	    if(status.equals("WDUES")){
	    	wClause="And DUES_STATUS=0";			
	    }else if(status.equals("WODUES")){
	    	wClause="And DUES_STATUS=1";
	    }
	    
	
		String from_con=issue_year+"01";
		String to_con="";
		if(issue_month.equals("01")){
			to_con=issue_year+"01";
		}else{
			to_con=issue_year+"00".substring(String.valueOf(Integer.valueOf(issue_month)).length())+String.valueOf(Integer.valueOf(issue_month));
		}
		
		try {
			String transaction_sql=	"SELECT aa.CATEGORY_ID, NVL (bb.COUNT, 0) COUNT, NVL (ISMETERED, 1) ISMETERED "+
									 " FROM mst_customer_category aa, "+
									   "   (  SELECT CATEGORY_ID,ISMETERED, COUNT (CUSTOMER_ID) COUNT "+
									          "  FROM (SELECT CH.CUSTOMER_ID, SUBSTR (CH.CUSTOMER_ID, 3, 2) CATEGORY_ID,CC.ISMETERED "+
									            "        FROM CLARIFICATION_HISTORY ch,customer_connection cc "+
									           "        WHERE CH.CUSTOMER_ID=cc.customer_id "+        
                                         "   AND to_char(ISSUE_DATE,'yyyy') || LPAD (to_char(ISSUE_DATE,'MM'), 2, 0) BETWEEN ? AND ?"+wClause+  
									                  " AND SUBSTR (CH.CUSTOMER_ID, 1, 2)=?)"+
									       " GROUP BY CATEGORY_ID,ISMETERED) bb "+
									"  WHERE aa.CATEGORY_ID = bb.CATEGORY_ID(+) "+
									"  order by  aa.CATEGORY_ID desc,ISMETERED desc ";
 


					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
			ps1.setString(1, from_con);
			ps1.setString(2, to_con);
			ps1.setString(3, area);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		String category=resultSet.getString("CATEGORY_ID");
        		String isMeter=resultSet.getString("ISMETERED");
        		String sKey=category+isMeter;
        		ppIssuedLatMonth.put(sKey,Integer.valueOf(resultSet.getString("COUNT")));
      
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return ppIssuedLatMonth;
	}
	
	
	private ArrayList<CustomerListDTO> getBurnerQuantity(){
		ArrayList<CustomerListDTO> customerinfo= new ArrayList<CustomerListDTO>();
	    
		String area=loggedInUser.getArea_id();
		
		try {
			String transaction_sql=	"select count(CUSTOMER_ID) CUSTOMER_ID,sum(NEW_DOUBLE_BURNER_QNT_BILLCAL) NEW_DOUBLE_BURNER_QNT_BILLCAL,sum(NEW_DOUBLE_BURNER_QNT) NEW_DOUBLE_BURNER_QNT from( " +
					"select MCI.CUSTOMER_ID CUSTOMER_ID,NEW_DOUBLE_BURNER_QNT_BILLCAL,NEW_DOUBLE_BURNER_QNT  " +
					"from MVIEW_CUSTOMER_INFO MCI,  " +
					"(select CUSTOMER_ID, NEW_DOUBLE_BURNER_QNT,NEW_DOUBLE_BURNER_QNT_BILLCAL  from BURNER_QNT_CHANGE where PID IN(  " +
					"select max(PID) from BURNER_QNT_CHANGE where SUBSTR(customer_id,1,2)='"+area+"' and  effective_date<=LAST_DAY (TO_DATE (12 || '-' || "+calender_year+", 'MM-YYYY'))   " +
					"GROUP BY CUSTOMER_ID)) tab1  " +
					"WHERE TAB1.CUSTOMER_ID = MCI.CUSTOMER_ID  " +
					"AND SUBSTR(MCI.CUSTOMER_ID,1,2)='"+area+"'  " +
					"AND MCI.ISMETERED=0  " +
					"AND NEW_DOUBLE_BURNER_QNT_BILLCAL<>0) "  ;

					
					
					
					
					
					
					
//					
//					"select SUM(NEW_DOUBLE_BURNER_QNT+(NEW_DOUBLE_BURNER_QNT_BILLCAL-NEW_DOUBLE_BURNER_QNT)*2) COUNT from burner_qnt_change "+
//										" where pid in"+	
//										"(select MAX(PID) from burner_qnt_change "+
//										"Where substr(CUSTOMER_ID,1,2)=?"+
//										"group by CUSTOMER_ID)"+
//										"and to_char(EFFECTIVE_DATE,'yyyy')<?";
									
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);		
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		customerListDTO = new CustomerListDTO();
        		
        		customerListDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		customerListDTO.setNumberOfCustomer(resultSet.getDouble("NEW_DOUBLE_BURNER_QNT"));
        		
        		customerinfo.add(customerListDTO);
      
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return customerinfo;
		
		
		
		
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



	public String getCalender_year() {
		return calender_year;
	}



	public void setCalender_year(String calender_year) {
		this.calender_year = calender_year;
	}



	public String getIssue_month() {
		return issue_month;
	}



	public void setIssue_month(String issue_month) {
		this.issue_month = issue_month;
	}



	public String getIssue_year() {
		return issue_year;
	}



	public void setIssue_year(String issue_year) {
		this.issue_year = issue_year;
	}



	
	
  }




package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


import javax.servlet.ServletContext;


import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.SalesDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;



import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class MonthlySalesSummary extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>salesSummaryList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>baghabariSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>pabnaSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>ishwardiSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>bograSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>shahajadpurSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>beraSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>santhiaSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>ullahparaSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>rajshahiSalesList = new ArrayList<SalesDTO>();
	ArrayList<SalesDTO>summarySalesList = new ArrayList<SalesDTO>();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for;
	    //
	    private  String area="24";
	    private  String[] areaList={"10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	    ArrayList<String> areaListAll = new ArrayList<String>(Arrays.asList(areaList));
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
		
		
		
		
	public String execute() throws Exception
	{
				
		String fileName="Sales_Summary_Report.pdf";
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

			pcell=new PdfPCell(new Paragraph("Revenue Section : ", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);
			

			pcell=new PdfPCell(new Paragraph("Sales Statement for the Month of "+ Month.values()[Integer.valueOf(bill_month)-1]+","+bill_year, ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);					
			
			document.add(mTable);
			
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable1 = new PdfPTable(13);
			
			datatable1.setWidthPercentage(100);
			datatable1.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);
			
			
			
			String currentCategory="";
			String previousCategory="";
			String currentCategoryID="";
			
			double actualExceptMin=0.0;
			double actualCunsumption=0.0;
			double billingUnit=0.0;
			double difference = 0.0;
			double totalActualCons=0.0;
			double valueOfTotalActualCons=0.0;
			double minimumCharge=0.0;
			double meterRent=0.0;
			double nHVhHV=0.0;
			double totalBillAmount=0.0;
			
			double gvtActualExceptMin=0.0;
			double gvtActualCunsumption=0.0;
			double gvtBillingUnit=0.0;
			double gvtDifference = 0.0;
			double gvtTotalActualCons=0.0;
			double gvtValueOfTotalActualCons=0.0;
			double gvtMinimumCharge=0.0;
			double gvtMeterRent=0.0;
			double gvtnHVhHV=0.0;
			double gvtTotalBillAmount=0.0;
			
			double dgvtActualExceptMin=0.0;
			double dgvtActualCunsumption=0.0;
			double dgvtBillingUnit=0.0;
			double dgvtDifference = 0.0;
			double dgvtTotalActualCons=0.0;
			double dgvtValueOfTotalActualCons=0.0;
			double dgvtMinimumCharge=0.0;
			double dgvtMeterRent=0.0;
			double dgvtnHVhHV=0.0;
			double dgvtTotalBillAmount=0.0;
			
			double pvtActualExceptMin=0.0;
			double pvtActualCunsumption=0.0;
			double pvtBillingUnit=0.0;
			double pvtDifference = 0.0;
			double pvtTotalActualCons=0.0;
			double pvtValueOfTotalActualCons=0.0;
			double pvtMinimumCharge=0.0;
			double pvtMeterRent=0.0;
			double pvtnHVhHV=0.0;
			double pvtTotalBillAmount=0.0;
			
			double dpvtActualExceptMin=0.0;
			double dpvtActualCunsumption=0.0;
			double dpvtBillingUnit=0.0;
			double dpvtDifference = 0.0;
			double dpvtTotalActualCons=0.0;
			double dpvtValueOfTotalActualCons=0.0;
			double dpvtMinimumCharge=0.0;
			double dpvtMeterRent=0.0;
			double dpvtnHVhHV=0.0;
			double dpvtTotalBillAmount=0.0;
			
			
			sirajSalesList = getAreaWiseSalesSirajganj();
// sirajSalesList contains all the necessary data needed for "customer type wise sales statement for the month of"
// except "Average unit against disorder"  ~comment made on Oct 9 ~ Prince
			int listSize = sirajSalesList.size();
			
			for (int i = 0; i < listSize; i++) {
				
				currentCategoryID = sirajSalesList.get(i).getCategoryID();
				currentCategory=sirajSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable1.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("SYLHET NORTH",font3));
					pcell.setColspan(13);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable1.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+sirajSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+sirajSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+sirajSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+sirajSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+sirajSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+sirajSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+sirajSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+sirajSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+sirajSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + sirajSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+sirajSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+sirajSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+sirajSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+sirajSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+sirajSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+sirajSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+sirajSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+sirajSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+sirajSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + sirajSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+sirajSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+sirajSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+sirajSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+sirajSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+sirajSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+sirajSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+sirajSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+sirajSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+sirajSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + sirajSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+sirajSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+sirajSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+sirajSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+sirajSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+sirajSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+sirajSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+sirajSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+sirajSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+sirajSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + sirajSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable1.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable1.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(sirajSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable1.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(sirajSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				actualExceptMin = actualExceptMin +sirajSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(sirajSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				actualCunsumption = actualCunsumption+sirajSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(sirajSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				billingUnit = billingUnit+sirajSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(sirajSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				difference = difference+sirajSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(sirajSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				totalActualCons = totalActualCons+sirajSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(sirajSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(sirajSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+sirajSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(sirajSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				minimumCharge = minimumCharge+sirajSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(sirajSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				meterRent = meterRent+sirajSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(sirajSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				nHVhHV = nHVhHV+sirajSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(sirajSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable1.addCell(pcell);
				
				totalBillAmount = totalBillAmount + sirajSalesList.get(i).getTotalBillAmount();
				
				previousCategory = sirajSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of NORTH(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable1.addCell(pcell);
			
			document.add(datatable1);
			///////////////////////////////////////////////////////////////////////////////////////////////////
			
			document.newPage();
			
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable2 = new PdfPTable(13);			
			datatable2.setWidthPercentage(100);
			datatable2.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable2.addCell(pcell);
			
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*-------------------------END OF SIRAJGANJ---------------------------------------------------*/
			
			/*--------------------------------- Sales Baghabari-------------------------------------------*/
			
			baghabariSalesList = getAreaWiseSalesBaghabari();
			int listSizeBaghabari = baghabariSalesList.size();
			
			for (int i = 0; i < listSizeBaghabari; i++) {
				
				currentCategoryID = baghabariSalesList.get(i).getCategoryID();
				currentCategory=baghabariSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable1.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("BAGHABARI AREA",font3));
					pcell.setColspan(13);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable2.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+baghabariSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+baghabariSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+baghabariSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+baghabariSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+baghabariSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+baghabariSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+baghabariSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+baghabariSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+baghabariSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + baghabariSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+baghabariSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+baghabariSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+baghabariSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+baghabariSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+baghabariSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+baghabariSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+baghabariSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+baghabariSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+baghabariSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + baghabariSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+baghabariSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+baghabariSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+baghabariSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+baghabariSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+baghabariSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+baghabariSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+baghabariSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+baghabariSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+baghabariSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + baghabariSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+baghabariSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+baghabariSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+baghabariSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+baghabariSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+baghabariSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+baghabariSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+baghabariSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+baghabariSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+baghabariSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + baghabariSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable2.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable2.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable2.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(baghabariSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable2.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(baghabariSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				actualExceptMin = actualExceptMin +baghabariSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(baghabariSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				actualCunsumption = actualCunsumption+baghabariSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(baghabariSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				billingUnit = billingUnit+baghabariSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(baghabariSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				difference = difference+baghabariSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(baghabariSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				totalActualCons = totalActualCons+baghabariSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(baghabariSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(baghabariSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+baghabariSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(baghabariSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				minimumCharge = minimumCharge+baghabariSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(baghabariSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				meterRent = meterRent+baghabariSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(baghabariSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				nHVhHV = nHVhHV+baghabariSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(baghabariSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable2.addCell(pcell);
				
				totalBillAmount = totalBillAmount + baghabariSalesList.get(i).getTotalBillAmount();
				
				previousCategory = baghabariSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of BAGHABARI(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable2.addCell(pcell);
			
			document.add(datatable2);
			
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable3 = new PdfPTable(13);			
			datatable3.setWidthPercentage(100);
			datatable3.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable3.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*--------------------------------- Sales Baghabari End---------------------------------------*/
			
			/*--------------------------------- Sales Pabna-----------------------------------------------*/
			
			pabnaSalesList = getAreaWiseSalesPabna();
			int listSizepabna = pabnaSalesList.size();
			
			for (int i = 0; i < listSizepabna; i++) {
				
				currentCategoryID = pabnaSalesList.get(i).getCategoryID();
				currentCategory=pabnaSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable3.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("PABNA AREA",font3));
					pcell.setColspan(13);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable3.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+pabnaSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+pabnaSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+pabnaSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+pabnaSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+pabnaSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+pabnaSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+pabnaSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+pabnaSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+pabnaSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + pabnaSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+pabnaSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+pabnaSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+pabnaSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+pabnaSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+pabnaSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+pabnaSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+pabnaSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+pabnaSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+pabnaSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + pabnaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+pabnaSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+pabnaSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+pabnaSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+pabnaSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+pabnaSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+pabnaSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+pabnaSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+pabnaSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+pabnaSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + pabnaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+pabnaSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+pabnaSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+pabnaSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+pabnaSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+pabnaSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+pabnaSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+pabnaSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+pabnaSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+pabnaSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + pabnaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable3.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable3.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(pabnaSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable3.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(pabnaSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				actualExceptMin = actualExceptMin +pabnaSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(pabnaSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				actualCunsumption = actualCunsumption+pabnaSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(pabnaSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				billingUnit = billingUnit+pabnaSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(pabnaSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				difference = difference+pabnaSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(pabnaSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				totalActualCons = totalActualCons+pabnaSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(pabnaSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(pabnaSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+pabnaSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(pabnaSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				minimumCharge = minimumCharge+pabnaSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(pabnaSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				meterRent = meterRent+pabnaSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(pabnaSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				nHVhHV = nHVhHV+pabnaSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(pabnaSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable3.addCell(pcell);
				
				totalBillAmount = totalBillAmount + pabnaSalesList.get(i).getTotalBillAmount();
				
				previousCategory = pabnaSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of PABNA(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable3.addCell(pcell);
			
			document.add(datatable3);
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable4 = new PdfPTable(13);			
			datatable4.setWidthPercentage(100);
			datatable4.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable4.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*--------------------------------- Sales Pabna End-------------------------------------------*/
			
			/*--------------------------------- Sales Ishwardi--------------------------------------------*/
			
			ishwardiSalesList= getAreaWiseSalesIshwardi();
			int listSizeish = ishwardiSalesList.size();
			
			for (int i = 0; i < listSizeish; i++) {
				
				currentCategoryID = ishwardiSalesList.get(i).getCategoryID();
				currentCategory=ishwardiSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable4.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("ISHWARDI AREA",font3));
					pcell.setColspan(13);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable4.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+ishwardiSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+ishwardiSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+ishwardiSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+ishwardiSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+ishwardiSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+ishwardiSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+ishwardiSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+ishwardiSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+ishwardiSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + ishwardiSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+ishwardiSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+ishwardiSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+ishwardiSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+ishwardiSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+ishwardiSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+ishwardiSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+ishwardiSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+ishwardiSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+ishwardiSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + ishwardiSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+ishwardiSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+ishwardiSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+ishwardiSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+ishwardiSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+ishwardiSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+ishwardiSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+ishwardiSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+ishwardiSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+ishwardiSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + ishwardiSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+ishwardiSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+ishwardiSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+ishwardiSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+ishwardiSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+ishwardiSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+ishwardiSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+ishwardiSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+ishwardiSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+ishwardiSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + ishwardiSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable4.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable4.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(ishwardiSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable4.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ishwardiSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				actualExceptMin = actualExceptMin +ishwardiSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ishwardiSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				actualCunsumption = actualCunsumption+ishwardiSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ishwardiSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				billingUnit = billingUnit+ishwardiSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ishwardiSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				difference = difference+ishwardiSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ishwardiSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				totalActualCons = totalActualCons+ishwardiSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ishwardiSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ishwardiSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+ishwardiSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ishwardiSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				minimumCharge = minimumCharge+ishwardiSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ishwardiSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				meterRent = meterRent+ishwardiSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ishwardiSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				nHVhHV = nHVhHV+ishwardiSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ishwardiSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable4.addCell(pcell);
				
				totalBillAmount = totalBillAmount + ishwardiSalesList.get(i).getTotalBillAmount();
				
				previousCategory = ishwardiSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of ISHWARDI(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable4.addCell(pcell);
			
			document.add(datatable4);
			
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable5 = new PdfPTable(13);			
			datatable5.setWidthPercentage(100);
			datatable5.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable5.addCell(pcell);
			
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*--------------------------------- Sales Ishwardi End----------------------------------------*/
			
			/*--------------------------------- Sales Bogra-----------------------------------------------*/
			
			bograSalesList= getAreaWiseSalesBogra();
			int listSizebogra = bograSalesList.size();
			
			for (int i = 0; i < listSizebogra; i++) {
				
				currentCategoryID = bograSalesList.get(i).getCategoryID();
				currentCategory=bograSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable5.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("BOGRA AREA",font3));
					pcell.setColspan(13);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable5.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+bograSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+bograSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+bograSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+bograSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+bograSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+bograSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+bograSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+bograSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+bograSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + bograSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+bograSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+bograSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+bograSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+bograSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+bograSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+bograSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+bograSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+bograSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+bograSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + bograSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+bograSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+bograSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+bograSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+bograSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+bograSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+bograSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+bograSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+bograSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+bograSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + bograSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+bograSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+bograSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+bograSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+bograSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+bograSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+bograSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+bograSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+bograSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+bograSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + bograSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable5.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable5.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable5.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(bograSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable5.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(bograSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				actualExceptMin = actualExceptMin +bograSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(bograSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				actualCunsumption = actualCunsumption+bograSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(bograSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				billingUnit = billingUnit+bograSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(bograSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				difference = difference+bograSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(bograSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				totalActualCons = totalActualCons+bograSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(bograSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(bograSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+bograSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(bograSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				minimumCharge = minimumCharge+bograSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(bograSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				meterRent = meterRent+bograSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(bograSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				nHVhHV = nHVhHV+bograSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(bograSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable5.addCell(pcell);
				
				totalBillAmount = totalBillAmount + bograSalesList.get(i).getTotalBillAmount();
				
				previousCategory = bograSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of BOGRA(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable5.addCell(pcell);
			
			document.add(datatable5);
			
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable6 = new PdfPTable(13);			
			datatable6.setWidthPercentage(100);
			datatable6.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable6.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*--------------------------------- Sales Bogra End-------------------------------------------*/
			
			/*---------------------------------Sales of Shahajadpur---------------------------------------*/
		
			shahajadpurSalesList= getAreaWiseSalesShahajadpur();
			int listSizeshahajadpur = shahajadpurSalesList.size();
			
			for (int i = 0; i < listSizeshahajadpur; i++) {
				
				currentCategoryID = shahajadpurSalesList.get(i).getCategoryID();
				currentCategory=shahajadpurSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable6.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("SHAHAJADPUR AREA",font3));
					pcell.setColspan(13);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable6.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+shahajadpurSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+shahajadpurSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+shahajadpurSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+shahajadpurSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+shahajadpurSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+shahajadpurSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+shahajadpurSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+shahajadpurSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+shahajadpurSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + shahajadpurSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+shahajadpurSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+shahajadpurSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+shahajadpurSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+shahajadpurSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+shahajadpurSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+shahajadpurSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+shahajadpurSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+shahajadpurSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+shahajadpurSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + shahajadpurSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+shahajadpurSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+shahajadpurSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+shahajadpurSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+shahajadpurSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+shahajadpurSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+shahajadpurSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+shahajadpurSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+shahajadpurSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+shahajadpurSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + shahajadpurSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+shahajadpurSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+shahajadpurSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+shahajadpurSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+shahajadpurSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+shahajadpurSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+shahajadpurSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+shahajadpurSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+shahajadpurSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+shahajadpurSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + shahajadpurSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable6.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable6.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable6.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(shahajadpurSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable6.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(shahajadpurSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				actualExceptMin = actualExceptMin +shahajadpurSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(shahajadpurSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				actualCunsumption = actualCunsumption+shahajadpurSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(shahajadpurSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				billingUnit = billingUnit+shahajadpurSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(shahajadpurSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				difference = difference+shahajadpurSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(shahajadpurSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				totalActualCons = totalActualCons+shahajadpurSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(shahajadpurSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(shahajadpurSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+shahajadpurSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(shahajadpurSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				minimumCharge = minimumCharge+shahajadpurSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(shahajadpurSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				meterRent = meterRent+shahajadpurSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(shahajadpurSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				nHVhHV = nHVhHV+shahajadpurSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(shahajadpurSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable6.addCell(pcell);
				
				totalBillAmount = totalBillAmount + shahajadpurSalesList.get(i).getTotalBillAmount();
				
				previousCategory = shahajadpurSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of SHAHAJADPUR(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable6.addCell(pcell);
			
			document.add(datatable6);
			
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable7 = new PdfPTable(13);			
			datatable7.setWidthPercentage(100);
			datatable7.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable7.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*---------------------------------End of Shahajadpur-----------------------------------------*/
			
			/*---------------------------------Sales of Bera----------------------------------------------*/
			
			beraSalesList= getAreaWiseSalesBera();
			int listSizebera = beraSalesList.size();
			
			for (int i = 0; i < listSizebera; i++) {
				
				currentCategoryID = beraSalesList.get(i).getCategoryID();
				currentCategory=beraSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable7.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("BERA AREA",font3));
					pcell.setColspan(13);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable7.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+beraSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+beraSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+beraSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+beraSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+beraSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+beraSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+beraSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+beraSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+beraSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + beraSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+beraSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+beraSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+beraSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+beraSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+beraSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+beraSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+beraSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+beraSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+beraSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + beraSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+beraSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+beraSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+beraSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+beraSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+beraSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+beraSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+beraSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+beraSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+beraSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + beraSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+beraSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+beraSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+beraSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+beraSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+beraSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+beraSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+beraSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+beraSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+beraSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + beraSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable7.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable7.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable7.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(beraSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable7.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(beraSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				actualExceptMin = actualExceptMin +beraSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(beraSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				actualCunsumption = actualCunsumption+beraSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(beraSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				billingUnit = billingUnit+beraSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(beraSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				difference = difference+beraSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(beraSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				totalActualCons = totalActualCons+beraSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(beraSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(beraSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+beraSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(beraSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				minimumCharge = minimumCharge+beraSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(beraSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				meterRent = meterRent+beraSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(beraSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				nHVhHV = nHVhHV+beraSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(beraSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable7.addCell(pcell);
				
				totalBillAmount = totalBillAmount + beraSalesList.get(i).getTotalBillAmount();
				
				previousCategory = beraSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of BERA(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable7.addCell(pcell);
			
			document.add(datatable7);
			
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable8 = new PdfPTable(13);			
			datatable8.setWidthPercentage(100);
			datatable8.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable8.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*---------------------------------End of Bera------------------------------------------------*/
			
			/*---------------------------------Sales of Santhia-------------------------------------------*/
			
			santhiaSalesList= getAreaWiseSalesSanthia();
			int listSizesant = santhiaSalesList.size();
			
			for (int i = 0; i < listSizesant; i++) {
				
				currentCategoryID = santhiaSalesList.get(i).getCategoryID();
				currentCategory=santhiaSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable8.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("SANTHIA AREA",font3));
					pcell.setColspan(13);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable8.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+santhiaSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+santhiaSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+santhiaSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+santhiaSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+santhiaSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+santhiaSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+santhiaSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+santhiaSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+santhiaSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + santhiaSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+santhiaSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+santhiaSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+santhiaSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+santhiaSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+santhiaSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+santhiaSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+santhiaSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+santhiaSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+santhiaSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + santhiaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+santhiaSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+santhiaSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+santhiaSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+santhiaSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+santhiaSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+santhiaSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+santhiaSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+santhiaSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+santhiaSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + santhiaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+santhiaSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+santhiaSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+santhiaSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+santhiaSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+santhiaSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+santhiaSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+santhiaSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+santhiaSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+santhiaSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + santhiaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable8.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable8.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable8.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(santhiaSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable8.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(santhiaSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				actualExceptMin = actualExceptMin +santhiaSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(santhiaSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				actualCunsumption = actualCunsumption+santhiaSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(santhiaSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				billingUnit = billingUnit+santhiaSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(santhiaSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				difference = difference+santhiaSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(santhiaSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				totalActualCons = totalActualCons+santhiaSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(santhiaSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(santhiaSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+santhiaSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(santhiaSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				minimumCharge = minimumCharge+santhiaSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(santhiaSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				meterRent = meterRent+santhiaSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(santhiaSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				nHVhHV = nHVhHV+santhiaSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(santhiaSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable8.addCell(pcell);
				
				totalBillAmount = totalBillAmount + santhiaSalesList.get(i).getTotalBillAmount();
				
				previousCategory = santhiaSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of SANTHIA(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable8.addCell(pcell);
			
			document.add(datatable8);
			
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable9 = new PdfPTable(13);			
			datatable9.setWidthPercentage(100);
			datatable9.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable9.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*---------------------------------End of Santhia---------------------------------------------*/
			
			/*---------------------------------Sales of Ullahpara-----------------------------------------*/
			
			ullahparaSalesList= getAreaWiseSalesUllahpara();
			int listSizeullah = ullahparaSalesList.size();
			
			for (int i = 0; i < listSizeullah; i++) {
				
				currentCategoryID = ullahparaSalesList.get(i).getCategoryID();
				currentCategory=ullahparaSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable9.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("ULLAHPARA AREA",font3));
					pcell.setColspan(13);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable9.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+ullahparaSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+ullahparaSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+ullahparaSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+ullahparaSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+ullahparaSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+ullahparaSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+ullahparaSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+ullahparaSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+ullahparaSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + ullahparaSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+ullahparaSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+ullahparaSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+ullahparaSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+ullahparaSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+ullahparaSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+ullahparaSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+ullahparaSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+ullahparaSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+ullahparaSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + ullahparaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+ullahparaSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+ullahparaSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+ullahparaSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+ullahparaSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+ullahparaSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+ullahparaSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+ullahparaSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+ullahparaSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+ullahparaSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + ullahparaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+ullahparaSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+ullahparaSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+ullahparaSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+ullahparaSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+ullahparaSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+ullahparaSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+ullahparaSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+ullahparaSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+ullahparaSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + ullahparaSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable9.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable9.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable9.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(ullahparaSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable9.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ullahparaSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				actualExceptMin = actualExceptMin +ullahparaSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ullahparaSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				actualCunsumption = actualCunsumption+ullahparaSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ullahparaSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				billingUnit = billingUnit+ullahparaSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ullahparaSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				difference = difference+ullahparaSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(ullahparaSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				totalActualCons = totalActualCons+ullahparaSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ullahparaSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ullahparaSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+ullahparaSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ullahparaSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				minimumCharge = minimumCharge+ullahparaSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ullahparaSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				meterRent = meterRent+ullahparaSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ullahparaSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				nHVhHV = nHVhHV+ullahparaSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(ullahparaSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable9.addCell(pcell);
				
				totalBillAmount = totalBillAmount + ullahparaSalesList.get(i).getTotalBillAmount();
				
				previousCategory = ullahparaSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of ULLAHPARA(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable9.addCell(pcell);
			
			document.add(datatable9);
			
			document.newPage();
			document.setMargins(20, 20, 30, 72);
			PdfPTable datatable10 = new PdfPTable(13);			
			datatable10.setWidthPercentage(100);
			datatable10.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable10.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*---------------------------------end of Ullahpara-------------------------------------------*/
			
			/*---------------------------------Sales of Rajshahi------------------------------------------*/
			
			rajshahiSalesList= getAreaWiseSalesRajshahi();
			int listSizeraj = rajshahiSalesList.size();
			
			for (int i = 0; i < listSizeraj; i++) {
				
				currentCategoryID = rajshahiSalesList.get(i).getCategoryID();
				currentCategory=rajshahiSalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					datatable10.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("RAJSHAHI AREA",font3));
					pcell.setColspan(13);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					datatable10.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+rajshahiSalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+rajshahiSalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+rajshahiSalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+rajshahiSalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+rajshahiSalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+rajshahiSalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+rajshahiSalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+rajshahiSalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+rajshahiSalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + rajshahiSalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+rajshahiSalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+rajshahiSalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+rajshahiSalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+rajshahiSalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+rajshahiSalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+rajshahiSalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+rajshahiSalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+rajshahiSalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+rajshahiSalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + rajshahiSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+rajshahiSalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+rajshahiSalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+rajshahiSalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+rajshahiSalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+rajshahiSalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+rajshahiSalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+rajshahiSalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+rajshahiSalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+rajshahiSalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + rajshahiSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+rajshahiSalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+rajshahiSalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+rajshahiSalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+rajshahiSalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+rajshahiSalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+rajshahiSalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+rajshahiSalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+rajshahiSalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+rajshahiSalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + rajshahiSalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					datatable10.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					datatable10.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable10.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(rajshahiSalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable10.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(rajshahiSalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				actualExceptMin = actualExceptMin +rajshahiSalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(rajshahiSalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				actualCunsumption = actualCunsumption+rajshahiSalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(rajshahiSalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				billingUnit = billingUnit+rajshahiSalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(rajshahiSalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				difference = difference+rajshahiSalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(rajshahiSalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				totalActualCons = totalActualCons+rajshahiSalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(rajshahiSalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(rajshahiSalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+rajshahiSalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(rajshahiSalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				minimumCharge = minimumCharge+rajshahiSalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(rajshahiSalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				meterRent = meterRent+rajshahiSalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(rajshahiSalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				nHVhHV = nHVhHV+rajshahiSalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(rajshahiSalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				datatable10.addCell(pcell);
				
				totalBillAmount = totalBillAmount + rajshahiSalesList.get(i).getTotalBillAmount();
				
				previousCategory = rajshahiSalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of RAJSHAHI(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			datatable10.addCell(pcell);
			
			document.add(datatable10);
			
			document.newPage();
			
			document.setMargins(20, 20, 30, 72);
			
			
			/*---------------------------------End of Rajshahi--------------------------------------------*/
			
			
			PdfPTable summaryTable = new PdfPTable(13);			
			summaryTable.setWidthPercentage(100);
			summaryTable.setWidths(new float[] {15,50,40,40,30,30,40,20,40,30,30,30,40});
			
			pcell=new PdfPCell(new Paragraph("Sl.No",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Category of Customer",font3));
			pcell.setRowspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Gas Consumption",font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billimg Amount",font3));
			pcell.setColspan(7);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption Except Minimum ChargeBills",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum Consumption",font3));
			pcell.setColspan(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rate",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Value of Total Actual Consumption",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Minimum charge",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("NHV/HHV",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph("Total Billed Amount",font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Cons of Minimum Bills",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Billing Unit",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("01",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("02",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("03",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("04",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("05",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("06=(05-04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("07=(03+04)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("rate",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("08=(07*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("09=(06*Rate)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);			
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("10",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("11",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);	
			
			pcell=new PdfPCell(new Paragraph("12=(08+09+10+11)",font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			summaryTable.addCell(pcell);
			
			actualExceptMin=0.0;
			actualCunsumption=0.0;
			billingUnit=0.0;
			difference=0.0;
			totalActualCons=0.0;
			valueOfTotalActualCons=0.0;
			minimumCharge=0.0;
			meterRent=0.0;
			nHVhHV=0.0;
			totalBillAmount=0.0;			
			gvtActualExceptMin=0.0;
			gvtActualCunsumption=0.0;
			gvtBillingUnit=0.0;
			gvtDifference = 0.0;
			gvtTotalActualCons=0.0;
			gvtValueOfTotalActualCons=0.0;
			gvtMinimumCharge=0.0;
			gvtMeterRent=0.0;
			gvtnHVhHV=0.0;
			gvtTotalBillAmount=0.0;
			dgvtActualExceptMin=0.0;
			dgvtActualCunsumption=0.0;
			dgvtBillingUnit=0.0;
			dgvtDifference = 0.0;
			dgvtTotalActualCons=0.0;
			dgvtValueOfTotalActualCons=0.0;
			dgvtMinimumCharge=0.0;
			dgvtMeterRent=0.0;
			dgvtnHVhHV=0.0;
			dgvtTotalBillAmount=0.0;
			pvtActualExceptMin=0.0;
			pvtActualCunsumption=0.0;
			pvtBillingUnit=0.0;
			pvtDifference = 0.0;
			pvtTotalActualCons=0.0;
			pvtValueOfTotalActualCons=0.0;
			pvtMinimumCharge=0.0;
			pvtMeterRent=0.0;
			pvtnHVhHV=0.0;
			pvtTotalBillAmount=0.0;
			dpvtActualExceptMin=0.0;
			dpvtActualCunsumption=0.0;
			dpvtBillingUnit=0.0;
			dpvtDifference = 0.0;
			dpvtTotalActualCons=0.0;
			dpvtValueOfTotalActualCons=0.0;
			dpvtMinimumCharge=0.0;
			dpvtMeterRent=0.0;
			dpvtnHVhHV=0.0;
			dpvtTotalBillAmount=0.0;
			
			/*---------------------------------end of Ullahpara-------------------------------------------*/
			
			/*---------------------------------Sales of Rajshahi------------------------------------------*/
			
			summarySalesList= getAreaWiseSalesSummary();
			int listSizesum = summarySalesList.size();
			
			for (int i = 0; i < listSizesum; i++) {
				
				currentCategoryID = summarySalesList.get(i).getCategoryID();
				currentCategory=summarySalesList.get(i).getCategoryType();
				
				if(i==0){
					
					pcell=new PdfPCell(new Paragraph("1",font3));
					summaryTable.addCell(pcell);
					pcell=new PdfPCell(new Paragraph("JGTDSL",font3));
					pcell.setColspan(13);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("A) GOVERNMENT",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",font3));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setColspan(11);
					summaryTable.addCell(pcell);
				}
				
				if(currentCategory.equals("GOVT")){
					
					gvtActualExceptMin=gvtActualExceptMin+summarySalesList.get(i).getActualExceptmin();
					gvtActualCunsumption = gvtActualCunsumption+summarySalesList.get(i).getActualConsumption();
					gvtBillingUnit = gvtBillingUnit+summarySalesList.get(i).getBillingUnit();
					gvtDifference = gvtDifference+summarySalesList.get(i).getDifference();
					gvtTotalActualCons = gvtTotalActualCons+summarySalesList.get(i).getTotalActualConsumption();
					gvtValueOfTotalActualCons = gvtValueOfTotalActualCons+summarySalesList.get(i).getValueOfTotalActualConsumption();
					gvtMinimumCharge = gvtMinimumCharge+summarySalesList.get(i).getMinimumCharge();
					gvtMeterRent = gvtMeterRent+summarySalesList.get(i).getMeterRent();
					gvtnHVhHV = gvtnHVhHV+summarySalesList.get(i).getnHVhHV();
					gvtTotalBillAmount = gvtTotalBillAmount + summarySalesList.get(i).getTotalBillAmount();
					
					
				}else if(currentCategory.equals("PVT")){
					
					pvtActualExceptMin=pvtActualExceptMin+summarySalesList.get(i).getActualExceptmin();
					pvtActualCunsumption = pvtActualCunsumption+summarySalesList.get(i).getActualConsumption();
					pvtBillingUnit = pvtBillingUnit+summarySalesList.get(i).getBillingUnit();
					pvtDifference = pvtDifference+summarySalesList.get(i).getDifference();
					pvtTotalActualCons = pvtTotalActualCons+summarySalesList.get(i).getTotalActualConsumption();
					pvtValueOfTotalActualCons = pvtValueOfTotalActualCons+summarySalesList.get(i).getValueOfTotalActualConsumption();
					pvtMinimumCharge = pvtMinimumCharge+summarySalesList.get(i).getMinimumCharge();
					pvtMeterRent = pvtMeterRent+summarySalesList.get(i).getMeterRent();
					pvtnHVhHV = pvtnHVhHV+summarySalesList.get(i).getnHVhHV();
					pvtTotalBillAmount = pvtTotalBillAmount + summarySalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("02")){
					
					dgvtActualExceptMin=dgvtActualExceptMin+summarySalesList.get(i).getActualExceptmin();
					dgvtActualCunsumption = dgvtActualCunsumption+summarySalesList.get(i).getActualConsumption();
					dgvtBillingUnit = dgvtBillingUnit+summarySalesList.get(i).getBillingUnit();
					dgvtDifference = dgvtDifference+summarySalesList.get(i).getDifference();
					dgvtTotalActualCons = dgvtTotalActualCons+summarySalesList.get(i).getTotalActualConsumption();
					dgvtValueOfTotalActualCons = dgvtValueOfTotalActualCons+summarySalesList.get(i).getValueOfTotalActualConsumption();
					dgvtMinimumCharge = dgvtMinimumCharge+summarySalesList.get(i).getMinimumCharge();
					dgvtMeterRent = dgvtMeterRent+summarySalesList.get(i).getMeterRent();
					dgvtnHVhHV = dgvtnHVhHV+summarySalesList.get(i).getnHVhHV();
					dgvtTotalBillAmount = dgvtTotalBillAmount + summarySalesList.get(i).getTotalBillAmount();
					
				}
				
				if(currentCategoryID.equals("01")){
					
					dpvtActualExceptMin=dpvtActualExceptMin+summarySalesList.get(i).getActualExceptmin();
					dpvtActualCunsumption = dpvtActualCunsumption+summarySalesList.get(i).getActualConsumption();
					dpvtBillingUnit = dpvtBillingUnit+summarySalesList.get(i).getBillingUnit();
					dpvtDifference = dpvtDifference+summarySalesList.get(i).getDifference();
					dpvtTotalActualCons = dpvtTotalActualCons+summarySalesList.get(i).getTotalActualConsumption();
					dpvtValueOfTotalActualCons = dpvtValueOfTotalActualCons+summarySalesList.get(i).getValueOfTotalActualConsumption();
					dpvtMinimumCharge = dpvtMinimumCharge+summarySalesList.get(i).getMinimumCharge();
					dpvtMeterRent = dpvtMeterRent+summarySalesList.get(i).getMeterRent();
					dpvtnHVhHV = dpvtnHVhHV+summarySalesList.get(i).getnHVhHV();
					dpvtTotalBillAmount = dpvtTotalBillAmount + summarySalesList.get(i).getTotalBillAmount();
					
				}
				
				if(i>0 && !currentCategory.equals(previousCategory)){
					
					pcell=new PdfPCell(new Paragraph("Total Dom.(GOVT)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(dgvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					
					
					pcell=new PdfPCell(new Paragraph("Sub Total (A)",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(2);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualExceptMin),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtActualCunsumption),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtBillingUnit),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtDifference),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtValueOfTotalActualCons),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMinimumCharge),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtMeterRent),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtnHVhHV),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph(consumption_format.format(gvtTotalBillAmount),ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("B) Private",ReportUtil.f9B));
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setColspan(2);
					summaryTable.addCell(pcell);
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setColspan(11);
					summaryTable.addCell(pcell);
					
				}
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				summaryTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(summarySalesList.get(i).getCustomerCategory(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				summaryTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(summarySalesList.get(i).getActualExceptmin()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				actualExceptMin = actualExceptMin +summarySalesList.get(i).getActualExceptmin();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(summarySalesList.get(i).getActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				actualCunsumption = actualCunsumption+summarySalesList.get(i).getActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(summarySalesList.get(i).getBillingUnit()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				billingUnit = billingUnit+summarySalesList.get(i).getBillingUnit();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(summarySalesList.get(i).getDifference()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				difference = difference+summarySalesList.get(i).getDifference();
				
				pcell=new PdfPCell(new Paragraph(consumption_format.format(summarySalesList.get(i).getTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				totalActualCons = totalActualCons+summarySalesList.get(i).getTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(summarySalesList.get(i).getRate()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(summarySalesList.get(i).getValueOfTotalActualConsumption()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				valueOfTotalActualCons = valueOfTotalActualCons+summarySalesList.get(i).getValueOfTotalActualConsumption();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(summarySalesList.get(i).getMinimumCharge()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				minimumCharge = minimumCharge+summarySalesList.get(i).getMinimumCharge();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(summarySalesList.get(i).getMeterRent()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				meterRent = meterRent+summarySalesList.get(i).getMeterRent();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(summarySalesList.get(i).getnHVhHV()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				nHVhHV = nHVhHV+summarySalesList.get(i).getnHVhHV();
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(summarySalesList.get(i).getTotalBillAmount()),ReportUtil.f9));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				summaryTable.addCell(pcell);
				
				totalBillAmount = totalBillAmount + summarySalesList.get(i).getTotalBillAmount();
				
				previousCategory = summarySalesList.get(i).getCategoryType();
				
			}
			
			
			
			pcell=new PdfPCell(new Paragraph("Total Dom.(PVT)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(dpvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Sub Total (B)",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtActualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtBillingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtDifference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtValueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMinimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtMeterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtnHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(pvtTotalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			/*-------------------------------Grand Total-----------------------------------------------*/
			
			pcell=new PdfPCell(new Paragraph("Total Sales of JGTDSL(A+B)=",ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setColspan(2);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualExceptMin),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(actualCunsumption),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(billingUnit),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(difference),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(valueOfTotalActualCons),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(minimumCharge),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(meterRent),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(nHVhHV),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(totalBillAmount),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			summaryTable.addCell(pcell);
			
			document.add(summaryTable);
			
							
		
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
				
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
		
	
	private ArrayList<SalesDTO>getAreaWiseSalesSirajganj(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql=  "SELECT MCC.CATEGORY_ID CATEGORY_ID, " +
					"         DECODE ( " +
					"            CONN.ISMETERED, " +
					"            1, SUBSTR (MCC.CATEGORY_NAME, 1, LENGTH (MCC.CATEGORY_NAME)), " +
					"            0, SUBSTR (MCC.CATEGORY_NAME, 1, LENGTH (MCC.CATEGORY_NAME) - 2) || ' (Non-Meter)') " +
					"            CATEGORY_NAME, " +
					"         MCC.CATEGORY_TYPE CATEGORY_TYPE, " +
					"         CONN.ISMETERED, " +
					"         SUM (ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM, " +
					"         SUM (ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, " +
					"         SUM (BILLING_UNIT) BILLING_UNIT, " +
					"         SUM (DIFFERENCE) DIFFERENCE, " +
					"         SUM (TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION, " +
					"         SR.RATE, " +
					"         SUM (VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION, " +
					"         SUM (MINIMUM_CHARGE) MINIMUM_CHARGE, " +
					"         SUM (METER_RENT) METER_RENT, " +
					"         SUM (SURCHARGE_AMOUNT) SURCHARGE_AMOUNT, " +
					"         SUM (HHV_NHV_AMOUNT) HHV_NHV_AMOUNT, " +
					"         SUM (TOTAL_AMOUNT) TOTAL_AMOUNT " +
					"    FROM SALES_REPORT SR, CUSTOMER_CONNECTION conn, MST_CUSTOMER_CATEGORY mcc " +
					"   WHERE     SR.customer_id = conn.customer_id " +
					"         AND BILLING_MONTH = '"+bill_month+"' " +
					"         AND BILLING_YEAR = '"+bill_year+"' " +
					"         AND SUBSTR (SR.customer_id, 3, 2) = MCC.CATEGORY_ID " +
					"         AND SUBSTR (SR.customer_id, 1, 2) = '"+area+"' " +
					"GROUP BY MCC.CATEGORY_ID, " +
					"         MCC.CATEGORY_NAME, " +
					"         CONN.ISMETERED, " +
					"         MCC.CATEGORY_TYPE, " +
					"         RATE " +
					"ORDER BY MCC.CATEGORY_TYPE ASC, MCC.CATEGORY_ID DESC, ISMETERED DESC " ;







			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesBaghabari(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='02' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesPabna(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='03' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesIshwardi(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='04' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesBogra(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='05' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesShahajadpur(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='06' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesBera(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='07' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesSanthia(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='08' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesUllahpara(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='09' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesRajshahi(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID and substr(SR.customer_id,1,2)='10' "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	private ArrayList<SalesDTO>getAreaWiseSalesSummary(){
		ArrayList<SalesDTO>sirajSalesList = new ArrayList<SalesDTO>();
		
		try {
			

			
			String defaulterSql="select MCC.CATEGORY_ID CATEGORY_ID,decode(CONN.ISMETERED,1,substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7),substr(CATEGORY_NAME,1,length(CATEGORY_NAME)-7)||' (Non-Meter)') CATEGORY_NAME" +
					",MCC.CATEGORY_TYPE CATEGORY_TYPE,CONN.ISMETERED,"+
					"sum(ACTUAL_EXCEPT_MINIMUM) ACTUAL_EXCEPT_MINIMUM,sum(ACTUAL_WITH_MINIMUM) ACTUAL_WITH_MINIMUM, "+
					"sum(BILLING_UNIT) BILLING_UNIT, sum(DIFFERENCE) DIFFERENCE,sum(TOTAL_ACTUAL_CONSUMPTION) TOTAL_ACTUAL_CONSUMPTION,RATE ,"+
					"sum(VALUE_OF_ACTUAL_CONSUMPTION) VALUE_OF_ACTUAL_CONSUMPTION,sum(MINIMUM_CHARGE) MINIMUM_CHARGE,sum(METER_RENT) METER_RENT, "+
					"sum(SURCHARGE_AMOUNT) SURCHARGE_AMOUNT,sum(HHV_NHV_AMOUNT) HHV_NHV_AMOUNT,sum(TOTAL_AMOUNT) TOTAL_AMOUNT "+
					"from SALES_REPORT SR,CUSTOMER_CONNECTION conn,MST_CUSTOMER_CATEGORY mcc "+
					"where SR.customer_id=conn.customer_id and BILLING_MONTH='"+bill_month+"' and BILLING_YEAR='"+bill_year+"'"+
					"and substr(SR.customer_id,3,2)=MCC.CATEGORY_ID "+ 
					"group by  MCC.CATEGORY_ID,MCC.CATEGORY_NAME,CONN.ISMETERED,MCC.CATEGORY_TYPE,RATE "+
					"ORDER BY MCC.CATEGORY_TYPE asc,MCC.CATEGORY_ID desc,ISMETERED desc";






			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		SalesDTO salesDTO = new SalesDTO();
        		
        		salesDTO.setCategoryID(resultSet.getString("CATEGORY_ID"));
        		salesDTO.setCategoryType(resultSet.getString("CATEGORY_TYPE"));
        		salesDTO.setCustomerCategory(resultSet.getString("CATEGORY_NAME"));
        		salesDTO.setActualExceptmin(resultSet.getDouble("ACTUAL_EXCEPT_MINIMUM"));
        		salesDTO.setActualConsumption(resultSet.getDouble("ACTUAL_WITH_MINIMUM"));
        		salesDTO.setBillingUnit(resultSet.getDouble("BILLING_UNIT"));
        		salesDTO.setDifference(resultSet.getDouble("DIFFERENCE"));
        		salesDTO.setTotalActualConsumption(resultSet.getDouble("TOTAL_ACTUAL_CONSUMPTION"));
        		salesDTO.setRate(resultSet.getDouble("RATE"));
        		salesDTO.setValueOfTotalActualConsumption(resultSet.getDouble("VALUE_OF_ACTUAL_CONSUMPTION"));
        		salesDTO.setMinimumCharge(resultSet.getDouble("MINIMUM_CHARGE"));
        		salesDTO.setMeterRent(resultSet.getDouble("METER_RENT"));
        		salesDTO.setnHVhHV(resultSet.getDouble("HHV_NHV_AMOUNT"));
        		salesDTO.setTotalBillAmount(resultSet.getDouble("TOTAL_AMOUNT"));
        		
        		sirajSalesList.add(salesDTO);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sirajSalesList;
	}
	
	

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
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






	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	
  }



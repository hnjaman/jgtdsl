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
import org.jgtdsl.dto.AddressDTO;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerPersonalDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.NonMeterReportDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class MeterReadingReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<MeterReadingDTO> readingReport=new ArrayList<MeterReadingDTO>();
	MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	DecimalFormat burner = new DecimalFormat("#,##,##,##,##,##0.0");
	DecimalFormat consumption_format = new DecimalFormat("##########0.000");
	DecimalFormat factor_format=new DecimalFormat("##########0.000");
	
	    private  String area;
	    private  String customer_id;
	    private  String customer_category;
	    private  String bill_month;
	    private  String bill_year;
	    private  String report_for;
	    private  String report_for2;
	    private  String category_name;
	    private  String reading_month;
	    private  String reading_year;
	    private  String from_date;
	    private  String to_date;

	public String execute() throws Exception
	{
				
		String fileName="Meter_Reading.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5,5,60,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		
		
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			
			
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
			
			
			if(report_for.equals("individual_wise")){
				generatePdfMeterReadingIndividual(document);
			}else{
				generatePdfMeterReadingAreaWise(document);
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
	
	private void generatePdfMeterReadingIndividual(Document document) throws DocumentException{
		
		PdfPCell pcell=null;
		PdfPTable ptable=null;
		PdfPTable headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,50,30});
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("METER READING FROM "+from_date+" TO "+to_date,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		ptable = new PdfPTable(14);
		ptable.setWidthPercentage(100);
		ptable.setWidths(new float[]{6,6,6,6,6,6,6,6,6,6,6,6,6,6});
		ptable.setSpacingBefore(10);
		
		String monthValue="";
		double percentage=0.0;
		
		readingReport=getIndividualMeterReadingInfo();
		int listSize=readingReport.size();
		
		for (int i = 0; i < listSize; i++) {
			
			if(i==0){
				
				pcell=new PdfPCell(new Paragraph("Customer Name : "+readingReport.get(i).getCustomer_name(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				pcell.setBorder(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Connection Date : "+readingReport.get(i).getCustomer().getConnectionInfo().getConnection_date(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(7);
				pcell.setBorder(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Code : "+readingReport.get(i).getCustomer_id(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				pcell.setBorder(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Category : "+readingReport.get(i).getCustomer().getCustomer_category_name(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setColspan(7);
				pcell.setBorder(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Address : "+readingReport.get(i).getAddress(),ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				pcell.setBorder(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(7);
				pcell.setBorder(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setColspan(14);
				pcell.setBorder(0);
				ptable.addCell(pcell);
				
				/*----------------------Header Row Start-------------------------*/
				
				pcell=new PdfPCell(new Paragraph("Bill Month",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Meter ID",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Previous Reading Date",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Previous Reading",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Current Reading Date",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Current Reading",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Difference",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Pressure",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Pressure Factor",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
								
				pcell=new PdfPCell(new Paragraph("Actual Consumption",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Min. Load",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Max. Load",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Percentage",ReportUtil.f9B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				ptable.addCell(pcell);				
				
			}
			
			
			pcell=new PdfPCell(new Paragraph(readingReport.get(i).getMonth()+"-"+readingReport.get(i).getYear(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(readingReport.get(i).getMeter_id(),ReportUtil.f9));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(readingReport.get(i).getPrev_reading_date(),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
		
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getCurr_reading()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(readingReport.get(i).getCurr_reading_date(),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getPrev_reading()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getDifference()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getPressure()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getPressure_factor()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getActual_consumption()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(taka_format.format(readingReport.get(i).getMeter_rent()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getMin_load()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(readingReport.get(i).getMax_load()),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			percentage=(readingReport.get(i).getActual_consumption()*100)/readingReport.get(i).getMax_load();
			
			pcell=new PdfPCell(new Paragraph(consumption_format.format(percentage),ReportUtil.f9B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(pcell);
			
			
		}
		
		document.add(ptable);
	}
	
	private void generatePdfMeterReadingAreaWise(Document document) throws DocumentException{
		
		PdfPCell pcell=null;
		PdfPTable ptable=null;
		PdfPTable headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{30,50,30});
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);	
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("METER READING FOR MONTH OF "+Month.values()[Integer.valueOf(bill_month)-1]+"'"+bill_year,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorderColor(BaseColor.WHITE);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		readingReport =getLoadIncraseInformation();
		
		int totalRecordsPerCategory=0;
		int total_burner=0;
		
		double totaldifference=0.0;
		double totalActualConsumption=0.0;

		int expireListSize=readingReport.size();
		String previousCustomerCategoryName=new String("");
		
		for(int i=0;i<expireListSize;i++)
		{
			meterReadingDTO = readingReport.get(i);
			String currentCustomerCategoryName=meterReadingDTO.getCustomer().getCustomer_category_name();
			
			if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
			{	
			
			if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
			{
				
				if(i>0)
				{
					pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(2);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
												
					
			
					
					pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
					pcell.setMinimumHeight(18f);
					pcell.setColspan(14);
					pcell.setBorder(0);
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
					ptable.addCell(pcell);
					document.add(ptable);
				
					totalRecordsPerCategory=0;
					total_burner=0;
				}
				
			}
			

		
			ptable = new PdfPTable(16);
			ptable.setWidthPercentage(100);
			ptable.setWidths(new float[]{15,120,30,30,40,30,40,30,30,30,40,30,30,30,30,30});
			ptable.setSpacingBefore(10);
			
			
			pcell=new PdfPCell(new Paragraph(currentCustomerCategoryName,ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(2);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(14);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
							
			
			pcell=new PdfPCell(new Paragraph("Sr.No",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Name & Address",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer ID",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Con. Date",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter No.",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Current",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Previous",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Difference",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Pressure",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Pressure Factor",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Actual Consumption",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Meter Rent",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Min Load",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Maxload",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Percent",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Remarks",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			
			}
			
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory+1),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(meterReadingDTO.getCustomer().getAddress(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(meterReadingDTO.getCustomer_id(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(meterReadingDTO.getCustomer().getConnectionInfo().getConnection_date(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(meterReadingDTO.getMeter_sl_no(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
					
			pcell = new PdfPCell(new Paragraph(consumption_format.format(meterReadingDTO.getCurr_reading()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(meterReadingDTO.getPrev_reading()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(meterReadingDTO.getDifference()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			totaldifference=totaldifference+meterReadingDTO.getDifference();
			
			pcell = new PdfPCell(new Paragraph(factor_format.format(meterReadingDTO.getPressure()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(factor_format.format(meterReadingDTO.getPressure_factor()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(meterReadingDTO.getActual_consumption()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			totalActualConsumption=totalActualConsumption+meterReadingDTO.getActual_consumption();
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(meterReadingDTO.getMeter_rent()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(meterReadingDTO.getMin_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(consumption_format.format(meterReadingDTO.getMax_load()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(meterReadingDTO.getPercent()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(taka_format.format(meterReadingDTO.getBurnerQty()),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			ptable.addCell(pcell);
			
			

			
			
			
		
			previousCustomerCategoryName=meterReadingDTO.getCustomer().getCustomer_category_name();
			totalRecordsPerCategory++;
			
			
		}
		/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
		pcell=new PdfPCell(new Paragraph("Total Records:"+String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setBorder(0);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
									

		
		pcell=new PdfPCell(new Paragraph("Total Difference : ",ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(5);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph(consumption_format.format(totaldifference),ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(1);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("Total Consumption :",ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(2);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph(consumption_format.format(totalActualConsumption),ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setColspan(6);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
		ptable.addCell(pcell);
		
		document.add(ptable);
	}
	
	private ArrayList<MeterReadingDTO> getLoadIncraseInformation()
	{
	ArrayList<MeterReadingDTO> meterReadingInfo=new ArrayList<MeterReadingDTO>();
		
		try {
			String wClause="";
			if(report_for.equals("area_wise"))
			{
				wClause="And substr(mr.customer_id,1,2)="+area;
			}else if(report_for.equals("category_wise"))
			{
				wClause="And substr(mr.customer_id,1,2)="+area+" And substr(mr.customer_id,3,2)="+customer_category;
			}
			
			
			String defaulterSql="SELECT mr.customer_id, " +
					"         mcc.CATEGORY_ID, " +
					"         MCC.CATEGORY_NAME, " +
					"         mcc.CATEGORY_TYPE, " +
					"         cpi.full_name || CHR (10) || ca.ADDRESS_LINE1 NAME_ADDRESS, " +
					"         TO_CHAR (CONNECTION_DATE, 'dd-MM-YYYY') CONNECTION_DATE, " +
					"         mmg.RATING_NAME || CHR (10) || METER_SL_NO Meter_info, " +
					"         CURR_READING, " +
					"         PREV_READING, " +
					"         DIFFERENCE, " +
					"         mr.PRESSURE, " +
					"         mr.PRESSURE_FACTOR, " +
					"         mr.ACTUAL_CONSUMPTION, " +
					"         mr.METER_RENT, " +
					"         mr.pmin_load, " +
					"         mr.pmax_load,tm.DOUBLE_BURNER_QNT_BILLCAL, " +
					"         DECODE (mr.pmax_load, " +
					"                 0, mr.pmax_load, " +
					"                 ( (mr.ACTUAL_CONSUMPTION * 100) / mr.pmax_load)) " +
					"            Percent " +
					"    FROM meter_reading mr, " +
					"         customer_connection cc, " +
					"         customer_meter cm, " +
					"         CUSTOMER_ADDRESS ca, " +
					"         customer cus, " +
					"         MST_CUSTOMER_CATEGORY mcc, " +
					"         CUSTOMER_PERSONAL_INFO cpi, " +
					"         MST_METER_GRATING mmg, " +
					"         (select PARENT_CONNECTION Customer_id,DOUBLE_BURNER_QNT_BILLCAL from customer_connection " +
					"        WHERE PARENT_CONNECTION is not null) tm " +
					"   WHERE     mr.customer_id = cc.customer_id " +
					"         AND mr.meter_id = cm.meter_id " +
					"         AND mr.customer_id = ca.customer_id " +
					"         AND mr.customer_id = CUS.CUSTOMER_ID " +
					"         AND MR.CUSTOMER_ID=TM.CUSTOMER_ID(+) " +
					"         AND SUBSTR (mr.customer_id, 3, 2) = MCC.CATEGORY_ID " +
					"         AND mr.customer_id = cpi.customer_id " +
					"         AND CM.G_RATING = mmg.RATING_ID " +
					"         AND mr.billing_month ='"+bill_month+"' " +
					"         AND mr.billing_year ='"+bill_year+"' " +wClause+
					"ORDER BY mcc.CATEGORY_ID DESC, mr.customer_id ASC " ;

					
					
					
					
					
					
//					"select mr.customer_id,mcc.CATEGORY_ID,MCC.CATEGORY_NAME,mcc.CATEGORY_TYPE,cpi.full_name||chr(10)||ca.ADDRESS_LINE1 NAME_ADDRESS,to_char(CONNECTION_DATE,'dd-MM-YYYY') CONNECTION_DATE,mmg.RATING_NAME||chr(10)||METER_SL_NO Meter_info,CURR_READING,PREV_READING,DIFFERENCE,mr.PRESSURE,mr.PRESSURE_FACTOR, " +
//					"mr.ACTUAL_CONSUMPTION,mr.METER_RENT,mr.pmin_load,mr.pmax_load, decode(mr.pmax_load,0,mr.pmax_load,((mr.ACTUAL_CONSUMPTION*100)/ mr.pmax_load)) Percent  " +
//					"from meter_reading mr,customer_connection cc,customer_meter cm,CUSTOMER_ADDRESS ca,customer cus,MST_CUSTOMER_CATEGORY mcc,CUSTOMER_PERSONAL_INFO cpi,MST_METER_GRATING mmg " +
//					"where mr.customer_id=cc.customer_id " +
//					"and mr.meter_id=cm.meter_id " +
//					"and mr.customer_id=ca.customer_id " +
//					"and mr.customer_id=CUS.CUSTOMER_ID " +
//					"and substr(mr.customer_id,3,2)=MCC.CATEGORY_ID " +
//					"and mr.customer_id=cpi.customer_id " +
//					"and CM.G_RATING=mmg.RATING_ID " +
//					"and mr.billing_month='"+bill_month+"' " +
//					"and mr.billing_year='"+bill_year+"' " +wClause+
//					"order by mcc.CATEGORY_ID desc ,mr.customer_id asc" ;




			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		MeterReadingDTO meterReadingDto=new MeterReadingDTO();
        		CustomerDTO customerDto=new CustomerDTO();
        		CustomerPersonalDTO personalInfoDto=new CustomerPersonalDTO();
        		AddressDTO addressInfoDto=new AddressDTO();
        		CustomerConnectionDTO connectionInfoDto=new CustomerConnectionDTO();
        		
        		meterReadingDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		connectionInfoDto.setConnection_date(resultSet.getString("CONNECTION_DATE"));
        		customerDto.setConnectionInfo(connectionInfoDto);
        		customerDto.setCustomer_category(resultSet.getString("CATEGORY_ID"));
        		customerDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		customerDto.setAddress(resultSet.getString("NAME_ADDRESS"));
        		meterReadingDto.setCustomer(customerDto);
        		
        		meterReadingDto.setMeter_sl_no(resultSet.getString("Meter_info"));
        		meterReadingDto.setActual_consumption(resultSet.getFloat("ACTUAL_CONSUMPTION"));
        		meterReadingDto.setPressure(resultSet.getFloat("PRESSURE"));
        		meterReadingDto.setPressure_factor(resultSet.getFloat("PRESSURE_FACTOR"));
        		meterReadingDto.setPrev_reading(resultSet.getLong("PREV_READING"));
        		meterReadingDto.setCurr_reading(resultSet.getLong("CURR_READING"));
        		meterReadingDto.setDifference(resultSet.getLong("DIFFERENCE"));
        		meterReadingDto.setMax_load(resultSet.getFloat("PMAX_LOAD"));
        		meterReadingDto.setMin_load(resultSet.getFloat("PMIN_LOAD"));
        		meterReadingDto.setMeter_rent(resultSet.getFloat("METER_RENT"));
        		meterReadingDto.setPercent(resultSet.getFloat("PERCENT"));
        		meterReadingDto.setBurnerQty(resultSet.getInt("DOUBLE_BURNER_QNT_BILLCAL"));
   		
        		meterReadingInfo.add(meterReadingDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return meterReadingInfo;
	}
	
	private ArrayList<MeterReadingDTO> getIndividualMeterReadingInfo()
	{
	ArrayList<MeterReadingDTO> meterReadingInfo=new ArrayList<MeterReadingDTO>();
		
		try {
			
			String wClause="";
			if(report_for2.endsWith("date_wise")){
				wClause=" AND CURR_READING_DATE BETWEEN TO_DATE('"+from_date+"','dd-mm-yyyy') AND TO_DATE('"+to_date+"','dd-mm-yyyy') ";
			}else{
				wClause=" AND BILLING_MONTH="+reading_month+"  AND BILLING_YEAR="+reading_year+" ";
			}
			
			
			String defaulterSql="select VM.BILLING_MONTH,VM.BILLING_YEAR,VM.CUSTOMER_ID,VM.PREV_READING,to_char(VM.PREV_READING_DATE,'dd-mm-yyyy') PREV_READING_DATE, " +
			         "VM.CURR_READING,to_char(VM.CURR_READING_DATE,'dd-mm-yyyy') CURR_READING_DATE,VM.DIFFERENCE,VM.PRESSURE,VM.PRESSURE_FACTOR,VM.ACTUAL_CONSUMPTION, " +
			         "VM.METER_RENT,VM.METER_ID,VM.PMIN_LOAD,VM.PMAX_LOAD,mci.CONNECTION_DATE,MCI.CATEGORY_NAME, " +
			         "MCI.FULL_NAME,MCI.ADDRESS_LINE1 from VIEW_METER_READING VM,MVIEW_CUSTOMER_INFO MCI " +
			         "WHERE VM.CUSTOMER_ID=MCI.CUSTOMER_ID " +
			         "AND VM.CUSTOMER_ID='"+customer_id+"' " +wClause+
			         "order by BILLING_YEAR ASC,BILLING_MONTH " ;




			
			PreparedStatement ps1=conn.prepareStatement(defaulterSql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		MeterReadingDTO meterReadingDto=new MeterReadingDTO();
        		CustomerDTO customerDto=new CustomerDTO();
        		CustomerPersonalDTO personalInfoDto=new CustomerPersonalDTO();
        		AddressDTO addressInfoDto=new AddressDTO();
        		CustomerConnectionDTO connectionInfoDto=new CustomerConnectionDTO();
        		
        		meterReadingDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		connectionInfoDto.setConnection_date(resultSet.getString("CONNECTION_DATE"));
        		customerDto.setConnectionInfo(connectionInfoDto);
        		customerDto.setCustomer_category_name(resultSet.getString("CATEGORY_NAME"));
        		meterReadingDto.setCustomer(customerDto);
        		
        		meterReadingDto.setCustomer_name(resultSet.getString("FULL_NAME"));
        		meterReadingDto.setAddress(resultSet.getString("ADDRESS_LINE1"));
        		meterReadingDto.setMonth(resultSet.getString("BILLING_MONTH"));
        		meterReadingDto.setYear(resultSet.getString("BILLING_YEAR"));
        		meterReadingDto.setPrev_reading(resultSet.getLong("PREV_READING"));
        		meterReadingDto.setPrev_reading_date(resultSet.getString("PREV_READING_DATE"));
        		meterReadingDto.setCurr_reading(resultSet.getLong("CURR_READING"));
        		meterReadingDto.setCurr_reading_date(resultSet.getString("CURR_READING_DATE"));
        		meterReadingDto.setDifference(resultSet.getLong("DIFFERENCE"));
        		meterReadingDto.setPressure(resultSet.getFloat("PRESSURE"));
        		meterReadingDto.setPressure_factor(resultSet.getFloat("PRESSURE_FACTOR"));
        		meterReadingDto.setActual_consumption(resultSet.getFloat("ACTUAL_CONSUMPTION"));
        		meterReadingDto.setMeter_rent(resultSet.getFloat("METER_RENT"));
        		meterReadingDto.setMeter_id(resultSet.getString("METER_ID"));
        		meterReadingDto.setMax_load(resultSet.getFloat("PMAX_LOAD"));
        		meterReadingDto.setMin_load(resultSet.getFloat("PMIN_LOAD"));    		
        		
        		
        		
        		
        		
   		
        		meterReadingInfo.add(meterReadingDto);
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return meterReadingInfo;
	}
	
	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList() {
		return customerCategoryList;
	}



	
	public void setCustomerCategoryList(ArrayList<CustomerCategoryDTO> customerCategoryList) {
		this.customerCategoryList = customerCategoryList;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public String getCustomer_category() {
		return customer_category;
	}


	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
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


	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}


	public String getCategory_name() {
		return category_name;
	}


	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}


	public String getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getReport_for2() {
		return report_for2;
	}

	public void setReport_for2(String report_for2) {
		this.report_for2 = report_for2;
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

	public String getReading_month() {
		return reading_month;
	}

	public void setReading_month(String reading_month) {
		this.reading_month = reading_month;
	}

	public String getReading_year() {
		return reading_year;
	}

	public void setReading_year(String reading_year) {
		this.reading_year = reading_year;
	}


	


	
  }



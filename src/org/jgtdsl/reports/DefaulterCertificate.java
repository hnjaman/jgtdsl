package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.SecurityRequireReportDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;
import org.jgtdsl.utils.connection.TransactionManager;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;

public class DefaulterCertificate extends ActionSupport implements ServletContextAware{

	ClearnessDTO clearnessDTO = new ClearnessDTO();
	ArrayList<ClearnessDTO> dueMonthList=new ArrayList<ClearnessDTO>();
	ArrayList<ClearnessDTO> customerList=new ArrayList<ClearnessDTO>();
	ClearnessDTO cto = new ClearnessDTO();
	
	private static final long serialVersionUID = 8854240739341830184L;
	private String customer_id;
	
	private  String report_for="area_wise";
	private  String download_type;
	private  String print_type;
	private  String area;
    private  String from_date;
    private  String from_customer_id;
    private  String to_customer_id;
    private  String customer_category;
    private  String customer_type;
    private  String calender_year;
    private  String collection_year;
    private  String collection_month;
    private  String officer_name;
    private  String officer_desig;
  

	static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
    
    
	UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
	Connection conn = ConnectionManager.getConnection();
	
	private ServletContext servlet;
	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}


	public String execute() throws Exception
	{	
		
	
		HttpServletResponse response = ServletActionContext.getResponse();
		
		
		PdfReader reader =null;
		ByteArrayOutputStream certificate = null;
		List<PdfReader> readers = null;
		String realPath = "";		
		String picPath = "";	
		Document document = new Document();
		ByteArrayOutputStream out = null;
		document.setPageSize(PageSize.A4);
		document.setMargins(10, 10, 10, 10);
		//left,right,top,bottom
		String fileName="";
		readers = new ArrayList<PdfReader>();
		
		//int range = Integer.parseInt(rang2)-Integer.parseInt(rang1)+1;
		
		
		if(download_type.equals("individual_wise")){             //////// Individual Report generate korle History te Data felano jabe na
			ClearnessDTO cDto = new ClearnessDTO();
			cDto.setCustomerID(customer_id);
			customerList.add(cDto);
		}else if(download_type.equals("category_wise")){
			customerList = getCustomerList(from_customer_id,to_customer_id,customer_type,customer_category,area);	
		}
		
	
			for(int a=0;a<customerList.size();a++){
				try
				{	
				if(report_for.equals("area_wise")){
					
					cto = getPayDate();
					
					if(download_type.equals("individual_wise")){
						clearnessDTO = getCustomerInfo(customer_id);
						dueMonthList=getDueMonth(customer_id);
					}else{
					
					
					clearnessDTO = getCustomerInfo(customerList.get(a).getCustomerID());
					dueMonthList=getDueMonth(customerList.get(a).getCustomerID());
					}
						
					picPath = servlet.getRealPath("/resources/images/logo.png");	
					
					int listSize=dueMonthList.size();
					
							
							if(listSize==0){
										
								if(collection_month==null && calender_year==null ){
									
									if(print_type.equals("01")){
										realPath = servlet.getRealPath("/resources/staticPdf/CCertificate.pdf");
									}else if(print_type.equals("02")){
										realPath = servlet.getRealPath("/resources/staticPdf/Printable.pdf");
									}
									
									//realPath = servlet.getRealPath("/resources/staticPdf/CCertificate.pdf");		
									document = new Document();
									out = new ByteArrayOutputStream();
									//left,right,top,bottom
									fileName="ClearnessCertificate.pdf";			
									
									
									BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
									
									
												
									reader = new PdfReader(new FileInputStream(realPath));
									certificate = new ByteArrayOutputStream();
									PdfStamper stamp = new PdfStamper(reader,certificate);
									PdfContentByte over;
									over = stamp.getOverContent(1);
									
									over.beginText();
									
									String dates=clearnessDTO.getPrattayanDate();
									String [] newDate=dates.split("-");
									
									//String day=newDate[0].toString();
									String month=newDate[1].toString();
									String year="20"+newDate[0].toString();
									
										double bqty=clearnessDTO.getBurner();
									
										String burnerQty=Double.toString(bqty);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(470, 749);
										over.showText(clearnessDTO.getTodaysDate());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(198, 710);
										over.showText(clearnessDTO.getTodaysDate());
										
										over.setFontAndSize(bf, 9);
										over.setTextMatrix(73, 689);
										over.showText(Month.values()[Integer.valueOf(month)-2]+","+year);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(80, 595);
										over.showText(clearnessDTO.getCustomerID());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(245, 595);
										over.showText(burnerQty);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(93, 506);
										over.showText(clearnessDTO.getCustomerName());
																		
										/*-----------------------------------------second part-----------------------------------------------*/
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(470, 325);
										over.showText(clearnessDTO.getTodaysDate());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(198, 285);
										over.showText(clearnessDTO.getTodaysDate());
										
										
										
										over.setFontAndSize(bf, 9);
										over.setTextMatrix(73, 265);
										over.showText(Month.values()[Integer.valueOf(month)-2]+","+year);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(80, 175);
										over.showText(clearnessDTO.getCustomerID());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(245, 175);
										over.showText(burnerQty);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(93, 70);
										over.showText(clearnessDTO.getCustomerName());
										
										Image image = Image.getInstance(picPath);
										image.scaleAbsolute(40, 50);
										image.setAbsolutePosition(100, 350);
										image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
										over.addImage(image);
										
										image.scaleAbsolute(40, 50);
										image.setAbsolutePosition(100, 770);
										image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
										over.addImage(image);
										
										if(clearnessDTO.getFathersName().equalsIgnoreCase("N/A") || clearnessDTO.getFathersName()==null){
											ColumnText ct = new ColumnText(over);						
											ct.setSimpleColumn(new Phrase(new Chunk(clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
													70, 500, 380, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
											ct.go();
											
											ColumnText ct1 = new ColumnText(over);						
											ct1.setSimpleColumn(new Phrase(new Chunk(clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
													70, 65, 380, 50, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
											ct1.go();
										}else{
											ColumnText ct = new ColumnText(over);						
											ct.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
													70, 500, 380, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
											ct.go();
											
											ColumnText ct1 = new ColumnText(over);						
											ct1.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
													70, 65, 380, 50, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
											ct1.go();
										}
										
										
										
										//////////////////////////////////////////////////////////////////////
										
										
															//top-leftX,top-leftY,?,?,lineHeight			
										
														
									
									over.endText();	
									
									stamp.close();
									readers.add(new PdfReader(certificate.toByteArray()));
									//insertClarificationHistory(customerList.get(a).getCustomerID(),from_date,loggedInUser.getUserId(),1);
									
								}else{
								
									if(print_type.equals("01")){
										realPath = servlet.getRealPath("/resources/staticPdf/CCertificate.pdf");
									}else if(print_type.equals("02")){
										realPath = servlet.getRealPath("/resources/staticPdf/Printable.pdf");
									}
									
										//realPath = servlet.getRealPath("/resources/staticPdf/CCertificate.pdf");		
										document = new Document();
										out = new ByteArrayOutputStream();
										//left,right,top,bottom
										fileName="ClearnessCertificate.pdf";			
										
										
										BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
										
										
													
										reader = new PdfReader(new FileInputStream(realPath));
										certificate = new ByteArrayOutputStream();
										PdfStamper stamp = new PdfStamper(reader,certificate);
										PdfContentByte over;
										over = stamp.getOverContent(1);
										
										over.beginText();
										
											double bqty=clearnessDTO.getBurner();
										
											String burnerQty=Double.toString(bqty);
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(231, 767);
											over.showText("Revenue Section : "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]));
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(470, 749);
											over.showText(clearnessDTO.getTodaysDate());
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(198, 710);
											over.showText(clearnessDTO.getTodaysDate());
											
											over.setFontAndSize(bf, 9);
											over.setTextMatrix(73, 689);
											over.showText(Month.values()[Integer.valueOf(collection_month)-1]+","+calender_year);
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(80, 595);
											over.showText(clearnessDTO.getCustomerID());
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(245, 595);
											over.showText(burnerQty);
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(93, 506);
											over.showText(clearnessDTO.getCustomerName());
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(440, 490);
											over.showText(officer_name);
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(440, 475);
											over.showText(officer_desig);
											
											/*-----------------------------------------second part-----------------------------------------------*/
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(231, 340);
											over.showText("Revenue Section : "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]));
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(470, 325);
											over.showText(clearnessDTO.getTodaysDate());
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(198, 285);
											over.showText(clearnessDTO.getTodaysDate());
											
											
											
											over.setFontAndSize(bf, 9);
											over.setTextMatrix(73, 265);
											over.showText(Month.values()[Integer.valueOf(collection_month)-1]+","+calender_year);
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(80, 175);
											over.showText(clearnessDTO.getCustomerID());
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(245, 175);
											over.showText(burnerQty);
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(93, 70);
											over.showText(clearnessDTO.getCustomerName());
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(440, 57);
											over.showText(officer_name);
											
											over.setFontAndSize(bf, 11);
											over.setTextMatrix(440, 43);
											over.showText(officer_desig);
											
											Image image = Image.getInstance(picPath);
											image.scaleAbsolute(40, 50);
											image.setAbsolutePosition(80, 770);
											image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
											over.addImage(image);
											
											image.scaleAbsolute(40, 50);
											image.setAbsolutePosition(80, 352);
											image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
											over.addImage(image);
											
											if(clearnessDTO.getFathersName().equalsIgnoreCase("N/A") || clearnessDTO.getFathersName()==null){
											ColumnText ct = new ColumnText(over);						
											ct.setSimpleColumn(new Phrase(new Chunk(clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
													70, 500, 380, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
											ct.go();
																	
											ColumnText ct1 = new ColumnText(over);						
											ct1.setSimpleColumn(new Phrase(new Chunk(clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
													70, 65, 380, 50, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
											ct1.go();
											}else{
												ColumnText ct = new ColumnText(over);						
												ct.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
														70, 500, 380, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
												ct.go();
																		
												ColumnText ct1 = new ColumnText(over);						
												ct1.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
														70, 65, 380, 50, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
												ct1.go();
											}
											
											//////////////////////////////////////////////////////////////////////
											
											
																//top-leftX,top-leftY,?,?,lineHeight			
											
															
										
										over.endText();	
										
										stamp.close();
										readers.add(new PdfReader(certificate.toByteArray()));
										insertClarificationHistory(customerList.get(a).getCustomerID(),from_date,loggedInUser.getUserId(),1);		// 1=No Dues		
									}
								}
							
							
							
							if(listSize!=0){
								
								if(collection_month==null && calender_year==null){
									
									realPath = servlet.getRealPath("/resources/staticPdf/ClearnessCertificate.pdf");		
									document = new Document();
									out = new ByteArrayOutputStream();
									//left,right,top,bottom
									fileName="ClearnessCertificate.pdf";			
									
									
									BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
									
									String dates=clearnessDTO.getPrattayanDate();
									String [] newDate=dates.split("-");
									
									//String day=newDate[0].toString();
									String month=newDate[1].toString();
									String year="20"+newDate[0].toString();
												
									reader = new PdfReader(new FileInputStream(realPath));
									certificate = new ByteArrayOutputStream();
									PdfStamper stamp = new PdfStamper(reader,certificate);
									PdfContentByte over;
									over = stamp.getOverContent(1);
									
									over.beginText();
									
									String duesString="";
									String dueMonth="";
									
									double totaldueAmount=0.00;
									for (int i = 0; i < listSize; i++) {
										duesString=duesString+","+dueMonthList.get(i).getDueMonth();
										
										
										totaldueAmount = totaldueAmount+dueMonthList.get(i).getDueAmount();
										
										
										
										if(i==listSize-1)	{
											
										dueMonth=duesString.substring(1);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(475, 752);
										over.showText(clearnessDTO.getTodaysDate());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(160, 714);
										over.showText(clearnessDTO.getTodaysDate());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(475, 714);
										over.showText(clearnessDTO.getCustomerID());
										
										over.setFontAndSize(bf, 10);
										over.setTextMatrix(74, 698);
										over.showText(Month.values()[Integer.valueOf(month)-2]+","+year);

										
										double bqty=clearnessDTO.getBurner();
										
										String burnerQty=Double.toString(bqty);
										
										String areaId=clearnessDTO.getCustomerID();
										String area=areaId.substring(0,2);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(310, 610);
										over.showText(String.valueOf(clearnessDTO.getBurner()));
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(426, 610);
										over.showText(String.valueOf(taka_format.format(totaldueAmount)));
//										over.showText("ABCDEFG");
										
										String areaName=String.valueOf(Area.values()[Integer.valueOf(area)-1]);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(277, 542);
										over.showText(areaName);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(94, 473);
										over.showText(clearnessDTO.getCustomerName());										
										
										/* Second part here*/
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(475, 332);
										over.showText(clearnessDTO.getTodaysDate());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(160, 294);
										over.showText(clearnessDTO.getTodaysDate());
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(475, 294);
										over.showText(clearnessDTO.getCustomerID());
										
										over.setFontAndSize(bf, 10);
										over.setTextMatrix(74, 278);
										over.showText(Month.values()[Integer.valueOf(month)-2]+","+year);
										
										
									
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(310, 190);
										over.showText(burnerQty);
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(426, 190);
										over.showText(String.valueOf(taka_format.format(totaldueAmount)));
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(277, 122);
										over.showText(String.valueOf(Area.values()[Integer.valueOf(area)-1]));
										
										over.setFontAndSize(bf, 11);
										over.setTextMatrix(94, 53);
										over.showText(clearnessDTO.getCustomerName());	
										
										Image image = Image.getInstance(picPath);
										image.scaleAbsolute(40, 50);
										image.setAbsolutePosition(110, 770);
										image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
										over.addImage(image);
										
										//Image image = Image.getInstance(picPath);
										image.scaleAbsolute(40, 50);
										image.setAbsolutePosition(110, 352);
										image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
										over.addImage(image);
										
										ColumnText ct = new ColumnText(over);
										
										ct.setSimpleColumn(new Phrase(new Chunk(dueMonth, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
												50, 630, 295, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
										ct.go();
										
										//ColumnText ct = new ColumnText(over);						
										ct.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
												75, 470, 370, 50, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);
										ct.go();
														
										ct.setSimpleColumn(new Phrase(new Chunk(dueMonth, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
												50, 210, 295, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
										ct.go();
											
										ct.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
												75, 2, 370, 50, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);
										ct.go();
										
										}
										
										
											
									}
									over.endText();	
									
									stamp.close();
									readers.add(new PdfReader(certificate.toByteArray()));
									//insertClarificationHistory(customerList.get(a).getCustomerID(),from_date,loggedInUser.getUserId(),0);  // 0= Dues 
								}else{
							
									if(print_type.equals("01")){
										realPath = servlet.getRealPath("/resources/staticPdf/ClearnessCertificate.pdf");
									}else if(print_type.equals("02")){
										realPath = servlet.getRealPath("/resources/staticPdf/Printable.pdf");
									}
											//realPath = servlet.getRealPath("/resources/staticPdf/ClearnessCertificate.pdf");		
											document = new Document();
											out = new ByteArrayOutputStream();
											//left,right,top,bottom
											fileName="ClearnessCertificate.pdf";			
											
											
											BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
											
											
														
											reader = new PdfReader(new FileInputStream(realPath));
											certificate = new ByteArrayOutputStream();
											PdfStamper stamp = new PdfStamper(reader,certificate);
											PdfContentByte over;
											over = stamp.getOverContent(1);
											
											over.beginText();
											
											String duesString="";
											String dueMonth="";
											
											double totaldueAmount=0.00;
											for (int i = 0; i < listSize; i++) {
												duesString=duesString+","+dueMonthList.get(i).getDueMonth();
												
												
												totaldueAmount = totaldueAmount+dueMonthList.get(i).getDueAmount();
												
												
												
												if(i==listSize-1)	{
													
												dueMonth=duesString.substring(1);
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(231, 767);
												over.showText("Revenue Section : "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]));
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(475, 752);
												over.showText(clearnessDTO.getTodaysDate());
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(160, 714);
												over.showText(clearnessDTO.getTodaysDate());
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(475, 714);
												over.showText(clearnessDTO.getCustomerID());
												
												over.setFontAndSize(bf, 10);
												over.setTextMatrix(74, 698);
												over.showText(Month.values()[Integer.valueOf(collection_month)-1]+","+calender_year);

												
												double bqty=clearnessDTO.getBurner();
												
												String burnerQty=Double.toString(bqty);
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(310, 610);
												over.showText(String.valueOf(clearnessDTO.getBurner()));
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(426, 610);
												over.showText(String.valueOf(taka_format.format(totaldueAmount)));
//												over.showText("ABCDEFG");
												
												String areaName="";
												
												if(area.equals("02") || area.equals("06") || area.equals("07") || area.equals("08") || area.equals("09")){
													areaName="BAGHABARI";
												}else if(area.equals("01")){
													areaName="SIRAJGANJ";
												}else if(area.equals("03")){
													areaName="PABNA";
												}else if(area.equals("04")){
													areaName="ISHWARDI";
												}else if(area.equals("05")){
													areaName="BOGRA";
												}else if(area.equals("10")){
													areaName="RAJSHAHI";
												}
												
												
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(277, 542);
												over.showText(areaName);
												
//												over.setFontAndSize(bf, 11);
//												over.setTextMatrix(277, 542);
//												over.showText(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]));
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(94, 473);
												over.showText(clearnessDTO.getCustomerName());
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(460, 460);
												over.showText(officer_name);
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(460, 446);
												over.showText(officer_desig);
																
												
												
												
												/* Second part here*/
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(231, 347);
												over.showText("Revenue Section : "+String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]));
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(475, 332);
												over.showText(clearnessDTO.getTodaysDate());
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(160, 294);
												over.showText(clearnessDTO.getTodaysDate());
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(475, 294);
												over.showText(clearnessDTO.getCustomerID());
												
												over.setFontAndSize(bf, 10);
												over.setTextMatrix(74, 278);
												over.showText(Month.values()[Integer.valueOf(collection_month)-1]+","+calender_year);
												
												
											
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(310, 190);
												over.showText(burnerQty);
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(426, 190);
												over.showText(String.valueOf(taka_format.format(totaldueAmount)));							
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(277, 122);
												over.showText(areaName);
												
//												over.setFontAndSize(bf, 11);
//												over.setTextMatrix(277, 122);
//												over.showText(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]));
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(94, 53);
												over.showText(clearnessDTO.getCustomerName());
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(460, 40);
												over.showText(officer_name);
												
												over.setFontAndSize(bf, 11);
												over.setTextMatrix(460, 28);
												over.showText(officer_desig);
												
												
												Image image = Image.getInstance(picPath);
												image.scaleAbsolute(40, 50);
												image.setAbsolutePosition(110, 770);
												image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
												over.addImage(image);
												
												//Image image = Image.getInstance(picPath);
												image.scaleAbsolute(40, 50);
												image.setAbsolutePosition(110, 352);
												image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
												over.addImage(image);
												
												ColumnText ct = new ColumnText(over);
												
												ct.setSimpleColumn(new Phrase(new Chunk(dueMonth, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
														50, 630, 295, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
												ct.go();
												if(clearnessDTO.getFathersName().equalsIgnoreCase("N/A") || clearnessDTO.getFathersName()==null){
												//ColumnText ct = new ColumnText(over);						
												ct.setSimpleColumn(new Phrase(new Chunk(clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
														75, 470, 370, 50, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);
												ct.go();
												
												ct.setSimpleColumn(new Phrase(new Chunk(clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
														75, 2, 370, 50, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);
												ct.go();
												}else{
													ct.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
															75, 470, 370, 50, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);
													ct.go();
													
													ct.setSimpleColumn(new Phrase(new Chunk("C/O-"+clearnessDTO.getFathersName()+","+clearnessDTO.getCustomerAddress()+", "+clearnessDTO.getPhoneNo(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL))),
															75, 2, 370, 50, 10, Element.ALIGN_LEFT | Element.ALIGN_TOP);
													ct.go();
												}
																
												ct.setSimpleColumn(new Phrase(new Chunk(dueMonth, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
														50, 210, 295, 60, 15, Element.ALIGN_LEFT | Element.ALIGN_TOP);
												ct.go();
													
												
												
												}
												
												
													
											}
											over.endText();	
											
											stamp.close();
											readers.add(new PdfReader(certificate.toByteArray()));
											insertClarificationHistory(customerList.get(a).getCustomerID(),from_date,loggedInUser.getUserId(),0);  // 0= Dues 
								}
							
				
							
							}	
							
			
			}	
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
			
		
			}
			
			
		
		
		if(readers.size()>0)
		{
			PdfWriter writer = PdfWriter.getInstance(document, out);
			
			document.open();
			
			PdfContentByte cb = writer.getDirectContent();
			PdfReader pdfReader = null;
			PdfImportedPage page;
			
			for(int k=0;k<readers.size();k++)
			{
				document.newPage();
				pdfReader = readers.get(k);
				page = writer.getImportedPage(pdfReader, 1);
				cb.addTemplate(page, 0, 0);
			}
			

			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(out, response,fileName);
			document=null;	
					
		}
		
		
		
		return null;	
		
	}

	public void insertClarificationHistory(String cust_id,String issue_date,String insert_by,int dues_status){
		ResponseDTO response=new ResponseDTO();
		TransactionManager transactionManager=new TransactionManager();
		Connection conn = transactionManager.getConnection();
		
//		response=validateReconnInfo(reconn,disconn);
//		if(response.isResponse()==false)
//			return response;
		
		
		String sqlInsert="INSERT INTO CLARIFICATION_HISTORY ( " +
							"   CUSTOMER_ID, CALENDER_YEAR, ISSUE_DATE,  " +
							"   STATUS, DUES_STATUS, INSERTED_ON,  " +
							"   INSERTED_BY)  " +
							"   VALUES ( ?,?,sysdate,?,?,sysdate,?)" ;

		
		
		
		String checkIsAvailable="Select count(customer_id) CUS_COUNT from CLARIFICATION_HISTORY where CALENDER_YEAR=? and customer_id=?";

		PreparedStatement stmt = null;
		ResultSet r = null;
		int count=0;
		

			try
			{
				stmt= conn.prepareStatement(checkIsAvailable);
				stmt.setString(1,calender_year);
				stmt.setString(2,cust_id);
				r = stmt.executeQuery();
				if (r.next())
					count=r.getInt("CUS_COUNT"); 
				
				
				if(count==0){
					stmt = conn.prepareStatement(sqlInsert);
					stmt.setString(1,cust_id);
					stmt.setString(2,calender_year);
					stmt.setInt(3,1); /// 1 means all generated(approved)
					stmt.setInt(4,dues_status);
					stmt.setString(5,insert_by);
					stmt.execute();
				}
				
				transactionManager.commit();	

			} 
			
			catch (Exception e){
				response.setMessasge(e.getMessage());
				response.setResponse(false);
				e.printStackTrace();
					try {
						transactionManager.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
	 		finally{try{stmt.close();transactionManager.close();} catch (Exception e)
				{e.printStackTrace();}stmt = null;conn = null;}

	 		return;
	}

	public ArrayList<ClearnessDTO> getDueMonth(String customer_id){
		ArrayList<ClearnessDTO> dueMonth = new ArrayList<ClearnessDTO>();
		
		try {
			if(collection_month==null){
				
				String transaction_sql="SELECT customer_id, BILL_ID, BILL_MONTH, BILL_YEAR, TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR,PAYABLE_AMOUNT, " +
						"0 PAID_AMOUNT, PAYABLE_AMOUNT DUE_AMOUNT  " +
						"						FROM BILL_METERED  " +
						"						WHERE status = 1 AND customer_id ='"+customer_id+"'  " +
						"						UNION  " +
						"						SELECT customer_id,  " +
						"						BILL_ID,  " +
						"						BILL_MONTH,  " +
						"						BILL_YEAR,TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR, " +
						"						ACTUAL_PAYABLE_AMOUNT PAYABLE_AMOUNT,  " +
						"						NVL(COLLECTED_PAYABLE_AMOUNT,0) PAID_AMOUNT,  " +
						"						ACTUAL_PAYABLE_AMOUNT - NVL(COLLECTED_PAYABLE_AMOUNT,0) DUE_AMOUNT  " +
						"						FROM BILL_NON_METERED  " +
						"						WHERE customer_id ='"+customer_id+"' AND STATUS = 1  " ;

						
						
				PreparedStatement ps1=conn.prepareStatement(transaction_sql);
			
	        	
	        	ResultSet resultSet=ps1.executeQuery();
	        	
	        	
	        	while(resultSet.next())
	        	{
	        		ClearnessDTO cDto = new ClearnessDTO();
	        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
	        		cDto.setDueMonth(resultSet.getString("MONTH_YEAR"));
	        		cDto.setBillYear(resultSet.getString("BILL_YEAR"));
	        		cDto.setBillMonth(resultSet.getString("BILL_MONTH"));
	        		cDto.setDueAmount(resultSet.getDouble("DUE_AMOUNT"));
	        		dueMonth.add(cDto);
	        		
	        	}
			}else{
			int month=Integer.parseInt(collection_month);
			String billMonth="";
			if(month<10){
				billMonth="0"+Integer.toString(month);
			}else{
				billMonth=Integer.toString(month);
			}
			
			
			String transaction_sql="SELECT customer_id, BILL_ID, BILL_MONTH, BILL_YEAR, TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR,PAYABLE_AMOUNT, " +
					"0 PAID_AMOUNT, PAYABLE_AMOUNT DUE_AMOUNT  " +
					"						FROM BILL_METERED  " +
					"						WHERE status = 1 AND BILL_YEAR||lpad(bill_month,2,0) <= "+calender_year+billMonth+" AND customer_id ='"+customer_id+"'  " +
					"						UNION  " +
					"						SELECT customer_id,  " +
					"						BILL_ID,  " +
					"						BILL_MONTH,  " +
					"						BILL_YEAR,TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR, " +
					"						BILLED_AMOUNT PAYABLE_AMOUNT,  " +
					"						NVL(COLLECTED_BILLED_AMOUNT,0) PAID_AMOUNT,  " +
					"						BILLED_AMOUNT - NVL(COLLECTED_BILLED_AMOUNT,0) DUE_AMOUNT  " +
					"						FROM BILL_NON_METERED  " +
					"						WHERE customer_id ='"+customer_id+"' AND STATUS = 1 AND BILL_YEAR||lpad(bill_month,2,0) <= "+calender_year+billMonth+" " ;

					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		ClearnessDTO cDto = new ClearnessDTO();
        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		cDto.setDueMonth(resultSet.getString("MONTH_YEAR"));
        		cDto.setBillYear(resultSet.getString("BILL_YEAR"));
        		cDto.setDueAmount(resultSet.getDouble("DUE_AMOUNT"));
        		dueMonth.add(cDto);
        		
        	}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return dueMonth;
	}
	
	public ArrayList<ClearnessDTO> getDueMonthWeb(String customer_id){
		ArrayList<ClearnessDTO> dueMonth = new ArrayList<ClearnessDTO>();
		
		try {
			String transaction_sql="SELECT customer_id, BILL_ID, BILL_MONTH, BILL_YEAR, TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR,PAYABLE_AMOUNT, " +
					"0 PAID_AMOUNT, PAYABLE_AMOUNT DUE_AMOUNT,  " +
					"						SURCHARGE_AMOUNT SURCHARGE"+
					"						FROM BILL_METERED  " +
					"						WHERE status = 1 AND customer_id ='"+customer_id+"'  " +
					"						UNION  " +
					"						SELECT customer_id,  " +
					"						BILL_ID,  " +
					"						BILL_MONTH,  " +
					"						BILL_YEAR,TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR, " +
					"						BILLED_AMOUNT PAYABLE_AMOUNT,  " +
					"						NVL(COLLECTED_BILLED_AMOUNT,0) PAID_AMOUNT,  " +
					"						BILLED_AMOUNT - NVL(COLLECTED_BILLED_AMOUNT,0) DUE_AMOUNT,  " +
					"						calcualteSurcharge(BILL_ID,to_char(sysdate,'dd-mm-yyyy')) SURCHARGE" +
					"						FROM BILL_NON_METERED  " +
					"						WHERE customer_id ='"+customer_id+"' AND STATUS = 1  " ;

					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		ClearnessDTO cDto = new ClearnessDTO();
        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		cDto.setDueMonth(resultSet.getString("MONTH_YEAR"));
        		cDto.setBillYear(resultSet.getString("BILL_YEAR"));
        		cDto.setBillMonth(resultSet.getString("BILL_MONTH"));
        		cDto.setDueAmount(resultSet.getDouble("DUE_AMOUNT"));
        		cDto.setDueSurcharge(resultSet.getDouble("SURCHARGE"));
        		cDto.setBillId(resultSet.getString("BILL_ID"));			// for online bill payment select checkbox, sujon
        		dueMonth.add(cDto);
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return dueMonth;
	}
	public ArrayList<ClearnessDTO> getDueMonthWeb(String customer_id, String billIds){
		ArrayList<ClearnessDTO> dueMonth = new ArrayList<ClearnessDTO>();
		String billIdClause = billIds.equals("")?"":" and Bill_Id in ("+billIds+")";
		
		try {
			String transaction_sql="select customer_id, BILL_ID, BILL_MONTH, BILL_YEAR,MONTH_YEAR,PAYABLE_AMOUNT,PAID_AMOUNT,DUE_AMOUNT,round((PAYABLE_AMOUNT*12*daydiff)/(100*365),0) SURCHARGE from( " +
					"SELECT customer_id, BILL_ID, BILL_MONTH, BILL_YEAR, TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON')||''''|| SUBSTR (BILL_YEAR, 3) MONTH_YEAR,PAYABLE_AMOUNT,  " +
					"0 PAID_AMOUNT, PAYABLE_AMOUNT DUE_AMOUNT ,to_date(to_char(sysdate),'dd-mm-yyyy')-to_date(to_char(LAST_PAY_DATE_W_SC),'dd-mm-yyyy') daydiff  " +
					"						FROM BILL_METERED   " +
					"						WHERE status = 1 AND customer_id ='"+customer_id+"' "+billIdClause+") " +
					"UNION " +
					"SELECT customer_id, " +
					"       BILL_ID, " +
					"       BILL_MONTH, " +
					"       BILL_YEAR, " +
					"          TO_CHAR (TO_DATE (BILL_MONTH, 'MM'), 'MON') " +
					"       || '''' " +
					"       || SUBSTR (BILL_YEAR, 3) " +
					"          MONTH_YEAR, " +
					"       BILLED_AMOUNT PAYABLE_AMOUNT, " +
					"       NVL (COLLECTED_BILLED_AMOUNT, 0) PAID_AMOUNT, " +
					"       BILLED_AMOUNT - NVL (COLLECTED_BILLED_AMOUNT, 0) DUE_AMOUNT,calcualteSurcharge (BILL_ID, TO_CHAR (SYSDATE, 'dd-mm-yyyy')) SURCHARGE " +
					"  FROM BILL_NON_METERED " +
					" WHERE customer_id ='"+customer_id+"' AND STATUS = 1  "+billIdClause ;
					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		ClearnessDTO cDto = new ClearnessDTO();
        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		cDto.setDueMonth(resultSet.getString("MONTH_YEAR"));
        		cDto.setBillYear(resultSet.getString("BILL_YEAR"));
        		cDto.setBillMonth(resultSet.getString("BILL_MONTH"));
        		cDto.setDueAmount(resultSet.getDouble("DUE_AMOUNT"));
        		cDto.setBillId(resultSet.getString("BILL_ID"));
        		cDto.setDueSurcharge(resultSet.getDouble("SURCHARGE"));
        		dueMonth.add(cDto);
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return dueMonth;
	}
	
	private ClearnessDTO getCustomerInfo(String customer_id) {
		ClearnessDTO ctrInfo = new ClearnessDTO();
		
		try {
		
			
			String customer_info_sql="SELECT CUSTOMER_ID, " +
					"       FULL_NAME, " +
					"       FATHER_NAME, " +
					"       ADDRESS_LINE1,PHONE, " +
					"       SDATE,to_date(sysdate,'dd-mm-yyyy') prattayandate, " +
					"       NEXTDAY, " +
					"       PYEAR,sum(NEW_DOUBLE_BURNER_QNT_BILLCAL) NEW_DOUBLE_BURNER_QNT_BILLCAL,sum(MAX_LOAD) MAX_LOAD from( " +
					"SELECT CUSTOMER_ID, " +
					"       FULL_NAME, " +
					"       FATHER_NAME, " +
					"       ADDRESS_LINE1,PHONE, " +
					"       SDATE, " +
					"       NEXTDAY, " +
					"       PYEAR, " +
					"       NEW_DOUBLE_BURNER_QNT_BILLCAL, " +
					"       MAX_LOAD " +
					"  FROM (SELECT cp.CUSTOMER_ID, " +
					"               cp.FULL_NAME, " +
					"               cp.FATHER_NAME, " +
					"               cp.ADDRESS_LINE1,cp.MOBILE PHONE, " +
					"               TO_CHAR (SYSDATE) SDATE, " +
					"               TO_CHAR (SYSDATE + 20) NEXTDAY, " +
					"               EXTRACT (YEAR FROM SYSDATE) - 1 PYEAR, " +
					"               NEW_DOUBLE_BURNER_QNT_BILLCAL, " +
					"               NVL(MAX_LOAD,0) MAX_LOAD " +
					"          FROM (SELECT NEW_DOUBLE_BURNER_QNT_BILLCAL, Customer_id " +
					"                  FROM burner_qnt_change " +
					"                 WHERE pid IN (SELECT MAX (PID) " +
					"                                 FROM burner_qnt_change " +
					"                                WHERE customer_id = ?)) tab1, " +
					"               MVIEW_CUSTOMER_INFO cp " +
					"         WHERE CP.CUSTOMER_ID = TAB1.CUSTOMER_ID) " +
					"UNION ALL " +
					"SELECT CUSTOMER_ID, " +
					"       FULL_NAME, " +
					"       FATHER_NAME, " +
					"       ADDRESS_LINE1,PHONE, " +
					"       SDATE, " +
					"       NEXTDAY, " +
					"       PYEAR, " +
					"       nvl(NEW_DOUBLE_BURNER_QNT_BILLCAL,0) NEW_DOUBLE_BURNER_QNT_BILLCAL, " +
					"       MAX_LOAD " +
					"  FROM (SELECT CUSTOMER_ID, " +
					"               FULL_NAME, " +
					"               FATHER_NAME, " +
					"               ADDRESS_LINE1,MOBILE PHONE, " +
					"               TO_CHAR (SYSDATE) SDATE, " +
					"               TO_CHAR (SYSDATE + 20) NEXTDAY, " +
					"               EXTRACT (YEAR FROM SYSDATE) - 1 PYEAR, " +
					"               NULL NEW_DOUBLE_BURNER_QNT_BILLCAL, " +
					"               MAX_LOAD " +
					"          FROM MVIEW_CUSTOMER_INFO " +
					"         WHERE CUSTOMER_ID = ?)) " +
					" group by CUSTOMER_ID,FULL_NAME,FATHER_NAME,ADDRESS_LINE1,SDATE,NEXTDAY,PYEAR,PHONE " ;

					
					
					
					
					
					
					
					
					
//					
//					"select CUSTOMER_ID,FULL_NAME,FATHER_NAME,ADDRESS_LINE1, SDATE,NEXTDAY, PYEAR, NEW_DOUBLE_BURNER_QNT_BILLCAL,MAX_LOAD from( " +
//					"SELECT cp.CUSTOMER_ID, " +
//					"       cp.FULL_NAME, " +
//					"       cp.FATHER_NAME, " +
//					"       cp.ADDRESS_LINE1, " +
//					"       TO_CHAR (SYSDATE) SDATE, " +
//					"       TO_CHAR (SYSDATE + 20) NEXTDAY, " +
//					"       EXTRACT (YEAR FROM SYSDATE) - 1 PYEAR, " +
//					"       NEW_DOUBLE_BURNER_QNT_BILLCAL,null MAX_LOAD " +
//					"  FROM (SELECT NEW_DOUBLE_BURNER_QNT_BILLCAL, Customer_id " +
//					"          FROM burner_qnt_change " +
//					"         WHERE pid IN (SELECT MAX (PID) " +
//					"                         FROM burner_qnt_change " +
//					"                        WHERE customer_id = ?)) tab1, " +
//					"       MVIEW_CUSTOMER_INFO cp " +
//					" WHERE CP.CUSTOMER_ID = TAB1.CUSTOMER_ID) " +
//					" union all  " +
//					" select CUSTOMER_ID,FULL_NAME,FATHER_NAME,ADDRESS_LINE1, SDATE,NEXTDAY, PYEAR, NEW_DOUBLE_BURNER_QNT_BILLCAL,MAX_LOAD from( " +
//					" select CUSTOMER_ID,FULL_NAME,FATHER_NAME,ADDRESS_LINE1,TO_CHAR(SYSDATE) SDATE,TO_CHAR (SYSDATE + 20) NEXTDAY,EXTRACT (YEAR FROM SYSDATE) - 1 PYEAR,null NEW_DOUBLE_BURNER_QNT_BILLCAL,MAX_LOAD from MVIEW_CUSTOMER_INFO " +
//					" where CUSTOMER_ID= ?) " ;



			
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			ps1.setString(1, customer_id);
			ps1.setString(2, customer_id);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		
        		ctrInfo.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		ctrInfo.setCustomerName(resultSet.getString("FULL_NAME"));
        		ctrInfo.setFathersName(resultSet.getString("FATHER_NAME"));
        		ctrInfo.setCustomerAddress(resultSet.getString("ADDRESS_LINE1"));
        		ctrInfo.setTodaysDate(resultSet.getString("SDATE"));
        		ctrInfo.setNextDuesDay(resultSet.getString("NEXTDAY"));
        		ctrInfo.setPriviousYear(resultSet.getString("PYEAR"));
        		ctrInfo.setBurner(resultSet.getDouble("NEW_DOUBLE_BURNER_QNT_BILLCAL"));
        		ctrInfo.setPrattayanDate(resultSet.getString("prattayandate"));
        		ctrInfo.setPhoneNo(resultSet.getString("PHONE"));
        		
        		
        		
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ctrInfo;
	}
	
private ClearnessDTO getPayDate(){
		
	ClearnessDTO noticeInfo = new ClearnessDTO();
		try {
			String transaction_sql="Select to_char(to_date('"+from_date+"','dd-MM-YYYY')-30) PAYDATE from dual";
					
					
					//"Select to_date('"+from_date+"','dd-MM-YYYY')+21 PAYDATE from dual";
			
			
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{

        		noticeInfo.setPayDate(resultSet.getString("PAYDATE"));
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return noticeInfo;
	}
	

	private ArrayList<ClearnessDTO> getCustomerList(String from_cus_id,String to_cus_id,String cus_type,String cust_cat_id,String area){
		
		ArrayList<ClearnessDTO> custList = new ArrayList<ClearnessDTO>();
		String from_month_year=calender_year+"01";
		String to_month_year=calender_year+"12";
		String table_name="";
		if(cus_type.equals("0")) {
			table_name="Bill_non_metered";
		}else if(cus_type.equals("1")){
			table_name="Bill_metered";
		}
		try {
			String transaction_sql= "select CUSTOMER_ID,STATUS from  " +
										"(select CUSTOMER_ID,to_number(substr(CUSTOMER_ID,1,9)) CUS_ID,getDuesStatus(CUSTOMER_ID,"+calender_year+",ISMETERED) STATUS from MVIEW_CUSTOMER_INFO  " +
										"Where substr(CUSTOMER_ID,1,2)=" +area+
										"AND ISMETERED="+cus_type+
										"AND CATEGORY_ID=" +cust_cat_id+")"+
										"Where STATUS=1 and cus_id between "+from_cus_id+" and "+to_cus_id +
										"  order by CUSTOMER_ID ";

							
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
        	ResultSet resultSet=ps1.executeQuery();
        	while(resultSet.next())
        	{
        		ClearnessDTO clearnessDTO = new ClearnessDTO();
        		clearnessDTO.setCustomerID(resultSet.getString("CUSTOMER_ID"));
        		custList.add(clearnessDTO);
        	}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return custList;
		
	}
	
		
	
	public String getDownload_type() {
		return download_type;
	}

	public void setDownload_type(String download_type) {
		this.download_type = download_type;
	}

	public String getFrom_customer_id() {
		return from_customer_id;
	}

	public void setFrom_customer_id(String from_customer_id) {
		this.from_customer_id = from_customer_id;
	}

	public String getTo_customer_id() {
		return to_customer_id;
	}

	public void setTo_customer_id(String to_customer_id) {
		this.to_customer_id = to_customer_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getReport_for() {
		return report_for;
	}

	public void setReport_for(String report_for) {
		this.report_for = report_for;
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

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getCustomer_type() {
		return customer_type;
	}

	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}

	public String getCalender_year() {
		return calender_year;
	}

	public void setCalender_year(String calender_year) {
		this.calender_year = calender_year;
	}

	public String getCollection_year() {
		return collection_year;
	}

	public void setCollection_year(String collection_year) {
		this.collection_year = collection_year;
	}

	public String getCollection_month() {
		return collection_month;
	}

	public void setCollection_month(String collection_month) {
		this.collection_month = collection_month;
	}

	public String getOfficer_name() {
		return officer_name;
	}

	public void setOfficer_name(String officer_name) {
		this.officer_name = officer_name;
	}

	public String getOfficer_desig() {
		return officer_desig;
	}

	public void setOfficer_desig(String officer_desig) {
		this.officer_desig = officer_desig;
	}

	public String getPrint_type() {
		return print_type;
	}

	public void setPrint_type(String print_type) {
		this.print_type = print_type;
	}


		

}

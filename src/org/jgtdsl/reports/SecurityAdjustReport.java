package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.dto.SecurityRequireReportDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
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

public class SecurityAdjustReport extends ActionSupport implements ServletContextAware{

	SecurityRequireReportDTO securityInfo=new SecurityRequireReportDTO();
	SecurityRequireReportDTO dateInfo=new SecurityRequireReportDTO();
	
	private static final long serialVersionUID = 8854240739341830184L;
	private String customer_id;
    private  String from_date;
    private  String time_limit;
	
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
		Document document = new Document();
		ByteArrayOutputStream out = null;
		document.setPageSize(PageSize.A4);
		document.setMargins(10, 10, 10, 10);
		//left,right,top,bottom
		String fileName="";
		
		try
		{
		
			securityInfo=getSecurityNoticeInfo();
			
			dateInfo = getPayDate();
			String customerCat=securityInfo.getCategory_id();
			
			if(customerCat.equals("09")){
				
				readers = new ArrayList<PdfReader>();
				realPath = servlet.getRealPath("/resources/staticPdf/CNG.pdf");		
				document = new Document();
				out = new ByteArrayOutputStream();
				//left,right,top,bottom
				fileName="Security.pdf";			
				
				
				BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
				
				
							
				reader = new PdfReader(new FileInputStream(realPath));
				certificate = new ByteArrayOutputStream();
				PdfStamper stamp = new PdfStamper(reader,certificate);
				PdfContentByte over;
				over = stamp.getOverContent(1);
				
				over.beginText();		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(505, 694);
					over.showText(from_date);
						
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 652);
					over.showText(securityInfo.getCustomerName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 637);
					over.showText(securityInfo.getPropriatorName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(82, 623);
					over.showText(securityInfo.getAddress());		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(122, 609);
					over.showText(securityInfo.getCustomer_id());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(180, 609);
					over.showText("("+securityInfo.getCategory_name()+")");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(322, 522);
					over.showText("2014");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(370, 522);
					over.showText("3.5");
					
					over.setFontAndSize(bf, 10);
					over.setTextMatrix(497, 506);
					over.showText("01-03-2017");
					
					String load=Double.toString(securityInfo.getMax_load());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(525, 491);
					over.showText(load);
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(65, 386);
					over.showText(load);
					
					double tarifRate=securityInfo.getRate();
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(135, 386);
					over.showText((taka_format.format(tarifRate)));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(210, 386);
					over.showText(securityInfo.getSd_month());
					
					double sdmonth=Double.parseDouble(securityInfo.getSd_month());
					
					double securityAmount=securityInfo.getMax_load()*securityInfo.getRate()*sdmonth;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(260, 386);
					over.showText(taka_format.format(securityAmount));
					
					/*---------------------------accounting-------------------------------------------------------*/
					
					double presentAmount=securityInfo.getPaid_sequrity_deposit();
							
						over.setFontAndSize(bf, 11);
						over.setTextMatrix(350, 386);
						over.showText("0.00");
						
						over.setFontAndSize(bf, 11);
						over.setTextMatrix(410, 386);
						over.showText(taka_format.format(presentAmount));
						
						
					double exceed=securityAmount-presentAmount;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(485, 386);
					over.showText(taka_format.format(exceed));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(265, 336);
					over.showText(taka_format.format(exceed));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(226, 290);
					over.showText(taka_format.format(exceed));
					
	/*--------------------------------------Adding Days to a Particular Date----------------------------------*/				
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(399, 290);
					over.showText(dateInfo.getPayDate());
						
					
				over.endText();	
				
				stamp.close();
				
			}else if(customerCat.equals("01")|| customerCat.equals("02")){
				
				readers = new ArrayList<PdfReader>();
				realPath = servlet.getRealPath("/resources/staticPdf/domestic.pdf");		
				document = new Document();
				out = new ByteArrayOutputStream();
				//left,right,top,bottom
				fileName="Security.pdf";			
				
				
				BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
				
				
							
				reader = new PdfReader(new FileInputStream(realPath));
				certificate = new ByteArrayOutputStream();
				PdfStamper stamp = new PdfStamper(reader,certificate);
				PdfContentByte over;
				over = stamp.getOverContent(1);
				
				over.beginText();		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(505, 693);
					over.showText(from_date);
						
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 651);
					over.showText(securityInfo.getCustomerName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 636);
					over.showText(securityInfo.getPropriatorName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(82, 621);
					over.showText(securityInfo.getAddress());		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(122, 608);
					over.showText(securityInfo.getCustomer_id());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(180, 608);
					over.showText("("+securityInfo.getCategory_name()+")");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(322, 522);
					over.showText("2014");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(371, 522);
					over.showText("3.5");
					
					over.setFontAndSize(bf, 10);
					over.setTextMatrix(497, 506);
					over.showText("01-03-2017");
					
					String load=Double.toString(securityInfo.getMax_load());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(527, 490);
					over.showText(load);
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(65, 396);
					over.showText(load);
					
					double tarifRate=securityInfo.getRate();
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(135, 396);
					over.showText((taka_format.format(tarifRate)));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(210, 396);
					over.showText(securityInfo.getSd_month());
					
					double sdmonth=Double.parseDouble(securityInfo.getSd_month());
					
					double securityAmount=securityInfo.getMax_load()*securityInfo.getRate()*sdmonth;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(260, 396);
					over.showText(taka_format.format(securityAmount));
					
					/*---------------------------accounting-------------------------------------------------------*/
					
					double presentAmount=securityInfo.getPaid_sequrity_deposit();
							
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(340, 396);
					over.showText(taka_format.format(presentAmount));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(418, 396);
					over.showText("0.00");
						
						
					double exceed=securityAmount-presentAmount;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(500, 396);
					over.showText(taka_format.format(exceed));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(226, 338);
					over.showText(taka_format.format(exceed));
					
	/*--------------------------------------Adding Days to a Particular Date----------------------------------*/				
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(81, 323);
					over.showText(dateInfo.getPayDate());
						
					
				over.endText();	
				
				stamp.close();
				
			}else if(customerCat.equals("03") || customerCat.equals("04")){
				
				readers = new ArrayList<PdfReader>();
				realPath = servlet.getRealPath("/resources/staticPdf/commercial.pdf");		
				document = new Document();
				out = new ByteArrayOutputStream();
				//left,right,top,bottom
				fileName="Security.pdf";			
				
				
				BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
				
				
							
				reader = new PdfReader(new FileInputStream(realPath));
				certificate = new ByteArrayOutputStream();
				PdfStamper stamp = new PdfStamper(reader,certificate);
				PdfContentByte over;
				over = stamp.getOverContent(1);
				
				over.beginText();		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(505, 694);
					over.showText(from_date);
						
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 652);
					over.showText(securityInfo.getCustomerName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 637);
					over.showText(securityInfo.getPropriatorName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(82, 623);
					over.showText(securityInfo.getAddress());		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(122, 609);
					over.showText(securityInfo.getCustomer_id());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(180, 609);
					over.showText("("+securityInfo.getCategory_name()+")");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(322, 522);
					over.showText("2014");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(370, 522);
					over.showText("3.5");
					
					over.setFontAndSize(bf, 10);
					over.setTextMatrix(495, 506);
					over.showText("01-03-2017");
					
					String load=Double.toString(securityInfo.getMax_load());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(525, 491);
					over.showText(load);
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(65, 393);
					over.showText(load);
					
					double tarifRate=securityInfo.getRate();
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(135, 393);
					over.showText((taka_format.format(tarifRate)));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(210, 393);
					over.showText(securityInfo.getSd_month());
					
					double sdmonth=Double.parseDouble(securityInfo.getSd_month());
					
					double securityAmount=securityInfo.getMax_load()*securityInfo.getRate()*sdmonth;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(260, 393);
					over.showText(taka_format.format(securityAmount));
					
					/*---------------------------accounting-------------------------------------------------------*/
					
					double presentAmount=securityInfo.getPaid_sequrity_deposit();
							
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(340, 393);
					over.showText(taka_format.format(presentAmount));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(418, 393);
					over.showText("0.00");
						
						
					double exceed=securityAmount-presentAmount;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(500, 393);
					over.showText(taka_format.format(exceed));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(198, 342);
					over.showText(taka_format.format(exceed));
					
	/*--------------------------------------Adding Days to a Particular Date----------------------------------*/				
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(503, 342);
					over.showText(dateInfo.getPayDate());
						
					
				over.endText();	
				
				stamp.close();
				
			}else{
				
				readers = new ArrayList<PdfReader>();
				realPath = servlet.getRealPath("/resources/staticPdf/ind_Captive.pdf");		
				document = new Document();
				out = new ByteArrayOutputStream();
				//left,right,top,bottom
				fileName="Security.pdf";			
				
				
				BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);
				
				
							
				reader = new PdfReader(new FileInputStream(realPath));
				certificate = new ByteArrayOutputStream();
				PdfStamper stamp = new PdfStamper(reader,certificate);
				PdfContentByte over;
				over = stamp.getOverContent(1);
				
				over.beginText();		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(505, 693);
					over.showText(from_date);
						
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 657);
					over.showText(securityInfo.getCustomerName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(73, 642);
					over.showText(securityInfo.getPropriatorName());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(82, 628);
					over.showText(securityInfo.getAddress());		
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(122, 614);
					over.showText(securityInfo.getCustomer_id());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(180, 614);
					over.showText("("+securityInfo.getCategory_name()+")");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(322, 528);
					over.showText("2014");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(368, 528);
					over.showText("3.5");
					
					over.setFontAndSize(bf, 9);
					over.setTextMatrix(495, 512);
					over.showText("01-03-2017");
					
					String load=Double.toString(securityInfo.getMax_load());
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(525, 496);
					over.showText(load);
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(65, 396);
					over.showText(load);
					
					double tarifRate=securityInfo.getRate();
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(135, 396);
					over.showText((taka_format.format(tarifRate)));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(210, 396);
					over.showText(securityInfo.getSd_month());
					
					double sdmonth=Double.parseDouble(securityInfo.getSd_month());
					
					double securityAmount=securityInfo.getMax_load()*securityInfo.getRate()*sdmonth;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(260, 396);
					over.showText(taka_format.format(securityAmount));
					
					/*---------------------------accounting-------------------------------------------------------*/
					
					double presentAmount=securityInfo.getPaid_sequrity_deposit();
					
					double cash=presentAmount/3;
					
					double check = (presentAmount*2)/3;
							
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(340, 396);
					over.showText(taka_format.format(cash));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(415, 396);
					over.showText(taka_format.format(check));
						
						
					double exceed=securityInfo.getRecv_exceed_deposit();
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(480, 396);
					over.showText(taka_format.format(exceed));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(262, 344);
					over.showText(taka_format.format(exceed));
					
					double dueCash=exceed/3;
					double dueCheck=exceed-dueCash;
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(78, 330);
					over.showText(taka_format.format(dueCash));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(435, 330);
					over.showText(taka_format.format(dueCheck));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(225, 268);
					over.showText(taka_format.format(exceed));
					
	/*--------------------------------------Adding Days to a Particular Date----------------------------------*/				
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(385, 268);
					over.showText(dateInfo.getPayDate());
						
					
				over.endText();	
				
				stamp.close();
				
			}
								
			
			readers.add(new PdfReader(certificate.toByteArray()));
			
				
		}catch(Exception ex)
		{
			ex.printStackTrace();
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

	private SecurityRequireReportDTO getPayDate(){
		
		SecurityRequireReportDTO noticeInfo = new SecurityRequireReportDTO();
		try {
			String transaction_sql="Select to_char(to_date('"+from_date+"','dd-MM-YYYY')+21) PAYDATE from dual";
					
					
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
	
	private SecurityRequireReportDTO getSecurityNoticeInfo(){
		SecurityRequireReportDTO noticeInfo = new SecurityRequireReportDTO();
		
		String area=loggedInUser.getArea_id();
		
		try {
			String transaction_sql="SELECT tbl.CUSTOMER_ID, " +
					"         FULL_NAME, " +
					"         ADDRESS_LINE1 ADDRESS, " +
					"         PROPRIETOR_NAME, " +
					"         mci.CATEGORY_ID, " +
					"         mci.CATEGORY_NAME, " +
					"         mci.AREA_ID, " +
					"         tbl.MAX_LOAD, " +
					"         tbl.SD_MONTH, " +
					"         tbl.RATE, " +
					"         (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) Required_deposit, " +
					"         SECURITY_AMOUNT, " +
					"         (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) - SECURITY_AMOUNT recv_exceed " +
					"    FROM (SELECT cc.CUSTOMER_ID, " +
					"                 MAX_LOAD, " +
					"                 3 SD_MONTH, " +
					"                 (SELECT Price " +
					"                    FROM CUSTOMER, MST_TARIFF " +
					"                   WHERE     Customer.CUSTOMER_CATEGORY = " +
					"                                Mst_Tariff.CUSTOMER_CATEGORY_ID " +
					"                         AND Customer_Id = cc.CUSTOMER_ID " +
					"                         AND Meter_Status = 1 " +
					"                         AND Effective_From <= " +
					"                                TO_DATE ('"+from_date+"', 'dd-MM-YYYY HH24:MI:SS') " +
					"                         AND (   Effective_To IS NULL " +
					"                              OR Effective_To >= " +
					"                                    TO_DATE ('"+from_date+"', " +
					"                                             'dd-MM-YYYY HH24:MI:SS'))) " +
					"                    RATE " +
					"            FROM customer_connection cc " +
					"           WHERE cc.ISMETERED = 01) tbl, " +
					"         (  SELECT CUSTOMER_ID, SUM (SECURITY_AMOUNT) SECURITY_AMOUNT " +
					"              FROM CUSTOMER_SECURITY_LEDGER " +
					"          GROUP BY CUSTOMER_ID) csl, " +
					"         MVIEW_CUSTOMER_INFO mci " +
					"   WHERE     tbl.customer_id = mci.customer_id " +
					"         AND tbl.customer_id = csl.customer_id " +
					"         AND (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) > SECURITY_AMOUNT " +
					"         AND TBL.CUSTOMER_ID ='"+customer_id+"' " +
					"         AND area_id ='"+area+"' " +
					"union all " +
					" SELECT tbl.CUSTOMER_ID, " +
					"         FULL_NAME, " +
					"         ADDRESS_LINE1 ADDRESS, " +
					"         PROPRIETOR_NAME, " +
					"         mci.CATEGORY_ID, " +
					"         mci.CATEGORY_NAME, " +
					"         mci.AREA_ID, " +
					"         mci.DOUBLE_BURNER_QNT_BILLCAL MAX_LOAD, " +
					"         tbl.SD_MONTH, " +
					"         tbl.RATE, " +
					"         (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) Required_deposit, " +
					"         SECURITY_AMOUNT, " +
					"         (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) - SECURITY_AMOUNT recv_exceed " +
					"    FROM (SELECT cc.CUSTOMER_ID, " +
					"                 MAX_LOAD, " +
					"                 3 SD_MONTH, " +
					"                 (SELECT Price " +
					"                    FROM CUSTOMER, MST_TARIFF " +
					"                   WHERE     Customer.CUSTOMER_CATEGORY = " +
					"                                Mst_Tariff.CUSTOMER_CATEGORY_ID " +
					"                         AND Customer_Id = cc.CUSTOMER_ID " +
					"                         AND Meter_Status =0 and BURNER_CATEGORY=2                          " +
					"                        and EFFECTIVE_FROM<=TO_DATE ('"+from_date+"','dd-MM-YYYY')                   " +
					"                         AND nvl(Effective_To,sysdate+365) >= TO_DATE ('"+from_date+"','dd-MM-YYYY')) " +
					"                    RATE " +
					"            FROM customer_connection cc " +
					"           WHERE cc.ISMETERED = 0) tbl, " +
					"         (  SELECT CUSTOMER_ID, SUM (SECURITY_AMOUNT) SECURITY_AMOUNT " +
					"              FROM CUSTOMER_SECURITY_LEDGER " +
					"          GROUP BY CUSTOMER_ID) csl, " +
					"         MVIEW_CUSTOMER_INFO mci " +
					"   WHERE     tbl.customer_id = mci.customer_id " +
					"         AND tbl.customer_id = csl.customer_id " +
					"         AND (mci.DOUBLE_BURNER_QNT_BILLCAL * tbl.SD_MONTH * tbl.RATE) > SECURITY_AMOUNT " +
					"         AND TBL.CUSTOMER_ID ='"+customer_id+"' " +
					"         AND area_id ='"+area+"' " ;

					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
//					
//					"  SELECT tbl.CUSTOMER_ID, " +
//									"         FULL_NAME, ADDRESS_LINE1 ADDRESS, PROPRIETOR_NAME, " +
//									"         mci.CATEGORY_ID, " +
//									"         mci.CATEGORY_NAME, " +
//									"         mci.AREA_ID, " +
//									"         tbl.MAX_LOAD, " +
//									"         tbl.SD_MONTH, " +
//									"         tbl.RATE, " +
//									"         (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) Required_deposit, " +
//									"         SECURITY_AMOUNT, " +
//									"         (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) - SECURITY_AMOUNT recv_exceed " +
//									"    FROM (SELECT cc.CUSTOMER_ID, " +
//									"                 MAX_LOAD, " +
//									"                 3 SD_MONTH, " +
//									"                 (SELECT Price " +
//									"                    FROM CUSTOMER, MST_TARIFF " +
//									"                   WHERE     Customer.CUSTOMER_CATEGORY = " +
//									"                                Mst_Tariff.CUSTOMER_CATEGORY_ID " +
//									"                         AND Customer_Id = cc.CUSTOMER_ID " +
//									"                         AND Meter_Status = 1 " +
//									"                         AND Effective_From <= " +
//									"                                TO_DATE ('"+from_date+"', 'dd-MM-YYYY HH24:MI:SS') " +
//									"                         AND (   Effective_To IS NULL " +
//									"                              OR Effective_To >= " +
//									"                                    TO_DATE ('"+from_date+"', " +
//									"                                             'dd-MM-YYYY HH24:MI:SS'))) " +
//									"                    RATE " +
//									"            FROM customer_connection cc " +
//									"           WHERE cc.ISMETERED = 01) tbl, " +
//									"         (  SELECT CUSTOMER_ID, SUM (SECURITY_AMOUNT) SECURITY_AMOUNT " +
//									"              FROM CUSTOMER_SECURITY_LEDGER " +
//									"          GROUP BY CUSTOMER_ID) csl, " +
//									"         MVIEW_CUSTOMER_INFO mci " +
//									"   WHERE     tbl.customer_id = mci.customer_id " +
//									"         AND tbl.customer_id = csl.customer_id " +
//									"         AND (tbl.MAX_LOAD * tbl.SD_MONTH * tbl.RATE) > SECURITY_AMOUNT " +
//									"         AND TBL.CUSTOMER_ID='"+customer_id+"' " +
//									"         AND area_id = 01 " +
//									"ORDER BY CATEGORY_ID, CUSTOMER_ID " ;
//
//
//					
					
			PreparedStatement ps1=conn.prepareStatement(transaction_sql);
		
        	
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{

        		//disconnDto.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		noticeInfo.setCustomer_id(resultSet.getString("CUSTOMER_ID"));
        		noticeInfo.setCategory_id(resultSet.getString("CATEGORY_ID"));
        		noticeInfo.setCategory_name(resultSet.getString("CATEGORY_NAME"));
        		noticeInfo.setAddress(resultSet.getString("ADDRESS"));
        		noticeInfo.setCustomerName(resultSet.getString("FULL_NAME"));
        		noticeInfo.setSd_month(resultSet.getString("SD_MONTH"));
        		noticeInfo.setRate(resultSet.getFloat("RATE"));
        		noticeInfo.setMax_load(resultSet.getFloat("MAX_LOAD"));
        		noticeInfo.setRequired_sequrity_deposit(resultSet.getFloat("Required_deposit"));
        		noticeInfo.setPaid_sequrity_deposit(resultSet.getFloat("SECURITY_AMOUNT"));
        		noticeInfo.setRecv_exceed_deposit(resultSet.getFloat("recv_exceed"));
        		noticeInfo.setPropriatorName(resultSet.getString("PROPRIETOR_NAME"));
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return noticeInfo;
	}
	
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	

		

}

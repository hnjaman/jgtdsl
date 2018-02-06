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

public class SecurityNotice extends ActionSupport implements ServletContextAware{

	SecurityRequireReportDTO securityInfo=new SecurityRequireReportDTO();
	
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
			
			readers = new ArrayList<PdfReader>();
			realPath = servlet.getRealPath("/resources/staticPdf/SecurityNotice.pdf");		
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
				over.setTextMatrix(496,694);
				over.showText(from_date);
					
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(70, 654);
				over.showText(securityInfo.getCustomerName());
				
				ColumnText ct = new ColumnText(over);						
				ct.setSimpleColumn(new Phrase(new Chunk(securityInfo.getAddress(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
						77, 648, 250, 100, 18, Element.ALIGN_LEFT | Element.ALIGN_TOP);
				
				ct.go();		
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(115, 589);
				over.showText(securityInfo.getCustomer_id());
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(320, 500);
				over.showText("2014");
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(365, 500);
				over.showText("3.5");
				
				over.setFontAndSize(bf, 8);
				over.setTextMatrix(506, 487);
				over.showText("01-09-2015");
				
				String load=Double.toString(securityInfo.getMax_load());
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(65, 454);
				over.showText(load);
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(450, 402);
				over.showText(taka_format.format(securityInfo.getRequired_sequrity_deposit()));
				
				double presentAmount=securityInfo.getPaid_sequrity_deposit();
								
				double cash=presentAmount/3;
				double check = (presentAmount*2)/3;
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(350, 389);
				over.showText(taka_format.format(cash));
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(350, 375);
				over.showText(taka_format.format(check));
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(450, 375);
				over.showText(taka_format.format(presentAmount));
				
				double exceed=securityInfo.getRecv_exceed_deposit();
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(450, 342);
				over.showText(taka_format.format(exceed));
				
				String customerCat=securityInfo.getCategory_id();
				
				if(customerCat.equals("09")){
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(260, 324);
					over.showText(taka_format.format(presentAmount));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(275, 309);
					over.showText("N/A");
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(120, 294);
					over.showText("N/A");
					
				}else{
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(260, 324);
					over.showText(taka_format.format(presentAmount));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(275, 309);
					over.showText(taka_format.format(cash));
					
					over.setFontAndSize(bf, 11);
					over.setTextMatrix(120, 294);
					over.showText(taka_format.format(check));
				}
				
				
				
				double receivetAmount=securityInfo.getRecv_exceed_deposit();				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(230, 261);
				over.showText(taka_format.format(receivetAmount));
				
				over.setFontAndSize(bf, 11);
				over.setTextMatrix(350, 261);
				over.showText(time_limit);
					
				
			over.endText();	
			
			stamp.close();
						
			
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

	
	private SecurityRequireReportDTO getSecurityNoticeInfo(){
		SecurityRequireReportDTO noticeInfo = new SecurityRequireReportDTO();
		
		try {
			String transaction_sql=	"  SELECT tbl.CUSTOMER_ID, " +
									"         FULL_NAME, ADDRESS_LINE1 ADDRESS, " +
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
									"         AND TBL.CUSTOMER_ID='"+customer_id+"' " +
									"         AND area_id = 01 " +
									"ORDER BY CATEGORY_ID, CUSTOMER_ID " ;


					
					
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

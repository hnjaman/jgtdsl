package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.InstallmentBillDTO;
import org.jgtdsl.dto.InstallmentSegmentDTO;
import org.jgtdsl.dto.MBillDTO;
import org.jgtdsl.dto.MeterReadingDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.InstallmentService;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class BillForInstallment extends BaseAction implements ServletContextAware{

	private static final long serialVersionUID = 8854240739341830184L;
	private ServletContext servlet;
	private String installment_id;
	private String customer_category;
	private String area_id;
	private String customer_id;
	private String bill_month;
	private String bill_year;
	private String download_type;
	private String bill_for;
	private String issue_date;
	//private String from_date;
	public static int textDiff=597;
	
	public ServletContext getServlet() {
		return servlet;
	}
	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}
	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}
	
	public String downloadBill() throws Exception
	{	
		int fSize=11;
		String file_customer_id="";
		String file_bill_month="";
		String file_bill_year="";
		int counter=0;
		
		UserDTO loggedInUser=(UserDTO)session.get("user");
		if(area_id==null || area_id.equalsIgnoreCase(""))
			area_id=loggedInUser.getArea_id();
		
		InstallmentService iService=new InstallmentService();
		ArrayList<InstallmentBillDTO> billList=iService.getBill(this.installment_id,this.customer_category,this.area_id,this.customer_id,this.bill_month,this.bill_year,this.download_type);
		HttpServletResponse response = ServletActionContext.getResponse();
		PdfReader reader =null;
		ByteArrayOutputStream certificate = null;
		List<PdfReader> readers = new ArrayList<PdfReader>();
		String printType="Single";
		String realPath=servlet.getRealPath("/resources/staticPdf/SinglePageInstallmentBilling.pdf");
		
		reader = new PdfReader(new FileInputStream(realPath));
		PdfStamper stamp =null;
				
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//Rectangle one = new Rectangle(1202.40f,828.00f); // For double page
		Rectangle one = new Rectangle(601.40f,830.00f); // For Single Page
		document.setPageSize(one);
		//document.setPageSize(PageSize.A4);
		document.setMargins(10, 10, 25, 2);
		DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		try
		{
			
			for(InstallmentBillDTO bill : billList){	
				//System.out.println(bill.getCustomer_name());
				
				if(counter==0){
					file_customer_id=bill.getCustomer_id();
					file_bill_month=bill.getBill_month()+"";
					file_bill_year=bill.getBill_year()+"";
				}
			counter++;
			PdfContentByte over;
			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);		     
			BaseFont bfBold = BaseFont.createFont(BaseFont.TIMES_BOLD,BaseFont.WINANSI,BaseFont.EMBEDDED);
			
			reader = new PdfReader(new FileInputStream(realPath));
			certificate = new ByteArrayOutputStream();
			stamp = new PdfStamper(reader,certificate);
			over = stamp.getOverContent(1);
					
			over.beginText();
			
			over.setFontAndSize(bfBold, 11);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,bill.getCustomer_category_name()+ " Due Gas Bill Installment",300, 680,0);
			int y=771;			
			over.setFontAndSize(bf, fSize);
			over.setTextMatrix(335, 701);
			over.showText(bill.getArea_name());			
			over.setTextMatrix(508, 672);
			over.showText(bill.getInstallmentId());			
			over.setTextMatrix(508, 722-75);
			//over.showText(from_date);
			over.showText(bill.getIssue_date());	
			
			over.setTextMatrix(508, 702-75);
			over.showText(bill.getDueDate());
			over.setTextMatrix(508, 680-75);
			over.showText(bill.getDueDate());
			over.setTextMatrix(508, 662-83);//662
			over.showText(bill.getLast_disconn_reconn_date()==null?"N/A":bill.getLast_disconn_reconn_date());
			over.setTextMatrix(508, 620-89);
			over.showText(bill.getMonthly_contractual_load()+"");
			
			over.setTextMatrix(126, 674);
			over.showText(bill.getBill_month_name()+", "+bill.getBill_year());
			over.setTextMatrix(126, 650);
			over.showText(bill.getCustomer_id());
			over.setTextMatrix(126, 510);//600
			over.showText(bill.getPhone()==null?"":bill.getPhone());	
			
			over.setTextMatrix(250, 457);
			over.showText(bill.getInstallmentId());
			
			over.setTextMatrix(480, 457);
			over.showText(bill.getInstallmentSerial());
			
			ArrayList<InstallmentSegmentDTO> segmentList=bill.getSegmentList();
			for(int i=0;i<segmentList.size();i++)
			{
				
				over.setFontAndSize(bf, 8);
				over.setTextMatrix(50,390-15*i); over.showText(segmentList.get(i).getBillMonthName()+", "+segmentList.get(i).getBillYear());
				
				over.setFontAndSize(bf, fSize);
				over.setTextMatrix(190,390-15*i); over.showText((taka_format.format(segmentList.get(i).getPrincipal())+""));
				over.setTextMatrix(300,390-15*i); over.showText((taka_format.format(segmentList.get(i).getMeterRent())+""));
				over.setTextMatrix(420,390-15*i); over.showText((taka_format.format(segmentList.get(i).getSurcharge())+""));
				over.setTextMatrix(520,390-15*i); over.showText((taka_format.format(segmentList.get(i).getTotal())+""));

			}
				
			
			over.setFontAndSize(bfBold, fSize);
			over.setTextMatrix(520, 292);//392
			over.showText((taka_format.format(bill.getPayable_amount())+""));
			
			over.setFontAndSize(bfBold, fSize);
			over.setTextMatrix(90, 240);//360
			over.showText(bill.getAmount_in_word());
			
			
			ColumnText ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getAddress(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     126, 592, 300, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getCustomer_name(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     126, 639, 400, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			ct = new ColumnText(over);						
			ct.setSimpleColumn(new Phrase(new Chunk(bill.getProprietor_name()==null?"":bill.getProprietor_name(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL))),
			                     126, 616, 350, 36, 12, Element.ALIGN_LEFT | Element.ALIGN_TOP);
								//top-leftX,top-leftY,?,?,lineHeight			
			ct.go(); 
			
			over.setFontAndSize(bf, 15);
			over.setTextMatrix(600, 607);
			over.showText(bill.getInstallmentId());
			over.endText();
			stamp.close();
			readers.add(new PdfReader(certificate.toByteArray()));
			}
						
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
			
			if(billList.size()>1)
				file_customer_id="";
			
			rptUtil.downloadPdf(out, response,"BILL"+((installment_id==null || installment_id.equalsIgnoreCase(""))?"":"-"+installment_id)+"-"+file_bill_month+"-"+file_bill_year+((file_customer_id==null || file_customer_id.equalsIgnoreCase(""))?"":"-"+file_customer_id)+".pdf");
			document=null;	
					
		}
		
		
		return null;	
		
	}
	
	public String getInstallment_id() {
		return installment_id;
	}
	public void setInstallment_id(String installmentId) {
		installment_id = installmentId;
	}
	public String getCustomer_category() {
		return customer_category;
	}
	public void setCustomer_category(String customerCategory) {
		customer_category = customerCategory;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String areaId) {
		area_id = areaId;
	}
	public String getDownload_type() {
		return download_type;
	}
	public void setDownload_type(String downloadType) {
		download_type = downloadType;
	}	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
	public String getBill_month() {
		return bill_month;
	}
	public void setBill_month(String billMonth) {
		bill_month = billMonth;
	}
	public String getBill_year() {
		return bill_year;
	}
	public void setBill_year(String billYear) {
		bill_year = billYear;
	}
	

	
	
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}
	public String getBill_for() {
		return bill_for;
	}
	public void setBill_for(String billFor) {
		bill_for = billFor;
	}
}

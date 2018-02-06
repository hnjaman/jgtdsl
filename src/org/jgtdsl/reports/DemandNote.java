package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.DemandNoteDTO;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DemandNoteService;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;

public class DemandNote extends ActionSupport implements ServletContextAware{

	public String getDemand_note_id() {
		return demand_note_id;
	}
	public void setDemand_note_id(String demand_note_id) {
		this.demand_note_id = demand_note_id;
	}
	private static final long serialVersionUID = 8854240739341830184L;
	private ServletContext servlet;
	private String customer_id;
	private String demand_note_id;
	
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

		CustomerService cs=new CustomerService();
		DemandNoteService ds=new DemandNoteService();
		CustomerDTO customer=cs.getCustomerInfo(this.customer_id);
		DemandNoteDTO dNote=ds.getDemandNote(this.customer_id,demand_note_id);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		PdfReader reader =null;
		ByteArrayOutputStream certificate = null;
		List<PdfReader> readers = new ArrayList<PdfReader>();
		String realPath=servlet.getRealPath("/resources/staticPdf/DemandNote.pdf");
		
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		document.setPageSize(PageSize.A4);
		document.setMargins(10, 10, 10, 10);
		DecimalFormat df = new DecimalFormat("#,##,###.00");
		
		
		try
		{
			reader = new PdfReader(new FileInputStream(realPath));
			certificate = new ByteArrayOutputStream();
			PdfStamper stamp = new PdfStamper(reader,certificate);
			PdfContentByte over;
			BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,BaseFont.EMBEDDED);		     
			over = stamp.getOverContent(1);
	
				
				over.beginText();
				over.setFontAndSize(bf, 10);
				over.setTextMatrix(240, 695);
				over.showText(customer.getPersonalInfo().getFull_name()==null?"":customer.getPersonalInfo().getFull_name());
				
				
				over.setFontAndSize(bf, 10);
				over.setTextMatrix(180, 681);
				over.showText(customer.getPersonalInfo().getProprietor_name()==null?(customer.getPersonalInfo().getFather_name()==null?"":customer.getPersonalInfo().getFather_name()):customer.getPersonalInfo().getProprietor_name());
				
				over.setFontAndSize(bf, 7);
				over.setTextMatrix(180, 655);
				//over.showText(customer.getAddressInfo().getDivision_name()+","+customer.getAddressInfo().getDistrict_name()+","+customer.getAddressInfo().getUpazila_name()+" "+",Post Office :"+customer.getAddressInfo().getPost_office()+",Post Code:"+customer.getAddressInfo().getPost_code()+" "+customer.getAddressInfo().getAddress_line1()+" "+customer.getAddressInfo().getAddress_line2());
				over.showText(customer.getAddress()==null?"":customer.getAddress());
								
				over.setFontAndSize(bf, 10);
				over.setTextMatrix(180, 642);
				over.showText(customer.getPersonalInfo().getMobile()==null?"":customer.getPersonalInfo().getMobile());
				
				over.setFontAndSize(bf, 10);
				over.setTextMatrix(180, 628);
				over.showText(customer.getCustomer_id()==null?"":customer.getCustomer_id());
				
				//Ka
				over.setFontAndSize(bf, 10); over.setTextMatrix(270, 532); over.showText(dNote.getSd_ka_1()==null|| dNote.getSd_ka_1().equalsIgnoreCase("") || Float.valueOf(dNote.getSd_ka_1())==0 ?"":dNote.getSd_ka_1());
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getSd_ka_amount()==null|| dNote.getSd_ka_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getSd_ka_amount())==0 ?"":df.format(new Double(dNote.getSd_ka_amount())), 550, 532, 0);
				
				over.setFontAndSize(bf, 10); over.setTextMatrix(125, 517); over.showText(dNote.getSd_kha_1()==null|| dNote.getSd_kha_1().equalsIgnoreCase("") || Float.valueOf(dNote.getSd_kha_1())==0 ?"":df.format(new Double(dNote.getSd_kha_1())));
				over.setFontAndSize(bf, 10); over.setTextMatrix(270, 517); over.showText(dNote.getSd_kha_2()==null|| dNote.getSd_kha_2().equalsIgnoreCase("") || Float.valueOf(dNote.getSd_kha_2())==0 ?"":dNote.getSd_kha_2());
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getSd_kha_amount()==null|| dNote.getSd_kha_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getSd_kha_amount())==0 ?"":df.format(new Double(dNote.getSd_kha_amount())), 550, 517, 0);
				
				over.setFontAndSize(bf, 10); over.setTextMatrix(155, 502); over.showText(dNote.getSd_ga_1());
				over.setFontAndSize(bf, 10); over.setTextMatrix(266, 502); over.showText(dNote.getSd_ga_2()==null|| dNote.getSd_ga_2().equalsIgnoreCase("") || Float.valueOf(dNote.getSd_ga_2())==0 ?"":df.format(new Double(dNote.getSd_ga_2())));

				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getSd_ga_amount()==null|| dNote.getSd_ga_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getSd_ga_amount())==0 ?"":df.format(new Double(dNote.getSd_ga_amount())), 550, 502, 0);
				
				//Kha
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getBsd_ka_amount()==null|| dNote.getBsd_ka_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getBsd_ka_amount())==0 ?"":df.format(new Double(dNote.getBsd_ka_amount())), 550, 465, 0);
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getBsd_kha_amount()==null|| dNote.getBsd_kha_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getBsd_kha_amount())==0 ?"":df.format(new Double(dNote.getBsd_kha_amount())), 550, 450, 0);
				
				//Ga
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getCl_ka_amount()==null|| dNote.getCl_ka_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_ka_amount())==0?"":df.format(new Double(dNote.getCl_ka_amount())), 550, 414, 0);
				
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getCl_kha_1()==null|| dNote.getCl_kha_1().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_kha_1())==0?"":df.format(new Double(dNote.getCl_kha_1())), 145, 383, 0);
				
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getCl_kha_amount()==null|| dNote.getCl_kha_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_kha_amount())==0?"":df.format(new Double(dNote.getCl_kha_amount())), 550, 383, 0);
			
					
				
				over.setFontAndSize(bf, 10); over.setTextMatrix(250, 368); over.showText((dNote.getCl_ga_1()==null || dNote.getCl_ga_1().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_ga_1())==0?"":dNote.getCl_ga_1()));
				over.setFontAndSize(bf, 10); over.setTextMatrix(305, 368); over.showText((dNote.getCl_ga_2()==null || dNote.getCl_ga_2().equalsIgnoreCase(""))|| Float.valueOf(dNote.getCl_ga_2())==0 ?"":dNote.getCl_ga_2());
				over.setFontAndSize(bf, 10); over.setTextMatrix(380, 368); over.showText((dNote.getCl_ga_3()==null || dNote.getCl_ga_3().equalsIgnoreCase(""))|| Float.valueOf(dNote.getCl_ga_3())==0?"":dNote.getCl_ga_3());
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getCl_ga_amount()==null|| dNote.getCl_ga_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_ga_amount())==0?"":df.format(new Double(dNote.getCl_ga_amount())), 550, 368, 0);
				
				over.setFontAndSize(bf, 10); over.setTextMatrix(255, 353); over.showText((dNote.getCl_gha_1()==null || dNote.getCl_gha_1().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_gha_1())==0)?"":dNote.getCl_gha_1());
				over.setFontAndSize(bf, 10); over.setTextMatrix(335, 353); over.showText((dNote.getCl_gha_2()==null || dNote.getCl_gha_2().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_gha_2())==0)?"":dNote.getCl_gha_2());
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getCl_gha_amount()==null|| dNote.getCl_gha_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_gha_amount())==0 ?"":df.format(new Double(dNote.getCl_gha_amount())), 550, 353, 0);
				
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getCl_uma_l1_amount()==null|| dNote.getCl_uma_l1_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getCl_uma_l1_amount())==0 ?"":df.format(new Double(dNote.getCl_uma_l1_amount())), 550, 338, 0);
					over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getCl_uma_l2_amount()==null|| dNote.getCl_uma_l2_amount().equalsIgnoreCase("")|| Float.valueOf(dNote.getCl_uma_l2_amount())==0 ?"":df.format(new Double(dNote.getCl_uma_l2_amount())), 550, 322, 0);
				
				over.setFontAndSize(bf, 10); over.setTextMatrix(210, 338); over.showText((dNote.getCl_uma_l1()==null ||dNote.getCl_uma_l1().equalsIgnoreCase("")|| Float.valueOf(dNote.getCl_uma_l1())==0)?"":dNote.getCl_uma_l1());
				over.setFontAndSize(bf, 10); over.setTextMatrix(210, 322); over.showText((dNote.getCl_uma_l2()==null ||dNote.getCl_uma_l2().equalsIgnoreCase("")|| Float.valueOf(dNote.getCl_uma_l2())==0)?"":dNote.getCl_uma_l2());
				
				//Gha
				over.setFontAndSize(bf, 10);	
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOf_ka_amount()==null|| dNote.getOf_ka_amount().equalsIgnoreCase("")|| Float.valueOf(dNote.getOf_ka_amount())==0 ?"":df.format(new Double(dNote.getOf_ka_amount())), 550, 285, 0);
				over.setFontAndSize(bf, 10); over.setTextMatrix(190, 270); over.showText((dNote.getOf_kha_amount()==null ||dNote.getOf_kha_amount().equalsIgnoreCase("")|| Float.valueOf(dNote.getOf_kha_amount())==0)?"":dNote.getOf_kha_amount());
				over.setFontAndSize(bf, 10); over.setTextMatrix(330, 270); over.showText((dNote.getOf_ga_amount()==null ||dNote.getOf_ga_amount().equalsIgnoreCase("")|| Float.valueOf(dNote.getOf_ga_amount())==0)?"":dNote.getOf_ga_amount());		
				double s1=Float.parseFloat(dNote.getOf_ga_amount()==null||dNote.getOf_ga_amount().equalsIgnoreCase("")?"0":dNote.getOf_ga_amount())+Float.parseFloat(dNote.getOf_kha_amount()==null||dNote.getOf_kha_amount().equalsIgnoreCase("")?"0":dNote.getOf_kha_amount());
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,  s1==0 ?"":df.format(new Double(s1)), 550, 270, 0);
				
				over.setFontAndSize(bf, 10); over.setTextMatrix(190, 255); over.showText((dNote.getOf_gha_amount()==null ||dNote.getOf_gha_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOf_gha_amount())==0)?"":dNote.getOf_gha_amount());
				over.setFontAndSize(bf, 10); over.setTextMatrix(330, 255); over.showText((dNote.getOf_uma_amount()==null ||dNote.getOf_uma_amount().equalsIgnoreCase("")|| Float.valueOf(dNote.getOf_uma_amount())==0)?"":dNote.getOf_uma_amount());
				double s2=Float.parseFloat(dNote.getOf_gha_amount()==null||dNote.getOf_gha_amount().equalsIgnoreCase("")?"0":dNote.getOf_gha_amount())+Float.parseFloat(dNote.getOf_uma_amount()==null||dNote.getOf_uma_amount().equalsIgnoreCase("")?"0":dNote.getOf_uma_amount());
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,  s1==0 ?"":df.format(new Double(s2)), 550, 255, 0);
				
				
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOf_ch_amount()==null|| dNote.getOf_ch_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOf_ch_amount())==0?"":df.format(new Double(dNote.getOf_ch_amount())), 550, 238, 0);
					over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOf_cho_1()==null|| dNote.getOf_cho_1().equalsIgnoreCase("") || Float.valueOf(dNote.getOf_cho_1())==0?"":df.format(new Double(dNote.getOf_cho_1())), 190, 224, 0);
					over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOf_cho_2()==null|| dNote.getOf_cho_2().equalsIgnoreCase("") || Float.valueOf(dNote.getOf_cho_2())==0?"":df.format(new Double(dNote.getOf_cho_2())), 270, 224, 0);
				
					over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOf_cho_amount()==null|| dNote.getOf_cho_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOf_cho_amount())==0?"":df.format(new Double(dNote.getOf_cho_amount())), 550, 224, 0);

					over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOf_jo_amount()==null|| dNote.getOf_jo_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOf_jo_amount())==0?"":df.format(new Double(dNote.getOf_jo_amount())), 550, 209, 0);
				
				//Uma
				over.setFontAndSize(bf, 10); 
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOthers_ka_amount()==null|| dNote.getOthers_ka_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOthers_ka_amount())==0?"":df.format(new Double(dNote.getOthers_ka_amount())), 550, 163, 0);
				over.setFontAndSize(bf, 10); 
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOthers_kha_amount()==null|| dNote.getOthers_kha_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOthers_kha_amount())==0?"":df.format(new Double(dNote.getOthers_kha_amount())), 550, 148, 0);
				over.setFontAndSize(bf, 10);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOthers_ga_amount()==null|| dNote.getOthers_ga_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOthers_ga_amount())==0?"":df.format(new Double(dNote.getOthers_ga_amount())), 550, 133, 0);
				over.setFontAndSize(bf, 10); 
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT, dNote.getOthers_gha_amount()==null|| dNote.getOthers_gha_amount().equalsIgnoreCase("") || Float.valueOf(dNote.getOthers_gha_amount())==0?"":df.format(new Double(dNote.getOthers_gha_amount())), 550, 118, 0);
				
				//Total
				over.setFontAndSize(bf, 10);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT, df.format(new Double(dNote.getTotal_in_amount())), 550, 77, 0);
				over.setFontAndSize(bf, 8); over.setTextMatrix(110, 77); over.showText(dNote.getTotal_in_word());
				
				
				
				
				
				
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
			rptUtil.downloadPdf(out, response,"DEMAND-NOTE-"+customer_id+".pdf");
			document=null;	
					
		}
		
		
		return null;	
		
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}
}

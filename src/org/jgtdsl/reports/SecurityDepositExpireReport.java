package org.jgtdsl.reports;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerCategoryDTO;
import org.jgtdsl.dto.DepositDTO;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.Utils;

import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class SecurityDepositExpireReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerCategoryDTO> customerCategoryList = new ArrayList<CustomerCategoryDTO>();
	ArrayList<DepositDTO> expireList=new ArrayList<DepositDTO>();
	public  ServletContext servlet;

	public String execute() throws Exception
	{
		
		DepositService depositeService = new  DepositService();
		
		
		
		
		String fileName="SecurityDepositExpire.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(5,5,60,72);
		PdfPTable ptable = null;
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			DepositDTO depositDTO = new DepositDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			headLinetable = new PdfPTable(3);
			headLinetable.setWidthPercentage(100);
			headLinetable.setWidths(new float[]{30,50,30});
			
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setBorderColor(BaseColor.WHITE);	
			headLinetable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("LIST OF SECURITY DEPOSIT EXPIRE INFORMATION",ReportUtil.f11B));
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
			
			expireList =depositeService.getCandidatesForBankGuaranteeExpire(0, 0,"","CUSTOMER_CATEGORY,expire_in","asc",0);
			
			int totalRecordsPerCategory=0;

			int expireListSize=expireList.size();
			String previousCustomerCategoryName=new String("");
			
			for(int i=0;i<expireListSize;i++)
			{
				depositDTO = expireList.get(i);
				String currentCustomerCategoryName=depositDTO.getCustomer_category_name();
				
				if (!currentCustomerCategoryName.equals(previousCustomerCategoryName))
				{	
				
				if(!(previousCustomerCategoryName.equals("")&&currentCustomerCategoryName.equals(previousCustomerCategoryName)))
				{
					
					if(i>0)
					{
						pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(6);
						pcell.setBorder(0);
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph("Total Records",ReportUtil.f11B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(1);
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						
						pcell=new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
						pcell.setMinimumHeight(18f);
						pcell.setColspan(1);
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
						ptable.addCell(pcell);
						document.add(ptable);
						totalRecordsPerCategory=0;
					}
					
				}
				ptable = new PdfPTable(8);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{20,30,30,30,50,20,30,20});
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
				pcell.setColspan(6);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
								
				
				pcell=new PdfPCell(new Paragraph("CusID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Customer Name*",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Bank",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Branch ",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Account Name ",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Valid Till",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Total Deposit(Tk.)",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Expire in(Days)",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				}
				
				//System.out.println(depositDTO.getCustomer_id());
				//System.out.println(depositDTO.getCustomer_category());
				//System.out.println(depositDTO.getCustomer_category_name());
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getCustomer_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getCustomer_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getBank_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getBranch_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getAccount_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getValid_to(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getTotal_deposit(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(depositDTO.getExpire_in(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
			
				previousCustomerCategoryName=depositDTO.getCustomer_category_name();
				totalRecordsPerCategory++;
				
				
			}
			/*[[[[[[[[[Start--->For Last row]]]]]]]]]*/
			pcell=new PdfPCell(new Paragraph("",ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(6);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Total Records",ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(String.valueOf(totalRecordsPerCategory),ReportUtil.f11B));
			pcell.setMinimumHeight(18f);
			pcell.setColspan(1);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
			ptable.addCell(pcell);
			
			document.add(ptable);
			/*[[[[[[[[[End--->For Last row]]]]]]]]]*/
			
			
			
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	
	
	
	public ArrayList<CustomerCategoryDTO> getCustomerCategoryList() {
		return customerCategoryList;
	}



	
	public void setCustomerCategoryList(ArrayList<CustomerCategoryDTO> customerCategoryList) {
		this.customerCategoryList = customerCategoryList;
	}
	
	


	
  }



package org.jgtdsl.reports.masterData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.BankDTO;
import org.jgtdsl.models.BankBranchService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class BankInformation extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<BankDTO> bankInformationList = new ArrayList<BankDTO>();
	public  ServletContext servlet;
	public String execute() throws Exception
	{
		
		BankBranchService bankBranchService = new BankBranchService();
		bankInformationList =bankBranchService.getBankList();
		
		
		String fileName="BankBranchInformation.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(60,40,40,72);
		PdfPTable ptable = null;
		PdfPCell pcell=null;
		
		try{
			ReportFormat eEvent = new ReportFormat(getServletContext());
			BankDTO bankDTO = new BankDTO();
			
			for(int i=0;i<bankInformationList.size();i++)
			{
				bankDTO= bankInformationList.get(i);
				if (i==0)
				{	
				PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
				
				document.open();
				
				ptable = new PdfPTable(3);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,5,50});
				
				pcell=new PdfPCell(new Paragraph("Bank Information",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_LEFT);
				pcell.setBorderWidth(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_LEFT);	
				pcell.setBorderWidth(0);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setVerticalAlignment(Element.ALIGN_LEFT);	
				pcell.setBorderWidth(0);
				ptable.addCell(pcell);
				
				}						
				  
				
				
			pcell=new PdfPCell(new Paragraph("Bank ID",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(":",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
		   
			pcell = new PdfPCell(new Paragraph(bankDTO.getBank_id(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Bank Name",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(":",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(bankDTO.getBank_name(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Bank Address",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(":",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(bankDTO.getAddress(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			
			
			pcell=new PdfPCell(new Paragraph("Contact Person",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(":",ReportUtil.f8));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pcell.setVerticalAlignment(Element.ALIGN_RIGHT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(bankDTO.getPhone(),ReportUtil.f8));
			pcell.setMinimumHeight(16f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			
			

			pcell=new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);	
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);	
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph(" ",ReportUtil.f9B));
			pcell.setMinimumHeight(18f);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_LEFT);	
			pcell.setBorderWidth(0);
			ptable.addCell(pcell);
			
			
			
			
			
			
			}
			
			document.add(ptable);
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	public ArrayList<BankDTO> getBankInformationList() {
		return bankInformationList;
	}

	public void setBankInformationList(ArrayList<BankDTO> bankInformationList) {
		this.bankInformationList = bankInformationList;
	}
	

	
 }



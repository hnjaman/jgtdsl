package org.jgtdsl.reports.masterData;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.DepositTypeDTO;
import org.jgtdsl.models.DepositService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class DepositType extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<DepositTypeDTO> depositTypeList = new ArrayList<DepositTypeDTO>();
	public  ServletContext servlet;
	public String execute() throws Exception
	{
		
	    DepositService depositService = new DepositService();
		
		depositTypeList =depositService.getDepositTypeList();
		
		
		String fileName="DepositType.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(60,60,60,72);
		PdfPTable ptable = null;
		PdfPCell pcell=null;
		
		try{
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			DepositTypeDTO depositTypeDTO = new DepositTypeDTO();
			
			for(int i=0;i<depositTypeList.size();i++)
			{
				depositTypeDTO = depositTypeList.get(i);
				if (i==0)
				{	
				PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
				
				document.open();
				
				ptable = new PdfPTable(3);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,45,40});
				
				pcell=new PdfPCell(new Paragraph("Type ID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Deposit Type Name",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Description ",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				}						
				  
				pcell = new PdfPCell(new Paragraph(depositTypeDTO.getType_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(depositTypeDTO.getType_name_eng(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(depositTypeDTO.getDescription(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
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
	
	
	public ArrayList<DepositTypeDTO> getDepositTypeList() {
		return depositTypeList;
	}

	public void setDpositTypeList(ArrayList<DepositTypeDTO> depositTypeList) {
		this.depositTypeList = depositTypeList;
	}

 }



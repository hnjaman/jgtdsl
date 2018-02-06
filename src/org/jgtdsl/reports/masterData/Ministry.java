package org.jgtdsl.reports.masterData;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.MinistryDTO;
import org.jgtdsl.models.MinistryService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class Ministry extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<MinistryDTO> ministryList = new ArrayList<MinistryDTO>();
	public  ServletContext servlet;
	public String execute() throws Exception
	{
		
	
		ministryList =MinistryService.getAllMinistry();
		
		String fileName="MinistryList.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(60,60,60,72);
		PdfPTable ptable = null;
		PdfPCell pcell=null;
		
		try{
			ReportFormat eEvent = new ReportFormat(getServletContext());
			MinistryDTO ministryDTO = new MinistryDTO();
			
			
			
			for(int i=0;i<ministryList.size();i++)
			{
				ministryDTO =ministryList.get(i);
				if (i==0)
				{	
				PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
				
				document.open();
				
				ptable = new PdfPTable(2);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,45});
				
				pcell=new PdfPCell(new Paragraph("Ministry ID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Name of Ministry",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
			
				}						
				  
				pcell = new PdfPCell(new Paragraph(ministryDTO.getMinistry_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(ministryDTO.getMinistry_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
			
				
				
			}
			
			document.add(ptable);
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName+".pdf");
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}
	
	public ArrayList<MinistryDTO> getMinistryList() {
		return ministryList;
	}



	
	public void setMinistryList(ArrayList<MinistryDTO> ministryList) {
		this.ministryList = ministryList;
	}




	
}



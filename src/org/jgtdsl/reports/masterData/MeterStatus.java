package org.jgtdsl.reports.masterData;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.StatusDTO;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class MeterStatus extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<StatusDTO> statusList = new ArrayList<StatusDTO>();
	public  ServletContext servlet;
	public String execute() throws Exception
	{
		MeterService meterService= new MeterService();
	
		statusList =meterService.getMeterStatus();
		
		String fileName="MeterStatus.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(60,60,60,72);
        PdfPTable ptable = null;
		PdfPCell pcell=null;
		
		try{
			ReportFormat eEvent = new ReportFormat(getServletContext());
			StatusDTO statusDTO = new StatusDTO();
			
			for(int i=0;i<statusList.size();i++)
			{
				statusDTO =statusList.get(i);
				if (i==0)
				{	
				PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
				
				document.open();
				
				ptable = new PdfPTable(2);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,30});
				
				pcell=new PdfPCell(new Paragraph("Status ID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Status Name",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
			
				}						
				  
				pcell = new PdfPCell(new Paragraph(statusDTO.getStatus_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(statusDTO.getStatus_name(),ReportUtil.f8));
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
	
	
	public ArrayList<StatusDTO> getstatusList() {
		return statusList;
	}



	
	public void setStatusList(ArrayList<StatusDTO> statusList) {
		this.statusList =statusList;
	}





	
}



package org.jgtdsl.reports.masterData;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.MeterTypeDTO;
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



public class MeterType extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<MeterTypeDTO> meterTypeList = new ArrayList<MeterTypeDTO>();
	public  ServletContext servlet;
	public String execute() throws Exception
	{
		
	    MeterService meterService = new MeterService();
		
		meterTypeList =meterService.getMeterTypeList();
		
		
		String fileName="MeterTypeList.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(60,60,60,72);
		PdfPTable ptable = null;
		PdfPCell pcell=null;
		
		try{
		
		    
		    ReportFormat eEvent = new ReportFormat(getServletContext());
			MeterTypeDTO meterTypeDTO = new  MeterTypeDTO();
			
			for(int i=0;i<meterTypeList.size();i++)
			{
				meterTypeDTO =meterTypeList.get(i);
				if (i==0)
				{	
				PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
				
				document.open();
				
				ptable = new PdfPTable(3);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,45,40});
				
				pcell=new PdfPCell(new Paragraph("Meter Type ID",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Meter Type Name",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Description",ReportUtil.f9B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				}						
				  
				pcell = new PdfPCell(new Paragraph(meterTypeDTO.getType_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(meterTypeDTO.getType_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(meterTypeDTO.getDescription(),ReportUtil.f8));
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
	
	
	public ArrayList<MeterTypeDTO> getMeterTypeList() {
		return meterTypeList;
	}


	public void setMeterTypeList(ArrayList<MeterTypeDTO> meterTypeList) {
		this.meterTypeList = meterTypeList;
	}




	
}

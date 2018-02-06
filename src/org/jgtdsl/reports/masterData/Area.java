
package org.jgtdsl.reports.masterData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.AreaDTO;
import org.jgtdsl.models.AreaService;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class Area extends BaseAction {
	private static final long serialVersionUID = 1L;
	private ArrayList<AreaDTO>  areaList = new ArrayList<AreaDTO>();
	public String execute() throws Exception
	{
		
        AreaService areaService = new  AreaService();		
		areaList =areaService.getAreaList();
		
		String fileName="Area.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		document.setMargins(60,60,60,72);
		PdfPTable ptable = null;
		PdfPCell pcell=null;
		
		try{
			ReportFormat eEvent = new ReportFormat(getServletContext());
		    AreaDTO areaDTO = new  AreaDTO();
			
			for(int i=0;i<areaList.size();i++)
			{
				areaDTO =areaList.get(i);
				if (i==0)
				{	
				PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
				
				document.open();
				
				ptable = new PdfPTable(3);
				ptable.setWidthPercentage(100);
				ptable.setWidths(new float[]{15,45,40});
				
				pcell=new PdfPCell(new Paragraph("Area ID",ReportUtil.f8B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);			
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Area Name",ReportUtil.f8B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph("Description",ReportUtil.f8B));
				pcell.setMinimumHeight(18f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				}						
				  
				pcell = new PdfPCell(new Paragraph(areaDTO.getArea_id(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(areaDTO.getArea_name(),ReportUtil.f8));
				pcell.setMinimumHeight(16f);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				ptable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(areaDTO.getDescription(),ReportUtil.f8));
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
	
	public ArrayList<AreaDTO> getAreaList() {
		return areaList;
	}
	public void setAreaList(ArrayList<AreaDTO> areaList) {
		this.areaList = areaList;
	}


	
}

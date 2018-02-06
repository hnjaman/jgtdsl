package org.jgtdsl.reports;
import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Black;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.*;
import java.text.*;



public class ReportFormat extends PdfPageEventHelper
{  private static final long serialVersionUID = 1L;

   String header=new Date().toString();
   int count=0;
   
   



	public ServletContext servletContext =null;
	public PdfTemplate total;
	public BaseFont helv;
	public PdfPTable footer;
	public PdfPCell pcell;
	
	public int header1;
	  static Font font1 = new Font(Font.FontFamily.HELVETICA  , 8, Font.BOLD);
      static Font font2 = new Font(Font.FontFamily.HELVETICA  , 8, Font.NORMAL);



	
	

   
	

	public ReportFormat(ServletContext sContext) {
		
		this.servletContext = sContext;
	}






public void onOpenDocument(PdfWriter writer, Document document) {
    total = writer.getDirectContent().createTemplate(30, 16);
    
    
    

    
}

public void onCloseDocument(PdfWriter writer, Document document) {
    ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
            new Phrase(String.valueOf(writer.getPageNumber() - 1)),
            2, 2, 0);
}

public void onStartPage(PdfWriter writer, Document document) {
	
	if(writer.getPageNumber()==1)
	   {
		   document.setMargins(2,2,2,72);
	   }
	
	PdfContentByte canvas = writer.getDirectContentUnder();
    ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(a1), 559, 806, 0);
    
	
	try {
		
		
		if(writer.getPageNumber()==1)
		{
			
			
		PdfPTable headerTable = new PdfPTable(3);
	    Rectangle page = document.getPageSize();
	    headerTable.setTotalWidth(page.getWidth());
		float a=((page.getWidth()*15)/100)/2;
		float b=((page.getWidth()*30)/100)/2;
			
		headerTable.setWidths(new float[] {
			a,b,a
		});
		
		
		pcell= new PdfPCell(new Paragraph(""));
		pcell.setBorder(Rectangle.BOTTOM);
		headerTable.addCell(pcell);
		
		
		
		PdfPTable mTable=new PdfPTable(1);
		mTable.setWidths(new float[]{b});
		pcell=new PdfPCell(new Paragraph("JALALABAD GAS TRANSMISSION AND DISTRIBUTION SYSTEM LIMITED"));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);	
		mTable.addCell(pcell);
		
		pcell=new PdfPCell(new Paragraph("(A company of PetroBangla)", ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		mTable.addCell(pcell);
				
		pcell=new PdfPCell(mTable);
		pcell.setBorder(Rectangle.BOTTOM);
		headerTable.addCell(pcell);
		
		
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(Rectangle.BOTTOM);
		headerTable.addCell(pcell);
		
			
		//headerTable.writeSelectedRows(0, -1, 0, page.getHeight(), writer.getDirectContent());
		}
		
	} catch (DocumentException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	
	
	
	
	


	


}




@Override
public void onEndPage(PdfWriter writer, Document document) {
	
	 int pagenumber = writer.getPageNumber();
	 int s=writer.getCurrentPageNumber();
	 //System.out.println(s);
	 
	 
	
	Rectangle page = document.getPageSize();
    PdfPTable table = new PdfPTable(4);
    PdfPCell cell = null;
    
	
	
	try
	{
		
		
		
			
		
		
		table.setWidths(new int[]{40,50,30,6});
	  
         table.setLockedWidth(true);
         table.getDefaultCell().setFixedHeight(20);
         table.getDefaultCell().setBorder(Rectangle.TOP);
       
         Paragraph printDatePg=new  Paragraph(header,font2);
         
         
         table.addCell(printDatePg);
        // table.addCell("Printed time:"+new Date().toString());
         table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
         
         Paragraph footerMSG=new Paragraph("",font2);
         cell = new PdfPCell(footerMSG);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         cell.setBorder(Rectangle.TOP);
         table.addCell(cell);
         
         Paragraph pageNo=new Paragraph(String.format("Page %d of", writer.getPageNumber()),ReportUtil.f8);
         cell = new PdfPCell(pageNo);
         cell.setBorder(Rectangle.TOP);
         cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         table.addCell(cell);
         Image img=Image.getInstance(total);
         img.scaleAbsolute(12,10);
         cell = new PdfPCell(img);
         cell.setPaddingLeft(2);
         cell.setPaddingTop(2);
         cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        
         cell.setBorder(Rectangle.TOP);
         table.addCell(cell);
         
         
         table.setTotalWidth(page.getWidth());
        
         
         table.writeSelectedRows(0, -1, 0, 12, writer.getDirectContent());
         ////////////signature table////////
         
      
 
		PdfPTable footerTable= new PdfPTable(7);
		PdfPCell fcell=null;
		
	    footerTable.setWidths(new int[]{50,50,50,50,50,50,50});
	    footerTable.setTotalWidth(page.getWidth());
	    
	   
	 
	    fcell=new PdfPCell(new Paragraph("Prepared by*",font1));
	    fcell.setBorder(Rectangle.TOP);
	    footerTable.addCell(fcell);
	    
	    fcell=new PdfPCell(new Paragraph("",font1));
	    fcell.setBorder(Rectangle.NO_BORDER);
	    footerTable.addCell(fcell);
	    
	    fcell=new PdfPCell(new Paragraph("",font1));
	    fcell.setBorder(Rectangle.NO_BORDER);
	    footerTable.addCell(fcell);
	    
	    
	    fcell=new PdfPCell(new Paragraph("Examined by",font1));
	
	    fcell.setBorder(Rectangle.TOP);
	    fcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    footerTable.addCell(fcell);
	    
	    fcell=new PdfPCell(new Paragraph("",font1));
	    fcell.setBorder(Rectangle.NO_BORDER);
	    footerTable.addCell(fcell);
	    
	    fcell=new PdfPCell(new Paragraph("",font1));
	    fcell.setBorder(Rectangle.NO_BORDER);
	    footerTable.addCell(fcell);
	    
	    
	    
	    fcell=new PdfPCell(new Paragraph("Approved by",font1));
	   /* PdfContentByte cb3 = writer.getDirectContent();
	    cb3.setLineWidth(.5f);
	    cb3.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), 15);
	    cb3.moveTo(470, 33);
	    cb3.lineTo(550,33);
	    cb3.stroke();*/
	    fcell.setBorder(Rectangle.TOP);
	    fcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    footerTable.addCell(fcell);
	    
	    fcell=new PdfPCell(new Paragraph("",font1));
	    fcell.setBorder(Rectangle.NO_BORDER);
	    footerTable.addCell(fcell);
	    
	    fcell=new PdfPCell(new Paragraph("                     .",font1));
	    fcell.setBorder(Rectangle.NO_BORDER);
	    footerTable.addCell(fcell);
	    
	    
	    
	    footerTable.writeSelectedRows(0, -1, 0, 33, writer.getDirectContent());
	    

		
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	

	
	
	
      }




private String a1;


public void setHeader1(String a1) {
    this.a1 = a1;
}


private static PdfPCell createTableNotHeaderCell(final String text){
	  final PdfPCell cell= new PdfPCell(new Paragraph(text,font2));
	
	  	cell.setMinimumHeight(16f);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		//cell.setBackgroundColor(new BaseColor(242,242,242));
		cell.setBorderColor(BaseColor.BLACK);
		
	  
	  return cell;
	}














     }



    
   


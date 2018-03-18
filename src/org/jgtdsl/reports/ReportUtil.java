package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

public class ReportUtil {
	
	public static Font f12B = FontFactory.getFont("Times-Roman", 12, Font.BOLD,BaseColor.BLACK);
	public static Font f11B = FontFactory.getFont("Times-Roman", 11, Font.BOLD,BaseColor.BLACK);
	public static Font f11 = FontFactory.getFont("Times-Roman", 11, Font.NORMAL,BaseColor.BLACK);
	public static Font f10B = FontFactory.getFont("Times-Roman", 10, Font.BOLD,BaseColor.BLACK);
	public static Font f9 = FontFactory.getFont("Times-Roman", 9, Font.NORMAL,BaseColor.BLACK);
	public static Font f9B = FontFactory.getFont("Times-Roman", 9, Font.BOLD,BaseColor.BLACK);
	public static Font f8 = FontFactory.getFont("Times-Roman", 8, Font.NORMAL,BaseColor.BLACK);
	public static Font f8B = FontFactory.getFont("Times-Roman", 8, Font.BOLD,BaseColor.BLACK);
	public static Font f8BU = FontFactory.getFont("Times-Roman", 8, Font.UNDERLINE| Font.BOLD, BaseColor.BLACK);
	
	public void downloadPdf(ByteArrayOutputStream out, HttpServletResponse response,String fileName)
	{
		try{
			byte[] b =out.toByteArray();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			response.setContentLength(b.length);
			response.getOutputStream().write(b);
			response.getOutputStream().flush();
		}catch(IOException ioEx){}
	}
	
}

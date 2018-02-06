package org.jgtdsl.reports.template;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class SinglePageBillingFinal extends PdfPageEventHelper {

	public static final String MBILL_FORMAT = "D:\\SinglePageBilling.pdf";


	public static void main(String[] args) throws IOException,
			DocumentException, SQLException {
		createStationary(MBILL_FORMAT);
		//System.out.println("Done");
	}

	public void createPdf(String filename) throws SQLException, IOException,
			DocumentException {

		Document document = new Document(PageSize.A4, 36, 36, 72, 36);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(filename));
		useStationary(writer);
		document.open();
		document.add(new Paragraph("Test Data 1"));
		document.newPage();
		document.add(new Paragraph("Test Data 2"));
		document.newPage();
		document.close();
	}

	protected PdfImportedPage page;

	public void useStationary(PdfWriter writer) throws IOException {
		writer.setPageEvent(this);
		PdfReader reader = new PdfReader(MBILL_FORMAT);
		page = writer.getImportedPage(reader, 1);
	}

	public void onEndPage(PdfWriter writer, Document document) {
		writer.getDirectContentUnder().addTemplate(page, 0, 0);
	}

	public static void createStationary(String filename) throws IOException,
			DocumentException {
		Document document = new Document(PageSize.A4, 20, 20, 30, 10);
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();

		Font f6 = FontFactory.getFont("Times-Roman", 6, Font.NORMAL,BaseColor.BLACK);
		Font f8 = FontFactory.getFont("Times-Roman", 8, Font.NORMAL,BaseColor.BLACK);
		Font f9 = FontFactory.getFont("Times-Roman", 9, Font.NORMAL,BaseColor.BLACK);
		Font f10 = FontFactory.getFont("Times-Roman", 10, Font.NORMAL,BaseColor.BLACK);
		Font f11 = FontFactory.getFont("Times-Roman", 11, Font.NORMAL,BaseColor.BLACK);
		
		Font f8B = FontFactory.getFont("Times-Roman", 8, Font.BOLD,BaseColor.BLACK);
		Font f10B = FontFactory.getFont("Times-Roman", 10, Font.BOLD,BaseColor.BLACK);
		Font f8BU = FontFactory.getFont("Times-Roman", 8, Font.UNDERLINE| Font.BOLD, BaseColor.BLACK);
		Font f11B = FontFactory.getFont("Times-Roman", 11, Font.BOLD,BaseColor.BLACK);
		Font f13B = FontFactory.getFont("Times-Roman", 11, Font.BOLD,BaseColor.BLACK);
		

		String logo = "D:\\logo.png";
		URL urlLogo = new URL("file", "localhost", logo);
		Image JGTDSLLogo = Image.getInstance(urlLogo);
		JGTDSLLogo.scalePercent(30.00f);

		PdfPTable HeaderMainTable = null;
		PdfPTable CompanyInfoTable = null;
		PdfPTable CopyTable = null;

		PdfPCell pcell = null;

		float[] HeaderMainTable_Width = { 0.20f, 0.60f, 0.20f };
		HeaderMainTable = new PdfPTable(HeaderMainTable_Width);
		HeaderMainTable.setWidthPercentage(100);
		HeaderMainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		HeaderMainTable.getDefaultCell().setPadding(0f);

		pcell = new PdfPCell();
		pcell.setFixedHeight(50f);
		pcell.setImage(JGTDSLLogo);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		HeaderMainTable.addCell(pcell);

		float[] CompanyInfo_Width = { 1.0f };
		CompanyInfoTable = new PdfPTable(CompanyInfo_Width);
		CompanyInfoTable.setWidthPercentage(100);

		pcell = new PdfPCell(new Paragraph(
				"PASHCHIMANCHAL GAS COMPANY LIMITED", f13B));
		pcell.setVerticalAlignment(Element.ALIGN_TOP);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(0);
		pcell.setBorderColor(BaseColor.WHITE);
		CompanyInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("(A Company of Petrobangla)", f10));
		pcell.setVerticalAlignment(Element.ALIGN_TOP);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(0);
		pcell.setBorderColor(BaseColor.WHITE);
		CompanyInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph(
				"Revenue section, Regional Office : __________________", f10));
		pcell.setVerticalAlignment(Element.ALIGN_TOP);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(0);
		pcell.setBorderColor(BaseColor.WHITE);
		CompanyInfoTable.addCell(pcell);

		HeaderMainTable.addCell(CompanyInfoTable);

		float[] CopyTable_Width = { 0.4f, 0.6f };
		CopyTable = new PdfPTable(CopyTable_Width);
		CopyTable.setWidthPercentage(100);

		pcell = new PdfPCell(new Paragraph("Copy to", f10B));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setFixedHeight(20);
		CopyTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Bank", f10B)); 
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setFixedHeight(20);
		CopyTable.addCell(pcell);

		pcell = new PdfPCell();
		pcell.setFixedHeight(20);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.addElement(CopyTable);
		HeaderMainTable.addCell(pcell);

		document.add(HeaderMainTable);

		LineSeparator ls = new LineSeparator();
		document.add(new Chunk(ls));

		float[] GeneralInfoTable_Width = { 0.6f, 0.4f };
		PdfPTable GeneralInfoTable = new PdfPTable(GeneralInfoTable_Width);
		GeneralInfoTable.setWidthPercentage(100);

		float[] LeftInfo_Width = { 0.3f, 0.2f, 0.5f };
		PdfPTable LeftInfoTable = new PdfPTable(LeftInfo_Width);
		LeftInfoTable.setWidthPercentage(100);
		LeftInfoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		pcell = new PdfPCell(new Paragraph("Bill Month   :", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(20);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(20);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("",f11B));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(20);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Customer Code   :", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(18);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(18);
		pcell.setColspan(2);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Customer Name   :", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(23);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(23);
		pcell.setColspan(2);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Proprietor Name   :", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(23);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(23);
		pcell.setColspan(2);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Address   :", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(25);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(18);
		pcell.setColspan(2);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(18);
		pcell.setColspan(3);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(18);
		pcell.setColspan(3);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Phone   :", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(18);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(18);
		pcell.setColspan(2);
		LeftInfoTable.addCell(pcell);

		pcell = new PdfPCell();
		pcell.addElement(LeftInfoTable);
		pcell.setBorder(Rectangle.NO_BORDER);
		GeneralInfoTable.addCell(pcell);

		float[] RightInfo_Width = { 0.65f, 0.35f };
		PdfPTable RightInfoTable = new PdfPTable(RightInfo_Width);
		RightInfoTable.setWidthPercentage(100);

		pcell = new PdfPCell(new Paragraph("Invoice No:", f11));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(20);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f8));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setFixedHeight(18);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Bill Issue Date", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f8));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Last Pay Date(without S/C)", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f8));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(18);
		RightInfoTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Last Pay Date(with S/C)", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f8));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(18);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Latest Discon/Recon Date", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setFixedHeight(18);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f8));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(18);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("VAT Registration No", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("12181021565", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("Monthly Contractual Load", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("", f8));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("1 SCM", f11));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		pcell = new PdfPCell(new Paragraph("35.3147 SCFT", f8));
		pcell.setBorderColor(BaseColor.BLACK);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(22);
		RightInfoTable.addCell(pcell);

		/*
		 * pcell = new PdfPCell(new Paragraph("1 SCM",f11));
		 * pcell.setBorderColor(BaseColor.BLACK);
		 * pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		 * pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		 * pcell.setFixedHeight(22); RightInfoTable.addCell(pcell);
		 * 
		 * pcell = new PdfPCell(new Paragraph("35.3147 SCFT",f8));
		 * pcell.setBorderColor(BaseColor.BLACK);
		 * pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		 * pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		 * pcell.setFixedHeight(22); RightInfoTable.addCell(pcell);
		 */

		pcell = new PdfPCell();
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.addElement(RightInfoTable);
		GeneralInfoTable.addCell(pcell);
		document.add(GeneralInfoTable);

		/* Meter Reading */
		float[] MeterReading_Width = {  0.1f, 0.1f,0.14f, 0.1f, 0.1f, 0.1f,
				0.1f, 0.1f, 0.08f, 0.08f };
		PdfPTable MeterReadingTable = new PdfPTable(MeterReading_Width);
		MeterReadingTable.setWidthPercentage(100);
		MeterReadingTable.setSpacingBefore(4f);

		pcell = new PdfPCell((new Paragraph("Billing Period", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(2);
		MeterReadingTable.addCell(pcell);
		
		pcell = new PdfPCell((new Paragraph("Meter No.", f11)));
		pcell.setRowspan(2);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Meter Reading", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		MeterReadingTable.addCell(pcell);

//		pcell = new PdfPCell((new Paragraph("Pressure", f11)));
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setRowspan(2);
//		MeterReadingTable.addCell(pcell);
//
//		pcell = new PdfPCell((new Paragraph("Temp.", f11)));
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setRowspan(2);
//		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Correction Factor", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setColspan(3);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Rate per SCM (TK)", f8)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setRowspan(2);
		MeterReadingTable.addCell(pcell);
		
		pcell = new PdfPCell((new Paragraph("From", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("To", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Present", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Previous", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Difference", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Pressure", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Temp.", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("HHV/NHV", f8)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		MeterReadingTable.addCell(pcell);

		
		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setFixedHeight(50);
		pcell.setColspan(10);
		MeterReadingTable.addCell(pcell);
		document.add(MeterReadingTable);
		

		/* Billing Main Table */
		float[] Billing_Width = { .426f, .02f, .426f };
		PdfPTable Billing_Table = new PdfPTable(Billing_Width);
		Billing_Table.setWidthPercentage(100);
		Billing_Table.setSpacingBefore(4f);
		Billing_Table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		Billing_Table.getDefaultCell().setPadding(0f);

		float[] Particulars_Width = { .65f, .35f };
		PdfPTable ParticularsTable = new PdfPTable(Particulars_Width);
		ParticularsTable.setWidthPercentage(100);

//		pcell = new PdfPCell((new Paragraph("Consumption", f11B)));
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setFixedHeight(22);
//		ParticularsTable.addCell(pcell);
//
//		pcell = new PdfPCell((new Paragraph("Quantity(SCM)", f11B)));
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setFixedHeight(22);
//		ParticularsTable.addCell(pcell);
//		
//		pcell = new PdfPCell((new Paragraph("Minimum Load     :",f11)));
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		pcell.setPaddingRight(10); pcell.setFixedHeight(22);
//		ParticularsTable.addCell(pcell);
//		
//		pcell = new PdfPCell((new Paragraph("",f11)));
//		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		pcell.setFixedHeight(22); ParticularsTable.addCell(pcell);
		
		pcell = new PdfPCell((new Paragraph(" Maximum Load(SCM)                   :",
				f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(22);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT | Rectangle.TOP);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setFixedHeight(22);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.TOP);
		ParticularsTable.addCell(pcell);
		
		pcell = new PdfPCell((new Paragraph(" Minimum Load(SCM)                    :",
				f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(22);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);		
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);
		
		
		pcell = new PdfPCell((new Paragraph("Particulars", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
		pcell.setPaddingLeft(10);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Quantity (SCM)", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);
		
		
		
		
		pcell = new PdfPCell((new Paragraph(" 1. Actual Gas Consumption            :",
				f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setFixedHeight(22);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 2. Other Consumption                     :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setPaddingRight(10);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 3. Mixed Consumption                    :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setPaddingRight(10);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(
				" 4. Total Billed Quantity (1+2-3)     : ", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setPaddingRight(10);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(
				" 5. HHV/NHV Adjustment Quantity:", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setPaddingRight(10);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Total Billed Qty. (4+5) =", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
		pcell.setPaddingRight(2);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
		pcell.setFixedHeight(22);
		ParticularsTable.addCell(pcell);
		
		pcell = new PdfPCell((new Paragraph("", f11B)));
		pcell.setColspan(2);
		pcell.setRowspan(3);
		ParticularsTable.addCell(pcell);

		Billing_Table.addCell(ParticularsTable);
		Billing_Table.addCell("");

		float[] PBmargin_Width = { .65f, .35f };
		PdfPTable PBmarginTable = new PdfPTable(PBmargin_Width);
		PBmarginTable.setWidthPercentage(100);

		pcell = new PdfPCell((new Paragraph("Bill Details     ", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingLeft(10);
		pcell.setFixedHeight(22);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT | Rectangle.TOP |  Rectangle.BOTTOM);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Amount (BDT)", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.TOP |  Rectangle.BOTTOM);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 1. Gas Bill                                        :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER | Rectangle.TOP);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 2. Bill for Minimum Load               :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER | Rectangle.TOP);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 3. Meter/CMS Rent                         :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER | Rectangle.TOP);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 4. Bill Due to HHV/NHV               :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER | Rectangle.TOP);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 5. Adjustment                                  :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER | Rectangle.TOP);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph(" 6. Others                                          :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);
		

		pcell = new PdfPCell((new Paragraph(" 7. Total Bill (1+2+....+6) =", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setPaddingRight(2);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);
		
		pcell = new PdfPCell((new Paragraph(" 8. VAT Rebate @          %              :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER | Rectangle.TOP);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT );
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);
		
		
		pcell = new PdfPCell((new Paragraph(" 9. Surcharge @     12     %              :", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingRight(10);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER| Rectangle.BOTTOM | Rectangle.RIGHT);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

	
		pcell = new PdfPCell((new Paragraph(" Total Payable(7-8+9) =", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
		pcell.setPaddingRight(2);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
		pcell.setFixedHeight(22);
		PBmarginTable.addCell(pcell);



		Billing_Table.addCell(PBmarginTable);
		document.add(Billing_Table);

		float[] InWord_Width = { 1f };
		PdfPTable InWordTable = new PdfPTable(InWord_Width);
		InWordTable.setWidthPercentage(100);
		InWordTable.setSpacingBefore(4);

		pcell = new PdfPCell((new Paragraph(
				"In Words : ", f11B)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingLeft(10);
		pcell.setFixedHeight(20);
		InWordTable.addCell(pcell);
		document.add(InWordTable);

		
		
		float[] Note_Width = { 1f };
		PdfPTable NoteTable = new PdfPTable(Note_Width);
		NoteTable.setWidthPercentage(100);
		NoteTable.setSpacingBefore(4);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingLeft(10);
		pcell.setFixedHeight(32);
		// pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP
				| Rectangle.BOTTOM);
		NoteTable.addCell(pcell);

		// pcell = new PdfPCell((new
		// Paragraph("Please turnover for instructions.",f11)));
		// pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		// pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		// pcell.setBorder(Rectangle.LEFT|Rectangle.RIGHT|Rectangle.BOTTOM);
		// pcell.setPaddingLeft(10);
		// pcell.setPaddingBottom(5);
		// NoteTable.addCell(pcell);
		document.add(NoteTable);
		
		float[] Due_Width = { 1f };
		PdfPTable DueTable = new PdfPTable(Due_Width);
		DueTable.setWidthPercentage(100);
		DueTable.setSpacingBefore(4);

		pcell = new PdfPCell((new Paragraph(
				"Due Bill Information (Excluding Surcharge)", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_TOP);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingLeft(10);
		pcell.setFixedHeight(15);
		pcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP
				| Rectangle.BOTTOM);
		DueTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("", f11)));
		pcell.setPaddingLeft(10);
		pcell.setFixedHeight(30);
		pcell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP
				| Rectangle.BOTTOM);
		DueTable.addCell(pcell);

		document.add(DueTable);
		

		// Paragraph para1 = new Paragraph("");
		// para1.setSpacingBefore(10);
		// document.add(para1);
		//		
		// document.add(new
		// Paragraph("Last Payment Date : _______________ , Last Payment Amount : _______________ , Dues Bill Amount : _______________",f11));
		//		

		

		float[] Signature_Width = { .33f, .33f, .33f };
		PdfPTable SignatureTable = new PdfPTable(Signature_Width);
		SignatureTable.setWidthPercentage(100);
		SignatureTable.setSpacingBefore(30);

		pcell = new PdfPCell((new Paragraph("_______________________", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(30);
		SignatureTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("_______________________", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(30);
		SignatureTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("_______________________", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setFixedHeight(30);
		SignatureTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Prepared By", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pcell.setPaddingLeft(30f);
		pcell.setBorder(Rectangle.NO_BORDER);
		SignatureTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Examined By", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setBorder(Rectangle.NO_BORDER);
		SignatureTable.addCell(pcell);

		pcell = new PdfPCell((new Paragraph("Approved By", f11)));
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pcell.setBorder(Rectangle.NO_BORDER);
		pcell.setPaddingRight(30f);
		SignatureTable.addCell(pcell);

		document.add(SignatureTable);

		/* Only for double page
		Paragraph para1 = new Paragraph("");
		para1.setSpacingBefore(5);
		document.add(para1);
		para1 = new Paragraph("Please Turnover for Instructions", f8);
		para1.setAlignment(Element.ALIGN_RIGHT);
		document.add(para1);
		*/

		   String  HEADER_BANGLA  = "জ�?বালানী নিরাপত�?তা সর�?বোচ�?চ অগ�?রাধিকার";
		   String  FOOTER_BANGLA  = "সময়মত গ�?যাস বিল পরিশোধ করে দেশের অর�?থনীতির চাকাকে সচল রাখ�?ন";
		   String  NOTE_LINE1 ="গ�?রাহকের বিশেষ জ�?ঞাতার�?থে : বিল পরিশোধের সময়সীমার মধ�?যে সম�?পূর�?ণ গ�?যাস বিল পরিশোধ না করলে, পৃথক কোন নোটিশ ছাড়াই গ�?যাস সংযোগ বিচ�?ছিন�?ন করা যাবে।   ";
		   String  NOTE_LINE2= "�?ই বিলই সংযোগ বিচ�?ছিন�?নকরণের চূড়ান�?ত নোটিশ বলে গণ�?য হবে।   ";
		   com.itextpdf.text.Image jpeg_header_bangla = null;
		com.itextpdf.text.Image jpeg_footer_bangla = null;
		com.itextpdf.text.Image jpeg_note_line1 = null;
		com.itextpdf.text.Image jpeg_note_line2 = null;

		try {
			jpeg_header_bangla = com.itextpdf.text.Image
					.getInstance(textToImage(HEADER_BANGLA, 25));
			jpeg_footer_bangla = com.itextpdf.text.Image
					.getInstance(textToImage(FOOTER_BANGLA, 25));
			jpeg_note_line1 = com.itextpdf.text.Image.getInstance(textToImage(
					NOTE_LINE1, 18));
			jpeg_note_line2 = com.itextpdf.text.Image.getInstance(textToImage(
					NOTE_LINE2, 18));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jpeg_header_bangla.scalePercent(50f);
		jpeg_header_bangla.setAbsolutePosition(218, 810);
		document.add(jpeg_header_bangla);

		jpeg_note_line1.scalePercent(50f);
		jpeg_note_line1.setAbsolutePosition(30, 212);
		document.add(jpeg_note_line1);

		jpeg_note_line2.scalePercent(50f);
		jpeg_note_line2.setAbsolutePosition(30, 200);
		document.add(jpeg_note_line2);

		jpeg_footer_bangla.scalePercent(50f);
		jpeg_footer_bangla.setAbsolutePosition(140, 2);
		document.add(jpeg_footer_bangla);

		document.close();
	}

	public static byte[] textToImage(String text, float size)
			throws IOException, FontFormatException {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		try {
			Graphics2D g2d = img.createGraphics();
			// Font font = new Font("Arial", Font.PLAIN, 48);
			java.awt.Font fnt = java.awt.Font.createFont(
					java.awt.Font.TRUETYPE_FONT, new File(
							"c:/windows/fonts/SolaimanLipi_20-04-07.ttf"));
			fnt = fnt.deriveFont(java.awt.Font.PLAIN, size);
			g2d.setFont(fnt);
			FontMetrics fm = g2d.getFontMetrics();
			int width = fm.stringWidth(text);
			int height = fm.getHeight();
			g2d.dispose();

			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			g2d = img.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
					RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
					RenderingHints.VALUE_STROKE_PURE);

			g2d.setFont(fnt);
			fm = g2d.getFontMetrics();
			g2d.setBackground(Color.WHITE);
			g2d.setColor(Color.BLACK);
			g2d.drawString(text, 0, fm.getAscent());
			g2d.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();

		return imageInByte;
	}
}
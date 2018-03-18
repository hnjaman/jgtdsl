package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class LedgerPrinting extends BaseAction implements ServletContextAware {
	private static final long serialVersionUID = 8854240739341830184L;
	private ServletContext servlet;
	private String bill_id;
	private String customer_category;
	private String area_id;
	private String customer_id;
	private String category_name;
	private String bill_month;
	private String bill_year;
	private String download_type;
	private String bill_for;
	private int serial = 0;
	public static int textDiff = 597;
	private boolean water_mark = false;

	public String downloadLedger() throws Exception {
		Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		Font font3 = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);

		UserDTO loggedInUser = (UserDTO) session.get("user");
		if ((area_id == null || area_id.equalsIgnoreCase(""))
				&& loggedInUser != null)
			area_id = loggedInUser.getArea_id();

		String fileName = "BILL-" + bill_id + ".pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		try {
			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			// writer.setPageEvent(Event);
			PdfPCell pcell = null;

			document.open();
			String realPath = servlet
					.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(30f, 34f);
			// img.setAbsolutePosition(145f, 780f);
			img.setAbsolutePosition(150f, 767f); // rotate

			document.add(img);
			
			PdfPTable ApplianceTable = new PdfPTable(4);
			ApplianceTable.setWidthPercentage(90);

			PdfPTable mTable = new PdfPTable(1);
			mTable.setWidthPercentage(100);
			pcell = new PdfPCell(new Paragraph(
					"JALALABAD GAS T & D SYSTEM LIMITED", ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",
					ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);			

			Chunk chunk1 = new Chunk("Regional Distribution: ", font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer
					.valueOf(getArea_id()) - 1]), ReportUtil.f8B);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(
					"ledger for: ", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			document.add(mTable);
			
			
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

	public String getBill_id() {
		return bill_id;
	}

	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getBill_month() {
		return bill_month;
	}

	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}

	public String getBill_year() {
		return bill_year;
	}

	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}

	public String getDownload_type() {
		return download_type;
	}

	public void setDownload_type(String download_type) {
		this.download_type = download_type;
	}

	public String getBill_for() {
		return bill_for;
	}

	public void setBill_for(String bill_for) {
		this.bill_for = bill_for;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public static int getTextDiff() {
		return textDiff;
	}

	public static void setTextDiff(int textDiff) {
		LedgerPrinting.textDiff = textDiff;
	}

	public boolean isWater_mark() {
		return water_mark;
	}

	public void setWater_mark(boolean water_mark) {
		this.water_mark = water_mark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}

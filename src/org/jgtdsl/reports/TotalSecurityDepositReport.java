package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletContext;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.BankBookDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;

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

public class TotalSecurityDepositReport extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String area;
	private String customer_category;
	private String bill_month;
	private String bill_year;
	private String from_date;
	private String to_date;
	private String report_for;
	private String category_name;
	ArrayList<BankBookDTO> securityList = new ArrayList<BankBookDTO>();
	ArrayList<BankBookDTO> totalSecurityList = new ArrayList<BankBookDTO>();

	Connection conn = ConnectionManager.getConnection();
	String sql = "";
	ArrayList<String> customerType = new ArrayList<String>();

	PreparedStatement ps = null;
	ResultSet rs = null;
	// String[] areaName=new String[10];
	int a = 0;

	public ServletContext servlet;
	ServletContext servletContext = null;

	static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
	static DecimalFormat consumption_format = new DecimalFormat(
			"##########0.000");

	public String execute() throws Exception {

		String fileName = "Total_Security_Deposit.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LEGAL.rotate());
		document.setMargins(5, 5, 60, 72);

		try {

			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(Event);
			PdfPCell pcell = null;

			document.open();
			PdfPTable headerTable = new PdfPTable(3);
			Rectangle page = document.getPageSize();
			headerTable.setTotalWidth(page.getWidth());
			float a = ((page.getWidth() * 15) / 100) / 2;
			float b = ((page.getWidth() * 30) / 100) / 2;

			headerTable.setWidths(new float[] { a, b, a });

			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);

			String realPath = servlet
					.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(28f, 31f);
			// img.setAbsolutePosition(145f, 780f);
			img.setAbsolutePosition(290f, 540f); // rotate

			document.add(img);

			PdfPTable mTable = new PdfPTable(1);
			mTable.setWidths(new float[] { b });
			pcell = new PdfPCell(new Paragraph(
					"JALALABAD GAS T & D SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",
					ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("HEAD OFFICE :", font2);
			Chunk chunk2 = new Chunk("SYLHET_NORTH", font3);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);

			pcell = new PdfPCell(mTable);
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(""));
			pcell.setBorder(Rectangle.NO_BORDER);
			headerTable.addCell(pcell);
			document.add(headerTable);

			// String[]
			// areaList={"01","02","03","04","05","06","07","08","09","10"};
			String[] areaList = { "10", "11", "12", "13", "14", "15", "16","17", "18", "19", "20", "21", "22", "23", "24" };
			ArrayList<String> areaListAll = new ArrayList<String>(Arrays.asList(areaList));
			for (int j = 0; j < areaListAll.size(); j++) {
				area = areaListAll.get(j);

				if (j != 0) {
					document.newPage();
				}

				PdfPTable headlineTable = new PdfPTable(3);
				headlineTable.setSpacingBefore(5);
				headlineTable.setSpacingAfter(10);
				headlineTable.setWidths(new float[] { 40, 70, 40 });
				pcell = null;
				pcell = new PdfPCell(new Paragraph("", ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setBorder(Rectangle.NO_BORDER);
				headlineTable.addCell(pcell);

				if (report_for.equals("month_wise")) {
					pcell = new PdfPCell(
							new Paragraph(
									"Statement of Security Deposit For the month Of "
											+ Month.values()[Integer
													.valueOf(bill_month) - 1]
											+ "'" + bill_year, ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setBorder(Rectangle.NO_BORDER);
					headlineTable.addCell(pcell);
				} else if (report_for.equals("date_wise")) {
					pcell = new PdfPCell(new Paragraph(
							"Statement of Security Deposit For the period Of "
									+ from_date + " to " + to_date,
							ReportUtil.f11B));
					pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pcell.setBorder(Rectangle.NO_BORDER);
					headlineTable.addCell(pcell);
				}

				pcell = new PdfPCell(new Paragraph("", ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setBorder(Rectangle.NO_BORDER);
				headlineTable.addCell(pcell);
				document.add(headlineTable);

				PdfPTable datatable1 = new PdfPTable(9);

				datatable1.setWidthPercentage(100);
				datatable1.setWidths(new float[] { 5, 20, 15, 10, 10, 10, 10,
						10, 10 });

				pcell = new PdfPCell(new Paragraph("Area Name : "
						+ Area.values()[Integer.valueOf(area) - 1], font3));
				pcell.setColspan(9);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("Sl.No", font3));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("Customer/Category Name",
						font3));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("Customer Code", font3));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph(
						"Amount of Security Deposit(Tk.)", font3));
				pcell.setColspan(5);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("Customer Status", font3));
				pcell.setRowspan(2);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("Cash", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("Bank Guaranty", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("FDR", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("PSP & Others", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("Total", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("A", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("B", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("C", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("D", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("E", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("F", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("G", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("H=(D+E+F+G)", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				pcell = new PdfPCell(new Paragraph("I", font3));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable1.addCell(pcell);

				/*--------------------------------------End of Headline-------------------------------------------------------------*/
				securityList = getSecurityDeposit();
				int listSize = securityList.size();

				double cashAmt = 0.0;
				double bankGauranty = 0.0;
				double fdrAmt = 0.0;
				double psfOthers = 0.0;
				double totalSecurityAmt = 0.0;
				String category = "";

				double totalCashAmt = 0.0;
				double totalBankGauranty = 0.0;
				double totalFdrAmt = 0.0;
				double totalPsfOthers = 0.0;
				double securityAmt = 0.0;

				try {
					for (int i = 0; i < listSize; i++) {

						category = securityList.get(i).getCustomerCategoryId();

						if (!category.equals("10") && !category.equals("08")) {

							totalCashAmt = totalCashAmt
									+ securityList.get(i).getCashBank();
							totalBankGauranty = totalBankGauranty
									+ securityList.get(i).getBankGuaranty();
							totalFdrAmt = totalFdrAmt
									+ securityList.get(i).getFdr();
							totalPsfOthers = totalPsfOthers
									+ securityList.get(i).getPsp()
									+ securityList.get(i).getOthers();
							securityAmt = securityAmt + totalCashAmt
									+ totalBankGauranty + totalFdrAmt
									+ totalPsfOthers;

							cashAmt = cashAmt
									+ securityList.get(i).getCashBank();
							bankGauranty = bankGauranty
									+ securityList.get(i).getBankGuaranty();
							fdrAmt = fdrAmt + securityList.get(i).getFdr();
							psfOthers = psfOthers
									+ securityList.get(i).getPsp()
									+ securityList.get(i).getOthers();
							totalSecurityAmt = totalSecurityAmt + cashAmt
									+ bankGauranty + fdrAmt + psfOthers;

							if (category.equals("12")) {

								pcell = new PdfPCell(new Paragraph("1", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("11")) {
								pcell = new PdfPCell(new Paragraph("2", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("09")) {
								pcell = new PdfPCell(new Paragraph("4",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("07")) {
								pcell = new PdfPCell(new Paragraph("5",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("06")) {
								pcell = new PdfPCell(new Paragraph("6", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("05")) {
								pcell = new PdfPCell(new Paragraph("7", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("04")) {
								pcell = new PdfPCell(new Paragraph("9", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("03")) {
								pcell = new PdfPCell(new Paragraph("10", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("02")) {
								pcell = new PdfPCell(new Paragraph("12", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else if (category.equals("01")) {
								pcell = new PdfPCell(new Paragraph("13", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							}

							if (category.equals("09") || category.equals("07")) {
								pcell = new PdfPCell(new Paragraph(securityList
										.get(i).getCustomerCategoryName(),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getCashBank()),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getBankGuaranty()),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getFdr()), ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getPsp()
												+ securityList.get(i)
														.getOthers()),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								double totalSec = securityList.get(i)
										.getCashBank()
										+ securityList.get(i).getBankGuaranty()
										+ securityList.get(i).getFdr()
										+ securityList.get(i).getPsp()
										+ securityList.get(i).getOthers();

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalSec),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							} else {
								pcell = new PdfPCell(new Paragraph(securityList
										.get(i).getCustomerCategoryName(),
										font3));
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getCashBank()), font3));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getBankGuaranty()), font3));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getFdr()), font3));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityList.get(i)
												.getPsp()
												+ securityList.get(i)
														.getOthers()), font3));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								double totalSec = securityList.get(i)
										.getCashBank()
										+ securityList.get(i).getBankGuaranty()
										+ securityList.get(i).getFdr()
										+ securityList.get(i).getPsp()
										+ securityList.get(i).getOthers();

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalSec), font3));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);
							}

							if (category.equals("11")) {

								pcell = new PdfPCell(new Paragraph("3",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										"Total Power (1+2)", ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(cashAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(bankGauranty),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(fdrAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(psfOthers),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalSecurityAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								cashAmt = 0.0;
								bankGauranty = 0.0;
								fdrAmt = 0.0;
								psfOthers = 0.0;
								totalSecurityAmt = 0.0;

							} else if (category.equals("09")
									|| category.equals("07")) {
								cashAmt = 0.0;
								bankGauranty = 0.0;
								fdrAmt = 0.0;
								psfOthers = 0.0;
								totalSecurityAmt = 0.0;
							} else if (category.equals("05")) {

								pcell = new PdfPCell(new Paragraph("8",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										"Total Industrial (6+7)",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(cashAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(bankGauranty),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(fdrAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(psfOthers),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalSecurityAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								cashAmt = 0.0;
								bankGauranty = 0.0;
								fdrAmt = 0.0;
								psfOthers = 0.0;
								totalSecurityAmt = 0.0;
							} else if (category.equals("03")) {
								pcell = new PdfPCell(new Paragraph("11",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										"Total Comercial (9+10)",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(cashAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(bankGauranty),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(fdrAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(psfOthers),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalSecurityAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								cashAmt = 0.0;
								bankGauranty = 0.0;
								fdrAmt = 0.0;
								psfOthers = 0.0;
								totalSecurityAmt = 0.0;
							} else if (category.equals("01")) {
								pcell = new PdfPCell(new Paragraph("14",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										"Total Domestic (12+13)",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(cashAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(bankGauranty),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(fdrAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(psfOthers),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalSecurityAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("Total=",
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								pcell.setColspan(2);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalCashAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalBankGauranty),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalFdrAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(totalPsfOthers),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph(
										taka_format.format(securityAmt),
										ReportUtil.f11B));
								pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								datatable1.addCell(pcell);

								pcell = new PdfPCell(new Paragraph("", font3));
								pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
								datatable1.addCell(pcell);

								totalCashAmt = 0.0;
								totalBankGauranty = 0.0;
								totalFdrAmt = 0.0;
								totalPsfOthers = 0.0;
								securityAmt = 0.0;

							}

						}

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				document.add(datatable1);

				/*----------------------------------------------Total Security Deposit-------------------------------------------------*/

			}
			document.newPage();

			PdfPTable datatable1 = new PdfPTable(9);

			datatable1.setWidthPercentage(100);
			datatable1.setWidths(new float[] { 5, 20, 15, 10, 10, 10, 10, 10,
					10 });

			if (report_for.equals("month_wise")) {
				pcell = new PdfPCell(
						new Paragraph(
								"Statement of Security Deposit For the month Of "
										+ Month.values()[Integer
												.valueOf(bill_month) - 1] + "'"
										+ bill_year, ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setBorder(Rectangle.NO_BORDER);
				pcell.setColspan(9);
				datatable1.addCell(pcell);
			} else if (report_for.equals("date_wise")) {
				pcell = new PdfPCell(
						new Paragraph(
								"Statement of Security Deposit For the period Of "
										+ from_date + " to " + to_date,
								ReportUtil.f11B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setBorder(Rectangle.NO_BORDER);
				pcell.setColspan(9);
				datatable1.addCell(pcell);
			}

			pcell = new PdfPCell(new Paragraph("Total", ReportUtil.f11B));
			pcell.setColspan(9);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Sl.No", font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Customer/Category Name", font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Customer Code", font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph(
					"Amount of Security Deposit(Tk.)", font3));
			pcell.setColspan(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Customer Status", font3));
			pcell.setRowspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Cash", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Bank Guaranty", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("FDR", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("PSP & Others", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("Total", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("A", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("B", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("C", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("D", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("E", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("F", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("G", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("H=(D+E+F+G)", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			pcell = new PdfPCell(new Paragraph("I", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable1.addCell(pcell);

			totalSecurityList = getTotalSecurityDeposit();
			int listSize1 = totalSecurityList.size();

			double cashAmt = 0.0;
			double bankGauranty = 0.0;
			double fdrAmt = 0.0;
			double psfOthers = 0.0;
			double totalSecurityAmt = 0.0;
			String category = "";

			double totalCashAmt = 0.0;
			double totalBankGauranty = 0.0;
			double totalFdrAmt = 0.0;
			double totalPsfOthers = 0.0;
			double securityAmt = 0.0;

			for (int i = 0; i < listSize1; i++) {

				category = totalSecurityList.get(i).getCustomerCategoryId();

				if (!category.equals("10") && !category.equals("08")) {

					totalCashAmt = totalCashAmt
							+ totalSecurityList.get(i).getCashBank();
					totalBankGauranty = totalBankGauranty
							+ totalSecurityList.get(i).getBankGuaranty();
					totalFdrAmt = totalFdrAmt
							+ totalSecurityList.get(i).getFdr();
					totalPsfOthers = totalPsfOthers
							+ totalSecurityList.get(i).getPsp()
							+ totalSecurityList.get(i).getOthers();
					securityAmt = securityAmt + totalCashAmt
							+ totalBankGauranty + totalFdrAmt + totalPsfOthers;

					cashAmt = cashAmt + totalSecurityList.get(i).getCashBank();
					bankGauranty = bankGauranty
							+ totalSecurityList.get(i).getBankGuaranty();
					fdrAmt = fdrAmt + totalSecurityList.get(i).getFdr();
					psfOthers = psfOthers + totalSecurityList.get(i).getPsp()
							+ totalSecurityList.get(i).getOthers();
					totalSecurityAmt = totalSecurityAmt + cashAmt
							+ bankGauranty + fdrAmt + psfOthers;

					if (category.equals("12")) {

						pcell = new PdfPCell(new Paragraph("1", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("11")) {
						pcell = new PdfPCell(new Paragraph("2", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("09")) {
						pcell = new PdfPCell(
								new Paragraph("4", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("07")) {
						pcell = new PdfPCell(
								new Paragraph("5", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("06")) {
						pcell = new PdfPCell(new Paragraph("6", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("05")) {
						pcell = new PdfPCell(new Paragraph("7", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("04")) {
						pcell = new PdfPCell(new Paragraph("9", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("03")) {
						pcell = new PdfPCell(new Paragraph("10", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("02")) {
						pcell = new PdfPCell(new Paragraph("12", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else if (category.equals("01")) {
						pcell = new PdfPCell(new Paragraph("13", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					}

					if (category.equals("09") || category.equals("07")) {
						pcell = new PdfPCell(new Paragraph(totalSecurityList
								.get(i).getCustomerCategoryName(),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityList.get(i)
										.getCashBank()), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityList.get(i)
										.getBankGuaranty()), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityList.get(i)
										.getFdr()), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(
								new Paragraph(
										taka_format.format(totalSecurityList
												.get(i).getPsp()
												+ totalSecurityList.get(i)
														.getOthers()),
										ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						double totalSec = totalSecurityList.get(i)
								.getCashBank()
								+ totalSecurityList.get(i).getBankGuaranty()
								+ totalSecurityList.get(i).getFdr()
								+ totalSecurityList.get(i).getPsp()
								+ totalSecurityList.get(i).getOthers();

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSec), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					} else {
						pcell = new PdfPCell(new Paragraph(totalSecurityList
								.get(i).getCustomerCategoryName(), font3));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityList.get(i)
										.getCashBank()), font3));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityList.get(i)
										.getBankGuaranty()), font3));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityList.get(i)
										.getFdr()), font3));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(
								new Paragraph(
										taka_format.format(totalSecurityList
												.get(i).getPsp()
												+ totalSecurityList.get(i)
														.getOthers()), font3));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						double totalSec = totalSecurityList.get(i)
								.getCashBank()
								+ totalSecurityList.get(i).getBankGuaranty()
								+ totalSecurityList.get(i).getFdr()
								+ totalSecurityList.get(i).getPsp()
								+ totalSecurityList.get(i).getOthers();

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSec), font3));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);
					}

					if (category.equals("11")) {

						pcell = new PdfPCell(
								new Paragraph("3", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Total Power (1+2)",
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(cashAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(bankGauranty),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(fdrAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(psfOthers), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityAmt),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						cashAmt = 0.0;
						bankGauranty = 0.0;
						fdrAmt = 0.0;
						psfOthers = 0.0;
						totalSecurityAmt = 0.0;

					} else if (category.equals("09") || category.equals("07")) {
						cashAmt = 0.0;
						bankGauranty = 0.0;
						fdrAmt = 0.0;
						psfOthers = 0.0;
						totalSecurityAmt = 0.0;
					} else if (category.equals("05")) {

						pcell = new PdfPCell(
								new Paragraph("8", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								"Total Industrial (6+7)", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(cashAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(bankGauranty),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(fdrAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(psfOthers), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityAmt),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						cashAmt = 0.0;
						bankGauranty = 0.0;
						fdrAmt = 0.0;
						psfOthers = 0.0;
						totalSecurityAmt = 0.0;
					} else if (category.equals("03")) {
						pcell = new PdfPCell(new Paragraph("11",
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								"Total Comercial (9+10)", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(cashAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(bankGauranty),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(fdrAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(psfOthers), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityAmt),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						cashAmt = 0.0;
						bankGauranty = 0.0;
						fdrAmt = 0.0;
						psfOthers = 0.0;
						totalSecurityAmt = 0.0;
					} else if (category.equals("01")) {
						pcell = new PdfPCell(new Paragraph("14",
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								"Total Domestic (12+13)", ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(cashAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(bankGauranty),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(fdrAmt), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(psfOthers), ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalSecurityAmt),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("Total=",
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pcell.setColspan(2);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalCashAmt),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalBankGauranty),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalFdrAmt),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(totalPsfOthers),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph(
								taka_format.format(securityAmt),
								ReportUtil.f11B));
						pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						datatable1.addCell(pcell);

						pcell = new PdfPCell(new Paragraph("", font3));
						pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
						datatable1.addCell(pcell);

						totalCashAmt = 0.0;
						totalBankGauranty = 0.0;
						totalFdrAmt = 0.0;
						totalPsfOthers = 0.0;
						securityAmt = 0.0;

					}

				}

			}

			document.add(datatable1);

			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private ArrayList<BankBookDTO> getSecurityDeposit() {
		Connection conn = ConnectionManager.getConnection();
		ArrayList<BankBookDTO> journalVoucherList = new ArrayList<BankBookDTO>();
		try {

			String wClause = "";
			if (report_for.equals("date_wise")) {
				wClause = " AND TRANS_DATE between to_date('" + from_date
						+ "','dd-mm-yyyy') AND to_date('" + to_date
						+ "','dd-mm-yyyy') ";
			} else if (report_for.equals("month_wise")) {
				wClause = " AND to_char(TRANS_DATE,'yyyy') =" + bill_year + " "
						+ "AND to_char(TRANS_DATE,'mm') =" + bill_month + " ";
			}

			String defaulterSql = "select cat, sum(CASH_BANK) CASH_BANK, sum(BANK_GURANTEE) BANK_GURANTEE,sum(FDR) FDR,sum(psp) psp,sum(OTHER_S) OTHER_S,CATEGORY_NAME,CATEGORY_TYPE,CATEGORY_ID from( "
					+ "select substr(CUSTOMER_ID,3,2) cat, decode(DESCRIPTION,'CASH_BANK' ,DEBIT) CASH_BANK, "
					+ "decode(DESCRIPTION,'BANK_GURANTEE' ,DEBIT) BANK_GURANTEE, "
					+ "decode(DESCRIPTION,'FDR' ,DEBIT) FDR, "
					+ "decode(DESCRIPTION,'PSP' ,DEBIT) psp, "
					+ "decode(DESCRIPTION,'OTHERS',DEBIT) OTHER_S "
					+ "from CUSTOMER_SECURITY_LEDGER CSL,DTL_DEPOSIT DD "
					+ "WHERE CSL.DEPOSIT_ID=DD.DEPOSIT_ID "
					+ "AND DD.TYPE_ID=16 "
					+ "AND substr(CUSTOMER_ID,1,2)='"
					+ area
					+ "' "
					+ wClause
					+ ") tab1,MST_CUSTOMER_CATEGORY MCC "
					+ "where TAB1.CAT(+)=MCC.CATEGORY_ID "
					+ "AND CATEGORY_ID<>'13' "
					+ "AND CATEGORY_ID<>'14' "
					+ "group by cat,CATEGORY_NAME,CATEGORY_TYPE,CATEGORY_ID "
					+ "order by CATEGORY_ID desc ";

			PreparedStatement ps1 = conn.prepareStatement(defaulterSql);

			ResultSet resultSet = ps1.executeQuery();

			while (resultSet.next()) {
				BankBookDTO jDto = new BankBookDTO();
				jDto.setCustomerCategoryId(resultSet.getString("CATEGORY_ID"));
				jDto.setCustomerCategoryName(resultSet
						.getString("CATEGORY_NAME"));
				jDto.setCustomerCategoryType(resultSet
						.getString("CATEGORY_TYPE"));
				jDto.setCashBank(resultSet.getDouble("CASH_BANK"));
				jDto.setBankGuaranty(resultSet.getDouble("BANK_GURANTEE"));
				jDto.setFdr(resultSet.getDouble("FDR"));
				jDto.setPsp(resultSet.getDouble("psp"));
				jDto.setOthers(resultSet.getDouble("OTHER_S"));

				journalVoucherList.add(jDto);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return journalVoucherList;
	}

	private ArrayList<BankBookDTO> getTotalSecurityDeposit() {
		Connection conn = ConnectionManager.getConnection();
		ArrayList<BankBookDTO> journalVoucherList = new ArrayList<BankBookDTO>();
		try {

			String wClause = "";
			if (report_for.equals("date_wise")) {
				wClause = " AND TRANS_DATE between to_date('" + from_date
						+ "','dd-mm-yyyy') AND to_date('" + to_date
						+ "','dd-mm-yyyy') ";
			} else if (report_for.equals("month_wise")) {
				wClause = " AND to_char(TRANS_DATE,'yyyy') =" + bill_year + " "
						+ "AND to_char(TRANS_DATE,'mm') =" + bill_month + " ";
			}

			String defaulterSql = "select cat, sum(CASH_BANK) CASH_BANK, sum(BANK_GURANTEE) BANK_GURANTEE,sum(FDR) FDR,sum(psp) psp,sum(OTHER_S) OTHER_S,CATEGORY_NAME,CATEGORY_TYPE,CATEGORY_ID from( "
					+ "select substr(CUSTOMER_ID,3,2) cat, decode(DESCRIPTION,'CASH_BANK' ,DEBIT) CASH_BANK, "
					+ "decode(DESCRIPTION,'BANK_GURANTEE' ,DEBIT) BANK_GURANTEE, "
					+ "decode(DESCRIPTION,'FDR' ,DEBIT) FDR, "
					+ "decode(DESCRIPTION,'PSP' ,DEBIT) psp, "
					+ "decode(DESCRIPTION,'OTHERS',DEBIT) OTHER_S "
					+ "from CUSTOMER_SECURITY_LEDGER CSL,DTL_DEPOSIT DD "
					+ "WHERE CSL.DEPOSIT_ID=DD.DEPOSIT_ID "
					+ "AND DD.TYPE_ID=01 "
					+ wClause
					+ ") tab1,MST_CUSTOMER_CATEGORY MCC "
					+ "where TAB1.CAT(+)=MCC.CATEGORY_ID "
					+ "AND CATEGORY_ID<>'13' "
					+ "AND CATEGORY_ID<>'14' "
					+ "group by cat,CATEGORY_NAME,CATEGORY_TYPE,CATEGORY_ID "
					+ "order by CATEGORY_ID desc ";

			PreparedStatement ps1 = conn.prepareStatement(defaulterSql);

			ResultSet resultSet = ps1.executeQuery();

			while (resultSet.next()) {
				BankBookDTO jDto = new BankBookDTO();
				jDto.setCustomerCategoryId(resultSet.getString("CATEGORY_ID"));
				jDto.setCustomerCategoryName(resultSet
						.getString("CATEGORY_NAME"));
				jDto.setCustomerCategoryType(resultSet
						.getString("CATEGORY_TYPE"));
				jDto.setCashBank(resultSet.getDouble("CASH_BANK"));
				jDto.setBankGuaranty(resultSet.getDouble("BANK_GURANTEE"));
				jDto.setFdr(resultSet.getDouble("FDR"));
				jDto.setPsp(resultSet.getDouble("psp"));
				jDto.setOthers(resultSet.getDouble("OTHER_S"));

				journalVoucherList.add(jDto);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return journalVoucherList;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public String getReport_for() {
		return report_for;
	}

	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getFrom_date() {
		return from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

}
package org.jgtdsl.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.jgtdsl.dto.IBankingDTO;


public class iBanking extends BaseAction{
	private static final long serialVersionUID = 5045062988808175387L;
	private File upload;
	private InputStream inputStream;
	private HttpServletResponse response;
	private ServletContext servlet;
	private String uploadContentType; //The content type of the file
	private String uploadFileName;
	private String fileCaption;
	private String type;
	
	@SuppressWarnings("deprecation")
	public String execute() throws Exception
	{  
		String type = uploadContentType.split("/")[1];
		if(type.endsWith("excel"))type = "xls";
		else{
			inputStream = new StringBufferInputStream("Not a Valid type"); 
			return "error";
		}
		//FileInputStream file = new FileInputStream(upload);
		
		InputStream inputStream = new FileInputStream(upload);
		//InputStream input = POIExample.class.getResourceAsStream( "qa.xls" );
        POIFSFileSystem fs = new POIFSFileSystem( inputStream );
        HSSFWorkbook wb = new HSSFWorkbook(fs);

        DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
        ArrayList<IBankingDTO> iBankingList=new ArrayList<IBankingDTO>();
        IBankingDTO iBank;
        
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
            java.util.Iterator<Row> rowIterator = sheet.iterator();
            while(rowIterator.hasNext()) {
            	HSSFRow  row = (HSSFRow) rowIterator.next();
				if(formatter.formatCellValue(row.getCell(0)).equalsIgnoreCase(""))
					continue;
            	
				
				try{
					iBank=new IBankingDTO();
	            	iBank.setTransactionId(formatter.formatCellValue(row.getCell(0)));
	            	iBank.setTransactionDate(formatter.formatCellValue(row.getCell(1)));
	            	iBank.setFromAccount(formatter.formatCellValue(row.getCell(2)));
	            	iBank.setToAccount(formatter.formatCellValue(row.getCell(3)));
	            	iBank.setReferenceId(formatter.formatCellValue(row.getCell(4)));
	            	iBank.setBillMonthYear(formatter.formatCellValue(row.getCell(5)));
	            	iBank.setCollectedAmount(formatter.formatCellValue(row.getCell(6)));
	            	iBank.setCounter(formatter.formatCellValue(row.getCell(7)));
	            	iBankingList.add(iBank);
				}
				catch(Exception ex){
					ex.printStackTrace();
				}       
        }
        }
		return SUCCESS;

	}

	
	public InputStream getInputStream()
	{
		return inputStream;
	}

	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}

	public void setServletResponse(HttpServletResponse response){
	    this.response = response;
	  }

	  public HttpServletResponse getServletResponse(){
	    return response;
	  }

	public ServletContext getServletContext()
	{
		return servlet;
	}

	public void setServletContext(ServletContext servlet)
	{
		this.servlet = servlet;
	}

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public String getUploadContentType()
	{
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType)
	{
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName()
	{
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}

	public String getFileCaption()
	{
		return fileCaption;
	}

	public void setFileCaption(String fileCaption)
	{
		this.fileCaption = fileCaption;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

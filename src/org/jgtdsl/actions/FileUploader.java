package org.jgtdsl.actions;

import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Random;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.RenderedOp;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
// this class is responsible for picture operations

public class FileUploader extends ActionSupport implements ServletResponseAware,ServletContextAware
{

	private static final long serialVersionUID = 2211973526619680479L;
	private File upload;
	private InputStream inputStream;
	private HttpServletResponse response;
	private ServletContext servlet;
	private String uploadContentType; //The content type of the file
	private String uploadFileName;
	private String fileCaption;
	private String userId;
	private String customerId;
	private String type;
	private String userType;

	


	public String execute() throws Exception
	{  
		String type = uploadContentType.split("/")[1];
		if(type.endsWith("png"))type = "png";
		else if(type.endsWith("jpeg"))type = "jpeg";
		else if(type.endsWith("jpg"))type = "jpg";
		else if(type.endsWith("gif"))type = "png";
		else if(type.endsWith("bmp"))type = "bmp";
		else
			{
				inputStream = new StringBufferInputStream("Not a Valid type"); 
				return "error";
			}
		
		FileInputStream in = new FileInputStream(upload);
		BufferedInputStream bf = new BufferedInputStream(in);
		byte[] b = new byte[(int) upload.length()];
		bf.read(b);
			
		String t = "";
		if (type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("jpg"))
			t = "JPEG";
		else if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("gif"))
			t = "PNG";
		else if (type.equalsIgnoreCase("bmp"))
			t = "BMP";

		else 
			{
			inputStream = new StringBufferInputStream("Not a Valid type");
			return "error";
			}
		
		int dimX = 120;
		int dimY = 150;
		byte[] finaldata = null;
		try
		{

			ByteArrayInputStream is = new ByteArrayInputStream(b);
			SeekableStream s = SeekableStream.wrapInputStream(is, true);
			RenderedOp objImage = JAI.create("stream", s);
			((OpImage) objImage.getRendering()).setTileCache(null);

			float width = (float) objImage.getWidth();
			float height = (float) objImage.getHeight();


			ParameterBlock pb = new ParameterBlock();
			pb.addSource(objImage); // The source image
			pb.add(dimX/width); // The xScale
			pb.add(dimY/height); // The yScale
			pb.add(0.0F); // The x translation
			pb.add(0.0F); // The y translation
			pb.add(new InterpolationNearest()); // The interpolation

			objImage = JAI.create("scale", pb, null);

			ByteArrayOutputStream out1 = new ByteArrayOutputStream();

			ImageEncoder imgEnc = ImageCodec.createImageEncoder(t, out1,
					null);
			imgEnc.encode(objImage);
			finaldata = out1.toByteArray();		
			
			if((upload.length()/1024) > 150)
				{
				inputStream = new StringBufferInputStream("largefile");
				return "error";
				}

			
		}catch(Exception e)
		{
			e.printStackTrace();
			inputStream = new StringBufferInputStream("Not a Valid type");
			return "error";
		}
		Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100000);

		String realpath = servlet.getRealPath("");
		String sessionId=ServletActionContext.getRequest().getSession().getId();
//		System.out.println("SessionId :"+ServletActionContext.getRequest().getSession().getId());
		String filepath = "/resources/tmpPhotos/"+sessionId +randomInt+".jpeg";
		FileOutputStream out =  new FileOutputStream(realpath+filepath);
		out.write(finaldata);
//		ServletActionContext.getRequest().getSession().setAttribute("uploadedimage", finaldata);
//		ServletActionContext.getRequest().getSession().setAttribute("uploadImageRand", randomInt);
		
		inputStream = new StringBufferInputStream(filepath);
		in.close();
		if(userType.equalsIgnoreCase("user"))
			ServletActionContext.getRequest().getSession().setAttribute("photo_"+userId,realpath+filepath);
		else if(userType.equalsIgnoreCase("customer"))
			ServletActionContext.getRequest().getSession().setAttribute("photo_"+customerId,realpath+filepath);
		return SUCCESS;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	public InputStream getInputStream()
	{
		return inputStream;
	}

	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}
	
}
 
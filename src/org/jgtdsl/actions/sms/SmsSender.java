package org.jgtdsl.actions.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.jgtdsl.models.SMSService;

public class SmsSender   {

		
	private String mobile;
	private String customerID;
	private String billMonth;
	private String billYear;		
	private String text;

		
//	public void run() {
//		
//		sendSMSSSL();
//		
//	}

	
	 public void sendSMSSSL() {
	        String targetURL = "http://sms.sslwireless.com/pushapi/dynamic_buet/server.php";
	        URL url;
	        HttpURLConnection connection = null;
	        try {
	            StringBuilder urlParameter = new StringBuilder();
	            urlParameter.append("sid=").append(URLEncoder.encode("BUET123", "UTF-8"));
	            urlParameter.append("&user=").append(URLEncoder.encode("buetiict123", "UTF-8"));
	            urlParameter.append("&pass=").append(URLEncoder.encode("buet@1231", "UTF-8"));
	            urlParameter.append("&sms[0][0]=").append(URLEncoder.encode("88" + this.mobile, "UTF-8"));
	            urlParameter.append("&sms[0][1]=").append(URLEncoder.encode(this.text, "UTF-8"));
	            urlParameter.append("&sms[0][2]=").append(URLEncoder.encode("abc", "UTF-8"));
	            url = new URL(targetURL);
	            connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");
	            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	            connection.setRequestProperty("Content-Length",""
	                            + Integer.toString(urlParameter.toString()
	                                    .getBytes().length));
	            connection.setRequestProperty("Content-Language", "en-US");

	            connection.setUseCaches(false);
	            connection.setDoInput(true);
	            connection.setDoOutput(true);

	            // Send request
	            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	            wr.writeBytes(urlParameter.toString());
	            wr.flush();
	            wr.close();
	           // ApplicationDAO.sendTextMSG_TT(this.appid, this.mobile);
	            InputStream is = connection.getInputStream();
	            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	            String line;
	            StringBuffer response = new StringBuffer();
	            while ((line = rd.readLine()) != null) {
	                response.append(line);
	                response.append('\r');
	            }
	            System.out.println(response.toString());
	            if(response.toString().contains("PARAMETER"))
	            	SMSService.sentCustSMS(this.customerID, this.billMonth, this.billYear );
	            
	            rd.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (connection != null) {
	                connection.disconnect();
	            }
	        }
	    }


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getCustomerID() {
		return customerID;
	}


	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}


	public String getBillMonth() {
		return billMonth;
	}


	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}


	public String getBillYear() {
		return billYear;
	}


	public void setBillYear(String billYear) {
		this.billYear = billYear;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}
	
	
	
}

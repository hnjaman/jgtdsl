package org.jgtdsl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONValue;

public class Utils {

	public static final String EMPTY_STRING = "";
	public static final String SBOX_LABEL_SEPERATOR= ":";
	public static final String SBOX_ITEM_SEPERATOR = ";";
	public static final String USER_DEFAULT_PASSWORD = "12345";
	
	public static String join(String s, Object... a) {
	    return a.length == 0 ? "" : a[0] + (a.length == 1 ? "" : s + join(s, Arrays.copyOfRange(a, 1, a.length)));
	}
	
	 public static String getJsonString(String status,String message){
		  Map<String, Object> map = new HashMap<String, Object>();
		  map.put("status", status);
		  map.put("message", message);
		  return JSONValue.toJSONString(map);
		 }
	 
	 public static String getJsonString(String status,String message,String dialogCaption){
		  Map<String, Object> map = new HashMap<String, Object>();
		  map.put("status", status);
		  map.put("message", message);
		  map.put("dialogCaption", dialogCaption);
		  return JSONValue.toJSONString(map);
		 }
	 
	 public static boolean copyFile(File file1,File file2)
	 {
		 	InputStream inStream = null;
	    	OutputStream outStream = null;
	    	try{
	 
	 
	    	    inStream = new FileInputStream(file1);
	    	    outStream = new FileOutputStream(file2); // for override file content
	    	    //outStream = new FileOutputStream(file2,<strong>true</strong>); // for append file content
	 
	    	    byte[] buffer = new byte[1024];
	 
	    	    int length;
	    	    while ((length = inStream.read(buffer)) > 0){
	    	    	outStream.write(buffer, 0, length);
	    	    }
	 
	    	    if (inStream != null)inStream.close();
	    	    if (outStream != null)outStream.close();
	 
	    	    System.out.println("File Copied..");
	    	    return true;
	    	}catch(IOException e){
	    		e.printStackTrace();
	    		return false;
	    	}
	    	
	 }
	 
	 public static boolean isNullOrEmpty(String val){
		 if(val==null || val.equalsIgnoreCase(""))
			 return true;
		 else
			 return false;
	 }
	 public static String null2Empty(String val){
		 if(val==null)
			 return Utils.EMPTY_STRING;
		 else
			 return val;
	 }

	 public static String constructCacheKey(String... args){
			StringBuilder key= new StringBuilder(Utils.EMPTY_STRING);

			for(int i=0;i<args.length;i++){
				key.append(args[i]).append("_");
			}
			
			if(key.length()>0)
				key.deleteCharAt(key.length()-1);
			
			return key.toString();
		 
		 
	 }
	 public static String constructCacheKey(int index, int offset,int total,String... args){
			StringBuilder key= new StringBuilder(Utils.EMPTY_STRING);

			key.append(index).append("_").append(offset).append("_").append(total).append("_");
			for(int i=0;i<args.length;i++){
				if(!Utils.isNullOrEmpty(args[i]))
					key.append(args[i].replaceAll("\\s++", "_").replaceAll("=", "_").replaceAll("'", "_")).append("_");
			}
			
			if(key.length()>0)
				key.deleteCharAt(key.length()-1);
			
			return key.toString();
		 
		 
	 }
	 
	 public static int getLastDayOfMonth(int month,int year)
	 {
	    
	     int mapLastDay = 0;
	  
	     switch (month)
	     {
	         case 1: // fall through
	         case 3: // fall through
	         case 5: // fall through
	         case 7: // fall through
	         case 8: // fall through
	         case 10: // fall through
	         case 12:
	             mapLastDay = 31;
	             break;
	         case 4: // fall through
	         case 6: // fall through
	         case 9: // fall through
	         case 11:
	             mapLastDay = 30;
	             break;
	         case 2:
	             if (0 == year % 4 && 0 != year % 100 || 0 == year % 400)
	             {
	                 mapLastDay = 29;
	             }
	             else
	             {
	                 mapLastDay = 28;
	             }
	             break;
	     }
	     return mapLastDay;
	 }
	 public static Double getPresusreFactor(float pressure){ 
		 return (double) ((pressure+AC.P_FACTOR)/AC.P_FACTOR);
	 }
	 
	 public static Double getTemperatureFactor(double temperature){
		 
		 return (AC.T_FACTOR1/(AC.T_FACTOR2+temperature));
	 }
	 
	 public static int getNumberOfMonth(String fromMonthYear,String toMonthYear){
		 	int fromMonth=Integer.valueOf(fromMonthYear.substring(4,6));
		 	int fromYear=Integer.valueOf(fromMonthYear.substring(0,4));
		 	int toMonth=Integer.valueOf(toMonthYear.substring(4,6));
		 	int toYear=Integer.valueOf(toMonthYear.substring(0,4));
		 	int currMonth=Integer.valueOf(fromMonthYear.substring(4,6));
		 	int currYear=Integer.valueOf(fromMonthYear.substring(0,4));
		 	int totalMonth=0;
		 	while (true) {
		 		totalMonth=totalMonth+1;
		 		
		 	    if (currMonth == toMonth && toYear==currYear) {
		 	        break;
		 	    }
		 	    if (currMonth ==12) {
		 			currMonth=0;
		 			currYear=currYear+1;
		 	    }
		 	   currMonth=currMonth+1;
		 	   
		 	}
			 
			 return totalMonth;
	 }
	 public static int getDateDiffInDays(String fromDate,String toDate){
		 SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			long diffDays =0l;
			try {
			    Date date1 = myFormat.parse(fromDate);
			    Date date2 = myFormat.parse(toDate);
			    long diff = date2.getTime() - date1.getTime();
			    diffDays= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			} catch (ParseException e) {
			    e.printStackTrace();
			}
			
			 
			 return (int) diffDays;
	 }
	 
	 public static int getDateDiffInDays4rmConnDateToReadingDate(String fromDate,String toDate){
		 SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			long diffDays =0l;
			try {
			    Date date1 = myFormat.parse(fromDate);
			    Date date2 = myFormat.parse(toDate);
			    long diff = date2.getTime() - date1.getTime();
			    diffDays= TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			} catch (ParseException e) {
			    e.printStackTrace();
			}
			
			 
			 return (int) diffDays;
	 }
	 
	 public static String getCurrentDate(String format){
		 Date dateNow = new Date();
		 SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		 return dateFormat.format(dateNow);
	 }
	 
	 public static void main(String[] args){
		 
		 Utils utils=new Utils();
		 int a;
		 a=getNumberOfMonth("201601","201612");
		 System.out.println(a);
		 
	      
	
	 
	 
		 
		 
		 
		/* System.out.println(utils.getDateDiffInDays("1-2-2016", "1-3-2016"));
		 System.out.println(utils.getProportionalLoad(46f,29,2,2016));*/
	 }
	 public static float getProportionalLoad(float load,int dayDiff,int month ,int year){
			 Calendar calendar = Calendar.getInstance();
			 calendar.set(Calendar.YEAR, year);
			 calendar.set(Calendar.MONTH, month-1);
			 int numDays = calendar.getActualMaximum(Calendar.DATE);
			 
			 if(numDays==31)
				 numDays=30;
			 
			 System.out.println("days"+numDays);
				 return (load/numDays)*dayDiff;
			
	 }
	 
		public static String stringOfSize(int size, char ch)
		{
		    final char[] array = new char[size];
		    Arrays.fill(array, ch);
		    return new String(array);
		}
		public static String getCustomerID(String area,String category,String code){
			
			String customer_id="";
			StringBuffer customer_ids = null;
			int count=code.contains("/")?code.substring(code.indexOf('/')+1).length():0;
			String leftString="";
			if(count==0){
				leftString=stringOfSize(5,'0');	
			}else if(count==1){
				leftString=stringOfSize(7,'0');
			}else if(count==2){
				leftString=stringOfSize(8,'0');
			}
			;
			customer_id=area+category+(leftString+code).substring(code.length());
			return customer_id;
		}
		
			
	 }


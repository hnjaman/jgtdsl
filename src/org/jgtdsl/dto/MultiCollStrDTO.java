package org.jgtdsl.dto;

import java.util.Comparator;

public class MultiCollStrDTO implements Comparable<MultiCollStrDTO>{

	private String billId;	
	private String collectedAmount;	
	private String collectedSurcharge;
	private String actualSurcharge;
	private String actualAmount;
	private String surchargePerCollection;                                                                                    
	private int billMonth; 
	private int billYear;
	
	
	
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getCollectedAmount() {
		return collectedAmount;
	}
	public void setCollectedAmount(String collectedAmount) {
		this.collectedAmount = collectedAmount;
	}
	public String getCollectedSurcharge() {
		return collectedSurcharge;
	}
	public void setCollectedSurcharge(String collectedSurcharge) {
		this.collectedSurcharge = collectedSurcharge;
	}
	public String getActualSurcharge() {
		return actualSurcharge;
	}
	public void setActualSurcharge(String actualSurcharge) {
		this.actualSurcharge = actualSurcharge;
	}
	public String getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}
	public String getSurchargePerCollection() {
		return surchargePerCollection;
	}
	public void setSurchargePerCollection(String surchargePerCollection) {
		this.surchargePerCollection = surchargePerCollection;
	}
	public int getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(int billMonth) {
		this.billMonth = billMonth;
	}
	public int getBillYear() {
		return billYear;
	}
	public void setBillYear(int billYear) {
		this.billYear = billYear;
	}
	
	
	@Override
	public int compareTo(MultiCollStrDTO o) {
		return Comparators.billYearMonth.compare(this, o);
		
	}
	
	
	
	
	public static class Comparators {

        public static Comparator<MultiCollStrDTO> billYear = new Comparator<MultiCollStrDTO>() {
            @Override
            public int compare(MultiCollStrDTO o1, MultiCollStrDTO o2) {
            	Integer year1 = o1.getBillYear() ;
            	Integer year2 = o2.getBillYear() ;            	
                return year1.compareTo(year2);
            }
        };
        
        
        public static Comparator<MultiCollStrDTO> billMonth = new Comparator<MultiCollStrDTO>() {
            @Override
            public int compare(MultiCollStrDTO o1, MultiCollStrDTO o2) {
            	Integer month1 = o1.getBillMonth() ;
            	Integer month2 = o2.getBillMonth() ;
            	return month1.compareTo(month2);
            }
        };
        public static Comparator<MultiCollStrDTO> billYearMonth = new Comparator<MultiCollStrDTO>() {
            @Override
            public int compare(MultiCollStrDTO o1, MultiCollStrDTO o2) {
                
        		Integer year1 = o1.getBillYear() ;
        		Integer year2 = o2.getBillYear() ;
        		
        		Integer month1 = o1.getBillMonth() ;
        		Integer month2 = o2.getBillMonth() ;
        		
        		
        		 int value1 = year1.compareTo(year2);
        	        if (value1 == 0) {
        	             return month1.compareTo(month2);        	            
        	        }
        	        return value1;
            
            }
        };
    }


	
	
	
}

package org.jgtdsl.actions;

import java.util.ArrayList;

import org.apache.struts2.interceptor.SessionAware;
import org.jgtdsl.dto.CacheKeyDTO;
import org.jgtdsl.dto.JqGridData;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.models.AdministrationService;
import org.jgtdsl.models.HolidayService;
import org.jgtdsl.utils.AC;

public class Administration extends BaseAction implements SessionAware{

	private static final long serialVersionUID = 7774247661672434299L;
	private String cacheName;
	private String cacheKey;
	private ArrayList<CacheKeyDTO> keyList;
	
	public String cacheMonitorHome()
	{
		return SUCCESS;
	}
	public String getAllKeys(){
		
		JqGridData<?> gridData = new JqGridData(0, 0, 0, AdministrationService.getAllCacheKeys(AC.CACHE_NAME));
	     //System.out.println("Grid Data: " + gridData.getJsonString());
	     try{
	    	 response.setContentType("json");
	    	 response.getWriter().write(gridData.getJsonString());
	          }
	        catch(Exception e) {e.printStackTrace();}
	        return null;
	}
	public String getCacheKeyValue(){
		
		try{
	    	 response.setContentType("json");
	    	 response.getWriter().write(AdministrationService.getCacheKeyValue(AC.CACHE_NAME,cacheKey));
	          }
	        catch(Exception e) {e.printStackTrace();}
	        return null;
	}

	public String deleteCacheKey(){		
		ResponseDTO response=AdministrationService.deleteCacheKey(AC.CACHE_NAME,cacheKey);		
		setJsonResponse(response);
		return null;	
	}
	public String clearCache(){		
		ResponseDTO response=AdministrationService.clearCache(AC.CACHE_NAME);		
		setJsonResponse(response);
		return null;	
	}
	
	
	
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public ArrayList<CacheKeyDTO> getKeyList() {
		return keyList;
	}
	public void setKeyList(ArrayList<CacheKeyDTO> keyList) {
		this.keyList = keyList;
	}
	public String getCacheKey() {
		return cacheKey;
	}
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	
}

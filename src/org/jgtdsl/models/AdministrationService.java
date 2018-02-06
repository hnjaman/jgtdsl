package org.jgtdsl.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.jgtdsl.dto.CacheKeyDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.utils.AC;
import org.jgtdsl.utils.cache.CacheUtil;

public class AdministrationService {

	@SuppressWarnings("unchecked")
	public static ArrayList<CacheKeyDTO> getAllCacheKeys(String cacheName){
		ArrayList<CacheKeyDTO> keyList=new ArrayList<CacheKeyDTO>();
		
		Cache cache=CacheUtil.cacheMgr.getCache(AC.CACHE_NAME);	
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
		CacheKeyDTO cacheKeyDTO;
		//for (Object key : cache.getKeys()) {
		for (Iterator<Object> it = cache.getKeys().iterator(); it.hasNext();) {
			cacheKeyDTO=new CacheKeyDTO();
		    Object key = it.next();
		    cacheKeyDTO.setKey(key.toString());
		    Element e = cache.getQuiet(key);
		    cacheKeyDTO.setCreated_on(sdf.format(new Date(e.getCreationTime())));
		    cacheKeyDTO.setLast_accessed_on(sdf.format(new Date(e.getLastAccessTime())));
		    cacheKeyDTO.setHit_count(String.valueOf(e.getHitCount()));
		    keyList.add(cacheKeyDTO);
		    //out.write("<td><A href=monitor.jsp?showCache=" + showCache + "&key=" + URLEncoder.encode(key.toString(),"UTF-8") + ">Object.toString()</A></td>");		    
		    }

		Collections.sort(keyList, new Comparator() {
			@Override
			public int compare(Object e1, Object e2) {
				String id1 = ((CacheKeyDTO) e1).getKey();
				String id2 = ((CacheKeyDTO) e2).getKey();

				// ascending order
				 return id1.compareTo(id2);

				// descending order
				//return id2.compareTo(id1);
			}
		});
		return keyList;
	}
	
	public static String getCacheKeyValue(String cache,String key){	
		Element e = CacheUtil.cacheMgr.getCache(AC.CACHE_NAME).getQuiet(key);
		return e.toString();
	}
	
	public static ResponseDTO deleteCacheKey(String cache,String key){	
		ResponseDTO response=new ResponseDTO();
		CacheUtil.cacheMgr.getCache(AC.CACHE_NAME).remove(key);
		response.setMessasge("Successfully Removed the Key.");
		response.setResponse(true);
		return response;
	}
	
	public static ResponseDTO clearCache(String cache){	
		ResponseDTO response=new ResponseDTO();
		CacheUtil.cacheMgr.getCache(AC.CACHE_NAME).removeAll();
		Runtime.getRuntime().gc();
		response.setMessasge("Successfully Cleaned Cache.");
		response.setResponse(true);
		return response;
	}
}

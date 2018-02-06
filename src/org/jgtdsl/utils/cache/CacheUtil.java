package org.jgtdsl.utils.cache;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

import org.jgtdsl.utils.AC;

/**
 * Utilitary class for Ehcache.
 * 
 * @author huseyin
 *
 */
public class CacheUtil {
	
	public static CacheManager cacheMgr = null;
	
	/*
	 * Get data from the cache. This method could be synchronized.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getListFromCache(String key,Class<T> klass){
		
		Ehcache cache = getCache(AC.CACHE_NAME);
		Element element = null;
		if(cache!=null){
			element = cache.get(key);
		}
		if(element!=null)
			return (ArrayList<T>) element.getObjectValue();
		else
			return null;
	}
	
	public static Object getObjFromCache(String key){
		
		Ehcache cache = getCache(AC.CACHE_NAME);
		Element element = null;
		if(cache!=null){
			element = cache.get(key);
		}
		if(element!=null)
			return element.getObjectValue();
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void setListToCache(String key,List objList){
		
		Ehcache cache = getCache(AC.CACHE_NAME);
		cache.put(new Element(key, objList));
		
		
	}
	
	public static void setObjToCache(String key,Object obj){
		
		Ehcache cache = getCache(AC.CACHE_NAME);
		cache.put(new Element(key, obj));		
		
		
	}
	
	public static void clearStartWith(String key){
		  Ehcache cache = getCache(AC.CACHE_NAME);
		  Query query;
		  Results results;
		  query = cache.createQuery();
		  query.includeKeys();
		  query.addCriteria(Query.KEY.ilike(key+"*")).end();
		  results = query.execute();
		  try{
			  for (Result result : results.all()) {
				  cache.remove(result.getKey());
			  }	
		  }
		  catch(Exception ex){}
	}
	

	public static void clear(String key){
		Ehcache cache = getCache(AC.CACHE_NAME);
		cache.remove(key);
	}
	/**
	 * Get the cache instance of Ehcache. This method could be synchronized.
	 * @param cacheName
	 * @return
	 */
	private static Ehcache getCache(String cacheName){
		if(cacheMgr == null){
			// We could use an environment or a VM variable
			//cacheMgr = CacheManager.create("d:\\cache_config\\ehcache.xml");
			cacheMgr = CacheManager.getInstance();
		}
		
		Ehcache cache = null;
		if(cacheMgr!=null){
			//cache = cacheMgr.addCacheIfAbsent(name);
			cache = cacheMgr.getEhcache(cacheName);
			//It is possible to override the parameters from ehcache.xml
			/*cache.getCacheConfiguration().setTimeToIdleSeconds(1);
			cache.getCacheConfiguration().setTimeToLiveSeconds(2);
			*/
		}
		
		return cache;
	}
	
	
	
	

}

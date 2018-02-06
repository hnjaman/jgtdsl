package org.jgtdsl.dto;

import com.google.gson.Gson;

public class CacheKeyDTO implements Comparable{

	private String key;
	private String created_on;
	private String last_accessed_on;
	private String hit_count;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String createdOn) {
		created_on = createdOn;
	}
	public String getLast_accessed_on() {
		return last_accessed_on;
	}
	public void setLast_accessed_on(String lastAccessedOn) {
		last_accessed_on = lastAccessedOn;
	}
	public String getHit_count() {
		return hit_count;
	}
	public void setHit_count(String hitCount) {
		hit_count = hitCount;
	}
	@Override
	public int compareTo(Object emplyoee) {
		String k1 = ((CacheKeyDTO) emplyoee).getKey();

		return k1.compareTo(this.key);
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
	
	
	
}

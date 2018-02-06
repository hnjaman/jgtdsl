package org.jgtdsl.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.json.simple.JSONValue;
 
public class JqGridData<T> {
 
 /** Total number of pages */
 private int total;
 /** The current page number */
 private int page;
 /** Total number of records */
 private int records;
 /** The actual data */
 private List<T> rows;
 
 public JqGridData(int total, int page, int records, List<T> rows) {
  this.total = total;
  this.page = page;
  this.records = records;
  this.rows = rows;
 }
 
 public int getTotal() {
  return total;
 }
 
 public int getPage() {
  return page;
 }
 
 public int getRecords() {
  return records;
 }
 
 public List<T> getRows() {
  return rows;
 }
  
 public String getJsonString(){
  Map<String, Object> map = new HashMap<String, Object>();
  map.put("page", page);
  map.put("total", total);
  map.put("records", records);
  map.put("rows", rows);
  return JSONValue.toJSONString(map);
 }
}
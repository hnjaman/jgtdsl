
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Troy Thompson, Bob Byron<p>
 * Company:      JavaUnderground<p>
 * @author       Troy Thompson, Bob Byron
 * @version 1.1
 */
package org.jgtdsl.utils.jdbc;

import java.util.Calendar;
import java.math.BigDecimal;
import java.io.*;
import java.sql.*;

public class DefaultSqlFormatter extends SqlFormatter {

  final String ymd24="'YYYY-MM-DD HH24:MI:SS.#'";

  private String format(Calendar cal){
    return "TO_DATE('" + new java.sql.Timestamp(cal.getTime().getTime()) + "',"+ymd24+")";
  }

  private String format(java.sql.Date date){
    return "TO_DATE('" + new java.sql.Timestamp(date.getTime()) + "',"+ymd24+")";
  }

  private String format(java.sql.Time time){
    Calendar cal = Calendar.getInstance();
    cal.setTime(new java.util.Date(time.getTime()));
    return "TO_DATE('" + cal.get(Calendar.HOUR_OF_DAY) + ":" +
      cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + "." +
      cal.get(Calendar.MILLISECOND) + "','HH24:MI:SS.#')";
  }

  private String format(java.sql.Timestamp timestamp){
    return "TO_DATE('" + timestamp.toString() + "','YYYY-MM-DD HH24:MI:SS.#')";
  }



  public String format(Object o) throws SQLException{
    if (o == null)               return "NULL";
    if (o instanceof Calendar)   return format((Calendar)o);
    if (o instanceof Date)       return format((Date)o);
    if (o instanceof Time)       return format((Time)o);
    if (o instanceof Timestamp)  return format((Timestamp)o);
    //if object not in one of our overridden methods, send to super class
    return super.format(o);
  }
}
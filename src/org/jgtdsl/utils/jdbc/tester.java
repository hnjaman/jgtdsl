package org.jgtdsl.utils.jdbc;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

 import java.sql.*;
 import java.util.*;

 public class tester {

  public tester() {
  }

  public static void main(String[] args) {
    System.out.println("Hello World");
    String[] a=new String[0];


    SqlFormatter formatter = new OracleSqlFormatter();
    Object o=new java.sql.Date(new java.util.Date().getTime());
    try{
        System.out.println("date="+formatter.format(o));
        o=new Long(198);
        System.out.println("Long="+formatter.format(o));
        o=new Boolean(true);
        System.out.println("Boolean="+formatter.format(o));
    }catch(SQLException e){
        e.printStackTrace();
    }

        //this goes in static debug class once, not individual classes
        StatementFactory.setDefaultDebug(DebugLevel.VERBOSE);
        StatementFactory.setDefaultFormatter(new OracleSqlFormatter());

        //test debuggable
        java.sql.Connection con = null;
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;

        String databaseURL = "jdbc:interbase://10.0.0.1/opt/interbase/examples/employee.gdb";
        System.out.println(databaseURL);
        String user = "sysdba";
        String password = "masterkey";
        String driverName = "interbase.interclient.Driver";

        try{
            Class.forName("interbase.interclient.Driver");
            System.out.println("interbase.interclient.Driver");
            java.util.Properties props = new java.util.Properties();
            props.put ("user", user);
            props.put ("password", password);
            System.out.println("user = " + props.get("user"));
            con = DriverManager.getConnection(databaseURL,props);
            System.out.println ("Connection established.");

            String sql = "select * from employee where last_name = ? or first_name = 'john?'";
            ps = StatementFactory.getStatement(con,sql);
            ps.setString(1,"Montgomery");
            System.out.println(ps.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("employee name = " + rs.getString("last_name"));
            }

        }catch(java.lang.ClassNotFoundException e){
            e.printStackTrace();
        }catch(java.sql.SQLException se){
            se.printStackTrace();
        }
        finally{
            try{
                if(rs != null){rs.close();}
                if(ps != null){ps.close();}
                if(!con.isClosed()) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }



  }
}
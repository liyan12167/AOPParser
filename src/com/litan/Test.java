package com.litan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
	try {
	    // 连接SQLite的JDBC
	    System.out.println("==start==");
	    long t1 = System.currentTimeMillis();
	    Class.forName("org.sqlite.JDBC");

	    // 建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下创建之

	    Connection conn = DriverManager
		    .getConnection("jdbc:sqlite:test.db");

	    Statement stat = conn.createStatement();

	    stat.executeUpdate("create table tbl1(id INTEGER PRIMARY KEY,name varchar(20), salary int);");// 创建一个表，两列
	    conn.setAutoCommit(false);
	    for (int i = 0; i < 2; i++) {
//		stat.executeUpdate("insert into tbl1 values('ZhangSan',8000);"); // 插入数据
//		stat.execute("insert into tbl1 values('ZhangSan',8000);"); // 插入数据
		stat.addBatch("insert into tbl1(name,salary) values('ZhangSan',8000);");
	    }
	    stat.executeBatch();
//	    stat.executeUpdate("insert into tbl1 values('LiSi',7800);");
//	    stat.executeUpdate("insert into tbl1 values('WangWu',5800);");
//	    stat.executeUpdate("insert into tbl1 values('ZhaoLiu',9100);");

	    ResultSet rs = stat.executeQuery("select * from tbl1;"); // 查询数据

	    while (rs.next()) { // 将查询到的数据打印出来
		System.out.println("id=" + rs.getInt("id"));
		System.out.print("name = " + rs.getString("name")); // 列属性一

		System.out.println("salary = " + rs.getString("salary")); // 列属性二

	    }
	    rs.close();
	    conn.close(); // 结束数据库的连接
	    System.out.println("==endt==");
	    long t2 = System.currentTimeMillis();
	    System.out.println((t2 - t1)/ 1000);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}

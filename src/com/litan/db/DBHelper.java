package com.litan.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private static final String DB_NAME = "aop.db";

    private static final String TABLE_PM_CREATE_SQL =
	    "create table PM("
	    + "id INTEGER PRIMARY KEY,"
	    + "time TEXT,"
	    + "pid INTEGER,"
	    + "tid INTEGER,"
	    + "tag TEXT,"
	    + "pkg TEXT,"
	    + "sig TEXT,"
	    + "tt INTEGER,"
	    + "ct INTEGER"
	    + ");";
    public static boolean isDbExist() {
	return new File(DB_NAME).exists();
    }

    public static void createDB() throws SQLException, ClassNotFoundException {
	if (isDbExist()) {
	    if (!new File(DB_NAME).delete()) {
		throw new RuntimeException("File delete failed");
	    }
	}
	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
	Statement stat = conn.createStatement();
	stat.executeUpdate(TABLE_PM_CREATE_SQL);
	conn.close();
    }
}

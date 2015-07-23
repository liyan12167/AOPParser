package com.litan.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.litan.Column;
import com.litan.ContentValues;

public class DBHelper {
    private static final String DB_NAME = "aop.db";
    private static final String TB_PM = "PM";
    private static final String TABLE_PM_CREATE_SQL =
	    "create table " + TB_PM + "("
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

    private static Statement sSt;
    private static Connection sConn;
    public static void createDB() throws SQLException, ClassNotFoundException {
	if (isDbExist()) {
	    if (!new File(DB_NAME).delete()) {
		throw new RuntimeException("File delete failed");
	    }
	}
	Class.forName("org.sqlite.JDBC");
	sConn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
	sConn.setAutoCommit(false);
	sSt = sConn.createStatement();
	sSt.executeUpdate(TABLE_PM_CREATE_SQL);
    }

    public static void insertPM(ContentValues cv) throws SQLException {
	StringBuilder INSERT = new StringBuilder("insert into " + TB_PM + " values(");
	INSERT.append("'").append(cv.getAsString(Column.TIME)).append("',");
	INSERT.append(cv.getAsInteger(Column.PID)).append(",");
	INSERT.append(cv.getAsInteger(Column.TID)).append(",");
	INSERT.append("'").append(cv.getAsString(Column.TAG)).append("',");
	INSERT.append("'").append(cv.getAsString(Column.PKG)).append("',");
	INSERT.append("'").append(cv.getAsString(Column.SIG)).append("',");
	INSERT.append(cv.getAsInteger(Column.THREAD_TIME)).append(",");
	INSERT.append(cv.getAsInteger(Column.CPU_TIME)).append(");");
	sSt.addBatch(INSERT.toString());
    }
    
    public static void commit() throws SQLException {
	sSt.executeBatch();
	sConn.commit();
	sSt.close();
	sConn.close();
    }
}

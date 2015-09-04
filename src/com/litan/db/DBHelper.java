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
	    + "id INTEGER PRIMARY KEY,"
	    + "time TEXT,"
	    + "pid INTEGER,"
	    + "tid INTEGER,"
	    + "tag TEXT,"
	    + "pkg TEXT,"
	    + "sig TEXT,"
	    + "tt INTEGER,"
	    + "ct INTEGER,"
	    + "tct LONG,"
	    + "pct LONG"
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

    private static final String INSERT_SQL = "insert into " + TB_PM + "(time,pid,tid,tag,pkg,sig,tt,ct,tct,pct)" + " values('%s',%d,%d,'%s','%s','%s',%d,%d,%d,%d);";
    public static void insertPM(ContentValues cv) throws SQLException {
	sSt.addBatch(String.format(INSERT_SQL, cv.getAsString(Column.TIME),
		cv.getAsInteger(Column.PID), cv.getAsInteger(Column.TID),
		cv.getAsString(Column.TAG), cv.getAsString(Column.PKG),
		cv.getAsString(Column.SIG),
		cv.getAsInteger(Column.THREAD_TIME),
		cv.getAsInteger(Column.CPU_TIME),
		cv.getAsLong(Column.CUR_THREAD_CPU_TIME),
		cv.getAsLong(Column.CUR_PROCESS_CPU_TIME)));
    }

    public static void commit() throws SQLException {
	sSt.executeBatch();
	sConn.commit();
	sSt.close();
	sConn.close();
    }
}

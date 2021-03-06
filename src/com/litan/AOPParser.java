package com.litan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.litan.db.DBHelper;

public class AOPParser {
    private static final String FILE = "f";
    private static final String DELETE = "d";

    public static void main(String[] args) throws ParseException, SQLException, ClassNotFoundException {
	Options opts = new Options();
	opts.addOption(new Option(FILE, true, "指定数据源文件"));
	opts.addOption(new Option(DELETE, "delete old DB, force to generate new DB"));
	CommandLineParser parser = new DefaultParser();
	CommandLine cl = parser.parse(opts, args);
	if (!cl.hasOption(FILE)) {
	    Log.e("-f 是必须的,用来制定数据源文件");
	    return;
	}
	if (!cl.hasOption(DELETE) && DBHelper.isDbExist()) {
	    Log.e("DB already exists, append -d to force delete it");
	    return;
	}
	DBHelper.createDB();
	String file = cl.getOptionValue(FILE);
	Log.d("file:" + file);
	try {
	    BufferedReader br = new BufferedReader(new FileReader(
		    new File(file)));
	    String line;
	    ParsedFlow flow = new ParsedFlow();
	    try {
		while ((line = br.readLine()) != null) {
		    if (line.equals("")) {
			continue;
		    }
		    if (line.startsWith("-")) {
			continue;
		    }
		    if (line.startsWith("*")) {
			continue;
		    }
		    flow.parseLine(line);
		}
	    } catch (ParsedException e) {
		e.printStackTrace();
	    }
	    line = br.readLine();
	} catch (FileNotFoundException e) {
	    Log.e("文件未找到:" + file);
	    return;
	} catch (IOException e) {
	    e.printStackTrace();
	    return;
	}
	Log.d("解析完毕");
	DBHelper.commit();
	Log.d("COUNT:" + ParsedFlow.COUNT);
	// opt = new Option("threadTime", false, "按方法执行的平均线程消耗时间从高到低显示前十个");
    }
}

package com.litan;

import java.sql.SQLException;
import java.util.ArrayList;

import com.litan.db.DBHelper;
import com.litan.parse.LineParser;

public class ParsedFlow {
    private static final int LINE_UNKNOW = -1;
    private static final int LINE_TIME = 0;
    private static final int LINE_LOG = 1;

    private static class TimeLineParser implements LineParser {
	public void parse(String line, ContentValues data)
		throws ParsedException {
	    String[] datas = line.split(" +");
	    String monthAndDay = datas[1];
	    String time = datas[2];
	    String pidAndTid = datas[3];
	    int pid, tid;
	    if (pidAndTid.charAt(pidAndTid.length() - 1) == ':') {
	    	pid = Integer.valueOf(pidAndTid.substring(0, pidAndTid.length() - 1));
	    	tid = Integer.valueOf(datas[4]);
	    } else {
	    	datas = pidAndTid.split(":");
	    	pid = Integer.valueOf(datas[0]);
	    	tid = Integer.valueOf(datas[1]);
	    }
	    data.put(Column.TIME, String.format("%s %s", monthAndDay, time));
	    data.put(Column.PID, pid);
	    data.put(Column.TID, tid);
	}
    }

    private static class BodyLineParser implements LineParser {

	public void parse(String line, ContentValues data)
		throws ParsedException {
	    String[] datas = line.split(";");
	    String tagAndBody = datas[0];
	    String threadTime = datas[1];
	    String cpuTime = datas[2];
	    datas = tagAndBody.split(":");
	    String tag = datas[0];
	    String body = datas[1];
	    int methodEnd = body.indexOf("(");
	    String totalSigWithoutArgs = body.substring(0, methodEnd);
	    datas = totalSigWithoutArgs.split(" ");
	    String pkgClzMethod = datas[datas.length - 1];
	    datas = pkgClzMethod.split("\\.");
	    int pkgIndexEnd = datas.length - 3;
	    String mthSig = String.format("%s.%s", datas[pkgIndexEnd + 1],
		    datas[pkgIndexEnd + 2] + body.substring(methodEnd, body.length()));
	    StringBuilder pkgSb = new StringBuilder();
	    for (int i = 0; i <= pkgIndexEnd; i++) {
		pkgSb.append(datas[i]);
		if (i != pkgIndexEnd) {
		    pkgSb.append(".");
		}
	    }
	    data.put(Column.TAG, tag);
	    data.put(Column.PKG, pkgSb.toString());
	    data.put(Column.SIG, mthSig);
	    data.put(Column.THREAD_TIME, Integer.valueOf(threadTime));
	    data.put(Column.CPU_TIME, Integer.valueOf(cpuTime));
	}

    }

    private final ArrayList<LineParser> LINE_PARSER_LIST = new ArrayList<LineParser>();

    private int mNextLineShouldBe = LINE_UNKNOW;
    private ContentValues mCurValues = new ContentValues(8);

    public ParsedFlow() {
	LINE_PARSER_LIST.add(new TimeLineParser());
	LINE_PARSER_LIST.add(new BodyLineParser());
    }

    private int check(String line) throws ParsedException {
	if (!line.startsWith("[")) {
	    if (!line.startsWith("PM")) {
		throw new ParsedException("不能解析的行:" + line);
	    } else {
		return LINE_LOG;
	    }
	} else {
	    return LINE_TIME;
	}
    }

    public static int COUNT = 0;
    public void parseLine(String line) throws ParsedException, SQLException {
	int lineNo = check(line);
	if (lineNo == LINE_TIME) {
	    LINE_PARSER_LIST.get(lineNo).parse(line, mCurValues);
	    mNextLineShouldBe = LINE_LOG;
	} else {
	    if (mNextLineShouldBe == LINE_UNKNOW) {
		Log.d("过滤不完整行:" + line);
		return;
	    }
	    if (lineNo != mNextLineShouldBe) {
		throw new ParsedException("数据源掺杂未知行:" + line);
	    }
	    if (lineNo == LINE_LOG) {
		LINE_PARSER_LIST.get(lineNo).parse(line, mCurValues);
		COUNT++;
		DBHelper.insertPM(mCurValues);
		mCurValues.clear();
		mNextLineShouldBe = LINE_UNKNOW;
	    }
	}
    }
}

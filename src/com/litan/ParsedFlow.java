package com.litan;

import java.util.ArrayList;

import com.litan.parse.ContentValues;
import com.litan.parse.LineParser;

public class ParsedFlow {
    private static final int LINE_UNKNOW = -1;
    private static final int LINE_TIME = 0;
    private static final int LINE_LOG = 1;
    private static final int LINE_BR = 2;

    private static class TimeLineParser implements LineParser {
	public void parse(String line, ContentValues data)
		throws ParsedException {
	    String[] datas = line.split(" +");
	    String monthAndDay = datas[1];
	    String time = datas[2];
	    String pidAndTid = datas[3];
	    datas = pidAndTid.split(":");
	    int pid = Integer.valueOf(datas[0]);
	    int tid = Integer.valueOf(datas[1]);
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
	    data.put(Column.METHOD_SIG, mthSig);
	    data.put(Column.THREAD_TIME, Integer.valueOf(threadTime));
	    data.put(Column.CPU_TIME, Integer.valueOf(cpuTime));
	}

    }

    private final ArrayList<LineParser> LINE_PARSER_LIST = new ArrayList<LineParser>();

    private int mNextLineShouldBe = LINE_UNKNOW;
    private ContentValues mCurValues;

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

    public void parseLine(String line) throws ParsedException {
	int lineNo = check(line);
	if (lineNo == LINE_TIME) {
	    mCurValues = new ContentValues();
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
		mNextLineShouldBe = LINE_BR;
	    } else if (lineNo == LINE_BR) {
		LINE_PARSER_LIST.get(lineNo).parse(line, mCurValues);
		mNextLineShouldBe = LINE_TIME;
	    }
	}
    }
}

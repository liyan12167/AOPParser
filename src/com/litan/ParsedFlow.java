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
			// TODO Auto-generated method stub

		}

	}

	private static class BodyLineParser implements LineParser {

		public void parse(String line, ContentValues data)
				throws ParsedException {
			// TODO Auto-generated method stub

		}

	}

	private static class BrLineParser implements LineParser {

		public void parse(String line, ContentValues data)
				throws ParsedException {
			// TODO Auto-generated method stub

		}

	}

	private final ArrayList<LineParser> LINE_PARSER_LIST = new ArrayList<LineParser>();

	private int mNextLineShouldBe = LINE_UNKNOW;
	private ContentValues mCurValues;

	public ParsedFlow() {
		LINE_PARSER_LIST.add(new TimeLineParser());
		LINE_PARSER_LIST.add(new BodyLineParser());
		LINE_PARSER_LIST.add(new BrLineParser());
	}

	private int check(String line) throws ParsedException {
		if (!line.startsWith("[")) {
			if (!line.startsWith("PM")) {
				if (!line.equals("\\br")) {
					StringBuilder sb = new StringBuilder();
					sb.append("不能解析：").append(line).append("\\br")
							.append("请使用adb logcat -v long -s AOP作为数据源");
					Log.e(sb.toString());
					throw new ParsedException("未解析的行：" + line);
				} else {
					return LINE_BR;
				}
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
				Log.d("过滤起始无意义行：" + line);
				return;
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

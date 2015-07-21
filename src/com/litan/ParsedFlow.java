package com.litan;

public class ParsedFlow {
	private static final int LINE_TIME = 0;
	private static final int LINE_LOG = 1;
	private static final int LINE_BR = 2;
	private int mLine = LINE_TIME;

	public void parseLine(String line) throws ParsedException {
		if (mLine == LINE_TIME) {
			parseTimeLine(line);
			mLine++;
		} else if (mLine == LINE_TIME) {
			parseLogLine(line);
			mLine++;
		} else if (mLine == LINE_TIME) {
			parseBRLine(line);
			mLine = LINE_TIME;
		}
	}

	private void parseTimeLine(String line) throws ParsedException {

	}

	private void parseLogLine(String line) throws ParsedException {

	}

	private void parseBRLine(String line) throws ParsedException {

	}
}

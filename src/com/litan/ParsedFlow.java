package com.litan;

public class ParsedFlow {
	private static final int LINE_TIME = 0;
	private static final int LINE_LOG = 1;
	private static final int LINE_BR = 2;

	private int check(String line) throws ParsedException {
		if (!line.startsWith("[")) {
			if (!line.startsWith("PM")) {
				if (!line.equals("\\br")) {
					StringBuilder sb = new StringBuilder();
					sb.append("���ܽ�����").append(line).append("\\br")
							.append("��ʹ��adb logcat -v long -s AOP��Ϊ����Դ");
					Log.e(sb.toString());
					throw new ParsedException("δ�������У�" + line);
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
			parseTimeLine(line);
		} else if (lineNo == LINE_TIME) {
			parseLogLine(line);
		} else if (lineNo == LINE_TIME) {
			parseBRLine(line);
		}
	}

	private void parseTimeLine(String line) throws ParsedException {
	}

	private void parseLogLine(String line) throws ParsedException {
	}

	private void parseBRLine(String line) throws ParsedException {
	}
}

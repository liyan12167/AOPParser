package com.litan;

public class ParsedFlow {
    private static final int LINE_TIME = 0;
    private static final int LINE_LOG = 0;
    private static final int LINE_BR = 0;
    private int mLine = LINE_TIME;

    public void parseLine(String line) throws ParsedException {
        if (mLine == LINE_TIME) {

        } else if (mLine == LINE_TIME) {

        } else if (mLine == LINE_TIME) {

        }
        parseTimeLine();
        parseLogLine();
        parseBRLine();
    }

    private void parseTimeLine() throws ParsedException {

    }

    private void parseLogLine() throws ParsedException {

    }

    private void parseBRLine() throws ParsedException {

    }
}

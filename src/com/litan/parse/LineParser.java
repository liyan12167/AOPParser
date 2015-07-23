package com.litan.parse;

import com.litan.ContentValues;
import com.litan.ParsedException;

public interface LineParser {
	void parse(String line, ContentValues data) throws ParsedException;
}

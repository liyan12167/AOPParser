package com.litan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AOPParser {
	private static final String FILE = "f";

	public static void main(String[] args) throws ParseException {
		Options opts = new Options();
		Option opt = new Option(FILE, true, "指定数据源文件");
		opts.addOption(opt);
		CommandLineParser parser = new DefaultParser();
		CommandLine cl = parser.parse(opts, args);
		if (cl.hasOption(FILE)) {
			String file = cl.getOptionValue(FILE);
			Log.d("file:" + file);
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File(
						file)));
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
		} else {
			Log.e("-f 是必须的,用来制定数据源文件");
			return;
		}
		// opt = new Option("threadTime", false, "按方法执行的平均线程消耗时间从高到低显示前十个");
	}
}

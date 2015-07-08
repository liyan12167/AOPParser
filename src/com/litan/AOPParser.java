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
            System.out.println("file:" + file);
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(file)));
                String line;
                boolean timeLine = true;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    if (line.startsWith("-")) {
                        continue;
                    }
                    if (timeLine && line.startsWith("[")){
                    } else if (!timeLine){
                    } else {
                        System.err.println("数据源文件格式不正确，应使用adb logcat -v long -s AOP");
                        return;
                    }
                    timeLine = false;
                }
                line = br.readLine();
            } catch (FileNotFoundException e) {
                System.err.println("文件未找到:" + file);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else {
            System.err.println("-f 是必须的,用来制定数据源文件");
            return;
        }
//        opt = new Option("threadTime", false, "按方法执行的平均线程消耗时间从高到低显示前十个");
    }
}

package com.dianxinos.powermanager;

import java.lang.reflect.Method;
import java.util.HashMap;

class AOPHelper {
    private static final int PROC_SPACE_TERM = (int) ' ';
    // private static final int PROC_COMBINE = 0x100;
    private static final int PROC_PARENS = 0x200;
    private static final int PROC_OUT_LONG = 0x2000;
    // private static final int PROC_OUT_STRING = 0x1000;
    private static final int[] PROCESS_STATS_FORMAT = new int[] {
            PROC_SPACE_TERM, PROC_SPACE_TERM | PROC_PARENS, PROC_SPACE_TERM,
            PROC_SPACE_TERM, PROC_SPACE_TERM, PROC_SPACE_TERM, PROC_SPACE_TERM,
            PROC_SPACE_TERM, PROC_SPACE_TERM, PROC_SPACE_TERM | PROC_OUT_LONG, // 10:
            // minor
            // faults
            PROC_SPACE_TERM, PROC_SPACE_TERM | PROC_OUT_LONG, // 12: major
            // faults
            PROC_SPACE_TERM, PROC_SPACE_TERM | PROC_OUT_LONG, // 14: utime
            PROC_SPACE_TERM | PROC_OUT_LONG, // 15: stime
    };
    private static Method sReadProcFile;
    static {
        try {
            // sGetUidForPid =
            // android.os.Process.class.getMethod("getUidForPid", int.class);
            sReadProcFile = android.os.Process.class.getMethod("readProcFile",
                    String.class, int[].class, String[].class, long[].class,
                    float[].class);
            // sGetPids = android.os.Process.class.getMethod("getPids",
            // String.class, int[].class);
        } catch (Exception e) {
        }
    }
    private static HashMap<Integer, String> pidStatMap = new HashMap<Integer, String>();
    static long getCpuTime() {
        try {
            long[] procStats = new long[4];
            sReadProcFile.invoke(android.os.Process.class, getStat(), PROCESS_STATS_FORMAT, null, procStats, null);
            return procStats[2] + procStats[3];
        } catch (Exception e) {
        }
        return 0;
    }
    private static String getStat() {
        int pid = android.os.Process.myPid();
        String stat = pidStatMap.get(pid);
        if (stat == null) {
            stat = String.format("/proc/%s/stat", pid);
            pidStatMap.put(pid, stat);
        }
        return stat;
    }
}

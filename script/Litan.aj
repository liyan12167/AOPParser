package com.dianxinos.powermanager;

import android.os.SystemClock;
import android.util.Log;

public aspect Litan {
    pointcut public_methods() : execution(public * *.*(..));

    Object around()  : public_methods() {
        long c1 = AOPHelper.getCpuTime();
        long t1 = SystemClock.currentThreadTimeMillis();
        Object o = proceed();
        long t2 = SystemClock.currentThreadTimeMillis();
        long c2 = AOPHelper.getCpuTime();
        Log.d("AOP", String.format("PM:%s;%d;%d", thisJoinPoint.getSignature()
                .toLongString(), t2 - t1, c2 - c1));
        return o;
    }
}
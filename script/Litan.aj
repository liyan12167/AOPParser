package com.dianxinos.powermanager.aop;

import android.os.SystemClock;
import android.util.Log;

public aspect Litan {
    pointcut not_aop() : within(!com.dianxinos.powermanager.aop.*);
    pointcut public_methods() : execution(public * *(..)) && not_aop() && within(com.dianxinos.powermanager.* || com.dianxinos.powermanager.*.*);

    Object around()  : public_methods() {
        long c1 = AOPHelper.getCpuTime(true);
        long t1 = SystemClock.currentThreadTimeMillis();
        Object o = proceed();
        long t2 = SystemClock.currentThreadTimeMillis();
        long p1 = AOPHelper.getCpuTime(false);
        long c2 = AOPHelper.getCpuTime(true);
        Log.d("AOP", String.format("PM:%s;%d;%d;%d;%d", thisJoinPoint.getSignature()
                .toLongString(), t2 - t1, c2 - c1, c2, p1));
        return o;
    }
    pointcut method_cflow() : cflow(execution(@MethodCflow * *(..))) && withincode(@MethodCflow * *(..)) && call(* *(..));
    before() : method_cflow() {
        long c1 = AOPHelper.getCpuTime(true);
        long t1 = SystemClock.currentThreadTimeMillis();
        Log.d("MC", String.format("%s;%d;%d", thisJoinPoint.getSignature().toShortString(), c1, t1));
    }
}
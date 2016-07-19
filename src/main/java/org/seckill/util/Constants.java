package org.seckill.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class Constants {
    public static final String ACCESS_HEAD = "Access-Token";
    public static final int MAX_THREADS = 110;
    public static final int MAX_ACCEPTOR = 100;
    public static final int MAX_SELECTOR =2;
    public static final int MAX_REDIS = 250;
    public static final boolean THREADLOCAL_MODEL = true;
    public static final boolean runAsTomcat = true;


    public static final boolean DEBUG = false;

    public AtomicLong getCountMap(Object key) {
        return countMap.get(key);
    }

    public AtomicLong getCostMap(Object key) {
        return costMap.get(key);
    }

    public static Map<String, AtomicLong> countMap = new HashMap<String, AtomicLong>();
    public static Map<String, AtomicLong> costMap = new HashMap<String, AtomicLong>();

    public static AtomicLong borrowCount = new AtomicLong();
    public static AtomicLong borrowWait = new AtomicLong();
}

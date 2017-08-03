package org.gobiiproject.gobiimodel.utils;

import java.util.HashMap;

/**
 * CA simple timer class. Stores statically the String key of a timing event, and it's start time.
 */



public class SimpleTimer {
    private static HashMap<String,Long> times= new HashMap<String,Long>();

    public static void start(String timer){
        times.put(timer,System.currentTimeMillis());
    }

    public static Long time(String timer){
        return times.get(timer);
    }

    /**
     * Returns time from start of timer to stop of timer
     * @param timer
     * @return
     */
    public static Long stop(String timer){
        Long time = time(timer);
        if(time==null)return null;
        return System.currentTimeMillis()-time;
    }
}

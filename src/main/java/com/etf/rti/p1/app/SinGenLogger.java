package com.etf.rti.p1.app;

import java.util.HashSet;
import java.util.Set;

public final class SinGenLogger {

    private static Set<SinGenLoggerListener> listeners = new HashSet<>();

    private SinGenLogger() {
    }

    public static void addListener(SinGenLoggerListener listener) {
        listeners.add(listener);
    }

    public static void info(String log) {
        listeners.forEach(l -> l.onInfo(log));
    }

    public static void content(String log) {
        listeners.forEach(l -> l.onContent(log));
    }

    public static void error(String log, Exception e) {
        String combinedLog = log + ": " + e.getMessage();
        listeners.forEach(l -> l.onError(combinedLog));
        e.printStackTrace();
    }
}

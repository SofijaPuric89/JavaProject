package com.etf.rti.p1.app;

public interface SinGenLoggerListener {
    void onInfo(String log);

    void onContent(String log);

    void onError(String log);
}

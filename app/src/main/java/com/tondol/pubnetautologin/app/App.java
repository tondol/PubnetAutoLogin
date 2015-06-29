package com.tondol.pubnetautologin.app;

import android.app.Application;

/**
 * Created by hosaka on 2015/06/29.
 */
public class App extends Application {

    static private App sInstance;
    static public App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    @Override
    public void onTerminate() {
        sInstance = null;

        super.onTerminate();
    }

    private LoggingListener loggingListener;
    public interface LoggingListener {
        public void onLog(String s);
    }
    public void setLoggingListener(LoggingListener listener) {
        loggingListener = listener;
    }
    public void log(String s) {
        android.util.Log.d("pubnetautologin", s);
        if (loggingListener != null) {
            loggingListener.onLog(s);
        }
    }
}

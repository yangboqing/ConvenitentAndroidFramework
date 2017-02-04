package com.convenitent.framework.app;

import android.content.Context;

/**
 * Created by yangboqing on 16/8/16.
 */
public class $ {

    /**
     * 单例
     */
    private volatile static $ instance;
    public static $ getInstance() {
        if (instance == null) {
            synchronized ($.class) {
                if (instance == null) {
                    instance = new $();
                }
            }
        }
        return instance;
    }

    /**
     * 名义上为build,实则是检查一些必须配置的变量
     */
    public void build(){
        if (sAppContext == null) {
            throw new RuntimeException("please config the lesscode application context");
        }
    }

    /**
     * *********************************************************************************************
     * Global ApplicationContext
     * *********************************************************************************************
     */
    public static Context sAppContext;
    public $ context(Context context) {
        sAppContext = context;
        return this;
    }

    /**
     * *********************************************************************************************
     * AppUtils
     * *********************************************************************************************
     */
    public static final String KEY_DOWNLOAD_URL = "download_url";
    public static final String KEY_DOWNLOAD_PACKAGE = "download_package";
    static String sUpdateJsonUrl;
    public $ app(String updateJsonUrl) {
        sUpdateJsonUrl = updateJsonUrl;
        return this;
    }

    /**
     * *********************************************************************************************
     * UpdateUtils
     * *********************************************************************************************
     */
    public static String sDownloadSDPath;
    public static int sUpdateIcon;
    @Deprecated
    public $ update(String downloadSDPath) {
        sDownloadSDPath = downloadSDPath;
        return this;
    }

    public $ update(String downloadSDPath, int updateIcon) {
        sDownloadSDPath = downloadSDPath;
        sUpdateIcon = updateIcon;
        return this;
    }


    /**
     * *********************************************************************************************
     * DownloadUtils
     * *********************************************************************************************
     */
    public static int sConnectTimeOut = 5000;
    public static int sReadTimeout = 5000;
    public $ http(int connectTimeOut, int readTimeout) {
        sConnectTimeOut = connectTimeOut;
        sReadTimeout = readTimeout;
        return this;
    }

    /**
     * *********************************************************************************************
     * LogLess
     * *********************************************************************************************
     */
    public static boolean sDebug;
    public static String sTAG;
    public $ log(boolean debug, String tag) {
        sDebug = debug;
        sTAG = tag;
        return this;
    }

}

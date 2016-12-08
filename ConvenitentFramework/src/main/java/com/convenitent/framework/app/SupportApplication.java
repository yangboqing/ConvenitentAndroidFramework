package com.convenitent.framework.app;

import android.app.Application;

import com.convenitent.framework.image.universal.UniversalImageLoader;

/**
 * 初始化Application一些工作
 * 例如:Imageloader
 * Created by yangboqing on 16/8/24.
 */
public class SupportApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化此框架
        $.getInstance()
                .context(getApplicationContext())
                .build();
        //初始化默认图片加载框架
        new UniversalImageLoader().init();
    }
}

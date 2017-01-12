package com.convenitent.test;

import com.convenitent.framework.app.$;
import com.convenitent.framework.app.SupportApplication;

/**
 * Created by yangboqing on 2017/1/4.
 */

public class App extends SupportApplication {

    private static final String TAG = "App";
    @Override
    public void onCreate() {
        super.onCreate();
        $.getInstance().log(true,TAG)
                .update(null,R.mipmap.ic_launcher)
                .build();
    }
}

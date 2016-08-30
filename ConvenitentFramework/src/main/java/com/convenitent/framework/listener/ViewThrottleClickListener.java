package com.convenitent.framework.listener;

/**
 * Created by yangboqing on 16/8/16.
 * 防止快速点击的ClickListener
 */

import android.view.View;

import java.util.Calendar;


public abstract class ViewThrottleClickListener implements View.OnClickListener {
    private static final int THROTTLE_TIME_DEFAULT = 1000; // 1s
    public long lastClickTime = 0;

    public long getThrottleTime() {
        return THROTTLE_TIME_DEFAULT;
    }

    public abstract void throttleClick(View view);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > getThrottleTime()) {
            lastClickTime = currentTime;
            throttleClick(v);
        }
    }
}

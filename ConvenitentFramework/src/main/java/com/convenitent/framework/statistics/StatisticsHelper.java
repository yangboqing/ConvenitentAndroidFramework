package com.convenitent.framework.statistics;

import android.content.Context;

import java.util.Map;

/**
 * Created by yangboqing on 16/8/17.
 * 统计封装的抽象类
 */
public abstract class StatisticsHelper implements StatisticsListener{

    @Override
    public void onStatisticsProfileSignIn(String id){

    }

    @Override
    public void onStatisticsProfileSignOff(){

    }

    public abstract void onStatisticsResume(Context context);

    public abstract void onStatisticsPause(Context context);

    public abstract void onStatisticsEvent(Context context, String event);


    public void onStatisticsPageStart(String content){

    }

    @Override
    public void onStatisticsPageEnd(String content){

    }

    @Override
    public void onStatisticsEvent(Context context, String event, Map<String, String> params){

    }
}

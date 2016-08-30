package com.convenitent.framework.statistics;

import android.content.Context;

import java.util.Map;

/**
 * Created by yangboqing on 16/8/17.
 * 项目统计需要实现的方法
 */
public interface StatisticsListener {

    /**
     * 注册用户账号到统计平台
     * @param id
     */
    void onStatisticsProfileSignIn(String id);

    /**
     * 注销用户账号到统计平台
     */
    void onStatisticsProfileSignOff();

    /**
     * activity onResume方法调用
     * @param context
     */
    void onStatisticsResume(Context context);

    /**
     * activity onPause方法调用
     * @param context
     */
    void onStatisticsPause(Context context);

    /**
     * 如果自己统计页面打开，需在onResume或OnStart中调用
     * 注意必须在{@link StatisticsListener}类中onStatisticsResume方法之前调用
     * @param content
     */
    void onStatisticsPageStart(String content);

    /**
     * 如果自己统计页面打开，需在onPause或OnStop中调用
     * 注意必须在{@link StatisticsListener}类中onStatisticsPause方法之前调用
     * @param content
     */
    void onStatisticsPageEnd(String content);


    /**
     * 点击事件调用
     * @param context
     * @param event
     */
    void onStatisticsEvent(Context context,String event);

    /**
     * 多参数点击事件
     * @param context
     * @param event
     * @param params
     */
    void onStatisticsEvent(Context context, String event, Map<String, String> params);

}

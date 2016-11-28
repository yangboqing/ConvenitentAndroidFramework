package com.convenitent.framework.http.volley;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.convenitent.framework.http.ParserInter;

/**
 * Created by yangboqing on 2016/11/24.
 */

public class VolleyFactory {

    private static VolleyFactory factory = null;

    private static Context mContext;

    private RequestQueue mMainRequestQueue;

    public static VolleyFactory getInstance(Context context){
        if (factory == null){
            mContext = context;
            factory = new VolleyFactory();
        }
        return factory;
    }

    //volley初始化,必须调用
    public void initVolley(){
        if (mMainRequestQueue == null){
            mMainRequestQueue = Volley.newRequestQueue(mContext);
        }
    }

    /**
     * 添加请求任务
     * @param request
     */
    public void addRequest(Request<ParserInter> request){
        if (mMainRequestQueue == null){
            initVolley();
        }
        mMainRequestQueue.add(request);
    }

    /**
     * 清空队列
     */
    public void clearRequestQueue(){
        mMainRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                    return true;
            }
        });
    }

    /**
     * 根据tag来清除请求
     * @param tag
     */
    public void clearRequestByTag(@NonNull final String tag){
        mMainRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {

                if (request.getTag()!=null&&TextUtils.equals(request.getTag().toString(),tag)){
                    return true;
                }else{
                    return false;
                }
            }
        });
    }


}

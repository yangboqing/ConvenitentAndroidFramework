package com.convenitent.framework.http;

/**
 * Created by yangboqing on 2016/11/24.
 */

public interface RequestCallBack<T> {

    void onResponseSuccess(T parser);
    void onResponseError(T parser);
}

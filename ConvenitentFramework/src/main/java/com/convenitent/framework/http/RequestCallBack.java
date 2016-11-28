package com.convenitent.framework.http;

/**
 * Created by yangboqing on 2016/11/24.
 */

public interface RequestCallBack {

    void onResponseSuccess(BaseParser parser);
    void onResponseError(BaseParser parser);
}

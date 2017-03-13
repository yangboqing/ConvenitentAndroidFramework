package com.convenitent.framework.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.convenitent.framework.http.BaseParser;
import com.convenitent.framework.http.RequestCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by yangboqing on 2016/11/24.
 */

public class VolleyRequest<T extends BaseParser> extends Request<T> {

    private T parser;
    private final Response.Listener<T> mListener;
    private final Map<String, String> mRequestBody;

    public VolleyRequest(final String url, Map<String, String> requestContent, final T parser, final RequestCallBack<T> callBack){
        super(Method.POST, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (parser == null || callBack == null) {
                    return;
                }
                parser.setTips("当前网络异常"+error.getMessage());
                callBack.onResponseError(parser);
            }
        });
        mListener = new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (parser == null || callBack == null) {
                    return;
                }
                if (response.getCode() == BaseParser.SUCCESS){
                    callBack.onResponseSuccess(response);
                }else{
                    callBack.onResponseError(response);
                }

            }
        };
        this.parser = parser;
        this.mRequestBody = requestContent;
        setShouldCache(false);
    }

    //可缓存数据的请求
    public VolleyRequest(final String url, Map<String, String> requestBody,
                          final T parser, final RequestCallBack callBack, boolean isShouldCache) {
        super(Method.POST, url,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (parser == null || callBack == null) {
                    return;
                }
                parser.setTips("当前网络异常"+error.getMessage());
                callBack.onResponseError(parser);
            }
        });
        mListener = new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (parser == null || callBack == null) {
                    return;
                }
                if (parser.getCode() == BaseParser.SUCCESS){
                    callBack.onResponseSuccess(response);
                }else{
                    callBack.onResponseError(response);
                }
            }
        };
        this.parser = parser;
        this.mRequestBody = requestBody;
        setShouldCache(isShouldCache);
    }

    public VolleyRequest(final String url, final T parser,
                          final RequestCallBack callBack) {
        super(Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (parser == null || callBack == null) {
                    return;
                }
                parser.setTips("当前网络异常"+error.getMessage());
                callBack.onResponseError(parser);
            }
        });
        mListener = new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (parser == null || callBack == null) {
                    return;
                }
                if (parser.getCode() == BaseParser.SUCCESS){
                    callBack.onResponseSuccess(response);
                }else{
                    callBack.onResponseError(response);
                }
            }
        };
        this.parser = parser;
        this.mRequestBody = null;
        setShouldCache(true);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        Response<T> success;
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            parser.parser(json);
            success = Response.success(parser,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            return Response.error(new ParseError(e));
        }
        return success;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.mRequestBody == null?null:this.mRequestBody;
    }
}

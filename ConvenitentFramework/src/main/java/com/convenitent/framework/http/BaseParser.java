package com.convenitent.framework.http;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseParser<T> implements ParserInter{

	/**Error:默认值，网络错误.**/
	public final static String ERROR = "-1";

	/**Failed:有网络连接但数据格式错误.**/
	public final static String FAILED = "-2";

	/**Empty:在Success情况下数据为空，子类处理.**/
	public final static String EMPTY = "-3";

	/** Success:服务器返回数据正常.**/
	public final static String SUCCESS = "200";

	private String mCode;
	private String mTips;
	private String mMessage;

	private JSONObject mResObj;

	private boolean isCache;


	public BaseParser(String json) {
		parser(json);
	}

	@Override
	public void parser(String data) {
		if (TextUtils.isEmpty(data)) {
			setCode(ERROR);
			setTips("数据加载失败");
//			//统一验证为空的问题
//			new Handler(Looper.getMainLooper()).post(new Runnable() {
//				@Override
//				public void run() {
//					new ToastView("返回数据为空，请重试").showCenter();
//				}
//			});
			return ;
		}
		try {
			JSONObject mResObj = new JSONObject(data);
			setCode(String.valueOf(mResObj.optInt("code")));
			setTips(mResObj.optString("tips"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public String getCode() {
		return mCode;
	}

	public void setCode(String code) {
		this.mCode = code;
	}

	public String getTips() {
		return mTips;
	}

	public void setTips(String tips) {
		this.mTips = tips;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		this.mMessage = message;
	}

	public JSONObject getJson() {
		return mResObj;
	}

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean cache) {
		isCache = cache;
	}
}

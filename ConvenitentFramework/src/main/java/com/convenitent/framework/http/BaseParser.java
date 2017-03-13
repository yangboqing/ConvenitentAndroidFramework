package com.convenitent.framework.http;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;

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
	private Gson mGson;

	public BaseParser(String json) {
		mGson = new Gson();
		parser(json);
	}

	public BaseParser() {
		mGson = new Gson();
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
		//Android 4.0及以上都已经在内部类中处理,Android 2.2至Android 2.3.3未作处理
		if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1){
			if (data != null && data.startsWith("\ufeff")) {
				data = data.substring(1);
			}
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

	public Gson getGson() {
		return mGson;
	}
}

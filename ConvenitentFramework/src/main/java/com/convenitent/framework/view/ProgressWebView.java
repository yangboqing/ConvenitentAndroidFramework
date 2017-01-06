package com.convenitent.framework.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.convenitent.framework.R;
import com.convenitent.framework.utils.DisplayUtils;


/**
 * 带进度条的WebView
 * 
 */
public class ProgressWebView extends WebView {

    protected static ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtils.$dp2px(3), 0, 0));
        progressbar.setProgressDrawable(ContextCompat.getDrawable(context,R.drawable.progress_web_horizontal));
        addView(progressbar);
        setWebChromeClient(new ProgressWebChromeClient());
    }

    
    
    public ProgressBar getProgressbar() {
		return progressbar;
	}

	public static class ProgressWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
package com.convenitent.test.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.convenitent.framework.activity.SupportActivity;
import com.convenitent.framework.dialog.ConfirmDialogFragment;
import com.convenitent.framework.http.volley.VolleyFactory;
import com.convenitent.framework.utils.ViewUtils;
import com.convenitent.test.R;

public class MainActivity extends SupportActivity implements View.OnClickListener{

    private Button dialog1Btn;
    private Button dialog2Btn;
    private Button dialog3Btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFullScreen();
        dialog1Btn = ViewUtils.$(this,R.id.btn_customer);
        dialog2Btn = ViewUtils.$(this,R.id.btn_confirm);
        dialog3Btn = ViewUtils.$(this,R.id.btn_progress);
        dialog1Btn.setOnClickListener(this);
        dialog2Btn.setOnClickListener(this);
        dialog3Btn.setOnClickListener(this);
        VolleyFactory.getInstance(getApplicationContext()).initVolley();
    }


    private void initFullScreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_customer:
                mDialogFactory.showProgressDialog("请稍等",true);
                break;
            case R.id.btn_confirm:
                mDialogFactory.showConfirmDialog("提示", "测试信息", true, new ConfirmDialogFragment.ConfirmDialogListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                break;
            case R.id.btn_progress:
                mDialogFactory.showProgressDialog("请稍等",true);
                break;
        }
    }
}

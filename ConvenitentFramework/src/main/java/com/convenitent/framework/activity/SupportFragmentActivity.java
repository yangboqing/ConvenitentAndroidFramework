package com.convenitent.framework.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.convenitent.framework.R;

/**
 * Created by yangboqing on 2017/2/14.
 */

public class SupportFragmentActivity extends AppCompatActivity {

    public static final String EXTRA_FRAGMENT_TYPE = "key_fragment_type";
    public static final String EXTRA_TITLE = "key_title";
    private String mClassName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_fragment);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mClassName = getIntent().getStringExtra(
                EXTRA_FRAGMENT_TYPE);
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.content_frame, getFragment(mClassName));
        mTransaction.commit();
    }

    private Fragment getFragment(String className) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        Bundle args = getIntent().getExtras();
        Fragment fragment = Fragment.instantiate(this, className, args);
        return fragment;
    }
}

package com.convenitent.test.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.convenitent.framework.activity.SupportActivity;
import com.convenitent.framework.utils.ViewUtils;
import com.convenitent.test.R;

public class LoginActivity extends SupportActivity {

    private TextInputLayout userView;
    private TextInputLayout passwordView;
    private EditText mUserEditView;
    private EditText mPasswordEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userView = ViewUtils.$(this,R.id.view_user_name);
        passwordView = ViewUtils.$(this,R.id.view_user_password);
        mUserEditView = ViewUtils.$(this,R.id.et_user_view);
        mPasswordEditView = ViewUtils.$(this,R.id.et_user_password);
        userView.setHint("userName");
        passwordView.setHint("password");
        userView.setError("errordddd");
        userView.setErrorEnabled(true);
    }
}

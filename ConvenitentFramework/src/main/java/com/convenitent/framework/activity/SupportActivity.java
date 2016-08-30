package com.convenitent.framework.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.convenitent.framework.dialog.BaseDialogFragment;
import com.convenitent.framework.dialog.DialogFactory;

/**
 * Created by yangboqing on 16/8/17.
 */
public class SupportActivity extends AppCompatActivity{

    protected DialogFactory mDialogFactory;




    public BaseDialogFragment.BaseDialogListener getDialogListener(){
        return mDialogFactory.mListenerHolder.getDialogListener();
    }

    /**
     * 清空DialogListenerHolder中的dialog listener
     */
    public void clearDialogListener(){
        mDialogFactory.mListenerHolder.setDialogListener(null);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDialogFactory.mListenerHolder.saveDialogListenerKey(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogFactory = new DialogFactory(getSupportFragmentManager(),savedInstanceState);
        mDialogFactory.restoreDialogListener(this);
    }

}

package com.convenitent.framework.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.convenitent.framework.dialog.BaseDialogFragment;
import com.convenitent.framework.dialog.DialogFactory;

/**
 * Created by yangboqing on 16/8/17.
 */
public class SupportFragment extends Fragment {

    protected DialogFactory mDialogFactory ;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDialogFactory = new DialogFactory(getChildFragmentManager(),savedInstanceState);
        mDialogFactory.restoreDialogListener(this);


    }




}

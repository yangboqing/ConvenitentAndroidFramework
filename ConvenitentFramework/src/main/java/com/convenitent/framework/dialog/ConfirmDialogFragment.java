package com.convenitent.framework.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.convenitent.framework.R;

/**
 * Created by yangboqing on 16/8/22.
 */
public class ConfirmDialogFragment extends BaseDialogFragment {

    private String message;
    private ConfirmDialogListener mListener;


    public interface ConfirmDialogListener extends BaseDialogListener,DialogInterface.OnClickListener{

    }

    /**
     *
     * @param title
     * @param message
     * @param cancelable
     * @return
     */
    public static ConfirmDialogFragment newInstance(String title,String message,boolean cancelable){
        ConfirmDialogFragment instance = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        putTitleParam(args,title);
        putMessageParam(args,message);
        putCancelableParam(args,cancelable);
        instance.setArguments(args);
        return instance;
    }

    @Override
    protected void onReceiveDialogListener(BaseDialogListener listener) {
        if (listener instanceof ConfirmDialogListener){
            mListener = (ConfirmDialogListener) listener;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!mIsCustomDialog){
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(mTitle==null?getString(R.string.app_name):mTitle)
                    .setMessage(message == null?" ":message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mListener.onClick(dialogInterface,i);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mListener.onClick(dialogInterface,i);
                        }
                    }).create();
            return dialog;
        }else {
            return super.onCreateDialog(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        if (mIsCustomDialog){
            View view = inflater.inflate(R.layout.dialog_custom_progress,container,false);
            //启用窗体的扩展特性。
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            return view;
        }else{
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    protected void parseArgs(Bundle args) {
        super.parseArgs(args);
        message = parseMessageParam();
    }
}

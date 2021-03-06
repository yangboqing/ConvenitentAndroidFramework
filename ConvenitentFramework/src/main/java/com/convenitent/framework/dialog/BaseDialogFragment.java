package com.convenitent.framework.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

import com.convenitent.framework.activity.SupportActivity;
import com.convenitent.framework.fragment.SupportFragment;

/**
 * Created by yangboqing on 16/8/17.
 * 自定义dialog，是所有自定义dialog的基类
 */
public class BaseDialogFragment extends AppCompatDialogFragment{

    protected SupportActivity mBaseActivity;

    private static final String EXTRA_DIALOG_TITLE_KEY = "extra_dialog_title_key";
    private static final String EXTRA_DIALOG_MESSAGE_KEY = "extra_dialog_message_key";
    private static final String EXTRA_DIALOG_CANELABLE_KEY = "extra_dialog_cancelable";
    private static final String EXTRA_DIALOG_IS_CUSTOM_KEY = "extra_dialog_custom_layout_id";
    private static final String EXTRA_DIALOG_ID_KEY = "extra_dialog_id";

    //是否是自定义dialog
    protected int mCustomLayoutId = -1;
    //每个对话框的ID
    protected int mDialogId;
    protected boolean mIsCancelable;
    protected String mTitle;

    protected boolean mIsParseDialogListener;
    /**
     * 基础的dialog listener，没有提供任何的方法，扩展的dialog，若该dialog有listener则必须继承本接口
     */
    public interface BaseDialogListener{}

    /** * 接收dialog listener对象，具体由子类进行实现 * @param listener */
    protected void onReceiveDialogListener(BaseDialogListener listener){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof SupportActivity) {
            mBaseActivity = (SupportActivity) getActivity();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgs(getArguments());
        setCancelable(mIsCancelable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         /*解析BaseDialogListener，fragment的级别要大于activity，若
              *  (getParentFragment() instanceof BaseFragment)为true* ，
               * 表明是一个fragment调起的dialog，否则是一个activity调起的diaolog
              * */
        BaseDialogListener listener = null;
        if (getParentFragment() instanceof SupportFragment) {
            listener = ((SupportFragment) getParentFragment()).getDialogListener();
        }else if(getActivity() instanceof SupportActivity){
            listener = ((SupportActivity)getActivity()).getDialogListener();
        }
        if(listener != null){
            onReceiveDialogListener(listener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseDialogListener listener = null;

        if(!mIsParseDialogListener){
            mIsParseDialogListener = true;


            /*/解析dialog listener，fragment的级别要大于activity，若 (getParentFragment() instanceof BaseFragment)为true
            * ，表明是一个fragment调起的dialog，否则是一个activity调起的diaolog
            * */
            if (getParentFragment() instanceof SupportFragment) {
                listener = ((SupportFragment) getParentFragment()).getDialogListener();
            }else if(mBaseActivity != null){
                listener = mBaseActivity.getDialogListener();
            }
            if (listener != null) {
                onReceiveDialogListener(listener);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        /*销毁basefragment或BaseActivity中的dialog listener，
           *  同理BaseFragment级别要高于BaseActivity
            * */
        if (getParentFragment() instanceof SupportFragment) {
            ((SupportFragment) getParentFragment()).clearDialogListener();
        }else if(mBaseActivity != null){
            mBaseActivity.clearDialogListener();
        }
    }

    /**
     * 从bundle中解析参数，args有可能来自fragment被系统回收，然后界面又重新被启动系统保存的参数；也有可能是其他使用者带过来的
     * ，具体实现交给子类
     *
     * @param args
     */
    protected void parseArgs(Bundle args) {
        mDialogId = args.getInt(EXTRA_DIALOG_ID_KEY);
        mTitle = args.getString(EXTRA_DIALOG_TITLE_KEY);
        mIsCancelable = args.getBoolean(EXTRA_DIALOG_CANELABLE_KEY);
        mCustomLayoutId = args.getInt(EXTRA_DIALOG_IS_CUSTOM_KEY);
    }

    protected static void putIdParam(Bundle args, int dialogId) {
        if (args != null) {
            args.putInt(EXTRA_DIALOG_ID_KEY, dialogId);
        }
    }

    @NonNull
    protected static void putCustomLayoutParam(Bundle args, int layoutId) {
        args.putInt(EXTRA_DIALOG_IS_CUSTOM_KEY, layoutId);
    }

    @NonNull
    protected static void putTitleParam(Bundle bundler, String title) {
        bundler.putString(EXTRA_DIALOG_TITLE_KEY, title);
    }

    @NonNull
    protected static void putCancelableParam(Bundle bundle, boolean cancelable) {
        bundle.putBoolean(EXTRA_DIALOG_CANELABLE_KEY, cancelable);
    }

    @NonNull
    protected static void putMessageParam(Bundle bundler, String message) {
        bundler.putString(EXTRA_DIALOG_MESSAGE_KEY, message);
    }

    protected String parseMessageParam() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return null;
        }
        return bundle.getString(EXTRA_DIALOG_MESSAGE_KEY);
    }

}

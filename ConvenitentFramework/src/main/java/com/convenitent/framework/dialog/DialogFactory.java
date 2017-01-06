package com.convenitent.framework.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by yangboqing on 16/8/17.
 * 创建dialogfragment工厂类
 */
public class DialogFactory {

    /**
     * 进度条的tag
     */
    private static final String DIALOG_PROGRESS_TAG = "progress";

    private static final String DIALOG_CONFIRM_TAG = "confirm";

    private FragmentManager mFragmentManager;

    public DialogListenerHolder mListenerHolder = new DialogListenerHolder();


    public DialogFactory(FragmentManager fragmentManager, Bundle savedInstanceState){
        this.mFragmentManager = fragmentManager;
        mListenerHolder.getDialogListenerKey(savedInstanceState);
    }


    /**
     * 这个方法很重要，是恢复dialog listener的一个关键点，在初始化DialogFactory或把DialogFactory赋值后，调用该方法，把调用该方法所在
     * 的类的实例作为参数。 该方法会把param中的属性依次遍历，尝试找属性是BaseDialogFragment.BaseDialogListener的实例，
     * 并且该属性就是保存在bundle中的dialog listener key对应的dialog listener
     * @param o
     */
    public void restoreDialogListener(Object o){
        mListenerHolder.restoreDialogListener(o);
    }

    /**
     * @param message 进度条显示的信息
     * @param cancelable 点击空白处是否可以取消
     */
    public void showProgressDialog(String message, boolean cancelable){
        if(mFragmentManager != null){

            /**
             * 为了不重复显示dialog，在显示对话框之前移除正在显示的对话框。
             */
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_PROGRESS_TAG);
            if (null != fragment) {
                ft.remove(fragment).commit();
            }

            ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(message, cancelable,-1);
            progressDialogFragment.show(mFragmentManager, DIALOG_PROGRESS_TAG);

            mFragmentManager.executePendingTransactions();
        }
    }
    /**
     * @param message 进度条显示的信息
     * @param cancelable 点击空白处是否可以取消
     * @param layoutId 自定义布局id
     */
    public void showCustomProgressDialog(String message, boolean cancelable,int layoutId){
        if(mFragmentManager != null){

            /**
             * 为了不重复显示dialog，在显示对话框之前移除正在显示的对话框。
             */
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_PROGRESS_TAG);
            if (null != fragment) {
                ft.remove(fragment).commit();
            }

            ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(message, cancelable,layoutId);
            progressDialogFragment.show(mFragmentManager, DIALOG_PROGRESS_TAG);

            mFragmentManager.executePendingTransactions();
        }
    }

    /**
     * 取消进度条
     */
    public void dissProgressDialog(){
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_PROGRESS_TAG);
        if (null != fragment) {
            ((ProgressDialogFragment)fragment).dismiss();
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    /**
     *     //显示确认对话框，dialogId是用来区分不同对话框的
     * @param title 对话框title
     * @param message
     * @param cancelable
     * @param listener
     */
    public void showConfirmDialog(String title,String message,boolean cancelable,boolean isCustomDialog, ConfirmDialogFragment.ConfirmDialogListener listener){

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CONFIRM_TAG);
        if (null != fragment) {
            ft.remove(fragment);
        }
        mListenerHolder.setDialogListener(listener);
        DialogFragment df = ConfirmDialogFragment.newInstance(title, message, cancelable,-1);
        df.show(mFragmentManager,DIALOG_CONFIRM_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     *     //显示自定义确认对话框，dialogId是用来区分不同对话框的
     * @param title 对话框title
     * @param message
     * @param cancelable
     * @param listener
     */
    public void showCustomConfirmDialog(String title,String message,boolean cancelable,int layoutId, ConfirmDialogFragment.ConfirmDialogListener listener){

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CONFIRM_TAG);
        if (null != fragment) {
            ft.remove(fragment);
        }
        mListenerHolder.setDialogListener(listener);
        DialogFragment df = ConfirmDialogFragment.newInstance(title, message, cancelable,layoutId);
        df.show(mFragmentManager,DIALOG_CONFIRM_TAG);
        mFragmentManager.executePendingTransactions();
    }

}

package com.convenitent.framework.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yangboqing on 16/8/24.
 */
public abstract class SupportAdapter<T> extends BaseQuickAdapter<T,BaseAdapterHelper> {

    public SupportAdapter(Context context,int layoutResId){
        super(context,layoutResId);
    }

    public SupportAdapter(Context context, int layoutResId, List<T> data){
        super(context,layoutResId,data);
    }

    @Override
    protected BaseAdapterHelper getAdapterHelper(int position, View convertView, ViewGroup parent) {
        return BaseAdapterHelper.get(mContext,convertView,parent,layoutResId,position);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, T item) {
        boolean itemChanged = helper.associatedObject == null || !helper.associatedObject.equals(item);
        helper.associatedObject = item;
        convert(helper, item, itemChanged);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     * @param itemChange Whether or not the helper was bound to another object before.
     */
    protected abstract void convert(BaseAdapterHelper helper,T item,boolean itemChange);
}

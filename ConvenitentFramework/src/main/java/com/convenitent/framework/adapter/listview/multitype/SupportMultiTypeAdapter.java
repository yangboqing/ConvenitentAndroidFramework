package com.convenitent.framework.adapter.listview.multitype;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.convenitent.framework.adapter.listview.BaseAdapterHelper;
import com.convenitent.framework.adapter.listview.multitype.bean.ObjectItem;

import java.util.List;

/**
 * Created by yangboqing on 16/8/24.
 */
public abstract class SupportMultiTypeAdapter extends BaseQuickMultiTypeAdapter<BaseAdapterHelper> {

    public SupportMultiTypeAdapter(Context context, int[] layoutResIds){
        super(context,layoutResIds);
    }

    public SupportMultiTypeAdapter(Context context, int[] layoutResIds, List<ObjectItem> data){
        super(context,layoutResIds,data);
    }

    @Override
    protected BaseAdapterHelper[] getAdapterHelper(int position, View convertView, ViewGroup parent) {
        BaseAdapterHelper[] helpers = new BaseAdapterHelper[layoutResIds.length];
        for (int i = 0;i<layoutResIds.length;i++){
            helpers[i] = BaseAdapterHelper.get(mContext,convertView,parent,layoutResIds[i],position);
        }
        return helpers;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ObjectItem item) {
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
    protected abstract void convert(BaseAdapterHelper helper,ObjectItem item,boolean itemChange);
}

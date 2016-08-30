package com.convenitent.framework.adapter.listview.multitype;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.convenitent.framework.adapter.listview.BaseAdapterHelper;
import com.convenitent.framework.adapter.listview.multitype.bean.ObjectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction class of a BaseAdapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 * Created by yangboqing on 16/8/24.
 */
public abstract class BaseQuickMultiTypeAdapter<H extends BaseAdapterHelper> extends BaseAdapter {

    protected static final String TAG = BaseQuickMultiTypeAdapter.class.getSimpleName();

    protected final Context mContext;

    protected final int[] layoutResIds;

    protected final List<ObjectItem> data;

    /**
     * Create a QuickAdapter.
     * @param context     The context.
     * @param layoutResIds The layout resource ids of each item.
     */
    public BaseQuickMultiTypeAdapter(Context context, int[] layoutResIds){
        this(context,layoutResIds,null);
    }

    public BaseQuickMultiTypeAdapter(Context context, int[] layoutResIds, List<ObjectItem> data){
        this.data = data == null?new ArrayList<ObjectItem>():new ArrayList<ObjectItem>(data);
        this.mContext = context;
        this.layoutResIds = layoutResIds;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ObjectItem getItem(int position) {
        if (position >=data.size()) return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return this.layoutResIds.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int type = getLayoutId(position);
        if (type == -1){
            throw new IllegalStateException("multi type might not be empty data -1 "+ this.getClass());
        }
        final H[] helper = getAdapterHelper(position,convertView,parent);
        ObjectItem item = getItem(position);
        for (int i = 0; i < this.layoutResIds.length; i++) {
            if (type == this.layoutResIds[i]){
                helper[i].setAssociatedObject(item);
                return helper[i].getView();
            }
        }
        return convertView;
    }

    private int getLayoutId(int position){
        return this.data.get(position).getLayoutId();
    }

    public void add(ObjectItem elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<ObjectItem> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(ObjectItem oldElem, ObjectItem newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, ObjectItem elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(ObjectItem elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<ObjectItem> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(ObjectItem elem) {
        return data.contains(elem);
    }

    /** Clear data list */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H helper,ObjectItem item);


    /**
     * You can override this method to use a custom BaseAdapterHelper in order to fit your needs
     * @param position    The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return An instance of BaseAdapterHelper
     */
    protected abstract H[] getAdapterHelper(int position, View convertView, ViewGroup parent);
}

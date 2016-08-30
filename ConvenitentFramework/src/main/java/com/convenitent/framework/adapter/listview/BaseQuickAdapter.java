package com.convenitent.framework.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction class of a BaseAdapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 * @param <T> The type of the items in the list.
 * Created by yangboqing on 16/8/24.
 */
public abstract class BaseQuickAdapter<T,H extends BaseAdapterHelper> extends BaseAdapter {

    protected static final String TAG = BaseQuickAdapter.class.getSimpleName();

    protected final Context mContext;

    protected final int layoutResId;

    protected final List<T> data;

    /**
     * Create a QuickAdapter.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public BaseQuickAdapter(Context context,int layoutResId){
        this(context,layoutResId,null);
    }

    public BaseQuickAdapter(Context context,int layoutResId,List<T> data){
        this.data = data == null?new ArrayList<T>():new ArrayList<T>(data);
        this.mContext = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final H helper = getAdapterHelper(position,convertView,parent);
        T item = getItem(position);
        convert(helper,item);
        helper.setAssociatedObject(item);
        return helper.getView();
    }

    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
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
    protected abstract void convert(H helper,T item);


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
    protected abstract H getAdapterHelper(int position, View convertView, ViewGroup parent);
}

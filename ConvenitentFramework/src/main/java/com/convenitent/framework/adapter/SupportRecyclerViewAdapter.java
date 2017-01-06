package com.convenitent.framework.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yangboqing on 2016/12/23.
 */

public abstract class SupportRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{


    public SimpleOnItemClickListener mOnItemClickListener;

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(@Nullable SimpleOnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * @return The callback to be invoked with an item in this AdapterView has
     *         been clicked, or null id no callback has been set.
     */
    @Nullable
    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }


    public abstract static class SimpleOnItemClickListener implements OnItemClickListener{

        @Override
        public abstract void onItemClick(View view, int position);

        @Override
        public void onItemLongCLick(View view, int position) {

        }
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * AdapterView has been clicked.
     */
    private interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongCLick(View view, int position);
    }



}

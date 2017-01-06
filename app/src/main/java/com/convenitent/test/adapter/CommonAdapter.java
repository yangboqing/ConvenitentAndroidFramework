package com.convenitent.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yangboqing on 2016/12/28.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<T> list;
    private int mLayoutId;

    public CommonAdapter(Context context, List<T> list,int layout) {
        this.mContext = context;
        this.list = list;
        this.mLayoutId = layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        convert(holder, list.get(position));
    }

    protected abstract void convert(RecyclerView.ViewHolder holder, T bean);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setText(int id,String title){
            TextView text = (TextView) view.findViewById(id);
            text.setText(title);
        }
    }
}

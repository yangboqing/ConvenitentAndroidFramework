package com.convenitent.test.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.convenitent.framework.adapter.SupportRecyclerViewAdapter;
import com.convenitent.framework.utils.ViewUtils;
import com.convenitent.test.R;

import java.util.List;

/**
 * Created by yangboqing on 2016/12/21.
 */

public class ListAdapter extends SupportRecyclerViewAdapter<ListAdapter.ViewHolder>{


    private List<String> list;

    public ListAdapter(){
    }


    public void setData(List<String> list){
        this.list = list;
    }

    public void addData(List<String> list){
        this.list.addAll(list);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(this.list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list!=null?this.list.size():0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = ViewUtils.$(itemView,R.id.text_view);
        }
    }
}

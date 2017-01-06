package com.convenitent.test.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.convenitent.framework.adapter.SupportRecyclerViewAdapter;
import com.convenitent.framework.fragment.SupportListFragment;
import com.convenitent.framework.utils.ToastUtils;
import com.convenitent.framework.view.DividerItemDecoration;
import com.convenitent.test.R;
import com.convenitent.test.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class RecyclerActivityFragment extends SupportListFragment {

    private ListAdapter mAdapter;
    private List<String> list;
    private static final List<String> tempList = Arrays.asList("Java", "php", "C++", "C#", "IOS", "html", "C", "J2ee", "j2se", "VB", ".net", "Http", "tcp", "udp", "www");



    @Override
    public boolean getHeaderEnable() {
        return true;
    }

    @Override
    public View getHeaderView(ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(R.layout.header_view,parent,false);
    }

    @Override
    public void requestData() {
        list = new ArrayList<>();
        list.addAll(tempList);
        mAdapter.setData(list);
    }

    @Override
    public void onRefreshData() {
        super.onRefreshData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    list.add(0,"jjjjjjjj");
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean getLoadMoreEnable() {
        return true;
    }

    @Override
    public void onLoadMoreData(int page) {
        super.onLoadMoreData(page);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    list.addAll(tempList);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new ListAdapter();
        mAdapter.setOnItemClickListener(new SupportRecyclerViewAdapter.SimpleOnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                ToastUtils.$(getContext(),"当前点击的位置："+position);
                Intent intent = new Intent(getActivity(),OverLayCardActivity.class);
                startActivity(intent);
            }
        });
        return mAdapter;
    }

    @Override
    public RecyclerView.ItemDecoration getDriverLine() {
        return new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    notifyDataSetChange();
                    break;
                case 1:
                    notifyLoadMoreFinish(false);
                    break;
            }
        }
    };
}

package com.convenitent.framework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.convenitent.framework.R;
import com.convenitent.framework.fragment.inter.SupportListCallBack;
import com.convenitent.framework.utils.ViewUtils;
import com.convenitent.framework.view.LoadMoreRecyclerView;


/**
 * Created by yangboqing on 2016/12/22.
 */

public abstract class SupportListFragment extends SupportFragment implements SupportListCallBack{

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    protected LoadMoreRecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;
    private FrameLayout mGroupLayout;
    private View mEmptyView;
    public int page = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        switch (getOrientation()){
            case VERTICAL:
                view = inflater.inflate(R.layout.layout_vertical_list_view,container,false);
                break;
            case HORIZONTAL:
                view = inflater.inflate(R.layout.layout_horizontal_list_view,container,false);
                break;
        }
        mRecyclerView = ViewUtils.$(view,R.id.list_view);
        mRefreshLayout = ViewUtils.$(view,R.id.swipe_refresh_layout);
        mGroupLayout = ViewUtils.$(view,R.id.layout_list_view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideEmptyView();
        requestData();
    }


    private void initView(){
        //设置空页面显示样式
        mEmptyView = getEmptyView();
        mGroupLayout.addView(mEmptyView);
        if (getRefreshIconColor() != null){
            mRefreshLayout.setColorSchemeColors(getRefreshIconColor());
        }
        mRecyclerView.setLayoutManager(getLayoutManager(mRecyclerView));
        if (getDriverLine() != null){
            mRecyclerView.addItemDecoration(getDriverLine());
        }
        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.setHeaderEnable(getHeaderEnable());
        if (getHeaderEnable()){
            if (getHeaderView(mRecyclerView) == null){
                throw new NullPointerException("please implements @method getHeaderView() !");
            }
            mRecyclerView.setHeaderView(getHeaderView(mRecyclerView));
        }
        mRecyclerView.setAutoLoadMoreEnable(getLoadMoreEnable());
        if (getLoadMoreEnable()){
            mRecyclerView.setFooterLoadMoreView(getFooterView(mRecyclerView));
        }
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMoreData(page);
            }
        });

    }

    /**
     * 判断结束加载更多数据
     * @param isFinish false为还有更多数据，反之为true
     */
    public final void notifyLoadMoreFinish(boolean isFinish){
        mRecyclerView.notifyMoreFinish(isFinish);
    }

    /**
     * 刷新数据页面展示
     */
    public final void notifyDataSetChange(){
        autoRefresh(false);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * 自动刷新
     * @param isRefresh
     */
    public final void autoRefresh(boolean isRefresh){
        mRefreshLayout.setRefreshing(isRefresh);
    }


    @Override
    public abstract void requestData();

    @Override
    public void onRefreshData(){
        page = 1;
    }

    @Override
    public void onLoadMoreData(int page) {
        if (!getLoadMoreEnable()){
            throw new IllegalArgumentException("please Override @method getLoadMoreEnable and return true!");
        }
    }

    @Override
    public boolean getHeaderEnable() {
        return false;
    }

    @Override
    public View getHeaderView(ViewGroup parent){
        return null;
    }

    @Override
    public boolean getLoadMoreEnable(){
        return false;
    }

    @Override
    public View getFooterView(ViewGroup parent){
        return LayoutInflater.from(getContext()).inflate(R.layout.footer_view,parent,false);
    }

    @Override
    public abstract RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter();


    public int[] getRefreshIconColor(){
        return null;
    }

    @Override
    public RecyclerView.ItemDecoration getDriverLine() {
        return null;
    }

    @Override
    public View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_empty,mGroupLayout,false);
    }

    /**
     * 显示空页面提示
     */
    public final void showEmptyView(){
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏空页面提示
     */
    public final void hideEmptyView(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(LoadMoreRecyclerView recyclerView) {
        return new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    }

    /**
     * 设置recyclerView的展示方向
     * @return
     */
    public int getOrientation(){
        return VERTICAL;
    }
}

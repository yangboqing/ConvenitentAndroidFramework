package com.convenitent.framework.fragment.inter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.convenitent.framework.view.LoadMoreRecyclerView;


/**
 * Created by yangboqing on 2016/12/22.
 */

public interface SupportListCallBack {

    /**
     * 获取数据
     */
    void requestData();

    /**
     * 下来刷新
     */
    void onRefreshData();

    /**
     * 上拉加载更多
     * @param page
     */
    void onLoadMoreData(int page);

    /**
     * 获取是否可以加载头部view
     * @return
     */
    boolean getHeaderEnable();

    /**
     * 加载头部view
     * @return
     */
    View getHeaderView(ViewGroup parent);

    /**
     * 获取是否显示加载更多样式
     * @return
     */
    boolean getLoadMoreEnable();

    /**
     * 获取加载更多View
     * @return
     */
    View getFooterView(ViewGroup parent);

    /**
     * 设置适配器
     */
    RecyclerView.Adapter getAdapter();

    /**
     * 当数据没有时需要显示的view
     * @return
     */
    View getEmptyView();

    /**
     * 添加分割线
     */
    RecyclerView.ItemDecoration getDriverLine();


    /**
     * 获取recyclerview展示的方向
     */
    RecyclerView.LayoutManager getLayoutManager(LoadMoreRecyclerView recyclerView);




}

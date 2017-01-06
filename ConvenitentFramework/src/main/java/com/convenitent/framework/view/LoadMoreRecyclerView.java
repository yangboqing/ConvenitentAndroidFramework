package com.convenitent.framework.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yangboqing on 2016/12/20.
 */

public class LoadMoreRecyclerView extends RecyclerView {

    public static final int TYPE_NORMAL = 0;
    public final static int TYPE_HEADER = 1;//头部--支持头部增加一个headerView
    public final static int TYPE_FOOTER = 2;//底部--往往是loading_more
    public final static int TYPE_LIST = 3;//代表item展示的模式是list模式
    public final static int TYPE_STAGGER = 4;//代码item展示模式是网格模式

    private boolean mIsFooterEnable = false;//是否允许加载更多

    /**
     * 自定义实现了头部和底部加载更多的adapter
     */
    private AutoLoadAdapter mAutoLoadAdapter;

    /**
     * 标记是否正在加载更多，防止再次调用加载更多接口
     */
    private boolean mIsLoadingMore;

    /**
     * 标记加载更多的position
     */
    private int mLoadMorePosition;


    /**
     * 加载更多的监听-业务需要实现加载数据
     */
    private LoadMoreListener mListener;



    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化-添加滚动监听
     * <p/>
     * 回调加载更多方法，前提是
     * <pre>
     *    1、有监听并且支持加载更多：null != mListener && mIsFooterEnable
     *    2、目前没有在加载，正在上拉（dy>0），当前最后一条可见的view是否是当前数据列表的最好一条--及加载更多
     * </pre>
     */
    private void init(){
        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null!= mListener && mIsFooterEnable && !mIsLoadingMore && dy > 0){
                    int lastVisiblePosition = getLastVisiblePositon();
                    if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()){
                        setLoadingMore(true);
                        mLoadMorePosition = lastVisiblePosition;
                        mListener.onLoadMore();
                    }
                }
            }
        });
    }

    /**
     * 切换layoutManager
     *
     * 为了保证切换之后页面上还是停留在当前展示的位置，记录下切换之前的第一条展示位置，切换完成之后滚动到该位置
     * 另外切换之后必须要重新刷新下当前已经缓存的itemView，否则会出现布局错乱（俩种模式下的item布局不同），
     * RecyclerView提供了swapAdapter来进行切换adapter并清理老的itemView cache
     *
     * @param layoutManager
     */
    public void switchLayoutManager(LayoutManager layoutManager){
        int firstVisiblePosition = getFirstVisiblePosition();
        setLayoutManager(layoutManager);
        getLayoutManager().scrollToPosition(firstVisiblePosition);
    }



    public int getFirstVisiblePosition(){
        int position = 0;
        if (getLayoutManager() instanceof LinearLayoutManager){
            position = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
        }else if(getLayoutManager() instanceof GridLayoutManager){
            position = ((GridLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
        }else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获取最后一条展示的位置
     * @return
     */
    private int getLastVisiblePositon(){
        int position = 0;
        if (getLayoutManager() instanceof LinearLayoutManager){
            position = ((LinearLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
        }else if(getLayoutManager() instanceof GridLayoutManager){
            position = ((GridLayoutManager)getLayoutManager()).findLastVisibleItemPosition();
        }else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }


    public class AutoLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        /**
         * 数据adapter
         */
        private RecyclerView.Adapter mInternalAdapter;

        private boolean mIsHeaderEnable;

        private View mHeaderView;
        private View mFooterView;


        public AutoLoadAdapter(RecyclerView.Adapter adapter) {
            mInternalAdapter = adapter;
            mIsHeaderEnable = false;
        }

        @Override
        public int getItemViewType(int position) {
            int headerPosition = 0;
            int footerPosition = getItemCount() - 1;
            if (headerPosition == position && mIsHeaderEnable && mHeaderView != null){
                return TYPE_HEADER;
            }
            if (footerPosition == position && mIsFooterEnable && mFooterView != null){
                return TYPE_FOOTER;
            }
            /**
             * 这么做保证layoutManager切换之后能及时的刷新上对的布局
             */
            if (getLayoutManager() instanceof LinearLayoutManager) {
                return TYPE_LIST;
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                return TYPE_STAGGER;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER){
                return new HeaderViewHolder(mHeaderView);
            }
            if (viewType == TYPE_FOOTER){
                return new FooterViewHolder(mFooterView);
            }
            return mInternalAdapter.onCreateViewHolder(parent,viewType);
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder{

            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class FooterViewHolder extends RecyclerView.ViewHolder{

            public FooterViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type != TYPE_FOOTER && type != TYPE_HEADER){
                if (mIsHeaderEnable){
                    mInternalAdapter.onBindViewHolder(holder,position-1);
                }else{
                    mInternalAdapter.onBindViewHolder(holder,position);
                }


            }
        }

        @Override
        public int getItemCount() {
            int count = mInternalAdapter.getItemCount();
            if (mIsHeaderEnable) count++;
            if (mIsFooterEnable) count++;
            return count;
        }

        public void setHeaderEnable(boolean enable){
            mIsHeaderEnable = enable;
        }
        public void setHeaderView(View headerView){
            mHeaderView = headerView;
        }
        public void setFooterView(View footView){
            mFooterView = footView;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null){
            mAutoLoadAdapter = new AutoLoadAdapter(adapter);
        }
        super.swapAdapter(mAutoLoadAdapter,true);
    }

    /**
     * 设置头部是否可以显示view
     * @param enable
     */
    public void setHeaderEnable(boolean enable) {
        if (mAutoLoadAdapter == null){
            throw new NullPointerException("this method can not be called before setAdapter()");
        }
        mAutoLoadAdapter.setHeaderEnable(enable);
    }

    /**
     * 设置头部显示的view
     * @param view
     */
    public void setHeaderView(View view){
        if (mAutoLoadAdapter == null){
            throw new NullPointerException("this method can not be called before setAdapter()");
        }
        mAutoLoadAdapter.setHeaderView(view);
    }
    /**
     * 设置是否支持自动加载更多
     *
     * @param autoLoadMore
     */
    public void setAutoLoadMoreEnable(boolean autoLoadMore){
        this.mIsFooterEnable = autoLoadMore;
    }

    /**
     * 设置加载更多显示的view
     * @param view
     */
    public void setFooterLoadMoreView(View view){
        if (mAutoLoadAdapter == null){
            throw new NullPointerException("this method can not be called before setAdapter()");
        }
        this.mAutoLoadAdapter.setFooterView(view);
    }

    /**
     * 通知更多的数据已经加载
     *
     * 每次加载完成之后添加了Data数据，用notifyItemRemoved来刷新列表展示，
     * 而不是用notifyDataSetChanged来刷新列表
     *
     */
    public void notifyMoreFinish(boolean isFinish){
        setAutoLoadMoreEnable(!isFinish);
        getAdapter().notifyItemRemoved(mLoadMorePosition);
        mIsLoadingMore = isFinish;
    }

    /**
     * 设置加载更多的监听
     *
     * @param listener
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 设置正在加载更多
     *
     * @param loadingMore
     */
    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;
    }

    /**
     * 加载更多监听
     */
    public interface LoadMoreListener {
        /**
         * 加载更多
         */
        void onLoadMore();
    }
}



package com.convenitent.test.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.convenitent.test.R;
import com.convenitent.test.adapter.CommonAdapter;
import com.convenitent.test.bean.SwipeCardBean;
import com.convenitent.test.view.CardConfig;
import com.convenitent.test.view.OverLayCardLayoutManager;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class OverLayCardActivityFragment extends Fragment {

    RecyclerView mRv;
    CommonAdapter<SwipeCardBean> mAdapter;
    List<SwipeCardBean> mDatas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_over_lay_card, container, false);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mRv.setLayoutManager(new OverLayCardLayoutManager());
        mRv.setAdapter(mAdapter = new CommonAdapter<SwipeCardBean>(getActivity(), mDatas = SwipeCardBean.initDatas(), R.layout.item_swipe_card) {
            public static final String TAG = "zxt/Adapter";

            @Override
            public void convert(RecyclerView.ViewHolder viewHolder, SwipeCardBean swipeCardBean) {
                Log.d(TAG, "convert() called with: viewHolder = [" + viewHolder + "], swipeCardBean = [" + swipeCardBean + "]");
                ((CommonAdapter.ViewHolder)viewHolder).setText(R.id.tvName, swipeCardBean.getName());
                ((CommonAdapter.ViewHolder)viewHolder).setText(R.id.tvPrecent, swipeCardBean.getPostition() + " /" + mDatas.size());
//                Picasso.with(SwipeCardActivity.this).load(swipeCardBean.getUrl()).into((ImageView) viewHolder.getView(R.id.iv));
            }
        });

        //初始化配置
        CardConfig.initConfig(getActivity());
        ItemTouchHelper.Callback callback = new RenRenCallBack(mRv, mAdapter, mDatas);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRv);
        return view;
    }
}

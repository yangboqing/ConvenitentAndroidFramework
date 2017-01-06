package com.convenitent.test.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yangboqing on 2016/12/28.
 */

public class OverLayCardLayoutManager extends RecyclerView.LayoutManager {


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount<1){
            return;
        }
        int bottomPosition;
        if (itemCount < CardConfig.MAX_SHOW_COUNT){
            bottomPosition = 0;
        }else{
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }

        for (int position = bottomPosition;position<itemCount;position++){
            View view = recycler.getViewForPosition(position);
            addView(view);
            measureChildWithMargins(view,0,0);
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            layoutDecoratedWithMargins(view,widthSpace/2,heightSpace/2,widthSpace/2+getDecoratedMeasuredWidth(view),
                    heightSpace/2+getDecoratedMeasuredHeight(view));
            /**
             * TopView的Scale 为1，translationY 0
             * 每一级Scale相差0.05f，translationY相差7dp左右
             *
             * 观察人人影视的UI，拖动时，topView被拖动，Scale不变，一直为1.
             * top-1View 的Scale慢慢变化至1，translation也慢慢恢复0
             * top-2View的Scale慢慢变化至 top-1View的Scale，translation 也慢慢变化只top-1View的translation
             * top-3View的Scale要变化，translation岿然不动
             */
             int level = itemCount - position -1;
             if (level>0){
                 view.setScaleX(1 - CardConfig.SCALE_GAP*level);
                 if (level <CardConfig.MAX_SHOW_COUNT -1){
                     view.setTranslationY(CardConfig.TRANS_Y_GAP*level);
                     view.setScaleY(1 - CardConfig.SCALE_GAP*level);
                 }else{
                     view.setTranslationY(CardConfig.TRANS_Y_GAP*(level-1));
                     view.setScaleY(1-CardConfig.SCALE_GAP*(level-1));
                 }
             }

        }
    }
}

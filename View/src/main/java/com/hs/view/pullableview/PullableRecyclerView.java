package com.hs.view.pullableview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * @Author: ljf
 * 时间:2019/4/30
 * 简述:<功能简述>
 */
public class PullableRecyclerView extends RecyclerView implements Pullable {
    private int lastPosition;

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 可以下拉
     *
     * @return 是否可以下拉(默认false)
     */
    @Override
    public boolean canPullDown() {
        if (getChildCount() == 0) {
            return true;
        } else if (getChildAt(0).getTop() >= 0) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int firstVisibleItem = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                return firstVisibleItem == 0;
            } else if (getLayoutManager() instanceof GridLayoutManager) {
                int firstVisibleItem = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                return firstVisibleItem == 0;
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                int firstVisibleItem = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                return firstVisibleItem == 0;
            }
        }
        return false;
    }

    /**
     * 可以上拉
     *
     * @return 是否可以上拉(默认false)
     */
    @Override
    public boolean canPullUp() {
        if (getChildCount() == 0) {
            return true;
        } else {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                //通过LayoutManager找到当前显示的最后的item的position
                lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof LinearLayoutManager) {
                lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                lastPosition = findMax(lastPositions);
            }

            //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
            //如果相等则说明已经滑动到最后了
            return lastPosition == getLayoutManager().getItemCount() - 1;
        }
    }
    
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}

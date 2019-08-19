package com.hs.adapter.statistician;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.adapter.CommonRecyclerAdapter;
import com.hs.R;
import com.hs.activity.BaseActivity;
import com.hs.bean.statistician.StatisticsDataBean;
import com.hs.view.listview.HorizontalListView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StatisticianListParentAdapter extends CommonRecyclerAdapter<StatisticsDataBean> {

    protected BaseActivity targetContext = null;
    private List<RecyclerView> recyclerViews;
    private int reallySize;

    public void setReallySize(int reallySize) {
        this.reallySize = reallySize;
    }

    public StatisticianListParentAdapter(@NonNull BaseActivity context, List<StatisticsDataBean> list) {
        super(context, R.layout.item_statistictian_aprent, list);
        targetContext = context;
        recyclerViews = new ArrayList<>();
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, StatisticsDataBean item, int position) {
        ViewHolder viewHolder = new ViewHolder(helper.getView(), item, position);
        viewHolder.initView();
    }

    class ViewHolder {
        @BindView(R.id.rv_child)
        RecyclerView rvChild;
        @BindView(R.id.divider_line)
        View dividerLine;
        private int itemPosition = 0;
        private StatisticsDataBean item;
        private View view;

        ViewHolder(View view, StatisticsDataBean item, int position) {
            ButterKnife.bind(this, view);
            itemPosition = position;
            this.item = item;
            this.view = view;
        }

        public void initView() {
            int bgColor = 0;
            int textColor = 0;
            if (itemPosition == 0) {
                bgColor = targetContext.getResources().getColor( R.color.color_ffc760);
                textColor = targetContext.getResources().getColor( R.color.color_242536);
            } else {
                if ((itemPosition & 1) != 1) {
                    bgColor =targetContext.getResources().getColor( R.color.color_242536);
                } else {
                    bgColor = targetContext.getResources().getColor(R.color.color_444553);
                }

                textColor = targetContext.getResources().getColor( R.color.white);
            }
            LinearLayoutManager mManager = new LinearLayoutManager(targetContext.getTargetActivity());
            mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvChild.setLayoutManager(mManager);
            recyclerViews.add(rvChild);
            List<String> listData = new ArrayList<>();
            StatisticianListChildAdapter childAdapter = new StatisticianListChildAdapter(targetContext.getTargetActivity(), listData, rvChild);
            childAdapter.setReallySize(reallySize);
            childAdapter.setCurrentBgColor(bgColor);
            childAdapter.setTextColor(textColor);
            rvChild.setAdapter(childAdapter);
            listData = getListData(item);
            childAdapter.replaceAll(listData);


            if (itemPosition == getItemCount() - 1) {
                dividerLine.setVisibility(View.GONE);
            } else {
                dividerLine.setVisibility(View.VISIBLE);
            }
            rvChild.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    for (RecyclerView view : recyclerViews) {
                        view.onScrolled(dx, dy);
                    }
                }
            });
        }
    }

    private List<String> getListData(StatisticsDataBean item) {
        List<String> dataList =new ArrayList<>();
        dataList.add(item.machineNo);
        dataList.addAll(item.dataList);
        return dataList;

    }
}

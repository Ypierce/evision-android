package com.hs.adapter.statistician;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.hs.R;
import com.hs.activity.BaseActivity;
import com.hs.common.util.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StatisticianListChildAdapter extends CommonRecyclerAdapter<String> {

    protected BaseActivity targetContext = null;
    private int currentBgColor;
    private int textColor;
    private int reallySize;
    private int viewWidth;
    private RecyclerView rootView;

    public void setCurrentBgColor(int currentBgColor) {
        this.currentBgColor = currentBgColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setReallySize(int reallySize) {
        this.reallySize = reallySize;
    }


    public StatisticianListChildAdapter(@NonNull BaseActivity context, List<String> list, RecyclerView recyclerView) {
        super(context, R.layout.item_statistictian_child, list);
        targetContext = context;

    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, String item, int position) {
        ViewHolder viewHolder = new ViewHolder(helper.getView(), item, position);
        viewHolder.initView();
    }

    class ViewHolder {
        @BindView(R.id.ll_name_value)
        RelativeLayout llNameValue;
        @BindView(R.id.tv_name_value)
        TextView tvNameValue;
        @BindView(R.id.divider_line)
        View dividerLine;
        private int itemPosition = 0;
        private String item;
        private View view;

        ViewHolder(View view, String item, int position) {
            ButterKnife.bind(this, view);
            itemPosition = position;
            this.item = item;
            this.view = view;
        }

        public void initView() {
            if (reallySize==0){
                return;
            }
            if (currentBgColor == 0) {
                currentBgColor = targetContext.getResources().getColor(R.color.color_ffc760);
            }
            if (textColor == 0) {
                textColor = targetContext.getResources().getColor( R.color.color_242536);
            }
            llNameValue.setBackgroundColor(currentBgColor);
            viewWidth = Tools.getScreenWidth(targetContext.getTargetActivity()) / reallySize;
            if (item != null) {
                if (item.length() > 2) {
                    viewWidth = 2 * viewWidth;
                }
                ViewGroup.LayoutParams params = llNameValue.getLayoutParams();
                params.width = viewWidth;
                view.setLayoutParams(params);
                tvNameValue.setText(item);
            }
            tvNameValue.setTextColor(textColor);

            if (itemPosition == getItemCount() - 1) {
                dividerLine.setVisibility(View.GONE);
            } else {
                dividerLine.setVisibility(View.VISIBLE);
            }
        }
    }

}

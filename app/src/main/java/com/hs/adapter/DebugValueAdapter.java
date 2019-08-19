package com.hs.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.classic.adapter.CommonRecyclerAdapter;
import com.hs.R;
import com.hs.activity.BaseActivity;
import com.hs.common.util.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DebugValueAdapter extends CommonAdapter<String> {
    public DebugValueAdapter(@NonNull Context context, List<String> data) {
        super(context, R.layout.adapter_debug_value, data);
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, String item, int position) {
        ViewHolder viewHolder = new ViewHolder(helper.getView(), item, position);
        viewHolder.initView();
    }

    class ViewHolder {
        @BindView(R.id.tv_value)
        TextView tvValue;
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
            tvValue.setText(item);
        }
    }

}

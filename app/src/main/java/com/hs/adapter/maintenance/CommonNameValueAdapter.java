package com.hs.adapter.maintenance;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.hs.R;
import com.hs.activity.BaseActivity;
import com.hs.bean.maintenance.CommonIdNameBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommonNameValueAdapter extends CommonRecyclerAdapter<CommonIdNameBean> {
    private CommonIdNameListener idNameListener;

    public void setIdNameListener(CommonIdNameListener idNameListener) {
        this.idNameListener = idNameListener;
    }

    public CommonNameValueAdapter(@NonNull BaseActivity context, List<CommonIdNameBean> list) {
        super(context, R.layout.adapter_common_id_name, list);
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, CommonIdNameBean item, int position) {
        ViewHolder viewHolder = new ViewHolder(helper.getView(), item, position);
        viewHolder.initView();
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ll_name)
        LinearLayout llName;
        private CommonIdNameBean item;

        ViewHolder(View view, CommonIdNameBean item, int position) {
            ButterKnife.bind(this, view);
            this.item = item;
        }

        public void initView() {
            if (item == null) {
                return;
            }
            String name = item.name;
            if (name != null) {
                tvName.setText(name);
            }
            llName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (idNameListener == null) {
                        return;
                    }
                    idNameListener.commonIdName(item);
                }
            });
        }

    }

    public interface CommonIdNameListener {
        void commonIdName(CommonIdNameBean idNameBean);
    }
}

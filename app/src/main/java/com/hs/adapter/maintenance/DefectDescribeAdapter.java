package com.hs.adapter.maintenance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.hs.R;
import com.hs.activity.BaseActivity;
import com.hs.bean.maintenance.DefectInfoListBean;
import com.hs.bean.maintenance.DefectInfoListBean.DefectListBean;
import com.hs.bean.maintenance.DefectInfoListBean.DefectListBean.ExtraParamBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 点击疵点列表,下方显示疵点的简略信息的描述
 */

public class DefectDescribeAdapter extends CommonAdapter<DefectListBean> {


    public DefectDescribeAdapter(@NonNull Context context, int layoutResId, List<DefectListBean> data) {
        super(context, layoutResId, data);
    }

    @Override
    public int getLayoutResId(DefectListBean item, int position) {
        int layoutResId = -1;
        if (item.extraParamList == null||item.extraParamList.size()==0) {
            layoutResId = R.layout.adapter_defect_des_un_edit;
        } else {
            layoutResId = R.layout.adappter_defect_des_edit_able;
        }
        return layoutResId;
    }


    @Override
    public void onUpdate(BaseAdapterHelper helper, DefectListBean item, int position) {
        if (item.extraParamList == null||item.extraParamList.size()==0) {
            ViewUnEditHolder unEditHolder = new ViewUnEditHolder(helper.getView(), item, position);
            unEditHolder.initView();
        } else {
            ViewHolder viewHolder = new ViewHolder(helper.getView(), item, position);
            viewHolder.initView();
        }
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.et_value)
        EditText etValue;
        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.tv_score)
        TextView tvScore;
        private DefectListBean item;

        ViewHolder(View view, DefectListBean item, int position) {
            ButterKnife.bind(this, view);
            this.item = item;
        }

        @SuppressLint("SetTextI18n")
        public void initView() {
            if (item == null) {
                return;
            }
            String name = item.name;
            if (name != null) {
                tvName.setText(name);
            }
            if (item.extraParamList!=null){
                if (item.extraParamList.size()>0){
                    ExtraParamBean paramBean = item.extraParamList.get(0);
                    String value = paramBean.value;
                    if (value != null) {
                        etValue.setText(value);
                    }
                    String unit = paramBean.unit;
                    if (unit != null) {
                        tvUnit.setText(unit);
                    }
                }

            }


            Integer score = item.score;
            if (score != null) {
                tvScore.setText(score + "");
            }
        }
    }

    class ViewUnEditHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_score)
        TextView tvScore;

        private DefectListBean item;

        ViewUnEditHolder(View view, DefectListBean item, int position) {
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
            Integer score = item.score;
            if (score != null) {
                tvScore.setText(score + "");
            }
        }
    }

}

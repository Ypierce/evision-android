package com.hs.adapter.work;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.hs.R;

import com.hs.bean.work.DefectListBan;
import com.hs.listener.DefectInfoShowOrEditListener;
import com.hs.listener.ShowOrEditDefectInfoListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlemishInfoListAdapter extends CommonAdapter<DefectListBan> {

    private ShowOrEditDefectInfoListener showOrEditDefectInfoListener;
    private DefectInfoShowOrEditListener defectInfoShowOrEditListener;

    public BlemishInfoListAdapter(@NonNull Context context, List<DefectListBan> data) {
        super(context, R.layout.item_blemish_info_one_line, data);
    }

    public void setShowOrEditDefectInfoListener(ShowOrEditDefectInfoListener showOrEditDefectInfoListener) {
        this.showOrEditDefectInfoListener = showOrEditDefectInfoListener;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdate(BaseAdapterHelper baseAdapterHelper, final DefectListBan defectListBan, int i) {
        LinearLayout llBlemishInfoOneLine = baseAdapterHelper.getView(R.id.ll_blemish_info_one_line);
        TextView tvBlemishNum = baseAdapterHelper.getView(R.id.tv_blemish_num);
        TextView tvBlemishName = baseAdapterHelper.getView(R.id.tv_blemish_name);
        TextView tvBlemishWhere = baseAdapterHelper.getView(R.id.tv_blemish_where);
        TextView tvBlemishPoint = baseAdapterHelper.getView(R.id.tv_blemish_point);
        ImageView ivBlemishWork = baseAdapterHelper.getView(R.id.iv_blemish_work);
        tvBlemishNum.setText(defectListBan.defectNum);
        tvBlemishName.setText(defectListBan.defectName);
        tvBlemishWhere.setText(defectListBan.location + "");
        tvBlemishPoint.setText(defectListBan.score + "");
        Integer backgroundFlag = defectListBan.backgroundFlag;

        int color = 0;
        if (backgroundFlag == 0) {
            color = Color.parseColor("#242536");
        } else if (backgroundFlag == 1) {
            color = Color.parseColor("#444553");
        }
        llBlemishInfoOneLine.setBackgroundColor(color);

        ivBlemishWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showOrEditDefectInfoListener == null) {
                    return;
                }
                showOrEditDefectInfoListener.editDefectInfo(defectListBan.id);
                if (defectInfoShowOrEditListener==null){
                    return;
                }
                defectInfoShowOrEditListener.showDefectInfo(defectListBan.id);
            }
        });
        llBlemishInfoOneLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showOrEditDefectInfoListener == null) {
                    return;
                }
                showOrEditDefectInfoListener.showDefectInfo(defectListBan.id, defectListBan.color);
                if (defectInfoShowOrEditListener==null){
                    return;
                }
                defectInfoShowOrEditListener.editDefectInfo(defectListBan.id);
            }
        });
    }

    class ViewHolder {
        @BindView(R.id.ll_blemish_info_one_line)
        LinearLayout llBlemishInfoOneLine;
        @BindView(R.id.tv_blemish_num)
        TextView tvBlemishNum;
        @BindView(R.id.tv_blemish_name)
        TextView tvBlemishName;
        @BindView(R.id.tv_blemish_where)
        TextView tvBlemishWhere;
        @BindView(R.id.tv_blemish_point)
        TextView tvBlemishPoint;
        @BindView(R.id.iv_blemish_work)
        ImageView ivBlemishWork;

        private int itemPosition = 0;
        private DefectListBan blemishInfoBean;
        private View view;

        ViewHolder(View view, DefectListBan item, int position) {
            ButterKnife.bind(this, view);
            itemPosition = position;
            this.blemishInfoBean = item;
            this.view = view;
        }

        @SuppressLint("SetTextI18n")
        public void initView() {

        }
    }
}

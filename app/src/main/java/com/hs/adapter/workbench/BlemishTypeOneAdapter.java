package com.hs.adapter.workbench;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.hs.R;
import com.hs.activity.BaseActivity;
import com.hs.bean.maintenance.DefectInfoListBean.DefectListBean;
import com.hs.listener.DefectDetailChildClickListener;

import java.util.List;

public class BlemishTypeOneAdapter extends CommonRecyclerAdapter<DefectListBean> {

    protected BaseActivity targetContext = null;
    private String color;

    private DefectDetailChildClickListener defectDetailChildClickListener;

    public void setColor(String color) {
        this.color = color;
    }

    public void setDefectDetailChildClickListener(DefectDetailChildClickListener defectDetailChildClickListener) {
        this.defectDetailChildClickListener = defectDetailChildClickListener;
    }

    public BlemishTypeOneAdapter(@NonNull BaseActivity context, List<DefectListBean> list, String colorString) {
        super(context, R.layout.item_add_blemish_info_type, list);
        targetContext = context;
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, final DefectListBean item, int position) {
        final TextView typeTxt = (TextView) helper.getView().findViewById(R.id.tv_blemish_type_rectangle);
        typeTxt.setText(item.name);
        final Integer selectFlag = item.selectFlag;

        String mColor = null;
        if (selectFlag == 0) {
            mColor = color;
        } else if (selectFlag == 1) {
            mColor = getStringColor(color);
        }
        item.color = mColor;
        typeTxt.setBackgroundColor(Color.parseColor(mColor));
        typeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mFlagAddOrRemove = 0;
                String color = null;
                if (item.selectFlag == 0) {
                    item.selectFlag = 1;
                    mFlagAddOrRemove = 0;
                    color = getStringColor(item.color);
                } else if (item.selectFlag == 1) {
                    item.selectFlag = 0;
                    mFlagAddOrRemove = 1;
                    color = item.color;
                }
                typeTxt.setBackgroundColor(Color.parseColor(color));
                if (defectDetailChildClickListener == null) {
                    return;
                }
                defectDetailChildClickListener.defectChildClick(item, mFlagAddOrRemove);
            }
        });
    }

    private String getStringColor(String color) {
        String needColor = "";
        color = color.replace("#", "");
        needColor = "#4D" + color;
        return needColor;
    }
}

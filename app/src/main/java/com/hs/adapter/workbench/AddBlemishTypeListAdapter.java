package com.hs.adapter.workbench;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.hs.R;
import com.hs.base.Viewable;
import com.hs.bean.maintenance.DefectInfoListBean;
import com.hs.bean.maintenance.DefectInfoListBean.DefectListBean;
import com.hs.bean.work.ClothInfoBean;
import com.hs.listener.DefectDetailChildClickListener;
import com.hs.listener.DefectDetailParentClickListener;

import java.util.ArrayList;
import java.util.List;


public class AddBlemishTypeListAdapter extends CommonAdapter<DefectInfoListBean> {

    private Viewable targetContext = null;
    private DefectDetailParentClickListener parentClickListener;
    private ClothInfoBean clothInfoBean;

    public AddBlemishTypeListAdapter(@NonNull Context context, List<DefectInfoListBean> data) {
        super(context, R.layout.item_blemish_list_group, data);
        targetContext = (Viewable) context;
    }

    public void setClothInfoBean(ClothInfoBean clothInfoBean) {
        this.clothInfoBean = clothInfoBean;
    }

    public void setParentClickListener(DefectDetailParentClickListener parentClickListener) {
        this.parentClickListener = parentClickListener;
    }

    @Override
    public void onUpdate(BaseAdapterHelper helper, final DefectInfoListBean item, int position) {
        RecyclerView recyclerView = (RecyclerView) helper.getView().findViewById(R.id.rv_blemish_type_group);

        GridLayoutManager mManager = new GridLayoutManager(targetContext.getTargetActivity(), 5);
        recyclerView.setLayoutManager(mManager);

        String colorString = item.color;
        List<DefectListBean> beanList = new ArrayList<>();
        BlemishTypeOneAdapter blemishTypeOneAdapter = new BlemishTypeOneAdapter(targetContext.getTargetActivity(), beanList, colorString);
        blemishTypeOneAdapter.setColor(colorString);
        blemishTypeOneAdapter.setDefectDetailChildClickListener(new DefectDetailChildClickListener() {
            @Override
            public void defectChildClick(DefectListBean mBean, int mFlagAddOrRemove) {
                if (parentClickListener == null) {
                    return;
                }
                parentClickListener.defectParentClick(item, mBean, mFlagAddOrRemove);
            }
        });
        recyclerView.setAdapter(blemishTypeOneAdapter);

        beanList = item.defectList;
        blemishTypeOneAdapter.replaceAll(beanList);
    }

}

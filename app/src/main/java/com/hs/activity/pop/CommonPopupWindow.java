package com.hs.activity.pop;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hs.R;
import com.hs.adapter.maintenance.CommonNameValueAdapter;
import com.hs.base.Viewable;
import com.hs.bean.maintenance.CommonIdNameBean;
import com.hs.bean.maintenance.VarietyListBean;
import com.hs.service.MaintenanceService;
import com.hs.service.listener.CommonResultListener;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * @Author: ljf
 * 时间:2019/6/10
 * 简述:<功能简述>
 */
public class CommonPopupWindow extends BasePopupWindow implements CommonNameValueAdapter.CommonIdNameListener
        , BasePopupWindow.OnBeforeShowCallback {
    private View rootView;
    private Viewable targetContext;
    private View mLocationView;
    private CommonNameValueAdapter nameValueAdapter;
    private List<CommonIdNameBean> list;
    private CommonNameValueAdapter.CommonIdNameListener commonIdNameListener;

    public void setCommonIdNameListener(CommonNameValueAdapter.CommonIdNameListener commonIdNameListener) {
        this.commonIdNameListener = commonIdNameListener;
    }

    @Override
    public boolean onBeforeShow(View view, View view1, boolean b) {
        return false;
    }

    public CommonPopupWindow(Context context, int width, int height, boolean delayInit
            , View mLocationView, List<CommonIdNameBean> list) {
        super(context, width, height, delayInit);
        targetContext = (Viewable) context;
        this.mLocationView = mLocationView;
        this.list = list;
        initView();
        initData();
        initListener();
    }

    @Override
    public View onCreateContentView() {
        rootView = createPopupById(R.layout.pop_common_list);
        return rootView;

    }

    private void initView() {
        RecyclerView rvValue = (RecyclerView) rootView.findViewById(R.id.rv_value);
        LinearLayoutManager mManager = new LinearLayoutManager(targetContext.getTargetActivity());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvValue.setLayoutManager(mManager);
        nameValueAdapter = new CommonNameValueAdapter(targetContext.getTargetActivity(), list);
        rvValue.setAdapter(nameValueAdapter);
    }

    private void initData() {
        nameValueAdapter.replaceAll(list);
        showPopupWindow(mLocationView);
    }


    private void initListener() {
        nameValueAdapter.setIdNameListener(this);
    }


    @Override
    public void commonIdName(CommonIdNameBean idNameBean) {
        if (commonIdNameListener == null) {
            return;
        }
        commonIdNameListener.commonIdName(idNameBean);
    }


}

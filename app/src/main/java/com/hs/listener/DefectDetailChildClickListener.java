package com.hs.listener;

import com.hs.bean.maintenance.DefectInfoListBean;
import com.hs.bean.maintenance.DefectInfoListBean.DefectListBean;

/**
 * @Author: ljf
 * 时间:2019/6/21
 * 简述:新增疵点,疵点明细点击
 */
public interface DefectDetailChildClickListener {
    void defectChildClick(DefectListBean mBean,int mFlagAddOrRemove);
}

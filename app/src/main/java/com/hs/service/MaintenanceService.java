package com.hs.service;

import com.google.gson.reflect.TypeToken;
import com.hs.base.Viewable;
import com.hs.bean.maintenance.CommonIdNameBean;
import com.hs.bean.maintenance.DefectInfoListBean;
import com.hs.bean.maintenance.VarietyListBean;
import com.hs.bean.work.DefectListBan;
import com.hs.service.listener.ResultListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:维护相关
 */
public class MaintenanceService extends BaseService {
    private static final String URL_LIST_VARIETY = "variety/list";
    private static final String URL_LIST_SHIFT = "shift/list";
    private static final String URL_LIST_CLOTH_PLY = "cloth/ply/list";
    private static final String URL_LIST_WARP_SUPPLY = "warp/supply/list";
    private static final String URL_LIST_WEFT_SUPPLY = "weft/supply/list";
    private static final String URL_LIST_DEFECT = "defect/list";

    public MaintenanceService(Viewable context) {
        super(context);
    }

    /**
     * 6.1 品种编号
     *
     * @param queryKey
     * @param resultListener
     */
    public void getVarietyList(String queryKey, ResultListener<List<VarietyListBean>> resultListener) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("queryKey",queryKey);
        addQueryKeyAfterToken(URL_LIST_VARIETY, paramMap, createBeanListener(resultListener, new TypeToken<List<VarietyListBean>>() {
        }));
    }

    /**
     * 6.2 班次
     *
     * @param resultListener
     */
    public void getShiftList(ResultListener<List<CommonIdNameBean>> resultListener) {
        get(URL_LIST_SHIFT, null, createBeanListener(resultListener, new TypeToken<List<CommonIdNameBean>>() {
        }));
    }

    /**
     * 6.3 布匹厚度列表
     *
     * @param resultListener
     */
    public void getClothPlyList(ResultListener<List<CommonIdNameBean>> resultListener) {
        get(URL_LIST_CLOTH_PLY, null, createBeanListener(resultListener, new TypeToken<List<CommonIdNameBean>>() {
        }));
    }

    /**
     * 6.4 经纱供应商列表
     *
     * @param queryKey
     * @param resultListener
     */
    public void getWarpSupplyList(String queryKey, ResultListener<List<CommonIdNameBean>> resultListener) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("queryKey",queryKey);
        addQueryKeyAfterToken(URL_LIST_WARP_SUPPLY, paramMap, createBeanListener(resultListener, new TypeToken<List<CommonIdNameBean>>() {
        }));
    }

    /**
     * 6.5 纬纱供应商列表
     *
     * @param queryKey
     * @param resultListener
     */
    public void getWeftSupplyList(String queryKey, ResultListener<List<CommonIdNameBean>> resultListener) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("queryKey",queryKey);
        addQueryKeyAfterToken(URL_LIST_WEFT_SUPPLY, paramMap, createBeanListener(resultListener, new TypeToken<List<CommonIdNameBean>>() {
        }));
    }

    /**
     * 6.6 疵点列表po9i
     *
     * @param resultListener
     */
    public void getDefectList(ResultListener<List<DefectInfoListBean>> resultListener) {
        get(URL_LIST_DEFECT, null, createBeanListener(resultListener, new TypeToken<List<DefectInfoListBean>>() {
        }));
    }

}

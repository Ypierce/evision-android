package com.hs.service;

import com.google.gson.reflect.TypeToken;
import com.hs.base.Viewable;
import com.hs.bean.maintenance.DefectParamBean;
import com.hs.bean.work.ClothIdBean;
import com.hs.bean.work.ClothInfoBean;
import com.hs.bean.work.DefectInfoBean;
import com.hs.bean.work.DefectListBan;
import com.hs.service.listener.ResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:验布工作台相关
 */
public class WorkService extends BaseService {
    private static final String URL_UPDATE_CLOTH = "cloth/update";
    private static final String URL_ADD_CLOTH = "cloth/add";
    private static final String URL_CLOTH_DETAIL = "cloth/detail";

    private static final String URL_ADD_CLOTH_DEFECT = "cloth/defect/add";
    private static final String URL_ADD_CLOTH_DEFECT_LIST = "cloth/defect/more/add";
    private static final String URL_DEFECT_RECORD = "cloth/defect/record/add";
    private static final String URL_CLOTH_DEFECT_LIST = "cloth/defect/list";
    private static final String URL_DEFECT_DETAIL = "cloth/defect/detail";
    private static final String URL_DEFECT_UPDATE = "cloth/defect/update";


    public WorkService(Viewable context) {
        super(context);
    }

    /**
     * @param id             布匹id
     * @param variety        品种
     * @param warpId         经纱供应商id
     * @param weftId         纬纱供应商id
     * @param machineNo      机台号
     * @param year           年
     * @param month          月
     * @param day            日
     * @param shiftId        班次id
     * @param plyId          厚度id
     * @param resultListener 结果回调
     */
    public void updateClothInfo(int id, String variety, int warpId, int weftId, String machineNo
            , int year, int month, int day, int shiftId, int plyId, ResultListener<JSONObject> resultListener) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("variety", variety);
        paramMap.put("warpId", warpId);
        paramMap.put("weftId", weftId);
        paramMap.put("machineNo", machineNo);
        paramMap.put("year", year);
        paramMap.put("month", month);
        paramMap.put("day", day);
        paramMap.put("shiftId", shiftId);
        paramMap.put("plyId", plyId);
        postJson(URL_UPDATE_CLOTH, paramMap, createBeanListener(resultListener, new TypeToken<JSONObject>() {
        }));
    }

    /**
     * @param variety        品种
     * @param warpId         经纱供应商id
     * @param weftId         纬纱供应商id
     * @param machineNo      机台号
     * @param year           年
     * @param month          月
     * @param day            日
     * @param shiftId        班次id
     * @param plyId          厚度id
     * @param resultListener 结果回调
     */
    public void addClothInfo(String variety, int warpId, int weftId, String machineNo
            , int year, int month, int day, int shiftId, int plyId, ResultListener<ClothIdBean> resultListener) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("variety", variety);
        paramMap.put("warpId", warpId);
        paramMap.put("weftId", weftId);
        paramMap.put("machineNo", machineNo);
        paramMap.put("year", year);
        paramMap.put("month", month);
        paramMap.put("day", day);
        paramMap.put("shiftId", shiftId);
        paramMap.put("plyId", plyId);
        postJson(URL_ADD_CLOTH, paramMap, createBeanListener(resultListener, new TypeToken<ClothIdBean>() {
        }));
    }

    /**
     * 获取布匹信息(暂时用不到)
     *
     * @param id             布匹Id
     * @param resultListener 结果回调
     */
    public void getClothInfoById(String id, ResultListener<ClothInfoBean> resultListener) {
        get(URL_CLOTH_DETAIL + "/" + id, null, createBeanListener(resultListener, new TypeToken<ClothInfoBean>() {
        }));
    }

    /**
     * 3.2.1 新增布匹疵点
     *
     * @param clothId
     * @param defectId
     * @param remark
     * @param location
     * @param extraParamList
     * @param resultListener
     */
    public void addNewDefect(Integer clothId, Integer defectId, String remark, Double location
            , List<DefectParamBean.ExtraParamListBean> extraParamList, ResultListener<JSONObject> resultListener) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("clothId", clothId);
        paramMap.put("defectId", defectId);
        paramMap.put("remark", remark);
        paramMap.put("location", location);
        paramMap.put("extraParamList", extraParamList);
        postJsonObject(URL_ADD_CLOTH_DEFECT, paramMap, createBeanListener(resultListener, new TypeToken<JSONObject>() {
        }));
    }

    public void addNewDefect(List<Object> paramBeanList, ResultListener<JSONObject> resultListener) {
        postJsonArray(URL_ADD_CLOTH_DEFECT_LIST, paramBeanList, createBeanListener(resultListener, new TypeToken<JSONObject>() {
        }));
    }

    /**
     * 机器疵点和人工疵点记录接口
     *
     * @param machineNo
     * @param defectList
     * @param resultListener
     */
    public void addDefectRecord(List<Integer> machineNo, List<Integer> defectList, ResultListener<JSONObject> resultListener) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("machineNo", machineNo);
        paramMap.put("defectList", defectList);
        postJsonObject(URL_DEFECT_RECORD, paramMap, createBeanListener(resultListener, new TypeToken<JSONObject>() {
        }));
    }

    /**
     * 3.2.2 布匹疵点列表
     *
     * @param clothId
     * @param resultListener
     */
    public void getDefectList(int clothId, ResultListener<List<DefectListBan>> resultListener) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("clothId", String.valueOf(clothId));
        addQueryKeyAfterToken(URL_CLOTH_DEFECT_LIST, paramMap, createBeanListener(resultListener, new TypeToken<List<DefectListBan>>() {
        }));
    }

    /**
     * 3.2.3 疵点明细详情
     *
     * @param id
     * @param resultListener
     */
    public void getDefectDetailById(int id, ResultListener<DefectInfoBean> resultListener) {
        get(URL_DEFECT_DETAIL + "/" + id, null, createBeanListener(resultListener, new TypeToken<DefectInfoBean>() {
        }));
    }

    /**
     * 3.2.4 布匹疵点编辑
     *
     * @param id
     * @param defectId
     * @param remark
     * @param location
     * @param extraParamList
     * @param resultListener
     */
    public void updateDefect(int id, int defectId, String remark, double location
            , List<DefectParamBean.ExtraParamListBean> extraParamList, ResultListener<JSONObject> resultListener) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("defectId", defectId);
        paramMap.put("remark", remark);
        paramMap.put("location", location);
        paramMap.put("extraParamList", extraParamList);
        postJsonObject(URL_DEFECT_UPDATE, paramMap, createBeanListener(resultListener, new TypeToken<JSONObject>() {
        }));
    }
}

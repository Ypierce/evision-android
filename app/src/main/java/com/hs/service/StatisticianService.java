package com.hs.service;

import com.google.gson.reflect.TypeToken;
import com.hs.base.Viewable;
import com.hs.bean.login.LoginInfoBean;
import com.hs.bean.statistician.StatisticsDataBean;
import com.hs.bean.statistician.StatisticsHeadBean;
import com.hs.bean.work.ClothInfoBean;
import com.hs.service.listener.ResultListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:统计员相关
 */
public class StatisticianService extends BaseService {
    private static final String URL_STATISTICIAN_HEAD = "cloth/statistic/head/list";
    private static final String URL_STATISTICIAN_DATA = "cloth/statistic/data/list";

    public StatisticianService(Viewable context) {
        super(context);
    }

    /**
     * 统计列表表头
     *
     * @param resultListener 结果回调
     */
    public void getStatisticianHead(ResultListener<StatisticsHeadBean> resultListener) {
        get(URL_STATISTICIAN_HEAD, null, createBeanListener(resultListener, new TypeToken<StatisticsHeadBean>() {
        }));
    }

    /**
     * @param machineNo      机号
     * @param date           日期， 年月日，“2019-12-12”格式，默认当天
     * @param variety        品种名称
     * @param resultListener 结果回调
     */
    public void getStatisticianData(String machineNo, String date, String variety, ResultListener<List<StatisticsDataBean>> resultListener) {
        Map<String, Object> paramMap = new HashMap<>();
        if (!"".equals(machineNo) && machineNo != null) {
            paramMap.put("machineNo", machineNo);
        }
        paramMap.put("date", date);
        if (!"".equals(variety) && variety != null) {
            paramMap.put("variety", variety);
        }
        postJson(URL_STATISTICIAN_DATA, paramMap, createBeanListener(resultListener, new TypeToken<List<StatisticsDataBean>>() {
        }));
    }
}

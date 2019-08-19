package com.hs.service;

import com.google.gson.reflect.TypeToken;
import com.hs.base.Viewable;
import com.hs.bean.statistics.PerchingCensusBean;
import com.hs.bean.work.ClothInfoBean;
import com.hs.service.listener.ResultListener;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:验布统计相关
 */
public class StatisticsService extends BaseService {
    private static final String URL_PERCHING_TODAY = "cloth/inspect/today";
    private static final String URL_PERCHING_YESTERDAY = "cloth/inspect/yesterday";
    private static final String URL_PERCHING_MONTH = "cloth/inspect/month";

    public StatisticsService(Viewable context) {
        super(context);
    }

    /**
     * 4.1 今日验布
     *
     * @param resultListener
     */
    public void getTodayPerching(ResultListener<PerchingCensusBean> resultListener) {
        get(URL_PERCHING_TODAY, null, createBeanListener(resultListener, new TypeToken<PerchingCensusBean>() {
        }));
    }

    /**
     * 4.2 昨日验布
     *
     * @param resultListener
     */
    public void getYesterdayPerching(ResultListener<PerchingCensusBean> resultListener) {
        get(URL_PERCHING_YESTERDAY, null, createBeanListener(resultListener, new TypeToken<PerchingCensusBean>() {
        }));
    }

    /**
     * 4.3 当月验布
     *
     * @param resultListener
     */
    public void getCurrentMonthPerching(ResultListener<PerchingCensusBean> resultListener) {
        get(URL_PERCHING_MONTH, null, createBeanListener(resultListener, new TypeToken<PerchingCensusBean>() {
        }));
    }
}

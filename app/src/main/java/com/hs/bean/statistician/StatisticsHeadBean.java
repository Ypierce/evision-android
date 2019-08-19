package com.hs.bean.statistician;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:<功能简述>
 */
public class StatisticsHeadBean implements Serializable {

    public List<String> headList;

    public static StatisticsHeadBean getHeadBean() {
        StatisticsHeadBean dataBean = new StatisticsHeadBean();
        dataBean.headList = getDataList();
        return dataBean;
    }

    private static List<String> getDataList() {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            String s = "";
            switch (i) {
                case 0:
                    s = "机号";
                    break;
                case 1:
                    s = "毛边";
                    break;
                case 2:
                    s = "双纬";
                    break;
                case 3:
                    s = "错纬";
                    break;
                case 4:
                    s = "纬缩";
                    break;
                case 5:
                    s = "百脚";
                    break;
                case 6:
                    s = "希弄";
                    break;
                case 7:
                    s = "经缩";
                    break;
                case 8:
                    s = "破洞";
                    break;
                case 9:
                    s = "破边";
                    break;
                case 10:
                    s = "油渍";
                    break;
                case 11:
                    s = "密路";
                    break;
                case 12:
                    s = "毛边";
                    break;
                case 13:
                    s = "断经";
                    break;
                case 14:
                    s = "双经";
                    break;
                case 15:
                    s = "穿错";
                    break;
                case 16:
                    s = "批写号错";
                    break;
                case 17:
                    s = "次布数量";
                    break;
            }
            stringList.add(s);
        }
        Logger.d(stringList);
        return stringList;
    }
}

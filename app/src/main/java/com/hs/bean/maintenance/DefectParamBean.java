package com.hs.bean.maintenance;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ljf
 * 时间:2019/6/11
 * 简述:<功能简述>
 */
public class DefectParamBean implements Serializable {

    /**
     * clothId : 1
     * defectId : 13
     * remark : 断经了123
     * location : 10.2
     * extraParamList : [{"id":2,"value":"2"}]
     */

    public Integer clothId;
    public Integer defectId;
    public String remark;
    public Integer score;
    public String name;
    public String color;
    public double location;
    public List<ExtraParamListBean> extraParamList;

    public static class ExtraParamListBean implements Serializable{
        /**
         * id : 2
         * value : 2
         */

        public int id;
        public String value;

    }
}

package com.hs.bean.work;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:<功能简述>
 */
public class DefectInfoBean implements Serializable {

    /**
     * id : 2
     * defectId : 13
     * defectName : 断经
     * location : 10.2
     * score : 5
     * remark : 断经了123
     * extraParamList : [{"id":1,"name":"长度","value":"2","unit":"米","editFlag":1}]
     */

    public int id;
    public Integer defectId;
    public String defectName;
    public double location;
    public int score;
    public String remark;
    public List<ExtraParamListBean> extraParamList;

    public static class ExtraParamListBean implements Serializable{
        /**
         * id : 1
         * name : 长度
         * value : 2
         * unit : 米
         * editFlag : 1
         */

        public int id;
        public String name;
        public String value;
        public String unit;
        public int editFlag;
    }
}

package com.hs.bean.maintenance;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:6.6 疵点列表
 */
public class DefectInfoListBean implements Serializable {


    /**
     * categoryId : 1
     * categoryName : 分类1
     * color : #00ffff
     * selectColor : #00ffff
     * defectList : [{"id":1,"name":"毛边","score":5,"extraParamList":[]},{"id":2,"name":"双纬","score":5,"extraParamList":[]},{"id":3,"name":"错纬","score":5,"extraParamList":[]},{"id":4,"name":"纬缩","score":5,"extraParamList":[]},{"id":5,"name":"百脚","score":5,"extraParamList":[]},{"id":6,"name":"希弄","score":5,"extraParamList":[]},{"id":7,"name":"经缩","score":5,"extraParamList":[]}]
     */

    public Integer categoryId;
    public String categoryName;
    public String color;
    public String selectColor;
    public List<DefectListBean> defectList;


    public static class DefectListBean implements Serializable {
        /**
         * id : 1
         * name : 毛边
         * score : 5
         * extraParamList : []
         */

        public Integer id;
        public String name;
        public Integer score;
        public String color;
        public int selectFlag;
        public List<ExtraParamBean> extraParamList;

        public static class ExtraParamBean implements Serializable {

            /**
             * id : 1
             * name : 长度
             * value :
             * unit : 米
             * editFlag : 1
             */

            public int id;
            public String name;
            public String value;
            public String color;
            public String unit;
            public int editFlag;
        }

    }

}

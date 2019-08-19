package com.hs.bean.statistics;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:验布统计相关
 */
public class PerchingCensusBean implements Serializable {

    /**
     * totalLength : 0
     * categoryList : ["分类4","分类2"]
     * categoryDataList : [2,1]
     * list : [{"name":"分类4","count":2},{"name":"分类2","count":1}]
     */

    public int totalLength;
    public List<String> categoryList;
    public List<Integer> categoryDataList;
    public List<ListBean> list;

    public static class ListBean implements Serializable{
        /**
         * name : 分类4
         * count : 2
         */

        public String name;
        public int count;
    }
}

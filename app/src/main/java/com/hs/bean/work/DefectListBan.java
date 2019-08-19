package com.hs.bean.work;

import java.io.Serializable;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:布匹疵点列表接口
 */
public class DefectListBan implements Serializable {

    /**
     * id : 1
     * defectName : 破洞
     * location : 5.3
     * score : 5
     */

    public Integer id;
    public String defectNum;
    public String defectName;
    public double location;
    public int score;
    public int backgroundFlag;
    public String color;


    public DefectListBan() {
    }
}

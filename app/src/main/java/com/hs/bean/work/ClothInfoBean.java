package com.hs.bean.work;

import java.io.Serializable;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:<功能简述>
 */
public class ClothInfoBean implements Serializable {
    /**
     * id : 3
     * variety : 品种1
     * warpId : 1
     * weftId : 1
     * machineNo : ybj001
     * year : 2019
     * month : 5
     * day : 31
     * shiftId : 1
     * plyId : 1
     * length : 0
     * defectNum : 0
     * score : 0
     */

    public Integer id;
    public String variety;
    public int warpId;
    public String strWarp;
    public int weftId;
    public String strWeft;
    public String machineNo;
    public int year;
    public int month;
    public int day;
    public int shiftId;
    public String strShift;
    public int plyId;
    public String strPly;
    public int length;
    public int clothDefectId;
}

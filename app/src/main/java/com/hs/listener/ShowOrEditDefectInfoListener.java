package com.hs.listener;

/**
 * @Author: ljf
 * 时间:2019/6/6
 * 简述:开始或者停止验布监听器
 */
public interface ShowOrEditDefectInfoListener {
    void showDefectInfo(Integer id,String color);
    void editDefectInfo(Integer id);
}

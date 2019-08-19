package com.hs.common.util;

import java.util.Map;

/**
 * @Author: ljf
 * 时间:2019/6/8
 * 简述:<功能简述>
 */
public class MapUtils {
    public static String getValueByKey(String key, Map<String, String> map) {
        String resultValue = "";
        for (Map.Entry<String, String> str : map.entrySet()) {
            String strKey = str.getKey();
            if (strKey.equals(key)){
                resultValue = str.getValue();
            }

        }
        return resultValue;
    }
}

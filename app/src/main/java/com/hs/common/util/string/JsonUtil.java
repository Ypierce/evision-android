package com.hs.common.util.string;

import com.google.gson.Gson;

/**
 * Created by jilil on 2018/6/30.
 */

public class JsonUtil {

    public static String object2JsonString(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object string2Object(String jsonData, Class clazz){
        Gson gs = new Gson();
        try {
            Object value = gs.fromJson(jsonData, clazz);
            return value;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}

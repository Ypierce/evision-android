package com.hs.service;

import com.google.gson.reflect.TypeToken;
import com.hs.base.Viewable;
import com.hs.bean.login.LoginInfoBean;
import com.hs.service.listener.ResultListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ljf
 * 时间:2019/6/4
 * 简述:登录相关
 */
public class LoginService extends BaseService {
    private static final String URL_LOGIN = "login";

    public LoginService(Viewable context) {
        super(context);
    }

    /**
     * @param jobNumber                  工号
     * @param password                   密码
     * @param clothInspectingMachineCode 验布机号，暂定固定值
     * @param resultListener             结果回调
     */
    public void login( String jobNumber, String password, String clothInspectingMachineCode, ResultListener<LoginInfoBean> resultListener) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("jobNumber", jobNumber);
        paramMap.put("password", password);
        paramMap.put("clothInspectingMachineCode", clothInspectingMachineCode);
        postJson(URL_LOGIN, paramMap, createBeanListener(resultListener, new TypeToken<LoginInfoBean>() {
        }));
    }
}

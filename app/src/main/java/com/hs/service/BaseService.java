package com.hs.service;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hs.activity.LoginActivity;
import com.hs.base.GlobalInfo;
import com.hs.base.MyActivityManager;
import com.hs.base.Viewable;
import com.hs.common.constant.AppConfig;
import com.hs.common.constant.SharedKeyConstant;
import com.hs.common.util.MySharedPreference;
import com.hs.common.util.Tools;
import com.hs.common.util.string.JsonUtil;
import com.hs.service.listener.BaseResponseListener;
import com.hs.service.listener.CommonResponseListener;
import com.hs.service.listener.ResponseListener;
import com.hs.service.listener.ResultListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 服务接口的基类
 * <p>
 * Created by jill on 2016/11/22.
 */

public abstract class BaseService {

    private static final String FIELD_CODE = "code";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_MSG = "msg";
    private static final String FIELD_TOKEN = "token";

    private static final String RESULT_SUCCESS = "SUCCESS";
    protected Viewable context;

    public BaseService(Viewable context) {
        this.context = context;
    }

    //get，将参数放到url后面，支持的URL路径：http://www.xx.com/app?id=1&name=2
    protected void get(String url, Map<String, String> params, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (!responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo
                .get(AppConfig.BASE_URL + url)
                .cacheKey(AppConfig.BASE_URL + url)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(params)
                .tag(url)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void addQueryKeyAfterToken(String url,Map<String ,String>  param, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (!responseListener.showLoading())
            context.addLoading();

        Map<String, String> baseParam = assembleParam(param);
        String suffix = "&";
        //== -1不包含
        if (url.indexOf("?") == -1) {
            suffix = "?";
        }
        String urlParam = suffix + map2UrlParamString(baseParam);
        OkGo
                .get(AppConfig.BASE_URL + url+urlParam)
                .cacheKey(AppConfig.BASE_URL + url+urlParam)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params(null)
                .tag(url)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void get(String url, Map<String, String> params, ResponseListener responseListener,
                       boolean useCache) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        params = assembleParam(params);
        if (responseListener.showLoading()) {
            context.addLoading();
        }

        OkGo.get(AppConfig.BASE_URL + url).params(params).tag(url).execute(new
                DefaultJsonObjectCallback(responseListener));
    }

    //post时，将参数放到url后面，支持的URL路径：http://www.xx.com/app?id=1&name=2
    protected void post(String url, Map<String, String> params, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo.post(AppConfig.BASE_URL + url).params(params).tag(url).execute(new
                DefaultJsonObjectCallback(responseListener));
    }

    //post时，将参数放到body，body的内容为json格式，支持的URL路径：http://www.xx.com/app
    protected void postJson(String url, Map<String, Object> params, ResponseListener
            responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (!responseListener.showLoading())
            context.addLoading();

        if (params == null) {
            params = new HashMap<>();
        }

        JSONObject jsonObject = new JSONObject(params);
        Map<String, String> baseParam = assembleParam(null);
        String suffix = "&";
        //== -1不包含
        if (url.indexOf("?") == -1) {
            suffix = "?";
        }
        String urlParam = suffix + map2UrlParamString(baseParam);
        OkGo.post(AppConfig.BASE_URL + url + urlParam).upJson(jsonObject).tag(url).execute(new
                DefaultJsonObjectCallback(responseListener));
    }

    //post时，将参数放到body，body的内容为json格式，支持的URL路径：http://www.xx.com/app
    protected void postJsonObject(String url, Object obj, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (!responseListener.showLoading())
            context.addLoading();

        String jsonString = JsonUtil.object2JsonString(obj);
        Map<String, String> baseParam = assembleParam(null);
        String suffix = "&";
        //== -1不包含
        if (url.indexOf("?") == -1) {
            suffix = "?";
        }
        String urlParam = suffix + map2UrlParamString(baseParam);
        OkGo.post(AppConfig.BASE_URL + url + urlParam).upJson(jsonString).tag(url).execute(new DefaultJsonObjectCallback(responseListener));
    }
    //post时，将参数放到body，body的内容为json格式，支持的URL路径：http://www.xx.com/app
    protected void postJsonArray(String url, List<Object> dataList, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (!responseListener.showLoading())
            context.addLoading();
        JSONArray jsonArray = new JSONArray(dataList);
        Map<String, String> baseParam = assembleParam(null);
        String suffix = "&";
        //== -1不包含
        if (url.indexOf("?") == -1) {
            suffix = "?";
        }
        String urlParam = suffix + map2UrlParamString(baseParam);
        OkGo.post(AppConfig.BASE_URL + url + urlParam).upJson(jsonArray).tag(url).execute(new DefaultJsonObjectCallback(responseListener));
    }
    /**
     * 将Map参数转成 key1=value1&key2=value2格式
     *
     * @param paramMap
     * @return key1=value1&key2=value2的字符串
     */
    private String map2UrlParamString(Map<String, String> paramMap) {
        String urlParam = "";
        for (String key : paramMap.keySet()) {
            urlParam += key + "=" + paramMap.get(key) + "&";
        }
        if (urlParam.endsWith("&")) {
            urlParam = urlParam.substring(0, urlParam.length() - 1);
        }

        return urlParam;
    }

    protected void postArray(String url, Map<String, String> map, Map<String, String[]> params,
                             String key, List<File> files, ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (!responseListener.showLoading())
            context.addLoading();
        map = assembleParam(map);
        params = assembleArrayParam(params);
        OkGo.post(AppConfig.BASE_URL + url)
                .params(map)
                .addFileParams(key, files)
                .tag(url)
                .isMultipart(true)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void postFile(String url, Map<String, String> params, String key, List<File> files,
                            ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo.post(AppConfig.BASE_URL + url)
                .params(params)
                .addFileParams(key, files)
                .tag(url)
                .isMultipart(true)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected void postOneFile(String url, Map<String, String> params, String key, File file,
                               ResponseListener responseListener) {
        boolean needContinue = beforeHttp();
        if (!needContinue)
            return;

        if (responseListener.showLoading())
            context.addLoading();

        params = assembleParam(params);
        OkGo.post(AppConfig.BASE_URL + url)
                .params(params)
                .params(key, file)
                .tag(url)
                .isMultipart(true)
                .execute(new DefaultJsonObjectCallback(responseListener));
    }

    protected Map<String, String[]> assembleArrayParam(Map<String, String[]> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }


    protected Map<String, String> assembleParam(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }

        String userToken = GlobalInfo.userToken;
        if (TextUtils.isEmpty(userToken)) {
            userToken = "\"\"";
        }
        params.put(FIELD_TOKEN, userToken);
        //   params.put("v", GlobalInfo.appVersionName);
        return params;
    }


    protected Map<String, Object> assembleObjectParam(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(FIELD_TOKEN, GlobalInfo.userToken);
        //   params.put("v", GlobalInfo.appVersionName);

        return params;
    }

    private boolean beforeHttp() {
        boolean networkAvailable = Tools.isNetworkAvailable(context.getTargetActivity());
//        Activity currentActivity = MyActivityManager.getInstance().getCurrentActivity();
//        boolean isCurNoNetwork = false;
//        if (currentActivity != null && currentActivity instanceof NoNetworkActivity) {
//            isCurNoNetwork = true;
//        }
//
//        if (!networkAvailable && !isCurNoNetwork) {
//            context.startActivity(NoNetworkActivity.class);
//            GlobalInfo.viewable = context.getTargetActivity();
//        }

        return networkAvailable;
    }

    protected CommonResponseListener createBeanListener(ResultListener resultListener, TypeToken typeToken) {
        return new CommonResponseListener(context, resultListener, typeToken);
    }

    protected BaseResponseListener createStringListener(ResultListener resultListener) {
        return new BaseResponseListener(context, resultListener);
    }

    protected BaseResponseListener createStringListener(ResultListener resultListener, String singleCode) {
        return new BaseResponseListener(context, resultListener, singleCode);
    }

    protected class DefaultJsonObjectCallback extends JSONObjectCallback {

        private ResponseListener responseListener;

        public DefaultJsonObjectCallback(ResponseListener responseListener) {
            this.responseListener = responseListener;
        }

        @Override
        public void onSuccess(JSONObject jsonObject, Call call, Response response) {
            try {
                int code = jsonObject.getInt(FIELD_CODE);
                if (!responseListener.showLoading())
                    context.removeLoading();
                if (200 == code) {
                    responseListener.success(jsonObject);
                } else {
                    String msg = jsonObject.getString(FIELD_MSG);
                    if (responseListener.showToast() && (code == 409 || code == 410)) {

                        OkGo.getInstance().cancelAll();
                        MySharedPreference.remove(SharedKeyConstant.TOKEN, context.getTargetActivity());
                        context.showToast("请先登录");

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                context.startActivity(LoginActivity.class);
                            }
                        };
                        timer.schedule(task, 1000); // 1秒跳转
                        return;
                    }
                    responseListener.fail(jsonObject.getString(FIELD_CODE), msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (responseListener.showToast())
                    context.showToast("服务器数据返回出现异常");
            } catch (JsonSyntaxException e) {
                if (responseListener.showToast())
                    context.showToast("服务器数据返回出现问题");
            }
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            responseListener.error(response, e);
        }
    }


}

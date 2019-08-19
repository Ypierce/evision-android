package com.hs.service.listener;

import com.google.gson.JsonSyntaxException;
import com.hs.base.Viewable;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

/**
 * Created by jill on 2016/11/22.
 */

public class BaseResponseListener implements ResponseListener {

    protected Viewable context;
    public ResultListener resultListener;
    private String singleCode = null;

    public BaseResponseListener(Viewable context, ResultListener resultListener){
        this.context = context;
        this.resultListener = resultListener;
        this.resultListener.setViewable(context);
    }

    public BaseResponseListener(Viewable context, ResultListener resultListener, String singleCode){
        this.context = context;
        this.resultListener = resultListener;
        this.singleCode = singleCode;
        this.resultListener.setViewable(context);
    }

    @Override
    public void success(JSONObject jsonObject) throws JsonSyntaxException, JSONException {
        String value = "";
        if(singleCode != null && singleCode.length() > 0) {
            value = jsonObject.getString(singleCode);
        }
        resultListener.successHandle(value);
    }

    @Override
    public void cacheSuccess(JSONObject jsonObject) throws JsonSyntaxException, JSONException {
        String value = "";
        if(singleCode != null && singleCode.length() > 0) {
            value = jsonObject.getString(singleCode);
        }
        resultListener.successHandle(value);
    }

    @Override
    public void fail(String errCode, String msg) {
        resultListener.failHandle(errCode, msg);
    }

    @Override
    public void error(Response response, Exception e) {
        resultListener.errorHandle(response, e);
    }

    public boolean showToast(){
        if(resultListener instanceof ShowableResultListener){
            return ((ShowableResultListener)resultListener).isShowToast();
        }

        return true;
    }

    public boolean showLoading(){
        if(resultListener instanceof ShowableResultListener){
            return ((ShowableResultListener)resultListener).isShowLoading();
        }
        return true;
    }

    public boolean readCache(){
        return false;
    }
}

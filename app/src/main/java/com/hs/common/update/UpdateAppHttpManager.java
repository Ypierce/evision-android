package com.hs.common.update;

import android.support.annotation.NonNull;

import com.hs.service.JSONObjectCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.vector.update_app.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by jilil on 2018/11/30.
 */

public class UpdateAppHttpManager implements HttpManager{

    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        OkGo.get(url).params(params).tag(url).execute(new JSONObjectCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject, Call call, Response response) {
                String jsonData = null;
                try {
                    jsonData = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callBack.onResponse(jsonData);
            }
        });
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {


    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        OkGo.get(url)
                .execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {

                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        callback.onResponse(file);
                    }

                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        callback.onProgress(progress, totalSize);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        callback.onBefore();
                    }
                });

    }
}

package com.hs.common.update;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hs.R;
import com.hs.activity.BaseActivity;
import com.hs.common.constant.AppConfig;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by jilil on 2018/11/29.
 */

public class VersionUpdateUtil {

    public static final String UPDATE_URL = AppConfig.BASE_URL + "version/detail/1";

    public static void update(final BaseActivity baseActivity){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Map<String, String> params = new HashMap<String, String>();

        int themeColor = baseActivity.getResources().getColor(R.color.theme_color);

        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(baseActivity)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpManager())
                //必须设置，更新地址
                .setUpdateUrl(UPDATE_URL)
                //以下设置，都是可选
                //设置请求方式，默认get
//                .setPost(false)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
//                .setTopPic()
                //为按钮，进度条设置颜色，默认从顶部图片自动识别。
//                .setThemeColor(themeColor)
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            Gson gson = new Gson();
                            TypeToken typeToken = new TypeToken<AppVersionBean>(){};
                            AppVersionBean bean = gson.fromJson(json, typeToken.getType());

                            int currentVersion = AppVersionUtil.versionCode(baseActivity);
                            String isUpdate = bean.version > currentVersion ? "Yes" : "No";
                            boolean isConstraint = bean.isConstraint == 1 ? true : false;

                            String url = bean.fileDownAddress;
                            if(!url.startsWith("http")){
                                url = AppConfig.BASE_IMAGE_URL + url;
                            }

                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(isUpdate)
                                    //（必须）新版本号，
                                    .setNewVersion("新")
                                    //（必须）下载地址
                                    .setApkFileUrl(url)
                                    //（必须）更新内容
                                    .setUpdateLog(bean.updateHint)
                                    //大小，不设置不显示大小，可以不设置
                                    .setTargetSize(bean.size)
                                    //是否强制更新，可以不设置
                                    .setConstraint(isConstraint);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {

                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {

                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    protected void noNewApp(String error) {
                        super.noNewApp(error);
                    }
                });
    }

}

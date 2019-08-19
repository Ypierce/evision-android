package com.hs.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.hs.common.constant.SharedKeyConstant;
import com.hs.common.util.MySharedPreference;
import com.lzy.okgo.OkGo;

public class MyApplication extends Application {
    static MyApplication applicationContext;

    private static Context mContext;

    public synchronized static MyApplication getIntance() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        init();
        registerActivityCallback();
    }

    public static Context getContext() {
        return mContext;
    }

    private void init() {
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String token = MySharedPreference.get(SharedKeyConstant.TOKEN, null, this);

        GlobalInfo.userToken = token;
        //-----------------------------------------------------------------------------------//
        //必须调用初始化
        OkGo.init(this);
    }

    private void registerActivityCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });

    }

}

package com.hs.common.intent;

import android.app.Activity;
import android.content.Intent;

import com.hs.base.MyActivityManager;

/**
 * Created by jilil on 2018/12/17.
 */

public class IntentStartActivityHelper {

    /**
     ** 关闭activity之前的所有activity
    **/
    public static void startActivityClearAll(Activity srcActivity, Class targetActivity){
        MyActivityManager.getInstance().finishAllActivity();

        Intent intent = new Intent(srcActivity, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        srcActivity.startActivity(intent);
    }
}
